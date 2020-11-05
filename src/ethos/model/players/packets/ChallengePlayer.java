package ethos.model.players.packets;

import java.util.Objects;

import ethos.Config;
import ethos.model.players.Boundary;
import ethos.model.players.PacketType;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;

/**
 * Challenge Player
 **/
public class ChallengePlayer implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		switch (packetType) {
		case 128:
			int answerPlayer = c.getInStream().readUnsignedWord();
			if (answerPlayer >= PlayerHandler.players.length || answerPlayer < 0) {
				return;
			}
			if (PlayerHandler.players[answerPlayer] == null) {
				return;
			}
			Player requested = PlayerHandler.players[answerPlayer];
			if (Objects.isNull(requested)) {
				return;
			}
			if (c.getBankPin().requiresUnlock()) {
				c.getBankPin().open(2);
				return;
			}
			if (requested.getBankPin().requiresUnlock()) {
				return;
			}
			if (c.getTutorial().isActive()) {
				c.getTutorial().refresh();
				return;
			}
			if (requested.getTutorial().isActive()) {
				c.sendMessage("This player needs to finish the tutorial first.");
				return;
			}
			if (c.getInterfaceEvent().isActive()) {
				c.sendMessage("Please finish what you're doing.");
				return;
			}
			if (requested.getInterfaceEvent().isActive()) {
				c.sendMessage("That player is busy right now.");
				return;
			}
			if (Boundary.isIn(c, Boundary.DUEL_ARENA) || Boundary.isIn(requested, Boundary.DUEL_ARENA)) {
				c.sendMessage("You cannot do this inside of the duel arena.");
				return;
			}
			if (Config.NEW_DUEL_ARENA_ACTIVE) {
				if (c.getDuel().requestable(requested)) {
					c.getDuel().request(requested);
				}
			} else {
				c.getDH().sendStatement("@red@Dueling Temporarily Disabled", "The duel arena minigame is currently being rewritten.",
						"No player has access to this minigame during this time.", "", "Thank you for your patience, Developer J.");
				c.nextChat = -1;
				// c.getTradeAndDuel().requestDuel(answerPlayer);
			}
			break;
		}
	}
}
