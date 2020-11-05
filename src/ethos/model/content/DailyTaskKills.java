package ethos.model.content;

import ethos.model.content.dailytasks.DailyTasks;
import ethos.model.content.dailytasks.DailyTasks.PossibleTasks;
import ethos.model.players.Player;

public class DailyTaskKills {

	public static void kills(Player player, int npcId) {
		switch (npcId) {
		case 5744:
		case 5762: //Shay
			DailyTasks.increase(player, PossibleTasks.SHAYZIEN_ASSAULT);
			break;
		case 7286: //skot
			DailyTasks.increase(player, PossibleTasks.SKOTIZO);
			break;
		case 259: // black drag
			DailyTasks.increase(player, PossibleTasks.BLACK_DRAGONS);
			break;
		case 268: //blue drag
			DailyTasks.increase(player, PossibleTasks.BLUE_DRAGONS);
			break;
		case 415: //abyssal demon
			DailyTasks.increase(player, PossibleTasks.ABYSSAL_DEMONS);
			break;
		case 4005: //dark beast
			DailyTasks.increase(player, PossibleTasks.DARK_BESTS);
			break;
		case 2215: //bandos
			DailyTasks.increase(player, PossibleTasks.GENERAL_GRAARDOR);
			break;
		case 3162: //arma
			DailyTasks.increase(player, PossibleTasks.KREE_ARRA);
			break;
		case 3129: //zamorak
			DailyTasks.increase(player, PossibleTasks.TSUTSAROTH);
			break;
		case 2205: //saradomin
			DailyTasks.increase(player, PossibleTasks.ZILYANA);
			break;
		case 1432: //black demon
			DailyTasks.increase(player, PossibleTasks.BLACK_DEMONS);
			break;
		case 273: //iron drag
			DailyTasks.increase(player, PossibleTasks.IRON_DRAGONS);
			break;
		case 274: //steel drag
			DailyTasks.increase(player, PossibleTasks.STEEL_DRAGONS);
			break;
		case 2919: //mith dragon
			DailyTasks.increase(player, PossibleTasks.MITHRIL_DRAGONS);
			break;
		}
	}
}
