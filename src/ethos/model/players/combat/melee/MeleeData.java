package ethos.model.players.combat.melee;

import ethos.model.items.ItemAssistant;
import ethos.model.npcs.animations.BlockAnimation;
import ethos.model.players.Player;
import ethos.model.players.combat.magic.MagicData;

public class MeleeData {

	public static void resetPlayerAttack(Player c) {
		c.usingMagic = false;
		c.npcIndex = 0;
		c.faceUpdate(0);
		c.playerIndex = 0;
		c.getPA().resetFollow();
		return;
	}

	public static boolean usingHally(Player c) {
		switch (c.playerEquipment[c.playerWeapon]) {
		case 3190:
		case 3192:
		case 3194:
		case 3196:
		case 3198:
		case 2054:
		case 3202:
		case 3204:
		case 13092:
			return true;

		default:
			return false;
		}
	}

	public static void getPlayerAnimIndex(Player c, String weaponName) {
		c.playerStandIndex = 0x328;
		c.playerTurnIndex = 0x337;
		c.playerWalkIndex = 0x333;
		c.playerTurn180Index = 0x334;
		c.playerTurn90CWIndex = 0x335;
		c.playerTurn90CCWIndex = 0x336;
		c.playerRunIndex = 0x338;
		
		if (weaponName.contains("hunting knife")) {
			c.playerStandIndex = 7329;
			c.playerWalkIndex = 7327;
			c.playerRunIndex = 7327;
			return;
		}
		
		if (weaponName.contains("bulwark")) {
			c.playerStandIndex = 7508;
			c.playerWalkIndex = 7510;
			c.playerRunIndex = 7509;
			return;
		}
		
		if (weaponName.contains("elder maul")) {
			c.playerStandIndex = 7518;
			c.playerWalkIndex = 7520;
			c.playerRunIndex = 7519;
			return;
		}
		
		if (weaponName.contains("ballista")) {
			c.playerStandIndex = 7220;
			c.playerWalkIndex = 7223;
			c.playerRunIndex = 7221;
			return;
		}
		if (weaponName.contains("clueless")) {
			c.playerStandIndex = 7271;
			c.playerWalkIndex = 7272;
			c.playerRunIndex = 7273;
			return;
		}
		if (weaponName.contains("casket")) {
			c.playerRunIndex = 7274;
			return;
		}
		if (weaponName.contains("halberd") || weaponName.contains("banner") || weaponName.contains("hasta") || weaponName.contains("spear") || weaponName.contains("guthan") || weaponName.contains("sceptre")) {
			c.playerStandIndex = 809;
			c.playerWalkIndex = 1146;
			c.playerRunIndex = 1210;
			return;
		}
		if (weaponName.contains("sled")) {
			c.playerStandIndex = 1461;
			c.playerWalkIndex = 1468;
			c.playerRunIndex = 1467;
			return;
		}
		if (weaponName.contains("dharok")) {
			c.playerStandIndex = 0x811;
			c.playerWalkIndex = 2064;
			return;
		}
		if (weaponName.contains("ahrim")) {
			c.playerStandIndex = 809;
			c.playerWalkIndex = 1146;
			c.playerRunIndex = 1210;
			return;
		}
		if (weaponName.contains("verac")) {
			c.playerStandIndex = 1832;
			c.playerWalkIndex = 1830;
			c.playerRunIndex = 1831;
			return;
		}
		if (weaponName.contains("wand") || weaponName.contains("staff") || weaponName.contains("trident")) {
			c.playerStandIndex = 809;
			c.playerRunIndex = 1210;
			c.playerWalkIndex = 1146;
			return;
		}
		if (weaponName.contains("karil")) {
			c.playerStandIndex = 2074;
			c.playerWalkIndex = 2076;
			c.playerRunIndex = 2077;
			return;
		}
		if (weaponName.contains("2h sword") || weaponName.contains("godsword") || weaponName.contains("saradomin sw") || weaponName.contains("saradomin's bless") || weaponName.contains("large spade")) {
			if (c.playerEquipment[c.playerWeapon] != 7158) {
				c.playerStandIndex = 7053;
				c.playerWalkIndex = 7052;
				c.playerRunIndex = 7043;
				c.playerTurnIndex = 7049;
				c.playerTurn180Index = 7047;
				c.playerTurn90CWIndex = 7047;
				c.playerTurn90CCWIndex = 7048;
				return;
			}
		}
		if (weaponName.contains("bow")) {
			c.playerStandIndex = 808;
			c.playerWalkIndex = 819;
			c.playerRunIndex = 824;
			return;
		}
		
		if (weaponName.contains("zamorakian")) {
			c.playerStandIndex = 1662;
			c.playerWalkIndex = 1663;
			c.playerRunIndex = 1664;
			return;
		}

		switch (c.playerEquipment[c.playerWeapon]) {
		case 7158:
			c.playerStandIndex = 2065;
			c.playerWalkIndex = 2064;
			break;
		case 4151:
		case 12773:
		case 12774:
		case 12006:
			c.playerWalkIndex = 1660;
			c.playerRunIndex = 1661;
			break;
		case 8004:
		case 7960:
			c.playerStandIndex = 2065;
			c.playerWalkIndex = 2064;
			break;
		case 6528:
		case 20756:
			c.playerStandIndex = 0x811;
			c.playerWalkIndex = 2064;
			c.playerRunIndex = 1664;
			break;
		case 12848:
		case 4153:
		case 13263:
			c.playerStandIndex = 1662;
			c.playerWalkIndex = 1663;
			c.playerRunIndex = 1664;
			break;
		case 10887:
			c.playerStandIndex = 5869;
			c.playerWalkIndex = 5867;
			c.playerRunIndex = 5868;
			break;
		case 11802:
		case 11804:
		case 11838:
		case 12809:
		case 11806:
		case 11808:
			c.playerStandIndex = 7053;
			c.playerWalkIndex = 7052;
			c.playerRunIndex = 7043;
			c.playerTurnIndex = 7049;
			c.playerTurn180Index = 7052;
			c.playerTurn90CWIndex = 7052;
			c.playerTurn90CCWIndex = 7052;
			break;
		case 1305:
			c.playerStandIndex = 809;
			break;
			
		default:
			c.playerStandIndex = 0x328;
			c.playerTurnIndex = 0x337;
			c.playerWalkIndex = 0x333;
			c.playerTurn180Index = 0x334;
			c.playerTurn90CWIndex = 0x335;
			c.playerTurn90CCWIndex = 0x336;
			c.playerRunIndex = 0x338;
			break;
		}
	}

