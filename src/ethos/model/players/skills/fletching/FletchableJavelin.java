package ethos.model.players.skills.fletching;

public enum FletchableJavelin {
	BRONZE(19570, 825, 1, 4.6), 
	IRON(19572, 826, 17, 5.8), 
	STEEL(19574, 827, 32, 8.3), 
	MITHRIL(19576, 828, 47, 10.8), 
	ADAMANT(19578, 829, 62, 14.3), 
	RUNE(19580, 830, 77, 16.8),
	AMETHYST(21352, 21318, 84, 17.9), 
	DRAGON(19582, 19484, 92, 19);

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

	private FletchableJavelin(int id, int reward, int levelRequired, double experience) {
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
