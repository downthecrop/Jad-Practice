package ethos.model.players.packets.commands.moderator;

import java.util.Optional;

import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.model.players.packets.commands.Command;

/**
 * Market Unmute a given player.
 * 
 * @author Emiel
 */
public class Marketunmute extends Command {

	@Override
	public void execute(Player c, String input) {
		Optional<Player> optionalPlayer = PlayerHandler.getOptionalPlayer(input);
		optionalPlayer.ifPresent(player -> {
			Player c2=player;
			c2.marketMuteEnd=0;
			c.sendMessage(c2.playerName+" has been unmuted on the market channel.");
			c2.sendMessage("@red@You have been unmuted by "+c.playerName+" on the market channel.");
			// TODO: Log handling
		});
	}
}
