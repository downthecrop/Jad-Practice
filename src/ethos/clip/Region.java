package ethos.clip;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.zip.GZIPInputStream;

import ethos.model.npcs.NPC;
import ethos.model.players.Player;
import ethos.util.Misc;

public class Region {

	private static Region[] regions;
	private int id;
	private int[][][] clips = new int[4][][];
	private int[][][] shootable = new int[4][][];
	private boolean members = false;

	/**
	 * An array of {@link WorldObject} objects that will be added after the maps have been loaded. int id, int x, int y, int height, int face
	 */
	private static final WorldObject[] EXISTANT_OBJECTS = {
			new WorldObject(25808, 2729, 3494, 0, 0), new WorldObject(25808, 2728, 3494, 0, 0),
			new WorldObject(25808, 2727, 3494, 0, 0), new WorldObject(25808, 2724, 3494, 0, 0),
			new WorldObject(25808, 2722, 3494, 0, 0), new WorldObject(25808, 2721, 3494, 0, 0),
			new WorldObject(26365, 2821, 5301, 0, 0),
			// Magebank
			new WorldObject(24101, 2535, 4719, 0, 0),
			// Lletya
			new WorldObject(24101, 2353, 3164, 0, 0),
			// Staffzone
			new WorldObject(24101, 3164, 3488, 2, 0),
			// Abyss
			new WorldObject(24101, 3039, 4834, 0, 0), new WorldObject(13642, 3039, 4831, 0, 2),
			// Edgeville
			new WorldObject(409, 3099, 3513, 0, 1),
			new WorldObject(6150, 3105, 3502, 0, 3), new WorldObject(6150, 3107, 3502, 0, 3),
			// Kbd gates
			new WorldObject(1596, 3008, 3850, 0, 1), new WorldObject(1596, 3008, 3849, 0, -1),
			// RFD
			new WorldObject(12309, 3079, 3489, 0, 2), new WorldObject(12355, 3079, 3489, 0, 2),
			// Resource area
			new WorldObject(9030, 3190, 3929, 0, 1), new WorldObject(9030, 3191, 3930, 0, 1), new WorldObject(9030, 3190, 3931, 0, 1),
			// Skilling area
			new WorldObject(24009, 3030, 3375, 0, 2), new WorldObject(1753, 3008, 3387, 0, 0),
			new WorldObject(1753, 3011, 3388, 0, 0), new WorldObject(1759, 3014, 3387, 0, 0),
			new WorldObject(1759, 3016, 3387, 0, 0), new WorldObject(1761, 3010, 3383, 0, 0),
			new WorldObject(1761, 3013, 3383, 0, 0), new WorldObject(1756, 3021, 3373, 0, 0),
			new WorldObject(1756, 3018, 3371, 0, 0), new WorldObject(1276, 3019, 3387, 0, 0),
			new WorldObject(1276, 3021, 3387, 0, 0), new WorldObject(1276, 3022, 3386, 0, 0),
			new WorldObject(24101, 3029, 3379, 0, 1), new WorldObject(7484, 3021, 3368, 0, 1),
			new WorldObject(7484, 3020, 3368, 0, 1), new WorldObject(7485, 3019, 3368, 0, 1),
			new WorldObject(7485, 3018, 3367, 0, 1), new WorldObject(7488, 3017, 3367, 0, 1),
			new WorldObject(7488, 3016, 3367, 0, 1), new WorldObject(7488, 3015, 3367, 0, 1),
			new WorldObject(7489, 3014, 3367, 0, 1), new WorldObject(7489, 3013, 3368, 0, 1),
			new WorldObject(7491, 3012, 3369, 0, 1), new WorldObject(7491, 3011, 3369, 0, 1),
			new WorldObject(9030, 3010, 3369, 0, 1), new WorldObject(7492, 3009, 3369, 0, 1),
			new WorldObject(7493, 3008, 3369, 0, 1), new WorldObject(114, 3030, 3383, 0, 1),
			new WorldObject(1751, 3007, 3376, 0, 0), new WorldObject(11730, 3003, 3384, 0, 2),
			new WorldObject(11731, 3003, 3381, 0, 0), new WorldObject(11732, 2999, 3384, 0, 2),
			new WorldObject(11734, 2999, 3381, 0, 0), new WorldObject(6150, 3027, 3375, 0, 2),
			new WorldObject(14888, 3030, 3381, 0, 3), new WorldObject(25824, 3029, 3381, 0, 1),
			new WorldObject(3840, 3001, 3372, 0, 2), new WorldObject(7674, 2999, 3373, 0, 0),
			new WorldObject(11731, 3003, 3384, 0, 0),
			//new WorldObject(27115, 1818, 3484, 0, 0),
			// Barrows
			new WorldObject(3610, 3550, 9695, 0, 0),

			// Flax
			new WorldObject(14896, 3014, 3372, 0, 0), new WorldObject(14896, 3014, 3373, 0, 0),
			new WorldObject(14896, 3014, 3374, 0, 0), new WorldObject(14896, 3013, 3371, 0, 0),
			new WorldObject(14896, 3013, 3372, 0, 0), new WorldObject(14896, 3013, 3373, 0, 0),
			new WorldObject(14896, 3013, 3374, 0, 0), new WorldObject(14896, 3013, 3375, 0, 0),
			new WorldObject(14896, 3012, 3371, 0, 0), new WorldObject(14896, 3012, 3372, 0, 0),
			new WorldObject(14896, 3012, 3373, 0, 0), new WorldObject(14896, 3012, 3374, 0, 0),
			new WorldObject(14896, 3012, 3375, 0, 0), new WorldObject(14896, 3011, 3371, 0, 0),
			new WorldObject(14896, 3011, 3372, 0, 0), new WorldObject(14896, 3011, 3373, 0, 0),
			new WorldObject(14896, 3011, 3374, 0, 0), new WorldObject(14896, 3011, 3375, 0, 0),
			new WorldObject(14896, 3010, 3372, 0, 0), new WorldObject(14896, 3010, 3373, 0, 0),
			new WorldObject(14896, 3010, 3374, 0, 0),

			//Christmas
			new WorldObject(2147, 2957, 3704, 0, 0), // Ladders
			new WorldObject(2147, 2952, 3821, 0, 0),
			new WorldObject(3309, 2953, 3821, 0, 0),
			//Halloween
			new WorldObject(298, 3088, 3497, 0, 0), //Bales
			new WorldObject(298, 3085, 3496, 0, 1),
			new WorldObject(298, 3085, 3493, 0, 1),

			//Legendary donator zone
			//Skilling
			new WorldObject(24009, 3360, 9632, 1, 3), //Furnace
			new WorldObject(170, 3369, 9640, 1, 3), //Chest
			new WorldObject(10, 3373, 9645, 1, 1), //Ladder
				//Adamant rocks
			new WorldObject(7493, 3373, 9626, 1, 1),
			new WorldObject(7493, 3374, 9627, 1, 1),
			new WorldObject(7493, 3375, 9628, 1, 1),
			new WorldObject(7493, 3376, 9629, 1, 1),
			new WorldObject(7493, 3377, 9630, 1, 1),
				//Rune rocks
			new WorldObject(7494, 3377, 9649, 1, 1),
			new WorldObject(7494, 3376, 9650, 1, 1),
			new WorldObject(7494, 3375, 9651, 1, 1),
			new WorldObject(7494, 3374, 9652, 1, 1),
			new WorldObject(7494, 3373, 9653, 1, 1),
				//Magic trees
			new WorldObject(1761, 3353, 9635, 1, 1),
			new WorldObject(1761, 3355, 9631, 1, 1),
			new WorldObject(1761, 3351, 9632, 1, 1),
				//Yew trees
			new WorldObject(1753, 3359, 9650, 1, 1),
			new WorldObject(1753, 3354, 9651, 1, 1),
			new WorldObject(1753, 3355, 9645, 1, 1),
				//Trapdoor
			new WorldObject(11803, 2126, 4919, 1, 0),
				//Teleportation device
			new WorldObject(13641, 2121, 4914, 1, 0),
			new WorldObject(13641, 2032, 4536, 1, 0),
				//Banks
			new WorldObject(24101, 3361, 9640, 1, 1),
			new WorldObject(24101, 3363, 9638, 1, 2),
			new WorldObject(24101, 3363, 9642, 1, 2),
				//Crafting guild
			new WorldObject(9030, 2940, 3288, 0, 1),
			new WorldObject(9030, 2942, 3288, 0, 1),
			new WorldObject(9030, 2941, 3289, 0, 1),
			/**
			 * Shilo
			 */

			//Bank room
			new WorldObject(24101, 2851, 2952, 0, 0), //Banks
			new WorldObject(24101, 2852, 2952, 0, 0),
			new WorldObject(24101, 2853, 2952, 0, 0),
			new WorldObject(13641, 2856, 2954, 0, 0), //Teleportation
			new WorldObject(14880, 2839, 2983, 0, 2), //Trapdoor
			new WorldObject(170, 2848, 2954, 0, 1), // Chest
			new WorldObject(14011, 2856, 2956, 0, 0), // Stall
			new WorldObject(6150, 2859, 2967, 0, 0), // Anvi

			new WorldObject(24101, 2854, 2967, 0, 0),

			//2nd bank room, other side
			new WorldObject(24101, 2834, 2981, 0, 0), //Banks
			new WorldObject(24101, 2835, 2981, 0, 0),
			new WorldObject(24101, 2836, 2981, 0, 0),
			new WorldObject(24101, 2837, 2981, 0, 0),
			new WorldObject(13641, 2837, 2991, 0, 0), //Teleportation
			new WorldObject(14880, 2859, 2954, 0, 2), //Trapdoor
			new WorldObject(170, 2833, 2991, 0, 1), // Chest

			//Mining
			new WorldObject(7494, 2824, 3003, 0, 0), //Rune
			new WorldObject(7494, 2823, 3002, 0, 0), //Rune
			new WorldObject(7494, 2822, 3001, 0, 0), //Rune
			new WorldObject(7494, 2821, 3000, 0, 0), //Rune
			new WorldObject(7494, 2820, 2999, 0, 0), //Rune
			new WorldObject(7494, 2819, 2998, 0, 0), //Rune
			new WorldObject(7493, 2821, 2996, 0, 0), //Adamant
			new WorldObject(7493, 2822, 2997, 0, 0), //Adamant
			new WorldObject(7493, 2823, 2998, 0, 0), //Adamant
			new WorldObject(7493, 2824, 2999, 0, 0), //Adamant
			new WorldObject(7493, 2825, 3000, 0, 0), //Adamant
			new WorldObject(7493, 2826, 3001, 0, 0), //Adamant
			new WorldObject(9030, 2819, 2997, 0, 0), //Gem
			new WorldObject(9030, 2825, 3003, 0, 0), //Gem

			//Crafting Guild
			new WorldObject(9030, 2940, 3290, 0, 1), //Gem
			new WorldObject(9030, 2941, 3288, 0, 1), //Gem
			new WorldObject(9030, 2942, 3289, 0, 1), //Gem
			new WorldObject(7492, 2943, 3285, 0, 1), //Mith
			new WorldObject(7492, 2943, 3284, 0, 1), //Mith
			new WorldObject(7492, 2943, 3283, 0, 1), //Mith
			new WorldObject(7492, 2943, 3282, 0, 1), //Mith

			//Woodcutting
			// Magic trees
			new WorldObject(1761, 2853, 2980, 0, 0),
			new WorldObject(1761, 2853, 2983, 0, 0),
			new WorldObject(1761, 2853, 2986, 0, 0),
			new WorldObject(1761, 2853, 2989, 0, 0),
			// Yew trees
			new WorldObject(1753, 2842, 2980, 0, 0),
			new WorldObject(1753, 2842, 2983, 0, 0),
			new WorldObject(1753, 2842, 2986, 0, 0),
			new WorldObject(1753, 2842, 2989, 0, 0),

	};

