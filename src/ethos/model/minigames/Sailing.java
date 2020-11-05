package ethos.model.minigames;

import ethos.event.CycleEvent;
import ethos.event.CycleEventContainer;
import ethos.event.CycleEventHandler;
import ethos.model.players.Player;

public class Sailing {

	private static final int[][] TRAVEL_DATA = { {}, // 0 - Null
			{ 2834, 3335, 8750 }, // 1 - From Port Sarim to Entrana
			{ 3048, 3234, 8750 }, // 2 - From Entrana to Port Sarim
			{ 2853, 3237, 7000 }, // 3 - From Port Sarim to Crandor
			{ 2834, 3335, 8000 }, // 4 - From Crandor to Port Sarim
			{ 2956, 3146, 4500 }, // 5 - From Port Sarim to Karajama
			{ 3029, 3217, 4500 }, // 6 - From Karajama to Port Sarim
			{ 2772, 3234, 2000 }, // 7 - From Ardougne to Brimhaven
			{ 3029, 3217, 2000 }, // 8 - From Brimhaven to Ardougne
			{}, // 9 - Null
			{}, // 10 - Null
			{ 2998, 3043, 14000 }, // 11 - From Port Khazard to Ship Yard
			{ 2676, 3170, 14000 }, // 12 - From Ship Yard to Port Khazard
			{ 2998, 3043, 10000 }, // 13 - From Cairn Island to Ship Yard
			{ 2659, 2676, 7000 }, // 14 - From Port Sarim to Pest Control
			{ 3041, 3202, 7000 }, // 15 - From Pest Control to Port Sarim
			{ 2763, 2956, 6000 }, // 16 - To Cairn Isle from Feldip Hills
	};

	public static void startTravel(final Player player, final int i) {
		player.getPA().showInterface(3281);
		player.getPA().sendFrame36(75, i);
		player.getPA().movePlayer(1, 1, 0);
		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer c) {
				player.getPA().movePlayer(getX(i), getY(i), 0);
				c.stop();
			}
		}, getTime(i) - 400);
		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer c) {
				player.getPA().sendFrame36(75, -1);
				player.getPA().closeAllWindows();
				c.stop();
			}
		}, getTime(i));
	}

	public static int getX(int i) {
		return TRAVEL_DATA[i][0];
	}

	public static int getY(int i) {
		return TRAVEL_DATA[i][1];
	}

	public static int getTime(int i) {
		return TRAVEL_DATA[i][2];
	}

}