package ethos.model.players.packets.commands.admin;

import ethos.model.players.Player;
import ethos.model.players.packets.commands.Command;

/**
 * Change the spellbook.
 * 
 * @author Emiel
 *
 */
public class Spells extends Command {

	@Override
	public void execute(Player c, String input) {
		if (c.playerMagicBook == 2) {
			c.sendMessage("You switch to modern magic.");
			c.setSidebarInterface(6, 938);
			c.playerMagicBook = 0;
		} else if (c.playerMagicBook == 0) {
			c.sendMessage("You switch to ancient magic.");
			c.setSidebarInterface(6, 838);
			c.playerMagicBook = 1;
		} else if (c.playerMagicBook == 1) {
			c.sendMessage("You switch to lunar magic.");
			c.setSidebarInterface(6, 29999);
			c.playerMagicBook = 2;
		}
	}
}
