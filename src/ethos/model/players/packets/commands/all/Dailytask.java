package ethos.model.players.packets.commands.all;

import ethos.model.players.Player;
import ethos.model.players.packets.commands.Command;

public class Dailytask extends Command {
	
	@Override
	public void execute(Player player, String input) {
		if (player.completedDailyTask == false)
			player.sendMessage("@blu@@cr10@ Your current daily task is: "+(player.currentTask.amount - player.totalDailyDone)+" " + player.currentTask.name().toLowerCase());
		else
			player.sendMessage("@blu@@cr10@ You have already completed your daily task!");
	}
}
