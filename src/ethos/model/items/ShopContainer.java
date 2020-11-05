package ethos.model.items;

import ethos.model.players.Player;

public class ShopContainer extends ItemContainer implements ItemContainerUI {

	public ShopContainer(int capacity, boolean duplicatesPermitted) {
		super(capacity, duplicatesPermitted);
	}

	@Override
	public void add(GameItem item, int slot) {

	}

	@Override
	public void remove(GameItem item, int slot) {
		super.reduce(item.getId(), item.getAmount(), slot);
	}

	@Override
	public void show(Player player) {
		player.getItems().resetItems(3823);
		player.getPA().sendFrame248(3824, 3822);
		player.getPA().sendFrame126("Test", 3901);
	}

	@Override
	public void update(Player player) {
		player.getOutStream().createFrameVarSizeWord(53);
		player.getOutStream().writeWord(3900);
		player.getOutStream().writeWord(super.capacity - super.getSlotsAvailable());
		for (int slot = 0; slot < super.items.length; slot++) {
			GameItem item = super.items[slot];
			if (item == null) {
				continue;
			}
			if (item.getAmount() > 254) {
				player.getOutStream().writeByte(255);
				player.getOutStream().writeDWord_v2(item.getAmount());
			} else {
				player.getOutStream().writeByte(item.getAmount());
			}
			player.getOutStream().writeWordBigEndianA(item.getId() + 1);
		}
		player.getOutStream().endFrameVarSizeWord();
		player.flushOutStream();
	}

}
