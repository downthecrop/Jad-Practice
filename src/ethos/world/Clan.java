package ethos.world;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import ethos.Config;
import ethos.Server;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.model.players.PlayerSave;
import ethos.punishments.PunishmentType;
import ethos.util.Misc;

/**
 * This class stores all information about the clan. This includes active members, banned members, ranked members and their ranks, clan title, and clan founder. All clan joining,
 * leaving, and moderation/setup is also handled in this class.
 * 
 * @author Galkon
 * 
 */
public class Clan {

	public Player c;

	/**
	 * Adds a member to the clan.
	 * 
	 * @param player
	 */
	public void addMember(Player player) {
		if (isBanned(player.playerName)) {
			player.sendMessage("<col=FF0000>You are currently banned from this clan chat.</col>");
			return;
		}
		if (whoCanJoin > Rank.ANYONE && !isFounder(player.playerName)) {
			if (getRank(player.playerName) < whoCanJoin) {
				player.sendMessage("Only " + getRankTitle(whoCanJoin) + "s+ may join this chat.");
				return;
			}
		}
		player.clan = this;
		player.setLastClanChat(getFounder());
		activeMembers.add(player.playerName);
		player.getPA().sendString("Leave chat", 18135);
		player.getPA().sendString("Talking in: <col=FDFF38>" + getTitle() + "</col>", 18139);
		player.getPA().sendString("Owner: <col=FFFFFF>" + Misc.formatPlayerName(getFounder()) + "</col>", 18140);
		player.sendMessage("Now talking in: " + getTitle() + ".");
		player.sendMessage("To talk, start each line of chat with the / symbol.");
		updateMembers();
	}

	/**
	 * Removes the player from the clan.
	 * 
	 * @param player
	 */
	public void removeMember(Player player) {
		List<String> remove = new ArrayList<>(1);
		for (String member : activeMembers) {
			if (Objects.isNull(member)) {
				continue;
			}
			if (member.equalsIgnoreCase(player.playerName)) {
				player.clan = null;
				resetInterface(player);
				remove.add(member);
			}
		}
		activeMembers.removeAll(remove);
		player.getPA().refreshSkill(21);
		player.getPA().refreshSkill(22);
		player.getPA().refreshSkill(23);
		updateMembers();
	}

	/**
	 * Removes the player from the clan.
	 * 
	 * @param player
	 */
	public void removeMember(String name) {
		List<String> remove = new ArrayList<>(1);
		for (String member : activeMembers) {
			if (Objects.isNull(member)) {
				continue;
			}
			if (member.equalsIgnoreCase(name)) {
				Player player = PlayerHandler.getPlayer(name);
				player.clan = null;
				resetInterface(player);
				remove.add(member);
			}
		}
		activeMembers.removeAll(remove);
		updateMembers();
	}

	/**
	 * Updates the members on the interface for the player.
	 * 
	 * @param player
	 */
	public void updateInterface(Player player) {
		player.getPA().sendString("Talking in: <col=FDFF38>" + getTitle() + "</col>", 18139);
		player.getPA().sendString("Owner: <col=FFFFFF>" + Misc.formatPlayerName(getFounder()) + "</col>", 18140);
		Collections.sort(activeMembers);
		for (int index = 0; index < 100; index++) {
			if (index < activeMembers.size()) {
				player.getPA().sendFrame126("<clan=" + getRank(activeMembers.get(index)) + ">" + Misc.formatPlayerName(activeMembers.get(index)), 18144 + index);
			} else {
				player.getPA().sendFrame126("", 18144 + index);
			}
		}
	}

	/**
	 * Updates the interface for all members.
	 */
	public void updateMembers() {
		for (Player player : PlayerHandler.nonNullStream().collect(Collectors.toList())) {
			if (Objects.nonNull(activeMembers) && Objects.nonNull(player)) {
				if (activeMembers.contains(player.playerName)) {
					updateInterface(player);
				}
			}
		}
	}

	/**
	 * Resets the clan interface.
	 * 
	 * @param player
	 */
	public void resetInterface(Player player) {
		player.getPA().sendString("Join chat", 18135);
		player.getPA().sendString("Talking in: Not in chat", 18139);
		player.getPA().sendString("Owner: None", 18140);
		for (int index = 0; index < 100; index++) {
			player.getPA().sendString("", 18144 + index);
		}
	}

