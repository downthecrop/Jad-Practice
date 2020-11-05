package ethos.model.multiplayer_session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import ethos.Server;
import ethos.model.items.GameItem;
import ethos.model.multiplayer_session.duel.DuelSession;
import ethos.model.multiplayer_session.trade.TradeSession;
import ethos.model.players.Player;
import ethos.model.players.PlayerSave;

public abstract class MultiplayerSession implements MultiplayerSessionItemDistribution, MultiplayerSessionLog {

	/**
	 * Represents the total number of players allowed in a single session.
	 */
	public static final int PLAYER_LIMIT = 2;

	/**
	 * A list containing just two players. This list will be maintained to ensure that no more than two players ever exist within the trade so that no mischief or glitching is
	 * occuring.
	 */
	protected List<Player> players = new ArrayList<>(2);

	/**
	 * A map of each trader and the items they have offered during the trade.
	 */
	protected Map<Player, List<GameItem>> items = new HashMap<>();

	/**
	 * A map containing a list of game items the player has before the trade starts
	 */
	protected Map<Player, List<GameItem>> presetItems = new HashMap<>();

	/**
	 * The stage this current session is on. See {@link ab.model.trade.MultiplayerSessionStage} for more information on stages.
	 */
	protected MultiplayerSessionStage stage = new MultiplayerSessionStage(MultiplayerSessionStage.REQUEST);

	/**
	 * The type of session
	 */
	protected MultiplayerSessionType type;

	/**
	 * Constructs a new session for two players
	 * 
	 * @param players
	 */
	public MultiplayerSession(List<Player> players, MultiplayerSessionType type) {
		this.players = players;
		this.type = type;
		players.stream().forEach(player -> items.put(player, new ArrayList<GameItem>()));
	}

	public abstract void accept(Player player, Player recipient, int stage);

	public abstract boolean itemAddable(Player player, GameItem item);

	public abstract boolean itemRemovable(Player player, GameItem item);

	public abstract void updateMainComponent();

	public abstract void updateOfferComponents();

