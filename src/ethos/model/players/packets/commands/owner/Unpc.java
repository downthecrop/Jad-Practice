package ethos.model.players.packets.commands.owner;

import ethos.model.players.Player;
import ethos.model.players.packets.commands.Command;

/**
 * Change back to the original player model.
 * 
 * @author Emiel
 *
 */
public class Unpc extends Command {

	@Override
	public void execute(Player c, String input) {
		c.isNpc = false;
		c.updateRequired = true;
		c.appearanceUpdateRequired = true;
		c.playerStandIndex = 808;
		c.playerWalkIndex = 819;
		c.playerRunIndex = 824;
		c.getPA().requestUpdates();
	}
}
