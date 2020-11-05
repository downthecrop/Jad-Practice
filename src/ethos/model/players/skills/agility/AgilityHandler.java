package ethos.model.players.skills.agility;

import ethos.event.CycleEvent;
import ethos.event.CycleEventContainer;
import ethos.event.CycleEventHandler;
import ethos.model.content.achievement.AchievementType;
import ethos.model.content.achievement.Achievements;
import ethos.model.items.Item;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.model.players.combat.Hitmark;
import ethos.model.players.skills.agility.impl.BarbarianAgility;
import ethos.model.players.skills.agility.impl.GnomeAgility;
import ethos.model.players.skills.agility.impl.Lighthouse;
import ethos.model.players.skills.agility.impl.Shortcuts;
import ethos.model.players.skills.agility.impl.WildernessAgility;
import ethos.model.players.skills.agility.impl.rooftop.RooftopArdougne;
import ethos.model.players.skills.agility.impl.rooftop.RooftopSeers;
import ethos.model.players.skills.agility.impl.rooftop.RooftopVarrock;
import ethos.util.Misc;

/**
 * AgilityHandler
 * 
 * @author Andrew (I'm A Boss on Rune-Server and Mr Extremez on Mopar & Runelocus)
 */

public class AgilityHandler {

	public boolean[] agilityProgress = new boolean[8];
	public int lapBonus = 0;

	public static final int LOG_EMOTE = 762, 
							PIPES_EMOTE = 844, 
							CLIMB_UP_EMOTE = 828, 
							CLIMB_DOWN_EMOTE = 827, 
							CLIMB_UP_MONKEY_EMOTE = 3487, 
							WALL_EMOTE = 840, 
							JUMP_EMOTE = 3067,
							FAIL_EMOTE = 770,
							CRAWL_EMOTE = 844;

	public int jumping, jumpingTimer = 0, agilityTimer = -1, moveHeight = -1, tropicalTreeUpdate = -1, zipLine = -1;

	public boolean barbarianRope = false, barbarianLog = false, barbarianNet = false, barbarianStairs = false, barbarianWallOne = false, barbarianWallTwo = false,
			barbarianWallThree = false;

	private int moveX, moveY, moveH;

	public void resetAgilityProgress() {
		for (int i = 0; i < 8; i++) {
			agilityProgress[i] = false;
		}
		lapBonus = 0;
	}

	/**
	 * sets a specific emote to walk to point x
	 */
	public void walkToEmote(Player c, int id) {
		c.isRunning2 = false;
		c.playerWalkIndex = id;
		c.getPA().requestUpdates();
	}

	/**
	 * resets the player animation
	 */
	public void stopEmote(Player c) {
		c.getPlayerAction().setAction(false);
		c.getPlayerAction().canWalk(true);
		c.getPA().requestUpdates();
		c.isRunning2 = true;
	}

	public void move(Player c, int EndX, int EndY, int Emote, int endingAnimation) {
		if (c.getItems().isWearingItem(4084)) {
			c.sendMessage("It would seem to be dangerous doing this.. on a sled.. right?");
			return;
		}
		c.getPlayerAction().setAction(true);
		c.getPlayerAction().canWalk(false);
		walkToEmote(c, Emote);
		c.getPA().walkTo2(EndX, EndY);
		destinationReached(c, EndX, EndY, endingAnimation);
	}