	/**
	 * A map containing each region as the key, and a Collection of real world objects as the value.
	 */
	private static HashMap<Integer, ArrayList<WorldObject>> worldObjects = new HashMap<>();

	/**
	 * Determines if an object is real or not. If the Collection of regions and real objects contains the properties passed in the parameters then the object will be determined
	 * real
	 *
	 * @param id the id of the object
	 * @param x the x coordinate of the object
	 * @param y the y coordinate of the object
	 * @param height the height of the object
	 * @return
	 */
	public static boolean isWorldObject(int id, int x, int y, int height) {
		Region region = getRegion(x, y);
		if (region == null) {
			return true;
		}
		Collection<WorldObject> regionObjects = worldObjects.get(region.id);
		if (regionObjects == null) {
			return true;
		}
		return regionObjects.stream().anyMatch(object -> object.id == id && object.x == x && object.y == y && object.height == height);
	}

	/**
	 * Determines if an object is real or not. If the Collection of regions and real objects contains the properties passed in the parameters then the object will be determined
	 * real
	 *
	 * @param x the x coordinate of the object
	 * @param y the y coordinate of the object
	 * @param height the height of the object
	 * @return
	 */
	public static boolean solidObjectExists(int x, int y, int height) {
		Region region = getRegion(x, y);
		if (region == null) {
			return false;
		}
		Collection<WorldObject> regionObjects = worldObjects.get(region.id);
		if (regionObjects == null) {
			return false;
		}
		Optional<WorldObject> exists = regionObjects.stream().filter(object -> object.x == x && object.y == y && object.height == height).findFirst();
		return exists.isPresent();
	}

