package ethos.clip.doors;



/**
 * Represents a single location
 *
 * @author Michael Sasse
 *
 */
public class Location {
    private short x = 0;

    private short y = 0;

    private short z = 0;

    /**
     * Constructs an empty location
     */
    public Location() {
    }

    /**
     * Creates a new location with an x and y coordinate
     *
     * @param x
     * @param y
     */
    public Location(int x, int y) {
        this(x, y, 0);
    };

    /**
     * Constructs a new location
     *
     * @param x
     *            The x coordinate
     * @param y
     *            The y coordinate
     * @param z
     *            The z coordinate
     */
    public Location(int x, int y, int z) {
        this.x = ((short) x);
        this.y = ((short) y);
        this.z = ((short) z);
    }

    /**
     * Constructs a new location based on another location
     *
     * @param other
     *            The other location
     * @param z
     *            The z coordinate
     */
    public Location(Location other) {
        x = ((short) other.getX());
        y = ((short) other.getY());
        z = ((short) other.getZ());
    }

    /**
     * Constructs a new location based on another location
     *
     * @param other
     *            The other location
     * @param z
     *            The z coordinate
     */
    public Location(Location other, int z) {
        x = ((short) other.getX());
        y = ((short) other.getY());
        this.z = ((short) z);
    }

    @Override
    public int hashCode() {
        return x << 16 | y << 8 | z;
    }

    @Override
    public boolean equals(Object other) {
        if ((other instanceof Location)) {
            Location p = (Location) other;
            return (x == p.x) && (y == p.y) && (z == p.z);
        }
        return false;
    }

    /**
     * Gets the location x for this location
     *
     * @return
     */
    public int getLocalX() {
        return getLocalX(this);
    }

    /**
     * Gets the location x for the provided location
     *
     * @param base
     *            The location to get the local x for
     * @return
     */
    public int getLocalX(Location base) {
        return x - 8 * base.getRegionX();
    }

    /**
     * Gets the local y for this location
     *
     * @return
     */
    public int getLocalY() {
        return getLocalY(this);
    }

    /**
     * Gets the location y for the provided location
     *
     * @param base
     *            The location to get the local y for
     * @return
     */
    public int getLocalY(Location base) {
        return y - 8 * base.getRegionY();
    }

    /**
     * Gets the region x position
     *
     * @return
     */
    public int getRegionX() {
        return (x >> 3) - 6;
    }

    /**
     * Gets the region y position
     *
     * @return
     */
    public int getRegionY() {
        return (y >> 3) - 6;
    }

    /**
     * Gets the x coordinate
     *
     * @return
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the y coordinate
     *
     * @return
     */
    public int getY() {
        return y;
    }

    /**
     * Gets the z coordinate
     *
     * @return
     */
    public int getZ() {
        return z;
    }

    /**
     * The player is in the king black dragon area
     *
     * @return
     */
    public boolean inKingBlackDragonArea() {
        return x >= 2250 && x <= 2290 && y >= 4670 && y <= 4714;
    }

    /**
     * The player is in the king black dragon area
     *
     * @return
     */
    public boolean inBossEvent() {
        return x >= 3020 && x <= 3044 && y >= 3297 && y <= 3314 ||
                x >= 2738 && x <= 2765 && y >= 3411 && y <= 3440 ||
                x >= 3179 && x <= 3220 && y >= 3141 && y <= 3180;
    }



    /**
     * Moves the coordinate by the provided values
     *
     * @param amountX
     *            The amount of x coordinates to move
     * @param amountY
     *            The amount of y coordinates to move
     */
    public void move(int amountX, int amountY) {
        x = ((short) (x + amountX));
        y = ((short) (y + amountY));
    }

    /**
     * Sets this location as another location
     *
     * @param other
     */
    public void setAs(Location other) {
        x = other.x;
        y = other.y;
        z = other.z;
    }

    /**
     * Sets the y coordinate
     *
     * @param x
     */
    public void setX(int x) {
        this.x = ((short) x);
    }

    /**
     * Sets the y coordinate
     *
     * @param y
     */
    public void setY(int y) {
        this.y = ((short) y);
    }

    /**
     * Sets the z coordinate
     *
     * @param z
     */
    public Location setZ(int z) {
        this.z = ((short) z);
        return this;
    }

    @Override
    public String toString() {
        return "Location(" + x + ", " + y + ", " + z + ")";
    }

    public boolean inLocation(Location southWest, Location northEast, boolean inclusive) {
        return !inclusive ? this.x > southWest.getX() && this.x < northEast.getX() && this.y > southWest.getY() && this.y < northEast.getY() : this.x >= southWest.getX() && this.x <= northEast.getX() && this.y >= southWest.getY() && this.y <= northEast.getY();
    }

    public final boolean inBarrows() {
        return (x > 3539 && x < 3582 && y >= 9675 && y < 9722);
    }




}

