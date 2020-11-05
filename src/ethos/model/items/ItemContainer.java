package ethos.model.items;

public abstract class ItemContainer {

	/**
	 * The maximum amount of {@link GameItem} items that can be stored in the container.
	 */
	protected final int capacity;

	/**
	 * An array of {@link GameItem} items that exist in the container
	 */
	protected GameItem[] items;

	/**
	 * Determines if this container permittes duplicate items. Items are deemed duplicates if the identification value of one {@link GameItem} matches another.
	 */
	protected final boolean duplicatesPermitted;

	/**
	 * Constructs a new {@code ItemContainer} with a maximum capacity.
	 * 
	 * @param capacity the maximum amount of separate items allowed in the container
	 */
	public ItemContainer(int capacity, boolean duplicatesPermitted) {
		this.capacity = capacity;
		this.duplicatesPermitted = duplicatesPermitted;
		this.items = new GameItem[capacity];
	}

	/**
	 * Adds a single item to the array of items
	 * 
	 * @param item the item to be added
	 * @param slot the slot the item is to be added
	 */
	public abstract void add(GameItem item, int slot);

	/**
	 * Removes a single item from the array of items
	 * 
	 * @param item the item to be removed
	 * @param slot the slot the item is to be removed from
	 */
	public abstract void remove(GameItem item, int slot);

	/**
	 * Attempts to perform a reduction on an item in the list of items at the specified slot.
	 * 
	 * @param item the item that must exist at the slot otherwise this will fail
	 * @param slot the slot the item resides at that will have the amount reduced
	 * @return true if the item amount can be reduced, otherwise false
	 */
	public boolean reduce(int itemId, int amount, int slot) {
		if (slot > capacity - 1 || slot < 0) {
			return false;
		}
		GameItem item = items[slot];
		if (item == null) {
			return false;
		}
		if (item.getId() != itemId) {
			return false;
		}
		if (item.getAmount() < amount) {
			return false;
		}
		item.setAmount(item.getAmount() - amount);
		return true;
	}

	/**
	 * Attempts an augemtation on the item amount in a specific slot
	 * 
	 * @param itemId the item id that must exist at the slot
	 * @param amount the amount that will be reduced
	 * @param slot the slot where the item exists that will have this performed on
	 * @return true if the augmentation is successful
	 */
	public boolean augment(int itemId, int amount, int slot) {
		if (slot > capacity - 1 || slot < 0) {
			return false;
		}
		GameItem item = items[slot];
		if (item == null) {
			return false;
		}
		if (item.getId() != itemId) {
			return false;
		}
		item.setAmount(item.getAmount() + amount);
		return true;
	}

	/**
	 * Determines if a certain item exists in the container. The amount of the item is checked as well. If the slot exceeds the maximum capacity or is less than 0 then false will
	 * be returned.
	 * 
	 * @param item the item we're determining exists in the slot
	 * @param slot the slot we're checking
	 * @return true if the item exists in the slot, otherwise false
	 */
	public boolean contains(int itemId, int amount, int slot) {
		if (slot > capacity - 1 || slot < 0) {
			return false;
		}
		GameItem item = items[slot];
		if (item == null) {
			return false;
		}
		if (item.getId() != itemId) {
			return false;
		}
		if (item.getAmount() < amount) {
			return false;
		}
		return true;
	}

	/**
	 * The amount of slots available. A slot is available if there is no item available at that index.
	 * 
	 * @return the amount of slots available
	 */
	public int getSlotsAvailable() {
		int available = 0;
		for (int slot = 0; slot < items.length; slot++) {
			if (items[slot] == null) {
				available++;
			}
		}
		return available;
	}
}
