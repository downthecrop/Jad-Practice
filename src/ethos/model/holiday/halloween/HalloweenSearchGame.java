package ethos.model.holiday.halloween;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import ethos.Server;
import ethos.model.npcs.NPC;
import ethos.model.npcs.NPCHandler;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.util.Misc;
import ethos.util.Misc.Direction;
import ethos.world.objects.GlobalObject;

public class HalloweenSearchGame {

	/**
	 * The item id of the pumpkin being given to the player
	 */
	static final int PUMPKIN_ID = 1;

	/**
	 * The default static amount of pumpkins
	 */
	static final int DEFAULT_PUMPKINS = 100;

	/**
	 * The object id of the chest used
	 */
	static final int CHEST_ID = 2403;

	/**
	 * A map containing the names of every player that has stolen from the crate for this hunt, and the number of times they have stolen.
	 */
	private Map<String, Integer> pumpkinsReceived = new HashMap<>();

	/**
	 * The x and y location of the container to be searched
	 */
	private int xLocation, yLocation;

	/**
	 * The amount of pumpkins remaining in the chest
	 */
	private int remaining;

	/**
	 * The chest object that will be stolen from
	 */
	GlobalObject chest;

	/**
	 * The npc ids of the 4 ghosts that will exist in this event
	 */
	private static final int[][] GHOST_TYPES = new int[][] { { 2527, 1, 1 }, { 2528, 1, -1 }, { 2529, -1, 1 }, { 2530, -1, -1 } };

	private static final int[][] CHEST_LOCATIONS = new int[][] { { 3007, 3634 }, { 3104, 3608 }, { 3037, 3679 },
			{ 3077, 3592 }, { 3141, 3675 }, { 3165, 3736 }, { 3244, 3790 }, { 3333, 3817 }, { 3336, 3891 },
			{ 3250, 3881 }, { 3015, 3602 }, { 2951, 3821 }, { 2974, 3566 }, { 3348, 3687 }, { 3311, 3771 },
			{ 3286, 3543 }, { 3074, 3536 }, { 3288, 3944 }, { 3236, 3947 }, { 3190, 3956 }, { 3103, 3962 }, 
	};

	/**
	 * Determines if the npcs have been spawned or not.
	 */
	private boolean spawned;
	
	/**
	 * Updates the state of the search game
	 */
	public void update() {
		Server.getGlobalObjects().remove(chest);
		pumpkinsReceived.clear();
		remaining = DEFAULT_PUMPKINS;
		generateLocation();
		updateGhosts();
		Server.getGlobalObjects().add(chest = new GlobalObject(CHEST_ID, xLocation, yLocation, 0, 2, 10, -1, Integer.MAX_VALUE));
		int index = 0;
		for (int i = 0; i < CHEST_LOCATIONS.length; i++) {
			if (xLocation == CHEST_LOCATIONS[i][0] && yLocation == CHEST_LOCATIONS[i][1]) {
				index = i + 1;
			}
		}
		//PlayerHandler.executeGlobalMessage("[@red@HALLOWEEN@bla@] The ghosts have moved to location @blu@#" + index + "@bla@.");
	}
	
	public void updateZombies() {
		switch (Misc.random(3)) {
		case 0:
			NPCHandler.spawnNpc(6462, 3080, 3505, 0, 1, 90, 8, 150, 200);
			NPCHandler.spawnNpc(6462, 3077, 3508, 0, 1, 90, 8, 150, 200);
			NPCHandler.spawnNpc(6462, 3082, 3512, 0, 1, 90, 8, 150, 200);
			NPCHandler.spawnNpc(6465, 3082, 3507, 0, 1, 100, 13, 150, 200);
			NPCHandler.spawnNpc(6465, 3080, 3512, 0, 1, 100, 13, 150, 200);
			PlayerHandler.executeGlobalMessage("[@red@HALLOWEEN@bla@] The zombies are invading the edgeville general store.");
			break;
		case 1:
			NPCHandler.spawnNpc(6462, 3089, 3527, 0, 1, 90, 8, 150, 200);
			NPCHandler.spawnNpc(6462, 3083, 3525, 0, 1, 90, 8, 150, 200);
			NPCHandler.spawnNpc(6462, 3080, 3524, 0, 1, 90, 8, 150, 200);
			NPCHandler.spawnNpc(6465, 3081, 3519, 0, 1, 100, 13, 150, 200);
			NPCHandler.spawnNpc(6465, 3094, 3519, 0, 1, 100, 13, 150, 200);
			PlayerHandler.executeGlobalMessage("[@red@HALLOWEEN@bla@] The zombies are invading the edgeville wilderness ditch.");
			break;
		case 2:
			NPCHandler.spawnNpc(6462, 3097, 9870, 0, 1, 90, 8, 150, 200);
			NPCHandler.spawnNpc(6462, 3097, 9877, 0, 1, 90, 8, 150, 200);
			NPCHandler.spawnNpc(6462, 3094, 9880, 0, 1, 90, 8, 150, 200);
			NPCHandler.spawnNpc(6462, 3098, 9884, 0, 1, 90, 8, 150, 200);
			NPCHandler.spawnNpc(6462, 3095, 9888, 0, 1, 90, 8, 150, 200);
			NPCHandler.spawnNpc(6465, 3097, 9882, 0, 1, 100, 13, 150, 200);
			NPCHandler.spawnNpc(6465, 3100, 9881, 0, 1, 100, 13, 150, 200);
			NPCHandler.spawnNpc(6465, 3103, 9883, 0, 1, 100, 13, 150, 200);
			NPCHandler.spawnNpc(6465, 3101, 9885, 0, 1, 100, 13, 150, 200);
			PlayerHandler.executeGlobalMessage("[@red@HALLOWEEN@bla@] The zombies are invading the edgeville dungeon.");
			break;
		}
	}

