package ethos.model.content;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import ethos.Server;

public class BonusActivation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5883846729794248981L;

	/**
	 * The total number of winners possible for each draw
	 */
	private static final int TOP_CONTRIBUTORS = 3;

	/**
	 * Compares the value of an entry
	 */
	private static final Comparator<Entry<String, Integer>> HIGHEST_VALUE = Entry.comparingByValue();

	/**
	 * Contains all of the entries
	 */
	private Map<String, Integer> entries = new HashMap<>();

	/**
	 * Top contributors
	 */
	private Map<String, Integer> topContributors;

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
		Server.getServerData().setBonusActivation(this);
	}

	/**
	 * Clears all entries in the map
	 */
	public void clear() {
		entries.clear();
	}

	/**
	 * Determines the contributioners
	 * 
	 * @return the contributioners
	 */
	public ArrayList<Entry<String, Integer>> getSortedResults() {
		List<Entry<String, Integer>> list = new ArrayList<>(entries.entrySet());
		list.sort(HIGHEST_VALUE);
		Collections.reverse(list);
		return new ArrayList<>(list.subList(0, list.size() < TOP_CONTRIBUTORS ? list.size() : TOP_CONTRIBUTORS));
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
		return topContributors;
	}

}
