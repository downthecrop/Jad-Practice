package ethos.world;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Predicate;

import ethos.Config;
import ethos.model.items.GroundItem;
import ethos.model.items.Item;
import ethos.model.items.ItemList;
import ethos.model.players.Boundary;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.model.players.mode.ModeType;
import ethos.model.players.skills.Skill;
import ethos.model.players.skills.prayer.Bone;
import ethos.model.players.skills.prayer.Prayer;
import ethos.model.shops.ShopAssistant;
import ethos.util.Misc;

public class ItemHandler {

	/**
	 * A list of all {@link GroundItem}'s in the game.
	 */
	private List<GroundItem> items = new LinkedList<>();

	/**
	 * The amount of ticks an item is hidden for.
	 */
	public static final int HIDE_TICKS = 200; //Approx 2 minutes

	/**
	 * The amount of game cycles or ticks that the item will show for aften hiding
	 */
	public static final int SHOW_TICKS = 400; //Approx 4 minutes

	public ItemList[] ItemList;

	public ItemHandler() {
		ItemList = new ItemList[Config.ITEM_LIMIT];
		for (int i = 0; i < Config.ITEM_LIMIT; i++) {
			ItemList[i] = null;
		}
		loadItemList("item_config.cfg");
		loadItemPrices("item_prices.txt");
	}

	public boolean itemExists(String controller, int itemId) {
		return items.stream().anyMatch(item -> item.getController().equalsIgnoreCase(controller) && item.getId() == itemId);
	}

	public GroundItem getGroundItem(int itemId, int x, int y, int height) {
		Optional<GroundItem> item = items.stream().filter(i -> i.getId() == itemId && i.getX() == x && i.getY() == y && i.getHeight() == height).findFirst();
		return item.orElse(null);
	}

	/**
	 * Item amount
	 **/
	public int itemAmount(String player, int itemId, int itemX, int itemY, int height) {
		for (GroundItem i : items) {
			if (i.hideTicks >= 1 && player.equalsIgnoreCase(i.getController()) || i.hideTicks < 1) {
				if (i.getId() == itemId && i.getX() == itemX && i.getY() == itemY && i.getHeight() == height) {
					return i.getAmount();
				}
			}
		}
		return 0;
	}

	/**
	 * Item exists
	 **/
	public boolean itemExists(int itemId, int itemX, int itemY, int height) {
		for (GroundItem i : items) {
			if (i.getId() == itemId && i.getX() == itemX && i.getY() == itemY && i.getHeight() == height) {
				return true;
			}
		}
		return false;
	}

	public void reloadItems(Player player) {
		if (player.getMode() == null || player.getTutorial().isActive()) {
			return;
		}
		Predicate<GroundItem> visible = item -> (((player.getItems().isTradable(item.getId()) || item.getController().equalsIgnoreCase(player.playerName))
				&& player.distanceToPoint(item.getX(), item.getY()) <= 60)
				&& (item.hideTicks > 0 && item.getController().equalsIgnoreCase(player.playerName) || item.hideTicks == 0) && player.heightLevel == item.getHeight());
		if (!player.inClanWars() && !player.inClanWarsSafe() && !player.getMode().isItemScavengingPermitted()) {
			visible = visible.and(item -> item.getController().equalsIgnoreCase(player.playerName));
		}
		items.stream().filter(visible).forEach(item -> player.getItems().removeGroundItem(item));
		items.stream().filter(visible).forEach(item -> player.getItems().createGroundItem(item));
	}

	public void process() {
		Iterator<GroundItem> it = items.iterator();
		while (it.hasNext()) {
			GroundItem i = it.next();
			if (i == null)
				continue;
			if (i.hideTicks > 0) {
				i.hideTicks--;
			}
			if (i.hideTicks == 1) {
				i.hideTicks = 0;
				createGlobalItem(i);
				i.removeTicks = SHOW_TICKS;
			}
			if (i.removeTicks > 0) {
				i.removeTicks--;
			}
			if (i.removeTicks == 1) {
				i.removeTicks = 0;
				PlayerHandler.stream().filter(Objects::nonNull).filter(p -> p.distanceToPoint(i.getX(), i.getY()) <= 60)
						.forEach(p -> p.getItems().removeGroundItem(i.getId(), i.getX(), i.getY(), i.getAmount()));
				it.remove();
			}
		}
	}

	public void createUnownedGroundItem(int id, int x, int y, int height, int amount) {
		if (id > 0 && amount > 0) {
			if (id >= 2412 && id <= 2414) {
				return;
			}
			if (!Item.itemStackable[id] && amount > 0) {
				if (amount > 28) {
					amount = 28;
				}
				for (int j = 0; j < amount; j++) {
					GroundItem item = new GroundItem(id, x, y, height, 1, HIDE_TICKS, "");
					items.add(item);
				}
			} else {
				GroundItem item = new GroundItem(id, x, y, height, amount, HIDE_TICKS, "");
				items.add(item);
			}
		}
	}
	
