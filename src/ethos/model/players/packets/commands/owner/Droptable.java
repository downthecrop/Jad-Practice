package ethos.model.players.packets.commands.owner;

import ethos.Server;
import ethos.model.players.Player;
import ethos.model.players.packets.commands.Command;

public class Droptable extends Command {

	@Override
	public void execute(Player c, String input) {
//		if (Config.SERVER_STATE != ServerState.PRIVATE || Config.SERVER_STATE != ServerState.PUBLIC_SECONDARY) {
//			c.sendMessage("You can only do this on the private server, not on the local server.");
//			return;
//		}
		c.getBank().getCurrentBankTab().getItems().clear();
		int npcId = Integer.parseInt(input.split("-")[0]);
		int amount = Integer.parseInt(input.split("-")[1]);
		Server.getDropManager().test(c, npcId, amount);
	}

}
