package ethos.model.players.packets.commands.admin;

import ethos.model.players.Player;
import ethos.model.players.packets.commands.Command;

/**
 * Teleport the player to the given coordinates.
 * 
 * @author Emiel
 *
 */
public class Tele extends Command {

	@Override
	public void execute(Player c, String input) {
		String[] args = input.split(" ");
		if (c.inClanWars() || c.inClanWarsSafe()) {
			c.sendMessage("@cr10@You can not teleport from here, speak to the doomsayer to leave.");
			return;
		}
		if (args.length == 3) {
			c.getPA().movePlayer(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]));
		} else if (args.length == 2) {
			c.getPA().movePlayer(Integer.parseInt(args[0]), Integer.parseInt(args[1]), c.heightLevel);
		}
	}
}
