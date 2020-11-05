package ethos.model.npcs.bosses.zulrah;

import java.util.ArrayList;

import ethos.Server;
import ethos.model.items.GameItem;
import ethos.model.items.ItemAssistant;
import ethos.model.items.bank.BankItem;
import ethos.model.players.Player;

@SuppressWarnings("serial")
public class ZulrahLostItems extends ArrayList<GameItem> {

	/**
	 * The player that has lost items
	 */
	private final Player player;

	/**
	 * Creates a new class for managing lost items by a single player
	 * 
	 * @param player the player who lost items
	 */
	public ZulrahLostItems(final Player player) {
		this.player = player;
	}

	/**
	 * Stores the players items into a list and deletes their items
	 */
	public void store() {
		for (int i = 0; i < player.playerItems.length; i++) {
			if (player.playerItems[i] < 1) {
				continue;
			}
			add(new GameItem(player.playerItems[i] - 1, player.playerItemsN[i]));
		}
		for (int i = 0; i < player.playerEquipment.length; i++) {
			if (player.playerEquipment[i] < 1) {
				continue;
			}
			add(new GameItem(player.playerEquipment[i], player.playerEquipmentN[i]));
		}
		player.getItems().deleteEquipment();
		player.getItems().deleteAllItems();
	}

	public void retain() {
		int price = player.getRechargeItems().hasItem(13144) && player.getRechargeItems().useItem(13144) || player.getRechargeItems().hasItem(13143) && player.getRechargeItems().useItem(13143) ? -1 : 500_000;
		if (!player.getItems().playerHasItem(995, price)) {
			player.talkingNpc = 2040;
			player.getDH().sendNpcChat("You need at least 500,000GP to claim your items.");
			return;
		}
		for (GameItem item : this) {
			if (player.getMode().isUltimateIronman()) {
				if (!player.getItems().addItem(item.getId(), item.getAmount())) {
					player.sendMessage("<col=CC0000>1x " + ItemAssistant.getItemName(item.getId()) + " has been dropped on the ground.</col>");
					Server.itemHandler.createGroundItem(player, item.getId(), player.getX(), player.getY(), player.heightLevel, item.getAmount());
				}
			} else {
				player.getItems().sendItemToAnyTabOrDrop(new BankItem(item.getId(), item.getAmount()), player.getX(), player.getY());
			}
		}
		clear();
		player.getItems().deleteItem2(995, price);
		player.talkingNpc = 2040;
		if (player.getMode().isUltimateIronman()) {
			player.getDH().sendNpcChat("You have retained all of your lost items for 500,000GP.", "Your items are in your inventory.",
					"@red@If there was not enough space, they were dropped.");
		} else {
			player.getDH().sendNpcChat("You have retained all of your lost items for 500,000GP.", "Your items are in your bank.");
		}
		player.nextChat = -1;
	}

}
