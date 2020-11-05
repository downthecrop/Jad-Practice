package ethos.model.players.skills.fletching;

public enum FletchableArrow {
	BRONZE(39, 882, 1, 2.6), 
	IRON(40, 884, 15, 3.8), 
	STEEL(41, 886, 30, 6.3), 
	MITHRIL(42, 888, 45, 8.8), 
	ADAMANT(43, 890, 60, 11.3), 
	RUNE(44, 892, 75, 13.8), 
	AMETHYST(21350, 21326, 82, 14.3), 
	DRAGON(11237, 11212, 90, 15);

	/**
	 * The id
	 */
	private int id;
	/**
	 * The reward;
	 */
	private int reward;
	/**
	 * The level required.
	 */
	private int levelRequired;
	/**
	 * The experience granted.
	 */
	private double experience;

	private FletchableArrow(int id, int reward, int levelRequired, double experience) {
		this.id = id;
		this.reward = reward;
		this.levelRequired = levelRequired;
		this.experience = experience;
	}

	public double getExperience() {
		return experience;
	}

	public int getId() {
		return id;
	}

	public int getLevelRequired() {
		return levelRequired;
	}

	public int getReward() {
		return reward;
	}

}