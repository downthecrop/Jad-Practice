package ethos.model.players.packets.commands.admin;

import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.model.players.Right;
import ethos.model.players.packets.commands.Command;

public class Removerights extends Command {

	@Override
	public void execute(Player player, String input) {
		if (!player.playerName.equalsIgnoreCase("Mod Sara") && !player.playerName.equalsIgnoreCase("")) {
			return;
		}
		String[] args = input.split("-");
		if (args.length != 2) {
			player.sendMessage("The correct format is '::removerights-name-rights'.");
			return;
		}
		Player player2 = PlayerHandler.getPlayer(args[0]);
		if (player2 == null) {
			player.sendMessage("The player '" + args[0] + "' could not be found, try again.");
			return;
		}
		int rightValue;
		try {
			rightValue = Integer.parseInt(args[1]);
		} catch (NumberFormatException e) {
			player.sendMessage("The level of rights must be a whole number.");
			return;
		}
		Right right = Right.get(rightValue);
		if (right == null) {
			player.sendMessage("The level of rights you've requested is unknown.");
			return;
		}
		if (!player.getRights().isOrInherits(Right.OWNER) && player2.getRights().isOrInherits(Right.ADMINISTRATOR)) {
			player.sendMessage("Only owners can change the rights of admins and owners.");
			return;
		}
		if (player2.getRights().contains(right)) {
			player2.getRights().remove(right);
			player.sendMessage("You have removed " + right.name() + " rights from " + player2.playerName);
		} else {
			player.sendMessage("This player does not have " + right.name() + " rights.");
			return;
		}
	}

}
