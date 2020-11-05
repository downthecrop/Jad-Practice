package ethos.model.players.combat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.ArrayUtils;

import ethos.Config;
import ethos.Server;
import ethos.clip.PathChecker;
import ethos.model.content.achievement_diary.wilderness.WildernessDiaryEntry;
import ethos.model.items.EquipmentSet;
import ethos.model.items.Item;
import ethos.model.items.ItemAssistant;
import ethos.model.multiplayer_session.MultiplayerSessionType;
import ethos.model.multiplayer_session.duel.DuelSession;
import ethos.model.multiplayer_session.duel.DuelSessionRules.Rule;
import ethos.model.npcs.NPCHandler;
import ethos.model.players.Boundary;
import ethos.model.players.ClientGameTimer;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.model.players.Right;
import ethos.model.players.combat.effects.AmuletOfTheDamnedDharokEffect;
import ethos.model.players.combat.effects.AmuletOfTheDamnedKarilEffect;
import ethos.model.players.combat.effects.SerpentineHelmEffect;
import ethos.model.players.combat.effects.ToxicBlowpipeEffect;
import ethos.model.players.combat.effects.ToxicStaffOfTheDeadEffect;
import ethos.model.players.combat.effects.TridentOfTheSwampEffect;
import ethos.model.players.combat.magic.MagicData;
import ethos.model.players.combat.range.Arrow;
import ethos.model.players.combat.range.RangeData;
import ethos.model.players.combat.range.RangeExtras;
import ethos.model.players.combat.specials.Shove;
import ethos.model.players.mode.ModeType;
import ethos.model.players.skills.Skill;
import ethos.model.players.skills.herblore.PoisonedWeapon;
import ethos.util.Misc;

public class AttackPlayer {

	public static void applyPlayerHit(final Player c, final int i, final Damage damage) {
		c.getCombat().applyPlayerMeleeDamage(i, 1, damage.getAmount(), damage.getHitmark());
	}

	public static boolean calculateBlockedHit(Player c, int defence) {
		if (defence > 450 && Misc.random(5) == 1)
			return true;
		if (defence > 400 && Misc.random(5) == 1)
			return true;
		if (defence > 350 && Misc.random(6) == 1)
			return true;
		if (defence > 300 && Misc.random(6) == 1)
			return true;
		if (Misc.random(6) == 1 && defence > 150)
			return true;
		if (defence > 10 && Misc.random(7) == 1)
			return true;
		return false;
	}

	public static int calculateDefenceDamageReduction(Player c, int i, int damage) {
		Player o = PlayerHandler.players[i];
		int defence = o.getCombat().calculateMeleeDefence();
		if (calculateBlockedHit(c, defence))
			return 0;
		if (defence > 450)
			return damage *= .635;
		if (defence > 400)
			return damage *= .655;
		if (defence > 350)
			return damage *= .715;
		if (defence > 300)
			return damage *= .775;
		if (defence > 250)
			return damage *= .835;
		if (defence > 200)
			return damage *= .85;
		if (defence > 150)
			return damage *= .9125;
		if (defence > 100)
			return damage *= .975;
		if (defence > 10)
			return damage *= .99;
		return damage;
	}

	public static void applyPlayerMeleeDamage(Player c, int i, int damageMask, int damage, Hitmark hitmark) {
		c.previousDamage = damage;

		Player o = PlayerHandler.players[i];
		if (o == null) {
			return;
		}
		boolean guthansEffect = false;
		if (c.getPA().fullGuthans()) {
			if (Misc.random(4) == 1) {
				guthansEffect = true;
			}
		}
		if (damage > 0 && guthansEffect) {
			c.getHealth().increase(damage);
			if (c.getHealth().getAmount() > c.getHealth().getMaximum()) {
				c.getHealth().reset();
			}
			o.gfx0(398);
		}
		if (c.ssSpec && damageMask == 2) {
			damage = 5 + Misc.random(11);
			c.ssSpec = false;
		}
		if (PlayerHandler.players[i].getHealth().getAmount() - damage < 0) {
			damage = PlayerHandler.players[i].getHealth().getAmount();
		}
		if (o.vengOn && damage > 0)
			c.getCombat().appendVengeance(i, damage);
		if (damage > 0)
			c.getCombat().applyRecoil(damage, i);
		switch (c.specEffect) {
		case 1: // dragon scimmy special
			if (damage > 0) {
				if (o.prayerActive[16] || o.prayerActive[17] || o.prayerActive[18]) {
					o.headIcon = -1;
					o.getPA().sendFrame36(c.PRAYER_GLOW[16], 0);
					o.getPA().sendFrame36(c.PRAYER_GLOW[17], 0);
					o.getPA().sendFrame36(c.PRAYER_GLOW[18], 0);
				}
				o.sendMessage("You have been injured!");
				o.stopPrayerDelay = System.currentTimeMillis();
				o.prayerActive[16] = false;
				o.prayerActive[17] = false;
				o.prayerActive[18] = false;
				o.getPA().requestUpdates();
			}
			break;

		case 2:
			if (damage > 0) {
				if (o.freezeTimer <= 0)
					o.freezeTimer = 30;
				o.gfx0(369);
				o.sendMessage("You have been frozen.");
				o.frozenBy = c.getIndex();
				o.stopMovement();
				c.sendMessage("You freeze your enemy.");
			}
			break;
		case 3:
			if (damage > 0) {
				o.playerLevel[1] -= damage;
				o.sendMessage("You feel weak.");
				if (o.playerLevel[1] < 1)
					o.playerLevel[1] = 1;
				o.getPA().refreshSkill(1);
			}
			break;
		case 4:
			if (damage > 0) {
				c.getHealth().increase(damage);
			}
			break;

		case 5:
			if (c.playerIndex > 0) {
				if (damage > 0) {
					c.playerLevel[5] += damage;
					if (c.playerLevel[5] < 0)
						c.playerLevel[5] = 0;
					else if (c.playerLevel[5] > c.getPA().getLevelForXP(c.playerXP[5]))
						c.playerLevel[5] = c.getPA().getLevelForXP(c.playerXP[5]);
					c.getPA().refreshSkill(5);

					o.playerLevel[5] -= damage;
					if (o.playerLevel[5] < 0)
						o.playerLevel[5] = 0;
					else if (o.playerLevel[5] > c.getPA().getLevelForXP(o.playerXP[5]))
						o.playerLevel[5] = c.getPA().getLevelForXP(o.playerXP[5]);
					o.getPA().refreshSkill(5);
				}
			}
			break;
		}
		c.specEffect = 0;
		o.logoutDelay = System.currentTimeMillis();
		o.underAttackBy = c.getIndex();
		o.killerId = c.getIndex();
		o.singleCombatDelay = System.currentTimeMillis();
		c.killedBy = PlayerHandler.players[i].getIndex();
		c.getCombat().applySmite(i, damage);
		o.appendDamage(damage, hitmark);
		o.addDamageTaken(c, damage);
	}

