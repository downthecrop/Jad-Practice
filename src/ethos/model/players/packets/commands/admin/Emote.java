package ethos.model.players.packets.commands.admin;

import ethos.model.players.Player;
import ethos.model.players.packets.commands.Command;

/**
 * Force the player to perform a given emote.
 * @author Emiel
 * 
 * And log if args extend to 2.
 * @author Matt
 *
 */
public class Emote extends Command {

	@Override
	public void execute(Player c, String input) {
		String[] args = input.split("-");
		if (Integer.parseInt(args[0]) > 7528) {
			c.sendMessage("Max animation id is: 7528");
			return;
		}
		if (args.length != 2) {
			c.startAnimation(Integer.parseInt(args[0]));
			c.getPA().requestUpdates();
			c.sendMessage("Performing emote: " + Integer.parseInt(args[0]));
			c.emote = Integer.parseInt(args[0]);
		} else {
			
			c.startAnimation(Integer.parseInt(args[0]));
			c.getPA().requestUpdates();
			c.sendMessage("Performing emote: " + Integer.parseInt(args[0]));
			c.sendMessage("Logging info: Emote: "+ args[0] +" Description: "+ args[1]);
			c.getPA().logEmote(Integer.parseInt(args[0]), args[1]);
		}
	}
}
