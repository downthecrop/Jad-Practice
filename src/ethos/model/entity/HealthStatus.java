package ethos.model.entity;

import org.apache.commons.lang3.text.WordUtils;

import com.google.common.base.Preconditions;

/**
 * An enumeration of elements that represents the Entities state of health.
 * 
 * <p>
 * Elements such as {@code #POISONED} and {@code #VENOMED} both effect the entities health substantially in different ways. If the status is {@code #NORMAL} it's safe to say
 * nothing is effecting it.
 * </p>
 */
public enum HealthStatus {
	NORMAL(0), POISON(1), VENOM(2);

	private final byte mask;

	private HealthStatus(final int mask) {
		Preconditions.checkArgument(mask > -1 && mask <= Byte.MAX_VALUE, "mask [" + mask + "] is less than or greater than required.");
		this.mask = (byte) mask;
	}

	/**
	 * Returns the mask id for the status
	 * 
	 * @return the id
	 */
	public byte getMask() {
		return mask;
	}

	/**
	 * Determines if this {@link HealthStatus} instance is equal to {@code #NORMAL}.
	 * 
	 * @return {@code true} if the status of the entities health is {@code #NORMAL}.
	 */
	public boolean isNormal() {
		return this == NORMAL;
	}

	/**
	 * Determines if this {@link HealthStatus} instance is equal to {@code #POISONED}.
	 * 
	 * @return {@code true} if the status of the entities health is {@code #POISONED}.
	 */
	public boolean isPoisoned() {
		return this == POISON;
	}

	/**
	 * Determines if this {@link HealthStatus} instance is equal to {@code #VENOMED}.
	 * 
	 * @return {@code true} if the status of the entities health is {@code #VENOMED}.
	 */
	public boolean isVenomed() {
		return this == VENOM;
	}

	@Override
	public String toString() {
		return WordUtils.capitalizeFully(name());
	}

}
