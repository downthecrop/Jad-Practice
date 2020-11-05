package ethos.model.players.packets.commands.admin;

import ethos.Server;
import ethos.model.players.Player;
import ethos.model.players.packets.commands.Command;
import ethos.punishments.Punishment;
import ethos.punishments.PunishmentType;
import ethos.punishments.Punishments;

public class Unipban extends Command {

	@Override
	public void execute(Player c, String input) {
		if (input.isEmpty()) {
			c.sendMessage("You must enter a valid IP address.");
			return;
		}
		String[] args = input.split("-");
		String ipToUnban = args[0];
		
		Punishments punishments = Server.getPunishments();
		Punishment punishment = punishments.getPunishment(PunishmentType.NET_BAN, ipToUnban);

		if (!punishments.contains(PunishmentType.NET_BAN, ipToUnban) || punishment == null) {
			c.sendMessage("This IP address is not banned.");
			return;
		}

		punishments.remove(punishment);
		c.sendMessage("The IP '" + input + "' has been removed from the IP ban list.");
	}

}
