package ethos.model.holiday.halloween;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import ethos.clip.ObjectDef;
import ethos.model.players.Player;
import ethos.util.Misc;

public class HalloweenRandomOrder {
	
	public static void generateOrder(Player player) {
		int[] objects = { 26281, 26282, 26283, 26284, 26285, 26286, 26287, 26288, 26289, 26290 };
		List<Integer> obj = new ArrayList<>();
		
		if (player.halloweenRiddleGiven[0] > 0) {
			return;
		}
		
		for (int i : objects) {
			obj.add(i);
		}
		
		Collections.shuffle(obj);
		
		for (int i = 0; i < obj.size(); i++) {
			player.halloweenRiddleGiven[i] = obj.get(i);
		}
	}
	
	public static void chooseOrder(Player player, int object) {
		boolean allChosen = IntStream.of(player.halloweenRiddleChosen).allMatch(id -> id > 0);
		boolean alreadyChosen = IntStream.of(player.halloweenRiddleChosen).anyMatch(id -> id == object);
		
		if (player.halloweenRiddleGiven[0] == 0) {
			player.getDH().sendDialogues(725, 7440);
			return;
		}
		
		if (allChosen) {
			player.getDH().sendDialogues(730, 7440);
			return;
		}
		
		if (alreadyChosen) {
			player.getDH().sendDialogues(726, 7440);
			return;
		}
		player.halloweenRiddleChosen[player.halloweenOrderNumber] = object;
		player.sendMessage("#" + (player.halloweenOrderNumber + 1) + " chosen: " + ObjectDef.getObjectDef(object).name.toLowerCase());
		if (player.halloweenOrderNumber == 9) {
			player.getDH().sendDialogues(730, 7440);
		}
		player.halloweenOrderNumber++;
	}
	
	private static final int[] rewards = { 20773, 20775, 20777, 20779 };
	
	public static void checkOrder(Player player) {
		int correct = 0;
		int number = 1;
		boolean allChosen = IntStream.of(player.halloweenRiddleChosen).allMatch(id -> id > 0);
		
		if (player.halloweenRiddleGiven[0] == 0) {
			player.getDH().sendDialogues(720, 7440);
			return;
		}
		
		if (allChosen) {
			for (int i = 0; i < player.halloweenRiddleChosen.length; i++) {
				if (player.halloweenRiddleChosen[i] == player.halloweenRiddleGiven[i]) {
					player.sendMessage("@gre@" + number);
					correct++;
				} else {
					player.sendMessage("@red@" + number);
				}
				number++;
			}
			if (correct == 10) {
				player.getItems().addItem(rewards[Misc.random(rewards.length - 1)], 1);
				player.getDH().sendDialogues(727, 7440);
				player.setHalloweenOrderCount(player.getHalloweenOrderCount() + 1);
				player.sendMessage("@blu@You have now completed the order " + player.getHalloweenOrderCount() + " times.");
				for (int i = 0; i < player.halloweenRiddleGiven.length; i++) {
					player.halloweenRiddleGiven[i] = 0;
				}
			} else {
				for (int i = 0; i < player.halloweenRiddleChosen.length; i++) {
					player.halloweenRiddleChosen[i] = 0;
				}
				player.getDH().sendDialogues(728, 7440);
			}
			number = 1;
			player.halloweenOrderNumber = 0;
		} else {
			player.getDH().sendDialogues(729, 7440);
		}
	}

}
