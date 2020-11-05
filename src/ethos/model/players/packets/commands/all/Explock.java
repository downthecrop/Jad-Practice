package ethos.model.players.packets.commands.all;

import java.util.Optional;

import ethos.model.players.Player;
import ethos.model.players.packets.commands.Command;

/**
 * Opens the experience lock interface
 * 
 * @author Tyler
 */
public class Explock extends Command {

	@Override
	public void execute(Player c, String input) {
		c.getExpLock().OpenExpLock();
	}

	@Override
	public Optional<String> getDescription() {
		return Optional.of("Interface to manage Exp Locks");
	}

}
