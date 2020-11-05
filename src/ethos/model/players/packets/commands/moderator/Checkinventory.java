package ethos.model.players.packets.commands.moderator;

import java.util.Optional;

import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.model.players.packets.commands.Command;

/**
 * Shows the inventory of a given player.
 * 
 * @author Emiel
 */
public class Checkinventory extends Command {

	@Override
	public void execute(Player c, String input) {
		Optional<Player> optionalPlayer = PlayerHandler.getOptionalPlayer(input);
		if (optionalPlayer.isPresent()) {
			Player c2 = optionalPlayer.get();
			c.getPA().otherInv(c, c2);
			c.getDH().sendDialogues(206, 0);
		} else {
			c.sendMessage(input + " is not online. You can only check the inventory of online players.");
		}
	}
}
