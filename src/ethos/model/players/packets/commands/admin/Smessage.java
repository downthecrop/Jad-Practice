package ethos.model.players.packets.commands.admin;

import java.util.Optional;

import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.model.players.packets.commands.Command;

/**
 * Send a fake server message to a given player.
 * 
 * @author Emiel
 */
public class Smessage extends Command {

	@Override
	public void execute(Player c, String input) {
		String[] args = input.split("-");
		if (args.length != 2) {
			c.sendMessage("Improper syntax; type ::smessage-player-message");
			return;
		}
		Optional<Player> optionalPlayer = PlayerHandler.getOptionalPlayer(args[0]);
		if (optionalPlayer.isPresent()) {
			Player c2 = optionalPlayer.get();
			c2.sendMessage(args[1]);
		} else {
			c.sendMessage(args[0] + " is not online. You can only send messages to online players.");
		}
	}
}
