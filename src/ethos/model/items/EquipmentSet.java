package ethos.model.items;

import java.util.Arrays;
import java.util.Optional;

import ethos.model.players.Player;

/**
 * 
 * @author Jason MacKeigan
 * @date Nov 24, 2014, 10:06:56 PM
 */
public enum EquipmentSet {
	
		DHAROK(new int[][] { { 4716, 4718, 4720, 4722 }, { 4880, 4886, 4892, 4898 }, { 4881, 4887, 4893, 4899 }, { 4882, 4888, 4894, 4900 }, { 4883, 4889, 4895, 4901 } }), 
		VERAC(new int[][] { { 4753, 4755, 4757, 4759 }, { 4976, 4982, 4988, 4994 }, { 4977, 4983, 4989, 4995 }, { 4978, 4984, 4990, 4996 }, { 4979, 4985, 4991, 4997 } }), 
		GUTHAN(new int[][] { { 4724, 4726, 4728, 4730 }, { 4904, 4910, 4916, 4922 }, { 4905, 4911, 4917, 4923 }, { 4906, 4912, 4918, 4924 }, { 4907, 4913, 4919, 4925 } }), 
		TORAG(new int[][] { { 4745, 4747, 4749, 4751 }, { 4952, 4958, 4964, 4970 }, { 4953, 4959, 4965, 4971 }, { 4954, 4960, 4966, 4972 }, { 4955, 4961, 4967, 4973 } }), 
		AHRIM(new int[][] { { 4708, 4710, 4712, 4714 }, { 4856, 4862, 4868, 4874 }, { 4857, 4863, 4869, 4875 }, { 4858, 4864, 4870, 4876 }, { 4859, 4865, 4871, 4877 } }), 
		KARIL(new int[][] { { 4732, 4734, 4736, 4738 }, { 4928, 4934, 4940, 4946 }, { 4929, 4935, 4941, 4947 }, { 4930, 4936, 4942, 4948 }, { 4931, 4937, 4943, 4949 } }), 
		IRON_MAN_ARMOUR(new int[][] { { 12810, 12811, 12812 } }), 
		DESERT_ROBES(new int[][] { { 1833, 1835 } }),
		ULTIMATE_IRON_MAN_ARMOUR(new int[][] { { 12813, 12814, 12815 } 
	
	});

	private int[][] equipment;

	EquipmentSet(int[][] equipment) {
		this.equipment = equipment;
	}

	/**
	 * An array of each barrow item
	 */
	static final EquipmentSet[] BARROWS = { KARIL, DHAROK, AHRIM, GUTHAN, TORAG, VERAC };

	public int[][] getEquipment() {
		return equipment;
	}

	/**
	 * Allows us to determine if a player is wearing an entire set.
	 * 
	 * @param player the player we are trying to determine this for
	 * @return true if we are wearing all of the items in a single set, otherwise false.
	 */
	public boolean isWearing(Player player) {
		for (int[] set : equipment) {
			int wornAmount = 0;
			for (int setItem : set) {
				if (player.getItems().isWearingItem(setItem)) {
					wornAmount++;
				}
			}
			if (wornAmount == set.length) {
				return true;
			}
		}
		return false;
	}

	public boolean isWearingBarrows(Player player) {
		Optional<EquipmentSet> barrow = Arrays.stream(BARROWS).filter(this::equals).findFirst();
		if (!barrow.isPresent()) {
			return false;
		}
		int[] items = new int[4];
		for (int col = 0; col < 4; col++) {
			for (int row = 0; row < 5; row++) {
				if (player.getItems().isWearingItem(equipment[row][col])) {
					items[col] = equipment[row][col];
					break;
				}
			}
		}
		for (int item1 : items) {
			if (item1==0) {
				return false;
			}
		}
		return true;
	}

}
