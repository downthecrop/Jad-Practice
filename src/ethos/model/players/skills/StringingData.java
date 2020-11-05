package ethos.model.players.skills;

/**
 * Class Stringing Data Contains the data for stringing.
 * 
 */

public class StringingData {

	/**
	 * This enum contains the stringing data. Unstrung ID, Strung ID, Level required, XP, Animation
	 */

	public static enum stringingData {
		SHORT_BOW(50, 841, 5, 50, 6678), 
		LONG_BOW(48, 839, 10, 100, 6684), 
		OAK_SHORT_BOW(54, 843, 20, 160, 6679), 
		OAK_LONG_BOW(56, 845, 25, 250, 6685), 
		COMPOSITE_BOW(4825, 4827, 30, 450, 6686), 
		WILLOW_SHORT_BOW(60, 849, 35, 330, 6680), 
		WILLOW_LONG_BOW(58, 847, 40, 410, 6686), 
		MAPLE_SHORT_BOW(64, 853, 50, 500, 6681), 
		MAPLE_LONG_BOW(62, 851, 55, 580, 6687), 
		YEW_SHORT_BOW(68, 857, 65, 680, 6682), 
		YEW_LONG_BOW(66, 855, 70, 750, 6688), 
		MAGIC_SHORT_BOW(72, 861, 80, 830, 6683), 
		MAGIC_LONG_BOW(70, 859, 85, 910, 6689);

		private int unstrung, strung, level, animation;
		private double xp;

		private stringingData(final int unstrung, final int strung, final int level, final double xp, final int animation) {
			this.unstrung = unstrung;
			this.strung = strung;
			this.level = level;
			this.xp = xp;
			this.animation = animation;
		}

		public int unStrung() {
			return unstrung;
		}

		public int Strung() {
			return strung;
		}

		public int getLevel() {
			return level;
		}

		public double getXP() {
			return xp;
		}

		public int getAnimation() {
			return animation;
		}
	}
}