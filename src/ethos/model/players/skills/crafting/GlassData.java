package ethos.model.players.skills.crafting;

public class GlassData {
	public static enum glassData {		
	
	BEER_GLASS(new int[][] {{48112, 1}, {48111, 5}, {48110, 10}, {48109, 28}}, 1919, 1, 1, 19.5),
	CANDLE_LANTERN(new int[][] {{48116, 1}, {48115, 5}, {48114, 10}, {48113, 28}}, 4527, 1, 4, 19.0),
	OIL_LAMP(new int[][] {{48120, 1}, {48119, 5}, {48118, 10}, {48117, 28}}, 4522, 1, 12, 25.0),
	VIAL(new int[][] {{44210, 1}, {44209, 5}, {44208, 10}, {44207, 28}}, 229, 1, 33, 35),
	FISHBOWL(new int[][] {{24059, 1}, {24058, 5}, {24057, 10}, {24056, 28}}, 6667, 1, 42, 42.5),
	GLASS_ORB(new int[][] {{48108, 1}, {48107, 5}, {48106, 10}, {44211, 28}}, 567, 1, 46, 52.5),
	BULLSEYE_LANTERN_LENS(new int[][] {{48124, 1}, {48123, 5}, {48122, 10}, {48121, 28}}, 4542, 1, 49, 55.0);
	
	private int[][] buttonId;
	private int newId, needed, levelReq;
	private double xp;
	
	private glassData(final int[][] buttonId, final int newId, final int needed, final int levelReq, final double xp) {
		this.buttonId = buttonId;
		this.newId = newId;
		this.needed = needed;
		this.levelReq = levelReq;
		this.xp = xp;
	}
	
	public int getButtonId(final int button) {
		for (int i = 0; i < buttonId.length; i++) {
			if (button == buttonId[i][0]) {
				return buttonId[i][0];
			}
		}
		return -1;
	}
	
	public int getAmount(final int button) {
		for (int i = 0; i < buttonId.length; i++) {
			if (button == buttonId[i][0]) {
				return buttonId[i][1];
			}
		}
		return -1;
	}

	public int getNewId() {
		return newId;
	}
	
	public int getNeeded() {
		return needed;
	}
	
	public int getLevelReq() {
		return levelReq;
	}

	public double getXP() {
		return xp;
	}
}
}
