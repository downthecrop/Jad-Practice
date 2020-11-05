package ethos.model.content.achievement_diary.wilderness;

import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;

public enum WildernessDiaryEntry {
	//Easy
	LOW_ALCH("Cast low alchemy at the fountain of rune"),
	WILDERNESS_LEVER("Enter the wilderness from the edgeville lever"),
	WILDERNESS_ALTAR("Pray at the chaos altar in level 38 wilderness"),
	KILL_EARTH_WARRIOR("Kill some earth warriors: %totalstage", 50),
	DEMONIC_RUINS("Restore some prayer at the demonic ruins"),
	KBD_LAIR("Enter the king black dragon lair"),
	MINE_IRON_WILD("Mine some iron ore"),
	ABYSS_TELEPORT("Have the mage of zamorak teleport you to the abyss"),
	
	//Medium
	MINE_MITHRIL_WILD("Mine some mithril ore"),
	WILDERNESS_GODWARS("Enter the wilderness god wars dungeon"),
	KILL_GREEN_DRAGON("Kill some green dragons: %totalstage", 128),
	KILL_BLOODVELD("Kill a bloodveld in the wilderness god wars dungeon"),
	MYSTERIOUS_EMBLEM("Sell a mysterious emblem to the emblem trader"),
	SMITH_MITHRIL_AXE("Smith an mithril axe in resource area"),
	WILDERNESS_AGILITY("Complete wilderness agility course: %totalstage", 50),
	
	//Hard
	CLAWS_OF_GUTHIX("Cast claws of guthix on another player"),
	SMITH_ADAMANT_SCIMITAR("Smith an adamant scimitar in the resource area"),
	CHAOS_ELEMENTAL("Kill chaos elemental: %totalstage", 30),
	CRAZY_ARCHAEOLOGIST("Kill crazy archaeologist: %totalstage", 30),
	CHAOS_FANATIC("Kill chaos fanatic: %totalstage", 30),
	SCORPIA("Kill scorpia: %totalstage", 30),
	SPIRITUAL_WARRIOR("Kill a spiritual warrior in the wilderness god wars dungeon"),
	KARAMBWAN("Fish some raw karambwans in the resource area: %totalstage", 150),
	
	//Elite
	CALLISTO("Kill callisto: %totalstage", 140),
	VENENATIS("Kill venenatis: %totalstage", 140),
	VETION("Kill vet'ion: %totalstage", 140),
	GHORROCK("Teleport to ghorrock"),
	DARK_CRAB("Cook some dark crab at resource area: %totalstage", 350),
	SMITH_RUNE_SCIM_WILD("Smith a rune scimitar in resource area"),
	SPIRITUAL_MAGE("Kill a spiritual mage in the wilderness god wars dungeon"),
	MAGIC_LOG_WILD("Cut some magic logs in resource area: %totalstage", 200);
	
	private final String description;
	
	private final int maximumStages;
	
	public static final Set<WildernessDiaryEntry> SET = EnumSet.allOf(WildernessDiaryEntry.class);
	
	WildernessDiaryEntry(String description) {
		this(description, -1);
	}
	
	WildernessDiaryEntry(String description, int maximumStages) {
		this.description = description;
		this.maximumStages = maximumStages;
	}
	
	public final String getDescription() {
		return description;
	}
	
	public static final Optional<WildernessDiaryEntry> fromName(String name) {
		return SET.stream().filter(entry -> entry.name().equalsIgnoreCase(name)).findAny();
	}

	public int getMaximumStages() {
		return maximumStages;
	}
}
