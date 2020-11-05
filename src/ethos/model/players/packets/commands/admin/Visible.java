package ethos.model.players.packets.commands.admin;

import ethos.model.players.Player;
import ethos.model.players.packets.commands.Command;

/**
 * Toggles whether the player will be visible or not.
 * 
 * @author Emiel
 */
public class Visible extends Command {

	@Override
	public void execute(Player c, String input) {
		if (c.isInvisible()) {
			c.setInvisible(false);
			c.sendMessage("You are no longer invisible.");
		} else {
			c.setInvisible(true);
			c.sendMessage("You are now invisible.");
		}
		c.getPA().requestUpdates();
	}
}
