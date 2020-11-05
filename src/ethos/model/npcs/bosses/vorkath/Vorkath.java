package ethos.model.npcs.bosses.vorkath;

import ethos.Server;
import ethos.event.CycleEvent;
import ethos.event.CycleEventContainer;
import ethos.event.CycleEventHandler;
import ethos.model.npcs.NPC;
import ethos.model.players.Player;
import ethos.util.Misc;

public class Vorkath {

	public static int attackStyle = 0;

	public static int[][] commonDrops = { { 1303, 2 + Misc.random(1) }, { 1201, 2 + Misc.random(1) },
			{ 562, 700 + Misc.random(300) }, { 1754, 14 + Misc.random(18) }, { 1752, 11 + Misc.random(19) },
			{ 450, 10 + Misc.random(20) }, { 1602, 10 + Misc.random(20) }, { 6694, 10 + Misc.random(20) },
			{ 1988, 250 + Misc.random(51) }, { 1514, 50 }, { 995, 37000 + Misc.random(44000) },
			{ 392, 25 + Misc.random(30) } };

	public static int[][] uncommonDrops = { { 21338, 27 + Misc.random(3) }, { 21930, 55 + Misc.random(45) },
			{ 21486, 1 }, { 22118, 1 }, { 1377, 1 }, { 1305, 1 }, { 4087, 1 }, { 4585, 1 },
			{ 560, 300 + Misc.random(200) }, { 1750, 10 + Misc.random(15) }, { 1748, 5 + Misc.random(20) },
			{ 9189, 25 + Misc.random(10) }, { 9190, 25 + Misc.random(10) }, { 9191, 31 }, { 9192, 30 },
			{ 9193, 7 + Misc.random(21) }, { 9194, 5 + Misc.random(5) }, { 9194, 26 + Misc.random(4) },
			{ 824, 86 + Misc.random(14) }, { 11232, 10 + Misc.random(40) }, { 11237, 27 + Misc.random(23) },
			{ 1616, 1 + Misc.random(2) }, { 5288, 1 }, { 5321, 15 }, { 5295, 1 }, { 5300, 1 }, { 5304, 1 }, { 5313, 1 },
			{ 5314, 1 }, { 5290, 1 }, { 537, 7 + Misc.random(21) } };

	public static int[][] rareDrops = { { 1249, 1 }, { 2366, 1 }, { 1247, 1 }, { 1201, 1 }, { 1149, 1 }, { 1617, 1 },
			{ 1615, 1 }, { 443, 100 }, { 1185, 1 }, { 1319, 1 }, { 1373, 1 }, { 560, 45 }, { 563, 45 }, { 561, 67 },
			{ 2363, 1 }, { 829, 20 }, { 830, 5 }, { 892, 42 }, { 886, 150 }, { 1462, 1 }, { 1619, 1 }, { 1621, 1 },
			{ 1623, 1 }, { 1452, 1 }, { 985, 1 }, { 987, 1 }, { 995, 3000 }, { 21880, 30 + Misc.random(70) },
			{ 21488, 1 }, { 1392, 5 + Misc.random(10) }, { 5316, 1 }, { 5317, 1 }, { 5289, 1 }, { 5315, 1 } };

	public static int[][] veryRareDrops = { { 11286, 1 }, { 22006, 1 } };

	public static int[] lootCoordinates = { 2268, 4061 };

	public static void drop(Player player) {
		Server.itemHandler.createGroundItem(player, 22124, lootCoordinates[0], lootCoordinates[1], player.heightLevel,
				1, player.getIndex());
		Server.itemHandler.createGroundItem(player, 1751, lootCoordinates[0], lootCoordinates[1], player.heightLevel, 1,
				player.getIndex());
		int roll = Misc.random(2500);
		int roll2 = Misc.random(500);
		int roll3 = Misc.random(1500);
		if (roll2 == 1) {
			Server.itemHandler.createGroundItem(player, 22111, lootCoordinates[0], lootCoordinates[1],
					player.heightLevel, 1, player.getIndex());
			return;
		}
		if (roll3 == 1) {
			Server.itemHandler.createGroundItem(player, 22106, lootCoordinates[0], lootCoordinates[1],
					player.heightLevel, 1, player.getIndex());
			return;
		}
		if (roll == 1) {
			int veryRareItemRoll = Misc.random(veryRareDrops.length - 1);
			Server.itemHandler.createGroundItem(player, veryRareDrops[veryRareItemRoll][0], lootCoordinates[0],
					lootCoordinates[1], player.heightLevel, veryRareDrops[veryRareItemRoll][1], player.getIndex());
		} else if (roll >= 2 && roll <= 250) {
			int rareItemRoll = Misc.random(rareDrops.length - 1);
			Server.itemHandler.createGroundItem(player, rareDrops[rareItemRoll][0], lootCoordinates[0],
					lootCoordinates[1], player.heightLevel, rareDrops[rareItemRoll][1], player.getIndex());
		} else if (roll > 250 && roll <= 1000) {
			int uncommonItemRoll = Misc.random(uncommonDrops.length - 1);
			Server.itemHandler.createGroundItem(player, uncommonDrops[uncommonItemRoll][0], lootCoordinates[0],
					lootCoordinates[1], player.heightLevel, uncommonDrops[uncommonItemRoll][1], player.getIndex());
		} else {
			int commonItemRoll = Misc.random(commonDrops.length - 1);
			Server.itemHandler.createGroundItem(player, commonDrops[commonItemRoll][0], lootCoordinates[0],
					lootCoordinates[1], player.heightLevel, commonDrops[commonItemRoll][1], player.getIndex());
		}
	}

	public static boolean inVorkath(Player player) {
		return (player.absX > 2255 && player.absX < 2288 && player.absY > 4053 && player.absY < 4083);
	}

	public static void poke(Player player, NPC npc) {
		if(player.heightLevel == 0) {
			player.sendMessage("Vorkath isn't interested in fighting right now... try rejoining the instance.");
			return;
		}
		npc.requestTransform(8027);
		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				npc.requestTransform(8028);
			}

			@Override
			public void stop() {
			}
		}, 7);

	}

	public static void enterInstance(Player player, int instance) {
		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (player.absY == 4052 && player.absX != 2272) {
					player.setForceMovement(2272, 4054, 10, 10, "NORTH", 1660);
				}
				if (player.absY == 4052 && player.absX == 2272) {
					player.setForceMovement(player.absX, 4054, 10, 10, "NORTH", 839);
					player.getPA().movePlayer(player.absX, player.absY, player.getIndex() * 4);
					Server.npcHandler.spawnNpc(player, 8026, 2272, 4065, player.getIndex() * 4, 0, 750,
							player.antifireDelay > 0 ? 0 : 61, 560, 114, true, false);
					container.stop();
				}
			}

			@Override
			public void stop() {
			}
		}, 1);
	}

	public static void exit(Player player) {
		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (player.absY == 4054 && player.absX != 2272) {
					player.setForceMovement(2272, 4052, 10, 10, "SOUTH", 1660);
				}
				if (player.absY == 4054 && player.absX == 2272) {
					player.setForceMovement(player.absX, 4052, 10, 10, "SOUTH", 839);
					player.getPA().movePlayer(player.absX, player.absY, 0);
					container.stop();
				}
			}

			@Override
			public void stop() {
			}
		}, 1);

	}

}
