package ethos.model.content;

import ethos.model.items.Item;
import ethos.model.players.Player;

public class QuickSets {
	
	public enum QUICKSETS {
		MELEE_ZERKER(0, 
				new int[][] { { 3751, 1 }, { 6570, 1 }, { 1704, 1 }, { 4151, 1 }, { 10551, 1 }, { 8850, 1 }, { -1, -1 }, { 1079, 1 }, { -1, -1 }, { 7462, 1 }, { 4131, 1 }, { -1, -1 }, { 2550, 1 }, { -1, -1 } }, 
				new int[][] { { 5698, 1 }, { 2440, 1 }, { 2436, 1 }, { 2442, 1 }, { 3024, 2 }, { 11936, 19 }, { 557, 1000 }, { 560, 1000 }, { 9075, 1000 } }),
		RANGE_TANK_MAIN(1, 
				new int[][] { { 3749, 1 }, { 10499, 1 }, { 1704, 1 }, { 9185, 1 }, { 2503, 1 }, { 1201, 1 }, { -1, -1 }, { 2497, 1 }, { -1, -1 }, { 7462, 1 }, { 4131, 1 }, { -1, -1 }, { 2550, 1 }, { 9244, 250 } }, 
				new int[][] { { 5698, 1 }, { 2444, 1 }, { 2442, 1 }, { 3024, 2 }, { 11936, 20 }, { 557, 1000 }, { 560, 1000 }, { 9075, 1000 } }),
		BRID_MAIN(2, 
				new int[][] { { 3755, 1 }, { 2412, 1 }, { 1704, 1 }, { 4675, 1 }, { 4091, 1 }, { 3842, 1 }, { -1, -1 }, { 4093, 1 }, { -1, -1 }, { 7462, 1 }, { 3105, 1 }, { -1, -1 }, { 2550, 1 }, { -1, -1 } }, 
				new int[][] { { 5698, 1 }, { 4151, 1 }, { 3040, 1 }, { 2440, 1 }, { 3024, 2 }, { 6685, 3 }, { 2503, 1 }, { 1079, 1 }, { 11936, 14 }, { 555, 1000 }, { 560, 1000 }, { 565, 1000 } }),
	
		MELEE_PURE(3, 
				new int[][] { { 740, 1 }, { 6570, 1 }, { 1704, 1 }, { 4151, 1 }, { 6107, 1 }, { 3842, 1 }, { -1, -1 }, { 6108, 1 }, { -1, -1 }, { 7458, 1 }, { 3105, 1 }, { -1, -1 }, { 2550, 1 }, { -1, -1 } }, 
				new int[][] { { 5698, 1 }, { 2440, 1 }, { 2436, 1 }, { 3024, 2 }, { 6685, 3 }, { 2497, 1 }, { 11936, 16 }, { 555, 1000 }, { 560, 1000 }, { 565, 1000 } }),
		RANGE_PURE(4, 
				new int[][] { { 740, 1 }, { 10499, 1 }, { 1704, 1 }, { 9185, 1 }, { 6107, 1 }, { 3842, 1 }, { -1, -1 }, { 2497, 1 }, { -1, -1 }, { 7458, 1 }, { 3105, 1 }, { -1, -1 }, { 2550, 1 }, { 9244, 250 } }, 
				new int[][] { { 5698, 1 }, { 2440, 1 }, { 2444, 1 }, { 3024, 2 }, { 6685, 3 }, { 11936, 17 }, { 557, 1000 }, { 560, 1000 }, { 9075, 1000 } }),
		BRID_PURE(5, 
				new int[][] { { 740, 1 }, { 2412, 1 }, { 1704, 1 }, { 4675, 1 }, { 6107, 1 }, { 3842, 1 }, { -1, -1 }, { 6108, 1 }, { -1, -1 }, { 7458, 1 }, { 3105, 1 }, { -1, -1 }, { 2550, 1 }, { 9244, 250 } }, 
				new int[][] { { 5698, 1 }, { 4151, 1 }, { 2440, 1 }, { 3040, 1 }, { 3024, 2 }, { 6685, 3 }, { 11936, 16 }, { 555, 1000 }, { 560, 1000 }, { 565, 1000 } }),
		
		F2P_PURE(6, 
				new int[][] { { -1, -1 }, { 2412, 1 }, { 1704, 1 }, { 4675, 1 }, { 6107, 1 }, { 3842, 1 }, { -1, -1 }, { 6108, 1 }, { -1, -1 }, { 7458, 1 }, { 3105, 1 }, { -1, -1 }, { 2550, 1 }, { 9244, 250 } }, 
				new int[][] { { 5698, 1 }, { 4151, 1 }, { 2440, 1 }, { 3040, 1 }, { 3024, 2 }, { 6685, 3 }, { 11936, 16 }, { 555, 1000 }, { 560, 1000 }, { 565, 1000 } });
	
