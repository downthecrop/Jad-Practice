package ethos.model.players.packets;

import ethos.model.players.PacketType;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;

public class FollowPlayer implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		int followPlayer = c.getInStream().readUnsignedWordBigEndian();
		if (c.getInterfaceEvent().isActive()) {
			c.sendMessage("Please finish what you're doing.");
			return;
		}
		if (c.getTutorial().isActive()) {
			c.getTutorial().refresh();
			return;
		}
		if (c.getBankPin().requiresUnlock()) {
			c.getBankPin().open(2);
			return;
		}
		if (PlayerHandler.players[followPlayer] != null) {
			c.playerIndex = 0;
			c.npcIndex = 0;
			c.mageFollow = false;
			c.usingBow = false;
			c.usingRangeWeapon = false;
			c.followDistance = 1;
			c.followId = followPlayer;
		}
	}
}