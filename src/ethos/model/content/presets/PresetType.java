package ethos.model.content.presets;

/**
 * A preset type represents the different 'types' of presets that exist.
 * 
 * @author Jason MacKeigan
 * @date Dec 30, 2014, 1:51:30 AM
 */
public enum PresetType {

	INVENTORY, EQUIPMENT;

	/**
	 * Determines if the type is an inventory preset
	 * 
	 * @return true if the type is an inventory preset
	 */
	public boolean isInventory() {
		return equals(INVENTORY);
	}

	/**
	 * Determines if the type is an equipment preset
	 * 
	 * @return true if the type is an equipment preset
	 */
	public boolean isEquipment() {
		return equals(EQUIPMENT);
	}

}
