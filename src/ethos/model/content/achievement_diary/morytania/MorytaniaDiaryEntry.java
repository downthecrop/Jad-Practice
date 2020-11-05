package ethos.model.content.achievement_diary.morytania;

import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;

public enum MorytaniaDiaryEntry {
	//Easy
	MAZCHNA("Complete slayer tasks from mazchna: %totalstage", 4),
	MORYTANIA_SWAMP("Teleport to morytania swamp"),
	KILL_BANSHEE("Kill banshee in slayer tower: %totalstage", 25),
	KILL_WEREWOLF("Kill some werewolves: %totalstage", 30),
	
	//Medium
	KILL_CAVE_HORROR("Kill some cave horrors: %totalstage", 80),
	CLIMB_CHAIN("Climb the advanced chain in slayer tower"),
	
	//Hard
	TEN_CONSECUTIVE("Complete 10 consecutive tasks in a row"),
	DHIDE_BODY("Craft a black d'hide body in canifis"),
	KILL_NECHRYAEL("Kill some nechryaels: %totalstage", 150),
	
	//Elite
	BARROWS_CHEST("Loot the barrows chest, wearing a full barrows set"),
	BARROWS_PIECE("Get a barrows piece from the chest"),
	KILL_ABYSSAL_DEMON("Kill some abyssal demons: %totalstage", 250);
	
	private final String description;
	
	private final int maximumStages;
	
	public static final Set<MorytaniaDiaryEntry> SET = EnumSet.allOf(MorytaniaDiaryEntry.class);
	
	MorytaniaDiaryEntry(String description) {
		this(description, -1);
	}
	
	MorytaniaDiaryEntry(String description, int maximumStages) {
		this.description = description;
		this.maximumStages = maximumStages;
	}
	
	public final String getDescription() {
		return description;
	}
	
	public static final Optional<MorytaniaDiaryEntry> fromName(String name) {
		return SET.stream().filter(entry -> entry.name().equalsIgnoreCase(name)).findAny();
	}

	public int getMaximumStages() {
		return maximumStages;
	}
}
