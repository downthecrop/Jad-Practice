package ethos.model.players.skills.fletching;

public enum FletchableBow {
	NORMAL_SHORTBOW(50, 841, 6678, 1, 5), NORMAL_LONGBOW(48, 839, 6684, 10, 10), OAK_SHORTBOW(54, 843, 6679, 20, 16.5), OAK_LONGBOW(56, 845, 6685, 25, 25), WILLOW_SHORTBOW(60, 849,
			6680, 35, 33.25), WILLOW_LONGBOW(58, 847, 6686, 40, 41.5), MAPLE_SHORTBOW(64, 853, 6681, 50, 50), MAPLE_LONGBOW(62, 851, 6687, 55, 58.2), YEW_SHORTBOW(68, 857, 6682,
					65, 67.5), YEW_LONGBOW(66, 855, 6688, 70, 75), MAGIC_SHORTBOW(72, 861, 6683, 80, 83.2), MAGIC_LONGBOW(70, 859, 6689, 85, 91.5);

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

	FletchableBow(int item, int product, int animation, int levelRequired, double experience) {
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
