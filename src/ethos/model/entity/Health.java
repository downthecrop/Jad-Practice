package ethos.model.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import ethos.Server;
import ethos.event.impl.PoisonEvent;
import ethos.event.impl.PoisonResistanceEvent;
import ethos.event.impl.VenomEvent;
import ethos.event.impl.VenomResistanceEvent;
import ethos.model.players.Player;

public final class Health {

	/**
	 * The entity this health was created for
	 */
	private final Entity entity;

	/**
	 * The amount of health the entity has
	 */
	private int amount;

	/**
	 * The maximum amount of health the entity has access to
	 */
	private int maximum;

	/**
	 * The status of the health
	 */
	private HealthStatus status = HealthStatus.NORMAL;

	/**
	 * A {@link Collection} of statuses that the entity is not susceptible to
	 */
	private Collection<HealthStatus> nonsusceptibles = new ArrayList<>();

	/**
	 * A new {@link Health} object to manage health operations
	 * 
	 * @param entity the single entity this is created for
	 */
	public Health(Entity entity) {
		this.entity = entity;
	}

	/**
	 * Determines if the health of the entity is not susceptible to a possible status change
	 * 
	 * @param status the possible new status change
	 * @return {@code true} if the entity is not susceptible to the status
	 */
	public boolean isNotSusceptibleTo(HealthStatus status) {
		return nonsusceptibles.contains(status);
	}

	/**
	 * Proposes that the health of the entity be changed to another
	 * 
	 * @param status the potential new status of the entity
	 */
	public void proposeStatus(HealthStatus status, int damage, Optional<Entity> inflictor) {
		if (!entity.susceptibleTo(status)) {
			return;
		}

		if (isNotSusceptibleTo(status)) {
			return;
		}

		if (this.status == status) {
			return;
		}

		if (this.status.isVenomed() && status.isPoisoned()) {
			return;
		}

		if (Server.getEventHandler().isRunning(entity, "health_status")) {
			Server.getEventHandler().stop(entity, "health_status");
		}

		if (status.isVenomed()) {
			Server.getEventHandler().submit(new VenomEvent(entity, damage, inflictor));
		}

		if (status.isPoisoned()) {
			Server.getEventHandler().submit(new PoisonEvent(entity, damage, inflictor));
		}

		this.status = status;

		if (entity instanceof Player) {
			Player player = (Player) entity;
			player.sendMessage("You have been effected by " + status.toString() + ".");
			player.getPA().requestUpdates();
		}
	}

	/**
	 * Attempts to resolve the status specified. If the entity is already not
	 * 
	 * @param status the status we're trying to resolve
	 * @param resistanceDuration the duration of the resistance if provided
	 */
	public void resolveStatus(HealthStatus status, int resistanceDuration) {
		if (!nonsusceptibles.contains(status)) {
			nonsusceptibles.add(status);
		}

		if (status == HealthStatus.POISON) {
			Server.getEventHandler().stop(entity, "poison_resistance_event");
			Server.getEventHandler().submit(new PoisonResistanceEvent(entity, resistanceDuration));
		}

		if (status == HealthStatus.VENOM) {
			Server.getEventHandler().stop(entity, "venom_resistance_event");
			Server.getEventHandler().submit(new VenomResistanceEvent(entity, resistanceDuration));
		}

		if (status == this.status) {
			if (entity instanceof Player) {
				Player player = (Player) entity;
				player.sendMessage("You have resolved the " + status.toString() + " that was effecting your health.");
			}
		}

		this.status = HealthStatus.NORMAL;
	}

	/**
	 * Removes and resolves all status effects such as venom or poison.
	 */
	public void removeAllStatuses() {
		status = HealthStatus.NORMAL;
		Server.getEventHandler().stop(entity, "health_status");
	}

	/**
	 * Removes all immunities the player may have such as being immune to venom or poison.
	 */
	public void removeAllImmunities() {
		Server.getEventHandler().stop(entity, "poison_resistance_event");
		Server.getEventHandler().stop(entity, "venom_resistance_event");
	}

	/**
	 * Allows us to remove the status from the list of non-susceptible.
	 * 
	 * @param status the status to be removed
	 * @return {@code true} if the status is in the
	 */
	public boolean removeNonsusceptible(HealthStatus status) {
		return nonsusceptibles.remove(status);
	}

	/**
	 * Requests that the health of the entity update some attribute of the player. In this case, the {@link Player} would have his or her health value sent to the client to show in
	 * the orb and on the statistic interface.
	 */
	private void requestUpdate() {
		if (entity instanceof Player) {
			Player player = (Player) entity;
			if (!player.initialized) {
				return;
			}
			player.getPA().refreshSkill(player.playerHitpoints);
		}
	}

	/**
	 * Retrieves the current status of the entities health
	 * 
	 * @return the status of the health
	 */
	public HealthStatus getStatus() {
		return status;
	}

	/**
	 * The amount of health the entity has
	 * 
	 * @return the amount of health
	 */
	public int getAmount() {
		return amount;
	}

	/**
	 * The absolute maximum health of this npc
	 * 
	 * @param maximum the maximum health
	 */
	public void setMaximum(int maximum) {
		this.maximum = maximum;
	}

	/**
	 * The maximum health the entity can have
	 * 
	 * @return the maximum health
	 */
	public int getMaximum() {
		return maximum;
	}

	/**
	 * Attempts to reduce the entities health amount by a specified amount
	 * 
	 * @param amount the amount the health amount is to be reduced by
	 */
	public void reduce(int amount) {
//		if (entity instanceof Player) {
//			Player player = (Player) entity;
//			Server.npcHandler.ringOfLife(player);
//		}
		if (amount <= 0) {
			return;
		}
		if (amount > this.amount) {
			amount = this.amount;
		}
		this.amount -= amount;
		requestUpdate();
	}

	/**
	 * Attempts to increase the entities health by a specified amount
	 * 
	 * @param amount the amount the health will be increased by
	 */
	public void increase(int amount) {
		if (this.amount + amount > maximum) {
			amount = maximum - this.amount;
		}
		this.amount += amount;
		requestUpdate();
	}

	/**
	 * Modifies the current amount of health to the amount specified in the parameter. <b> It is worth noting that the amount can exceed that of the {@code maximum}. </b>
	 * 
	 * @param amount the new amount of health
	 */
	public void setAmount(int amount) {
		this.amount = amount;
		requestUpdate();
	}

	/**
	 * Sets the value of {@code #amount} to that of {@code #maximum} in an effort to revitalize the health of the entity.
	 */
	public void reset() {
		amount = maximum;
		requestUpdate();
	}

}
