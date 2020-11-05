package ethos.model.players.combat.melee;

import java.util.stream.IntStream;

import ethos.Config;
import ethos.model.items.EquipmentSet;
import ethos.model.npcs.NPC;
import ethos.model.npcs.NPCHandler;
import ethos.model.players.Player;
import ethos.util.Misc;

public class MeleeMaxHit {

	/**
	 * @param c
	 * @param special
	 * @return
	 */
	@SuppressWarnings("unused")
	public static double calculateBaseDamage(Player c, boolean special) {
		double base = 0;
		int attBonus = c.playerBonus[10]; // attack
		int attack = c.playerLevel[0]; // attack
		int strength = c.playerLevel[2]; // strength
		int defBonus = c.playerBonus[10]; // defense
		int defense = c.playerLevel[1]; // defense
		int attlvlForXP = c.getLevelForXP(c.playerXP[0]); // attack
		int strlvlForXP = c.getLevelForXP(c.playerXP[2]); // strength
		int deflvlForXP = c.getLevelForXP(c.playerXP[1]); // defense
		int lvlForXP = c.getLevelForXP(c.playerXP[2]);
		double effective = getEffectiveStr(c);
		double specialBonus = getSpecialStr(c);
		double strengthBonus = c.playerBonus[10];
		base = (13 + effective + (c.getCombat().strBonus / 8) + ((effective * c.getCombat().strBonus) / 64)) / 10;
		if (c.npcIndex > 0) {
			NPC npc = NPCHandler.npcs[c.npcIndex];
			if (npc != null) {
				if (c.getSlayer().getTask().isPresent()) {
					if (npc != null && c.getSlayer().getTask().get().matches(npc.getDefinition().getNpcName()) || npc.npcType == 7413) {
						boolean SLAYER_HELM = IntStream.of(c.SLAYER_HELMETS).anyMatch(i -> c.getItems().isWearingItem(i));
						if (!c.getItems().isWearingItem(4081) && SLAYER_HELM || c.getItems().isWearingItem(8901)) {
							base *= 1.15;
						}
					}
				}
				if (c.getItems().isWearingItem(4081, c.playerAmulet)) {
					if (Misc.linearSearch(Config.UNDEAD_IDS, npc.npcType) != -1) {
						base *= 1.15;
					}
				}
				if (c.getItems().isWearingItem(19675, c.playerWeapon) && c.getArcLightCharge() > 0) {
					if (Misc.linearSearch(Config.DEMON_IDS, npc.npcType) != -1) {
						base *= 1.70;
					}
				}
			}
		}

		if (EquipmentSet.DHAROK.isWearingBarrows(c)) {
			base *= ((c.getLevelForXP(c.playerXP[3]) - c.getHealth().getAmount()) * .01) + 1;
		}
		if (hasObsidianEffect(c) || c.fullVoidMelee()) {
			base = (base * 1.10);
		}
		if (c.prayerActive[1]) {
			strength += (int) (lvlForXP * .05);
		} else if (c.prayerActive[6]) {
			strength += (int) (lvlForXP * .1);
		} else if (c.prayerActive[14]) {
			strength += (int) (lvlForXP * .15);
		} else if (c.prayerActive[25]) {
			strength += (int) (lvlForXP * .18);
		} else if (c.prayerActive[26]) {
			strength += (int) (lvlForXP * .23);
		}
		return Math.floor(base);
	}

	public static double getEffectiveStr(Player c) {
		return ((c.playerLevel[2]) * getPrayerStr(c)) + getStyleBonus(c);
	}

	public static int getStyleBonus(Player c) {
		return c.fightMode == 2 ? 3 : c.fightMode == 3 ? 1 : c.fightMode == 4 ? 3 : 0;
	}

	public static double getPrayerStr(Player c) {
		if (c.prayerActive[1])
			return 1.05;
		else if (c.prayerActive[6])
			return 1.1;
		else if (c.prayerActive[14])
			return 1.15;
		else if (c.prayerActive[25])
			return 1.18;
		else if (c.prayerActive[26])
			return 1.23;
		return 1;
	}

	public static final double[][] special = { { 5698, 1.05 }, { 5680, 1.05 }, { 1231, 1.05 }, { 1215, 1.05 }, { 3204, 0.90 }, { 1305, 1.15 }, { 1434, 1.45 }, { 11802, 1.375 },
			{ 11804, 1.21 }, { 11806, 1.10 }, { 11808, 1.10 }, { 861, 1.1 }, { 4151, 1.1 }, { 10887, 1.2933 }, { 12926, 1.35 } };

	public static double getSpecialStr(Player c) {
		for (double[] slot : special) {
			if (c.playerEquipment[3] == slot[0])
				return slot[1];
		}
		return 1;
	}

	public static final int[] obsidianWeapons = { 746, 747, 6523, 6525, 6526, 6527, 6528 };

	public static boolean hasObsidianEffect(Player c) {
		if (c.playerEquipment[2] != 11128)
			return false;

		for (int weapon : obsidianWeapons) {
			if (c.playerEquipment[3] == weapon)
				return true;
		}
		return false;
	}

	public static boolean hasVoid(Player c) {
		return c.playerEquipment[c.playerHat] == 11665 && c.playerEquipment[c.playerLegs] == 8840 || c.playerEquipment[c.playerLegs] == 13073 && c.playerEquipment[c.playerChest] == 8839 || c.playerEquipment[c.playerChest] == 13072 && c.playerEquipment[c.playerHands] == 8842;
	}

