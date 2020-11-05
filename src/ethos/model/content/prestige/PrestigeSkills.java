package ethos.model.content.prestige;

import ethos.Config;
import ethos.model.players.Player;

public class PrestigeSkills {

	public Player player;
	
	public PrestigeSkills(Player player) {
		this.player = player;
	}
	
	public final int MAX_PRESTIGE = 10;
	
	public int points = 1; // This is the base prestige points given
	
	public void openPrestige() { // Refreshes all text lines before showing the interface - Looks better
		for (int j = 0; j < 22; j++) {
			player.getPA().sendFrame126(""+player.prestigeLevel[j]+"", 37400 + j); // Update Current Prestige on interface
		}
		registerClick(0);
		player.getPA().showInterface(37300);
	}
	
	public void openShop() {
		player.sendMessage("@blu@ You have "+player.getPrestigePoints()+" prestige points.");
		player.getShops().openShop(120);
	}
	
	public void registerClick(int i) {
		player.prestigeNumber = i;
		player.currentPrestigeLevel = player.prestigeLevel[player.prestigeNumber];
		player.canPrestige = (player.getPA().getLevelForXP(player.playerXP[player.prestigeNumber]) == 99) ? true : false; //Update global canPrestige boolean
		String canPrestige = ((player.canPrestige == true) ? "@gre@Yes" : "@red@No"); // String version for interface Yes or No
		player.getPA().sendFrame126(Config.SKILL_NAME[player.prestigeNumber], 37307); // Update Skill Name
		player.getPA().sendFrame126("Current Prestige: @whi@"+player.currentPrestigeLevel, 37308); // Update Current Prestige in box
		player.getPA().sendFrame126("Reward: @whi@"+(points + ((player.currentPrestigeLevel + 1)))+" Points", 37309); // Update Reward
		player.getPA().sendFrame126("Can Prestige: "+ canPrestige, 37390); // Update If you can prestige
	}
	
	public void prestige() {
		if (player.currentPrestigeLevel == MAX_PRESTIGE) { // Change to prestige master
			player.sendMessage("You are the max prestige level in this skill!");
			return;
		}
		if (player.prestigeNumber <= 6 && player.getItems().isWearingItems()) { // Change to prestige master
			player.getDH().sendNpcChat1("You must remove your equipment to prestige this stat.", 2989, "Ak-Haranu");
			return;
		}
		player.canPrestige = (player.getPA().getLevelForXP(player.playerXP[player.prestigeNumber]) == 99) ? true : false; //Update global canPrestige boolean
		if (player.canPrestige) { // If the skill is 99
			if (player.VERIFICATION == 0) { // Verification Check
				player.sendMessage("@red@Please click prestige again to confirm prestiging of the "+Config.SKILL_NAME[player.prestigeNumber]+" skill.");
				player.VERIFICATION++;
				return;
			}
			player.VERIFICATION = 0;
			if (player.prestigeNumber != 3) { // If not Hitpoints
				player.playerLevel[player.prestigeNumber] = 1;
				player.playerXP[player.prestigeNumber] = player.getPA().getXPForLevel(1);
				player.getPA().setSkillLevel(player.prestigeNumber, 1, player.getPA().getXPForLevel(1));
				player.getPA().refreshSkill(player.prestigeNumber); // Refresh skills
			} else { // Hitpoints should be 10
				player.playerLevel[player.prestigeNumber] = 10;
				player.playerXP[player.prestigeNumber] = player.getPA().getXPForLevel(10) + 1;
				player.getPA().setSkillLevel(player.prestigeNumber, 10, player.getPA().getXPForLevel(10));
				player.getPA().refreshSkill(player.prestigeNumber); // Refresh skills
			}
			if (player.prestigeNumber <= 6) {
				player.combatLevel = player.calculateCombatLevel();
				player.getPA().sendFrame126("Combat Level: " + player.combatLevel + "", 3983);
			}
			player.prestigePoints+= points + ((player.currentPrestigeLevel + 1));
			player.prestigeLevel[player.prestigeNumber] += 1;
			registerClick(player.prestigeNumber);
			player.getPA().sendFrame126(""+player.prestigeLevel[player.prestigeNumber]+"", 37400 + player.prestigeNumber); // Update Current Prestige on interface
		} else {
			player.sendMessage("You cannot prestige "+Config.SKILL_NAME[player.prestigeNumber]+" you need to gain "+ (99 -  player.getPA().getLevelForXP(player.playerXP[player.prestigeNumber])) +" more "+Config.SKILL_NAME[player.prestigeNumber]+" levels.");
		}
	}
	
	public boolean prestigeClicking(Player c, int id) {
		if (id != 146015)
			player.VERIFICATION = 0;
		switch (id) {
			case 145191:
				registerClick(0);
			return true;
			case 145192:
				registerClick(1);
			return true;
			case 145193:
				registerClick(2);
			return true;
			case 145194:
				registerClick(3);
			return true;
			case 145195:
				registerClick(4);
			return true;
			case 145196:
				registerClick(5);
			return true;
			case 145197:
				registerClick(6);
			return true;
			case 145198:
				registerClick(7);
			return true;
			case 145199:
				registerClick(8);
			return true;
			case 145200:
				registerClick(9);
			return true;
			case 145201:
				registerClick(10);
			return true;
			case 145202:
				registerClick(11);
			return true;
			case 145203:
				registerClick(12);
			return true;
			case 145204:
				registerClick(13);
			return true;
			case 145205:
				registerClick(14);
			return true;
			case 145206:
				registerClick(15);
			return true;
			case 145207:
				registerClick(16);
			return true;
			case 145208:
				registerClick(17);
			return true;
			case 145209:
				registerClick(18);
			return true;
			case 145210:
				registerClick(19);
			return true;
			case 145211:
				registerClick(20);
			return true;
			case 145212:
				registerClick(21);
			return true;
			case 146015:
				prestige();
			return true;
			case 145182:
				player.getPA().closeAllWindows();
			return true;
		}
		return false;
	}
}