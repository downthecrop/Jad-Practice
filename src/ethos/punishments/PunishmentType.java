package ethos.punishments;

public enum PunishmentType {
	MUTE(0, "mute.dat"), BAN(1, "ban.dat"), NET_MUTE(2, "network_mute.dat"), NET_BAN(3, "network_ban.dat"), MAC_BAN(4, "mac_ban.dat");

	/**
	 * The identification value associated with this type of punishment
	 */
	private final int id;

	/**
	 * The name of the file associated with the type of punishment
	 */
	private final String fileName;

	/**
	 * Creates a new type of punishment associated with a file to log data
	 * 
	 * @param file the name of the file
	 */
	private PunishmentType(int id, String file) {
		this.id = id;
		this.fileName = file;
	}

	/**
	 * The name of the file
	 * 
	 * @return the name
	 */
	public final String getFileName() {
		return fileName;
	}

	/**
	 * The id associated with this type
	 * 
	 * @return the id
	 */
	public final int getId() {
		return id;
	}
}
