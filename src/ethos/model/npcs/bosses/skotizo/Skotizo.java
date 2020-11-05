package ethos.model.npcs.bosses.skotizo;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import ethos.Server;
import ethos.model.content.instances.SingleInstancedArea;
import ethos.model.minigames.rfd.DisposeTypes;
import ethos.model.npcs.NPC;
import ethos.model.npcs.NPCHandler;
import ethos.model.players.Boundary;
import ethos.model.players.Player;
import ethos.util.Misc;
import ethos.world.objects.GlobalObject;

public class Skotizo extends SingleInstancedArea {

	/**
	 * Player variables, start coordinates.
	 */
	private static final int START_X = 1700, START_Y = 9893;
	
	/**
	 * Npc variables, start coordinates.
	 */
	public static final int SPAWN_X = 1688, SPAWN_Y = 9880, SKOTIZO_ID = 7286, AWAKENED_ALTAR_NORTH = 7288, AWAKENED_ALTAR_SOUTH = 7290, AWAKENED_ALTAR_WEST = 7292, AWAKENED_ALTAR_EAST = 7294, REANIMATED_DEMON = 7287, DARK_ANKOU = 7296;
	
	public boolean northAltar, southAltar, eastAltar, westAltar, ankouSpawned, demonsSpawned = false;
	
	public boolean firstHit = true;
	
    public Map<Integer, String> altarMap = Collections.synchronizedMap(new HashMap<Integer, String>());
    
    private String altarMapDirection[] = {"NORTH", "SOUTH", "WEST", "EAST"}; 
    
    public int altarCount = 0;
	
    private int getAltar() { return (Misc.random(3) + 1); }
    
	public Skotizo(Player player, Boundary boundary, int height) {
		super(player, boundary, height);
	}
	
	
	public int calculateSkotizoHit(Player attacker, int damage) {
		if (altarCount == 0) {
			if (attacker.debugMessage)
				player.sendMessage("full hit");
		} else if (attacker.getSkotizo().altarCount == 1) {
			if (attacker.debugMessage)
				attacker.sendMessage("3/4 hit");
				damage = (int)(damage * .75);
		} else if (attacker.getSkotizo().altarCount == 2) {
			if (attacker.debugMessage)
				attacker.sendMessage("1/2 hit");
				damage = (int)(damage * .50);
		} else if (attacker.getSkotizo().altarCount == 3) {
			if (attacker.debugMessage)
				attacker.sendMessage("1/4 hit");
				damage = (int)(damage * .25);
		} else if (attacker.getSkotizo().altarCount == 4) {
			if (attacker.debugMessage)
				attacker.sendMessage("0 hit");
				damage = 0;
		}
		return damage;
	}
	
	public void arclightEffect(NPC npc) {
		if (npc.npcType == AWAKENED_ALTAR_NORTH) {
			NPCHandler.kill(AWAKENED_ALTAR_NORTH, height);
		} else if (npc.npcType == AWAKENED_ALTAR_SOUTH) {
			NPCHandler.kill(AWAKENED_ALTAR_SOUTH, height);
		} else if (npc.npcType == AWAKENED_ALTAR_WEST) {
			NPCHandler.kill(AWAKENED_ALTAR_WEST, height);
		} else if (npc.npcType == AWAKENED_ALTAR_EAST) {
			NPCHandler.kill(AWAKENED_ALTAR_EAST, height);
		}
	}
	
