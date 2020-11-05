package ethos.model.players.packets.commands.owner;

import ethos.Config;
import ethos.model.players.Player;
import ethos.model.players.packets.commands.Command;

public class Hpray extends Command {

	@Override
	public void execute(Player c, String input) {
		if (c.inGodmode()) {
			c.getHealth().setMaximum(c.getLevelForXP(c.playerXP[Config.HITPOINTS]));
			c.getHealth().reset();
			c.playerLevel[Config.PRAYER] = c.getLevelForXP(c.playerXP[Config.PRAYER]);
			c.getPA().refreshSkill(Config.PRAYER);
			c.specAmount = 10.0;
			c.getPA().requestUpdates();
			c.setSafemode(false);
			c.setGodmode(false);
			c.sendMessage("Mode is now: Off");
		} else {
			c.getHealth().setMaximum(9999);
			c.getHealth().reset();
			c.playerLevel[Config.PRAYER] = 9999;
			c.getPA().refreshSkill(Config.PRAYER);
			c.specAmount = 9999;
			c.getPA().requestUpdates();
			c.setSafemode(true);
			c.setGodmode(true);
			c.sendMessage("Mode is now: On");
		}
	}
}
