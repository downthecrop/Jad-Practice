package ethos.util;

public class Location3D {

	private int x, y, z;

	/**
	 * A representation of a location on a 3-dimenionsal map.
	 * 
	 * @param x the x position on the map
	 * @param y the y position on the map
	 * @param z the z position, or height, on the map
	 */
	public Location3D(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Retrieves the x position of this location
	 * 
	 * @return the position on the x-axis
	 */
	public int getX() {
		return x;
	}

	/**
	 * Retrieves the y position of this location
	 * 
	 * @return the position on the y-axis
	 */
	public int getY() {
		return y;
	}

	/**
	 * Retrieves the position on the z-axis
	 * 
	 * @return the position on the z-axis
	 */
	public int getZ() {
		return z;
	}

}
