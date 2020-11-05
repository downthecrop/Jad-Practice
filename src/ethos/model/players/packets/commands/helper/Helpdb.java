package ethos.model.players.packets.commands.helper;

import ethos.model.content.help.HelpDatabase;
import ethos.model.players.Player;
import ethos.model.players.packets.commands.Command;

/**
 * Opens an interface containing all help tickets.
 * 
 * @author Emiel
 */
public class Helpdb extends Command {

	@Override
	public void execute(Player c, String input) {
		HelpDatabase.getDatabase().openDatabase(c);
	}
}
