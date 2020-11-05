package ethos.model.players.packets;

import java.util.Objects;

import ethos.Server;
import ethos.event.CycleEvent;
import ethos.event.CycleEventContainer;
import ethos.event.CycleEventHandler;
import ethos.model.items.GroundItem;
import ethos.model.multiplayer_session.MultiplayerSessionFinalizeType;
import ethos.model.multiplayer_session.MultiplayerSessionStage;
import ethos.model.multiplayer_session.MultiplayerSessionType;
import ethos.model.multiplayer_session.duel.DuelSession;
import ethos.model.players.PacketType;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;

/**
 * Pickup Item
 **/
public class PickupItem implements PacketType {

	@Override
	public void processPacket(final Player c, int packetType, int packetSize) {
		c.walkingToItem = false;
		c.itemY = c.getInStream().readSignedWordBigEndian();
		c.itemId = c.getInStream().readUnsignedWord();
		c.itemX = c.getInStream().readSignedWordBigEndian();
		if (Math.abs(c.getX() - c.itemX) > 25 || Math.abs(c.getY() - c.itemY) > 25) {
			c.resetWalkingQueue();
			return;
		}
		DuelSession duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c, MultiplayerSessionType.DUEL);
		if (Objects.nonNull(duelSession) && duelSession.getStage().getStage() > MultiplayerSessionStage.REQUEST
				&& duelSession.getStage().getStage() < MultiplayerSessionStage.FURTHER_INTERATION) {
			c.sendMessage("Your actions have declined the duel.");
			duelSession.getOther(c).sendMessage("The challenger has declined the duel.");
			duelSession.finish(MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
			return;
		}
		if (c.itemId == 11941 && c.getItems().getItemCount(11941, true) > 1) {
			c.sendMessage("You cannot own multiples of this item.");
			return;
		}
		if (c.itemId == 12791 && c.getItems().getItemCount(12791, true) > 1) {
			c.sendMessage("You cannot own multiples of this item.");
			return;
		}
		if (c.getTutorial().isActive()) {
			c.getTutorial().refresh();
			return;
		}
		if (c.getBankPin().requiresUnlock()) {
			c.getBankPin().open(2);
			return;
		}
		if (c.isStuck) {
			c.isStuck = false;
			c.sendMessage("@red@You've disrupted stuck command, you will no longer be moved home.");
			return;
		}
		if (c.isDead || c.getHealth().getAmount() <= 0) {
			return;
		}
		GroundItem item = Server.itemHandler.getGroundItem(c.itemId, c.itemX, c.itemY, c.heightLevel);
		if (item == null) {
			return;
		}
		if (!c.inClanWars() && !c.inClanWarsSafe() && !c.getMode().isItemScavengingPermitted()) {
			Player owner = PlayerHandler.getPlayer(item.getController());
			if (owner == null || !c.playerName.equalsIgnoreCase(item.getController())) {
				c.sendMessage("Your mode restricts you from picking up items that are not yours.");
				return;
			}
		}
		if (c.getInterfaceEvent().isActive()) {
			c.sendMessage("Please finish what you're doing.");
			return;
		}
		if (c.getPA().viewingOtherBank) {
			c.getPA().resetOtherBank();
		}
		c.getCombat().resetPlayerAttack();
		if (c.teleportingToDistrict) {
			return;
		}
		if (c.inClanWars() || c.inClanWarsSafe()) {
			if (!c.pkDistrict) {
				return;
			}
		}
		if (c.getX() == c.itemX && c.getY() == c.itemY) {
			Server.itemHandler.removeGroundItem(c, c.itemId, c.itemX, c.itemY, c.heightLevel, true);
		} else {
			c.walkingToItem = true;
			CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					if (!c.walkingToItem)
						container.stop();
					if (c.getX() == c.itemX && c.getY() == c.itemY) {
						Server.itemHandler.removeGroundItem(c, c.itemId, c.itemX, c.itemY, c.heightLevel, true);
						container.stop();
					}
				}

				@Override
				public void stop() {
					c.walkingToItem = false;
				}
			}, 1);
		}

	}

}
