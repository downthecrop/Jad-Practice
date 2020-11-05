package ethos.model.content.bonus;

import java.util.Calendar;

public class DoubleSlayerPoints {

	public static boolean isDoubleSlayer() {
		return Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY;
	}
}
