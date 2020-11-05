package ethos.model.content.wogw;

import java.io.*;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import ethos.Config;
import ethos.model.items.ItemAssistant;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.util.Misc;

public class Wogw {

	public static String[] lastDonators = new String[5];
	private static int slot = 0;
	
	private static final int LEAST_ACCEPTED_AMOUNT = 1000000; //1m 

	public static long EXPERIENCE_TIMER = 0, PC_POINTS_TIMER = 0, DOUBLE_DROPS_TIMER = 0;
	public static int MONEY_TOWARDS_EXPERIENCE = 0, MONEY_TOWARDS_PC_POINTS = 0, MONEY_TOWARDS_DOUBLE_DROPS = 0;

	@SuppressWarnings("resource")
	public static void init() {
        try {
            File f = new File("./data/wogw.txt");
            Scanner sc = new Scanner(f);
            int i = 0;
            while(sc.hasNextLine()){
            	i++;
                String line = sc.nextLine();
                String[] details = line.split("=");
                String amount = details[1];
                
                switch (i) {
                case 1:
                	MONEY_TOWARDS_EXPERIENCE = (int) Long.parseLong(amount);
                	break;
                case 2:
                	EXPERIENCE_TIMER = (int) Long.parseLong(amount);
                	break;
                case 3:
                	MONEY_TOWARDS_PC_POINTS = (int) Long.parseLong(amount);
                	break;
                case 4:
                	PC_POINTS_TIMER = (int) Long.parseLong(amount);
                	break;
                case 5:
                	MONEY_TOWARDS_DOUBLE_DROPS = (int) Long.parseLong(amount);
                	break;
                case 6:
                	DOUBLE_DROPS_TIMER = (int) Long.parseLong(amount);
                	break;
                }
            }

        } catch (FileNotFoundException e) {         
            e.printStackTrace();
        }
	}
	
	public static void save() {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter("./data/wogw.txt"));
			out.write("experience-amount=" + MONEY_TOWARDS_EXPERIENCE);
			out.newLine();
			out.write("experience-timer=" + EXPERIENCE_TIMER);
			out.newLine();
			out.write("pc-amount=" + MONEY_TOWARDS_PC_POINTS);
			out.newLine();
			out.write("pc-timer=" + PC_POINTS_TIMER);
			out.newLine();
			out.write("drops-amount=" + MONEY_TOWARDS_DOUBLE_DROPS);
			out.newLine();
			out.write("drops-timer=" + DOUBLE_DROPS_TIMER);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void donate(Player player, int amount) {
		if (amount < LEAST_ACCEPTED_AMOUNT) {
			player.sendMessage("You must donate at least 1 million coins.");
			return;
		}
		if (!player.getItems().playerHasItem(995, amount)) {
			player.sendMessage("@cr10@You do not have " + Misc.getValueWithoutRepresentation(amount) + ".");
			return;
		}
		player.getItems().deleteItem(995, amount);
		player.getPA().sendFrame171(1, 38020);
		
		/**
		 * Updating latest donators
		 */
		String towards =Objects.equals(player.wogwOption, "pc") ? "+5 bonus PC Points!" : Objects.equals(player.wogwOption, "experience") ? "double experience!" : Objects.equals(player.wogwOption, "drops") ? "double drops!" : "";
		player.sendMessage("You successfully donated " + Misc.format((int) player.wogwDonationAmount) + "gp to the well of goodwill towards");
		player.sendMessage(towards);
		Wogw.lastDonators[Wogw.slot] = "" + Misc.formatPlayerName(player.playerName) + " donated " + Misc.getValueWithoutRepresentation(player.wogwDonationAmount) + " towards " + towards;
		player.getPA().sendFrame126(Wogw.lastDonators[Wogw.slot], 38033 + Wogw.slot);
		
		/**
		 * Setting sprites back to unticked
		 */
		player.getPA().sendChangeSprite(38006, (byte) 1);
		player.getPA().sendChangeSprite(38007, (byte) 1);
		player.getPA().sendChangeSprite(38008, (byte) 1);
		/**
		 * Announcing donations over 10m
		 */
		if (player.wogwDonationAmount >= 10_000_000) {
			PlayerHandler.executeGlobalMessage("@cr10@[@blu@WOGW@bla@]@blu@" + Misc.formatPlayerName(player.playerName) + "@bla@ donated @blu@" + Misc.getValueWithoutRepresentation(player.wogwDonationAmount) + "@bla@ to the well of goodwill!");
		}
		/**
		 * Setting the amounts and enabling bonus if the amount reaches above required.
		 */
		switch (player.wogwOption) {
		case "experience":
			handleMoneyToExperience(amount);
			break;
			
		case "pc":
			if (MONEY_TOWARDS_PC_POINTS + amount >= 50000000 && PC_POINTS_TIMER == 0) {
				MONEY_TOWARDS_PC_POINTS = MONEY_TOWARDS_PC_POINTS + amount - 50000000;
				PlayerHandler.executeGlobalMessage("@cr10@[@blu@WOGW@bla@] <col=6666FF>The Well of Goodwill has been filled!");
				PlayerHandler.executeGlobalMessage("@cr10@[@blu@WOGW@bla@] <col=6666FF>It is now granting everyone 1 hour of +5 bonus pc points.");
				PC_POINTS_TIMER += TimeUnit.HOURS.toMillis(1) / 600;
				Config.BONUS_PC_WOGW = true;
			} else {
				MONEY_TOWARDS_PC_POINTS += amount;
			}
			break;
			
		case "drops":
			if (MONEY_TOWARDS_DOUBLE_DROPS + amount >= 100000000 && DOUBLE_DROPS_TIMER == 0) {
				MONEY_TOWARDS_DOUBLE_DROPS = MONEY_TOWARDS_DOUBLE_DROPS + amount - 100000000;
				PlayerHandler.executeGlobalMessage("@cr10@[@blu@WOGW@bla@] <col=6666FF>The Well of Goodwill has been filled!");
				PlayerHandler.executeGlobalMessage("@cr10@[@blu@WOGW@bla@] <col=6666FF>It is now granting everyone 1 hour of double drop rate.");
				DOUBLE_DROPS_TIMER += TimeUnit.HOURS.toMillis(1) / 600;
				Config.DOUBLE_DROPS = true;
			} else {
				MONEY_TOWARDS_DOUBLE_DROPS += amount;
			}
			break;
		}
		player.getPA().sendFrame126("" + Misc.getValueWithoutRepresentation(Wogw.MONEY_TOWARDS_EXPERIENCE) + "/50M", 38012);
		player.getPA().sendFrame126("" + Misc.getValueWithoutRepresentation(Wogw.MONEY_TOWARDS_PC_POINTS) + "/50M", 38013);
		player.getPA().sendFrame126("" + Misc.getValueWithoutRepresentation(Wogw.MONEY_TOWARDS_DOUBLE_DROPS) + "/100M", 38014);
		player.refreshQuestTab(8);
		Wogw.slot++;
		if (Wogw.slot == 5) {
			Wogw.slot = 0;
		}
		player.wogwOption = "";
		player.wogwDonationAmount = 0;
	}
	
