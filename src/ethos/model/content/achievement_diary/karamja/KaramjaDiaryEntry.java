package ethos.model.content.achievement_diary.karamja;

import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;

public enum KaramjaDiaryEntry {
	//Easy
	PICK_BANANAS("Pick bananas: %totalstage", 32),
	TELEPORT_TO_KARAMJA("Use a glory teleport to karamja"),
	SAIL_TO_ARDOUGNE("Travel to ardougne from the port in brimhaven"),
	FISH_LOBSTER_KAR("Fish some lobsters: %totalstage", 58),
	ENTER_BRIMHAVEN_DUNGEON("Enter brimhaven dungeon through the entrance"),
	
	//Medium
	DURADEL("Be assigned a slayer task by duradel"),
	STEEL_DRAGON("Kill steel dragons in brimhaven dungeon: %totalstage", 50),
	MOSS_GIANT("Kill moss giants on moss giant island: %totalstage", 120),
	
	//Hard
	COMPLETE_FIGHT_CAVES("Complete the fight caves 63 waves minigame"),
	MINE_GOLD_KAR("Mine some gold in tzhaar city: %totalstage", 150),
	KILL_TZHAAR_XIL("Kill some tzhaar-xil: %totalstage", 250),
	
	//Elite
	CRAFT_NATURES("Craft more than 50 nature runes in one go: %totalstage", 85),
	ANTI_VENOM("Create some anti-venom potions in brimhaven: %totalstage", 120),
	EQUIP_FIRE_CAPE("Equip a fire cape in the tzhaar city");
	
	private final String description;
	
	private final int maximumStages;
	
	public static final Set<KaramjaDiaryEntry> SET = EnumSet.allOf(KaramjaDiaryEntry.class);
	
	KaramjaDiaryEntry(String description) {
		this(description, -1);
	}
	
	KaramjaDiaryEntry(String description, int maximumStages) {
		this.description = description;
		this.maximumStages = maximumStages;
	}
	
	public final String getDescription() {
		return description;
	}
	
	public static final Optional<KaramjaDiaryEntry> fromName(String name) {
		return SET.stream().filter(entry -> entry.name().equalsIgnoreCase(name)).findAny();
	}

	public int getMaximumStages() {
		return maximumStages;
	}
}
