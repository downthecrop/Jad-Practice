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
 * IP-Ban a given player.
 * 
 * @author Emiel
 */
public class Ipban extends Command {

	@SuppressWarnings("unused")
	@Override
	public void execute(Player c, String input) {
		try {
			String[] args = input.split("-");
			if (args.length != 2) {
				throw new IllegalArgumentException();
			}
			String name = args[0];
			String reason = args[1];
			Optional<Player> optionalPlayer = PlayerHandler.getOptionalPlayer(name);
			if (optionalPlayer.isPresent()) {
				Player c2 = optionalPlayer.get();
				if (c.playerName.equals(c2.playerName)) {
					c.sendMessage("You cannot IP Ban yourself.");
					return;
				}
				if (c2.getRights().isOrInherits(Right.ADMINISTRATOR)) {
					c.sendMessage("You cannot ban this player.");
					return;
				}
				Punishments punishments = Server.getPunishments();
				if (punishments.contains(PunishmentType.NET_BAN, c2.connectedFrom)) {
					c.sendMessage("This player is already ip-banned.");
					return;
				}
				if (!punishments.contains(PunishmentType.NET_BAN, c2.connectedFrom)) {
					punishments.add(new Punishment(PunishmentType.NET_BAN, Long.MAX_VALUE, c2.connectedFrom));
					punishments.add(new Punishment(PunishmentType.BAN, Long.MAX_VALUE, c2.playerName));
					if (Server.getMultiplayerSessionListener().inAnySession(c2)) {
						MultiplayerSession session = Server.getMultiplayerSessionListener().getMultiplayerSession(c2);
						session.finish(MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
					}
					c2.disconnected = true;
					c.sendMessage("You have IP banned the user: " + c2.playerName + " with the host: " + c2.connectedFrom);
					// TODO: Log handling
				} else {
					c.sendMessage("This user is already IP Banned.");
				}
			} else {
				c.sendMessage(name + " is not online. Use ::banip instead to IP-Ban an offline player.");
			}
		} catch (Exception e) {
			c.sendMessage("Error. Correct syntax: ::ipban-player-reason");
		}
	}
}