	/**
	 * Updates all of the ghosts npcs in the game.
	 */
	private void updateGhosts() {
		if (spawned) {
			for (int[] ghost : GHOST_TYPES) {
				NPC npc = NPCHandler.getNpc(ghost[0]);
				if (Objects.isNull(npc)) {
					NPCHandler.spawnNpc(ghost[0], xLocation + ghost[1], yLocation + ghost[2], 0, 0, 100, 10, 75, 75);
				} else {
					npc.needRespawn = true;
					npc.actionTimer = 0;
					npc.absX = xLocation + ghost[1];
					npc.absY = yLocation + ghost[2];
					npc.makeX = xLocation + ghost[1];
					npc.makeY = yLocation + ghost[2];
					npc.isDead = true;
					npc.killerId = 0;
					npc.underAttack = false;
					npc.updateRequired = true;
				}
			}
		} else {
			for (int[] ghost : GHOST_TYPES) {
				NPCHandler.spawnNpc(ghost[0], xLocation + ghost[1], yLocation + ghost[2], 0, 0, 100, 10, 75, 75);
			}
			spawned = true;
		}
	}

	/**
	 * Generates a new, random location for the container, and npcs.
	 */
	private void generateLocation() {
		int oldX = xLocation;
		int oldY = yLocation;
		int attempts = 0;
		while (oldX == xLocation && oldY == yLocation && attempts++ < 50) {
			int index = Misc.random(CHEST_LOCATIONS.length - 1);
			int locX = CHEST_LOCATIONS[index][0];
			int locY = CHEST_LOCATIONS[index][1];
			if (locX != oldX && locY != oldY) {
				xLocation = locX;
				yLocation = locY;
				break;
			}
		}
	}

	/**
	 * This function is called upon when a player clicks the container. If successfull they receive a {@link HalloweenSearchGame.PUMPKIN_ID}.
	 * 
	 * @param player the player receiving the item
	 * @param x the x location of the object
	 * @param y the y location of the object
	 */
	public void receive(Player player, int x, int y) {
		if (x != xLocation || y != yLocation) {
			return;
		}
		if (player.getHolidayStages().getStage("Halloween") < 4) {
			player.sendMessage("You must talk to death before stealing from this container.");
			return;
		}
		if (remaining <= 0) {
			player.sendMessage("There are no pumpkins left in this container.");
			return;
		}
		if (pumpkinsReceived.containsKey(player.playerName) && pumpkinsReceived.get(player.playerName) + 1 >= 20) {
			player.sendMessage("You have already received 20 pumpkins from this chest.");
			return;
		}
		if (System.currentTimeMillis() - player.getLastContainerSearch() < 2000) {
			return;
		}
		if (player.getItems().freeSlots() < 1) {
			player.sendMessage("You need atleast one slot to steal from this.");
			return;
		}
		int amount = pumpkinsReceived.containsKey(player.playerName) ? pumpkinsReceived.get(player.playerName) + 1 : 0;
		player.turnPlayerTo(xLocation, yLocation);
		player.startAnimation(881);
		player.setLastContainerSearch(System.currentTimeMillis());
		player.getItems().addItem(1959, 1);
		pumpkinsReceived.put(player.playerName, amount++);
		player.sendMessage("You have taken a pumpkin from the chest.");
		if (player.getHolidayStages().getStage("Halloween") == 5) {
			if (!player.getItems().playerHasItem(12845) && !player.getItems().bankContains(12845) && !player.getItems().isWearingItem(12845)) {
				if (player.getItems().freeSlots() > 0) {
					if (Misc.random(50) == 0) {
						player.getItems().addItem(12845, 1);
						player.getHolidayStages().setStage("Halloween", 6);
						player.sendMessage("You found a 'grim reaper hood' in the chest.");
					}
				}
			}
		}
	}

	/**
	 * Operates the locator item to find the location
	 * 
	 * @param player the player
	 */
	public void operateLocator(Player player) {
		int distance = Misc.distanceToPoint(player.getX(), player.getY(), xLocation, yLocation);
		String direction = Direction.getName(Misc.direction(player.getX(), player.getY(), xLocation, yLocation));
		player.sendMessage("The chest can be found " + distance + " tiles " + direction + " from your location.");
	}

	/**
	 * Returns the base location
	 * 
	 * @return
	 */
	public Point getLocation() {
		return new Point(xLocation, yLocation);
	}

}
