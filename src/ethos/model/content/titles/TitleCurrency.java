package ethos.model.content.titles;

/**
 * An enumeration of currencies used to purchase titles.
 * 
 * @author Jason MacKeigan
 * @date Jan 24, 2015, 1:55:59 PM
 */
public enum TitleCurrency {
	NONE(-1, (byte) 0), PK_TICKETS(2996, (byte) 1), COINS(995, (byte) 2), BOSS_TICKETS(4067, (byte) 3), FIRECAPE(6570, (byte) 4);

	/**
	 * The identification value of the item that represents the currency
	 */
	private final int itemId;

	/**
	 * The index in an array of images that tells us what image represents this currency
	 */
	private final byte spriteIndex;

	/**
	 * Creates a new currency that utilizes an item and a sprite
	 * 
	 * @param itemId the item id
	 * @param spriteIndex the index
	 */
	private TitleCurrency(int itemId, byte spriteIndex) {
		this.itemId = itemId;
		this.spriteIndex = spriteIndex;
	}

	/**
	 * Determines the item id used in this currency
	 * 
	 * @return the item
	 */
	public int getItemId() {
		return itemId;
	}

	/**
	 * The index in an array of sprites
	 * 
	 * @return the index
	 */
	public byte getSpriteIndex() {
		return spriteIndex;
	}

}
