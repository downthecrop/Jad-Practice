package ethos.model.players.skills.fletching;

public enum FletchableLogGroup {
	NORMAL(FletchableLog.SHORTBOW, FletchableLog.LONGBOW, FletchableLog.ARROW_SHAFT), 
	MAGIC(FletchableLog.MAGIC_SHORTBOW, FletchableLog.MAGIC_LONGBOW, FletchableLog.MAGIC_SHAFT), 
	YEW(FletchableLog.YEW_SHORTBOW, FletchableLog.YEW_LONGBOW, FletchableLog.YEW_SHAFT), 
	MAPLE(FletchableLog.MAPLE_SHORTBOW, FletchableLog.MAPLE_LONGBOW, FletchableLog.MAPLE_SHAFT), 
	WILLOW(FletchableLog.WILLOW_SHORTBOW, FletchableLog.WILLOW_LONGBOW, FletchableLog.WILLOW_SHAFT), 
	OAK(FletchableLog.OAK_SHORTBOW, FletchableLog.OAK_LONGBOW, FletchableLog.OAK_SHAFT);

	private FletchableLog[] fletchables;

	private FletchableLogGroup(FletchableLog... fletchables) {
		this.fletchables = fletchables;
	}

	public FletchableLog[] getFletchables() {
		return fletchables;
	}

}
