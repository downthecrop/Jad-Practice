package ethos.model.holiday.christmas;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import ethos.Server;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.util.Misc;
import ethos.world.objects.GlobalObject;

public class Snowfall {

	/**
	 * The item id of the snowball being given to the player
	 */
	static final int SNOWBALL_ID = 1;

	/**
	 * The default static amount of snowballs
	 */
	static final int DEFAULT_SNOWBALL = 100;

	/**
	 * The object id of the snow used
	 */
	static final int SNOW_ID = 15615, SNOWFALL_ID = 26494;

	/**
	 * A map containing the names of every player that has gotten snowballs from the pile, and the number of times they have gotten.
	 */
	private Map<String, Integer> snowballsReceived = new HashMap<>();

	/**
	 * The x and y location of the container to be searched
	 */
	private int xLocation, yLocation;

	/**
	 * The amount of snowballs remaining in the pile
	 */
	private int remaining;

	/**
	 * The snow pile and falling snow objects
	 */
	GlobalObject snow, cornerFall1, cornerFall2, cornerFall3, cornerFall4;

	/**
	 * The snow locations
	 */
	private static final int[][] SNOW_LOCATIONS = new int[][] { 
		{ 3085, 3487 }, { 3088, 3505 }, { 3102, 3502 }, { 3073, 3504 }, { 3073, 3491 }, 
		{ 3101, 3481 }, { 3114, 3499 }, { 3107, 3493 }, { 3109, 3506 }, { 3117, 3503 }, 
		{ 3122, 3510 }, { 3127, 3517 }, { 3103, 3515 }, { 3094, 3516 }, { 3079, 3516 }, 
		{ 3073, 3517 }, };

	/**
	 * Updates the state of the snow piles
	 */
	@SuppressWarnings("unused")
	public void update() {
		Server.getGlobalObjects().remove(snow);
		Server.getGlobalObjects().remove(cornerFall1);
		Server.getGlobalObjects().remove(cornerFall2);
		Server.getGlobalObjects().remove(cornerFall3);
		Server.getGlobalObjects().remove(cornerFall4);
		snowballsReceived.clear();
		remaining = DEFAULT_SNOWBALL;
		generateLocation();
		Server.getGlobalObjects().add(snow = new GlobalObject(SNOW_ID, xLocation, yLocation, 0, 2, 10, -1, Integer.MAX_VALUE));
		Server.getGlobalObjects().add(cornerFall1 = new GlobalObject(SNOWFALL_ID, xLocation -1, yLocation -1, 0, 2, 10, -1, Integer.MAX_VALUE));
		Server.getGlobalObjects().add(cornerFall2 = new GlobalObject(SNOWFALL_ID, xLocation +1, yLocation +1, 0, 2, 10, -1, Integer.MAX_VALUE));
		Server.getGlobalObjects().add(cornerFall3 = new GlobalObject(SNOWFALL_ID, xLocation -1, yLocation +1, 0, 2, 10, -1, Integer.MAX_VALUE));
		Server.getGlobalObjects().add(cornerFall4 = new GlobalObject(SNOWFALL_ID, xLocation +1, yLocation -1, 0, 2, 10, -1, Integer.MAX_VALUE));
		int index = 0;
		for (int i = 0; i < SNOW_LOCATIONS.length; i++) {
			if (xLocation == SNOW_LOCATIONS[i][0] && yLocation == SNOW_LOCATIONS[i][1]) {
				index = i + 1;
			}
		}
		PlayerHandler.executeGlobalMessage("[@red@CHRISTMAS@bla@] Some snow has fallen within the edgeville area.");
		PlayerHandler.executeGlobalMessage("@red@[@bla@CHRISTMAS@red@]@bla@ Anti-Santa Minions are striking against edgeville.");
	}

	/**
	 * Generates a new, random location for the container.
	 */
	private void generateLocation() {
		int oldX = xLocation;
		int oldY = yLocation;
		int attempts = 0;
		while (oldX == xLocation && oldY == yLocation && attempts++ < 50) {
			int index = Misc.random(SNOW_LOCATIONS.length - 1);
			int locX = SNOW_LOCATIONS[index][0];
			int locY = SNOW_LOCATIONS[index][1];
			if (locX != oldX && locY != oldY) {
				xLocation = locX;
				yLocation = locY;
				break;
			}
		}
	}

	/**
	 * This function is called upon when a player clicks the container. If successfull they receive a {@link Snowfall.SNOWBALL_ID}.
	 * 
	 * @param player the player receiving the item
	 * @param x the x location of the object
	 * @param y the y location of the object
	 */
	public void receive(Player player, int x, int y) {
		if (x != xLocation || y != yLocation) {
			return;
		}
		if (player.getHolidayStages().getStage("Christmas") < 7) {
			player.sendMessage("You have not completed the event and can therefore not hug snowballs.");
			return;
		}
		if (remaining <= 0) {
			player.sendMessage("The snow is melting, you cannot make snowballs with this..");
			return;
		}
		if (snowballsReceived.containsKey(player.playerName) && snowballsReceived.get(player.playerName) + 1 >= 20) {
			player.sendMessage("You have already made 20 snowballs off this pile.");
			return;
		}
		if (System.currentTimeMillis() - player.getLastContainerSearch() < 2000) {
			return;
		}
		if (player.getItems().freeSlots() < 1) {
			player.sendMessage("You need atleast one slot to make a snowball.");
			return;
		}
		int amount = snowballsReceived.containsKey(player.playerName) ? snowballsReceived.get(player.playerName) + 1 : 0;
		player.turnPlayerTo(xLocation, yLocation);
		player.startAnimation(5067);
		player.setLastContainerSearch(System.currentTimeMillis());
		player.getItems().addItem(10501, 1);
		snowballsReceived.put(player.playerName, amount++);
		player.sendMessage("You successfully made a snowball.");
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
