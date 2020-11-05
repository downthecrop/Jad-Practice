package ethos.model.items.item_combinations;

import ethos.model.players.Player;

public class Godswords {

	public static void makeGodsword(Player player, int i) {
		if(player.getItems().playerHasItem(11798 ) && player.getItems().playerHasItem(i))
		{
			player.getItems().deleteItem(11798,1);
			player.getItems().deleteItem(i,1);
			player.getItems().addItem(i-8,1);
			player.getDH().sendStatement("You combine the hilt and the blade to make a full godsword.");
		}
	}

	public static void dismantle(Player player, int i){
		if (player.getItems().playerHasItem(i)){
			if (player.getItems().freeSlots() < 2){
				player.getDH().sendStatement("You need at least two free inventory slots to do this.");
				return;
			}
			player.getItems().deleteItem(i,1);
			player.getItems().addItem(i+8,1);
			player.getItems().addItem(11798,1);
			player.getDH().sendStatement("You dismantle the hilt from the blade.");

		}

	}

}