	public static Optional<WorldObject> getWorldObject(int id, int x, int y, int height) {
		Region region = getRegion(x, y);
		if (region == null) {
			return Optional.empty();
		}
		Collection<WorldObject> regionObjects = worldObjects.get(region.id);
		if (regionObjects == null) {
			return Optional.empty();
		}
		Optional<WorldObject> exists = regionObjects.stream().filter(object -> object.id == id && object.x == x && object.y == y && object.height == height).findFirst();
		return exists;
	}

	public static HashMap<Integer, ArrayList<WorldObject>> getWorldObjects() {
		return worldObjects;
	}

	public int getId() {
		return id;
	}

	/**
	 * Adds a {@link WorldObject} to the {@link worldObjects} map based on the x, y, height, and identification of the object.
	 *
	 * @param id the id of the object
	 * @param x the x position of the object
	 * @param y the y position of the object
	 * @param height the height of the object
	 */
	public static void addWorldObject(int id, int x, int y, int height, int face) {
		Region region = getRegion(x, y);
		if (region == null) {
			return;
		}
		int regionId = region.id;
		if (worldObjects.containsKey(regionId)) {
			if (objectExists(regionId, id, x, y, height)) {
				return;
			}
			worldObjects.get(regionId).add(new WorldObject(id, x, y, height, face));
		} else {
			ArrayList<WorldObject> object = new ArrayList<>(1);
			object.add(new WorldObject(id, x, y, height, face));
			worldObjects.put(regionId, object);
		}
	}

	/**
	 * A convenience method for lamda expressions
	 *
	 * @param object the world object being added
	 */
	private static void addWorldObject(WorldObject object) {
		addWorldObject(object.getId(), object.getX(), object.getY(), object.getHeight(), object.getFace());
	}

	/**
	 * Determines if an object exists in a region
	 *
	 * @param region the region
	 * @param id the object id
	 * @param x` the object x pos
	 * @param y the object y pos
	 * @param height the object z pos
	 * @return true if the object exists in the region, otherwise false
	 */
	private static boolean objectExists(int region, int id, int x, int y, int height) {
		List<WorldObject> objects = worldObjects.get(region);
		for (WorldObject object : objects) {
			if (object == null) {
				continue;
			}
			if (object.getId() == id && object.getX() == x && object.getY() == y && object.getHeight() == height) {
				return true;
			}
		}
		return false;
	}

	private void addProjectileClip(int x, int y, int height, int shift) {
		int regionAbsX = (id >> 8) * 64;
		int regionAbsY = (id & 0xff) * 64;
		if (shootable[height] == null) {
			shootable[height] = new int[64][64];
		}
		shootable[height][x - regionAbsX][y - regionAbsY] |= shift;
	}

	private void removeProjectileClip(int x, int y, int height, int shift) {
		int regionAbsX = (id >> 8) * 64;
		int regionAbsY = (id & 0xff) * 64;
		if (shootable[height] == null) {
			shootable[height] = new int[64][64];
		}
		shootable[height][x - regionAbsX][y - regionAbsY] += shift;
	}

	private void addClip(int x, int y, int height, int shift) {
		int regionAbsX = (id >> 8) * 64;
		int regionAbsY = (id & 0xff) * 64;
		if (clips[height] == null) {
			clips[height] = new int[64][64];
		}
		clips[height][x - regionAbsX][y - regionAbsY] |= shift;
	}

	private int getClip(int x, int y, int height) {
		int regionAbsX = (id >> 8) * 64;
		int regionAbsY = (id & 0xff) * 64;
		if (clips[height] == null) {
			return 0;
		}
		return clips[height][x - regionAbsX][y - regionAbsY];
	}

	public static int[] getNextStep(int baseX, int baseY, int toX, int toY, int height, int xLength, int yLength) {
		int moveX = 0;
		int moveY = 0;
		if (baseX - toX > 0) {
			moveX--;
		} else if (baseX - toX < 0) {
			moveX++;
		}
		if (baseY - toY > 0) {
			moveY--;
		} else if (baseY - toY < 0) {
			moveY++;
		}
		if (canMove(baseX, baseY, baseX + moveX, baseY + moveY, height, xLength, yLength)) {
			return new int[] { baseX + moveX, baseY + moveY };
		} else if (moveX != 0 && canMove(baseX, baseY, baseX + moveX, baseY, height, xLength, yLength)) {
			return new int[] { baseX + moveX, baseY };
		} else if (moveY != 0 && canMove(baseX, baseY, baseX, baseY + moveY, height, xLength, yLength)) {
			return new int[] { baseX, baseY + moveY };
		}
		return new int[] { baseX, baseY };
	}

	public static boolean pathBlocked(Player attacker, NPC victim) {

		double offsetX = Math.abs(attacker.getX() - victim.getX());
		double offsetY = Math.abs(attacker.getY() - victim.getY());

		int distance = Misc.distanceToPoint(attacker.getX(), attacker.getY(), victim.getX(), victim.getY());

		if (distance == 0) {
			return true;
		}

		offsetX = offsetX > 0 ? offsetX / distance : 0;
		offsetY = offsetY > 0 ? offsetY / distance : 0;

		int[][] path = new int[distance][5];

		int curX = attacker.getX();
		int curY = attacker.getY();
		int next = 0;
		int nextMoveX = 0;
		int nextMoveY = 0;

		double currentTileXCount = 0.0;
		double currentTileYCount = 0.0;

		while (distance > 0) {
			distance--;
			nextMoveX = 0;
			nextMoveY = 0;
			if (curX > victim.getX()) {
				currentTileXCount += offsetX;
				if (currentTileXCount >= 1.0) {
					nextMoveX--;
					curX--;
					currentTileXCount -= offsetX;
				}
			} else if (curX < victim.getX()) {
				currentTileXCount += offsetX;
				if (currentTileXCount >= 1.0) {
					nextMoveX++;
					curX++;
					currentTileXCount -= offsetX;
				}
			}
			if (curY > victim.getY()) {
				currentTileYCount += offsetY;
				if (currentTileYCount >= 1.0) {
					nextMoveY--;
					curY--;
					currentTileYCount -= offsetY;
				}
			} else if (curY < victim.getY()) {
				currentTileYCount += offsetY;
				if (currentTileYCount >= 1.0) {
					nextMoveY++;
					curY++;
					currentTileYCount -= offsetY;
				}
			}
			path[next][0] = curX;
			path[next][1] = curY;
			path[next][2] = attacker.getHeightLevel();
			path[next][3] = nextMoveX;
			path[next][4] = nextMoveY;
			next++;
		}
		for (int i = 0; i < path.length; i++) {
			if (!getClipping(path[i][0], path[i][1], path[i][2], path[i][3], path[i][4])) {
				return true;
			}
		}
		return false;
	}

