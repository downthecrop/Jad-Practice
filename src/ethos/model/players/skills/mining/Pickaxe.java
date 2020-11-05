package ethos.model.players.skills.mining;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

import ethos.model.players.Player;
import ethos.model.players.skills.Skill;

/**
 * An enumeration of mining pickaxes.
 * 
 * @author Jason MacKeigan
 * @date Feb 18, 2015, 5:47:35 PM
 */
public enum Pickaxe {
		BRONZE(0, 1265, 1, 6747, 0), 
		IRON(1, 1267, 1, 6748, 2), 
		STEEL(2, 1269, 6, 6749, 3), 
		BLACK(3, 12297, 15, 6751, 5), 
		MITHRIL(4, 1273, 21, 6751, 5), 
		ADAMANT(5, 1271, 31, 6750, 7), 
		RUNE(6, 1275, 41, 6746, 10), 
		DRAGON(7, 11920, 61, 7139, 13), 
		DRAGON_OR(8, 12797, 61, 335, 13), 
		INFERNAL(9, 13243, 61, 4482, 13), 
		THIRD_AGE(10, 20014, 61, 7283, 13);

	/**
	 * The priority of the pickaxe. The higher the value, the higher the priority. This serves as a replacement to the {@link Enum.ordinal()} function.
	 */
	private final int priority;

	/**
	 * The item identification value of the pickaxe
	 */
	private final int itemId;

	/**
	 * The level required to operate the pickaxe
	 */
	private final int level;

	/**
	 * The animation displayed when the pickaxe is being operated
	 */
	private final int animation;

	/**
	 * The amount of cycles operating this pickaxe will reduce the time by
	 */
	private final int extractionReduction;

	/**
	 * Constructs a new {@link Pickaxe} object
	 * 
	 * @param itemId the item id value of the pickaxe
	 * @param level the level required to operate
	 * @param animation the animation displayed when used
	 */
	private Pickaxe(int priority, int itemId, int level, int animation, int extractionReduction) {
		this.priority = priority;
		this.itemId = itemId;
		this.level = level;
		this.animation = animation;
		this.extractionReduction = extractionReduction;
	}

	/**
	 * The item identification value of the pickaxe
	 * 
	 * @return the pickaxe id
	 */
	public int getItemId() {
		return itemId;
	}

	/**
	 * The level required to operate the pickaxe
	 * 
	 * @return the level to operate the pickaxe is always above 1 and cannot exceed 61
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * The animation displayed when the pickaxe is in use
	 * 
	 * @return the animation displayed
	 */
	public int getAnimation() {
		return animation;
	}

	/**
	 * The amount of reduction this pickaxe effects the extraction process
	 * 
	 * @return the reduction
	 */
	public int getExtractionReduction() {
		return extractionReduction;
	}

	/**
	 * The level of priority the pickaxe has.
	 * 
	 * @return the level of priority
	 */
	public int getPriority() {
		return priority;
	}

	/**
	 * A set of all pickaxes that will act as as constant
	 */
	private static final Set<Pickaxe> PICKAXES = Collections.unmodifiableSet(EnumSet.allOf(Pickaxe.class));

	/**
	 * Attempts to retrieve the best pickaxe the player has on their person.
	 * 
	 * @param player the player we're trying to get the best pickaxe of
	 * 
	 * @return the best pickaxe the person has will be returned, otherwise null to ensure the player has no pickaxe;
	 */
	public static Pickaxe getBestPickaxe(Player player) {
		Pickaxe pickaxe = null;
		for (Pickaxe pick : PICKAXES) {
			if (player.getItems().playerHasItem(pick.itemId) || player.getItems().isWearingItem(pick.itemId)) {
				if (player.playerLevel[Skill.MINING.getId()] >= pick.level) {
					if (pickaxe == null || pick.priority > pickaxe.priority) {
						pickaxe = pick;
					}
				}
			}
		}
		return pickaxe;
	}

}
