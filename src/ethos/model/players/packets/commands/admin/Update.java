package ethos.model.players.packets.commands.admin;

import java.util.Objects;

import ethos.Server;
import ethos.model.content.wogw.Wogw;
import ethos.model.multiplayer_session.MultiplayerSessionFinalizeType;
import ethos.model.multiplayer_session.MultiplayerSessionStage;
import ethos.model.multiplayer_session.MultiplayerSessionType;
import ethos.model.multiplayer_session.duel.DuelSession;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.model.players.packets.commands.Command;

/**
 * Start the update timer and update the server.
 * 
 * @author Emiel
 *
 */
public class Update extends Command {

	@Override
	public void execute(Player c, String input) {
		int seconds = Integer.parseInt(input);
		if (seconds < 15) {
			c.sendMessage("The timer cannot be lower than 15 seconds so other operations can be sorted.");
			seconds = 15;
		}
		PlayerHandler.updateSeconds = seconds;
		PlayerHandler.updateAnnounced = false;
		PlayerHandler.updateRunning = true;
		PlayerHandler.updateStartTime = System.currentTimeMillis();
		Wogw.save();
		for (Player player : PlayerHandler.players) {
			if (player == null) {
				continue;
			}
			Player client = player;
			if (client.getPA().viewingOtherBank) {
				client.getPA().resetOtherBank();
				client.sendMessage("An update is now occuring, you cannot view banks.");
			}
			DuelSession duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(client, MultiplayerSessionType.DUEL);
			if (Objects.nonNull(duelSession)) {
				if (duelSession.getStage().getStage() == MultiplayerSessionStage.FURTHER_INTERATION) {
					if (!duelSession.getWinner().isPresent()) {
						duelSession.finish(MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
						duelSession.getPlayers().forEach(p -> {
							p.sendMessage("The duel has been cancelled by the server because of an update.");
							duelSession.moveAndClearAttributes(p);
						});
					}
				} else if (duelSession.getStage().getStage() < MultiplayerSessionStage.FURTHER_INTERATION) {
					duelSession.finish(MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
					duelSession.getPlayers().forEach(p -> {
						p.sendMessage("The duel has been cancelled by the server because of an update.");
						duelSession.moveAndClearAttributes(p);
					});
				}
			}
		}
	}
}
