package ethos.model.minigames.pest_control;

import java.util.ArrayList;
import java.util.Collections;

import ethos.Config;
import ethos.event.CycleEvent;
import ethos.event.CycleEventContainer;
import ethos.event.CycleEventHandler;
import ethos.model.content.achievement.AchievementType;
import ethos.model.content.achievement.Achievements;
import ethos.model.content.achievement_diary.western_provinces.WesternDiaryEntry;
import ethos.model.npcs.NPC;
import ethos.model.npcs.NPCHandler;
import ethos.model.players.Boundary;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.util.Misc;

/**
 * 
 * @author Jason http://www.rune-server.org/members/jason
 * @date Sep 16, 2013
 */
public class PestControl {

	private static final Object LOBBY_EVENT_LOCK = new Object();

	private static final Object GAME_EVENT_LOCK = new Object();

	public static final Boundary GAME_BOUNDARY = new Boundary(2624, 2550, 2690, 2619);

	public static final Boundary LOBBY_BOUNDARY = new Boundary(2660, 2638, 2663, 2643);

	public static boolean gameActive, lobbyActive;

	private static int gameTime, lobbyTime, timeState;

	private static final int GAME_TIME = 300;

	private static final int LOBBY_TIME = 60;

	private static final int PLAYERS_REQUIRED = 1;

	private static final int POINT_REWARD = 5;

	private static final int COIN_REWARD = 500;

	public static final int MINIMUM_DAMAGE = 35;

	private static ArrayList<Player> lobbyMembers = new ArrayList<>();

	private static ArrayList<Player> gameMembers = new ArrayList<>();

	private static final int[][] NPC_DATA = { 
			{ 1739, 2628, 2591, 250, 0, 150 }, // portal
			{ 1740, 2680, 2588, 250, 0, 150 }, // portal
			{ 1741, 2669, 2570, 250, 0, 150 }, // portal
			{ 1742, 2645, 2569, 250, 0, 150 }, // portal
	};

	/**
	 * Adds a player to the lobby area
	 * 
	 * @param player the player to be added
	 */
	public static void addToLobby(Player player) {
		if (Boundary.isIn(player, GAME_BOUNDARY) || Boundary.isIn(player, LOBBY_BOUNDARY))
			return;
		if (player.combatLevel < 40) {
			player.sendMessage("You need a combat level of atleast 40 to join in.");
			return;
		}
		if (!lobbyActive && lobbyMembers.size() == 0 && lobbyTime <= 0)
			createNewLobby();
			addLobbyMember(player);
			player.getPA().movePlayer(2661, 2639, 0);
		if (gameActive)
			player.sendMessage("You have joined the pest control waiting lobby. There is currently a game going on.");
		else
			player.sendMessage("You have joined the pest control waiting lobby. A new game will start shortly.");
	}

	/**
	 * Removes a player from the lobby
	 * 
	 * @param player the player to be removed
	 */
	public static void removeFromLobby(Player player) {
		removeLobbyMember(player);
		if (Boundary.isIn(player, LOBBY_BOUNDARY)) {
			player.getPA().movePlayer(2657, 2639, 0);
			player.sendMessage("You have left the pest control waiting lobby.");
		}
	}

