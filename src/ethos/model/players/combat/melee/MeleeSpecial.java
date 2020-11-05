package ethos.model.players.combat.melee;

import ethos.model.items.ItemAssistant;
import ethos.model.npcs.NPCHandler;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.util.Misc;

/**
 *
 * Weapon Specials
 *
 * Class MeleeData
 * 
 * @author 2012
 *
 */

public class MeleeSpecial {

	public static boolean checkSpecAmount(Player c, int weapon) {
		switch (weapon) {
		case 1215:
		case 1231:
		case 1249:
		case 1263:
		case 1305:
		case 1434:
		case 5680:
		case 5698:
		case 5716:
		case 5730:
		case 13899:
		case 11824:
		case 11889:
			if (c.specAmount >= 2.5) {
				c.specAmount -= 2.5;
				c.getItems().addSpecialBar(weapon);
				return true;
			}
			return false;
		case 13902:
		case 13905:
			if (c.specAmount >= 3.5) {
				c.specAmount -= 3.5;
				c.getItems().addSpecialBar(weapon);
				return true;
			}
			return false;

		case 4151:
		case 11802:
		case 12006:
		case 11806:
		case 12848:
		case 4153:
		case 14484:
		case 12788:
		case 10887:
		case 13263:
			if (c.specAmount >= 5) {
				c.specAmount -= 5;
				c.getItems().addSpecialBar(weapon);
				return true;
			}
			return false;

		case 3204:
		case 12926:
			if (c.specAmount >= 5) {
				c.specAmount -= 5;
				c.getItems().addSpecialBar(weapon);
				return true;
			}
			return false;

		case 1377:
		case 11804:
		case 11838:
		case 21028:
		case 11061:
			if (c.specAmount >= 10) {
				c.specAmount -= 10;
				c.getItems().addSpecialBar(weapon);
				return true;
			}
			return false;

		case 11785:
			if (c.specAmount > 4) {
				c.specAmount -= 4;
				c.getItems().addSpecialBar(weapon);
				return true;
			}
			return false;
		case 12809:
			if (c.specAmount > 6.5) {
				c.specAmount -= 6.5;
				c.getItems().addSpecialBar(weapon);
				return true;
			}
			return false;
		case 4587:
		case 859:
		case 861:
		case 12765:
		case 12766:
		case 12767:
		case 12768:
		case 11235:
		case 11808:
			if (c.specAmount >= 5.5) {
				c.specAmount -= 5.5;
				c.getItems().addSpecialBar(weapon);
				return true;
			}
			return false;

		default:
			return true; // incase u want to test a weapon
		}
	}