	public static int getWepAnim(Player c, String weaponName) {
		if (c.playerEquipment[c.playerWeapon] <= 0) {
			switch (c.fightMode) {
			case 0:
				return 422;
			case 2:
				return 423;
			case 1:
				return 422;
			}
		}
		if (weaponName.contains("bulwark")) {
			return 7511;
		}
		if (weaponName.contains("elder maul")) {
			return 7516;
		}
		if (weaponName.contains("zamorakian")) {
			return 2080;
		}
		if (weaponName.contains("hunting knife")) {
			return 7328;
		}
		if (weaponName.contains("ballista")) {
			return 7218;
		}
		if (weaponName.contains("toxic blowpipe")) {
			return 5061;
		}
		if (weaponName.contains("warhammer")) {
			return 401;
		}
		if (weaponName.contains("dart")) {
			return c.fightMode == 2 ? 806 : 6600;
		}
		if (weaponName.contains("dragon 2h")) {
			return 407;
		}
		if (weaponName.contains("knife") || weaponName.contains("javelin") || weaponName.contains("thrownaxe")) {
			return 806;
		}
		if (weaponName.contains("cross") && !weaponName.contains("karil") || weaponName.contains("c'bow") && !weaponName.contains("karil")) {
			return 4230;
		}
		if (weaponName.contains("halberd")) {
			return 440;
		}
		if (weaponName.startsWith("dragon dagger")) {
			return 402;
		}
		if (weaponName.contains("byssal dagger")) {
			return c.fightMode == 1 ? 3295 : c.fightMode == 0 || c.fightMode == 2 ? 3297 : 3294;
		}
		if (weaponName.contains("dagger")) {
			return 412;
		}
		if (weaponName.contains("2h sword") || weaponName.contains("godsword") || weaponName.contains("aradomin sword") || weaponName.contains("blessed sword") || weaponName.contains("large spade")) {
			switch (c.fightMode) {
			case 0:// attack
				return 7045;
			case 2:// str
				return 7045;
			case 1:// def
				return 7046;
			case 3:// crush
				return 7046;
			}
		}
		if (weaponName.contains("dharok")) {
			switch (c.fightMode) {
			case 0:// attack
				return 2067;
			case 2:// str
				return 2067;
			case 1:// def
				return 2067;
			case 3:// crush
				return 2066;
			}
		}
		if (weaponName.contains("sword") && !weaponName.contains("training")) {
			return 451;
		}
		if (weaponName.contains("karil")) {
			return 2075;
		}
		if (weaponName.contains("bow") && !weaponName.contains("'bow") && !weaponName.contains("karil")) {
			return 426;
		}
		if (weaponName.contains("'bow") && !weaponName.contains("karil")) {
			return 4230;
		}
		if (weaponName.contains("hasta") || weaponName.contains("spear")) {
			return 400;
		}
		switch (c.playerEquipment[c.playerWeapon]) { // if you don't want to use
														// strings
		case 9703:
			return 412;
			
		case 13263:
			return 3298;

		case 6522:
			return 2614;
		case 11959:
		case 10034:
		case 10033:
			return 2779;
		case 11791:
		case 12904:
			return 440;
		case 8004:
		case 7960:
			return 2075;
		case 12848:
		case 4153: // granite maul
			return 1665;
		case 4726: // guthan
			return 2080;
		case 4747: // torag
			return 0x814;
		case 4710: // ahrim
			return 406;
		case 4755: // verac
			return 2062;
		case 4734: // karil
			return 2075;
		case 4151:
		case 12773:
		case 12774:
		case 12006:
			return 1658;
		case 6528:
			return 2661;
		case 10887:
			return 5865;
		default:
			return 451;
		}
	}