		/**
		 * 	HEAD: 0
			CAPE: 1
			AMULET: 2
			WEAPON: 3
			CHEST: 4
			SHIELD: 5
			LEGS: 7
			HANDS: 9
			FEET: 10
			RING: 12
			ARROWS: 13
		 */
		
		private int id;
		private int[][] gear, inventory;
		
		QUICKSETS(int id, int[][] gear, int[][] inventory) {
			this.id = id;
			this.gear = gear;
			this.inventory = inventory;
		}
	}
	
	public static void gearUp(final Player player, int setId) {
		for (QUICKSETS set : QUICKSETS.values()) {
			String name = set.name().toLowerCase().replaceAll("_", " ");
			if (setId == set.id) {
				
				switch (setId) {
				//Main sets
				case 0:
				case 1:
					for (int i = 0; i < 7; i++) {
						player.playerLevel[i] = 99;
						player.playerXP[i] = player.getPA().getXPForLevel(99) + 1;
						player.getPA().setSkillLevel(i, player.playerLevel[i], player.playerXP[i]);
						player.getPA().refreshSkill(i);
					}
					player.playerMagicBook = 2;
					player.setSidebarInterface(6, 28064);
					break;
					
				case 2:
					for (int i = 0; i < 7; i++) {
						player.playerLevel[i] = 99;
						player.playerXP[i] = player.getPA().getXPForLevel(99) + 1;
						player.getPA().setSkillLevel(i, player.playerLevel[i], player.playerXP[i]);
						player.getPA().refreshSkill(i);
					}
					player.playerMagicBook = 1;
					player.setSidebarInterface(6, 28062);
					break;
					
					//Pure sets
				case 3:
				case 4:
				case 5:
					player.playerLevel[0] = 99;
					player.playerLevel[1] = 1;
					player.playerLevel[2] = 99;
					player.playerLevel[3] = 99;
					player.playerLevel[4] = 99;
					player.playerLevel[5] = 52;
					player.playerXP[0] = player.getPA().getXPForLevel(99) + 1;
					player.playerXP[1] = 0;
					player.playerXP[2] = player.getPA().getXPForLevel(99) + 1;
					player.playerXP[3] = player.getPA().getXPForLevel(99) + 1;
					player.playerXP[4] = player.getPA().getXPForLevel(99) + 1;
					player.playerXP[5] = player.getPA().getXPForLevel(52) + 1;
					player.getPA().setSkillLevel(0, player.playerLevel[0], player.playerXP[0]);
					player.getPA().refreshSkill(0);
					player.getPA().setSkillLevel(1, player.playerLevel[1], player.playerXP[1]);
					player.getPA().refreshSkill(1);
					player.getPA().setSkillLevel(2, player.playerLevel[2], player.playerXP[2]);
					player.getPA().refreshSkill(2);
					player.getPA().setSkillLevel(3, player.playerLevel[3], player.playerXP[3]);
					player.getPA().refreshSkill(3);
					player.getPA().setSkillLevel(4, player.playerLevel[4], player.playerXP[4]);
					player.getPA().refreshSkill(4);
					player.getPA().setSkillLevel(5, player.playerLevel[5], player.playerXP[5]);
					player.getPA().refreshSkill(5);
					player.playerMagicBook = 1;
					player.setSidebarInterface(6, 28062);
					break;
				}
				
				player.getItems().deleteAllItems();
				for (int i = 0; i < set.gear.length; i++) {
					player.playerEquipment[i] = set.gear[i][0];
					player.playerEquipmentN[i] = set.gear[i][1];
					player.getItems().setEquipment(set.gear[i][0], set.gear[i][1], i);
				}
				for (int i = 0; i < set.inventory.length; i++) {
					player.getItems().addItem(set.inventory[i][0], set.inventory[i][1]);
				}
				player.getItems().resetItems(3214);
				player.getItems().resetBonus();
				player.getItems().getBonus();
				player.getItems().writeBonus();
				player.getItems().resetItems(3823);
				player.getCombat().getPlayerAnimIndex(Item.getItemName(player.playerEquipment[player.playerWeapon]));
				player.getPA().sendFrame126("Combat Level: " + player.combatLevel + "", 3983);
				player.getPA().sendFrame126("Total Lvl: " + player.totalLevel + "", 3984);
				player.sendMessage("Successfully loaded set: " + name);
				player.getPA().removeAllWindows();
			}
		}
	}
}
