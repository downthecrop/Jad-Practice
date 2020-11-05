package ethos.model.players.skills.woodcutting;

public enum Tree {
	NORMAL(new int[] { 1276, 1278, 1279 }, 1342, 1511, 1, 5, 20, 25, 15, 12000), 
	OAK(new int[] { 1751, 11756 }, 1356, 1521, 15, 8, 50, 38, 25, 11500), 
	WILLOW(new int[] { 1756, 1760, 1750, 1758, 11759, 11761, 11763, 11755 }, 1356, 1519, 30, 10, 60, 68, 35, 11000), 
	TEAK(new int[] { 9036 }, 1356, 6333, 35, 10, 65, 68, 35, 10500), 
	MAPLE(new int[] { 1759, 11762 }, 1356, 1517, 45, 13, 75, 100, 45, 10000), 
	ARCTIC_PINE(new int[] { 3037 }, 1356, 10810, 54, 14, 85, 100, 50, 90400),
	YEW(new int[] { 1753, 1754, 11758, 27255 }, 1356, 1515, 60, 15, 100, 175, 60, 9000), 
	MAGIC(new int[] { 1761, 11764 }, 9713, 1513, 75, 20, 125, 250, 75, 8600), 
	REDWOOD(new int[] { 29668, 29670 }, 29669, 19669, 90, 35, 150, 380, 150, 8400),
	SAPLING(new int[] { 29763 }, 29764, 20799, 65, 13, 75, 25, 15, 100000);

	private int[] treeIds;
	private int stumpId, wood, levelRequired, chopsRequired, deprecationChance, respawn, petChance;
	private double experience;

	private Tree(int[] treeIds, int stumpId, int wood, int levelRequired, int chopsRequired, int deprecationChance, double experience, int respawn, int petChance) {
		this.treeIds = treeIds;
		this.stumpId = stumpId;
		this.wood = wood;
		this.levelRequired = levelRequired;
		this.experience = experience;
		this.deprecationChance = deprecationChance;
		this.chopsRequired = chopsRequired;
		this.respawn = respawn;
		this.petChance = petChance;
	}

	public int[] getTreeIds() {
		return treeIds;
	}

	public int getStumpId() {
		return stumpId;
	}

	public int getWood() {
		return wood;
	}

	public int getLevelRequired() {
		return levelRequired;
	}

	public int getChopsRequired() {
		return chopsRequired;
	}

	public int getChopdownChance() {
		return deprecationChance;
	}

	public double getExperience() {
		return experience;
	}

	public int getRespawnTime() {
		return respawn;
	}
	
	public int getPetChance() {
		return petChance;
	}

	public static Tree forObject(int objectId) {
		for (Tree tree : values()) {
			for (int treeId : tree.treeIds) {
				if (treeId == objectId) {
					return tree;
				}
			}
		}
		return null;
	}

}
