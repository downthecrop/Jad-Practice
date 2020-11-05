package ethos.event;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;

public class CycleEventHandler {

	private static CycleEventHandler instance = new CycleEventHandler();

	public static CycleEventHandler getSingleton() {
		if (instance == null) {
			instance = new CycleEventHandler();
		}
		return instance;
	}

	private Queue<CycleEventContainer> pending;

	private List<CycleEventContainer> events;

	public CycleEventHandler() {
		this.pending = new ArrayDeque<>(150);
		this.events = new LinkedList<>();
	}

	public void addEvent(int id, Object owner, CycleEvent event, int cycles) {
		this.pending.add(new CycleEventContainer(id, owner, event, cycles));
	}

	public void addEvent(int id, Object owner, CycleEvent event, int cycles, boolean randomized) {
		this.pending.add(new CycleEventContainer(id, owner, event, cycles, randomized));
	}

	public void addEvent(CycleEventContainer container) {
		pending.add(container);
	}

	public void addEvent(Object owner, CycleEvent event, int cycles) {
		addEvent(-1, owner, event, cycles);
	}

	public void executeAll(Object owner) {
		events.stream().filter(Objects::nonNull).filter(container -> container.getOwner() != null).filter(container -> container.getOwner().equals(owner))
				.forEach(container -> container.execute());
	}

	public void process() {
		CycleEventContainer container;
		while ((container = pending.poll()) != null) {
			if (container.isRunning())
				events.add(container);
		}
		Iterator<CycleEventContainer> it = events.iterator();
		List<CycleEventContainer> randomizedEvents = new ArrayList<>();
		while (it.hasNext()) {
			container = it.next();
			if (container != null) {
				if (container.isRunning()) {
					container.update();
					if (container.needsExecution()) {
						if (container.isRandomized()) {
							randomizedEvents.add(container);
						} else {
							container.execute();
						}
					}
				}
				if (!container.isRunning()) {
					it.remove();
				}
			}
		}
		if (randomizedEvents.size() > 0) {
			Collections.shuffle(randomizedEvents);
			randomizedEvents.forEach(CycleEventContainer::execute);
		}
	}

	public int getEventsCount() {
		return this.events.size();
	}

	public void stopEvents(Object owner) {
		for (CycleEventContainer container : events) {
			if (container.getOwner().equals(owner)) {
				container.stop();
			}
		}
		for (CycleEventContainer container : pending) {
			if (container.getOwner().equals(owner)) {
				container.stop();
			}
		}
	}

	public void stopEvents(Object owner, int id) {
		if (id == -1) {
			throw new IllegalStateException("Illegal identification value, -1 is not permitted.");
		}
		for (CycleEventContainer c : events) {
			if (c.getOwner() == owner && id == c.getID()) {
				c.stop();
			}
		}
		for (CycleEventContainer container : pending) {
			if (container.getOwner().equals(owner) && id == container.getID()) {
				container.stop();
			}
		}
	}

	public void stopEvents(int id) {
		if (id == -1) {
			throw new IllegalStateException("Illegal identification value, -1 is not permitted.");
		}
		for (CycleEventContainer c : events) {
			if (id == c.getID()) {
				c.stop();
			}
		}
		for (CycleEventContainer container : pending) {
			if (container.getID() == id) {
				container.stop();
			}
		}
	}

	/**
	 * Determines if an event is active where the object passed is the owner and the event id is the id of the event.
	 * 
	 * @param owner the owner of the event
	 * @return true if the event is alive
	 */
	public boolean isAlive(Object owner, int eventId) {
		Optional<CycleEventContainer> op = events.stream().filter(container -> container.getOwner().equals(owner) && container.getID() == eventId).findFirst();
		return op.isPresent();
	}

	/**
	 * Determines if an event is active where the object passed is the owner.
	 * 
	 * @param owner the owner of the event
	 * @return true if the event is alive
	 */
	public boolean isAlive(Object owner) {
		Optional<CycleEventContainer> op = events.stream().filter(container -> container.getOwner().equals(owner)).findFirst();
		return op.isPresent();
	}

	public interface Event {
		int PEST_CONTROL_LOBBY = 10000;
		int PEST_CONTROL_GAME = 10005;
		int BONE_ON_ALTAR = 10010;
		int WARRIORS_GUILDS = 10015;
		int OVERLOAD_BOOST_ID = 10020;
		int OVERLOAD_HITMARK_ID = 10025;
		int PLAYER_COMBAT_DAMAGE = 10030;
		int CHRISTMAS_ENGINEER = 10035;
	}

}