	/**
	 * when a player reaches he's point the stopEmote() method gets called this method calculates when the player reached he's point
	 */
	public void destinationReached(final Player c, int x2, int y2, final int endingEmote) {
		if (x2 >= 0 && y2 >= 0 && x2 != y2) {
			CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					if (c.disconnected) {
						container.stop();
						return;
					}
					if (moveHeight >= 0) {
						c.getPA().movePlayer(c.getX(), c.getY(), moveHeight);
						moveHeight = -1;
					}
					stopEmote(c);
					c.startAnimation(endingEmote);
					container.stop();
				}
				@Override
				public void stop() {
					if (c != null && !c.disconnected) {
						if (c.playerEquipment[c.playerWeapon] == -1) {
							c.playerStandIndex = 0x328;
							c.playerTurnIndex = 0x337;
							c.playerWalkIndex = 0x333;
							c.playerTurn180Index = 0x334;
							c.playerTurn90CWIndex = 0x335;
							c.playerTurn90CCWIndex = 0x336;
							c.playerRunIndex = 0x338;
						} else {
							c.getCombat().getPlayerAnimIndex(Item.getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
						}
					}
				}
			}, x2 + y2);
		} else if (x2 == y2) {
			CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					if (c.disconnected) {
						container.stop();
						return;
					}
					if (moveHeight >= 0) {
						c.getPA().movePlayer(c.getX(), c.getY(), moveHeight);
						moveHeight = -1;
					}
					stopEmote(c);
					c.startAnimation(endingEmote);
					container.stop();
				}

				@Override
				public void stop() {
					if (c != null && !c.disconnected) {
						if (c.playerEquipment[c.playerWeapon] == -1) {
							c.playerStandIndex = 0x328;
							c.playerTurnIndex = 0x337;
							c.playerWalkIndex = 0x333;
							c.playerTurn180Index = 0x334;
							c.playerTurn90CWIndex = 0x335;
							c.playerTurn90CCWIndex = 0x336;
							c.playerRunIndex = 0x338;
						} else {
							c.getCombat().getPlayerAnimIndex(Item.getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
						}
					}
				}
			}, x2);
		} else if (x2 < 0) {
			CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					if (c.disconnected) {
						container.stop();
						return;
					}
					if (moveHeight >= 0) {
						c.getPA().movePlayer(c.getX(), c.getY(), moveHeight);
						moveHeight = -1;

					}
					stopEmote(c);
					c.startAnimation(endingEmote);
					container.stop();
				}

				@Override
				public void stop() {
					if (c != null && !c.disconnected) {
						if (c.playerEquipment[c.playerWeapon] == -1) {
							c.playerStandIndex = 0x328;
							c.playerTurnIndex = 0x337;
							c.playerWalkIndex = 0x333;
							c.playerTurn180Index = 0x334;
							c.playerTurn90CWIndex = 0x335;
							c.playerTurn90CCWIndex = 0x336;
							c.playerRunIndex = 0x338;
						} else {
							c.getCombat().getPlayerAnimIndex(Item.getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
						}
					}
				}
			}, -x2 + y2);
		} else if (y2 < 0) {
			CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					if (c.disconnected) {
						container.stop();
						return;
					}
					if (moveHeight >= 0) {
						c.getPA().movePlayer(c.getX(), c.getY(), moveHeight);
						moveHeight = -1;

					}
					stopEmote(c);
					c.startAnimation(endingEmote);
					container.stop();
				}

				@Override
				public void stop() {
					if (c != null && !c.disconnected) {
						if (c.playerEquipment[c.playerWeapon] == -1) {
							c.playerStandIndex = 0x328;
							c.playerTurnIndex = 0x337;
							c.playerWalkIndex = 0x333;
							c.playerTurn180Index = 0x334;
							c.playerTurn90CWIndex = 0x335;
							c.playerTurn90CCWIndex = 0x336;
							c.playerRunIndex = 0x338;
						} else {
							c.getCombat().getPlayerAnimIndex(Item.getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
						}
					}
				}
			}, x2 - y2);
		}
	}

	/**
	 * @param objectId : the objectId to know how much exp a player receives
	 */

	public double getXp(int objectId) {
		switch (objectId) {
		case GnomeAgility.TREE_OBJECT:
		case GnomeAgility.TREE_BRANCH_OBJECT:
			return 5;
		case GnomeAgility.LOG_OBJECT:
		case GnomeAgility.PIPES1_OBJECT:
		case GnomeAgility.PIPES2_OBJECT:
		case GnomeAgility.NET2_OBJECT:
		case GnomeAgility.NET1_OBJECT:
		case GnomeAgility.ROPE_OBJECT:
			return 7.5;

		case BarbarianAgility.BARBARIAN_SWING_ROPE_OBJECT:
		case BarbarianAgility.BARBARIAN_LOG_BALANCE_OBJECT:
		case BarbarianAgility.BARBARIAN_NET_OBJECT:
		case BarbarianAgility.BARBARIAN_LEDGE_OBJECT:
		case BarbarianAgility.BARBARIAN_LADDER_OBJECT:
		case BarbarianAgility.BARBARIAN_WALL_OBJECT:
			return 14;

		case WildernessAgility.WILDERNESS_PIPE_OBJECT:
			return 12;
		case WildernessAgility.WILDERNESS_SWING_ROPE_OBJECT:
		case WildernessAgility.WILDERNESS_STEPPING_STONE_OBJECT:
		case WildernessAgility.WILDERNESS_LOG_BALANCE_OBJECT:
			return 20;
		case WildernessAgility.WILDERNESS_ROCKS_OBJECT:
			return 0;
		}
		return -1;
	}

	/**
	 * @param objectId : the objectId to fit with the right agility level required
	 */

	private int getLevelRequired(int objectId) {
		switch (objectId) {

		case WildernessAgility.WILDERNESS_PIPE_OBJECT:
		case WildernessAgility.WILDERNESS_SWING_ROPE_OBJECT:
		case WildernessAgility.WILDERNESS_STEPPING_STONE_OBJECT:
		case WildernessAgility.WILDERNESS_ROCKS_OBJECT:
		case WildernessAgility.WILDERNESS_LOG_BALANCE_OBJECT:
			return 52;

		case Lighthouse.BASALT_ROCK:
			return 40;

		case BarbarianAgility.BARBARIAN_SWING_ROPE_OBJECT:
		case BarbarianAgility.BARBARIAN_LOG_BALANCE_OBJECT:
		case BarbarianAgility.BARBARIAN_NET_OBJECT:
		case BarbarianAgility.BARBARIAN_LEDGE_OBJECT:
		case BarbarianAgility.BARBARIAN_LADDER_OBJECT:
		case BarbarianAgility.BARBARIAN_WALL_OBJECT:
			return 35;

		case RooftopSeers.WALL:
			return 60;

		case RooftopVarrock.ROUGH_WALL:
			return 30;
			
		case RooftopArdougne.WOODEN_BEAMS:
			return 90;

		case Shortcuts.SLAYER_TOWER_CHAIN_UP:
			return 61;
			
		case Shortcuts.RELLEKKA_STRANGE_FLOOR:
			return 81;
			
		case Shortcuts.RELLEKKA_CREVICE:
			return 62;
			
		case Shortcuts.STEPPING_STONE:
			return 90;
		}
		return -1;
	}

	/**
	 * @param objectId : the objectId to fit with the right animation played
	 */

	public int getAnimation(int objectId) {
		switch (objectId) {
		case GnomeAgility.LOG_OBJECT:
		case WildernessAgility.WILDERNESS_LOG_BALANCE_OBJECT:
		case BarbarianAgility.BARBARIAN_LOG_BALANCE_OBJECT:
		case GnomeAgility.ROPE_OBJECT:
		case 2332:
			return LOG_EMOTE;
		case 154:
		case 4084:
		case 9330:
		case 9228:
		case 5100:
		case WildernessAgility.WILDERNESS_PIPE_OBJECT:
			return PIPES_EMOTE;
		case WildernessAgility.WILDERNESS_SWING_ROPE_OBJECT:
		case BarbarianAgility.BARBARIAN_SWING_ROPE_OBJECT:
			return 3067;
		case WildernessAgility.WILDERNESS_STEPPING_STONE_OBJECT:
			return 1604; // 2588
		case WildernessAgility.WILDERNESS_ROCKS_OBJECT:
			return 1148;
		case BarbarianAgility.BARBARIAN_LEDGE_OBJECT:
			return 756;
		case BarbarianAgility.BARBARIAN_WALL_OBJECT:
			return 839;
		}
		return -1;
	}
	
	public static void delayFade(final Player c, String emote, final int moveX, final int moveY, final int moveH, String message, String endMessage, int time) {
		if (emote == "CLIMB_DOWN")
			c.startAnimation(CLIMB_DOWN_EMOTE);
		if (emote == "CLIMB_UP")
			c.startAnimation(CLIMB_UP_EMOTE);
		if (emote == "JUMP")
			c.startAnimation(JUMP_EMOTE);
		if (emote == "FAIL")
			c.startAnimation(FAIL_EMOTE);
		if (emote == "CRAWL")
			c.startAnimation(CRAWL_EMOTE);
		if (emote == "NONE") {
			
		}

		c.getPA().sendScreenFade(message, 1, time);
		c.getPlayerAction().setAction(true);
		c.getPlayerAction().canWalk(false);
		c.sendMessage(message);
		CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (c.disconnected) {
					stop();
					return;
				}
				c.getPlayerAction().setAction(false);
				c.getPlayerAction().canWalk(true);
				c.getPA().movePlayer(moveX, moveY, moveH);
				c.sendMessage("..."+ endMessage);
				container.stop();
			}

			@Override
			public void stop() {
			}
		}, time + 2);
	}
	
	public static boolean failedObstacle = false;
	public static boolean failObstacle(final Player c, int x, int y, int z) {
		c.lastObstacleFail = System.currentTimeMillis();
		failedObstacle = false;
		int chance = 10 + c.playerLevel[c.playerAgility] / 13;
		if (Misc.random(chance) == 1) {
			failedObstacle = true;
			AgilityHandler.delayEmote(c, "FAIL", x, y, z, 2);
			c.appendDamage(5, Hitmark.HIT);
			c.sendMessage("You slipped and hurt yourself.");
			c.getAgilityHandler().resetAgilityProgress();
			return true;
		}
		return false;
	}

	/**
	 * climbDown a ladder or anything. small delay before getting teleported to destination
	 */

	public static void delayEmote(final Player c, String emote, final int moveX, final int moveY, final int moveH, int time) {
		switch (emote) {
		case "CLIMB_DOWN":
			c.startAnimation(CLIMB_DOWN_EMOTE);
			break;
		case "CLIMB_UP":
			c.startAnimation(CLIMB_UP_EMOTE);
			break;
		case "FAIL":
			c.startAnimation(FAIL_EMOTE);
			break;
		case "JUMP":
			c.startAnimation(JUMP_EMOTE);
			break;
		case "JUMP_GRAB":
			c.startAnimation(5039);
			break;
		case "BALANCE":
			c.startAnimation(756);
			break;
		case "HANG":
			c.startAnimation(3060);
			break;
		case "JUMP_DOWN":
			c.startAnimation(2586);
			break;
		case "CLIMB_UP_WALL":
			c.startAnimation(737);
			break;
		case "HANG_ON_POST":
			c.startAnimation(1118);
			break;
		case "CRAWL":
			c.startAnimation(CRAWL_EMOTE);
			break;
		}

		c.getPlayerAction().setAction(true);
		c.getPlayerAction().canWalk(false);
		CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (c.disconnected) {
					stop();
					return;
				}
				c.getPlayerAction().setAction(false);
				c.getPlayerAction().canWalk(true);
				c.getPA().movePlayer(moveX, moveY, moveH);
				c.getAgilityHandler().stopEmote(c);
				c.startAnimation(-1);
				container.stop();
			}

			@Override
			public void stop() {
				if (c != null && !c.disconnected) {
					if (c.playerEquipment[c.playerWeapon] == -1) {
						c.playerStandIndex = 0x328;
						c.playerTurnIndex = 0x337;
						c.playerWalkIndex = 0x333;
						c.playerTurn180Index = 0x334;
						c.playerTurn90CWIndex = 0x335;
						c.playerTurn90CCWIndex = 0x336;
						c.playerRunIndex = 0x338;
					} else {
						c.getCombat().getPlayerAnimIndex(Item.getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
					}
				}
			}
		}, time);
	}
	
	/**
	 * climbDown a ladder or anything. small delay before getting teleported to destination
	 */

	public static void delayEmote(final Player c, int emote, final int moveX, final int moveY, final int moveH, int time) {
		c.startAnimation(emote);
		c.getPlayerAction().setAction(true);
		c.getPlayerAction().canWalk(false);
		CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (c.disconnected) {
					stop();
					return;
				}
				c.getPlayerAction().setAction(false);
				c.getPlayerAction().canWalk(true);
				c.getPA().movePlayer(moveX, moveY, moveH);
				c.getAgilityHandler().stopEmote(c);
				c.startAnimation(-1);
				container.stop();
			}

			@Override
			public void stop() {
				if (c != null && !c.disconnected) {
					if (c.playerEquipment[c.playerWeapon] == -1) {
						c.playerStandIndex = 0x328;
						c.playerTurnIndex = 0x337;
						c.playerWalkIndex = 0x333;
						c.playerTurn180Index = 0x334;
						c.playerTurn90CWIndex = 0x335;
						c.playerTurn90CCWIndex = 0x336;
						c.playerRunIndex = 0x338;
					} else {
						c.getCombat().getPlayerAnimIndex(Item.getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
					}
				}
			}
		}, time);
	}

	/**
	 * a specific position the player has to stand on before the action is set to true
	 */

	public boolean hotSpot(Player c, int hotX, int hotY) {
		if (c.getX() == hotX && c.getY() == hotY) {
			return true;
		}
		return false;
	}

	public void lapProgress(Player c, int progress, int obj) {
		if(agilityProgress[progress]) {
			double exp = getXp(obj) * 5;
			c.getPlayerAssistant().addSkillXP((int)exp, 16, true);
		}
	}

	public void lapFinished(Player c, int progress, int experience, int petChance) {
		if (agilityProgress[progress]) {
			resetAgilityProgress();
			c.sendMessage("You received some XP for completing the track!");
			c.getPlayerAssistant().addSkillXP(experience, 16, true);
			Achievements.increase(c, AchievementType.AGIL, 1);
			 if (Misc.random(petChance) == 20 && c.getItems().getItemCount(20659, false) == 0 && c.summonId != 20659) {
				 PlayerHandler.executeGlobalMessage("[<col=CC0000>News</col>] @cr20@ <col=255>" + c.playerName + "</col> is apperantly agile like a <col=CC0000>Squirrel</col> pet!");
				 c.getItems().addItemUnderAnyCircumstance(20659, 1);
			 }
		} else {
			c.sendMessage("You must complete the full course to gain experience.");
			return;
		}
	}

	public void roofTopFinished(Player c, int progress, int experience, int petChance) {
		if (agilityProgress[progress]) {
			resetAgilityProgress();
			c.sendMessage("You received some XP for completing the track!");
			c.getPlayerAssistant().addSkillXP(experience, 16, true);
			Achievements.increase(c, AchievementType.ROOFTOP, 1);
			 if (Misc.random(petChance) == 20 && c.getItems().getItemCount(20659, false) == 0 && c.summonId != 20659) {
				 PlayerHandler.executeGlobalMessage("[<col=CC0000>News</col>] @cr20@ <col=255>" + c.playerName + "</col> is apperantly agile like a <col=CC0000>Squirrel</col> pet!");
				 c.getItems().addItemUnderAnyCircumstance(20659, 1);
			 }
		} else {
			c.sendMessage("You must complete the full course to gain experience.");
			return;
		}
	}

	/**
	 * 600 ms process for some agility actions
	 */

	public void agilityProcess(Player c) {
		if (jumping > 0 && jumpingTimer == 0) {
			move(c, -1, 0, getAnimation(WildernessAgility.WILDERNESS_STEPPING_STONE_OBJECT), -1);
			jumping--;
			jumpingTimer = 2;
		}

		if (jumpingTimer > 0) {
			jumpingTimer--;
		}

//		if (hotSpot(c, 3190, 3414) || hotSpot(c, 3190, 3413) || hotSpot(c, 3190, 3412) || hotSpot(c, 3190, 3411)) {
//			move(c, 0, -1, 1122, -1);
//		}

//		if (hotSpot(c, 3190, 3410)) {
//			c.getPA().movePlayer(3190, 3409, 1);
//		}
//
//		if (hotSpot(c, 3190, 3410) || hotSpot(c, 3190, 3409) || hotSpot(c, 3190, 3408) || hotSpot(c, 3190, 3407) || hotSpot(c, 3190, 3406)) {
//			move(c, 0, -1, 756, -1);
//		}

//		if (hotSpot(c, 3190, 3405)) {
//			delayEmote(c, "JUMP", 3192, 3405, 3, 2);
//		}

		if (hotSpot(c, 3215, 3399)) {
			delayEmote(c, "JUMP", 3218, 3399, 3, 2);
		}
		
		if (hotSpot(c, 3253, 3180)) {
			delayEmote(c, "JUMP", 3259, 3179, 0, 2);
		}

		if (agilityTimer > 0) {
			agilityTimer--;
		}

		if (agilityTimer == 0) {
			c.getPA().movePlayer(moveX, moveY, moveH);
			moveX = -1;
			moveY = -1;
			moveH = 0;
			agilityTimer = -1;
		}

	}

	public boolean checkLevel(Player c, int objectId) {
		if (getLevelRequired(objectId) > c.playerLevel[c.playerAgility]) {
			c.sendMessage("You need an agility level of atleast " + getLevelRequired(objectId) + " to do this.");
			return true;
		}
		return false;
	}

	static int changeObjectTimer = 10;
	static int rndChance;
	static int newObjectX, newObjectY;

}
