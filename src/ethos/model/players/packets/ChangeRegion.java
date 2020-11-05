package ethos.model.players.packets;

import ethos.model.players.PacketType;
import ethos.model.players.Player;

public class ChangeRegion implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		c.getPA().removeObjects();
		// Server.objectManager.loadObjects(c);
	}

}
