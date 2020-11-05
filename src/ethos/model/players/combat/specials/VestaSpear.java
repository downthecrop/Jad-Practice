package ethos.model.players.combat.specials;

import ethos.model.entity.Entity;
import ethos.model.players.Player;
import ethos.model.players.combat.Damage;
import ethos.model.players.combat.Special;

public class VestaSpear extends Special {

	public VestaSpear() {
		super(3.5, 1.25, 1.10, new int[] { 13905 });
	}

	@Override
	public void activate(Player player, Entity target, Damage damage) {
		player.gfx0(1240);
		player.startAnimation(7294);
	}

	@Override
	public void hit(Player player, Entity target, Damage damage) {

	}

}
