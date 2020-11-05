package ethos.model.players.combat.specials;

import ethos.model.entity.Entity;
import ethos.model.players.Player;
import ethos.model.players.combat.Damage;
import ethos.model.players.combat.Special;
import ethos.model.players.skills.Skill;

public class DragonHarpoon extends Special {
	public DragonHarpoon() {
		super(10.0, 2.0, 2.0, new int[] { 21028 });
	}

	@Override
	public void activate(Player player, Entity target, Damage damage) {
		player.forcedChat("Here fishy fishies!");
		player.gfx0(246);
		player.playerLevel[Skill.FISHING.getId()] += 3;
		player.getPA().refreshSkill(Skill.FISHING.getId());
	}


	@Override
	public void hit(Player player, Entity target, Damage damage) {

	}
}
