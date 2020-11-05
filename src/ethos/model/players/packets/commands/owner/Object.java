package ethos.model.players.packets.commands.owner;

import java.util.Arrays;

import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.model.players.packets.commands.Command;

/**
 * Spawn a specific Object.
 * 
 * @author Emiel
 *
 */
public class Object extends Command {

	@Override
	public void execute(Player c, String input) {
		String[] args = input.split(" ");
		if (args.length < 2) {
			Arrays.stream(PlayerHandler.players).forEach(p -> {
				if (p != null) {
					p.getPA().object(Integer.parseInt(args[0]), c.absX, c.absY, 0, 10);
				}
			});
			c.sendMessage("Object: " + Integer.parseInt(args[0]) + ", Type: 10");
		} else {
			Arrays.stream(PlayerHandler.players).forEach(p -> {
				if (p != null) {
					p.getPA().object(Integer.parseInt(args[0]), c.absX, c.absY, Integer.parseInt(args[1]), 10);
				}
			});
			c.sendMessage("Object: " + Integer.parseInt(args[0]) + ", Type: 10");
		}
	}
}
