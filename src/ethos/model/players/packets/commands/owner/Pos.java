package ethos.model.players.packets.commands.owner;

import ethos.model.players.Player;
import ethos.model.players.packets.commands.Command;

/**
 * Show the current position.
 * 
 * @author Emiel
 *
 */
public class Pos extends Command {

	@Override
	public void execute(Player player, String input) {
		player.sendMessage("Current coordinates x: " + player.absX + " y:" + player.absY + " h:" + player.heightLevel);
	}
}
