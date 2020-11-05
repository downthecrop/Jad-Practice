package ethos.model.content.instances;

import ethos.model.players.Boundary;
import ethos.model.players.Player;

public class SingleInstancedArea extends InstancedArea {

	/**
	 * The player in this single instanced area
	 */
	protected Player player;

	/**
	 * Creates a new single instanced area for a player
	 * 
	 * @param boundary the boundary of the instanced area
	 * @param height the height of the instanced area
	 */
	public SingleInstancedArea(Boundary boundary, int height) {
		super(boundary, height);
	}

	/**
	 * Creates a new single instanced area for a player
	 * 
	 * @param player the player in the instanced area
	 * @param boundary the boundary of the instanced area
	 * @param height the height of the instanced area
	 */
	public SingleInstancedArea(Player player, Boundary boundary, int height) {
		super(boundary, height);
		this.player = player;
	}

	/**
	 * The player for this instanced area
	 * 
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}

	@Override
	public void onDispose() {

	}
}
