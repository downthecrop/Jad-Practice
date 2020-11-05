package ethos.model.content.presets;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import org.apache.commons.lang3.text.WordUtils;

import ethos.Server;
import ethos.model.items.GameItem;
import ethos.model.items.ItemDefinition;
import ethos.model.players.Boundary;
import ethos.model.players.Player;

/**
 * 
 * @author Jason MacKeigan
 * @date Dec 30, 2014, 1:38:10 AM
 */
public class PresetContainer {

	/**
	 * The type of container
	 */
	private PresetType type;

	/**
	 * The size of the container
	 */
	private int size;

	/**
	 * The default value of the slot on the container
	 */
	private int defaultComponentSlot;

	/**
	 * Contains a mapping of the items in the container. The key of the map is the slot, where as the value is the item.
	 */
	private Map<Integer, GameItem> items = new HashMap<>(size);

	/**
	 * The time of the last load, or output of items
	 */
	private long lastOutput;

	/**
	 * Constructs a new container with a definitive size
	 * 
	 * @param type the type of preset
	 * @param size the size of the container
	 * @param defaultComponentSlot the default slot on the interface items appear
	 */
	public PresetContainer(PresetType type, int size, int defaultComponentSlot) {
		this.type = type;
		this.size = size;
		this.defaultComponentSlot = defaultComponentSlot;
	}

