package ethos.model.players.packets.objectoptions;

import java.util.Objects;
import java.util.stream.IntStream;

import ethos.Server;
import ethos.clip.ObjectDef;
import ethos.clip.doors.DoorDefinition;
import ethos.clip.doors.DoorHandler;
import ethos.event.CycleEvent;
import ethos.event.CycleEventContainer;
import ethos.event.CycleEventHandler;
import ethos.model.content.CrystalChest;
import ethos.model.content.Obelisks;
import ethos.model.content.achievement_diary.ardougne.ArdougneDiaryEntry;
import ethos.model.content.achievement_diary.desert.DesertDiaryEntry;
import ethos.model.content.achievement_diary.falador.FaladorDiaryEntry;
import ethos.model.content.achievement_diary.fremennik.FremennikDiaryEntry;
import ethos.model.content.achievement_diary.kandarin.KandarinDiaryEntry;
import ethos.model.content.achievement_diary.karamja.KaramjaDiaryEntry;
import ethos.model.content.achievement_diary.lumbridge_draynor.LumbridgeDraynorDiaryEntry;
import ethos.model.content.achievement_diary.morytania.MorytaniaDiaryEntry;
import ethos.model.content.achievement_diary.varrock.VarrockDiaryEntry;
import ethos.model.content.achievement_diary.wilderness.WildernessDiaryEntry;
import ethos.model.content.godwars.God;
import ethos.model.entity.HealthStatus;
import ethos.model.holiday.halloween.HalloweenRandomOrder;
import ethos.model.items.EquipmentSet;
import ethos.model.minigames.lighthouse.DagannothMother;
import ethos.model.minigames.lighthouse.DisposeType;
import ethos.model.minigames.pest_control.PestControl;
import ethos.model.minigames.pk_arena.Highpkarena;
import ethos.model.minigames.pk_arena.Lowpkarena;
import ethos.model.minigames.raids.Raids;
import ethos.model.minigames.rfd.DisposeTypes;
import ethos.model.minigames.rfd.RecipeForDisaster;
import ethos.model.multiplayer_session.MultiplayerSessionType;
import ethos.model.multiplayer_session.duel.DuelSession;
import ethos.model.multiplayer_session.duel.DuelSessionRules.Rule;
import ethos.model.npcs.bosses.cerberus.Cerberus;
import ethos.model.npcs.bosses.vorkath.Vorkath;
import ethos.model.players.Boundary;
import ethos.model.players.Player;
import ethos.model.players.PlayerAssistant;
import ethos.model.players.PlayerHandler;
import ethos.model.players.Right;
import ethos.model.players.WildernessDitch;
import ethos.model.players.combat.Hitmark;
import ethos.model.players.packets.objectoptions.impl.DarkAltar;
import ethos.model.players.packets.objectoptions.impl.Overseer;
import ethos.model.players.packets.objectoptions.impl.RaidObjects;
import ethos.model.players.packets.objectoptions.impl.TrainCart;
import ethos.model.players.skills.FlaxPicking;
import ethos.model.players.skills.Skill;
import ethos.model.players.skills.agility.AgilityHandler;
import ethos.model.players.skills.crafting.JewelryMaking;
import ethos.model.players.skills.hunter.Hunter;
import ethos.model.players.skills.runecrafting.Runecrafting;
import ethos.model.players.skills.woodcutting.Tree;
import ethos.model.players.skills.woodcutting.Woodcutting;
import ethos.util.Location3D;
import ethos.util.Misc;
import ethos.world.objects.GlobalObject;

/*
 * @author Matt
 * Handles all first options for objects.
 */

public class ObjectOptionOne {

	static int[] barType = { 2363, 2361, 2359, 2353, 2351, 2349 };

