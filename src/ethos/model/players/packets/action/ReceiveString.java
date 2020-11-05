package ethos.model.players.packets.action;

import ethos.Server;
import ethos.model.content.tradingpost.Listing;
import ethos.model.players.PacketType;
import ethos.model.players.Player;
import ethos.model.players.PlayerSave;
import ethos.util.Misc;
import ethos.world.Clan;

public class ReceiveString implements PacketType {

	@Override
	public void processPacket(Player player, int packetType, int packetSize) {
		String text = player.getInStream().readString();
		int index = text.indexOf(",");
		int id = Integer.parseInt(text.substring(0, index));
		String string = text.substring(index + 1);
		switch (id) {
		case 0:
			if (player.clan != null) {
				player.clan.removeMember(player);
				player.setLastClanChat("");
			}
			break;
		case 1:
			if (string.length() == 0) {
				break;
			} else if (string.length() > 15) {
				string = string.substring(0, 15);
			}
			Clan clan = player.getPA().getClan();
			if (clan == null) {
				Server.clanManager.create(player);
				clan = player.getPA().getClan();
			}
			if (clan != null) {
				clan.setTitle(string);
				player.getPA().sendFrame126(clan.getTitle(), 18306);
				clan.save();
			}
			break;
		case 2:
			if (string.length() == 0) {
				break;
			} else if (string.length() > 12) {
				string = string.substring(0, 12);
			}
			if (string.equalsIgnoreCase(player.playerName)) {
				break;
			}
			if (!PlayerSave.playerExists(string)) {
				player.sendMessage("This player doesn't exist!");
				break;
			}
			clan = player.getPA().getClan();
			if (clan.isBanned(string)) {
				player.sendMessage("You cannot promote a banned member.");
				break;
			}
			if (clan != null) {
				clan.setRank(Misc.formatPlayerName(string), 1);
				player.getPA().setClanData();
				clan.save();
			}
			break;
		case 3:
			if (string.length() == 0) {
				break;
			} else if (string.length() > 12) {
				string = string.substring(0, 12);
			}
			if (string.equalsIgnoreCase(player.playerName)) {
				break;
			}
			if (!PlayerSave.playerExists(string)) {
				player.sendMessage("This player doesn't exist!");
				break;
			}
			clan = player.getPA().getClan();
			if (clan.isRanked(string)) {
				player.sendMessage("You cannot ban a ranked member.");
				break;
			}
			if (clan != null) {
				clan.banMember(Misc.formatPlayerName(string));
				player.getPA().setClanData();
				clan.save();
			}
			break;
			
		case 5: //item searching trading post
			if (string.length() == 0)
				return;

			Listing.loadItemName(player, string.replace(" ", "_"));
			break;

		case 6: //player searching trading post
			if (string.length() == 0)
				return;

			Listing.loadPlayerName(player, string.replace(" ", "_"));
			break;
		default:
			System.out.println("Received string: identifier=" + id + ", string=" + string);
			break;
		}
	}

}
