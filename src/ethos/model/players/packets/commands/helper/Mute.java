package ethos.model.players.packets.commands.helper;

import java.util.Optional;

import ethos.Server;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.model.players.packets.commands.Command;
import ethos.punishments.Punishment;
import ethos.punishments.PunishmentType;

/**
 * Forces a given player to log out.
 * 
 * @author Emiel
 */
public class Mute extends Command {

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
			long muteEnd;
			if (duration == 0) {
				muteEnd = Long.MAX_VALUE;
			} else {
				muteEnd = System.currentTimeMillis() + duration * 1000 * 60;
			}
			String reason = args[2];

			Optional<Player> optionalPlayer = PlayerHandler.getOptionalPlayer(name);
			if (optionalPlayer.isPresent()) {
				Player c2 = optionalPlayer.get();
				c2.muteEnd = muteEnd;
				Punishment punishment = new Punishment(PunishmentType.MUTE, muteEnd, c2.playerName);
				Server.getPunishments().add(punishment);
				if (duration == 0) {
					c2.sendMessage("@red@You have been permanently muted by: " + c.playerName + ".");
					c.sendMessage("Successfully permanently " + c2.playerName + " for " + duration + " minutes.");
					// TODO: Log handling
				} else {
					c2.sendMessage("@red@You have been muted by: " + c.playerName + " for " + duration + " minutes");
					c.sendMessage("Successfully muted " + c2.playerName + " for " + duration + " minutes.");
					// TODO: Log handling
				}
			} else {
				c.sendMessage(name + " is not online. You can only mute online players.");
			}
		} catch (Exception e) {
			c.sendMessage("Error. Correct syntax: ::mute-player-duration-reason.");
		}
	}
}
