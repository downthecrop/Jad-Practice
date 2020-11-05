package ethos.model.players.packets.commands.all;

import ethos.Server;
import ethos.model.multiplayer_session.MultiplayerSessionType;
import ethos.model.players.Player;
import ethos.model.players.packets.commands.Command;
import ethos.model.players.packets.dialogueoptions.FiveOptions;
import ethos.model.players.packets.dialogueoptions.FourOptions;
import ethos.model.players.packets.dialogueoptions.ThreeOptions;
import ethos.model.players.packets.dialogueoptions.TwoOptions;

public class Dialoguecontinuation extends Command {

	@Override
	public void execute(Player c, String input) {
		if (Server.getMultiplayerSessionListener().inSession(c, MultiplayerSessionType.TRADE)) {
			c.sendMessage("You must decline the trade before doing this.");
			return;
		}
		if (c.getInterfaceEvent().isActive()) {
			c.sendMessage("Please finish what you're doing.");
			c.getInterfaceEvent().draw();
			return;
		}
		switch (input) {
		case "continue":
			if (c.nextChat > 0) {
				c.getDH().sendDialogues(c.nextChat, c.talkingNpc);
			} else {
				c.getDH().sendDialogues(0, -1);
			}
			break;
			
		case "option_one":
			switch (c.dialogueOptions) {
			case 2:
				TwoOptions.handleOption1(c);
				break;
				
			case 3:
				ThreeOptions.handleOption1(c);
				break;
				
			case 4:
				FourOptions.handleOption1(c);
				break;
				
			case 5:
				FiveOptions.handleOption1(c);
				break;
			}
			break;
			
		case "option_two":
			switch (c.dialogueOptions) {
			case 2:
				TwoOptions.handleOption2(c);
				break;
				
			case 3:
				ThreeOptions.handleOption2(c);
				break;
				
			case 4:
				FourOptions.handleOption2(c);
				break;
				
			case 5:
				FiveOptions.handleOption2(c);
				break;
			}
			break;
			
		case "option_three":
			switch (c.dialogueOptions) {
			case 3:
				ThreeOptions.handleOption3(c);
				break;
				
			case 4:
				FourOptions.handleOption3(c);
				break;
				
			case 5:
				FiveOptions.handleOption3(c);
				break;
			}
			break;
			
		case "option_four":
			switch (c.dialogueOptions) {
			case 4:
				FourOptions.handleOption4(c);
				break;
				
			case 5:
				FiveOptions.handleOption4(c);
				break;
			}
			break;
			
		case "option_five":
			switch (c.dialogueOptions) {
			case 5:
				FiveOptions.handleOption5(c);
				break;
			}
			break;
		}
	}

}
