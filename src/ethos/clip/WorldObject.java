package ethos.clip;

public class WorldObject {

	public int x, y, height, id, type, face;

	public WorldObject() {

	}

	public WorldObject(int id, int x, int y, int height) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.height = height;
	}

	public WorldObject(int id, int x, int y, int height, int face) {
		this(id, x, y, height);
		this.face = face;
	}

	public int getId() {
		return id;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getHeight() {
		return height;
	}

	public int getFace() {
		return face;
	}

}