	public static void activateSpecial(Player c, int weapon, int i) {
		if (NPCHandler.npcs[i] == null && c.npcIndex > 0) {
			return;
		}
		if (i > PlayerHandler.players.length - 1) {
			return;
		}
		c.doubleHit = false;
		c.specEffect = 0;
		c.projectileStage = 0;
		c.specMaxHitIncrease = 2;
		c.logoutDelay = System.currentTimeMillis();
		if (c.npcIndex > 0) {
			c.oldNpcIndex = i;
		} else if (c.playerIndex > 0) {
			c.oldPlayerIndex = i;
			PlayerHandler.players[i].underAttackBy = c.getIndex();
			PlayerHandler.players[i].logoutDelay = System.currentTimeMillis();
			PlayerHandler.players[i].singleCombatDelay = System.currentTimeMillis();
			PlayerHandler.players[i].killerId = c.getIndex();
		}
		if (c.playerIndex > 0) {
			c.getPA().followPlayer();
		} else if (c.npcIndex > 0) {
			c.getPA().followNpc();
		}
		switch (weapon) {

		/*
		 * case 14484: c.startAnimation(7290); c.gfx0(1242); c.hitDelay = c.getCombat().getHitDelay(i, ItemAssistant.getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
		 * c.specAccuracy = 4.5; c.usingClaws = true; break;
		 * 
		 * case 11061: c.gfx0(1052); c.startAnimation(6147); c.hitDelay = c.getCombat().getHitDelay(i, ItemAssistant.getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
		 * c.specDamage = 1.30; c.specAccuracy = 1.65; c.specEffect = 5; break; case 13905: // Vesta spear c.gfx0(1240); c.startAnimation(7294); c.specAccuracy = 1.25; c.specEffect
		 * = 6; c.hitDelay = c.getCombat().getHitDelay(i, ItemAssistant.getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase()); break; case 13899: // Vesta LongSword
		 * c.startAnimation(7295); c.hitDelay = c.getCombat().getHitDelay(i, ItemAssistant.getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase()+1); c.specDamage = 1.25;
		 * c.specAccuracy = 2.0; break; case 13902: // Statius c.gfx0(1241); c.startAnimation(7296); c.hitDelay = c.getCombat().getHitDelay(i,
		 * ItemAssistant.getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase()+1); c.specDamage = 1.35; c.specAccuracy = 2.25; break;
		 * 
		 * case 10887: c.gfx0(1027); c.startAnimation(5870); c.hitDelay = c.getCombat().getHitDelay(i, ItemAssistant.getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
		 * c.specDamage = 1.20; c.specAccuracy = 1.85; break;
		 * 
		 * case 1305: // dragon long c.gfx100(248); c.startAnimation(1058); c.hitDelay = c.getCombat().getHitDelay(i,
		 * ItemAssistant.getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase()); c.specAccuracy = 1.10; c.specDamage = 1.20; break;
		 * 
		 * case 1215: // dragon daggers case 1231: case 5680: case 5698: c.gfx100(252); c.startAnimation(1062); c.hitDelay = c.getCombat().getHitDelay(i,
		 * ItemAssistant.getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase()); c.doubleHit = true; c.specAccuracy = 0.75; c.specDamage = 1.00; break;
		 * 
		 * case 11785: c.startAnimation(4230); c.usingBow = true; c.rangeItemUsed = c.playerEquipment[c.playerArrows]; if (c.playerIndex > 0) c.getCombat().fireProjectilePlayer(0);
		 * else if (c.npcIndex > 0) c.getCombat().fireProjectileNpc(0); c.hitDelay = c.getCombat().getHitDelay(i,
		 * ItemAssistant.getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase()); c.specAccuracy = 2; c.getCombat().calculateRangeAttack(); break;
		 * 
		 * case 11838: c.gfx100(1224); c.startAnimation(7072); c.hitDelay = c.getCombat().getHitDelay(i,
		 * ItemAssistant.getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase()); c.doubleHit = true; c.ssSpec = true; c.specAccuracy = 1.30; break;
		 * 
		 * case 12809: c.gfx100(1224); c.startAnimation(7072); c.hitDelay = c.getCombat().getHitDelay(i,
		 * ItemAssistant.getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase()); //c.doubleHit = true; c.ssSpec = true; c.specAccuracy = 2; c.specDamage = 1.10; break;
		 * 
		 * case 4151: // whip if(NPCHandler.npcs[i] != null) { NPCHandler.npcs[i].gfx100(341); } c.specAccuracy = 1.10; c.startAnimation(1658); c.hitDelay =
		 * c.getCombat().getHitDelay(i, ItemAssistant.getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase()); break;
		 * 
		 * case 12006: // tentacle whip if(NPCHandler.npcs[i] != null) { NPCHandler.npcs[i].gfx100(341); } c.specAccuracy = 1.20; c.startAnimation(1658); if (o != null) {
		 * o.gfx100(341); o.freezeTimer = 5; } c.hitDelay = c.getCombat().getHitDelay(i, ItemAssistant.getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase()); break;
		 * 
		 * case 11802: // ags c.startAnimation(7074); c.specDamage = 1.20; c.specAccuracy = 2.90; c.gfx0(1222); c.hitDelay = c.getCombat().getHitDelay(i,
		 * ItemAssistant.getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase()); break;
		 * 
		 * case 11808: c.startAnimation(7070); c.gfx0(1221); c.specAccuracy = 1.25; c.hitDelay = c.getCombat().getHitDelay(i,
		 * ItemAssistant.getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase()); c.specEffect = 2; break;
		 * 
		 * case 11804: c.startAnimation(7073); c.gfx0(1223); c.specDamage = 1.10; c.specAccuracy = 1.5; c.hitDelay = c.getCombat().getHitDelay(i,
		 * ItemAssistant.getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase()); c.specEffect = 3; break;
		 * 
		 * case 11806: c.startAnimation(7071); c.gfx0(1220); c.specAccuracy = 1.25; c.specEffect = 4; c.hitDelay = c.getCombat().getHitDelay(i,
		 * ItemAssistant.getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase()); break;
		 * 
		 * case 1249: c.startAnimation(405); c.gfx100(253); if (c.playerIndex > 0) { o.getPA().getSpeared(c.absX, c.absY, 1); o.gfx0(80); } break;
		 * 
		 * 
		 * case 3204: // d hally c.gfx100(282); c.startAnimation(1203); c.specAccuracy = 1.40; c.hitDelay = c.getCombat().getHitDelay(i,
		 * ItemAssistant.getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase()); if(NPCHandler.npcs[i] != null && c.npcIndex > 0) { // if(!c.goodDistance(c.getX(), c.getY(),
		 * NPCHandler.npcs[i].getX(), NPCHandler.npcs[i].getY(), 1)){ c.doubleHit = true; //} } if(PlayerHandler.players[i] != null && c.playerIndex > 0) { //
		 * if(!c.goodDistance(c.getX(), c.getY(), PlayerHandler.players[i].getX(),PlayerHandler.players[i].getY(), 1)){ c.doubleHit = true; c.delayedDamage2 =
		 * Misc.random(c.getCombat().calculateMeleeMaxHit()); //} } break;
		 */

		case 12848:
		case 4153: // maul
			if (c.isDead)
				return;
			c.startAnimation(1667);
			c.hitDelay = c.getCombat().getHitDelay(i, ItemAssistant.getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
			c.gfx100(337);
			break;

		/*
		 * case 4587: // dscimmy c.gfx100(347); c.specEffect = 1; c.startAnimation(1872); c.hitDelay = c.getCombat().getHitDelay(i,
		 * ItemAssistant.getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase()); break;
		 * 
		 * case 1434: // mace c.startAnimation(1060); c.gfx100(251); c.specMaxHitIncrease = 3; c.hitDelay = c.getCombat().getHitDelay(i,
		 * ItemAssistant.getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase())+1; c.specDamage = 1.35; c.specAccuracy = 1.15; break;
		 * 
		 * case 859: // magic long c.usingBow = true; c.bowSpecShot = 3; c.rangeItemUsed = c.playerEquipment[c.playerArrows]; c.getItems().deleteArrow(); c.lastWeaponUsed = weapon;
		 * c.startAnimation(426); c.gfx100(250); c.hitDelay = c.getCombat().getHitDelay(i, ItemAssistant.getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
		 * c.projectileStage = 1; if (c.fightMode == 2) c.attackTimer--; break;
		 * 
		 * case 12926: c.usingBow = true; c.usingArrows = true; c.usingMagic = false; c.startAnimation(5061); c.specAccuracy = 1.35; c.specDamage = 1.52; c.hitDelay =
		 * c.getCombat().getHitDelay(i, ItemAssistant.getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase()); c.lastWeaponUsed = 12926; c.projectileStage = 1; if
		 * (c.playerIndex > 0) c.getCombat().fireProjectilePlayer(0); else if (c.npcIndex > 0) c.getCombat().fireProjectileNpc(0); if (c.fightMode == 2) c.attackTimer--; break;
		 * 
		 * case 861: // magic short c.usingBow = true; c.bowSpecShot = 1; c.rangeItemUsed = c.playerEquipment[c.playerArrows]; c.getItems().deleteArrow();
		 * c.getItems().deleteArrow(); c.lastWeaponUsed = weapon; c.startAnimation(1074); c.hitDelay = 3; c.projectileStage = 1; c.hitDelay = c.getCombat().getHitDelay(i,
		 * ItemAssistant.getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase()); if (c.fightMode == 2) c.attackTimer--; if (c.playerIndex > 0) {
		 * c.getCombat().fireProjectilePlayer(0); c.getCombat().fireProjectilePlayer(5); } else if (c.npcIndex > 0) { c.getCombat().fireProjectileNpc(0);
		 * c.getCombat().fireProjectileNpc(5); } break;
		 * 
		 * case 12788: // magic short c.usingBow = true; c.bowSpecShot = 1; c.rangeItemUsed = c.playerEquipment[c.playerArrows]; c.getItems().deleteArrow();
		 * c.getItems().deleteArrow(); c.lastWeaponUsed = weapon; c.startAnimation(1074); c.hitDelay = 3; c.projectileStage = 1; c.hitDelay = c.getCombat().getHitDelay(i,
		 * ItemAssistant.getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase()); if (c.fightMode == 2) c.attackTimer--; if (c.playerIndex > 0) {
		 * c.getCombat().fireProjectilePlayer(0); c.getCombat().fireProjectilePlayer(5); } else if (c.npcIndex > 0) { c.getCombat().fireProjectileNpc(0);
		 * c.getCombat().fireProjectileNpc(5); } break;
		 * 
		 * case 12765: case 12766: case 12767: case 12768: case 11235: // dark bow c.usingBow = true; c.dbowSpec = true; c.rangeItemUsed = c.playerEquipment[c.playerArrows];
		 * c.getItems().deleteArrow(); c.getItems().deleteArrow(); if (c.playerIndex > 0) { c.getItems().dropArrowPlayer(); } else if(c.npcIndex > 0) {
		 * c.getItems().dropArrowNpc(NPCHandler.npcs[i]); } c.lastWeaponUsed = weapon; c.hitDelay = 3; c.startAnimation(426); c.projectileStage = 1;
		 * c.gfx100(c.getCombat().getRangeStartGFX()); c.hitDelay = c.getCombat().getHitDelay(i, ItemAssistant.getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase()); if
		 * (c.fightMode == 2) c.attackTimer--; if (c.playerIndex > 0) c.getCombat().fireProjectilePlayer(0); else if (c.npcIndex > 0) c.getCombat().fireProjectileNpc(0);
		 * c.specAccuracy = 2.85; c.specDamage = 1.5; break;
		 */
		}
		c.delayedDamage = Misc.random(c.getCombat().calculateMeleeMaxHit());
		c.delayedDamage2 = Misc.random(c.getCombat().calculateMeleeMaxHit());
	}
}