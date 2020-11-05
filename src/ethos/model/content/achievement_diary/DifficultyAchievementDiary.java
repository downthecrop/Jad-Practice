package ethos.model.content.achievement_diary;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import ethos.model.players.Player;

public abstract class DifficultyAchievementDiary<T extends Enum<T>> extends AchievementDiary<T> {
	   
    private final Set<EntryDifficulty> claimed = new HashSet<>();
   
    public enum EntryDifficulty {
        EASY,
        MEDIUM,
        HARD,
        ELITE;
    	
    	public static final ImmutableSet<EntryDifficulty> VALUES = Sets.immutableEnumSet(EnumSet.allOf(EntryDifficulty.class));
       
        public static Optional<EntryDifficulty> forString(String claim) {
            return VALUES.stream().filter(diff -> diff.name().equalsIgnoreCase(claim)).findAny();
        }
    }
 
    public DifficultyAchievementDiary(String name, Player player) {
        super(name, player);
    }
   
    public abstract Set<T> getEasy();
    public abstract Set<T> getMedium();
    public abstract Set<T> getHard();
    public abstract Set<T> getElite();
   
    public final boolean hasDone(EntryDifficulty difficulty) {
        if (difficulty == EntryDifficulty.EASY) {
            return hasDone(getEasy());
        }
        if (difficulty == EntryDifficulty.MEDIUM) {
            return hasDone(getMedium());
        }
        if (difficulty == EntryDifficulty.HARD) {
            return hasDone(getHard());
        }
        return hasDone(getElite());
    }
   
    public final boolean claim(EntryDifficulty difficulty) {
        return claimed.add(difficulty);
    }
    public final boolean hasClaimed(EntryDifficulty difficulty) {
        return claimed.contains(difficulty);
    }
   
    public final Set<T> getAll() {
        Set<T> set = new LinkedHashSet<>();
        set.addAll(getEasy());
        set.addAll(getMedium());
        set.addAll(getHard());
        set.addAll(getElite());
        return set;
    }
    
	public Set<EntryDifficulty> getClaimed() {
		return claimed;
	}
   
    public final boolean hasDoneEasy() {
        return hasDone(EntryDifficulty.EASY);
    }
   
    public final boolean hasDoneMedium() {
        return hasDone(EntryDifficulty.MEDIUM);
    }
   
    public final boolean hasDoneHard() {
        return hasDone(EntryDifficulty.HARD);
    }
   
    public final boolean hasDoneElite() {
        return hasDone(EntryDifficulty.ELITE);
    }
   
    public final boolean hasDoneAll() {
        boolean hasDone = hasDoneEasy() && hasDoneMedium() && hasDoneHard() && hasDoneElite();
        return hasDone;
    }
 
}