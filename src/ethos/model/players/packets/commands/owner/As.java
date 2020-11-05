package ethos.model.players.packets.commands.owner;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import ethos.Server;
import ethos.model.players.Player;
import ethos.model.players.packets.commands.Command;

public class As extends Command {

	@Override
	public void execute(Player player, String input) {
		addSpawn(player, input);
	}
	
	public void addSpawn(Player player, String animal) {
		String filePath = "./Data/" + player.playerName + " Lava Dragon.txt";
		BufferedWriter bw = null;

		try {
			bw = new BufferedWriter(new FileWriter(filePath, true));
			switch (animal.toUpperCase()) {
			
			case "B":
				bw.write("spawn =	6593	" + player.absX + "	" + player.absY + "	" + player.heightLevel + "	1	23	240	150	Lava Dragon");
				Server.npcHandler.spawnNpc(player, 6593, player.absX, player.absY, 0, 1, 0, 0, 0, 0, false, false);
				break;

			case "W":
				bw.write("spawn =	955	" + player.absX + "	" + player.absY + "	" + player.heightLevel + "	1	3	20	20	Kalphite Worker");
				Server.npcHandler.spawnNpc(player, 955, player.absX, player.absY, 0, 1, 0, 0, 0, 0, false, false);
				break;

			case "G":
				bw.write("spawn =	959	" + player.absX + "	" + player.absY + "	" + player.heightLevel + "	1	12	110	110	Kalphite Guardian");
				Server.npcHandler.spawnNpc(player, 959, player.absX, player.absY, 0, 1, 0, 0, 0, 0, false, false);
				break;
				
			}
			player.sendMessage("@red@You set spawn at: X: " + player.absX + ", Y: " + player.absY);
			bw.newLine();
			bw.flush();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException ignored) {
				}
			}
		}
	}

}
