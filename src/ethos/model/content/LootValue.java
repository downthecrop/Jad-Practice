package ethos.model.content;

import ethos.model.players.Player;
import ethos.util.Misc;

public class LootValue {
	
	public static void configureValue(Player player, String config, int value) {
		player.getPA().closeAllWindows();
		player.settingLootValue = false;
		
		switch (config) {
		case "setvalue":
			player.lootValue = value;
			if (player.lootValue > 0)
			player.sendMessage("You've set the lootvalue to: @blu@"+ Misc.getValueWithoutRepresentation(player.lootValue));
			else
			player.sendMessage("You have not set your lootvalue.");
			break;

		case "checkvalue":
			if (player.lootValue > 0)
				player.sendMessage("You've set the lootvalue to: @blu@"+ Misc.getValueWithoutRepresentation(player.lootValue));
			else
			player.sendMessage("You have not set your lootvalue.");
			break;

		case "resetvalue":
			player.lootValue = 0;
			player.sendMessage("You've reset your lootvalue.");
			break;
		}
	}

}
