package ethos.model.players.combat.effects.bolts;

import ethos.model.npcs.NPC;
import ethos.model.players.Player;
import ethos.model.players.combat.Damage;
import ethos.model.players.combat.DamageEffect;
import ethos.model.players.combat.range.RangeExtras;
import ethos.util.Misc;

public class DragonBoltSpecial implements DamageEffect {

	@Override
	public void execute(Player attacker, Player defender, Damage damage) {
		if (defender.antifireDelay > 0 || defender.getItems().isWearingAnyItem(11283, 11284, 1540)) {
			return;
		}
		int change = Misc.random((int) (damage.getAmount() * 1.45));
		damage.setAmount(change);
		RangeExtras.createCombatGraphic(attacker, defender, 756, false);
	}

	@Override
	public void execute(Player attacker, NPC defender, Damage damage) {
		if (defender.getDefinition().getNpcName() != null && defender.getDefinition().getNpcName().toLowerCase().contains("dragon")) {
			return;
		}
		int change = Misc.random((int) (damage.getAmount() * 1.45));
		damage.setAmount(change);
		RangeExtras.createCombatGraphic(attacker, defender, 756, false);
	}

	@Override
	public boolean isExecutable(Player operator) {
		return RangeExtras.boltSpecialAvailable(operator, 9244);
	}

}
