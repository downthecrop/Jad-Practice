package ethos.model.players.skills.agility.impl.rooftop;

import ethos.event.CycleEvent;
import ethos.event.CycleEventContainer;
import ethos.event.CycleEventHandler;
import ethos.model.content.achievement_diary.kandarin.KandarinDiaryEntry;
import ethos.model.content.dailytasks.DailyTasks;
import ethos.model.content.dailytasks.DailyTasks.PossibleTasks;
import ethos.model.players.Player;
import ethos.model.players.mode.ModeType;
import ethos.model.players.skills.agility.AgilityHandler;
import ethos.model.players.skills.agility.MarkOfGrace;

/**
 * Rooftop Agility Seers
 * 
 * @author Matt
 */

public class RooftopSeers {

	public static final int WALL = 11373, JUMP_GAP = 11374, TIGHT_ROPE = 11378, JUMP_2ND_GAP = 11375, JUMP_3RD_GAP = 11376, JUMP_EDGE = 11377;
	
	public static int[] SEERS_OBJECTS = { WALL, JUMP_GAP, TIGHT_ROPE, JUMP_2ND_GAP, JUMP_3RD_GAP, JUMP_EDGE };

	public boolean execute(final Player c, final int objectId) {
		
		for (int id : SEERS_OBJECTS) {
			if (System.currentTimeMillis() - c.lastObstacleFail < 3000) {
				return false;
			}
			if (c.getAgilityHandler().checkLevel(c, objectId)) {
				return false;
			}
			if (id == objectId) {
				MarkOfGrace.spawnMarks(c, "SEERS");
			}
		}
		
		switch (objectId) {
		case WALL:
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
					case 0:
						AgilityHandler.delayEmote(c, "CLIMP_UP_WALL", 2729, 3489, 1, 1);
						break;
						
					case 2:
						c.startAnimation(1118);
						AgilityHandler.delayEmote(c, "HANG_ON_POST", 2729, 3491, 3, 1);
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
			if (AgilityHandler.failObstacle(c, 2720, 3493, 0)) {
				return false;
			}
				CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
					int ticks = 0;
					@Override
					public void execute(CycleEventContainer container) {
						if (c.disconnected) {
							stop();
							return;
						}
						switch (ticks++) {
						case 0:
							AgilityHandler.delayEmote(c, "JUMP_DOWN", 2719, 3495, 2, 1);
							break;
							
						case 3:
							AgilityHandler.delayEmote(c, "JUMP", 2713, 3494, 2, 2);
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
			
		case TIGHT_ROPE:
			if (AgilityHandler.failObstacle(c, 2710, 3486, 0)) {
				return false;
			}
			c.setForceMovement(2710, 3481, 0, 220, "SOUTH", 762);
			c.getAgilityHandler().agilityProgress[2] = true;
			return true;
			
		case JUMP_2ND_GAP:
			if (AgilityHandler.failObstacle(c, 2711, 3475, 0)) {
				return false;
			}
			
			AgilityHandler.delayEmote(c, "JUMP", 2712, 3472, 3, 2);
			c.getAgilityHandler().agilityProgress[3] = true;
			return true;
			
		case JUMP_3RD_GAP:
			if (AgilityHandler.failObstacle(c, 2720, 3493, 0)) {
				return false;
			}
			
			AgilityHandler.delayEmote(c, "JUMP", 2700, 3465, 2, 2);
			c.getAgilityHandler().agilityProgress[4] = true;
			return true;
			
		case JUMP_EDGE:
			AgilityHandler.delayEmote(c, "JUMP", 2704, 3464, 0, 2);
			c.getAgilityHandler().roofTopFinished(c, 4, c.getMode().getType().equals(ModeType.OSRS) ? 570 : 16200, 6000);
			c.getDiaryManager().getKandarinDiary().progress(KandarinDiaryEntry.SEERS_AGILITY);
			DailyTasks.increase(c, PossibleTasks.SEERS_COURSE);
			return true;
		}
		return false;
	}

}
