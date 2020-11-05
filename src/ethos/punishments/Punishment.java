package ethos.punishments;

import java.util.Arrays;

public class Punishment {

	/**
	 * The type of punishment
	 */
	private final PunishmentType type;

	/**
	 * The duration of the punishment
	 */
	private long duration;

	/**
	 * An array of information that pertains to this punishment
	 */
	private final String[] data;

	/**
	 * A new punishment with some information
	 * 
	 * @param type the type of punishment
	 * @param data the group of data
	 */
	public Punishment(PunishmentType type, long duration, String... data) {
		this.type = type;
		this.duration = duration;
		this.data = data;
	}

	/**
	 * Determines if any information from the punishment matches the information given
	 * 
	 * @param information the information given
	 * @return {@code true} if any of the information matches.
	 */
	public boolean contains(String information) {
		return Arrays.stream(data).anyMatch(s -> s.equalsIgnoreCase(information));
	}

	/**
	 * The duration of the punishment
	 * 
	 * @return the duration
	 */
	public long getDuration() {
		return duration;
	}

	/**
	 * The data or information about this punishment
	 * 
	 * @return the data
	 */
	public String[] getData() {
		return data;
	}

	/**
	 * The type of punishment
	 * 
	 * @return the type
	 */
	public PunishmentType getType() {
		return type;
	}

	public String toString() {
		return Arrays.toString(data);
	}

}
