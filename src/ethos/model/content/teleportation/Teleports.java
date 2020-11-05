package ethos.model.content.teleportation;

import ethos.model.npcs.NPC;
import ethos.model.npcs.NPCHandler;
import ethos.model.players.Player;

public class Teleports {
	
	public static enum teleports {
		
		Teleport(new TeleportOption("@cr10@@bla@Zeah Regions", 0, 0, 0),
				new TeleportOption("Hosidius House", 1676, 3565, 0),
				new TeleportOption("Piscarilius House", 1802, 3778, 0),
				new TeleportOption("Shayzien House", 1504, 3620, 0),
				new TeleportOption("@cr22@Lovakengj House", 1498, 3809, 0),
				new TeleportOption("Catacombs of Kourend", 1629, 3672, 0),
				new TeleportOption("Mount Quidamortem", 1254, 3567, 0),
				new TeleportOption("Shayzien's Wall", 1402, 3536, 0),
				new TeleportOption("", 0, 0, 0),
				new TeleportOption("@cr10@@bla@Low Level Training", 0, 0, 0),
				new TeleportOption("Bob's Training Island(Cb 70+)", 2525, 4776, 0),
				new TeleportOption("Chickens", 1655, 3575, 0),
				new TeleportOption("Cows", 1611, 3629, 0),
				new TeleportOption("Rock Crabs", 1777, 3707, 0),
				new TeleportOption("", 0, 0, 0),
				new TeleportOption("@cr10@@bla@Skilling", 0, 0, 0),
				new TeleportOption("Land's End(Hunter, Craft)", 1504, 3423, 0),
				new TeleportOption("Skiller's Cove", 1719, 3463, 0),
				new TeleportOption("Woodcutting Guild", 1658, 3505, 0),
				new TeleportOption("Farming Patches", 3003, 3376, 0),
				new TeleportOption("Agility", 1647, 3681, 0),
				new TeleportOption("", 0, 0, 0),
				new TeleportOption("@cr10@@bla@Dungeons", 0, 0, 0),
				new TeleportOption("Asgarnian Ice Cave", 3029, 9582, 0),
				new TeleportOption("Brimhaven Dungeon", 1571, 3656, 0),
				new TeleportOption("Taverly Dungeon", 1662, 3529, 0),
				new TeleportOption("Relleka Dungeon", 1260, 3502, 0),
				new TeleportOption("Stronghold Slayer Cave", 1277, 3552, 0),
				new TeleportOption("Slayer Dungeon(Slayer Tower)", 1650, 3665, 0),
				new TeleportOption("Kalphite Cave", 1845, 3809, 0),
				new TeleportOption("", 0, 0, 0),
				new TeleportOption("@cr10@@bla@Minigames", 0, 0, 0),
				new TeleportOption("Raids", 3301, 5192, 0),
				new TeleportOption("Barrows", 1483, 3552, 0),
				new TeleportOption("Duel Arena", 3367, 3266, 0),
				new TeleportOption("Fight Caves(Jad)", 1460, 3690, 0),
				new TeleportOption("Pest Control", 2660, 2648, 0),
				new TeleportOption("Shayzien Assault", 1461, 3689, 0),
				new TeleportOption("Warriors Guild", 2865, 3547, 0),
				new TeleportOption("", 0, 0, 0),
				new TeleportOption("@cr10@@bla@Bosses", 0, 0, 0),
				new TeleportOption("Barrelchest", 1229, 3497, 0),
				new TeleportOption("@cr22@Callisto", 3325, 3845, 0),
				new TeleportOption("@cr22@Chaos Fanatic", 2993, 3854, 0),
				new TeleportOption("@cr22@Chaos Elemental", 1189, 3501, 0),
				new TeleportOption("@cr22@Crazy Archaeologist", 2991, 3695, 0),
				new TeleportOption("Dagannoth Kings", 1912, 4367, 0),
				new TeleportOption("Demonic Gorillas", 1349, 3590, 0),
				new TeleportOption("God Wars Dungeon", 1421, 3586, 0),
				new TeleportOption("@cr22@King Black Dragon", 2271, 4680, 0),
				new TeleportOption("Lizardman Shaman", 1470, 3687, 0),
				new TeleportOption("@cr22@Scorpia", 3233, 3947, 0),
				new TeleportOption("@cr22@Vet'ion", 3200, 3794, 0),
				new TeleportOption("@cr22@Venenatis", 3339, 3756, 0),
				new TeleportOption("Zulrah", 2200, 3055, 0),
				new TeleportOption("", 0, 0, 0),
				new TeleportOption("@cr10@@cr22@@bla@Wilderness", 0, 0, 0),
				new TeleportOption("@cr22@Dwarf Cave", 3246, 3794, 0),
				new TeleportOption("@cr22@East Dragons", 3357, 3674, 0),
				new TeleportOption("@cr22@Lava Dragons", 3204, 3857, 0),
				new TeleportOption("@cr22@Mage Bank", 2539, 4716, 0),
				new TeleportOption("@cr22@West Dragons", 2977, 3603, 0));

		
		private TeleportOption[] teleports;
		
		private teleports(TeleportOption... o) {
			this.teleports = o;
		}
		
		public TeleportOption[] getTeleports() {
			return teleports;
		}
	}