	public static void addCombatXP(Player c, CombatType type, int damage) {
		int experience = 0;
		List<Integer> skills = new ArrayList<>();
		switch (type) {
		default:
		case MELEE:
			experience += (int) Math.ceil((damage * (c.getMode().getType().equals(ModeType.OSRS) ? 4 : Config.MELEE_EXP_RATE))) / 3;
			skills.add(Skill.HITPOINTS.getId());
			c.getPA().addSkillXP((int) Math.ceil((damage * (c.getMode().getType().equals(ModeType.OSRS) ? 4 : Config.MELEE_EXP_RATE))) / 3, 3, false);

			switch (c.fightMode) {
			default:
			case 0: // Accurate
				skills.add(Skill.ATTACK.getId());
				experience += (int) Math.ceil(damage * (c.getMode().getType().equals(ModeType.OSRS) ? 4 : Config.MELEE_EXP_RATE));
				c.getPA().addSkillXP((int) Math.ceil(damage * (c.getMode().getType().equals(ModeType.OSRS) ? 4 : Config.MELEE_EXP_RATE)), 0, false);
				break;
			case 1: // Block
				skills.add(Skill.DEFENCE.getId());
				experience += (int) Math.ceil(damage * (c.getMode().getType().equals(ModeType.OSRS) ? 4 : Config.MELEE_EXP_RATE));
				c.getPA().addSkillXP((int) Math.ceil(damage * (c.getMode().getType().equals(ModeType.OSRS) ? 4 : Config.MELEE_EXP_RATE)), 1, false);
				break;
			case 2: // Aggressive
				skills.add(Skill.STRENGTH.getId());
				experience += (int) Math.ceil(damage * (c.getMode().getType().equals(ModeType.OSRS) ? 4 : Config.MELEE_EXP_RATE));
				c.getPA().addSkillXP((int) Math.ceil(damage * (c.getMode().getType().equals(ModeType.OSRS) ? 4 : Config.MELEE_EXP_RATE)), 2, false);
				break;
			case 3: // Controlled
				int split = (int) Math.ceil(damage * (c.getMode().getType().equals(ModeType.OSRS) ? 4 : Config.MELEE_EXP_RATE)) / 3;
				experience += (int) Math.ceil(damage * (c.getMode().getType().equals(ModeType.OSRS) ? 4 : Config.MELEE_EXP_RATE));
				for (int i = 0; i < 3; i++) {
					c.getPA().addSkillXP(split, i, false);// 1.3
					skills.add(i);
				}
				break;
			}
			break;

		case RANGE:
			skills.add(Skill.HITPOINTS.getId());
			experience += (damage * (c.getMode().getType().equals(ModeType.OSRS) ? 4 : Config.RANGE_EXP_RATE)) / 3;
			c.getPA().addSkillXP((damage * (c.getMode().getType().equals(ModeType.OSRS) ? 4 : Config.RANGE_EXP_RATE)) / 3, 3, false);
			switch (c.fightMode) {
			case 0: // Accurate
			case 2: // Rapid
				skills.add(Skill.RANGED.getId());
				experience += (damage * (c.getMode().getType().equals(ModeType.OSRS) ? 4 : Config.RANGE_EXP_RATE));
				c.getPA().addSkillXP(damage * (c.getMode().getType().equals(ModeType.OSRS) ? 4 : Config.RANGE_EXP_RATE), 4, false);
				break;
			case 1: // Block
			case 3:
				skills.add(Skill.RANGED.getId());
				skills.add(Skill.DEFENCE.getId());
				experience += (damage * (c.getMode().getType().equals(ModeType.OSRS) ? 4 : Config.RANGE_EXP_RATE)) + (damage * (c.getMode().getType().equals(ModeType.OSRS) ? 4 : Config.RANGE_EXP_RATE) / 2);
				c.getPA().addSkillXP(damage * (c.getMode().getType().equals(ModeType.OSRS) ? 4 : Config.RANGE_EXP_RATE), 1, false);
				c.getPA().addSkillXP(damage * (c.getMode().getType().equals(ModeType.OSRS) ? 4 : Config.RANGE_EXP_RATE) / 2, 4, false);
				break;
			}
			break;
		case MAGE:
			skills.add(Skill.HITPOINTS.getId());
			skills.add(Skill.MAGIC.getId());
			experience += ((damage * (c.getMode().getType().equals(ModeType.OSRS) ? 4 : Config.MAGIC_EXP_RATE)) / 3) + (damage * (c.getMode().getType().equals(ModeType.OSRS) ? 4 : Config.MAGIC_EXP_RATE));
			c.getPA().addSkillXP((damage * (c.getMode().getType().equals(ModeType.OSRS) ? 4 : Config.MAGIC_EXP_RATE)) / 3, 3, false);
			c.getPA().addSkillXP(damage * (c.getMode().getType().equals(ModeType.OSRS) ? 4 : Config.MAGIC_EXP_RATE), 6, false);
			break;
		}
		if (experience > 0) {
			c.getPA().sendExperienceDrop(true, experience, ArrayUtils.toPrimitive(skills.toArray(new Integer[skills.size()])));
		}
	}

	public static void playerDelayedHit(final Player c, final int i, final Damage d) {
		if (PlayerHandler.players[i] != null) {
			Player o = PlayerHandler.players[i];
			if (o == null || o.isDead || c.isDead || PlayerHandler.players[i].getHealth().getAmount() <= 0 || c.getHealth().getAmount() <= 0) {
				c.playerIndex = 0;
				return;
			}
			if (o.respawnTimer > 0) {
				c.faceUpdate(0);
				c.playerIndex = 0;
				return;
			}
			if (d.getSpecial() != null) {
				d.getSpecial().hit(c, o, d);
			}

			if (d.getCombatType() == CombatType.MELEE) {
				if (Server.getEventHandler().isRunning(o, "staff_of_the_dead")) {
					Special special = Specials.STAFF_OF_THE_DEAD.getSpecial();
					special.hit(o, c, d);
				}
			}

			int damage = d.getAmount();
			o.getPA().removeAllWindows();
			if (o.playerIndex <= 0 && o.npcIndex <= 0) {
				if (o.autoRet == 1) {
					o.playerIndex = c.getIndex();
					o.followId = c.getIndex();
				}
			}
			if (c.hasOverloadBoost) {
				c.getPotions().resetOverload();
			}
			if (o.attackTimer <= 3 || o.attackTimer == 0 && o.playerIndex == 0 && !c.usingMagic) { // block
																									// animation
				o.startAnimation(o.getCombat().getBlockEmote());
			}
			if (c.projectileStage == 0 && !c.usingMagic) { // melee hit damage
				c.getCombat().applyPlayerHit(c, i, d);
			}
			DamageEffect effect = new AmuletOfTheDamnedDharokEffect();
			if (effect.isExecutable(o)) {
				effect.execute(c, o, d);
			}
			DamageEffect venom = new SerpentineHelmEffect();
			if (venom.isExecutable(o)) {
				venom.execute(c, o, new Damage(6));
			}
			c.getCombat().checkDemonItems();
			if (!c.usingMagic && c.projectileStage > 0) { // range hit damage
				c.rangeEndGFX = RangeData.getRangeEndGFX(c);
				DamageEffect karilEffect = new AmuletOfTheDamnedKarilEffect();
				if (karilEffect.isExecutable(c)) {
					karilEffect.execute(c, o, d);
				}
				if (o.vengOn) {
					c.getCombat().appendVengeance(i, damage);
				}
				if (damage > 0)
					c.getCombat().applyRecoil(damage, i);
				boolean dropArrows = true;
				if (c.lastWeaponUsed >= 4212 || c.lastWeaponUsed <= 4223 || Item.getItemName(c.playerEquipment[3]).contains("crystal bow")) {
					dropArrows = false;
				}
				for (int noArrowId : c.NO_ARROW_DROP) {
					if (c.lastWeaponUsed == noArrowId) {
						dropArrows = false;
						break;
					}
				}
				if (dropArrows) {
					c.getItems().dropArrowPlayer();
				}
				if (c.rangeEndGFX > 0 && !c.getCombat().usingBolts(c.lastArrowUsed)) {
					if (c.rangeEndGFXHeight) {
						o.gfx100(c.rangeEndGFX);
						c.inSpecMode = false;
					} else {
						o.gfx0(c.rangeEndGFX);
						c.inSpecMode = false;
					}
				}
				if (c.dbowSpec) {
					o.gfx100(Arrow.matchesMaterial(c.lastArrowUsed, Arrow.DRAGON) ? 1100 : 1103);
					c.dbowSpec = false;
				}
				if (damage > PlayerHandler.players[i].getHealth().getAmount()) {
					damage = PlayerHandler.players[i].getHealth().getAmount();
				}
				o.underAttackBy = c.getIndex();
				o.logoutDelay = System.currentTimeMillis();
				o.singleCombatDelay = System.currentTimeMillis();
				o.killerId = c.getIndex();
				o.appendDamage(damage, d.getHitmark());
				o.addDamageTaken(c, damage);
				c.killedBy = PlayerHandler.players[i].getIndex();
				c.ignoreDefence = false;
				o.updateRequired = true;
				c.getCombat().applySmite(i, damage);
			} else if (c.projectileStage > 0) {
				if (c.spellSwap) {
					c.spellSwap = false;
					c.setSidebarInterface(6, 16640);
					c.playerMagicBook = 2;
					c.gfx0(-1);
				}
				if (o.vengOn)
					c.getCombat().appendVengeance(i, damage);
				if (damage > 0)
					c.getCombat().applyRecoil(damage, i);
				if (c.getCombat().getEndGfxHeight() == 100 && !c.magicFailed) { // end
					PlayerHandler.players[i].gfx100(MagicData.MAGIC_SPELLS[c.oldSpellId][5]);
				} else if (!c.magicFailed) {
					PlayerHandler.players[i].gfx0(MagicData.MAGIC_SPELLS[c.oldSpellId][5]);
				} else if (c.magicFailed) {
					PlayerHandler.players[i].gfx100(85);
				}
				if (!c.magicFailed) {
					if (System.currentTimeMillis() - PlayerHandler.players[i].reduceStat > 35000) {
						PlayerHandler.players[i].reduceStat = System.currentTimeMillis();
						switch (MagicData.MAGIC_SPELLS[c.oldSpellId][0]) {
						case 12987:
						case 13011:
						case 12999:
						case 13023:
							PlayerHandler.players[i].playerLevel[0] -= ((o.getPA().getLevelForXP(PlayerHandler.players[i].playerXP[0]) * 10) / 100);
							break;
						}
					}

					switch (MagicData.MAGIC_SPELLS[c.oldSpellId][0]) {

					case 12901:
					case 12919: // blood spells
					case 12911:
					case 12929:
						int heal = damage / 4;
						c.getHealth().increase(heal);
						break;

					case 1153:
						PlayerHandler.players[i].playerLevel[0] -= ((o.getPA().getLevelForXP(PlayerHandler.players[i].playerXP[0]) * 5) / 100);
						o.sendMessage("Your attack level has been reduced!");
						PlayerHandler.players[i].reduceSpellDelay[c.reduceSpellId] = System.currentTimeMillis();
						o.getPA().refreshSkill(0);
						break;

					case 1157:
						o.playerLevel[2] -= ((o.getPA().getLevelForXP(PlayerHandler.players[i].playerXP[2]) * 5) / 100);
						o.sendMessage("Your strength level has been reduced!");
						o.reduceSpellDelay[c.reduceSpellId] = System.currentTimeMillis();
						o.getPA().refreshSkill(2);
						break;

					case 1161:
						o.playerLevel[1] -= ((o.getPA().getLevelForXP(PlayerHandler.players[i].playerXP[1]) * 5) / 100);
						o.sendMessage("Your defence level has been reduced!");
						o.reduceSpellDelay[c.reduceSpellId] = System.currentTimeMillis();
						o.getPA().refreshSkill(1);
						break;

					case 1542:
						o.playerLevel[1] -= ((o.getPA().getLevelForXP(PlayerHandler.players[i].playerXP[1]) * 10) / 100);
						o.sendMessage("Your defence level has been reduced!");
						o.reduceSpellDelay[c.reduceSpellId] = System.currentTimeMillis();
						o.getPA().refreshSkill(1);
						break;

					case 1543:
						o.playerLevel[2] -= ((o.getPA().getLevelForXP(PlayerHandler.players[i].playerXP[2]) * 10) / 100);
						o.sendMessage("Your strength level has been reduced!");
						PlayerHandler.players[i].reduceSpellDelay[c.reduceSpellId] = System.currentTimeMillis();
						o.getPA().refreshSkill(2);
						break;

					case 1562:
						o.playerLevel[0] -= ((o.getPA().getLevelForXP(PlayerHandler.players[i].playerXP[0]) * 10) / 100);
						o.sendMessage("Your attack level has been reduced!");
						PlayerHandler.players[i].reduceSpellDelay[c.reduceSpellId] = System.currentTimeMillis();
						o.getPA().refreshSkill(0);
						break;
					}
				}
				DamageEffect tridentOfTheSwampEffect = new TridentOfTheSwampEffect();
				if (tridentOfTheSwampEffect.isExecutable(c)) {
					tridentOfTheSwampEffect.execute(c, NPCHandler.npcs[i], new Damage(6));
				}
				if (damage > PlayerHandler.players[i].getHealth().getAmount()) {
					damage = PlayerHandler.players[i].getHealth().getAmount();
				}
				o.logoutDelay = System.currentTimeMillis();
				o.underAttackBy = c.getIndex();
				o.killerId = c.getIndex();
				o.singleCombatDelay = System.currentTimeMillis();
				if (c.getCombat().magicMaxHit() != 0) {
					o.addDamageTaken(c, damage);
					if (!c.magicFailed) {
						o.appendDamage(damage, damage > 0 ? Hitmark.HIT : Hitmark.MISS);
					}
				}
				c.getCombat().applySmite(i, damage);
				c.killedBy = PlayerHandler.players[i].getIndex();
				o.updateRequired = true;
				c.usingMagic = false;
				if (o.inMulti() && c.getCombat().multis()) {
					c.barrageCount = 0;
					for (int j = 0; j < PlayerHandler.players.length; j++) {
						if (PlayerHandler.players[j] != null) {
							if (j == o.getIndex())
								continue;
							if (c.barrageCount >= 9)
								break;
							if (o.goodDistance(o.getX(), o.getY(), PlayerHandler.players[j].getX(), PlayerHandler.players[j].getY(), 1))
								c.getCombat().appendMultiBarrage(j, c.magicFailed);
						}
					}
				}
				c.getPA().refreshSkill(6);
				c.oldSpellId = 0;
			}
			c.getCombat().checkVenomousItems();
		}
		c.getCombat().checkDemonItems();
		Degrade.degrade(c);
		c.getPA().requestUpdates();
		if (c.bowSpecShot <= 0) {
			c.oldPlayerIndex = 0;
			c.projectileStage = 0;
			c.lastWeaponUsed = 0;
			c.doubleHit = false;
			c.usingClaws = false;
			c.bowSpecShot = 0;
		}
		if (c.bowSpecShot != 0) {
			c.bowSpecShot = 0;
		}
	}

