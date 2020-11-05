package ethos.model.content.teleportation;

import ethos.Config;
import ethos.event.impl.WheatPortalEvent;
import ethos.model.content.achievement_diary.ardougne.ArdougneDiaryEntry;
import ethos.model.content.achievement_diary.falador.FaladorDiaryEntry;
import ethos.model.content.achievement_diary.fremennik.FremennikDiaryEntry;
import ethos.model.content.achievement_diary.kandarin.KandarinDiaryEntry;
import ethos.model.content.achievement_diary.lumbridge_draynor.LumbridgeDraynorDiaryEntry;
import ethos.model.content.achievement_diary.morytania.MorytaniaDiaryEntry;
import ethos.model.content.achievement_diary.western_provinces.WesternDiaryEntry;
import ethos.model.players.Player;

/**
 * Teleport Handler Class - Used for our new teleporting system Probably should
 * have used enums, oh well..
 * 
 * @author Tyler
 */
public class TeleportHandler {

	private Player c;

	public TeleportHandler(Player player) {
		this.c = player;
	}

	String[] monsterNames = { "Select your Monster Teleport", "Rock Crabs", "Cows", "Yaks", "Bob's Island",
			"Desert Bandits", "Elf Warriors", "Dagannoths", "Mithril Dragons", "Slayer Tower", "Demonic Gorillas",
			"Taverley Dungeon", "Stronghold Cave", "Smoke Devils", "", "", "", "", "", "", "" };
	String[] minigameNames = { "Select your Minigame Teleport", "Raids", "Warriors Guild", "Pest Control",
			"Fight Caves", "Barrows", "Clan Wars", "Shayzien Assault", "Mage Arena", "Duel Arena", "", "", "", "", "",
			"", "", "", "", "", "" };
	String[] bossNames = { "Select your Boss Teleport", "Barrelchest", "Dagannoth Kings",
			"King Black Dragon@red@ (DANGEROUS!)", "Giant Mole@red@ (DANGEROUS!)", "Kalphite Queen", "God Wars Dungeon",
			"Corporeal Beast", "Dagannoth Mother", "Kraken", "Zulrah", "Cerberus", "Thermonuclear Smoke Devils",
			"Abyssal Sire", "Demonic Gorillas", "Lizardman Shaman", "Vorkath", "", "", "", "" };
	String[] wildernessNames = { "Select your Wilderness Teleport", "West Dragons @red@(10)", "Mage Bank @yel@(Safe)",
			"Dark Castle @red@(15)", "Hill Giants (Multi) @red@(18)", "Wilderness Agility Course @red@(52)",
			"Vet'ion @red@(40) ", "Callisto @red@(43)", "Scorpia @red@(54)", "Venenatis @red@(28)",
			"Chaos Elemental @red@(50)", "Chaos Fanatic @red@(41)", "Crazy Archaeologist @red@(23)", "", "", "", "", "",
			"", "", "" };
	String[] cityNames = { "Select your City Teleport", "Varrock", "Yanille", "Edgeville", "Lumbridge", "Ardougne",
			"Neitiznot", "Karamja", "Falador", "Taverley", "Camelot", "Catherby", "Al Kharid", "", "", "", "", "", "",
			"", "" };
	String[] donatorNames = { "Select your Dungeon Teleport", "Catacombs", "Fremennik Slayer Dungeon",
			"Taverley Dungeon", "Skeletal Wyverns", "Asgarnian Ice Dungeon", "Brimhaven Dungeon", "", "", "", "", "",
			"", "", "", "", "", "", "", "", "" };
	String[] otherNames = { "Select your Skilling Teleport", "Slayer Masters", "Lands End", "Skillers Cove",
			"Woodcutting Guild", "Farming Patches", "Agility (Gnome)", "Hunter", "Puro Puro", "Mining Guild", "", "",
			"", "", "", "", "", "", "", "", "" };

	public void loadMonsterTab() {
		int[] ids = { 44505, 40505, 40506, 40507, 40508, 40509, 40510, 40511, 40512, 40513, 40514, 40515, 40516, 40517,
				40518, 40519, 40520, 40521/* , 65085, 65087, 65089 */ };
		for (int j = 0; j <= 17; j++) {
			c.getPA().sendFrame126(monsterNames[j], ids[j]);
		}
	}

	public void loadMinigameTab() {
		int[] ids = { 44505, 40505, 40506, 40507, 40508, 40509, 40510, 40511, 40512, 40513, 40514, 40515, 40516, 40517,
				40518, 40519, 40520, 40521/* , 65085, 65087, 65089 */ };
		for (int j = 0; j <= 17; j++) {
			c.getPA().sendFrame126(minigameNames[j], ids[j]);
		}
	}

