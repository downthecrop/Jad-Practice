package ethos.model.players.combat.monsterhunt;

public class MonsterHuntLocation {

	private int x;
	
	private int y;
	
	private String locationName;
		
	public MonsterHuntLocation(int x, int y, String locationName) {
		this.x = x;
		this.y = y;
		this.locationName = locationName;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
}