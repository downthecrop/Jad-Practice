package ethos.model.content.achievement_diary.desert;

import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;

public enum DesertDiaryEntry {
	//Easy
	PASS_GATE("Pass through shantay pass gate"),
	CUT_CACTUS("Cut a cactus for some water"),
	KILL_SNAKES_DESERT("Kill some snakes: %totalstage", 20),
	KILL_LIZARDS_DESERT("Kill some lizards: %totalstage", 20),
	MINE_CLAY("Mine some clay in the north-eastern part of the desert"),
	
	//Medium
	KILL_BANDIT("Kill some bandits: %totalstage", 80),
	PASS_GATE_ROBES("Pass through shantay pass wearing desert robes"),
	COMBAT_POTION("Create a combat potion in the desert"),
	CHOP_TEAK("Chop some teak logs near uzer: %totalstage", 30),
	PICKPOCKET_THUG("Pickpocket a menaphite thug: %totalstage", 150),
	ACTIVATE_ANCIENT("Activate ancient magics at the pyramid"),
	CAST_BARRAGE("Travel to Nardah via magic carpet"),
	KILL_VULTURE("Kill some vultures: %totalstage", 70),
	
	//Hard
	TRAVEL_POLLNIVNEACH("Travel to pollnivneach via magic carpet"),
	KILL_DUST_DEVIL("Kill some dust devils, wearing a slayer helmet: %totalstage", 80),
	
	//Elite
	PRAY_SOPHANEM("Restore at least 85 Prayer points when praying at the\\nAltar in Sophanem");
	
	private final String description;
	
	private final int maximumStages;
	
	public static final Set<DesertDiaryEntry> SET = EnumSet.allOf(DesertDiaryEntry.class);
	
	DesertDiaryEntry(String description) {
		this(description, -1);
	}
	
	DesertDiaryEntry(String description, int maximumStages) {
		this.description = description;
		this.maximumStages = maximumStages;
	}
	
	public final String getDescription() {
		return description;
	}
	
	public static final Optional<DesertDiaryEntry> fromName(String name) {
		return SET.stream().filter(entry -> entry.name().equalsIgnoreCase(name)).findAny();
	}

	public int getMaximumStages() {
		return maximumStages;
	}
}
