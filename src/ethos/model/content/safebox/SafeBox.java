package ethos.model.content.safebox;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ethos.Server;
import ethos.model.content.LootingBag.LootingBagItem;
import ethos.model.multiplayer_session.MultiplayerSessionFinalizeType;
import ethos.model.multiplayer_session.MultiplayerSessionStage;
import ethos.model.multiplayer_session.MultiplayerSessionType;
import ethos.model.multiplayer_session.duel.DuelSession;
import ethos.model.players.Player;

public class SafeBox {
		
		public Player player;
		public List<LootingBagItem> items;
		
		public int selectedItem = -1;
		public int selectedSlot = -1;

		public SafeBox(Player player) {
			this.player = player;
			items = new ArrayList<>();
		}

		/**
		 * Checks wether or not a player is allowed to configure the safe box
		 * @return
		 */
		public boolean configurationPermitted() {
			if (player.getBankPin().requiresUnlock()) {
				player.getBankPin().open(2);
				return false;
			}
			if (player.getTutorial().isActive()) {
				player.getTutorial().refresh();
				return false;
			}
			DuelSession duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(player,
					MultiplayerSessionType.DUEL);
			if (Objects.nonNull(duelSession) && duelSession.getStage().getStage() > MultiplayerSessionStage.REQUEST
					&& duelSession.getStage().getStage() < MultiplayerSessionStage.FURTHER_INTERATION) {
				player.sendMessage("Your actions have declined the duel.");
				duelSession.getOther(player).sendMessage("The challenger has declined the duel.");
				duelSession.finish(MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
				return false;
			}
			if (Server.getMultiplayerSessionListener().inSession(player, MultiplayerSessionType.TRADE)) {
				player.sendMessage("You must decline the trade to start walking.");
				return false;
			}
			if (player.isStuck) {
				player.isStuck = false;
				player.sendMessage("@red@You've disrupted stuck command, you will no longer be moved home.");
				return false;
			}
			return true;
		}

		public boolean handleButton(int buttonId) {
			if (buttonId == 136186) {
				closeLootbag();
				return true;
			}
			return false;
		}

		/**
		 * Opens the check looting bag interface
		 */
		public void openSafeBox() {
			if (!configurationPermitted()) {
				player.sendMessage("You cannot do this right now.");
				return;
			}
			onClose();
			sendItems();
			player.getPA().sendFrame171(1, 35010);
			player.getPA().sendFrame126("" + (items.size()) + "/"+ player.safeBoxSlots, 35005);
			if (player.getOutStream() != null && player != null) {
				player.getItems().resetItems(5064);
				player.getItems().resetTempItems();
				player.getOutStream().createFrame(248);
				player.getOutStream().writeWordA(35000);// ok perfect
				player.getOutStream().writeWord(5063);
				player.flushOutStream();
			}
			player.inSafeBox = true;
		}
		
		public void removeMultipleItemsFromBag(int id, int amount) {
			if (amount >= Integer.MAX_VALUE) {
				amount = countItems(id);
			}
			if (!player.inClanWarsSafe() && id != 13307) {
				player.sendMessage("You are only able to withdraw blood money from here.");
				return;
			}
			int count = 0;
			while (containsItem(id)) {
				if (!withdraw(id, amount)) {
					return;
				}
				if (player.getItems().isStackable(id)) {
					count += amount;
				} else {
					count++;
				}
				if (count >= amount) {
					return;
				}
			}
		}
		
		public boolean containsItem(int id) {
			for (LootingBagItem item : items) {
				if (item.getId() == id) {
					return true;
				}
			}
			return false;
		}

		public int findIndexInLootBag(int id) {
			for (LootingBagItem item : items) {
				if (item.getId() == id) {
					return items.indexOf(item);
				}
			}
			return -1;
		}

		/**
		 * Handles withdrawal from the lootingbag
		 * @param id		The id of the item being withdrawn
		 * @param amount	The amount of the item being withdrawn
		 */
		public boolean withdraw(int id, int amount) {
			int index = findIndexInLootBag(id);
			int amountToAdd = 0;
			if (!player.inClanWarsSafe() && id != 13307) {
				player.sendMessage("You are only able to withdraw blood money from here.");
				return false;
			}
			if (items.size() <= 0) {
				return false;
			}
			if (index == -1) {
				return false;
			}
			LootingBagItem item = items.get(index);
			if (item == null) {
				return false;
			}
			if (item == null || item.getId() <= 0 || item.getAmount() <= 0 || player.getItems().freeSlots() <= 0) {
				return false;
			}
			if (!configurationPermitted()) {
				player.sendMessage("You cannot do this right now.");
				return false;
			}
			if (player.getItems().getItemCount(id, false) + amount >= Integer.MAX_VALUE || player.getItems().getItemCount(id, false) + amount <= 0) {
				return false;
			}
			if ((items.get(items.indexOf(item)).getAmount()) > amount) {
				amountToAdd = amount;
				items.get(items.indexOf(item)).incrementAmount(-amount);
			} else {
				amountToAdd = item.getAmount();
				items.remove(index);
			}
			player.getPA().sendFrame126("" + (items.size()) + "/"+ player.safeBoxSlots, 35005);
			player.getItems().addItem(item.getId(), amountToAdd);
			sendItems();
			sendInventoryItems();
			return true;
		}

		/**
		 * Handles depositing of items into the looting bag
		 * @param id		The id of the item being deposited
		 * @param amount	The amount of the item being deposited
		 */
		public void deposit(int id, int amount) {
			if (amount >= Integer.MAX_VALUE) {
				amount = player.getItems().getItemCount(id, false);
			}
			if (!player.inClanWarsSafe() && id != 13307) {
				player.sendMessage("You are only able to deposit blood money from here.");
				return;
			}
	        int bagSpotsLeft = player.safeBoxSlots - items.size();
	        boolean stackable = player.getItems().isStackable(id);
	        boolean bagContainsItem = containsItem(id);
	        if (amount > bagSpotsLeft && !stackable) {
	          if (!(stackable && bagContainsItem)) {
	            amount = bagSpotsLeft;
	            }
	        }
			if (!player.getItems().playerHasItem(id)) {
				return;
			}
			if (items.size() >= player.safeBoxSlots) {
				if (player.safeBoxSlots < 50) {
					player.sendMessage("The safe-box cannot hold anymore items, purchase more by clicking the +.");
					return;
				} else {
					player.sendMessage("The safe-box cannot hold anymore items.");
					return;
				}
			}
			if (!configurationPermitted()) {
				player.sendMessage("You cannot do this right now.");
				return;
			}
			if (countItems(id) + amount >= Integer.MAX_VALUE || countItems(id) + amount <= 0) {
				return;
			}
			player.getPA().sendFrame126("" + (items.size() + 1) + "/"+ player.safeBoxSlots, 35005);
			List<Integer> amounts = player.getItems().deleteItemAndReturnAmount(id, amount);
			
			int count = 0;
			for (int amt : amounts) {
				if (!addItemToList(id, amt)) {
					break;
				}
				count++;
				if (count >= amount) {
					break;
				}
			}
			
			resetItems();
			sendItems();
			sendInventoryItems();
		}
		
		public int countItems(int id) {
			int count = 0;
			for (LootingBagItem item : items) {
				if (item.getId() == id) {
					count += item.getAmount();
				}
			}
			return count;
		}

		public boolean addItemToList(int id, int amount) {
			for (LootingBagItem item : items) {
				if (item.getId() == id) {
					if (item.getAmount() + amount >= Integer.MAX_VALUE) {
						return false;
					}
					if (player.getItems().isStackable(id)) {
						item.incrementAmount(amount);
						return true;
					}
				}
			}
			items.add(new LootingBagItem(id, amount));
			return true;
		}

		/**
		 * Closing the looting bag and resetting
		 */
		public void closeLootbag() {
			player.setSidebarInterface(3, 3213);
			player.inSafeBox = false;
			player.getPA().closeAllWindows();
			onClose();
		}

		public void onClose() {
			player.inSafeBox = false;
		}

		public void sendItems() {
			final int START_ITEM_INTERFACE = 35007;
			for (int i = 0; i < player.safeBoxSlots; i++) {
				int id = 0;
				int amt = 0;

				if (i < items.size()) {
					LootingBagItem item = items.get(i);
					if (item != null) {
						id = item.getId();
						amt = item.getAmount();
					}
				}

				if (id <= 0) {
					id = -1;
				}
				//player.getPA().sendFrame34a(START_ITEM_INTERFACE + i, id, 0, amt);
				player.getPA().itemOnInterface(id, amt, START_ITEM_INTERFACE, i);
			}
		}

		public void sendInventoryItems() {
			player.getItems().resetItems(5064);
			player.getItems().resetTempItems();
		}

		@SuppressWarnings("unused")
		private String getShortAmount(int amount) {
			if (amount <= 1) {
				return "";
			}
			String amountToString = "" + amount;
			if (amount > 1000000000) {
				amountToString = "@gre@" + (amount / 1000000000) + "B";
			} else if (amount > 1000000) {
				amountToString = "@gre@" + (amount / 1000000) + "M";
			} else if (amount > 1000) {
				amountToString = "@whi@" + (amount / 1000) + "K";
			}
			return amountToString;
		}

		private void resetItems() {
			player.getItems().resetItems(3214);
			player.getPA().requestUpdates();
		}

}
