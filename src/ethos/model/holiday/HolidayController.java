package ethos.model.holiday;

import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import ethos.Server;
import ethos.model.holiday.christmas.Christmas;
import ethos.model.holiday.christmas.ChristmasCycleEvent;
import ethos.model.holiday.halloween.Halloween;
import ethos.model.holiday.halloween.HalloweenCycleEvent;
import ethos.model.players.Player;

/**
 * 
 * @author Jason MacKeigan
 * @date Oct 27, 2014, 11:13:48 AM
 */
public class HolidayController {

	public static final Halloween HALLOWEEN = new Halloween("Halloween", new GregorianCalendar(Server.getCalendar().getInstance().get(Calendar.YEAR), Calendar.OCTOBER, 24, 0, 0),
			new GregorianCalendar(Server.getCalendar().getInstance().get(Calendar.YEAR), Calendar.OCTOBER, 30, 0, 0), new HalloweenCycleEvent());

	public static final Christmas CHRISTMAS = new Christmas("Christmas", new GregorianCalendar(Server.getCalendar().getInstance().get(Calendar.YEAR), Calendar.DECEMBER, 5, 0, 0),
			new GregorianCalendar(Server.getCalendar().getInstance().get(Calendar.YEAR), Calendar.DECEMBER, 30, 0, 0), new ChristmasCycleEvent());

	/**
	 * A list that will contian all of the available holidays so we can micro manage them
	 */
	private static final List<Holiday> HOLIDAYS = Arrays.asList(HALLOWEEN, CHRISTMAS);

	/**
	 * Manages npc interaction that relates to holidays this includes dialogue
	 * 
	 * @param player the player interacting with the npc
	 * @param type the npc click type
	 * @param npcId the npc id
	 */
	public boolean clickNpc(Player player, int type, int npcId) {
		Optional<Holiday> op = HOLIDAYS.stream().filter(holiday -> holiday.isActive() && holiday.clickNpc(player, type, npcId)).findFirst();
		return op.isPresent();
	}

	/**
	 * Manages clicking objects that relate to holiday events
	 * 
	 * @param player the player clicking the object
	 * @param type the object type, whether type; 1, 2, 3, 4
	 * @param objectId
	 */
	public boolean clickObject(Player player, int type, int objectId, int x, int y) {
		Optional<Holiday> op = HOLIDAYS.stream().filter(holiday -> holiday.isActive() && holiday.clickObject(player, type, objectId, x, y)).findFirst();
		return op.isPresent();
	}

	/**
	 * Manages clicking a certain button that relates to holiday events
	 * 
	 * @param player the player clicking the button
	 * @param buttonId the button clicked
	 */
	public boolean clickButton(Player player, int buttonId) {
		Optional<Holiday> op = HOLIDAYS.stream().filter(holiday -> holiday.isActive() && holiday.clickButton(player, buttonId)).findFirst();
		return op.isPresent();
	}

	/**
	 * Clicks an ingame item and attempts to check if any holiday manages clicking that item
	 * 
	 * @param player the player
	 * @param itemId the item id
	 * @return true if the item they clicked corresponds with a holiday item
	 */
	public boolean clickItem(Player player, int itemId) {
		Optional<Holiday> op = HOLIDAYS.stream().filter(holiday -> holiday.isActive() && holiday.clickItem(player, itemId)).findFirst();
		return op.isPresent();
	}

	/**
	 * Gives the player a reward for their efforts in the event, depending if they have met the required stage.
	 * 
	 * @param player
	 * @param holiday
	 */
	public void giveReward(Player player, Holiday holiday) {
		Optional<Holiday> op = HOLIDAYS.stream().filter(h -> Objects.equals(h, holiday)).findFirst();
		if (Objects.nonNull(op) && op.isPresent()) {
			Holiday h = op.get();
			if (!h.isActive()) {
				return;
			}
			h.receive(player);
		}
	}

	/**
	 * Initializes all necessary information for each holiday
	 */
	public void initialize() {
		HOLIDAYS.stream().filter(holiday -> holiday.isActive()).forEach(holiday -> holiday.initializeHoliday());
	}

}
