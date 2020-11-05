package ethos.model.players.skills;

import java.util.Random;

import ethos.Config;
import ethos.Server;
import ethos.event.Event;
import ethos.model.content.SkillcapePerks;
import ethos.model.content.achievement.AchievementType;
import ethos.model.content.achievement.Achievements;
import ethos.model.content.achievement_diary.lumbridge_draynor.LumbridgeDraynorDiaryEntry;
import ethos.model.content.achievement_diary.varrock.VarrockDiaryEntry;
import ethos.model.content.achievement_diary.western_provinces.WesternDiaryEntry;
import ethos.model.content.achievement_diary.wilderness.WildernessDiaryEntry;
import ethos.model.content.dailytasks.DailyTasks;
import ethos.model.content.dailytasks.DailyTasks.PossibleTasks;
import ethos.model.items.ItemAssistant;
import ethos.model.players.Boundary;
import ethos.model.players.Player;
import ethos.model.players.mode.ModeType;
import ethos.util.Misc;

/**
 * Class Cooking Handles Cooking
 * 
 * @author 2012 START: 20:13 25/10/2010 FINISH: 20:21 25/10/2010
 * @author edited by Snappie
 */

public class Cooking extends SkillHandler {

	public static int[] fishIds = { 315, 2309, 319, 325, 347, 355, 333, 339, 351, 329, 361, 365, 2140, 2142, 379, 373,
			385, 397, 391, 7946, 11936, 3144, 13441 };

	public static void cookThisFood(Player p, int i, int object) {
		switch (i) {
		case 317:
			cookFish(p, i, 30, 1, 323, 315, object);
			break;
		case 2307:
			cookFish(p, i, 1, 1, 2309, 2309, object);
			break;
		case 321:
			cookFish(p, i, 30, 1, 323, 319, object);
			break;
		case 327:
			cookFish(p, i, 40, 1, 367, 325, object);
			break;
		case 345:
			cookFish(p, i, 50, 5, 357, 347, object);
			break;
		case 353:
			cookFish(p, i, 60, 10, 357, 355, object);
			break;
		case 335:
			cookFish(p, i, 70, 15, 343, 333, object);
			break;
		case 341:
			cookFish(p, i, 75, 18, 343, 339, object);
			break;
		case 349:
			cookFish(p, i, 80, 20, 343, 351, object);
			break;
		case 331:
			cookFish(p, i, 90, 25, 343, 329, object);
			break;
		case 359:
			cookFish(p, i, 100, 30, 367, 361, object);
			break;
		case 361:
			cookFish(p, i, 100, 30, 367, 365, object);
			break;
		case 2138:
			cookFish(p, i, 30, 1, 2144, 2140, object);
			break;
		case 2132:
			cookFish(p, i, 30, 1, 2146, 2142, object);
			break;
		case 377:
			cookFish(p, i, 120, 40, i + 4, i + 2, object);
			break;
		case 371:
			cookFish(p, i, 140, 45, i + 4, i + 2, object);
			break;
		case 383:
			cookFish(p, i, 210, 80, i + 4, i + 2, object);
			break;
		case 395:
			cookFish(p, i, 212, 82, i + 4, i + 2, object);
			break;
		case 389:
			cookFish(p, i, 216, 91, i + 4, i + 2, object);
			break;
		case 7944:
			cookFish(p, i, 150, 62, i + 4, i + 2, object);
			break;
		case 11934: //Dark crab
			cookFish(p, i, 220, 90, i + 4, i + 2, object);
			break;
		case 13439: //Angler
			cookFish(p, i, 212, 84, i + 4, i + 2, object);
			break;
		case 3142:
			cookFish(p, i, 190, 30, i + 4, i + 2, object);
			break;
		default:
			p.sendMessage("You can't cook this!");
			break;
		}
	}

	private static int fishStopsBurning(int i) {
		switch (i) {
		case 317:
			return 20;
		case 2307:
			return 34;
		case 321:
			return 34;
		case 2138:
			return 34;
		case 2132:
			return 34;
		case 327:
			return 38;
		case 345:
			return 37;
		case 353:
			return 45;
		case 335:
			return 50;
		case 341:
			return 39;
		case 349:
			return 52;
		case 331:
			return 58;
		case 359:
			return 63;
		case 377:
			return 74;
		case 363:
			return 80;
		case 371:
			return 86;
		case 7944:
			return 90;
		case 383:
			return 94;
		case 11934:
		case 13439:
			return 109;
		default:
			return 99;
		}
	}

	private static void cookFish(Player c, int itemID, int xpRecieved, int levelRequired, int burntFish, int cookedFish, int object) {
		if (!hasRequiredLevel(c, 7, levelRequired, "cooking", "cook this")) {
			return;
		}
		int chance = c.playerLevel[7];
		if (c.playerEquipment[c.playerHands] == 775 || SkillcapePerks.COOKING.isWearing(c) || SkillcapePerks.isWearingMaxCape(c)) {
			chance = c.playerLevel[7] + 10;
		}
		if (chance <= 0) {
			chance = Misc.random(5);
		}
		c.playerSkillProp[7][0] = itemID;
		c.playerSkillProp[7][1] = xpRecieved * (c.getMode().getType().equals(ModeType.OSRS) ? 1 : Config.COOKING_EXPERIENCE);
		c.playerSkillProp[7][2] = levelRequired;
		c.playerSkillProp[7][3] = burntFish;
		c.playerSkillProp[7][4] = cookedFish;
		c.playerSkillProp[7][5] = object;
		c.playerSkillProp[7][6] = chance;
		c.stopPlayerSkill = false;
		int item = c.getItems().getItemAmount(c.playerSkillProp[7][0]);
		if (item == 1) {
			c.doAmount = 1;
			cookTheFish(c);
			return;
		}
		viewCookInterface(c, itemID);
	}

