package ethos.model.players.skills.farming;

import ethos.model.items.ItemAssistant;

/**
 * 
 * @author Jason http://www.rune-server.org/members/jason
 * @date Oct 27, 2013
 */
public class FarmingHerb {

	public enum Herb {
		//Not 100% sure of these times going off what was already there...
		GUAM(5291, 199, 100, 30, 1, 50, 8000), //.50 mins - 30 seconds
		MARRENTIL(5292, 201, 200, 50, 7, 50, 7800),
		TARROMIN(5293, 203, 300, 100, 19, 50, 7600),
		HARRALANDER(5294, 205, 500, 150, 26, 50, 7400), 
		RANARR(5295, 207, 600, 200, 32, 125, 7200), // 1.25 mins - 75 seconds
		TOADFLAX(5296, 3049, 1000, 250, 38, 125, 7000), 
		IRIT(5297, 209, 1250, 300, 44, 125, 6800), 
		AVANTOE(5298, 211, 1500, 350, 50, 125, 6600),
		KWUARM(5299, 213, 1750, 375, 56, 125, 6400), 
		SNAP_DRAGON(5300, 3051, 2000, 400, 62, 250, 6200), //2.5 mins - 150 seconds
		CADANTINE(5301, 215, 2250, 425, 67, 250, 6000), 
		LANTADYME(5302, 2485, 2300, 450, 73, 250, 5800), 
		DWARF_WEED(5303, 217, 2400, 475, 79, 250, 5600), 
		TORSTOL(5304, 219, 2500, 500, 85, 500, 5000); // 5 mins - 300 seconds

		int seedId, grimyId, plantXp, harvestXp, levelRequired, time, petChance;

		Herb(int seedId, int grimyId, int plantXp, int harvestXp, int levelRequired, int time, int petChance) {
			this.seedId = seedId;
			this.grimyId = grimyId;
			this.plantXp = plantXp;
			this.harvestXp = harvestXp;
			this.levelRequired = levelRequired;
			this.time = time;
			this.petChance = petChance;
		}

		public int getSeedId() {
			return seedId;
		}

		public int getGrimyId() {
			return grimyId;
		}

		public int getPlantingXp() {
			return plantXp;
		}

		public int getHarvestingXp() {
			return harvestXp;
		}

		public int getLevelRequired() {
			return levelRequired;
		}

		public int getGrowthTime() {
			return time;
		}

		public String getSeedName() {
			return ItemAssistant.getItemName(seedId);
		}

		public String getGrimyName() {
			return ItemAssistant.getItemName(grimyId);
		}
		public int getPetChance() {
			return petChance;
		}

	}

	public static Herb getHerbForSeed(int seedId) {
		for (Herb h : Herb.values())
			if (h.getSeedId() == seedId)
				return h;
		return null;
	}

	public static Herb getHerbForGrimy(int grimyId) {
		for (Herb h : Herb.values())
			if (h.getGrimyId() == grimyId)
				return h;
		return null;
	}

}
