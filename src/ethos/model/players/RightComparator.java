package ethos.model.players;

import java.util.Comparator;

import org.apache.commons.lang3.ArrayUtils;

public class RightComparator implements Comparator<Right> {

	public static final RightComparator RIGHT_COMPARATOR = new RightComparator();

	public static final Comparator<Player> PLAYER_COMPARATOR = new Comparator<Player>() {

		@Override
		public int compare(Player o1, Player o2) {
			return RIGHT_COMPARATOR.compare(o1.getRights().getPrimary(), o2.getRights().getPrimary());
		}
	};

	@Override
	public int compare(Right o1, Right o2) {
		final int o1Index = ArrayUtils.indexOf(Right.PRIORITY, o1);
		final int o2Index = ArrayUtils.indexOf(Right.PRIORITY, o2);
		if (o1Index < o2Index) {
			return 1;
		} else if (o1Index > o2Index) {
			return -1;
		}
		return 0;
	}

}
