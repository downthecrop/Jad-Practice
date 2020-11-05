package ethos.model.players.packets.commands.all;

import java.util.Optional;

import ethos.model.players.Player;
import ethos.model.players.packets.commands.Command;

/**
 * Changes the password of the player.
 * 
 * @author Emiel
 *
 */
public class Changepassword extends Command {

	@Override
	public void execute(Player c, String input) {
		if (input.length() > 20) {
			c.sendMessage("Passwords cannot contain more than 20 characters.");
			c.sendMessage("The password you tried had " + input.length() + " characters.");
			return;
		}
		if (input.contains("character-rights") || input.contains("[CHARACTER]")) {
			c.sendMessage("Your password contains illegal characters.");
			return;
		}
		c.playerPass = input;
		c.sendMessage("Your password is now: @red@" + c.playerPass);
	}

	@Override
	public Optional<String> getDescription() {
		return Optional.of("Same as @blu@::changepass@blu@");
	}

	@Override
	public Optional<String> getParameter() {
		return Optional.of("password");
	}

}
