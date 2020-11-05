package ethos.model.content.staff;

import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.model.players.packets.Commands;

/**
 * Class handling the actions within the staff control
 * @author Matt - https://www.rune-server.org/members/matt%27/
 *
 * @date 13 okt. 2016
 */

public class StaffControl {
	
	/**
	 * Points to the username options are being used towards
	 */
	public static String username = "";
	
	public static boolean isUsingControl = false;
	
	/**
	 * Loads various possible options to perform on a player
	 * @param player
	 * @param option		The options we are going to load
	 */
	public static String[] options = { 
				"Kick", "Quick Jail", "Quick Ban", "Quick Mute", 
				"Teleport To", "Teleport To Me", "Move Home", "Move To Questioning",
				"Check Bank", "Check Inventory", "Check If Bank-Pin", "Check Stats", "Check Coordinates",
				"Back"
	};
	public static void loadOnPlayerOptions(Player player) {
		isUsingControl = true;
		emptyList(player);
		int line = 45005;
		for (int i = 0; i < options.length; i++) {
			player.getPA().sendFrame126("@or2@" + options[i], line);
			line++;
		}
	}
	
	public static void clickActions(Player player, int actionButton) {
		String split = "-", space = " ";
		Player chosen_player = PlayerHandler.getPlayer(username);
		if (!isUsingControl) {
			return;
		}
		switch (actionButton) {
		case 175205:
			Commands.executeCommand(player, "kick" + space + username, "helper");
			break;

		case 175206:
			Commands.executeCommand(player, "jail" + split + username + split + "1440" + split + "Quick jail 'staff-control'", "moderator");
			break;

		case 175207:
			Commands.executeCommand(player, "ban" + split + username + split + "1440" + split + "Quick ban 'staff-control'", "moderator");
			break;

		case 175208:
			Commands.executeCommand(player, "mute" + split + username + split + "1440" + split + "Quick mute 'staff-control'", "moderator");
			break;

		case 175209:
			Commands.executeCommand(player, "xteleto" + space + username, "moderator");
			break;

		case 175210:
			Commands.executeCommand(player, "teletome" + space + username, "moderator");
			break;

		case 175211:
			Commands.executeCommand(player, "unjail" + space + username, "moderator");
			break;

		case 175212:
			Commands.executeCommand(player, "questioning" + space + username, "moderator");
			break;

		case 175213:
			Commands.executeCommand(player, "checkbank" + space + username, "admin");
			break;

		case 175214:
			Commands.executeCommand(player, "checkinventory" + space + username, "moderator");
			break;

		case 175215:
			player.getDH().sendStatement(username + " "+ (chosen_player.getBankPin().getPin().length() > 0 ? "@gre@does@bla@" : "@red@does not@bla@") +" have a bank-pin set");
			break;

		case 175216:
			//TODO: check stats
			break;

		case 175217:
			player.sendMessage("Player Coordinates: X: " + chosen_player.absX + " Y: " + chosen_player.absY + " H: " + chosen_player.heightLevel);
			break;

		case 175218:
			goBack(player);
			break;
		}
	}
	
	public static void emptyList(Player player) {
		int line = 45005;
        for (int i = 1; i < 200; i++) {
            	player.getPA().sendFrame126("", line);
                line++;
        }
	}
	
	/**
	 * Handles the action of going back
	 * @param player
	 */
	public static void goBack(Player player) {
		emptyList(player);
		isUsingControl = false;
		player.getPA().sendFrame126("<col=0xFF981F>Players", 45254);
		int line = 45005;
		player.getPA().sendFrame126("Online: " + PlayerHandler.getPlayerCount(), 45001);
		for (Player p : PlayerHandler.players) {
			if (p == null)
				continue;
			player.getPA().sendFrame126("@or2@" + p.playerName + "  -  " + p.getHealth().getAmount() + "/" + p.getHealth().getMaximum(), line);
            line++;
		}
	}

}
