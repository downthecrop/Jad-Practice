package ethos.model.players.packets.commands.owner;

import ethos.model.players.Player;
import ethos.model.players.packets.commands.Command;

public class Anim extends Command {

	@Override
	public void execute(Player player, String input) {
		int id = Integer.parseInt(input);
		player.startAnimation(id);
	}

}
