package ethos.model.players.skills;

import java.util.Arrays;

import ethos.util.Misc;

public enum Skill {
		ATTACK(0), 
		DEFENCE(1), 
		STRENGTH(2), 
		HITPOINTS(3), 
		RANGED(4), 
		PRAYER(5), 
		MAGIC(6), 
		COOKING(7), 
		WOODCUTTING(8), 
		FLETCHING(9), 
		FISHING(10), 
		FIREMAKING(11), 
		CRAFTING(12), 
		SMITHING(13), 
		MINING(14), 
		HERBLORE(15), 
		AGILITY(16), 
		THIEVING(17), 
		SLAYER(18), 
		FARMING(19), 
		RUNECRAFTING(20),
		CONSTRUCTION(22),
		HUNTER(21);

	private int id;

	private Skill(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		String name = name().toLowerCase();
		return Misc.capitalize(name);
	}

	public static Skill forId(int id) {
		return Arrays.asList(values()).stream().filter(s -> s.id == id).findFirst().orElse(null);
	}

	public static final int MAXIMUM_SKILL_ID = 22;
}
