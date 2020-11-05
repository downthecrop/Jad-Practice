package ethos.model.players;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import ethos.Server;
import ethos.event.CycleEvent;
import ethos.event.CycleEventContainer;
import ethos.event.CycleEventHandler;
import ethos.model.content.SkillcapePerks;
import ethos.model.content.achievement_diary.wilderness.WildernessDiaryEntry;
import ethos.model.entity.HealthStatus;
import ethos.model.items.ItemAssistant;
import ethos.model.multiplayer_session.MultiplayerSessionType;
import ethos.model.multiplayer_session.duel.DuelSession;
import ethos.model.multiplayer_session.duel.DuelSessionRules.Rule;
import ethos.model.players.combat.Hitmark;

/**
 * @author Sanity
 */

public class Potions {

	private Player c;

	public Potions(Player c) {
		this.c = c;
	}

	public void handlePotion(int itemId, int slot) {
		if (itemId >= 20989 && itemId <= 20992) {
			if (!Boundary.isIn(c, Boundary.RAID_MAIN)) {
				c.sendMessage("I should not attempt to drink this outside of the Raids dungeons.");
				return;
			}
		}
		if (Boundary.isIn(c, Boundary.DUEL_ARENA)) {
			if (itemId >= 11730 && itemId <= 11733) {
				c.sendMessage("You are not allowed to drink overloads whilst in the duel arena.");
				return;
			}
			DuelSession session = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c, MultiplayerSessionType.DUEL);
			if (Objects.nonNull(session)) {
				if (session.getRules().contains(Rule.NO_DRINKS)) {
					c.sendMessage("Drinks have been disabled for this duel.");
					return;
				}
			}
		}
		if (c.isDead) {
			return;
		}
		if (System.currentTimeMillis() - c.lastSpear < 3000) {
			c.sendMessage("You are stunned and can not drink!");
			return;
		}
		if (c.potionTimer.elapsed() > 1200) {
			c.potionTimer.reset();
			switch (itemId) {
			case 3040:
				drinkMagicPotion(itemId, 3042, slot, 6, false); // Magic pots
				break;
			case 3042:
				drinkMagicPotion(itemId, 3044, slot, 6, false);
				break;
			case 3044:
				drinkMagicPotion(itemId, 3046, slot, 6, false);
				break;
			case 3046:
				drinkMagicPotion(itemId, 229, slot, 6, false);
				break;
			case 6685:
				drinkSaradominBrew(itemId, 6687, slot); // saradomin brew
				break;
			case 6687:
				drinkSaradominBrew(itemId, 6689, slot);
				break;
			case 6689:
				drinkSaradominBrew(itemId, 6691, slot);
				break;
			case 6691:
				drinkSaradominBrew(itemId, 229, slot);
				break;
			case 2450:
				drinkZamorakBrew(itemId, 189, slot); // zammorak brew
				break;
			case 189:
				drinkZamorakBrew(itemId, 191, slot);
				break;
			case 191:
				drinkZamorakBrew(itemId, 193, slot);
				break;
			case 193:
				drinkZamorakBrew(itemId, 229, slot);
				break;
			case 2436:
				drinkStatPotion(itemId, 145, slot, 0, true); // sup attack
				break;
			case 145:
				drinkStatPotion(itemId, 147, slot, 0, true);
				break;
			case 147:
				drinkStatPotion(itemId, 149, slot, 0, true);
				break;
			case 149:
				drinkStatPotion(itemId, 229, slot, 0, true);
				break;
			case 2440:
				drinkStatPotion(itemId, 157, slot, 2, true); // sup str
				break;
			case 157:
				drinkStatPotion(itemId, 159, slot, 2, true);
				break;
			case 159:
				drinkStatPotion(itemId, 161, slot, 2, true);
				break;
			case 161:
				drinkStatPotion(itemId, 229, slot, 2, true);
				break;
			case 2444:
				drinkStatPotion(itemId, 169, slot, 4, false); // range pot
				break;
			case 169:
				drinkStatPotion(itemId, 171, slot, 4, false);
				break;
			case 171:
				drinkStatPotion(itemId, 173, slot, 4, false);
				break;
			case 173:
				drinkStatPotion(itemId, 229, slot, 4, false);
				break;
			case 2432:
				drinkStatPotion(itemId, 133, slot, 1, false); // def pot
				break;
			case 133:
				drinkStatPotion(itemId, 135, slot, 1, false);
				break;
			case 135:
				drinkStatPotion(itemId, 137, slot, 1, false);
				break;
			case 137:
				drinkStatPotion(itemId, 229, slot, 1, false);
				break;
			case 113:
				drinkStatPotion(itemId, 115, slot, 2, false); // str pot
				break;
			case 115:
				drinkStatPotion(itemId, 117, slot, 2, false);
				break;
			case 117:
				drinkStatPotion(itemId, 119, slot, 2, false);
				break;
			case 119:
				drinkStatPotion(itemId, 229, slot, 2, false);
				break;
			case 2428:
				drinkStatPotion(itemId, 121, slot, 0, false); // attack pot
				break;
			case 121:
				drinkStatPotion(itemId, 123, slot, 0, false);
				break;
			case 123:
				drinkStatPotion(itemId, 125, slot, 0, false);
				break;
			case 125:
				drinkStatPotion(itemId, 229, slot, 0, false);
				break;
			case 2442:
				drinkStatPotion(itemId, 163, slot, 1, true); // super def pot
				break;
			case 163:
				drinkStatPotion(itemId, 165, slot, 1, true);
				break;
			case 165:
				drinkStatPotion(itemId, 167, slot, 1, true);
				break;
			case 167:
				drinkStatPotion(itemId, 229, slot, 1, true);
				break;
			case 2430:
				drinkRestorePot(itemId, 127, slot); // restore
				break;
			case 127:
				drinkRestorePot(itemId, 129, slot);
				break;
			case 129:
				drinkRestorePot(itemId, 131, slot);
				break;
			case 131:
				drinkRestorePot(itemId, 229, slot);
				break;
			case 3024:
				drinkSuperRestorePot(itemId, 3026, slot); // sup restore
				break;
			case 3026:
				drinkSuperRestorePot(itemId, 3028, slot);
				break;
			case 3028:
				drinkSuperRestorePot(itemId, 3030, slot);
				break;
			case 3030:
				drinkSuperRestorePot(itemId, 229, slot);
				break;
			// case 10925:
			// drinkPrayerPot(itemId, 10927, slot, true); // sanfew serums
			// curePoison(0);
			// break;
			// case 10927:
			// drinkPrayerPot(itemId, 10929, slot, true);
			// curePoison(0);
			// break;
			// case 10929:
			// drinkPrayerPot(itemId, 10931, slot, true);
			// curePoison(0);
			// break;
			// case 10931:
			// drinkPrayerPot(itemId, 229, slot, true);
			// curePoison(0);
			// break;
			case 2438:
				drinkStatPotion(itemId, 151, slot, 10, 3); // fishing pot
				break;
			case 151:
				drinkStatPotion(itemId, 153, slot, 10, 3);
				break;
			case 153:
				drinkStatPotion(itemId, 155, slot, 10, 3);
				break;
			case 155:
				drinkStatPotion(itemId, 229, slot, 10, 3);
				;
				break;
			case 3032:
				drinkStatPotion(itemId, 3034, slot, 16, 3); // agility pot
				break;
			case 3034:
				drinkStatPotion(itemId, 3036, slot, 16, 3);
				break;
			case 3036:
				drinkStatPotion(itemId, 3038, slot, 16, 3);
				break;
			case 3038:
				drinkStatPotion(itemId, 229, slot, 16, 3);
				;
				break;
			case 2434:
				drinkPrayerPot(itemId, 139, slot); // pray pot
				break;
			case 139:
				drinkPrayerPot(itemId, 141, slot);
				break;
			case 141:
				drinkPrayerPot(itemId, 143, slot);
				break;
			case 143:
				drinkPrayerPot(itemId, 229, slot);
				break;
			case 2446:
				drinkAntiPoison(itemId, 175, slot, 200); // anti poisons
				break;
			case 175:
				drinkAntiPoison(itemId, 177, slot, 200);
				break;
			case 177:
				drinkAntiPoison(itemId, 179, slot, 200);
				break;
			case 179:
				drinkAntiPoison(itemId, 229, slot, 200);
				break;
			case 2448:
				drinkAntiPoison(itemId, 181, slot, 600); // superanti poisons
				break;
			case 181:
				drinkAntiPoison(itemId, 183, slot, 600);
				break;
			case 183:
				drinkAntiPoison(itemId, 185, slot, 600);
				break;
			case 185:
				drinkAntiPoison(itemId, 229, slot, 600);
				break;

			case 5943:
				drinkAntidote(itemId, 5945, slot, 518); // antidote+
				break;
			case 5945:
				drinkAntidote(itemId, 5947, slot, 518);
				break;
			case 5947:
				drinkAntidote(itemId, 5949, slot, 518);
				break;
			case 5949:
				drinkAntidote(itemId, 229, slot, 518);
				break;

			case 5952:
				drinkAntidote(itemId, 5954, slot, 1200); // antidote++
				break;
			case 5954:
				drinkAntidote(itemId, 5956, slot, 1200);
				break;
			case 5956:
				drinkAntidote(itemId, 5958, slot, 1200);
				break;
			case 5958:
				drinkAntidote(itemId, 229, slot, 1200);
				break;

			case 9739:
				drinkCombatPotion(itemId, 9741, slot); // combat pot
				break;
			case 9741:
				drinkCombatPotion(itemId, 9743, slot);
				break;
			case 9743:
				drinkCombatPotion(itemId, 9745, slot);
				break;
			case 9745:
				drinkCombatPotion(itemId, 229, slot);
				break;
			case 12695:
				drinkStatPotion(itemId, 12697, slot, 0, true); // supercombat pot
				enchanceStat(1, true);
				enchanceStat(2, true);
				break;
			case 12697:
				drinkStatPotion(itemId, 12699, slot, 0, true);
				enchanceStat(1, true);
				enchanceStat(2, true);
				break;
			case 12699:
				drinkStatPotion(itemId, 12701, slot, 0, true);
				enchanceStat(1, true);
				enchanceStat(2, true);
				break;
			case 12701:
				drinkStatPotion(itemId, 229, slot, 0, true);
				enchanceStat(1, true);
				enchanceStat(2, true);
				break;
			case 20992:
				doOverload(itemId, 20991, slot);
				break;
			case 20991:
				doOverload(itemId, 20990, slot);
				break;
			case 20990:
				doOverload(itemId, 20989, slot);
				break;
			case 20989:
				doOverload(itemId, -1, slot);
				break;
			case 11730:
				doOverload(itemId, 11731, slot);
				break;
			case 11731:
				doOverload(itemId, 11732, slot);
				break;
			case 11732:
				doOverload(itemId, 11733, slot);
				break;
			case 11733:
				doOverload(itemId, 229, slot);
				break;
			case 12905:
				drinkAntiVenom(12907, slot, 1);
				break;
			case 12907:
				drinkAntiVenom(12909, slot, 1);
				break;
			case 12909:
				drinkAntiVenom(12911, slot, 1);
				break;
			case 12911:
				drinkAntiVenom(229, slot, 1);
				break;
			case 12913:
				drinkAntiVenom(12915, slot, 500);
				break;
			case 12915:
				drinkAntiVenom(12917, slot, 500);
				break;
			case 12917:
				drinkAntiVenom(12919, slot, 500);
				break;
			case 12919:
				drinkAntiVenom(229, slot, 500);
				break;
			case 12625:
				drinkStamina(12627, slot, TimeUnit.MINUTES.toMillis(2));
				break;
			case 12627:
				drinkStamina(12629, slot, TimeUnit.MINUTES.toMillis(2));
				break;
			case 12629:
				drinkStamina(12631, slot, TimeUnit.MINUTES.toMillis(2));
				break;
			case 12631:
				drinkStamina(229, slot, TimeUnit.MINUTES.toMillis(2));
				break;
			case 2452:
				drinkAntifire(2454, slot, TimeUnit.MINUTES.toMillis(6));
				break;
			case 2454:
				drinkAntifire(2456, slot, TimeUnit.MINUTES.toMillis(6));
				break;
			case 2456:
				drinkAntifire(2458, slot, TimeUnit.MINUTES.toMillis(6));
				break;
			case 2458:
				drinkAntifire(229, slot, TimeUnit.MINUTES.toMillis(6));
				break;

			/*
			 * Run energy
			 */
			case 3008:
			case 3010:
			case 3012:
				drinkEnergyPots(itemId, itemId + 2, slot);
				break;

			case 3014:
				drinkEnergyPots(itemId, 229, slot);
				break;

			case 3016:
			case 3018:
			case 3020:
				drinkSuperEnergyPots(itemId, itemId + 2, slot);
				break;

			case 3022:
				drinkSuperEnergyPots(itemId, 229, slot);
				break;

			/*
			 * case 3144: drinkStatPotion2(itemId, -1, slot, 1, true); c.sendMessage("Eating it"); break;
			 */
			}
		}
	}

	public void drinkAntifire(int replaceItem, int slot, long duration) {
		c.startAnimation(829);
		c.playerItems[slot] = replaceItem + 1;
		c.getItems().resetItems(3214);
		c.sendMessage("You now have resistance against dragon fire.");
		c.lastAntifirePotion = System.currentTimeMillis();
		c.antifireDelay = duration;
		c.getPA().sendGameTimer(ClientGameTimer.ANTIFIRE, TimeUnit.MILLISECONDS, (int) (duration));
	}
	
	public void drinkStamina(int replaceItem, int slot, long duration) {
		c.startAnimation(829);
		c.playerItems[slot] = replaceItem + 1;
		c.getItems().resetItems(3214);
		c.staminaDelay = duration;
		int total = c.getRunEnergy() + 20;
		if (total > 100) {
			c.setRunEnergy(100);
			c.getPA().sendFrame126(Integer.toString(100), 149);
		} else {
			c.getPA().sendFrame126(Integer.toString(c.getRunEnergy()), 149);
			c.setRunEnergy(c.getRunEnergy() + 20);
		}
		c.getPA().sendGameTimer(ClientGameTimer.STAMINA, TimeUnit.MILLISECONDS, (int) (duration));
	}

	public void drinkAntiVenom(int replaceItem, int slot, int duration) {
		c.startAnimation(829);
		c.playerItems[slot] = replaceItem + 1;
		c.getItems().resetItems(3214);
		c.getHealth().resolveStatus(HealthStatus.VENOM, duration);
		c.getPA().requestUpdates();
		if (duration > 0) {
			c.getPA().sendGameTimer(ClientGameTimer.ANTIVENOM, TimeUnit.SECONDS, (int) (duration * .6));
		}
	}

	public void drinkAntidote(int itemId, int replaceItem, int slot, int duration) {
		boolean venom = c.getHealth().getStatus().isVenomed();
		c.startAnimation(829);
		c.playerItems[slot] = replaceItem + 1;
		c.getItems().resetItems(3214);
		c.getPA().requestUpdates();

		if (venom) {
			c.getHealth().resolveStatus(HealthStatus.VENOM, duration);
			c.getPA().sendGameTimer(ClientGameTimer.ANTIVENOM, TimeUnit.SECONDS, (int) (duration * .6));
			c.sendMessage("Venom");
		} else {
			c.getHealth().resolveStatus(HealthStatus.POISON, duration);
			c.getPA().sendGameTimer(ClientGameTimer.ANTIPOISON, TimeUnit.SECONDS, (int) (duration * .6));
		}
	}

	public void drinkAntiPoison(int itemId, int replaceItem, int slot, int duration) {
		c.startAnimation(829);
		c.playerItems[slot] = replaceItem + 1;
		c.getItems().resetItems(3214);
		c.getHealth().resolveStatus(HealthStatus.POISON, duration);
		c.getPA().sendGameTimer(ClientGameTimer.ANTIPOISON, TimeUnit.SECONDS, (int) (duration * .6));
		c.getPA().requestUpdates();
	}

	public void curePoison(long duration) {
		c.sendMessage("You have cured yourself of the poison.");
		c.getPA().requestUpdates();
	}

	public void eatChoc(int itemId, int replaceItem, int slot, int stat, boolean sup) {
		if (c.potionTimer.elapsed() > 1200) {
			c.potionTimer.reset();
			c.startAnimation(829);
			c.getItems().deleteItem(9553, slot, 1);
			c.getHealth().increase(10);
			c.sendMessage("The choc. bomb heals you.");
		}
	}

	public void drinkStatPotion(int itemId, int replaceItem, int slot, int stat, boolean sup) {
		c.startAnimation(829);
		c.playerItems[slot] = replaceItem + 1;
		c.getItems().resetItems(3214);
		enchanceStat(stat, sup);
	}

	public void drinkCombatPotion(int itemId, int replaceItem, int slot) {
		c.startAnimation(829);
		c.playerItems[slot] = replaceItem + 1;
		c.getItems().resetItems(3214);
		enchanceStat(0, false);
		enchanceStat(2, false);
	}

	public void drinkStatPotion(int itemId, int replaceItem, int slot, int stat, int amount) {
		c.startAnimation(829);
		c.playerItems[slot] = replaceItem + 1;
		c.getItems().resetItems(3214);
		c.playerLevel[stat] += amount;
		if (c.playerLevel[stat] > c.getLevelForXP(c.playerXP[stat]) + amount) {
			c.playerLevel[stat] = c.getLevelForXP(c.playerXP[stat]) + amount;
		}
		c.getPA().refreshSkill(stat);
	}

	public void drinkPrayerPot(int itemId, int replaceItem, int slot) {
		c.startAnimation(829);
		c.playerItems[slot] = replaceItem + 1;
		c.getItems().resetItems(3214);
		c.playerLevel[5] += (c.getLevelForXP(c.playerXP[5]) * .33);
		if (Boundary.isIn(c, Boundary.DEMONIC_RUINS_BOUNDARY)) {
			c.getDiaryManager().getWildernessDiary().progress(WildernessDiaryEntry.DEMONIC_RUINS);
		}
		if (SkillcapePerks.PRAYER.isWearing(c) || SkillcapePerks.isWearingMaxCape(c))
			c.playerLevel[5] += 5;
		if (c.playerLevel[5] > c.getLevelForXP(c.playerXP[5]))
			c.playerLevel[5] = c.getLevelForXP(c.playerXP[5]);
			c.getPA().refreshSkill(5);
	}

	public void drinkEnergyPots(int itemId, int replaceItem, int slot) {
		c.startAnimation(829);
		int total = c.getRunEnergy() + 10;
		if (total > 100) {
			c.setRunEnergy(100);
			c.getPA().sendFrame126(Integer.toString(100), 149);
		} else {
			c.getPA().sendFrame126(Integer.toString(c.getRunEnergy()), 149);
			c.setRunEnergy(c.getRunEnergy() + 10);
		}
		c.playerItems[slot] = replaceItem + 1;
		c.getItems().resetItems(3214);
	}

	public void drinkSuperEnergyPots(int itemId, int replaceItem, int slot) {
		c.startAnimation(829);
		int total = c.getRunEnergy() + 20;
		if (total > 100) {
			c.setRunEnergy(100);
			c.getPA().sendFrame126(Integer.toString(100), 149);
		} else {
			c.getPA().sendFrame126(Integer.toString(c.getRunEnergy()), 149);
			c.setRunEnergy(c.getRunEnergy() + 20);
		}
		c.playerItems[slot] = replaceItem + 1;
		c.getItems().resetItems(3214);
	}

	public void drinkRestorePot(int itemId, int replaceItem, int slot) {
		c.startAnimation(829);
		c.playerItems[slot] = replaceItem + 1;
		c.getItems().resetItems(3214);
		for (int skill = 0; skill <= 6; skill++) {
			if (skill == 5 || skill == 3)
				continue;
			if (c.playerLevel[skill] < c.getLevelForXP(c.playerXP[skill])) {
				c.playerLevel[skill] += 10 + (c.getLevelForXP(c.playerXP[skill]) * .30);
				if (c.playerLevel[skill] > c.getLevelForXP(c.playerXP[skill])) {
					c.playerLevel[skill] = c.getLevelForXP(c.playerXP[skill]);
				}
				if (Boundary.isIn(c, Boundary.DEMONIC_RUINS_BOUNDARY)) {
					c.getDiaryManager().getWildernessDiary().progress(WildernessDiaryEntry.DEMONIC_RUINS);
				}
				c.getPA().refreshSkill(skill);
				c.getPA().setSkillLevel(skill, c.playerLevel[skill], c.playerXP[skill]);
			}
		}
	}

	public void drinkSuperRestorePot(int itemId, int replaceItem, int slot) {
		c.startAnimation(829);
		c.playerItems[slot] = replaceItem + 1;
		c.getItems().resetItems(3214);
		for (int skill = 0; skill < c.playerLevel.length; skill++) {
			if (skill == 3)
				continue;
			if (c.playerLevel[skill] < c.getLevelForXP(c.playerXP[skill])) {
				c.playerLevel[skill] += 8 + (c.getLevelForXP(c.playerXP[skill]) * .25);
				if (SkillcapePerks.PRAYER.isWearing(c) || SkillcapePerks.isWearingMaxCape(c))
					c.playerLevel[skill] += 5;
				if (c.playerLevel[skill] > c.getLevelForXP(c.playerXP[skill])) {
					c.playerLevel[skill] = c.getLevelForXP(c.playerXP[skill]);
				}
				if (Boundary.isIn(c, Boundary.DEMONIC_RUINS_BOUNDARY)) {
					c.getDiaryManager().getWildernessDiary().progress(WildernessDiaryEntry.DEMONIC_RUINS);
				}
				c.getPA().refreshSkill(skill);
				c.getPA().setSkillLevel(skill, c.playerLevel[skill], c.playerXP[skill]);
			}
		}
	}

	public void drinkMagicPotion(int itemId, int replaceItem, int slot, int stat, boolean sup) {
		c.startAnimation(829);
		c.playerItems[slot] = replaceItem + 1;
		c.getItems().resetItems(3214);
		enchanceMagic(stat, sup);

	}

	public void enchanceMagic(int skillID, boolean sup) {
		c.playerLevel[skillID] += getBoostedMagic(skillID, sup);
		c.getPA().refreshSkill(skillID);
	}

	public int getBoostedMagic(int skill, boolean sup) {
		int increaseBy = 0;
		if (sup)
			increaseBy = (int) (c.getLevelForXP(c.playerXP[skill]) * .06);
		else
			increaseBy = (int) (c.getLevelForXP(c.playerXP[skill]) * .06);
		if (c.playerLevel[skill] + increaseBy > c.getLevelForXP(c.playerXP[skill]) + increaseBy + 1) {
			return c.getLevelForXP(c.playerXP[skill]) + increaseBy - c.playerLevel[skill];
		}
		return increaseBy;
	}

	public void drinkSaradominBrew(int itemId, int replaceItem, int slot) {
		if (c.getHealth().getAmount() <= 0 || c.isDead) {
			return;
		}
		DuelSession session = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c, MultiplayerSessionType.DUEL);
		if (Objects.nonNull(session)) {
			if (session.getRules().contains(Rule.NO_FOOD)) {
				c.sendMessage("The saradomin brew has been disabled because of its healing effect.");
				return;
			}
		}
		if (System.currentTimeMillis() - c.foodDelay >= 1800 && !c.getFood().isFood(c.lastClickedItem)) {
			c.foodDelay = System.currentTimeMillis();
		}
		c.startAnimation(829);
		c.playerItems[slot] = replaceItem + 1;
		c.getItems().resetItems(3214);
		int[] toDecrease = { 0, 2, 4, 6 };

		for (int tD : toDecrease) {
			c.playerLevel[tD] -= getBrewStat(tD, .10);
			if (c.playerLevel[tD] < 0)
				c.playerLevel[tD] = 1;
			c.getPA().refreshSkill(tD);
			c.getPA().setSkillLevel(tD, c.playerLevel[tD], c.playerXP[tD]);
		}
		c.playerLevel[1] += getBrewStat(1, .20) + 2;
		if (c.playerLevel[1] > (c.getLevelForXP(c.playerXP[1]) * 1.2 + 2)) {
			c.playerLevel[1] = (int) (c.getLevelForXP(c.playerXP[1]) * 1.2 + 2);
		}
		c.getPA().refreshSkill(1);

		int offset = getBrewStat(3, .15) + 2;
		int maximum = c.getHealth().getMaximum() + offset;
		if (c.getHealth().getAmount() + offset >= maximum) {
			c.getHealth().setAmount(maximum);
		} else {
			c.getHealth().setAmount(c.getHealth().getAmount() + offset);
		}
	}

	public void drinkZamorakBrew(int itemId, int replaceItem, int slot) {
		if (c.getHealth().getAmount() <= 0 || c.isDead) {
			return;
		}
		c.startAnimation(829);
		c.playerItems[slot] = replaceItem + 1;
		c.getItems().resetItems(3214);

		c.playerLevel[1] -= getBrewStat(1, .10) + 2;
		if (c.playerLevel[1] < 0)
			c.playerLevel[1] = 1;
		c.getPA().refreshSkill(1);
		c.getPA().setSkillLevel(1, c.playerLevel[1], c.playerXP[1]);

		int damage = (getBrewStat(1, .10) + 2);
		c.appendDamage(damage, damage > 0 ? Hitmark.HIT : Hitmark.MISS);

		c.playerLevel[0] += getBrewStat(0, .20) + 2;
		if (c.playerLevel[0] > (c.getLevelForXP(c.playerXP[0]) * 1.2 + 2)) {
			c.playerLevel[0] = (int) (c.getLevelForXP(c.playerXP[0]) * 1.2 + 2);
		}
		c.getPA().refreshSkill(0);

		c.playerLevel[2] += getBrewStat(2, .12) + 2;
		if (c.playerLevel[2] > (c.getLevelForXP(c.playerXP[2]) * 1.12 + 2)) {
			c.playerLevel[2] = (int) (c.getLevelForXP(c.playerXP[2]) * 1.12 + 2);
		}
		c.getPA().refreshSkill(2);
	}

	public void enchanceStat(int skillID, boolean sup) {
		c.playerLevel[skillID] += getBoostedStat(skillID, sup);
		c.getPA().refreshSkill(skillID);
	}

	public int getBrewStat(int skill, double amount) {
		return (int) (c.getLevelForXP(c.playerXP[skill]) * amount);
	}

	public int getBoostedStat(int skill, boolean sup) {
		int increaseBy = 0;
		if (sup)
			increaseBy = (int) (c.getLevelForXP(c.playerXP[skill]) * .20);
		else
			increaseBy = (int) (c.getLevelForXP(c.playerXP[skill]) * .13) + 1;
		if (c.playerLevel[skill] + increaseBy > c.getLevelForXP(c.playerXP[skill]) + increaseBy + 1) {
			return c.getLevelForXP(c.playerXP[skill]) + increaseBy - c.playerLevel[skill];
		}
		return increaseBy;
	}

	public void doOverload(int itemId, int replaceItem, int slot) {
		int health = c.getHealth().getAmount();
		if (health <= 50) {
			c.sendMessage("I should get some more lifepoints before using this!");
			return;
		}
		c.getPA().sendGameTimer(ClientGameTimer.OVERLOAD, TimeUnit.MINUTES, 5);
		c.hasOverloadBoost = false;
		c.startAnimation(829);
		c.playerItems[slot] = replaceItem + 1;
		c.getItems().resetItems(3214);
		c.hasOverloadBoost = true;
		createOverloadDamageEvent();
		doOverloadBoost();
		handleOverloadTimers();
		c.getPA().refreshSkill(0);
		c.getPA().refreshSkill(1);
		c.getPA().refreshSkill(2);
		c.getPA().refreshSkill(3);
		c.getPA().refreshSkill(4);
		c.getPA().refreshSkill(6);
	}

	private void createOverloadDamageEvent() {
		CycleEventHandler.getSingleton().stopEvents(c, CycleEventHandler.Event.OVERLOAD_HITMARK_ID);
		CycleEventHandler.getSingleton().addEvent(CycleEventHandler.Event.OVERLOAD_HITMARK_ID, c, new CycleEvent() {
			int time = 5;

			@Override
			public void execute(CycleEventContainer b) {
				if (c == null) {
					b.stop();
					return;
				}
				if (time <= 0) {
					b.stop();
					return;
				}
				if (time > 0) {
					if (c.getHealth().getAmount() <= 10) {
						b.stop();
						return;
					}
					time--;
					c.startAnimation(3170);
					c.appendDamage(10, Hitmark.HIT);
				}
			}

			@Override
			public void stop() {

			}
		}, 1);
	}

	public void resetOverload() {
		if (!c.hasOverloadBoost)
			return;
		c.hasOverloadBoost = false;
		int[] toNormalise = { 0, 1, 2, 4, 6 };
		for (int i = 0; i < toNormalise.length; i++) {
			c.playerLevel[toNormalise[i]] = c.getLevelForXP(c.playerXP[toNormalise[i]]);
			c.getPA().refreshSkill(toNormalise[i]);
		}
		c.sendMessage("The effects of the potion have worn off...");
	}

	public void handleOverloadTimers() {
		CycleEventHandler.getSingleton().stopEvents(c, CycleEventHandler.Event.OVERLOAD_BOOST_ID);
		CycleEventHandler.getSingleton().addEvent(CycleEventHandler.Event.OVERLOAD_BOOST_ID, c, new CycleEvent() {

			@Override
			public void execute(CycleEventContainer b) {
				if (c == null) {
					b.stop();
					return;
				}
				resetOverload();
			}

			@Override
			public void stop() {

			}
		}, 500); // 5 minutes
	}

	public void doOverloadBoost() {
		int[] toIncrease = { 0, 1, 2, 4, 6 };
		int boost;
		for (int i = 0; i < toIncrease.length; i++) {
			boost = (int) (getOverloadBoost(toIncrease[i]));
			c.playerLevel[toIncrease[i]] += boost;
			if (c.playerLevel[toIncrease[i]] > (c.getLevelForXP(c.playerXP[toIncrease[i]]) + boost))
				c.playerLevel[toIncrease[i]] = (c.getLevelForXP(c.playerXP[toIncrease[i]]) + boost);
			c.getPA().refreshSkill(toIncrease[i]);
		}
	}

	public double getOverloadBoost(int skill) {
		double boost = 1;
		switch (skill) {
		case 0:
		case 1:
		case 2:
			boost = 5 + (c.getLevelForXP(c.playerXP[skill]) * .22);
			break;
		case 4:
			boost = 3 + (c.getLevelForXP(c.playerXP[skill]) * .22);
			break;
		case 6:
			boost = 7;
			break;
		}
		return boost;
	}

	public boolean isPotion(int itemId) {
		if (c.getItems().isNoted(itemId)) {
			return false;
		}
		String name = ItemAssistant.getItemName(itemId);
		return name.contains("(4)") || name.contains("(3)") || name.contains("(2)") || name.contains("(1)") || name.contains("Antidote");
	}

}