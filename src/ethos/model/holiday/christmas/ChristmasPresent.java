package ethos.model.holiday.christmas;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import ethos.event.CycleEvent;
import ethos.event.CycleEventContainer;
import ethos.event.CycleEventHandler;
import ethos.model.items.GameItem;
import ethos.model.items.ItemAssistant;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.util.Misc;

public class ChristmasPresent extends CycleEvent {

	/**
	 * The item id of the christmas present required to trigger the event
	 */
	public static final int PRESENT = 6542;

	/**
	 * A map containing a List of {@link GameItem}'s that contain items relevant to their rarity.
	 */
	private static Map<Rarity, List<GameItem>> items = new HashMap<>();

	/**
	 * Stores an array of items into each map with the corresponding rarity to the list
	 */
	static {
		items.put(Rarity.COMMON, 
			Arrays.asList(
				new GameItem(12890, 1), 
				new GameItem(12891, 1), 
				new GameItem(12895, 1), 
				new GameItem(12896, 1))
		);
		
	items.put(Rarity.UNCOMMON,
			Arrays.asList(
					new GameItem(1050), 
					new GameItem(12887), 
					new GameItem(12888), 
					new GameItem(12889), 
					new GameItem(12892), 
					new GameItem(12893), 
					new GameItem(12894))
			);
	
	items.put(Rarity.RARE,
			Arrays.asList(
					new GameItem(11863, 1))
			);
}

	/**
	 * The player object that will be triggering this event
	 */
	private Player player;

	/**
	 * Constructs a new christmas present to handle item receiving for this player and this player alone
	 * 
	 * @param player the player
	 */
	public ChristmasPresent(Player player) {
		this.player = player;
	}

	/**
	 * Random action of obtaining a santa jr pet
	 */
	private void randomPet() {
		if (Misc.random(4) == 1 && player.getItems().getItemCount(9958, true) == 0 && player.summonId != 9958) {
			PlayerHandler.executeGlobalMessage("[<col=CC0000>News</col>] @cr20@ <col=255>" + player.playerName + "</col> hit the jackpot and got a <col=CC0000>Santa Jr</col> pet from a christmas present!");
			player.getItems().addItemUnderAnyCircumstance(9958, 1);
		}
	}	
	
	/**
	 * Opens a christmas present if possible, and ultimately triggers and event, if possible.
	 * 
	 * @param player the player triggering the event
	 */
	public void open() {
		if (System.currentTimeMillis() - player.lastMysteryBox < 600 * 2) {
			return;
		}
		if (player.getItems().freeSlots() < 2) {
			player.sendMessage("You need atleast two free slots to open a christmas present.");
			return;
		}
		if (!player.getItems().playerHasItem(PRESENT)) {
			player.sendMessage("You need a christmas present to do this.");
			return;
		}
		randomPet();
		player.getItems().deleteItem(PRESENT, 1);
		player.lastMysteryBox = System.currentTimeMillis();
		CycleEventHandler.getSingleton().stopEvents(this);
		CycleEventHandler.getSingleton().addEvent(this, this, 2);
	}

	/**
	 * Executes the event for receiving the christmas present
	 */
	@Override
	public void execute(CycleEventContainer container) {
		if (player.disconnected || Objects.isNull(player)) {
			container.stop();
			return;
		}
		int random = Misc.random(100);
		List<GameItem> itemList = random < 51 ? items.get(Rarity.COMMON) : random > 50 && random < 91 ? items.get(Rarity.UNCOMMON) : items.get(Rarity.RARE);
		GameItem item = Misc.getRandomItem(itemList);
			player.getItems().addItem(item.getId(), item.getAmount());
			player.sendMessage("You receive <col=255>" + item.getAmount() + "x " + ItemAssistant.getItemName(item.getId()) + "</col>.");
			PlayerHandler.executeGlobalMessage("<img=10>" + player.playerName + " has received <col=255>" + item.getAmount() + "x " + ItemAssistant.getItemName(item.getId()) + "</col> from a christmas present.");
			container.stop();
	}

	/**
	 * Represents the rarity of a certain list of items
	 */
	enum Rarity {
		UNCOMMON, COMMON, RARE
	}

}