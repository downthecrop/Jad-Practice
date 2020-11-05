package ethos.model.minigames.bounty_hunter;

import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import ethos.model.players.Player;

public enum BountyHunterEmblem {
	TIER_1(12746, 50_000, 0), TIER_2(12748, 100_000, 1), TIER_3(12749, 200_000, 2), TIER_4(12750, 400_000, 3), TIER_5(12751, 750_000, 4), TIER_6(12752, 1_250_000, 5), TIER_7(12753,
			1_750_000, 6), TIER_8(12754, 2_500_000, 7), TIER_9(12755, 3_500_000, 8), TIER_10(12756, 5_000_000, 9);

	private final int itemId;
	private final int bounties;
	private final int index;

	private BountyHunterEmblem(int itemId, int bounties, int index) {
		this.itemId = itemId;
		this.bounties = bounties;
		this.index = index;
	}

	public int getItemId() {
		return itemId;
	}

	public int getBounties() {
		return bounties;
	}

	public int getIndex() {
		return index;
	}

	/**
	 * Retrieves the next element in the enum, if it cannot it returns the last
	 * 
	 * @return the next or last element
	 */
	public BountyHunterEmblem getNextOrLast() {
		return valueOf(index + 1).orElse(TIER_10);
	}

	/**
	 * Retrieves the previous element in the enum, if it cannot it returns the first
	 * 
	 * @return the previous or first element
	 */
	public BountyHunterEmblem getPreviousOrFirst() {
		return valueOf(index - 1).orElse(TIER_1);
	}

	/**
	 * A set containing the BountyHunterEmblem elements.
	 */
	public static final Set<BountyHunterEmblem> EMBLEMS = Collections.unmodifiableSet(EnumSet.allOf(BountyHunterEmblem.class));

	/**
	 * The element at the specified index
	 * 
	 * @param index the index of the element
	 * @return the element
	 */
	public static Optional<BountyHunterEmblem> valueOf(int index) {
		return EMBLEMS.stream().filter(emblem -> emblem.index == index).findFirst();
	}

	/**
	 * Compares the two emblems and determiens which of the two are 'better' based on the item identification value.
	 */
	static final Comparator<BountyHunterEmblem> BEST_EMBLEM_COMPARATOR = (first, second) -> Integer.compare(first.itemId, second.itemId);

	/**
	 * Attempts to retrieve the best emblem the player has. The exclude parameter although may look out of place, allows us to determine if the TIER_10 element should be excluded
	 * when determining the best. This is due to the two scenarios in which we use this method. One when upgrading the emblem after killing a player, and the other when dropping
	 * the emblem on death.
	 * 
	 * @param player the player this is determined for
	 * @param exclude true if we are to exclude the TIER_10 element from the list
	 * @return the best possible emblem
	 */
	public static Optional<BountyHunterEmblem> getBest(Player player, boolean exclude) {
		List<BountyHunterEmblem> emblems = EMBLEMS.stream().filter(exclude(player, exclude)).collect(Collectors.toList());

		if (emblems.isEmpty()) {
			return Optional.empty();
		}

		return emblems.stream().max(BEST_EMBLEM_COMPARATOR);
	}

	/**
	 * Under certain circumstances we may want to exclude the tier 10 emblem from the possible stream of tiers we're trying to obtain. This is the case when dropping an emblem.
	 * 
	 * @param player the player that contains the emblem
	 * @param exclude true if the emblem is to be excluded, otherwise false
	 * @return a positive test if the player has the item and the other condition is met
	 */
	private static Predicate<BountyHunterEmblem> exclude(Player player, boolean exclude) {
		return emblem -> player.getItems().playerHasItem(emblem.getItemId()) && (!exclude || exclude && !emblem.equals(TIER_10));
	}
}
