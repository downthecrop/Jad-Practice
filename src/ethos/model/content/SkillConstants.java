package ethos.model.content;

/**
 * The Identification Of Skills.
 * 
 * @author Graham Revised by Shawn Notes by Shawn
 */
public class SkillConstants {

	/**
	 * Skill IDs.
	 */
	public final static int ATTACK = 0;
	public final static int DEFENCE = 1;
	public final static int STRENGTH = 2;
	public final static int HITPOINTS = 3;
	public final static int RANGE = 4;
	public final static int PRAYER = 5;
	public final static int MAGIC = 6;
	public final static int COOKING = 7;
	public final static int WOODCUTTING = 8;
	public final static int FLETCHING = 9;
	public final static int FISHING = 10;
	public final static int FIREMAKING = 11;
	public final static int CRAFTING = 12;
	public final static int SMITHING = 13;
	public final static int MINING = 14;
	public final static int HERBLORE = 15;
	public final static int AGILITY = 16;
	public final static int THIEVING = 17;
	public final static int SLAYER = 18;
	public final static int FARMING = 19;
	public final static int RUNECRAFTING = 20;

	/**
	 * How many skills are listed into the server.
	 */
	public final static int SKILLS_COUNT = 25;

	/**
	 * The names of the skills.
	 */
	public final static String[] SKILL_NAMES = { "attack", "defence", "strength", "hitpoints", "range", "prayer", "magic", "cooking", "woodcutting", "fletching", "fishing",
			"firemaking", "crafting", "smithing", "mining", "herblore", "agility", "thieving", "slayer", "farming", "runecrafting" };

	public final static String[] PRE_SKILL_NAMES = { "an", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "an", "a", "a", "a", "a" };

	/**
	 * Interface displayed when leveling.
	 */
	public final static int[] SKILL_LEVEL_UP_INTERFACES = { 6247, 6253, 6206, 6216, 4443, 6242, 6211, 6226, 4272, 6231, 6258, 4282, 6263, 6221, 4416, 6237, 4277, 4261,
			/* 12123,4261 */-1, 4267, 4267 };

}