	/**
	 * The interface for both the lobby and the game
	 * 
	 * @param player the player that the interface is drawn for
	 * @param type the string that determines whether its for the game or the lobby
	 */
	public static void drawInterface(Player player, String type) {
		switch (type.toLowerCase()) {
		case "lobby":
			int minutes, seconds;
			if (gameActive) {
				minutes = (gameTime + lobbyTime) / 60;
				seconds = (gameTime + lobbyTime) % 60;
			} else {
				minutes = lobbyTime / 60;
				seconds = lobbyTime % 60;
			}
			if (seconds > 9)
				player.getPA().sendString("Next Departure: " + minutes + ":" + seconds, 21120);
			else
				player.getPA().sendString("Next Departure: " + minutes + ":0" + seconds, 21120);
				player.getPA().sendString("Players Ready: " + lobbyMembers.size() + "", 21121);
				player.getPA().sendString("Players Required: " + PLAYERS_REQUIRED + "", 21122);
				player.getPA().sendString("Commendation Points: " + player.pcPoints + "", 21123);
			break;
		case "game":
			player.getPA().sendString("@or1@Members: " + PestControl.gameMembers.size(), 21115);
			player.getPA().sendString("@or1@Health: " + PestControl.getAllPortalHP(), 21116);
			for (int i = 0; i < NPC_DATA.length; i++) {
				NPC npc = NPCHandler.getNpc(NPC_DATA[i][0], NPC_DATA[i][1], NPC_DATA[i][2]);
				if (npc == null) {
					player.getPA().sendString("@red@Error", 21111 + i);
					continue;
				}
				String color = npc.isDead ? "@red@" : npc.getHealth().getAmount() > 0 && npc.getHealth().getAmount() < 150 ? "@or1@" : "@gre@";
				int hp = npc.isDead ? 0 : npc.getHealth().getAmount();
				player.getPA().sendString(color + hp, 21111 + i);
			}
			if (gameTime <= 20)
				player.getPA().sendString("@red@Time Remaining: " + gameTime, 21117);
			else if (gameTime > 20 && gameTime < 100)
				player.getPA().sendString("@or1@Time Remaining: " + gameTime, 21117);
			else
				player.getPA().sendString("@gre@Time Remaining: " + gameTime, 21117);
			break;
		}
	}

	/**
	 * Destroys, or finalizes the lobby under certain circumstances that are not extracted to the method
	 */
	private static void finalizeLobby() {
		lobbyTime = -1;
		lobbyActive = false;
		timeState = 0;
		lobbyMembers.clear();
		lobbyMembers.trimToSize();
	}

	/**
	 * Destroys or finalizes the game under certain circumstances that are not extracted to the method
	 */
	public static void finalizeGame() {
		gameTime = -1;
		gameActive = false;
		gameMembers.clear();
		gameMembers.trimToSize();
	}

	public static void handleGameOutcome(boolean state) {
		if (state) {
			int coins = 25000;
			for (int i = 0; i < gameMembers.size(); i++) {
				if (gameMembers.get(i) != null) {
					if (Boundary.isIn(gameMembers.get(i), GAME_BOUNDARY)) {
						Player player = gameMembers.get(i);
						player.getPA().movePlayer(2657, 2638 + Misc.random(10), 0);
						if (player.pestControlDamage > MINIMUM_DAMAGE) {
							Achievements.increase(player, AchievementType.PEST_CONTROL_ROUNDS, 1);
							player.getDiaryManager().getWesternDiary().progress(WesternDiaryEntry.PEST_CONTROL);
							
							int point_reward = Config.BONUS_PC && Config.BONUS_PC_WOGW ? 15 : 
											   !Config.BONUS_PC && Config.BONUS_PC_WOGW ? 10 : 
											   Config.BONUS_PC && !Config.BONUS_PC_WOGW ? 10 : POINT_REWARD;
							
							player.pcPoints += point_reward;
							player.refreshQuestTab(3);
							player.sendMessage("You won! You obtain "+ point_reward +" commendation points and " + coins + " coins as a bonus.");
							player.getItems().addItem(995, coins);
						} else {
							player.sendMessage("You won but you didn't deal enough damage on the portals.");
							player.sendMessage("You must play your part in defeating the portals to get rewarded.");
						}
						player.getCombat().resetPrayers();
						player.getHealth().reset();
						player.playerLevel[5] = player.getLevelForXP(player.playerXP[5]);
						player.getPA().refreshSkill(5);
					}
				}
			}
		} else {
			for (int i = 0; i < gameMembers.size(); i++) {
				if (gameMembers.get(i) != null) {
					if (Boundary.isIn(gameMembers.get(i), GAME_BOUNDARY)) {
						Player player = gameMembers.get(i);
						player.getPA().movePlayer(2657, 2639, 0);
						player.sendMessage("You lost....better luck next time.");
						player.getCombat().resetPrayers();
						player.getHealth().reset();
						player.playerLevel[5] = player.getLevelForXP(player.playerXP[5]);
						player.getPA().refreshSkill(5);
					}
				}
			}
		}
	}

