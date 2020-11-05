package ethos.model.players.packets;

import ethos.model.players.PacketType;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.model.players.Right;

public class Moderate implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		int playerId = c.getInStream().readInteger();
		if (!c.getRights().isOrInherits(Right.MODERATOR)) {
			PlayerHandler.nonNullStream().filter(p -> p.getRights().isOrInherits(Right.MODERATOR))
					.forEach(p -> p.sendMessage("WARNING: " + c.playerName + " just attempted to operate the moderate option."));
			return;
		}
		if (playerId < 0 || playerId > PlayerHandler.players.length - 1) {
			return;
		}
		Player target = PlayerHandler.players[playerId];
		if (target == null) {
			return;
		}
		if (c.getInterfaceEvent().isActive()) {
			c.sendMessage("Please finish what you're doing.");
			return;
		}
		if (target.getRights().isOrInherits(Right.MODERATOR) && !c.getRights().isOrInherits(Right.OWNER)) {
			target.sendMessage(c.playerName + " just attempted to use the punishment panel on you.");
			c.sendMessage("You cannot punish " + target.playerName + ", they are staff.");
			return;
		}
		c.getPunishmentPanel().open(target);
	}

}
