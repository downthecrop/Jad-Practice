package ethos.model.players.packets.commands.owner;

import ethos.model.players.Player;
import ethos.model.players.packets.commands.Command;

public class Addxp extends Command {

	@Override
	public void execute(Player c, String input) {
		String[] args = input.split(" ");
		c.getPA().addSkillXP(Integer.parseInt(args[1]), Integer.parseInt(args[0]), true);
	}

}
