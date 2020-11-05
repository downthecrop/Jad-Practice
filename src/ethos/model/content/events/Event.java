package ethos.model.content.events;

import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;

public class Event implements Runnable {
	
	Player player;
	int id, type;
	String name;
	
	public Event(Player player, int id, int type, String name) {
		this.player = player;
		this.id = id;
		this.type = type;
		this.name = name;
		this.update(id, name);
	}
	
	public void update(int type, String name) {
		String getEventNameByType = null;
		switch (type) {
		case 1:
			getEventNameByType = "Event";
			break;
		}
		if (getEventNameByType == null) {
			return;
		}
		player.getPA().sendFrame126("@or1@" + getEventNameByType + ": @yel@" + name , 29166);
	}
	
	public void run() {
		
	}
}
