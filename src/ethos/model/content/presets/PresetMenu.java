package ethos.model.content.presets;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;

/**
 * 
 * @author Jason MacKeigan
 * @date Jan 2, 2015, 3:14:44 AM
 */
public enum PresetMenu {

	SLOT_0(0, 133150, 133149, 133148, 32033), 
	SLOT_1(1, 133183, 133182, 133181, 32036), 
	SLOT_2(2, 133216, 133215, 133214, 32039), 
	SLOT_3(3, 133249, 133248, 133247, 32042), 
	SLOT_4(4, 134026, 134025, 134024, 32045), 
	SLOT_5(5, 134059, 134058, 134057, 32048), 
	SLOT_6(6, 134092, 134091, 134090, 32051), 
	SLOT_7(7, 134125, 134124, 134123, 32054), 
	SLOT_8(8, 134158, 134157, 134156, 32057), 
	SLOT_9(9, 134191, 134190, 134189, 32060);

	private int slot, view, loadInventory, loadEquipment, alias;

	/**
	 * The menu has several actions; Viewing, loading, and saving. Each action has a button value associated with it.
	 * 
	 * @param viewAction clicked to view the specific preset
	 * @param loadInventory clicked to load the inventory
	 * @param loadEquipment clicked to load the equipment
	 */
	private PresetMenu(int slot, int view, int loadEquipment, int loadInventory, int alias) {
		this.slot = slot;
		this.view = view;
		this.loadEquipment = loadEquipment;
		this.loadInventory = loadInventory;
		this.alias = alias;
	}

	/**
	 * The menu has 10 available slots, each action refers to a specific slot.
	 * 
	 * @return the slot
	 */
	public int getSlot() {
		return slot;
	}

	/**
	 * The identification value associated with the view button
	 * 
	 * @return the button id
	 */
	public int getViewId() {
		return view;
	}

	/**
	 * The identification value associated with the load equipment button
	 * 
	 * @return the button id
	 */
	public int getLoadEquipmentId() {
		return loadEquipment;
	}

	/**
	 * The identification value associated with the load inventory button
	 * 
	 * @return the button id
	 */
	public int getLoadInventoryId() {
		return loadInventory;
	}

	/**
	 * The frame or component identification value for the alias in each menu slot
	 * 
	 * @return the component or frame id
	 */
	public int getAliasComponentId() {
		return alias;
	}

	/**
	 * A set containing all of the elements of the PresetMenu enum
	 */
	public static final Set<PresetMenu> MENU = Collections.unmodifiableSet(EnumSet.allOf(PresetMenu.class));

	/**
	 * Obtains an element from the PresetMenu enum with the same slot value as the parameter
	 * 
	 * @param slot the slot value
	 * @return the PresetMenu element with a matching slot value
	 */
	public static Optional<PresetMenu> forId(int slot) {
		return MENU.stream().filter(m -> m.slot == slot).findFirst();
	}
}
