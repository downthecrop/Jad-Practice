package ethos.model.players.combat.range;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public enum Javelin {

	BRONZE(882, 883, 5616, 5622), 
	IRON(884, 885, 5617, 5623), 
	STEEL(886, 887, 5618, 5624), 
	MITHRIL(888, 889, 5619, 5625), 
	BROAD(4160, -1, -1, -1), 
	ADAMANT(890, 891, 5620, 5626), 
	RUNE(892, 893, 5621, 5627),
	AMETHYST(21318, 21320, 21322, 21324), 
	DRAGON(11212, 11227, 11228, 11229);

	private List<Integer> javelinIds;

	/**
	 * The order in which arrows can be used by bows. If a bow can use a certain arrow, it must also be able to use all arrows with a lower index in the array.
	 */
	private static Javelin[] order = { BRONZE, IRON, STEEL, MITHRIL, BROAD, ADAMANT, RUNE, AMETHYST, DRAGON };

	Javelin(Integer... javelinIds) {
		this.javelinIds = Arrays.asList(javelinIds);
	}

	/**
	 * Unmodifiable Set of the enum.
	 */
	private static final Set<Javelin> VALUES = Collections.unmodifiableSet(EnumSet.allOf(Javelin.class));

	/**
	 * Turns an item id into a Arrow Object, in case it's present.
	 * 
	 * @param itemId The id of the arrow we want.
	 * @return The Arrow Object.
	 */
	public static Optional<Javelin> getJavelin(int itemId) {
		return VALUES.stream().filter(javelin -> javelin.javelinIds.contains(itemId)).findFirst();
	}

	/**
	 * Test whether a given item id matches a certain material.
	 * 
	 * @param itemId Item id of the arrow.
	 * @param material The material.
	 * @return True if they equal, false if the given item id is not an arrow or if they don't match.
	 */
	public static boolean matchesMaterial(int itemId, Javelin material) {
		Optional<Javelin> javelin = getJavelin(itemId);
		if (javelin.isPresent()) {
			return javelin.get() == material;
		}
		return false;
	}

	/**
	 * Finds the index of a given arrow in {@link #order}.
	 * 
	 * @param arrow The arrow of which we want to find the index.
	 * @return The index of a given arrow in the {@link #order} array.
	 * @throws IllegalArgumentException Thrown in case the arrow hasn't been added to {@link #order}.
	 */
	public static int indexOf(Javelin javelin) throws IllegalArgumentException {
		for (int i = 0; i < order.length; i++) {
			if (order[i] == javelin) {
				return i;
			}
		}
		throw new IllegalArgumentException();
	}

}
