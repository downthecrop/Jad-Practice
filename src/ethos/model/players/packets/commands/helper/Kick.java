package ethos.model.players.packets.commands.helper;

import java.util.Optional;

import ethos.Config;
import ethos.Server;
import ethos.ServerState;
import ethos.event.CycleEventHandler;
import ethos.model.players.ConnectedFrom;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.model.players.packets.commands.Command;

/**
 * Forces a given player to log out.
 * 
 * @author Emiel
 */
public class Kick extends Command {

	@Override
	public void execute(Player c, String input) {
		Optional<Player> optionalPlayer = PlayerHandler.getOptionalPlayer(input);
		if (optionalPlayer.isPresent()) {
			Player c2 = optionalPlayer.get();
			if (Server.getMultiplayerSessionListener().inAnySession(c)) {
				c.sendMessage("The player is in a trade, or duel. You cannot do this at this time.");
				return;
			}
			if (c.playerName.equalsIgnoreCase("Mod Sara")) {
				if (c2.getBankPin().requiresUnlock()) {
					c2.getBankPin().open(2);
					c.sendMessage("This player is currently in lock-down and cannot be kicked.");
					return;
				}
			}
			c2.outStream.createFrame(109);
			CycleEventHandler.getSingleton().stopEvents(c2);
			c2.properLogout = true;			
			c2.disconnected = true;
			c2.logoutDelay = Long.MAX_VALUE;
			ConnectedFrom.addConnectedFrom(c2, c2.connectedFrom);
			c.sendMessage("Kicked " + c2.playerName);
			if (Config.SERVER_STATE == ServerState.PUBLIC_PRIMARY) {
				// TODO: Log handling
			}
		} else {
			c.sendMessage(input + " is not online. You can only kick online players.");
		}
	}
}
