package ethos.model.players.packets.commands.owner;

import ethos.model.players.Player;
import ethos.model.players.packets.commands.Command;

public class Max extends Command {

	@Override
	public void execute(Player player, String input) {
		for (int i = 0; i < 22; i++) {
			player.playerLevel[i] = 99;
			player.playerXP[i] = player.getPA().getXPForLevel(99) + 1;
			player.getPA().refreshSkill(i);
			player.getPA().setSkillLevel(i, player.playerLevel[i], player.playerXP[i]);
			player.getPA().levelUp(i);
		}
	}

}