	public static void calculateCombatDamage(Player attacker, Player defender, CombatType combatType, Special special) {
		int maximumAccuracy = 0;
		int maximumDamage = 0;
		int damage = 0;
		int damage2 = -1;
		int defence = 0;
		Hitmark hitmark1 = null;
		Hitmark hitmark2 = null;
		int accuracy = 0;
		if (Objects.nonNull(attacker) && Objects.nonNull(defender)) {
			if (combatType.equals(CombatType.MELEE)) {
				maximumDamage = attacker.getCombat().calculateMeleeMaxHit();
				maximumAccuracy = attacker.getCombat().calculateMeleeAttack();
				if (attacker.debugMessage)
					attacker.sendMessage("Max Melee hit: "+maximumDamage);
				if (special != null) {
					maximumAccuracy *= special.getAccuracy();
					maximumDamage *= special.getDamageModifier();
				}
				damage = Misc.random(maximumDamage);
				accuracy = maximumAccuracy;
				defence = 10 + defender.getCombat().calculateMeleeDefence();
				boolean veracsEffect = false;
				if (attacker.getPA().fullVeracs()) {
					if (Misc.random(4) == 1) {
						veracsEffect = true;
					}
				}
				if (defender.playerEquipment[defender.playerShield] == 12817) {
					if (Misc.random(100) > 30 && damage > 0) {
						damage *= .75;
					}
				}
				if (EquipmentSet.TORAG.isWearingBarrows(defender) && defender.getItems().isWearingItem(12853)) {
					defence += defender.getLevelForXP(defender.playerXP[defender.playerHitpoints]) - defender.playerLevel[defender.playerHitpoints];
				}
				if (Misc.random(defence) > Misc.random(accuracy) && !veracsEffect) {
					damage = 0;
				}
				if (attacker.doubleHit) {
					damage2 = Misc.random(attacker.getCombat().calculateMeleeMaxHit());
				}
				if (defender.prayerActive[18] && !veracsEffect) {
					damage = damage * 60 / 100;
					if (damage2 > 0) {
						damage2 = damage2 * 60 / 100;
					}
				}
				hitmark1 = damage > 0 ? Hitmark.HIT : Hitmark.MISS;
				hitmark2 = damage2 > 0 ? Hitmark.HIT : Hitmark.MISS;
				if (attacker.getMode().isPVPCombatExperienceGained()) {
					if (!(special instanceof Shove)) {
						AttackPlayer.addCombatXP(attacker, CombatType.MELEE, damage + (damage2 > 0 ? damage2 : 0));
					}
				}
			} else if (combatType.equals(CombatType.RANGE)) {
				maximumDamage = attacker.getCombat().rangeMaxHit();
				maximumAccuracy = attacker.getCombat().calculateRangeAttack();
				if (attacker.debugMessage)
					attacker.sendMessage("Max Range hit: "+maximumDamage);
				if (special != null) {
					maximumAccuracy *= special.getAccuracy();
					maximumDamage *= special.getDamageModifier();
				}
				damage = Misc.random(maximumDamage);
				accuracy = maximumAccuracy;
				defence = defender.getCombat().calculateRangeDefence();
				if (EquipmentSet.TORAG.isWearing(defender) && defender.getItems().isWearingItem(12853)) {
					defence += defender.getLevelForXP(defender.playerXP[defender.playerHitpoints]) - defender.playerLevel[defender.playerHitpoints];
				}
				if (Misc.random(defence) > Misc.random(accuracy) && !attacker.ignoreDefence) {
					damage = 0;
				}
				if (attacker.lastWeaponUsed == 11235 || attacker.lastWeaponUsed == 12765 || attacker.lastWeaponUsed == 12766 || attacker.lastWeaponUsed == 12767
						|| attacker.lastWeaponUsed == 12768 || attacker.bowSpecShot == 1) {
					damage2 = Misc.random(maximumDamage);
					if (Misc.random(defence) > Misc.random(accuracy)) {
						damage2 = 0;
					}
				}
				if (RangeExtras.wearingCrossbow(attacker) && RangeExtras.wearingBolt(attacker)) {
					damage = RangeExtras.executeBoltSpecial(attacker, defender, new Damage(damage));
				}
				DamageEffect venomEffect = new ToxicBlowpipeEffect();
				if (venomEffect.isExecutable(attacker)) {
					venomEffect.execute(attacker, defender, new Damage(6));
				}
				if (attacker.dbowSpec) {
					if (damage < 8) {
						damage = 8;
					}
					if (damage2 < 8) {
						damage2 = 8;
					}
				}
				if (defender.prayerActive[17]) {
					damage = damage * 60 / 100;
					if (attacker.lastWeaponUsed == 11235 || attacker.lastWeaponUsed == 12765 || attacker.lastWeaponUsed == 12766 || attacker.lastWeaponUsed == 12767
							|| attacker.lastWeaponUsed == 12768 || attacker.bowSpecShot == 1)
						damage2 = damage2 * 60 / 100;
				}
				if (defender.getHealth().getAmount() - damage < 0) {
					damage = defender.getHealth().getAmount();
				}
				if (defender.getHealth().getAmount() - damage - damage2 < 0) {
					damage2 = defender.getHealth().getAmount() - damage;
				}
				if (damage < 0)
					damage = 0;
				if (damage2 < 0 && damage2 != -1)
					damage2 = 0;
				hitmark1 = damage > 0 ? Hitmark.HIT : Hitmark.MISS;
				hitmark2 = damage2 > 0 ? Hitmark.HIT : Hitmark.MISS;
				if (attacker.getMode().isPVPCombatExperienceGained()) {
					addCombatXP(attacker, CombatType.RANGE, damage + (damage2 > 0 ? damage2 : 0));
				}
			} else if (combatType.equals(CombatType.MAGE)) {
				maximumDamage = attacker.getCombat().magicMaxHit();
				maximumAccuracy = attacker.getCombat().mageAtk();
				if (attacker.debugMessage)
					attacker.sendMessage("Max Magic hit: "+maximumDamage);
				if (special != null) {
					maximumAccuracy *= special.getAccuracy();
					maximumDamage *= special.getDamageModifier();
				}
				damage = Misc.random(maximumDamage);
				accuracy = maximumAccuracy;
				defence = defender.getCombat().mageDef();
				if (attacker.fullVoidMage() && attacker.playerEquipment[attacker.playerWeapon] == 8841) {
					damage = Misc.random(maximumDamage + 13);
				}
				if (damage > 0 && EquipmentSet.AHRIM.isWearing(attacker) && attacker.getItems().isWearingItem(12853) && Misc.random(100) < 30) {
					damage = Misc.random((int) (maximumDamage * 1.2));
				}
				if (attacker.getCombat().godSpells()) {
					if (System.currentTimeMillis() - attacker.godSpellDelay < Config.GOD_SPELL_CHARGE) {
						damage += 10;
					}
				}
				if (Misc.random(accuracy) < Misc.random(defence)) {
					damage = 0;
					attacker.magicFailed = true;
				} else {
					attacker.magicFailed = false;
				}
				if (damage == 0 && MagicData.MAGIC_SPELLS[attacker.oldSpellId][6] != 0) {
					attacker.magicFailed = true;
				}
				if (attacker.magicFailed) {
					damage = 0;
				}
				if (defender.prayerActive[16]) {
					damage = damage * 60 / 100;
				}
				if (defender.getHealth().getAmount() - damage < 0) {
					damage = defender.getHealth().getAmount();
				}
				if (MagicData.MAGIC_SPELLS[attacker.oldSpellId][0] == 1191 && !attacker.magicFailed) {
					attacker.getDiaryManager().getWildernessDiary().progress(WildernessDiaryEntry.CLAWS_OF_GUTHIX);
				}
				if (MagicData.MAGIC_SPELLS[attacker.oldSpellId][0] == 12445 && !attacker.magicFailed) {
					if (System.currentTimeMillis() - defender.teleBlockDelay > defender.teleBlockLength) {
						defender.teleBlockDelay = System.currentTimeMillis();
						defender.sendMessage("You have been teleblocked.");
						if (defender.prayerActive[16] && System.currentTimeMillis() - defender.protMageDelay > 1500) {
							defender.teleBlockLength = 150000;
							defender.getPA().sendGameTimer(ClientGameTimer.TELEBLOCK, TimeUnit.SECONDS, 150);
						} else {
							defender.teleBlockLength = 300000;
							defender.getPA().sendGameTimer(ClientGameTimer.TELEBLOCK, TimeUnit.MINUTES, 5);
						}
					}
				}
				int freezeDelay = attacker.getCombat().getFreezeTime();
				if (freezeDelay > 0 && defender.freezeTimer <= -3 && !attacker.magicFailed) {
					defender.freezeTimer = freezeDelay;
					defender.resetWalkingQueue();
					defender.sendMessage("You have been frozen.");
					defender.frozenBy = attacker.getIndex();
				}
				if (attacker.magicDef) {
					attacker.getPA().addSkillXP((damage * (attacker.getMode().getType().equals(ModeType.OSRS) ? 3 : Config.MELEE_EXP_RATE) / 3), 1, false);
					attacker.getPA().refreshSkill(1);
				}
				DamageEffect tsotdEffect = new ToxicStaffOfTheDeadEffect();
				if (tsotdEffect.isExecutable(attacker)) {
					tsotdEffect.execute(attacker, defender, new Damage(6));
				}
				hitmark1 = damage > 0 ? Hitmark.HIT : Hitmark.MISS;
				hitmark2 = damage2 > 0 ? Hitmark.HIT : Hitmark.MISS;
				if (attacker.getMode().isPVPCombatExperienceGained()) {
					addCombatXP(attacker, CombatType.MAGE, damage + (damage2 > 0 ? damage2 : 0));
				}
				attacker.getPA().refreshSkill(6);
			}
		}
		int delay = attacker.hitDelay;
		Damage hit1 = new Damage(defender, damage, delay, attacker.playerEquipment, hitmark1, combatType, special);
		if (special != null) {
			special.activate(attacker, defender, hit1);
		}
		int weapon = attacker.playerEquipment[attacker.playerWeapon];
		attacker.attackTimer = attacker.getCombat().getAttackDelay(ItemAssistant.getItemName(attacker.playerEquipment[attacker.playerWeapon]).toLowerCase());
		Optional<Integer> optional = PoisonedWeapon.getOriginal(attacker.playerEquipment[attacker.playerWeapon]);
		if ((optional.isPresent() && optional.get() == 1249 || attacker.getItems().isWearingItem(1249, attacker.playerWeapon)) && attacker.usingSpecial) {
			return;
		}
		
		if (weapon == 11824 || weapon == 11889) {
			if (attacker.usingSpecial) {
				return;
			}
		}
		attacker.getDamageQueue().add(hit1);
		if (damage2 > -1) {
			attacker.getDamageQueue().add(new Damage(defender, damage2, delay, attacker.playerEquipment, hitmark2, combatType, special));
		}
		delay += 1;
	}