	/**
	 * Adds a new item to the container which will replace an existing item if one exists in the slot
	 * 
	 * @param slot the slot of the item
	 * @param item the item to go in the slot
	 */
	public void add(Player player, int slot, GameItem item) {
		if (slot > size) {
			throw new IllegalStateException("Range of slot exceeds size of container.");
		}
		ItemDefinition itemDef = ItemDefinition.forId(item.getId());
		if (itemDef == null) {
			return;
		}
		int id = item.getId();
		int amount = item.getAmount();
		if (type.isEquipment()) {
			int equipmentSlot = itemDef.getSlot();
			if (!itemDef.isWearable()) {
				player.sendMessage("This item is not wearable.");
			}
			if (amount > 1) {
				if (equipmentSlot != 13 && equipmentSlot != 3 || !itemDef.isStackable()) {
					amount = 1;
				}
			}
			if (equipmentSlot == 5 || equipmentSlot == 3) {
				int shieldSlot = items.containsKey(5) ? items.get(5).getId() : -1;
				int weaponSlot = items.containsKey(3) ? items.get(3).getId() : -1;
				if (shieldSlot != -1 && equipmentSlot == 3) {
					if (itemDef.isTwoHanded()) {
						player.sendMessage("You cannot add a 2 handed weapon whilst wearing a shield.");
						return;
					}
				}
				if (weaponSlot != -1 && equipmentSlot == 5) {
					ItemDefinition i = ItemDefinition.forId(weaponSlot);
					if (i == null) {
						return;
					}
					if (i.isTwoHanded()) {
						player.sendMessage("You cannot add a shield whilst wearing a 2 handed weapon.");
						return;
					}
				}
			}
		}
		if (id < 0) {
			return;
		}
		if (amount < 1) {
			amount = 1;
		}
		boolean containsItem = items.values().stream().anyMatch(i -> i.getId() == id);
		if (item.isStackable() && containsItem) {
			Iterator<Entry<Integer, GameItem>> iterator = items.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<Integer, GameItem> entry = iterator.next();
				int currentSlot = entry.getKey();
				GameItem currentItem = entry.getValue();
				if (currentItem.getId() == id) {
					long total = ((long) currentItem.getAmount() + (long) amount);
					if (total > Integer.MAX_VALUE) {
						iterator.remove();
						entry.setValue(new GameItem(id, Integer.MAX_VALUE));
					} else {
						entry.setValue(new GameItem(id, currentItem.getAmount() + amount));
					}
					slot = currentSlot;
					break;
				}
			}
		} else if (item.isStackable() && !containsItem || type.isEquipment()) {
			items.put(slot, new GameItem(id, amount));
		} else {
			if (items.containsKey(slot)) {
				items.remove(slot);
			}
			if (amount > availableSlots()) {
				amount = availableSlots();
			}
			int initialSlot = slot;
			while (amount-- > 0) {
				if (items.containsKey(initialSlot)) {
					slot = selectOpenSlot();
				}
				if (slot == -1) {
					break;
				}
				items.put(slot, new GameItem(item.getId(), 1));
				player.getPA().sendFrame34a(defaultComponentSlot + slot, item.getId(), 0, 1);
			}
			return;
		}
		player.getPA().sendFrame34a(defaultComponentSlot + slot, item.getId(), 0, items.get(slot).getAmount());
	}

	/**
	 * The amount of available open slots is equivillent to the maximum size subtracted by the space occupied by existing items
	 * 
	 * @return the amount of slots available
	 */
	private int availableSlots() {
		return size - items.size();
	}

	/**
	 * Searches for a single open slot in the container.
	 * 
	 * @return If no slots are available -1 will be returned, otherwise the first found slot will be returned. The slot value cannot exceed the value of the predetermined size.
	 */
	private int selectOpenSlot() {
		for (int i = 0; i < size; i++) {
			if (!items.containsKey(i)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Draws each item on the interface in each appropriate slot
	 * 
	 * @param player the player we're drawing this for
	 */
	public void draw(Player player) {
		for (int i = 0; i < size; i++) {
			player.getPA().sendFrame34a(defaultComponentSlot + i, -1, 0, 0);
		}
		if (items.size() <= 0) {
			return;
		}
		items.forEach((slot, item) -> player.getPA().sendFrame34a(defaultComponentSlot + slot, item.getId(), 0, item.getAmount()));
	}

	/**
	 * Removes an item from the container at a certain slot, if it exists
	 * 
	 * @param slot the slot of the item
	 */
	public void remove(Player player, int slot) {
		if (slot < 0 || slot > size) {
			throw new IllegalStateException("Negative or exceeding range value when removing.");
		}
		if (!items.containsKey(slot)) {
			player.sendMessage("No item exists in this slot.");
			return;
		}
		items.remove(slot);
		player.getPA().sendFrame34a(defaultComponentSlot + slot, -1, 0, 0);
	}

	/**
	 * Loads an entire preset for either the inventory or equipment.
	 * 
	 * @param player the player requesting the preset to be loaded
	 */
	public void load(Player player) {
		if (Boundary.isIn(player, Boundary.WILDERNESS_PARAMETERS)) {
			player.sendMessage("Loading cannot be completed while within the wilderness.");
			return;
		}
		if (player.getBankPin().isLocked() && player.getBankPin().getPin().trim().length() > 0) {
			player.sendMessage("You have a bank pin you must unlock before using this.");
			return;
		}
		if (player.playerIndex > 0 || player.npcIndex > 0) {
			player.sendMessage("You cannot load presets this whilst in combat with a player or npc.");
			return;
		}
		if (Server.getMultiplayerSessionListener().inAnySession(player)) {
			player.sendMessage("You cannot do this whilst interacting with another player.");
			return;
		}
		if (items.size() <= 0) {
			player.sendMessage("There are currently no items existing in this preset.");
			return;
		}
		if (System.currentTimeMillis() - lastOutput < 1_500) {
			player.sendMessage("You attempted to load a set too quickly, please wait a few seconds " + "and try again.");
			return;
		}
		if (type.isEquipment()) {
			if (player.getItems().isWearingItems()) {
				player.sendMessage("You cannot be wearing equipment when loading an equipments preset.");
				return;
			}
		}
		if (type.isInventory()) {
			if (player.getItems().freeSlots() < 28) {
				player.sendMessage("You cannot have any items in your inventory when loading an" + " inventory preset.");
				return;
			}
		}
		Optional<GameItem> itemsNonexistant = items.values().stream().filter(item -> !player.getItems().bankContains(item.getId(), item.getAmount())).findFirst();
		if (itemsNonexistant.isPresent()) {
			player.sendMessage("You cannot load this preset, one or more items do not exist in your bank.");
			return;
		}
		if (type.isEquipment()) {
			for (Entry<Integer, GameItem> entry : items.entrySet()) {
				ItemDefinition itemDefinition = ItemDefinition.forId(entry.getValue().getId());
				if (itemDefinition != null) {
					if (!itemDefinition.isWearable()) {
						player.sendMessage(itemDefinition.getName() + " is not wearable and therefore " + "should not be loaded.");
						return;
					}
					int slot = PresetSlotAction.getEquipmentSlot(type, entry.getKey());
					if (slot != itemDefinition.getSlot()) {
						player.sendMessage(itemDefinition.getName() + " cannot be loaded from this slot.");
						return;
					}
					for (int i = 0; i < itemDefinition.getRequirements().length; i++) {
						if (player.playerLevel[i] < itemDefinition.getRequirements()[i]) {
							player.sendMessage("You dont have the level requirement to use " + itemDefinition.getRequirements()[i] + ".");
							return;
						}
					}
				} else {
					player.sendMessage("The item (" + entry.getValue().getId() + ") is not supported. Please inform" + " any staff member.");
					return;
				}
			}
			for (Entry<Integer, GameItem> entry : items.entrySet()) {
				int slot = PresetSlotAction.getEquipmentSlot(type, entry.getKey());
				GameItem item = entry.getValue();
				if (player.getItems().removeFromAnyTabWithoutAdding(item.getId(), item.getAmount(), false)) {
					player.getItems().wearItem(item.getId(), item.getAmount(), slot);
					if (slot == 3) {
						player.autocasting = false;
						player.autocastId = 0;
						player.getPA().sendFrame36(108, 0);
						player.usingSpecial = false;
						player.getItems().addSpecialBar(item.getId());
					}
				} else {
					player.sendMessage("We were not able to load your set entirely.");
					return;
				}
			}
		} else if (type.isInventory()) {
			for (Entry<Integer, GameItem> entry : items.entrySet()) {
				int slot = entry.getKey();
				GameItem item = entry.getValue();
				if (player.getItems().removeFromAnyTabWithoutAdding(item.getId(), item.getAmount(), false)) {
					player.playerItems[slot] = item.getId() + 1;
					player.playerItemsN[slot] = item.getAmount();
				} else {
					player.sendMessage("We were not able to load your set entirely.");
					break;
				}
			}
			player.getItems().updateInventory();
		}
		player.sendMessage("You have successfully loaded your " + WordUtils.capitalize(type.name().toLowerCase()) + " .");
	}

	/**
	 * A map of the game items
	 * 
	 * @return the items
	 */
	public Map<Integer, GameItem> getItems() {
		return items;
	}

}
