package ethos.model.players.packets;

import ethos.Config;
import ethos.Server;
import ethos.ServerState;
import ethos.model.players.PacketType;
import ethos.model.players.Player;
import ethos.punishments.PunishmentType;
import ethos.util.Misc;
import ethos.util.log.PlayerLogging;
import ethos.util.log.PlayerLogging.LogType;

/**
 * Chat
 **/
public class Chat implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		c.setChatTextEffects(c.getInStream().readUnsignedByteS());
		c.setChatTextColor(c.getInStream().readUnsignedByteS());
		c.setChatTextSize((byte) (c.packetSize - 2));
		c.inStream.readBytes_reverseA(c.getChatText(), c.getChatTextSize(), 0);

		if (Server.getPunishments().contains(PunishmentType.NET_MUTE, c.connectedFrom)) {
			c.sendMessage("Your entire network has been muted. Other players cannot see your message.");
			return;
		}

		if (Server.getPunishments().contains(PunishmentType.MUTE, c.playerName)) {
			c.sendMessage("You are currently muted. Other players cannot see your message.");
			return;
		}

		if (System.currentTimeMillis() < c.muteEnd) {
			c.sendMessage("You are currently muted. Other players cannot see your message.");
			return;
		}
		String message = Misc.decodeMessage(c.getChatText(), c.getChatTextSize());
		
		PlayerLogging.write(LogType.PUBLIC_CHAT, c, "Spoke = "+message);
		
		if (Config.SERVER_STATE == ServerState.PUBLIC_PRIMARY) {
			//TODO public chat logging
		}
		c.setChatTextUpdateRequired(true);
	}
}
