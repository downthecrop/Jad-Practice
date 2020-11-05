package ethos.model.content;

import java.util.ArrayList;
import java.util.List;

import ethos.model.content.LootingBag.LootingBagItem;
import ethos.model.players.Player;
import ethos.model.players.PlayerSave;

public class RunePouch extends Pouch {
	public List<LootingBagItem> items;

	public static final int RUNE_POUCH_ID = 12791;
	private static final boolean CHECK_FOR_POUCH = true;

	private final int START_ITEM_INTERFACE = 29908;

	public int selectedItem = -1;
	public int selectedSlot = -1;
	public int interfaceId = -1;

	public RunePouch(Player player) {
		this.player = player;
		items = new ArrayList<>();
	}

	public int[] runes = new int[] { 554, 555, 556, 557, 558, 559, 560, 561, 562, 563, 564, 565, 566};

	public void onDeath(Player o, String entity) {
		if (o == null) {
			return;
		}
		handleDeath(player,entity,items);
		sendItems();
		PlayerSave.saveGame(player);
	}


	public static boolean isRunePouch(Player player, int itemId) {
		return itemId == RUNE_POUCH_ID;
	}

	public boolean handleButton(int buttonId) {
		if (buttonId == 116181) {
			closeLootbag();
			return true;
		}
		return false;
	}

	public void openRunePouch() {
		if (!player.getItems().playerHasItem(RUNE_POUCH_ID) && CHECK_FOR_POUCH) {
			return;
		}
		if (!configurationPermitted()) {
			player.sendMessage("You cannot do this right now.");
			return;
		}
		onClose();
		Player c =  player;
		sendItems();
		sendInventoryItems();
		c.getPA().showInterface(29875);
		player.viewingRunePouch = true;
	}

	public void withdrawAll() {
		withdrawItems(items);
	}
	private void removeMultipleItemsFromBag(int id, int amount) {
		System.out.print("Removing multiple items from bag");
		if (amount >= Integer.MAX_VALUE) {
			amount = countItems(id,items);
		}
		int count = 0;
		while (containsItem(id)) {
			if (!removeItemFromRunePouch(id, amount)) {
				return;
			}
			count+=amount;
			if (count >= amount) {
				return;
			}
		}
	}

	private boolean containsItem(int id) {
		for (LootingBagItem item : items) {
			if (item.getId() == id) {
				return true;
			}
		}
		return false;
	}
	public boolean handleClickItem(int id, int amount, int interfaceId) {
		if (!player.viewingRunePouch) {
			return false;
		}
		if (!configurationPermitted()) {
			player.sendMessage("You cannot do this right now.");
			return false;
		}

		if (interfaceId >= START_ITEM_INTERFACE) {
			removeMultipleItemsFromBag(id, amount);
			return true;
		} else {
			addItemToRunePouch(id, amount);
			player.getItems().deleteItem(id,amount);
			return true;
		}
	}

	private int findIndexInLootBag(int id) {
		for (LootingBagItem item : items) {
			if (item.getId() == id) {
				return items.indexOf(item);
			}
		}
		return -1;
	}

	private boolean removeItemFromRunePouch(int id, int amount) {
		if (!configurationPermitted()) {
			player.sendMessage("You cannot do this right now.");
			return false;
		}
		if (items.size() <= 0) {
			return false;
		}
		int index = findIndexInLootBag(id);
		if (index == -1) {
			return false;
		}
		LootingBagItem item = items.get(index);
		if (item == null) {
			return false;
		}
		if (item.getId() <= 0 || item.getAmount() <= 0) {
			return false;
		}
		if (player.getItems().freeSlots() <= 0) {
			if (!(player.getItems().playerHasItem(id) && player.getItems().isStackable(id))) {
				return false;
			}
		}

		if (player.getItems().getItemCount(id) + amount >= Integer.MAX_VALUE ||player.getItems().getItemCount(id) + amount <= 0) {
			return false;
		}

		int amountToAdd = 0;
		if ((items.get(items.indexOf(item)).getAmount()) > amount) {
			amountToAdd = amount;
			items.get(items.indexOf(item)).incrementAmount(-amount);
		} else {
			amountToAdd = item.getAmount();
			items.remove(index);
		}

		player.getItems().addItem(item.getId(), amountToAdd);
		sendItems();
		sendInventoryItems();
		return true;
	}

	public void deleteItemFromRunePouch(int id, int amount) {
		if (!configurationPermitted()) {
			player.sendMessage("You cannot do this right now.");
			return;
		}
		if (items.size() <= 0) {
			return;
		}
		int index = findIndexInLootBag(id);
		if (index == -1) {
			return;
		}
		LootingBagItem item = items.get(index);
		if (item == null) {
			return;
		}
		if (item.getId() <= 0 || item.getAmount() <= 0) {
			return;
		}
		if ((items.get(items.indexOf(item)).getAmount()) > amount) {
			items.get(items.indexOf(item)).incrementAmount(-amount);
		} else {
			items.remove(index);
		}
		sendItems();
	}

	private boolean pouchContainsItem(int id) {
		for (LootingBagItem item : items) {
			if (item.getId() == id) {
				return true;
			}
		}
		return false;
	}

