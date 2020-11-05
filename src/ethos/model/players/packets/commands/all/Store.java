package ethos.model.players.packets.commands.all;

import java.util.Optional;

import ethos.model.players.Player;
import ethos.model.players.packets.commands.Command;

/**
 * Opens the store page in the default web browser.
 * 
 * @author Emiel
 */
public class Store extends Command {

	@Override
	public void execute(Player c, String input) {
	}

	@Override
	public Optional<String> getDescription() {
		return Optional.of("Same as @blu@::donate@blu@");
	}

}
