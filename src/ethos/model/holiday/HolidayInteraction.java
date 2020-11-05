package ethos.model.holiday;

import ethos.model.players.Player;

public interface HolidayInteraction {

	public abstract boolean clickNpc(Player player, int type, int npcId);

	public abstract boolean clickObject(Player player, int type, int objectId, int x, int y);

	public abstract boolean clickButton(Player player, int buttonId);

	public abstract boolean clickItem(Player player, int itemId);

}
