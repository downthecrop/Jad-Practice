package ethos.model.players.packets.objectoptions.impl;

import ethos.model.players.Boundary;
import ethos.model.players.Player;

public class RaidObjects {
	
	public static boolean clickObject1(Player player, int objectId, int objX, int objY) {
		if (!Boundary.isIn(player, Boundary.RAID_MAIN)) {
			return false;
		}
		
		if (objectId == 29789) {
			if (objX == 3307 && objY == 5205) {
				player.getPA().movePlayer(3307, 5208, 0);
			} else if (objX == 3311 && objY == 5276) {
				player.getPA().movePlayer(3311, 5279, 0);
			} else if (objX == 3311 && objY == 5308) {
				player.getPA().movePlayer(3311, 5311, 0);
			} else if (objX == 3310 && objY == 5370) {
				player.getPA().movePlayer(3311, 5373, 0);
			} else if (objX == 3318 && objY == 5400) {
				player.getPA().movePlayer(3311, 5373, 0);
			}
		}
		
		player.sendMessage("Clicked "+objectId+" at "+objX+", "+objY+"");
		return false;
	}
}
