package ethos.model.content.barrows;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import ethos.model.players.Coordinate;

/**
 * Locations in the Barrows maze which players can teleport to when going into the hidden tunnel.
 * 
 * @author Emiel
 *
 */
public enum RoomLocation {

	EAST(3568, 9694), NORTH(3552, 9712), NORTH_EAST(3568, 9712), NORTH_WEST(3534, 9711), WEST(3535, 9695), SOUTH(3551, 9678), SOUTH_EAST(3569, 9677), SOUTH_WEST(3534, 9677);

	private int x;
	private int y;

	RoomLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}

	private static final List<RoomLocation> SPAWN_LOCATIONS = Collections.unmodifiableList(Arrays.asList(NORTH_EAST, NORTH_WEST, SOUTH_EAST, SOUTH_WEST));

	/**
	 * Get a random location entry from the enum.
	 * 
	 * @return A random location, represented as a {@link Coordinate}.
	 */
	public static Coordinate getRandomSpawn() {
		RoomLocation randomLoc = SPAWN_LOCATIONS.get(new Random().nextInt(SPAWN_LOCATIONS.size()));
		return new Coordinate(randomLoc.x, randomLoc.y, 0);
	}
}
