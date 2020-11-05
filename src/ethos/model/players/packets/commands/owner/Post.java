package ethos.model.players.packets.commands.owner;

import ethos.model.content.tradingpost.Listing;
import ethos.model.players.Player;
import ethos.model.players.packets.commands.Command;

public class Post extends Command {

	@Override
	public void execute(Player c, String input) {
		Listing.openPost(c, false, true);
	}
}
