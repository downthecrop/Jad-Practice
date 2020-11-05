package ethos.model.multiplayer_session;

import ethos.model.players.Player;

/**
 * @author Jason MacKeigan
 * @date Oct 19, 2014, 8:03:05 PM
 */
public abstract class Multiplayer {
	/**
	 * The last time, in milliseconds, that you accepted a trade.
	 */
	protected long lastAccept;

	/**
	 * Player associated with trading operations
	 */
	protected Player player;

	/**
	 * Constructs a new class for managing trade operations
	 * 
	 * @param player
	 */
	public Multiplayer(Player player) {
		this.player = player;
	}

	public abstract boolean requestable(Player request);

	public abstract void request(Player requested);

	/**
	 * The last time in millisecods that you traded
	 * 
	 * @return the last time in milliseconds
	 */
	public long getLastAccept() {
		return lastAccept;
	}

	/**
	 * Records the last time you accepted a trade
	 * 
	 * @param lastAccept the last time in milliseconds
	 */
	public void setLastAccept(long lastAccept) {
		this.lastAccept = lastAccept;
	}
}
