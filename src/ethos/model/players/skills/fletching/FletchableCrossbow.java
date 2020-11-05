package ethos.model.players.skills.fletching;

public enum FletchableCrossbow {
	ADAMANR_CROSSBOW(9445, 9185, 4442, 1, 92), RUNE_CROSSBOW(9465, 9185, 4442, 69, 92);

	/**
	 * The unstrung bow
	 */
	private final int item;
	/**
	 * The finished product
	 */
	private final int product;
	/**
	 * The level required for making a bow
	 */
	private final int levelRequired;
	/**
	 * The experience gained from making the bow
	 */
	private final double experience;

	private final int animation;

	FletchableCrossbow(int item, int product, int animation, int levelRequired, double experience) {
		this.item = item;
		this.product = product;
		this.animation = animation;
		this.levelRequired = levelRequired;
		this.experience = experience;
	}

	/**
	 * The experience gained from crafting a bow
	 * 
	 * @return The experience gained from crafting a bow
	 */
	public double getExperience() {
		return experience;
	}

	/**
	 * The unstrung bows that need stringing
	 * 
	 * @return the bows that need to be strung
	 */
	public int getItem() {
		return item;
	}

	/**
	 * The level that is required for a bow
	 * 
	 * @return The certain level for a required bow
	 */
	public int getLevelRequired() {
		return levelRequired;
	}

	/**
	 * The finished product or "strung bow"
	 * 
	 * @return The finished product
	 */
	public int getProduct() {
		return product;
	}

	public int getAnimation() {
		return animation;
	}

}
