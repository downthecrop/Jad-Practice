package ethos.model.players.packets.commands.moderator;

import ethos.Config;
import ethos.model.content.OneYearQuiz;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.model.players.packets.commands.Command;

public class Q extends Command {

	@Override
	public void execute(Player player, String input) {
		
		String[] args = input.split("-");
		
		switch (args[0]) {
		
		case "":
			player.sendMessage("@red@Usage: ::q start, end, show, check or set-question-answer");
			break;
		
		case "start":
			OneYearQuiz.configureEvent("start");
			player.sendMessage("@red@Quizmode started");
			PlayerHandler.executeGlobalMessage("[@red@Quiz@bla@] Quizmode started, get ready..");
			break;
			
		case "end":
			OneYearQuiz.configureEvent("end");
			player.sendMessage("@red@Quizmode ended");
			PlayerHandler.executeGlobalMessage("[@red@Quiz@bla@] Quizmode ended, make sure to try your luck on the next one!");
			break;
			
		case "check":
			player.sendMessage("Question: " + Config.QUESTION);
			player.sendMessage("Answer: " + Config.ANSWER);
			break;
			
		case "show":
			PlayerHandler.executeGlobalMessage("[@red@Quiz@bla@] " + Config.QUESTION);
			PlayerHandler.executeGlobalMessage("[@red@Quiz@bla@] Answer by using ::answer (your answer)");
			break;
			
		case "set":
			OneYearQuiz.configureEvent("start");
			OneYearQuiz.setQA(args[1], args[2]);
			player.sendMessage("Questions set: " + args[1]);
			player.sendMessage("Answer set: " + args[2]);
			break;
		}
	}
}
