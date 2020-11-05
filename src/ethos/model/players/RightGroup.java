package ethos.model.players;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class RightGroup {

	private Player player;
	private Right primary;
	private Set<Right> rights;

	public RightGroup(Player player, Right primary) {
		this.player = player;
		this.primary = primary;
		this.rights = new LinkedHashSet<>();
		rights.add(primary);
	}

	public RightGroup(Player player, List<Right> rights) {
		this.player = player;
		this.primary = findHighestPriority(rights);
		this.rights = new LinkedHashSet<>(rights);
	}

	public RightGroup(Player player, Right primary, List<Right> rights) {
		this.player = player;
		this.primary = primary;
		this.rights = new LinkedHashSet<>(rights);
		this.rights.add(primary);
	}

	public void add(Right toAdd) {
		if (rights.stream().anyMatch(r -> r.isOrInherits(toAdd))) {
			return;
		}
		Right prevPrimary = primary;
		rights.stream().filter(r -> toAdd.isOrInherits(r)).forEach(this::remove);
		if (prevPrimary != primary) {
			updatePrimary();
		}
		rights.add(toAdd);
	}

	public void remove(Right toRemove) {
		if (toRemove == Right.PLAYER) {
			return;
		}
		rights.remove(toRemove);
		if (toRemove == primary) {
			updatePrimary();
		}
	}

	public void removeIfInherits(Right toRemove) {
		rights.stream().filter(r -> r.isOrInherits(toRemove)).forEach(this::remove);
	}

	public Right getPrimary() {
		return primary;
	}

	public void setPrimary(Right primary) {
		this.primary = primary;
		rights.add(primary);
		player.getPA().requestUpdates();
	}

	public void updatePrimary() {
		primary = findHighestPriority(rights);
		player.getPA().requestUpdates();
	}

	public boolean contains(Right right) {
		return rights.contains(right);
	}

	public boolean isOrInherits(Right right) {
		return rights.stream().anyMatch(r -> r.isOrInherits(right));
	}

	public boolean isOrInherits(Right... rights) {
		return Arrays.stream(rights).anyMatch(this::isOrInherits);
	}

	private Right findHighestPriority(Collection<Right> rights) {
		if (rights.size() <= 0) {
			return Right.PLAYER;
		} else if (rights.size() == 1) {
			return rights.iterator().next();
		} else {
			return rights.stream().min(RightComparator.RIGHT_COMPARATOR).get();
		}
	}

	public Set<Right> getSet() {
		return rights;
	}
}
