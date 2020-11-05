package ethos.model.players.combat.effects.bolts;

import ethos.model.npcs.NPC;
import ethos.model.players.Player;
import ethos.model.players.combat.Damage;
import ethos.model.players.combat.DamageEffect;
import ethos.model.players.combat.Hitmark;
import ethos.model.players.combat.range.RangeExtras;
import ethos.util.Misc;

public class RubyBoltSpecial implements DamageEffect {

	@Override
	public void execute(Player attacker, Player defender, Damage damage) {
		int change = Misc.random((int) (defender.getHealth().getAmount() / 5));
		int playerDamage = attacker.getHealth().getAmount() / 10;
		if (attacker.getHealth().getAmount() < 10) {
			return;
		}
		attacker.addDamageTaken(attacker, playerDamage);
		damage.setAmount(change);
		RangeExtras.createCombatGraphic(attacker, defender, 754, false);
	}

	@Override
	public void execute(Player attacker, NPC defender, Damage damage) {
		if (defender.getDefinition().getNpcName() == null) {
			return;
		}
		int change = Misc.random((int) (defender.getHealth().getAmount() / 5));
		int playerDamage = attacker.getHealth().getAmount() / 10;
		if (attacker.getHealth().getAmount() < 10) {
			return;
		}
		attacker.appendDamage(playerDamage, Hitmark.HIT);
		if (change > 100) {
			change = 100;
		}
		damage.setAmount(change);
		RangeExtras.createCombatGraphic(attacker, defender, 754, false);
	}

	@Override
	public boolean isExecutable(Player operator) {
		return RangeExtras.boltSpecialAvailable(operator, 9242);
	}

}
