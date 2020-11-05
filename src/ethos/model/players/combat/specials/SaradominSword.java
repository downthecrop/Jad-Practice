package ethos.model.players.combat.specials;

import ethos.model.entity.Entity;
import ethos.model.players.Player;
import ethos.model.players.combat.CombatType;
import ethos.model.players.combat.Damage;
import ethos.model.players.combat.Hitmark;
import ethos.model.players.combat.Special;
import ethos.util.Misc;

public class SaradominSword extends Special {

	public SaradominSword() {
		super(10.0, 1.30, 1.0, new int[] { 11838 });
	}

	@Override
	public void activate(Player player, Entity target, Damage damage) {
		player.startAnimation(1132);
		if (damage.getAmount() > 0) {
			player.getDamageQueue().add(new Damage(target, player.getCombat().magicMaxHit() + (1 + Misc.random(15)), 2, player.playerEquipment, Hitmark.HIT, CombatType.MAGE));
		}
	}

	@Override
	public void hit(Player player, Entity target, Damage damage) {

	}

}
