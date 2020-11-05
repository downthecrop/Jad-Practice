package ethos.model.items;

import org.apache.commons.lang3.RandomUtils;

import ethos.Config;
import ethos.Server;
import ethos.ServerState;
import ethos.model.content.SkillcapePerks;
import ethos.model.content.LootingBag.LootingBagItem;
import ethos.model.content.achievement_diary.ardougne.ArdougneDiaryEntry;
import ethos.model.content.achievement_diary.karamja.KaramjaDiaryEntry;
import ethos.model.entity.Entity;
import ethos.model.items.bank.BankItem;
import ethos.model.items.bank.BankTab;
import ethos.model.multiplayer_session.MultiplayerSessionType;
import ethos.model.multiplayer_session.duel.DuelSession;
import ethos.model.multiplayer_session.duel.DuelSessionRules.Rule;
import ethos.model.npcs.NPC;
import ethos.model.players.Boundary;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.model.players.PlayerSave;
import ethos.model.players.combat.Degrade.DegradableItem;
import ethos.model.players.combat.range.RangeData;
import ethos.model.players.mode.ModeType;
import ethos.model.players.skills.Skill;
import ethos.model.shops.ShopAssistant;
import ethos.util.Misc;

import java.util.*;
import java.util.stream.IntStream;

/**
 * Indicates Several Usage Of Items
 * 
 * @author Sanity Revised by Shawn Notes by Shawn
 */
public class ItemAssistant {

	private Player c;

	public ItemAssistant(Player client) {
		this.c = client;
	}

	public boolean updateInventory = false;

	public void updateInventory() {
		updateInventory = false;
		resetItems(3214);
	}

	public int[] Nests = { 5291, 5292, 5293, 5294, 5295, 5296, 5297, 5298, 5299, 5300, 5301, 5302, 5303, 5304 };

	public int getWornItemSlot(int itemId) {
		for (int i = 0; i < c.playerEquipment.length; i++)
			if (c.playerEquipment[i] == itemId)
				return i;
		return -1;
	}

	public void handleNests(int itemId) {
		int reward = Nests[Misc.random(14)];
		addItem(reward, 3 + Misc.random(5));
		deleteItem(itemId, 1);
		c.sendMessage("You search the nest");
	}

	/**
	 * Sends an item to the bank in any tab possible.
	 * 
	 * @param itemId the item id
	 * @param amount the item amount
	 */
	public void sendItemToAnyTab(int itemId, int amount) {
		BankItem item = new BankItem(itemId, amount);
		for (BankTab tab : c.getBank().getBankTab()) {
			if (tab.freeSlots() > 0 || tab.contains(item)) {
				c.getBank().setCurrentBankTab(tab);
				addItemToBank(itemId, amount);
				return;
			}
		}
		addItemToBank(itemId, amount);
	}

	/**
	 * Adds an item to the players inventory, bank, or drops it. It will do this under any circumstance so if it cannot be added to the inventory it will next try to send it to the
	 * bank and if it cannot, it will drop it.
	 * 
	 * @param itemId the item
	 * @param amount the amount of said item
	 */
	public void addItemUnderAnyCircumstance(int itemId, int amount) {
		if (!addItem(itemId, amount)) {
			if (c.getMode().isUltimateIronman()) {
				Server.itemHandler.createGroundItem(c, itemId, c.getX(), c.getY(), c.heightLevel, amount);
				c.sendMessage("@red@Your box has been dropped to the ground!");
				return;
			}
			sendItemToAnyTabOrDrop(new BankItem(itemId, amount), c.getX(), c.getY());
		}
	}

	/**
	 * The x and y represents the possible x and y location of the dropped item if in fact it cannot be added to the bank.
	 */
	public void sendItemToAnyTabOrDrop(BankItem item, int x, int y) {
		item = new BankItem(item.getId() + 1, item.getAmount());
		if (Item.itemIsNote[item.getId()] && bankContains(item.getId() - 2)) {
			if (isBankSpaceAvailable(item)) {
				sendItemToAnyTab(item.getId() - 1, item.getAmount());
			} else {
				Server.itemHandler.createGroundItem(c, item.getId() - 1, x, y, c.heightLevel, item.getAmount());
			}
		} else {
			sendItemToAnyTab(item.getId() - 1, item.getAmount());
		}
	}

	/**
	 * Determines if the player is wearing a specific item at a particular slot
	 * 
	 * @param itemId the item we're checking to see the player is wearing
	 * @param slot the slot the item should be detected in
	 * @return true if the item is being word
	 */
	public boolean isWearingItem(int itemId, int slot) {

		return slot>=0&&slot<=c.playerEquipment.length-1&&c.playerEquipment[slot]==itemId;
	}

