package ethos.model.players.skills.fletching;

public enum FletchableUnfinishedBolt {
	BRONZE(9375, 877, 9, 1), IRON(9377, 9140, 39, 2), STEEL(9378, 9141, 46, 4), MITHRIL(9379, 9142, 54, 5), ADAMANT(9380, 9143, 61, 7), RUNE(9381, 9144, 69, 10), BROAD(11876, 11875, 55, 7);

	private final int unfinished, bolt, level, experience;

	private FletchableUnfinishedBolt(int unfinished, int bolt, int level, int experience) {
		this.unfinished = unfinished;
		this.bolt = bolt;
		this.level = level;
		this.experience = experience;
	}

	public int getUnfinished() {
		return unfinished;
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
