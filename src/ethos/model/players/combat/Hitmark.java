package ethos.model.players.combat;

/**
 * An enumeration of different hitmarks. Each type of hitmark has an identification value that seperates it from the rest.
 * 
 * @author Jason MacKeigan
 * @date Jan 26, 2015, 2:41:46 AM
 */
public enum Hitmark {
	MISS(0), HIT(1), POISON(2), VENOM(5);

	/**
	 * The id of the hitmark
	 */
	private final int id;

	/**
	 * Creates a new hitmark with an id
	 * 
	 * @param id the id
	 */
	private Hitmark(int id) {
		this.id = id;
	}

	/**
	 * The identification value for this hitmark
	 * 
	 * @return the value
	 */
	public int getId() {
		return id;
	}

	/**
	 * Determines if this hitmark is blue, a miss.
	 * 
	 * @return true if the hitmark signifies a miss
	 */
	public boolean isMiss() {
		return equals(MISS);
	}

	/**
	 * Determines if this hitmark is red, a hit.
	 * 
	 * @return true if the hitmark signifies a hit
	 */
	public boolean isHit() {
		return equals(HIT);
	}

	/**
	 * Determines if this hitmark is green, poison.
	 * 
	 * @return true if the hitmark is green
	 */
	public boolean isPoison() {
		return equals(POISON);
	}

	/**
	 * Determines if the hitmark is dark green, venom.
	 * 
	 * @return true if the hitmark is venom
	 */
	public boolean isVenom() {
		return equals(VENOM);
	}

}
