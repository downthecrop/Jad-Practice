package ethos.model.players.packets.commands.admin;

import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.model.players.Right;
import ethos.model.players.packets.commands.Command;
import ethos.util.Misc;

public class Reset extends Command {
	
	@Override
	public void execute(Player c, String input) {
		String[] args = input.split("-");
		Player player = PlayerHandler.getPlayer(args[1]);
		if (player == null) {
			c.sendMessage("Player is null.");
			return;
		}
		
		switch (args[0]) {
		case "":
			player.sendMessage("@red@Usage: ::reset-farming-username");
			break;
			
		case "district":
			player.pkDistrict = !player.pkDistrict;
			player.sendMessage("" + Misc.formatPlayerName(player.playerName) + ", pk district setting have been set to " + player.pkDistrict);
			break;
			
		case "farming":
			player.setFarmingHarvest(0, -1);
			player.setFarmingSeedId(0, -1);
			player.setFarmingState(0, -1);
			player.setFarmingTime(0, -1);
			c.sendMessage("Successfully reset " + player.playerName +"'s farming patch.");
			player.sendMessage(c.playerName +" has reset your farming patch.");
			break;
			
		case "check":
			c.getPA().sendFrame126("Check Bank", 36008);
			c.getPA().sendFrame126("Kick", 36009);
			c.getPA().sendFrame126("", 36010);
			c.getPA().sendFrame126("", 36011);
			c.getPA().sendFrame126("", 36012);
			c.getPA().sendFrame126("", 36013);
			c.getPA().sendFrame126("", 36014);
			c.getPA().sendFrame126("", 36015);
			if (!c.getRights().isOrInherits(Right.MODERATOR)) {
				c.getItems().deleteItem(6713, 10);
				return;
			}
			for (int i = 0; i < player.playerEquipment.length; i++) {
				if (player.playerEquipment[i] == -1) {
					continue;
				}
				c.getPA().itemOnInterface(player.playerEquipment[i], player.playerEquipmentN[i], 36081, i);
			}
			for (int i = 0; i < player.playerItems.length; i++) {
				if (player.playerItems[i] == 0) {
					continue;
				}
				c.getPA().itemOnInterface(player.playerItems[i], player.playerItemsN[i], 36083, i);
			}
			for (int i = 0; i < player.playerLevel.length; i++) {
				c.getPA().sendFrame126("" + player.playerLevel[i], 36049 + i);
			}
			break;
		}
	}

}
