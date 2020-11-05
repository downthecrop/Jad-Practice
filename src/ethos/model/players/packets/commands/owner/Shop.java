package ethos.model.players.packets.commands.owner;

import ethos.model.players.Player;
import ethos.model.players.packets.commands.Command;

/**
 * Open a specific shop.
 * 
 * @author Emiel
 *
 */
public class Shop extends Command {

	@Override
	public void execute(Player c, String input) {
		try {
			c.getShops().openShop(Integer.parseInt(input));
			c.sendMessage("You successfully opened shop #" + input + ".");
		} catch (IndexOutOfBoundsException ioobe) {
			c.sendMessage("Error. Correct syntax: ::shop shopid");
		}
	}
}
