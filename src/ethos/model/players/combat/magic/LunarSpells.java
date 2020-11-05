package ethos.model.players.combat.magic;

import java.util.concurrent.TimeUnit;

import ethos.event.CycleEvent;
import ethos.event.CycleEventContainer;
import ethos.event.CycleEventHandler;
import ethos.model.entity.HealthStatus;
import ethos.model.players.Boundary;
import ethos.model.players.ClientGameTimer;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.model.players.combat.Hitmark;

public class LunarSpells extends MagicRequirements {

	public static boolean castOnOtherSpells(Player c) {
		int[] spells = { 12435, 12455, 12425, 30298, 30290, 30282, };
		for (int i = 0; i < spells.length; i++) {
			if (c.spellId == spells[i]) {
				return true;
			}
		}
		return false;
	}
	
	public static void lunarButton(final Player c, int actionButtonId) {
		switch (actionButtonId) {
//		case 117226:
//			Dream(c);
//			break;
		}
	}
	
	@SuppressWarnings("unused")
	public static void CastingLunarOnPlayer(final Player c, int castingSpellId) {
		final Player castOnPlayer = (Player) PlayerHandler.players[c.playerIndex];
		c.turnPlayerTo(castOnPlayer.absX, castOnPlayer.absY);
		c.stopMovement();
		c.getCombat().resetPlayerAttack();
		if (castOnPlayer == null) {
			return;
		}
		switch(castingSpellId) {
			case 30130:
				if (!castOnPlayer.acceptAid) {
					c.sendMessage("This player is currently not accepting aid.");
					return;
				}
				if (!c.getCombat().checkMagicReqs(86)) {
					return;
				}
				c.startAnimation(6293);
				c.gfx0(1060);
				c.getPA().sendFrame126("Stats of: "+castOnPlayer.playerName, 8144);
				c.getPA().sendFrame126(""+castOnPlayer.playerName+"'s Attack Level: "+castOnPlayer.playerLevel[0]+"/"+castOnPlayer.getLevelForXP(castOnPlayer.playerXP[0])+"", 8147);
				c.getPA().sendFrame126(""+castOnPlayer.playerName+"'s Strength Level: "+castOnPlayer.playerLevel[2]+"/"+castOnPlayer.getLevelForXP(castOnPlayer.playerXP[2])+"", 8148);
				c.getPA().sendFrame126(""+castOnPlayer.playerName+"'s Defence Level: "+castOnPlayer.playerLevel[1]+"/"+castOnPlayer.getLevelForXP(castOnPlayer.playerXP[1])+"", 8149);
				c.getPA().sendFrame126(""+castOnPlayer.playerName+"'s Hitpoints Level: "+castOnPlayer.playerLevel[3]+"/"+castOnPlayer.getLevelForXP(castOnPlayer.playerXP[3])+"", 8150);
				c.getPA().sendFrame126(""+castOnPlayer.playerName+"'s Range Level: "+castOnPlayer.playerLevel[4]+"/"+castOnPlayer.getLevelForXP(castOnPlayer.playerXP[4])+"", 8151);
				c.getPA().sendFrame126(""+castOnPlayer.playerName+"'s Prayer Level: "+castOnPlayer.playerLevel[5]+"/"+castOnPlayer.getLevelForXP(castOnPlayer.playerXP[5])+"", 8152);
				c.getPA().sendFrame126(""+castOnPlayer.playerName+"'s Magic Level: "+castOnPlayer.playerLevel[6]+"/"+castOnPlayer.getLevelForXP(castOnPlayer.playerXP[6])+"", 8153);
				c.getPA().showInterface(8134);
				castOnPlayer.gfx0(736);
			break;
			case 30298:
				if (!castOnPlayer.acceptAid) {
					c.sendMessage("This player is currently not accepting aid.");
					return;
				}
				if (!c.getCombat().checkMagicReqs(89)) {
					return;
				}
				if (System.currentTimeMillis() - c.lastCast < 30000) {
					c.sendMessage("You can only cast vengeance every 30 seconds.");
					return;
				}
				if (castOnPlayer.vengOn) {
					c.sendMessage("This player already have vengeance activated.");
					return;
				}
				c.getPA().sendGameTimer(ClientGameTimer.VENGEANCE, TimeUnit.SECONDS, 30);
				castOnPlayer.vengOn = true;
				c.lastCast = System.currentTimeMillis();
				castOnPlayer.gfx100(725);
				c.getPA().refreshSkill(6);
				c.startAnimation(4411);
			break;
			case 30048:
				if (!castOnPlayer.acceptAid) {
					c.sendMessage("This player is currently not accepting aid.");
					return;
				}
				if (!c.getCombat().checkMagicReqs(85)) {
					return;
				}
				if (!castOnPlayer.getHealth().getStatus().isPoisoned()) {
					c.sendMessage("This player is currently not poisoned.");
					return;
				}
				castOnPlayer.getHealth().resolveStatus(HealthStatus.POISON, 100);
				castOnPlayer.sendMessage("You have been cured by " + c.playerName + ".");
				castOnPlayer.gfx100(738);
				c.startAnimation(4411);
			break;
			
			case 30290:
				if (castOnPlayer.inClanWars() || castOnPlayer.inClanWarsSafe()) {
					c.sendMessage("@cr10@This spell can not be used at the district");
					return;
				}
				if (!castOnPlayer.acceptAid) {
					c.sendMessage("This player is currently not accepting aid.");
					return;
				}
				double hpPercent = c.getHealth().getAmount() * 0.75;
				if (!c.getCombat().checkMagicReqs(88)) {
					return;
				}
				if (c.playerLevel[3] - c.playerLevel[3] * .75 < 1) {
					c.sendMessage("Your hitpoints are too low to do this!");
					return;
				}
				if (castOnPlayer.getHealth().getAmount() + (int) hpPercent >= castOnPlayer.getLevelForXP(castOnPlayer.playerXP[3])) {
					if (castOnPlayer.getHealth().getAmount() > (int) hpPercent) {
						hpPercent = (castOnPlayer.getHealth().getAmount() - (int) hpPercent);
					} else {
						hpPercent = ((int) hpPercent - castOnPlayer.getHealth().getAmount());
					}
				}
				if (castOnPlayer.getHealth().getAmount() >= castOnPlayer.getLevelForXP(castOnPlayer.playerXP[3])) {
					c.sendMessage("This player already have full hitpoints.");
					castOnPlayer.getHealth().reset();
					return;
				}
				c.appendDamage((int) hpPercent, hpPercent >= 1.0 ? Hitmark.HIT : Hitmark.MISS);
				
				castOnPlayer.getHealth().increase((int) hpPercent);
				c.updateRequired = true;

				c.startAnimation(4411);
				castOnPlayer.gfx100(738);
				c.gfx100(727);
			break;
			case 30282: 
				if (castOnPlayer.inClanWars() || castOnPlayer.inClanWarsSafe()) {
					c.sendMessage("@cr10@This spell can not be used at the district");
					return;
				}
				if (Boundary.isIn(castOnPlayer, Boundary.DUEL_ARENA)) {
					c.sendMessage("You cannot use this spell in the duel arena.");
					return;
				}
				if (!castOnPlayer.acceptAid) {
					c.sendMessage("This player is currently not accepting aid.");
					return;
				}
				if (!c.getCombat().checkMagicReqs(87)) {
					return;
				}
				if (castOnPlayer.specAmount >= 10) {
					c.sendMessage("This player already have full special energy.");
					return;
				}
				if (c.specAmount < 5) {
					c.sendMessage("You do not have enough special energy to transfer.");
					return;
				}
				c.startAnimation(4411);
				castOnPlayer.gfx0(734);
				castOnPlayer.specAmount += 5;
				c.specAmount -= 5;
				c.getItems().updateSpecialBar();
				castOnPlayer.getItems().updateSpecialBar();
				castOnPlayer.sendMessage("Your special energy has been restored by 50%!");
				c.sendMessage("You transfer 50% of your energy to " + castOnPlayer.playerName + ".");
			break;
		}
	}

