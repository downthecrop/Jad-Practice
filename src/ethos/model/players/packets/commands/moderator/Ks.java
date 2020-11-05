package ethos.model.players.packets.commands.moderator;

import java.util.Optional;

import ethos.model.content.kill_streaks.Killstreak;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.model.players.packets.commands.Command;

/**
 * Shows the killstreaks of a given player.
 * 
 * @author Emiel
 */
public class Ks extends Command {

	@Override
	public void execute(Player c, String input) {
		Optional<Player> optionalPlayer = PlayerHandler.getOptionalPlayer(input);
		optionalPlayer.ifPresent(player -> {
			Player c2=player;
			c.sendMessage("Hunter killstreak of "+c2.playerName+" : "+c2.getKillstreak().getAmount(Killstreak.Type.HUNTER));
			c.sendMessage("Rogue killstreak of "+c2.playerName+" : "+c2.getKillstreak().getAmount(Killstreak.Type.ROGUE));
		});
	}
}
