package ethos.event.impl;

import ethos.event.Event;
import ethos.model.players.Player;

public class StaffOfTheDeadEvent extends Event<Player> {

	public StaffOfTheDeadEvent(Player attachment) {
		super("staff_of_the_dead", attachment, 1);
	}

	@Override
	public void execute() {
		if (attachment == null || attachment.disconnected) {
			super.stop();
			return;
		}
		if (attachment.playerEquipment[attachment.playerWeapon] != 11791 && attachment.playerEquipment[attachment.playerWeapon] != 12904) {
			super.stop();
			return;
		}
		if (super.getElapsedTicks() > 100) {
			super.stop();
			return;
		}
	}

}
