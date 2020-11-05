package ethos.model.players.packets.commands.all;

import ethos.model.items.bank.BankItem;
import ethos.model.players.Player;
import ethos.model.players.packets.commands.Command;

public class Placeholder extends Command {

	@Override
	public void execute(Player c, String input) {
		String[] args = input.split("-");
		//int slot = Integer.parseInt(args[0]);
		int itemID = Integer.parseInt(args[0]);
		c.getItems().removeFromBankPlaceholder(itemID, c.getBank().getCurrentBankTab().getItemAmount(new BankItem(itemID + 1)), true);
	}

}
