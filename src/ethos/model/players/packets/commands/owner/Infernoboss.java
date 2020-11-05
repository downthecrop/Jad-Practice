package ethos.model.players.packets.commands.owner;

import ethos.model.players.Player;
import ethos.model.players.packets.commands.Command;

public class Infernoboss extends Command {

	@Override
	public void execute(Player c, String input) {
		c.createTzkalzukInstance();
		c.getInferno().initiateTzkalzuk();
	}

}

