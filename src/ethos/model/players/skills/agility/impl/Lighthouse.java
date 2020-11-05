package ethos.model.players.skills.agility.impl;

import org.apache.commons.lang3.Range;

import ethos.model.players.Player;
import ethos.model.players.combat.Hitmark;
import ethos.model.players.skills.agility.AgilityHandler;
import ethos.util.Misc;

public class Lighthouse {

	public static final int BASALT_ROCK = 4551, BASALT_ROCK_2_1 = 4552, BASALT_ROCK_2_2 = 4553, BASALT_ROCK_3_1 = 4554, BASALT_ROCK_3_2 = 4555, BASALT_ROCK_4_1 = 4556,
			BASALT_ROCK_4_2 = 4557, BASALT_ROCK_5_1 = 4558, BASALT_ROCK_5_2 = 4559;

	public static final Range<Integer> ROCK_IDS = Range.between(4551, 4559);

	public boolean execute(final Player c, final int objectId) {
		if (!ROCK_IDS.contains(objectId)) {
			return false;
		}

		if (Misc.passedProbability(Range.between(0, 100), 30)) {
			AgilityHandler.delayEmote(c, "FAIL", 2519, 3594, 0, 2);
			c.appendDamage(5, Hitmark.HIT);
			c.sendMessage("You slipped and hurt yourself.");
			return false;
		}

		switch (objectId) {
		case BASALT_ROCK:
			if (c.getAgilityHandler().checkLevel(c, objectId)) {
				return false;
			}
			if (c.getAgilityHandler().hotSpot(c, 2522, 3595) || c.getAgilityHandler().hotSpot(c, 2522, 3594)) {
				c.getAgilityHandler().move(c, 0, 5, 1604, -1);
			}

			return true;
		case BASALT_ROCK_2_1:
		case BASALT_ROCK_2_2:
			if (c.getAgilityHandler().hotSpot(c, 2522, 3600)) {
				c.getAgilityHandler().move(c, 0, 2, 4721, -1);
			}
			return true;
		case BASALT_ROCK_3_1:
		case BASALT_ROCK_3_2:
			if (c.getAgilityHandler().hotSpot(c, 2518, 3611)) {
				c.getAgilityHandler().move(c, -2, 0, 4721, -1);
			}
			return true;
		case BASALT_ROCK_4_1:
		case BASALT_ROCK_4_2:
			if (c.getAgilityHandler().hotSpot(c, 2514, 3613)) {
				c.getAgilityHandler().move(c, 0, 2, 4721, -1);
			}
			return true;
		case BASALT_ROCK_5_1:
		case BASALT_ROCK_5_2:
			if (c.getAgilityHandler().hotSpot(c, 2514, 3616) || c.getAgilityHandler().hotSpot(c, 2514, 3617)) {
				c.getAgilityHandler().move(c, 0, 2, 1604, -1);
			}
			return true;
		}
		return false;
	}

}
