package ethos.event.impl;

import ethos.event.Event;
import ethos.model.players.Player;
import ethos.util.Misc.Direction;

public class WildernessDitchJumpEvent extends Event<Player> {

	public WildernessDitchJumpEvent(Player attachment, Direction direction) {
		super("wilderness_ditch", attachment, 1);
	}

	@Override
	public void execute() {
		if ((attachment.absY == 3520 || attachment.absY == 3523) && super.getElapsedTicks() == 1) {
			// attachment.startAnimation(6132);
			// attachment.playerWalkIndex = 6132;
			attachment.turnPlayerTo(attachment.objectX, attachment.objectY);
			// attachment.setForceMovement(0, -3, 0, 100, 2, 6132);
			attachment.getAgilityHandler().move(attachment, 0, 3, 6132, -1);
		}
		if (super.getElapsedTicks() > 3) {
			stop();
		}
	}

	@Override
	public void stop() {
		super.stop();
		if (attachment != null && !attachment.disconnected) {
			attachment.stopAnimation();
		}
	}

}
