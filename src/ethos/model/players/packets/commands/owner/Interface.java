package ethos.model.players.packets.commands.owner;

import ethos.model.players.Player;
import ethos.model.players.packets.commands.Command;

/**
 * Open a specific interface.
 * 
 * @author Emiel
 *
 */
public class Interface extends Command {

	@Override
	public void execute(Player c, String input) {
		try {
			int a = Integer.parseInt(input);
			c.getPA().showInterface(a);
		} catch (Exception e) {
			c.sendMessage("::interface ####");
		}
	}
}
