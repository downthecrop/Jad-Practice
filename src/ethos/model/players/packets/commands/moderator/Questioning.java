package ethos.model.players.packets.commands.moderator;

import java.util.Optional;

import ethos.event.CycleEvent;
import ethos.event.CycleEventContainer;
import ethos.event.CycleEventHandler;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.model.players.packets.commands.Command;

public class Questioning extends Command {

	@Override
	public void execute(Player player, String input) {
		Optional<Player> optionalPlayer = PlayerHandler.getOptionalPlayer(input);
		if (optionalPlayer.isPresent()) {
			Player chosen_player = optionalPlayer.get();
			chosen_player.teleportToX = 1952;
			chosen_player.teleportToY = 4764;
			chosen_player.heightLevel = 1;
			player.teleportToX = 1952;
			player.teleportToY = 4768;
			player.heightLevel = 1;
				
				CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer container) {
						if (player.disconnected) {
							container.stop();
							return;
						}
						player.turnPlayerTo(1952, 4764);
						chosen_player.turnPlayerTo(1952, 4768);
						container.stop();
					}

					@Override
					public void stop() {

					}
				}, 3);
				
			chosen_player.isStuck = false;
			player.sendMessage("You have moved " + chosen_player.playerName + " to questioning.");
			chosen_player.sendMessage(player.playerName + " has moved you to questioning.");
		} else {
			player.sendMessage(input + " is offline. You can only teleport online players.");
		}
	}
}