	public static int[] simulateCombatDamage(Player attacker, Player defender, CombatType combatType, Special special) {
		int maximumAccuracy = 0;
		int maximumDamage = 0;
		int damage = 0;
		int damage2 = -1;
		int defence = 0;
		int accuracy = 0;
		if (Objects.nonNull(attacker) && Objects.nonNull(defender)) {
			if (combatType.equals(CombatType.MELEE)) {
				maximumDamage = attacker.getCombat().calculateMeleeMaxHit();
				maximumAccuracy = attacker.getCombat().calculateMeleeAttack();
				if (special != null) {
					maximumAccuracy *= special.getAccuracy();
					maximumDamage *= special.getDamageModifier();
				}
				damage = Misc.random(maximumDamage);
				accuracy = maximumAccuracy;
				defence = 10 + defender.getCombat().calculateMeleeDefence();
				boolean veracsEffect = false;
				if (attacker.getPA().fullVeracs()) {
					if (Misc.random(4) == 1) {
						veracsEffect = true;
					}
				}
				if (defender.playerEquipment[defender.playerShield] == 12817) {
					if (Misc.random(100) > 30 && damage > 0) {
						damage *= .75;
					}
				}
				if (EquipmentSet.TORAG.isWearingBarrows(defender) && defender.getItems().isWearingItem(12853)) {
					defence += defender.getLevelForXP(defender.playerXP[defender.playerHitpoints]) - defender.playerLevel[defender.playerHitpoints];
				}
				if (Misc.random(defence) > Misc.random(accuracy) && !veracsEffect) {
					damage = 0;
				}
				if (attacker.doubleHit) {
					damage2 = Misc.random(attacker.getCombat().calculateMeleeMaxHit());
				}
				if (defender.prayerActive[18] && !veracsEffect) {
					damage = damage * 60 / 100;
					if (damage2 > 0) {
						damage2 = damage2 * 60 / 100;
					}
				}
			} else if (combatType.equals(CombatType.RANGE)) {
				maximumDamage = attacker.getCombat().rangeMaxHit();
				maximumAccuracy = attacker.getCombat().calculateRangeAttack();
				if (special != null) {
					maximumAccuracy *= special.getAccuracy();
					maximumDamage *= special.getDamageModifier();
				}
				damage = Misc.random(maximumDamage);
				accuracy = maximumAccuracy;
				defence = defender.getCombat().calculateRangeDefence();
				if (EquipmentSet.TORAG.isWearing(defender) && defender.getItems().isWearingItem(12853)) {
					defence += defender.getLevelForXP(defender.playerXP[defender.playerHitpoints]) - defender.playerLevel[defender.playerHitpoints];
				}
				if (Misc.random(defence) > Misc.random(accuracy) && !attacker.ignoreDefence) {
					damage = 0;
				}
				if (attacker.lastWeaponUsed == 11235 || attacker.lastWeaponUsed == 12765 || attacker.lastWeaponUsed == 12766 || attacker.lastWeaponUsed == 12767
						|| attacker.lastWeaponUsed == 12768 || attacker.bowSpecShot == 1) {
					damage2 = Misc.random(maximumDamage);
					if (Misc.random(defence) > Misc.random(accuracy)) {
						damage2 = 0;
					}
				}
				@SuppressWarnings("unused")
				DamageEffect venomEffect = new ToxicBlowpipeEffect();
				if (attacker.dbowSpec) {
					if (damage < 8) {
						damage = 8;
					}
					if (damage2 < 8) {
						damage2 = 8;
					}
				}
				if (defender.prayerActive[17]) {
					damage = damage * 60 / 100;
					if (attacker.lastWeaponUsed == 11235 || attacker.lastWeaponUsed == 12765 || attacker.lastWeaponUsed == 12766 || attacker.lastWeaponUsed == 12767
							|| attacker.lastWeaponUsed == 12768 || attacker.bowSpecShot == 1)
						damage2 = damage2 * 60 / 100;
				}
				if (defender.getHealth().getAmount() - damage < 0) {
					damage = defender.getHealth().getAmount();
				}
				if (defender.getHealth().getAmount() - damage - damage2 < 0) {
					damage2 = defender.getHealth().getAmount() - damage;
				}
				if (damage < 0)
					damage = 0;
				if (damage2 < 0 && damage2 != -1)
					damage2 = 0;
			} else if (combatType.equals(CombatType.MAGE)) {
				maximumDamage = attacker.getCombat().magicMaxHit();
				maximumAccuracy = attacker.getCombat().mageAtk();
				if (special != null) {
					maximumAccuracy *= special.getAccuracy();
					maximumDamage *= special.getDamageModifier();
				}
				damage = Misc.random(maximumDamage);
				accuracy = maximumAccuracy;
				defence = defender.getCombat().mageDef();
				if (attacker.fullVoidMage() && attacker.playerEquipment[attacker.playerWeapon] == 8841) {
					damage = Misc.random(maximumDamage + 13);
				}
				if (damage > 0 && EquipmentSet.AHRIM.isWearing(attacker) && attacker.getItems().isWearingItem(12853) && Misc.random(100) < 30) {
					damage = Misc.random((int) (maximumDamage * 1.2));
				}
				if (attacker.getCombat().godSpells()) {
					if (System.currentTimeMillis() - attacker.godSpellDelay < Config.GOD_SPELL_CHARGE) {
						damage += 10;
					}
				}
				if (Misc.random(accuracy) < Misc.random(defence)) {
					damage = 0;
					attacker.magicFailed = true;
				} else {
					attacker.magicFailed = false;
				}
				if (damage == 0 && MagicData.MAGIC_SPELLS[attacker.oldSpellId][6] != 0) {
					attacker.magicFailed = true;
				}
				if (attacker.magicFailed) {
					damage = 0;
				}
				if (defender.prayerActive[16]) {
					damage = damage * 60 / 100;
				}
				if (defender.getHealth().getAmount() - damage < 0) {
					damage = defender.getHealth().getAmount();
				}
				if (MagicData.MAGIC_SPELLS[attacker.oldSpellId][0] == 12445 && !attacker.magicFailed) {
					if (System.currentTimeMillis() - defender.teleBlockDelay > defender.teleBlockLength) {
						defender.teleBlockDelay = System.currentTimeMillis();
						defender.sendMessage("You have been teleblocked.");
						if (defender.prayerActive[16] && System.currentTimeMillis() - defender.protMageDelay > 1500)
							defender.teleBlockLength = 150000;
						else
							defender.teleBlockLength = 300000;
					}
				}
				int freezeDelay = attacker.getCombat().getFreezeTime();
				if (freezeDelay > 0 && defender.freezeTimer <= -3 && !attacker.magicFailed) {
					defender.freezeTimer = freezeDelay;
					defender.resetWalkingQueue();
					defender.sendMessage("You have been frozen.");
					defender.frozenBy = attacker.getIndex();
				}
				if (attacker.magicDef) {
					attacker.getPA().addSkillXP((damage * (attacker.getMode().getType().equals(ModeType.OSRS) ? 3 : Config.MELEE_EXP_RATE) / 3), 1, false);
					attacker.getPA().refreshSkill(1);
				}
				DamageEffect tsotdEffect = new ToxicStaffOfTheDeadEffect();
				if (tsotdEffect.isExecutable(attacker)) {
					tsotdEffect.execute(attacker, defender, new Damage(6));
				}
				if (attacker.getMode().isPVPCombatExperienceGained()) {
					addCombatXP(attacker, CombatType.MAGE, damage + (damage2 > 0 ? damage2 : 0));
				}
				attacker.getPA().refreshSkill(6);
			}
		}
		int[] damageArray = { 0, 0 };
		Optional<Integer> optional = PoisonedWeapon.getOriginal(attacker.playerEquipment[attacker.playerWeapon]);
		if (optional.isPresent() && optional.get() == 1249 && attacker.usingSpecial) {
			return damageArray;
		}
		damageArray[0] = damage;
		if (damage2 > -1) {
			damageArray[1] = damage2;
		}
		return damageArray;
	}

