package ethos.model.players.skills.agility.impl.rooftop;

import ethos.event.CycleEvent;
import ethos.event.CycleEventContainer;
import ethos.event.CycleEventHandler;
import ethos.model.content.achievement_diary.varrock.VarrockDiaryEntry;
import ethos.model.players.Player;
import ethos.model.players.combat.Hitmark;
import ethos.model.players.mode.ModeType;
import ethos.model.players.skills.agility.AgilityHandler;
import ethos.model.players.skills.agility.MarkOfGrace;

/**
 * Rooftop Agility Varrock
 * 
 * @author Matt
 */

public class RooftopVarrock {

	public static final int ROUGH_WALL = 10586, 
			CLOTHES_LINE = 10587, LEAP_GAP = 10642, 
			BALANCE_WALL_JUMP = 10777, LEAP_2ND_GAP = 10778, 
			LEAP_3RD_GAP = 10779, LEAP_4TH_GAP = 10780,
			HURDLE_LEDGE = 10781, JUMP_OFF_EDGE = 10817;
	
	public static int[] VARROCK_OBJECTS = { ROUGH_WALL, CLOTHES_LINE, LEAP_GAP, BALANCE_WALL_JUMP, LEAP_2ND_GAP, LEAP_3RD_GAP, LEAP_4TH_GAP, HURDLE_LEDGE, JUMP_OFF_EDGE };

	public boolean execute(final Player c, final int objectId) {
		
		for (int id : VARROCK_OBJECTS) {
			if (System.currentTimeMillis() - c.lastObstacleFail < 3000) {
				return false;
			}
			if (c.getAgilityHandler().checkLevel(c, objectId)) {
				return false;
			}
			if (id == objectId) {
				MarkOfGrace.spawnMarks(c, "VARROCK");
			}
		}
		
		switch (objectId) {
		case ROUGH_WALL:
			AgilityHandler.delayEmote(c, "CLIMB_UP", 3219, 3414, 3, 2);
			c.getAgilityHandler().agilityProgress[0] = true;
			return true;
			
		case CLOTHES_LINE:
			if (AgilityHandler.failObstacle(c, 3212, 3414, 0)) {
				return false;
			}
			if (c.getAgilityHandler().hotSpot(c, 3214, 3414)) {
				c.setForceMovement(3208, 3414, 0, 100, "WEST", 741);
				c.getAgilityHandler().agilityProgress[1] = true;
			}
			return true;
			
		case LEAP_GAP:
			AgilityHandler.delayEmote(c, "JUMP", 3197, 3416, 1, 2);
			c.getAgilityHandler().agilityProgress[2] = true;
			return true;
			
		case BALANCE_WALL_JUMP:
			//AgilityHandler.delayEmote(c, "JUMP", 3190, 3414, 1, 3);
			c.startAnimation(3067);
			c.setForceMovement(3190, 3414, 0, 20, "WEST", -1);
			c.getAgilityHandler().agilityProgress[3] = true;
			CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					if (c.disconnected) {
						stop();
						return;
					}
					if (c.absX == 3190 && c.absY == 3414) {
						c.setForceMovement(3190, 3410, 0, 150, "SOUTH", 3060);
					} else if (c.absX == 3190 && c.absY == 3410) {
						c.setForceMovement(3190, 3405, 0, 150, "SOUTH", 756);
					} else if (c.absX == 3190 && c.absY == 3405) {
						c.turnPlayerTo(3192, 3405);
						AgilityHandler.delayEmote(c, "JUMP", 3192, 3405, 3, 2);
						c.getAgilityHandler().agilityProgress[3] = true;
						container.stop();
					}
				}

				@Override
				public void stop() {

				}
			}, 2);
			return true;
			
		case LEAP_2ND_GAP:
			if (c.getAgilityHandler().agilityProgress[3] == true) {
				AgilityHandler.delayEmote(c, "JUMP", c.absX, 3398, 3, 2);
				c.getAgilityHandler().agilityProgress[4] = true;
			} else {
				c.appendDamage(1, Hitmark.HIT);
				c.sendMessage("Apperantly i skipped a gap, ouch..");
			}
			return true;
			
		case LEAP_3RD_GAP:
			AgilityHandler.delayEmote(c, "JUMP", 3215, 3399, 3, 2);
			c.getAgilityHandler().agilityProgress[5] = true;
			return true;
			
		case LEAP_4TH_GAP:
			if (c.getAgilityHandler().agilityProgress[5] == true) {
				AgilityHandler.delayEmote(c, "JUMP", 3236, 3403, 3, 2);
				c.getAgilityHandler().agilityProgress[6] = true;
			} else {
				c.appendDamage(1, Hitmark.HIT);
				c.sendMessage("Apperantly i skipped a gap, ouch..");
			}
			return true;
			
		case HURDLE_LEDGE:
			AgilityHandler.delayEmote(c, "CLIMB_UP", 3236, 3410, 3, 2);
			c.getAgilityHandler().agilityProgress[7] = true;
			return true;
			
		case JUMP_OFF_EDGE:
			c.getAgilityHandler().roofTopFinished(c, 7, c.getMode().getType().equals(ModeType.OSRS) ? 238 : 10000, 8000);
			AgilityHandler.delayEmote(c, "JUMP", 3236, 3417, 0, 2);
			c.getDiaryManager().getVarrockDiary().progress(VarrockDiaryEntry.VARROCK_ROOFTOP);
			return true;
		}
		return false;
	}

}
