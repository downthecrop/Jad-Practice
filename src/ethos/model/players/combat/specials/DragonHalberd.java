package ethos.model.players.combat.specials;

import ethos.model.entity.Entity;
import ethos.model.npcs.NPC;
import ethos.model.players.Player;
import ethos.model.players.combat.AttackNPC;
import ethos.model.players.combat.CombatType;
import ethos.model.players.combat.Damage;
import ethos.model.players.combat.Special;

public class DragonHalberd extends Special {

	public DragonHalberd() {
		super(3.0, 1.15, 1.00, new int[] { 3204 });
	}

	@Override
	public void activate(Player player, Entity target, Damage damage) {
		player.gfx100(282);
		player.startAnimation(1203);
	}

	@Override
	public void hit(Player player, Entity target, Damage damage) {
		if (target instanceof NPC) {
			NPC other = (NPC) target;
			if (other != null && player.npcIndex > 0) {
				if (player.goodDistance(player.getX(), player.getY(), other.getX(), other.getY(), other.getSize()) && other.getSize() > 1) {
					AttackNPC.calculateCombatDamage(player, other, CombatType.MELEE, null);
				}
			}
		}
	}

}
