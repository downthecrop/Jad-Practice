package ethos.model.players.packets.commands.admin;

import ethos.model.players.Player;
import ethos.model.players.packets.commands.Command;

public class Coords extends Command {

	@Override
	public void execute(Player c, String input) {
		c.sendMessage("Your coordinates are: "+c.absX+", "+c.absY+" "+c.heightLevel+"");
	}

}
