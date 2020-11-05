package ethos.model.players.packets.commands.all;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.text.WordUtils;

import ethos.Config;
import ethos.model.content.OneYearQuiz;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.model.players.Right;
import ethos.model.players.packets.commands.Command;

public class Answer extends Command {

	@Override
	public void execute(Player player, String input) {
		// OneYearQuiz.answerQA(c, input);

		if (!Config.ONE_YEAR_QUIZ ||Objects.equals(Config.QUESTION, "")) {
			player.sendMessage("There is currently no question to answer.");
			return;
		}
		
		player.sendMessage("You answered: " + input);
		
		int right = player.getRights().getPrimary().getValue() - 1;

		if (input.contains(Config.ANSWER)) {
			PlayerHandler.executeGlobalMessage("[@red@Quiz@bla@] <img=" + right + ">@blu@" + player.playerName + "@bla@ answered the question correctly!");
			PlayerHandler.executeGlobalMessage("[@red@Quiz@bla@] Question: " + Config.QUESTION);
			PlayerHandler.executeGlobalMessage("[@red@Quiz@bla@] Answer: " + Config.ANSWER);
			player.sendMessage("Your answer was correct! A staff-member will contact you shortly.");
			
			List<Player> staff = PlayerHandler.nonNullStream().filter(Objects::nonNull).filter(p -> p.getRights().isOrInherits(Right.ADMINISTRATOR)).collect(Collectors.toList());
			
			if (staff.size() > 0) {
				PlayerHandler.sendMessage("@blu@[Quiz] " + WordUtils.capitalize(player.playerName) + "" + " answered the question correctly, contact them.", staff);
			}
			
			OneYearQuiz.configureEvent("end");
		} else {
			player.sendMessage("Your answer was incorrect");
		}
	}

}