	public static void resetSpells(Player c) {
		if (c.playerMagicBook == 0) {
			c.setSidebarInterface(6, 938); // modern
		}
		if (c.playerMagicBook == 1) {
			c.setSidebarInterface(6, 838); // ancient
		}
		if (c.playerMagicBook == 2) {
			c.setSidebarInterface(6, 29999); // lunar
		}
	}

	public static void attackPlayer(Player c, int i) {
		if (PlayerHandler.players[i] == null) {
			return;
		}
		if (c.playerEquipment[c.playerWeapon] == 12904 && c.usingSpecial) {
			c.usingSpecial=false;
			c.getItems().updateSpecialBar();
			c.getCombat().resetPlayerAttack();
			return;
		}
		Player o = PlayerHandler.players[i];
		if (System.currentTimeMillis() - c.lastSpear < 3000) {
			c.getCombat().resetPlayerAttack();
			return;
		}
		if (c.getBankPin().requiresUnlock()) {
			c.getBankPin().open(2);
			c.sendMessage("You cannot do this while in lock-down.");
			c.getCombat().resetPlayerAttack();
			return;
		}		
		if (o.getBankPin().requiresUnlock()) {
			c.sendMessage("You cannot do this while a player is in lock-down.");
			c.getCombat().resetPlayerAttack();
			return;
		}
		if (c.getInterfaceEvent().isActive()) {
			c.sendMessage("Please finish what you're doing.");
			c.getCombat().resetPlayerAttack();
			return;
		}
		if (c.getInterfaceEvent().isActive()) {
			c.sendMessage("This player needs to finish what they're doing.");
			c.getCombat().resetPlayerAttack();
			return;
		}
		if (c.playerEquipment[c.playerWeapon] == 4734 && c.playerEquipment[c.playerArrows] != 4740) {
			c.sendMessage("You must use bolt racks with the karil's crossbow.");
			c.getCombat().resetPlayerAttack();
			return;
		}
		if (c.isInvisible()) {
			c.sendMessage("You cannot attack another player whilst you are invisible.");
			c.getCombat().resetPlayerAttack();
			return;
		}
		if (o != null && o.isInvisible()) {
			c.sendMessage("You cannot attack another player whilst they are invisible.");
			c.getCombat().resetPlayerAttack();
			return;
		}
		if (c == o) {
			c.sendMessage("You cannot attack yourself, " + o.playerName + ".");
			c.getCombat().resetPlayerAttack();
			return;
		}
		if (Boundary.isIn(c, Boundary.RESOURCE_AREA) && !Boundary.isIn(o, Boundary.RESOURCE_AREA)
				|| Boundary.isIn(o, Boundary.RESOURCE_AREA) && !Boundary.isIn(c, Boundary.RESOURCE_AREA)) {
			c.sendMessage("You and your target must be both in or outside of the resource area.");
			c.getCombat().resetPlayerAttack();
			return;
		}
		if (c.playerEquipment[c.playerWeapon] == 11907 || c.playerEquipment[c.playerWeapon] == 12899) {
			c.sendMessage("You cannot attack another player with this staff.");
			return;
		}
		if (Boundary.isIn(c, Boundary.DUEL_ARENA)) {
			if (Boundary.isIn(o, Boundary.DUEL_ARENA)) {
				DuelSession session = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c, MultiplayerSessionType.DUEL);
				if (Objects.isNull(session)) {
					c.sendMessage("You cannot attack this player.");
					c.getCombat().resetPlayerAttack();
					return;
				}
				if (!session.getPlayers().containsAll(Arrays.asList(o, c))) {
					c.sendMessage("This player is not your opponent.");
					c.getCombat().resetPlayerAttack();
					return;
				}
				if (session instanceof DuelSession) {
					if (!Boundary.isInSameBoundary(c, session.getOther(c), Boundary.DUEL_ARENA)) {
						c.sendMessage("You cannot attack a player if you're not in the same arena.");
						c.getPA().movePlayer(session.getArenaBoundary().getMinimumX(), session.getArenaBoundary().getMinimumX(), 0);
						c.getCombat().resetPlayerAttack();
						return;
					}
					if (!session.isAttackingOperationable()) {
						c.sendMessage("You cannot attack your opponent yet.");
						c.getCombat().resetPlayerAttack();
						return;
					}
				}
			} else {
				c.sendMessage("You cannot attack a player outside of the duel arena.");
				c.getCombat().resetPlayerAttack();
				return;
			}
		}
		if (c.hasOverloadBoost) {
			c.sendMessage("<col=CC0000>You cannot use overload effects against another player.</col>");
			c.getPotions().resetOverload();
			for (int skillId = 0; skillId < 7; skillId++) {
				if (skillId == 3 || skillId == 5)
					continue;
				c.getPotions().enchanceStat(skillId, true);
			}
			c.getCombat().resetPlayerAttack();
			return;
		}
		if (c.checkCombatDistance(c, o)) {
			resetSpells(c);
			if (c.playerIndex >= PlayerHandler.players.length || c.playerIndex < 0) {
				return;
			}
			if (PlayerHandler.players[i] != null) {
				c.getCombat().strBonus = c.playerBonus[10];
				if (PlayerHandler.players[i].isDead) {
					c.getCombat().resetPlayerAttack();
					return;
				}
				if (c.teleTimer > 0) {
					c.getCombat().resetPlayerAttack();
					return;
				}
				Player c2 = PlayerHandler.players[i];
				if (c.connectedFrom.equals(c2.connectedFrom) && !c.getRights().isOrInherits(Right.MODERATOR) && Server.serverlistenerPort != 5555) {
					c.sendMessage("You cannot attack same ip address.");
					c.getCombat().resetPlayerAttack();
					return;
				}
				if (c.playerEquipment[c.playerWeapon] == 9703) {
					c.sendMessage("You cannot attack players with training sword.");
					return;
				}
				if (c.getItems().isWearingAnyItem(4212, 4213, 4214, 4215, 4216, 4217, 4218, 4219, 4220, 4221, 4222, 4223) && c.playerEquipment[c.playerArrows] > -1) {
					c.sendMessage("You cannot use any arrows with this bow.");
					c.getCombat().resetPlayerAttack();
					return;
				}
				if (c.respawnTimer > 0 || PlayerHandler.players[i].respawnTimer > 0) {
					c.getCombat().resetPlayerAttack();
					return;
				}
				if (!c.getCombat().checkReqs()) {
					return;
				}
				boolean sameSpot = c.absX == PlayerHandler.players[i].getX() && c.absY == PlayerHandler.players[i].getY();
				if (!c.goodDistance(PlayerHandler.players[i].getX(), PlayerHandler.players[i].getY(), c.getX(), c.getY(), 25) && !sameSpot) {
					c.getCombat().resetPlayerAttack();
					return;
				}
				if (PlayerHandler.players[i].respawnTimer > 0) {
					PlayerHandler.players[i].playerIndex = 0;
					c.getCombat().resetPlayerAttack();
					return;
				}

				if (PlayerHandler.players[i].heightLevel != c.heightLevel) {
					c.getCombat().resetPlayerAttack();
					return;
				}
				c.followId = i;
				c.followId2 = 0;
				if (c.attackTimer <= 0) {
					c.usingBow = false;
					c.specEffect = 0;
					c.usingRangeWeapon = false;
					c.rangeItemUsed = 0;
					c.usingArrows = false;
					c.usingOtherRangeWeapons = false;
					c.usingCross = c.playerEquipment[c.playerWeapon] == 4734 || c.playerEquipment[c.playerWeapon] == 9185 || c.playerEquipment[c.playerWeapon] == 11785 || c.playerEquipment[c.playerWeapon] == 21012;
					c.usingBallista = c.playerEquipment[c.playerWeapon] == 19481 || c.playerEquipment[c.playerWeapon] == 19478;
					c.projectileStage = 0;
					CombatType combatType;
					if (c.absX == PlayerHandler.players[i].absX && c.absY == PlayerHandler.players[i].absY) {
						if (c.freezeTimer > 0) {
							c.getCombat().resetPlayerAttack();
							return;
						}
						c.followId = i;
						c.attackTimer = 0;
						return;
					}
					if (c.getItems().isWearingItem(12931) || c.getItems().isWearingItem(13197) || c.getItems().isWearingItem(13199)) {
						if (c.getSerpentineHelmCharge() <= 0) {
							c.sendMessage("Your serpentine helm has no charge, you need to recharge it.");
							c.getCombat().resetPlayerAttack();
							return;
						}
					}
					if (c.usingMagic) {
						c.usingCross = false;
						c.usingBallista = false;
					}
					if (!c.usingMagic) {
						for (int bowId : c.BOWS) {
							if (c.playerEquipment[c.playerWeapon] == bowId && System.currentTimeMillis() - c.switchDelay >= 600) {
								c.usingBow = true;
								if (bowId == 19481 || bowId == 19478) {
									c.usingBow = false;
									c.usingBallista = true;
								}
								c.rangeDelay = 3;
								for (int arrowId : c.ARROWS) {
									if (c.playerEquipment[c.playerArrows] == arrowId) {
										c.usingArrows = true;
									}
								}
							}
						}

						for (int otherRangeId : c.OTHER_RANGE_WEAPONS) {
							if (c.playerEquipment[c.playerWeapon] == otherRangeId) {
								c.usingOtherRangeWeapons = true;
							}
						}

						if (c.getItems().isWearingItem(12926)) {
							if (c.getToxicBlowpipeAmmo() == 0 || c.getToxicBlowpipeAmmoAmount() == 0 || c.getToxicBlowpipeCharge() == 0) {
								c.sendMessage("Your blowpipe is out of ammo or charge.");
								c.getCombat().resetPlayerAttack();
								return;
							}
							c.usingBow = true;
							c.usingArrows = true;
						}
					}
					if (c.autocasting) {
						c.spellId = c.autocastId;
						c.usingMagic = true;
						if (MagicData.MAGIC_SPELLS[c.spellId][0] >= 12861 && MagicData.MAGIC_SPELLS[c.spellId][0] <= 13023) {
							if (c.playerEquipment[c.playerWeapon] != 4675 && c.playerEquipment[c.playerWeapon] != 6914 && c.playerEquipment[c.playerWeapon] != 21006 && c.playerEquipment[c.playerWeapon] != 12904) {
								c.sendMessage("You cannot autocast with your current weapon.");
								c.getCombat().resetPlayerAttack();
								c.getPA().resetAutocast();
								c.stopMovement();
								return;
							}
						}
					}
					if (c.spellId > 0) {
						c.usingMagic = true;
						c.usingRangeWeapon = false;
						c.usingArrows = false;
						c.usingOtherRangeWeapons = false;
						c.usingCross = false;
						c.usingBallista = false;
						c.usingBow = false;
					}
					if (c.clanWarRule[2] && (c.usingBow || c.usingOtherRangeWeapons)) {
						c.sendMessage("You are not allowed to use ranged during this war!");
						return;
					}

					if (c.clanWarRule[0] && (!c.usingBow && !c.usingOtherRangeWeapons && !c.usingMagic)) {
						c.sendMessage("You are not allowed to use melee during this war!");
						return;
					}

					if (c.clanWarRule[1] && c.usingMagic) {
						c.sendMessage("You are not allowed to use magic during this war!");
						c.getCombat().resetPlayerAttack();
						return;
					}
					if (Boundary.isIn(c, Boundary.DUEL_ARENA)) {
						DuelSession session = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c, MultiplayerSessionType.DUEL);
						if (!Objects.isNull(session)) {
							if (session.getRules().contains(Rule.NO_RANGE) && (c.usingBow || c.usingOtherRangeWeapons)) {
								c.sendMessage("<Range has been disabled in this duel!");
								c.getCombat().resetPlayerAttack();
								return;
							}
							if (session.getRules().contains(Rule.NO_MELEE) && (!c.usingBow && !c.usingOtherRangeWeapons && !c.usingMagic)) {
								c.sendMessage("<col=CC0000>Melee has been disabled in this duel!");
								c.getCombat().resetPlayerAttack();
								return;
							}
							if (session.getRules().contains(Rule.NO_MAGE) && c.usingMagic) {
								c.sendMessage("<col=CC0000>Magic has been disabled in this duel!");
								c.getCombat().resetPlayerAttack();
								return;
							}
							if (session.getRules().contains(Rule.WHIP_AND_DDS)) {
								String weaponName = ItemAssistant.getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase();
								if (!weaponName.contains("whip") && !weaponName.contains("dragon dagger") || weaponName.contains("tentacle")) {
									c.sendMessage("<col=CC0000>You can only use a whip and dragon dagger in this duel.");
									c.getCombat().resetPlayerAttack();
									return;
								}
							}
							if (session.getRules().contains(Rule.NO_HELM)) {
								if (c.playerEquipment[c.playerHat] > -1) {
									c.sendMessage("<col=CC0000>Wearing helmets has been disabled for this duel.");
									c.getCombat().resetPlayerAttack();
									return;
								}
							}
							if (session.getRules().contains(Rule.NO_AMULET)) {
								if (c.playerEquipment[c.playerAmulet] > -1) {
									c.sendMessage("<col=CC0000>Wearing amulets has been disabled for this duel.");
									c.getCombat().resetPlayerAttack();
									return;
								}
							}
							if (session.getRules().contains(Rule.NO_ARROWS)) {
								if (c.playerEquipment[c.playerArrows] > -1) {
									c.sendMessage("<col=CC0000>Wearing arrows has been disabled for this duel.");
									c.getCombat().resetPlayerAttack();
									return;
								}
							}
							if (session.getRules().contains(Rule.NO_BODY)) {
								if (c.playerEquipment[c.playerChest] > -1) {
									c.sendMessage("Wearing platebodies has been disabled for this duel.");
									c.getCombat().resetPlayerAttack();
									return;
								}
							}
							if (session.getRules().contains(Rule.NO_BOOTS)) {
								if (c.playerEquipment[c.playerFeet] > -1) {
									c.sendMessage("<col=CC0000>Wearing boots has been disabled for this duel.");
									c.getCombat().resetPlayerAttack();
									return;
								}
							}
							if (session.getRules().contains(Rule.NO_GLOVES)) {
								if (c.playerEquipment[c.playerHands] > -1) {
									c.sendMessage("<col=CC0000>Wearing gloves has been disabled for this duel.");
									c.getCombat().resetPlayerAttack();
									return;
								}
							}
							if (session.getRules().contains(Rule.NO_CAPE)) {
								if (c.playerEquipment[c.playerCape] > -1) {
									c.sendMessage("<col=CC0000>Wearing capes has been disabled for this duel.");
									c.getCombat().resetPlayerAttack();
									return;
								}
							}
							if (session.getRules().contains(Rule.NO_LEGS)) {
								if (c.playerEquipment[c.playerLegs] > -1) {
									c.sendMessage("<col=CC0000>Wearing platelegs has been disabled for this duel.");
									c.getCombat().resetPlayerAttack();
									return;
								}
							}
							if (session.getRules().contains(Rule.NO_RINGS)) {
								if (c.playerEquipment[c.playerRing] > -1) {
									c.sendMessage("<col=CC0000>Wearing a ring has been disabled for this duel.");
									c.getCombat().resetPlayerAttack();
									return;
								}
							}
							if (session.getRules().contains(Rule.NO_WEAPON)) {
								if (c.playerEquipment[c.playerWeapon] > -1) {
									c.sendMessage("<col=CC0000>Wearing weapons has been disabled for this duel.");
									c.getCombat().resetPlayerAttack();
									return;
								}
							}
							if (session.getRules().contains(Rule.NO_SHIELD)) {
								if (c.playerEquipment[c.playerShield] > -1
										|| c.getItems().is2handed(ItemAssistant.getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase(), c.playerEquipment[c.playerWeapon])) {
									c.sendMessage("<col=CC0000>Wearing shields has been disabled for this duel.");
									c.getCombat().resetPlayerAttack();
									return;
								}
							}
						}
					}

					if ((!c.goodDistance(c.getX(), c.getY(), PlayerHandler.players[i].getX(), PlayerHandler.players[i].getY(), 4)
							&& (c.usingOtherRangeWeapons && !c.usingBow && !c.usingMagic))
							|| (!c.goodDistance(c.getX(), c.getY(), PlayerHandler.players[i].getX(), PlayerHandler.players[i].getY(), 2)
									&& (!c.usingOtherRangeWeapons && c.getCombat().usingHally() && !c.usingBow && !c.usingMagic))
							|| (!c.goodDistance(c.getX(), c.getY(), PlayerHandler.players[i].getX(), PlayerHandler.players[i].getY(), c.getCombat().getRequiredDistance())
									&& (!c.usingOtherRangeWeapons && !c.getCombat().usingHally() && !c.usingBow && !c.usingMagic))
							|| (!c.goodDistance(c.getX(), c.getY(), PlayerHandler.players[i].getX(), PlayerHandler.players[i].getY(), 10) && (c.usingBow || c.usingMagic))) {
						c.attackTimer = 1;
						if (!c.usingBow && !c.usingMagic && !c.usingOtherRangeWeapons && c.freezeTimer > 0) {
							c.getCombat().resetPlayerAttack();
							return;
						}
					}
					
					if (!c.usingBallista && !c.usingCross && !c.usingArrows && c.usingBow && (c.playerEquipment[c.playerWeapon] < 4212 || c.playerEquipment[c.playerWeapon] > 4223) && !c.usingMagic) {
						c.sendMessage("You have run out of arrows!");
						c.stopMovement();
						c.getCombat().resetPlayerAttack();
						return;
					}
					if (!c.getCombat().correctBowAndArrows() && Config.CORRECT_ARROWS && c.usingBow && !c.getCombat().usingCrystalBow() && c.playerEquipment[c.playerWeapon] != 4734
							&& c.playerEquipment[c.playerWeapon] != 9185 && c.playerEquipment[c.playerWeapon] != 11785 && c.playerEquipment[c.playerWeapon] != 21012 && !c.usingMagic && !c.getItems().isWearingItem(12926) && c.playerEquipment[c.playerWeapon] != 19478 && c.playerEquipment[c.playerWeapon] != 19481) {
						c.sendMessage("You can't use " + ItemAssistant.getItemName(c.playerEquipment[c.playerArrows]).toLowerCase() + "'s with a "
								+ ItemAssistant.getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase() + ".");
						c.stopMovement();
						c.getCombat().resetPlayerAttack();
						return;
					}

					if (c.playerEquipment[c.playerWeapon] == 9185 && !c.getCombat().properBolts() && !c.usingMagic
							|| c.playerEquipment[c.playerWeapon] == 11785 && !c.getCombat().properBolts() && !c.usingMagic || c.playerEquipment[c.playerWeapon] == 21012 && !c.getCombat().properBolts() && !c.usingMagic) {
						c.sendMessage("You must use bolts with a crossbow.");
						c.stopMovement();
						c.getCombat().resetPlayerAttack();
						return;
					}

					if (c.playerEquipment[c.playerWeapon] == 19478 && !c.getCombat().properJavelins() || c.playerEquipment[c.playerWeapon] == 19481 && !c.getCombat().properJavelins()) {
						c.sendMessage("You must use javelins with a ballista.");
						c.stopMovement();
						c.getCombat().resetPlayerAttack();
						return;
					}



					boolean projectile = c.usingOtherRangeWeapons || c.usingBow || c.usingMagic || c.getCombat().usingCrystalBow() || c.usingMelee;
					
					if (projectile) {
						if (!PathChecker.isProjectilePathClear(c.absX, c.absY, c.heightLevel, o.absX, o.absY) 
								&& !PathChecker.isProjectilePathClear(o.absX, o.absY, c.heightLevel, c.absX, c.absY)) {
							c.attackTimer = 1;
							return;
						} else {
							c.stopMovement();
						}
					}
					
					if (c.usingBow || c.usingMagic || c.usingOtherRangeWeapons || c.getCombat().usingHally()) {
						c.stopMovement();
					}

					if (!c.getCombat().checkMagicReqs(c.spellId)) {
						c.stopMovement();
						c.getCombat().resetPlayerAttack();
						return;
					}

					c.faceUpdate(i + 32768);
					if (!Boundary.isIn(c, Boundary.DUEL_ARENA)) {
						if (!c.attackedPlayers.contains(c.playerIndex) && !PlayerHandler.players[c.playerIndex].attackedPlayers.contains(c.getIndex())) {
							c.attackedPlayers.add(c.playerIndex);
							c.isSkulled = true;
							c.skullTimer = Config.SKULL_TIMER;
							c.headIconPk = 0;
							c.getPA().requestUpdates();
						}
					}
					if (c.usingBow || c.usingBallista || c.usingRangeWeapon || c.usingOtherRangeWeapons || c.usingCross || c.playerEquipment[c.playerWeapon] == 10034 || c.playerEquipment[c.playerWeapon] == 10033 || c.playerEquipment[c.playerWeapon] == 11959) {
						combatType = CombatType.RANGE;
					} else if (c.usingMagic) {
						combatType = CombatType.MAGE;
					} else {
						combatType = CombatType.MELEE;
					}
					c.specAccuracy = 1.0;
					c.specDamage = 1.0;
					c.delayedDamage = c.delayedDamage2 = 0;
					c.lastWeaponUsed = c.playerEquipment[c.playerWeapon];
					if (c.usingSpecial && !c.usingMagic) {
						if (Boundary.isIn(c, Boundary.DUEL_ARENA)) {
							DuelSession session = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c, MultiplayerSessionType.DUEL);
							if (Objects.nonNull(session)) {
								if (session.getRules().contains(Rule.NO_SPECIAL_ATTACK)) {
									c.sendMessage("Special attacks have been disabled during this duel!");
									c.usingSpecial = false;
									c.getItems().updateSpecialBar();
									c.getCombat().resetPlayerAttack();
									return;
								}
								Optional<Integer> optional = PoisonedWeapon.getOriginal(c.playerEquipment[c.playerWeapon]);
								if (optional.isPresent()) {
									int item = optional.get();
									if (item == 1249) {
										c.sendMessage("You cannot use this special attack whilst in the duel arena.");
										c.usingSpecial = false;
										c.getItems().updateSpecialBar();
										c.getCombat().resetPlayerAttack();
										return;
									}
								}
							}
						}
						Special special = Specials.forWeaponId(c.playerEquipment[c.playerWeapon]);
						if (special == null) {
							return;
						}
						if (special.getRequiredCost() > c.specAmount) {
							c.sendMessage("You don't have enough power left.");
							c.usingSpecial = false;
							c.getItems().updateSpecialBar();
							c.getItems().addSpecialBar(c.playerEquipment[c.playerWeapon]);
							c.playerIndex = 0;
							c.getCombat().resetPlayerAttack();
							return;
						}
						c.projectileStage = 0;
						c.logoutDelay = System.currentTimeMillis();
						c.lastArrowUsed = c.playerEquipment[c.playerArrows];
						c.followId = c.playerIndex;
						c.oldPlayerIndex = c.playerIndex;
						c.specAmount -= special.getRequiredCost();
						c.hitDelay = c.getCombat().getHitDelay(i, ItemAssistant.getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
						if (c.usingOtherRangeWeapons || c.usingBow) {
							if (c.fightMode == 2) {
								c.attackTimer--;
							}
						}
						calculateCombatDamage(c, PlayerHandler.players[i], combatType, special);
						c.usingSpecial = false;
						c.getItems().updateSpecialBar();
						c.getItems().addSpecialBar(c.playerEquipment[c.playerWeapon]);
						return;
					}
					if (c.getHealth().getAmount() > 0 && !c.isDead && PlayerHandler.players[i].getHealth().getAmount() > 0) {
						if (!c.usingMagic) {
							c.startAnimation(c.getCombat().getWepAnim(ItemAssistant.getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase()));
							c.mageFollow = false;
						} else {
							c.startAnimation(MagicData.MAGIC_SPELLS[c.spellId][2]);
							c.mageFollow = true;
							c.followId = c.playerIndex;
						}
					}
					c.logoutDelay = System.currentTimeMillis();
					Player target = PlayerHandler.players[i];
					target.underAttackBy = c.getIndex();
					target.logoutDelay = System.currentTimeMillis();
					target.singleCombatDelay = System.currentTimeMillis();
					target.killerId = c.getIndex();
					c.lastArrowUsed = 0;
					c.rangeItemUsed = 0;
					if (!c.usingBow && !c.usingMagic && !c.usingSpecial && !c.usingOtherRangeWeapons) { // melee
						c.followId = PlayerHandler.players[c.playerIndex].getIndex();
						c.getPA().followPlayer();
						c.hitDelay = c.getCombat().getHitDelay(i, ItemAssistant.getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
						c.projectileStage = 0;
						c.oldPlayerIndex = i;
					}

					if (c.usingBow && !c.usingOtherRangeWeapons && !c.usingMagic || c.usingCross || c.usingBallista) { // range
																									// hit
																									// delay
						if (c.playerEquipment[c.playerWeapon] >= 4212 && c.playerEquipment[c.playerWeapon] <= 4223) {
							c.rangeItemUsed = c.playerEquipment[c.playerWeapon];
							c.crystalBowArrowCount++;
						} else {
							c.rangeItemUsed = c.playerEquipment[c.playerArrows];
							c.getItems().deleteArrow();
						}
						if (c.usingCross)
						c.usingBow = true;
						c.inSpecMode = true;
						c.followId = PlayerHandler.players[c.playerIndex].getIndex();
						c.getPA().followPlayer();
						c.lastWeaponUsed = c.playerEquipment[c.playerWeapon];
						c.lastArrowUsed = c.playerEquipment[c.playerArrows];
						c.gfx100(c.getCombat().getRangeStartGFX());
						c.hitDelay = c.getCombat().getHitDelay(i, ItemAssistant.getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
						c.projectileStage = 1;
						c.oldPlayerIndex = i;
						c.getCombat().fireProjectilePlayer(0);
					}

					if (c.usingOtherRangeWeapons) { // knives, darts, etc hit
													// delay
						c.rangeItemUsed = c.playerEquipment[c.playerWeapon];
						c.getItems().deleteEquipment();
						c.usingRangeWeapon = true;
						c.followId = PlayerHandler.players[c.playerIndex].getIndex();
						c.getPA().followPlayer();
						c.gfx100(c.getCombat().getRangeStartGFX());
						c.hitDelay = c.getCombat().getHitDelay(i, ItemAssistant.getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
						c.projectileStage = 1;
						c.oldPlayerIndex = i;
						c.getCombat().fireProjectilePlayer(0);
					}

					if (c.usingMagic) { // magic hit delay
						int pX = c.getX();
						int pY = c.getY();
						int nX = PlayerHandler.players[i].getX();
						int nY = PlayerHandler.players[i].getY();
						int offX = (pY - nY) * -1;
						int offY = (pX - nX) * -1;
						c.projectileStage = 2;
						if (MagicData.MAGIC_SPELLS[c.spellId][3] > 0) {
							if (c.getCombat().getStartGfxHeight() == 100) {
								c.gfx100(MagicData.MAGIC_SPELLS[c.spellId][3]);
							} else {
								c.gfx0(MagicData.MAGIC_SPELLS[c.spellId][3]);
							}
						}
						if (MagicData.MAGIC_SPELLS[c.spellId][4] > 0) {
							c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, 78, MagicData.MAGIC_SPELLS[c.spellId][4], c.getCombat().getStartHeight(),
									c.getCombat().getEndHeight(), -i - 1, c.getCombat().getStartDelay());
						}
						if (c.autocastId > 0) {
							c.followId = c.playerIndex;
							c.followDistance = 5;
						} else {
							c.followId = 0;
							c.stopMovement();
						}
						c.hitDelay = c.getCombat().getHitDelay(i, ItemAssistant.getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
						c.oldPlayerIndex = i;
						c.oldSpellId = c.spellId;
						c.spellId = 0;
						if (MagicData.MAGIC_SPELLS[c.oldSpellId][0] == 12891 && target.isMoving) {
							c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, 85, 368, 25, 25, -i - 1, c.getCombat().getStartDelay());
						}
						// if (Misc.random(target.getCombat().mageDef()) >
						// Misc.random(c.getCombat().mageAtk())) {
						// c.magicFailed = true;
						// } else {
						// c.magicFailed = false;
						// }
						if (!c.autocasting && c.spellId <= 0)
							c.playerIndex = 0;
					}
					if (System.currentTimeMillis() - c.lastDamageCalculation > 1000) {
						calculateCombatDamage(c, PlayerHandler.players[i], combatType, null);
						c.lastDamageCalculation = System.currentTimeMillis();
					}
					if (c.usingOtherRangeWeapons || c.usingBow) {
						if (c.fightMode == 2)
							c.attackTimer--;
					}
					if (c.usingBow && Config.CRYSTAL_BOW_DEGRADES) { // crystal
																		// bow
																		// degrading
						if (c.playerEquipment[c.playerWeapon] == 4212) { // new
																			// crystal
																			// bow
																			// becomes
																			// full
																			// bow
																			// on
																			// the
																			// first
																			// shot
							c.getItems().wearItem(4214, 1, 3);
						}

						if (c.crystalBowArrowCount >= 250) {
							switch (c.playerEquipment[c.playerWeapon]) {

							case 4223: // 1/10 bow
								c.getItems().wearItem(-1, 1, 3);
								c.sendMessage("Your crystal bow has fully degraded.");
								if (!c.getItems().addItem(4207, 1)) {
									Server.itemHandler.createGroundItem(c, 4207, c.getX(), c.getY(), c.heightLevel, 1);
								}
								c.crystalBowArrowCount = 0;
								break;

							default:
								c.getItems().wearItem(++c.playerEquipment[c.playerWeapon], 1, 3);
								c.sendMessage("Your crystal bow degrades.");
								c.crystalBowArrowCount = 0;
								break;
							}
						}
					}
				}
			}
		}
	}
}
