package ethos.model.players.skills.agility.impl.rooftop;

import ethos.model.players.Player;
import ethos.model.players.skills.agility.AgilityHandler;

/**
 * Rooftop Agility Falador
 * 
 * @author Matt
 */

public class RooftopFalador {

	public static final int ROUGH_WALL = 10833, TIGHT_ROPE = 10834;

	public boolean execute(final Player c, final int objectId) {
		switch (objectId) {
		case ROUGH_WALL:
			AgilityHandler.delayEmote(c, "CLIMB_UP", 3036, 3343, 3, 2);
			c.getAgilityHandler().agilityProgress[0] = true;
			return true;
		case TIGHT_ROPE:
			if (c.getAgilityHandler().hotSpot(c, 3039, 3343))
				c.getAgilityHandler().move(c, 8, 0, 762, -1);
			c.getAgilityHandler().agilityProgress[1] = true;
			return true;
		}
		/*
		 * 
		 * To be continued..
		 * 
		 */
		return false;
	}

}