	public void createGroundItem(Player player, int itemId, int itemX, int itemY, int height, int itemAmount,
			int playerId) {
		if (playerId < 0 || playerId > PlayerHandler.players.length - 1) {
			return;
		}
		Player owner = PlayerHandler.players[playerId];
		if (owner == null) {
			return;
		}
		if (itemId > 0 && itemAmount > 0) {
			if (itemId >= 2412 && itemId <= 2414) {
				player.sendMessage("The cape vanishes as it touches the ground.");
				return;
			}
			/**
			 * Lootvalue
			 */
			if (player.lootValue > 0) {
				if (ShopAssistant.getItemShopValue(itemId) >= player.lootValue) {
					player.getPA().stillGfx(1177, itemX, itemY, height, 5);
					player.sendMessage("@red@Your lootvalue senses a drop valued at or over "
							+ Misc.getValueWithoutRepresentation(player.lootValue) + " coins.");
				}
			}
			/**
			 * Bone crusher
			 */
			boolean crusher = player.getItems().playerHasItem(13116);
			Optional<Bone> bone = Prayer.isOperableBone(itemId);
			if (crusher && bone.isPresent()) {
				double experience = player.getRechargeItems().hasItem(13114) ? 0.75 : player.getRechargeItems().hasItem(13115) ? 1 : 0.50;
				if (itemId == bone.get().getItemId()) {
					player.getPA().addSkillXP((int) (bone.get().getExperience() * (player.getMode().getType().equals(ModeType.OSRS) ? 1 : Config.PRAYER_EXPERIENCE) * experience),
							Skill.PRAYER.getId(), true);
				}
				return;
			}
			
			if (!Item.itemStackable[itemId] && itemAmount > 0) {
				if (itemAmount > 28) {
					itemAmount = 28;
				}
				for (int j = 0; j < itemAmount; j++) {
					player.getItems().createGroundItem(itemId, itemX, itemY, 1);
					GroundItem item = new GroundItem(itemId, itemX, itemY, height, 1, HIDE_TICKS, owner.playerName);
					items.add(item);
				}
			} else {
				player.getItems().createGroundItem(itemId, itemX, itemY, itemAmount);
				GroundItem item = new GroundItem(itemId, itemX, itemY, height, itemAmount, HIDE_TICKS,
						owner.playerName);
				items.add(item);
			}
		}
	}

	public void createGroundItem(Player player, int itemId, int itemX, int itemY, int height, int itemAmount) {
		if (itemId > 0 && itemAmount > 0) {
			if (itemId >= 2412 && itemId <= 2414) {
				player.sendMessage("The cape vanishes as it touches the ground.");
				return;
			}
			if (!Item.itemStackable[itemId] && itemAmount > 0) {
				if (itemAmount > 28) {
					itemAmount = 28;
				}
				for (int j = 0; j < itemAmount; j++) {
					player.getItems().createGroundItem(itemId, itemX, itemY, 1);
					GroundItem item = new GroundItem(itemId, itemX, itemY, height, 1, HIDE_TICKS, player.playerName);
					items.add(item);
				}
			} else {
				if (itemId != 11849 && !Boundary.isIn(player, Boundary.ROOFTOP_COURSES))
					player.getItems().createGroundItem(itemId, itemX, itemY, itemAmount);
					GroundItem item = new GroundItem(itemId, itemX, itemY, height, itemAmount, HIDE_TICKS, player.playerName);
					items.add(item);
			}
		}
	}

	/**
	 * Shows items for everyone who is within 60 squares
	 **/
	public void createGlobalItem(GroundItem i) {
		for (Player p : PlayerHandler.players) {
			if (p != null) {
				Player person = p;
				if (person.getMode() == null) {
					continue;
				}
				if (!person.getMode().isItemScavengingPermitted()) {
					continue;
				}
				if (!person.playerName.equalsIgnoreCase(i.getController())) {
					if (!person.getItems().isTradable(i.getId())) {
						continue;
					}
					if (person.distanceToPoint(i.getX(), i.getY()) <= 60 && person.heightLevel == i.getHeight()) {
						person.getItems().createGroundItem(i.getId(), i.getX(), i.getY(), i.getAmount());
					}
				}
			}
		}
	}

	/**
	 * Removing the ground item
	 **/

	public void removeGroundItem(Player c, int itemId, int itemX, int itemY, int height, boolean add) {
		for (GroundItem i : items) {
			if (i.getId() == itemId && i.getX() == itemX && i.getY() == itemY && i.getHeight() == height) {
				if (i.hideTicks > 0 && i.getController().equalsIgnoreCase(c.playerName)) {
					if (add) {
						if (c.getItems().addItem(i.getId(), i.getAmount())) {
							removeControllersItem(i, c, i.getId(), i.getX(), i.getY(), i.getAmount());
							break;
						}
					} else {
						removeControllersItem(i, c, i.getId(), i.getX(), i.getY(), i.getAmount());
						break;
					}
				} else if (i.hideTicks <= 0) {
					if (add) {
						if (c.getItems().addItem(i.getId(), i.getAmount())) {
							removeGlobalItem(i, i.getId(), i.getX(), i.getY(), i.getHeight(), i.getAmount());
							break;
						}
					} else {
						removeGlobalItem(i, i.getId(), i.getX(), i.getY(), i.getHeight(), i.getAmount());
						break;
					}
				}
			}
		}
	}

