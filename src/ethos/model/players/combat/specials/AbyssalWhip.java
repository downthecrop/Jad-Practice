package ethos.model.players.combat.specials;

import ethos.model.entity.Entity;
import ethos.model.npcs.NPC;
import ethos.model.players.Player;
import ethos.model.players.combat.Damage;
import ethos.model.players.combat.Special;

public class AbyssalWhip extends Special {

	public AbyssalWhip() {
		super(5.0, 1.10, 1.1, new int[] { 4151, 12773, 12774 });
	}

	@Override
	public void activate(Player player, Entity target, Damage damage) {
		player.startAnimation(1658);
	}

	@Override
	public void hit(Player player, Entity target, Damage damage) {
		if (target instanceof NPC) {
			((NPC) target).gfx100(341);
		} else if (target instanceof Player) {
			((Player) target).gfx100(341);
			
			if (damage.getAmount() > 0) {
				if (((Player) target).getRunEnergy() > 0) {
					((Player) target).setRunEnergy(((Player) target).getRunEnergy() - 10);
				}
				if (!(player.getRunEnergy() > 89)) {
					player.setRunEnergy(player.getRunEnergy() + 10);
				}
			}
		}
	}

}
