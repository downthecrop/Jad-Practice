package ethos.model.players.packets.commands.owner;

import java.io.BufferedWriter;
import java.io.FileWriter;

import ethos.model.content.wogw.Wogwitems;
import ethos.model.players.Player;
import ethos.model.players.packets.commands.Command;

public class Dump extends Command {

	@Override
	public void execute(Player player, String input) {
		try {
			try (BufferedWriter coord=new BufferedWriter(new FileWriter("./WOGW.txt", true))) {
				int i=0;
				for (final Wogwitems.itemsOnWell t : Wogwitems.itemsOnWell.values()) {
					if (i==0)
						coord.write(t.getItemId()+"	0");
					else
						coord.write("	"+t.getItemId()+"	0");
					i++;
				}
			}
			}  catch (Exception e) {
				player.sendMessage("Invalid Format. ::dump");
			}
	}
}