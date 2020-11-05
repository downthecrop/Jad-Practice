package ethos.model.players.packets;

import ethos.model.players.PacketType;
import ethos.model.players.Player;
import ethos.util.Misc;

/**
 * Chat
 **/
public class ClanChat implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		String textSent = Misc.longToPlayerName2(c.getInStream().readLong());
		textSent = textSent.replaceAll("_", " ");
	}
}