	public boolean pouchContainsItem(int id, int amount) {
		for (LootingBagItem item : items) {
			if (item.getId() == id && item.getAmount() >= amount) {
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("unused")
	public boolean hasRunes(int runes, int amount) {
		if(!player.getItems().playerHasItem(RUNE_POUCH_ID)){
			return false;
		}
		return (pouchContainsItem(runes, amount));
	}

	public boolean hasRunes(int[] runes, int[] runeAmounts) {
		if (!player.getItems().playerHasItem(RUNE_POUCH_ID) && CHECK_FOR_POUCH) {
			return false;
		}
		for (int i = 0; i < runes.length; i++) {
			if (!pouchContainsItem(runes[i], runeAmounts[i])) {
				return false;
			}
		}
		return true;
	}

	public boolean deleteRunesOnCast(int runes, int runeAmounts) {
		if (!player.getItems().playerHasItem(RUNE_POUCH_ID) && CHECK_FOR_POUCH) {
			return false;
		}
		if (!hasRunes(runes, runeAmounts)) {
			return false;
		}
		deleteItemFromRunePouch(runes, runeAmounts);
		return true;
	}

	public boolean deleteRunesOnCast(int[] runes, int[] runeAmounts) {
		if (!player.getItems().playerHasItem(RUNE_POUCH_ID) && CHECK_FOR_POUCH) {
			return false;
		}
		if (!hasRunes(runes, runeAmounts)) {
			return false;
		}
		for (int i = 0; i < runes.length; i++) {
			deleteItemFromRunePouch(runes[i], runeAmounts[i]);
		}
		return true;
	}

	public void addItemToRunePouch(int id, int amount) {
		if (!configurationPermitted()) {
			player.sendMessage("You cannot do this right now.");
			return;
		}
		if (amount >= Integer.MAX_VALUE) {
			amount = player.getItems().getItemCount(id);
		}
		if (id == RUNE_POUCH_ID) {
			player.sendMessage("Don't be silly.");
			return;
		}
		if (!(id >= 554 && id <= 566) && id != 9075) {
			player.sendMessage("You can only store runes in a rune pouch.");
			return;
		}
		if (items.size() >= 3 && !(pouchContainsItem(id) && player.getItems().isStackable(id))) {
			player.sendMessage("Pouch cannot hold more than 3 different runes.");
			return;
		}
		if (id <= 0 || amount <= 0) {
			return;
		}
		if (countItems(id,items) + amount >= Integer.MAX_VALUE || countItems(id,items) + amount <= 0) {
			return;
		}
		// int amt = player.getItems().deleteItemAndReturnAmount(id, amount);
		// addItemToList(id, amt);

		List<Integer> amounts = player.getItems().returnAmount(id, amount);
		/*for (int amt : amounts) {
			addItemToList(id, amt);
		}*/
		player.getItems().deleteItem(id, amount);
		int count = 0;
		for (int amt : amounts) {
			if (!addItemToList(id, amt,items)) {
				break;
			}
			count++;
			player.getItems().deleteItemAndReturnAmount(id, amount);
			if (count >= amount) {
				break;
			}
		}
		player.getItems().addItem(id, amount);


		resetItems();
		sendItems();
		sendInventoryItems();
	}

	private void closeLootbag() {
		onClose();
	}

	public void withdraw() {
		if (!configurationPermitted()) {
			player.sendMessage("You cannot do this right now.");
			return;
		}
		if (!player.getItems().playerHasItem(RUNE_POUCH_ID) && CHECK_FOR_POUCH) {
			return;
		}
		openRunePouch();
	}

	private void onClose() {
		player.viewingRunePouch = false;
		player.getPA().closeAllWindows();
	}

	public void onLogin() {
		sendItems();
	}

	private void sendItems() {
		if (!player.getItems().playerHasItem(RUNE_POUCH_ID) && CHECK_FOR_POUCH) {
			return;
		}

		StringBuilder sendSpells =new StringBuilder("#");

		for (int i = 0; i < 3; i++) {
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
			player.getPA().sendFrame34a(START_ITEM_INTERFACE + i, id, 0 , amt);
			//PlayerFunction.itemOnInterface(c, START_ITEM_INTERFACE + i, 0, id, amt);
			if (id == -1)
				id = 0;
			if (i == 2) {
				sendSpells.append(id).append(":").append(amt);
			} else {
				sendSpells.append(id).append(":").append(amt).append("-");
			}
		}
		sendSpells.append("$");
		player.getPA().sendFrame126(sendSpells.toString(), 49999);
	}

	private void sendInventoryItems() {
		if (!player.getItems().playerHasItem(RUNE_POUCH_ID) && CHECK_FOR_POUCH) {
			return;
		}
		for (int i = 0; i < 28; i++) {
			int id = 0;
			int amt = 0;

			if (i < player.playerItems.length) {
				id = player.playerItems[i];
				amt = player.playerItemsN[i];
			}
			int START_INVENTORY_INTERFACE=29880;
			player.getPA().sendFrame34a(START_INVENTORY_INTERFACE+ i, id - 1, 0 , amt);
		}
	}

	private void resetItems() {
		player.getItems().resetItems(3214);
		player.getPA().requestUpdates();
	}
}
