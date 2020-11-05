package ethos.model.content;

import java.util.*;

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
public class PvmCasket extends CycleEvent {

	/**
	 * The item id of the PvM Casket required to trigger the event
	 */
	public static final int MYSTERY_BOX = 405; //Casket

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
					new GameItem(454, 150), 
					new GameItem(1079),
					new GameItem(1077),
					new GameItem(1075), 
					new GameItem(1071), 
					new GameItem(1073), 
					new GameItem(6106),
					new GameItem(6107),
					new GameItem(6108),
					new GameItem(6109),
					new GameItem(6110),
					new GameItem(6111),
					new GameItem(2550),
					new GameItem(1127), 
					new GameItem(995, 10000),
					new GameItem(1125), 
					new GameItem(1123), 
					new GameItem(1121), 
					new GameItem(987),
					new GameItem(1333),
					new GameItem(7947, 100), 
					new GameItem(1331), 
					new GameItem(1381), 
					new GameItem(1383), 
					new GameItem(1385), 
					new GameItem(1387), 
					new GameItem(1329), 
					new GameItem(1327), 
					new GameItem(1325),
					new GameItem(1323),
					new GameItem(6128),
					new GameItem(6129),
					new GameItem(6130),
					new GameItem(6131),
					new GameItem(6133),
					new GameItem(6135),
					new GameItem(6137),
					new GameItem(6143),
					new GameItem(6149),
					new GameItem(1321),
					new GameItem(1319), 
					new GameItem(1317), 
					new GameItem(1315),
					new GameItem(6185), 
					new GameItem(6322),
					new GameItem(6324),
					new GameItem(6326),
					new GameItem(6328),
					new GameItem(6330),
					new GameItem(5667),
					new GameItem(5668),
					new GameItem(536, 10), 
					new GameItem(6181), 
					new GameItem(6179), 
					new GameItem(1725),
					new GameItem(1727),
					new GameItem(1729),
					new GameItem(1731),
					new GameItem(1704),
					new GameItem(527, 50), 
					new GameItem(114, 20), 
					new GameItem(128, 20), 
					new GameItem(4129),
					new GameItem(4127),
					new GameItem(4125),
					new GameItem(4123),
					new GameItem(4121),
					new GameItem(1215, 1), 
					new GameItem(134, 20), 
					new GameItem(3145, 100), 
					new GameItem(6177), 
					new GameItem(985, 1))
					
			);
			
		items.put(Rarity.UNCOMMON,
				Arrays.asList(
						new GameItem(4566),
						new GameItem(4587), 
						new GameItem(861),
						new GameItem(158, 25),
						new GameItem(164, 25),
						new GameItem(146, 25),
						new GameItem(140, 25),
						new GameItem(537, 50),
						new GameItem(859),
						new GameItem(4131),
						new GameItem(538, 50),
						new GameItem(5698, 1),
						new GameItem(6568),
						new GameItem(4089),
						new GameItem(4091),
						new GameItem(4093),
						new GameItem(4095),
						new GameItem(4097),
						new GameItem(4099),
						new GameItem(4100),
						new GameItem(4102),
						new GameItem(4675),
						new GameItem(4104),
						new GameItem(4106),
						new GameItem(2368),
						new GameItem(4108),
						new GameItem(6536),
						new GameItem(6524),
						new GameItem(6525),
						new GameItem(6526),
						new GameItem(6522, 200),
						new GameItem(6527),			
						new GameItem(11937, 100), 
						new GameItem(386, 100), 
						new GameItem(1409),
						new GameItem(140, 20), 
						new GameItem(11876, 100),
						new GameItem(995, 100000))
						
		);
			
			items.put(Rarity.RARE,
					Arrays.asList(
							new GameItem(4566), 
							new GameItem(990, 10),
							new GameItem(4224), 
							new GameItem(3751),
							new GameItem(3753),
							new GameItem(3755),
							new GameItem(6577),
							new GameItem(2572),
							new GameItem(6528),
							new GameItem(9242, 50),
							new GameItem(9244, 50),
							new GameItem(9245, 50),
							new GameItem(11212, 50),
							new GameItem(4212), 
							new GameItem(535, 100), 
							new GameItem(4153, 1),
							new GameItem(11840),
							new GameItem(2366),
							new GameItem(4151, 1),
							new GameItem(995, 250000),
							new GameItem(6199, 1),
							new GameItem(11818, 1)));
							
							
	}

	/**
	 * The player object that will be triggering this event
	 */
	private Player player;

	/**
	 * Constructs a new PvM Casket to handle item receiving for this player and this player alone
	 * 
	 * @param player the player
	 */
	public PvmCasket(Player player) {
		this.player = player;
	}

	/**
	 * Opens a PvM Casket if possible, and ultimately triggers and event, if possible.
	 *
	 */
	public void open() {
		if (System.currentTimeMillis() - player.lastMysteryBox < 150 * 4) {
			return;
		}
		if (player.getItems().freeSlots() < 2) {
			player.sendMessage("You need atleast two free slots to open a PvM Casket.");
			return;
		}
		if (!player.getItems().playerHasItem(MYSTERY_BOX)) {
			player.sendMessage("You need PvM Casket to do this.");
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
		int coins = 50000 + Misc.random(15000);
		int coinsDouble = 100000 + Misc.random(50000);
		int random = Misc.random(100);
		List<GameItem> itemList = random < 55 ? items.get(Rarity.COMMON) : random >= 55 && random <= 80 ? items.get(Rarity.UNCOMMON) : items.get(Rarity.RARE);
		GameItem item = Misc.getRandomItem(itemList);
		GameItem itemDouble = Misc.getRandomItem(itemList);

		if (Misc.random(10) == 0) {
			player.getItems().addItem(995, coins + coinsDouble);
			player.getItems().addItem(item.getId(), item.getAmount());
			player.getItems().addItem(itemDouble.getId(), itemDouble.getAmount());
			player.sendMessage("You receive <col=255>" + item.getAmount() + " x " + ItemAssistant.getItemName(item.getId()) + "</col>, and <col=255>"
					+ Misc.insertCommas(Integer.toString(coins)) + "</col>GP.");
			player.sendMessage("You receive <col=255>" + itemDouble.getAmount() + " x " + ItemAssistant.getItemName(itemDouble.getId()) + "</col>, and <col=255>"
					+ Misc.insertCommas(Integer.toString(coins)) + "</col>GP.");
			PlayerHandler.executeGlobalMessage("<img=10>" + Misc.formatPlayerName(player.playerName) + " just got very lucky and hit the double!");
			PlayerHandler.executeGlobalMessage("<img=10>" + Misc.formatPlayerName(player.playerName) + " has received <col=255>" + item.getAmount() + " x " + ItemAssistant.getItemName(item.getId())
					+ "</col> and <col=255>" + itemDouble.getAmount() + " x " + ItemAssistant.getItemName(itemDouble.getId()) + "</col> from a PvM Casket.");
		} else {
			player.getItems().addItem(995, coins);
			player.getItems().addItem(item.getId(), item.getAmount());
			player.sendMessage("You receive <col=255>" + item.getAmount() + " x " + ItemAssistant.getItemName(item.getId()) + "</col>, and <col=255>"
					+ Misc.insertCommas(Integer.toString(coins)) + "</col>GP.");
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