package ethos.model.players.packets.commands.moderator;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.model.players.packets.commands.Command;
import ethos.util.Misc;

/**
 * Find all other online accounts of a given player or IP.
 * 
 * @author Emiel
 */
public class Find extends Command {

	@Override
	public void execute(Player c, String input) {
		if (Misc.isIPv4Address(input)) {
			System.out.println("Recognized input as IP");
			List<Player> players = PlayerHandler.stream().filter(Objects::nonNull).filter(p -> p.connectedFrom.equals(input)).collect(Collectors.toList());
			if (players.size() > 0) {
				StringBuilder sb = new StringBuilder();
				sb.append("Players connected from ").append(input).append(": ");
				for (Player player : players) {
					sb.append(player.playerName).append(", ");
				}
				c.sendMessage(sb.substring(0, sb.length() - 2));
			} else {
				c.sendMessage("No players online with ip " + input);
			}
		} else {
			Optional<Player> optionalPlayer = PlayerHandler.getOptionalPlayer(input);
			if (optionalPlayer.isPresent()) {
				Player c2 = optionalPlayer.get();
				List<Player> players = PlayerHandler.stream().filter(Objects::nonNull).filter(p -> p.connectedFrom.equals(c2.connectedFrom)).collect(Collectors.toList());
				if (players.size() > 0) {
					StringBuilder sb = new StringBuilder();
					sb.append("Online accounts of ").append(input).append(": ");
					for (Player player : players) {
						sb.append(player.playerName).append(", ");
					}
					c.sendMessage(sb.substring(0, sb.length() - 2));
				} else {
					c.sendMessage("No other players online with the same ip");
				}
			} else {
				c.sendMessage(input + " is not line.");
			}

		}
	}
}
