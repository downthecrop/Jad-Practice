package ethos.model.content.instances.impl;

import ethos.Server;
import ethos.model.content.instances.SingleInstancedArea;
import ethos.model.npcs.NPCHandler;
import ethos.model.npcs.bosses.zulrah.Zulrah;
import ethos.model.players.Boundary;
import ethos.model.players.Player;

public class SingleInstancedZulrah extends SingleInstancedArea {

	public SingleInstancedZulrah(Player player, Boundary boundary, int height) {
		super(player, boundary, height);
	}

	@Override
	public void onDispose() {
		Zulrah zulrah = player.getZulrahEvent();
		if (zulrah.getNpc() != null) {
			NPCHandler.kill(zulrah.getNpc().npcType, height);
		}
		Server.getGlobalObjects().remove(11700, height);
		NPCHandler.kill(Zulrah.SNAKELING, height);
	}

}
