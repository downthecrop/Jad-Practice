package ethos.model.content.presets;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import ethos.model.players.Player;

/**
 * 
 * @author Jason MacKeigan
 * @date Dec 30, 2014, 1:46:46 AM
 */
public class Presets {

	private static final int MAXIMUM_SLOTS = 10;

	/**
	 * The owner of these presets
	 */
	private Player player;

	/**
	 * A mapping of all of the possible presets, from 1 to 10
	 */
	private Map<Integer, Preset> presets = new HashMap<>(MAXIMUM_SLOTS);

	/**
	 * The current preset being modified
	 */
	private Preset current = null;

	/**
	 * The time in milliseconds of the last tab switch
	 */
	private long lastSwitch;

	/**
	 * Constructs a new class for managing all presets
	 * 
	 * @param player the owner of these presets
	 */
	public Presets(Player player) {
		this.player = player;
		for (int i = 0; i < MAXIMUM_SLOTS; i++) {
			presets.put(i, new Preset(i));
		}
		current = presets.get(0);
	}

	/**
	 * Visually displays the entire interface aside from the search component
	 */
	public void displayInterface() {
		if (player.getBankPin().isLocked() && player.getBankPin().getPin().trim().length() > 0) {
			player.sendMessage("You have a bank pin you must unlock before using this.");
			return;
		}
		switchPreset(0);
		refreshMenus(0, MAXIMUM_SLOTS - 1);
		player.getPA().showInterface(32000);
	}

	/**
	 * Opens the appropriate search interface
	 */
	public void displaySearch() {
		player.getPA().sendFrame171(0, 32007);
	}

	/**
	 * Switches the current preset we are on to another
	 * 
	 * @param slot the slot value of the preset
	 */
	public void switchPreset(int slot) {
		if (slot < 0 || slot > MAXIMUM_SLOTS) {
			throw new IllegalStateException("Invalid slot, cannot be negative or above the maximum range.");
		}
		current = presets.get(slot);
		current.setSelectedSlot(0);
		current.getInventory().draw(player);
		current.getEquipment().draw(player);
		player.getPA().sendString(current.getAlias(), 32002);
		hideSearch();
	}

	/**
	 * Closes and resets the search interface
	 */
	public void hideSearch() {
		player.getPA().sendFrame171(1, 32007);
		player.getPA().sendFrame126("Name", 32009);
		player.getPA().sendFrame126("Amount", 32012);
	}

	/**
	 * Refreshes each of the menus from the initial index passed, to the last index
	 * 
	 * @param index the index of the first menu
	 * @param lastIndex the index of the last menu
	 */
	public void refreshMenus(int index, int lastIndex) {
		if (current == null) {
			return;
		}
		if (index < 0 || lastIndex < 0) {
			throw new IllegalStateException("The initial or last index cannot be negative.");
		}
		if (lastIndex < index) {
			throw new IllegalStateException("The last index cannot be numerically lower than the initial index.");
		}
		if (lastIndex > MAXIMUM_SLOTS) {
			throw new IllegalStateException("The last index cannot exceed the maximum amount of slots.");
		}
		while (index < lastIndex) {
			Optional<PresetMenu> menu = PresetMenu.forId(index);
			if (!menu.isPresent()) {
				continue;
			}
			Preset preset = presets.get(index);
			if (preset == null) {
				continue;
			}
			player.getPA().sendString(preset.getAlias(), menu.get().getAliasComponentId());
			index++;
		}
	}

	/**
	 * Manages clicking certain buttons related to the preset interface
	 * 
	 * @param buttonId the button id
	 * @return clicking a button
	 */
	public boolean clickButton(int buttonId) {
		if (player.inClanWars() || player.inClanWarsSafe()) {
			return false;
		}
		if (buttonId == 108007) {
			displayInterface();
			return true;
		}
		if (buttonId == 125015) {
			hideSearch();
			current.setSelectedSlot(0);
			return true;
		}
		for (PresetSlotAction slotAction : PresetSlotAction.SLOTS) {
			if (buttonId == slotAction.getEdit()) {
				current.setEditingType(slotAction.getType());
				current.setSelectedSlot(slotAction.getSlot());
				displaySearch();
				return true;
			}
			if (buttonId == slotAction.getDelete()) {
				current.setEditingType(slotAction.getType());
				if (slotAction.getType().isInventory()) {
					current.getInventory().remove(player, slotAction.getItemSlot());
				} else if (slotAction.getType().isEquipment()) {
					current.getEquipment().remove(player, slotAction.getSlot());
				}
				return true;
			}
		}
		for (PresetMenu menu : PresetMenu.MENU) {
			if (buttonId == menu.getViewId()) {
				if (System.currentTimeMillis() - lastSwitch < 5000) {
					player.sendMessage("You must wait 5 seconds before switching from tab to tab.");
					return true;
				}
				lastSwitch = System.currentTimeMillis();
				switchPreset(menu.getSlot());
				return true;
			} else if (buttonId == menu.getLoadEquipmentId()) {
				current.getEquipment().load(player);
				return true;
			} else if (buttonId == menu.getLoadInventoryId()) {
				current.getInventory().load(player);
				return true;
			}
		}
		return false;
	}

	/**
	 * The current Preset we're managing
	 * 
	 * @return the preset
	 */
	public Preset getCurrent() {
		if (current == null) {
			return presets.get(0);
		}
		return current;
	}

	/**
	 * The map of presets
	 * 
	 * @return presets
	 */
	public Map<Integer, Preset> getPresets() {
		return presets;
	}

}
