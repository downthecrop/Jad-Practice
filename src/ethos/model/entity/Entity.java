package ethos.model.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import ethos.model.npcs.NPC;
import ethos.model.players.Boundary;
import ethos.model.players.Player;
import ethos.model.players.combat.Damage;
import ethos.model.players.combat.Hitmark;
import ethos.util.Stream;

/**
 * Represents a game-world presence that exists among others of the same nature.
 * The objective is to allow multiple entities to share common similarities and
 * allow simple but effective reference in doing so.
 * 
 * @author Jason MacKeigan
 * @date Mar 27, 2015, 2015, 8:00:45 PM
 */
public abstract class Entity {

	/**
	 * The visual alias or name of the entity that describes and separates this
	 * entity from others of the same nature.
	 */
	protected String name;

	/**
	 * The absolute x-position of the entity
	 */
	protected int x;

	/**
	 * The absolute y-position of the entity
	 */
	protected int y;

	/**
	 * The absolute height of the entity
	 */
	public int height;

	/**
	 * The index in the list that the player resides
	 */
	protected final int index;

	/**
	 * A mapping of all damage that has been taken by other entities in the game
	 */
	protected Map<Entity, List<Damage>> damageTaken = new HashMap<>();

	/**
	 * The {@link Entity} that has been determined the killer of this {@link Entity}
	 */
	protected Entity killer;

	/**
	 * The health of the entity
	 */
	protected Health health;

	/**
	 * TO-DO
	 */
	protected Hitmark hitmark1 = null;
	protected Hitmark hitmark2 = null;
	public boolean updateRequired = true;
	public int hitDiff2;
	public int hitDiff = 0;
	public boolean hitUpdateRequired2;
	public boolean hitUpdateRequired = false;

	/**
	 * Creates a new {@link Entity} object with a specified index value representing
	 * where in their respective array they reside. An {@link Entity} is an object
	 * that exists within the game-world. A {@link Player} or {@link NPC} are all
	 * examples of entities.
	 * 
	 * @param index
	 *            the index in the list where this {@link Entity} resides.
	 */
	public Entity(int index, String name) {
		this.index = index;
		this.name = name;
	}

	/**
	 * The index value where the {@link Entity} resides along with other common
	 * counterparts.
	 * 
	 * @return the index of the array where this object resides
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * The x-position on the map where the entity exists. This is on the x-axis
	 * 
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * Modifies the x-position of the entity.
	 * 
	 * @param x
	 *            the new position
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * The y-position on the map where the entity exists. This is on the y-axis.
	 * 
	 * @return the y-position
	 */
	public int getY() {
		return y;
	}

	/**
	 * Modifies the y-position of the entity.
	 * 
	 * @param y
	 *            the new position
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * The height level of the entity.
	 * 
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Modifies the height of the entity.
	 * 
	 * @param height
	 *            the new height
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * The visual name of the {@link Entity} whether its a player name or npc.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Adds some damage value to the entities list of taken damage
	 * 
	 * @param entity
	 *            the entity that dealt the damage
	 * @param damage
	 *            the total damage taken
	 */
	public void addDamageTaken(Entity entity, int damage) {
		if (entity == null || damage <= 0) {
			return;
		}
		Damage combatDamage = new Damage(damage);
		if (damageTaken.containsKey(entity)) {
			damageTaken.get(entity).add(new Damage(damage));
		} else {
			damageTaken.put(entity, new ArrayList<>(Arrays.asList(combatDamage)));
		}
	}

	/**
	 * Clears any and all damage that has been taken by the entity
	 */
	public void resetDamageTaken() {
		damageTaken.clear();
	}

	/**
	 * Sends some information to the Stream regarding a possible new hit on the
	 * entity.
	 * 
	 * @param str
	 *            the stream for the entity
	 */
	protected abstract void appendHitUpdate(Stream str);

