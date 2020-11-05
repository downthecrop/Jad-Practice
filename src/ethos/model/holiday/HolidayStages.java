package ethos.model.holiday;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Jason MacKeigan
 * @date Oct 27, 2014, 2:23:07 PM
 */
public class HolidayStages {
	/**
	 * Represents the stage each player is on for each holiday.
	 */
	private Map<String, Integer> holidayStages = new HashMap<>();

	/**
	 * Returns the stage as a integer value for the holiday
	 * 
	 * @param holiday the holiday
	 * @return the stage the player is on for the holiday
	 */
	public int getStage(String holiday) {
		if (!holidayStages.containsKey(holiday)) {
			return 0;
		}
		return holidayStages.get(holiday);
	}

	/**
	 * Returns the map containing the holiday stages
	 * 
	 * @return the map
	 */
	public Map<String, Integer> getStages() {
		return holidayStages;
	}

	/**
	 * Sets the stage of a certain holiday to that of what we have passed
	 * 
	 * @param holiday the holiday
	 * @param stage the new stage
	 */
	public void setStage(String holiday, int stage) {
		holidayStages.put(holiday, stage);
	}

}
