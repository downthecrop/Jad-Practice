package ethos.model.players.packets.commands.moderator;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import ethos.Server;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.model.players.packets.commands.Command;
import ethos.punishments.Punishment;
import ethos.punishments.PunishmentType;

public class Netmute extends Command {

	@SuppressWarnings("unused")
	@Override
	public void execute(Player c, String input) {
		try {
			String[] arguments = input.split("-");
			if (arguments.length < 3) {
				throw new IllegalArgumentException("Invalid arguments. Correct format; ::netmute-name-length-reason");
			}
			Optional<Player> player = PlayerHandler.getOptionalPlayer(arguments[0]);

			if (!player.isPresent()) {
				throw new IllegalArgumentException("The player specified is not online.");
			}
			long duration = Long.parseLong(arguments[1]);
			final long minutes = duration;

			if (duration <= 0) {
				duration = Long.MAX_VALUE;
			} else {
				duration = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(duration);
			}
			final long hours = duration;
			final String reason = arguments[2];

			if (reason.length() < 10) {
				throw new IllegalArgumentException("The reason must be at least 10 characters.");
			}

			player.ifPresent(p -> {
				Punishment punishment = new Punishment(PunishmentType.NET_MUTE, hours, p.connectedFrom);
				Server.getPunishments().add(punishment);
				c.sendMessage("You have successfully ip muted " + p.playerName + " with the IP: " + p.connectedFrom + ".");
				if (hours == Long.MAX_VALUE) {
					// TODO: Log handling
					p.sendMessage("You have been permanently muted for the following reason:");
					p.sendMessage(reason);
				} else {
					// TODO: Log handling
					p.sendMessage("You have been temporarily muted for the following reason:");
					p.sendMessage(reason);
				}
			});

		} catch (IllegalArgumentException iae) {
			c.sendMessage(iae.getMessage());
		}
	}

}
