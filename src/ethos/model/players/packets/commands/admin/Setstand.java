package ethos.model.players.packets.commands.admin;

import java.util.Optional;

import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.model.players.packets.commands.Command;

/**
 * Changes the stand animation of a player.
 * 
 * @author Emiel
 *
 */
public class Setstand extends Command {

	@Override
	public void execute(Player c, String input) {
		try {
			String args[] = input.split("-");
			if (args.length != 2) {
				throw new IllegalArgumentException();
			}
			Optional<Player> optionalPlayer = PlayerHandler.getOptionalPlayer(args[0]);
			if (optionalPlayer.isPresent()) {
				Player c2 = optionalPlayer.get();
				int standAnim = Integer.parseInt(args[1]);
				standAnim = standAnim > 0 && standAnim < 7157 ? standAnim : 808;
				c2.playerStandIndex = standAnim;
				c2.getPA().requestUpdates();
			} else {
				throw new IllegalStateException();
			}
		} catch (IllegalStateException e) {
			c.sendMessage("You can only use the command on online players.");
		} catch (Exception e) {
			c.sendMessage("Error. Correct syntax: ::setstand-player-standId");
		}
	}
}
