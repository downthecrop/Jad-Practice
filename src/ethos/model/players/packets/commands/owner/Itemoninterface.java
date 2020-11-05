package ethos.model.players.packets.commands.owner;

import ethos.model.players.Player;
import ethos.model.players.packets.commands.Command;

/**
 * Open a specific interface.
 * 
 * @author Emiel
 *
 */
public class Itemoninterface extends Command {

	@Override
	public void execute(Player c, String input) {
		try {
			int a = Integer.parseInt(input);
			//c.getPA().showInterface(a);
			c.getPA().itemOnInterface(a, 1, 64503, 0);
			c.getPA().showInterface(64500);
		} catch (Exception e) {
			c.sendMessage("::interface ####");
		}
	}
}
