package ethos.model.players.skills;

import ethos.Config;
import ethos.event.CycleEvent;
import ethos.event.CycleEventContainer;
import ethos.event.CycleEventHandler;
import ethos.model.content.SkillcapePerks;
import ethos.model.content.achievement.AchievementType;
import ethos.model.content.achievement.Achievements;
import ethos.model.content.achievement_diary.falador.FaladorDiaryEntry;
import ethos.model.content.achievement_diary.kandarin.KandarinDiaryEntry;
import ethos.model.content.achievement_diary.karamja.KaramjaDiaryEntry;
import ethos.model.content.achievement_diary.wilderness.WildernessDiaryEntry;
import ethos.model.content.dailytasks.DailyTasks;
import ethos.model.content.dailytasks.DailyTasks.PossibleTasks;
import ethos.model.items.ItemAssistant;
import ethos.model.players.Boundary;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.model.players.mode.ModeType;
import ethos.util.Misc;

/**
 * Class Fishing Handles: Fishing
 * 
 * @author: PapaDoc START: 22:07 23/12/2010 FINISH: 22:28 23/12/2010
 */

public class Fishing extends SkillHandler {
	
	public static int[] anglerOuftit = { 13258, 13259, 13260, 13261 };

	/**
	 * Fishing data
	 * Id, Level, Equipment, Bait, Raw material, XP, Animation,  
	 */
	public static int[][] data = { 
			{ 1, 1, 303, -1, 317, 10, 621, 321, 15, 30, 33000 }, // SHRIMP
			{ 2, 5, 307, 313, 327, 20, 622, 345, 10, 30, 32900 }, // SARDINE + HERRING
			{ 3, 16, 305, -1, 353, 20, 620, -1, -1, -1, 32800 }, // MACKEREL
			{ 4, 20, 309, 314, 335, 50, 622, 331, 30, 70, 32700 }, // TROUT
			{ 5, 23, 305, -1, 341, 45, 619, 363, 46, 100, 32600 }, // BASS + COD
			{ 6, 25, 309, 314, 349, 60, 622, -1, -1, -1, 32500 }, // PIKE
			{ 7, 35, 311, -1, 359, 80, 618, 371, 50, 100, 32300 }, // TUNA + SWORDIE
			{ 8, 40, 301, -1, 377, 90, 619, -1, -1, -1, 32000 }, // LOBSTER
			{ 9, 62, 303, -1, 7944, 100, 620, -1, -1, -1, 31600 }, // MONKFISH
			{ 10, 76, 311, -1, 383, 110, 618, -1, -1, -1, 31200 }, // SHARK
			{ 11, 79, 305, -1, 395, 100, 620, 389, 81, 130, 31000 }, // SEA TURTLE
			{ 12, 81, 305, -1, 389, 130, 620, -1, -1, -1, 30600 }, // MANTA RAY
			{ 13, 85, 301, 11940, 11934, 132, 619, -1, -1, -1, 30000 }, // DARK CRAB
			{ 14, 5, 303, -1, 3150, 20, 621, -1, -1, -1, 32900 }, // Karambwanji
			{ 15, 65, 3159, 3159, 3142, 105, 620, -1, -1, -1, 30200 }, // Karambwan
			{ 16, 82, 307, 13431, 13439, 118, 622, -1, -1, -1, 30400 } // Anglerfish
	};
	
	private static void clueBottles(Player player) {
		int chance = player.playerSkillProp[10][10] / 40;
		int bottleRoll = Misc.random(10);
		if (Misc.random(chance) == 1) {
			player.sendMessage("You catch a clue bottle!");
			if (bottleRoll < 6) {
				player.getItems().addItem(13648, 1);
			} else if (bottleRoll > 5 && bottleRoll < 9) {
				player.getItems().addItemUnderAnyCircumstance(13649, 1);
			} else {
				player.getItems().addItemUnderAnyCircumstance(13650, 1);
			}
		}
	}

