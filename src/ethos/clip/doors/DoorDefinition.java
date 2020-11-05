package ethos.clip.doors;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import ethos.model.players.Coordinate;

public class DoorDefinition {

	private static Map<Coordinate, DoorDefinition> definitions = new HashMap<>();

	public static void load() throws IOException {
		List<DoorDefinition> list = new Gson().fromJson(FileUtils.readFileToString(new File("./Data/json/door_definitions.json")), new TypeToken<List<DoorDefinition>>() {
		}.getType());

		list.stream().filter(Objects::nonNull).forEach(door -> definitions.put(door.getCoordinate(), door));

		System.out.println("Loaded " + definitions.size() + " door definitions.");
	}

	/**
	 * Get a door definition by Coordinate.
	 *
	 * @param Coordiante The coordinate of the door.
	 * @return The door definition.
	 */
	public static DoorDefinition forCoordinate(Coordinate coordinate) {
		return definitions.get(coordinate);
	}

	/**
	 * Get a door definition by Coordinate.
	 *
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 * @return The door definition.
	 */
	public static DoorDefinition forCoordinate(int x, int y) {
		return definitions.get(new Coordinate(x, y));
	}

	/**
	 * Get a door definition by Coordinate.
	 *
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 * @param y The height coordinate.
	 * @return The door definition.
	 */
	public static DoorDefinition forCoordinate(int x, int y, int h) {
		return definitions.get(new Coordinate(x, y, h));
	}

	/**
	 * A map of all definitions.
	 * 
	 * @return the map.
	 */
	public static Map<Coordinate, DoorDefinition> getDefinitions() {
		return definitions;
	}

	private int id;

	private int x;

	private int y;

	private int h;

	private int face;

	public int getId() {
		return id;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getH() {
		return h;
	}

	public int getFace() {
		return face;
	}

	public Coordinate getCoordinate() {
		return new Coordinate(x, y, h);
	}

	@Override
	public String toString() {
		return "DoorDefinition [id=" + id + ", x=" + x + ", y=" + y + ", h=" + h + ", face=" + face + "]";
	}

}