	public static int getBlockEmote(Player c) {
		String shield = ItemAssistant.getItemName(c.playerEquipment[c.playerShield]).toLowerCase();
		String weapon = ItemAssistant.getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase();
		if (shield.contains("defender"))
			return 4177;
		if (shield.contains("2h") && c.playerEquipment[c.playerWeapon] != 7158)
			return 7050;
		if (shield.contains("book") || (weapon.contains("wand") || (weapon.contains("staff") || weapon.contains("trident"))))
			return 420;
		if (shield.contains("shield"))
			return 1156;
		if (shield.contains("warhammer"))
			return 397;
		if (shield.contains("bulwark"))
			return 7512;
		if (shield.contains("elder maul"))
			return 7517;
		switch (c.playerEquipment[c.playerWeapon]) {
		case 1734:
		case 411:
			return 3895;
		case 1724:
			return 3921;
		case 1709:
			return 3909;
		case 1704:
			return 3916;
		case 1699:
			return 3902;
		case 1689:
			return 3890;
		case 4755:
			return 2063;
		case 14484:
			return 397;
		case 12848:
		case 4153:		
		case 13263:
			return 1666;
		case 13265:
		case 13267:
		case 13269:
		case 13271:
			return 3295;
		case 7158:
			return 410;
		case 4151:
		case 12773:
		case 12774:
		case 12006:
			return 1659;

		case 11802:
		case 11806:
		case 11808:
		case 11804:
		case 11838:
		case 12809:
		case 11730:
			return 7056;
		case -1:
			return 424;
		default:
			return 424;
		}
	}

