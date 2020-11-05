package ethos.model.players.skills.herblore;

import ethos.model.items.GameItem;

public enum Potion {
	AGILITY(new GameItem(3032), 34, 80, new GameItem(2998), new GameItem(2152)), 
	ANTI_VENOM(new GameItem(12905), 87, 120, new GameItem(5954), new GameItem(12934, 15)), 
	ANTI_VENOM_PLUS(new GameItem(12913), 94, 125, new GameItem(12907), new GameItem(269)), 
	ANTIDOTE_PLUS(new GameItem(5943), 68, 155, new GameItem(2998), new GameItem(6049)), 
	ANTIDOTE_PLUS_PLUS(new GameItem(5952), 79, 177, new GameItem(259), new GameItem(6051)), 
	STAMINA(new GameItem(12625), 77, 152, new GameItem(3016), new GameItem(12640, 3)), 
	ANTIFIRE(new GameItem(2452), 69, 157, new GameItem(2481), new GameItem(241)), 
	ANTIPOISON(new GameItem(2446), 5, 37, new GameItem(251), new GameItem(235)), 
	ATTACK(new GameItem(2428), 3, 25, new GameItem(249), new GameItem(221)), 
	COMBAT(new GameItem(9739), 36, 84, new GameItem(255), new GameItem(9736)), 
	DEFENCE(new GameItem(2432), 30, 75, new GameItem(257), new GameItem(239)), 
	ENERGY(new GameItem(3008), 26, 67, new GameItem(255), new GameItem(1975)), 
	FISHING(new GameItem(2438), 50, 112, new GameItem(261), new GameItem(231)), 
	GUTHIX_BALANCE(new GameItem(7660), 22, 62, new GameItem(257), new GameItem(223), new GameItem(1550), new GameItem(7650)), 
	MAGIC(new GameItem(3040), 76, 172, new GameItem(2481), new GameItem(3138)), 
	PRAYER(new GameItem(2434), 38, 87, new GameItem(257), new GameItem(231)), 
	RANGING(new GameItem(2444), 72, 162, new GameItem(267), new GameItem(245)), 
	RESTORE(new GameItem(2430), 22, 62, new GameItem(255), new GameItem(223)), 
	SARADOMIN_BREW(new GameItem(6685), 81, 180, new GameItem(2998), new GameItem(6693)), 
	STRENGTH(new GameItem(113), 12, 50, new GameItem(253), new GameItem(225)), 
	SUPER_ANTIPOISON(new GameItem(2448), 48, 106, new GameItem(259), new GameItem(235)), 
	SUPER_ATTACK(new GameItem(2436), 45, 100, new GameItem(259), new GameItem(221)), 
	SUPER_COMBAT(new GameItem(12695), 90, 150, new GameItem(269), new GameItem(2436), new GameItem(2440), new GameItem(2442)), 
	SUPER_DEFENCE(new GameItem(2442), 66, 150, new GameItem(265), new GameItem(239)), 
	SUPER_ENERGY(new GameItem(3016), 52, 117, new GameItem(261), new GameItem(2970)), 
	SUPER_RESTORE(new GameItem(3024), 63, 142, new GameItem(3000), new GameItem(223)), 
	SUPER_STRENGTH(new GameItem(2440), 55, 125, new GameItem(263), new GameItem(225)), 
	WEAPON_POISON(new GameItem(187), 60, 137, new GameItem(263), new GameItem(241)), 
	WEAPON_POISON_PLUS(new GameItem(5937), 73, 165, new GameItem(6124), new GameItem(6016), new GameItem(223)), 
	WEAPON_POISON_PLUS_PLUS(new GameItem(5940), 82, 190, new GameItem(5935), new GameItem(2398), new GameItem(6018)), 
	ZAMORAK_BREW(new GameItem(2450), 78, 175, new GameItem(269), new GameItem(247));

	/**
	 * The primary ingredient required
	 */
	private final GameItem primary;

	/**
	 * An array of {@link GameItem} objects that represent the ingredients
	 */
	private final GameItem[] ingredients;

	/**
	 * The item received from combining the ingredients
	 */
	private final GameItem result;

	/**
	 * The level required to make this potion
	 */
	private final int level;

	/**
	 * The experience gained from making this potion
	 */
	private final int experience;

	/**
	 * Creates a new in-game potion that will be used in herblore
	 * 
	 * @param result the result from combining ingredients
	 * @param level the level required
	 * @param experience the experience
	 * @param ingredients the ingredients to make the result
	 */
	private Potion(GameItem result, int level, int experience, GameItem primary, GameItem... ingredients) {
		this.result = result;
		this.level = level;
		this.experience = experience;
		this.primary = primary;
		this.ingredients = ingredients;
	}

	/**
	 * The result from combining the ingredients
	 * 
	 * @return the result
	 */
	public GameItem getResult() {
		return result;
	}

	/**
	 * The level required to combine the ingredients
	 * 
	 * @return the level required
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * The total amount of experience gained in the herblore skill
	 * 
	 * @return the experience gained
	 */
	public int getExperience() {
		return experience;
	}

	/**
	 * An array of {@link GameItem} objects that represent the ingredients required to create this potion.
	 * 
	 * @return the ingredients required
	 */
	public GameItem[] getIngredients() {
		return ingredients;
	}

	/**
	 * The primary ingredient required for the potion
	 * 
	 * @return the primary ingredient
	 */
	public GameItem getPrimary() {
		return primary;
	}

}