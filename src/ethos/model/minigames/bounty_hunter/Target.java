package ethos.model.minigames.bounty_hunter;

/**
 * Represents a player target for the bounty hunter minigame.
 * 
 * @author Jason MacKeigan
 * @date Nov 12, 2014, 6:34:06 PM
 */
public class Target {
	/**
	 * The name of the player target.
	 */
	String name;

	/**
	 * Creates a new target based on the player name
	 * 
	 * @param name
	 */
	public Target(String name) {
		this.name = name;
	}

	/**
	 * Returns the name of the player target
	 * 
	 * @return the target name
	 */
	public String getName() {
		return name;
	}

}
