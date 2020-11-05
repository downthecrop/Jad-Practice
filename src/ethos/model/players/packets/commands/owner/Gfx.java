package ethos.model.players.packets.commands.owner;

import ethos.model.players.Player;
import ethos.model.players.packets.commands.Command;

/**
 * Open a specific interface.
 * 
 * @author Emiel
 *
 */
public class Gfx extends Command {
	
	@Override
	public void execute(Player c, String input) {

		String[] args = input.split(" ");
		
		if (Integer.parseInt(args[0]) > 1370) {
			c.sendMessage("Max graphic id is: 1370");
			return;
		}

		if (args.length == 1) {
			c.gfx0(Integer.parseInt(args[0]));
			c.sendMessage("Performing graphic: " + Integer.parseInt(args[0]));
			c.gfx = Integer.parseInt(args[0]);
			
		} else {

			switch (args[1]) {
			case "plus":
				//c.gfx0(c.gfx);
				c.getPA().stillGfx(c.gfx, c.absX, c.absY + 1, 0, 15);
				c.sendMessage("Performing graphic: " + c.gfx);
				c.gfx += Integer.parseInt(args[2]);
				break;

			case "minus":
				//c.gfx0(c.gfx);
				c.getPA().stillGfx(c.gfx, c.absX, c.absY + 1, 0, 15);
				c.sendMessage("Performing graphic: " + c.gfx);
				c.gfx -= Integer.parseInt(args[2]);
				break;
			}
		}
	}
}
