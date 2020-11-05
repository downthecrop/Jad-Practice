package ethos.model.players.combat;

import ethos.model.entity.Entity;
import ethos.model.items.GameItem;
import ethos.model.players.Player;

/**
 * A {@link Special} object represents a weapons effect on another player. A special can have various characteristics that make up the effect.
 * 
 * @author Jason MacKeigan
 * @date Apr 4, 2015, 2015, 11:43:15 PM
 */
public abstract class Special {

	/**
	 * The total cost and required amount of the special to activate
	 */
	protected final double cost;

	/**
	 * The accuracy of the weapon
	 */
	protected final double accuracy;

	/**
	 * The amount damage is multiplied by
	 */
	protected final double damageModifier;

	/**
	 * The {@link GameItem} items that this special is activated for
	 */
	protected final int[] weapon;

	/**
	 * Creates a new special attack for a particular weapon or set of weapons
	 * 
	 * @param weapon the weapons that activate this special
	 * @param cost the cost of {@link Player#specAmount} when activated
	 * @param accuracy the accuracy of the weapon
	 * @param damageModifier the amount that any and all damage will be modified by
	 */
	public Special(double cost, double accuracy, double damageModifier, int[] weapon) {
		this.cost = cost;
		this.accuracy = accuracy;
		this.damageModifier = damageModifier;
		this.weapon = weapon;
	}

	/**
	 * Activates the special attack
	 * 
	 * @param player the player activating the special
	 * @param target the target being hit
	 * @param damage the damage being dealt
	 */
	public abstract void activate(Player player, Entity target, Damage damage);

	/**
	 * Deals damage on the target as a special
	 * 
	 * @param player the player hitting the entity
	 * @param target the target being hit
	 * @param damage the damage being modified
	 */
	public abstract void hit(Player player, Entity target, Damage damage);

	/**
	 * The weapon or weapons that activate this special
	 * 
	 * @return the weapons
	 */
	public final int[] getWeapon() {
		return weapon;
	}

	/**
	 * The accuracy of the weapon that manipulates the players probability of the damage landing, if any
	 * 
	 * @return the accuracy of
	 */
	public final double getAccuracy() {
		return accuracy;
	}

	/**
	 * The cost of the players total special amount to activate this special attack
	 * 
	 * @return the amount required
	 */
	public final double getRequiredCost() {
		return cost;
	}

	/**
	 * The amount the maximum output of a damage is multiplied by
	 * 
	 * @return the modifier
	 */
	public final double getDamageModifier() {
		return damageModifier;
	}

}