	@SuppressWarnings("unused")
	private static void Dream(final Player c) {
		//ticks = 0;
		CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer event) {
				if (c.getHealth().getAmount() == c.getLevelForXP(c.playerXP[3])) {
					c.sendMessage("You already have full hitpoints");
					event.stop();
					return;
				}
				if (c.dreamTime == 0) {
					c.startAnimation(6295);
					c.sendMessage("The sleeping has an effect on your health");
				} else if (c.dreamTime == 2) {
					c.startAnimation(6296);
				} else if (c.dreamTime > 2) {
					c.gfx0(1056);
					c.getHealth().increase(5);
					c.getPA().refreshSkill(3);
					if (c.getHealth().getAmount() == c.getLevelForXP(c.playerXP[3])) {
						c.sendMessage("You wake up from your dream..");
						c.gfx0(-1);
						c.startAnimation(6297);
						c.dreamTime = 0;
						event.stop();
					}
				}
				c.dreamTime++;
			}
			@Override
			public void stop() {	
			}
		}, 2);
	}

	public static void teleOtherDistance(Player c, int type, int i) {
		Player castOn = PlayerHandler.players[i];
		int[][] data = { { 74, SOUL, LAW, EARTH, 1, 1, 1 }, { 82, SOUL, LAW, WATER, 1, 1, 1 }, { 90, SOUL, LAW, -1, 2, 1, -1 }, };
		if (!hasRequiredLevel(c, data[type][0])) {
			c.sendMessage("You need to have a magic level of " + data[type][0] + " to cast this spell.");
			return;
		}
		if (!hasRunes(c, new int[] { data[type][1], data[type][2], data[type][3] }, new int[] { data[type][4], data[type][5], data[type][6] })) {
			return;
		}
		deleteRunes(c, new int[] { data[type][1], data[type][2], data[type][3] }, new int[] { data[type][4], data[type][5], data[type][6] });
		String[] location = { "Lumbridge", "Falador", "Camelot", };
		c.faceUpdate(i + 32768);
		c.startAnimation(1818);
		c.gfx0(343);
		if (castOn != null) {
			if (castOn.distanceToPoint(c.absX, c.absY) <= 15) {
				if (c.heightLevel == castOn.heightLevel) {
					castOn.getPA().sendFrame126(location[type], 12560);
					castOn.getPA().sendFrame126(c.playerName, 12558);
					castOn.getPA().showInterface(12468);
					castOn.teleotherType = type;
				}
			}
		}
	}

	public static void teleOtherLocation(final Player c, final int i, boolean decline) {
		c.getPA().removeAllWindows();
		final int[][] coords = { { 3222, 3218 }, // LUMBRIDGE
				{ 2964, 3378 }, // FALADOR
				{ 2757, 3477 }, // CAMELOT
		};
		if (!decline) {
			CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					c.startAnimation(715);
					c.teleportToX = coords[c.teleotherType][0];
					c.teleportToY = coords[c.teleotherType][1];
					c.teleotherType = -1;
					container.stop();
				}

				@Override
				public void stop() {

				}
			}, 3);
			c.startAnimation(1816);
			c.gfx100(342);
		}
	}
}