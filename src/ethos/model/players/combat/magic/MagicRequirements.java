package ethos.model.players.combat.magic;

import org.apache.commons.lang3.RandomUtils;

import ethos.Config;
import ethos.model.items.ItemAssistant;
import ethos.model.players.Boundary;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;

public class MagicRequirements extends MagicConfig {

	public static boolean hasRunes(Player c, int[] runes, int[] amount) {
		if (c.getRunePouch().hasRunes(runes, amount)) {
			return true;
		}
		for (int i = 0; i < runes.length; i++) {
			if (c.getItems().playerHasItem(runes[i], amount[i])) {
				return true;
			}
		}
		c.sendMessage("You don't have enough required runes to cast this spell!");
		return false;
	}

	public static void deleteRunes(Player c, int[] runes, int[] amount) {
		if (c.getRunePouch().hasRunes(runes, amount)) {
			c.getRunePouch().deleteRunesOnCast(runes, amount);
			return;
		}
		for (int i = 0; i < runes.length; i++) {
			c.getItems().deleteItem(runes[i], c.getItems().getItemSlot(runes[i]), amount[i]);
		}
	}

	public static boolean hasRequiredLevel(Player c, int i) {
		return c.playerLevel[6] >= i;
	}

	public static boolean wearingStaff(Player c, int runeId) {
		int wep = c.playerEquipment[c.playerWeapon];
		switch (runeId) {
		case FIRE:
			if (wep == 1387 || wep == 1393 || wep == 1401 || wep == 12796 || wep == 11789 || wep == 12000 || wep == 11998 || wep == 12795)
				return true;
			break;
		case WATER:
			if (wep == 1383 || wep == 1395 || wep == 12796 || wep == 11789 || wep == 6563 || wep == 1403 || wep == 21006 || wep == 20730  || wep == 12795)
				return true;
			break;
		case AIR:
			if (wep == 1381 || wep == 1397 || wep == 1405 || wep == 12000 || wep == 20736 || wep == 20736 || wep == 20730)
				return true;
			break;
			
		case EARTH:
			if (wep == 1385 || wep == 1399 || wep == 1407 || wep == 6563 || wep == 20736 | wep == 20736  || wep == 11998)
				return true;
			break;
		}
		return false;
	}

