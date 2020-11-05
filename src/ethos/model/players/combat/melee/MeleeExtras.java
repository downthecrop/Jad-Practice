package ethos.model.players.combat.melee;

import java.util.Objects;

import ethos.Server;
import ethos.model.multiplayer_session.MultiplayerSessionType;
import ethos.model.multiplayer_session.duel.DuelSession;
import ethos.model.multiplayer_session.duel.DuelSessionRules.Rule;
import ethos.model.npcs.NPCHandler;
import ethos.model.players.Boundary;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.model.players.combat.AttackNPC;
import ethos.model.players.combat.Hitmark;
import ethos.util.Misc;

public class MeleeExtras {

	public static void applySmite(Player c, int index, int damage) {
		if (!c.prayerActive[23])
			return;
		if (damage <= 0)
			return;
		if (PlayerHandler.players[index] != null) {
			Player c2 = PlayerHandler.players[index];
			c2.playerLevel[5] -= damage / 4;
			if (c2.playerLevel[5] <= 0) {
				c2.playerLevel[5] = 0;
				c2.getCombat().resetPrayers();
			}
			c2.getPA().refreshSkill(5);
		}
	}

	public static void appendVengeanceNPC(Player c, int damage, int i) {
		if (damage <= 0)
			return;
		if (!c.vengOn)
			return;
		
		if (NPCHandler.npcs[i] != null) {
			c.forcedText = "Taste vengeance!";
			c.forcedChatUpdateRequired = true;
			c.updateRequired = true;
			c.vengOn = false;
			if ((NPCHandler.npcs[i].getHealth().getAmount() - damage) > 0) {
				damage = (int) (damage * 0.75);
				if (damage > NPCHandler.npcs[i].getHealth().getAmount()) {
					damage = NPCHandler.npcs[i].getHealth().getAmount();
				}
				NPCHandler.npcs[i].appendDamage(c, damage, damage > 0 ? Hitmark.HIT : Hitmark.MISS);
			}
		}
		c.updateRequired = true;
	}

	public static void appendVengeance(Player c, int otherPlayer, int damage) {
		if (damage <= 0)
			return;
		Player o = PlayerHandler.players[otherPlayer];
		o.forcedText = "Taste vengeance!";
		o.forcedChatUpdateRequired = true;
		o.updateRequired = true;
		o.vengOn = false;
		if ((o.getHealth().getAmount() - damage) > 0) {
			damage = (int) (damage * 0.75);
			c.appendDamage(damage, damage > 0 ? Hitmark.HIT : Hitmark.MISS);
			c.addDamageTaken(o, damage);
		}
		c.updateRequired = true;
	}

	public static void applyRecoilNPC(Player c, int damage, int i) {
		if (damage <= 0) 
			return;
		if (damage > 0 && c.playerEquipment[c.playerRing] == 2550) {
			int recDamage = damage / 10;
			if (recDamage < 1 && damage > 0) {
				recDamage = 1;
			}
			if (NPCHandler.npcs[i].getHealth().getAmount() <= 0 || NPCHandler.npcs[i].isDead) {
				return;
			}
			NPCHandler.npcs[i].appendDamage(c, recDamage, Hitmark.HIT);
			removeRecoil(c);
			c.recoilHits += recDamage;
		}
	}

	public static void applyRecoil(Player c, int damage, int i) {
		if (damage <= 0) 
			return;
		if (Boundary.isIn(c, Boundary.DUEL_ARENA)) {
			DuelSession session = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c, MultiplayerSessionType.DUEL);
			if (Objects.nonNull(session)) {
				if (session.getRules().contains(Rule.NO_RINGS)) {
					return;
				}
			}
		}
		if (damage > 0 && PlayerHandler.players[i].playerEquipment[c.playerRing] == 2550) {
			int recDamage = damage / 10 + 1;
			c.appendDamage(recDamage, recDamage > 0 ? Hitmark.HIT : Hitmark.MISS);
			c.addDamageTaken(PlayerHandler.players[i], recDamage);
			c.updateRequired = true;
			removeRecoil(c);
			c.recoilHits += damage;
		}
	}

	public static void removeRecoil(Player c) {
		if (c.recoilHits >= 200) {
			if (c.playerEquipment[c.playerRing] == 2550) {
				c.getItems().removeItem(2550, c.playerRing);
				c.getItems().deleteItem(2550, c.getItems().getItemSlot(2550), 1);
				c.sendMessage("Your ring of recoil shaters!");
				c.recoilHits = 0;
			}
		} else {
			c.recoilHits++;
		}
	}

	public static void graniteMaulSpecial(Player c) {
		if (c.playerIndex > 0 && c.getHealth().getAmount() > 0) {
			Player o = PlayerHandler.players[c.playerIndex];
			if (c.goodDistance(c.getX(), c.getY(), o.getX(), o.getY(), c.getCombat().getRequiredDistance())) {
				if (c.getCombat().checkReqs()) {
					if (c.getCombat().checkSpecAmount(4153) || c.getCombat().checkSpecAmount(12848)) {
						boolean hit = Misc.random(c.getCombat().calculateMeleeAttack()) > Misc.random(o.getCombat().calculateMeleeDefence());
						int damage = 0;
						if (hit)
							damage = Misc.random(c.getCombat().calculateMeleeMaxHit());
						if (o.prayerActive[18])
							damage *= .6;
						if (o.getHealth().getAmount() - damage <= 0) {
							damage = o.getHealth().getAmount();
						}
						if (o.getHealth().getAmount() > 0) {
							o.appendDamage(damage, damage > 0 ? Hitmark.HIT : Hitmark.MISS);
							c.startAnimation(1667);
							o.gfx100(337);
							o.addDamageTaken(c, damage);
						}
					}
				}
			}
		} else if (c.npcIndex > 0) {
			int x = NPCHandler.npcs[c.npcIndex].absX;
			int y = NPCHandler.npcs[c.npcIndex].absY;
			if (c.goodDistance(c.getX(), c.getY(), x, y, NPCHandler.npcs[c.npcIndex].getSize()) && AttackNPC.isAttackable(c, c.npcIndex)) {
				if (c.getCombat().checkSpecAmount(4153) || c.getCombat().checkSpecAmount(12848)) {
					int damage = Misc.random(c.getCombat().calculateMeleeMaxHit());
					if (NPCHandler.npcs[c.npcIndex].getHealth().getAmount() - damage < 0) {
						damage = NPCHandler.npcs[c.npcIndex].getHealth().getAmount();
					}
					if (NPCHandler.npcs[c.npcIndex].getHealth().getAmount() > 0) {
						NPCHandler.npcs[c.npcIndex].appendDamage(c, damage, Hitmark.HIT);
						c.startAnimation(1667);
						c.gfx100(337);
					}
				}
			}
		}
	}
}