package ethos.model.content.achievement_diary.ardougne;

import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;

public enum ArdougneDiaryEntry {
	//Easy
	STEAL_CAKE("Steal cake from the baker's stall: %totalstage", 50),
	WILDERNESS_LEVER("Use the lever teleport to the wilderness"),
	TELEPORT_ESSENCE_ARD("Have the wizard teleport you to the essence mine"),
	CROSS_THE_LOG("Walk across the log shortcut"),
	
	//Medium
	TELEPORT_ARDOUGNE("Teleport to ardougne"),
	PICKPOCKET_ARD("Pickpocket the master farmer: %totalstage", 220),
	IBANS_STAFF("Equip an iban's staff"),
	DRAGON_SQUARE("Combine two shield halves to create a dragon shield"),
	
	//Hard
	STEAL_FUR("Steal some fur from the fur stall: %totalstage", 200),
	PRAY_WITH_CHIVALRY("Pray at the altar with chivalry active at the church"),
	CRAFT_DEATH("Craft some death runes"),
	ARDOUGNE_ROOFTOP("Run the rooftop course: %totalstage", 125),
	
	//Elite
	STEAL_GEM_ARD("Steal some gems from the gem stall: %totalstage", 200),
	PICKPOCKET_HERO("Pickpocket some heroes: %totalstage", 400),
	SUPER_COMBAT_ARD("Create super combat potions on \\nthe bridge within the zoo: %totalstage", 100);
	
	private final String description;
	
	private final int maximumStages;
	
	public static final Set<ArdougneDiaryEntry> SET = EnumSet.allOf(ArdougneDiaryEntry.class);
	
	ArdougneDiaryEntry(String description) {
		this(description, -1);
	}
	
	ArdougneDiaryEntry(String description, int maximumStages) {
		this.description = description;
		this.maximumStages = maximumStages;
	}
	
	public final String getDescription() {
		return description;
	}
	
	public static final Optional<ArdougneDiaryEntry> fromName(String name) {
		return SET.stream().filter(entry -> entry.name().equalsIgnoreCase(name)).findAny();
	}

	public int getMaximumStages() {
		return maximumStages;
	}
}