	/**
	 * Changes all the necessary values for each variable needed for a new game
	 */
	public static void createNewLobby() {
		timeState = 0;
		lobbyTime = LOBBY_TIME;
		lobbyActive = true;
		lobbyMembers.clear();
		lobbyMembers.trimToSize();
		lobbyCycle();
	}

	/**
	 * Adds a player to the game aswell as refershes their statistics
	 * 
	 * @param player the player
	 */
	public static void addPlayerToGame(Player player) {
		removeLobbyMember(player);
		addGameMember(player);
		player.pestControlDamage = 0;
		player.getPA().movePlayer(2656 + Misc.random(2), 2614 - Misc.random(3), 0);
		player.sendMessage("Welcome to pest control, defeat all the portals within the time frame.", 255);
		player.specAmount = 10;
		player.setRunEnergy(100);
		player.getHealth().reset();
		player.playerLevel[5] = player.getLevelForXP(player.playerXP[5]);
		player.getPA().refreshSkill(5);
	}

	/**
	 * Creates a new pest control game from the players in the lobby
	 */
	public static void createNewGame() {
		killAllNpcs();
		for (int i = 0; i < PlayerHandler.players.length; i++) {
			Player player = PlayerHandler.players[i];
			if (player != null) {
				if (Boundary.isIn(player, LOBBY_BOUNDARY)) {
					addPlayerToGame(player);
				}
			}
		}
		finalizeLobby();
		spawnAllNpcs();
		gameTime = PestControl.GAME_TIME;
		gameActive = true;
		gameCycle();
	}

	public static int getAllPortalHP() {
		int total = 0;
		for (int i = 0; i < NPC_DATA.length; i++) {
			NPC npc = NPCHandler.getNpc(NPC_DATA[i][0], NPC_DATA[i][1], NPC_DATA[i][2]);
			if (npc == null || npc.isDead) {
				continue;
			}
			total += npc.getHealth().getAmount();
		}
		return total;
	}

