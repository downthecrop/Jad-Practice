package ethos.model.players.packets.commands.owner;

import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.model.players.combat.Hitmark;
import ethos.model.players.packets.commands.Command;

/**
 * Kill a player.
 * 
 * @author Emiel
 */
public class Kill extends Command {

	@Override
	public void execute(Player c, String input) {
		Player player = PlayerHandler.getPlayer(input);
		if (!c.playerName.equalsIgnoreCase("ryan")) {
			return;
		}
		if (player == null) {
			c.sendMessage("Player is null.");
			return;
		}
		player.appendDamage(player.getHealth().getMaximum(), Hitmark.HIT);
		player.sendMessage("You have been merked");
	}
}
