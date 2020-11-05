package ethos.model.players.skills.fletching;

public enum FletchableBolt {
	SAPPHIRE(9142, 9189, 9337, 54, 5), 
	EMERALD(9142, 9190, 9338, 61, 7), 
	RUBY(9143, 9191, 9339, 63, 8), 
	DIAMOND(9143, 9192, 9340, 65, 9), 
	DRAGON(9144, 9193, 9341, 71, 10), 
	ONYX(9144, 9194, 9342, 73, 12),
	AMETHYST(11875, 21338, 21316, 76, 11);

	private final int unfinished, tip, bolt, level, experience;

	private FletchableBolt(int unfinished, int tip, int bolt, int level, int experience) {
		this.unfinished = unfinished;
		this.tip = tip;
		this.bolt = bolt;
		this.level = level;
		this.experience = experience;
	}

	public int getUnfinished() {
		return unfinished;
	}

	public int getTip() {
		return tip;
	}

	public int getBolt() {
		return bolt;
	}

	public int getLevel() {
		return level;
	}

	public int getExperience() {
		return experience;
	}

}
