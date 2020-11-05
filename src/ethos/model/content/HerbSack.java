package ethos.model.content;

import java.util.ArrayList;
import java.util.List;

import ethos.model.content.LootingBag.LootingBagItem;
import ethos.model.items.ItemAssistant;
import ethos.model.players.Player;
import ethos.model.players.PlayerSave;

public class HerbSack extends Pouch {

	public List<LootingBagItem> items;

	/**
	 * The herb sack id and boolean to set if we want to check if a player has a herb sack
	 */
	private static final int HERB_SACK_ID = 13226;

	/**
	 * The herb sack class
	 * @param player
	 */
	public HerbSack(Player player) {
		this.player = player;
		items = new ArrayList<>();
	}

	/**
	 * Handles players death with a herb sack in their inventory
	 * @param o
	 * @param entity
	 */
	public void onDeath(Player o, String entity) {
		if (o == null) {
			return;
		}
		handleDeath(player,entity,items);
		PlayerSave.saveGame(player);
	}
	
	/**
	 * Attempts to withdraw all herbs from the herb sack
	 */
	public void withdrawAll() {
		withdrawItems(items);
	}

	/**
	 * The id's of the herbs you are allowed to store in the herb sack
	 */
	private int[] cleanHerbs = new int[] { 249, 251, 253, 255, 257, 2998, 259, 261, 263, 3000, 265, 2481, 267, 269 };
	
	/**
	 * Attempts to fill the sack with the herbs a player has in their inventory
	 */
	public void fillSack() {

		for (int cleanHerb : cleanHerbs) {
			if (player.getItems().playerHasItem(cleanHerb, 1)) {
				addItemToHerbSack(cleanHerb, player.getItems().getItemAmount(cleanHerb));
			}
		}
	}

	/**
	 * Attempts  to add the herbs chosen to the herb sack
	 * @param id
	 * @param amount
	 */
	public void addItemToHerbSack(int id, int amount) {
		if (!configurationPermitted()) {
			player.sendMessage("You cannot do this right now.");
			return;
		}
		if (player.getItems().isStackable(id)) {
			return;
		}
		if (amount >= 28) {
			amount = player.getItems().getItemCount(id, false);
		}
		if (id == HERB_SACK_ID) {
			player.sendMessage("Don't be silly.");
			return;
		}
		if (!(id >= 249 && id <= 269) && id != 2481 && id != 3000 && id != 2998) {
			player.sendMessage("You can only store clean herbs in the herb sack.");
			return;
		}
		if (items.size() >= 14 && !(sackContainsItem(id,items) && player.getItems().isStackable(id))) {
			player.sendMessage("Herb sack cannot hold more than 14 different herbs.");
			return;
		}
		if (id <= 0 || amount <= 0) {
			return;
		}
		if (countItems(id,items) + amount >= 51 || countItems(id,items) + amount <= 0) {
			return;
		}
		player.sendMessage("Filled the sack with x" + amount + " " + ItemAssistant.getItemName(id));
		for (int amt = 0; amt < amount; amount--) {
			player.getItems().deleteItem(id, 1);
			addItemToList(id + 1, 1,items);
		}
	}
	
	/**
	 * Checks the amount and of what herb you have stored in the sack
	 */
	public void check() {
		int frame = 8149;
		int totalAmount = 0;
		for (int i2 = 8144; i2 < 8195; i2++) {
			player.getPA().sendFrame126("", i2);
		}
		player.getPA().sendFrame126("@dre@                   Herb Sack", 8144);
		player.getPA().sendFrame126("", 8145);
		player.getPA().sendFrame126("", 8148);
		for (int i = 0; i < 14; i++) {
			int id = 0;
			int amt = 0;

			if (i < items.size()) {
				LootingBagItem item = items.get(i);
				if (item != null) {
					id = item.getId();
					amt = item.getAmount();
				}
				totalAmount += amt;
				player.getPA().sendFrame126("@red@Total Amount: "+totalAmount+"/700", 8147);
				player.getPA().sendFrame126("@blu@x" + amt + " " + ItemAssistant.getItemName(id) + "", frame);
				frame++;
			}
			player.getPA().showInterface(8134);
		}
	}

}