	public static boolean canMove(int x, int y, int z, int direction) {
		if (direction == 0) {
			return !blockedNorthWest(x, y, z) && !blockedNorth(x, y, z)
					&& !blockedWest(x, y, z);
		} else if (direction == 1) {
			return !blockedNorth(x, y, z);
		} else if (direction == 2) {
			return !blockedNorthEast(x, y, z) && !blockedNorth(x, y, z)
					&& !blockedEast(x, y, z);
		} else if (direction == 3) {
			return !blockedWest(x, y, z);
		} else if (direction == 4) {
			return !blockedEast(x, y, z);
		} else if (direction == 5) {
			return !blockedSouthWest(x, y, z) && !blockedSouth(x, y, z)
					&& !blockedWest(x, y, z);
		} else if (direction == 6) {
			return !blockedSouth(x, y, z);
		} else if (direction == 7) {
			return !blockedSouthEast(x, y, z) && !blockedSouth(x, y, z)
					&& !blockedEast(x, y, z);
		}
		return false;
	}

	public static boolean canShoot(int x, int y, int z, int direction) {
		if (direction == 0) {
			return !projectileBlockedNorthWest(x, y, z) && !projectileBlockedNorth(x, y, z)
					&& !projectileBlockedWest(x, y, z);
		} else if (direction == 1) {
			return !projectileBlockedNorth(x, y, z);
		} else if (direction == 2) {
			return !projectileBlockedNorthEast(x, y, z) && !projectileBlockedNorth(x, y, z)
					&& !projectileBlockedEast(x, y, z);
		} else if (direction == 3) {
			return !projectileBlockedWest(x, y, z);
		} else if (direction == 4) {
			return !projectileBlockedEast(x, y, z);
		} else if (direction == 5) {
			return !projectileBlockedSouthWest(x, y, z) && !projectileBlockedSouth(x, y, z)
					&& !projectileBlockedWest(x, y, z);
		} else if (direction == 6) {
			return !projectileBlockedSouth(x, y, z);
		} else if (direction == 7) {
			return !projectileBlockedSouthEast(x, y, z) && !projectileBlockedSouth(x, y, z)
					&& !projectileBlockedEast(x, y, z);
		}
		return false;
	}

	public static boolean projectileBlockedNorth(int x, int y, int z) {
		return (getProjectileClipping(x, y + 1, z) & 0x1280120) != 0;
	}

	public static boolean projectileBlockedEast(int x, int y, int z) {
		return (getProjectileClipping(x + 1, y, z) & 0x1280180) != 0;
	}

	public static boolean projectileBlockedSouth(int x, int y, int z) {
		return (getProjectileClipping(x, y - 1, z) & 0x1280102) != 0;
	}

	public static boolean projectileBlockedWest(int x, int y, int z) {
		return (getProjectileClipping(x - 1, y, z) & 0x1280108) != 0;
	}

	public static boolean projectileBlockedNorthEast(int x, int y, int z) {
		return (getProjectileClipping(x + 1, y + 1, z) & 0x12801e0) != 0;
	}

	public static boolean projectileBlockedNorthWest(int x, int y, int z) {
		return (getProjectileClipping(x - 1, y + 1, z) & 0x1280138) != 0;
	}

	public static boolean projectileBlockedSouthEast(int x, int y, int z) {
		return (getProjectileClipping(x + 1, y - 1, z) & 0x1280183) != 0;
	}

	public static boolean projectileBlockedSouthWest(int x, int y, int z) {
		return (getProjectileClipping(x - 1, y - 1, z) & 0x128010e) != 0;
	}

	public static boolean canMove(int startX, int startY, int endX, int endY, int height, int xLength, int yLength) {
		int diffX = endX - startX;
		int diffY = endY - startY;
		int max = Math.max(Math.abs(diffX), Math.abs(diffY));
		for (int ii = 0; ii < max; ii++) {
			int currentX = endX - diffX;
			int currentY = endY - diffY;
			for (int i = 0; i < xLength; i++) {
				for (int i2 = 0; i2 < yLength; i2++) {
					if (diffX < 0 && diffY < 0) {
						if ((getClipping(currentX + i - 1, currentY + i2 - 1, height) & 0x128010e) != 0 || (getClipping(currentX + i - 1, currentY + i2, height) & 0x1280108) != 0
								|| (getClipping(currentX + i, currentY + i2 - 1, height) & 0x1280102) != 0) {
							return false;
						}
					} else if (diffX > 0 && diffY > 0) {
						if ((getClipping(currentX + i + 1, currentY + i2 + 1, height) & 0x12801e0) != 0 || (getClipping(currentX + i + 1, currentY + i2, height) & 0x1280180) != 0
								|| (getClipping(currentX + i, currentY + i2 + 1, height) & 0x1280120) != 0) {
							return false;
						}
					} else if (diffX < 0 && diffY > 0) {
						if ((getClipping(currentX + i - 1, currentY + i2 + 1, height) & 0x1280138) != 0 || (getClipping(currentX + i - 1, currentY + i2, height) & 0x1280108) != 0
								|| (getClipping(currentX + i, currentY + i2 + 1, height) & 0x1280120) != 0) {
							return false;
						}
					} else if (diffX > 0 && diffY < 0) {
						if ((getClipping(currentX + i + 1, currentY + i2 - 1, height) & 0x1280183) != 0 || (getClipping(currentX + i + 1, currentY + i2, height) & 0x1280180) != 0
								|| (getClipping(currentX + i, currentY + i2 - 1, height) & 0x1280102) != 0) {
							return false;
						}
					} else if (diffX > 0 && diffY == 0) {
						if ((getClipping(currentX + i + 1, currentY + i2, height) & 0x1280180) != 0) {
							return false;
						}
					} else if (diffX < 0 && diffY == 0) {
						if ((getClipping(currentX + i - 1, currentY + i2, height) & 0x1280108) != 0) {
							return false;
						}
					} else if (diffX == 0 && diffY > 0) {
						if ((getClipping(currentX + i, currentY + i2 + 1, height) & 0x1280120) != 0) {
							return false;
						}
					} else if (diffX == 0 && diffY < 0) {
						if ((getClipping(currentX + i, currentY + i2 - 1, height) & 0x1280102) != 0) {
							return false;
						}
					}
				}
			}
			if (diffX < 0) {
				diffX++;
			} else if (diffX > 0) {
				diffX--;
			}
			if (diffY < 0) {
				diffY++;
			} else if (diffY > 0) {
				diffY--;
			}
		}
		return true;
	}