	public static void donateItems(Player player, int amount) {
		//if (amount < LEAST_ACCEPTED_AMOUNT) {
		//	player.sendMessage("You must donate at least 1 million worth of items.");
		//	return;
		//}
		//for (final Wogwitems.itemsOnWell t : Wogwitems.itemsOnWell.values()) {
		if (!player.getItems().playerHasItem(player.wellItem, 1)) {
			player.sendMessage("You do not have any items to donate.");
			return;
			//}
		}
		//player.getItems().deleteItem(995, amount);
		player.getPA().sendFrame171(1, 38020);
		
		/**
		 * Updating latest donators
		 */
		//String towards = player.wogwOption == "pc" ? "+5 bonus PC Points!" : player.wogwOption == "experience" ? "double experience!" : player.wogwOption == "drops" ? "double drops!" : "";
		//player.sendMessage("You successfully donated " + Misc.format((int) player.wogwDonationAmount) + "gp to the well of goodwill towards");
		//player.sendMessage(towards);
		//Wogw.lastDonators[Wogw.slot] = "" + Misc.formatPlayerName(player.playerName) + " donated " + Misc.getValueWithoutRepresentation(player.wogwDonationAmount) + " towards " + towards;
		player.getPA().sendFrame126(Wogw.lastDonators[Wogw.slot], 38033 + Wogw.slot);
		
		/**
		 * Setting sprites back to unticked
		 */
		player.getPA().sendChangeSprite(38006, (byte) 1);
		player.getPA().sendChangeSprite(38007, (byte) 1);
		player.getPA().sendChangeSprite(38008, (byte) 1);
		/**
		 * Announcing donations over 1m
		 */
		String name = ""+ItemAssistant.getItemName(player.wellItem)+"";
		String determine = "a";
		if (name.startsWith("A") || name.startsWith("E") || name.startsWith("I") || name.startsWith("O") || name.startsWith("U"))
			determine = "an";
		if (player.wogwDonationAmount >= 1_000_000) {
			PlayerHandler.executeGlobalMessage("@cr10@[@blu@WOGW@bla@]@blu@" + Misc.formatPlayerName(player.playerName) + "@bla@ donated "+ determine +" @blu@" + ItemAssistant.getItemName(player.wellItem) + " @bla@worth @blu@" + Misc.getValueWithoutRepresentation(player.wogwDonationAmount) + "@bla@ to the well of goodwill!");
		}
		/**
		 * Setting the amounts and enabling bonus if the amount reaches above required.
		 */
		switch (player.wogwOption) {
		case "experience":
			handleMoneyToExperience(amount);
			break;
		}
		player.getPA().sendFrame126("" + Misc.getValueWithoutRepresentation(Wogw.MONEY_TOWARDS_EXPERIENCE) + "/50M", 38012);
		player.refreshQuestTab(8);
		Wogw.slot++;
		if (Wogw.slot == 5) {
			Wogw.slot = 0;
		}
		player.wogwOption = "";
		player.wogwDonationAmount = 0;
	}

