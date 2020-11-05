package ethos.model.players;

import ethos.event.CycleEvent;
import ethos.event.CycleEventContainer;
import ethos.event.CycleEventHandler;

/**
 * Class WildernessDitch Handles Crossing the wilderness ditch
 * 
 * @author Organic 5-4-2012
 */

public class WildernessDitch {

	private static final int EMOTE = 6132;
	private static int AMOUNT_TO_MOVE = 3;

	private static void setAnimationBack(Player c) {
		c.isRunning2 = true;
		c.getPA().sendFrame36(173, 1);
		c.playerWalkIndex = 0x333;
		c.getPA().requestUpdates();
	}

	public static void movePlayer(Player c, int x, int y) {
		c.resetWalkingQueue();
		c.teleportToX = x;
		c.teleportToY = y;
		c.getPA().requestUpdates();
	}

	public static void wildernessDitchEnter(final Player c) {
		c.setForceMovement(c.absX, 3523, 0, 10, "NORTH", 0);
		if (c.stopPlayerPacket) {
			return;
		}
		c.stopPlayerPacket = true;
		c.startAnimation(EMOTE);
		CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (c.absY <= 3523) {
					container.stop();
				} else if (c.absX <= 2998) {
					container.stop();
				}
			}

			@Override
			public void stop() {
			}
		}, 1);
		CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				movePlayer(c, c.absX, c.absY + AMOUNT_TO_MOVE);
				if (c.absY <= 3523) {
					container.stop();
				} else if (c.absX <= 2998) {
					container.stop();
				}
			}

			@Override
			public void stop() {
				setAnimationBack(c);
				c.stopPlayerPacket = false;
			}
		}, 2);
	}

	public static void wildernessDitchLeave(final Player c) {
		c.setForceMovement(c.absX, 3520, 0, 10, "SOUTH", 0);
		if (c.stopPlayerPacket) {
			return;
		}
		c.stopPlayerPacket = true;
		c.startAnimation(EMOTE);
		CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (c.absY <= 3523) {
					container.stop();
				} else if (c.absX <= 2995) {
					container.stop();
				}
			}

			@Override
			public void stop() {
			}
		}, 1);
		CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				movePlayer(c, c.absX, c.absY - AMOUNT_TO_MOVE);
				if (c.absY <= 3523) {
					container.stop();
				} else if (c.absX <= 2995) {
					container.stop();
				}
			}

			@Override
			public void stop() {
				setAnimationBack(c);
				c.stopPlayerPacket = false;
			}
		}, 2);
	}
}