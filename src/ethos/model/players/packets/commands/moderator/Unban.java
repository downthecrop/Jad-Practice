package ethos.model.players.packets.commands.moderator;

import ethos.Server;
import ethos.model.players.Player;
import ethos.model.players.packets.commands.Command;
import ethos.punishments.Punishment;
import ethos.punishments.PunishmentType;
import ethos.punishments.Punishments;

/**
 * Unbans a given player.
 * 
 * @author Emiel
 */
public class Unban extends Command {

	@Override
	public void execute(Player c, String input) {
		Punishments punishments = Server.getPunishments();
		Punishment punishment = punishments.getPunishment(PunishmentType.BAN, input);

		if (!punishments.contains(PunishmentType.BAN, input) || punishment == null) {
			c.sendMessage("A punishment could not be found for: " + input);
			return;
		}

		punishments.remove(punishment);
		c.sendMessage("You have successfully removed " + input + " from the ban list.");
		// TODO: Log handling
	}
}