	/**
	 * Check all slots and determine whether or not a slot is accompanied by that item
	 */
	public boolean isWearingItem(int itemID) {
		for (int i = 0; i < c.playerEquipment.length; i++) {
			if (c.playerEquipment[i] == itemID) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Determines if the player is wearing any of the given items.
	 * 
	 * @param items the array of item id values.
	 * @return true if the player is wearing any of the optional items.
	 */
	public boolean isWearingAnyItem(int... items) {
		for (int equipmentId : c.playerEquipment) {
			for (int item : items) {
				if (equipmentId == item) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Check all slots and determine the amount of said item worn in that slot
	 */
	public int getWornItemAmount(int itemID) {
		for (int i = 0; i < c.playerEquipment.length; i++) {
			if (c.playerEquipment[i] == itemID) {
				return c.playerEquipmentN[i];
			}
		}
		return 0;
	}

	/**
	 * Trimmed and untrimmed skillcapes.
	 */
	public int[][] skillcapes = { { 9747, 9748 }, // Attack
			{ 9753, 9754 }, // Defence
			{ 9750, 9751 }, // Strength
			{ 9768, 9769 }, // Hitpoints
			{ 9756, 9757 }, // Range
			{ 9759, 9760 }, // Prayer
			{ 9762, 9763 }, // Magic
			{ 9801, 9802 }, // Cooking
			{ 9807, 9808 }, // Woodcutting
			{ 9783, 9784 }, // Fletching
			{ 9798, 9799 }, // Fishing
			{ 9804, 9805 }, // Firemaking
			{ 9780, 9781 }, // Crafting
			{ 9795, 9796 }, // Smithing
			{ 9792, 9793 }, // Mining
			{ 9774, 9775 }, // Herblore
			{ 9771, 9772 }, // Agility
			{ 9777, 9778 }, // Thieving
			{ 9786, 9787 }, // Slayer
			{ 9810, 9811 }, // Farming
			{ 9765, 9766 } // Runecraft
	};

	/**
	 * Broken barrows items.
	 */
	public int[][] brokenBarrows = { { 4708, 4860 }, { 4710, 4866 }, { 4712, 4872 }, { 4714, 4878 }, { 4716, 4884 }, { 4720, 4896 }, { 4718, 4890 }, { 4720, 4896 }, { 4722, 4902 },
			{ 4732, 4932 }, { 4734, 4938 }, { 4736, 4944 }, { 4738, 4950 }, { 4724, 4908 }, { 4726, 4914 }, { 4728, 4920 }, { 4730, 4926 }, { 4745, 4956 }, { 4747, 4926 },
			{ 4749, 4968 }, { 4751, 4794 }, { 4753, 4980 }, { 4755, 4986 }, { 4757, 4992 }, { 4759, 4998 } };

	/**
	 * Empties all of (a) player's items.
	 */
	public void resetItems(int WriteFrame) {
		// synchronized (c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrameVarSizeWord(53);
			c.getOutStream().writeWord(WriteFrame);
			c.getOutStream().writeWord(c.playerItems.length);
			for (int i = 0; i < c.playerItems.length; i++) {
				if (c.playerItemsN[i] > 254) {
					c.getOutStream().writeByte(255);
					c.getOutStream().writeDWord_v2(c.playerItemsN[i]);
				} else {
					c.getOutStream().writeByte(c.playerItemsN[i]);
				}
				c.getOutStream().writeWordBigEndianA(c.playerItems[i]);
			}
			c.getOutStream().endFrameVarSizeWord();
			c.flushOutStream();
		}
		// }
	}

	/**
	 * Check to see if an item is noted.
	 * 
	 * @param itemId The item ID of the item which is to be checked.
	 * @return True in case the item is noted, False otherwise.
	 */
	public boolean isNoted(int itemId) {

		if (itemId<0) {
			return false;
		}
		ItemList list=Server.itemHandler.ItemList[itemId];
		return list!=null&&list.itemDescription!=null&&list.itemDescription.startsWith("Swap this note at any bank");

	}

	public void addItemToBank(int itemId, int amount) {
		BankTab tab = c.getBank().getCurrentBankTab();
		BankItem item = new BankItem(itemId + 1, amount);
		if (Item.itemIsNote[itemId]) {
			item = new BankItem(Server.itemHandler.getCounterpart(itemId) + 1, amount);
		}
		Iterator<BankTab> iterator = Arrays.asList(c.getBank().getBankTab()).iterator();
		outer: while (iterator.hasNext()) {
			BankTab t = iterator.next();
			if (t != null && t.size() > 0) {
				for (BankItem i : t.getItems()) {
					if (i.getId()==item.getId()) {
						if (t.getTabId()!=tab.getTabId()) {
							tab=t;
							break outer;
						}
					}
				}
			}
		}
		if (isNoted(itemId)) {
			item = new BankItem(Server.itemHandler.ItemList[itemId].getCounterpartId() + 1, amount);
		}
		if (tab.freeSlots() == 0) {
			c.sendMessage("The item has been dropped on the floor.");
			Server.itemHandler.createGroundItem(c, itemId, c.absX, c.absY, c.heightLevel, amount, c.getIndex());
			return;
		}
		long totalAmount = ((long) tab.getItemAmount(item) + (long) item.getAmount());
		if (totalAmount >= Integer.MAX_VALUE) {
			c.sendMessage("The item has been dropped on the floor.");
			Server.itemHandler.createGroundItem(c, itemId, c.absX, c.absY, c.heightLevel, amount, c.getIndex());
			return;
		}
		tab.add(item);
		resetTempItems();
		if (c.isBanking) {
			resetBank();
		}
		c.sendMessage(getItemName(itemId) + " x" + item.getAmount() + " has been added to your bank.");
	}

	@SuppressWarnings("unused")
	public void replaceItem(Player c, int i, int l) {

		for (int playerItem : c.playerItems) {
			if (playerHasItem(i, 1)) {
				deleteItem(i, getItemSlot(i), 1);
				addItem(l, 1);
			}
		}
	}

	/**
	 * Gets the bonus' of an item.
	 */
	public void writeBonus() {
		int offset = 0;
		String send = "";
		for (int i = 0; i < c.playerBonus.length; i++) {
			if (c.playerBonus[i] >= 0) {
				send = BONUS_NAMES[i] + ": +" + c.playerBonus[i];
			} else {
				send = BONUS_NAMES[i] + ": -" + java.lang.Math.abs(c.playerBonus[i]);
			}

			if (i == 10) {
				offset = 1;
			}
			c.getPA().sendFrame126(send, (1675 + i + offset));
		}

	}

	/**
	 * Gets the total count of (a) player's items.
	 * 
	 * @param itemID
	 * @return
	 */
	public int getTotalCount(int itemID) {
		int count = 0;
		for (int j = 0; j < c.playerItems.length; j++) {
			if (Item.itemIsNote[itemID + 1]) {
				if (itemID + 2 == c.playerItems[j])
					count += c.playerItemsN[j];
			}
			if (!Item.itemIsNote[itemID + 1]) {
				if (itemID + 1 == c.playerItems[j]) {
					count += c.playerItemsN[j];
				}
			}
		}
		for (int j = 0; j < c.bankItems.length; j++) {
			if (c.bankItems[j] == itemID + 1) {
				count += c.bankItemsN[j];
			}
		}
		return count;
	}

	/**
	 * Send the items kept on death.
	 */
	public void sendItemsKept() {
		// synchronized (c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrameVarSizeWord(53);
			c.getOutStream().writeWord(6963);
			c.getOutStream().writeWord(c.itemKeptId.length);
			for (int i = 0; i < c.itemKeptId.length; i++) {
				if (c.playerItemsN[i] > 254) {
					c.getOutStream().writeByte(255);
					c.getOutStream().writeDWord_v2(1);
				} else {
					c.getOutStream().writeByte(1);
				}
				if (c.itemKeptId[i] > 0) {
					c.getOutStream().writeWordBigEndianA(c.itemKeptId[i] + 1);
				} else {
					c.getOutStream().writeWordBigEndianA(0);
				}
			}
			c.getOutStream().endFrameVarSizeWord();
			c.flushOutStream();
		}
		// }
	}

	/**
	 * Item kept on death
	 **/
	public void keepItem(int keepItem, boolean deleteItem) {
		int value = 0;
		int item = 0;
		int slotId = 0;
		boolean itemInInventory = false;
		for (int i = 0; i < c.playerItems.length; i++) {
			if (c.playerItems[i] - 1 > 0) {
				int inventoryItemValue = ShopAssistant.getItemShopValue(c.playerItems[i] - 1);
				if (inventoryItemValue > value && (!c.invSlot[i])) {
					value = inventoryItemValue;
					item = c.playerItems[i] - 1;
					slotId = i;
					itemInInventory = true;
				}
			}
		}
		for (int i1 = 0; i1 < c.playerEquipment.length; i1++) {
			if (c.playerEquipment[i1] > 0) {
				int equipmentItemValue = ShopAssistant.getItemShopValue(c.playerEquipment[i1]);
				if (equipmentItemValue > value && (!c.equipSlot[i1])) {
					value = equipmentItemValue;
					item = c.playerEquipment[i1];
					slotId = i1;
					itemInInventory = false;
				}
			}
		}
		if (itemInInventory) {
			c.invSlot[slotId] = true;
			if (deleteItem) {
				deleteItem(c.playerItems[slotId] - 1, getItemSlot(c.playerItems[slotId] - 1), 1);
			}
		} else {
			c.equipSlot[slotId] = true;
			if (deleteItem) {
				deleteEquipment(item, slotId);
			}
		}
		c.itemKeptId[keepItem] = item;
	}

	/**
	 * Reset items kept on death.
	 **/
	public void resetKeepItems() {
		for (int i = 0; i < c.itemKeptId.length; i++) {
			c.itemKeptId[i] = -1;
		}
		for (int i1 = 0; i1 < c.invSlot.length; i1++) {
			c.invSlot[i1] = false;
		}
		for (int i2 = 0; i2 < c.equipSlot.length; i2++) {
			c.equipSlot[i2] = false;
		}
	}

	/**
	 * Deletes all of a player's items.
	 **/
	public void deleteAllItems() {
		for (int i1 = 0; i1 < c.playerEquipment.length; i1++) {
			deleteEquipment(c.playerEquipment[i1], i1);
		}
		for (int i = 0; i < c.playerItems.length; i++) {
			deleteItem(c.playerItems[i] - 1, getItemSlot(c.playerItems[i] - 1), c.playerItemsN[i]);
		}
	}

	/**
	 * Drops all items for a killer.
	 **/
	@SuppressWarnings("unused")
	public void dropAllItems() {
		Entity killer = c.getKiller();
		List<GameItem> droppedItems = new ArrayList<>();
		for (int i = 0; i < c.playerItems.length; i++) {
			if (c.playerItems[i] == 0) {
				continue;
			}
			boolean keepItem = false;
			for (int item = 0; item < Config.ITEMS_KEPT_ON_DEATH.length; item++) {
				int itemId = Config.ITEMS_KEPT_ON_DEATH[item];
				if ((c.playerItems[i] - 1) == 13307) {
					keepItem = true;
					break;
				}
				if ((c.playerItems[i] - 1) == itemId) {
					keepItem = true;
					break;
				}
			}
			if (killer != null && killer instanceof Player) {
				Player playerKiller = (Player) killer;
				if (isTradable(c.playerItems[i] - 1) && c.playerItems[i] - 1 != 13307) {
					if (playerKiller.getMode().isItemScavengingPermitted() || playerKiller.inClanWars()) {
						Server.itemHandler.createGroundItem(playerKiller, c.playerItems[i] - 1, c.getX(), c.getY(), c.heightLevel, c.playerItemsN[i], c.killerId);
					} else {
						Server.itemHandler.createUnownedGroundItem(c.playerItems[i] - 1, c.getX(), c.getY(), c.heightLevel, c.playerItemsN[i]);
					}
				}
			} else {
				if (!keepItem) {
					Server.itemHandler.createGroundItem(c, c.playerItems[i] - 1, c.getX(), c.getY(), c.heightLevel, c.playerItemsN[i], c.getIndex());
				}
			}
			addToDroppedItems(droppedItems, new GameItem(c.playerItems[i] - 1, c.playerItemsN[i]));
		}
		for (int e = 0; e < c.playerEquipment.length; e++) {
			if (c.playerEquipment[e] < 0) {
				continue;
			}
			boolean keepItem = false;
			for (int item = 0; item < Config.ITEMS_KEPT_ON_DEATH.length; item++) {
				int itemId = Config.ITEMS_KEPT_ON_DEATH[item];
				if (c.playerEquipment[e] == itemId) {
					keepItem = true;
					break;
				}
			}
			if (killer != null && killer instanceof Player) {
				Player playerKiller = (Player) killer;
					if (c.playerItems[e] == 12007) {
						c.playerItems[e] = 12005;
					}
				if (isTradable(c.playerEquipment[e])) {
					if (playerKiller.getMode().isItemScavengingPermitted()) {
						Server.itemHandler.createGroundItem(playerKiller, c.playerEquipment[e], c.getX(), c.getY(), c.heightLevel, c.playerEquipmentN[e], c.killerId);
					} else {
						Server.itemHandler.createUnownedGroundItem(c.playerEquipment[e], c.getX(), c.getY(), c.heightLevel, c.playerEquipmentN[e]);
					}
				}
			} else {
				if (!keepItem) {
					Server.itemHandler.createGroundItem(c, c.playerEquipment[e], c.getX(), c.getY(), c.heightLevel, c.playerEquipmentN[e], c.getIndex());
				}
			}
			addToDroppedItems(droppedItems, new GameItem(c.playerEquipment[e], c.playerEquipmentN[e]));
		}
		if (killer != null) {
			if (killer instanceof Player) {
				Player playerKiller = (Player) killer;
				if (Config.SERVER_STATE == ServerState.PUBLIC_PRIMARY) {
					//TODO PvP Log Kills
				}
				Server.itemHandler.createGroundItem(playerKiller, 526, c.getX(), c.getY(), c.heightLevel, 1);
			} else if (killer instanceof NPC) {
				NPC npc = (NPC) killer;
				if (Config.SERVER_STATE == ServerState.PUBLIC_PRIMARY) {
					//TODO PvM Log Kills
				}
				Server.itemHandler.createGroundItem(c, 526, c.getX(), c.getY(), c.heightLevel, 1);
			}
		}
	}

	/**
	 * Adds an untradeable item to a list if it's not already on the list. If it is, it increases the amount of the item.
	 * 
	 * @param droppedItems A list of {@link GameItem}s.
	 * @param item An item which is to be added to the list.
	 */
	private void addToDroppedItems(List<GameItem> droppedItems, GameItem item) {
		for (GameItem listElement : droppedItems) {
			if (listElement.getId() == item.getId()) {
				int newAmount = listElement.getAmount() + item.getAmount();
				listElement.setAmount(newAmount);
				return;
			}
		}
		droppedItems.add(item);
	}

	/**
	 * Untradable items with a special currency. (Tokkel, etc)
	 * 
	 * @param item
	 * @return amount
	 */
	public int getUntradePrice(int item) {
		switch (item) {
		case 2518:
		case 2524:
		case 2526:
			return 100000;
		case 2520:
		case 2522:
			return 150000;
		}
		return 0;
	}

	/**
	 * Voided items. (Not void knight items..)
	 * 
	 * @param itemId
	 */
	public void addToVoidList(int itemId) {
		switch (itemId) {
		case 2518:
			c.voidStatus[0]++;
			break;
		case 2520:
			c.voidStatus[1]++;
			break;
		case 2522:
			c.voidStatus[2]++;
			break;
		case 2524:
			c.voidStatus[3]++;
			break;
		case 2526:
			c.voidStatus[4]++;
			break;
		}
	}

	/**
	 * Counts the number of items a player possesses with given item ID.
	 * 
	 * @param itemId The ID of the item we're looking for.
	 * @param includeCounterpart True in case the counterpart (noted/unnoted) should also be included in the count, false otherwise.
	 * @return The amount of items the player possesses.
	 */
	public int getItemCount(int itemId, boolean includeCounterpart) {
		int counter = 0;
		int counterpart = -1;
		if (includeCounterpart) {
			counterpart = Server.itemHandler.getCounterpart(itemId);
		}
		for (LootingBagItem item : c.getLootingBag().items) {
			if (item.getId() == itemId || counterpart > 0 && item.getId() == counterpart) {
				counter += item.getAmount();
			}
		}
		for (GameItem item : c.getZulrahLostItems()) {
			if (item.getId() == itemId || counterpart > 0 && item.getId() == counterpart) {
				counter += item.getAmount();
			}
		}
		for (GameItem item : c.getCerberusLostItems()) {
			if (item.getId() == itemId || counterpart > 0 && item.getId() == counterpart) {
				counter += item.getAmount();
			}
		}
		for (int i = 0; i < c.playerItems.length; i++) {
			if (c.playerItems[i] == itemId + 1 || counterpart > 0 && c.playerItems[i] == counterpart + 1) {
				counter += c.playerItemsN[i];
				if (Item.itemStackable[c.playerItems[i] - 1]) {
					break;
				}
			}
		}
		for (int i = 0; i < c.playerEquipment.length; i++) {
			if (c.playerEquipment[i] == itemId || counterpart > 0 && c.playerEquipment[i] == counterpart) {
				counter += c.playerEquipmentN[i];
				if (Item.itemStackable[c.playerEquipment[i] - 1]) {
					break;
				}
			}
		}
		for (BankTab tab : c.getBank().getBankTab()) {
			if (tab == null) {
				continue;
			}
			for (BankItem item : tab.getItems()) {
				if (item.getId() == itemId + 1 || counterpart > 0 && item.getId() == counterpart + 1) {
					counter += item.getAmount();
					break;
				}
			}
		}
		return counter;
	}
	
	/**
	 * Handles tradable items.
	 */
	public boolean isTradable(int itemId) {
		if (itemId == 12899 && c.getToxicTridentCharge() > 0 || itemId == 11907 && c.getTridentCharge() > 0) {
			c.sendMessage("You cannot trade your trident whilst it has a charge.");
			return false;
		}
		if (Item.getItemName(itemId).contains("graceful")) {
			return false;
		}
		boolean CANNOT_SHARE = IntStream.of(Config.NOT_SHAREABLE).anyMatch(shareable -> shareable == itemId);
		if (CANNOT_SHARE && itemId != 13307) {
			return false;
		}
		if (itemId == 13307) {
			return true;
		}
		return true;
	}
	
	/**
	 * Adds an item to a player's inventory.
	 **/

	public boolean addItem(int item, int amount) {
		// synchronized(c) {
		if (amount < 1 && amount != 0) {
			amount = 1;
		}
		if (item <= 0 || amount <= 0) {
			return false;
		}
		if ((((freeSlots() >= 1) || playerHasItem(item, 1)) && Item.itemStackable[item]) || ((freeSlots() > 0) && !Item.itemStackable[item])) {
			for (int i = 0; i < c.playerItems.length; i++) {
				if ((c.playerItems[i] == (item + 1)) && Item.itemStackable[item] && (c.playerItems[i] > 0)) {
					c.playerItems[i] = (item + 1);
					if (((c.playerItemsN[i] + amount) < Config.MAXITEM_AMOUNT) && ((c.playerItemsN[i] + amount) > -1)) {
						c.playerItemsN[i] += amount;
					} else {
						c.playerItemsN[i] = Config.MAXITEM_AMOUNT;
					}
					if (c.getOutStream() != null && c != null) {
						c.getOutStream().createFrameVarSizeWord(34);
						c.getOutStream().writeWord(3214);
						c.getOutStream().writeByte(i);
						c.getOutStream().writeWord(c.playerItems[i]);
						if (c.playerItemsN[i] > 254) {
							c.getOutStream().writeByte(255);
							c.getOutStream().writeDWord(c.playerItemsN[i]);
						} else {
							c.getOutStream().writeByte(c.playerItemsN[i]);
						}
						c.getOutStream().endFrameVarSizeWord();
						c.flushOutStream();
					}
					i = 30;
					return true;
				}
			}
			for (int i = 0; i < c.playerItems.length; i++) {
				if (c.playerItems[i] <= 0) {
					c.playerItems[i] = item + 1;
					if ((amount < Config.MAXITEM_AMOUNT) && (amount > -1)) {
						c.playerItemsN[i] = 1;
						if (amount > 1) {
							c.getItems().addItem(item, amount - 1);
							return true;
						}
					} else {
						c.playerItemsN[i] = Config.MAXITEM_AMOUNT;
					}
					resetItems(3214);
					i = 30;
					return true;
				}
			}
			return false;
		} else {
			resetItems(3214);
			c.sendMessage("Not enough space in your inventory.");
			return false;
		}
		// }
	}

	/**
	 * Gets the item type.
	 */
	public String itemType(int item) {
		for (int i = 0; i < Item.capes.length; i++) {
			if (item == Item.capes[i])
				return "cape";
		}
		for (int i = 0; i < Item.hats.length; i++) {
			if (item == Item.hats[i])
				return "hat";
		}
		for (int i = 0; i < Item.boots.length; i++) {
			if (item == Item.boots[i])
				return "boots";
		}
		for (int i = 0; i < Item.gloves.length; i++) {
			if (item == Item.gloves[i])
				return "gloves";
		}
		for (int i = 0; i < Item.shields.length; i++) {
			if (item == Item.shields[i])
				return "shield";
		}
		for (int i = 0; i < Item.amulets.length; i++) {
			if (item == Item.amulets[i])
				return "amulet";
		}
		for (int i = 0; i < Item.arrows.length; i++) {
			if (item == Item.arrows[i])
				return "arrows";
		}
		for (int i = 0; i < Item.rings.length; i++) {
			if (item == Item.rings[i])
				return "ring";
		}
		for (int i = 0; i < Item.body.length; i++) {
			if (item == Item.body[i])
				return "body";
		}
		for (int i = 0; i < Item.legs.length; i++) {
			if (item == Item.legs[i])
				return "legs";
		}
		return "weapon";
	}

	/**
	 * Item bonuses.
	 **/
	public final String[] BONUS_NAMES = { "Stab", "Slash", "Crush", "Magic", "Range", "Stab", "Slash", "Crush", "Magic", "Range", "Strength", "Prayer" };

	/**
	 * Resets item bonuses.
	 */
	public void resetBonus() {
		for (int i = 0; i < c.playerBonus.length; i++) {
			c.playerBonus[i] = 0;
		}
	}

	/**
	 * Gets the item bonus from the item.cfg.
	 */
	public void getBonus() {
		for (int i = 0; i < c.playerEquipment.length; i++) {
			if (c.playerEquipment[i] > -1) {
				int equipmentItem = c.playerEquipment[i];
				ItemList itemList = Server.itemHandler.ItemList[equipmentItem];
				if (itemList == null) {
					continue;
				}
				for (int k = 0; k < c.playerBonus.length; k++) {
					c.playerBonus[k] += itemList.Bonuses[k];
				}
			}
		}
		if (c.getItems().isWearingItem(12926) && c.getToxicBlowpipeAmmoAmount() > 0 && c.getToxicBlowpipeCharge() > 0) {
			int dartStrength = RangeData.getRangeStr(c.getToxicBlowpipeAmmo());
			if (dartStrength > 18) {
				dartStrength = 18;
			}
			c.playerBonus[4] += dartStrength;
		}
		if (EquipmentSet.VERAC.isWearingBarrows(c) && isWearingItem(12853)) {
			c.playerBonus[11] += 4;
		}
	}

	/**
	 * Weapon type.
	 **/
	public void sendWeapon(int Weapon, String WeaponName) {
		String WeaponName2 = WeaponName.replaceAll("Bronze", "");
		WeaponName2 = WeaponName2.replaceAll("Iron", "");
		WeaponName2 = WeaponName2.replaceAll("Steel", "");
		WeaponName2 = WeaponName2.replaceAll("Black", "");
		WeaponName2 = WeaponName2.replaceAll("Mithril", "");
		WeaponName2 = WeaponName2.replaceAll("Adamant", "");
		WeaponName2 = WeaponName2.replaceAll("Rune", "");
		WeaponName2 = WeaponName2.replaceAll("Granite", "");
		WeaponName2 = WeaponName2.replaceAll("Dragon", "");
		WeaponName2 = WeaponName2.replaceAll("Drag", "");
		WeaponName2 = WeaponName2.replaceAll("Crystal", "");
		WeaponName2 = WeaponName2.trim();
		/**
		 * Attack styles.
		 */
		if (WeaponName.equals("Unarmed")) {
			c.setSidebarInterface(0, 5855); // punch, kick, block
			c.getPA().sendFrame126(WeaponName, 5857);
		} else if (WeaponName.endsWith("whip") || WeaponName.contains("tentacle")) {
			c.setSidebarInterface(0, 12290); // flick, lash, deflect
			c.getPA().sendFrame246(12291, 200, Weapon);
			c.getPA().sendFrame126(WeaponName, 12293);
		} else if (WeaponName.endsWith("bow") || WeaponName.contains("shortbow (i)") || WeaponName.endsWith("10") || WeaponName.endsWith("bow full")
				|| WeaponName.startsWith("seercull") || WeaponName.contains("blowpipe") || WeaponName.contains("ballista") || WeaponName.contains("thrownaxe") || WeaponName.contains("chinchompa")) {
			c.setSidebarInterface(0, 1764); // accurate, rapid, longrange
			c.getPA().sendFrame246(1765, 200, Weapon);
			c.getPA().sendFrame126(WeaponName, 1767);
		} else if ((WeaponName.startsWith("Staff") || WeaponName.endsWith("staff") || WeaponName.equals("Slayer's staff (e)") || WeaponName.endsWith("of the seas") || WeaponName.endsWith("wand"))
				&& !WeaponName.contains("dead") || WeaponName.equals("Ancient staff")) {
			c.setSidebarInterface(0, 328);
			c.getPA().sendFrame246(329, 200, Weapon);
			c.getPA().sendFrame126(WeaponName, 331);
		} else if (WeaponName2.startsWith("dart") || WeaponName2.startsWith("knife") || WeaponName2.startsWith("javelin") || WeaponName.equalsIgnoreCase("toktz-xil-ul")) {
			c.setSidebarInterface(0, 4446); // accurate, rapid, longrange
			c.getPA().sendFrame246(4447, 200, Weapon);
			c.getPA().sendFrame126(WeaponName, 4449);
		} else if (WeaponName2.startsWith("dagger") || WeaponName2.contains("anchor") || WeaponName2.contains("sword") || WeaponName2.contains("byssal dagger")) {
			c.setSidebarInterface(0, 2276); // stab, lunge, slash, block
			c.getPA().sendFrame246(2277, 200, Weapon);
			c.getPA().sendFrame126(WeaponName, 2279);
		} else if (WeaponName2.startsWith("pickaxe")) {
			c.setSidebarInterface(0, 5570); // spike, impale, smash, block
			c.getPA().sendFrame246(5571, 200, Weapon);
			c.getPA().sendFrame126(WeaponName, 5573);
		} else if (WeaponName2.startsWith("axe") || WeaponName2.startsWith("battleaxe")) {
			c.setSidebarInterface(0, 1698); // chop, hack, smash, block
			c.getPA().sendFrame246(1699, 200, Weapon);
			c.getPA().sendFrame126(WeaponName, 1701);
		} else if (WeaponName2.startsWith("halberd")) {
			c.setSidebarInterface(0, 8460); // jab, swipe, fend
			c.getPA().sendFrame246(8461, 200, Weapon);
			c.getPA().sendFrame126(WeaponName, 8463);
		} else if (WeaponName.equals("Staff of the dead") || WeaponName.equals("Toxic staff of the dead")) {
			c.setSidebarInterface(0, 28500);
			c.getPA().sendFrame246(28500, 200, Weapon);
			c.getPA().sendFrame126(WeaponName, 331);
		} else if (WeaponName2.startsWith("Scythe")) {
			c.setSidebarInterface(0, 8460); // jab, swipe, fend
			c.getPA().sendFrame246(8461, 200, Weapon);
			c.getPA().sendFrame126(WeaponName, 8463);
		} else if (WeaponName2.startsWith("spear") || c.playerEquipment[c.playerWeapon] == 13905 || 
				WeaponName2.toLowerCase().contains("zamorakian") || WeaponName2.toLowerCase().contains("zamorakian hasta")) {
			c.setSidebarInterface(0, 4679); // lunge, swipe, pound, block
			c.getPA().sendFrame246(4680, 200, Weapon);
			c.getPA().sendFrame126(WeaponName, 4682);
		} else if (WeaponName2.toLowerCase().contains("mace")) {
			c.setSidebarInterface(0, 3796);
			c.getPA().sendFrame246(3797, 200, Weapon);
			c.getPA().sendFrame126(WeaponName, 3799);
		} else if (WeaponName2.contains("warhammer") || c.playerEquipment[c.playerWeapon] == 4153 || c.playerEquipment[c.playerWeapon] == 13263 || c.playerEquipment[c.playerWeapon] == 12848 || c.playerEquipment[c.playerWeapon] == 13902) {
			c.setSidebarInterface(0, 425); // war hammer equip.
			c.getPA().sendFrame246(426, 200, Weapon);
			c.getPA().sendFrame126(WeaponName, 428);
		} else if (WeaponName2.equals("claws")) {
			c.setSidebarInterface(0, 7762);
			c.getPA().sendFrame246(7763, 200, Weapon);
			c.getPA().sendFrame126(WeaponName, 7765);
		} else {
			c.setSidebarInterface(0, 2423); // chop, slash, lunge, block
			c.getPA().sendFrame246(2424, 200, Weapon);
			c.getPA().sendFrame126(WeaponName, 2426);
		}
	}

	/**
	 * Two handed weapon check.
	 **/
	public boolean is2handed(String itemName, int itemId) {

		if (itemName.contains("godsword") || itemName.contains("crystal") || itemName.contains("aradomin sword")
				|| itemName.contains("2h") || itemName.contains("spear") || itemName.contains("halberd")
				|| itemName.contains("longbow") || itemName.contains("shortbow") || itemName.contains("ark bow")
				|| itemName.contains("ahrim") || itemName.contains("karil") || itemName.contains("verac")
				|| itemName.contains("guthan") || itemName.contains("dharok") || itemName.contains("torag") || 
				itemName.contains("abyssal bludgeon") || itemName.contains("spade") || itemName.contains("casket") || 
				itemName.contains("clueless") || itemName.contains("ballista") || itemName.contains("hunting knife") || itemName.contains("elder maul") || itemName.contains("bulwark") || itemName.contains("claws")) {
			return true;
		}
		switch (itemId) {
		case 12926:
		case 6724:
		case 11838:
		case 12809:
		case 14484:
		case 4153:
		case 12848:
		case 6528:
		case 10887:
		case 12424:
		case 20784:
		case 20997:
			return true;
		}
		return false;
	}

	/**
	 * Adds special attack bar to special attack weapons. Removes special attack bar to weapons that do not have special attacks.
	 **/
	public void addSpecialBar(int weapon) {
		switch (weapon) {
		case 14484: //Claw
		case 11791:
		case 12904:
		case 20784:
			c.getPA().sendFrame171(0, 7800);
			specialAmount(weapon, c.specAmount, 7812);
			break;
		case 4151: // whip
		case 12773:
		case 12774:
		case 12006:
			c.getPA().sendFrame171(0, 12323);
			specialAmount(weapon, c.specAmount, 12335);
			break;

		case 859: // magic bows
		case 861:
		case 12424:
		case 11235:
		case 12765:
		case 12766:
		case 12767:
		case 12768:
		case 11785:
		case 12788:
		case 12926:
		case 19478:
		case 19481:
		case 20849:
			c.getPA().sendFrame171(0, 7549);
			specialAmount(weapon, c.specAmount, 7561);
			break;

		case 4587: // dscimmy
			c.getPA().sendFrame171(0, 7599);
			specialAmount(weapon, c.specAmount, 7611);
			break;

		case 3204: // d hally
		case 13092:
			c.getPA().sendFrame171(0, 8493);
			specialAmount(weapon, c.specAmount, 8505);
			break;

		case 1377: // d battleaxe
			c.getPA().sendFrame171(0, 7499);
			specialAmount(weapon, c.specAmount, 7511);
			break;
		case 12848:
		case 4153: // gmaul
		case 13263:
		case 13576:
			c.getPA().sendFrame171(0, 7474);
			specialAmount(weapon, c.specAmount, 7486);
			break;

		case 1249: // dspear
		case 1263:
		case 21028:
		case 5716:
		case 5730:
		case 13905:
		case 11824:
		case 11889:
			c.getPA().sendFrame171(0, 7674);
			specialAmount(weapon, c.specAmount, 7686);
			break;

		case 1215:// dragon dagger
		case 1231:
		case 5680:
		case 5698:
		case 1305: // dragon long
		case 11802:
		case 11806:
		case 11808:
		case 11838:
		case 12809:
		case 11804:
		case 10887:
		case 13899:
		case 13265:
		case 13267:
		case 13269:
		case 13271:
		case 21009: //Dragon sword
			c.getPA().sendFrame171(0, 7574);
			specialAmount(weapon, c.specAmount, 7586);
			break;
		case 13902: // Statius War
			c.getPA().sendFrame171(0, 7474);
			specialAmount(weapon, c.specAmount, 7486);
			break;
		case 1434: // dragon mace
		case 11061:
			c.getPA().sendFrame171(0, 7624);
			specialAmount(weapon, c.specAmount, 7636);
			break;

		default:
			c.getPA().sendFrame171(1, 7624); // mace interface
			c.getPA().sendFrame171(1, 7474); // hammer, gmaul
			c.getPA().sendFrame171(1, 7499); // axe
			c.getPA().sendFrame171(1, 7549); // bow interface
			c.getPA().sendFrame171(1, 7574); // sword interface
			c.getPA().sendFrame171(1, 7599); // scimmy sword interface, for most
			c.getPA().sendFrame171(1, 8493);
			c.getPA().sendFrame171(1, 12323); // whip interface
			break;
		}
	}

	/**
	 * Special attack bar filling amount.
	 **/
	public void specialAmount(int weapon, double specAmount, int barId) {
		c.specBarId = barId;
		c.getPA().sendFrame70(specAmount >= 10 ? 500 : 0, 0, (--barId));
		c.getPA().sendFrame70(specAmount >= 9 ? 500 : 0, 0, (--barId));
		c.getPA().sendFrame70(specAmount >= 8 ? 500 : 0, 0, (--barId));
		c.getPA().sendFrame70(specAmount >= 7 ? 500 : 0, 0, (--barId));
		c.getPA().sendFrame70(specAmount >= 6 ? 500 : 0, 0, (--barId));
		c.getPA().sendFrame70(specAmount >= 5 ? 500 : 0, 0, (--barId));
		c.getPA().sendFrame70(specAmount >= 4 ? 500 : 0, 0, (--barId));
		c.getPA().sendFrame70(specAmount >= 3 ? 500 : 0, 0, (--barId));
		c.getPA().sendFrame70(specAmount >= 2 ? 500 : 0, 0, (--barId));
		c.getPA().sendFrame70(specAmount >= 1 ? 500 : 0, 0, (--barId));
		updateSpecialBar();
		sendWeapon(weapon, getItemName(weapon));
	}

	/**
	 * Special attack text.
	 **/
	public void updateSpecialBar() {
		String percent = Double.toString(c.specAmount);
		if (percent.contains(".")) {
			percent = percent.replace(".", "");
		}
		if (percent.startsWith("0") && !percent.equals("00")) {
			percent = percent.replace("0", "");
		}
		if (percent.startsWith("0") && percent.equals("00")) {
			percent = percent.replace("00", "0");
		}
		c.getPA().sendSpecialAttack(Integer.valueOf(percent), c.usingSpecial ? 1 : 0);
		c.getPA().sendFrame126(c.usingSpecial ? "@yel@Special Attack (" + percent + "%)" : "@bla@Special Attack (" + percent + "%)", c.specBarId);
	}

	/**
	 * Wielding items.
	 **/
	public boolean wearItem(int wearID, int slot) {
		c.getSkilling().stop();
		// synchronized (c) {
		int targetSlot = 0;
		boolean canWearItem = true;
		if(c.insidePost) {
			return false;
		}
		if (c.playerItems[slot] == (wearID + 1)) {
			ItemDefinition item = ItemDefinition.forId(wearID);
			if (item == null) {
				if (wearID == 15098) {
					return false;
				}
				
				c.sendMessage("This item is currently unwearable.");
				return false;
			}
			boolean contains = IntStream.of(c.GRACEFUL).anyMatch(x -> x == wearID);
			if (contains) {
				c.graceSum();
			}
			switch (wearID) {
				case 21003:
					if(c.playerLevel[0] < 75) {
						c.sendMessage("You must have a attack level of at least 75 to weild the Elder Maul");
						return false;
					}
					if(c.playerLevel[2] < 75) {
						c.sendMessage("You must have a strength level of at least 75 to weild the Elder Maul");
						return false;
					}
					break;

				case 13235:
					if(c.playerLevel[6] < 75) {
						c.sendMessage("You must have a magic level of at least 75 to wear these boots");
						return false;
					}
					if(c.playerLevel[1] < 75) {
						c.sendMessage("You must have a defence level of at least 75 to wear these boots");
						return false;
					}
					break;
				case 13237:
					if(c.playerLevel[4] < 75) {
						c.sendMessage("You must have a ranged level of at least 75 to wear these boots");
						return false;
					}
					if(c.playerLevel[1] < 75) {
						c.sendMessage("You must have a defence level of at least 75 to wear these boots");
						return false;
					}
					break;
				case 13239:
					if(c.playerLevel[1] < 75) {
						c.sendMessage("You must have a defence level of at least 75 to wear these boots");
						return false;
					}
					if(c.playerLevel[2] < 75) {
						c.sendMessage("You must have a strength level of at least 75 to wear these boots");
						return false;
					}
				case 20784:
					if(c.playerLevel[0] < 60) {
						c.sendMessage("You must have a attack level of at least 60 to weild Dragon Claws.");
						return false;
					}
					break;
				case 13199:
				case 12931:
				case 13197:
					if(c.playerLevel[1] < 75) {
						c.sendMessage("You must have a defence level of at least 75 to wear this helm.");
						return false;
					}
					break;
				case 21633:
					if(c.playerLevel[6] < 70) {
						c.sendMessage("You must have a magic level of at least 70 to wear this Ancient Shield");
						return false;
					}
					if(c.playerLevel[1] < 75) {
						c.sendMessage("You must have a defence level of at least 75 to wear this Ancient Shield ");
						return false;
					}
					break;
				case 21298:
				case 21301:
				case 21304:
					if(c.playerLevel[1] < 60) {
						c.sendMessage("You must have a defence level of at least 60 to wear Obsidian Armour.");
						return false;
					}
					break;
				case 19481:
					if(c.playerLevel[4] < 75) {
						c.sendMessage("You must have a ranged level of at least 75 to weild the Heavy Ballista.");
						return false;
					}
					break;
				case 19478:
					if(c.playerLevel[4] < 65) {
						c.sendMessage("You must have a ranged level of at least 65 to weild the Light Ballista.");
						return false;
					}
					break;
			case 13280:
			case 13329:
			case 13337:
			case 13331:
			case 13333:
			case 13335:
			case 20760:
				if (!c.maxRequirements(c)) {
					c.sendMessage("You must have maxed out all skills in order to wear the max cape.");
					return false;
				}
				break;
				
			case 20005:
			case 20017:
				if (!c.getPA().morphPermissions()) {
					return false;
				}
				for (int i = 0; i <= 12; i++) {
					c.setSidebarInterface(i, 6014);
				}
				c.npcId2 = wearID == 20017 ? 7315 : 7314;
				c.isNpc = true;
				c.updateRequired = true;
				c.morphed = true;
				c.setAppearanceUpdateRequired(true);
				break;
			
			case 1409:
				if (Boundary.isIn(c, Boundary.ARDOUGNE_BOUNDARY)) {
					c.getDiaryManager().getArdougneDiary().progress(ArdougneDiaryEntry.IBANS_STAFF);
				}
				break;
				
			case 6570:
				if (Boundary.isIn(c, Boundary.TZHAAR_CITY_BOUNDARY)) {
					if (!c.getDiaryManager().getKaramjaDiary().hasCompleted("HARD")) {
						c.sendMessage("@red@If you complete all the hard tasks in the karamja diary, this will");
						c.sendMessage("@red@count towards your elite tasks!");
					} else {
						c.getDiaryManager().getKaramjaDiary().progress(KaramjaDiaryEntry.EQUIP_FIRE_CAPE);
					}
				}
				break;
				
			case 13069:
			case 13070:
				if (!c.getAchievements().hasCompletedAll()) {
					c.sendMessage("You can not wear this without finishing all the achievements.");
					return false;
				}
				break;
			case 10501:
				if (Boundary.isIn(c, Boundary.DUEL_ARENA)) {
					return false;
				}
				c.getPA().showOption(3, 0, "Throw-At", 1);
				break;
			}
			for (int i = 0; i < item.getRequirements().length; i++) {
				if (c.getLevelForXP(c.playerXP[i]) < item.getRequirements()[i]) {
					c.sendMessage("You need an " + Config.SKILL_NAME[i] + " level of " + item.getRequirements()[i] + " to wear this item.");
					return false;
				}
			}
			Optional<DegradableItem> degradable = DegradableItem.forId(wearID);
			if (degradable.isPresent()) {
				if (c.claimDegradableItem[degradable.get().ordinal()]) {
					c.sendMessage("A previous item similar to this has degraded. You must go to the old man");
					c.sendMessage("in edgeville to claim this item.");
					return false;
				}
			}
			targetSlot = item.getSlot();

			if (Boundary.isIn(c, Boundary.DUEL_ARENA)) {
				DuelSession session = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c, MultiplayerSessionType.DUEL);
				if (!Objects.isNull(session)) {
					if (targetSlot == c.playerHat && session.getRules().contains(Rule.NO_HELM)) {
						c.sendMessage("Wearing helmets has been disabled for this duel.");
						return false;
					}
					if (targetSlot == c.playerAmulet && session.getRules().contains(Rule.NO_AMULET)) {
						c.sendMessage("Wearing amulets has been disabled for this duel.");
						return false;
					}
					if (targetSlot == c.playerArrows && session.getRules().contains(Rule.NO_ARROWS)) {
						c.sendMessage("Wearing arrows has been disabled for this duel.");
						return false;
					}
					if (targetSlot == c.playerChest && session.getRules().contains(Rule.NO_BODY)) {
						c.sendMessage("Wearing platebodies has been disabled for this duel.");
						return false;
					}
					if (targetSlot == c.playerFeet && session.getRules().contains(Rule.NO_BOOTS)) {
						c.sendMessage("Wearing boots has been disabled for this duel.");
						return false;
					}
					if (targetSlot == c.playerHands && session.getRules().contains(Rule.NO_GLOVES)) {
						c.sendMessage("Wearing gloves has been disabled for this duel.");
						return false;
					}
					if (targetSlot == c.playerCape && session.getRules().contains(Rule.NO_CAPE)) {
						c.sendMessage("Wearing capes has been disabled for this duel.");
						return false;
					}
					if (targetSlot == c.playerLegs && session.getRules().contains(Rule.NO_LEGS)) {
						c.sendMessage("Wearing platelegs has been disabled for this duel.");
						return false;
					}
					if (targetSlot == c.playerRing && session.getRules().contains(Rule.NO_RINGS)) {
						c.sendMessage("Wearing a ring has been disabled for this duel.");
						return false;
					}
					if (targetSlot == c.playerWeapon && session.getRules().contains(Rule.NO_WEAPON)) {
						c.sendMessage("Wearing weapons has been disabled for this duel.");
						return false;
					}
					if (session.getRules().contains(Rule.NO_SHIELD)) {
						if (targetSlot == c.playerShield || targetSlot == c.playerWeapon && is2handed(getItemName(wearID).toLowerCase(), wearID)) {
							c.sendMessage("Wearing shields and 2handed weapons has been disabled for this duel.");
							return false;
						}
					}
				}
			}
			if (targetSlot == 3) {
				c.spellId = 0;
				c.usingMagic = false;
				c.autocasting = false;
				c.autocastId = 0;
				c.getPA().sendFrame36(108, 0);
				c.usingSpecial = false;
				addSpecialBar(wearID);
				c.getItems().updateSpecialBar();
				c.getPA().resetAutocast();
				if (wearID != 4153 && wearID != 12848) {
					c.getCombat().resetPlayerAttack();
				}
			}
			if (targetSlot == -1 || !item.isWearable()) {
				if (wearID >= 5509 && wearID <= 5512 || wearID == 21347 || wearID == 15098 || wearID == 11918 || wearID == 13656 || wearID == 7959 || wearID == 7960) {
					return false;
				} else {
				c.sendMessage("This item cannot be worn.");
				return false;
				}
			}
			if (!canWearItem) {
				return false;
			}

			int wearAmount = c.playerItemsN[slot];
			if (wearAmount < 1) {
				return false;
			}
			if (slot >= 0 && wearID >= 0) {
				int toEquip = c.playerItems[slot];
				int toEquipN = c.playerItemsN[slot];
				int toRemove = c.playerEquipment[targetSlot];
				int toRemoveN = c.playerEquipmentN[targetSlot];
				boolean stackable = false;
				stackable=getItemName(toRemove).contains("javelin")||getItemName(toRemove).contains("dart")||getItemName(toRemove).contains("knife")
						||getItemName(toRemove).contains("bolt")||getItemName(toRemove).contains("arrow")||getItemName(toRemove).contains("Bolt")
						||getItemName(toRemove).contains("bolts")||getItemName(toRemove).contains("thrownaxe")||getItemName(toRemove).contains("throwing");
				if (toEquip == toRemove + 1 && Item.itemStackable[toRemove]) {
					deleteItem(toRemove, getItemSlot(toRemove), toEquipN);
					c.playerEquipmentN[targetSlot] += toEquipN;
				} else if (targetSlot != 5 && targetSlot != 3) {
					if (playerHasItem(toRemove, 1) &&stackable) {
						c.playerItems[slot] = 0;// c.playerItems[slot] =
												// toRemove + 1;
						c.playerItemsN[slot] = 0;// c.playerItemsN[slot] =
													// toRemoveN;
						if (toRemove > 0 && toRemoveN > 0) // c.playerEquipment[targetSlot]
															// = toEquip - 1;
							addItem(toRemove, toRemoveN);// c.playerEquipmentN[targetSlot]
															// = toEquipN;
					} else {
						c.playerItems[slot] = toRemove + 1;
						c.playerItemsN[slot] = toRemoveN;
					}
					c.playerEquipment[targetSlot] = toEquip - 1;
					c.playerEquipmentN[targetSlot] = toEquipN;
				} else if (targetSlot == 5) {
					boolean wearing2h = is2handed(getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase(), c.playerEquipment[c.playerWeapon]);
					if (wearing2h) {
						toRemove = c.playerEquipment[c.playerWeapon];
						toRemoveN = c.playerEquipmentN[c.playerWeapon];
						c.playerEquipment[c.playerWeapon] = -1;
						c.playerEquipmentN[c.playerWeapon] = 0;
						updateSlot(c.playerWeapon);
					}
					c.playerItems[slot] = toRemove + 1;
					c.playerItemsN[slot] = toRemoveN;
					c.playerEquipment[targetSlot] = toEquip - 1;
					c.playerEquipmentN[targetSlot] = toEquipN;
				} else if (targetSlot == 3) {
					boolean is2h = is2handed(getItemName(wearID).toLowerCase(), wearID);
					boolean wearingShield = c.playerEquipment[c.playerShield] > 0;
					boolean wearingWeapon = c.playerEquipment[c.playerWeapon] > 0;
					if (is2h) {
						if (wearingShield && wearingWeapon) {
							if (freeSlots() > 0) {
								if (playerHasItem(toRemove, 1) &&stackable) {
									c.playerItems[slot] = 0;// c.playerItems[slot]
															// = toRemove + 1;
									c.playerItemsN[slot] = 0;// c.playerItemsN[slot]
																// = toRemoveN;
									if (toRemove > 0 && toRemoveN > 0) // c.playerEquipment[targetSlot]
																		// =
																		// toEquip
																		// - 1;
										addItem(toRemove, toRemoveN);// c.playerEquipmentN[targetSlot]
																		// =
																		// toEquipN;
								} else {
									c.playerItems[slot] = toRemove + 1;
									c.playerItemsN[slot] = toRemoveN;
								}
								c.playerEquipment[targetSlot] = toEquip - 1;
								c.playerEquipmentN[targetSlot] = toEquipN;
								removeItem(c.playerEquipment[c.playerShield], c.playerShield);
							} else {
								c.sendMessage("You do not have enough inventory space to do this.");
								return false;
							}
						} else if (wearingShield && !wearingWeapon) {
							c.playerItems[slot] = c.playerEquipment[c.playerShield] + 1;
							c.playerItemsN[slot] = c.playerEquipmentN[c.playerShield];
							c.playerEquipment[targetSlot] = toEquip - 1;
							c.playerEquipmentN[targetSlot] = toEquipN;
							c.playerEquipment[c.playerShield] = -1;
							c.playerEquipmentN[c.playerShield] = 0;
							updateSlot(c.playerShield);
						} else {
							if (playerHasItem(toRemove, 1) &&stackable) {
								c.playerItems[slot] = 0;// c.playerItems[slot] =
														// toRemove + 1;
								c.playerItemsN[slot] = 0;// c.playerItemsN[slot]
															// = toRemoveN;
								if (toRemove > 0 && toRemoveN > 0) // c.playerEquipment[targetSlot]
																	// = toEquip
																	// - 1;
									addItem(toRemove, toRemoveN);// c.playerEquipmentN[targetSlot]
																	// =
																	// toEquipN;
							} else {
								c.playerItems[slot] = toRemove + 1;
								c.playerItemsN[slot] = toRemoveN;
							}
							c.playerEquipment[targetSlot] = toEquip - 1;
							c.playerEquipmentN[targetSlot] = toEquipN;
						}
					} else {
						if (playerHasItem(toRemove, 1) &&stackable) {
							c.playerItems[slot] = 0;// c.playerItems[slot] =
													// toRemove + 1;
							c.playerItemsN[slot] = 0;// c.playerItemsN[slot] =
														// toRemoveN;
							if (toRemove > 0 && toRemoveN > 0) // c.playerEquipment[targetSlot]
																// = toEquip -
																// 1;
								addItem(toRemove, toRemoveN);// c.playerEquipmentN[targetSlot]
																// = toEquipN;
						} else {
							c.playerItems[slot] = toRemove + 1;
							c.playerItemsN[slot] = toRemoveN;
						}
						c.playerEquipment[targetSlot] = toEquip - 1;
						c.playerEquipmentN[targetSlot] = toEquipN;
					}
				}
				//GameItem value = new GameItem(c.playerEquipment[targetSlot], c.playerEquipmentN[targetSlot]);
				//c.getEquipment().update(Slot.valueOf(targetSlot), value);
				resetItems(3214);
			}
			if (c.getOutStream() != null && c != null) {
				c.getOutStream().createFrameVarSizeWord(34);
				c.getOutStream().writeWord(1688);
				c.getOutStream().writeByte(targetSlot);
				c.getOutStream().writeWord(wearID + 1);

				if (c.playerEquipmentN[targetSlot] > 254) {
					c.getOutStream().writeByte(255);
					c.getOutStream().writeDWord(c.playerEquipmentN[targetSlot]);
				} else {
					c.getOutStream().writeByte(c.playerEquipmentN[targetSlot]);
				}

				c.getOutStream().endFrameVarSizeWord();
				c.flushOutStream();
			}
			sendWeapon(c.playerEquipment[c.playerWeapon], getItemName(c.playerEquipment[c.playerWeapon]));
			resetBonus();
			getBonus();
			writeBonus();
			if (c.inGodwars()) {
				c.updateGodItems();
			}
			c.getCombat().getPlayerAnimIndex(getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
			c.getPA().requestUpdates();
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Indicates the action to wear an item.
	 *
	 */
	public void updateItems() {
		resetItems(3214);
		c.updateItems = false;
	}

	public void wearItem(int wearID, int wearAmount, int targetSlot) {
		// synchronized (c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrameVarSizeWord(34);
			c.getOutStream().writeWord(1688);
			c.getOutStream().writeByte(targetSlot);
			c.getOutStream().writeWord(wearID + 1);
			if (wearAmount > 254) {
				c.getOutStream().writeByte(255);
				c.getOutStream().writeDWord(wearAmount);
			} else {
				c.getOutStream().writeByte(wearAmount);
			}
			c.getOutStream().endFrameVarSizeWord();
			c.flushOutStream();
			c.playerEquipment[targetSlot] = wearID;
			c.playerEquipmentN[targetSlot] = wearAmount;
			c.getItems().sendWeapon(c.playerEquipment[c.playerWeapon], getItemName(c.playerEquipment[c.playerWeapon]));
			c.getItems().resetBonus();
			c.updateItems = true;
			c.getItems().getBonus();
			c.getItems().writeBonus();
			c.getCombat().getPlayerAnimIndex(getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
			c.updateRequired = true;
			c.setAppearanceUpdateRequired(true);
		}
		// }
	}

	/**
	 * Updates the slot when wielding an item.
	 * 
	 * @param slot
	 */
	public void updateSlot(int slot) {
		// synchronized (c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrameVarSizeWord(34);
			c.getOutStream().writeWord(1688);
			c.getOutStream().writeByte(slot);
			c.getOutStream().writeWord(c.playerEquipment[slot] + 1);
			if (c.playerEquipmentN[slot] > 254) {
				c.getOutStream().writeByte(255);
				c.getOutStream().writeDWord(c.playerEquipmentN[slot]);
			} else {
				c.getOutStream().writeByte(c.playerEquipmentN[slot]);
			}
			c.getOutStream().endFrameVarSizeWord();
			c.flushOutStream();
		}
		// }

	}

	/**
	 * Removes a wielded item.
	 **/
	public void removeItem(int wearID, int slot) {
		// synchronized(c) {
		c.getSkilling().stop();
		if(c.insidePost) {
			return;
		}
		if (c.getOutStream() != null && c != null) {
			if (c.playerEquipment[slot] > -1) {
				if (addItem(c.playerEquipment[slot], c.playerEquipmentN[slot])) {
					c.playerEquipment[slot] = -1;
					c.playerEquipmentN[slot] = 0;
					sendWeapon(c.playerEquipment[c.playerWeapon], getItemName(c.playerEquipment[c.playerWeapon]));
					resetBonus();
					getBonus();
					writeBonus();
					boolean contains = IntStream.of(c.GRACEFUL).anyMatch(x -> x == wearID);
					if (contains) {
						c.graceSum();
					}
					switch (wearID) {
					case 10501:
						c.getPA().showOption(3, 0, "Null", 1);
						break;
					}
					if (c.inGodwars()) {
						c.updateGodItems();
					}
					c.getCombat().getPlayerAnimIndex(getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
					c.getOutStream().createFrame(34);
					c.getOutStream().writeWord(6);
					c.getOutStream().writeWord(1688);
					c.getOutStream().writeByte(slot);
					c.getOutStream().writeWord(0);
					c.getOutStream().writeByte(0);
					c.flushOutStream();
					c.updateRequired = true;
					c.setAppearanceUpdateRequired(true);
					c.isFullHelm = Item.isFullHat(c.playerEquipment[c.playerHat]);
					c.isFullMask = Item.isFullMask(c.playerEquipment[c.playerHat]);
					c.isFullBody = Item.isFullBody(c.playerEquipment[c.playerChest]);
				}
			}
		}
		// }
	}

	/**
	 * Items in your bank.
	 */
	public void rearrangeBank() {
		int totalItems = 0;
		int highestSlot = 0;
		for (int i = 0; i < Config.BANK_SIZE; i++) {
			if (c.bankItems[i] != 0) {
				totalItems++;
				if (highestSlot <= i) {
					highestSlot = i;
				}
			}
		}

		for (int i = 0; i <= highestSlot; i++) {
			if (c.bankItems[i] == 0) {
				boolean stop = false;

				for (int k = i; k <= highestSlot; k++) {
					if (c.bankItems[k] != 0 && !stop) {
						int spots = k - i;
						for (int j = k; j <= highestSlot; j++) {
							c.bankItems[j - spots] = c.bankItems[j];
							c.bankItemsN[j - spots] = c.bankItemsN[j];
							stop = true;
							c.bankItems[j] = 0;
							c.bankItemsN[j] = 0;
						}
					}
				}
			}
		}

		int totalItemsAfter = 0;
		for (int i = 0; i < Config.BANK_SIZE; i++) {
			if (c.bankItems[i] != 0) {
				totalItemsAfter++;
			}
		}

		if (totalItems != totalItemsAfter)
			c.disconnected = true;
	}

	/**
	 * Items displayed on the armor interface.
	 * 
	 * @param id
	 * @param amount
	 */
	public void itemOnInterface(int id, int amount) {
		// synchronized (c) {
		c.getOutStream().createFrameVarSizeWord(53);
		c.getOutStream().writeWord(2274);
		c.getOutStream().writeWord(1);
		if (amount > 254) {
			c.getOutStream().writeByte(255);
			c.getOutStream().writeDWord_v2(amount);
		} else {
			c.getOutStream().writeByte(amount);
		}
		c.getOutStream().writeWordBigEndianA(id);
		c.getOutStream().endFrameVarSizeWord();
		c.flushOutStream();
		// }
	}

	/**
	 * Reseting your bank.
	 */
	public void resetBank() {
		int tabId = c.getBank().getCurrentBankTab().getTabId();
		for (int i = 0; i < c.getBank().getBankTab().length; i++) {
			if (i == 0)
				continue;
			if (i != c.getBank().getBankTab().length - 1 && c.getBank().getBankTab()[i].size() == 0 && c.getBank().getBankTab()[i + 1].size() > 0) {
				for (BankItem item : c.getBank().getBankTab()[i + 1].getItems()) {
					c.getBank().getBankTab()[i].add(item);
				}
				c.getBank().getBankTab()[i + 1].getItems().clear();
			}
		}
		c.getPA().sendFrame36(700, 0);
		c.getPA().sendFrame34a(58040, -1, 0, 0);
		int newSlot = -1;
		for (int i = 0; i < c.getBank().getBankTab().length; i++) {
			BankTab tab = c.getBank().getBankTab()[i];
			if (i == tabId) {
				c.getPA().sendFrame36(700 + i, 1);
			} else {
				c.getPA().sendFrame36(700 + i, 0);
			}
			if (tab.getTabId() != 0 && tab.size() > 0 && tab.getItem(0) != null) {
				c.getPA().sendFrame171(0, 58050 + i);
				c.getPA().sendFrame34a(58040 + i, c.getBank().getBankTab()[i].getItem(0).getId() - 1, 0, c.getBank().getBankTab()[i].getItem(0).getAmount());
			} else if (i != 0) {
				if (newSlot == -1) {
					newSlot = i;
					c.getPA().sendFrame34a(58040 + i, -1, 0, 0);
					c.getPA().sendFrame171(0, 58050 + i);
					continue;
				}
				c.getPA().sendFrame34a(58040 + i, -1, 0, 0);
				c.getPA().sendFrame171(1, 58050 + i);
			}
		}
		c.getOutStream().createFrameVarSizeWord(53);
		c.getOutStream().writeWord(5382); // bank
		c.getOutStream().writeWord(Config.BANK_SIZE);
		BankTab tab = c.getBank().getCurrentBankTab();
		for (int i = 0; i < Config.BANK_SIZE; i++) {
			if (i > tab.size() - 1) {
				c.getOutStream().writeByte(0);
				c.getOutStream().writeWordBigEndianA(0);
			} else {
				BankItem item = tab.getItem(i);
				if (item == null) {
					item = new BankItem(-1, 0);
				}
				if (item.getAmount() > 254) {
					c.getOutStream().writeByte(255);
					c.getOutStream().writeDWord_v2(item.getAmount());
				} else {
					c.getOutStream().writeByte(item.getAmount());
				}
				if (item.getAmount() < 1)
					item.setAmount(0);
				if (item.getId() > Config.ITEM_LIMIT || item.getId() < 0)
					item.setId(-1); //DOn't seem to be saved at 0
				c.getOutStream().writeWordBigEndianA(item.getId());
			}
		}
		c.getOutStream().endFrameVarSizeWord();
		c.flushOutStream();
		c.getPA().sendFrame126("" + c.getBank().getCurrentBankTab().size(), 58061);
		c.getPA().sendFrame126(Integer.toString(tabId), 5292);
		c.getPA().sendFrame126(Misc.capitalize(c.playerName.toLowerCase()) + "'s Bank.", 58064);
	}

	/**
	 * Resets temporary worn items. Used in minigames, etc
	 */
	public void resetTempItems() {
		// synchronized (c) {
		int itemCount = 0;
		for (int i = 0; i < c.playerItems.length; i++) {
			if (c.playerItems[i] > -1) {
				itemCount = i;
			}
		}
		c.getOutStream().createFrameVarSizeWord(53);
		c.getOutStream().writeWord(5064);
		c.getOutStream().writeWord(itemCount + 1);
		for (int i = 0; i < itemCount + 1; i++) {
			if (c.playerItemsN[i] > 254) {
				c.getOutStream().writeByte(255);
				c.getOutStream().writeDWord_v2(c.playerItemsN[i]);
			} else {
				c.getOutStream().writeByte(c.playerItemsN[i]);
			}
			if (c.playerItems[i] > Config.ITEM_LIMIT || c.playerItems[i] < 0) {
				c.playerItems[i] = Config.ITEM_LIMIT;
			}
			c.getOutStream().writeWordBigEndianA(c.playerItems[i]);
		}
		c.getOutStream().endFrameVarSizeWord();
		c.flushOutStream();
		// }
	}

	/**
	 * Banking your item.
	 * 
	 * @param itemId
	 * @param amount
	 * @return
	 */

	public boolean addToBank(int itemId, int amount, boolean updateView) {
		if (c.getPA().viewingOtherBank) {
			c.getPA().resetOtherBank();
			return false;
		}
		if (c.getTutorial().isActive()) {
			c.getTutorial().refresh();
			return false;
		}
		if (!c.getMode().isBankingPermitted()) {
			c.sendMessage("Your game mode prohibits use of the banking system.");
			return false;
		}
		if (!c.isBanking)
			return false;
		if (c.inSafeBox)
			return false;
		if (!c.getItems().playerHasItem(itemId))
			return false;
		if (c.getBank().getBankSearch().isSearching()) {
			c.getBank().getBankSearch().reset();
			return false;
		}
		if (c.getBankPin().requiresUnlock()) {
			resetBank();
			c.getBankPin().open(2);
			return false;
		}
		BankTab tab = c.getBank().getCurrentBankTab();
		BankItem item = new BankItem(itemId + 1, amount);
		if (Item.itemIsNote[itemId]) {
			item = new BankItem(Server.itemHandler.getCounterpart(itemId) + 1, amount);
		}
		Iterator<BankTab> iterator = Arrays.asList(c.getBank().getBankTab()).iterator();
		outer: while (iterator.hasNext()) {
			BankTab t = iterator.next();
			if (t != null && t.size() > 0) {
				for (BankItem i : t.getItems()) {
					if (i.getId()==item.getId()) {
						if (t.getTabId()!=tab.getTabId()) {
							tab=t;
							break outer;
						}
					}
				}
			}
		}
		if (item.getAmount() > getItemAmount(itemId))
			item.setAmount(getItemAmount(itemId));
		if (tab.getItemAmount(item) == Integer.MAX_VALUE) {
			c.sendMessage("Your bank is already holding the maximum amount of " + getItemName(itemId).toLowerCase() + " possible.");
			return false;
		}
		if (tab.freeSlots() == 0 && !tab.contains(item)) {
			c.sendMessage("Your current bank tab is full.");
			return false;
		}
		long totalAmount = ((long) tab.getItemAmount(item) + (long) item.getAmount());
		if (totalAmount >= Integer.MAX_VALUE) {
			int difference = Integer.MAX_VALUE - tab.getItemAmount(item);
			item.setAmount(difference);
			deleteItem2(itemId, difference);
		} else {
			deleteItem2(itemId, item.getAmount());
		}
		tab.add(item);
		if (updateView) {
			resetTempItems();
			resetBank();
		}
		return true;
	}

	public boolean bankContains(int itemId) {
		for (BankTab tab : c.getBank().getBankTab())
			if (tab.contains(new BankItem(itemId + 1)))
				return true;
		return false;
	}

	public boolean bankContains(int itemId, int itemAmount) {
		for (BankTab tab : c.getBank().getBankTab()) {
			if (tab.containsAmount(new BankItem(itemId + 1, itemAmount))) {
				return true;
			}
		}
		return false;
	}

	public boolean isBankSpaceAvailable(BankItem item) {
		for (BankTab tab : c.getBank().getBankTab()) {
			if (tab.contains(item)) {
				return tab.spaceAvailable(item);
			}
		}
		return false;
	}

	public boolean removeFromAnyTabWithoutAdding(int itemId, int itemAmount, boolean updateView) {
		if (c.getPA().viewingOtherBank) {
			c.getPA().resetOtherBank();
		}
		BankTab tab = null;
		BankItem item = new BankItem(itemId + 1, itemAmount);
		for (BankTab searchedTab : c.getBank().getBankTab()) {
			if (searchedTab.contains(item)) {
				tab = searchedTab;
				break;
			}
		}
		if (tab == null) {
			return false;
		}
		if (freeSlots() == 0 && !playerHasItem(itemId)) {
			c.sendMessage("There is not enough space in your inventory.");
			return false;
		}
		if (itemAmount <= 0)
			return false;
		if (!tab.contains(item))
			return false;
		if (tab.getItemAmount(item) < itemAmount) {
			item.setAmount(tab.getItemAmount(item));
		}
		if (item.getAmount() < 0)
			item.setAmount(0);
			tab.remove(item, 0, c.placeHolders);
		if (tab.size() == 0) {
			c.getBank().setCurrentBankTab(c.getBank().getBankTab(0));
		}
		if (updateView) {
			resetBank();
		}
		c.getItems().resetItems(5064);
		return true;
	}

	public void removeFromBank(int itemId, int itemAmount, boolean updateView) {
		BankTab tab = c.getBank().getCurrentBankTab();
		BankItem item = new BankItem(itemId + 1, itemAmount);
		boolean noted = false;
		// boolean notable = isNoted(itemId + 1);		//does this work to remove items?
		if (!c.isBanking)
			return;
		if (c.getTutorial().isActive()) {
			c.getTutorial().refresh();
			return;
		}
		if (freeSlots() == 0 && !playerHasItem(itemId)) {
			c.sendMessage("Not enough space in your inventory.");
			return;
		}
		if (!c.getMode().isBankingPermitted()) {
			c.sendMessage("Your game mode prohibits use of the banking system.");
			return;
		}
		if (c.getPA().viewingOtherBank) {
			c.getPA().resetOtherBank();
			return;
		}
		
		if (itemAmount <= 0)
			return;
		
		if (c.getBankPin().requiresUnlock()) {
			resetBank();
			c.getBankPin().open(2);
			return;
		}
		if (Server.getMultiplayerSessionListener().inAnySession(c)) {
			c.getPA().closeAllWindows();
			return;
		}
		if (!tab.contains(item))
			return;
		if (c.takeAsNote) {
			if (freeSlots() == 0 && !playerHasItem(itemId + 1)) {
				c.sendMessage("Not enough space in your inventory.");
				return;
			}
			if (!Item.itemIsNote[itemId] && Server.itemHandler.getCounterpart(itemId) > 0) {
				noted = true;
			} else
				c.sendMessage("This item cannot be taken out as noted.");
		}
		if (getItemAmount(itemId) == Integer.MAX_VALUE) {
			c.sendMessage("Your inventory is already holding the maximum amount of " + getItemName(itemId).toLowerCase() + " possible.");
			return;
		}
		boolean stackable = isStackable(item.getId() - 1);
		if (stackable || noted) {
			long totalAmount = (long) getItemAmount(itemId) + (long) itemAmount;
			if (totalAmount > Integer.MAX_VALUE)
				item.setAmount(tab.getItemAmount(item) - getItemAmount(itemId));
		}
		if (tab.getItemAmount(item) < itemAmount) {
			item.setAmount(tab.getItemAmount(item));
		}
		if (!stackable && !noted) {
			if (freeSlots() < item.getAmount())
				item.setAmount(freeSlots());
		}
		
		if (item.getAmount() == 0) {
			if (!c.placeHolderWarning) {
				c.lastPlaceHolderWarning = item.getId();
				c.sendMessage("@cr10@@red@Are you sure you want to release the placeholder of " + Item.getItemName(item.getId() - 1) + "?");
				c.sendMessage("@cr10@@red@If so, click the item once more.");
				c.placeHolderWarning = true;
				return;
			} else {
				if (item.getId() != c.lastPlaceHolderWarning) {
					c.placeHolderWarning = false;
					return;
				}
				c.placeHolderWarning = false;
			}
		} else {
			if (c.placeHolderWarning) {
				c.placeHolderWarning = false;
			}
		}
		
		if (item.getAmount() < 0)
			item.setAmount(0);
		
		if (!noted)
			addItem(item.getId() - 1, item.getAmount());
		else
			addItem(Server.itemHandler.getCounterpart(item.getId() - 1), item.getAmount());
		
		int type = 0;
		if (item.getAmount() <= 0) // if already a placeholder aka amt 0
			type = 1;
		tab.remove(item, type, c.placeHolders);
		
		
		if (tab.size() == 0) {
			c.getBank().setCurrentBankTab(c.getBank().getBankTab(0));
		}
		if (updateView) {
			resetBank();
		}
		c.getItems().resetItems(5064);
	}
	
	public void removeFromBankPlaceholder(int itemId, int itemAmount, boolean updateView) {
		BankTab tab = c.getBank().getCurrentBankTab();
		BankItem item = new BankItem(itemId + 1, itemAmount);
		boolean noted = false;
		// boolean notable = isNoted(itemId + 1);		//does this work to remove items?
		
		if (!c.isBanking)
			return;
		if (c.getTutorial().isActive()) {
			c.getTutorial().refresh();
			return;
		}
		if (freeSlots() == 0 && !playerHasItem(itemId)) {
			c.sendMessage("Not enough space in your inventory.");
			return;
		}
		if (!c.getMode().isBankingPermitted()) {
			c.sendMessage("Your game mode prohibits use of the banking system.");
			return;
		}
		if (c.getPA().viewingOtherBank) {
			c.getPA().resetOtherBank();
			return;
		}
		
		if (itemAmount <= 0)
			return;
		
		if (c.getBankPin().requiresUnlock()) {
			resetBank();
			c.getBankPin().open(2);
			return;
		}
		if (Server.getMultiplayerSessionListener().inAnySession(c)) {
			c.getPA().closeAllWindows();
			return;
		}
		if (!tab.contains(item))
			return;
		if (c.takeAsNote) {
			if (freeSlots() == 0 && !playerHasItem(itemId + 1)) {
				c.sendMessage("Not enough space in your inventory.");
				return;
			}
			if (!Item.itemIsNote[itemId] && Server.itemHandler.getCounterpart(itemId) > 0) {
				noted = true;
			} else
				c.sendMessage("This item cannot be taken out as noted.");
		}
		if (getItemAmount(itemId) == Integer.MAX_VALUE) {
			c.sendMessage("Your inventory is already holding the maximum amount of " + getItemName(itemId).toLowerCase() + " possible.");
			return;
		}
		boolean stackable = isStackable(item.getId() - 1);
		if (stackable || noted) {
			long totalAmount = (long) getItemAmount(itemId) + (long) itemAmount;
			if (totalAmount > Integer.MAX_VALUE)
				item.setAmount(tab.getItemAmount(item) - getItemAmount(itemId));
		}
		if (tab.getItemAmount(item) < itemAmount) {
			item.setAmount(tab.getItemAmount(item));
		}
		if (!stackable && !noted) {
			if (freeSlots() < item.getAmount())
				item.setAmount(freeSlots());
		}
		
		if (item.getAmount() < 0)
			item.setAmount(0);
		
		if (!noted)
			addItem(item.getId() - 1, item.getAmount());
		else
			addItem(Server.itemHandler.getCounterpart(item.getId() - 1), item.getAmount());
		
		int type = 0;
		if (item.getAmount() <= 0) // if already a placeholder aka amt 0
			type = 1;
		tab.remove(item, type, true);		
		
		if (tab.size() == 0) {
			c.getBank().setCurrentBankTab(c.getBank().getBankTab(0));
		}
		if (updateView) {
			resetBank();
		}
		c.getItems().resetItems(5064);
	}

	public boolean addEquipmentToBank(int itemId, int slot, int amount, boolean updateView) {
		if (c.getPA().viewingOtherBank) {
			c.getPA().resetOtherBank();
			return false;
		}
		if (!c.isBanking)
			return false;
		if (c.inSafeBox)
			return false;
		if (c.playerEquipment[slot] != itemId || c.playerEquipmentN[slot] <= 0)
			return false;
		BankTab tab = c.getBank().getCurrentBankTab();
		BankItem item = new BankItem(itemId + 1, amount);
		if (Item.itemIsNote[itemId]) {
			item = new BankItem(Server.itemHandler.getCounterpart(itemId) + 1, amount);
		}
		Iterator<BankTab> iterator = Arrays.asList(c.getBank().getBankTab()).iterator();
		outer: while (iterator.hasNext()) {
			BankTab t = iterator.next();
			if (t != null && t.size() > 0) {
				for (BankItem i : t.getItems()) {
					if (i.getId()==item.getId()) {
						if (t.getTabId()!=tab.getTabId()) {
							tab=t;
							break outer;
						}
					}
				}
			}
		}

		if (item.getAmount() > c.playerEquipmentN[slot])
			item.setAmount(c.playerEquipmentN[slot]);
		if (tab.getItemAmount(item) == Integer.MAX_VALUE) {
			c.sendMessage("Your bank is already holding the maximum amount of " + getItemName(itemId).toLowerCase() + " possible.");
			return false;
		}
		if (tab.freeSlots() == 0 && !tab.contains(item)) {
			c.sendMessage("Your current bank tab is full.");
			return false;
		}
		long totalAmount = ((long) tab.getItemAmount(item) + (long) item.getAmount());
		if (totalAmount >= Integer.MAX_VALUE) {
			c.sendMessage("Your bank is already holding the maximum amount of this item.");
			return false;
		} else
			c.playerEquipmentN[slot] -= item.getAmount();
		if (c.playerEquipmentN[slot] <= 0) {
			c.playerEquipmentN[slot] = -1;
			c.playerEquipment[slot] = -1;
		}
		tab.add(item);
		if (updateView) {
			resetTempItems();
			resetBank();
			updateSlot(slot);
		}
		return true;
	}

	/**
	 * Checking item amounts.
	 * 
	 * @param itemID
	 * @return
	 */
	public int itemAmount(int itemID) {
		int tempAmount = 0;
		for (int i = 0; i < c.playerItems.length; i++) {
			if (c.playerItems[i] == itemID) {
				tempAmount += c.playerItemsN[i];
			}
		}
		return tempAmount;
	}

	/**
	 * Checks if the item is stackable.
	 * 
	 * @param itemID
	 * @return
	 */
	public boolean isStackable(int itemID) {
		return Item.itemStackable[itemID];
	}

	/**
	 * Updates the equipment tab.
	 **/
	public void setEquipment(int wearID, int amount, int targetSlot) {
		c.isFullHelm = Item.isFullHat(c.playerEquipment[c.playerHat]);
		c.isFullMask = Item.isFullMask(c.playerEquipment[c.playerHat]);
		c.isFullBody = Item.isFullBody(c.playerEquipment[c.playerChest]);
		// synchronized (c) {
		c.getOutStream().createFrameVarSizeWord(34);
		c.getOutStream().writeWord(1688);
		c.getOutStream().writeByte(targetSlot);
		c.getOutStream().writeWord(wearID + 1);
		if (amount > 254) {
			c.getOutStream().writeByte(255);
			c.getOutStream().writeDWord(amount);
		} else {
			c.getOutStream().writeByte(amount);
		}
		c.getOutStream().endFrameVarSizeWord();
		c.flushOutStream();
		c.playerEquipment[targetSlot] = wearID;
		c.playerEquipmentN[targetSlot] = amount;
		c.updateRequired = true;
		c.setAppearanceUpdateRequired(true);
		// }
	}

	public void swapBankItem(int from, int to) {
		BankItem item = c.getBank().getCurrentBankTab().getItem(from);
		c.getBank().getCurrentBankTab().setItem(from, c.getBank().getCurrentBankTab().getItem(to));
		c.getBank().getCurrentBankTab().setItem(to, item);
	}

	public void moveItems(int from, int to, int moveWindow, boolean insertMode) {
		if (moveWindow == 3214) {
			int tempI;
			int tempN;
			tempI = c.playerItems[from];
			tempN = c.playerItemsN[from];
			c.playerItems[from] = c.playerItems[to];
			c.playerItemsN[from] = c.playerItemsN[to];
			c.playerItems[to] = tempI;
			c.playerItemsN[to] = tempN;
		}
		if (moveWindow == 5382) {
			if (!c.isBanking) {
				c.getPA().removeAllWindows();
				resetBank();
				return;
			}
			if (c.getPA().viewingOtherBank) {
				c.getPA().resetOtherBank();
				return;
			}
			if (c.getBank().getBankSearch().isSearching()) {
				c.getBank().getBankSearch().reset();
				return;
			}
			if (c.getBankPin().requiresUnlock()) {
				resetBank();
				c.isBanking = false;
				c.inSafeBox = false;
				c.getBankPin().open(2);
				return;
			}
			if (to > 999) {
				int tabId = to - 1000;
				if (tabId < 0)
					tabId = 0;
				if (tabId == c.getBank().getCurrentBankTab().getTabId()) {
					c.sendMessage("You cannot add an item from it's tab to the same tab.");
					resetBank();
					return;
				}
				if (from > c.getBank().getCurrentBankTab().size()) {
					resetBank();
					return;
				}
				BankItem item = c.getBank().getCurrentBankTab().getItem(from);
				if (item == null) {
					resetBank();
					return;
				}
				c.getBank().getCurrentBankTab().remove(item, 0, false);
				c.getBank().getBankTab()[tabId].add(item);
			} else {
				if (from > c.getBank().getCurrentBankTab().size() - 1 || to > c.getBank().getCurrentBankTab().size() - 1) {
					resetBank();
					return;
				}
				if (!insertMode) {
					BankItem item = c.getBank().getCurrentBankTab().getItem(from);
					if (item == null) {
						resetBank();
						return;
					}
					c.getBank().getCurrentBankTab().setItem(from, c.getBank().getCurrentBankTab().getItem(to));
					c.getBank().getCurrentBankTab().setItem(to, item);
				} else {
					int tempFrom = from;
					for (int tempTo = to; tempFrom != tempTo;)
						if (tempFrom > tempTo) {
							swapBankItem(tempFrom, tempFrom - 1);
							tempFrom--;
						} else if (tempFrom < tempTo) {
							swapBankItem(tempFrom, tempFrom + 1);
							tempFrom++;
						}
				}
			}
		}
		if (moveWindow == 5382) {
			resetBank();
		}
		if (moveWindow == 5064) {
			int tempI;
			int tempN;
			tempI = c.playerItems[from];
			tempN = c.playerItemsN[from];

			c.playerItems[from] = c.playerItems[to];
			c.playerItemsN[from] = c.playerItemsN[to];
			c.playerItems[to] = tempI;
			c.playerItemsN[to] = tempN;
			resetItems(3214);
		}
		resetTempItems();
		if (moveWindow == 3214) {
			resetItems(3214);
		}

	}

	/**
	 * Delete item equipment.
	 **/
	public void deleteEquipment(int i, int j) {
		// synchronized (c) {
		if (PlayerHandler.players[c.getIndex()] == null) {
			return;
		}
		if (i < 0) {
			return;
		}

		c.playerEquipment[j] = -1;
		c.playerEquipmentN[j] = c.playerEquipmentN[j] - 1;
		c.getOutStream().createFrame(34);
		c.getOutStream().writeWord(6);
		c.getOutStream().writeWord(1688);
		c.getOutStream().writeByte(j);
		c.getOutStream().writeWord(0);
		c.getOutStream().writeByte(0);
		getBonus();
		if (j == c.playerWeapon) {
			sendWeapon(-1, "Unarmed");
		}
		resetBonus();
		getBonus();
		writeBonus();
		c.updateRequired = true;
		c.setAppearanceUpdateRequired(true);
		// }
	}

	/**
	 * Delete items.
	 * 
	 * @param id
	 * @param amount
	 */
	public void deleteItem(int id, int amount) {
		deleteItem(id, getItemSlot(id), amount);
	}

	public void deleteItem(int id, int slot, int amount) {
		if (id <= 0 || slot < 0) {
			return;
		}
		if (c.playerItems[slot] == (id + 1)) {
			if (c.playerItemsN[slot] > amount) {
				c.playerItemsN[slot] -= amount;
			} else {
				c.playerItemsN[slot] = 0;
				c.playerItems[slot] = 0;
			}
			PlayerSave.saveGame(c);
			resetItems(3214);
		}
	}

	public List<Integer> returnAmount(int id, int amount) {
		if (id <= 0) {
			return null;
		}
		List<Integer> amountsToReturn = new ArrayList<>();
		for (int j = 0; j < c.playerItems.length; j++) {
			if (c.playerItems[j] == (id + 1)) {
				if (c.playerItemsN[j] > amount) {
					amountsToReturn.add(amount);
				} else {
					amountsToReturn.add(c.playerItemsN[j]);
				}
			}
		}
		return amountsToReturn;
	}

	public List<Integer> deleteItemAndReturnAmount(int id, int amount) {
		if (id <= 0) {
			return null;
		}
		int count = 0;
		List<Integer> amountsToReturn = new ArrayList<>();
		for (int j = 0; j < c.playerItems.length; j++) {
			if (count >= amount) {
				break;
			}
			if (c.playerItems[j] == (id + 1)) {
				if (c.playerItemsN[j] > amount) {
					c.playerItemsN[j] -= amount;
					count += amount;
					amountsToReturn.add(amount);
				} else {
					count += c.playerItemsN[j];
					amountsToReturn.add(c.playerItemsN[j]);
					c.playerItemsN[j] = 0;
					c.playerItems[j] = 0;
				}
			}
		}
		resetItems(3214);
		PlayerSave.saveGame(c);

		return amountsToReturn;
	}
	/*public int deleteItemAndReturnAmount(int id, int amount) {
		if (id <= 0) {
			return -1;
		}
		int amountToReturn = 0;
		for (int j = 0; j < c.playerItems.length; j++) {
			
			if (c.playerItems[j] == id + 1) {
				if (c.playerItemsN[j] > amount) {
					c.playerItemsN[j] -= amount;
					amountToReturn = amount;
				} else {
					amountToReturn = c.playerItemsN[j];
					c.playerItemsN[j] = 0;
					c.playerItems[j] = 0;
				}
				break;
			}
		}
		resetItems(3214);
		PlayerSave.saveGame(c);
		return amountToReturn;
	}*/
	public int deleteItemAndReturnAmount(int id) {
		if (id <= 0) {
			return -1;
		}
		int amount = 0;
		for (int j = 0; j < c.playerItems.length; j++) {
			if (c.playerItems[j] == id + 1) {
				c.playerItems[j] = 0;
				amount = c.playerItemsN[j];
				c.playerItemsN[j] = 0;
				break;
			}
		}
		resetItems(3214);
		PlayerSave.saveGame(c);
		return amount;
	}


	public void deleteItemNoSave(int id, int slot, int amount) {
		if (id <= 0 || slot < 0) {
			return;
		}
		if (c.playerItems[slot] == (id + 1)) {
			if (c.playerItemsN[slot] > amount) {
				c.playerItemsN[slot] -= amount;
			} else {
				c.playerItemsN[slot] = 0;
				c.playerItems[slot] = 0;
			}
			resetItems(3214);
		}
	}

	public void deleteItem2(int id, int amount) {
		int am = amount;
		for (int slot = 0; slot < c.playerItems.length; slot++) {
			if (am == 0) {
				break;
			}
			if (c.playerItems[slot] == (id + 1)) {
				if (c.playerItemsN[slot] > amount) {
					c.playerItemsN[slot] -= amount;
					break;
				} else {
					c.playerItems[slot] = 0;
					c.playerItemsN[slot] = 0;
					am--;
				}
			}
		}
		resetItems(3214);
	}

	/**
	 * Delete arrows.
	 **/
	public void deleteArrow() {
		if (c.getItems().isWearingItem(10499, c.playerCape) || c.getItems().isWearingItem(22109, c.playerCape) ||SkillcapePerks.RANGING.isWearing(c) || SkillcapePerks.isWearingMaxCape(c) && !c.getItems().isWearingItem(4734) &&
				!c.getItems().isWearingItem(10033) && !c.getItems().isWearingItem(10034) && !c.getItems().isWearingItem(11959)) {
			if (RandomUtils.nextInt(0, 15) > 1) {
				return;
			}
		}
		int arrow = c.playerEquipment[c.playerArrows];
		int stock = c.playerEquipmentN[c.playerArrows];
		int slot = c.playerArrows;

		if (stock > 1) {
			c.getItems().wearItem(arrow, stock - 1, slot);
		} else if (stock == 1) {
			c.getItems().wearItem(-1, 0, slot);
		}
	}

	public void removeRangedWeaponry() {
		if (c.getItems().isWearingItem(10499, c.playerCape) || SkillcapePerks.RANGING.isWearing(c) || SkillcapePerks.isWearingMaxCape(c) && !c.getItems().isWearingItem(4734) && 
				!c.getItems().isWearingItem(10033) && !c.getItems().isWearingItem(10034) && !c.getItems().isWearingItem(11959)) {
			if (RandomUtils.nextInt(0, 15) > 1) {
				return;
			}
		}
		int wep = c.playerEquipment[c.playerWeapon];

		if (wep > 1) {
			c.getItems().wearItem(wep, wep - 1, wep);
		} else if (wep == 1) {
			c.getItems().wearItem(-1, 0, wep);
		}
	}

	public void deleteEquipment() {
		if (!c.getItems().isWearingItem(10033) && !c.getItems().isWearingItem(10034)
				&& !c.getItems().isWearingItem(11959)) {
			if (c.getItems().isWearingItem(10499, c.playerCape) || c.getItems().isWearingItem(22109, c.playerCape)|| SkillcapePerks.RANGING.isWearing(c)
					|| SkillcapePerks.isWearingMaxCape(c) && !c.getItems().isWearingItem(4734)) {
				if (RandomUtils.nextInt(0, 15) > 1) {
					return;
				}
			}
		}

		int weapon = c.playerEquipment[c.playerWeapon];
		int stock = c.playerEquipmentN[c.playerWeapon];
		int slot = c.playerWeapon;

		if (stock > 1) {
			c.getItems().wearItem(weapon, stock - 1, slot);
		} else if (stock == 1) {
			c.getItems().wearItem(-1, 0, slot);
		}
	}

	/**
	 * Dropping arrows
	 **/
	public void dropArrowNpc(NPC npc) {
		if (c.playerEquipment[c.playerCape] == 10499 || c.getItems().isWearingItem(22109, c.playerCape) ||SkillcapePerks.RANGING.isWearing(c) || SkillcapePerks.isWearingMaxCape(c)) {
			return;
		}
		int enemyX = npc.getX();
		int enemyY = npc.getY();
		int height = npc.heightLevel;
		if (Misc.random(10) >= 4) {
			if (Server.itemHandler.itemAmount(c.playerName, c.rangeItemUsed, enemyX, enemyY, height) == 0) {
				Server.itemHandler.createGroundItem(c, c.rangeItemUsed, enemyX, enemyY, height, 1, c.getIndex());
			} else if (Server.itemHandler.itemAmount(c.playerName, c.rangeItemUsed, enemyX, enemyY, height) != 0) {
				int amount = Server.itemHandler.itemAmount(c.playerName, c.rangeItemUsed, enemyX, enemyY, height);
				Server.itemHandler.removeGroundItem(c, c.rangeItemUsed, enemyX, enemyY, height, false);
				Server.itemHandler.createGroundItem(c, c.rangeItemUsed, enemyX, enemyY, height, amount + 1, c.getIndex());
			}
		}
	}

	/**
	 * Ranging arrows.
	 */
	public void dropArrowPlayer() {
		int enemyX = PlayerHandler.players[c.oldPlayerIndex].getX();
		int enemyY = PlayerHandler.players[c.oldPlayerIndex].getY();
		int height = PlayerHandler.players[c.oldPlayerIndex].heightLevel;
		if (c.playerEquipment[c.playerCape] == 10499 || c.getItems().isWearingItem(22109, c.playerCape) ||SkillcapePerks.RANGING.isWearing(c) || SkillcapePerks.isWearingMaxCape(c))
			return;
		if (Misc.random(10) >= 4) {
			if (Server.itemHandler.itemAmount(c.playerName, c.rangeItemUsed, enemyX, enemyY, height) == 0) {
				Server.itemHandler.createGroundItem(c, c.rangeItemUsed, enemyX, enemyY, height, 1, c.getIndex());
			} else if (Server.itemHandler.itemAmount(c.playerName, c.rangeItemUsed, enemyX, enemyY, height) != 0) {
				int amount = Server.itemHandler.itemAmount(c.playerName, c.rangeItemUsed, enemyX, enemyY, height);
				Server.itemHandler.removeGroundItem(c, c.rangeItemUsed, enemyX, enemyY, height, false);
				Server.itemHandler.createGroundItem(c, c.rangeItemUsed, enemyX, enemyY, height, amount + 1, c.getIndex());
			}
		}
	}

	/**
	 * Removes all items from player's equipment.
	 */
	public void removeAllItems() {
		for (int i = 0; i < c.playerItems.length; i++) {
			c.playerItems[i] = 0;
		}
		for (int i = 0; i < c.playerItemsN.length; i++) {
			c.playerItemsN[i] = 0;
		}
		resetItems(3214);
	}

	/**
	 * Checks if you have a free slot.
	 * 
	 * @return
	 */
	public int freeSlots() {
		int freeS = 0;
		for (int i = 0; i < c.playerItems.length; i++) {
			if (c.playerItems[i] <= 0) {
				freeS++;
			}
		}
		return freeS;
	}

	public int freeEquipmentSlots() {
		int slots = 0;
		for (int i = 0; i < c.playerEquipment.length; i++) {
			if (c.playerEquipment[i] <= 0) {
				slots++;
			}
		}
		return slots;
	}

	public boolean isWearingItems() {
		return freeEquipmentSlots() < 14;
	}

	/**
	 * Finds the item.
	 * 
	 * @param id
	 * @param items
	 * @param amounts
	 * @return
	 */
	public int findItem(int id, int[] items, int[] amounts) {
		for (int i = 0; i < c.playerItems.length; i++) {
			if (((items[i] - 1) == id) && (amounts[i] > 0)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Gets the item name from the item.cfg
	 * 
	 * @return
	 */
	public static String getItemName(int itemId) {
		if (itemId < 0) {
			return "Unarmed";
		}

		ItemList itemList = Server.itemHandler.ItemList[itemId];
		if (itemList == null) {
			return "Unarmed";
		}
		return itemList.itemName == null ? "Unarmed" : itemList.itemName;
	}

	/**
	 * Gets the item ID from the item.cfg
	 * 
	 * @param itemName
	 * @return
	 */
	public int getItemId(String itemName) {
		for (int i = 0; i < Config.ITEM_LIMIT; i++) {
			if (Server.itemHandler.ItemList[i] != null) {
				if (Server.itemHandler.ItemList[i].itemName.equalsIgnoreCase(itemName)) {
					return Server.itemHandler.ItemList[i].itemId;
				}
			}
		}
		return -1;
	}

	/**
	 * Gets the item slot.
	 * 
	 * @param ItemID
	 * @return
	 */
	public int getItemSlot(int ItemID) {
		for (int i = 0; i < c.playerItems.length; i++) {
			if ((c.playerItems[i] - 1) == ItemID) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Gets the item amount.
	 * 
	 * @param ItemID
	 * @return
	 */
	public int getItemAmount(int ItemID) {
		int itemCount = 0;
		for (int i = 0; i < c.playerItems.length; i++) {
			if ((c.playerItems[i] - 1) == ItemID) {
				itemCount += c.playerItemsN[i];
			}
		}
		return itemCount;
	}

	/**
	 * Counts (a) player's items.
	 * 
	 * Deprecated. Use {@link #getItemCount(int, boolean)} instead.
	 * 
	 * @param itemID
	 * @return count start
	 * 
	 */
	public int getItemCount(int itemID) {
		int count = 0;
		for (int j = 0; j < c.playerItems.length; j++) {
			if (c.playerItems[j] == itemID + 1) {
				count += c.playerItemsN[j];
			}
		}
		return count;
	}
	
	/**
	 * Checks if the player has the item.
	 * 
	 * @param itemID
	 * @param amt
	 * @param slot
	 * @return
	 */
	public boolean playerHasItem(int itemID, int amt, int slot) {
		itemID++;
		int found = 0;
		if (c.playerItems[slot] == (itemID)) {
			for (int i = 0; i < c.playerItems.length; i++) {
				if (c.playerItems[i] == itemID) {
					if (c.playerItemsN[i] >= amt) {
						return true;
					} else {
						found++;
					}
				}
			}
			return found>=amt;
		}
		return false;
	}

	public boolean playerHasItem(int itemID) {
		itemID++;
		for (int i = 0; i < c.playerItems.length; i++) {
			if (c.playerItems[i] == itemID)
				return true;
		}
		return false;
	}

	public boolean playerHasAllItems(int[] items) {

		for (int item : items) {
			if (!playerHasItem(item))
				return false;
		}
		return true;
	}

	public boolean playerHasItem(int itemID, int amt) {
		itemID++;
		int found = 0;
		for (int i = 0; i < c.playerItems.length; i++) {
			if (c.playerItems[i] == itemID) {
				if (c.playerItemsN[i] >= amt) {
					return true;
				} else {
					found++;
				}
			}
		}
		return found>=amt;
	}
	
	
	/**
	 * Check if account contains the given ID
	 * @param itemId	id checked for
	 * @return			true if containing the id, else false
	 */
	public boolean accountContains(int itemId, int amount) {
		playerHasItem(itemId, amount);
		bankContains(itemId, amount);
		
		return false;
	}

	/**
	 * Getting un-noted items.
	 * 
	 * @param ItemID
	 * @return
	 */
	public int getUnnotedItem(int ItemID) {
		int NewID = ItemID - 1;
		String NotedName = "";
		for (int i = 0; i < Config.ITEM_LIMIT; i++) {
			if (Server.itemHandler.ItemList[i] != null) {
				if (Server.itemHandler.ItemList[i].itemId == ItemID) {
					NotedName = Server.itemHandler.ItemList[i].itemName;
				}
			}
		}
		for (int i = 0; i < Config.ITEM_LIMIT; i++) {
			if (Server.itemHandler.ItemList[i] != null) {
				if (Objects.equals(Server.itemHandler.ItemList[i].itemName, NotedName)) {
					if (!Server.itemHandler.ItemList[i].itemDescription.startsWith("Swap this note at any bank for a")) {
						NewID = Server.itemHandler.ItemList[i].itemId;
						break;
					}
				}
			}
		}
		return NewID;
	}

	/**
	 * Dropping items
	 **/
	public void createGroundItem(int itemID, int itemX, int itemY, int itemAmount) {
		if (c.getOutStream() != null) {
			c.getOutStream().createFrame(85);
			c.getOutStream().writeByteC((itemY - 8 * c.mapRegionY));
			c.getOutStream().writeByteC((itemX - 8 * c.mapRegionX));
			c.getOutStream().createFrame(44);
			c.getOutStream().writeWordBigEndianA(itemID);
			c.getOutStream().writeDWord_v1(itemAmount);
			c.getOutStream().writeByte(0);
			c.flushOutStream();
		}
	}

	/**
	 * Pickup items from the ground.
	 **/
	public void removeGroundItem(int itemID, int itemX, int itemY, int Amount) {
		// synchronized (c) {
		c.getOutStream().createFrame(85);
		c.getOutStream().writeByteC((itemY - 8 * c.mapRegionY));
		c.getOutStream().writeByteC((itemX - 8 * c.mapRegionX));
		c.getOutStream().createFrame(156);
		c.getOutStream().writeByteS(0);
		c.getOutStream().writeWord(itemID);
		c.flushOutStream();
		// }
	}

	public void createGroundItem(GroundItem item) {
		c.getOutStream().createFrame(85);
		c.getOutStream().writeByteC((item.getY() - 8 * c.mapRegionY));
		c.getOutStream().writeByteC((item.getX() - 8 * c.mapRegionX));
		c.getOutStream().createFrame(44);
		c.getOutStream().writeWordBigEndianA(item.getId());
		c.getOutStream().writeDWord_v1(item.getAmount());
		c.getOutStream().writeByte(0);
		c.flushOutStream();
	}

	public void removeGroundItem(GroundItem item) {
		c.getOutStream().createFrame(85);
		c.getOutStream().writeByteC((item.getY() - 8 * c.mapRegionY));
		c.getOutStream().writeByteC((item.getX() - 8 * c.mapRegionX));
		c.getOutStream().createFrame(156);
		c.getOutStream().writeByteS(0);
		c.getOutStream().writeWord(item.getId());
		c.flushOutStream();
	}

	/**
	 * Checks if a player owns a cape.
	 * 
	 * @return
	 */
	public boolean ownsCape() {
		if (c.getItems().playerHasItem(2412, 1) || c.getItems().playerHasItem(2413, 1) || c.getItems().playerHasItem(2414, 1))
			return true;
		for (int j = 0; j < Config.BANK_SIZE; j++) {
			if (c.bankItems[j] == 2412 || c.bankItems[j] == 2413 || c.bankItems[j] == 2414)
				return true;
		}
		return c.playerEquipment[c.playerCape]==2413||c.playerEquipment[c.playerCape]==2414||c.playerEquipment[c.playerCape]==2415;
	}
	
	/**
	 * Handles Amethyst crafting
	 */
	public void handleAmethyst() {
		if (!c.boltTips&&!c.arrowTips&&!c.javelinHeads) {
			c.sendMessage("@pur@Right click your Amethyst to set its crafting method.");
		} else if (c.boltTips) {
			if (c.playerLevel[Skill.CRAFTING.getId()] < 83) {
				c.sendMessage("You need 83 crafting to do this.");
				return;
			}
			c.getItems().deleteItem(21347, 1);
			c.getItems().addItem(21338, 15);
			c.getPA().addSkillXP((c.getMode().getType().equals(ModeType.OSRS) ? 60 : 220), 12, true);
			c.sendMessage("You make some amethyst bolt tips.");
		} else if (c.arrowTips) {
			if (c.playerLevel[Skill.CRAFTING.getId()] < 85) {
				c.sendMessage("You need 85 crafting to do this.");
				return;
			}
			c.getItems().deleteItem(21347, 1);
			c.getItems().addItem(21350, 15);
			c.getPA().addSkillXP((c.getMode().getType().equals(ModeType.OSRS) ? 60 : 220), 12, true);
			c.sendMessage("You make some amethyst arrowtips.");
		} else if (c.javelinHeads) {
			if (c.playerLevel[Skill.CRAFTING.getId()] < 87) {
				c.sendMessage("You need 87 crafting to do this.");
				return;
			}
			c.getItems().deleteItem(21347, 1);
			c.getItems().addItem(21352, 5);
			c.getPA().addSkillXP((c.getMode().getType().equals(ModeType.OSRS) ? 60 : 220), 12, true);
			c.sendMessage("You make some amethyst javelin heads.");
		} else {
			c.sendMessage("Error. Please report.");
		}
	}
	
	/**
	 * Checks if the player has all the Kodai wand pieces.
	 * 
	 * @return
	 */
	public boolean hasAllKodai() {
		return playerHasItem(21043, 1) && playerHasItem(6914, 1);
	}
	
	/**
	 * Checks if the player has all the shards.
	 * 
	 * @return
	 */
	public boolean hasAllShards() {
		return playerHasItem(11818, 1) && playerHasItem(11820, 1) && playerHasItem(11822, 1);
	}
	
	public boolean hasAllPieces() {
		return playerHasItem(19679, 1) && playerHasItem(19681, 1) && playerHasItem(19683, 1);
	}
	/**
	 * Makes the Kodai wand.
	 */
	public void makeKodai() {
		deleteItem(21043, 1);
		deleteItem(6914, 1);
		addItem(21006, 1);
		c.getDH().sendStatement("You combine the insignia and wand to make a Kodai wand.");
	}
	/**
	 * Makes the godsword blade.
	 */
	public void makeBlade() {
		deleteItem(11818, 1);
		deleteItem(11820, 1);
		deleteItem(11822, 1);
		addItem(11798, 1);
		c.getDH().sendStatement("You combine the shards to make a godsword blade.");
	}

	public void makeTotem() {
		deleteItem(19679, 1);
		deleteItem(19681, 1);
		deleteItem(19683, 1);
		addItem(19685, 1);
		c.getDH().sendStatement("You combine the pieces to make a dark totem.");
	}

	/**
	 * Makes the godsword.
	 *
	 * @param i
	 */

	public int getTotalRiskedWorth() {
		int worth = getTotalWorth();
		resetKeepItems();
		if (!c.isSkulled) {
			for (int i = 0; i < 3; i++) {
				keepItem(i, false);
			}
		}
		if (c.prayerActive[10] && System.currentTimeMillis() - c.lastProtItem > 700) {
			keepItem(3, false);
		}
		for (int i = 0; i < c.itemKeptId.length; i++) {
			worth -= ShopAssistant.getItemShopValue(c.itemKeptId[i]);
		}
		return worth;
	}

	public int getTotalWorth() {
		int worth = 0;
		for (int inventorySlot = 0; inventorySlot < c.playerItems.length; inventorySlot++) {
			int inventoryId = c.playerItems[inventorySlot] - 1;
			int inventoryAmount = c.playerItemsN[inventorySlot];
			int price = ShopAssistant.getItemShopValue(inventoryId);
			if (inventoryId == 996)
				price = 1;
			if (inventoryId > 0 && inventoryAmount > 0) {
				worth += (price * inventoryAmount);
			}
		}
		for (int equipmentSlot = 0; equipmentSlot < c.playerEquipment.length; equipmentSlot++) {
			int equipmentId = c.playerEquipment[equipmentSlot];
			int equipmentAmount = c.playerEquipmentN[equipmentSlot];
			int price = ShopAssistant.getItemShopValue(equipmentId);
			if (equipmentId > 0 && equipmentAmount > 0) {
				worth += (price * equipmentAmount);
			}
		}
		return worth;
	}

	/**
	 * Checks if the item is a godsword hilt.
	 * 
	 * @param i
	 * @return
	 */
	public boolean isHilt(int i) {
		return i >= 11810 && i <= 11816 && i % 2 == 0;
	}

}
