package ethos.model.players.combat.effects.bolts;

import ethos.model.npcs.NPC;
import ethos.model.players.Player;
import ethos.model.players.combat.Damage;
import ethos.model.players.combat.DamageEffect;
import ethos.model.players.combat.range.RangeExtras;
import ethos.util.Misc;

public class JadeBoltSpecial implements DamageEffect {

	@Override
	public void execute(Player attacker, Player defender, Damage damage) {
		int change = Misc.random((int) (damage.getAmount()));
		damage.setAmount(change);
		RangeExtras.createCombatGraphic(attacker, defender, 755, false);
		defender.freezeTimer = 33;
		// Find knocking down emote
	}

	@Override
	public void execute(Player attacker, NPC defender, Damage damage) {
		if (defender.getDefinition().getNpcName() == null) {
			return;
		}
		int change = Misc.random((int) (damage.getAmount()));
		damage.setAmount(change);
		RangeExtras.createCombatGraphic(attacker, defender, 755, false);
		defender.freezeTimer = 33;
	}

	@Override
	public boolean isExecutable(Player operator) {
		return RangeExtras.boltSpecialAvailable(operator, 9237);
	}

}
