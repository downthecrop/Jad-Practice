package ethos.model.content.barrows;

import ethos.event.Event;
import ethos.model.npcs.NPC;
import ethos.model.npcs.NPCHandler;
import ethos.model.players.Boundary;
import ethos.model.players.Player;
import ethos.model.players.combat.Hitmark;

public class TunnelEvent extends Event<Player> {

	public TunnelEvent(String signature, Player attachment, int ticks) {
		super(signature, attachment, ticks);
	}

	@Override
	public void execute() {
		if (attachment == null) {
			super.stop();
			return;
		}
		if (!Boundary.isIn(attachment, Barrows.TUNNEL)) {
			if (attachment.getBarrows().isCompleted()) {
				attachment.getBarrows().initialize();
			}
			stop();
			return;
		}
		attachment.getBarrows().getActive().ifPresent(brother -> {
			if (brother.getNPC() == null) {
				stop();
			} else {
				NPC npc = brother.getNPC();
				if (attachment.distanceToPoint(npc.absX, npc.absY, npc.heightLevel) > 20) {
					stop();
				}
			}
		});
		if (attachment.getBarrows().isCompleted()) {
			attachment.getPA().shakeScreen(3, 3, 3, 3);
			if ((getElapsedTicks() + 1) % 10 == 0) {
				attachment.appendDamage(5, Hitmark.HIT);
			}
		} else if ((getElapsedTicks() + 1) % 30 == 0) {
			attachment.getBarrows().drainPrayer();
		}
	}

	@Override
	public void stop() {
		super.stop();
		if (attachment == null) {
			return;
		}
		attachment.getPA().sendFrame107();
		attachment.getBarrows().getActive().ifPresent(brother -> {
			brother.setActive(false);
			NPC npc = brother.getNPC();
			if (npc != null) {
				NPCHandler.npcs[npc.getIndex()] = null;
			}
		});
	}

}
