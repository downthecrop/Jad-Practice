package ethos.model.content.achievement_diary.lumbridge_draynor;

import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;

public enum LumbridgeDraynorDiaryEntry {
	//Easy
	TELEPORT_ESSENCE_LUM("Have Sedridor teleport you to the essence mine"),
	CRAFT_WATER("Craft some water runes"),
	HANS("Have hans tell you how long you've played"),
	PICKPOCKET_MAN_LUM("Pickpocket a man"),
	BURN_OAK("Burn some oak logs: %totalstage", 28),
	COOK_SHRIMP("Cook some shrimp within the castle"),
	MINE_IRON_LUM("Mine some iron at the al-kharid mine: %totalstage", 50),
	
	//Medium,
	LUMBRIDGE_TELEPORT("Teleport to lumbridge"),
	RIVER_LUM_SHORTCUT("Take the shortcut over the river lum"),
	ATTRACTOR("Purchase an attractor from ava"),
	CHOP_WILLOW_DRAY("Chop some willows in draynor: %totalstage", 150),
	PICKPOCKET_FARMER_DRAY("Pickpocket the master farmer in draynor: %totalstage", 150),
	
	//Hard,
	BONES_TO_PEACHES("Cast bones to peaches spell in al-kharid"),
	CRAFT_COSMIC("Craft more than 56 cosmic runes in one go: %totalstage", 30),
	PURCHASE_BARROW_GLOVES("Purchase barrow gloves from culinaromancer's chest"),
	
	//Elite
	CHOP_MAGIC_AL("Chop some magic logs in al-kharid: %totalstage", 250),
	RUNE_PLATE_LUM("Smith a rune platebody"),
	ACHIEVEMENT_EMOTE("Perform the achievement cape emote in draynor");
	
	private final String description;
	
	private final int maximumStages;
	
	public static final Set<LumbridgeDraynorDiaryEntry> SET = EnumSet.allOf(LumbridgeDraynorDiaryEntry.class);
	
	LumbridgeDraynorDiaryEntry(String description) {
		this(description, -1);
	}
	
	LumbridgeDraynorDiaryEntry(String description, int maximumStages) {
		this.description = description;
		this.maximumStages = maximumStages;
	}
	
	public final String getDescription() {
		return description;
	}
	
	public static final Optional<LumbridgeDraynorDiaryEntry> fromName(String name) {
		return SET.stream().filter(entry -> entry.name().equalsIgnoreCase(name)).findAny();
	}

	public int getMaximumStages() {
		return maximumStages;
	}
}
