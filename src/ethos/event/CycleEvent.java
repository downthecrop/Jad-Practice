package ethos.event;

/**
 * What The Event Must Implement
 * 
 * @author Stuart <RogueX> Revised by Shawn
 */
public abstract class CycleEvent {

	/**
	 * Code which should be ran when the event is executed.
	 * 
	 * @param container
	 */
	public abstract void execute(CycleEventContainer container);

	/**
	 * The update function is referenced every cycle the event is alive for.
	 */
	public void update(CycleEventContainer container) {
	}

	/**
	 * Code which should be ran when the event stops.
	 */
	public void stop() {

	}

}