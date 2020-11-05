package ethos.model.players.combat.specials;

import ethos.model.entity.Entity;
import ethos.model.players.Player;
import ethos.model.players.combat.Damage;
import ethos.model.players.combat.Special;

public class DragonLongsword extends Special {

	public DragonLongsword() {
		super(2.5, 1.25, 1.15, new int[] { 1305 });
	}

	@Override
	public void activate(Player player, Entity target, Damage damage) {
		player.gfx100(248);
		player.startAnimation(1058);
	}

	@Override
	public void hit(Player player, Entity target, Damage damage) {

	}
}