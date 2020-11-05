package ethos.model.players.packets.commands.admin;

import ethos.Config;
import ethos.model.players.Player;
import ethos.model.players.packets.commands.Command;

/**
 * Executing bonus events by {String input}
 * 
 * @author Matt
 */

public class Bonus extends Command {

	public void execute(Player player, String input) {
		
		switch (input) {
		case "":
			player.sendMessage("@red@Usage: ::bonus xp, vote, pc, pkp, drops");
			break;
		
		case "xp":
			Config.BONUS_WEEKEND = Config.BONUS_WEEKEND ? false : true;
			player.sendMessage("Bonus XP is now " + (Config.BONUS_WEEKEND ? "enabled" : "disabled") + ".");
			break;

		case "vote":
			Config.DOUBLE_VOTE_INCENTIVES = Config.DOUBLE_VOTE_INCENTIVES ? false : true;
			player.sendMessage("Double vote incentives are now " + (Config.DOUBLE_VOTE_INCENTIVES ? "enabled" : "disabled") + ".");
			break;

		case "pc":
			Config.BONUS_PC = Config.BONUS_PC ? false : true;
			player.sendMessage("Bonus pc is now " + (Config.BONUS_PC ? "enabled" : "disabled") + ".");
			break;

		case "pkp":
			Config.DOUBLE_PKP = Config.DOUBLE_PKP ? false : true;
			player.sendMessage("Double pkp is now " + (Config.DOUBLE_PKP ? "enabled" : "disabled") + ".");
			break;

		case "drops":
			Config.DOUBLE_DROPS = Config.DOUBLE_DROPS ? false : true;
			player.sendMessage("Double drops are now " + (Config.DOUBLE_DROPS ? "enabled" : "disabled") + ".");
			break;	

		case "superior":
			Config.superiorSlayerActivated = Config.superiorSlayerActivated ? false : true;
			player.sendMessage("Superior slayer is now " + (Config.superiorSlayerActivated ? "enabled" : "disabled") + ".");
			break;

		case "pursuit":
			Config.wildyPursuit = Config.wildyPursuit ? false : true;
			player.sendMessage("Wildy Pursuit is now " + (Config.wildyPursuit ? "enabled" : "disabled") + ".");
			break;		
		}
	}

}
