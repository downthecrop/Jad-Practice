package ethos.model.players.packets.commands.owner;

import ethos.Config;
import ethos.model.players.Player;
import ethos.model.players.packets.commands.Command;

public class Set extends Command {

	@Override
	public void execute(Player c, String input) {
		String[] args = input.split(" ");
		
		switch (args[0]) {
		
		case "":
			c.sendMessage("Usage: ::set minions or slayer");
			break;
		
		case "minions":
			Config.AMOUNT_OF_SANTA_MINIONS = Integer.parseInt(args[1]);
			c.sendMessage("Amount of minions set to: "+ Integer.parseInt(args[1]));
			break;
			
		case "slayer":
			c.getSlayer().setPoints(Integer.parseInt(args[1]));
			c.refreshQuestTab(5);
			c.sendMessage("Slayer points set to: "+ Integer.parseInt(args[1]));
			break;
			
		case "dp":
			c.donatorPoints += Integer.parseInt(args[1]);
			c.sendMessage("Amount of donator points added: "+ Integer.parseInt(args[1]));
			c.refreshQuestTab(1);
			break;
			
		case "pkp":
			c.pkp += Integer.parseInt(args[1]);
			c.refreshQuestTab(0);
			c.sendMessage("Amount of pk points added: "+ Integer.parseInt(args[1]));
			break;
			
		case "players":
			Config.PLAYERMODIFIER = Integer.parseInt(args[1]);
			c.sendMessage("Player Count Modifier: +"+ Integer.parseInt(args[1]));
			break;
		
		}
	}
}
