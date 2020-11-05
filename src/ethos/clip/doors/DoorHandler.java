package ethos.clip.doors;

import ethos.model.players.Player;

public class DoorHandler {

	private static final int WEST = 0;
	private static final int NORTH = 1;
	private static final int EAST = 2;
	private static final int SOUTH = 3;

	public static boolean clickDoor(Player player, DoorDefinition door) {
		int xOffset = -1;
		int yOffset = -1;
		if (player.getCoordinate().getDistance(door.getCoordinate()) > 1) {
			return false;
		}
		switch (door.getFace()) {
		case WEST:
			yOffset = 0;
			if (player.getX() == door.getX()) {
				xOffset = -1;
			} else if (player.getX() == door.getX() - 1) {
				xOffset = 0;
			} else {
				return false;
			}
			break;
		case NORTH:
			xOffset = 0;
			if (player.getY() == door.getY()) {
				yOffset = 1;
			} else if (player.getY() == door.getY() + 1) {
				yOffset = 0;
			} else {
				return false;
			}
			break;
		case EAST:
			yOffset = 0;
			if (player.getX() == door.getX()) {
				xOffset = 1;
			} else if (player.getX() == door.getX() + 1) {
				xOffset = 0;
			} else {
				return false;
			}
			break;
		case SOUTH:
			xOffset = 0;
			if (player.getY() == door.getY()) {
				yOffset = -1;
			} else if (player.getY() == door.getY() - 1) {
				yOffset = 0;
			} else {
				return false;
			}
			break;
		}
		player.getPA().movePlayer(door.getX() + xOffset, door.getY() + yOffset, door.getH());
		return true;
	}

}
