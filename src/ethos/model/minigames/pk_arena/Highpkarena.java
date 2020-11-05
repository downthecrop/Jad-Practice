package ethos.model.minigames.pk_arena;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import ethos.model.players.Player;
import ethos.util.Misc;

/**
 *
 * @author ArrowzFtw
 */
public class Highpkarena {

    /**
     * @note States of minigames
     */
    private static final String PLAYING = "PLAYING";
    private static final String WAITING = "WAITING";
    /**
     * @note Current fight pits champion
     */
    private static String pitsChampion = "None";
    /**
     *@note Countdown for game to start
     */
    private static int highGameStartTimer = 600;
    /**
     * @note Elapsed Game start time
     */
    private static int elapsedGameTime = 0;
    private static final int END_GAME_TIME = 600;
    /*
     * @note Game started or not?
     */
    private static boolean gameStarted = false;
    /**
     * @note Stores player and State
     */
    private static Map<Player, String> playerMap = Collections.synchronizedMap(new HashMap<Player, String>());
    
    /**
     * @note Where to spawn when pits game starts
     */
    private static final int MINIGAME_START_POINT_X = 2392;
    private static final int MINIGAME_START_POINT_Y = 5139;
    /**
     * @note Exit game area
     */
    private static final int EXIT_GAME_X = 2399;
    private static final int EXIT_GAME_Y = 5169;
    /**
     * @note Exit waiting room
     */
    public static final int EXIT_WAITING_X = 2399;
    public static final int EXIT_WAITING_Y = 5177;
    /**
     * @note Waiting room coordinates
     */
    private static final int WAITING_ROOM_X = 2399;
    private static final int WAITING_ROOM_Y = 5175;

    /**
     * @return HashMap Value
     */
    public static String getState(Player c) {
        return playerMap.get(c);
    }
    
    private static final int TOKKUL_ID = 6529;

    /**
     *@note Adds player to waiting room.
     */
    public static void addPlayer(Player c) {
        playerMap.put(c, WAITING);
        c.getPA().movePlayer(WAITING_ROOM_X, WAITING_ROOM_Y, 0);
    }

    /**
     * @note Starts the game and moves players to arena
     */
    private static void enterGame(Player c) {
    	playerMap.put(c, PLAYING);
        int teleportToX = MINIGAME_START_POINT_X + Misc.random(12);
        int teleportToY = MINIGAME_START_POINT_Y + Misc.random(12);
        c.getPA().movePlayer(teleportToX, teleportToY, 0);
		c.getPA().showOption(3, 0, "Attack", 1);
		c.getPA().showOption(2, 0, "Null", 1);
    }

    /**
     * @note Removes player from pits if there in waiting or in game
     */
    public static void removePlayer(Player c, boolean forceRemove) {
        if (forceRemove) {
        	c.getPA().movePlayer(EXIT_WAITING_X, EXIT_WAITING_Y, 0);
            playerMap.remove(c);
            return;
        }
        String state = playerMap.get(c);
        if(state == null) {
        	c.getPA().movePlayer(EXIT_WAITING_X, EXIT_WAITING_Y, 0);
            return;
        }
        
        if (state.equals(PLAYING)) {
            if (getListCount(PLAYING) - 1 == 0 && !forceRemove) {
                pitsChampion = c.playerName;
                c.headIcon = 21;
                c.updateRequired = true;
                c.getItems().addItem(TOKKUL_ID, 1500+Misc.random(500));
                
            }
            c.getPA().movePlayer(EXIT_GAME_X, EXIT_GAME_Y, 0);
        } else if (state.equals(WAITING)) { 
        	c.getPA().movePlayer(EXIT_WAITING_X, EXIT_WAITING_Y, 0);
            c.getPA().walkableInterface(-1);
        }
        playerMap.remove(c);
        
        if (state.equals(PLAYING)) {
            if(!forceRemove) {
                playerMap.put(c, WAITING);
            }
        }
    }

    /**
     * @return Players playing fight pits
     */
    public static int getListCount(String state) {
    	int count = 0;
    	for (String s : playerMap.values()) {
   		 if(state == s) {
   			count++; 
   		 }
    	}
        return count;
    }

    /**
     * @note Updates players
     */
    private static void update() {
    	for (Player c : playerMap.keySet()) {
    		String status = playerMap.get(c);
    		@SuppressWarnings("unused")
            boolean updated = status == WAITING ? updateWaitingRoom(c) : updateGame(c);
    	}
    }

    /**
     * @note Updates waiting room interfaces etc.
     */
    public static boolean updateWaitingRoom(Player c) {
        c.getPA().sendFrame126("Next Game Begins In : " + highGameStartTimer, 2805);
        c.getPA().sendFrame126("Winner: " + pitsChampion, 2806);
        c.getPA().sendFrame36(560, 1);
        c.getPA().walkableInterface(2804);
        return true;
    }

    /**
     * @note Updates players in game interfaces etc.
     */
    public static boolean updateGame(Player c) {
        c.getPA().sendFrame126("Foes Remaining: " + getListCount(PLAYING), 2805);
        c.getPA().sendFrame126("Winner: " + pitsChampion, 2806);
        c.getPA().sendFrame36(560, 1);
        c.getPA().walkableInterface(2804);
        return true;
    }

    /**
     * @note Handles death and respawn rubbish.
     */
    public static void handleDeath(Player c) {
        removePlayer(c, true);
    }

    /*
     * @process 600ms Tick
     */
	static int i = 0;
    public static void process() {
        update();
        if (!gameStarted) {
            if (highGameStartTimer > 0) {
            	if (i == 1) {
            		highGameStartTimer--;
            		i = 0;
            	} else {
            		i++;
            	}
            } else if (highGameStartTimer == 0) {
                if (getListCount(WAITING) != 1) {
                    beginGame();
                }
                //highGameStartTimer = 600;
            }
        }

        if (gameStarted) {
            elapsedGameTime++;
            if (elapsedGameTime == END_GAME_TIME) {
                endGame();
                elapsedGameTime = 0;
                gameStarted = false;
                //highGameStartTimer = 80;
            }
        }
    }

    /**
     * @note Starts game for the players in waiting room
     */
    private static void beginGame() {
    	for (Player c : playerMap.keySet()) {
    		 enterGame(c);
    	}
    }

    /**
     * @note Ends game and returns player to their normal spot.
     */
    private static void endGame() {
    	for (Player c : playerMap.keySet()) {
   		 removePlayer(c, true);
    	}
    }
}