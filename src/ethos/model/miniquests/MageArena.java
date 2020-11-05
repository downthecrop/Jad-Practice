package ethos.model.miniquests;

import ethos.Server;
import ethos.event.CycleEvent;
import ethos.event.CycleEventContainer;
import ethos.event.CycleEventHandler;
import ethos.model.npcs.NPC;
import ethos.model.npcs.NPCHandler;
import ethos.model.players.Player;

public class MageArena {

	Player player;

	public MageArena(Player player) {
		this.player = player;
	}

	public void start() {
		player.getPA().removeAllWindows();
		NPC kolodion = NPCHandler.getNpc(1603);
		NPC stage1 = NPCHandler.getNpc(1605);
		kolodion.facePlayer(player.getIndex());
		kolodion.startAnimation(811);
		player.getPA().startTeleport(3105, 3934, 0, "modern", false);
		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				Server.npcHandler.spawnNpc(player, 1605, 3106, 3934, 0, 1, 3, 17, 70, 60, true, true);
				container.stop();
			}
		}, 8);
	}

}
