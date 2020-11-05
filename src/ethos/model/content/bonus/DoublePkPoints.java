package ethos.model.content.bonus;

import java.util.Calendar;

public class DoublePkPoints {

	public static boolean isDoublePk() {
		return Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY;
	}
}
