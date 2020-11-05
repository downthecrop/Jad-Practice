package ethos.model.content.dailytasks;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import ethos.model.players.Player;
import ethos.util.Misc;

/**
 * 
 * @author Nighel
 * Credits to: Tyler for the idea
 *
 */

public class DailyTasks {
	
	public enum PossibleTasks {
		
		SHAYZIEN_ASSAULT(TaskTypes.PVM, 50, "Get 50 Kills at Shayzien Assault"),
		PEST_CONTROL(TaskTypes.PVM, 15, "Do 15 Rounds of Pest Control"),
		SLAYER_TASK(TaskTypes.PVM, 1, "Complete 1 Slayer Task"),
		BARROWS_CHESTS(TaskTypes.PVM, 3, "Loot 3 Barrows Chests"),
		SKOTIZO(TaskTypes.PVM, 1, "Kill 1 Skotizo"),
		BLACK_DRAGONS(TaskTypes.PVM, 50, "Kill 50 Black Dragons"),
		BLUE_DRAGONS(TaskTypes.PVM, 50, "Kill 50 Blue Dragons"),
		ABYSSAL_DEMONS(TaskTypes.PVM, 50, "Kill 50 Abyssal Demons"),
		DARK_BESTS(TaskTypes.PVM, 50, "Kill 50 Dark Beasts"),
		GENERAL_GRAARDOR(TaskTypes.PVM, 5, "Kill 5 General Graardor"),
		KREE_ARRA(TaskTypes.PVM, 5, "Kill 5 Kree'Arra"),
		TSUTSAROTH(TaskTypes.PVM, 5, "Kill 5 Tsutsaroth"),
		ZILYANA(TaskTypes.PVM, 5, "Kill 5 Zilyana"),
		SKELETAL_MYSTICS_RAID(TaskTypes.PVM, 1, "Complete 1 Skeletal Mystics Raid"),
		TEKTON_RAID(TaskTypes.PVM, 1, "Complete 1 Tekton Raid"),
		BLACK_DEMONS(TaskTypes.PVM, 50, "Kill 50 Black Demon"),
		IRON_DRAGONS(TaskTypes.PVM, 50, "Kill 50 Iron Dragons"),
		STEEL_DRAGONS(TaskTypes.PVM, 50, "Kill 50 Steel Dragons"),
		MITHRIL_DRAGONS(TaskTypes.PVM, 50, "Kill 50 Mithril Dragons"),
		
		LAW_RUNES(TaskTypes.SKILLING, 200, "craft 200 Law Runes."),
		RED_CHINCHOMPAS(TaskTypes.SKILLING, 30, "Catch 30 Red Chinchompas"),
		SEERS_COURSE(TaskTypes.SKILLING, 10, "Run 10 Laps - Seers Course"),
		PRAYER_POTIONS(TaskTypes.SKILLING, 80, "Make 80 Prayer Potions"),
		MASTER_FARMER(TaskTypes.SKILLING, 75, "Pickpocket 75 Times - Master Farmer"),
		DRAGONSTONES(TaskTypes.SKILLING, 100, "Cut 100 Dragonstones"),
		YEW_SHORTBOWS(TaskTypes.SKILLING, 100, "Fletch 100 Yew Shortbows"),
		GEM_ROCK(TaskTypes.SKILLING, 200, "Mine 200 Gems - Gem Rock"),
		IRON_PLATEBODYS(TaskTypes.SKILLING, 35, "Make 35 Iron Platebodies - Smithing"),
		SHARKS(TaskTypes.SKILLING, 100, "Catch 100 Shark - Harpoon"),
		LOBSTERS(TaskTypes.SKILLING, 150, "Cook 150 Lobsters"),
		LIGHT_YEW_LOGS(TaskTypes.SKILLING, 100, "Light 100 Yew Logs"),
		YEW_LOGS(TaskTypes.SKILLING, 100, "Cut 100 Yew Logs"),
		SNAPDRAGONS(TaskTypes.SKILLING, 50, "Harvest 50 Snapdragon - Farming"),
		SILVER_SICKLES(TaskTypes.SKILLING, 150, "Steal 150 Silver Sickles - Stall"),
		MAGIC_LOGS(TaskTypes.SKILLING, 60, "Cut 60 Magic Logs"),
		LIGHT_MAGIC_LOGS(TaskTypes.SKILLING, 70, "Light 70 Magic Log"),
		RUNITE_ORE(TaskTypes.SKILLING, 25, "Mine 25 Runite Ore"),
		NATURE_RUNES(TaskTypes.SKILLING, 300, "craft 300 Nature Runes"),
		;
		
		public TaskTypes type;
		public int amount;
		String message;
		
		private PossibleTasks(TaskTypes type, int amount, String message) {
			
			this.type = type;
			this.amount = amount;
			this.message = message;
			
		}
		
	}
	
	/**
	 * Gets today's date
	 * @return
	 */
	
	private static int getTodayDate() {
		Calendar cal = new GregorianCalendar();
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int month = cal.get(Calendar.MONTH);
		return (month * 100 + day);
	}
	
	/**
	 * Assigns a task to a player when it has daily's enabled and has no current task.
	 * @param player
	 */
	
	public static void assignTask(Player player) {
		if (player.dailyEnabled) {

			if(player.currentTask != null) {
				player.sendMessage("@red@You already have a daily task. Type '::dailytask' to check your task!");
				return;
			}
			
			if (player.dailyTaskDate == getTodayDate() && player.currentTask == null && player.completedDailyTask == true) {
				player.sendMessage("@red@You already completed your daily task");
				return;
			} else {
				if (player.completedDailyTask == false && player.dailyTaskDate != getTodayDate() && player.currentTask == null) {
					player.currentTask = getRandomTask(player.playerChoice);
					player.sendMessage("@red@New Daily Task: " + player.currentTask.message);
				}
			}
			
		} 
	}
	
	public static void complete(Player player) {
		if(player.currentTask == null)
			return;
		
		if(player.totalDailyDone >= player.currentTask.amount) {
			player.totalDailyDone = 0;
			player.dailyTaskDate = getTodayDate();
			player.completedDailyTask = true;
			player.sendMessage("@blu@@cr10@ You have completed your daily task: "+ player.currentTask.name().toLowerCase() + ", take these rewards!");
			player.getItems().addItemUnderAnyCircumstance(13307, 200 + Misc.random(300));
			player.getItems().addItemUnderAnyCircumstance(player.currentTask.type.equals(TaskTypes.PVM) ? 20703 : 20791, 1);
			player.sendMessage("@blu@@cr8@You've been rewarded with some blood money and a daily task crate.");
			player.currentTask = null;
		}
	}
	
	public static void increase(Player player, PossibleTasks task) {
		if(player.currentTask == null)
			return;
		
		if (player.currentTask.equals(task)) {
			player.totalDailyDone++;
			player.sendMessage(player.totalDailyDone + "/"+player.currentTask.amount+" completed of your task.");
			complete(player);
		}
	}
	
	/**
	 * Gets a random task on the choice of what the player has selected
	 * @param type
	 * @return
	 */
	private static PossibleTasks getRandomTask(TaskTypes type) {
		ArrayList<PossibleTasks> possibleTasks = new ArrayList<PossibleTasks>();
		for(PossibleTasks tasks : PossibleTasks.values())
			if(tasks.type.equals(type))
				possibleTasks.add(tasks);
		return possibleTasks.get(Misc.random(possibleTasks.size()));
	}
	
}