package ethos.model.content.bonus;

import java.util.Calendar;


public class DoubleMinigames {

	public static boolean isDoubleMinigames() {
		return Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY;
	}
}
