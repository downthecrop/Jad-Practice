package ethos.model.players.combat.magic;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import ethos.Config;
import ethos.model.npcs.NPC;
import ethos.model.npcs.NPCHandler;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.model.players.combat.AttackPlayer;
import ethos.model.players.combat.CombatType;
import ethos.model.players.combat.Hitmark;
import ethos.model.players.mode.ModeType;
import ethos.util.Misc;

public class MagicExtras {

	public static void multiSpellEffectNPC(Player c, int npcId, int damage) {
		NPC npc = NPCHandler.npcs[npcId];
		if (npc.npcType >= 2042 && npc.npcType <= 2044) {
			return;
		}
		switch (MagicData.MAGIC_SPELLS[c.oldSpellId][0]) {
		case 12891:
		case 12881:
			if (NPCHandler.npcs[npcId].freezeTimer < -4) {
				NPCHandler.npcs[npcId].freezeTimer = c.getCombat().getFreezeTime();
			}
			break;

		case 12919: // blood spells
		case 12929:
			int heal = damage / 4;
			c.getHealth().increase(heal);
			c.getPA().refreshSkill(3);
			break;
		}
	}

	public static boolean checkMultiBarrageReqsNPC(int i) {
		if (NPCHandler.npcs[i] == null) {
			return false;
		}
		if (NPCHandler.npcs[i].npcType == 6611 || NPCHandler.npcs[i].npcType == 6612) {
			List<NPC> minion = Arrays.asList(NPCHandler.npcs);
			if (minion.stream().filter(Objects::nonNull)
					.anyMatch(n -> n.npcType == 5054 && !n.isDead && n.getHealth().getAmount() > 0)) {
				return false;
			}
		}
		return true;
	}

	public static boolean checkMultiBarrageReqs(Player c, int i) {
		if (PlayerHandler.players[i] == null) {
			return false;
		}
		if (i == c.getIndex())
			return false;
		if (!PlayerHandler.players[i].inWild()) {
			return false;
		}
		if (Config.COMBAT_LEVEL_DIFFERENCE) {
			int combatDif1 = c.getCombat().getCombatDifference(c.combatLevel, PlayerHandler.players[i].combatLevel);
			if (combatDif1 > c.wildLevel || combatDif1 > PlayerHandler.players[i].wildLevel) {
				c.sendMessage("Your combat level difference is too great to attack that player here.");
				return false;
			}
		}

		if (Config.SINGLE_AND_MULTI_ZONES) {
			if (!PlayerHandler.players[i].inMulti()) { // single combat zones
				if (PlayerHandler.players[i].underAttackBy != c.getIndex() && PlayerHandler.players[i].underAttackBy != 0) {
					return false;
				}
				if (PlayerHandler.players[i].getIndex() != c.underAttackBy && c.underAttackBy != 0) {
					c.sendMessage("You are already in combat.");
					return false;
				}
			}
		}
		return true;
	}

	public static void appendMultiBarrageNPC(Player c, int npcId, boolean splashed) {
		if (NPCHandler.npcs[npcId] != null) {
			NPC n = NPCHandler.npcs[npcId];
			if (n.isDead)
				return;
			if (n.heightLevel != c.heightLevel)
				return;
			if (checkMultiBarrageReqsNPC(npcId)) {
				c.barrageCount++;
				c.multiAttacking = true;
				NPCHandler.npcs[npcId].underAttackBy = c.getIndex();
				NPCHandler.npcs[npcId].underAttack = true;
				if (Misc.random(c.getCombat().mageAtk()) > Misc.random(NPCHandler.npcs[npcId].defence) && !c.magicFailed) {
					n.gfx100(MagicData.MAGIC_SPELLS[c.oldSpellId][5]);
					int damage = Misc.random(c.getCombat().magicMaxHit());
					if (n.getHealth().getAmount() - damage < 0) {
						damage = n.getHealth().getAmount();
					}
					if (n.npcType == 7413) {
						n.appendDamage(c, c.getCombat().magicMaxHit(), Hitmark.HIT);
					} else {
						AttackPlayer.addCombatXP(c, CombatType.MAGE, damage);
						n.appendDamage(c, damage, Hitmark.HIT);
					}
					c.getCombat().multiSpellEffectNPC(npcId, damage);
					c.totalDamageDealt += damage;
				} else {
					n.gfx0(85);
				}
			}
		}
	}

	public static void multiSpellEffect(Player c, int playerId, int damage) {
		switch (MagicData.MAGIC_SPELLS[c.oldSpellId][0]) {
		case 13011:
		case 13023:
			if (System.currentTimeMillis() - PlayerHandler.players[playerId].reduceStat > 35000) {
				PlayerHandler.players[playerId].reduceStat = System.currentTimeMillis();
				PlayerHandler.players[playerId].playerLevel[0] -= ((PlayerHandler.players[playerId].getLevelForXP(PlayerHandler.players[playerId].playerXP[0]) * 10) / 100);
			}
			break;
		case 12919: // blood spells
		case 12929:
			int heal = damage / 4;
			c.getHealth().increase(heal);
			c.getPA().refreshSkill(3);
			break;
		case 12891:
		case 12881:
			if (PlayerHandler.players[playerId].freezeTimer < -4) {
				PlayerHandler.players[playerId].freezeTimer = c.getCombat().getFreezeTime();
				PlayerHandler.players[playerId].stopMovement();
			}
			break;
		}
	}

	public static void appendMultiBarrage(Player c, int playerId, boolean splashed) {
		if (PlayerHandler.players[playerId] != null) {
			Player c2 = PlayerHandler.players[playerId];
			if (c2.isDead || c2.respawnTimer > 0)
				return;
			if (c.getCombat().checkMultiBarrageReqs(playerId)) {
				c.barrageCount++;
				if (Misc.random(c.getCombat().mageAtk()) > Misc.random(c2.getCombat().mageDef()) && !c.magicFailed) {
					if (c.getCombat().getEndGfxHeight() == 100) { // end GFX
						c2.gfx100(MagicData.MAGIC_SPELLS[c.oldSpellId][5]);
					} else {
						c2.gfx0(MagicData.MAGIC_SPELLS[c.oldSpellId][5]);
					}
					int damage = Misc.random(c.getCombat().magicMaxHit());
					if (c2.prayerActive[12]) {
						damage *= (int) (.60);
					}
					if (c2.getHealth().getAmount() - damage < 0) {
						damage = c2.getHealth().getAmount();
					}
					c.getPA().addSkillXP((MagicData.MAGIC_SPELLS[c.oldSpellId][7] + damage * (c.getMode().getType().equals(ModeType.OSRS) ? 1 : Config.MAGIC_EXP_RATE)), 6, true);
					c.getPA().addSkillXP((MagicData.MAGIC_SPELLS[c.oldSpellId][7] + damage * (c.getMode().getType().equals(ModeType.OSRS) ? 1 : Config.MAGIC_EXP_RATE) / 3), 3, true);
					c2.appendDamage(damage, damage > 0 ? Hitmark.HIT : Hitmark.MISS);
					c2.addDamageTaken(c, damage);
					c2.getPA().refreshSkill(3);
					c.getCombat().multiSpellEffect(playerId, damage);
				} else {
					c2.gfx100(85);
				}
			}
		}
	}
}