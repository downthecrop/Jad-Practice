package ethos.model.items;

/**
 * Represents an item on the ground at a specific location on the map.
 * 
 * @author Jason MacKeigan
 * @date Feb 10, 2015, 4:08:14 PM
 */
public class GroundItem {

	/**
	 * The identification value of the item that makes it unique from the rest
	 */
	private int id;

	/**
	 * The location of the item on the x-axis
	 */
	private int x;

	/**
	 * The location of the item on the y-axis
	 */
	private int y;

	/**
	 * The height level of the item on the ground
	 */
	private int height;

	/**
	 * The amount of the item.
	 */
	private int amount;

	public int hideTicks;

	public int removeTicks;

	public String ownerName;

	/**
	 * Creates a new {@link GroundItem} object on the x, y, and z-axis.
	 * 
	 * @param id the identification value of the item
	 * @param x the x location
	 * @param y the y location
	 * @param height the height on the map
	 * @param amount the amount of the item
	 * @param controller the player id, the controller
	 * @param hideTicks the amount of ticks until hidden
	 * @param name the name of the owner
	 */
	public GroundItem(int id, int x, int y, int height, int amount, int hideTicks, String name) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.height = height;
		this.amount = amount;
		this.hideTicks = hideTicks;
		this.ownerName = name;
	}

	/**
	 * The item identification value
	 * 
	 * @return the item id
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Retrieves the absolute x position of the item on the map
	 * 
	 * @return the x position
	 */
	public int getX() {
		return x;
	}

	/**
	 * Retrieves the absolute y position of the item on the map
	 * 
	 * @return the y position
	 */
	public int getY() {
		return y;
	}

	/**
	 * The amount of the item that exists at this position
	 * 
	 * @return the amount of the item
	 */
	public int getAmount() {
		return amount;
	}

	/**
	 * Item name.
	 * 
	 * @return
	 */
	public String getController() {
		return this.ownerName;
	}

	/**
	 * The ground item must be displayed on a height level. The ground item can only appear on the height level its created on.
	 * 
	 * @return the height level of the ground item
	 */
	public int getHeight() {
		return height;
	}

}