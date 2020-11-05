package ethos.model.players.packets.commands.owner;

import ethos.Server;
import ethos.model.players.Player;
import ethos.model.players.packets.commands.Command;

/**
 * Send the item IDs of all matching items to the player.
 * 
 * @author Emiel
 *
 */
public class Getid extends Command {

	@Override
	public void execute(Player c, String input) {
		if (input.length() < 3) {
			c.sendMessage("You must give at least 3 letters of input to narrow down the item.");
			return;
		}
		int results = 0;
		c.sendMessage("Searching: " + input);
		for (int j = 0; j < Server.itemHandler.ItemList.length; j++) {
			if (results == 100) {
				c.sendMessage("100 results have been found, the maximum number of allowed results. If you cannot");
				c.sendMessage("find the item, try and enter more characters to refine the results.");
				return;
			}
			if (Server.itemHandler.ItemList[j] != null && Server.itemHandler.ItemList[j].itemDescription != null
					&& !Server.itemHandler.ItemList[j].itemDescription.equalsIgnoreCase("null")) {
				if (Server.itemHandler.ItemList[j].itemName.replace("_", " ").toLowerCase().contains(input.toLowerCase())) {
					c.sendMessage("@red@" + Server.itemHandler.ItemList[j].itemName.replace("_", " ") + " - " + Server.itemHandler.ItemList[j].itemId);
					results++;
				}
			}
		}
		c.sendMessage(results + " results found...");
	}
}
