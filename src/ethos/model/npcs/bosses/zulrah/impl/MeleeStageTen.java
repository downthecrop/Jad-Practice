package ethos.model.npcs.bosses.zulrah.impl;

import ethos.event.CycleEventContainer;
import ethos.model.npcs.bosses.zulrah.Zulrah;
import ethos.model.npcs.bosses.zulrah.ZulrahLocation;
import ethos.model.npcs.bosses.zulrah.ZulrahStage;
import ethos.model.players.Player;
import ethos.model.players.combat.CombatType;

public class MeleeStageTen extends ZulrahStage {

	public MeleeStageTen(Zulrah zulrah, Player player) {
		super(zulrah, player);
	}

	@Override
	public void execute(CycleEventContainer container) {
		if (container.getOwner() == null || zulrah == null || zulrah.getNpc() == null || zulrah.getNpc().isDead || player == null || player.isDead
				|| zulrah.getInstancedZulrah() == null) {
			container.stop();
			return;
		}
		if (zulrah.getNpc().totalAttacks > 1 && zulrah.getNpc().attackTimer == 9) {
			player.getZulrahEvent().changeStage(11, CombatType.RANGE, ZulrahLocation.NORTH);
			zulrah.getNpc().totalAttacks = 0;
			zulrah.getNpc().setFacePlayer(true);
			container.stop();
			return;
		}
	}
}
