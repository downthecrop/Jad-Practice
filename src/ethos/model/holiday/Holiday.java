package ethos.model.holiday;

import java.util.Calendar;

import ethos.Server;
import ethos.event.CycleEvent;
import ethos.model.players.Player;

/**
 * @author Jason MacKeigan (http://www.rune-server.org/members/jason)
 * @date Oct 27, 2014, 12:26:55 AM
 */
public abstract class Holiday implements HolidayInteraction, HolidayReward {
	/**
	 * The start, and end date of the event.
	 */
	protected Calendar start, end;

	/**
	 * The name of the holiday
	 */
	protected String name;

	/**
	 * The cycled event for this holiday which is only created once during runtime to work as a pulse
	 */
	protected CycleEvent event;

	/**
	 * The minimum stage that a player could be on for any given holiday
	 */
	protected int minimumStage;

	/**
	 * The maximum stage that a player could be on for any given holiday
	 */
	protected int maximumStage;

	/**
	 * Constructs a new holiday
	 * 
	 * @param name the name of the holiday
	 * @param start the starting date of the holiday
	 * @param end the end date of the holiday
	 */
	public Holiday(String name, Calendar start, Calendar end, CycleEvent event) {
		this.name = name;
		this.start = start;
		this.end = end;
		this.event = event;
	}

	/**
	 * Initialize anything neccesary upon server startup
	 */
	public abstract void initializeHoliday();

	/**
	 * Cleans up and finishes the holiday, this is typically referenced to when the holiday has been active and is no longer active.
	 */
	public abstract void finalizeHoliday();

	/**
	 * Determines if the player has completed the holiday event or not
	 * 
	 * @param player the player
	 * @return true if they have completed it, false otherwise.
	 */
	public abstract boolean completed(Player player);

	/**
	 * Returns the minimum stage a player can be on
	 * 
	 * @return the minimum stage
	 */
	public abstract int getMinimumStage();

	/**
	 * Returns the maximum stage a player can be on
	 * 
	 * @return the maximum stage
	 */
	public abstract int getMaximumStage();

	/**
	 * Determines if the server date is before the end date, and after the start date of the holiday
	 * 
	 * @return true if is the holidays, otherwise false
	 */
	public boolean isActive() {
		Calendar date = Server.getCalendar().getInstance();
		return date.before(end) && date.after(start);
	}

	/**
	 * Returns the name of the holoday
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

}
