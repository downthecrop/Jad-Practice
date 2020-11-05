package ethos.punishments;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import ethos.Server;
import ethos.event.Event;

import java.util.Queue;

public class PunishmentCycleEvent extends Event<Punishments> {

	public PunishmentCycleEvent(Punishments attachment, int ticks) {
		super(attachment, ticks);
	}

	@Override
	public void update() {
		Map<PunishmentType, List<Punishment>> punishments = attachment.getPunishments();
		Queue<Punishment> add = attachment.getAddQueue();
		Queue<Punishment> remove = attachment.getRemoveQueue();
		List<PunishmentType> typesUpdated = new ArrayList<>();

		if (!add.isEmpty()) {
			add.forEach(punishment -> {
				List<Punishment> list = punishments.get(punishment.getType());
				list.add(punishment);
				if (!typesUpdated.contains(punishment.getType())) {
					typesUpdated.add(punishment.getType());
				}
			});
			add.clear();
		}

		if (!remove.isEmpty()) {
			remove.forEach(punishment -> {
				List<Punishment> list = punishments.get(punishment.getType());
				list.remove(punishment);
				if (!typesUpdated.contains(punishment.getType())) {
					typesUpdated.add(punishment.getType());
				}
			});
			remove.clear();
		}

		if (!typesUpdated.isEmpty()) {
			typesUpdated.forEach(type -> Server.getPunishments().write(type));
		}
	}

	@Override
	public void execute() {
		for (Entry<PunishmentType, List<Punishment>> entry : attachment.getPunishments().entrySet()) {
			List<Punishment> punishments = entry.getValue();

			for (Punishment punishment : punishments) {
				if (punishment.getDuration() == 0 || punishment.getDuration() == Long.MAX_VALUE) {
					continue;
				}
				if (System.currentTimeMillis() > punishment.getDuration()) {
					Server.getPunishments().remove(punishment);
					continue;
				}
			}
		}
	}

}