	public static boolean blockedNorth(int x, int y, int z) {
    	return (getClipping(x, y + 1, z) & 0x1280120) != 0;
    }
    public static boolean blockedEast(int x, int y, int z) {
    	return (getClipping(x + 1, y, z) & 0x1280180) != 0;
    }
    public static boolean blockedSouth(int x, int y, int z) {
    	return (getClipping(x, y - 1, z) & 0x1280102) != 0;
    }
    public static boolean blockedWest(int x, int y, int z) {
    	return (getClipping(x - 1, y, z) & 0x1280108) != 0;
    }
    public static boolean blockedNorthEast(int x, int y, int z) {
    	return (getClipping(x + 1, y + 1, z) & 0x12801e0) != 0;
    }
    public static boolean blockedNorthWest(int x, int y, int z) {
    	return (getClipping(x - 1, y + 1, z) & 0x1280138) != 0;
    }
    public static boolean blockedSouthEast(int x, int y, int z) {
    	return (getClipping(x + 1, y - 1, z) & 0x1280183) != 0;
    }
    public static boolean blockedSouthWest(int x, int y, int z) {
    	return (getClipping(x - 1, y - 1, z) & 0x128010e) != 0;
    }

	private static void addProjectileClipping(int x, int y, int height, int shift) {
		Region region = getRegion(x, y);
		if (region != null) {
			if (shift > 0) {
				region.addProjectileClip(x, y, height, shift);
			} else {
				region.removeProjectileClip(x, y, height, shift);
			}
		}
	}

	private static void addClipping(int x, int y, int height, int shift) {
		int regionX = x >> 3;
		int regionY = y >> 3;
		int regionId = ((regionX / 8) << 8) + (regionY / 8);
		for (Region r : regions) {
			if (r.id() == regionId) {
				r.addClip(x, y, height, shift);
				break;
			}
		}
	}

	public static Region getRegion(int x, int y) {
		int regionX = x >> 3;
		int regionY = y >> 3;
		int regionId = (regionX / 8 << 8) + regionY / 8;
		for (Region region : regions) {
			if (region.id() == regionId) {
				return region;
			}
		}
		return null;
	}

	public Region(int id, boolean members) {
		this.id = id;
		this.members = members;
	}

	public int id() {
		return id;
	}

	public boolean members() {
		return members;
	}

	public static boolean isMembers(int x, int y) {
		if (x >= 3272 && x <= 3320 && y >= 2752 && y <= 2809)
			return false;
		if (x >= 2640 && x <= 2677 && y >= 2638 && y <= 2679)
			return false;
		int regionX = x >> 3;
		int regionY = y >> 3;
		int regionId = ((regionX / 8) << 8) + (regionY / 8);
		for (Region r : regions) {
			if (r.id() == regionId) {
				return r.members();
			}
		}
		return false;
	}

	private static void setProjectileClippingForVariableObject(int x, int y, int height,
			int type, int direction, boolean flag, boolean negative) {
		if (type == 0) {
			if (direction == 0) {
				addProjectileClipping(x, y, height, negative ? -128 : 128);
				addProjectileClipping(x - 1, y, height, negative ? -8 : 8);
			} else if (direction == 1) {
				addProjectileClipping(x, y, height, negative ? -2 : 2);
				addProjectileClipping(x, y + 1, height, negative ? -32 : 32);
			} else if (direction == 2) {
				addProjectileClipping(x, y, height, negative ? -8 : 8);
				addProjectileClipping(x + 1, y, height, negative ? -128 : 128);
			} else if (direction == 3) {
				addProjectileClipping(x, y, height, negative ? -32 : 32);
				addProjectileClipping(x, y - 1, height, negative ? -2 : 2);
			}
		} else if (type == 1 || type == 3) {
			if (direction == 0) {
				addProjectileClipping(x, y, height, negative ? -1 : 1);
				addProjectileClipping(x - 1, y + 1, height, negative ? -16 : 16);//wrong method217(16, x - 1, y + 1);
			} else if (direction == 1) {
				addProjectileClipping(x, y, height, negative ? -4 : 4);
				addProjectileClipping(x + 1, y + 1, height, negative ? -64 : 64);
			} else if (direction == 2) {
				addProjectileClipping(x, y, height, negative ? -16 : 16);
				addProjectileClipping(x + 1, y - 1, height, negative ? -1 : 1);
			} else if (direction == 3) {
				addProjectileClipping(x, y, height, negative ? -64 : 64);
				addProjectileClipping(x - 1, y - 1, height, negative ? -4 : 4);
			}
		} else if (type == 2) {
			if (direction == 0) {
				addProjectileClipping(x, y, height, 130);
				addProjectileClipping(x - 1, y, height, negative ? -8 : 8);
				addProjectileClipping(x, y + 1, height, negative ? -32 : 32);
			} else if (direction == 1) {
				addProjectileClipping(x, y, height, negative ? -10 : 10);
				addProjectileClipping(x, y + 1, height, negative ? -32 : 32);
				addProjectileClipping(x + 1, y, height, negative ? -128 : 128);
			} else if (direction == 2) {
				addProjectileClipping(x, y, height, negative ? -40 : 40);
				addProjectileClipping(x + 1, y, height, negative ? -128 : 128);
				addProjectileClipping(x, y - 1, height, negative ? -2 : 2);
			} else if (direction == 3) {
				addProjectileClipping(x, y, height, negative ? -160 : 160);
				addProjectileClipping(x, y - 1, height, negative ? -2 : 2);
				addProjectileClipping(x - 1, y, height, negative ? -8 : 8);
			}
		}
		if (flag) {
			if (type == 0) {
				if (direction == 0) {
					addProjectileClipping(x, y, height, negative ? -0x10000 : 0x10000);
					addProjectileClipping(x - 1, y, height, negative ? -4096 : 4096);
				} else if (direction == 1) {
					addProjectileClipping(x, y, height, negative ? -1024 : 1024);
					addProjectileClipping(x, y + 1, height, negative ? -16384 : 16384);
				} else if (direction == 2) {
					addProjectileClipping(x, y, height, negative ? -4096 : 4096);
					addProjectileClipping(x + 1, y, height, negative ? -0x10000 : 0x10000);
				} else if (direction == 3) {
					addProjectileClipping(x, y, height, negative ? -16384 : 16384);
					addProjectileClipping(x, y - 1, height, negative ? -1024 : 1024);
				}
			}
			if (type == 1 || type == 3) {
				if (direction == 0) {
					addProjectileClipping(x, y, height, negative ? -512 : 512);
					addProjectileClipping(x - 1, y + 1, height, negative ? -8192 : 8192);
				} else if (direction == 1) {
					addProjectileClipping(x, y, height, negative ? -2048 : 2048);
					addProjectileClipping(x + 1, y + 1, height, negative ? -32768 : 32768);
				} else if (direction == 2) {
					addProjectileClipping(x, y, height, negative ? -8192 : 8192);
					addProjectileClipping(x + 1, y + 1, height, negative ? -512 : 512);
				} else if (direction == 3) {
					addProjectileClipping(x, y, height, negative ? -32768 : 32768);
					addProjectileClipping(x - 1, y - 1, height, negative ? -2048 : 2048);
				}
			} else if (type == 2) {
				if (direction == 0) {
					addProjectileClipping(x, y, height, negative ? -0x10400 : 0x10400);
					addProjectileClipping(x - 1, y, height, negative ? -4096 : 4096);
					addProjectileClipping(x, y + 1, height, negative ? -16384 : 16384);
				} else if (direction == 1) {
					addProjectileClipping(x, y, height, negative ? -5120 : 5120);
					addProjectileClipping(x, y + 1, height, negative ? -16384 : 16384);
					addProjectileClipping(x + 1, y, height, negative ? -0x10000 : 0x10000);
				} else if (direction == 2) {
					addProjectileClipping(x, y, height, negative ? -20480 : 20480);
					addProjectileClipping(x + 1, y, height, negative ? -0x10000 : 0x10000);
					addProjectileClipping(x, y - 1, height, negative ? -1024 : 1024);
				} else if (direction == 3) {
					addProjectileClipping(x, y, height, negative ? -81920 : 81920);
					addProjectileClipping(x, y - 1, height, negative ? -1024 : 1024);
					addProjectileClipping(x - 1, y, height, negative ? -4096 : 4096);
				}
			}
		}
	}