	public void loadBossTab() {
		int[] ids = { 44505, 40505, 40506, 40507, 40508, 40509, 40510, 40511, 40512, 40513, 40514, 40515, 40516, 40517,
				40518, 40519, 40520, 40521/* , 65085, 65087, 65089 */ };
		for (int j = 0; j <= 17; j++) {
			c.getPA().sendFrame126(bossNames[j], ids[j]);
		}
	}

	public void loadWildernessTab() {
		int[] ids = { 44505, 40505, 40506, 40507, 40508, 40509, 40510, 40511, 40512, 40513, 40514, 40515, 40516, 40517,
				40518, 40519, 40520, 40521/* , 65085, 65087, 65089 */ };
		for (int j = 0; j <= 17; j++) {
			c.getPA().sendFrame126(wildernessNames[j], ids[j]);
		}
	}

	public void loadCityTab() {
		int[] ids = { 44505, 40505, 40506, 40507, 40508, 40509, 40510, 40511, 40512, 40513, 40514, 40515, 40516, 40517,
				40518, 40519, 40520, 40521/* , 65085, 65087, 65089 */ };
		for (int j = 0; j <= 17; j++) {
			c.getPA().sendFrame126(cityNames[j], ids[j]);
		}
	}

	public void loadDonatorTab() {
		int[] ids = { 44505, 40505, 40506, 40507, 40508, 40509, 40510, 40511, 40512, 40513, 40514, 40515, 40516, 40517,
				40518, 40519, 40520, 40521/* , 65085, 65087, 65089 */ };
		for (int j = 0; j <= 17; j++) {
			c.getPA().sendFrame126(donatorNames[j], ids[j]);
		}
	}

	public void loadOtherTab() {
		int[] ids = { 44505, 40505, 40506, 40507, 40508, 40509, 40510, 40511, 40512, 40513, 40514, 40515, 40516, 40517,
				40518, 40519, 40520, 40521/* , 65085, 65087, 65089 */ };
		for (int j = 0; j <= 17; j++) {
			c.getPA().sendFrame126(otherNames[j], ids[j]);
		}
	}

	public void loadTab(Player player, int tab) {
		if (player.teleSelected == 0) {
			loadMonsterTab();
		} else if (player.teleSelected == 1) {
			loadMinigameTab();
		} else if (player.teleSelected == 2) {
			loadBossTab();
		} else if (player.teleSelected == 3) {
			loadWildernessTab();
		} else if (player.teleSelected == 4) {
			loadCityTab();
		} else if (player.teleSelected == 5) {
			loadDonatorTab();
		} else if (player.teleSelected == 6) {
			loadOtherTab();
		}
	}

	public void selection(Player player, int i) {
		player.teleSelected = i;
		loadTab(player, i);
	}

	public boolean teleportCheck(Player player) {
		/*
		 * if (!player.modeTut) { player.
		 * sendMessage("You must finish the tutorial before teleporting anywhere.");
		 * return false; }
		 */
		return true;
	}