	/**
	 * Remove item for just the item controller (item not global yet)
	 **/

	public void removeControllersItem(GroundItem i, Player c, int itemId, int itemX, int itemY, int itemAmount) {
		c.getItems().removeGroundItem(itemId, itemX, itemY, itemAmount);
		items.remove(i);
	}

	/**
	 * Remove item for everyone within 60 squares
	 **/

	public void removeGlobalItem(GroundItem i, int itemId, int itemX, int itemY, int height, int itemAmount) {
		for (Player p : PlayerHandler.players) {
			if (p != null) {
				Player person = p;
				if (person.distanceToPoint(itemX, itemY) <= 60 && person.heightLevel == height) {
					person.getItems().removeGroundItem(itemId, itemX, itemY, itemAmount);
				}
			}
		}
		items.remove(i);
	}

	/**
	 * The counterpart of the item whether it is the noted or un noted version
	 * 
	 * @param itemId the item id we're finding the counterpart of
	 * @return the note or unnoted version or -1 if none exists
	 */
	public int getCounterpart(int itemId) {
		if (itemId < 0) {
			return -1;
		}

		ItemList unnoted = ItemList[itemId];

		if (unnoted == null) {
			return -1;
		}

		int counterpart = unnoted.getCounterpartId();

		if (counterpart == -1) {
			return -1;
		}

		if (ItemList[counterpart].getCounterpartId() == itemId) {
			return counterpart;
		}

		return -1;
	}

	public void newItemList(int itemId, String ItemName, String ItemDescription, double ShopValue, double LowAlch, double HighAlch, int Bonuses[]) {
		ItemList newItemList = new ItemList(itemId);
		newItemList.itemName = ItemName;
		newItemList.itemDescription = ItemDescription;
		newItemList.ShopValue = ShopValue;
		newItemList.LowAlch = LowAlch;
		newItemList.HighAlch = HighAlch;
		newItemList.Bonuses = Bonuses;
		ItemList[itemId] = newItemList;
	}

	public void loadItemPrices(String filename) {
		try (Scanner s = new Scanner(new File("./Data/cfg/" + filename))) {
			while (s.hasNextLine()) {
				String[] line = s.nextLine().split(" ");
				ItemList temp = getItemList(Integer.parseInt(line[0]));
				if (temp != null)
					temp.ShopValue = Integer.parseInt(line[1]);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ItemList getItemList(int itemId) {
		if (itemId < 0 || itemId > ItemList.length) {
			return null;
		}
		return ItemList[itemId];
	}

	public boolean loadItemList(String FileName) {
		String line = "";
		String token = "";
		String token2 = "";
		String token2_2 = "";
		String[] token3 = new String[10];
		ItemList = new ItemList[Config.ITEM_LIMIT];
		for (int i = 0; i < Config.ITEM_LIMIT; i++) {
			ItemList[i] = null;
		}
		try (BufferedReader file = new BufferedReader(new FileReader("./Data/cfg/" + FileName))) {
			while ((line = file.readLine()) != null && !line.equals("[ENDOFITEMLIST]")) {
				line = line.trim();
				int spot = line.indexOf("=");
				if (spot > -1) {
					token = line.substring(0, spot);
					token = token.trim();
					token2 = line.substring(spot + 1);
					token2 = token2.trim();
					token2_2 = token2.replaceAll("\t\t", "\t");
					token2_2 = token2_2.replaceAll("\t\t", "\t");
					token2_2 = token2_2.replaceAll("\t\t", "\t");
					token2_2 = token2_2.replaceAll("\t\t", "\t");
					token2_2 = token2_2.replaceAll("\t\t", "\t");
					token3 = token2_2.split("\t");
					if (token.equals("item")) {
						int[] Bonuses = new int[12];
						for (int i = 0; i < 12; i++)
							if (token3[(6 + i)] != null) {
								Bonuses[i] = Integer.parseInt(token3[(6 + i)]);
							} else {
								break;
							}
						newItemList(Integer.parseInt(token3[0]), token3[1].replaceAll("_", " "), token3[2].replaceAll("_", " "), Double.parseDouble(token3[4]),
								Double.parseDouble(token3[4]), Double.parseDouble(token3[6]), Bonuses);
					}
				}
			}
		} catch (FileNotFoundException fileex) {
			Misc.println(FileName + ": file not found.");
			return false;
		} catch (IOException ioexception) {
			Misc.println(FileName + ": error loading file.");
			return false;
		}
		try {
			List<String> stackableData = Files.readAllLines(Paths.get("./Data/", "data", "note_ids.dat"));
			for (String data : stackableData) {
				int id = Integer.parseInt(data.split("\t")[0]);
				int counterpart = Integer.parseInt(data.split("\t")[1]);
				if (ItemList[id] == null) {
					ItemList[id] = new ItemList(id);
				}
				ItemList[id].setCounterpartId(counterpart);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}
}
