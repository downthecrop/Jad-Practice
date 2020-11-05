package ethos.event.impl;

import java.util.Optional;

import ethos.event.Event;
import ethos.model.entity.Entity;
import ethos.model.entity.Health;
import ethos.model.entity.HealthStatus;
import ethos.model.players.combat.Hitmark;

public class PoisonEvent extends Event<Entity> {

	private int damage;

	private int hits;

	private Optional<Entity> inflictor;

	public PoisonEvent(Entity attachment, int damage, Optional<Entity> inflictor) {
		super("health_status", attachment, 100);
		this.damage = damage;
		this.inflictor = inflictor;
	}

	@Override
	public void execute() {
		if (attachment == null) {
			super.stop();
			return;
		}

		Health health = attachment.getHealth();

		if (health.isNotSusceptibleTo(HealthStatus.POISON)) {
			super.stop();
			return;
		}

		if (attachment.getHealth().getAmount() <= 0) {
			super.stop();
			return;
		}

		attachment.appendDamage(damage, Hitmark.POISON);
		inflictor.ifPresent(inf -> attachment.addDamageTaken(inf, damage));

		hits++;

		if (hits >= 4) {
			damage--;
			hits = 0;
		}

		if (damage <= 0) {
			super.stop();
			return;
		}
	}

}
