package ethos.model.content;

import ethos.model.players.Boundary;
import ethos.model.players.Player;

/**
 * Reset an individual player skill for a cost.
 * 
 * @author Matt
 *
 */
public class StatReset {

	/**
	 * Item id of what the player will be paying with
	 */
	private static int VALUE = 995;
	
	/**
	 * The amount the player will pay
	 */
	private static int SKILL_RESET_COST = 4_000_000;

	/**
	 * Tries to execute and reset a chosen skill
	 * 
	 * @param player The player executing for
	 * @param skill The skill id which is being reset
	 */
	public static void execute(Player player, int skill) {
		player.getPA().removeAllWindows();
		player.nextChat = -1;
		if (player.getItems().isWearingItems()) {
			player.getDH().sendNpcChat1("Warrior, you must remove your equipment..", player.talkingNpc, "Combat Instructor");
			return;
		}
		if (Boundary.isIn(player, Boundary.DUEL_ARENA) || Boundary.isIn(player, Boundary.FIGHT_CAVE)) {
			player.getDH().sendNpcChat2("You cannot do this whilst in this area.", "Please finish what you're doing.", player.talkingNpc, "Combat Instructor");
			return;
		}
		if (player.playerLevel[skill] == 1) {
			player.getDH().sendNpcChat2("You are already level 1 in this skill, it is not", "recommended that you do this.", player.talkingNpc, "Combat Instructor");
			return;
		}
		if (!player.getItems().playerHasItem(VALUE, SKILL_RESET_COST)) {
			player.getDH().sendNpcChat1("Warrior, you do not seem to have 4M GP..", player.talkingNpc, "Combat Instructor");
			return;
		}
		player.getItems().deleteItem(VALUE, SKILL_RESET_COST); 
		if (skill != 3) {
			player.playerLevel[skill] = 1;
			player.playerXP[skill] = player.getPA().getXPForLevel(1);
		} else {
			player.playerLevel[skill] = 10;
			player.playerXP[skill] = player.getPA().getXPForLevel(10) + 1;
		}
		player.getPA().refreshSkill(skill);
	}

}
