package ethos.model.players;

public final class Position {

	private final int x;
	
	private final int y;
	
	private final int height;
	
	public Position(int x, int y, int height) {
		this.x = x;
		this.y = y;
		this.height = height;
	}
	
	public Position(int x, int y) {
		this(x, y, 0);
	}

	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}
	
	/**
	 * Checks if this position is within distance of another position.
	 * 
	 * @param position
	 *            the position to check the distance for.
	 * @param distance
	 *            the distance to check.
	 * @return true if this position is within the distance of another position.
	 */
	public boolean withinDistance(Position position, int distance) {
		if (this.height != position.height)
			return false;

		return Math.abs(position.getX() - this.getX()) <= distance && Math.abs(position.getY() - this.getY()) <= distance;
	}
	
	
}
