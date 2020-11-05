package ethos.model.players.packets.objectoptions.impl;

import ethos.model.players.Player;
import ethos.model.players.skills.agility.AgilityHandler;

public class TrainCart {

	public static void handleInteraction(Player c){

		switch(c.safeBoxSlots){
			case 1:
				AgilityHandler.delayFade(c, "CLIMB_UP", 1761, 3710, 0, "You crawl into the cart...", "and you find yourself in Piscarlarius Mine.", 3);
				break;
			case 2:
				AgilityHandler.delayFade(c, "CLIMB_UP", 1655, 3543, 0, "You crawl into the cart...", "and you find yourself in Hosidius House.", 3);
				break;
			case 3:
				AgilityHandler.delayFade(c, "CLIMB_UP", 1590, 3622, 0, "You crawl into the cart...", "and you find yourself in Shayzien House.", 3);
				break;
			case 4:
				AgilityHandler.delayFade(c, "CLIMB_UP", 1255, 3548, 0, "You crawl into the cart...", "and you find yourself in Mount Q.", 3);
				break;
			case 5:
				AgilityHandler.delayFade(c, "CLIMB_UP", 1670, 3833, 0, "You crawl into the cart...", "and you find yourself in Arceuus House.", 3);
				break;
			default: c.sendMessage("You need to set a destination with the Control Panel.");
		}
	}

}
