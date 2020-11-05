package ethos.model.items;

import org.apache.commons.lang3.RandomUtils;

import ethos.model.players.skills.hunter.impling.ItemRarity;

/**
 * Represents an item that exists within the game. Items hold identification and value, and have the potential to contain other information as well.
 * 
 * @author Jason MacKeigan
 * @date Oct 20, 2014, 2:55:17 PM
 */
public class GameItem {

	private int id, amount, slot;

	private boolean stackable;

	/**
	 * Constructs a new game item with an id and amount of 0
	 * 
	 * @param id the id of the item
	 * @param amount the amount of the item
	 */
	public GameItem(int id) {
		this.id = id;
		this.amount = 1;
		if (Item.itemStackable[id]) {
			stackable = true;
		}
	}
	
	/**
	 * Constructs a new game item with an id and amount
	 * 
	 * @param id the id of the item
	 * @param amount the amount of the item
	 */
	public GameItem(int id, int amount) {
		this(id);
		this.amount = amount;
	}

	public GameItem(int id, int amount, int slot) {
		this(id, amount);
		this.amount = amount;
		this.slot = slot;
	}

	/**
	 * Attempts to return the same item with a randomized amount.
	 * 
	 * @return the same item with a randomized amount
	 */
	public GameItem randomizedAmount() {
		if (amount == 1) {
			return this;
		}

		return new GameItem(id, RandomUtils.nextInt(1, amount));
	}

	/**
	 * Returns a new GameItem object with a new id and amount
	 * 
	 * @param id the item id
	 * @param amount the item amount
	 * @return a new game item
	 */
	public GameItem set(int id, int amount) {
		return new GameItem(id, amount);
	}

	/**
	 * Retries the item id for the game item
	 * 
	 * @return the item id
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * @param id the id to set
	 */
	public void changeDrop(int id, int amount) {
		this.id = id;
		this.amount = amount;
	}

	/**
	 * Retrieves the amount of the game item
	 * 
	 * @return the amount
	 */
	public int getAmount() {
		return amount;
	}

	/**
	 * Sets the amount of the game item
	 * 
	 * @param the amount
	 */
	public void setAmount(int amount) {
		this.amount = amount;
	}

	/**
	 * The slot the game item exists in the container
	 * 
	 * @return the slot
	 */
	public int getSlot() {
		return slot;
	}

	/**
	 * Determines if the item is stackable
	 * 
	 * @return true if the item is stackable, false if it is not.
	 */
	public boolean isStackable() {
		return stackable;
	}

	@Override
	public String toString() {
		return "GameItem [id=" + id + ", amount=" + amount + ", slot=" + slot + ", stackable=" + stackable + "]";
	}
	
	/** ITEM RARITY **/
	public ItemRarity rarity;

	public GameItem setRarity(ItemRarity rarity) {
		this.rarity = rarity;
		return this;
	}
	
	public ItemRarity getRarity() {
		return this.rarity;
	}
}