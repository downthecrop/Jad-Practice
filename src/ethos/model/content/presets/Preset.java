package ethos.model.content.presets;

public class Preset {
	/**
	 * The slot on the menu associated with the interface
	 */
	private int menuSlot;

	/**
	 * The slot selected by the player for editing
	 */
	private int selectedSlot;

	/**
	 * The alias or name of the preset
	 */
	private String alias;

	/**
	 * The current type of preset we're editing
	 */
	private PresetType editingType = PresetType.INVENTORY;

	/**
	 * The inventory container
	 */
	private PresetContainer inventory = new PresetContainer(PresetType.INVENTORY, 28, 32129);

	/**
	 * The equipment container
	 */
	private PresetContainer equipment = new PresetContainer(PresetType.EQUIPMENT, 11, 32212);

	/**
	 * Creates a new preset and defines which slot it exists in
	 * 
	 * @param menuSlot
	 */
	public Preset(int menuSlot) {
		this.menuSlot = menuSlot;
		this.alias = "New slot";
	}

	/**
	 * The main menu has a maximum amount of slots. Each menu has a slot it resides to.
	 * 
	 * @return the slot this preset resides to
	 */
	public int getMenuSlot() {
		return menuSlot;
	}

	/**
	 * The selected slot on the interface where each item resides
	 * 
	 * @return the slot
	 */
	public int getSelectedSlot() {
		return selectedSlot;
	}

	/**
	 * Mutates the current selected slot to that of the parameter
	 * 
	 * @param selectedSlot the selected slot
	 */
	public void setSelectedSlot(int selectedSlot) {
		this.selectedSlot = selectedSlot;
	}

	/**
	 * The alias of the preset
	 * 
	 * @return the alias or name
	 */
	public String getAlias() {
		return alias;
	}

	/**
	 * Mutates the alias of the preset
	 * 
	 * @param alias the alias or name
	 */
	public void setAlias(String alias) {
		this.alias = alias;
	}

	/**
	 * The type of preset we're editing
	 * 
	 * @return the type
	 */
	public PresetType getEditingType() {
		return editingType;
	}

	/**
	 * Changes the current editing type to the param
	 * 
	 * @param editingType the type of preset we're editing
	 */
	public void setEditingType(PresetType editingType) {
		this.editingType = editingType;
	}

	/**
	 * Returns the container that manages the inventory slots
	 * 
	 * @return the inventory container
	 */
	public PresetContainer getInventory() {
		return inventory;
	}

	/**
	 * Returns the container that manages the equipment slots
	 * 
	 * @return the equipment container
	 */
	public PresetContainer getEquipment() {
		return equipment;
	}

}
