package ethos.model.content.explock;

import ethos.model.players.Player;

public class ExpLock {

	public Player player;
	
	public ExpLock(Player player) {
		this.player = player;
	}
	
	public void OpenExpLock() { // Refreshes all text lines before showing the interface - Looks better
		for (int j = 0; j < 7; j++) {
			if (player.skillLock[j]) {
				player.getPA().sendFrame126("@red@Locked", 37536 + j); //Locked skill update text
				player.getPA().sendFrame126("@red@"+player.getPA().getLevelForXP(player.playerXP[j])+"", 37544 + j); // Update skill level text
			} else {
				player.getPA().sendFrame126("@gre@Unlocked", 37536 + j); //Locked skill update text
				player.getPA().sendFrame126("@gre@"+player.getPA().getLevelForXP(player.playerXP[j])+"", 37544 + j); // Update skill level text
			}
		}
		
		player.getPA().showInterface(37500);
	}
	
	public void ToggleLock(int i) { // Refreshes all text lines before showing the interface - Looks better
		if (!player.skillLock[i]) {
			player.skillLock[i] = true;
			player.getPA().sendFrame126("@red@Locked", 37536 + i); //Locked skill update text
			player.getPA().sendFrame126("@red@"+player.getPA().getLevelForXP(player.playerXP[i])+"", 37544 + i); // Update skill level text
		} else {
			player.skillLock[i] = false;
			player.getPA().sendFrame126("@gre@Unlocked", 37536 + i); //Locked skill update text
			player.getPA().sendFrame126("@gre@"+player.getPA().getLevelForXP(player.playerXP[i])+"", 37544 + i); // Update skill level text
		}
	}
	
	public boolean ExpLockClicking(Player c, int id) {
		switch (id) {
			case 146135:
				ToggleLock(0);
			return true;
			case 146136:
				ToggleLock(1);
			return true;
			case 146137:
				ToggleLock(2);
			return true;
			case 146138:
				ToggleLock(3);
			return true;
			case 146139:
				ToggleLock(4);
			return true;
			case 146140:
				ToggleLock(5);
			return true;
			case 146141:
				ToggleLock(6);
			return true;
			case 146126:
				player.getPA().closeAllWindows();
			return true;
		}
		return false;
	}
	
}