	public void handleTeleports(Player player, int Id) {
		if (player.inWild() && player.wildLevel > Config.NO_TELEPORT_WILD_LEVEL) {
			// player.sendMessage("You can not open a teleport interface while above level
			// 20 wilderness.");
			return;
		}

		switch (Id) {
		// Handle Magic Book Teleports
		case 4140: // Monsters
		case 50235:
		case 117112:
			// player.getPA().showInterface(65000);
			// selection(player, 0);
			// player.sendMessage("If you wish to teleport, please talk to Wizard Mizgog in
			// Edgeville.");
			break;
		case 4143: // Minigames
		case 50245:
		case 117123:
			// player.getPA().showInterface(65000);
			// selection(player, 1);
			// player.sendMessage("If you wish to teleport, please talk to Wizard Mizgog in
			// Edgeville.");
			break;
		case 4146: // Bosses
		case 50253:
		case 117131:
			// player.getPA().showInterface(65000);
			// selection(player, 2);
			// player.sendMessage("If you wish to teleport, please talk to Wizard Mizgog in
			// Edgeville.");
			break;
		case 4150: // Wilderness
		case 51005:
		case 117154:
			// player.getPA().showInterface(65000);
			// selection(player, 3);
			// player.sendMessage("If you wish to teleport, please talk to Wizard Mizgog in
			// Edgeville.");
			break;
		case 6004: // City
		case 51013:
		case 117162:
			// player.getPA().showInterface(65000);
			// selection(player, 4);
			// player.sendMessage("If you wish to teleport, please talk to Wizard Mizgog in
			// Edgeville.");
			break;
		case 6005: // Donator
		case 51023:
		case 117186:
			// if (player.getRights().isDonator() || player.getRights().isSuperDonator() ||
			// player.getRights().isLegendaryDonator() ||
			// player.getRights().isExtremeDonator() ||
			// c.getRights().isOrInherits(Right.OWNER)) {
			// player.getPA().showInterface(65000);
			// selection( player, 5);
			// } else {
			// player.sendMessage("You must be a donator to use this.");
			// }
			// player.sendMessage("If you wish to teleport, please talk to Wizard Mizgog in
			// Edgeville.");
			break;
		case 29031: // Other
		case 51031:
		case 117194:
			// player.getPA().showInterface(65000);
			// selection(player, 6);
			// player.sendMessage("If you wish to teleport, please talk to Wizard Mizgog in
			// Edgeville.");
			break;
		case 51039:
		case 72038:
		case 117210:
			// player.sendMessage("If you wish to teleport, please talk to Wizard Mizgog in
			// Edgeville.");
			break;

		// Start of Tabs
		case 161033: // Monsters Tab
			selection(player, 0);
			break;
		case 161036: // Minigames Tab
			selection(player, 1);
			break;
		case 165018: // Bosses Tab
			selection(player, 2);
			break;
		case 165015: // Wilderness Tab
			selection(player, 3);
			break;
		case 165024: // City Tab
			selection(player, 4);
			break;
		case 165027: // Donator Tab
			// if (player.getRights().isDonator() || player.getRights().isSuperDonator() ||
			// player.getRights().isLegendaryDonator() ||
			// player.getRights().isExtremeDonator() ||
			// c.getRights().isOrInherits(Right.OWNER))
			selection(player, 5);
			// else
			// player.sendMessage("You must be a donator to use this.");
			break;
		case 165021: // Other Tab
			selection(player, 6);
			break;
		// End of Tabs

		// Start of Buttons
		case 180181: // Button 1
			if (!teleportCheck(player))
				return;
			if (player.teleSelected == 0) { // Monsters - Rock Crabs
				player.getPA().startTeleport(2673, 3710, 0, "modern", false);
			} else if (player.teleSelected == 1) { // Minigames - Raids
				player.getPA().startTeleport(1255, 3562, 0, "modern", false); // change
				// player.sendMessage("Teleporting to "+minigameNames[1]+".");
			} else if (player.teleSelected == 2) { // Bosses - Barrelchest
				player.getPA().startTeleport(1229, 3497, 0, "modern", false);// change
				// player.sendMessage("Teleporting to "+bossNames[1]+".");
			} else if (player.teleSelected == 3) { // Wilderness - Green Dragons
				player.getPA().startTeleport(2976, 3591, 0, "modern", false);
				// player.sendMessage("Teleporting to "+wildernessNames[1]+".");
			} else if (player.teleSelected == 4) { // City - Varrock
				player.getPA().startTeleport(3210, 3424, 0, "modern", false);
				// player.sendMessage("Teleporting to "+cityNames[1]+".");
			} else if (player.teleSelected == 5) { // Donator - Donator Lobby
				player.getPA().startTeleport(1661, 10049, 0, "modern", false);
			} else if (player.teleSelected == 6) { // Other - Slayer Masters
				player.getPA().startTeleport(3107, 3507, 0, "modern", false); // change
				// player.sendMessage("Teleporting to "+otherNames[1]+".");
			}
			break;
		case 180184: // Button 2
			if (!teleportCheck(player))
				return;
			if (player.teleSelected == 0) { // Monster - Cow
				player.getPA().startTeleport(3260, 3272, 0, "modern", false);
				// player.sendMessage("Teleporting to "+monsterNames[2]+".");
			} else if (player.teleSelected == 1) { // Minigames - Warriors Guild
				player.getPA().startTeleport(2874, 3546, 0, "modern", false);
				// player.sendMessage("Teleporting to "+minigameNames[2]+".");
			} else if (player.teleSelected == 2) { // Bosses - Daggononoth Kings
				player.getPA().startTeleport(1913, 4367, 0, "modern", false);
				// player.sendMessage("Teleporting to "+bossNames[2]+".");
			} else if (player.teleSelected == 3) { // Wilderness - Mage Bank
				player.getPA().startTeleport(2539, 4716, 0, "modern", false);
				// player.sendMessage("Teleporting to "+wildernessNames[2]+".");
			} else if (player.teleSelected == 4) { // City - Yanille
				player.getPA().startTeleport(2606, 3093, 0, "modern", false);
				// player.sendMessage("Teleporting to "+cityNames[2]+".");
			} else if (player.teleSelected == 5) { // Donator - Basic Slayer
				player.getPA().startTeleport(2807, 10003, 0, "modern", false);
			} else if (player.teleSelected == 6) { // Other - Lands end
				player.getPA().startTeleport(1504, 3419, 0, "modern", false); // change
				// player.sendMessage("Teleporting to "+otherNames[2]+".");
			}
			break;

		case 180188:
			if (!teleportCheck(player))
				return;
			if (player.teleSelected == 0) { // Monster - Yaks
				player.getPA().startTeleport(2326, 3801, 0, "modern", false);
				// player.sendMessage("Teleporting to "+monsterNames[3]+".");
			} else if (player.teleSelected == 1) { // Minigames - Pest Control
				player.getPA().startTeleport(2660, 2648, 0, "modern", false);
				// player.sendMessage("Teleporting to "+minigameNames[3]+".");
				player.getDiaryManager().getWesternDiary().progress(WesternDiaryEntry.PEST_CONTROL_TELEPORT);
			} else if (player.teleSelected == 2) { // Bosses - King Black Dragon
				player.getPA().startTeleport(3005, 3849, 0, "modern", false);
				// player.sendMessage("Teleporting to "+bossNames[3]+".");
			} else if (player.teleSelected == 3) { // Wilderness - Dark Castle
				player.getPA().startTeleport(3020, 3632, 0, "modern", false);
				// player.sendMessage("Teleporting to "+wildernessNames[3]+".");
			} else if (player.teleSelected == 4) { // City - Edgeville
				player.getPA().startTeleport(3093, 3493, 0, "modern", false);
				// player.sendMessage("Teleporting to "+cityNames[3]+".");
			} else if (player.teleSelected == 5) { // Super - Advanced Slayer
				player.getPA().startTeleport(2883, 9800, 0, "modern", false);
			} else if (player.teleSelected == 6) { // Other - Skillers Cove
				player.getPA().startTeleport(1721, 3464, 0, "modern", false);
				// player.sendMessage("Teleporting to "+otherNames[3]+".");
			}
			break;
		case 180191:
			if (!teleportCheck(player))
				return;
			if (player.teleSelected == 0) { // Monsters - Bob's Island
				player.getPA().startTeleport(2524, 4775, 0, "modern", false);
				// player.sendMessage("Teleporting to "+monsterNames[4]+".");
			} else if (player.teleSelected == 1) { // Minigames - Fight Caves
				player.getPA().startTeleport(2444, 5179, 0, "modern", false);
				// player.sendMessage("Teleporting to "+minigameNames[4]+".");
				player.sendMessage("The minigame entrance can be found to the south!");
			} else if (player.teleSelected == 2) { // Bosses - Giant Mole
				player.getPA().startTeleport(3078, 3924, 0, "modern", false);
				// player.sendMessage("Teleporting to "+bossNames[4]+".");
				// player.sendMessage("Right click and Look-inside mole hills to fight the Giant
				// Mole!");
			} else if (player.teleSelected == 3) { // Wilderness - Hill Giants Multi
				player.getPA().startTeleport(3304, 3657, 0, "modern", false);
				// player.sendMessage("Teleporting to "+wildernessNames[4]+".");
			} else if (player.teleSelected == 4) { // City - Lumbridge
				player.getPA().startTeleport(3222, 3218, 0, "modern", false);
				// player.sendMessage("Teleporting to "+cityNames[4]+".");
				c.getDiaryManager().getLumbridgeDraynorDiary().progress(LumbridgeDraynorDiaryEntry.LUMBRIDGE_TELEPORT);
			} else if (player.teleSelected == 5) { // Super - Basic Skilling
				player.getPA().startTeleport(3066, 9544, 0, "modern", false);
			} else if (player.teleSelected == 6) { // Other - Woodcutting Guild
				player.getPA().startTeleport(1658, 3505, 0, "modern", false);
				// player.sendMessage("Teleporting to "+otherNames[4]+".");
			}
			break;
		case 180194:
			if (!teleportCheck(player))
				return;
			if (player.teleSelected == 0) { // Monsters - Desert Bandit
				player.getPA().startTeleport(3176, 2987, 0, "modern", false);
				// player.sendMessage("Teleporting to "+monsterNames[5]+".");
			} else if (player.teleSelected == 1) { // Minigames - Barrows
				player.getPA().startTeleport(3565, 3316, 0, "modern", false);
				// player.sendMessage("Teleporting to "+minigameNames[5]+".");
			} else if (player.teleSelected == 2) { // Bosses - Kalphite Queen Tunnels
				player.getPA().startTeleport(3510, 9496, 2, "modern", false);
				// player.sendMessage("Teleporting to "+bossNames[5]+".");
			} else if (player.teleSelected == 3) { // Wilderness - Wilderness Agility Course
				player.getPA().startTeleport(3003, 3934, 0, "modern", false);
				// player.sendMessage("Teleporting to "+wildernessNames[5]+".");
			} else if (player.teleSelected == 4) { // City - Ardougne
				player.getPA().startTeleport(2662, 3305, 0, "modern", false);
				// player.sendMessage("Teleporting to "+cityNames[5]+".");
				c.getDiaryManager().getArdougneDiary().progress(ArdougneDiaryEntry.TELEPORT_ARDOUGNE);
			} else if (player.teleSelected == 5) { // Super - Demons
				player.getPA().startTeleport(3048, 9582, 0, "modern", false);
			} else if (player.teleSelected == 6) { // Other - Farming Patches
				player.getPA().startTeleport(3003, 3376, 0, "modern", false);
				// player.sendMessage("Teleporting to "+otherNames[5]+".");
			}
			break;
		case 180197:
			if (!teleportCheck(player))
				return;
			if (player.teleSelected == 0) { // Monsters - Elf Warrior
				player.getPA().startTeleport(2897, 2725, 0, "modern", false);
				// player.sendMessage("Teleporting to "+monsterNames[6]+".");
			} else if (player.teleSelected == 1) { // Minigames - Clan Wars
				player.getPA().startTeleport(3387, 3158, 0, "modern", false);
				// player.sendMessage("Teleporting to "+minigameNames[6]+".");
			} else if (player.teleSelected == 2) { // Bosses - God Wars Dungeon
				player.getPA().startTeleport(2881, 5310, 2, "modern", false);
				// player.sendMessage("Teleporting to "+bossNames[6]+".");
			} else if (player.teleSelected == 3) { // Wilderness - Vet'ion
				player.getPA().startTeleport(3200, 3794, 0, "modern", false);
				// player.sendMessage("Teleporting to "+wildernessNames[6]+".");
			} else if (player.teleSelected == 4) { // City - Neitiznot
				player.getPA().startTeleport(2321, 3804, 0, "modern", false);
				// player.sendMessage("Teleporting to "+cityNames[6]+".");
			} else if (player.teleSelected == 5) { // Extreme - Advanced Skilling
				player.getPA().startTeleport(2710, 9466, 0, "modern", false);
			} else if (player.teleSelected == 6) { // Other - Agility - Grace
				player.getPA().startTeleport(2480, 3437, 0, "modern", false);
				// player.sendMessage("Teleporting to "+otherNames[6]+".");
			}
			break;
		case 180200:
			if (!teleportCheck(player))
				return;
			if (player.teleSelected == 0) { // Monsters - Dagannoths
				player.getPA().startTeleport(2442, 10147, 0, "modern", false);
				// player.sendMessage("Teleporting to "+monsterNames[7]+".");
			} else if (player.teleSelected == 1) { // Minigames - Shayzien Assault
				player.getPA().startTeleport(1461, 3689, 0, "modern", false);
				// player.sendMessage("Teleporting to "+minigameNames[7]+".");
			} else if (player.teleSelected == 2) { // Bosses - Corporeal Beast
				player.getPA().startTeleport(2964, 4382, 2, "modern", false);
				// player.sendMessage("Teleporting to "+bossNames[7]+".");
			} else if (player.teleSelected == 3) { // Wilderness - Callisto
				player.getPA().startTeleport(3325, 3845, 0, "modern", false);
				// player.sendMessage("Teleporting to "+wildernessNames[7]+".");
			} else if (player.teleSelected == 4) { // City - Karamja
				player.getPA().startTeleport(2948, 3147, 0, "modern", false);
				// player.sendMessage("Teleporting to "+cityNames[7]+".");
			} else if (player.teleSelected == 5) { // Extreme - Dragons(Hides)

			} else if (player.teleSelected == 6) { // Other - Hunter
				player.getPA().startTeleport(1580, 3437, 0, "modern", false);
				// player.sendMessage("Teleporting to "+otherNames[7]+".");
			}
			break;
		case 180203:
			if (!teleportCheck(player))
				return;
			if (player.teleSelected == 0) { // Monsters - Chickens
				player.getPA().startTeleport(1740, 5342, 0, "modern", false);
				// player.sendMessage("Teleporting to "+monsterNames[8]+".");
			} else if (player.teleSelected == 1) { // Minigames - Mage Arena
				player.getPA().startTeleport(2541, 4716, 0, "modern", false);
				// player.sendMessage("Teleporting to "+minigameNames[8]+".");
			} else if (player.teleSelected == 2) { // Bosses - Daggonoth Mother
				player.getPA().startTeleport(2508, 3643, 0, "modern", false);
				 player.sendMessage("Enter underground to fight boss. Buy the books with the rusty casket she drops.");
			} else if (player.teleSelected == 3) { // Wilderness - Scorpia
				player.getPA().startTeleport(3233, 3945, 0, "modern", false);
				// player.sendMessage("Teleporting to "+wildernessNames[8]+".");
			} else if (player.teleSelected == 4) { // City - Falador
				player.getPA().startTeleport(2964, 3378, 0, "modern", false);
				// player.sendMessage("Teleporting to "+cityNames[8]+".");
				c.getDiaryManager().getFaladorDiary().progress(FaladorDiaryEntry.TELEPORT_TO_FALADOR);
			} else if (player.teleSelected == 5) { // Extreme - Dragons(Metal)

			} else if (player.teleSelected == 6) { // Other - Puro Puro
				if (WheatPortalEvent.xLocation > 0 && WheatPortalEvent.yLocation > 0) {
					player.getPA().spellTeleport(WheatPortalEvent.xLocation + 1, WheatPortalEvent.yLocation + 1, 0,
							false);
				} else {
					player.sendMessage("There is currently no portal available, wait 5 minutes.");
					return;
				}
			}
			break;
		case 180206:
			if (!teleportCheck(player))
				return;
			if (player.teleSelected == 0) { // Monsters - Slayer Tower
				player.getPA().startTeleport(3428, 3538, 0, "modern", false);
				// player.sendMessage("Teleporting to "+monsterNames[9]+".");
			} else if (player.teleSelected == 1) { // Minigames - Duel Arena
				player.getPA().startTeleport(3365, 3266, 0, "modern", false);
				// player.sendMessage("Teleporting to "+minigameNames[9]+".");
			} else if (player.teleSelected == 2) { // Bosses - Kraken
				player.getPA().startTeleport(2280, 10016, 0, "modern", false);
				// player.sendMessage("Teleporting to "+bossNames[9]+".");
			} else if (player.teleSelected == 3) { // Wilderness - Venenatis
				player.getPA().startTeleport(3345, 3754, 0, "modern", false);
				// player.sendMessage("Teleporting to "+wildernessNames[9]+".");
			} else if (player.teleSelected == 4) { // City - Taverly
				player.getPA().startTeleport(2928, 3451, 0, "modern", false);
				// player.sendMessage("Teleporting to "+cityNames[9]+".");
			} else if (player.teleSelected == 5) { // Extreme - Smoke Devil

			} else if (player.teleSelected == 6) { // Other - Mining Guild
				player.getPA().startTeleport(3046, 9756, 0, "modern", false);
				// player.sendMessage("Teleporting to "+otherNames[9]+".");
			}
			break;
		case 180212:
			if (!teleportCheck(player))
				return;
			if (player.teleSelected == 0) {
				player.getPA().startTeleport(2883, 9800, 0, "modern", false);
			} else if (player.teleSelected == 3) {
				player.getPA().startTeleport(2978, 3833, 0, "modern", false);
			} else if (player.teleSelected == 2) {
				player.getPA().startTeleport(1310, 1237, 0, "modern", false);
			} else if (player.teleSelected == 4) {
				player.getPA().startTeleport(2804, 3432, 0, "modern", false);
			}
			break;
		case 180215:
			if (!teleportCheck(player))
				return;
			if (player.teleSelected == 0) {
				player.getPA().startTeleport(2452, 9820, 0, "modern", false);
			} else if (player.teleSelected == 3) {
				player.getPA().startTeleport(2979, 3697, 0, "modern", false);
			} else if (player.teleSelected == 2) {
				player.getPA().startTeleport(2404, 9415, 0, "modern", false);
			} else if (player.teleSelected == 4) {
				player.getPA().startTeleport(3293, 3179, 0, "modern", false);
			}
			break;
		case 180221:
			if (player.teleSelected == 2) {
				player.getPA().startTeleport(2124, 5660, 0, "modern", false);
			}
			break;
		case 180224:
			if (player.teleSelected == 2) {
				player.getPA().startTeleport(1558, 3696, 0, "modern", false);
			}
			break;
		case 180227: //vorkath
			player.getPA().startTeleport(2272, 4050, 0, "modern", false);
			//player.sendMessage("This has been temporarily disabled");
			break;
		case 180218:
			if (!teleportCheck(player))
				return;
			if (player.teleSelected == 0) {
				player.getPA().startTeleport(2404, 9415, 0, "modern", false);
			} else if (player.teleSelected == 2) {
				player.getPA().startTeleport(3037, 4765, 0, "modern", false);
			} else if (player.teleSelected == 4) {
				player.getPA().startTeleport(3293, 3179, 0, "modern", false);
			}
			break;
		case 180209:
			if (!teleportCheck(player))
				return;
			if (player.teleSelected == 0) { // Monsters - Brimhaven Dungasgarnianeon
				player.getPA().startTeleport(2124, 5660, 0, "modern", false);
				// player.sendMessage("Teleporting to "+monsterNames[10]+".");
			} else if (player.teleSelected == 2) { // Bosses - Zulrah - Instanced
				player.getPA().startTeleport(2202, 3056, 0, "modern", false);
				// player.sendMessage("Teleporting to "+bossNames[10]+".");
			} else if (player.teleSelected == 3) { // Wilderness - Chaos Elemental
				player.getPA().startTeleport(3281, 3910, 0, "modern", false);
				// player.sendMessage("Teleporting to "+wildernessNames[10]+".");
			} else if (player.teleSelected == 4) { // City - Camelot
				player.getPA().startTeleport(2757, 3477, 0, "modern", false);
				// player.sendMessage("Teleporting to "+cityNames[10]+".");
				c.getDiaryManager().getKandarinDiary().progress(KandarinDiaryEntry.CAMELOT_TELEPORT);
			} else if (player.teleSelected == 5) { // Legendary - Skeletal Wyverns

			}
			break;
		case 254046:
			if (!teleportCheck(player))
				return;
			if (player.teleSelected == 0) { // Monsters - Taverley Dungeon
				player.getPA().startTeleport(2884, 9796, 0, "modern", false);
				// player.sendMessage("Teleporting to "+monsterNames[11]+".");
			} else if (player.teleSelected == 2) { // Bosses - Cerberus
				player.getPA().startTeleport(2873, 9847, 0, "modern", false);
				// player.sendMessage("Teleporting to "+bossNames[11]+".");
			} else if (player.teleSelected == 3) { // Wilderness - Chaos Fanatic
				player.getPA().startTeleport(2981, 3836, 0, "modern", false);
				// player.sendMessage("Teleporting to "+wildernessNames[11]+".");
			} else if (player.teleSelected == 4) { // City - Catherby
				player.getPA().startTeleport(2813, 3447, 0, "modern", false);
				// player.sendMessage("Teleporting to "+cityNames[11]+".");
				c.getDiaryManager().getKandarinDiary().progress(KandarinDiaryEntry.CATHERY_TELEPORT);
			} else if (player.teleSelected == 5) { // Legendary - Fanatic/Archaeologist

			}
			break;
		case 254048:
			if (!teleportCheck(player))
				return;
			if (player.teleSelected == 0) { // Monsters - Stronghold Slayer Dungeon
				player.getPA().startTeleport(2432, 3423, 0, "modern", false);
				// player.sendMessage("Teleporting to "+monsterNames[12]+".");
			} else if (player.teleSelected == 2) { // Bosses - Thermonuclear Smoke Devil
				if (player.playerLevel[18] < 93) {
					player.sendMessage("You need a Slayer level of 93 to kill these.");
					return;
				}
				player.getPA().startTeleport(2376, 9452, 0, "modern", false);
				// player.sendMessage("Teleporting to "+bossNames[12]+".");
			} else if (player.teleSelected == 3) { // Wilderness - Crazy Archaeologist
				player.getPA().startTeleport(2984, 3713, 0, "modern", false);
				// player.sendMessage("Teleporting to "+wildernessNames[12]+".");
			} else if (player.teleSelected == 4) { // City - Al Kharid
				player.getPA().startTeleport(3293, 3174, 0, "modern", false);
				// player.sendMessage("Teleporting to "+cityNames[12]+".");
			} /*
				 * else if (c.getRights().isOrInherits(Right.VIP) ||
				 * c.getRights().isOrInherits(Right.OWNER)) { if (player.teleSelected == 5) { //
				 * Legendary - Runecrafting player.getPA().showInterface(26100);
				 * //player.getPA().startTeleport(1992,4530, 3, "modern");
				 * player.sendMessage("Opening "+donatorNames[12]+" Interface."); } }
				 */
			break;
		case 254050:
			if (!teleportCheck(player))
				return;
			if (player.teleSelected == 0) { // Monsters - Relleka Dungeon
				player.getPA().startTeleport(2808, 10002, 0, "modern", false);
				// player.sendMessage("Teleporting to "+monsterNames[13]+".");
			} else if (player.teleSelected == 2) { // Bosses - Abyssal Sire
				player.getPA().startTeleport(3039, 4788, 0, "modern", false);
				// player.sendMessage("Teleporting to "+bossNames[13]+".");
			} else if (player.teleSelected == 3) { // Wilderness - Skeletal Wyverns
				player.getPA().startTeleport(2963, 3916, 0, "modern", false);
				// player.sendMessage("Teleporting to "+wildernessNames[13]+".");
			} else if (player.teleSelected == 4) { // City - Morytania
				player.getPA().startTeleport(3432, 3451, 0, "modern", false);
				// player.sendMessage("Teleporting to "+cityNames[13]+".");
				c.getDiaryManager().getMorytaniaDiary().progress(MorytaniaDiaryEntry.MORYTANIA_SWAMP);
			}
			break;
		case 254052:
			if (!teleportCheck(player))
				return;
			if (player.teleSelected == 0) { // Monsters - Mithril Dragons
				player.getPA().startTeleport(1746, 5323, 0, "modern", false);
				// player.sendMessage("Teleporting to "+monsterNames[14]+".");
			} else if (player.teleSelected == 2) { // Bosses - Demonic Gorillas
				player.getPA().startTeleport(2130, 5647, 0, "modern", false);
				// player.sendMessage("Teleporting to "+bossNames[14]+".");
			} else if (player.teleSelected == 3) { // Wilderness - East Dragons
				player.getPA().startTeleport(3351, 3659, 0, "modern", false);
				// player.sendMessage("Teleporting to "+wildernessNames[14]+".");
			} else if (player.teleSelected == 4) { // City - Shilo Village
				player.getPA().startTeleport(2827, 2995, 0, "modern", false);
				// player.sendMessage("Teleporting to "+cityNames[14]+".");
			}
			break;
		case 254054:
			if (!teleportCheck(player))
				return;
			if (player.teleSelected == 0) { // Monsters - Asgarnian Ice Cave
				player.getPA().startTeleport(3029, 9582, 0, "modern", false);
				// player.sendMessage("Teleporting to "+monsterNames[15]+".");
			} else if (player.teleSelected == 2) { // Bosses - Lizardman Shaman
				player.getPA().startTeleport(1469, 3687, 0, "modern", false);
				// player.sendMessage("Teleporting to "+bossNames[15]+".");
			} else if (player.teleSelected == 3) { // Wilderness - Wildy Volcano
				player.getPA().startTeleport(3366, 3935, 0, "modern", false);
				// player.sendMessage("Teleporting to "+wildernessNames[15]+".");
			} else if (player.teleSelected == 4) { // City - Waterbirth Tele
				player.getPA().startTeleport(2508, 3725, 0, "modern", false);
				// player.sendMessage("Teleporting to "+cityNames[15]+".");
				c.getDiaryManager().getFremennikDiary().progress(FremennikDiaryEntry.WATERBIRTH_TELEPORT);
			}
			break;
		case 254056:
			if (!teleportCheck(player))
				return;
			if (player.teleSelected == 0) { // Monsters - Catacombs
				player.getPA().startTeleport(1630, 3673, 0, "modern", false);
				// player.sendMessage("Teleporting to "+monsterNames[16]+".");
				player.sendMessage("Click on the statue to enter the Catacombs!");
			} else if (player.teleSelected == 3) { // Wilderness - Chaos Altar
				player.getPA().startTeleport(3236, 3628, 0, "modern", false);
				// player.sendMessage("Teleporting to "+wildernessNames[16]+".");
			} else if (player.teleSelected == 4) { // City - Lletya
				player.getPA().startTeleport(2352, 3162, 0, "modern", false);
				// player.sendMessage("Teleporting to "+cityNames[16]+".");
			}
			break;
		case 254058:
			if (!teleportCheck(player))
				return;
			if (player.teleSelected == 0) { // Monsters - Cave Kraken
				player.getPA().startTeleport(2277, 10001, 0, "modern", false);
				// player.sendMessage("Teleporting to "+monsterNames[17]+".");
			} else if (player.teleSelected == 3) { // Wilderness - Lava Dragons
				player.getPA().startTeleport(3202, 3860, 0, "modern", false);
				// player.sendMessage("Teleporting to "+wildernessNames[17]+".");
			} else if (player.teleSelected == 4) { // City - Brimhaven
				player.getPA().startTeleport(2898, 3546, 0, "modern", false);
				// player.sendMessage("Teleporting to "+cityNames[17]+".");
			}
			break;
		case 254060:
			if (!teleportCheck(player))
				return;
			if (player.teleSelected == 0) { // Monsters - Smoke Devils
				if (player.playerLevel[18] < 93) {
					player.sendMessage("You need a Slayer level of 93 to kill these.");
					return;
				}
				player.getPA().startTeleport(2404, 9415, 0, "modern", false);
				// player.sendMessage("Teleporting to "+monsterNames[18]+".");
			} else if (player.teleSelected == 4) { // City - Entrana
				player.getPA().startTeleport(2827, 3336, 0, "modern", false);
				// player.sendMessage("Teleporting to "+cityNames[18]+".");
			}
		case 254062:
			if (!teleportCheck(player))
				return;
			if (player.teleSelected == 4) { // City - Draynor
				player.getPA().startTeleport(3077, 3252, 0, "modern", false);
				// player.sendMessage("Teleporting to "+cityNames[19]+".");
			}
			break;
		case 254064:
			if (!teleportCheck(player))
				return;
			if (player.teleSelected == 4) { // City - Trollheim
				player.getPA().startTeleport(2911, 3612, 0, "modern", false);
				// player.sendMessage("Teleporting to "+cityNames[20]+".");
				c.getDiaryManager().getFremennikDiary().progress(FremennikDiaryEntry.TROLLHEIM_TELEPORT);
			}
			break;
		// End of Buttons
		}
	}
}