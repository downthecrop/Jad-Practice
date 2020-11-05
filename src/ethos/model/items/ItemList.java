package ethos.model.items;

/**
 * Gets Items From The Item List
 * 
 * @author Sanity Revised by Shawn Notes by Shawn
 */
public class ItemList {

	public int itemId;
	public String itemName;
	public String itemDescription;
	public double ShopValue;
	public double LowAlch;
	public double HighAlch;
	public int[] Bonuses = new int[100];

	/**
	 * The identification value that represents either the noted version of this item or the un-noted version.
	 */
	private int counterpartId;

	/**
	 * Gets the item ID.
	 * 
	 * @param _itemId
	 */
	public ItemList(int itemId) {
		this.itemId = itemId;
	}

	public int getCounterpartId() {
		return counterpartId;
	}

	public void setCounterpartId(int counterpartId) {
		this.counterpartId = counterpartId;
	}
}
