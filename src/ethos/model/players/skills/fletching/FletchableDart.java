package ethos.model.players.skills.fletching;

public enum FletchableDart {
	BRONZE(819, 806, 1, 1.8), 
	IRON(820, 807, 22, 3.8), 
	STEEL(821, 808, 38, 7.5), 
	MITHRIL(822, 809, 52, 11.2), 
	ADAMANT(823, 810, 67, 15), 
	RUNE(824, 811, 81, 18.8), 
	DRAGON(11232, 11230, 95, 25);

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

	private FletchableDart(int id, int reward, int levelRequired, double experience) {
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
