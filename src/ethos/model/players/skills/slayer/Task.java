package ethos.model.players.skills.slayer;

import com.google.common.base.Preconditions;

public class Task {

	/**
	 * The names of the monsters that can be killed for experience whilst on this task
	 */
	private final String[] names;

	/**
	 * The level of slayer required to obtain this npc as a task
	 */
	private final int level;

	/**
	 * The experience gained from killing a monster on task
	 */
	private final int experience;

	/**
	 * The minimum amount of the task that is obtainable
	 */
	private final int minimum;

	/**
	 * The maximum amount of the task that is obtainable
	 */
	private final int maximum;

	/**
	 * The x, y, and z value of the teleport location
	 */
	private int[] teleport;

	/**
	 * An array of locations this task can be found at on the map
	 */
	private final String[] locations;

	/**
	 * Creates a new task that is owned by each slayer master.
	 * 
	 * @param names the names of the monsters on this task
	 * @param level the level required to obtain this task
	 * @param experience the experience gained from a task monster kill
	 * @param minimum the minimum amount of the task a player can obtain
	 * @param maximum the maximum amount of the task a player can obtain
	 * @param locations the locations this task can be found
	 */
	public Task(String[] names, int level, int experience, int minimum, int maximum, String[] locations, int[] teleport) {
		Preconditions.checkArgument(names == null, "No master name, or group of names to represent the task found.");
		Preconditions.checkArgument(teleport.length < 3, "Teleport length cannot exceed 3. Plane is only 3-dimensional.");
		Preconditions.checkArgument(maximum > minimum, "Minimum cannot be less than maximum.");
		Preconditions.checkArgument(level < 99, "Level cannot exceed that of level 99.");
		this.names = names;
		this.level = level;
		this.experience = experience;
		this.minimum = minimum;
		this.maximum = maximum;
		this.locations = locations;
		this.teleport = teleport;
	}

	/**
	 * Determines if the input matches any of the task names.
	 * 
	 * @param input the name we're looking to see matches any of the others
	 * @return {@code true} if a match can be found, otherwise {@code false}
	 */
	public boolean matches(String input) {
		for (String name : names) {
			if (name.equals(input)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * The x and y coordinates of the teleport location for this non playable character.
	 * 
	 * @return the x, y, and z or -1 for each axis if the location cannot be teleported to.
	 */
	public int[] getTeleportLocation() {
		return teleport;
	}

	/**
	 * The master name of the task to differentiate it from others
	 * 
	 * @return the name
	 */
	public String getPrimaryName() {
		return names[0];
	}

	/**
	 * Each of the individual names that represent this task
	 * 
	 * @return the names
	 */
	public String[] getNames() {
		return names;
	}

	/**
	 * The level of slayer required to obtain this task
	 * 
	 * @return the slayer level required
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * The experience gained in slayer when killing a task monster
	 * 
	 * @return the experience gained
	 */
	public int getExperience() {
		return experience;
	}

	/**
	 * The absolute minimum amount of this task that can be obtained
	 * 
	 * @return the minimum amount a player can have of this task
	 */
	public int getMinimum() {
		return minimum;
	}

	/**
	 * The absolute maximum amount of this task that can be obtained
	 * 
	 * @return the maximum amount a player can have of this task
	 */
	public int getMaximum() {
		return maximum;
	}

	/**
	 * The names of the locations on the game map that the monsters can be found or located
	 * 
	 * @return the names of the locations on the map
	 */
	public String[] getLocations() {
		return locations;
	}

}
