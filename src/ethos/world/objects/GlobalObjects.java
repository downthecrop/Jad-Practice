package ethos.world.objects;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.stream.Collectors;

import ethos.clip.ObjectDef;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;

/**
 * 
 * @author Jason MacKeigan
 * @date Dec 18, 2014, 12:14:09 AM
 */
public class GlobalObjects {

	/**
	 * A collection of all existing objects
	 */
	Queue<GlobalObject> objects = new LinkedList<>();

	/**
	 * A collection of all objects to be removed from the game
	 */
	Queue<GlobalObject> remove = new LinkedList<>();

	/**
	 * Adds a new global object to the game world
	 * 
	 * @param object the object being added
	 */
	public void add(GlobalObject object) {
		updateObject(object, object.getObjectId());
		objects.add(object);
	}

	/**
	 * Removes a global object from the world. If the object is present in the game, we find the reference to that object and add it to the remove list.
	 * 
	 * @param id the identification value of the object
	 * @param x the x location of the object
	 * @param y the y location of the object
	 * @param height the height of the object
	 */
	public void remove(int id, int x, int y, int height) {
		Optional<GlobalObject> existing = objects.stream().filter(o -> o.getObjectId() == id && o.getX() == x && o.getY() == y && o.getHeight() == height).findFirst();
		existing.ifPresent(this::remove);
	}

	/**
	 * Attempts to remove any and all objects on a certain height that have the same object id.
	 * 
	 * @param id the id of the object
	 * @param height the height the object must be on to be removed
	 */
	public void remove(int id, int height) {
		objects.stream().filter(o -> o.getObjectId() == id && o.getHeight() == height).forEach(this::remove);
	}

	/**
	 * Removes a global object from the world based on object reference
	 * 
	 * @param object the global object
	 */
	public void remove(GlobalObject object) {
		if (!objects.contains(object)) {
			return;
		}
		updateObject(object, -1);
		remove.add(object);
	}

	public void replace(GlobalObject remove, GlobalObject add) {
		remove(remove);
		add(add);
	}

	/**
	 * Determines if an object exists in the game world
	 * 
	 * @param id the identification value of the object
	 * @param x the x location of the object
	 * @param y the y location of the object
	 * @param height the height location of the object
	 * @return true if the object exists, otherwise false.
	 */
	public boolean exists(int id, int x, int y, int height) {
		return objects.stream().anyMatch(object -> object.getObjectId() == id && object.getX() == x && object.getY() == y && object.getHeight() == height);
	}

	/**
	 * Determines if any object exists in the game world at the specified location
	 * 
	 * @param x the x location of the object
	 * @param y the y location of the object
	 * @param height the height location of the object
	 * @return true if the object exists, otherwise false.
	 */
	public boolean anyExists(int x, int y, int height) {
		return objects.stream().anyMatch(object -> object.getX() == x && object.getY() == y && object.getHeight() == height);
	}

	/**
	 * Determines if an object exists in the game world
	 * 
	 * @param id the identification value of the object
	 * @param x the x location of the object
	 * @param y the y location of the object
	 * @return true if the object exists, otherwise false.
	 */
	public boolean exists(int id, int x, int y) {
		return exists(id, x, y, 0);
	}

	public GlobalObject get(int id, int x, int y, int height) {
		Optional<GlobalObject> obj = objects.stream().filter(object -> object.getObjectId() == id && object.getX() == x && object.getY() == y && object.getHeight() == height)
				.findFirst();
		return obj.orElse(null);

	}

	/**
	 * All global objects have a unique value associated with them that is referred to as ticks remaining. Every six hundred milliseconds each object has their amount of ticks
	 * remaining reduced. Once an object has zero ticks remaining the object is replaced with it's counterpart. If an object has a tick remaining value that is negative, the object
	 * is never removed unless indicated otherwise.
	 */
	public void pulse() {
		if (objects.size() == 0) {
			return;
		}
		Queue<GlobalObject> updated = new LinkedList<>();
		GlobalObject object = null;
		objects.removeAll(remove);
		remove.clear();
		while ((object = objects.poll()) != null) {
			if (object.getTicksRemaining() < 0) {
				updated.add(object);
				continue;
			}
			object.removeTick();
			if (object.getTicksRemaining() == 0) {
				updateObject(object, object.getRestoreId());
			} else {
				updated.add(object);
			}
		}
		objects.addAll(updated);
	}

	/**
	 * Updates a single global object with a new object id in the game world for every player within a region.
	 * 
	 * @param object the new global object
	 * @param objectId the new object id
	 */
	public void updateObject(final GlobalObject object, final int objectId) {
		List<Player> players = PlayerHandler.nonNullStream().filter(Objects::nonNull)
				.filter(player -> player.distanceToPoint(object.getX(), object.getY()) <= 60 && player.heightLevel == object.getHeight()).collect(Collectors.toList());
		players.forEach(player -> player.getPA().object(objectId, object.getX(), object.getY(), object.getFace(), object.getType()));
	}