	/**
	 * Sends some information to the Stream regarding a possible new hit on the
	 * entity.
	 * 
	 * @param str
	 *            the stream for the entity
	 */
	protected abstract void appendHitUpdate2(Stream str);

	/**
	 * Used to append some amount of damage to the entity and inflict on their total
	 * amount of health. The method will also display a new hitmark on that entity.
	 * 
	 * @param damage
	 *            the damage dealt
	 * @param hitmark
	 *            the hitmark to show with the damage
	 */
	public abstract void appendDamage(int damage, Hitmark hitmark);

	/**
	 * Determines if the entity is susceptible to a status based on their nature.
	 * For example some players when wearing certain equipment are exempt from venom
	 * or poison status. In other situations, NPC's are susceptible to venom.
	 * 
	 * @param status
	 *            the status the entity may not be susceptible to
	 * @return {code true} if the entity is not susceptible to a particular status
	 */
	public abstract boolean susceptibleTo(HealthStatus status);

	/**
	 * When an entity dies it is paramount that we know who dealt the most damage to
	 * that entity so that we can determine who will receive the drop.
	 * 
	 * @return the {@link Entity} that dealt the most damage to this {@link Entity}.
	 */
	public Entity calculateKiller() {
		final long VALID_TIMEFRAME = this instanceof NPC ? TimeUnit.MINUTES.toMillis(5) : TimeUnit.SECONDS.toMillis(90);
		Entity killer = null;
		int totalDamage = 0;

		for (Entry<Entity, List<Damage>> entry : damageTaken.entrySet()) {
			Entity tempKiller = entry.getKey();
			List<Damage> damageList = entry.getValue();
			int damage = 0;

			if (tempKiller == null) {
				continue;
			}

			for (Damage d : damageList) {
				if (System.currentTimeMillis() - d.getTimestamp() < VALID_TIMEFRAME) {
					damage += d.getAmount();
				}
			}
			if (totalDamage == 0 || damage > totalDamage || killer == null) {
				totalDamage = damage;
				killer = tempKiller;
			}

			if (killer != null && killer instanceof Player && this instanceof NPC) {
				Player player = (Player) killer;
				NPC npc = (NPC) this;

				if ((player.getMode().isIronman() || player.getMode().isUltimateIronman())
						&& !Boundary.isIn(player, Boundary.GODWARS_BOSSROOMS)
						&& !Boundary.isIn(player, Boundary.CORPOREAL_BEAST_LAIR)
						&& !Boundary.isIn(player, Boundary.DAGANNOTH_KINGS) && !Boundary.isIn(player, Boundary.TEKTON)
						&& !Boundary.isIn(player, Boundary.SKELETAL_MYSTICS)
						&& !Boundary.isIn(player, Boundary.RAID_MAIN)) {

					double percentile = ((double) totalDamage / (double) npc.getHealth().getMaximum()) * 100D;
					if (percentile < 75.0) {
						killer = null;
					}
				}
			}

		}
		return killer;
	}

	/**
	 * The status of the entities health whether it's normal, poisoned, or some
	 * other nature.
	 * 
	 * @return the status of the entities health
	 */
	public Health getHealth() {
		if (health == null) {
			health = new Health(this);
		}
		return health;
	}

	/**
	 * Updates the {@link Entity} that has killed this {@link Entity
	 * 
	 * @param killer
	 *            the {@link Entity} killer
	 */
	public void setKiller(Entity killer) {
		this.killer = killer;
	}

	/**
	 * The {@link Entity} that has dealt the most damage in combat
	 * 
	 * @return the killer
	 */
	public Entity getKiller() {
		return killer;
	}

	/**
	 * Retrieves the current hitmark
	 * 
	 * @return the hitmark
	 */
	public Hitmark getHitmark() {
		return hitmark1;
	}

	/**
	 * Retrieves the second hitmark
	 * 
	 * @return the second hitmark
	 */
	public Hitmark getSecondHitmark() {
		return hitmark2;
	}

}
