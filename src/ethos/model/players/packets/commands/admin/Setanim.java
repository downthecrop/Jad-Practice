package ethos.model.players.packets.commands.admin;

import java.util.Optional;

import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.model.players.packets.commands.Command;

/**
 * Changes the walk, stand and run animations of a player.
 * 
 * @author Emiel
 *
 */
public class Setanim extends Command {

	@Override
	public void execute(Player c, String input) {
		try {
			String args[] = input.split("-");
			if (args.length != 4) {
				throw new IllegalArgumentException();
			}
			Optional<Player> optionalPlayer = PlayerHandler.getOptionalPlayer(args[0]);
			if (optionalPlayer.isPresent()) {
				Player c2 = optionalPlayer.get();

				int standAnim = Integer.parseInt(args[1]);
				int walkAnim = Integer.parseInt(args[2]);
				int runAnim = Integer.parseInt(args[3]);

				standAnim = standAnim > 0 && standAnim < 7500 ? standAnim : 808;
				walkAnim = walkAnim > 0 && walkAnim < 7500 ? walkAnim : 819;
				runAnim = runAnim > 0 && runAnim < 7500 ? runAnim : 824;

				c2.playerStandIndex = standAnim;
				c2.playerWalkIndex = walkAnim;
				c2.playerRunIndex = runAnim;
				c2.getPA().requestUpdates();
				c.sendMessage("Setting animations: Stand: "+ standAnim + " Walk: "+ walkAnim + " Run: "+ runAnim);
			} else {
				throw new IllegalStateException();
			}
		} catch (IllegalStateException e) {
			c.sendMessage("You can only use the command on online players.");
		} catch (Exception e) {
			c.sendMessage("Error. Correct syntax: ::setanim-player-standId-walkId-runId");
		}
	}
}
