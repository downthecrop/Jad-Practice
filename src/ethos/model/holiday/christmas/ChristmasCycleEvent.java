package ethos.model.holiday.christmas;

import ethos.Config;
import ethos.event.CycleEvent;
import ethos.event.CycleEventContainer;
import ethos.model.holiday.HolidayController;
import ethos.model.players.PlayerHandler;

public class ChristmasCycleEvent extends CycleEvent {

	@Override
	public void execute(CycleEventContainer container) {
		Christmas christmas = (Christmas) container.getOwner();
		if (christmas == null) {
			container.stop();
			return;
		}
		if (!HolidayController.CHRISTMAS.isActive()) {
			PlayerHandler.executeGlobalMessage("@red@The Christmas event is officially over. Enjoy the rest of your Holidays.");
			christmas.finalizeHoliday();
			container.stop();
			return;
		}
		christmas.getSnowball().update();
		for (int i = 0; i < Config.AMOUNT_OF_SANTA_MINIONS; i++) {
			christmas.getMinion().update();
		}
	}

}
