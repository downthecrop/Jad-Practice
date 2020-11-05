package ethos.model.players.packets.commands.moderator;

import java.util.Optional;

import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.model.players.Right;
import ethos.model.players.packets.commands.Command;

/**
 * Teleport a given player to the player who issued the command.
 * 
 * @author Emiel
 */
public class Teletome extends Command {

	@Override
	public void execute(Player c, String input) {
		Optional<Player> optionalPlayer = PlayerHandler.getOptionalPlayer(input);
		if (optionalPlayer.isPresent()) {
			Player c2 = optionalPlayer.get();
			if (!c.getRights().isOrInherits(Right.ADMINISTRATOR)) {
				if (c2.inClanWars() || c2.inClanWarsSafe()) {
					c.sendMessage("@cr10@This player is currently at the pk district.");
					return;
				}
			}
			c2.teleportToX = c.absX;
			c2.teleportToY = c.absY;
			c2.heightLevel = c.heightLevel;
			c2.isStuck = false;
			c.sendMessage("You have teleported " + c2.playerName + " to you.");
			c2.sendMessage("You have been teleported to " + c.playerName + "");
		} else {
			c.sendMessage(input + " is offline. You can only teleport online players.");
		}
	}
}