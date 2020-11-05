package ethos.model.content;

import ethos.Config;
import ethos.model.players.PlayerHandler;

public class OneYearQuiz {
	
	public static String answer = "";
	
	public static void configureEvent(String config) {
		switch (config) {
		case "start":
			Config.ONE_YEAR_QUIZ = true;
			break;
			
		case "end":
			Config.ONE_YEAR_QUIZ = false;
			Config.QUESTION = "";
			Config.ANSWER = "";
			answer = "";
			break;
		}
	}
	
	public static void setQA(String q, String a) {
		PlayerHandler.executeGlobalMessage("[@red@Quiz@bla@] " + q);
		PlayerHandler.executeGlobalMessage("[@red@Quiz@bla@] Answer by using ::answer (your answer)");
		Config.QUESTION = q;
		Config.ANSWER = a;
		answer = a;
	}

}
