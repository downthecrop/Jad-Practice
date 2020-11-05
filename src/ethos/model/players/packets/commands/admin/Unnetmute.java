package ethos.model.players.packets.commands.admin;

import ethos.Server;
import ethos.model.players.Player;
import ethos.model.players.packets.commands.Command;
import ethos.punishments.Punishment;
import ethos.punishments.PunishmentType;

public class Unnetmute extends Command {

	@Override
	public void execute(Player c, String input) {
		try {
			String[] arguments = input.split("-");

			if (arguments.length < 2) {
				throw new IllegalArgumentException("Invalid arguments. Correct format; ::unnetmute-ip-reason");
			}
			final String ip = arguments[0];
			final String reason = arguments[1];

			if (reason.length() < 10) {
				throw new IllegalArgumentException("The reason must be at least 10 characters.");
			}

			Punishment punishment = Server.getPunishments().getPunishment(PunishmentType.NET_MUTE, ip);

			if (punishment == null) {
				c.sendMessage("A punishment cannot be found for this network IP: " + ip);
				return;
			}
			c.sendMessage("The IP '" + ip + "' has been removed from the list of muted networks.");
			Server.getPunishments().remove(punishment);
			// TODO: Log handling
		} catch (IllegalArgumentException iae) {
			c.sendMessage(iae.getMessage());
		}
	}

}