	public void skotizoSpecials() {
		NPC SKOTIZO = NPCHandler.getNpc(SKOTIZO_ID, height);
		
		if (SKOTIZO.isDead) {
			return;
		}
		
		int random = Misc.random(11);
		
		if (random == 1) {
			
			int altarNumber = getAltar();
			boolean unique = false;
			
			while (!unique) {
				if ((altarMap.get(1) == "NORTH") && (altarMap.get(2)== "SOUTH") && (altarMap.get(3) == "WEST") && (altarMap.get(4) == "EAST")) {
					player.sendMessage("@or2@Your hits do not effect Skotizo... Maybe I should kill some of the altars...");
					break;
				}
				String altar = altarMap.get(altarNumber);
				if(altar == null) {
					altarMap.put(altarNumber, altarMapDirection[altarNumber-1]);
					unique = true;
					if (altarMapDirection[altarNumber-1] == "NORTH") {
						player.sendMessage("@or2@The north altar has just awakened!");
						player.getPA().sendChangeSprite(29232, (byte) 1);
						Server.getGlobalObjects().remove(28924, 1694, 9904, height); // Remove North - Empty Altar
						Server.getGlobalObjects().add(new GlobalObject(28923, 1694, 9904, height, 2, 10, -1, -1)); // North - Awakened Altar
						Server.npcHandler.spawnNpc(player, AWAKENED_ALTAR_NORTH, 1694, 9904, height, 0, 100, 10, 200, 200, false, false);
						altarCount++;
						northAltar = true;
					} else if (altarMapDirection[altarNumber-1] == "SOUTH") {
						player.sendMessage("@or2@The south altar has just awakened!");
						player.getPA().sendChangeSprite(29233, (byte) 1);
						Server.getGlobalObjects().remove(28924, 1696, 9871, height); // Remove South - Empty Altar
						Server.getGlobalObjects().add(new GlobalObject(28923, 1696, 9871, height, 0, 10, -1, -1)); // South - Awakened Altar
						Server.npcHandler.spawnNpc(player, AWAKENED_ALTAR_SOUTH, 1696, 9871, height, 0, 100, 10, 200, 200, false, false);
						altarCount++;
						southAltar = true;
					} else if (altarMapDirection[altarNumber-1] == "WEST") {
						player.sendMessage("@or2@The west altar has just awakened!");
						player.getPA().sendChangeSprite(29234, (byte) 1);
						Server.getGlobalObjects().remove(28924, 1678, 9888, height); // Remove West - Empty Altar
						Server.getGlobalObjects().add(new GlobalObject(28923, 1678, 9888, height, 1, 10, -1, -1)); // West - Awakened Altar
						Server.npcHandler.spawnNpc(player, AWAKENED_ALTAR_WEST, 1678, 9888, height, 0, 100, 10, 200, 200, false, false);
						altarCount++;
						westAltar = true;
					} else if (altarMapDirection[altarNumber-1] == "EAST") {
						player.sendMessage("@or2@The east altar has just awakened!");
						player.getPA().sendChangeSprite(29235, (byte) 1);
						Server.getGlobalObjects().remove(28924, 1714, 9888, height); // Remove East - Empty Altar
						Server.getGlobalObjects().add(new GlobalObject(28923, 1714, 9888, height, 3, 10, -1, -1)); // East - Awakened Altar
						Server.npcHandler.spawnNpc(player, AWAKENED_ALTAR_EAST, 1714, 9888, height, 0, 100, 10, 200, 200, false, false);
						altarCount++;
						eastAltar = true;
					}
				} else {
					altarNumber = getAltar();
				}
			}
		} else if (random == 2 || random == 3) {
			if (SKOTIZO.getHealth().getAmount() < 225 && !demonsSpawned) {
				NPCHandler.npcs[SKOTIZO.getIndex()].forceChat("Gar mulno ful taglo!");
				Server.npcHandler.spawnNpc(player, REANIMATED_DEMON, player.absX + 1, player.absY, height, 0, 85, 8, 350, 300, true, false);
				Server.npcHandler.spawnNpc(player, REANIMATED_DEMON, player.absX - 1, player.absY, height, 0, 85, 8, 350, 300, true, false);
				Server.npcHandler.spawnNpc(player, REANIMATED_DEMON, player.absX, player.absY + 1, height, 0, 85, 8, 350, 300, true, false);
				demonsSpawned = true;
			}
		} else if (random == 4 && Misc.random(5) == 0) {
			if (SKOTIZO.getHealth().getAmount() < 150 && !ankouSpawned) {
				Server.npcHandler.spawnNpc(player, DARK_ANKOU, player.absX, player.absY - 1, height, 0, 60, 8, 350, 300, true, false);
				ankouSpawned = true;
			}
		}
	}

	/**
	 * Constructs the content by creating an event
	 */
	public void init() {
		Server.npcHandler.spawnNpc(player, SKOTIZO_ID, SPAWN_X, SPAWN_Y, height, 0, 450, 38, 500, 600, true, false);
		
		player.getPA().movePlayer(START_X, START_Y, height);
		
		player.getPA().sendChangeSprite(29232, (byte) 0);
		player.getPA().sendChangeSprite(29233, (byte) 0);
		player.getPA().sendChangeSprite(29234, (byte) 0);
		player.getPA().sendChangeSprite(29235, (byte) 0);
		
		Server.getGlobalObjects().add(new GlobalObject(28924, 1696, 9871, height, 0, 10, -1, -1)); // South - Empty Altar
		Server.getGlobalObjects().add(new GlobalObject(28924, 1694, 9904, height, 2, 10, -1, -1)); // North - Empty Altar
		Server.getGlobalObjects().add(new GlobalObject(28924, 1678, 9888, height, 1, 10, -1, -1)); // West - Empty Altar
		Server.getGlobalObjects().add(new GlobalObject(28924, 1714, 9888, height, 3, 10, -1, -1)); // East - Empty Altar
	}

