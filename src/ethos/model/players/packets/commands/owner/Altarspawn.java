package ethos.model.players.packets.commands.owner;

import ethos.model.players.Player;
import ethos.model.players.packets.commands.Command;

/**
 * Update the shops.
 * 
 * @author Emiel
 *
 */
public class Altarspawn extends Command {

	@Override
	public void execute(Player player, String input) {
		player.getSkotizo().skotizoSpecials();
	}
}
