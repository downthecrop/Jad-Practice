package ethos.event.impl;

import ethos.event.Event;
import ethos.model.entity.Entity;
import ethos.model.entity.Health;
import ethos.model.entity.HealthStatus;

public class PoisonResistanceEvent extends Event<Entity> {

	public PoisonResistanceEvent(Entity attachment, int ticks) {
		super("poison_resistance_event", attachment, ticks);
	}

	@Override
	public void execute() {
		if (attachment == null) {
			super.stop();
			return;
		}
		super.stop();

		Health health = attachment.getHealth();
		health.removeNonsusceptible(HealthStatus.POISON);
	}

}