	public static void openUpInterface(Player player) {
		//player.getPA().showInterface(62100);
		int startId = 62107;
		for (final teleports t : teleports.values()) {
			for(int i = 0; i < t.getTeleports().length; i++) {
				TeleportOption tele = t.getTeleports()[i];
				player.getPA().sendFrame126("<shad=-1>"+tele.getName(), startId);
				startId++;
			}
		}
		for(int i = startId; i < 62197; i++) {
			player.getPA().sendFrame126("", i);
		}
	}
	
	public static void teleportButtons(Player player, int buttonId) {
		switch(buttonId) {
			//case 242155: Title
			case 242156:
				startTeleport(player, 1);
				break;
			case 242157:
				startTeleport(player, 2);
				break;
			case 242158:
				startTeleport(player, 3);
				break;
			case 242159:
				startTeleport(player, 4);
				break;
			case 242160:
				startTeleport(player, 5);
				break;
			case 242161:
				startTeleport(player, 6);
				break;
			case 242162:
				startTeleport(player, 7);
				break;
			//case 242163:
			//	startTeleport(player, 8);
			//	break;				
			//case 242164:
				//startTeleport(player, 9);
				//break;		
			case 242165:
				startTeleport(player, 10);
				break;	
			case 242166:
				startTeleport(player, 11);
				break;	
			case 242167:
				startTeleport(player, 12);
				break;	
			case 242168:
				startTeleport(player, 13);
				break;	
			//case 242169:
			//	startTeleport(player, 14);
			//	break;	
			//case 242170:
			//	startTeleport(player, 15);
			//	break;	
			case 242171:
				startTeleport(player, 16);
				break;	
			case 242172:
				startTeleport(player, 17);
				break;	
			case 242173:
				startTeleport(player, 18);
				break;
			case 242174:
				startTeleport(player, 19);
				break;	
			case 242175:
				startTeleport(player, 20);
				break;	
			//case 242176:
			//	startTeleport(player, 21);
			//	break;	
			//case 242177:
			//	startTeleport(player, 22);
			//	break;	
			case 242178:
				startTeleport(player, 23);
				break;
			case 242179:
				startTeleport(player, 24);
				break;	
			case 242180:
				startTeleport(player, 25);
				break;	
			case 242181:
				startTeleport(player, 26);
				break;	
			case 242182:
				startTeleport(player, 27);
				break;
			case 242183:
				startTeleport(player, 28);
				break;	
			case 242184:
				startTeleport(player, 29);
				break;	
			//case 242185:
			//	startTeleport(player, 30);
			//	break;	
			//case 242186:
			//	startTeleport(player, 31);
			//	break;	
			case 242187:
				startTeleport(player, 32);
				break;	
			case 242188:
				startTeleport(player, 33);
				break;	
			case 242189:
				startTeleport(player, 34);
				break;	
			case 242190:
				startTeleport(player, 35);
				break;	
			case 242191:
				startTeleport(player, 36);
				break;	
			case 242192:
				startTeleport(player, 37);
				break;	
			case 242193:
				startTeleport(player, 38);
				break;	
			//case 242194:
			//	startTeleport(player, 39);
			//	break;	
			//case 242195:
			//	startTeleport(player, 40);
			//	break;	
			case 242196:
				startTeleport(player, 41);
				break;	
			case 242197:
				startTeleport(player, 42);
				break;
			case 242198:
				startTeleport(player, 43);
				break;	
			case 242199:
				startTeleport(player, 44);
				break;	
			case 242200:
				startTeleport(player, 45);
				break;	
			case 242201:
				startTeleport(player, 46);
				break;	
			case 242202:
				startTeleport(player, 47);
				break;	
			case 242203:
				startTeleport(player, 48);
				break;	
			case 242204:
				startTeleport(player, 49);
				break;	
			case 242205:
				startTeleport(player, 50);
				break;	
			case 242206:
				startTeleport(player, 51);
				break;	
			case 242207:
				startTeleport(player, 52);
				break;	
			case 242208:
				startTeleport(player, 53);
				break;	
			case 242209:
				startTeleport(player, 54);
				break;	
			//case 242210:
			//	startTeleport(player, 55);
			//	break;	
			//case 242211:
			//	startTeleport(player, 56);
			//	break;	
			case 242212:
				startTeleport(player, 57);
				break;	
			case 242213:
				startTeleport(player, 58);
				break;	
			case 242214:
				startTeleport(player, 59);
				break;	
			case 242215:
				startTeleport(player, 60);
				break;	
			case 242216:
				startTeleport(player, 61);
				break;	
		
		}
	}
	
	public static void startTeleport(Player player, int id) {
		String type = player.playerMagicBook == 1 ? "ancient" : "modern";
		for (final teleports t : teleports.values()) {
			if(id > t.getTeleports().length) return;
			final NPC WIZARD = NPCHandler.getNpc(5314);
			WIZARD.facePlayer(player.getIndex());
			WIZARD.startAnimation(1818);
			WIZARD.gfx0(343);
			WIZARD.forceChat("Senventior Disthine Molenko!");
			TeleportOption tele = t.getTeleports()[id];
			player.getPA().startTeleport(tele.getX(), tele.getY(), tele.getZ(), type, false);
			player.getPA().removeAllWindows();
		}
	}
}