	private static void addClippingForVariableObject(int x, int y, int height, int type, int direction, boolean flag) {
		if (type == 0) {
			if (direction == 0) {
				addClipping(x, y, height, 128);
				addClipping(x - 1, y, height, 8);
			} else if (direction == 1) {
				addClipping(x, y, height, 2);
				addClipping(x, y + 1, height, 32);
			} else if (direction == 2) {
				addClipping(x, y, height, 8);
				addClipping(x + 1, y, height, 128);
			} else if (direction == 3) {
				addClipping(x, y, height, 32);
				addClipping(x, y - 1, height, 2);
			}
		} else if (type == 1 || type == 3) {
			if (direction == 0) {
				addClipping(x, y, height, 1);
				addClipping(x - 1, y, height, 16);
			} else if (direction == 1) {
				addClipping(x, y, height, 4);
				addClipping(x + 1, y + 1, height, 64);
			} else if (direction == 2) {
				addClipping(x, y, height, 16);
				addClipping(x + 1, y - 1, height, 1);
			} else if (direction == 3) {
				addClipping(x, y, height, 64);
				addClipping(x - 1, y - 1, height, 4);
			}
		} else if (type == 2) {
			if (direction == 0) {
				addClipping(x, y, height, 130);
				addClipping(x - 1, y, height, 8);
				addClipping(x, y + 1, height, 32);
			} else if (direction == 1) {
				addClipping(x, y, height, 10);
				addClipping(x, y + 1, height, 32);
				addClipping(x + 1, y, height, 128);
			} else if (direction == 2) {
				addClipping(x, y, height, 40);
				addClipping(x + 1, y, height, 128);
				addClipping(x, y - 1, height, 2);
			} else if (direction == 3) {
				addClipping(x, y, height, 160);
				addClipping(x, y - 1, height, 2);
				addClipping(x - 1, y, height, 8);
			}
		}
		if (flag) {
			if (type == 0) {
				if (direction == 0) {
					addClipping(x, y, height, 65536);
					addClipping(x - 1, y, height, 4096);
				} else if (direction == 1) {
					addClipping(x, y, height, 1024);
					addClipping(x, y + 1, height, 16384);
				} else if (direction == 2) {
					addClipping(x, y, height, 4096);
					addClipping(x + 1, y, height, 65536);
				} else if (direction == 3) {
					addClipping(x, y, height, 16384);
					addClipping(x, y - 1, height, 1024);
				}
			}
			if (type == 1 || type == 3) {
				if (direction == 0) {
					addClipping(x, y, height, 512);
					addClipping(x - 1, y + 1, height, 8192);
				} else if (direction == 1) {
					addClipping(x, y, height, 2048);
					addClipping(x + 1, y + 1, height, 32768);
				} else if (direction == 2) {
					addClipping(x, y, height, 8192);
					addClipping(x + 1, y + 1, height, 512);
				} else if (direction == 3) {
					addClipping(x, y, height, 32768);
					addClipping(x - 1, y - 1, height, 2048);
				}
			} else if (type == 2) {
				if (direction == 0) {
					addClipping(x, y, height, 66560);
					addClipping(x - 1, y, height, 4096);
					addClipping(x, y + 1, height, 16384);
				} else if (direction == 1) {
					addClipping(x, y, height, 5120);
					addClipping(x, y + 1, height, 16384);
					addClipping(x + 1, y, height, 65536);
				} else if (direction == 2) {
					addClipping(x, y, height, 20480);
					addClipping(x + 1, y, height, 65536);
					addClipping(x, y - 1, height, 1024);
				} else if (direction == 3) {
					addClipping(x, y, height, 81920);
					addClipping(x, y - 1, height, 1024);
					addClipping(x - 1, y, height, 4096);
				}
			}
		}
	}

	public static boolean isPathClear(final int x, final int y, int z, final int x2, final int y2) {
		double x3 = x;
		double y3 = y;
		double xs = x2 - x;
		double ys = y2 - y;

		while (xs >= 1 || ys >= 1 || xs <= -1 || ys <= -1) {
			xs = xs / 2;
			ys = ys / 2;
		}

		int prevX = x;
		int prevY = y;

		while (true) {
			x3 += xs;
			y3 += ys;

			if (!(prevX == (int) x3 && prevY == (int) y3)) {

				if (!canShootOver((int) x3, (int) y3, z, prevX, prevY)) {
					return false;
				}

				prevX = (int) x3;
				prevY = (int) y3;
			}

			if (x3 >= x2 && y3 >= y2) {
				return true;
			}
		}
	}

	private static boolean canShootOver(int x, int y, int z, int prevX, int prevY) {
		int dir = -1;
		int dir2 = -1;

		for (int i = 0; i < DIR.length; i++) {
			if (x + DIR[i][0] == prevX && y + DIR[i][1] == prevY) {
				dir = i;
			}

			if (prevX + DIR[i][0] == x && prevY + DIR[i][1] == y) {
				dir2 = i;
			}
		}

		if (dir == -1 || dir2 == -1) {
			return false;
		}
		Region region2 = Region.getRegion(prevX, prevY);
		if (canMove(x, y, prevX, prevY, z, x - prevX, y - prevY)) {
			return true;
		}
		return (region2.getClip(x, y, z) & 0x20000) == 0;
	}

	private static final int[][] DIR = { { -1, 1 }, { 0, 1 }, { 1, 1 }, { -1, 0 }, { 1, 0 }, { -1, -1 }, { 0, -1 }, { 1, -1 } };

