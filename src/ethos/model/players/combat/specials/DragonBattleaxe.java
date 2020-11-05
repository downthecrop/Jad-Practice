package ethos.model.players.combat.specials;

import ethos.model.entity.Entity;
import ethos.model.players.Player;
import ethos.model.players.combat.Damage;
import ethos.model.players.combat.Special;

public class DragonBattleaxe extends Special {

	public DragonBattleaxe() {
		super(10.0, 1.0, 1.0, new int[] { 1377 });
	}

	@Override
	public void activate(Player player, Entity target, Damage damage) {
		int[] decreased = { player.playerAttack, player.playerDefence, player.playerRanged, player.playerMagic };
		int[] increased = { player.playerStrength };

		for (int skill : decreased) {
			player.playerLevel[skill] -= player.getPA().getLevelForXP(player.playerXP[skill]) * .1;
			if (player.playerLevel[skill] < 1) {
				player.playerLevel[skill] = 1;
			}
			player.getPA().refreshSkill(skill);
		}

		for (int skill : increased) {
			if (player.playerLevel[skill] > player.getPA().getLevelForXP(player.playerXP[skill])) {
				return;
			}
			player.playerLevel[skill] += player.getPA().getLevelForXP(player.playerXP[skill]) * .1;
			if (player.playerLevel[skill] > 120) {
				player.playerLevel[skill] = 120;
			}
			player.getPA().refreshSkill(skill);
		}

		player.attackTimer += 1;
		player.forcedChat("Raarrrrrgggggghhhhhhh!");
		player.gfx0(246);
		player.startAnimation(1056);
	}

	@Override
	public void hit(Player player, Entity target, Damage damage) {

	}

}