	public static int bestMeleeDef(Player c) {
		if (c.playerBonus[5] > c.playerBonus[6] && c.playerBonus[5] > c.playerBonus[7]) {
			return 5;
		}
		if (c.playerBonus[6] > c.playerBonus[5] && c.playerBonus[6] > c.playerBonus[7]) {
			return 6;
		}
		return c.playerBonus[7] <= c.playerBonus[5] || c.playerBonus[7] <= c.playerBonus[6] ? 5 : 7;
	}

	public static int calculateMeleeDefence(Player c) {
		int defenceLevel = c.playerLevel[1];
		int i = c.playerBonus[bestMeleeDef(c)];
		if (c.prayerActive[0]) {
			defenceLevel += c.getLevelForXP(c.playerXP[c.playerDefence]) * 0.05;
		} else if (c.prayerActive[5]) {
			defenceLevel += c.getLevelForXP(c.playerXP[c.playerDefence]) * 0.1;
		} else if (c.prayerActive[13]) {
			defenceLevel += c.getLevelForXP(c.playerXP[c.playerDefence]) * 0.15;
		} else if (c.prayerActive[25]) {
			defenceLevel += c.getLevelForXP(c.playerXP[c.playerDefence]) * 0.2;
		} else if (c.prayerActive[26]) {
			defenceLevel += c.getLevelForXP(c.playerXP[c.playerDefence]) * 0.25;
		}
		return (int) (defenceLevel + (defenceLevel * 0.12) + (i + i * 0.03));// 4
	}

	public static int bestMeleeAtk(Player c) {
		if (c.playerBonus[0] > c.playerBonus[1] && c.playerBonus[0] > c.playerBonus[2]) {
			return 0;
		}
		if (c.playerBonus[1] > c.playerBonus[0] && c.playerBonus[1] > c.playerBonus[2]) {
			return 1;
		}
		return c.playerBonus[2] <= c.playerBonus[1] || c.playerBonus[2] <= c.playerBonus[0] ? 0 : 2;
	}

	public static int calculateMeleeAttack(Player c) {
		int attackLevel = c.playerLevel[0];
		if (c.prayerActive[2]) {
			attackLevel += c.getLevelForXP(c.playerXP[c.playerAttack]) * 0.05;
		} else if (c.prayerActive[7]) {
			attackLevel += c.getLevelForXP(c.playerXP[c.playerAttack]) * 0.1;
		} else if (c.prayerActive[15]) {
			attackLevel += c.getLevelForXP(c.playerXP[c.playerAttack]) * 0.15;
		} else if (c.prayerActive[25]) {
			attackLevel += c.getLevelForXP(c.playerXP[c.playerAttack]) * 0.15;
		} else if (c.prayerActive[26]) {
			attackLevel += c.getLevelForXP(c.playerXP[c.playerAttack]) * 0.2;
		}
		if (c.playerEquipment[c.playerWeapon] == 4153 || c.playerEquipment[c.playerWeapon] == 12848) {
			attackLevel -= c.getLevelForXP(c.playerXP[c.playerAttack]) * 0.15;
		}
		if (c.playerEquipment[c.playerWeapon] == 4718 && c.playerEquipment[c.playerHat] == 4716 && c.playerEquipment[c.playerChest] == 4720
				&& c.playerEquipment[c.playerLegs] == 4722) {
			attackLevel -= c.getLevelForXP(c.playerXP[c.playerAttack]) * 0.15;
		}
		if (c.fullVoidMelee()) {
			attackLevel += c.getLevelForXP(c.playerXP[c.playerAttack]) * 0.10;
		}
		if (c.debugMessage)
			c.sendMessage("Accuracy from whip: "+ attackLevel +"");
		if (c.getItems().isWearingItem(19675, c.playerWeapon) && c.getArcLightCharge() > 0) {
			if (c.debugMessage)
					c.sendMessage("Accuracy on reg: "+ attackLevel +"");
			NPC npc = NPCHandler.npcs[c.npcIndex];
			if (Misc.linearSearch(Config.DEMON_IDS, npc.npcType) != -1) {
				attackLevel += c.getLevelForXP(c.playerXP[c.playerAttack]) * 0.80;
				if (c.debugMessage)
					c.sendMessage("Accuracy on demon: "+ attackLevel +"");
			}
		}
		if (c.npcIndex > 0 && c.getSlayer().getTask().isPresent()) {
			NPC npc = NPCHandler.npcs[c.npcIndex];
			if (npc != null && c.getSlayer().getTask().get().matches(npc.getDefinition().getNpcName()) || npc.npcType == 7413) {
				boolean SLAYER_HELM = IntStream.of(c.SLAYER_HELMETS).anyMatch(i -> c.getItems().isWearingItem(i));
				if (!c.getItems().isWearingItem(4081) && SLAYER_HELM || c.getItems().isWearingItem(8901)) {
					attackLevel += c.getLevelForXP(c.playerXP[c.playerAttack]) * 0.15;
				}
			}
			if (c.getItems().isWearingItem(4081, c.playerAmulet)) {
				if (Misc.linearSearch(Config.UNDEAD_IDS, npc.npcType) != -1) {
					attackLevel += c.getLevelForXP(c.playerXP[c.playerAttack]) * 0.15;
				}
			}
		}
		attackLevel *= c.specAccuracy;
		int i = c.playerBonus[bestMeleeAtk(c)];
		i += c.bonusAttack;
		if (hasObsidianEffect(c) || c.fullVoidMelee()) {
			i *= 1.20;
		}
		return (int) (attackLevel + (attackLevel * 0.20) + (i + i * 0.10));
	}
}