	/**
	 * Sends a message to the clan.
	 * 
	 * @param player
	 * @param message
	 */
	public void sendChat(Player paramClient, String paramString) {
		if (getRank(paramClient.playerName) < this.whoCanTalk) {
			paramClient.sendMessage("Only " + getRankTitle(this.whoCanTalk) + "s+ may talk in this chat.");
			return;
		}
		if (System.currentTimeMillis() < paramClient.muteEnd || Server.getPunishments().contains(PunishmentType.NET_MUTE, paramClient.connectedFrom)) {
			paramClient.sendMessage("You are muted, you cannot talk in this chat.");
			return;
		}
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				Player c = PlayerHandler.players[j];
				String icon = paramClient.getRights().getPrimary().getValue() > 0 ? "<img=" + (paramClient.getRights().getPrimary().getValue() - 1) + ">" : "";
				if ((c != null) && (this.activeMembers.contains(c.playerName)))
					c.sendMessage("/@bla@[@blu@" + getTitle() + "@bla@] " + icon + "@bla@" + Misc.optimizeText(paramClient.playerName) + ": @dre@"
							+ paramString.substring(1, 2).toUpperCase() + paramString.substring(2));
			}
		}
	}

	/**
	 * Sends a message to the clan.
	 * 
	 * @param player
	 * @param message
	 */
	public void sendMessage(String message) {
		for (int index = 0; index < Config.MAX_PLAYERS; index++) {
			Player p = PlayerHandler.players[index];
			if (p != null) {
				if (activeMembers.contains(p.playerName)) {
					p.sendMessage(message);
				}
			}
		}
	}

	/**
	 * Sets the rank for the specified name.
	 * 
	 * @param name
	 * @param rank
	 */
	public void setRank(String name, int rank) {
		if (rankedMembers.contains(name)) {
			ranks.set(rankedMembers.indexOf(name), rank);
		} else {
			rankedMembers.add(name);
			ranks.add(rank);
		}
		save();
	}

	/**
	 * Demotes the specified name.
	 * 
	 * @param name
	 */
	public void demote(String name) {
		if (!rankedMembers.contains(name)) {
			return;
		}
		int index = rankedMembers.indexOf(name);
		rankedMembers.remove(index);
		ranks.remove(index);
		save();
	}

	/**
	 * Gets the rank of the specified name.
	 * 
	 * @param name
	 * @return
	 */
	public int getRank(String name) {
		name = Misc.formatPlayerName(name);
		if (rankedMembers.contains(name)) {
			return ranks.get(rankedMembers.indexOf(name));
		}
		if (isFounder(name)) {
			return Rank.OWNER;
		}
		if (PlayerSave.isFriend(getFounder(), name)) {
			return Rank.FRIEND;
		}
		return -1;
	}

	/**
	 * Can they kick?
	 * 
	 * @param name
	 * @return
	 */
	public boolean canKick(String name) {
		if (isFounder(name)) {
			return true;
		}
		if (getRank(name) >= whoCanKick) {
			return true;
		}
		return false;
	}

	/**
	 * Can they ban?
	 * 
	 * @param name
	 * @return
	 */
	public boolean canBan(String name) {
		if (isFounder(name)) {
			return true;
		}
		if (getRank(name) >= whoCanBan) {
			return true;
		}
		return false;
	}

	/**
	 * Returns whether or not the specified name is the founder.
	 * 
	 * @param name
	 * @return
	 */
	public boolean isFounder(String name) {
		if (getFounder().equalsIgnoreCase(name)) {
			return true;
		}
		return false;
	}

	/**
	 * Returns whether or not the specified name is a ranked user.
	 * 
	 * @param name
	 * @return
	 */
	public boolean isRanked(String name) {
		name = Misc.formatPlayerName(name);
		if (rankedMembers.contains(name)) {
			return true;
		}
		return false;
	}

	/**
	 * Returns whether or not the specified name is banned.
	 * 
	 * @param name
	 * @return
	 */
	public boolean isBanned(String name) {
		name = Misc.formatPlayerName(name);
		if (bannedMembers.contains(name)) {
			return true;
		}
		return false;
	}

	/**
	 * Kicks the name from the clan chat.
	 * 
	 * @param name
	 */
	public void kickMember(String name) {
		if (!activeMembers.contains(name)) {
			return;
		}
		if (name.equalsIgnoreCase(getFounder())) {
			return;
		}
		removeMember(name);
		Player player = PlayerHandler.getPlayer(name);
		if (player != null) {
			player.sendMessage("You have been kicked from the clan chat.");
		}
		sendMessage("@blu@[Attempting to kick/ban @dre@'" + Misc.formatPlayerName(name) + "'" + " @blu@from this friends chat]");
	}

	/**
	 * Bans the name from entering the clan chat.
	 * 
	 * @param name
	 */
	public void banMember(String name) {
		name = Misc.formatPlayerName(name);
		if (bannedMembers.contains(name)) {
			return;
		}
		if (name.equalsIgnoreCase(getFounder())) {
			return;
		}
		if (isRanked(name)) {
			return;
		}
		removeMember(name);
		bannedMembers.add(name);
		save();
		Player player = PlayerHandler.getPlayer(name);
		if (player != null) {
			player.sendMessage("You have been kicked from the clan chat.");
		}
		sendMessage("@blu@[Attempting to kick/ban @dre@'" + Misc.formatPlayerName(name) + "'" + " @blu@from this friends chat]");
	}

	/**
	 * Unbans the name from the clan chat.
	 * 
	 * @param name
	 */
	public void unbanMember(String name) {
		name = Misc.formatPlayerName(name);
		if (bannedMembers.contains(name)) {
			bannedMembers.remove(name);
			save();
		}
	}

	/**
	 * Saves the clan.
	 */
	public void save() {
		Server.clanManager.save(this);
		updateMembers();
	}

	/**
	 * Deletes the clan.
	 */
	public void delete() {
		for (String name : activeMembers) {
			removeMember(name);
			Player player = PlayerHandler.getPlayer(name);
			player.sendMessage("The clan you were in has been deleted.");
		}
		Server.clanManager.delete(this);
	}

	/**
	 * Creates a new clan for the specified player.
	 * 
	 * @param player
	 */
	public Clan(Player player) {
		setTitle(player.playerName + "");
		setFounder(player.playerName.toLowerCase());
	}

	/**
	 * Creates a new clan for the specified title and founder.
	 * 
	 * @param title
	 * @param founder
	 */
	public Clan(String title, String founder) {
		setTitle(title);
		setFounder(founder);
	}

	/**
	 * Gets the founder of the clan.
	 * 
	 * @return
	 */
	public String getFounder() {
		return founder;
	}

	/**
	 * Sets the founder.
	 * 
	 * @param founder
	 */
	public void setFounder(String founder) {
		this.founder = founder;
	}

	/**
	 * Gets the title of the clan.
	 * 
	 * @return
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the title.
	 * 
	 * @param title
	 * @return
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * The title of the clan.
	 */
	public String title;

	/**
	 * The founder of the clan.
	 */
	public String founder;

	/**
	 * The active clan members.
	 */
	public LinkedList<String> activeMembers = new LinkedList<String>();

	/**
	 * The banned members.
	 */
	public LinkedList<String> bannedMembers = new LinkedList<String>();

	/**
	 * The ranked clan members.
	 */
	public LinkedList<String> rankedMembers = new LinkedList<String>();

	/**
	 * The clan member ranks.
	 */
	public LinkedList<Integer> ranks = new LinkedList<Integer>();

	/**
	 * The clan ranks.
	 * 
	 * @author Galkon
	 * 
	 */
	public static class Rank {
		public final static int ANYONE = -1;
		public final static int FRIEND = 0;
		public final static int RECRUIT = 1;
		public final static int CORPORAL = 2;
		public final static int SERGEANT = 3;
		public final static int LIEUTENANT = 4;
		public final static int CAPTAIN = 5;
		public final static int GENERAL = 6;
		public final static int OWNER = 7;
	}

	/**
	 * Gets the rank title as a string.
	 * 
	 * @param rank
	 * @return
	 */
	public String getRankTitle(int rank) {
		switch (rank) {
		case -1:
			return "Anyone";
		case 0:
			return "Friend";
		case 1:
			return "Recruit";
		case 2:
			return "Corporal";
		case 3:
			return "Sergeant";
		case 4:
			return "Lieutenant";
		case 5:
			return "Captain";
		case 6:
			return "General";
		case 7:
			return "Only Me";
		}
		return "";
	}

	/**
	 * Sets the minimum rank that can join.
	 * 
	 * @param rank
	 */
	public void setRankCanJoin(int rank) {
		whoCanJoin = rank;
	}

	/**
	 * Sets the minimum rank that can talk.
	 * 
	 * @param rank
	 */
	public void setRankCanTalk(int rank) {
		whoCanTalk = rank;
	}

	/**
	 * Sets the minimum rank that can kick.
	 * 
	 * @param rank
	 */
	public void setRankCanKick(int rank) {
		whoCanKick = rank;
	}

	/**
	 * Sets the minimum rank that can ban.
	 * 
	 * @param rank
	 */
	public void setRankCanBan(int rank) {
		whoCanBan = rank;
	}

	public void sendLootShareMessage(String message) {
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				Player c2 = PlayerHandler.players[j];
				c2.sendMessage("@red@[SERVER] " + message + "");
			}
		}
		// c.sendClan("Lootshare", message, clans[clanId].name, 3);
	}

	// public int[] unallowed = {592, 4587, 1149, 530, 526, 536, 1333, 995,
	// 1247, 1089, 1047, 1319};

	public void handleLootShare(Player c, int item, int amount) {
		/*
		 * for (int i = 0; i < unallowed.length; i++) { /* if (item == unallowed[i]) { return; } }
		 */
		if (c.getShops().getItemShopValue(item, 1, c.getItems().getItemSlot(item)) > 1) {
			sendLootShareMessage(c.playerName + " has received " + amount + "x " + ethos.model.items.Item.getItemName(item) + ".");
		}
	}

	/**
	 * The ranks privileges require (joining, talking, kicking, banning).
	 */
	public int whoCanJoin = Rank.ANYONE;
	public int whoCanTalk = Rank.ANYONE;
	public int whoCanKick = Rank.GENERAL;
	public int whoCanBan = Rank.OWNER;

}