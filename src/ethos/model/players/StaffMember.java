package ethos.model.players;

public class StaffMember {

	/**
	 * The name of the member of staff
	 */
	private final String name;

	/**
	 * The level of rights the member of staff has
	 */
	private final Right rights;

	/**
	 * An array of {@link Player#getMacAddress()}'s that the player connects from
	 */
	private final String[] addresses;

	/**
	 * A new member of staff with some established information about them
	 * 
	 * @param name the name of the staff member
	 * @param rights the level of rights of the staff member
	 * @param addresses an array of mac addresses the staff member connects from
	 */
	public StaffMember(String name, Right rights, String[] addresses) {
		this.name = name;
		this.rights = rights;
		this.addresses = addresses;
	}

	/**
	 * The name of the staff member
	 * 
	 * @return the {@link Player#playerName} of the staff member
	 */
	public String getName() {
		return name;
	}

	/**
	 * The level of rights the staff member has
	 * 
	 * @return the level of rights
	 */
	public Right getRights() {
		return rights;
	}

	/**
	 * The mac address that a staff member is permitted to log into
	 * 
	 * @return the staff members mac addresses
	 */
	public String[] getAddresses() {
		return addresses;
	}

}