	public static boolean checkMagicReqs(Player c, int spell) {
		//System.out.println("Spell: " + spell + ", Spellid: " + c.spellId + ", Old Spellid: " + c.oldSpellId);
		if (c.usingMagic) {
			if (!Boundary.isIn(c, Boundary.FOUNTAIN_OF_RUNE_BOUNDARY)) {
				boolean hasRunesInPouch1 = c.getRunePouch().hasRunes(MagicData.MAGIC_SPELLS[spell][8], MagicData.MAGIC_SPELLS[spell][9]);
				boolean hasRunesInPouch2 = c.getRunePouch().hasRunes(MagicData.MAGIC_SPELLS[spell][10], MagicData.MAGIC_SPELLS[spell][11]);
				boolean hasRunesInPouch3 = c.getRunePouch().hasRunes(MagicData.MAGIC_SPELLS[spell][12], MagicData.MAGIC_SPELLS[spell][13]);
				boolean hasRunesInPouch4 = c.getRunePouch().hasRunes(MagicData.MAGIC_SPELLS[spell][14], MagicData.MAGIC_SPELLS[spell][15]);

				boolean hasRunesInInventory1 = c.getItems().playerHasItem(MagicData.MAGIC_SPELLS[spell][8], MagicData.MAGIC_SPELLS[spell][9]);
				boolean hasRunesInInventory2 = c.getItems().playerHasItem(MagicData.MAGIC_SPELLS[spell][10], MagicData.MAGIC_SPELLS[spell][11]);
				boolean hasRunesInInventory3 = c.getItems().playerHasItem(MagicData.MAGIC_SPELLS[spell][12], MagicData.MAGIC_SPELLS[spell][13]);
				boolean hasRunesInInventory4 = c.getItems().playerHasItem(MagicData.MAGIC_SPELLS[spell][14], MagicData.MAGIC_SPELLS[spell][15]);

				boolean hasStaff1 = wearingStaff(c, MagicData.MAGIC_SPELLS[spell][8]);
				boolean hasStaff2 = wearingStaff(c, MagicData.MAGIC_SPELLS[spell][10]);
				boolean hasStaff3 = wearingStaff(c, MagicData.MAGIC_SPELLS[spell][12]);
				boolean hasStaff4 = wearingStaff(c, MagicData.MAGIC_SPELLS[spell][14]);

				if (!(hasRunesInPouch1 || hasRunesInInventory1 || hasStaff1)) {
					c.sendMessage("You don't have the required runes to cast this spell.");
					return false;
				}

				if (!(hasRunesInPouch2 || hasRunesInInventory2 || hasStaff2)) {
					c.sendMessage("You don't have the required runes to cast this spell.");
					return false;
				}

				if (!(hasRunesInPouch3 || hasRunesInInventory3 || hasStaff3)) {
					c.sendMessage("You don't have the required runes to cast this spell.");
					return false;
				}

				if (!(hasRunesInPouch4 || hasRunesInInventory4 || hasStaff4)) {
					c.sendMessage("You don't have the required runes to cast this spell.");
					return false;
				}
			}
		}

		if (c.usingMagic && c.playerIndex > 0) {
			if (PlayerHandler.players[c.playerIndex] != null) {
				for (int r = 0; r < c.REDUCE_SPELLS.length; r++) { //Reducing/weaken spells
					if (PlayerHandler.players[c.playerIndex].REDUCE_SPELLS[r] == MagicData.MAGIC_SPELLS[spell][0]) {
						c.reduceSpellId = r;
						if ((System.currentTimeMillis() - PlayerHandler.players[c.playerIndex].reduceSpellDelay[c.reduceSpellId]) > PlayerHandler.players[c.playerIndex].REDUCE_SPELL_TIME[c.reduceSpellId]) {
							PlayerHandler.players[c.playerIndex].canUseReducingSpell[c.reduceSpellId] = true;
						} else {
							PlayerHandler.players[c.playerIndex].canUseReducingSpell[c.reduceSpellId] = false;
						}
						break;
					}
				}
				if (!PlayerHandler.players[c.playerIndex].canUseReducingSpell[c.reduceSpellId]) {
					c.sendMessage("That player is currently immune to this spell.");
					c.usingMagic = false;
					c.stopMovement();
					c.getCombat().resetPlayerAttack();
					return false;
				}
			}
		}

		int staffRequired = getStaffNeeded(c);
		if (c.usingMagic && staffRequired > 0 && Config.RUNES_REQUIRED) { // staff required
			if (c.playerEquipment[c.playerWeapon] != staffRequired) {
				c.sendMessage("You need a " + ItemAssistant.getItemName(staffRequired).toLowerCase() + " to cast this spell.");
				return false;
			}
		}

		if (c.usingMagic && Config.MAGIC_LEVEL_REQUIRED) { // check magic level
			if (c.playerLevel[6] < MagicData.MAGIC_SPELLS[spell][1]) {
				c.sendMessage("You need to have a magic level of " + MagicData.MAGIC_SPELLS[spell][1] + " to cast this spell.");
				return false;
			}
		}
		boolean runesNecessary = true;
		if ((c.playerEquipment[c.playerWeapon] == 11791 || c.playerEquipment[c.playerWeapon] == 12904) && RandomUtils.nextInt(0, 100) < 13) {
			runesNecessary = false;
		}

		if (!Boundary.isIn(c, Boundary.FOUNTAIN_OF_RUNE_BOUNDARY)) {
			if (c.usingMagic && Config.RUNES_REQUIRED && runesNecessary) {
				if (MagicData.MAGIC_SPELLS[spell][8] > 0) { // deleting runes
					if (!wearingStaff(c, MagicData.MAGIC_SPELLS[spell][8])) {
						c.getRunePouch().deleteRunesOnCast(MagicData.MAGIC_SPELLS[spell][8], MagicData.MAGIC_SPELLS[spell][9]);
						c.getItems().deleteItem(MagicData.MAGIC_SPELLS[spell][8], MagicData.MAGIC_SPELLS[spell][9]);
					}
				}
				if (MagicData.MAGIC_SPELLS[spell][10] > 0) {
					if (!wearingStaff(c, MagicData.MAGIC_SPELLS[spell][10])) {
						c.getRunePouch().deleteRunesOnCast(MagicData.MAGIC_SPELLS[spell][10], MagicData.MAGIC_SPELLS[spell][11]);
						c.getItems().deleteItem(MagicData.MAGIC_SPELLS[spell][10], MagicData.MAGIC_SPELLS[spell][11]);
					}
				}

				if (MagicData.MAGIC_SPELLS[spell][12] > 0) {
					if (!wearingStaff(c, MagicData.MAGIC_SPELLS[spell][12])) {
						c.getRunePouch().deleteRunesOnCast(MagicData.MAGIC_SPELLS[spell][12], MagicData.MAGIC_SPELLS[spell][13]);
						c.getItems().deleteItem(MagicData.MAGIC_SPELLS[spell][12], MagicData.MAGIC_SPELLS[spell][13]);

					}
				}
				if (MagicData.MAGIC_SPELLS[spell][14] > 0) {
					if (!wearingStaff(c, MagicData.MAGIC_SPELLS[spell][14])) {
						c.getRunePouch().deleteRunesOnCast(MagicData.MAGIC_SPELLS[spell][14], MagicData.MAGIC_SPELLS[spell][15]);
						c.getItems().deleteItem(MagicData.MAGIC_SPELLS[spell][14], MagicData.MAGIC_SPELLS[spell][15]);

					}
				}
			}
		}
		return true;
	}

	public static final int FIRE = 554;
	public static final int WATER = 555;
	public static final int AIR = 556;
	public static final int EARTH = 557;
	public static final int MIND = 558;
	public static final int BODY = 559;
	public static final int DEATH = 560;
	public static final int NATURE = 561;
	public static final int CHAOS = 562;
	public static final int LAW = 563;
	public static final int COSMIC = 564;
	public static final int BLOOD = 565;
	public static final int SOUL = 566;
	public static final int ASTRAL = 9075;
}