	/**
	 * Adds a {@code GameItem} to the list of items that a player has offered
	 * 
	 * @param player The player in the trade offering the item or items
	 * @param item The game item the player is offering
	 */
	public void addItem(Player player, GameItem item) {
		int id = item.getId();
		int amount = item.getAmount();
		boolean listContainsItem = items.get(player).stream().anyMatch(i -> i.getId() == id);
		if (Objects.isNull(player) || Objects.isNull(item)) {
			return;
		}
		if (player.inClanWars() || player.inClanWarsSafe()) {
			System.out.println("[ABUSE] " + player.playerName + ", Attempted to add items into a multiplayer asset while at district!");
			return;
		}
		if (!player.getItems().playerHasItem(id)) {
			return;
		}
		if (!Server.getMultiplayerSessionListener().inAnySession(player)) {
			finish(MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
			return;
		}
		if (Server.getMultiplayerSessionListener().containsSessionInconsistancies(player)) {
			return;
		}
		if (!itemAddable(player, item)) {
			return;
		}
		int inventoryAmount = player.getItems().getItemAmount(id);
		if (amount > inventoryAmount) {
			amount = inventoryAmount;
		}
		if (amount < 0 || id < 0) {
			return;
		}
		if (!player.getItems().playerHasItem(id, amount)) {
			return;
		}
		if (!presetListContains(player, id, amount)) {
			player.sendMessage("You cannot offer an item that you didn't have before the session started.");
			finish(MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
			return;
		}
		if (!item.isStackable() && getItems(player).size() + amount >= 28) {
			amount = 28 - getItems(player).size();
		}
		if (getItems(player).size() >= 28) {
			if (!containsItem(player, id) || containsItem(player, id) && !item.isStackable()) {
				player.sendMessage("You have already offered the maximum possible items.");
				return;
			}
		}
		if (item.isStackable() && listContainsItem) {
			for (GameItem i : items.get(player)) {
				if (i.getId() == id) {
					long total = ((long) i.getAmount() + (long) amount);
					if (total > Integer.MAX_VALUE) {
						items.get(player).remove(i);
						player.getItems().deleteItem2(id, Integer.MAX_VALUE - i.getAmount());
						items.get(player).add(new GameItem(id, Integer.MAX_VALUE));
					} else {
						items.get(player).remove(i);
						items.get(player).add(new GameItem(id, i.getAmount() + amount));
						player.getItems().deleteItem2(id, amount);
					}
					break;
				}
			}
		} else if (item.isStackable() && !listContainsItem) {
			items.get(player).add(new GameItem(id, amount));
			player.getItems().deleteItem2(id, amount);
		} else {
			while (amount-- > 0) {
				items.get(player).add(new GameItem(id, 1));
				player.getItems().deleteItem2(id, 1);
			}
		}
		stage.setAttachment(null);
		updateOfferComponents();
	}

	/**
	 * Removes a {@code GameItem} from the list of items that a player has offered
	 * 
	 * @param player The player in the trade removing the items
	 * @param item The game item the player is removing
	 */
	public void removeItem(Player player, int slot, GameItem item) {
		int id = item.getId();
		int amount = item.getAmount();
		if (Objects.isNull(player) || Objects.isNull(item)) {
			return;
		}
		if (Server.getMultiplayerSessionListener().containsSessionInconsistancies(player)) {
			return;
		}
		if (!itemRemovable(player, item)) {
			return;
		}
		List<GameItem> items = this.items.get(player);
		int freeSlots = player.getItems().freeSlots();
		if (!items.stream().anyMatch(i -> i.getId() == id)) {
			player.sendMessage("Tried to remove item that does not exist in list.");
			return;
		}
		for (GameItem gameItem : items) {
			if (gameItem.getId() == id) {
				if (!item.isStackable()) {
					if (amount > freeSlots) {
						amount = freeSlots;
					}
				}
				break;
			}
		}
		if (amount < 0 || id < 0) {
			return;
		}
		item = new GameItem(id, amount);
		if (item.isStackable()) {
			for (int i = 0; i < items.size(); i++) {
				if (items.get(i).getId() == id) {
					long total = ((long) amount + (long) player.getItems().getItemCount(id));
					if (total > Integer.MAX_VALUE) {
						amount = Integer.MAX_VALUE - player.getItems().getItemCount(id);
					}
					if (amount > items.get(i).getAmount()) {
						amount = items.get(i).getAmount();
					}
					if (!presetListContains(player, id, amount) || inventoryContainsIllegalItem(player, new GameItem(id, amount))) {
						items.remove(i);
						player.sendMessage("You tried to remove an item from the screen that you did not have before");
						player.sendMessage("the session started, the item has been deleted because of this.");
						finish(MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
						return;
					}
					if (!containsItem(player, id, amount)) {
						player.sendMessage("Tried to remove item that does not exist in list.");
						return;
					}
					if (items.get(i).getAmount() - amount > 0) {
						if (player.getItems().addItem(id, amount)) {
							int previousAmount = items.get(i).getAmount();
							items.set(i, new GameItem(id, previousAmount - amount));
						}
					} else {
						if (player.getItems().addItem(id, amount)) {
							items.remove(i);
						}
					}
					break;
				}
			}
		} else {
			if (amount > getItemAmount(player, id)) {
				amount = getItemAmount(player, id);
			}
			if (!presetListContains(player, id, amount) || inventoryContainsIllegalItem(player, item)) {
				this.items.get(player).remove(slot);
				player.sendMessage("You tried to remove an item from the trade screen that you did not have before");
				player.sendMessage("the trade started, the item has been deleted because of this.");
				finish(MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
				return;
			}
			if (!containsItem(player, id, amount)) {
				player.sendMessage("Tried to remove item that does not exist in list.");
				return;
			}
			while (amount-- > 0) {
				if (player.getItems().addItem(id, 1)) {
					for (GameItem i : items) {
						if (i.getId() == id) {
							items.remove(i);
							break;
						}
					}
				} else {
					break;
				}
			}
		}
		stage.setAttachment(null);
		updateOfferComponents();
	}

	public void accept(Player player, int stageId) {
		Player other = getOther(player);
		if (player.inWild()) {
			if (this instanceof TradeSession) {
				finish(MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
			}
			return;
		}
		if (!Server.getMultiplayerSessionListener().inAnySession(player)) {
			finish(MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
			return;
		}
		if (stage.getStage() != stageId) {
			if (this instanceof TradeSession) {
				player.sendMessage("You cannot continue because you have not made it to that stage.");
				finish(MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
			}
			return;
		}
		if (Server.getMultiplayerSessionListener().containsSessionInconsistancies(player)) {
			return;
		}
		if (players.stream().anyMatch(Objects::isNull)) {
			finish(MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
			return;
		}
		Player recipient = getOther(player);
		boolean illegalItems = false;
		for (Player p : players) {
			Iterator<GameItem> itemList = items.get(p).iterator();
			while (itemList.hasNext()) {
				GameItem item = itemList.next();
				if (!presetListContains(p, item.getId(), item.getAmount()) || inventoryContainsIllegalItem(p, item)) {
					itemList.remove();
					illegalItems = true;
				}
			}
		}
		if (illegalItems) {
			finish(MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
			return;
		}
		if (player.getBankPin().requiresUnlock()) {
			player.getBankPin().open(2);
			return;
		}
		if (player.getTutorial().isActive()) {
			player.getTutorial().refresh();
			finish(MultiplayerSessionFinalizeType.DISPOSE_ITEMS);
			return;
		}
		if (other.getTutorial().isActive()) {
			other.getTutorial().refresh();
			finish(MultiplayerSessionFinalizeType.DISPOSE_ITEMS);
			return;
		}
		if (this instanceof TradeSession) {
			if (!player.getMode().isTradingPermitted() || !other.getMode().isTradingPermitted()) {
				finish(MultiplayerSessionFinalizeType.DISPOSE_ITEMS);
				return;
			}
		}
		if (this instanceof DuelSession) {
			if (!player.getMode().isStakingPermitted() || !other.getMode().isStakingPermitted()) {
				finish(MultiplayerSessionFinalizeType.DISPOSE_ITEMS);
				return;
			}
		}
		accept(player, recipient, stageId);
	}

	public void refreshItemContainer(Player player, Player other, int containerId) {
		player.getOutStream().createFrameVarSizeWord(53);
		player.getOutStream().writeWord(containerId);
		int len = items.get(other).size();
		int current = 0;
		player.getOutStream().writeWord(len);
		if (len > 0) {
			for (GameItem item : items.get(other)) {
				if (item.getAmount() > 254) {
					player.getOutStream().writeByte(255);
					player.getOutStream().writeDWord_v2(item.getAmount());
				} else {
					player.getOutStream().writeByte(item.getAmount());
				}
				player.getOutStream().writeWordBigEndianA(item.getId() + 1);
				current++;
			}
		}
		if (current < 28) {
			for (int i = current; i < 28; i++) {
				player.getOutStream().writeByte(1);
				player.getOutStream().writeWordBigEndianA(-1);
			}
		}
		player.getOutStream().endFrameVarSizeWord();
		player.flushOutStream();
	}

	/**
	 * Declines the trade, the premise for receiving items is depicted by the situation
	 * 
	 * @param type the type of declining protocol
	 */
	public void finish(MultiplayerSessionFinalizeType type) {
		if (stage.getStage() == MultiplayerSessionStage.FINALIZE) {
			throw new IllegalStateException("Attempted to finish session after already being finished once before.");
		}
		stage.setStage(MultiplayerSessionStage.FINALIZE);
		for (Player p : players) {
			Iterator<GameItem> itemList = items.get(p).iterator();
			while (itemList.hasNext()) {
				GameItem item = itemList.next();
				if (!presetListContains(p, item.getId(), item.getAmount()) || inventoryContainsIllegalItem(p, item)) {
					itemList.remove();
				}
			}
		}
		//logSession(type); //Logs
		switch (type) {
		case GIVE_ITEMS:
			give();
			break;

		case WITHDRAW_ITEMS:
			withdraw();
			break;

		case DISPOSE_ITEMS:
			dispose();
			break;
		}
		for (Player player : players) {
			if (Objects.isNull(players)) {
				continue;
			}
			if (this.type.equals(MultiplayerSessionType.DUEL)) {
				if (type.equals(MultiplayerSessionFinalizeType.WITHDRAW_ITEMS)) {
					player.getPA().closeAllWindows();
				}
			}
			if (this.type.equals(MultiplayerSessionType.TRADE)) {
				player.setTrading(false);
				player.getPA().closeAllWindows();
			}
			PlayerSave.saveGame(player);
		}
		Server.getMultiplayerSessionListener().remove(this);
	}

	/**
	 * Populates the map of preset items from each of the players inventory.
	 */
	public void populatePresetItems() {
		for (Player player : players) {
			List<GameItem> realItems = new ArrayList<>();
			for (int i = 0; i < player.playerItems.length; i++) {
				int itemId = player.playerItems[i] - 1;
				int amount = player.playerItemsN[i];
				if (itemId > 0 && amount > 0) {
					realItems.add(new GameItem(itemId, amount));
				}
			}
			for (int i = 0; i < player.playerEquipment.length; i++) {
				int itemId = player.playerEquipment[i];
				int amount = player.playerEquipmentN[i];
				if (itemId > 0 && amount > 0) {
					realItems.add(new GameItem(itemId, amount));
				}
			}
			presetItems.put(player, realItems);
		}
	}

	/**
	 * Determines if an item exists in the list of game items for a specific player.
	 * 
	 * @param player the player
	 * @param itemId the item id
	 * @param amount the amount of the item
	 * @return true if the player has the item before the trade
	 */
	protected boolean presetListContains(Player player, int itemId, int amount) {
		if (!new GameItem(itemId, amount).isStackable()) {
			Optional<GameItem> op = presetItems.get(player).stream().filter(i -> i.getId() == itemId).findFirst();
			return op.isPresent();
		}
		Optional<GameItem> op = presetItems.get(player).stream().filter(i -> i.getId() == itemId && i.getAmount() >= amount).findFirst();
		return op.isPresent();
	}

	/**
	 * Determines if the offered item list contains items that the preset list doesn't contain.
	 * 
	 * @param player the player
	 * @return true if the offered items contains an item that the preset doesnt have
	 */
	protected boolean inventoryContainsIllegalItem(Player player, GameItem i) {
		long amount;
		if (!i.isStackable()) {
			amount = presetItems.get(player).stream().filter(item -> item.getId() == i.getId()).count();
		} else {
			Optional<GameItem> op = presetItems.get(player).stream().filter(item -> item.getId() == i.getId()).findAny();
			if (!op.isPresent()) {
				return true;
			}
			amount = op.get().getAmount();
		}
		return player.getItems().getItemAmount(i.getId()) + this.getItemAmount(player, i.getId()) > amount;
	}

	/**
	 * Returns a list of players that are in this session. This list should be modified from outside the class. This is to prevent the creation of any technical game bugs.
	 * 
	 * @return
	 */
	public List<Player> getPlayers() {
		return players;
	}

	/**
	 * Returns the current stage the trade is in, by default it's request.
	 * 
	 * @return the trade stage
	 */
	public MultiplayerSessionStage getStage() {
		return stage;
	}

	/**
	 * Returns the other player in the trade.
	 * 
	 * @param player not the player we're trying to retrieve
	 * @return the player
	 */
	public Player getOther(Player player) {
		return players.stream().filter(p -> !Objects.equals(player, p)).findAny().get();
	}

	/**
	 * Determines if the item list for the specified player has a certain item, and item amount.
	 * 
	 * @param player the player
	 * @param itemId the item we're looking for
	 * @param itemAmount the amount of the item we're looking for
	 * @return true if it contains the item, false if not.
	 */
	protected boolean containsItem(Player player, int itemId, int itemAmount) {
		if (!items.containsKey(player))
			return false;
		GameItem item = new GameItem(itemId, itemAmount);
		if (!item.isStackable()) {
			return items.get(player).stream().filter(i -> i.getId() == item.getId()).count() >= itemAmount;
		}
		for (GameItem i : items.get(player)) {
			if (i.getId() == itemId && i.getAmount() >= itemAmount) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Determines if the item list contains atleast one instance of this item
	 * 
	 * @param player the player
	 * @param itemId the item we're looking for
	 * @return true if it contains the item, false if not.
	 */
	protected boolean containsItem(Player player, int itemId) {
		return containsItem(player, itemId, 1);
	}

	/**
	 * Determines how much of a certain item we have in the session
	 * 
	 * @param player the player
	 * @param itemId the item
	 * @return
	 */
	protected int getItemAmount(Player player, int itemId) {
		int amount = 0;
		for (GameItem item : items.get(player)) {
			if (item.getId() == itemId) {
				amount += item.getAmount();
			}
		}
		return amount;
	}

	/**
	 * Returns a GameItem object if any of the items offered by the player exist in the recipients inventory, and if the combined total amount is greater than the maximum value of
	 * an Integer type.
	 * 
	 * @param player the player with the offered items were checking
	 * @return a GameItem object if the pre-explained conditions are met
	 */
	protected GameItem getOverlappedItem(Player player) {
		Player recipient = getOther(player);
		for (GameItem playerItem : items.get(player)) {
			if (!playerItem.isStackable()) {
				continue;
			}
			if (!recipient.getItems().playerHasItem(playerItem.getId())) {
				continue;
			}
			long amount = ((long) playerItem.getAmount() + (long) recipient.getItems().getItemAmount(playerItem.getId()));
			if (amount > Integer.MAX_VALUE) {
				return new GameItem(playerItem.getId(), recipient.getItems().getItemAmount(playerItem.getId()));
			}
		}
		return null;
	}

	/**
	 * Returns the list of items offered by the player
	 * 
	 * @param player the player
	 * @return a list of items
	 */
	protected List<GameItem> getItems(Player player) {
		if (Objects.isNull(items.get(player)))
			return null;
		return items.get(player);
	}

}
