package ethos.model.players;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import ethos.model.items.GameItem;
import ethos.model.items.ItemNotFoundException;

public class Equipment {

	/**
	 * A mapping for items worn by a player for their each respective equipment slot.
	 */
	private Map<Slot, GameItem> equipment = new HashMap<>();

	/**
	 * Creates a new {@link Equipment} object with the given information. Currently, this is information created from the player upon initialization.
	 * 
	 * @param equipment an array of item id values and item amount values.
	 */
	public Equipment(GameItem[] equipment) {
		for (int index = 0; index < equipment.length; index++) {
			Slot slot = Slot.valueOf(index);
			if (slot != null) {
				this.equipment.put(slot, equipment[index]);
			}
		}
	}

	/**
	 * Updates the equipment slot for the given item. If the mapping does not contain the slot, the new value is inserted. If the key exists, then the item is updated. This is
	 * principle of the put function.
	 * 
	 * @param slot the equipment slot for the item.
	 * @param item the new item to be updated.
	 */
	public void update(Slot slot, GameItem item) {
		equipment.put(slot, item);
	}

	/**
	 * Retains the item for the given slot.
	 * 
	 * @param slot the equipment slot we get the game item from.
	 * @return the item for the slot or a NullPointerException if no item exists for that slot.
	 */
	public GameItem getItem(Slot slot) {
		if (!equipment.containsKey(slot)) {
			throw new ItemNotFoundException();
		}
		return equipment.get(slot);
	}

	/**
	 * Determines if the player is wearing any items for the given slot.
	 * 
	 * @param slot the slot where the item should be worn.
	 * @param items an array of items the player must be wearing one of.
	 * @return {@code} true if the player is wearing at least one of the items.
	 */
	public boolean wearingAny(Slot slot, GameItem... items) {
		GameItem item = null;

		try {
			item = getItem(slot);

			for (GameItem i : items) {
				if (item.getId() == i.getId()) {
					return true;
				}
			}
		} catch (ItemNotFoundException e) {
			return false;
		}
		return false;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		equipment.entrySet().forEach(e -> {
			GameItem item = e.getValue();
			if (item != null) {
				sb.append(item.getId() + ", " + item.getAmount() + "\n");
			}
		});
		return sb.toString();
	}

	public enum Slot {
		HELMET(0), CAPE(1), AMULET(2), WEAPON(3), CHEST(4), SHIELD(5), LEGS(7), HANDS(9), FEET(10), RING(12), AMMO(13);

		private final int slot;

		private Slot(int slot) {
			this.slot = slot;
		}

		public int getSlot() {
			return slot;
		}

		public static final Slot valueOf(int slot) throws NullPointerException {
			return Stream.of(values()).filter(s -> s.slot == slot).findFirst().orElse(null);
		}
	}
}
