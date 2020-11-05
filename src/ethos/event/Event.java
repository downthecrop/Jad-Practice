package ethos.event;

import com.google.common.base.Preconditions;

/**
 * 
 * @author Jason MacKeigan
 * @date Jan 11, 2015, 3:59:45 PM
 */
public abstract class Event<T> {

	/**
	 * The name or signature of the event.
	 */
	protected String signature;

	/**
	 * The attachment serves as a lock to the event. If the object reference to the attachment is ever null, the lock is 'broken'. When broken, the event stops any and all further
	 * execution of the event and is disposed of.
	 */
	protected T attachment;

	/**
	 * Represents whether or not the event is still running
	 */
	protected boolean running = true;

	/**
	 * The current number of game ticks the event must wait before executing the context of the event.
	 */
	protected int ticks;

	/**
	 * The total amount of ticks that have elapsed since the creation of the event.
	 */
	protected int elapsed;

	/**
	 * The maximum number of ticks the event must wait before executing the context of the event.
	 */
	protected int maximumTicks;

	/**
	 * Creates a new event with a specific context.
	 * 
	 * @param attachment the attachment to this event
	 * @param ticks the number of ticks until execution
	 */
	public Event(T attachment, int ticks) {
		Preconditions.checkArgument(ticks > 0, "Negative or zero ticks were passed as a parameter.");
		this.attachment = attachment;
		this.ticks = ticks;
		this.maximumTicks = ticks;
	}

	/**
	 * Creates a new event with a specific context.
	 * 
	 * @param signature the unique signature of the event
	 * @param attachment the attachment to this event
	 * @param ticks the number of ticks until execution
	 */
	public Event(String signature, T attachment, int ticks) {
		this(attachment, ticks);
		this.signature = signature;
	}

	/**
	 * Executes the event under a certain context
	 */
	public abstract void execute();

	/**
	 * If this function is overridden, some code will execute every tick
	 */
	public void update() {

	}

	/**
	 * Terminates the event by setting the state of {@link #running} to false.
	 * 
	 * <p>
	 * Please note that it is <b>mandatory</b> that if this function is overridden, a reference to {@code super.stop()} still be made to ensure the state of {@link #running} is
	 * false. Otherwise the event will continue to execute.
	 * </p>
	 * <b>Example of Implementation</b>
	 * 
	 * <pre>
	 * <code>@Override
	 * public void stop() {
	 * 	super.stop()
	 * } </code>
	 * </pre>
	 */
	public void stop() {
		running = false;
	}

	/**
	 * Determines if the event requires termination under some basis such as the attachment reference being null.
	 * 
	 * @return {@code true} if the event requires termination
	 */
	public boolean requiresTermination() {
		return false;
	}

	/**
	 * The name or signature of the event
	 * 
	 * @return the signature
	 */
	public String getSignature() {
		return signature;
	}

	/**
	 * Returns the attachment to the event.
	 * 
	 * @return the attachment
	 */
	public T getAttachment() {
		return attachment;
	}

	/**
	 * Returns the number of ticks remaining until execution
	 * 
	 * @return the number of ticks is always positive. When the number of ticks reaches zero, the event will execute the context of the event.
	 */
	public int getTicks() {
		return ticks;
	}

	/**
	 * Returns the maximum number of ticks the event must wait before execution.
	 * 
	 * @return the number of ticks
	 */
	public int getMaximumTicks() {
		return maximumTicks;
	}

	/**
	 * Removes a single tick from the amount remaining.
	 * 
	 * @return the number of ticks after subtracting a single tick.
	 */
	public int removeTick() {
		ticks--;
		return ticks;
	}

	/**
	 * Increases the total value of {@link #elapsed} by one every tick the event is alive for.
	 */
	public void increaseElapsed() {
		elapsed++;
	}

	/**
	 * Resets the number of ticks remaining to that of the maximum
	 */
	public void reset() {
		ticks = maximumTicks;
	}

	/**
	 * Determines if the event is still running. Unless the event has been stopped, the event will continue to run and execute when it should.
	 * 
	 * @return
	 */
	public boolean isAlive() {
		return running;
	}

	/**
	 * Returns the total amount of ticks that have elapsed since the initial creation of this event. The amount of elapsed ticks cannot be reset, calling {@link #reset()} will not
	 * rest this.
	 * 
	 * @return the amount of elapsed ticks
	 */
	public int getElapsedTicks() {
		return elapsed;
	}
}
