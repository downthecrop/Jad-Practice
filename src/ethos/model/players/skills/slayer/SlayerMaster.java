package ethos.model.players.skills.slayer;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.google.common.base.Preconditions;

import ethos.model.npcs.NPC;
import ethos.util.Misc;

public class SlayerMaster {

	/**
	 * The identification value of the slayer master npc.
	 */
	private final int id;

	/**
	 * The level required to obtain a task from this master.
	 */
	private final int level;

	/**
	 * The array of {@link Task}'s that are available to obtain from this slayer master.
	 */
	private final Task[] available;

	/**
	 * An array of integer values that represent points a player is rewarded for when finishing a task.
	 */
	private final int[] pointRewards;

	/**
	 * A {@link List} of all {@link SlayerMaster}'s.
	 */
	public static final List<SlayerMaster> MASTERS = Misc.jsonArrayToList(Paths.get("Data", "json", "slayer_masters.json"), SlayerMaster[].class);

	/**
	 * Creates a new slayer master that players can interact with to obtain new tasks.
	 * 
	 * @param id the id of the npc
	 * @param level the level required to interact
	 * @param available the available tasks that can be obtained
	 */
	public SlayerMaster(int id, int level, int[] pointRewards, Task[] available) {
		Preconditions.checkArgument(pointRewards.length == 6, "The 'pointRewards' variable must have a length of 6.");
		Preconditions.checkArgument(Arrays.stream(pointRewards).allMatch(Misc::isNonNegative));
		this.id = id;
		this.level = level;
		this.pointRewards = pointRewards;
		this.available = available;
	}

	/**
	 * The identification value for the master.
	 * 
	 * @return the id value
	 */
	public int getId() {
		return id;
	}

	/**
	 * The level required to interact and obtain tasks.
	 * 
	 * @return the level of slayer required
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * An element from the array of integer values that represent an amount of slayer points a player is awarded for completing a task is returned.
	 * 
	 * @return the point reward for the given index. The index must non-negative and must have a length equal to that of ({@code pointRewards.length - 1}) or less.
	 */
	public int getPointReward(int index) {
		Preconditions.checkArgument(index > -1 && index < 6, "Index must be non-negative and less than three.");
		return pointRewards[index];
	}

	/**
	 * An array of {@link Task} objects that are available to the player from this master.
	 * 
	 * @return the available tasks
	 */
	public Task[] getAvailable() {
		return available;
	}

	/**
	 * Retrieves the slayer master for the associated identification value. The id value represents the {@link NPC} type.
	 * 
	 * @param id the {@link NPC} type that is master id
	 * @return An {@link Optional} of {@link SlayerMaster} if one exists with the same identification value, otherwise returns {@link Optional#empty()}.
	 */
	public static Optional<SlayerMaster> get(int id) {
		return MASTERS.stream().filter(master -> master.id == id).findFirst();
	}

	/**
	 * Retrieves the task from a slayer master that has the same name as the parameter provided.
	 * 
	 * @param name the name of the task
	 * @return the task that has the same name, if it exists.
	 */
	public static Optional<Task> get(String name) {
		return MASTERS.stream().flatMap(master -> Arrays.stream(master.available).filter(task -> task.getPrimaryName().equals(name))).findFirst();
	}

}