	private static void handleMoneyToExperience(int amount) {

		if (MONEY_TOWARDS_EXPERIENCE + amount >= 50000000 && EXPERIENCE_TIMER == 0) {
			MONEY_TOWARDS_EXPERIENCE = MONEY_TOWARDS_EXPERIENCE + amount - 50000000;
			PlayerHandler.executeGlobalMessage("@cr10@[@blu@WOGW@bla@] <col=6666FF>The Well of Goodwill has been filled!");
			PlayerHandler.executeGlobalMessage("@cr10@[@blu@WOGW@bla@] <col=6666FF>It is now granting everyone 1 hour of double experience.");
			EXPERIENCE_TIMER += TimeUnit.HOURS.toMillis(1) / 600;
			Config.BONUS_XP_WOGW = true;
		} else {
			MONEY_TOWARDS_EXPERIENCE += amount;
		}
	}

	public static void appendBonus() {
			if (MONEY_TOWARDS_EXPERIENCE >= 50000000) {
				PlayerHandler.executeGlobalMessage("@cr10@[@blu@WOGW@bla@] <col=6666FF>The Well of Goodwill was filled above the top!");
				PlayerHandler.executeGlobalMessage("@cr10@[@blu@WOGW@bla@] <col=6666FF>It is now granting everyone 1 more hour of double experience.");
				EXPERIENCE_TIMER += TimeUnit.HOURS.toMillis(1) / 600;
				MONEY_TOWARDS_EXPERIENCE -= 50000000;
				PlayerHandler.nonNullStream().forEach(player -> {
					player.getPA().sendFrame126("" + Misc.getValueWithoutRepresentation(Wogw.MONEY_TOWARDS_EXPERIENCE) + "/50M", 38012);
					player.getPA().sendFrame126("" + Misc.getValueWithoutRepresentation(Wogw.MONEY_TOWARDS_PC_POINTS) + "/50M", 38013);
					player.getPA().sendFrame126("" + Misc.getValueWithoutRepresentation(Wogw.MONEY_TOWARDS_DOUBLE_DROPS) + "/100M", 38014);
				});
				Config.BONUS_XP_WOGW = true;
				return;
			}
			if (MONEY_TOWARDS_PC_POINTS >= 50000000) {
				PlayerHandler.executeGlobalMessage("@cr10@[@blu@WOGW@bla@] <col=6666FF>The Well of Goodwill was filled above the top!");
				PlayerHandler.executeGlobalMessage("@cr10@[@blu@WOGW@bla@] <col=6666FF>It is now granting everyone 1 more hour of +5 bonus pc points.");
				PC_POINTS_TIMER += TimeUnit.HOURS.toMillis(1) / 600;
				MONEY_TOWARDS_PC_POINTS -= 50000000;
				PlayerHandler.nonNullStream().forEach(player -> {
					player.getPA().sendFrame126("" + Misc.getValueWithoutRepresentation(Wogw.MONEY_TOWARDS_EXPERIENCE) + "/50M", 38012);
					player.getPA().sendFrame126("" + Misc.getValueWithoutRepresentation(Wogw.MONEY_TOWARDS_PC_POINTS) + "/50M", 38013);
					player.getPA().sendFrame126("" + Misc.getValueWithoutRepresentation(Wogw.MONEY_TOWARDS_DOUBLE_DROPS) + "/100M", 38014);
				});
				Config.BONUS_PC_WOGW = true;
				return;
			}
			if (MONEY_TOWARDS_DOUBLE_DROPS >= 100000000) {
				PlayerHandler.executeGlobalMessage("@cr10@[@blu@WOGW@bla@] <col=6666FF>The Well of Goodwill was filled above the top!");
				PlayerHandler.executeGlobalMessage("@cr10@[@blu@WOGW@bla@] <col=6666FF>It is now granting everyone 1 more hour of double drop rate");
				DOUBLE_DROPS_TIMER += TimeUnit.HOURS.toMillis(1) / 600;
				MONEY_TOWARDS_DOUBLE_DROPS -= 100000000;
				PlayerHandler.nonNullStream().forEach(player -> {
					player.getPA().sendFrame126("" + Misc.getValueWithoutRepresentation(Wogw.MONEY_TOWARDS_EXPERIENCE) + "/50M", 38012);
					player.getPA().sendFrame126("" + Misc.getValueWithoutRepresentation(Wogw.MONEY_TOWARDS_PC_POINTS) + "/50M", 38013);
					player.getPA().sendFrame126("" + Misc.getValueWithoutRepresentation(Wogw.MONEY_TOWARDS_DOUBLE_DROPS) + "/100M", 38014);
				});
				Config.DOUBLE_DROPS = true;
			}
	}

}