	public static void attemptdata(final Player player, int npcId) {
		double multiplier = 1;
		for (int i = 0; i < anglerOuftit.length; i++) {
			if (player.getItems().isWearingItem(anglerOuftit[i])) {
				multiplier += 0.625;
			}
		}
		if (!noInventorySpace(player, "fishing")) {
			player.sendMessage("You must have space in your inventory to start fishing.");
			return;
		}
		// resetFishing(c);
		for (int i = 0; i < data.length; i++) {
			if (npcId == data[i][0]) {
				if (player.playerLevel[player.playerFishing] < data[i][1]) {
					player.sendMessage("You haven't got high enough fishing level to fish here!");
					player.sendMessage("You at least need the fishing level of " + data[i][1] + ".");
					player.getPA().sendStatement("You need the fishing level of " + data[i][1] + " to fish here.");
					return;
				}
				if (data[i][3] > 0) {
					if (!player.getItems().playerHasItem(data[i][3])) {
						player.sendMessage("You haven't got any " + ItemAssistant.getItemName(data[i][3]) + "!");
						player.sendMessage("You need " + ItemAssistant.getItemName(data[i][3]) + " to fish here.");
						return;
					}
				}
				if (player.playerSkilling[10]) {
					return;
				}
				//double percentOfXp = player.getMode().getType().equals(ModeType.OSRS) ? data[i][5] * multiplier : (data[i][5] * Config.FISHING_EXPERIENCE / 100) * multiplier;
				
				player.playerSkillProp[10][0] = data[i][6]; // ANIM
				player.playerSkillProp[10][1] = data[i][4]; // FISH
				double experience = data[i][5] * (player.getMode().getType().equals(ModeType.OSRS) ? 1 : Config.FISHING_EXPERIENCE) * multiplier; // XP
				player.playerSkillProp[10][3] = data[i][3]; // BAIT
				player.playerSkillProp[10][4] = data[i][2]; // EQUIP
				player.playerSkillProp[10][5] = data[i][7]; // sFish
				player.playerSkillProp[10][6] = data[i][8]; // sLvl
				player.playerSkillProp[10][7] = data[i][4]; // FISH
				player.playerSkillProp[10][9] = Misc.random(1) == 0 ? 7 : 5;
				player.playerSkillProp[10][10] = data[i][10]; // petChance

				if (!hasFishingEquipment(player, player.playerSkillProp[10][4])) {
					return;
				}
				player.sendMessage("You start fishing.");
				player.startAnimation(player.playerSkillProp[10][0]);
				player.stopPlayerSkill = true;

				player.playerSkilling[10] = true;

				CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer container) {
						if (player.playerSkillProp[10][5] > 0) {
							if (player.playerLevel[player.playerFishing] >= player.playerSkillProp[10][6]) {
								player.playerSkillProp[10][1] = player.playerSkillProp[10][Misc.random(1) == 0 ? 7 : 5];
							}
						}
						if (Misc.random(100) == 0 && player.getInterfaceEvent().isExecutable()) {
							player.getInterfaceEvent().execute();
							container.stop();
							return;
						}
						if (player.playerSkillProp[10][1] > 0) {
							player.sendMessage("You catch a " + ItemAssistant.getItemName(player.playerSkillProp[10][1]) + ".");
							Achievements.increase(player, AchievementType.FISH, 1);
							player.getItems().addItem(player.playerSkillProp[10][1], SkillcapePerks.FISHING.isWearing(player) || SkillcapePerks.isWearingMaxCape(player) && player.getFishingEffect() && Misc.random(2) == 1 ? 2 : 1);
							player.startAnimation(player.playerSkillProp[10][0]);
							clueBottles(player);
							
							if (Boundary.isIn(player, Boundary.RESOURCE_AREA)) {
								if (Misc.random(20) == 5) {
									int randomAmount = Misc.random(3) + 1;
									player.sendMessage("You received " + randomAmount + " blood money while fishing!");
									player.getItems().addItem(13307, randomAmount);
								}
							}

							if (Misc.random(12000) == 5555) {
								player.getItems().addItemUnderAnyCircumstance(anglerOuftit[Misc.random(anglerOuftit.length - 1)], 1);
								player.sendMessage("You notice a angler piece floating in the water and pick it up.");
							}
							
							 if (Misc.random(player.playerSkillProp[10][10]) == 2 && player.getItems().getItemCount(13320, true) == 0 && player.summonId != 13320) {
								 PlayerHandler.executeGlobalMessage("[<col=CC0000>News</col>] @cr20@ <col=255>" + player.playerName + "</col> caught a fish and a <col=CC0000>Heron</col> pet!");
								 player.getItems().addItemUnderAnyCircumstance(13320, 1);
							 }
						}
						switch (player.playerSkillProp[10][1]) {
						case 389:
							if (Boundary.isIn(player, Boundary.FALADOR_BOUNDARY)) {
								player.getDiaryManager().getFaladorDiary().progress(FaladorDiaryEntry.FISH_MANTA);
							}
							break;
						case 371:
							if (Boundary.isIn(player, Boundary.CATHERBY_BOUNDARY)) {
								player.getDiaryManager().getKandarinDiary().progress(KandarinDiaryEntry.FISH_SWORD);
							}
							break;
							
						case 377:
							if (Boundary.isIn(player, Boundary.KARAMJA_BOUNDARY)) {
								player.getDiaryManager().getKaramjaDiary().progress(KaramjaDiaryEntry.FISH_LOBSTER_KAR);
							}
							break;
							
						case 3142:
							if (Boundary.isIn(player, Boundary.RESOURCE_AREA_BOUNDARY)) {
								player.getDiaryManager().getWildernessDiary().progress(WildernessDiaryEntry.KARAMBWAN);
							}
							break;
						}
						switch (player.playerSkillProp[10][7]) {
						case 389:
							if (Boundary.isIn(player, Boundary.FALADOR_BOUNDARY)) {
								player.getDiaryManager().getFaladorDiary().progress(FaladorDiaryEntry.FISH_MANTA);
							}
							break;
						}
						
						switch (player.playerSkillProp[10][4]) {
						case 383:
							DailyTasks.increase(player, PossibleTasks.SHARKS);
							break;
						case 389:
							if (Boundary.isIn(player, Boundary.FALADOR_BOUNDARY)) {
								player.getDiaryManager().getFaladorDiary().progress(FaladorDiaryEntry.FISH_MANTA);
							}
							break;
							
						case 377:
							if (Boundary.isIn(player, Boundary.KARAMJA_BOUNDARY)) {
								player.getDiaryManager().getKaramjaDiary().progress(KaramjaDiaryEntry.FISH_LOBSTER_KAR);
							}
							break;
							
						case 3142:
							if (Boundary.isIn(player, Boundary.RESOURCE_AREA_BOUNDARY)) {
								player.getDiaryManager().getWildernessDiary().progress(WildernessDiaryEntry.KARAMBWAN);
							}
							break;
						}
						
						if (experience > 0) {
							player.getPA().addSkillXP((int)(experience), player.playerFishing, true);
						}
						if (player.playerSkillProp[10][3] > 0) {
							player.getItems().deleteItem(player.playerSkillProp[10][3], player.getItems().getItemSlot(player.playerSkillProp[10][3]), 1);
							if (player.playerSkillProp[10][3] == 3159) {
								player.getItems().addItem(3157, 1);
							}
							if (!player.getItems().playerHasItem(player.playerSkillProp[10][3])) {
								player.sendMessage("You haven't got any " + ItemAssistant.getItemName(player.playerSkillProp[10][3]) + " left!");
								player.sendMessage("You need " + ItemAssistant.getItemName(player.playerSkillProp[10][3]) + " to fish here.");
								container.stop();
							}
						}
						if (!hasFishingEquipment(player, player.playerSkillProp[10][4])) {
							container.stop();
						}
						if (!noInventorySpace(player, "fishing")) {
							container.stop();
						}
						if (!player.stopPlayerSkill) {
							container.stop();
						}
						if (!player.playerSkilling[10]) {
							container.stop();
						}
					}

					@Override
					public void stop() {
						resetFishing(player);
					}
				}, getTimer(player, npcId) + 5 + playerFishingLevel(player));
			}
		}
	}

	private static boolean hasFishingEquipment(Player c, int equipment) {
		if (!c.getItems().playerHasItem(equipment)) {
			if (equipment == 311) {
				if (!c.getItems().playerHasItem(311) && !c.getItems().playerHasItem(10129) && c.playerEquipment[3] != 10129) {
					c.sendMessage("You need a " + ItemAssistant.getItemName(equipment) + " to fish here.");
					return false;
				}
			} else {
				c.sendMessage("You need a " + ItemAssistant.getItemName(equipment) + " to fish here.");
				return false;
			}
		}
		return true;
	}

	private static void resetFishing(Player c) {
		c.startAnimation(65535);
		c.getPA().removeAllWindows();
		c.playerSkilling[10] = false;
		for (int i = 0; i < 11; i++) {
			c.playerSkillProp[10][i] = -1;
		}
	}

	private static int playerFishingLevel(Player c) {
		return (10 - (int) Math.floor(c.playerLevel[c.playerFishing] / 10));
	}

	private final static int getTimer(Player c, int npcId) {
		switch (npcId) {
		case 1:
			return 2;
		case 2:
			return 3;
		case 3:
			return 4;
		case 4:
			return 4;
		case 5:
			return 4;
		case 6:
			return 5;
		case 7:
			return 5;
		case 8:
			return 5;
		case 9:
			return 5;
		case 10:
			return 5;
		case 11:
			return 9;
		case 12:
			return 9;
		default:
			return -1;
		}
	}

	public static void fishingNPC(Player c, int i, int l) {
		switch (i) {
		case 1:
			switch (l) {
			case 319:
			case 329:
			case 323:
				// case 325:
			case 326:
			case 327:
			case 330:
			case 332:
			case 316: // NET + BAIT
				Fishing.attemptdata(c, 1);
				break;
				
			case 325:
				Fishing.attemptdata(c, 12);
				break;
			case 334:
			case 313: // NET + HARPOON
				Fishing.attemptdata(c, 3);
				break;
			case 322: // NET + HARPOON
				Fishing.attemptdata(c, 5);
				break;

			case 309: // LURE
			case 310:
			case 311:
			case 314:
			case 315:
			case 317:
			case 318:
			case 328:
			case 331:
				Fishing.attemptdata(c, 4);
				break;

			case 312:
			case 321:
			case 324: // CAGE + HARPOON
				Fishing.attemptdata(c, 8);
				break;

			case 3317: //Net
				attemptdata(c, 14);
				break;
			}
			break;
		case 2:
			switch (l) {
			case 3317:
				attemptdata(c, 14);
				break;
			case 326:
			case 327:
			case 330:
			case 332:
			case 316: // BAIT + NET
				Fishing.attemptdata(c, 2);
				break;
			case 319:
			case 323:
			case 325: // BAIT + NET
				Fishing.attemptdata(c, 9);
				break;
			case 310:
			case 311:
			case 314:
			case 315:
			case 317:
			case 318:
			case 328:
			case 329:
			case 331:
			case 309: // BAIT + LURE
				Fishing.attemptdata(c, 6);
				break;
			case 312:
			case 321:
			case 324:// SWORDIES+TUNA-CAGE+HARPOON
				Fishing.attemptdata(c, 7);
				break;
			case 313:
			case 322:
			case 334: // NET+HARPOON
				Fishing.attemptdata(c, 10);
				break;
			}
			break;
		}
	}

	public static boolean fishingNPC(Player c, int npc) {
		for (int i = 308; i < 335; i++) {
			if (npc == i) {
				return true;
			}
		}
		return false;
	}
}