package ethos.model.content.barrows;

public enum RewardLevel {

	COMMON(220), UNCOMMON(15), RARE(2);

	final static int KC_MULTIPLIER = 20;

	private int rarity;

	RewardLevel(int rarity) {
		this.rarity = rarity;
	}

	public int getRarity() {
		return rarity;
	}

}
