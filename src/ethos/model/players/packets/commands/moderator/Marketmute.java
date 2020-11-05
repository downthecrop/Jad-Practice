package ethos.model.players.packets.commands.moderator;

import java.util.Optional;

import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.model.players.packets.commands.Command;

/**
 * Market mute a given player.
 * 
 * @author Emiel
 */
public class Marketmute extends Command {

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
			long muteEnd = 0;
			if (duration == 0) {
				muteEnd = Long.MAX_VALUE;
			} else {
				muteEnd = System.currentTimeMillis() + duration * 1000 * 60;
			}
			String reason = args[2];

			Optional<Player> optionalPlayer = PlayerHandler.getOptionalPlayer(name);
			if (optionalPlayer.isPresent()) {
				Player c2 = optionalPlayer.get();
				c2.marketMuteEnd = muteEnd;
				if (duration == 0) {
					c2.sendMessage("@red@You have been permanently muted by: " + c.playerName + ". on the market channel.");
					c.sendMessage("Successfully permanently " + c2.playerName + " for " + duration + " minutes on the market channel.");
					// TODO: Log handling
				} else {
					c2.sendMessage("@red@You have been muted by: " + c.playerName + " for " + duration + " minutes on the market channel.");
					c.sendMessage("Successfully muted " + c2.playerName + " for " + duration + " minutes on the market channel.");
					// TODO: Log handling
				}
			} else {
				c.sendMessage(name + " is not online. You can only marketmute online players.");
			}
		} catch (Exception e) {
			c.sendMessage("Error. Correct syntax: ::marketmute-player-duration-reason.");
		}
	}
}