	public static int getAttackDelay(Player c, String s) {
		if (c.usingMagic) {
			if (c.spellId == 52 || c.spellId == 53) {
				return 4;
			}
			switch (MagicData.MAGIC_SPELLS[c.spellId][0]) {
			case 12871: // ice blitz
			case 13023: // shadow barrage
			case 12891: // ice barrage
				return 5;

			default:
				return 5;
			}
		}
		if (c.playerEquipment[c.playerWeapon] == -1)
			return 4;// unarmed
		switch (c.playerEquipment[c.playerWeapon]) {
		case 12926:
			return c.playerIndex > 0 ? 4 : 3;
		case 12765:
		case 12766:
		case 12767:
		case 12768:
		case 11235:
			return 9;
		case 12424:
		case 11838:
		case 12809:
			return 4;
		case 6528:
		case 19478:
		case 19481:
			return 7;
		case 10033:
		case 10034:
		case 21012:
			return 5;
		case 9703:
			return 5;
		}
		if (s.endsWith("greataxe"))
			return 7;
		else if (s.equals("torags hammers"))
			return 5;
		else if (s.equals("barrelchest anchor"))
			return 7;
		else if (s.equals("guthans warspear"))
			return 5;
		else if (s.equals("veracs flail"))
			return 5;
		else if (s.equals("ahrims staff"))
			return 6;
		else if (s.contains("staff")) {
			if (s.contains("zamarok") || s.contains("guthix") || s.contains("saradomian") || s.contains("slayer") || s.contains("ancient") || s.contains("trident"))
				return 4;
			else
				return 5;
		} else if (s.contains("bow")) {
			if (s.contains("composite") || s.equals("seercull"))
				return 5;
			else if (s.contains("aril"))
				return 4;
			else if (s.contains("Ogre"))
				return 8;
			else if (s.contains("short") || s.contains("hunt") || s.contains("sword"))
				return 4;
			else if (s.contains("long") || s.endsWith("crystal bow"))
				return 6;
			else if (s.contains("'bow") && !s.contains("armadyl"))
				return 6;
			return 5;
		} else if (s.contains("dagger"))
			return 4;
		else if (s.contains("godsword") || s.contains("2h"))
			return 6;
		else if (s.contains("longsword") || s.contains("elder maul"))
			return 5;
		else if (s.contains("sword") || s.contains("bulwark"))
			return 4;
		else if (s.contains("scimitar") || s.contains("of the dead"))
			return 4;
		else if (s.contains("mace"))
			return 5;
		else if (s.contains("battleaxe"))
			return 6;
		else if (s.contains("pickaxe"))
			return 5;
		else if (s.contains("thrownaxe"))
			return 5;
		else if (s.contains("axe"))
			return 5;
		else if (s.contains("warhammer"))
			return 6;
		else if (s.contains("2h"))
			return 7;
		else if (s.contains("spear"))
			return 5;
		else if (s.contains("claw"))
			return 4;
		else if (s.contains("halberd"))
			return 7;
		else if (s.equals("granite maul"))
			return 7;
		else if (s.equals("toktz-xil-ak")) // sword
			return 4;
		else if (s.equals("tzhaar-ket-em")) // mace
			return 5;
		else if (s.equals("tzhaar-ket-om")) // maul
			return 7;
		else if (s.equals("toktz-xil-ek")) // knife
			return 4;
		else if (s.equals("toktz-xil-ul")) // rings
			return 4;
		else if (s.equals("toktz-mej-tal")) // staff
			return 6;
		else if (s.contains("whip") || s.contains("tentacle") || s.contains("abyssal bludgeon"))
			return 4;
		else if (s.contains("dart"))
			return 3;
		else if (s.contains("knife"))
			return 3;
		else if (s.contains("javelin"))
			return 6;
		else if (s.contains("hasta")) {
			if (s.contains("zamorakian")) {
				return 4;
			}
			return 5;
		}
		return 5;
	}

	public static int getHitDelay(Player c, int i, String weaponName) {
		if (c.usingMagic) {
			switch (MagicData.MAGIC_SPELLS[c.spellId][0]) {
			case 12891:
				return 4;
			case 12871:
				return 6;
			default:
				return 4;
			}
		}
		if (weaponName.contains("dart")) {
			return 3;
		}
		if (weaponName.contains("knife") || weaponName.contains("javelin") || weaponName.contains("thrownaxe")) {
			return 3;
		}
		if (weaponName.contains("cross") || weaponName.contains("c'bow")) {
			return 4;
		}
		if (weaponName.contains("ballista")) {
			return 5;
		}
		if (weaponName.contains("bow") && !c.dbowSpec) {
			return 4;
		} else if (c.dbowSpec) {
			return 4;
		}

		switch (c.playerEquipment[c.playerWeapon]) {
		case 6522: // Toktz-xil-ul
			return 3;
		case 10887:
			return 3;
		case 10034:
		case 10033:
			return 3;
		default:
			return 2;
		}
	}

	public static int npcDefenceAnim(int i) {
		return BlockAnimation.handleEmote(i);
	}
}