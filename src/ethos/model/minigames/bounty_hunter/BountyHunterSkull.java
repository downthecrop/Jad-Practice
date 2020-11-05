package ethos.model.minigames.bounty_hunter;

/**
 * Represents the skull on the bounty hunter interface. The risk is the amount in gold coins the player must be risking to obtain the skull.
 * 
 * @author Jason MacKeigan
 * @date Nov 14, 2014, 2:11:19 PM
 */
public enum BountyHunterSkull {

	NONE(-1, 28030, "None"), BRONZE(0, 28032, "V. Low"), SILVER(1_000_000, 28034, "Low"), GREEN(3_000_000, 28036, "Medium"), BLUE(6_000_000, 28038, "High"), RED(10_000_000, 28040,
			"V. High");

	private int risk, frameId;
	private String representation;

	private BountyHunterSkull(int risk, int frameId, String representation) {
		this.risk = risk;
		this.frameId = frameId;
		this.representation = representation;
	}

	public int getRisk() {
		return risk;
	}

	public int getFrameId() {
		return frameId;
	}

	public String getRepresentation() {
		return representation;
	}

	public static BountyHunterSkull getSkull(int risk) {
		BountyHunterSkull skull = BountyHunterSkull.BRONZE;
		for (int i = BountyHunterSkull.BRONZE.ordinal(); i < BountyHunterSkull.values().length; i++) {
			int req = BountyHunterSkull.values()[i].risk;
			if (skull.risk < req && risk >= req) {
				skull = BountyHunterSkull.values()[i];
			}
		}
		return skull;
	}

}
