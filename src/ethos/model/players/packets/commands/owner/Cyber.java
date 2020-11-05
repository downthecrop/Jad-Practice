package ethos.model.players.packets.commands.owner;

import ethos.Config;
import ethos.model.players.Player;
import ethos.model.players.packets.commands.Command;

/**
 * Toggle the Cyber Monday sale on or off.
 * 
 * @author Emiel
 *
 */
public class Cyber extends Command {

	@Override
	public void execute(Player c, String input) {
		Config.CYBER_MONDAY = !Config.CYBER_MONDAY;
		String status = Config.CYBER_MONDAY ? "On" : "Off";
		c.sendMessage("Cyber monday: " + status);
	}
}
