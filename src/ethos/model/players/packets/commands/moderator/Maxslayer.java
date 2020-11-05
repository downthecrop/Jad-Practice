package ethos.model.players.packets.commands.moderator;

import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;

import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.model.players.packets.commands.Command;

public class Maxslayer extends Command {
	
	@Override
	public void execute(Player c, String input) {
		Optional<Player> op = PlayerHandler.nonNullStream().filter(Objects::nonNull).max(Comparator.comparing(client -> client.getSlayer().getPoints()));
		op.ifPresent(player -> c.sendMessage("Highest slayer points: "+player.playerName+" - "+player.getSlayer().getPoints()));
	}

}
