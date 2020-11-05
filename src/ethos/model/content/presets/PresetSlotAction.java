package ethos.model.content.presets;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;

/**
 * Although the delete buttin may be the value of (edit - 1), values may need to change in the future.
 * 
 * @author Jason MacKeigan
 * @date Dec 30, 2014, 12:21:42 PM
 */
public enum PresetSlotAction {
	INVENTORY_0(0, 136140, 136139), 
	INVENTORY_1(1, 136151, 136150), 
	INVENTORY_2(2, 136162, 136161), 
	INVENTORY_3(3, 136173, 136172), 
	INVENTORY_4(4, 136184, 136183), 
	INVENTORY_5(5, 136195, 136194), 
	INVENTORY_6(6, 136206, 136205), 
	INVENTORY_7(7, 136217, 136216), 
	INVENTORY_8(8, 136228, 136227), 
	INVENTORY_9(9, 136239, 136238), 
	INVENTORY_10(10, 136250, 136249), 
	INVENTORY_11(11, 137005, 137004),
	INVENTORY_12(12, 137016, 137015),
	INVENTORY_13(13, 137027, 137026), 
	INVENTORY_14(14, 137038, 137037), 
	INVENTORY_15(15, 137049, 137048),
	INVENTORY_16(16, 137060, 137059),
	INVENTORY_17(17, 137071, 137070), 
	INVENTORY_18(18, 137082, 137081), 
	INVENTORY_19(19, 137093, 137092), 
	INVENTORY_20(20, 137104, 137103), 
	INVENTORY_21(21, 137115, 137114), 
	INVENTORY_22(22, 137126, 137125), 
	INVENTORY_23(23, 137137, 137136), 
	INVENTORY_24(24, 137148, 137147), 
	INVENTORY_25(25, 137159, 137158), 
	INVENTORY_26(26, 137170, 137169), 
	INVENTORY_27(27, 137181, 137180), 
	
	EQUIPMENT_0(0, 140216, 140215), 
	EQUIPMENT_1(1, 140227, 140226), 
	EQUIPMENT_2(2, 140238, 140237), 
	EQUIPMENT_3(3, 140249, 140248), 
	EQUIPMENT_4(4, 141004, 141003),
	EQUIPMENT_5(5, 141015, 141014),
	EQUIPMENT_7(6, 141026, 141025), 
	EQUIPMENT_9(7, 141037, 141036), 
	EQUIPMENT_10(8, 141048, 141047), 
	EQUIPMENT_12(9, 141059, 141058), 
	EQUIPMENT_13(10, 141070, 141069);

	private PresetType type;
	private int slot, edit, delete;

	private PresetSlotAction(int slot, int edit, int delete) {
		this.slot = slot;
		this.edit = edit;
		this.delete = delete;
		String name = name().toLowerCase().substring(0, name().indexOf("_"));
		this.type = name.equals("inventory") ? PresetType.INVENTORY : PresetType.EQUIPMENT;
	}

	public int getEdit() {
		return edit;
	}

	public int getDelete() {
		return delete;
	}

	public int getSlot() {
		return slot;
	}

	public int getItemSlot() {
		return Integer.parseInt(name().split("_")[1]);
	}

	public PresetType getType() {
		return type;
	}

	public static final Set<PresetSlotAction> SLOTS = Collections.unmodifiableSet(EnumSet.allOf(PresetSlotAction.class));

	public static int getEquipmentSlot(PresetType type, int slot) {
		Optional<PresetSlotAction> psa = SLOTS.stream().filter(s -> s.type.equals(type) && s.slot == slot).findFirst();
		return psa.get().getItemSlot();
	}

}
