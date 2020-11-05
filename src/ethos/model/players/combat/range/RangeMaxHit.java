package ethos.model.players.combat.range;

import java.util.stream.IntStream;

import ethos.Config;
import ethos.model.items.Item;
import ethos.model.npcs.NPC;
import ethos.model.npcs.NPCHandler;
import ethos.model.players.Player;
import ethos.util.Misc;

public class RangeMaxHit extends RangeData {

	public static int calculateRangeDefence(Player c) {
		int defenceLevel = c.playerLevel[1];
		if (c.prayerActive[0]) {
			defenceLevel += c.getLevelForXP(c.playerXP[c.playerDefence]) * 0.05;
		} else if (c.prayerActive[5]) {
			defenceLevel += c.getLevelForXP(c.playerXP[c.playerDefence]) * 0.1;
		} else if (c.prayerActive[13]) {
			defenceLevel += c.getLevelForXP(c.playerXP[c.playerDefence]) * 0.15;
		} else if (c.prayerActive[26]) {
			defenceLevel += c.getLevelForXP(c.playerXP[c.playerDefence]) * 0.2;
		} else if (c.prayerActive[27]) {
			defenceLevel += c.getLevelForXP(c.playerXP[c.playerDefence]) * 0.25;
		} else if (c.prayerActive[28]) {
			defenceLevel += c.getLevelForXP(c.playerXP[c.playerDefence]) * 0.25;
		}
		return defenceLevel + c.playerBonus[9] + (c.playerBonus[9] / 2);
	}

	public static int calculateRangeAttack(Player c) {
		int rangeLevel = c.playerLevel[4];
		if (c.playerIndex > 0) {
			rangeLevel *= c.specAccuracy;
		}
		if (c.npcIndex > 0 && c.getItems().isWearingItem(20997)) {
			rangeLevel += 1.35;
		}
		if (c.fullVoidRange()) {
			rangeLevel += c.getLevelForXP(c.playerXP[c.playerRanged]) * 0.1;
		}
		if (c.prayerActive[3]) {
			rangeLevel *= 1.05;
		} else if (c.prayerActive[11]) {
			rangeLevel *= 1.10;
		} else if (c.prayerActive[19]) {
			rangeLevel *= 1.15;
		} else if (c.prayerActive[27]) {
			rangeLevel *= 1.23;
		}
		if (c.npcIndex > 0 && c.getSlayer().getTask().isPresent()) {
			NPC npc = NPCHandler.npcs[c.npcIndex];
			if (npc != null && c.getSlayer().getTask().get().matches(npc.getDefinition().getNpcName()) || npc.npcType == 7413) {
				boolean SLAYER_HELM = IntStream.of(c.IMBUED_SLAYER_HELMETS).anyMatch(i -> c.getItems().isWearingItem(i));
				if (!c.getItems().isWearingItem(4081) && SLAYER_HELM) {
					rangeLevel *= 1.15;
				}
			}
			if (c.getItems().isWearingItem(4081, c.playerAmulet)) {
				if (Misc.linearSearch(Config.UNDEAD_IDS, npc.npcType) != -1) {
					rangeLevel *= 1.15;
				}
			}

		}

		if (c.fullVoidRange() && c.specAccuracy > 1.15) {
			rangeLevel *= 1.75;
		}
		return (int) (rangeLevel + (c.playerBonus[4] * 1.95));
	}
	
	public static boolean wearingCrystalBow(Player c) {
		return c.playerEquipment[3] != -1 && Item.getItemName(c.playerEquipment[3]).toLowerCase().contains("crystal bow");
	}

	public static int maxHit(Player c) {
		int rangeLevel = c.playerLevel[4];
		int rangedStrength = 
			/**
			 * If a player is NOT using a blowpipe or crystal bow
			 * But is using a bow or ballista, we grab the strength from the arrow slot
			 */
			c.playerEquipment[c.playerWeapon] != 12926 && !wearingCrystalBow(c) && c.usingBow || c.usingBallista ? getRangeStr(c.playerEquipment[c.playerArrows]) :
			
			/**
			 * If a player IS using a blowpipe
			 * We grab the strength from the ammo which is saved on players accounts
			 */
			c.playerEquipment[c.playerWeapon] == 12926 ? getRangeStr(c.getToxicBlowpipeAmmo()) : 
				
			/**
			 * If a player IS using a crystal bow
			 * We grab the strength from the weapon slot
			 */
			wearingCrystalBow(c) ? getRangeStr(c.playerEquipment[c.playerWeapon]) :
				
			/**
			 * If none of the above is applicable
			 * We grab the strength from the weapon slot and divide it by 2
			 */
			getRangeStr(c.playerEquipment[c.playerWeapon]) / 3;
			
		double b = 1.00;
		if (c.prayerActive[3]) {
			b *= 1.05;
		} else if (c.prayerActive[11]) {
			b *= 1.10;
		} else if (c.prayerActive[19]) {
			b *= 1.15;
		} else if (c.prayerActive[27]) {
			b *= 1.23;
		}
		if (c.fullVoidRange()) {
			b *= 1.20;
		}
		if (c.npcIndex > 0 && c.getItems().isWearingItem(20997)) {
			b*= 3.00;
		}
		if (c.npcIndex > 0 && c.getSlayer().getTask().isPresent()) {
			NPC npc = NPCHandler.npcs[c.npcIndex];
			if (npc != null && c.getSlayer().getTask().get().matches(npc.getDefinition().getNpcName()) || npc.npcType == 7413) {
				boolean SLAYER_HELM = IntStream.of(c.IMBUED_SLAYER_HELMETS).anyMatch(i -> c.getItems().isWearingItem(i));
				if (SLAYER_HELM) {
					b *= 1.15;
				}
			}
		}
		double e = Math.floor(rangeLevel * b);
		if (c.fightMode == 0) {
			e = (e + 3.0);
		}
		double darkbow = 1.0;
		if (c.usingSpecial) {
			if (c.playerEquipment[3] == 11235 || c.playerEquipment[3] == 12765 || c.playerEquipment[3] == 12766 || c.playerEquipment[3] == 12767 || c.playerEquipment[3] == 12768) {
				if (Arrow.matchesMaterial(c.lastArrowUsed, Arrow.DRAGON)) {
					darkbow = 1.05;
				} else {
					darkbow = 1.01;
				}
			}
		}
		double max = (1.3 + e / 10 + rangedStrength / 80 + e * rangedStrength / 640) * darkbow;
		if (c.usingSpecial) {
			max *= c.specDamage;
		}
		return (int) max;
	}
}