	private static void addProjectileClippingForSolidObject(int x, int y, int height,
			int xLength, int yLength, boolean flag) {
		int clipping = 256;
		if (flag) {
			clipping += 0x20000;
		}
		for (int i = x; i < x + xLength; i++) {
			for (int i2 = y; i2 < y + yLength; i2++) {
				addProjectileClipping(i, i2, height, clipping);
			}
		}
	}

	private static void addClippingForSolidObject(int x, int y, int height, int xLength, int yLength, boolean flag) {
		int clipping = 256;
		if (flag) {
			clipping += 0x20000;
		}
		for (int i = x; i < x + xLength; i++) {
			for (int i2 = y; i2 < y + yLength; i2++) {
				addClipping(i, i2, height, clipping);
			}
		}
	}

	public static void addObject(int objectId, int x, int y, int height, int type, int direction) {
		ObjectDef def = ObjectDef.getObjectDef(objectId);
		if (def == null) {
			return;
		}
		int xLength;
		int yLength;
		if (direction != 1 && direction != 3) {
			xLength = def.xLength();
			yLength = def.yLength();
		} else {
			xLength = def.yLength();
			yLength = def.xLength();
		}
		if (type == 22) {
			if (def.hasActions() && def.aBoolean767()) {
				addClipping(x, y, height, 0x200000);
				if (def.aBoolean757) {
					addProjectileClipping(x, y, height, 0x200000);
				}
			}
		} else if (type >= 9) {
			if (def.aBoolean767()) {
				addClippingForSolidObject(x, y, height, xLength, yLength, def.solid());
				if (def.aBoolean757) {
					addProjectileClippingForSolidObject(x, y, height, xLength, yLength, true);
				}
			}
		} else if (type >= 0 && type <= 3) {
			if (def.aBoolean767()) {
				addClippingForVariableObject(x, y, height, type, direction, def.solid());
				if (def.aBoolean757) {
					setProjectileClippingForVariableObject(x, y, height, type, direction, def.solid(), false);
				}
			}
		}
	}

	public static int getProjectileClipping(int x, int y, int height) {
		if (height > 3)
			height = 0;
		int regionX = x >> 3;
		int regionY = y >> 3;
		int regionId = ((regionX / 8) << 8) + (regionY / 8);
		for (Region r : regions) {
			if (r.id() == regionId) {
				return r.getProjectileClip(x, y, height);
			}
		}
		return 0;
	}

	private int getProjectileClip(int x, int y, int height) {
		if (height > 3) height = height % 4;
		int regionAbsX = (id >> 8) * 64;
		int regionAbsY = (id & 0xff) * 64;
		if (shootable[height] == null) {
			return 0;
		}
		return shootable[height][x - regionAbsX][y - regionAbsY];
	}

	public static int getClipping(int x, int y, int height) {
		if (height > 3)
			height = 0;
		int regionX = x >> 3;
		int regionY = y >> 3;
		int regionId = ((regionX / 8) << 8) + (regionY / 8);
		for (Region r : regions) {
			if (r.id() == regionId) {
				return r.getClip(x, y, height);
			}
		}
		return 0;
	}

	public static boolean getClipping(int x, int y, int height, int moveTypeX, int moveTypeY) {
		try {
			if (height > 3)
				height = 0;
			int checkX = (x + moveTypeX);
			int checkY = (y + moveTypeY);
			if (moveTypeX == -1 && moveTypeY == 0)
				return (getClipping(x, y, height) & 0x1280108) == 0;
			else if (moveTypeX == 1 && moveTypeY == 0)
				return (getClipping(x, y, height) & 0x1280180) == 0;
			else if (moveTypeX == 0 && moveTypeY == -1)
				return (getClipping(x, y, height) & 0x1280102) == 0;
			else if (moveTypeX == 0 && moveTypeY == 1)
				return (getClipping(x, y, height) & 0x1280120) == 0;
			else if (moveTypeX == -1 && moveTypeY == -1)
				return ((getClipping(x, y, height) & 0x128010e) == 0 && (getClipping(checkX - 1, checkY, height) & 0x1280108) == 0
						&& (getClipping(checkX - 1, checkY, height) & 0x1280102) == 0);
			else if (moveTypeX == 1 && moveTypeY == -1)
				return ((getClipping(x, y, height) & 0x1280183) == 0 && (getClipping(checkX + 1, checkY, height) & 0x1280180) == 0
						&& (getClipping(checkX, checkY - 1, height) & 0x1280102) == 0);
			else if (moveTypeX == -1 && moveTypeY == 1)
				return ((getClipping(x, y, height) & 0x1280138) == 0 && (getClipping(checkX - 1, checkY, height) & 0x1280108) == 0
						&& (getClipping(checkX, checkY + 1, height) & 0x1280120) == 0);
			else if (moveTypeX == 1 && moveTypeY == 1)
				return ((getClipping(x, y, height) & 0x12801e0) == 0 && (getClipping(checkX + 1, checkY, height) & 0x1280180) == 0
						&& (getClipping(checkX, checkY + 1, height) & 0x1280120) == 0);
			else {
				// System.out.println("[FATAL ERROR]: At getClipping: " + x + ", "
				// + y + ", " + height + ", " + moveTypeX + ", "
				// + moveTypeY);
				return false;
			}
		} catch (Exception e) {
			return true;
		}
	}

