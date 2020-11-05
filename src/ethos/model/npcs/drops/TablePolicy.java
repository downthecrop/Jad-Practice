package ethos.model.npcs.drops;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

/**
 * The {@link TablePolicy} enumeration is made up of elements where the {@link #name()} of each element represents a {@link Table}'s accessibility.
 * <p>
 * Each element does not have a value to determine it's accessibility, that is chosen by the designer when creating a particular table for a non-playable character. Although there
 * are no constraints on the accessibility, it is assumed that the accessibility increases in the same chronological order as the elements in this enumeration.
 * </p>
 */
public enum TablePolicy {
	CONSTANT, COMMON, UNCOMMON, RARE, VERY_RARE;

	/**
	 * A {@link Set} of elements from the {@link TablePolicy} enumeration.
	 */
	public static final Set<TablePolicy> POLICIES = Collections.unmodifiableSet(EnumSet.allOf(TablePolicy.class));
}
