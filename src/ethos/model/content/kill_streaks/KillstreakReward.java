package ethos.model.content.kill_streaks;

import ethos.model.players.Player;

/**
 * A reward is something obtained from a killstreak whether it is an item, points, or even coins.
 * 
 * @author Jason MacKeigan
 * @date Jan 10, 2015, 3:42:30 AM
 */
public abstract class KillstreakReward {

	/**
	 * The killstreak required to access this reward
	 */
	protected int killstreak;

	/**
	 * An object that manages giving the reward to the player
	 * 
	 * @param killstreak the killstreak required
	 */
	public KillstreakReward(int killstreak) {
		this.killstreak = killstreak;
	}

	/**
	 * Appends a new reward to the player for reaching a new killstreak milestone.
	 * 
	 * @param player the player that obtained the killstreak
	 */
	public abstract void append(Player player);

}
