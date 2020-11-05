package ethos.model.content.bonus;

import java.util.Calendar;

public class DropRateBoost {

	public static boolean isDropRateBoost() {
		return Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY;
	}
}
