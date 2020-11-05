package ethos.model.players.combat;

import ethos.util.Misc;

public enum CombatType {

	MELEE, RANGE, MAGE, DRAGON_FIRE, SPECIAL;

	/**
	 * Retrieves a random {@link CombatType} from the specified {@code types} array.
	 * 
	 * @param types the array of types that could be returned
	 * @return a random {@code CombatType} from the array
	 */
	public static CombatType getRandom(CombatType... types) {
		if (types.length == 1) {
			return types[0];
		}
		return Misc.randomSearch(types, 0, types.length - 1);
	}

}
