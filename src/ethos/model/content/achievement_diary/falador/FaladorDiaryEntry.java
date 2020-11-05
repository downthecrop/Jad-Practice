package ethos.model.content.achievement_diary.falador;

import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;

public enum FaladorDiaryEntry {
	//Easy
	TELEPORT_ESSENCE_FAL("Have the zammy wizard teleport you to the ess mine"),
	CHOP_WILLOW("Chop willow logs: %totalstage", 85),
	PICK_FLAX("Pick flax from the garden: %totalstage", 85),
	PICKPOCKET_MAN("Pickpocket a man for some money: %totalstage", 50),
	COMPOST_BUCKET("Fill buckets with compost: %totalstage", 85),
	GRAPPLE_NORTH_WALL("Grapple the north wall shortcut"),
	TELEPORT_TO_FALADOR("Teleport to falador"),
	
	//Medium
	KILL_WHITE_KNIGHT("Kill white knights: %totalstage", 200),
	RECOLOR_GRACEFUL("Color a graceful piece"),
	CHOP_YEW("Chop yew logs: %totalstage", 100),
	PICKPOCKET_MASTER_FARMER_FAL("Pickpocket a master farmer for some seeds: %totalstage", 250),
	ALTAR_OF_GUTHIX("Pray at the guthix altar in taverly wearing full initiate"),
	CRAFT_MIND("Craft more than 100 mind runes in one go: %totalstage", 30),
	
	//Hard
	PICK_POSION_BERRY("Pick poison berries: %totalstage", 56),
	STEAL_GEM_FAL("Steal gems from the gem stall: %totalstage", 84),
	KILL_GIANT_MOLE("Kill giant moles: %totalstage", 45),
	MINE_MITHRIL("Mine some mithril ore: %totalstage", 84),
	
	//Elite
	HARVEST_TORSTOL("Harvest torstols: %totalstage", 250),
	FISH_MANTA("Fish manta rays: %totalstage", 450),
	MINE_GEM_FAL("Mine some gems from a gem rock: %totalstage", 500);
	
	private final String description;
	
	private final int maximumStages;
	
	public static final Set<FaladorDiaryEntry> SET = EnumSet.allOf(FaladorDiaryEntry.class);
	
	FaladorDiaryEntry(String description) {
		this(description, -1);
	}
	
	FaladorDiaryEntry(String description, int maximumStages) {
		this.description = description;
		this.maximumStages = maximumStages;
	}
	
	public final String getDescription() {
		return description;
	}
	
	public static final Optional<FaladorDiaryEntry> fromName(String name) {
		return SET.stream().filter(entry -> entry.name().equalsIgnoreCase(name)).findAny();
	}

	public int getMaximumStages() {
		return maximumStages;
	}
}
