package ethos.event;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;
import java.util.function.Predicate;

/**
 * A class that manages events that are pulsed every game cycle.
 * 
 * @author Jason MacKeigan
 * @date Jan 12, 2015, 5:15:04 PM
 */
public class EventHandler {

	/**
	 * A list of all the events that need to be added to the list of running events.
	 */
	private Queue<Event<?>> pendingAddition = new LinkedList<>();

	/**
	 * A list of all the events that need to be removed from the list of running events.
	 */
	private Queue<Event<?>> pendingRemoval = new LinkedList<>();

	/**
	 * A list of the currently running events
	 */
	private Queue<Event<?>> active = new LinkedList<>();

	/**
	 * Submits a new event to be added.
	 * 
	 * @param event the event
	 */
	public <T> void submit(Event<T> event) {
		Objects.requireNonNull(event);
		pendingAddition.add(event);
	}

	/**
	 * Attempts to stop any and all events that have a common attachment
	 * 
	 * @param attachment
	 */
	public <T> void stop(T attachment) {
		Predicate<Event<?>> equalTo = event -> event.getAttachment().equals(attachment);
		active.stream().filter(equalTo).forEach(pendingRemoval::add);
		pendingAddition.stream().filter(equalTo).forEach(pendingRemoval::add);
	}

	/**
	 * Attempts to stop any and all events that have a common attachment and signature
	 * 
	 * @param attachment the attachment that the event must have
	 * @param signature the signature the event must have
	 */
	public <T> void stop(T attachment, String signature) {
		Predicate<Event<?>> equalTo = event -> event.getAttachment().equals(attachment) && event.getSignature() != null && event.getSignature().equals(signature);
		active.stream().filter(equalTo).forEach(pendingRemoval::add);
		pendingAddition.stream().filter(equalTo).forEach(pendingRemoval::add);
	}

	/**
	 * Attempts to stop all events with the same signature that are active or are being added to the active list.
	 * 
	 * @param signature the signature of the events being compared to.
	 */
	public void stop(String signature) {
		Predicate<Event<?>> predicate = event -> event.getSignature() != null && event.getSignature().equals(signature);
		active.stream().filter(predicate).forEach(pendingRemoval::add);
		pendingAddition.stream().filter(predicate).forEach(pendingRemoval::add);
	}

	/**
	 * Determines if an event with the same attachment and signature is active.
	 * 
	 * @param attachment the attachment of the event
	 * @param signature the signature of the event
	 * @return {@code true} if the event is found and meets the predicate terms, otherwise {@code false}.
	 */
	public <T> boolean isRunning(T attachment, String signature) {
		Predicate<Event<?>> running = event -> event.isAlive() && event.getAttachment().equals(attachment) && event.getSignature() != null
				&& event.getSignature().equals(signature);
		return active.stream().anyMatch(running) || pendingAddition.stream().anyMatch(running);
	}

	/**
	 * Processes each of the events that are currently running.
	 */
	public void process() {
		if (pendingAddition.size() > 0) {
			pendingAddition.stream().filter(Objects::nonNull).forEach(active::add);
			pendingAddition.clear();
		}
		Event<?> event;
		while ((event = pendingRemoval.poll()) != null) {
			if (event.isAlive()) {
				event.stop();
			}
		}
		while ((event = active.poll()) != null) {
			if (event.getAttachment() == null || event.requiresTermination()) {
				event.stop();
				continue;
			}
			if (event.isAlive()) {
				event.increaseElapsed();
				event.update();
			}
			if (event.isAlive()) {
				if (event.getTicks() > 1) {
					event.removeTick();
					pendingAddition.add(event);
				} else if (event.getTicks() <= 1) {
					event.execute();
					event.reset();
					pendingAddition.add(event);
				}
			}
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Events Active");
		pendingAddition.forEach(e -> sb.append("--> " + e.getAttachment().toString() + " : " + e.getSignature() + "\n"));
		sb.append("[adding = " + pendingAddition.size() + ", active = " + active.size() + ", removing = " + pendingRemoval.size() + "]");
		return sb.toString();
	}
}
