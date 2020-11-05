package ethos.model.npcs.drops;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import ethos.Server;
import ethos.model.content.godwars.Godwars;
import ethos.model.items.GameItem;
import ethos.model.items.Item;
import ethos.model.items.ItemAssistant;
import ethos.model.npcs.NPC;
import ethos.model.npcs.NPCDefinitions;
import ethos.model.npcs.NPCHandler;
import ethos.model.players.Boundary;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.model.players.Right;
import ethos.model.players.skills.slayer.SlayerMaster;
import ethos.model.players.skills.slayer.Task;
import ethos.util.Location3D;
import ethos.util.Misc;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DropManager {

	
	public static int AMOUNT_OF_TABLES = 0;

	private static final Comparator<Integer> COMPARE_NAMES =(o1, o2) -> {
		String name1 = NPCDefinitions.get(o1).getNpcName();
		String name2 = NPCDefinitions.get(o2).getNpcName();
		return name1.compareToIgnoreCase(name2);
	};

	private Map<List<Integer>, TableGroup> groups = new HashMap<>();

	private List<Integer> ordered = new ArrayList<>();

	@SuppressWarnings("unchecked")
	public void read() {
		JSONParser parser = new JSONParser();
		try {
			fileReader = new FileReader("./Data/json/npc_droptable.json");
			JSONArray data = (JSONArray) parser.parse(fileReader);
			for (Object aData : data) {
				JSONObject drop=(JSONObject) aData;
				List<Integer> npcIds=new ArrayList<>();
				if (drop.get("npc_id") instanceof JSONArray) {
					JSONArray idArray=(JSONArray) drop.get("npc_id");
					idArray.forEach(id -> npcIds.add(((Long) id).intValue()));
				} else {
					npcIds.add(((Long) drop.get("npc_id")).intValue());
				}
				TableGroup group=new TableGroup(npcIds);
				for (TablePolicy policy : TablePolicy.POLICIES) {
					if (!drop.containsKey(policy.name().toLowerCase())) {
						continue;
					}
					JSONObject dropTable=(JSONObject) drop.get(policy.name().toLowerCase());
					Table table=new Table(policy, ((Long) dropTable.get("accessibility")).intValue());
					JSONArray tableItems=(JSONArray) dropTable.get("items");
					for (Object tableItem : tableItems) {
						JSONObject item=(JSONObject) tableItem;
						int id=((Long) item.get("item")).intValue();
						int minimumAmount=((Long) item.get("minimum")).intValue();
						int maximumAmount=((Long) item.get("maximum")).intValue();
						table.add(new Drop(npcIds, id, minimumAmount, maximumAmount));
					}
					group.add(table);
				}
				groups.put(npcIds, group);
			}
			ordered.clear();

			for (TableGroup group : groups.values()) {
				if (group.getNpcIds().size() == 1) {
					ordered.add(group.getNpcIds().get(0));
					continue;
				}
				for (int id : group.getNpcIds()) {
					String name = NPCDefinitions.get(id).getNpcName();
					if (ordered.stream().noneMatch(i -> NPCDefinitions.get(i).getNpcName().equals(name))) {
						ordered.add(id);
					}
				}
			}

			ordered.sort(COMPARE_NAMES);
			Misc.println("Loaded " + ordered.size() + " drop tables.");
			AMOUNT_OF_TABLES = ordered.size();
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Attempts to create a drop for a player after killing a non-playable character
	 * 
	 * @param player the player receiving a possible drop
	 * @param npc the npc dropping the items
	 */
	static boolean test = false;
	
	static int[] bosses = { 
			/* Misc bosses */
			6619, 6618, 6615, 6766, 963, 965, 5890, 6609, 319, 6610, 6611, 5779, 6342, 2205, 2215, 3129, 3162, 2054, 2265, 2266, 2267,
			7544, 7604, 7605, 7606, 
			/* Godwars minions */ 
			2206, 2207, 2208, 3130, 3131, 3132, 2216, 2217, 2218, 3163, 3164, 3165
	};

	public void testOpen(Player player) {
		for(int i = 0; i < 100; i++) {
			player.getPA().sendFrame126("", (33008  + i));
		}
		for (int index = 0; index < ordered.size(); index++) {
			player.getPA().sendFrame126(StringUtils.capitalize(NPCDefinitions.get(ordered.get(index)).getNpcName().toLowerCase().replaceAll("_", " ")), 33008 + index);
		}

		player.getPA().showInterface(33000);
	}
	
	public void create(Player player, NPC npc, Location3D location, int repeats) {
		Optional<TableGroup> group = groups.values().stream().filter(g -> g.getNpcIds().contains(npc.npcType)).findFirst();
		
		group.ifPresent(g -> {
			double modifier = getModifier(player);
			List<GameItem> drops = g.access(player, modifier, repeats);

			for (GameItem item : drops) {
				if (item.getId() == 536) {
					if (player.getRechargeItems().hasItem(13111) && player.inWild()) {
						item.changeDrop(537, item.getAmount());
					}
				}
				if (item.getId() == 6529) {
					if (player.getRechargeItems().hasItem(11136)) {
						item.changeDrop(6529, (int) (item.getAmount() * 1.20));
					}
					if (player.getRechargeItems().hasItem(11138)) {
						item.changeDrop(6529, (int) (item.getAmount() * 1.50));
					}
					if (player.getRechargeItems().hasItem(11140)) {
						item.changeDrop(6529, (int) (item.getAmount() * 1.70));
					}
					if (player.getRechargeItems().hasItem(13103)) {
						item.changeDrop(6529, (int) (item.getAmount() * 1.90));
					}
				}
				if (item.getId() == 6729 && player.getRechargeItems().hasItem(13132)) {
					item.changeDrop(6730, item.getAmount());
				}
				if (item.getId() == 13233 && !Boundary.isIn(player, Boundary.CERBERUS_BOSSROOMS)) {
					//player.sendMessage("@red@Something hot drops from the body of your vanquished foe");
				}
				
				if (IntStream.of(bosses).anyMatch(id -> id == npc.npcType)) {
					PlayerHandler.nonNullStream()
					.filter(p -> p.distanceToPoint(player.absX, player.absY) < 10 && p.heightLevel == player.heightLevel)
					.forEach(p -> {
						if (item.getAmount() > 1)
							p.sendMessage("@red@" + Misc.formatPlayerName(player.playerName) + " received a drop: " + Misc.format(item.getAmount()) + " x " + Item.getItemName(item.getId()) + ".");
						else
							p.sendMessage("@red@" + Misc.formatPlayerName(player.playerName) + " received a drop: " + Item.getItemName(item.getId()) + ".");
					});
				}

				Server.itemHandler.createGroundItem(player, item.getId(), location.getX(), location.getY(),
						location.getZ(), item.getAmount(), player.getIndex());
			}

			/**
			 * Looting bag and rune pouch
			 */
			if (npc.inWild()) {
				switch (Misc.random(60)) {
				case 2:
					if (player.getItems().getItemCount(11941, true) < 1) {
						Server.itemHandler.createGroundItem(player, 11941, location.getX(), location.getY(), location.getZ(), 1, player.getIndex());
					}
					break;
					
				case 8:
					if (player.getItems().getItemCount(12791, true) < 1) {
						Server.itemHandler.createGroundItem(player, 12791, location.getX(), location.getY(), location.getZ(), 1, player.getIndex());
					}
					break;
				}
			}
			/**
			 * Slayer's staff enchantment and Emblems
			 */
			Optional<Task> task = player.getSlayer().getTask();
			Optional<SlayerMaster> myMaster = SlayerMaster.get(player.getSlayer().getMaster());
			task.ifPresent(t -> {
			String name = npc.getDefinition().getNpcName().toLowerCase().replaceAll("_", " ");
			
				if (name.equals(t.getPrimaryName()) || ArrayUtils.contains(t.getNames(), name)) {
					myMaster.ifPresent(m -> {
						if (npc.inWild() && m.getId() == 7663) {
							int slayerChance = 650;
							int emblemChance = 100;
							if (Misc.random(emblemChance) == 1) {
								Server.itemHandler.createGroundItem(player, 12746, location.getX(), location.getY(), location.getZ(), 1, player.getIndex());
								//player.sendMessage("@red@A mysterious emblem has dropped from your foe!");
							}
							if (Misc.random(slayerChance) == 1) {
								Server.itemHandler.createGroundItem(player, 21257, location.getX(), location.getY(), location.getZ(), 1, player.getIndex());
								//player.sendMessage("@red@A slayer's enchantment has dropped from your foe!");
								//PlayerHandler.executeGlobalMessage(
										//"@red@" + Misc.capitalize(player.playerName) + " received a drop: Slayer's Enchantment.</col>.");
								//PlayerHandler.executeGlobalMessage("<col=FF0000>[Lootations] @cr19@ </col><col=255>"+ Misc.capitalize(player.playerName) + "</col> received a <col=255>Slayer's Enchantment</col>.");
							}
						}
					});
				}
			});
				
			/**
			 * Clue scrolls
			 */
			int chance = player.getRechargeItems().hasItem(13118) ? 142 : player.getRechargeItems().hasItem(13119) ? 135 : player.getRechargeItems().hasItem(13120) ? 120 : 150;
			if (Misc.random(chance) == 1) {
				//player.sendMessage("@pur@You sense a @red@clue scroll @pur@being dropped to the ground.");
				if (npc.getDefinition().getNpcCombat() > 0 && npc.getDefinition().getNpcCombat() <= 70) {
					Server.itemHandler.createGroundItem(player, 2677, location.getX(), location.getY(), location.getZ(), 1, player.getIndex());
				} 
				if (npc.getDefinition().getNpcCombat() > 70 && npc.getDefinition().getNpcCombat() <= 110) {
					Server.itemHandler.createGroundItem(player, 2801, location.getX(), location.getY(), location.getZ(), 1, player.getIndex());
				} 
				if (npc.getDefinition().getNpcCombat() > 110) {
					Server.itemHandler.createGroundItem(player, 2722, location.getX(), location.getY(), location.getZ(), 1, player.getIndex());
				}
			}
			
			
			/**
			 * Runecrafting pouches
			 */
			if (Misc.random(100) == 10) {
				if (npc.getDefinition().getNpcCombat() >= 70 && npc.getDefinition().getNpcCombat() <= 100 && player.getItems().getItemCount(5509, true) == 1 && player.getItems().getItemCount(5510, true) != 1) {
					Server.itemHandler.createGroundItem(player, 5510, location.getX(), location.getY(), location.getZ(), 1, player.getIndex());
					//player.sendMessage("@pur@You sense an upgraded Runecrafting Pouch!");
				} else if (npc.getDefinition().getNpcCombat() > 100 && player.getItems().getItemCount(5510, true) == 1 && player.getItems().getItemCount(5512, true) != 1) {
					Server.itemHandler.createGroundItem(player, 5512, location.getX(), location.getY(), location.getZ(), 1, player.getIndex());
					//player.sendMessage("@pur@You sense an upgraded Runecrafting Pouch!");
				}
			}

			/**
			 * Crystal keys
			 */
			if (Misc.random(115) == 1) {
				//player.sendMessage("@pur@You sense a @red@crystal key @pur@being dropped to the ground.");
				Server.itemHandler.createGroundItem(player, 989, location.getX(), location.getY(), location.getZ(), 1, player.getIndex());
			}
			
			/**
			 * Ecumenical Keys
			 */
			if (Boundary.isIn(npc, Boundary.WILDERNESS_GOD_WARS_BOUNDARY)) {
				if (Misc.random(60 + 10 * player.getItems().getItemCount(Godwars.KEY_ID, true)) == 1) {
					/**
					 * Key will not drop if player owns more than 3 keys already
					 */
					int key_amount = player.getDiaryManager().getWildernessDiary().hasCompleted("ELITE") ? 6 : 3;
					if (player.getItems().getItemCount(Godwars.KEY_ID, true) > key_amount) {
						return;
					}
					Server.itemHandler.createGroundItem(player, Godwars.KEY_ID, npc.getX(), npc.getY(), player.heightLevel, 1, player.getIndex());
					//player.sendMessage("@pur@An Ecumenical Key drops from your foe.");
				}
			}
			
			/**
			 * Dark Light
			 */
			if (Boundary.isIn(npc, Boundary.CATACOMBS)) {
				if (Misc.random(1000) == 1) {
					
					//PlayerHandler.executeGlobalMessage("<col=FF0000>[Lootations] @cr19@ </col><col=255>"+ Misc.capitalize(player.playerName) + "</col> received a <col=255>Darklight!</col>.");
					Server.itemHandler.createGroundItem(player, 6746, location.getX(), location.getY(), location.getZ(), 1, player.getIndex());
				}
			}
			
			/**
			 * Dark totem Pieces
			 */
			if (Boundary.isIn(npc, Boundary.CATACOMBS)) {
				switch (Misc.random(25)) {
				case 1:
					if (player.getItems().getItemCount(19679, false) < 1) {
						Server.itemHandler.createGroundItem(player, 19679, location.getX(), location.getY(), location.getZ(), 1, player.getIndex());
						//player.sendMessage("@red@A surge of dark energy fills your body as you notice something on the ground.");
					}
					break;
					
				case 2:
					if (player.getItems().getItemCount(19681, false) < 1) {
						Server.itemHandler.createGroundItem(player, 19681, location.getX(), location.getY(), location.getZ(), 1, player.getIndex());
						//player.sendMessage("@red@A surge of dark energy fills your body as you notice something on the ground.");
					}
					break;
				
				
				case 3:
					if (player.getItems().getItemCount(19683, false) < 1) {
						Server.itemHandler.createGroundItem(player, 19683, location.getX(), location.getY(), location.getZ(), 1, player.getIndex());
						//player.sendMessage("@red@A surge of dark energy fills your body as you notice something on the ground.");
					}
					break;
				}
			}
		});
	}

	private double getModifier(Player player) {
		double modifier = 1.0;
		if(player.getMode().isOsrs()){
			modifier -=.15;
		}
		if (player.getItems().isWearingItem(2572)) {
			modifier -= .03;
		} else if (player.getItems().isWearingItem(12785)) {
			modifier -= .05;
		}
		if (player.getRights().contains(Right.LEGENDARY)) {
			modifier -= 0.150;
		} else if (player.getRights().contains(Right.EXTREME_DONATOR)) {
			modifier -= 0.120;
		} else if (player.getRights().contains(Right.SUPER_DONATOR)) {
			modifier -= 0.100;
		} else if (player.getRights().contains(Right.DONATOR)) {
			modifier -= 0.100;
		} else if (player.getRights().contains(Right.SUPPORTER)) {
			modifier -= 0.070;
		} else if (player.getRights().contains(Right.SPONSOR)) {
			modifier -= 0.05;
		} else if (player.getRights().contains(Right.CONTRIBUTOR)) {
			modifier -= 0.030;
		}
		return modifier;
	}
	public static double getModifier1(Player player) {
		int modifier = 0;
		if (player.getItems().isWearingItem(2572)) {
			modifier += 3;
		} else if (player.getItems().isWearingItem(12785)) {
			modifier += 15;
		}
		if(player.getMode().isOsrs()){
			modifier += 15;
		}
		if (player.summonId == 9959) {
			modifier += 15;
		}else if (player.summonId == 13326) {
			modifier += 10;
		}
		if (player.getRights().contains(Right.LEGENDARY)) {
			modifier += 16;
		} else if (player.getRights().contains(Right.EXTREME_DONATOR)) {
			modifier += 15;
		} else if (player.getRights().contains(Right.SUPER_DONATOR)) {
			modifier += 10;
		} else if (player.getRights().contains(Right.DONATOR)) {
			modifier += 10;
		} else if (player.getRights().contains(Right.SUPPORTER)) {
			modifier += 7;
		} else if (player.getRights().contains(Right.SPONSOR)) {
			modifier += 5;
		} else if (player.getRights().contains(Right.CONTRIBUTOR)) {
			modifier += 3;
		}
		return modifier;
	}
	/**
	 * Clears the interface of all parts.
	 * 
	 * Used on searching and initial load.
	 * @param player
	 */
	public void clear(Player player) {
		for(int i = 0; i < 150; i++) {
			player.getPA().sendFrame126("", 33008 + i);
		}
		
		player.getPA().sendFrame126("", 43110);
		player.getPA().sendFrame126("", 43111);
		player.getPA().sendFrame126("", 43112);
		player.getPA().sendFrame126("", 43113);
		
		for(int i = 0;i<80;i++){
			player.getPA().itemOnInterface(-1, 0, 34010+i, 0);
			player.getPA().sendString("", 34200+i);
			player.getPA().sendString("", 34300+i);
			player.getPA().sendString("", 34100+i);
			player.getPA().sendString("", 34400+i);
		}
		player.searchList.clear();
	}

	public void open2(Player player) {
		clear(player);

		for (int index = 0; index < ordered.size(); index++) {
			player.getPA().sendFrame126(StringUtils.capitalize(NPCDefinitions.get(ordered.get(index)).getNpcName().toLowerCase().replaceAll("_", " ")), 33008 + index);
		}

		player.getPA().showInterface(33000);
	}

	/**
	 * Searchers after the player inputs a npc name
	 * @param player
	 * @param name
	 */
	public void search(Player player, String name) {
		if(name.matches("^(?=.*[A-Z])(?=.*[0-9])[A-Z0-9]+$")) {
			player.sendMessage("You may not search for alphabetical and numerical combinations.");
			return;
		}
		if (System.currentTimeMillis() - player.lastDropTableSearch < TimeUnit.SECONDS.toMillis(5)) {
			player.sendMessage("You can only do this once every 5 seconds.");
			return;
		}
		player.lastDropTableSearch = System.currentTimeMillis();
		
		clear(player);

		List<Integer> definitions = ordered.stream().filter(Objects::nonNull).filter(def -> NPCDefinitions.get(def).getNpcName() != null).filter(def -> NPCDefinitions.get(def).getNpcName().toLowerCase().contains(name.toLowerCase())).collect(Collectors.toList());

		if(definitions.isEmpty()) {
			definitions = ordered.stream().filter(Objects::nonNull).collect(Collectors.toList());
			List<Integer> npcs = new ArrayList<>();
			int count = 0;
			for(Integer index : definitions) {
				Optional<TableGroup> group = groups.values().stream().filter(g -> g.getNpcIds().contains(NPCDefinitions.get(index).getNpcId())).findFirst();
				if(group.isPresent()) {
					TableGroup g = group.get();
					
					for(TablePolicy policy : TablePolicy.values()) {
						Optional<Table> table = g.stream().filter(t -> t.getPolicy() == policy).findFirst();
						if(table.isPresent()) {
							for(Drop drop : table.get()) {
								if(drop == null) {
									continue;
								}
								
								if(ItemAssistant.getItemName(drop.getItemId()).toLowerCase().contains(name.toLowerCase())) {
									npcs.add(index);
									player.getPA().sendFrame126(StringUtils.capitalize(NPCDefinitions.get(NPCDefinitions.get(index).getNpcId()).getNpcName().toLowerCase().replaceAll("_", " ")), 33008 + count);
									count++;
								}
							}
						}
					}
				}

			}
			
			player.searchList = npcs;
			return;
			
		}
		
		for(int index = 0; index < definitions.size(); index++) {
			if(index >= 150) {
				break;
			}
			player.getPA().sendFrame126(StringUtils.capitalize(NPCDefinitions.get(definitions.get(index)).getNpcName().toLowerCase().replaceAll("_", " ")), 33008 + index);
		}

		player.searchList = definitions;
	}

	/**
	 * Loads the selected npc choosen by the player to view their drops
	 * @param player
	 * @param button
	 */
	public void select(Player player, int button) {
		int listIndex;
		
		//So the idiot client dev didn't organize the buttons in a singulatiry order. So i had to shift around the id's
		//so if you have 50 npcs in the search you can click them all fine
		if(button <= 128255) {
			listIndex = button - 128240;
		} else {
			listIndex = (128255 - 128240) + 1 + button - 129000;
		}
		
		if (listIndex < 0 || listIndex > ordered.size() - 1) {
			return;
		}

		//Finding NPC ID
		int npcId = player.searchList.isEmpty() ? ordered.get(listIndex) : player.searchList.get(listIndex);
		
		Optional<TableGroup> group = groups.values().stream().filter(g -> g.getNpcIds().contains(npcId)).findFirst();

		//If the group in the search area contains this NPC
		group.ifPresent(g -> {
			if (System.currentTimeMillis() - player.lastDropTableSelected < TimeUnit.SECONDS.toMillis(5)) {
				player.sendMessage("You can only do this once every 5 seconds.");
				return;
			}

			//Loads the definition and maxhit/aggressiveness to display
			NPCDefinitions npcDef = NPCDefinitions.get(npcId);
			
			player.getPA().sendFrame126("Health: @whi@" + npcDef.getNpcHealth(), 43110);
			player.getPA().sendFrame126("Combat Level: @whi@" + npcDef.getNpcCombat(), 43111);
			if(NPCHandler.getNpc(npcId) != null){
				player.getPA().sendFrame126("Max Hit: @whi@" + NPCHandler.getNpc(npcId).maxHit, 43112);
			} else {
				player.getPA().sendFrame126("Max Hit: @whi@?", 43112);
			}
			player.getPA().sendFrame126("Aggressive: @whi@" + (Server.npcHandler.isAggressive(npcId, true) ? "true" : "false"), 43113);
			
			player.lastDropTableSelected = System.currentTimeMillis();
			
			double modifier = getModifier(player);
			
			//Iterates through all 5 drop table's (Found in TablePolicy -> Enum)
			for (TablePolicy policy : TablePolicy.POLICIES) {
				Optional<Table> table = g.stream().filter(t -> t.getPolicy() == policy).findFirst();
				if (table.isPresent()) {
					double chance = (1.0 /(table.get().getAccessibility() * modifier)) * 100D;
					int in_kills = (int) (100 / chance);
					if (chance > 100.0) {
						chance = 100.0;
					}
					if (in_kills == 0) {
						in_kills = 1;
					}
					
					//Updates the interface with all new information
					updateAmounts(player, policy, table.get(), in_kills);
				} else {
					updateAmounts(player, policy, new ArrayList<>(), -10);
				}
			}
			
			//If the game has displayed all drops and there are empty slots that haven't been filled, clear them
			if(player.dropSize < 80) {
				for(int i = player.dropSize;i<80;i++){
					player.getPA().sendString("", 34200+i);
					player.getPA().itemOnInterface(-1, 0, 34010+i, 0);
					player.getPA().sendString("", 34300+i);
					player.getPA().sendString("", 34100+i);
					player.getPA().sendString("", 34400+i);
				}
			}
			player.dropSize = 0;
		});
	}

	/**
	 * Updates the interface for the selected NPC
	 * @param player
	 * @param policy
	 * @param drops
	 * @param kills
	 */
	private void updateAmounts(Player player, TablePolicy policy, List<Drop> drops, int kills) {
		
		//Iterates through all drops in that catagory
		for (int index = 0; index < drops.size(); index++) {
			Drop drop = drops.get(index);
			int minimum = drop.getMinimumAmount();
			int maximum = drop.getMaximumAmount();
			int frame = (34200 + player.dropSize + index);//collumnOffset + (index * 2);
			
			//if max = min, just send the max
			if (minimum == maximum) {
				player.getPA().sendString(Misc.getValueWithoutRepresentation(drop.getMaximumAmount()), frame);
			} else {
				player.getPA().sendString(Misc.getValueWithoutRepresentation(drop.getMinimumAmount()) + " - " + Misc.getValueWithoutRepresentation(drop.getMaximumAmount()), frame);
			}
			player.getPA().itemOnInterface(drop.getItemId(), 1, 34010+player.dropSize + index, 0);
			player.getPA().sendString(Misc.optimizeText(policy.name().toLowerCase()), 34300+player.dropSize + index);
			player.getPA().sendString(Server.itemHandler.getItemList(drop.getItemId()).itemName, 34100 + player.dropSize + index);
			if(kills == -10){
				player.getPA().sendString(1 + "/?", 34400 + player.dropSize + index);
			} else {
				player.getPA().sendString(1 + "/"+kills, 34400 + player.dropSize + index);
			}
		}
		
		player.dropSize += drops.size();
	}

	static int amountt = 0;

	private FileReader fileReader;

	/**
	 * Testing droptables of chosen npcId
	 * @param player		The player who is testing the droptable
	 * @param npcId			The npc who of which the player is testing the droptable from
	 * @param amount		The amount of times the player want to grab a drop from the npc droptable
	 */
	public void test(Player player, int npcId, int amount) {
		Optional<TableGroup> group = groups.values().stream().filter(g -> g.getNpcIds().contains(npcId)).findFirst();

		amountt = amount;

		while (amount-- > 0) {
			group.ifPresent(g -> {
				List<GameItem> drops = g.access(player, 1.0, 1);

				for (GameItem item : drops) {
					player.getItems().addItemToBank(item.getId(), item.getAmount());
				}
			});
		}
		player.sendMessage("Completed " + amountt + " drops from " + Server.npcHandler.getNpcName(npcId) + ".");
	}


}
