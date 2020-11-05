package ethos.model.players.packets.commands.helper;
import java.util.Optional;

import ethos.Server;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.model.players.packets.commands.Command;
import ethos.punishments.Punishment;
import ethos.punishments.PunishmentType;

/**
 * Unmute a given player.
 * 
 * @author Emiel
 */
public class Unmute extends Command {

	@Override
	public void execute(Player c, String input) {
		Optional<Player> optionalPlayer = PlayerHandler.getOptionalPlayer(input);
		if (optionalPlayer.isPresent()) {
			Player c2 = optionalPlayer.get();

			Punishment punishment = Server.getPunishments().getPunishment(PunishmentType.MUTE, c2.playerName);

			if (punishment == null) {
				c.sendMessage("This player is not muted.");
				return;
			}

			Server.getPunishments().remove(punishment);
			c2.muteEnd = 0;
			c.sendMessage(c2.playerName + " has been unmuted.");
			c2.sendMessage("@red@You have been unmuted by " + c.playerName + ".");
			// TODO: Log handling
		}
	}
}
