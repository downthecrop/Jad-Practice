package ethos.model.content;

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

/**
 * Revamped a simple means of receiving a random item based on chance.
 * 
 * @author Jason MacKeigan
 * @date Oct 29, 2014, 1:43:44 PM
 */
public class DailySkillBox extends CycleEvent {

	/**
	 * The item id of the mystery box required to trigger the event
	 */
	public static final int MYSTERY_BOX = 20791;

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
				new GameItem(11849, 15 + Misc.random(15)), 
				new GameItem(1518, 50 + Misc.random(25)),
				new GameItem(450, 50 + Misc.random(50)),
				new GameItem(2360, 50 + Misc.random(50)),
				new GameItem(450, 50 + Misc.random(50)),
				new GameItem(2362, 50 + Misc.random(50)),
				new GameItem(264, 25 + Misc.random(25)),
				new GameItem(3001, 25 + Misc.random(25)),
				new GameItem(266, 25 + Misc.random(25)),
				new GameItem(2506, 50 + Misc.random(50)),
				new GameItem(2508, 50 + Misc.random(50)),
				new GameItem(1620, 25 + Misc.random(25)),
				new GameItem(1618, 25 + Misc.random(25)),
				new GameItem(995, 150000),
				new GameItem(2677))
		);
		
	items.put(Rarity.UNCOMMON,
			Arrays.asList(
					new GameItem(11849, 15 + Misc.random(30)), 
					new GameItem(1518, 50 + Misc.random(50)),
					new GameItem(450, 50 + Misc.random(100)),
					new GameItem(2360, 50 + Misc.random(100)),
					new GameItem(450, 50 + Misc.random(100)),
					new GameItem(2362, 50 + Misc.random(100)),
					new GameItem(264, 25 + Misc.random(50)),
					new GameItem(3001, 25 + Misc.random(50)),
					new GameItem(266, 25 + Misc.random(50)),
					new GameItem(2506, 50 + Misc.random(100)),
					new GameItem(2508, 50 + Misc.random(100)),
					new GameItem(1620, 25 + Misc.random(50)),
					new GameItem(1618, 25 + Misc.random(50)),
					new GameItem(995, 300000),
					new GameItem(2801))
	);
		
		items.put(Rarity.RARE,
				Arrays.asList(
						new GameItem(1514, 50 + Misc.random(100)),
						new GameItem(452, 50 + Misc.random(50)),
						new GameItem(2364, 50 + Misc.random(50)),
						new GameItem(1624, 25 + Misc.random(100)),
						new GameItem(2482, 25 + Misc.random(100)),
						new GameItem(268, 25 + Misc.random(100)),
						new GameItem(270, 25 + Misc.random(100)),
						new GameItem(2510, 50 + Misc.random(100)),
						new GameItem(995, 500000),
						new GameItem(2722)));
	}

	/**
	 * The player object that will be triggering this event
	 */
	private Player player;

	/**
	 * Constructs a new myster box to handle item receiving for this player and this player alone
	 * 
	 * @param player the player
	 */
	public DailySkillBox(Player player) {
		this.player = player;
	}

	/**
	 * Opens a mystery box if possible, and ultimately triggers and event, if possible.
	 * 
	 * @param player the player triggering the evnet
	 */
	public void open() {
		if (System.currentTimeMillis() - player.lastMysteryBox < 150 * 4) {
			return;
		}
		if (player.getItems().freeSlots() < 2) {
			player.sendMessage("You need atleast two free slots to open a mystery box.");
			return;
		}
		if (!player.getItems().playerHasItem(MYSTERY_BOX)) {
			player.sendMessage("You need a daily gear box to do this.");
			return;
		}
		player.getItems().deleteItem(MYSTERY_BOX, 1);
		player.lastMysteryBox = System.currentTimeMillis();
		CycleEventHandler.getSingleton().stopEvents(this);
		CycleEventHandler.getSingleton().addEvent(this, this, 2);
	}

	/**
	 * Executes the event for receiving the mystery box
	 */
	@Override
	public void execute(CycleEventContainer container) {
		if (player.disconnected || Objects.isNull(player)) {
			container.stop();
			return;
		}
		int random = Misc.random(10);
		List<GameItem> itemList = random < 5 ? items.get(Rarity.COMMON) : random >= 5 && random <= 8 ? items.get(Rarity.UNCOMMON) : items.get(Rarity.RARE);
		GameItem item = Misc.getRandomItem(itemList);
		GameItem itemDouble = Misc.getRandomItem(itemList);
		
		if (Misc.random(200) == 1) {
			PlayerHandler.executeGlobalMessage("[<col=CC0000>Daily Box</col>] @cr20@ <col=255>" + player.playerName
					+ "</col> hit the jackpot on a Daily Skill Box!");
			switch(Misc.random(21)) {
			case 0:
				player.getItems().addItemUnderAnyCircumstance(1632, 500);
				break;
			case 1:
				player.getItems().addItemUnderAnyCircumstance(13646, 1);
				break;
			case 2:
				player.getItems().addItemUnderAnyCircumstance(13640, 1);
				break;
			case 3:
				player.getItems().addItemUnderAnyCircumstance(13642, 1);
				break;
			case 4:
				player.getItems().addItemUnderAnyCircumstance(13644, 1);
				break;
			case 5:
				player.getItems().addItemUnderAnyCircumstance(10941, 1);
				break;
			case 6:
				player.getItems().addItemUnderAnyCircumstance(10940, 1);
				break;
			case 7:
				player.getItems().addItemUnderAnyCircumstance(10939, 1);
				break;
			case 8:
				player.getItems().addItemUnderAnyCircumstance(10933, 1);
				break;
			case 9:
				player.getItems().addItemUnderAnyCircumstance(12013, 1);
				break;
			case 10:
				player.getItems().addItemUnderAnyCircumstance(12015, 1);
				break;
			case 11:
				player.getItems().addItemUnderAnyCircumstance(12014, 1);
				break;
			case 12:
				player.getItems().addItemUnderAnyCircumstance(12016, 1);
				break;
			case 13:
				player.getItems().addItemUnderAnyCircumstance(19988, 1);
				break;
			case 14:
				player.getItems().addItemUnderAnyCircumstance(20708, 1);
				break;
			case 15:
				player.getItems().addItemUnderAnyCircumstance(20706, 1);
				break;
			case 16:
				player.getItems().addItemUnderAnyCircumstance(20704, 1);
				break;
			case 17:
				player.getItems().addItemUnderAnyCircumstance(20710, 1);
				break;
			case 18:
				player.getItems().addItemUnderAnyCircumstance(13258, 1);
				break;
			case 19:
				player.getItems().addItemUnderAnyCircumstance(13260, 1);
				break;
			case 20:
				player.getItems().addItemUnderAnyCircumstance(13259, 1);
				break;
			case 21:
				player.getItems().addItemUnderAnyCircumstance(13261, 1);
				break;
			}
		}

		if (Misc.random(25) == 0) {
			player.getItems().addItem(item.getId(), item.getAmount());
			player.getItems().addItem(itemDouble.getId(), itemDouble.getAmount());
			player.sendMessage("You receive <col=255>" + item.getAmount() + " x " + ItemAssistant.getItemName(item.getId()) + "</col>.");
			player.sendMessage("You receive <col=255>" + itemDouble.getAmount() + " x " + ItemAssistant.getItemName(itemDouble.getId()) + "</col>.");
		} else {
			player.getItems().addItem(item.getId(), item.getAmount());
			player.sendMessage("You receive <col=255>" + item.getAmount() + " x " + ItemAssistant.getItemName(item.getId()) + "</col>.");
		}
		container.stop();
	}

	/**
	 * Represents the rarity of a certain list of items
	 */
	enum Rarity {
		UNCOMMON, COMMON, RARE
	}

}