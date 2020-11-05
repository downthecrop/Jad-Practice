package ethos.event;

/**
 * The wrapper for our event
 * 
 * @author Stuart <RogueX>
 * @author Null++
 * 
 */

public class CycleEventContainer {

	/**
	 * Event owner
	 */
	private Object owner;

	/**
	 * Is the event running or not
	 */
	private boolean isRunning;

	/**
	 * The amount of cycles per event execution
	 */
	private int tick;

	/**
	 * The actual event
	 */
	private CycleEvent event;

	/**
	 * The current amount of cycles passed
	 */
	private int cyclesPassed;

	/**
	 * The event ID
	 */
	private int eventID;

	/**
	 * The total sum of game ticks that have passed during the lifetime of the event
	 */
	private int ticks;

	/**
	 * Determines if this event should be randomized every cycle
	 */
	private boolean randomized;

	/**
	 * Sets the event containers details
	 * 
	 * @param owner , the owner of the event
	 * @param event , the actual event to run
	 * @param tick , the cycles between execution of the event
	 */
	public CycleEventContainer(int id, Object owner, CycleEvent event, int tick) {
		this.eventID = id;
		this.owner = owner;
		this.event = event;
		this.isRunning = true;
		this.cyclesPassed = 0;
		this.tick = tick;
	}

	public CycleEventContainer(int id, Object owner, CycleEvent event, int tick, boolean randomized) {
		this(id, owner, event, tick);
		this.randomized = randomized;
	}

	/**
	 * Execute the contents of the event
	 */
	public void execute() {
		event.execute(this);
	}

	public void update() {
		event.update(this);
	}

	/**
	 * Stop the event from running
	 */
	public void stop() {
		isRunning = false;
		event.stop();
	}

	/**
	 * Does the event need to be ran?
	 * 
	 * @return true yes false no
	 */
	public boolean needsExecution() {
		if (!this.isRunning()) {
			return false;
		}
		ticks++;
		if (++this.cyclesPassed >= this.tick) {
			this.cyclesPassed = 0;
			return true;
		}
		return false;
	}

	public CycleEvent getEvent() {
		return event;
	}

	/**
	 * Randomization occurs during the process of the main game loop. Events that are randomized are swapped randomly in execution order until there are none left.
	 * 
	 * @return
	 */
	public boolean isRandomized() {
		return randomized;
	}

	/**
	 * Returns the owner of the event
	 * 
	 * @return
	 */
	public Object getOwner() {
		return owner;
	}

	/**
	 * Is the event running?
	 * 
	 * @return true yes false no
	 */
	public boolean isRunning() {
		return isRunning;
	}

	/**
	 * Returns the event id
	 *
	 * @return id
	 */
	public int getID() {
		return eventID;
	}

	public int getTick() {
		return tick;
	}
	
	public void setTick(int tick) {
		this.cyclesPassed = 0;
		this.tick = tick;
	}

	/**
	 * The number of game ticks that have passed since the creation of the event.
	 * 
	 * @return game ticks
	 */
	public int getTotalTicks() {
		return ticks;
	}

}