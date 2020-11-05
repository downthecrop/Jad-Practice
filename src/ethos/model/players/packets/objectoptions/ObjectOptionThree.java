package ethos.model.players.packets.objectoptions;

import ethos.Server;
import ethos.model.content.tradingpost.Listing;
import ethos.model.players.Player;
import ethos.model.players.Right;

/*
 * @author Matt
 * Handles all 3rd options for objects.
 */

public class ObjectOptionThree {

	public static void handleOption(final Player c, int objectType, int obX, int obY) {
		if (Server.getMultiplayerSessionListener().inAnySession(c)) {
			return;
		}
		c.clickObjectType = 0;
		// c.sendMessage("Object type: " + objectType);
		if (Server.getHolidayController().clickObject(c, 3, objectType, obX, obY)) {
			return;
		}
		
		if (c.getRights().isOrInherits(Right.OWNER))
			c.sendMessage("Clicked Object Option 3:  "+objectType+"");
		
		switch (objectType) {
		case 24101:
			if (c.debugMessage) {
				Listing.openPost(c, false, true);
			} else {
				c.sendMessage("Trading post is currently disabled until further notice.");
			}
			//Listing.openPost(c, false, true);
			break;
		case 8356://streexerics
			c.getPA().movePlayer(1311, 3614, 0);
			break;
		case 7811:
			if (!c.inClanWarsSafe()) {
				return;
			}
			c.getDH().sendDialogues(818, 6773);
			break;
		}
	}

}
