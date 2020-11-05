package ethos.model.players.packets.commands.all;

import java.util.Map.Entry;

import ethos.Server;
import ethos.model.players.Player;
import ethos.model.players.packets.commands.Command;

import java.util.Optional;

/**
 * Shows a list of commands.
 * 
 * @author Emiel
 *
 */
public class Commands extends Command {

	@Override
	public void execute(Player c, String input) {
		if (Server.getMultiplayerSessionListener().inAnySession(c)) {
			return;
		}
		int counter = 8144;
		c.getPA().sendFrame126("@dre@Ascend Commands", counter++);
		c.getPA().sendFrame126("", counter++);
		counter++; // 8146 is already being used
		counter = sendCommands(c, "all", counter);
		c.getPA().sendFrame126("", counter++);
		c.getPA().sendFrame126("@dre@Donators Only", counter++);
		//noinspection UnusedAssignment
		counter = sendCommands(c, "donator", counter);
		c.getPA().showInterface(8134);
	}

	public int sendCommands(Player player, String rank, int counter) {
		for (Entry<String, Command> entry : ethos.model.players.packets.Commands.COMMAND_MAP.entrySet()) {
			if (entry.getKey().contains("." + rank.toLowerCase() + ".")) {
				if (entry.getValue().isHidden()) {
					continue;
				}
				String command = entry.getValue().getClass().getSimpleName().toLowerCase();
				if (entry.getValue().getParameter().isPresent()) {
					command += " @dre@" + entry.getValue().getParameter().get() + "@bla@";
				}
				String description = entry.getValue().getDescription().orElse("No description");
				player.getPA().sendFrame126("@blu@::" + command + "@bla@ - " + description, counter);
				counter++;
			}
		}
		return counter;
	}

	@Override
	public Optional<String> getDescription() {
		return Optional.of("Shows a list of all commands");
	}

}
