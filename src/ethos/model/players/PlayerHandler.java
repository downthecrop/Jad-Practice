package ethos.model.players;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import ethos.Config;
import ethos.Server;
import ethos.ServerState;
import ethos.model.multiplayer_session.MultiplayerSessionFinalizeType;
import ethos.model.multiplayer_session.MultiplayerSessionStage;
import ethos.model.multiplayer_session.MultiplayerSessionType;
import ethos.model.multiplayer_session.duel.DuelSession;
import ethos.model.npcs.NPCHandler;
import ethos.util.Stream;

public class PlayerHandler {

	public static Object lock = new Object();
	public static Player players[] = new Player[Config.MAX_PLAYERS];
	public static String messageToAll = "";

	public static boolean updateAnnounced;
	public static boolean updateRunning;
	public static int updateSeconds;
	public static long updateStartTime;
	private boolean kickAllPlayers = false;

	public static PlayerSave save;

	public static Player getPlayer(String name) {
		for (int d = 0; d < Config.MAX_PLAYERS; d++) {
			if (PlayerHandler.players[d] != null) {
				Player o = PlayerHandler.players[d];
				if (o.playerName.equalsIgnoreCase(name)) {
					return o;
				}
			}
		}
		return null;
	}

	public static Optional<Player> getOptionalPlayer(String name) {
		return getPlayers().stream().filter(Objects::nonNull).filter(client -> client.playerName.equalsIgnoreCase(name)).findFirst();
	}

	public static Player getPlayerByLongName(long name) {
		for (int i = 0; i < Config.MAX_PLAYERS; i++) {
			if (PlayerHandler.players[i] == null)
				continue;
			if (PlayerHandler.players[i].getNameAsLong() == name)
				return PlayerHandler.players[i];
		}
		return null;
	}

	public static int getPlayerID(String playerName) {
		for (int i = 0; i < Config.MAX_PLAYERS; i++) {
			if (PlayerHandler.players[i] != null) {
				Player p = PlayerHandler.players[i];
				if (p.playerName.equalsIgnoreCase(playerName)) {
					return i;
				}
			}
		}
		return -1;
	}

	/**
	 * The next available slot between 1 and {@link Config#MAX_PLAYERS}.
	 * 
	 * @return the next slot
	 */
	public int nextSlot() {
		for (int index = 1; index < Config.MAX_PLAYERS; index++) {
			if (players[index] == null) {
				return index;
			}
		}
		return -1;
	}

	public void add(Player player) {
		players[player.getIndex()] = player;
		players[player.getIndex()].isActive = true;
		if (Config.SERVER_STATE == ServerState.PUBLIC_PRIMARY) {
			//TODO login log addition
		}
	}

	public static int getPlayerCount() {
		int count = 0;
		for (int i = 0; i < players.length; i++) {
			if (players[i] != null) {
				count++;
			}
		}
		return (count + Config.PLAYERMODIFIER);
	}

