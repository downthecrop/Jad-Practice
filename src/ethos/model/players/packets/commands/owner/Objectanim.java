package ethos.model.players.packets.commands.owner;

import ethos.Server;
import ethos.model.players.Player;
import ethos.model.players.packets.commands.Command;
import ethos.world.objects.GlobalObject;

/**
 * Spawn a specific Object.
 * 
 * @author Emiel
 *
 */
public class Objectanim extends Command {

	@Override
	public void execute(Player c, String input) {
		String[] args = input.split(" ");
			Server.getGlobalObjects().add(new GlobalObject(Integer.parseInt(args[0]), c.absX, c.absY, c.getHeight(), 0, 10, -1, -1)); 
			c.getPA().sendPlayerObjectAnimation(c, c.absX, c.absY, Integer.parseInt(args[1]), 10, 0, c.getHeight());
	}
}
