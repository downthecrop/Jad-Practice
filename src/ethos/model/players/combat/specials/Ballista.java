package ethos.model.players.combat.specials;

import ethos.model.entity.Entity;
import ethos.model.npcs.NPC;
import ethos.model.players.Player;
import ethos.model.players.combat.Damage;
import ethos.model.players.combat.Special;
import ethos.model.players.combat.range.RangeData;

public class Ballista extends Special {

	public Ballista() {
		super(6.5, 2.0, 1.5, new int[] { 19478, 19481 });
	}

	@Override
	public void activate(Player player, Entity target, Damage damage) {
		player.usingBow = true;
		player.rangeItemUsed = player.playerEquipment[player.playerArrows];
		player.startAnimation(7222);
		if (player.playerIndex > 0 && target instanceof Player) {
			RangeData.fireProjectilePlayer(player, (Player) target, 50, 70, player.getCombat().getRangeProjectileGFX(), 43, 31, 37, 10);
		} else if (player.npcIndex > 0 && target instanceof NPC) {
			RangeData.fireProjectileNpc(player, (NPC) target, 50, 70, player.getCombat().getRangeProjectileGFX(), 43, 31, 37, 10);
		}
	}

	@Override
	public void hit(Player player, Entity target, Damage damage) {

	}
}
