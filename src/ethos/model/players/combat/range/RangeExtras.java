package ethos.model.players.combat.range;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import ethos.Config;
import ethos.model.entity.Entity;
import ethos.model.npcs.NPC;
import ethos.model.npcs.NPCHandler;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.model.players.combat.AttackPlayer;
import ethos.model.players.combat.CombatType;
import ethos.model.players.combat.Damage;
import ethos.model.players.combat.DamageEffect;
import ethos.model.players.combat.Hitmark;
import ethos.model.players.combat.effects.bolts.DiamondBoltSpecial;
import ethos.model.players.combat.effects.bolts.DragonBoltSpecial;
import ethos.model.players.combat.effects.bolts.EmeraldBoltSpecial;
import ethos.model.players.combat.effects.bolts.JadeBoltSpecial;
import ethos.model.players.combat.effects.bolts.OnyxBoltSpecial;
import ethos.model.players.combat.effects.bolts.OpalBoltSpecial;
import ethos.model.players.combat.effects.bolts.PearlBoltSpecial;
import ethos.model.players.combat.effects.bolts.RubyBoltSpecial;
import ethos.model.players.combat.effects.bolts.SapphireBoltSpecial;
import ethos.model.players.combat.effects.bolts.TopazBoltSpecial;
import ethos.util.Misc;

public class RangeExtras {

	public static boolean checkMultiChinchompaReqsNPC(int i) {
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
	
	public static boolean checkMultiChinReqs(Player c, int i) {
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

	public static void appendMultiChinchompa(Player c, int npcId) {
		if (NPCHandler.npcs[npcId] != null) {
			NPC n = NPCHandler.npcs[npcId];
			if (n.isDead)
				return;
			if (n.heightLevel != c.heightLevel)
				return;
			if (checkMultiChinchompaReqsNPC(npcId)) {
				c.barrageCount++;
				c.multiAttacking = true;
				NPCHandler.npcs[npcId].underAttackBy = c.getIndex();
				NPCHandler.npcs[npcId].underAttack = true;
				if (Misc.random(c.getCombat().calculateRangeAttack()) > Misc.random(NPCHandler.npcs[npcId].defence)) {
					n.gfx100(909);

					int damage = Misc.random(c.getCombat().rangeMaxHit());
					if (n.getHealth().getAmount() - damage < 0) {
						damage = n.getHealth().getAmount();
					}
					if (damage == 0) {
						return;
					}
					if (n.npcType == 7413) {
						n.appendDamage(c, c.getCombat().rangeMaxHit(), Hitmark.HIT);
					} else {
						AttackPlayer.addCombatXP(c, CombatType.RANGE, damage);
						n.appendDamage(c, damage, Hitmark.HIT);
					}
					c.totalDamageDealt += damage;
				} else {
					n.gfx0(909);
				}
			}
		}
	}

	public static void createCombatGraphic(Player player, Entity entity, int graphic, boolean height100) {
		if (entity instanceof Player) {
			Player target = (Player) entity;
			if (height100 == true) {
				target.gfx100(graphic);
			} else {
				target.gfx0(graphic);
			}
		} else if (entity instanceof NPC) {
			NPC target = (NPC) entity;
			if (height100 == true) {
				target.gfx100(graphic);
			} else {
				target.gfx0(graphic);
			}
		}
	}

	private static final List<DamageEffect> BOLT_EFFECTS = Collections
			.unmodifiableList(Arrays.asList(new DragonBoltSpecial(), new OpalBoltSpecial(), new JadeBoltSpecial(), new PearlBoltSpecial(), new TopazBoltSpecial(),
					new EmeraldBoltSpecial(), new SapphireBoltSpecial(), new RubyBoltSpecial(), new DiamondBoltSpecial(), new OnyxBoltSpecial()));

	public static int executeBoltSpecial(Player attacker, Entity defender, Damage accumulativeDamage) {
		Optional<DamageEffect> boltEffect = BOLT_EFFECTS.stream().filter(e -> e.isExecutable(attacker)).findFirst();
		boltEffect.ifPresent(effect -> {
			if (defender instanceof NPC) {
				effect.execute(attacker, (NPC) defender, accumulativeDamage);
			} else if (defender instanceof Player) {
				effect.execute(attacker, (Player) defender, accumulativeDamage);
			}
		});

		return accumulativeDamage.getAmount();
	}

	public static boolean boltSpecialAvailable(Player player, int bolt) {
		if (player.playerEquipment[player.playerArrows] != bolt) {
			return false;
		}
		int probability = 0;

		switch (bolt) {
		case 9236:
		case 9237:
		case 9238:
		case 9239:
		case 9240:
		case 9241:
		case 9243:
		case 9244:
		case 9245:
			probability = player.getRechargeItems().hasItem(13139) || player.getRechargeItems().hasItem(13140) ? 18 : 20;
			break;
			

		case 9242:
			probability = player.getRechargeItems().hasItem(13139) || player.getRechargeItems().hasItem(13140) ? 45 : 50;
			break;

		default:
			probability = 20;
			break;
		}

		if (player.playerEquipment[player.playerWeapon] == 11785 && player.usingSpecial && player.specAmount >= 4) {
			probability = 3;
		}

		return Misc.random(probability) == 0;
	}

	public static boolean wearingCrossbow(Player player) {
		return player.playerEquipment[player.playerWeapon] == 11785 || player.playerEquipment[player.playerWeapon] == 9185 || player.playerEquipment[player.playerWeapon] == 21012;
	}

	public static boolean wearingBolt(Player player) {
		return player.playerEquipment[player.playerArrows] >= 9236 && player.playerEquipment[player.playerArrows] <= 9245;
	}
}