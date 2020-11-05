package ethos.model.content.achievement_diary;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

import ethos.model.players.Player;

/**
 * Represents an Achievement diary implementation
 * 
 * @author Kaleem
 * @version 1.1
 *
 * @param <T>
 *            Enumeration representing the various achievments a {@link Player}
 *            can get
 */
public abstract class AchievementDiary<T extends Enum<T>> {

	/**
	 * The name of this {@link AchievementDiary}
	 */
	private final String name;

	/**
	 * The {@link Player} object the {@link AchievementDiary} is responsible for
	 */
	protected final Player player;

	/**
	 * A {@link HashSet} representing the various {@link T} achievements the
	 * {@link #player} has
	 */
	protected final Set<T> achievements = new HashSet<>();

	/**
	 * Creates a new {@link AchievementDiary} implementation (should be effectively immutable)
	 * @param name
	 * 		The name of this {@link AchievementDiary}
	 * @param player
	 * 		
	 */
	public AchievementDiary(String name, Player player) {
		this.name = name;
		this.player = player;
	}

	public boolean complete(T achievement) {
		boolean success = achievements.add(achievement);
		if (success)
			uponCompletion(achievement);
		return success;
	}
	
	public final boolean nonNotifyComplete(T achievement) {
		return achievements.add(achievement);
	}

	public final boolean remove(T achievement) {
		return achievements.remove(achievement);
	}

	public void uponCompletion(T achievement) {
		player.sendMessage("@mag@Well done! You have completed a task in the "+ getName() +". Your Achievement");
		player.sendMessage("@mag@Diary has been updated.");
	}

	public String getName() {
		return name;
	}

	public boolean hasDone(T entry) {
		return get(entry).isPresent();
	}

	public boolean hasDone(Set<T> entries) {
		boolean containsAll = true;
		for (T entry : entries) {
			if (!achievements.contains(entry)) {
				containsAll = false;
			}
		}
		return containsAll;
	}
	
	public void forEach(Consumer<T> action) {
		achievements.forEach(entry -> action.accept(entry));
	}

	public Optional<T> get(T entry) {
		return achievements.stream().filter(some -> some.equals(entry))
				.findAny();
	}
	
	public Set<T> getAchievements() {
		return achievements;
	}

	public Player getPlayer() {
		return player;
	}
}