	/**
	 * The main cycle for the lobby that is only active when a player is in the lobby
	 */
	public static void lobbyCycle() {
		CycleEventHandler.getSingleton().stopEvents(LOBBY_EVENT_LOCK);
		CycleEventHandler.getSingleton().addEvent(LOBBY_EVENT_LOCK, new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				if (!lobbyActive) {
					finalizeLobby();
					container.stop();
					return;
				}
				if (lobbyTime > 0) {
					if (!gameActive) {
						if (lobbyMembers.size() >= 2 && lobbyTime > (LOBBY_TIME / 2))
							lobbyTime = (LOBBY_TIME / 3);
					}
					lobbyTime--;
					if (lobbyTime <= 0) {
						if (lobbyMembers.size() < PestControl.PLAYERS_REQUIRED) {
							if (timeState == 0)
								lobbyTime = (LOBBY_TIME / 2);
							else if (timeState == 1)
								lobbyTime = (LOBBY_TIME / 3);
							else if (timeState == 2)
								lobbyTime = (LOBBY_TIME / 4);
							else {
								lobbyTime = LOBBY_TIME;
								timeState = 0;
							}
							sendLobbyMessage("There is not enough players to start a new game, you need atleast 2 players.");
							timeState++;
							container.stop();
							lobbyCycle();
							return;
						}
						if (PestControl.gameActive) {
							lobbyTime = LOBBY_TIME;
							PestControl.sendLobbyMessage("Game is still active, timer restarted.");
							container.stop();
							lobbyCycle();
							return;
						}
						createNewGame();
						container.stop();
						return;
					}
				}
				if (lobbyMembers.size() == 0) {
					finalizeLobby();
					container.stop();
					return;
				}
			}

		}, 2);
	}

	/**
	 * The main game cycle meant to handle operations that include the players within the pest control minigame
	 */
	public static void gameCycle() {
		CycleEventHandler.getSingleton().stopEvents(GAME_EVENT_LOCK);
		CycleEventHandler.getSingleton().addEvent(GAME_EVENT_LOCK, new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				if (!gameActive || gameMembers.size() == 0) {
					container.stop();
					finalizeGame();
					return;
				}
				if (gameTime > 0) {
					gameTime--;
					if (PestControl.getAllPortalHP() == 0) {
						container.stop();
						handleGameOutcome(true);
						finalizeGame();
						return;
					}
					if (gameTime <= 0) {
						container.stop();
						handleGameOutcome(false);
						finalizeGame();
						return;
					}
				}
			}

		}, 2);
	}

	/**
	 * Sending the message to each of the lobby members
	 * 
	 * @param string the message
	 */
	private static void sendLobbyMessage(String string) {
		for (Player p : lobbyMembers)
			if (p != null)
				if (Boundary.isIn(p, LOBBY_BOUNDARY))
					p.sendMessage(string, 255);
	}

	/**
	 * Adds the player to the lobby in LILO order
	 * 
	 * @param player the player
	 */
	private static void addLobbyMember(Player player) {
		if (player == null)
			return;
		if (lobbyMembers.contains(player))
			lobbyMembers.remove(player);
		lobbyMembers.add(player);
		lobbyMembers.trimToSize();
	}

	/**
	 * Removes a member from the lobby
	 * 
	 * @param player the player
	 */
	private static void removeLobbyMember(Player player) {
		lobbyMembers.removeAll(Collections.singleton(null));
		if (player == null)
			return;
		if (lobbyMembers.contains(player))
			lobbyMembers.remove(player);
		lobbyMembers.trimToSize();
	}

	/**
	 * Adds a member to the game
	 * 
	 * @param player the player
	 */
	private static void addGameMember(Player player) {
		if (player == null)
			return;
		if (gameMembers.contains(player))
			gameMembers.remove(player);
		gameMembers.add(player);
		gameMembers.trimToSize();
	}

	/**
	 * Remoes a member from the gameMembers ArrayList
	 * 
	 * @param player the player to be removed
	 */
	public static void removeGameMember(Player player) {
		gameMembers.removeAll(Collections.singleton(null));
		if (player == null)
			return;
		if (gameMembers.contains(player))
			gameMembers.remove(player);
		gameMembers.trimToSize();
	}

	/**
	 * A flag to return whether or not the player passed in the params is a meber of the lobby
	 * 
	 * @param player The player
	 * @return
	 */
	public static boolean isLobbyMember(Player player) {
		if (player == null)
			return false;
		return lobbyMembers.contains(player) && Boundary.isIn(player, LOBBY_BOUNDARY);
	}

	/**
	 * A flag to return whether or not the player passed in the params is a meber of the game
	 * 
	 * @param player The player
	 * @return
	 */
	public static boolean isGameMember(Player player) {
		if (player == null)
			return false;
		return gameMembers.contains(player) && Boundary.isIn(player, GAME_BOUNDARY);
	}

	/**
	 * Outputs a list of data to the player for debug reasons
	 * 
	 * @param c The player
	 */
	public static void read(Player c) {
		c.sendMessage("-------------------------------------------------------------", 255);
		c.sendMessage("Lobby Active: " + lobbyActive, 255);
		c.sendMessage("Game Active: " + gameActive, 255);
		c.sendMessage("Lobby Members: " + lobbyMembers.size(), 255);
		c.sendMessage("Game Members: " + gameMembers.size(), 255);
		c.sendMessage("Lobby Time: " + lobbyTime, 255);
		c.sendMessage("Game Time: " + gameTime, 255);
		c.sendMessage("-------------------------------------------------------------", 255);
	}

	public static void spawnAllNpcs() {
		int increase = gameMembers.size() * 25;
		for (int i = 0; i < NPC_DATA.length; i++) {
			NPCHandler.spawnNpc(NPC_DATA[i][0], NPC_DATA[i][1], NPC_DATA[i][2], 0, 0, NPC_DATA[i][3] + increase, 0, 0, 150);
		}
	}

	public static void killAllNpcs() {
		for (int i = 0; i < NPC_DATA.length; i++) {
			NPC npc = NPCHandler.getNpc(NPC_DATA[i][0]);
			if (npc != null) {
				NPCHandler.npcs[npc.getIndex()].applyDead = true;
				NPCHandler.npcs[npc.getIndex()].updateRequired = true;
				NPCHandler.npcs[npc.getIndex()] = null;
			}
		}
	}

	public static void drawRewardInterface(Player player) {
		player.getPA().showInterface(35_000);
	}
}
