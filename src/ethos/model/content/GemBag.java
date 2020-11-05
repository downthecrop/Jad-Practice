package ethos.model.content;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import ethos.model.content.LootingBag.LootingBagItem;
import ethos.model.items.ItemAssistant;
import ethos.model.players.Player;
import ethos.model.players.PlayerSave;

public class GemBag extends Pouch {
	/**
	 * Checks whether a player is allowed to configure the gem bag or not
	 * @return
	 */

	/**
	 * The gem bag id and boolean to set if we want to check if a player has one
	 */
	private static final int GEM_BAG_ID = 12020;
	public List<LootingBagItem> items;

	/**
	 * The gem bag class
	 * @param player
	 */
	public GemBag(Player player) {
		this.player = player;
		items = new ArrayList<>();
	}

	/**
	 * Handles players death with a gem bag in their inventory
	 * @param o
	 * @param entity
	 */
	public void onDeath(Player o, String entity) {
		if (o == null) {
			return;
		}
		handleDeath(o,entity,items);
		PlayerSave.saveGame(player);
	}
	
	/**
	 * Attempts to withdraw all gems from the gem bag
	 */
	public void withdrawAll() {
		withdrawItems(items);
	}

	/**
	 * The id's of the gems you are allowed to store in the gem bag
	 */
	private int[] uncutGems = new int[] { 1617, 1619, 1621, 1623, 1625, 1627, 1629, 1631, 6571, 19496 };
	
	/**
	 * Attempts to fill the bag with the gems a player has in their inventory
	 */
	public void fillBag() {

		for (int uncutGem : uncutGems) {
			if (player.getItems().playerHasItem(uncutGem, 1)) {
				addItemToGemBag(uncutGem, player.getItems().getItemAmount(uncutGem));
			}
		}
	}

	/**
	 * Attempts  to add the gems chosen to the gem bag
	 * @param id
	 * @param amount
	 */
	public void addItemToGemBag(int id, int amount) {
		boolean isUncut = IntStream.of(uncutGems).anyMatch(identification -> identification == id);
		boolean haveUncut = IntStream.of(uncutGems).anyMatch(identification -> player.getItems().playerHasItem(identification));
		if (!haveUncut) {
			player.sendMessage("You currently do not have any uncut gems to store.");
			return;
		}
		if (!isUncut) {
			player.sendMessage("You can only store uncut gems in the gem bag.");
			return;
		}
		if (!configurationPermitted()) {
			player.sendMessage("You cannot do this right now.");
			return;
		}
		if (player.getItems().isStackable(id)) {
			return;
		}
		if (amount >= 28) {
			amount = player.getItems().itemAmount(id);
		}
		if (id == GEM_BAG_ID) {
			player.sendMessage("Don't be silly.");
			return;
		}
		if (items.size() >= 61 && !(sackContainsItem(id,items) && player.getItems().isStackable(id))) {
			System.out.println("This");
			return;
		}
		if (id <= 0 || amount <= 0) {
			return;
		}
		if (countItems(id,items) + amount >= 61 || countItems(id,items) + amount <= 0) {
			player.sendMessage("You cannot store this many of this gem.");
			return;
		}
		player.sendMessage("Filled the gem bag with x" + amount + " " + ItemAssistant.getItemName(id));
		for (int amt = 0; amt < amount; amount--) {
			player.getItems().deleteItem(id, 1);
			addItemToList(id + 1, 1,items);
		}
	}
	
	/**
	 * Checks the amount and of what gem you have stored in the sack
	 */
	public void check() {
		int frame = 8149;
		int totalAmount = 0;
		for (int i2 = 8144; i2 < 8195; i2++) {
			player.getPA().sendFrame126("", i2);
		}
		player.getPA().sendFrame126("@dre@                   Gem Bag", 8144);
		player.getPA().sendFrame126("", 8145);
		player.getPA().sendFrame126("", 8148);
		if (totalAmount == 0) {
			player.getPA().sendFrame126("@red@EMPTY", 8147);
		}
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
				player.getPA().sendFrame126("@red@Total Amount: "+totalAmount+"/600", 8147);
				player.getPA().sendFrame126("@blu@x" + amt + " " + ItemAssistant.getItemName(id) + "", frame);
				frame++;
			}
			player.getPA().showInterface(8134);
		}
	}

}
