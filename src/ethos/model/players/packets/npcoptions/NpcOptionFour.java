package ethos.model.players.packets.npcoptions;

import ethos.Server;
import ethos.model.players.Player;
import ethos.model.players.skills.agility.AgilityHandler;

/*
 * @author Matt
 * Handles all 4th options on non playable characters.
 */

public class NpcOptionFour {

	public static void handleOption(Player player, int npcType) {
		if (Server.getMultiplayerSessionListener().inAnySession(player)) {
			return;
		}
		player.clickNpcType = 0;
		player.rememberNpcIndex = player.npcClickIndex;
		player.npcClickIndex = 0;

		switch (npcType) {
		case 17: //Rug merchant - Sophanem
			if (!player.getDiaryManager().getDesertDiary().hasCompleted("EASY")) {
				player.getDH().sendNpcChat1("You must have completed all easy diaries here in the desert \\n to use this location.", 17, "Rug Merchant");
				return;
			}
			player.startAnimation(2262);
			AgilityHandler.delayFade(player, "NONE", 3285, 2815, 0, "You step on the carpet and take off...", "at last you end up in sophanem.", 3);
			break;

		case 2580:
			player.getPA().startTeleport(3039, 4788, 0, "modern", false);
			player.teleAction = -1;
			break;

		case 402:
		case 401:
		case 405:
		case 6797:
		case 7663:
			player.getSlayer().handleInterface("buy");
			break;
			
		case 1501:
			player.getShops().openShop(23);
			break;

		case 315:
			player.getDH().sendDialogues(545, npcType);
			break;
		}
	}

}
