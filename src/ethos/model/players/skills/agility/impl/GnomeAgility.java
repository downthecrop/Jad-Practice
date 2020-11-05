package ethos.model.players.skills.agility.impl;

import ethos.model.content.achievement_diary.western_provinces.WesternDiaryEntry;
import ethos.model.players.Player;
import ethos.model.players.mode.ModeType;
import ethos.model.players.skills.agility.AgilityHandler;

/**
 * GnomeAgility
 * 
 * @author Andrew (I'm A Boss on Rune-Server and Mr Extremez on Mopar & Runelocus)
 */

public class GnomeAgility {

	private static long clickTimer = 0;

	public static final int LOG_OBJECT = 23145, NET1_OBJECT = 23134, TREE_OBJECT = 23559, ROPE_OBJECT = 23557, TREE_BRANCH_OBJECT = 23560, NET2_OBJECT = 23135,
			PIPES1_OBJECT = 23138, PIPES2_OBJECT = 23139;// gnome
															// course
															// objects

	public boolean gnomeCourse(Player c, int objectId) {
		switch (objectId) {
		case LOG_OBJECT:
			if (c.getAgilityHandler().hotSpot(c, 2474, 3436) || c.getAgilityHandler().hotSpot(c, 2475, 3436) || c.getAgilityHandler().hotSpot(c, 2473, 3436)) {
				if(c.absX != 2474) {
					c.getPlayerAssistant().movePlayer(2474, 3436);
				}
				//c.getAgilityHandler().move(c, 0, -7, c.getAgilityHandler().getAnimation(objectId), -1);
				c.setForceMovement(2474, 3429, 0, 200, "SOUTH", c.getAgilityHandler().getAnimation(objectId));
			} else if (c.absX == 2474 && c.absY > 3429 && c.absY < 3436) {
				c.getPlayerAssistant().movePlayer(2474, 3429, 0);
				c.getAgilityHandler().stopEmote(c);
			}
			c.getAgilityHandler().resetAgilityProgress();
			c.getAgilityHandler().agilityProgress[0] = true;
			c.getAgilityHandler().lapProgress(c, 0, objectId);
			return true;

		case NET1_OBJECT:
			AgilityHandler.delayEmote(c, "CLIMB_UP", c.getX(), c.getY() - 2, 1, 2);
			if (c.getAgilityHandler().agilityProgress[0] == true) {
				c.getAgilityHandler().lapProgress(c, 0, objectId);
				c.getAgilityHandler().agilityProgress[1] = true;
			}
			return true;

		case TREE_OBJECT:
			AgilityHandler.delayEmote(c, "CLIMB_UP", c.getX(), c.getY() - 3, 2, 2);
			if (c.getAgilityHandler().agilityProgress[1] == true) {
				c.getAgilityHandler().lapProgress(c, 1, objectId);
				c.getAgilityHandler().agilityProgress[2] = true;
			}
			return true;

		case ROPE_OBJECT:
			if (c.getAgilityHandler().hotSpot(c, 2477, 3420)) {
			//	c.getAgilityHandler().move(c, 6, 0, c.getAgilityHandler().getAnimation(objectId), -1);
				c.setForceMovement(2483, 3420, 0, 200, "EAST", c.getAgilityHandler().getAnimation(objectId));
			} else if (c.absY == 3420 && c.absX > 2477 && c.absX < 2483) {
				c.getPlayerAssistant().movePlayer(2483, 3420, 2);
				c.getAgilityHandler().stopEmote(c);
			}
			if (c.getAgilityHandler().agilityProgress[2] == true) {
				c.getAgilityHandler().lapProgress(c, 2, objectId);
				c.getAgilityHandler().agilityProgress[3] = true;
			}
			return true;

		case TREE_BRANCH_OBJECT:
			AgilityHandler.delayEmote(c, "CLIMB_DOWN", c.getX(), c.getY(), 0, 2);

			if (c.getAgilityHandler().agilityProgress[3] == true) {
				c.getAgilityHandler().lapProgress(c, 3, objectId);
				c.getAgilityHandler().agilityProgress[4] = true;
			}
			return true;

		case NET2_OBJECT:
			if (System.currentTimeMillis() - clickTimer < 1800) {
				return false;
			}
			if (c.getY() == 3425 && System.currentTimeMillis() - clickTimer > 1800) {
				AgilityHandler.delayEmote(c, "CLIMB_UP", c.getX(), c.getY() + 2, 0, 2);

				clickTimer = System.currentTimeMillis();
				if (c.getAgilityHandler().agilityProgress[4] == true) {
					c.getAgilityHandler().lapProgress(c, 4, objectId);
					c.getAgilityHandler().agilityProgress[5] = true;
				}
			}
			return true;

		case PIPES1_OBJECT:
			if (c.getAgilityHandler().hotSpot(c, 2484, 3430)) {
				//c.getAgilityHandler().move(c, 0, 7, c.getAgilityHandler().getAnimation(objectId), 748);
				c.setForceMovement(2484, 3437, 0, 200, "NORTH", 844);
				c.getDiaryManager().getWesternDiary().progress(WesternDiaryEntry.GNOME_AGILITY);
				c.getAgilityHandler().lapFinished(c, 5, c.getMode().getType().equals(ModeType.OSRS) ? 87 : 6000, 10000);
			} else if (c.absY > 3430 && c.absY < 3436 && System.currentTimeMillis() - clickTimer > 1800) {
				c.getPlayerAssistant().movePlayer(2484, 3437, 0);
				c.getAgilityHandler().stopEmote(c);
			}
			return true;

		case PIPES2_OBJECT:
			if (c.getAgilityHandler().hotSpot(c, 2487, 3430)) {
				//c.getAgilityHandler().move(c, 0, 7, c.getAgilityHandler().getAnimation(objectId), 748);
				c.setForceMovement(2487, 3437, 0, 200, "NORTH", 844);
				c.getDiaryManager().getWesternDiary().progress(WesternDiaryEntry.GNOME_AGILITY);
				c.getAgilityHandler().lapFinished(c, 5, c.getMode().getType().equals(ModeType.OSRS) ? 87 : 6000, 10000);
			} else if (c.absY > 3430 && c.absY < 3436) {
				c.getPlayerAssistant().movePlayer(2487, 3437, 0);
				c.getAgilityHandler().stopEmote(c);
			}
			return true;
		}
		return false;
	}
}