	/**
	 * Disposes of the content by moving the player and finalizing and or removing any left over content.
	 * 
	 * @param dispose the type of dispose
	 */
	public final void end(DisposeTypes dispose) {
		if (player == null) {
			return;
		}
		if (dispose == DisposeTypes.COMPLETE) {
			//player.sendMessage("You killed Skotizo.");
			if (demonsSpawned) {
				NPCHandler.kill(REANIMATED_DEMON, height);
				NPCHandler.kill(REANIMATED_DEMON, height);
				NPCHandler.kill(REANIMATED_DEMON, height);
			}
			if (ankouSpawned)
				NPCHandler.kill(DARK_ANKOU, height);
			if (northAltar) {
				Server.getGlobalObjects().remove(28923, 1694, 9904, height); // Remove North - Awakened Altar
				NPCHandler.kill(AWAKENED_ALTAR_NORTH, height);
				player.getPA().sendChangeSprite(29232, (byte) 0);
				altarMap.remove(1);
				northAltar = false;
			} else {
				Server.getGlobalObjects().remove(28924, 1694, 9904, height); // Remove North - Empty Altar
			}
			if (southAltar) {
				Server.getGlobalObjects().remove(28923, 1696, 9871, height); // Remove South - Awakened Altar
				NPCHandler.kill(AWAKENED_ALTAR_SOUTH, height);
				player.getPA().sendChangeSprite(29233, (byte) 0);
				altarMap.remove(2);
				southAltar = false;
			} else {
				Server.getGlobalObjects().remove(28924, 1696, 9871, height); // Remove South - Empty Altar
			}
			if (westAltar) {
				Server.getGlobalObjects().remove(28923, 1678, 9888, height); // Remove West - Awakened Altar
				NPCHandler.kill(AWAKENED_ALTAR_WEST, height);
				player.getPA().sendChangeSprite(29234, (byte) 0);
				altarMap.remove(3);
				westAltar = false;
			} else {
				Server.getGlobalObjects().remove(28924, 1678, 9888, height); // Remove West - Empty Altar
			}
			if (eastAltar) {
				Server.getGlobalObjects().remove(28923, 1714, 9888, height); // Remove East - Awakened Altar
				NPCHandler.kill(AWAKENED_ALTAR_EAST, height);
				player.getPA().sendChangeSprite(29235, (byte) 0);
				altarMap.remove(4);
				eastAltar = false;
			} else {
				Server.getGlobalObjects().remove(28924, 1714, 9888, height); // Remove East - Empty Altar
			}
			//Server.getGlobalObjects().remove(28924, height);
			//Server.getGlobalObjects().remove(28923, height);
			
		} else if (dispose == DisposeTypes.INCOMPLETE) {			
			NPCHandler.kill(SKOTIZO_ID, height);
			if (northAltar) {
				NPCHandler.kill(AWAKENED_ALTAR_NORTH, height);
				Server.getGlobalObjects().remove(28923, 1694, 9904, height); // Remove North - Awakened Altar
				Server.getGlobalObjects().add(new GlobalObject(28924, 1694, 9904, height, 2, 10, -1, -1)); // Add North - Empty Altar
				player.getPA().sendChangeSprite(29232, (byte) 0);
				altarMap.remove(1);
				northAltar = false;
			} else {
				Server.getGlobalObjects().remove(28924, 1694, 9904, height); // Remove North - Empty Altar
			}
			if (southAltar) {
				NPCHandler.kill(AWAKENED_ALTAR_SOUTH, height);
				Server.getGlobalObjects().remove(28923, 1696, 9871, height); // Remove South - Awakened Altar
				Server.getGlobalObjects().add(new GlobalObject(28924, 1696, 9871, height, 0, 10, -1, -1)); // Add South - Empty Altar
				player.getPA().sendChangeSprite(29233, (byte) 0);
				altarMap.remove(2);
				southAltar = false;
			} else {
				Server.getGlobalObjects().remove(28924, 1696, 9871, height); // Remove South - Empty Altar
			}
			if (westAltar) {
				NPCHandler.kill(AWAKENED_ALTAR_WEST, height);
				Server.getGlobalObjects().remove(28923, 1678, 9888, height); // Remove West - Awakened Altar
				Server.getGlobalObjects().add(new GlobalObject(28924, 1678, 9888, height, 1, 10, -1, -1)); // Add West - Empty Altar
				player.getPA().sendChangeSprite(29234, (byte) 0);
				altarMap.remove(3);
				westAltar = false;
			} else {
				Server.getGlobalObjects().remove(28924, 1678, 9888, height); // Remove West - Empty Altar
			}
			if (eastAltar) {
				NPCHandler.kill(AWAKENED_ALTAR_EAST, height);
				Server.getGlobalObjects().remove(28923, 1714, 9888, height); // Remove East - Awakened Altar
				Server.getGlobalObjects().add(new GlobalObject(28924, 1714, 9888, height, 3, 10, -1, -1)); // Add East - Empty Altar
				player.getPA().sendChangeSprite(29235, (byte) 0);
				altarMap.remove(4);
				eastAltar = false;
			} else {
				Server.getGlobalObjects().remove(28924, 1714, 9888, height); // Remove East - Empty Altar
			}
			if (demonsSpawned) {
				NPCHandler.kill(REANIMATED_DEMON, height);
				NPCHandler.kill(REANIMATED_DEMON, height);
				NPCHandler.kill(REANIMATED_DEMON, height);
			}
			if (ankouSpawned)
				NPCHandler.kill(DARK_ANKOU, height);
			//Server.getGlobalObjects().remove(28924, height);
			//Server.getGlobalObjects().remove(28923, height);
			//player.getPA().movePlayer(1665, 10046, 0);
		}
	}

	@Override
	public void onDispose() {
		end(DisposeTypes.INCOMPLETE);
	}
	
	public int getHeight() {
		return height;
	}
}