	/**
	 * Updates all region objects for a specific player
	 * 
	 * @param player the player were updating all objects for
	 */
	public void updateRegionObjects(Player player) {
		objects.stream().filter(Objects::nonNull).filter(object -> player.distanceToPoint(object.getX(), object.getY()) <= 60 && object.getHeight() == player.heightLevel)
				.forEach(object -> player.getPA().object(object.getObjectId(), object.getX(), object.getY(), object.getFace(), object.getType()));
		loadCustomObjects(player);
	}

	/**
	 * Used for spawning objects that cannot be inserted into the file
	 * 
	 * @param player the player
	 */
	private void loadCustomObjects(Player player) {
		player.getFarming().updateObjects();
		for(int i = 0; i < ObjectDef.totalObjects; i++) {
			ObjectDef def = ObjectDef.getObjectDef(i);
			boolean remove = false;
			if (def != null) {
				if (def.name != null) {
					if (def.name.toLowerCase().contains(("door")) || def.name.toLowerCase().contains(("gate"))) {
					remove = true;
					}
				}
				if(remove){
					remove(i,0);
					remove(i,1);
					remove(i,2);
					remove(i,3);
				}
			}

		}
//		if (HolidayController.CHRISTMAS.isActive()) {
//			player.getPA().checkObjectSpawn(0, 3083, 3500, 0, 10);
//			player.getPA().checkObjectSpawn(1516, 2950, 3824, 0, 10); //Benches
//			player.getPA().checkObjectSpawn(1516, 2952, 3822, 1, 10);
//			player.getPA().checkObjectSpawn(1516, 2949, 3822, 1, 10);
//			player.getPA().checkObjectSpawn(1516, 2959, 3704, 0, 10);
//			player.getPA().checkObjectSpawn(1516, 2960, 3701, 2, 10);
//			player.getPA().checkObjectSpawn(19038, 3083, 3499, 0, 10); //Tree
//			player.getPA().checkObjectSpawn(2147, 2957, 3704, 0, 10); //Ladders
//			player.getPA().checkObjectSpawn(2147, 2952, 3821, 0, 10);
//			player.getPA().checkObjectSpawn(3309, 2953, 3821, 0, 10);
//			player.getPA().checkObjectSpawn(-1, 2977, 3634, 0, 10);
//			player.getPA().checkObjectSpawn(-1, 2979, 3642, 0, 10);
//		}
//		if (HolidayController.HALLOWEEN.isActive()) {
//			player.getPA().checkObjectSpawn(298, 3088, 3497, 0, 10);
//			player.getPA().checkObjectSpawn(298, 3085, 3496, 1, 10);
//			player.getPA().checkObjectSpawn(298, 3085, 3493, 1, 10);
//			player.getPA().checkObjectSpawn(2715, 3088, 3494, 1, 10);
//			player.getPA().checkObjectSpawn(0, 3088, 3496, 0, 10);
//			player.getPA().checkObjectSpawn(0, 3089, 3496, 0, 10);
//			player.getPA().checkObjectSpawn(0, 3088, 3495, 0, 10);
//			player.getPA().checkObjectSpawn(0, 3089, 3495, 0, 10);
//		}
	}

	/**
	 * Loads all object information from a simple text file
	 * 
	 * @throws IOException an exception likely to occur from file non-existence or during reading protocol
	 */
	public void loadGlobalObjectFile() throws IOException {
		try (BufferedReader reader = new BufferedReader(new FileReader(new File("./Data/cfg/global_objects.cfg")))) {
			String line = null;
			int lineNumber = 0;
			while ((line = reader.readLine()) != null) {
				if (line.isEmpty() || line.startsWith("//")) {
					continue;
				}
				String[] data = line.split("\t");
				if (data.length != 6) {
					continue;
				}
				int id, x, y, height, face, type;
				try {
					id = Integer.parseInt(data[0]);
					x = Integer.parseInt(data[1]);
					y = Integer.parseInt(data[2]);
					height = Integer.parseInt(data[3]);
					face = Integer.parseInt(data[4]);
					type = Integer.parseInt(data[5]);
				} catch (NumberFormatException nfe) {
					System.out.println("WARNING: Unable to load object from file."+lineNumber);
					lineNumber++;
					continue;
				}
				add(new GlobalObject(id, x, y, height, face, type, -1));
				lineNumber++;
			}
		}
	}

	/**
	 * This is a convenience method that should only be referenced when testing game content on a private host. This should not be referenced during the active game.
	 * 
	 * @throws IOException
	 */
	public void reloadObjectFile(Player player) throws IOException {
		objects.clear();
		loadGlobalObjectFile();
		updateRegionObjects(player);
	}

	@Override
	public String toString() {
		List<GlobalObject> copy = new ArrayList<>(objects);
		long matches = objects.stream().filter(o -> copy.stream().anyMatch(m -> m.getX() == o.getX() && m.getY() == o.getY())).count();
		StringBuilder sb = new StringBuilder();
		sb.append("GlobalObjects: <size: " + objects.size() + ", same spot: " + matches + "> [");
		sb.append("\n");
		for (GlobalObject object : objects) {
			if (object == null) {
				continue;
			}
			sb.append("\t<id: " + object.getObjectId() + ", x: " + object.getX() + ", y: " + object.getY() + ">\n");
		}
		sb.append("]");
		return sb.toString();
	}

}
