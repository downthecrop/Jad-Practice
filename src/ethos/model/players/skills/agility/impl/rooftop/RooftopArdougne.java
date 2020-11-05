package ethos.model.players.skills.agility.impl.rooftop;

import ethos.event.CycleEvent;
import ethos.event.CycleEventContainer;
import ethos.event.CycleEventHandler;
import ethos.model.content.achievement_diary.ardougne.ArdougneDiaryEntry;
import ethos.model.players.Player;
import ethos.model.players.mode.ModeType;
import ethos.model.players.skills.agility.AgilityHandler;
import ethos.model.players.skills.agility.MarkOfGrace;

/**
 * Rooftop Agility Ardougne
 * 
 * @author Matt
 */

public class RooftopArdougne {

	public static final int WOODEN_BEAMS = 11405, JUMP_GAP = 11406, PLANK = 11631, JUMP_2ND_GAP = 11429, JUMP_3RD_GAP = 11430, STEEP_ROOF = 11633, JUMP_4TH_GAP = 11630;
	public static int[] ARDOUGNE_OBJECTS = { WOODEN_BEAMS, JUMP_GAP, PLANK, JUMP_2ND_GAP, JUMP_3RD_GAP, STEEP_ROOF, JUMP_4TH_GAP };

	public boolean execute(final Player c, final int objectId) {
		for (int id : ARDOUGNE_OBJECTS) {
			if (System.currentTimeMillis() - c.lastObstacleFail < 3000) {
				return false;
			}
			if (c.getAgilityHandler().checkLevel(c, objectId)) {
				return false;
			}
			if (id == objectId) {
				MarkOfGrace.spawnMarks(c, "ARDOUGNE");
			}
		}
		
		switch (objectId) {
		case WOODEN_BEAMS:
			c.startAnimation(737);
			CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
				int ticks = 0;
				@Override
				public void execute(CycleEventContainer container) {
					if (c.disconnected) {
						stop();
						return;
					}
					switch (ticks++) {
					case 1:
						AgilityHandler.delayEmote(c, "CLIMP_UP_WALL", 2673, 3298, 1, 1);
						break;
						
					case 3:
						c.startAnimation(737);
						AgilityHandler.delayEmote(c, "CLIMP_UP_WALL", 2673, 3298, 2, 1);
						break;
						
					case 5:
						c.startAnimation(737);
						AgilityHandler.delayEmote(c, "CLIMP_UP_WALL", 2671, 3299, 3, 2);
						c.getAgilityHandler().agilityProgress[0] = true;
						container.stop();
						break;
					}
				}

				@Override
				public void stop() {

				}
			}, 1);
			return true;
			
		case JUMP_GAP:
			AgilityHandler.delayEmote(c, "JUMP", 2667, 3311, 1, 1);
			CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
				int ticks = 0;
				@Override
				public void execute(CycleEventContainer container) {
					if (c.disconnected) {
						stop();
						return;
					}
					switch (ticks++) {
					case 3:
						AgilityHandler.delayEmote(c, "JUMP", 2665, 3314, 1, 1);
						break;
						
					case 7:
						AgilityHandler.delayEmote(c, "JUMP", 2665, 3318, 3, 1);
						c.getAgilityHandler().agilityProgress[1] = true;
						container.stop();
						break;
					}
				}

				@Override
				public void stop() {

				}
			}, 1);
			return true;
			
		case PLANK:
			// c.getAgilityHandler().move(c, -6, 0, 762, -1);
			c.setForceMovement(2656, 3318, 0, 200, "WEST", 762);
			c.getAgilityHandler().agilityProgress[2] = true;
			return true;
			
		case JUMP_2ND_GAP:
			AgilityHandler.delayEmote(c, "JUMP", 2653, 3314, 3, 2);
			c.getAgilityHandler().agilityProgress[3] = true;
			return true;
			
		case JUMP_3RD_GAP:
			AgilityHandler.delayEmote(c, "JUMP_DOWN", 2651, 3309, 3, 2);
			c.getAgilityHandler().agilityProgress[4] = true;
			return true;
			
		case STEEP_ROOF:
			c.setForceMovement(2656, 3297, 0, 200, "SOUTH", 762);
			//c.getAgilityHandler().move(c, 3, -3, 762, -1);
			c.getAgilityHandler().agilityProgress[5] = true;
			return true;
			
		case JUMP_4TH_GAP:
			AgilityHandler.delayEmote(c, "JUMP_DOWN", 2658, 3298, 1, 2);
			CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					if (c.disconnected) {
						stop();
						return;
					}
					if (c.absX == 2658 && c.absY == 3298) {
						c.setForceMovement(2661, 3298, 0, 80, "EAST", 0x333);
					} else if (c.absX == 2661 && c.absY == 3298) {
						c.setForceMovement(2663, 3297, 0, 80, "EAST", 3067);
					} else if (c.absX == 2663 && c.absY == 3297) {
						c.setForceMovement(2666, 3297, 0, 80, "EAST", 0x333);
					} else if (c.absX == 2666 && c.absY == 3297) {
						AgilityHandler.delayEmote(c, "JUMP_DOWN", 2668, 3297, 0, 1);
						c.getAgilityHandler().roofTopFinished(c, 5, c.getMode().getType().equals(ModeType.OSRS) ? 793 : 20000, 4000);
						c.getDiaryManager().getArdougneDiary().progress(ArdougneDiaryEntry.ARDOUGNE_ROOFTOP);
						container.stop();
					}
				}
				@Override
				public void stop() {

				}
			}, 1);
			return true;
		}
		return false;
	}
}
