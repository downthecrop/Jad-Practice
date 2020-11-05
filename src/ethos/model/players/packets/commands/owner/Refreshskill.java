package ethos.model.players.packets.commands.owner;

import ethos.model.players.Player;
import ethos.model.players.packets.commands.Command;

public class Refreshskill extends Command {

	@Override
	public void execute(Player c, String input) {
		c.getPA().refreshSkill(Integer.parseInt(input));
	}

}
