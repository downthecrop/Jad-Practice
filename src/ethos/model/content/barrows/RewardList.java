package ethos.model.content.barrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("serial")
class RewardList extends ArrayList<RewardItem> {

	RewardList() {
		addAll(Reward.asList());
	}

	public void reset() {
		clear();
		addAll(Reward.asList());
	}

	int getTotalWeight(int killCount) {
		int total = 0;
		for (RewardItem item : this) {
			int rarity = item.getRarityLevel().getRarity();
			if (item.getRarityLevel() == RewardLevel.COMMON) {
				rarity = firstTierRarity(killCount);
			}
			total += rarity;
		}
		return total;
	}

	int firstTierRarity(int killCount) {
		int size = (int) Reward.VALUES.stream().filter(reward -> reward.rarity == RewardLevel.COMMON).count();
		return RewardLevel.COMMON.getRarity() - (RewardLevel.KC_MULTIPLIER * killCount / size);
	}
	private enum Reward {

		MIND_RUNE(558, 1, 400, RewardLevel.COMMON), 
		CHAOS_RUNE(562, 1, 150, RewardLevel.COMMON), 
		DEATH_RUNE(560, 1, 100, RewardLevel.COMMON), 
		BLOOD_RUNE(565, 1, 80, RewardLevel.COMMON), 
		COINS(995, 1, 25306, RewardLevel.COMMON), 
		BOLT_RACKS(4740, 1, 191, RewardLevel.COMMON), 
		ASTRAL_RUNE(9075, 1, 150, RewardLevel.COMMON),

		CRYSTAL_KEY(990, 1, 2, RewardLevel.UNCOMMON), 
		SUPER_STRENGTH_POTION(158, 1, 10, RewardLevel.UNCOMMON), 
		SUPER_ATTACK_POTION(146, 1, 10, RewardLevel.UNCOMMON), 
		SUPER_DEFENCE_POTION(164, 1, 10, RewardLevel.UNCOMMON), 
		PRAYER_POT(140, 1, 10, RewardLevel.UNCOMMON), 
		RANGE_POTION(170, 1, 10, RewardLevel.UNCOMMON), 
		CLUE_HARD(2722, 1, 1, RewardLevel.UNCOMMON);

		private static final List<Reward> VALUES = Collections.unmodifiableList(Arrays.asList(values()));

		private final int itemId;
		private final int minAmount;
		private final int maxAmount;
		private final RewardLevel rarity;

		Reward(int itemId, int minAmount, int maxAmount, RewardLevel rarity) {
			this.itemId = itemId;
			this.minAmount = minAmount;
			this.maxAmount = maxAmount;
			this.rarity = rarity;
		}

		public static List<RewardItem> asList() {
			return VALUES.stream().map(reward -> new RewardItem(reward.itemId, reward.minAmount, reward.maxAmount, reward.rarity)).collect(Collectors.toList());
		}

	}

}
