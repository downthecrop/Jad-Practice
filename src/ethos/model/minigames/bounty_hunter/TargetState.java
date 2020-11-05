package ethos.model.minigames.bounty_hunter;

/**
 * 
 * @author Jason MacKeigan
 * @date Nov 13, 2014, 2:56:17 PM
 */
public enum TargetState {

	NONE, PENALIZED, RECENT_TARGET_KILL, SELECTING, SELECTED;

	/**
	 * Determines if the target state is set to none
	 * 
	 * @return true if the state of the target is none
	 */
	public boolean isNone() {
		return equals(NONE);
	}

	/**
	 * Determines if the target state is delayed
	 * 
	 * @return true if the target has receive too many warnings
	 */
	public boolean isPenalized() {
		return equals(PENALIZED);
	}

	/**
	 * Determines if the target is still selecting another target
	 * 
	 * @return true if the target is still selecting another
	 */
	public boolean isSelecting() {
		return equals(SELECTING);
	}

	/**
	 * Determines if the player already has a target selected
	 * 
	 * @return true if the target is selected
	 */
	public boolean isSelected() {
		return equals(SELECTED);
	}

	/**
	 * Determines if the playerh as killed another target recently
	 * 
	 * @return true if they have, otherwise false
	 */
	public boolean hasKilledRecently() {
		return equals(RECENT_TARGET_KILL);
	}

}
