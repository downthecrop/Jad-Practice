package ethos.model.players.packets.objectoptions;

import ethos.Server;
import ethos.model.players.Player;

public class ObjectOptionSix {
	
	public static void handleOption(final Player c, int objectType, int obX, int obY) {
		if (Server.getMultiplayerSessionListener().inAnySession(c)) {
			return;
		}
		c.clickObjectType = 0;
		
		switch (objectType) {
		
		case 8356://piscdocks
			c.getPA().movePlayer(1806, 3689, 0);
			break;
		}
	}

}
