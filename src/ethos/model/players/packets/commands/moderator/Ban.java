package ethos.model.players.packets.commands.moderator;

import java.util.Optional;

import ethos.Server;
import ethos.model.multiplayer_session.MultiplayerSession;
import ethos.model.multiplayer_session.MultiplayerSessionFinalizeType;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.model.players.Right;
import ethos.model.players.packets.commands.Command;
import ethos.punishments.Punishment;
import ethos.punishments.PunishmentType;
import ethos.punishments.Punishments;

/**
 * Ban a given player.
 * 
 * @author Emiel
 */
public class Ban extends Command {

	@SuppressWarnings("unused")
	@Override
	public void execute(Player c, String input) {
		try {
			String[] args = input.split("-");
			if (args.length != 3) {
				throw new IllegalArgumentException();
			}
			String name = args[0];
			int duration = Integer.parseInt(args[1]);
			String reason = args[2];
			long banEnd;
			if (duration == 0) {
				banEnd = Long.MAX_VALUE;
			} else {
				banEnd = System.currentTimeMillis() + duration * 1000 * 60;
			}
			Punishments punishments = Server.getPunishments();
			if (punishments.contains(PunishmentType.BAN, name)) {
				c.sendMessage("This player is already banned.");
				return;
			}
			Server.getPunishments().add(new Punishment(PunishmentType.BAN, banEnd, name));
			Optional<Player> optionalPlayer = PlayerHandler.getOptionalPlayer(name);
			if (optionalPlayer.isPresent()) {
				Player c2 = optionalPlayer.get();
				if (!c.getRights().isOrInherits(Right.OWNER) && c2.getRights().isOrInherits(Right.ADMINISTRATOR)) {
					c.sendMessage("You cannot ban this player.");
					return;
				}
				if (Server.getMultiplayerSessionListener().inAnySession(c2)) {
					MultiplayerSession session = Server.getMultiplayerSessionListener().getMultiplayerSession(c2);
					session.finish(MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
				}
				c2.properLogout = true;
				c2.disconnected = true;

				return;
			}
			if (duration == 0) {
				c.sendMessage(name + " has been permanently banned.");
				// TODO: Log handling
			} else {
				c.sendMessage(name + " has been banned for " + duration + " minute(s).");
				// TODO: Log handling
			}
		} catch (Exception e) {
			c.sendMessage("Correct usage: ::ban-player-duration-reason (0 as duration for permanent)");
		}
	}
}
