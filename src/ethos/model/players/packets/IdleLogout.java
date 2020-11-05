package ethos.model.players.packets;

import ethos.model.players.PacketType;
import ethos.model.players.Player;

public class IdleLogout implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
//		if (c.underAttackBy > 0) {
//			return;
//		}
//		try {
		if (!c.isIdle) {
			if (c.debugMessage)
				c.sendMessage("You are now in idle mode.");
			c.isIdle = true;
		}
//			c.logout();
//			c.disconnected = true;
//			Misc.println(c.playerName + " is idle, kicked.");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}
}