package ethos.model.holiday.christmas;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

public enum ChristmasToy {
	STAR(6822, 6824, 6826), BOX(6828, 6830, 6832), DIAMOND(6834, 6836, 6838), TREE(6840, 6842, 6844), BELL(6846, 6848, 6850);

	private int[] items;

	private ChristmasToy(int... items) {
		this.items = items;
	}

	public int[] getItems() {
		return items;
	}

	public static Optional<ChristmasToy> forItem(int item) {
		return Stream.of(values()).filter(t -> {
			for (int toyId : t.items) {
				if (toyId == item) {
					return true;
				}
			}
			return false;
		}).findFirst();
	}

	public Optional<Integer> getNextItem(int item) {
		int index = Arrays.binarySearch(items, item) + 1;
		return index > items.length - 1 ? Optional.empty() : Optional.of(items[index]);
	}

	public Optional<ChristmasToy> getNextToy() {
		return valueOf(ordinal() + 1);
	}

	private Optional<ChristmasToy> valueOf(int index) {
		return Arrays.asList(values()).stream().filter(toy -> toy.ordinal() == index).findFirst();
	}

}