	public static void getAmount(Player c, int amount) {
		int item = c.getItems().getItemAmount(c.playerSkillProp[7][0]);
		if (amount > item) {
			amount = item;
		}
		c.doAmount = amount;
		cookTheFish(c);
	}

	public static void resetCooking(Player c) {
		c.playerSkilling[7] = false;
		c.stopPlayerSkill = false;
		c.isCooking = false;
		for (int i = 0; i < 6; i++) {
			c.playerSkillProp[7][i] = -1;
		}
	}

	private static void viewCookInterface(Player c, int item) {
		c.getPA().sendFrame164(1743);
		c.getPA().sendFrame246(13716, 190, item);
		c.getPA().sendFrame126("\\n\\n\\n\\n\\n" + ItemAssistant.getItemName(item) + "", 13717);
	}

	/**
	 * Determines whether the fish is going to be cooked. A higher cooking level will yield a higher chance to successfully cook the fish. Having the level requirement gives a 30%
	 * chance to cook it and having the same level as the level at which the fish stops burning will give a 100% cooking chance.
	 * 
	 * @param c The player.
	 * @return Whether the fish should be cooked or not.
	 */
	public static boolean cookFish(Player c) {
		int cookLevel = c.playerLevel[7];
		if (c.playerEquipment[c.playerHands] == 775) {
			cookLevel = c.playerLevel[7] + 8;
		}
		int requiredLevel = c.playerSkillProp[7][2];
		int stopBurningAt = fishStopsBurning(c.playerSkillProp[7][0]);
		double bonusChance = (double) (cookLevel - requiredLevel) / (stopBurningAt - requiredLevel);
		double random = new Random().nextDouble();
		return 0.30 + 0.70 * bonusChance >= random;
	}

	private static void cookTheFish(final Player c) {
		if (c.playerSkilling[7]) {
			return;
		}
		c.playerSkilling[7] = true;
		c.stopPlayerSkill = true;
		c.isCooking = true;
		c.getPA().removeAllWindows();
		if (c.playerSkillProp[7][5] > 0) {
			c.startAnimation(c.playerSkillProp[7][5] == 5249 || c.playerSkillProp[7][5] == 26185 ? 897 : 896);
		}
		c.getSkilling().stop();
		c.getSkilling().setSkill(Skill.COOKING);
		Server.getEventHandler().submit(new Event<Player>("skilling", c, 2) {
			@Override
			public void execute() {
				if (attachment == null || attachment.disconnected) {
					this.stop();
					return;
				}
				if (!attachment.getItems().playerHasItem(attachment.playerSkillProp[7][0])) {
					this.stop();
					return;
				}
				attachment.getItems().deleteItem(attachment.playerSkillProp[7][0], attachment.getItems().getItemSlot(attachment.playerSkillProp[7][0]), 1);
				if (attachment.playerSkillProp[7][6] >= fishStopsBurning(attachment.playerSkillProp[7][0]) || cookFish(c)) {
					attachment.sendMessage("You successfully cook the " + ItemAssistant.getItemName(attachment.playerSkillProp[7][0]).toLowerCase() + ".");
					
					switch (c.playerSkillProp[7][0]) {
					case 7944:
						c.getDiaryManager().getWesternDiary().progress(WesternDiaryEntry.COOK_MONK);
						break;
					case 377:
						if (c.playerSkillProp[7][5] == 7183)
							c.getDiaryManager().getVarrockDiary().progress(VarrockDiaryEntry.COOK_LOBSTER);
						DailyTasks.increase(c, PossibleTasks.LOBSTERS);
						break;
					case 317:
						if (Boundary.isIn(c, Boundary.LUMRIDGE_BOUNDARY)) {
							c.getDiaryManager().getLumbridgeDraynorDiary().progress(LumbridgeDraynorDiaryEntry.COOK_SHRIMP);
						}
						break;
					case 11934:
						if (Boundary.isIn(c, Boundary.RESOURCE_AREA_BOUNDARY)) {
							c.getDiaryManager().getWildernessDiary().progress(WildernessDiaryEntry.DARK_CRAB);
						}
						break;
					}
					
					attachment.getPA().addSkillXP(attachment.playerSkillProp[7][1], c.playerCooking, true);
					attachment.getItems().addItem(attachment.playerSkillProp[7][4], 1);
					Achievements.increase(c, AchievementType.COOK, 1);
				} else {
					attachment.sendMessage("Oops! You accidentally burnt the " + ItemAssistant.getItemName(attachment.playerSkillProp[7][0]).toLowerCase() + "!");
					attachment.getItems().addItem(attachment.playerSkillProp[7][3], 1);
				}
				deleteTime(c);
				if (!attachment.getItems().playerHasItem(attachment.playerSkillProp[7][0], 1) || attachment.doAmount <= 0) {
					this.stop();
					return;
				}
				if (!attachment.stopPlayerSkill) {
					this.stop();
					return;
				}
			}

			@Override
			public void update() {
				if (attachment == null || attachment.disconnected) {
					return;
				}
				if (super.getElapsedTicks() % 4 == 0) {
					c.startAnimation(c.playerSkillProp[7][5] == 2732 ? 897 : 896);
				}
			}

			@Override
			public void stop() {
				super.stop();
				if (attachment != null && !attachment.disconnected) {
					resetCooking(c);
				}
			}
		});
	}
}