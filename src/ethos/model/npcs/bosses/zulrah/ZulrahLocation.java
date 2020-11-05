package ethos.model.npcs.bosses.zulrah;

import java.awt.Point;

public enum ZulrahLocation {
	NORTH(new Point(2266, 3072)), SOUTH(new Point(2267, 3064)), WEST(new Point(2258, 3072)), EAST(new Point(2277, 3072));

	private final Point location;

	private ZulrahLocation(Point location) {
		this.location = location;
	}

	public Point getLocation() {
		return location;
	}

}
