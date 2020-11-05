package ethos.model.players.packets.action;

import ethos.Server;
import ethos.model.players.PacketType;
import ethos.model.players.Player;
import ethos.util.Misc;
import ethos.world.Clan;

public class JoinChat implements PacketType {

	@Override
	public void processPacket(Player player, int packetType, int packetSize) {
		String owner = Misc.longToPlayerName2(player.getInStream().readLong()).replaceAll("_", " ");
		
		if (player.documentGraphic) {
			player.sendMessage("Test: " + owner);
			
			return;
		}
		
		if (owner != null && owner.length() > 0) {
			if (player.clan == null) {
				/*
				 * if (player.inArdiCC) { return; }
				 */
				Clan clan = Server.clanManager.getClan(owner);
				if (clan != null) {
					clan.addMember(player);
				} else if (owner.equalsIgnoreCase(player.playerName)) {
					Server.clanManager.create(player);
				} else {
					player.sendMessage(Misc.formatPlayerName(owner) + " has not created a clan yet.");
				}
				player.getPA().refreshSkill(21);
				player.getPA().refreshSkill(22);
				player.getPA().refreshSkill(23);
			}
		}
	}

}