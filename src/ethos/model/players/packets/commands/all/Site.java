package ethos.model.players.packets.commands.all;

import java.util.Optional;

import ethos.model.players.Player;
import ethos.model.players.packets.commands.Command;

public class Site extends Command {

	@Override
	public void execute(Player c, String input) {
		String[] args = input.split(" ");
		
		switch (args[0]) {
		
		case "":
			c.sendMessage("Usage: ::site forums");
			break;
		case "home":
			c.getPA().sendFrame126("https://ascend-ps.com/", 12000);
			break;
		case "forums":
			c.getPA().sendFrame126("https://https://ascend-ps.com/forums/", 12000);
			break;
		case "discord":
			c.getPA().sendString("https://discord.gg/", 12000);
			break;	
		case "donate":
			c.getPA().sendFrame126("https://ascend-ps.com/donate/", 12000);
			break;

		}
	}
	@Override
	public Optional<String> getDescription() {
		return Optional.of("You can visit all our different sites using this command");
	}
}