	public static void handleOption(final Player c, int objectType, int obX, int obY) {
		if (Server.getMultiplayerSessionListener().inAnySession(c)) {
			return;
		}
		if (c.teleTimer > 0) {
			return;
		}
		GlobalObject object = new GlobalObject(objectType, obX, obY, c.heightLevel);
		c.getPA().resetVariables();
		c.clickObjectType = 0;
		c.turnPlayerTo(obX, obY);
		c.getFarming().patchObjectInteraction(objectType, -1, obX, obY);
		c.boneOnAltar = false;
		Tree tree = Tree.forObject(objectType);

		RaidObjects.clickObject1(c, objectType, obX, obY);
		if (tree != null) {
			Woodcutting.getInstance().chop(c, objectType, obX, obY);
			return;
		}
		if (Server.getHolidayController().clickObject(c, 1, objectType, obX, obY)) {
			return;
		}
		if (c.getGnomeAgility().gnomeCourse(c, objectType)) {
			return;
		}
		if (c.getWildernessAgility().wildernessCourse(c, objectType)) {
			return;
		}
		if (c.getBarbarianAgility().barbarianCourse(c, objectType)) {
			return;
		}
		if (c.getBarbarianAgility().barbarianCourse(c, objectType)) {
			return;
		}
		if (c.getAgilityShortcuts().agilityShortcuts(c, objectType)) {
			return;
		}
		if (c.getRoofTopSeers().execute(c, objectType)) {
			return;
		}
		if (c.getRoofTopFalador().execute(c, objectType)) {
			return;
		}
		if (c.getRoofTopVarrock().execute(c, objectType)) {
			return;
		}
		if (c.getRoofTopArdougne().execute(c, objectType)) {
			return;
		}
		if (c.getLighthouse().execute(c, objectType)) {
			return;
		}
		ObjectDef def = ObjectDef.getObjectDef(objectType);
		if ((def != null ? def.name : null) != null && def.name.toLowerCase().contains("bank")) {
			c.getPA().openUpBank();
		}
		final int[] HUNTER_OBJECTS = new int[] { 9373, 9377, 9379, 9375, 9348, 9380, 9385, 9344, 9345, 9383, 721 };
		if (IntStream.of(HUNTER_OBJECTS).anyMatch(id -> objectType == id)) {
			if (Hunter.pickup(c, object)) {
				return;
			}
			if (Hunter.claim(c, object)) {
				return;
			}
		}
		c.getMining().mine(objectType, new Location3D(obX, obY, c.heightLevel));
		Obelisks.get().activate(c, objectType);
		Runecrafting.execute(c, objectType);
		if (objectType >= 26281 && objectType <= 26290) {
			HalloweenRandomOrder.chooseOrder(c, objectType);
		}
		DoorDefinition door = DoorDefinition.forCoordinate(c.objectX, c.objectY, c.getHeight());

		if (door != null && DoorHandler.clickDoor(c, door)) {
			return;
		}

		if (c.getRaids().handleObjectClick(c,objectType)) {
			return;
		}

		switch (objectType) {

		case 31990:
			if (c.absY == 4054) {
				Vorkath.exit(c);
			} else if (c.absY == 4052) {
				Vorkath.enterInstance(c, 10);
			}
			break;
			case 31561:
				// south jump north
				if(c.absY == obY - 2) {
					c.getPA().walkTo2(obX, obY-2);
					c.turnPlayerTo(obX, obY);
					AgilityHandler.delayEmote(c, "JUMP", obX, obY, 0, 2);
					c.startAnimation(3067);
					AgilityHandler.delayEmote(c, "JUMP", obX, obY+2, 0, 4);

				}
				//north jump south
				if(c.absY == obY + 2) {
					c.getPA().walkTo2(obX, obY+2);
					c.turnPlayerTo(obX, obY);
					AgilityHandler.delayEmote(c, "JUMP", obX, obY, 0, 2);
					c.getPlayerAction().setAction(true);
					c.startAnimation(3067);
					AgilityHandler.delayEmote(c, "JUMP", obX, obY-2, 0, 4);
				}
				//east jump west
				if(c.absX == obX + 2) {
					c.getPA().walkTo2(obX, obX+2);
					c.turnPlayerTo(obX, obY);
					AgilityHandler.delayEmote(c, "JUMP", obX, obY, 0, 2);
					c.getPlayerAction().setAction(true);
					c.startAnimation(3067);
					AgilityHandler.delayEmote(c, "JUMP", obX-2, obY, 0, 4);
				}
				//west jump east
				if(c.absX == obX - 2) {
					c.getPA().walkTo2(obX, obX-2);
					c.turnPlayerTo(obX, obY);
					AgilityHandler.delayEmote(c, "JUMP", obX, obY, 0, 2);
					c.getPlayerAction().setAction(true);
					c.startAnimation(3067);
					AgilityHandler.delayEmote(c, "JUMP", obX+2, obY, 0, 4);
				}
				break;
		case 23271:
			CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					if (c.absY == 3520) {
						WildernessDitch.wildernessDitchEnter(c);
						container.stop();
					} else if (c.absY == 3523) {
						WildernessDitch.wildernessDitchLeave(c);
						container.stop();
					}
				}

				@Override
				public void stop() {
				}
			}, 1);
			break;

		case 16680:
			c.getPA().movePlayer(2884, 9798, 0);
			break;


		case 29150:
			int spellBook = c.playerMagicBook == 0 ? 1 : (c.playerMagicBook == 1 ? 2 : 0);
			int interfaceId = c.playerMagicBook == 0 ? 838 : (c.playerMagicBook == 1 ? 29999 : 938);
			String type = c.playerMagicBook == 0 ? "ancient" : (c.playerMagicBook == 1 ? "lunar" : "normal");

			c.sendMessage("You switch spellbook to " + type + " magic.");
			c.setSidebarInterface(6, interfaceId);
			c.playerMagicBook = spellBook;
			c.autocasting = false;
			c.autocastId = -1;
			c.getPA().resetAutocast();
			return;
		case 29241:
			if (c.amDonated == 0 && !c.getRights().isOrInherits(Right.DONATOR)
					&& !c.getRights().isOrInherits(Right.ADMINISTRATOR)) {
				c.sendMessage("@red@You need to be a donator to use this feature.");
				return;
			}

			if (c.specRestore > 0) {
				int seconds = ((int) Math.floor(c.specRestore * 0.6));
				c.sendMessage("You have to wait another " + seconds + " seconds to use this altar.");
				return;
			}

			c.startAnimation(645);
			c.specRestore = 120;
			if (c.getHealth().getStatus() == HealthStatus.POISON || c.getHealth().getStatus() == HealthStatus.VENOM) {
				System.out.println("All health status has been restored.");
			}
			c.getHealth().removeAllStatuses();
			c.getHealth().reset();
			c.specAmount = 10.0;
			c.getItems().addSpecialBar(c.playerEquipment[c.playerWeapon]);
			c.playerLevel[5] = c.getPA().getLevelForXP(c.playerXP[5]);
			c.getHealth().removeAllStatuses();
			c.getHealth().reset();
			c.getPA().refreshSkill(5);
			c.sendMessage("You feel rejuvinated.");
			break;
		case 6150:

			if (c.getItems().playerHasItem(barType[0])) {
				c.getSmithingInt().showSmithInterface(barType[0]);
			} else if (c.getItems().playerHasItem(barType[1])) {
				c.getSmithingInt().showSmithInterface(barType[1]);
			} else if (c.getItems().playerHasItem(barType[2])) {
				c.getSmithingInt().showSmithInterface(barType[2]);
			} else if (c.getItems().playerHasItem(barType[3])) {
				c.getSmithingInt().showSmithInterface(barType[3]);
			} else if (c.getItems().playerHasItem(barType[4])) {
				c.getSmithingInt().showSmithInterface(barType[4]);
			} else if (c.getItems().playerHasItem(barType[5])) {
				c.getSmithingInt().showSmithInterface(barType[5]);
			} else {
				c.sendMessage("You don't have any bars.");
			}
			break;
		case 11846:
			if (c.combatLevel >= 100) {
				if (c.getY() > 5175) {
					Highpkarena.addPlayer(c);
				} else {
					Highpkarena.removePlayer(c, false);
				}
			} else if (c.combatLevel >= 80) {
				if (c.getY() > 5175) {
					Lowpkarena.addPlayer(c);
				} else {
					Lowpkarena.removePlayer(c, false);
				}
			} else {
				c.sendMessage("You must be at least level 80 to compete in events.");
			}
			break;

		case 11845:
			if (c.combatLevel >= 100) {
				if (c.getY() < 5169) {
					Highpkarena.removePlayer(c, false);
				}
			} else if (c.combatLevel >= 80) {
				if (c.getY() < 5169) {
					Lowpkarena.removePlayer(c, false);
				}
			} else {
				c.sendMessage("You must be at least level 80 to compete in events.");
			}

			break;

		case 10068:
			c.getDH().sendDialogues(637, 2040);
			break;
		case 12941:
			PlayerAssistant.refreshSpecialAndHealth(c);
			break;
		case 29349:// miniportal
			// c.getDH().sendDialogues(402, 403);
			c.getPA().showInterface(65000);
			c.getTeleport().loadMinigameTab();
			break;
		case 29347:// bossportal
			// c.getDH().sendDialogues(404, 406);
			c.getPA().showInterface(65000);
			c.getTeleport().loadBossTab();
			break;
		case 29345:// Training
			// c.getDH().sendDialogues(400, 399);
			c.getPA().showInterface(65000);
			c.getTeleport().loadMonsterTab();
			break;
		case 29346:// Wilderness
			// c.getDH().sendDialogues(401, 0);
			c.getPA().showInterface(65000);
			c.getTeleport().loadWildernessTab();
			break;
		/*
		 * case 29747: //Ice Demon Brazziers if (c.getItems().playerHasItem(20799, 1)) {
		 * Server.getGlobalObjects().replace(new GlobalObject(29747, obX, obY,
		 * c.heightLevel, 0, 10, 50, -1), new GlobalObject(29748, obX, obY,
		 * c.heightLevel, 0, 10, 0, -1)); c.getItems().deleteItem(20799, 1); } else {
		 * c.sendMessage("You need some kindling to light this brazier!"); } break; case
		 * 29748: if (c.getItems().playerHasItem(20799, 1)) { //addBrazzierVariable
		 * c.sendMessage("You add a piece of kindling to the brazier.");
		 * c.getItems().deleteItem(20799, 1); } else {
		 * c.sendMessage("You need some kindling to light this brazier!"); } break;
		 */
			case 8720:
				c.getShops().openShop(77);
				c.sendMessage("@red@ Please type in ::vote to go to the site. And do ::claimvote to receive them!");
				c.sendMessage("@red@ Thank you for supporting Ascend!");
				break;
			case 31556:
				c.getPA().movePlayer(3241, 10234);
				break;
			case 31558:
				c.getPA().movePlayer(3126, 3833);
				break;

		case 7811:
			if (!c.inClanWarsSafe()) {
				return;
			}
			c.getShops().openShop(116);
			break;
		case 4150:
			c.getPA().spellTeleport(2855, 3543, 0, false);
		case 23115:// from bobs
			c.getPA().spellTeleport(1644, 3673, 0, false);
			break;
		case 10251:
			c.getPA().spellTeleport(2525, 4776, 0, false);
			break;
		case 26756:

			break;

		case 27057:
			Overseer.handleBludgeon(c);
			break;

		case 14918:
			if (!c.getDiaryManager().getWildernessDiary().hasDoneAll()) {
				c.sendMessage("You must have completed the whole wilderness diary to use this shortcut.");
				return;
			}

			if (c.absY > 3808) {
				AgilityHandler.delayEmote(c, "JUMP", 3201, 3807, 0, 2);
			} else {
				AgilityHandler.delayEmote(c, "JUMP", 3201, 3810, 0, 2);
			}
			break;

		case 29728:
			if (c.absY > 3508) {
				AgilityHandler.delayEmote(c, "JUMP", 1722, 3507, 0, 2);
			} else {
				AgilityHandler.delayEmote(c, "JUMP", 1722, 3512, 0, 2);
			}
			break;

		case 28893:
			if (c.playerLevel[16] < 54) {
				c.sendMessage("You need an Agility level of 54 to pass this.");
				return;
			}
			if (c.absY > 10064) {
				AgilityHandler.delayEmote(c, "JUMP", 1610, 10062, 0, 2);
			} else {
				AgilityHandler.delayEmote(c, "JUMP", 1613, 10069, 0, 2);
			}
			break;

		case 27987: // scorpia
			if (c.absX == 1774) {
				AgilityHandler.delayEmote(c, "CLIMB_DOWN", 1769, 3849, 0, 2);
			} else {
				AgilityHandler.delayEmote(c, "CLIMB_UP", 1774, 3849, 0, 2);
			}
			break;

		case 27988: // scorpia
			if (c.absX == 1774) {
				AgilityHandler.delayEmote(c, "CLIMB_DOWN", 1769, 3849, 0, 2);
			} else {
				AgilityHandler.delayEmote(c, "CLIMB_UP", 1774, 3849, 0, 2);
			}
			break;

		case 27985:
			if (c.absY > 3872) {
				AgilityHandler.delayEmote(c, "CLIMB_DOWN", 1761, 3871, 0, 2);
			} else {
				AgilityHandler.delayEmote(c, "CLIMB_UP", 1761, 3874, 0, 2);
			}
			break;

		case 27984:
			if (c.absY > 3872) {
				AgilityHandler.delayEmote(c, "CLIMB_DOWN", 1761, 3871, 0, 2);
			} else {
				AgilityHandler.delayEmote(c, "CLIMB_UP", 1761, 3874, 0, 2);
			}
			break;

		case 29730:
			if (c.absX > 1604) {
				AgilityHandler.delayEmote(c, "JUMP", 1603, 3571, 0, 2);
			} else {
				AgilityHandler.delayEmote(c, "JUMP", 1607, 3571, 0, 2);
			}
			break;

		case 25014:
			if (Boundary.isIn(c, Boundary.PURO_PURO)) {
				c.getPA().startTeleport(2525, 2916, 0, "puropuro", false);
			} else {
				c.getPA().startTeleport(2592, 4321, 0, "puropuro", false);
			}
			break;

		case 4154:// lizexit
			c.getPA().movePlayer(1465, 3687, 0);
			break;

		case 30366:// Mining Guild Entrance
			if (c.absX == 3043 && c.absY == 9730) {
				if (c.playerLevel[c.playerMining] >= 60) {
					c.getPA().movePlayer(3043, 9729, 0);
				} else {
					c.sendMessage("You must have a mining level of 60 to enter.");
				}
			} else if (c.absX == 3043 && c.absY == 9729) {
				c.getPA().movePlayer(3043, 9730, 0);
			}
			break;

		case 30365:// Mining Guild Entrance
			if (c.absX == 3019 && c.absY == 9733) {
				if (c.playerLevel[c.playerMining] >= 60) {
					c.getPA().movePlayer(3019, 9732, 0);
				} else {
					c.sendMessage("You must have a mining level of 60 to enter.");
				}
			} else if (c.absX == 3019 && c.absY == 9732) {
				c.getPA().movePlayer(3019, 9733, 0);
			}
			break;

		case 8356:
			c.getDH().sendDialogues(55874, 2200);
			break;

		case 1727:
		case 1728: // Kbd gates
			if (c.absX == 3007) {
				c.getPA().walkTo(+1, 0);
			} else if (c.absX == 3008) {
				c.getPA().walkTo(-1, 0);
			}
			break;

		case 10439:
		case 7814:
			PlayerAssistant.refreshHealthWithoutPenalty(c);
			break;
		case 2670:
			if (!c.getItems().playerHasItem(1925) || !c.getItems().playerHasItem(946)) {
				c.sendMessage("You must have an empty bucket and a knife to do this.");
				return;
			}
			c.getItems().deleteItem(1925, 1);
			c.getItems().addItem(1929, 1);
			c.sendMessage("You cut the cactus and pour some water into the bucket.");
			c.getDiaryManager().getDesertDiary().progress(DesertDiaryEntry.CUT_CACTUS);
			break;
		// Carts Start
		case 7029:
			TrainCart.handleInteraction(c);
			break;
		case 28837:
			c.getDH().sendDialogues(193193, -1);
			break;
		// Carts End
		case 10321:
			c.getPA().spellTeleport(1752, 5232, 0, false);
			c.sendMessage("Welcome to the Giant Mole cave, try your luck for a granite maul.");
			break;
		case 1294:
			c.getDH().tree = "stronghold";
			c.getDH().sendDialogues(65, -1);
			break;

		case 1293:
			c.getDH().tree = "village";
			c.getDH().sendDialogues(65, -1);
			break;

		case 1295:
			c.getDH().tree = "grand_exchange";
			c.getDH().sendDialogues(65, -1);
			break;

		case 2073:
			c.getItems().addItem(1963, 1);
			c.getDiaryManager().getKaramjaDiary().progress(KaramjaDiaryEntry.PICK_BANANAS);
			break;

		case 20877:
			AgilityHandler.delayFade(c, "CRAWL", 2712, 9564, 0, "You crawl into the entrance.",
					"and you end up in a dungeon.", 3);
			c.getDiaryManager().getKaramjaDiary().progress(KaramjaDiaryEntry.ENTER_BRIMHAVEN_DUNGEON);
			break;
		case 20878:
			AgilityHandler.delayFade(c, "CRAWL", 1571, 3659, 0, "You crawl into the entrance.",
					"and you end up in a dungeon.", 3);
			c.getDiaryManager().getKaramjaDiary().progress(KaramjaDiaryEntry.ENTER_BRIMHAVEN_DUNGEON);
			break;
		case 16675:
			AgilityHandler.delayEmote(c, "CLIMB_UP", 2445, 3416, 1, 2);
			break;
		case 16677:
			AgilityHandler.delayEmote(c, "CLIMB_UP", 2445, 3416, 0, 2);
			break;

		case 6434:
			AgilityHandler.delayEmote(c, "CLIMB_DOWN", 3118, 9644, 0, 2);
			break;

		case 11441:
			AgilityHandler.delayEmote(c, "CLIMB_DOWN", 2856, 9570, 0, 2);
			break;

		case 18969:
			AgilityHandler.delayEmote(c, "CLIMB_UP", 2857, 3167, 0, 2);
			break;

		case 11835:
			AgilityHandler.delayFade(c, "CRAWL", 2480, 5175, 0, "You crawl into the entrance.",
					"and you end up in Tzhaar City.", 3);
			break;
		case 11836:
			AgilityHandler.delayFade(c, "CRAWL", 1212, 3540, 0, "You crawl into the entrance.",
					"and you end up back on Mt. Quidamortem.", 3);
			break;

		case 155:
			AgilityHandler.delayEmote(c, "BALANCE", 3096, 3359, 0, 2);
			break;
		case 160:
			AgilityHandler.delayEmote(c, 2140, 3098, 3357, 0, 2);
			break;

		case 23568:
			c.getPA().movePlayer(2704, 3205, 0);
			break;

		case 23569:
			c.getPA().movePlayer(2709, 3209, 0);
			break;

		case 17068:
			if (c.playerLevel[c.playerAgility] < 8 || c.playerLevel[c.playerStrength] < 19
					|| c.playerLevel[c.playerRanged] < 37) {
				c.sendMessage(
						"You need an agility level of 8, strength level of 19 and ranged level of 37 to do this.");
				return;
			}
			AgilityHandler.delayEmote(c, "JUMP", 3253, 3180, 0, 2);
			c.getDiaryManager().getLumbridgeDraynorDiary().progress(LumbridgeDraynorDiaryEntry.RIVER_LUM_SHORTCUT);
			break;

		case 16465:
			if (!c.getDiaryManager().getDesertDiary().hasCompletedSome("ELITE")) {
				c.sendMessage("You must have completed all tasks in the desert diary to do this.");
				return;
			}
			if (c.playerLevel[c.playerAgility] < 82) {
				c.sendMessage("You need an agility level of at least 82 to squeeze through here.");
				return;
			}
			c.sendMessage("You squeeze through the crevice.");
			if (c.absX == 3506 && c.absY == 9505)
				c.getPA().movePlayer(3500, 9510, 2);
			else if (c.absX == 3500 && c.absY == 9510)
				c.getPA().movePlayer(3506, 9505, 2);
			break;

		case 2147:
			AgilityHandler.delayEmote(c, "CLIMB_DOWN", 3104, 9576, 0, 2);
			break;
		case 2148:
			AgilityHandler.delayEmote(c, "CLIMB_UP", 3105, 3162, 0, 2);
			break;
		case 1579:
			AgilityHandler.delayEmote(c, "CLIMB_DOWN", 3097, 9868, 0, 2);
			break;
		case 17385:
			AgilityHandler.delayEmote(c, "CLIMB_UP", 3096, 3468, 0, 2);
			break;

		case 27785:
			c.getDH().sendDialogues(70300, -1);
			break;
		case 30266:
			{
				c.sendMessage("The Inferno is currently under construction.");
				return;
			}
		case 28894:
		case 28895:
		case 28898:
		case 28897:
		case 28896: // catacomb exits
			c.getPA().movePlayer(1639, 3673, 0);
			c.sendMessage("You return to the statue.");
			break;
		case 882:
			c.getPA().movePlayer(2885, 5292, 2);
			c.sendMessage("Welcome to the Godwars Dungeon!.");
			break;
		case 27777:
			c.getPA().movePlayer(1781, 3412, 0);
			c.sendMessage("Welcome to the CrabClaw Isle, try your luck for a tentacle or Trident of the Seas!.");
			break;
		case 3828:
			c.getPA().movePlayer(3484, 9510, 2);
			c.sendMessage("Welcome to the Kalphite Lair, try your luck for a dragon chain or uncut onyx!.");
			break;

		case 3829:
			c.getPA().movePlayer(1845, 3809, 0);
			c.sendMessage("You find the light of day outside of the tunnel!");
			break;
		case 3832:
			c.getPA().movePlayer(3510, 9496, 2);
			break;

		case 4031:
			if (c.absY == 3117) {
				if (EquipmentSet.DESERT_ROBES.isWearing(c)) {
					c.getDiaryManager().getDesertDiary().progress(DesertDiaryEntry.PASS_GATE_ROBES);
				} else {
					c.getDiaryManager().getDesertDiary().progress(DesertDiaryEntry.PASS_GATE);
				}
				c.getPA().movePlayer(c.absX, 3115);
			} else {
				c.getPA().movePlayer(c.absX, 3117);
			}
			break;

		case 7122:
			if (c.absX == 2564 && c.absY == 3310)
				c.getPA().movePlayer(2563, 3310);
			else if (c.absX == 2563 && c.absY == 3310)
				c.getPA().movePlayer(2564, 3310);
			break;

		case 24958:
			if (c.getDiaryManager().getVarrockDiary().hasCompleted("EASY")) {
				if (c.absX == 3143 && c.absY == 3443)
					c.getPA().movePlayer(3143, 3444);
				else if (c.absX == 3143 && c.absY == 3444)
					c.getPA().movePlayer(3143, 3443);
			} else {
				c.sendMessage("You must have completed all easy tasks in the varrock diary to enter.");
				return;
			}
			break;

		case 10045:
			if (c.getDiaryManager().getVarrockDiary().hasCompleted("EASY")) {
				if (c.absX == 3143 && c.absY == 3452)
					c.getPA().movePlayer(3144, 3452);
				else if (c.absX == 3144 && c.absY == 3452)
					c.getPA().movePlayer(3143, 3452);
			} else {
				c.sendMessage("You must have completed all easy tasks in the varrock diary to enter.");
				return;
			}
			break;

		case 11780:
			if (c.getDiaryManager().getVarrockDiary().hasCompleted("HARD")) {
				if (c.absX == 3255)
					c.getPA().movePlayer(3256, c.absY);
				else
					c.getPA().movePlayer(3255, c.absY);
			} else {
				c.sendMessage("You must have completed all hard tasks in the varrock diary to enter.");
				return;
			}
			break;
		case 1805:
			if (c.getDiaryManager().getVarrockDiary().hasCompleted("EASY")) {
				c.getDiaryManager().getVarrockDiary().progress(VarrockDiaryEntry.CHAMPIONS_GUILD);
				if (c.absY == 3362)
					c.getPA().movePlayer(c.absX, 3363);
				else
					c.getPA().movePlayer(c.absX, 3362);
			} else {
				c.sendMessage("You must have completed all easy tasks in the varrock diary to enter.");
				return;
			}
			break;

		case 538:
			c.getPA().movePlayer(2280, 10016, 0);
			break;

		case 537:
			c.getPA().movePlayer(2280, 10022, 0);
			break;

		case 6462: // Ice gate
		case 6461:
			c.getPA().movePlayer(2852, 3809, 2);
			break;

		case 6456: // Ice ledge
			c.getPA().movePlayer(2855, c.absY, 1);
			break;

		case 6455: // Ice ledge (Bottom)
			if (c.absY >= 3804)
				c.getPA().movePlayer(2837, 3803, 1);
			else
				c.getPA().movePlayer(2837, 3805, 0);
			break;

		case 677:
			c.getAgilityHandler().move(c, c.absX <= 2970 ? 4 : -4, 0, 844, -1);
			break;

		case 13641: // Teleportation Device
			c.getDH().sendDialogues(63, -1);
			break;

		case 23104:
			if (System.currentTimeMillis() - c.cerbDelay > 5000) {
				Cerberus cerb = c.createCerberusInstance();
				if (!c.debugMessage)
					if (!c.getSlayer().getTask().isPresent()) {
						c.sendMessage("You must have an active cerberus or hellhound task to enter this cave...");
						return;
					}
				if (!c.debugMessage)
					if (!c.getSlayer().getTask().get().getPrimaryName().equals("cerberus")
							&& !c.getSlayer().getTask().get().getPrimaryName().equals("hellhound")) {
						c.sendMessage("You must have an active cerberus or hellhound task to enter this cave...");
						return;
					}
				if (c.getCerberusLostItems().size() > 0) {
					c.getDH().sendDialogues(642, 5870);
					c.nextChat = -1;
					return;
				}

				if (cerb == null) {
					c.sendMessage("We are unable to allow you in at the moment.");
					c.sendMessage("Too many players.");
					return;
				}

				if (Server.getEventHandler().isRunning(c, "cerb")) {
					c.sendMessage("You're about to fight start the fight, please wait.");
					return;
				}
				c.getCerberus().init();
				c.cerbDelay = System.currentTimeMillis();
			} else {
				c.sendMessage("Please wait a few seconds between clicks.");
			}
			break;

		case 21772:
			if (!Boundary.isIn(c, Boundary.BOSS_ROOM_WEST)) {
				return;
			}
			Cerberus cerb = c.getCerberus();

			if (cerb != null) {
				cerb.end(DisposeTypes.INCOMPLETE);
			} else {
				c.getPA().movePlayer(1309, 1250, 0);
			}
			break;

		case 28900:
			DarkAltar.handleDarkTeleportInteraction(c);
			break;
		case 28925:
			DarkAltar.handlePortalInteraction(c);
			break;

		case 23105:
			c.appendDamage(5, Hitmark.HIT);
			if (c.absY == 1241) {
				c.getPA().walkTo(0, +2);
			} else {
				if (c.getCerberus() != null) {
					c.getCerberus().end(DisposeTypes.INCOMPLETE);
					c.getPA().movePlayer(1309, 1250, 0);
				}
			}
			break;

		case 12355:
			RecipeForDisaster rfd = c.createRecipeForDisasterInstance();

			if (c.rfdChat == 1) {
				if (rfd == null) {
					c.sendMessage("We are unable to allow you to start the minigame.");
					c.sendMessage("Too many players.");
					return;
				}

				if (Server.getEventHandler().isRunning(c, "rfd")) {
					c.sendMessage("You're about to fight start the minigame, please wait.");
					return;
				}
				c.getrecipeForDisaster().init();
			} else {
				c.getDH().sendDialogues(58, 4847);
			}
			break;

		case 12356: // Rfd Portal
			if (!Boundary.isIn(c, Boundary.RFD)) {
				return;
			}
			rfd = c.getrecipeForDisaster();

			if (rfd != null) {
				rfd.end(DisposeTypes.INCOMPLETE);
			} else {
				c.getPA().movePlayer(3218, 9622, 0);
			}
			break;

		case 4383:
			DagannothMother mother = c.createDagannothMotherInstance();

			if (mother == null) {
				c.sendMessage("We are unable to allow you to fight the mother.");
				c.sendMessage("She is already fighting too many players.");
				return;
			}

			if (Server.getEventHandler().isRunning(c, "dagannoth_mother")) {
				c.sendMessage("You're about to fight the mother, please wait.");
				return;
			}

			c.getDagannothMother().init();
			break;

		case 4577: // Lighthouse door
			if (c.getY() >= 3636)
				c.getPA().movePlayer(2509, 3635, 0);
			else
				c.getPA().movePlayer(2509, 3636, 0);
			break;

		case 4413:
			if (!Boundary.isIn(c, Boundary.LIGHTHOUSE)) {
				return;
			}
			mother = c.getDagannothMother();

			if (mother != null) {
				c.getDagannothMother().end(DisposeType.INCOMPLETE);
			} else {
				c.getPA().movePlayer(2509, 3639, 0);
			}
			break;

		case 13642: // Lectern
			c.getDH().sendDialogues(10, -1);
			break;

		case 8930:
			c.getPA().movePlayer(1975, 4409, 3);
			break;

		case 10177: // Dagganoth kings ladder
			c.getPA().movePlayer(2900, 4449, 0);
			break;

		case 10193:
			c.getPA().movePlayer(2545, 10143, 0);
			break;

		case 10195:
			c.getPA().movePlayer(1809, 4405, 2);
			break;

		case 10196:
			c.getPA().movePlayer(1807, 4405, 3);
			break;

		case 10197:
			c.getPA().movePlayer(1823, 4404, 2);
			break;

		case 10198:
			c.getPA().movePlayer(1825, 4404, 3);
			break;

		case 10199:
			c.getPA().movePlayer(1834, 4388, 2);
			break;

		case 10200:
			c.getPA().movePlayer(1834, 4390, 3);
			break;

		case 10201:
			c.getPA().movePlayer(1811, 4394, 1);
			break;

		case 10202:
			c.getPA().movePlayer(1812, 4394, 2);
			break;

		case 10203:
			c.getPA().movePlayer(1799, 4386, 2);
			break;

		case 10204:
			c.getPA().movePlayer(1799, 4388, 1);
			break;

		case 10205:
			c.getPA().movePlayer(1796, 4382, 1);
			break;

		case 10206:
			c.getPA().movePlayer(1796, 4382, 2);
			break;

		case 10207:
			c.getPA().movePlayer(1800, 4369, 2);
			break;

		case 10208:
			c.getPA().movePlayer(1802, 4370, 1);
			break;

		case 10209:
			c.getPA().movePlayer(1827, 4362, 1);
			break;

		case 10210:
			c.getPA().movePlayer(1825, 4362, 2);
			break;

		case 10211:
			c.getPA().movePlayer(1863, 4373, 2);
			break;

		case 10212:
			c.getPA().movePlayer(1863, 4371, 1);
			break;

		case 10213:
			c.getPA().movePlayer(1864, 4389, 1);
			break;

		case 10214:
			c.getPA().movePlayer(1864, 4387, 2);
			break;

		case 10215:
			c.getPA().movePlayer(1890, 4407, 0);
			break;

		case 10216:
			c.getPA().movePlayer(1890, 4406, 1);
			break;

		case 10217:
			c.getPA().movePlayer(1957, 4373, 1);
			break;

		case 10218:
			c.getPA().movePlayer(1957, 4371, 0);
			break;

		case 10219:
			c.getPA().movePlayer(1824, 4379, 3);
			break;

		case 10220:
			c.getPA().movePlayer(1824, 4381, 2);
			break;

		case 10221:
			c.getPA().movePlayer(1838, 4375, 2);
			break;

		case 10222:
			c.getPA().movePlayer(1838, 4377, 3);
			break;

		case 10223:
			c.getPA().movePlayer(1850, 4386, 1);
			break;

		case 10224:
			c.getPA().movePlayer(1850, 4387, 2);
			break;

		case 10225:
			c.getPA().movePlayer(1932, 4378, 1);
			break;

		case 10226:
			c.getPA().movePlayer(1932, 4380, 2);
			break;

		case 10227:
			if (c.getX() == 1961 && c.getY() == 4392)
				c.getPA().movePlayer(1961, 4392, 2);
			else
				c.getPA().movePlayer(1932, 4377, 1);
			break;

		case 10228:
			c.getPA().movePlayer(1961, 4393, 3);
			break;

		case 10229:
			c.getPA().movePlayer(1912, 4367, 0);
			break;

		/**
		 * Dagannoth king entrance
		 */
		case 10230:
			if (c.getRights().isOrInherits(Right.IRONMAN) || c.getRights().isOrInherits(Right.ULTIMATE_IRONMAN)) {
				c.getPA().movePlayer(2899, 4449, 4);
			} else {
				c.getPA().movePlayer(2899, 4449, 0);
			}
			break;

		case 8958:
			if (c.getX() <= 2490)
				c.getPA().movePlayer(2492, 10163, 0);
			if (c.getX() >= 2491)
				c.getPA().movePlayer(2490, 10163, 0);
			break;
		case 8959:
			if (c.getX() <= 2490)
				c.getPA().movePlayer(2492, 10147, 0);
			if (c.getX() >= 2491)
				c.getPA().movePlayer(2490, 10147, 0);
			break;
		case 8960:
			if (c.getX() <= 2490)
				c.getPA().movePlayer(2492, 10131, 0);
			if (c.getX() >= 2491)
				c.getPA().movePlayer(2490, 10131, 0);
			break;
		//
		case 26724:
			if (c.playerLevel[Skill.AGILITY.getId()] < 72) {
				c.sendMessage("You need an agility level of 72 to cross over this mud slide.");
				return;
			}
			if (c.getX() == 2427 && c.getY() == 9767) {
				c.getPA().movePlayer(2427, 9762);
			} else if (c.getX() == 2427 && c.getY() == 9762) {
				c.getPA().movePlayer(2427, 9767);
			}
			break;
		case 535:
			if (obX == 3722 && obY == 5798) {
				if (c.getMode().isIronman() || c.getMode().isUltimateIronman()) {
					c.getPA().movePlayer(3677, 5775, 4);
				} else {
					c.getPA().movePlayer(3677, 5775, 0);
				}
			}
			break;

		case 536:
			if (obX == 3678 && obY == 5775) {
				c.getPA().movePlayer(3723, 5798);
			}
			break;

		case 26720:
			if (obX == 2427 && obY == 9747) {
				if (c.getX() == 2427 && c.getY() == 9748) {
					c.getPA().movePlayer(2427, 9746);
				} else if (c.getX() == 2427 && c.getY() == 9746) {
					c.getPA().movePlayer(2427, 9748);
				}
			} else if (obX == 2420 && obY == 9750) {
				if (c.getX() == 2420 && c.getY() == 9751) {
					c.getPA().movePlayer(2420, 9749);
				} else if (c.getX() == 2420 && c.getY() == 9749) {
					c.getPA().movePlayer(2420, 9751);
				}
			} else if (obX == 2418 && obY == 9742) {
				if (c.getX() == 2418 && c.getY() == 9741) {
					c.getPA().movePlayer(2418, 9743);
				} else if (c.getX() == 2418 && c.getY() == 9743) {
					c.getPA().movePlayer(2418, 9741);
				}
			} else if (obX == 2357 && obY == 9778) {
				if (c.getX() == 2358 && c.getY() == 9778) {
					c.getPA().movePlayer(2356, 9778);
				} else if (c.getX() == 2356 && c.getY() == 9778) {
					c.getPA().movePlayer(2358, 9778);
				}
			} else if (obX == 2388 && obY == 9740) {
				if (c.getX() == 2389 && c.getY() == 9740) {
					c.getPA().movePlayer(2387, 9740);
				} else if (c.getX() == 2387 && c.getY() == 9740) {
					c.getPA().movePlayer(2389, 9740);
				}
			} else if (obX == 2379 && obY == 9738) {
				if (c.getX() == 2380 && c.getY() == 9738) {
					c.getPA().movePlayer(2378, 9738);
				} else if (c.getX() == 2378 && c.getY() == 9738) {
					c.getPA().movePlayer(2380, 9738);
				}
			}
			break;

		case 26721:
			if (obX == 2358 && obY == 9759) {
				if (c.getX() == 2358 && c.getY() == 9758) {
					c.getPA().movePlayer(2358, 9760);
				} else if (c.getX() == 2358 && c.getY() == 9760) {
					c.getPA().movePlayer(2358, 9758);
				}
			}
			if (obX == 2380 && obY == 9750) {
				if (c.getX() == 2381 && c.getY() == 9750) {
					c.getPA().movePlayer(2379, 9750);
				} else if (c.getX() == 2379 && c.getY() == 9750) {
					c.getPA().movePlayer(2381, 9750);
				}
			}
			break;

		case 154:
			if (obX == 2356 && obY == 9783) {
				if (c.playerLevel[Skill.SLAYER.getId()] < 93) {
					c.sendMessage("You need a slayer level of 93 to enter into this crevice.");
					return;
				}
				c.getPA().movePlayer(3748, 5761, 0);
			}
			break;

		case 534:
			if (obX == 3748 && obY == 5760) {
				c.getPA().movePlayer(2356, 9782, 0);
			}
			break;
		case 9706:
			if(c.maRound !=2){
				c.sendMessage("@blu@Please talk to Kolodion before having access to this magical arena. He is located in Mage Bank towards the east.");
				return;
			}
			if (obX == 3104 && obY == 3956) {
				c.getPA().startLeverTeleport(3105, 3951, 0);
			}
			break;

		case 9707:
			if(c.maRound !=2){
				c.sendMessage("@blu@Please talk to Kolodion before having access to this magical arena. He is located in Mage Bank towards the east.");
				return;
			}
			if (obX == 3105 && obY == 3952) {
				c.getPA().startLeverTeleport(3105, 3956, 0);
			}
			break;
		case 3610:
			if (obX == 3550 && obY == 9695) {
				c.getPA().startTeleport(3565, 3308, 0, "modern", false);
			}
			break;
		case 26561:
			if (obX == 2913 && obY == 5300) {
				c.getPA().movePlayer(2914, 5300, 1);
			}
			break;
		case 26562:
			if (obX == 2920 && obY == 5274) {
				c.getPA().movePlayer(2920, 5274, 0);
			}
			break;
		case 26504:
			if (obX == 2908 && obY == 5265) {
				c.getGodwars().enterBossRoom(God.SARADOMIN);
			}
			break;
		case 26518:
			if (obX == 2885 && obY == 5333) {
				c.getPA().movePlayer(2885, 5344);
			} else if (obX == 2885 && obY == 5344) {
				c.getPA().movePlayer(2885, 5333);
			}
			break;
		case 26505:
			if (obX == 2925 && obY == 5332) {
				c.getGodwars().enterBossRoom(God.ZAMORAK);
			}
		case 26503:
			if (obX == 2863 && obY == 5354) {
				c.getGodwars().enterBossRoom(God.BANDOS);
			}
			break;
		case 26380:
			if (obX == 2871 && obY == 5270) {
				if (c.getY() == 5279) {
					c.getPA().movePlayer(2872, 5269);
				} else if (c.getY() == 5269) {
					c.getPA().movePlayer(2872, 5279);
				}
			}
			break;
		case 21578: // Stairs up
		case 10:
			if (!c.getRights().isOrInherits(Right.DONATOR)) {
				c.sendMessage("You must be <img=8><col=FFCC24>VIP</col> to enter the top floor.");
				return;
			}
			if (c.heightLevel == 0) {
				AgilityHandler.delayEmote(c, "CLIMB_UP", 3372, 9645, 1, 2);
			} else {
				AgilityHandler.delayEmote(c, "CLIMB_DOWN", 3372, 9645, 0, 2);
			}
			break;
		case 26502:
			if (obX == 2839 && obY == 5295) {
				c.getGodwars().enterBossRoom(God.ARMADYL);
			}
			break;
		case 7674:
			c.getFarming().farmPoisonBerry();
			break;
		case 172:
		case 170:
			CrystalChest.searchChest(c);
			break;
		case 4873:
		case 26761:
			c.getPA().startLeverTeleport(3158, 3953, 0);
			c.getDiaryManager().getWildernessDiary().progress(WildernessDiaryEntry.WILDERNESS_LEVER);
			break;
		case 7813:
		case 3840: // Compost Bin
			c.getFarming().handleCompostAdd();
			break;
		case 2492:
		case 15638:
		case 7479:
			c.getPA().startTeleport(3088, 3504, 0, "modern", false);
			break;
		case 11803:
			if (c.getRights().isOrInherits(Right.CONTRIBUTOR)) {
				AgilityHandler.delayEmote(c, "CLIMB_DOWN", 3577, 9927, 0, 2);
				c.sendMessage("<img=4> Welcome to the donators only slayer cave.");
			}
			break;
		case 17387:
			if (c.getRights().isOrInherits(Right.CONTRIBUTOR)) {
				AgilityHandler.delayEmote(c, "CLIMB_UP", 2125, 4913, 0, 2);
			}
			break;
		case 25824:
			c.turnPlayerTo(obX, obY);
			c.getDH().sendDialogues(40, -1);
			break;

		case 5097:
		case 21725:
			c.getPA().movePlayer(2636, 9510, 2);
			break;
		case 5098:
		case 21726:
			c.getPA().movePlayer(2636, 9517, 0);
			break;
		case 5094:
		case 21722:
			c.getPA().movePlayer(2643, 9594, 2);
			break;
		case 5096:
		case 21724:
			c.getPA().movePlayer(2649, 9591, 0);
			break;
		case 2320:
		case 23566:
			if (obX == 3119 && obY == 9964 || obX == 3121 && obY == 9963|| obX == 3120 && obY == 9963) {
				c.getPA().movePlayer(3120, 9970, 0);
			} else if (obX == 3119 && obY == 9969 || obX == 3120 && obY == 9970 ||  obX == 3121 && obY == 9970||  obX == 3121 && obY == 9969) {
				c.getPA().movePlayer(3120, 9963, 0);
			}
			break;
		case 26760:
			if (c.absX == 3184 && c.absY == 3945) {
				c.getDH().sendDialogues(631, -1);
			} else if (c.absX == 3184 && c.absY == 3944) {
				c.getPA().movePlayer(3184, 3945, 0);
			}
			break;
		case 19206:
		//	if (c.absX == 1502 && c.absY == 3838) {
			//	c.getDH().sendDialogues(63100, -1);
		//	} else if (c.absX == 1502 && c.absY == 3840) {
		//		c.getPA().movePlayer(1502, 3838, 0);
		//	}
			break;
		case 9326:
			if (c.playerLevel[16] < 62) {
				c.sendMessage("You need an Agility level of 62 to pass this.");
				return;
			}
			if (c.absX < 2769) {
				c.getPA().movePlayer(2775, 10003, 0);
			} else {
				c.getPA().movePlayer(2768, 10002, 0);
			}
			break;
		case 4496:
		case 4494:
			if (c.heightLevel == 2) {
				c.getPA().movePlayer(3412, 3540, 1);
			} else if (c.heightLevel == 1) {
				c.getPA().movePlayer(3418, 3540, 0);
			}
			break;
		case 9319:
			if (c.heightLevel == 0)
				c.getPA().movePlayer(c.absX, c.absY, 1);
			else if (c.heightLevel == 1)
				c.getPA().movePlayer(c.absX, c.absY, 2);
			break;

		case 9320:
			if (c.heightLevel == 1)
				c.getPA().movePlayer(c.absX, c.absY, 0);
			else if (c.heightLevel == 2)
				c.getPA().movePlayer(c.absX, c.absY, 1);
			break;
		case 4493:
			if (c.heightLevel == 0) {
				c.getPA().movePlayer(c.absX - 5, c.absY, 1);
			} else if (c.heightLevel == 1) {
				c.getPA().movePlayer(c.absX + 5, c.absY, 2);
			}
			break;

		case 4495:
			if (c.heightLevel == 1 && c.absY > 3538 && c.absY < 3543) {
				c.getPA().movePlayer(c.absX + 5, c.absY, 2);
			} else {
				c.sendMessage("I can't reach that!");
			}
			break;
		case 2623:
			if (c.absX == 2924 && c.absY == 9803) {
				c.getPA().movePlayer(c.absX - 1, c.absY, 0);
			} else if (c.absX == 2923 && c.absY == 9803) {
				c.getPA().movePlayer(c.absX + 1, c.absY, 0);
			}
			break;
		case 15644:
		case 15641:
		case 24306:
		case 24309:
			if (c.heightLevel == 2) {
				// if(Boundary.isIn(c, WarriorsGuild.WAITING_ROOM_BOUNDARY) &&
				// c.heightLevel == 2) {
				c.getWarriorsGuild().handleDoor();
				return;
				// }
			}
			if (c.heightLevel == 0) {
				if (c.absX == 2855 || c.absX == 2854) {
					if (c.absY == 3546)
						c.getPA().movePlayer(c.absX, c.absY - 1, 0);
					else if (c.absY == 3545)
						c.getPA().movePlayer(c.absX, c.absY + 1, 0);
					c.turnPlayerTo(obX, obY);
				}
			}
			break;
		case 15653:
			if (c.absY == 3546) {
				if (c.absX == 2877)
					c.getPA().movePlayer(c.absX - 1, c.absY, 0);
				else if (c.absX == 2876)
					c.getPA().movePlayer(c.absX + 1, c.absY, 0);
				c.turnPlayerTo(obX, obY);
			}
			break;

		case 18987: // Kbd ladder
			c.getPA().movePlayer(3069, 10255, 0);
			break;
		case 1817:
			c.getPA().startLeverTeleport(1647, 3673, 0);
			break;

		case 18988:
			c.getPA().movePlayer(3017, 3850, 0);
			break;

		case 24303:
			c.getPA().movePlayer(2840, 3539, 0);
			break;

		case 16671:
			int distanceToPoint = c.distanceToPoint(2840, 3539);
			if (distanceToPoint < 5) {
				c.getPA().movePlayer(2840, 3539, 2);
			}
			break;
		case 2643:
		case 14888:

			JewelryMaking.mouldInterface(c);
			break;
		case 878:
			c.getDH().sendDialogues(613, -1);
			break;
		case 1733:
			if (c.absY > 3920 && c.inWild())
				c.getPA().movePlayer(3045, 10323, 0);
			break;
		case 1734:
			if (c.absY > 9000 && c.inWild())
				c.getPA().movePlayer(3044, 3927, 0);
			break;
		case 2466:
			if (c.absY > 3920 && c.inWild())
				c.getPA().movePlayer(1622, 3673, 0);
			break;
		case 2467:
			c.getPA().spellTeleport(2604, 3154, 0, false);
			c.sendMessage("This is the dicing area. Place a bet on designated hosts.");
			break;
		case 28851:// wcgate
			if (c.playerLevel[8] < 60) {
				c.sendMessage("You need a Woodcutting level of 60 to enter the Woodcutting Guild.");
				return;
			} else {
				c.getPA().movePlayer(1657, 3505, 0);
			}
			break;
		case 28852:// wcgate
			if (c.playerLevel[8] < 60) {
				c.sendMessage("You need a Woodcutting level of 60 to enter the Woodcutting Guild.");
				return;
			} else {
				c.getPA().movePlayer(1657, 3504, 0);
			}
			break;
		case 2309:
			if (c.getX() == 2998 && c.getY() == 3916) {
				c.getAgility().doWildernessEntrance(c, 2998, 3916, false);
			}
			if (c.absX == 2998 && c.absY == 3917) {
				c.getPA().movePlayer(2998, 3916, 0);
			}
			break;
		case 1766:
			if (c.inWild() && c.absX == 3069 && c.absY == 10255) {
				c.getPA().movePlayer(3017, 3850, 0);
			}
			break;
		case 1765:
			if (c.inWild() && c.absY >= 3847 && c.absY <= 3860) {
				c.getPA().movePlayer(3069, 10255, 0);
			}
			break;

		case 2118:
			if (Boundary.isIn(c, new Boundary(3433, 3536, 3438, 3539))) {
				c.getPA().movePlayer(3438, 3537, 0);
			}
			break;

		case 2114:
			if (Boundary.isIn(c, new Boundary(3433, 3536, 3438, 3539))) {
				c.getPA().movePlayer(3433, 3537, 1);
			}
			break;


		case 7108:
		case 7111:
			if (c.absX == 2907 || c.absX == 2908) {
				if (c.absY == 9698) {
					c.getPA().walkTo(0, -1);
				} else if (c.absY == 9697) {
					c.getPA().walkTo(0, +1);
				}
			}
			break;

		case 2119:
			if (c.heightLevel == 1) {
				if (c.absX == 3412 && (c.absY == 3540 || c.absY == 3541)) {
					c.getPA().movePlayer(3417, c.absY, 2);
				}
			}
			break;

		case 2120:
			if (c.heightLevel == 2) {
				if (c.absX == 3417 && (c.absY == 3540 || c.absY == 3541)) {
					c.getPA().movePlayer(3412, c.absY, 1);
				}
			}
			break;

		case 2102:
		case 2104:
			if (c.heightLevel == 1) {
				if (c.absX == 3426 || c.absX == 3427) {
					if (c.absY == 3556) {
						c.getPA().walkTo(0, -1);
					} else if (c.absY == 3555) {
						c.getPA().walkTo(0, +1);
					}
				}
			}
			break;

		case 1597:
		case 1596:
			// case 7408:
			// case 7407:
			if (c.absY < 9000) {
				if (c.absY > 3903) {
					c.getPA().movePlayer(c.absX, c.absY - 1, 0);
				} else {
					c.getPA().movePlayer(c.absX, c.absY + 1, 0);
				}
			} else if (c.absY > 9917) {
				c.getPA().movePlayer(c.absX, c.absY - 1, 0);
			} else {
				c.getPA().movePlayer(c.absX, c.absY + 1, 0);
			}
			break;
		/*
		 * case 1276: if (!c.inWc() && !c.inDz()) { return; }
		 * c.getWoodcutting().startWoodcutting(0, c.objectX, c.objectY,
		 * c.clickObjectType); break; case 1278: if (!c.inWc() && !c.inDz()) { return; }
		 * c.getWoodcutting().startWoodcutting(1, c.objectX, c.objectY,
		 * c.clickObjectType); break; case 1286: if (!c.inWc() && !c.inDz()) { return; }
		 * c.getWoodcutting().startWoodcutting(2, c.objectX, c.objectY,
		 * c.clickObjectType); break; case 1281: if (!c.inWc() && !c.inDz()) { return; }
		 * c.getWoodcutting().startWoodcutting(3, c.objectX, c.objectY,
		 * c.clickObjectType); break; case 1308: if (!c.inWc() && !c.inDz()) { return; }
		 * c.getWoodcutting().startWoodcutting(4, c.objectX, c.objectY,
		 * c.clickObjectType); break; case 5552: if (!c.inWc() && !c.inDz()) { return; }
		 * c.getWoodcutting().startWoodcutting(5, c.objectX, c.objectY,
		 * c.clickObjectType); break; case 1307: if (!c.inWc() && !c.inDz()) { return; }
		 * c.getWoodcutting().startWoodcutting(6, c.objectX, c.objectY,
		 * c.clickObjectType); break; case 1309: if (!c.inWc() && !c.inDz()) { return; }
		 * c.getWoodcutting().startWoodcutting(7, c.objectX, c.objectY,
		 * c.clickObjectType); break; case 1306: if (!c.inWc() && !c.inDz()) { return; }
		 * c.getWoodcutting().startWoodcutting(8, c.objectX, c.objectY,
		 * c.clickObjectType); break; case 5551: if (!c.inWc() && !c.inDz()) { return; }
		 * c.getWoodcutting().startWoodcutting(9, c.objectX, c.objectY,
		 * c.clickObjectType); break; case 5553: if (!c.inWc() && !c.inDz()) { return; }
		 * c.getWoodcutting().startWoodcutting(10, c.objectX, c.objectY,
		 * c.clickObjectType); break;
		 */

		case 24600:
			c.getDH().sendDialogues(500, -1);
			break;

		case 20973:
			c.getBarrows().useChest();
			break;

		case 20720:
		case 20721:
		case 20722:
		case 20770:
		case 20771:
		case 20772:
			c.getBarrows().spawnBrother(objectType);
			break;

		case 14315:
			PestControl.addToLobby(c);
			break;

		case 14314:
			PestControl.removeFromLobby(c);
			break;

		case 14235:
		case 14233:
			if (c.objectX == 2670) {
				if (c.absX <= 2670) {
					c.absX = 2671;
				} else {
					c.absX = 2670;
				}
			}
			if (c.objectX == 2643) {
				if (c.absX >= 2643) {
					c.absX = 2642;
				} else {
					c.absX = 2643;
				}
			}
			if (c.absX <= 2585) {
				c.absY += 1;
			} else {
				c.absY -= 1;
			}
			c.getPA().movePlayer(c.absX, c.absY, 0);
			break;

		case 245:
			c.getPA().movePlayer(c.absX, c.absY + 2, 2);
			break;
		case 246:
			c.getPA().movePlayer(c.absX, c.absY - 2, 1);
			break;
		case 272:
			c.getPA().movePlayer(c.absX, c.absY, 1);
			break;
		case 273:
			c.getPA().movePlayer(c.absX, c.absY, 0);
			break;
		/* Godwars Door */
		/*
		 * case 26426: // armadyl if (c.absX == 2839 && c.absY == 5295) {
		 * c.getPA().movePlayer(2839, 5296, 2);
		 * c.sendMessage("@blu@May the gods be with you."); } else {
		 * c.getPA().movePlayer(2839, 5295, 2); } break; case 26425: // bandos if
		 * (c.absX == 2863 && c.absY == 5354) { c.getPA().movePlayer(2864, 5354, 2);
		 * c.sendMessage( "@blu@May the gods be with you."); } else {
		 * c.getPA().movePlayer(2863, 5354, 2); } break; case 26428: // bandos if
		 * (c.absX == 2925 && c.absY == 5332) { c.getPA().movePlayer(2925, 5331, 2);
		 * c.sendMessage("@blu@May the gods be with you."); } else {
		 * c.getPA().movePlayer(2925, 5332, 2); } break; case 26427: // bandos if
		 * (c.absX == 2908 && c.absY == 5265) { c.getPA().movePlayer(2907, 5265, 0);
		 * c.sendMessage("@blu@May the gods be with you."); } else {
		 * c.getPA().movePlayer(2908, 5265, 0); } break;
		 */

		case 5960:
			if (!c.leverClicked) {
				c.getDH().sendDialogues(114, 9985);
				c.leverClicked = true;
			} else {
				c.getPA().startLeverTeleport(3090, 3956, 0);
			}
			break;
		case 5959:
			c.getPA().startLeverTeleport(2539, 4712, 0);
			break;
		case 1814:
			if (Boundary.isIn(c, Boundary.ARDOUGNE_BOUNDARY)) {
				c.getDiaryManager().getArdougneDiary().progress(ArdougneDiaryEntry.WILDERNESS_LEVER);
			}
			c.getPA().startLeverTeleport(3158, 3953, 0);
			break;
		case 1815:
			c.getPA().startLeverTeleport(3087, 3500, 0);
			break;
		case 1816:
			c.getPA().startLeverTeleport(2271, 4680, 0);
			c.getDiaryManager().getWildernessDiary().progress(WildernessDiaryEntry.KBD_LAIR);
			break;
		/* Start Brimhavem Dungeon */
		case 2879:
			c.getPA().movePlayer(2542, 4718, 0);
			break;
		case 2878:
			c.getPA().movePlayer(2509, 4689, 0);
			break;
		case 5083:
			c.getPA().movePlayer(2713, 9564, 0);
			c.sendMessage("You enter the dungeon.");
			break;

		case 5103:
			if (c.absX == 2691 && c.absY == 9564) {
				c.getPA().movePlayer(2689, 9564, 0);
			} else if (c.absX == 2689 && c.absY == 9564) {
				c.getPA().movePlayer(2691, 9564, 0);
			}
			break;

		case 5106:
		case 21734:
			if (c.absX == 2674 && c.absY == 9479) {
				c.getPA().movePlayer(2676, 9479, 0);
			} else if (c.absX == 2676 && c.absY == 9479) {
				c.getPA().movePlayer(2674, 9479, 0);
			}
			break;
		case 5105:
		case 21733:
			if (c.absX == 2672 && c.absY == 9499) {
				c.getPA().movePlayer(2674, 9499, 0);
			} else if (c.absX == 2674 && c.absY == 9499) {
				c.getPA().movePlayer(2672, 9499, 0);
			}
			break;

		case 5107:
		case 21735:
			if (c.absX == 2693 && c.absY == 9482) {
				c.getPA().movePlayer(2695, 9482, 0);
			} else if (c.absX == 2695 && c.absY == 9482) {
				c.getPA().movePlayer(2693, 9482, 0);
			}
			break;

		case 21731:
			if (c.absX == 2691) {
				c.getPA().movePlayer(2689, 9564, 0);
			} else if (c.absX == 2689) {
				c.getPA().movePlayer(2691, 9564, 0);
			}
			break;

		case 5104:
		case 21732:
			if (c.absX == 2683 && c.absY == 9568) {
				c.getPA().movePlayer(2683, 9570, 0);
			} else if (c.absX == 2683 && c.absY == 9570) {
				c.getPA().movePlayer(2683, 9568, 0);
			}
			break;

		case 5100:
			if (c.absY <= 9567) {
				c.getPA().movePlayer(2655, 9573, 0);
			} else if (c.absY >= 9572) {
				c.getPA().movePlayer(2655, 9566, 0);
			}
			break;
		case 21728:
			if (c.playerLevel[16] < 34) {
				c.sendMessage("You need an Agility level of 34 to pass this.");
				return;
			}
			if (c.absY == 9566) {
				AgilityHandler.delayEmote(c, "CRAWL", 2655, 9573, 0, 2);
			} else {
				AgilityHandler.delayEmote(c, "CRAWL", 2655, 9566, 0, 2);
			}
			break;
		case 5099:
		case 21727:
			if (c.playerLevel[16] < 34) {
				c.sendMessage("You need an Agility level of 34 to pass this.");
				return;
			}
			if (c.objectX == 2698 && c.objectY == 9498) {
				c.getPA().movePlayer(2698, 9492, 0);
			} else if (c.objectX == 2698 && c.objectY == 9493) {
				c.getPA().movePlayer(2698, 9499, 0);
			}
			break;
		case 5088:
		case 20882:
			if (c.playerLevel[16] < 30) {
				c.sendMessage("You need an Agility level of 30 to pass this.");
				return;
			}
			c.getPA().movePlayer(2687, 9506, 0);
			break;
		case 5090:
		case 20884:
			if (c.playerLevel[16] < 30) {
				c.sendMessage("You need an Agility level of 30 to pass this.");
				return;
			}
			c.getPA().movePlayer(2682, 9506, 0);
			break;

		case 16511:
			if (c.playerLevel[16] < 51) {
				c.sendMessage("You need an agility level of at least 51 to squeeze through.");
				return;
			}
			if (c.absX == 3149 && c.absY == 9906) {
				c.getDiaryManager().getVarrockDiary().progress(VarrockDiaryEntry.OBSTACLE_PIPE);
				c.getPA().movePlayer(3155, 9906, 0);
			} else if (c.absX == 3155 && c.absY == 9906) {
				c.getPA().movePlayer(3149, 9906, 0);
			}
			break;

		case 5110:
		case 21738:
			if (c.playerLevel[16] < 12) {
				c.sendMessage("You need an Agility level of 12 to pass this.");
				return;
			}
			c.getPA().movePlayer(2647, 9557, 0);
			break;
		case 5111:
		case 21739:
			if (c.playerLevel[16] < 12) {
				c.sendMessage("You need an Agility level of 12 to pass this.");
				return;
			}
			c.getPA().movePlayer(2649, 9562, 0);
			break;
		case 27362:// lizardmen
			if (c.absY > 3688) {
				AgilityHandler.delayEmote(c, "CLIMB_DOWN", 1454, 3690, 0, 2);
				c.sendMessage("You climb down into Shayzien Assault.");
			} else
				AgilityHandler.delayEmote(c, "CLIMB_DOWN", 1477, 3690, 0, 2);
			c.sendMessage("You climb down into Lizardman Camp.");
			break;
		case 4155:// zulrah
			c.getPA().movePlayer(2200, 3055, 0);
			c.sendMessage("You climb down.");
			break;
		case 5084:
			c.getPA().movePlayer(2744, 3151, 0);
			c.sendMessage("You exit the dungeon.");
			break;
		/* End Brimhavem Dungeon */
		case 6481:
			c.getPA().movePlayer(3233, 9315, 0);
			break;

		/*
		 * case 17010: if (c.playerMagicBook == 0) {
		 * c.sendMessage("You switch spellbook to lunar magic.");
		 * c.setSidebarInterface(6, 29999); c.playerMagicBook = 2; c.autocasting =
		 * false; c.autocastId = -1; c.getPA().resetAutocast(); break; } if
		 * (c.playerMagicBook == 1) {
		 * c.sendMessage("You switch spellbook to lunar magic.");
		 * c.setSidebarInterface(6, 29999); c.playerMagicBook = 2; c.autocasting =
		 * false; c.autocastId = -1; c.getPA().resetAutocast(); break; } if
		 * (c.playerMagicBook == 2) { c.setSidebarInterface(6, 1151); c.playerMagicBook
		 * = 0; c.autocasting = false;
		 * c.sendMessage("You feel a drain on your memory."); c.autocastId = -1;
		 * c.getPA().resetAutocast(); break; } break;
		 */

		case 1551:
			if (c.absX == 3252 && c.absY == 3266) {
				c.getPA().movePlayer(3253, 3266, 0);
			}
			if (c.absX == 3253 && c.absY == 3266) {
				c.getPA().movePlayer(3252, 3266, 0);
			}
			break;
		case 1553:
			if (c.absX == 3252 && c.absY == 3267) {
				c.getPA().movePlayer(3253, 3266, 0);
			}
			if (c.absX == 3253 && c.absY == 3267) {
				c.getPA().movePlayer(3252, 3266, 0);
			}
			break;
		case 3044:
		case 24009:
		case 26300:
		case 16469:
		case 14838:
		case 2030:
			c.getSmithing().sendSmelting();
			break;
		/*
		 * case 2030: if (c.absX == 1718 && c.absY == 3468) {
		 * c.getSmithing().sendSmelting(); } else { c.getSmithing().sendSmelting(); }
		 * break;
		 */

		/* AL KHARID */
		case 2883:
		case 2882:
			c.getDH().sendDialogues(1023, 925);
			break;
		// case 2412:
		// Sailing.startTravel(c, 1);
		// break;
		// case 2414:
		// Sailing.startTravel(c, 2);
		// break;
		// case 2083:
		// Sailing.startTravel(c, 5);
		// break;
		// case 2081:
		// Sailing.startTravel(c, 6);
		// break;
		// case 14304:
		// Sailing.startTravel(c, 14);
		// break;
		// case 14306:
		// Sailing.startTravel(c, 15);
		// break;

		case 2213:
		case 24101:
		case 3045:
		case 14367:
		case 3193:
		case 10517:
		case 11402:
		case 26972:
		case 4483:
		case 25808:
		case 11744:
		case 10060:
		case 12309:
		case 10058:
		case 2693:
		case 21301:
		case 6943:
		case 3194:
		case 10661:
			c.getPA().openUpBank();
			break;

		case 21305:
			if (c.getItems().playerHasItem(10810, 5)) {
				c.getItems().deleteItem2(10810, 5);
				c.getItems().addItem(10826, 1);
				c.getDiaryManager().getFremennikDiary().progress(FremennikDiaryEntry.FREMENNIK_SHIELD);
			} else {
				c.sendMessage("You need 5 arctic pine logs to create a fremennik shield.");
				return;
			}
			break;

		case 3506:
		case 3507:
			if (c.absY == 3458)
				c.getPA().movePlayer(c.absX, 3457, 0);
			c.getDiaryManager().getMorytaniaDiary().progress(MorytaniaDiaryEntry.MORYTANIA_SWAMP);
			if (c.absY == 3457)
				c.getPA().movePlayer(c.absX, 3458, 0);
			break;

		case 11665:
			if (c.absX == 2658)
				c.getPA().movePlayer(2659, 3437, 0);
			c.getDiaryManager().getKandarinDiary().progress(KandarinDiaryEntry.RANGING_GUILD);
			if (c.absX == 2659)
				c.getPA().movePlayer(2657, 3439, 0);
			break;

		/**
		 * Entering the Fight Caves.
		 */
		case 11833:
			if (Boundary.entitiesInArea(Boundary.FIGHT_CAVE) >= 50) {
				c.sendMessage("There are too many people using the fight caves at the moment. Please try again later");
				return;
			}
			c.getDH().sendDialogues(633, -1);
			break;

		case 20667:
		case 20668:
		case 20669:
		case 20670:
		case 20671:
		case 20672:
			c.getBarrows().moveUpStairs(objectType);
			break;

		/**
		 * Clicking on the Ancient Altar.
		 */
		case 6552:
			if (c.inWild()) {
				return;
			}
			c.autocasting = false;
			c.autocastId = -1;
			c.getPA().resetAutocast();
			if (c.absY == 9312) {
				c.getDiaryManager().getDesertDiary().progress(DesertDiaryEntry.ACTIVATE_ANCIENT);
			}
			PlayerAssistant.switchSpellBook(c);
			break;

		/**
		 * c.setSidebarInterface(6, 1151); Recharing prayer points.
		 */
		case 20377:
			if (c.inWild()) {
				return;
			}
			if (c.playerLevel[5] < c.getPA().getLevelForXP(c.playerXP[5])) {

				if (c.getPA().getLevelForXP(c.playerXP[5]) > 85 && c.playerLevel[5] < 15) {
					c.getDiaryManager().getDesertDiary().progress(DesertDiaryEntry.PRAY_SOPHANEM);
				}
				c.startAnimation(645);
				c.playerLevel[5] = c.getPA().getLevelForXP(c.playerXP[5]);
				c.sendMessage("You recharge your prayer points.");
				c.getPA().refreshSkill(5);
			} else {
				c.sendMessage("You already have full prayer points.");
			}
			break;
		case 61:
			if (c.inWild()) {
				return;
			}
			if (c.absY >= 3508 && c.absY <= 3513) {
				if (c.playerLevel[5] < c.getPA().getLevelForXP(c.playerXP[5])) {
					if (Boundary.isIn(c, Boundary.VARROCK_BOUNDARY)
							&& c.getDiaryManager().getVarrockDiary().hasCompleted("HARD")) {
						if (c.prayerActive[25]) {
							c.getDiaryManager().getVarrockDiary().progress(VarrockDiaryEntry.PRAY_WITH_PIETY);
						}
					}
					c.startAnimation(645);
					c.playerLevel[5] = c.getPA().getLevelForXP(c.playerXP[5]);
					c.sendMessage("You recharge your prayer points.");
					c.getPA().refreshSkill(5);
				} else {
					c.sendMessage("You already have full prayer points.");
				}
			}
			break;

		case 410:
			if (c.inWild()) {
				return;
			}
			if (c.playerLevel[5] == c.getPA().getLevelForXP(c.playerXP[5])) {
				c.sendMessage("You already have full prayer points.");
				return;
			}
			if (Boundary.isIn(c, Boundary.TAVERLY_BOUNDARY)) {
				if (c.getItems().isWearingItem(5574) && c.getItems().isWearingItem(5575)
						&& c.getItems().isWearingItem(5576)) {
					c.getDiaryManager().getFaladorDiary().progress(FaladorDiaryEntry.ALTAR_OF_GUTHIX);
				}
			}
			c.startAnimation(645);
			c.playerLevel[5] = c.getPA().getLevelForXP(c.playerXP[5]);
			c.sendMessage("You recharge your prayer points.");
			c.getPA().refreshSkill(5);
			break;

		case 409:
		case 6817:
		case 14860:
			if (c.inWild()) {
				return;
			}
			if (c.playerLevel[5] == c.getPA().getLevelForXP(c.playerXP[5])) {
				c.sendMessage("You already have full prayer points.");
				return;
			}
			if (Boundary.isIn(c, Boundary.VARROCK_BOUNDARY)) {
				if (c.prayerActive[23]) {
					c.getDiaryManager().getVarrockDiary().progress(VarrockDiaryEntry.PRAY_WITH_SMITE);
				}
			}
			if (Boundary.isIn(c, Boundary.ARDOUGNE_BOUNDARY)) {
				if (c.prayerActive[24]) {
					if (!c.getDiaryManager().getArdougneDiary().hasCompleted("MEDIUM")) {
						c.sendMessage("You must have completed all the medium tasks in the ardougne diary to do this.");
						return;
					}
					c.getDiaryManager().getArdougneDiary().progress(ArdougneDiaryEntry.PRAY_WITH_CHIVALRY);
				}
			}
			c.startAnimation(645);
			c.playerLevel[5] = c.getPA().getLevelForXP(c.playerXP[5]);
			c.sendMessage("You recharge your prayer points.");
			c.getPA().refreshSkill(5);
			break;

		case 411:
			if (c.playerLevel[5] < c.getPA().getLevelForXP(c.playerXP[5])) {
				if (c.inWild()) {
					c.getDiaryManager().getWildernessDiary().progress(WildernessDiaryEntry.WILDERNESS_ALTAR);
				}
				c.startAnimation(645);
				c.playerLevel[5] = c.getPA().getLevelForXP(c.playerXP[5]);
				c.sendMessage("You recharge your prayer points.");
				c.getPA().refreshSkill(5);
			} else {
				c.sendMessage("You already have full prayer points.");
			}
			break;

		case 14896:
			c.turnPlayerTo(obX, obY);
			FlaxPicking.getInstance().pick(c, new Location3D(obX, obY, c.heightLevel));
			break;

		case 412:
			if (c.inWild()) {
				return;
			}
			if (c.getMode().isIronman() || c.getMode().isUltimateIronman()) {
				c.sendMessage("Your game mode prohibits use of this altar.");
				return;
			}
			// if (c.absY >= 3504 && c.absY <= 3507) {
			if (c.specAmount < 10.0) {
				if (c.specRestore > 0) {
					int seconds = ((int) Math.floor(c.specRestore * 0.6));
					c.sendMessage("You have to wait another " + seconds + " seconds to use this altar.");
					return;
				}
				if (c.getRights().isOrInherits(Right.CONTRIBUTOR)) {
					c.specRestore = 120;
					c.specAmount = 10.0;
					c.getItems().addSpecialBar(c.playerEquipment[c.playerWeapon]);
					c.sendMessage("Your special attack has been restored. You can restore it again in 3 minutes.");
				} else {
					c.specRestore = 240;
					c.specAmount = 10.0;
					c.getItems().addSpecialBar(c.playerEquipment[c.playerWeapon]);
					c.sendMessage("Your special attack has been restored. You can restore it again in 6 minutes.");
				}
			}
			// }
			break;

		case 26366: // Godwars altars
		case 26365:
		case 26364:
		case 26363:
			if (c.inWild()) {
				return;
			}
			if (c.gwdAltar > 0) {
				int seconds = ((int) Math.floor(c.gwdAltar * 0.6));
				c.sendMessage("You have to wait another " + seconds + " seconds to use this altar.");
				return;
			}
			if (c.playerLevel[5] < c.getPA().getLevelForXP(c.playerXP[5])) {
				c.startAnimation(645);
				c.playerLevel[5] = c.getPA().getLevelForXP(c.playerXP[5]);
				c.sendMessage("You recharge your prayer points.");
				c.gwdAltar = 600;
				c.getPA().refreshSkill(5);
			} else {
				c.sendMessage("You already have full prayer points.");
			}
			break;

		/**
		 * Aquring god capes.
		 */
		case 2873:
			if (!c.getItems().ownsCape()) {
				c.startAnimation(645);
				c.sendMessage("Saradomin blesses you with a cape.");
				c.getItems().addItem(2412, 1);
			} else {
				c.sendMessage("You already have a cape");
			}
			break;
		case 2875:
			if (!c.getItems().ownsCape()) {
				c.startAnimation(645);
				c.sendMessage("Guthix blesses you with a cape.");
				c.getItems().addItem(2413, 1);
			} else {
				c.sendMessage("You already have a cape");
			}
			break;
		case 2874:
			if (!c.getItems().ownsCape()) {
				c.startAnimation(645);
				c.sendMessage("Zamorak blesses you with a cape.");
				c.getItems().addItem(2414, 1);
			} else {
				c.sendMessage("You already have a cape");
			}
			break;

		/**
		 * Oblisks in the wilderness.
		 */
		case 14829:
		case 14830:
		case 14827:
		case 14828:
		case 14826:
		case 14831:

			break;

		/**
		 * Clicking certain doors.
		 */
		case 6749:
			if (obX == 3562 && obY == 9678) {
				c.getPA().object(3562, 9678, 6749, -3, 0);
				c.getPA().object(3562, 9677, 6730, -1, 0);
			} else if (obX == 3558 && obY == 9677) {
				c.getPA().object(3558, 9677, 6749, -1, 0);
				c.getPA().object(3558, 9678, 6730, -3, 0);
			}
			break;

		case 6730:
			if (obX == 3558 && obY == 9677) {
				c.getPA().object(3562, 9678, 6749, -3, 0);
				c.getPA().object(3562, 9677, 6730, -1, 0);
			} else if (obX == 3558 && obY == 9678) {
				c.getPA().object(3558, 9677, 6749, -1, 0);
				c.getPA().object(3558, 9678, 6730, -3, 0);
			}
			break;

		case 6727:
			if (obX == 3551 && obY == 9684) {
				c.sendMessage("You cant open this door..");
			}
			break;

		case 6746:
			if (obX == 3552 && obY == 9684) {
				c.sendMessage("You cant open this door..");
			}
			break;

		case 6748:
			if (obX == 3545 && obY == 9678) {
				c.getPA().object(3545, 9678, 6748, -3, 0);
				c.getPA().object(3545, 9677, 6729, -1, 0);
			} else if (obX == 3541 && obY == 9677) {
				c.getPA().object(3541, 9677, 6748, -1, 0);
				c.getPA().object(3541, 9678, 6729, -3, 0);
			}
			break;

		case 6729:
			if (obX == 3545 && obY == 9677) {
				c.getPA().object(3545, 9678, 6748, -3, 0);
				c.getPA().object(3545, 9677, 6729, -1, 0);
			} else if (obX == 3541 && obY == 9678) {
				c.getPA().object(3541, 9677, 6748, -1, 0);
				c.getPA().object(3541, 9678, 6729, -3, 0);
			}
			break;

		case 6726:
			if (obX == 3534 && obY == 9684) {
				c.getPA().object(3534, 9684, 6726, -4, 0);
				c.getPA().object(3535, 9684, 6745, -2, 0);
			} else if (obX == 3535 && obY == 9688) {
				c.getPA().object(3535, 9688, 6726, -2, 0);
				c.getPA().object(3534, 9688, 6745, -4, 0);
			}
			break;

		case 6745:
			if (obX == 3535 && obY == 9684) {
				c.getPA().object(3534, 9684, 6726, -4, 0);
				c.getPA().object(3535, 9684, 6745, -2, 0);
			} else if (obX == 3534 && obY == 9688) {
				c.getPA().object(3535, 9688, 6726, -2, 0);
				c.getPA().object(3534, 9688, 6745, -4, 0);
			}
			break;

		case 6743:
			if (obX == 3545 && obY == 9695) {
				c.getPA().object(3545, 9694, 6724, -1, 0);
				c.getPA().object(3545, 9695, 6743, -3, 0);
			} else if (obX == 3541 && obY == 9694) {
				c.getPA().object(3541, 9694, 6724, -1, 0);
				c.getPA().object(3541, 9695, 6743, -3, 0);
			}
			break;

		case 6724:
			if (obX == 3545 && obY == 9694) {
				c.getPA().object(3545, 9694, 6724, -1, 0);
				c.getPA().object(3545, 9695, 6743, -3, 0);
			} else if (obX == 3541 && obY == 9695) {
				c.getPA().object(3541, 9694, 6724, -1, 0);
				c.getPA().object(3541, 9695, 6743, -3, 0);
			}
			break;

		case 1516:
		case 1519:
			if (c.objectY == 9698) {
				if (c.absY >= c.objectY)
					c.getPA().walkTo(0, -1);
				else
					c.getPA().walkTo(0, 1);
				break;
			}

		case 11737:
			if (!c.getRights().isOrInherits(Right.CONTRIBUTOR)) {
				return;
			}
			c.getPA().movePlayer(3365, 9641, 0);
			break;

		// case 12355:
		// if (!c.getRights().isOrInherits(Right.CONTRIBUTOR)) {
		// return;
		// }
		// c.getPA().movePlayer(3577, 9927, 0);
		// break;

		case 5126:
		case 2100:
			if (c.absY == 3554)
				c.getPA().walkTo(0, 1);
			else
				c.getPA().walkTo(0, -1);
			break;

		case 1759:
			if (c.objectX == 2884 && c.objectY == 3397)
				c.getPA().movePlayer(c.absX, c.absY + 6400, 0);
			break;
		case 1557:
		case 1558:
		case 7169:
			if ((c.objectX == 3106 || c.objectX == 3105) && c.objectY == 9944) {
				if (c.getY() > c.objectY)
					c.getPA().walkTo(0, -1);
				else
					c.getPA().walkTo(0, 1);
			} else {
				if (c.getX() > c.objectX)
					c.getPA().walkTo(-1, 0);
				else
					c.getPA().walkTo(1, 0);
			}
			break;
		case 2558:
			c.sendMessage("This door is locked.");
			break;

		case 9294:
			if (c.absX < c.objectX) {
				c.getPA().movePlayer(c.objectX + 1, c.absY, 0);
			} else if (c.absX > c.objectX) {
				c.getPA().movePlayer(c.objectX - 1, c.absY, 0);
			}
			break;

		case 9293:
			if (c.absX < c.objectX) {
				c.getPA().movePlayer(2892, 9799, 0);
			} else {
				c.getPA().movePlayer(2886, 9799, 0);
			}
			break;

		case 10529:
		case 10527:
			if (c.absY <= c.objectY)
				c.getPA().walkTo(0, 1);
			else
				c.getPA().walkTo(0, -1);
			break;

		case 733:
			GlobalObject objectOne = null;
			int chance = c.getRechargeItems().hasAnyItem(13108, 13109, 13110, 13111) ? 0 : 4;
			c.startAnimation(451);
			c.sendMessage("You fail to cut through it.");
			if (Misc.random(chance) == 0) {
				c.sendMessage("You slash the web apart.");
				if (c.objectX == 3092 && c.objectY == 3957) {
					objectOne = new GlobalObject(734, obX, obY, c.heightLevel, 2, 0, 50, 733);
				} else if (c.objectX == 3095 && c.objectY == 3957) {
					objectOne = new GlobalObject(734, obX, obY, c.heightLevel, 0, 0, 50, 733);
				} else if (c.objectX == 3158 && c.objectY == 3951) {
					objectOne = new GlobalObject(734, obX, obY, c.heightLevel, 1, 10, 50, 733);
				} else if (c.objectX == 3105 && c.objectY == 3958 || c.objectX == 3106 && c.objectY == 3958) {
					objectOne = new GlobalObject(734, obX, obY, c.heightLevel, 3, 10, 50, 733);
				}
				if (objectOne != null) {
					Server.getGlobalObjects().add(objectOne);
				}
			}
			break;

		case 7407:
			GlobalObject gate;
			gate = new GlobalObject(objectType, obX, obY, c.heightLevel, 2, 0, 50, 7407);
			Server.getGlobalObjects().add(gate);
			break;

		case 7408:
			GlobalObject secondGate;
			secondGate = new GlobalObject(objectType, obX, obY, c.heightLevel, 0, 0, 50, 7408);
			Server.getGlobalObjects().add(secondGate);
			break;

		/**
		 * Forfeiting a duel.
		 */
		case 3203:
			DuelSession session = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c,
					MultiplayerSessionType.DUEL);
			if (Objects.isNull(session)) {
				return;
			}
			if (!Boundary.isIn(c, Boundary.DUEL_ARENA)) {
				return;
			}
			if (session.getRules().contains(Rule.FORFEIT)) {
				c.sendMessage("You are not permitted to forfeit the duel.");
				return;
			}
			break;

		}
	}

}