	public static boolean isPlayerOn(String playerName) {
		for (int i = 0; i < Config.MAX_PLAYERS; i++) {
			if (players[i] != null) {
				if (players[i].playerName.equalsIgnoreCase(playerName)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Create an int array of the specified length, containing all values between 0 and length once at random positions.
	 * 
	 * @param length The size of the array.
	 * @return The randomly shuffled array.
	 */
	private int[] shuffledList(int length) {
		int[] array = new int[length];
		for (int i = 0; i < array.length; i++) {
			array[i] = i;
		}
		Random rand = new Random();
		for (int i = 0; i < array.length; i++) {
			int index = rand.nextInt(i + 1);
			int a = array[index];
			array[index] = array[i];
			array[i] = a;
		}
		return array;
	}

	public void process() {
		synchronized (lock) {
			if (kickAllPlayers) {
				for (int i = 1; i < Config.MAX_PLAYERS; i++) {
					if (players[i] != null) {
						players[i].disconnected = true;
					}
				}
			}
			int[] randomOrder = shuffledList(Config.MAX_PLAYERS);
			for (int i = 0; i < Config.MAX_PLAYERS; i++) {
				if (players[randomOrder[i]] == null || !players[randomOrder[i]].isActive || !players[randomOrder[i]].initialized)
					continue;
				try {
					Player player = players[randomOrder[i]];
					if (players[randomOrder[i]].disconnected
							&& (System.currentTimeMillis() - players[randomOrder[i]].logoutDelay > 90000 || players[randomOrder[i]].properLogout || kickAllPlayers
									|| Boundary.isIn(player, Boundary.DUEL_ARENA) && Server.getMultiplayerSessionListener().inSession(player, MultiplayerSessionType.DUEL))) {
						if (Server.getMultiplayerSessionListener().inSession(player, MultiplayerSessionType.TRADE)) {
							Server.getMultiplayerSessionListener().finish(player, MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
						}
						DuelSession duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(players[randomOrder[i]], MultiplayerSessionType.DUEL);
						if (Objects.nonNull(duelSession) && duelSession.getStage().getStage() > MultiplayerSessionStage.REQUEST) {
							if (duelSession.getStage().getStage() < MultiplayerSessionStage.FURTHER_INTERATION) {
								duelSession.finish(MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
							} else {
								Player winner = duelSession.getOther(players[randomOrder[i]]);
								duelSession.setWinner(winner);
								duelSession.finish(MultiplayerSessionFinalizeType.GIVE_ITEMS);
							}
						}
						if (Config.BOUNTY_HUNTER_ACTIVE) {
							if (player.getBH().hasTarget()) {
								player.getBH().setWarnings(player.getBH().getWarnings() + 1);
							}
						}
						Player o = PlayerHandler.players[randomOrder[i]];
						if (PlayerSave.saveGame(o)) {
							System.out.println("Game saved for player " + players[randomOrder[i]].playerName);
						} else {
							System.out.println("Could not save for " + players[randomOrder[i]].playerName);
						}
						removePlayer(players[randomOrder[i]]);
						players[randomOrder[i]] = null;
						continue;
					}
					players[randomOrder[i]].preProcessing();
					players[randomOrder[i]].processQueuedPackets();
					players[randomOrder[i]].process();
					players[randomOrder[i]].postProcessing();
					players[randomOrder[i]].getNextPlayerMovement();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			for (int i = 0; i < Config.MAX_PLAYERS; i++) {
				if (players[randomOrder[i]] == null || !players[randomOrder[i]].isActive || !players[randomOrder[i]].initialized)
					continue;
				try {
					players[randomOrder[i]].update();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			for (int i = 0; i < Config.MAX_PLAYERS; i++) {
				if (players[randomOrder[i]] == null || !players[randomOrder[i]].isActive || !players[randomOrder[i]].initialized)
					continue;
				try {
					players[randomOrder[i]].clearUpdateFlags();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (updateRunning && !updateAnnounced) {
				updateAnnounced = true;
				Server.UpdateServer = true;
			}
			if (updateRunning && (System.currentTimeMillis() - updateStartTime > (updateSeconds * 1000))) {
				kickAllPlayers = true;
			}
		}

	}

	public void updateNPC(Player plr, Stream str) {
		// synchronized(plr) {
		updateBlock.currentOffset = 0;

		str.createFrameVarSizeWord(65);
		str.initBitAccess();

		str.writeBits(8, plr.npcListSize);
		int size = plr.npcListSize;
		plr.npcListSize = 0;
		for (int i = 0; i < size; i++) {
			if (plr.rebuildNPCList == false && plr.withinDistance(plr.npcList[i]) == true && !plr.npcList[i].teleporting) {
				plr.npcList[i].updateNPCMovement(str);
				plr.npcList[i].appendNPCUpdateBlock(updateBlock);
				plr.npcList[plr.npcListSize++] = plr.npcList[i];
			} else {
				int id = plr.npcList[i].getIndex();
				plr.npcInListBitmap[id >> 3] &= ~(1 << (id & 7));
				str.writeBits(1, 1);
				str.writeBits(2, 3);
			}
		}

		for (int i = 0; i < NPCHandler.maxNPCs; i++) {
			if (NPCHandler.npcs[i] != null) {
				int id = NPCHandler.npcs[i].getIndex();
				if (plr.rebuildNPCList == false && (plr.npcInListBitmap[id >> 3] & (1 << (id & 7))) != 0) {

				} else if (plr.withinDistance(NPCHandler.npcs[i]) == false) {

				} else {
					plr.addNewNPC(NPCHandler.npcs[i], str, updateBlock);
				}
			}
		}

		plr.rebuildNPCList = false;

		if (updateBlock.currentOffset > 0) {
			str.writeBits(14, 16383);
			str.finishBitAccess();
			str.writeBytes(updateBlock.buffer, updateBlock.currentOffset, 0);
		} else {
			str.finishBitAccess();
		}
		str.endFrameVarSizeWord();
		// }
	}

	private Stream updateBlock = new Stream(new byte[Config.BUFFER_SIZE]);

	public void updatePlayer(Player plr, Stream str) {
		updateBlock.currentOffset = 0;
		if (updateRunning && !updateAnnounced) {
			str.createFrame(114);
			str.writeWordBigEndian(updateSeconds * 50 / 30);
		}
		plr.updateThisPlayerMovement(str);
		boolean saveChatTextUpdate = plr.isChatTextUpdateRequired();
		plr.setChatTextUpdateRequired(false);
		plr.appendPlayerUpdateBlock(updateBlock);
		plr.setChatTextUpdateRequired(saveChatTextUpdate);
		str.writeBits(8, plr.playerListSize);
		int size = plr.playerListSize;
		if (size >= 79) {
			size = 79;
		}
		plr.playerListSize = 0;
		for (int i = 0; i < size; i++) {
			if (!plr.didTeleport && !plr.playerList[i].didTeleport && plr.withinDistance(plr.playerList[i])) {
				plr.playerList[i].updatePlayerMovement(str);
				plr.playerList[i].appendPlayerUpdateBlock(updateBlock);
				plr.playerList[plr.playerListSize++] = plr.playerList[i];
			} else {
				int id = plr.playerList[i].getIndex();
				plr.playerInListBitmap[id >> 3] &= ~(1 << (id & 7));
				str.writeBits(1, 1);
				str.writeBits(2, 3);
			}
		}

		for (int i = 0; i < Config.MAX_PLAYERS; i++) {
			if (players[i] == null || !players[i].isActive || players[i] == plr) {
				continue;
			}
			int id = players[i].getIndex();

			if ((plr.playerInListBitmap[id >> 3] & (1 << (id & 7))) != 0) {
				continue;
			}
			if (!plr.withinDistance(players[i])) {
				continue;
			}
			plr.addNewPlayer(players[i], str, updateBlock);
		}

		if (updateBlock.currentOffset > 0) {
			str.writeBits(11, 2047);
			str.finishBitAccess();
			str.writeBytes(updateBlock.buffer, updateBlock.currentOffset, 0);
		} else {
			str.finishBitAccess();
		}

		str.endFrameVarSizeWord();
	}

	private void removePlayer(Player plr) {
		plr.destruct();
	}

	public static void executeGlobalMessage(String message) {
		Player[] clients = new Player[players.length];
		System.arraycopy(players, 0, clients, 0, players.length);
		Arrays.asList(clients).stream().filter(Objects::nonNull).forEach(player -> player.sendMessage(message));
	}

	public static void sendMessage(String message, List<Player> players) {
		for (Player player : players) {
			if (Objects.isNull(player)) {
				continue;
			}
			player.sendMessage(message);
		}
	}

	//@Deprecated
	public static List<Player> getPlayers() {
		Player[] clients = new Player[players.length];
		System.arraycopy(players, 0, clients, 0, players.length);
		return Arrays.asList(clients).stream().filter(Objects::nonNull).collect(Collectors.toList());
	}

	@Deprecated
	public static List<Player> getPlayerList() {
		return Arrays.asList(players).stream().filter(Objects::nonNull).collect(Collectors.toList());
	}

	public static java.util.stream.Stream<Player> stream() {
		return Arrays.stream(players);
	}

	public static java.util.stream.Stream<Player> nonNullStream() {
		return Arrays.stream(players).filter(Objects::nonNull);
	}

}
