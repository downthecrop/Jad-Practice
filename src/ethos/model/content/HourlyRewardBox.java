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
public class HourlyRewardBox extends CycleEvent {

	/**
	 * The item id of the mystery box required to trigger the event
	 */
	public static final int MYSTERY_BOX = 11739;

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
				new GameItem(20199),
				new GameItem(20202),
				new GameItem(20205),
				new GameItem(20208),
				new GameItem(20169),
				new GameItem(20172),
				new GameItem(20175),
				new GameItem(20178),
				new GameItem(20181),
				new GameItem(20184),
				new GameItem(20187),
				new GameItem(20190),
				new GameItem(20193),
				new GameItem(20196),
				new GameItem(12956),
				new GameItem(12957),
				new GameItem(12958),
				new GameItem(12959),
				new GameItem(12892),
				new GameItem(12893),
				new GameItem(12894),
				new GameItem(12895),
				new GameItem(12896),
				new GameItem(12887),
				new GameItem(12888),
				new GameItem(12889),
				new GameItem(12890),
				new GameItem(12891),
				new GameItem(12412),
				new GameItem(12379),
				new GameItem(12351),
				new GameItem(12323),
				new GameItem(12321),
				new GameItem(12319),
				new GameItem(12315),
				new GameItem(12317),
				new GameItem(12313),
				new GameItem(12311),
				new GameItem(12309),
				new GameItem(12307),
				new GameItem(12305),
				new GameItem(12303),
				new GameItem(12301),
				new GameItem(12299),
				new GameItem(12297),
				new GameItem(12205),
				new GameItem(12207),
				new GameItem(12209),
				new GameItem(12211),
				new GameItem(12213),
				new GameItem(12235),
				new GameItem(12237),
				new GameItem(12239),
				new GameItem(12241),
				new GameItem(12243),
				new GameItem(12325, 1))
		);
		
	items.put(Rarity.UNCOMMON,
			Arrays.asList(
					new GameItem(20095),
					new GameItem(20098),
					new GameItem(20101),
					new GameItem(20104),
					new GameItem(20107),
					new GameItem(20199),
					new GameItem(20110),
					new GameItem(20080),
					new GameItem(20083),
					new GameItem(20086),
					new GameItem(20089),
					new GameItem(20092),
					new GameItem(20059),
					new GameItem(20053),
					new GameItem(20050),
					new GameItem(20035),
					new GameItem(20038),
					new GameItem(20041),
					new GameItem(20044),
					new GameItem(20047),
					new GameItem(19973),
					new GameItem(19976),
					new GameItem(19979),
					new GameItem(19982),
					new GameItem(19985),
					new GameItem(19958),
					new GameItem(19961),
					new GameItem(19964),
					new GameItem(19967),
					new GameItem(19970),
					new GameItem(13640),
					new GameItem(13642),
					new GameItem(13644),
					new GameItem(13646),
					new GameItem(13258),
					new GameItem(13259),
					new GameItem(13260),
					new GameItem(13261),
					new GameItem(12349),
					new GameItem(12347),
					new GameItem(12345),
					new GameItem(12343),
					new GameItem(12339),
					new GameItem(12341),
					new GameItem(12335),
					new GameItem(12337),
					new GameItem(12277),
					new GameItem(12279),
					new GameItem(12281),
					new GameItem(12283),
					new GameItem(12285),
					new GameItem(12249),
					new GameItem(12251),
					new GameItem(12013),
					new GameItem(12014),
					new GameItem(12015),
					new GameItem(12016),
					new GameItem(11919),
					new GameItem(2615),
					new GameItem(2617),
					new GameItem(2619),
					new GameItem(2621),
					new GameItem(2631),
					new GameItem(2633),
					new GameItem(2653),
					new GameItem(2655),
					new GameItem(2657),
					new GameItem(2659),
					new GameItem(2661),
					new GameItem(2663),
					new GameItem(2665),
					new GameItem(2667),
					new GameItem(2669),
					new GameItem(2671),
					new GameItem(2673),
					new GameItem(2675),
					new GameItem(7390),
					new GameItem(7386),
					new GameItem(7388),
					new GameItem(7392),
					new GameItem(7394),
					new GameItem(7396),
					new GameItem(7398),
					new GameItem(7399),
					new GameItem(7400),
					new GameItem(7401),
					new GameItem(7402),
					new GameItem(7332),
					new GameItem(7334),
					new GameItem(7336),
					new GameItem(7338),
					new GameItem(7340),
					new GameItem(7342),
					new GameItem(7344),
					new GameItem(7346),
					new GameItem(7348),
					new GameItem(7350),
					new GameItem(7352),
					new GameItem(7354),
					new GameItem(7356),
					new GameItem(7358),
					new GameItem(7360),
					new GameItem(7362),
					new GameItem(7364),
					new GameItem(7366),
					new GameItem(7368),
					new GameItem(7370),
					new GameItem(7372),
					new GameItem(7374),
					new GameItem(7376),
					new GameItem(7378),
					new GameItem(7380),
					new GameItem(7382),
					new GameItem(7384),
					
					
					
					new GameItem(12247))
	);
		
		items.put(Rarity.RARE,
				Arrays.asList(
						new GameItem(20020, 1), 
						new GameItem(20023, 1),
						new GameItem(20029),
						new GameItem(20032),
						new GameItem(20005),
						new GameItem(20017),
						new GameItem(20000),
						new GameItem(19994),
						new GameItem(19991),
						new GameItem(19988),
						new GameItem(12518),
						new GameItem(12520),
						new GameItem(12522),
						new GameItem(12524),
						new GameItem(12517),
						new GameItem(12514),
						new GameItem(12441),
						new GameItem(12443),
						new GameItem(12439),
						new GameItem(12434),
						new GameItem(12432),
						new GameItem(12428),
						new GameItem(12419),
						new GameItem(12245),
						new GameItem(12420),
						new GameItem(12421),
						new GameItem(12414),
						new GameItem(12415),
						new GameItem(12416),
						new GameItem(12417),
						new GameItem(12418),
						new GameItem(12397),
						new GameItem(12371),
						new GameItem(12367),
						new GameItem(12365),
						new GameItem(12363),
						new GameItem(12361),
						new GameItem(3481),
						new GameItem(3483),
						new GameItem(3485),
						new GameItem(3486),
						new GameItem(3488),
						new GameItem(12389),
						new GameItem(12391),
						new GameItem(20146),
						new GameItem(20149),
						new GameItem(20152),
						new GameItem(20155),
						new GameItem(20161),
						new GameItem(12359),
						new GameItem(20026, 1)));
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
	public HourlyRewardBox(Player player) {
		this.player = player;
	}

	/**
	 * Opens a mystery box if possible, and ultimately triggers and event, if possible.
	 * 
	 * @param player the player triggering the evnet
	 */
	public void open() {
		if (System.currentTimeMillis() - player.lastMysteryBox < 600 * 4) {
			return;
		}
		if (player.getItems().freeSlots() < 2) {
			player.sendMessage("You need atleast two free slots to open a hourly box.");
			return;
		}
		if (!player.getItems().playerHasItem(MYSTERY_BOX)) {
			player.sendMessage("You need a hourly box to do this.");
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
		//int coins = 500_000 + Misc.random(1_500_000);
		//int coinsDouble = 500_000 + Misc.random(1_500_000);
		int random = Misc.random(100);
		List<GameItem> itemList = random < 55 ? items.get(Rarity.COMMON) : random >= 55 && random <= 80 ? items.get(Rarity.UNCOMMON) : items.get(Rarity.RARE);
		GameItem item = Misc.getRandomItem(itemList);
		GameItem itemDouble = Misc.getRandomItem(itemList);
		
		if (Misc.random(200) == 2 && player.getItems().getItemCount(19730, true) == 0 && player.summonId != 19730) {
			PlayerHandler.executeGlobalMessage("[<col=CC0000>Mbox</col>] @cr20@ <col=255>" + player.playerName
					+ "</col> hit the jackpot and got a <col=CC0000>Rainbow Afro</col> !");
			player.getItems().addItemUnderAnyCircumstance(12430, 1);
		}

		if (Misc.random(10) == 0) {
			//player.getItems().addItem(995, coins + coinsDouble);
			player.getItems().addItem(item.getId(), item.getAmount());
			player.getItems().addItem(itemDouble.getId(), itemDouble.getAmount());
			player.sendMessage("You receive <col=255>" + item.getAmount() + " x " + ItemAssistant.getItemName(item.getId()) + "</col>.");
		player.sendMessage("You receive <col=255>" + itemDouble.getAmount() + " x " + ItemAssistant.getItemName(itemDouble.getId()) + "</col>.");
			PlayerHandler.executeGlobalMessage("<img=10>" + Misc.formatPlayerName(player.playerName) + " just got very lucky and hit the double!");
			PlayerHandler.executeGlobalMessage("<img=10>" + Misc.formatPlayerName(player.playerName) + " has received <col=255>" + item.getAmount() + " x " + ItemAssistant.getItemName(item.getId())
					+ "</col> and <col=255>" + itemDouble.getAmount() + " x " + ItemAssistant.getItemName(itemDouble.getId()) + "</col> from a hourly reward box.");
		} else {
			//player.getItems().addItem(995, coins);
			player.getItems().addItem(item.getId(), item.getAmount());
			player.sendMessage("You receive <col=255>" + item.getAmount() + " x " + ItemAssistant.getItemName(item.getId()) + "</col>.");
			//PlayerHandler.executeGlobalMessage(
			//		"<img=10>" + Misc.formatPlayerName(player.playerName) + " has received <col=255>" + item.getAmount() + " x " + ItemAssistant.getItemName(item.getId()) + "</col> from a hourly reward box.");
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