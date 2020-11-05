package ethos.model.content.achievement;

import ethos.model.players.Player;

/**
 * 
 * @author Jason http://www.rune-server.org/members/jason
 * @date Mar 26, 2014
 */
public abstract class AchievementRequirement {
	/**
	 * 
	 * @param player
	 * @return The state at which the Player object has the ability or pre-requisites
	 */
	abstract boolean isAble(Player player);
}
