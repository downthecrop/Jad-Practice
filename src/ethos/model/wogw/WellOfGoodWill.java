package ethos.model.wogw;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import ethos.Server;
import ethos.util.Misc;

/**
 * @author Jason MacKeigan
 * @date Dec 21, 2014, 9:16:32 AM
 */
public class WellOfGoodWill implements Serializable {

	private static final long serialVersionUID = -3335621107999346623L;

	/**
	 * The total number of winners possible for each draw
	 */
	private static final int MAXIMUM_WINNERS = 3;

	/**
	 * Compares the value of an entry
	 */
	private static final Comparator<Entry<String, Integer>> HIGHEST_VALUE = Entry.comparingByValue();

	/**
	 * Contains all of the entries for the week
	 */
	private Map<String, Integer> entries = new HashMap<>();

	/**
	 * Winners of the previous week
	 */
	private Map<String, Integer> winners;

	/**
	 * The date the week is over. The initial date is set.
	 */
	private Date date = Misc.getFutureDate(2014, Calendar.DECEMBER, 28, 18, 0, 0);

	/**
	 * Requests that an update be made on the entries variable.
	 * 
	 * @param player the key being updated
	 * @param amount the amount being added
	 */
	public void update(String player, Integer amount) {
		if (!entries.containsKey(player)) {
			entries.put(player, amount);
		} else {
			int oldValue = entries.get(player);
			entries.replace(player, oldValue, oldValue + amount);
		}
		Server.getServerData().setWellOfGoodWill(this);
	}

	/**
	 * Clears all entries in the map
	 */
	public void clear() {
		entries.clear();
	}

	/**
	 * Determines the weekly winner based on their contributions
	 * 
	 * @return the winner
	 */
	public ArrayList<Entry<String, Integer>> getSortedResults() {
		List<Entry<String, Integer>> list = new ArrayList<>(entries.entrySet());
		list.sort(HIGHEST_VALUE);
		Collections.reverse(list);
		return new ArrayList<>(list.subList(0, list.size() < MAXIMUM_WINNERS ? list.size() : MAXIMUM_WINNERS));
	}

	/**
	 * The map containing all of the entries for the week
	 * 
	 * @return the entries
	 */
	public Map<String, Integer> getEntries() {
		return entries;
	}

	/**
	 * The winner of the previous week
	 * 
	 * @return the winner
	 */
	public Map<String, Integer> getWinners() {
		return winners;
	}

	/**
	 * Sets the new winner of the week
	 * 
	 * @param winner the winner
	 */
	public void setWinners(Map<String, Integer> winners) {
		this.winners = winners;
	}

	/**
	 * The date the week started
	 * 
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * Sets the date of the week the event starts on
	 * 
	 * @param date the date
	 */
	public void setDate(Date date) {
		this.date = date;
	}

}