	public static void load() {
		try {
			System.out.println("Loading mapdata..  this can take around 20 seconds.");
			File f = new File("./Data/world/map_index.dat");
			byte[] buffer = new byte[(int) f.length()];
			DataInputStream dis = new DataInputStream(new FileInputStream(f));
			dis.readFully(buffer);
			dis.close();
			ByteStream in = new ByteStream(buffer);
			int size = in.readUnsignedWord();
			int[] regionIds = new int[size];
			int[] mapGroundFileIds = new int[size];
			int[] mapObjectsFileIds = new int[size];
			for (int i = 0; i < size; i++) {
				regionIds[i] = in.readUnsignedWord();
				mapGroundFileIds[i] = in.readUnsignedWord();
				mapObjectsFileIds[i] = in.readUnsignedWord();
			}
			regions = new Region[size];
			for (int i = 0; i < size; i++) {
				regions[i] = new Region(regionIds[i], false);
			}
			for (int i = 0; i < size; i++) {
				byte[] file1 = getBuffer(new File("./Data/world/map/" + mapObjectsFileIds[i] + ".gz"));
				byte[] file2 = getBuffer(new File("./Data/world/map/" + mapGroundFileIds[i] + ".gz"));

				if (file1 == null || file2 == null) {
					continue;
				}
				try {
					loadMaps(regionIds[i], new ByteStream(file1), new ByteStream(file2));
				} catch (Exception e) {
					//System.out.println("Error loading map region: " + regionIds[i] + ", objectFile: " + mapObjectsFileIds[i] + ", floorFile: " + mapGroundFileIds[i]);
					//e.printStackTrace();
				}
			}
			Arrays.asList(EXISTANT_OBJECTS).forEach(o -> addWorldObject(o));
			int numberOfRegions = worldObjects.size();
			long totalObjects = worldObjects.values().stream().mapToInt(list -> list.size()).sum();
			Misc.println("Loaded region configuration: " + numberOfRegions + " regions and " + totalObjects + " total objects.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void loadMaps(int regionId, ByteStream str1, ByteStream str2) {
		int absX = (regionId >> 8) * 64;
		int absY = (regionId & 0xff) * 64;
		int[][][] someArray = new int[4][64][64];
		for (int i = 0; i < 4; i++) {
			for (int i2 = 0; i2 < 64; i2++) {
				for (int i3 = 0; i3 < 64; i3++) {
					while (true) {
						int v = str2.getUByte();
						if (v == 0) {
							break;
						} else if (v == 1) {
							str2.skip(1);
							break;
						} else if (v <= 49) {
							str2.skip(1);
						} else if (v <= 81) {
							someArray[i][i2][i3] = v - 49;
						}
					}
				}
			}
		}
		for (int i = 0; i < 4; i++) {
			for (int i2 = 0; i2 < 64; i2++) {
				for (int i3 = 0; i3 < 64; i3++) {
					if ((someArray[i][i2][i3] & 1) == 1) {
						int height = i;
						if ((someArray[1][i2][i3] & 2) == 2) {
							height--;
						}
						if (height >= 0 && height <= 3) {
							addClipping(absX + i2, absY + i3, height, 0x200000);
						}
					}
				}
			}
		}
		int objectId = -1;
		int incr;
		while ((incr = str1.getUSmart()) != 0) {
			objectId += incr;
			int location = 0;
			int incr2;
			while ((incr2 = str1.getUSmart()) != 0) {
				location += incr2 - 1;
				int localX = (location >> 6 & 0x3f);
				int localY = (location & 0x3f);
				int height = location >> 12;
				int objectData = str1.getUByte();
				int type = objectData >> 2;
				int direction = objectData & 0x3;
				if (localX < 0 || localX >= 64 || localY < 0 || localY >= 64) {
					continue;
				}
				if ((someArray[1][localX][localY] & 2) == 2) {
					height--;
				}
				if (height >= 0 && height <= 3) {
					//System.out.println("Adding "+objectId+" at "+ absX + localX);
					addObject(objectId, absX + localX, absY + localY, height, type, direction);
					addWorldObject(objectId, absX + localX, absY + localY, height, direction);
				}
			}
		}
	}

	public static byte[] getBuffer(File f) throws Exception {
		if (!f.exists()) {
			return null;
		}
		byte[] buffer = new byte[(int) f.length()];
		try (DataInputStream dis = new DataInputStream(new FileInputStream(f))) {
			dis.readFully(buffer);
		} catch (Exception e) {
			e.printStackTrace();
		}
		byte[] gzipInputBuffer = new byte[999999];
		int bufferlength = 0;
		try (GZIPInputStream gzip = new GZIPInputStream(new ByteArrayInputStream(buffer))) {
			do {
				if (bufferlength == gzipInputBuffer.length) {
					System.out.println("Error inflating data.\nGZIP buffer overflow.");
					break;
				}
				int readByte = gzip.read(gzipInputBuffer, bufferlength, gzipInputBuffer.length - bufferlength);
				if (readByte == -1)
					break;
				bufferlength += readByte;
			} while (true);
			byte[] inflated = new byte[bufferlength];
			System.arraycopy(gzipInputBuffer, 0, inflated, 0, bufferlength);
			buffer = inflated;
			if (buffer.length < 10) {
				return null;
			}
		}
		return buffer;
	}

	public static boolean blockedNorthNPC(int x, int y, int z, int size) {
		if (size > 0) {
			for (int i = 0; i < size; i++) {
				boolean clipped = (getClipping(x + i, y + size + 1, z) != 0);
				if (clipped) {
					return true;
				}
			}
		}
		return (getClipping(x, y + 1, z) != 0);
	}

	public static boolean blockedEastNPC(int x, int y, int z, int size) {
		if (size > 0) {
			for (int i = 0; i < size; i++) {
				boolean clipped = (getClipping(x + size + 1, y + i, z) != 0);
				if (clipped) {
					return true;
				}
			}
		}
		return (getClipping(x + 1, y, z) != 0);
	}

	public static boolean blockedSouthNPC(int x, int y, int z, int size) {
		if (size > 0) {
			for (int i = 0; i < size; i++) {
				boolean clipped = (getClipping(x + i, y - 1, z) != 0);
				if (clipped) {
					return true;
				}
			}
		}
		return (getClipping(x, y - 1, z) != 0);
	}

	public static boolean blockedWestNPC(int x, int y, int z, int size) {
		if (size > 0) {
			for (int i = 0; i < size; i++) {
				boolean clipped = (getClipping(x - 1, y + i, z) != 0);
				if (clipped) {
					return true;
				}
			}
		}
		return (getClipping(x - 1, y, z) != 0);
	}

	public static boolean blockedNorthEastNPC(int x, int y, int z, int size) {
		if (size > 0) {
			for (int i = 0; i < size; i++) {
				boolean clipped = (getClipping(x + size + 1, y + i + 1, z) != 0);
				boolean clipped2 = (getClipping(x + i + 1, y + size + 1, z) != 0);
				if (clipped || clipped2) {
					return true;
				}
			}
		}
		return (getClipping(x + 1, y + 1, z) != 0);
	}

	public static boolean blockedNorthWestNPC(int x, int y, int z, int size) {
		if (size > 0) {
			for (int i = 0; i < size; i++) {
				boolean clipped = (getClipping(x - 1, y + i + 1, z) != 0);
				boolean clipped2 = (getClipping(x + i - 1, y + size + 1, z) != 0);
				if (clipped || clipped2) {
					return true;
				}
			}
		}
		return (getClipping(x - 1, y + 1, z) != 0);
	}

	public static boolean blockedSouthEastNPC(int x, int y, int z, int size) {
		if (size > 0) {
			for (int i = 0; i < size; i++) {
				boolean clipped = (getClipping(x + size + 1, y + i - 1, z) != 0);
				boolean clipped2 = (getClipping(x + i + 1, y - 1, z) != 0);
				if (clipped || clipped2) {
					return true;
				}
			}
		}
		return (getClipping(x + 1, y - 1, z) != 0);
	}

	public static boolean blockedSouthWestNPC(int x, int y, int z, int size) {
		if (size > 0) {
			for (int i = 0; i < size; i++) {
				boolean clipped = (getClipping(x - 1, y + i - 1, z) != 0);
				boolean clipped2 = (getClipping(x + i - 1, y - 1, z) != 0);
				if (clipped || clipped2) {
					return true;
				}
			}
		}
		return (getClipping(x - 1, y - 1, z) != 0);
	}
	
}