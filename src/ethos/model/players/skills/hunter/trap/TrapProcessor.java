package ethos.model.players.skills.hunter.trap;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import ethos.event.CycleEvent;

/**
 * Represents a processor which manages a list of traps.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class TrapProcessor {

	/**
	 * The list of traps to manage.
	 */
	private final List<Trap> traps = new ArrayList<>();
	
	/**
	 * The task running for the list of traps.
	 */
	private Optional<CycleEvent> task = Optional.empty();
	
	/**
	 * Constructs a new _empty_ (non-set default initialization) {@link TrapProcessor}.
	 */
	public TrapProcessor() {
	}
	
	/**
	 * @return the list of traps.
	 */
	public List<Trap> getTraps() {
		return traps;
	}
	
	/**
	 * @return the task running.
	 */
	public Optional<CycleEvent> getTask() {
		return task;
	}
		
	/**
	 * @param task	the task to set.
	 */
	public void setTask(Optional<CycleEvent> task) {
		this.task = task;
	}
	
	/**
	 * @param task	the task to set.
	 */
	public void setTask(CycleEvent task) {
		setTask(Optional.of(task));
	}
	

}
