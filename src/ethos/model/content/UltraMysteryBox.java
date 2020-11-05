package ethos.model.content;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import ethos.event.CycleEvent;
import ethos.event.CycleEventContainer;
import ethos.event.CycleEventHandler;
import ethos.model.items.GameItem;
import ethos.model.items.ItemAssistant;
import ethos.model.items.ItemDefinition;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.util.Misc;

/**
 * Revamped a simple means of receiving a random item based on chance.
 * 
 * @author Jason MacKeigan
 * @date Oct 29, 2014, 1:43:44 PM
 */
public class UltraMysteryBox {

	/**
	 * The item id of the mystery box required to trigger the event
	 */
	public static final int MYSTERY_BOX = 13346;

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
				new GameItem(4716),
				new GameItem(4724),
				new GameItem(4745),
				new GameItem(4753),
				new GameItem(4708),
				new GameItem(4732),
				new GameItem(4720),
				new GameItem(4728),
				new GameItem(4749),
				new GameItem(4757),
				new GameItem(4712),
				new GameItem(4736),
				new GameItem(4722),
				new GameItem(4730),
				new GameItem(4751),
				new GameItem(4759),
				new GameItem(4714),
				new GameItem(4738),
				new GameItem(4718),
				new GameItem(4726),
				new GameItem(4747),
				new GameItem(4755),
				new GameItem(4710),
				new GameItem(4734),
				new GameItem(11804),
				new GameItem(11808),
				new GameItem(11806),
				new GameItem(6585),
				new GameItem(13229),
				new GameItem(13227),
				new GameItem(13231),
				new GameItem(4225),
				new GameItem(11838),
				new GameItem(21298),
				new GameItem(21301),
				new GameItem(21298),
				new GameItem(21304),
				new GameItem(11771),
				new GameItem(11770),
				new GameItem(11773),
				new GameItem(11772),
				new GameItem(2577),
				new GameItem(19550, 1))
		);
		
	items.put(Rarity.UNCOMMON,
			Arrays.asList(
					new GameItem(12006),
					new GameItem(19481),
					new GameItem(12929),
					new GameItem(13198),
					new GameItem(13196),
					new GameItem(19547),
					new GameItem(19544),
					new GameItem(12900),
					new GameItem(11785),
					new GameItem(21012),
					new GameItem(11785),
					new GameItem(11283),
					new GameItem(13271),
					new GameItem(21633),
					new GameItem(11832),
					new GameItem(11834),
					new GameItem(11828),
					new GameItem(11830),
					new GameItem(11802),
					new GameItem(21003),
					new GameItem(19553))
	);
		
		items.put(Rarity.RARE,
				Arrays.asList(
						new GameItem(20997),
						new GameItem(1038),
						new GameItem(1040),
						new GameItem(1042),
						new GameItem(1044),
						new GameItem(1046),
						new GameItem(1048),
						new GameItem(11862),
						new GameItem(11863),
						new GameItem(12399),
						new GameItem(20784),
						new GameItem(12817),
						new GameItem(13343),
						new GameItem(13344),
						new GameItem(11847),
						new GameItem(11863),
						new GameItem(5521),
						new GameItem(9104),
						new GameItem(1053),
						new GameItem(1055),
						new GameItem(1057),
						new GameItem(1050),
						new GameItem(9959, 1)));
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
	public UltraMysteryBox(Player player) {
		this.player = player;
	}

	/**
	 * Can the player open the mystery box
	 */
	private boolean canMysteryBox = true;
	
	/**
	 * The prize received
	 */
	private int mysteryPrize;

	private int mysteryAmount;
	
	private int spinNum = 0;
	
	/**
	 * The chance to obtain the item
	 */
	private int random;
	
	private final int INTERFACE_ID = 47000;
	private final int ITEM_FRAME = 47101;
	
	/**
	 * The rarity of the reward
	 */
	private Rarity rewardRarity;

	/**
	 * Represents the rarity of a certain list of items
	 */
	enum Rarity {
		UNCOMMON("<col=005eff>"),
		COMMON("<col=336600>"),
		RARE("<col=B80000>");
		
		private String color;
		
		Rarity(String color) {
			this.color = color;
		}
		
		public String getColor() {
			return color;
		}
		
	    public static Rarity forId(int id) {
	        for (Rarity tier : Rarity.values()) {
	            if (tier.ordinal() == id)
	                return tier;
	        }
	        return null;
	    }
	}
	
	public void spin() {
		// Server side checks for spin
		if (!canMysteryBox) {
			player.sendMessage("Please finish your current spin.");
			return;
		}
		if (!player.getItems().playerHasItem(MYSTERY_BOX)) {
			player.sendMessage("You require a mystery box to do this.");
			return;
		}

		// Delete box
		player.getItems().deleteItem(MYSTERY_BOX, 1);
		// Initiate spin
		player.sendMessage(":resetBox");
		for (int i=0; i<66; i++){
			player.getPA().mysteryBoxItemOnInterface(-1, 1, ITEM_FRAME, i);
		}
		spinNum = 0;
		player.sendMessage(":spin");
		process();
	}
	
	public void process() {
		// Reset prize
		mysteryPrize = -1;

		mysteryAmount = -1;
		// Can't spin when already in progress
		canMysteryBox = false;
		
		random = Misc.random(100);
		List<GameItem> itemList = random < 40 ? items.get(Rarity.COMMON) : random >= 40 && random <= 95 ? items.get(Rarity.UNCOMMON) : items.get(Rarity.RARE);
		rewardRarity = random < 40 ? Rarity.COMMON : random >= 40 && random <= 95 ? Rarity.UNCOMMON : Rarity.RARE;
		
		GameItem item = Misc.getRandomItem(itemList);

		mysteryPrize = item.getId();

		mysteryAmount = item.getAmount();

		// Send items to interface
		// Move non-prize items client side if you would like to reduce server load
		if (spinNum == 0) {
			for (int i=0; i<66; i++){
				Rarity notPrizeRarity = Rarity.values()[new Random().nextInt(Rarity.values().length)];
				GameItem NotPrize =Misc.getRandomItem(items.get(notPrizeRarity));
				final int NOT_PRIZE_ID = NotPrize.getId();
				final int NOT_PRIZE_AMOUNT = NotPrize.getAmount();
				sendItem(i, 55, mysteryPrize, NOT_PRIZE_ID,1);
			}
		} else {
			for (int i=spinNum*50 + 16; i<spinNum*50 + 66; i++){
				Rarity notPrizeRarity = Rarity.values()[new Random().nextInt(Rarity.values().length)];	
				final int NOT_PRIZE_ID = Misc.getRandomItem(items.get(notPrizeRarity)).getId();
				sendItem(i, (spinNum+1)*50 + 5, mysteryPrize, NOT_PRIZE_ID, mysteryAmount);
			}
		}
		spinNum++;
	}
	
	public void reward() {
		if (mysteryPrize == -1) {
			return;
		}
		
		//player.boxCurrentlyUsing = -1;
		
		player.getItems().addItemUnderAnyCircumstance(mysteryPrize, mysteryAmount);
		
		// Reward text colour
		String tier = rewardRarity.getColor();
		
		// Reward message
		String name = ItemDefinition.forId(mysteryPrize).getName();
		if (name.substring(name.length() - 1).equals("s")) {
			player.sendMessage("Congratulations, you have won " + tier + name + "@bla@!");
		}
		else {
			player.sendMessage("Congratulations, you have won a " + tier + name + "@bla@!");
		}
		
		if (random > 95) {
			PlayerHandler.executeGlobalMessage("[<col=CC0000>Ultra</col>] <col=255>" + Misc.formatPlayerName(player.playerName)
					+ "</col> hit the jackpot and got a <col=CC0000>"+name+"</col> !");
		}
		
		// Can now spin again
		canMysteryBox = true;
	}
	
	public void sendItem(int i, int prizeSlot, int PRIZE_ID, int NOT_PRIZE_ID, int amount) {
		if (i == prizeSlot) {
			player.getPA().mysteryBoxItemOnInterface(PRIZE_ID, amount, ITEM_FRAME, i);
		}
		else {
			player.getPA().mysteryBoxItemOnInterface(NOT_PRIZE_ID, amount, ITEM_FRAME, i);
		}
	}
	
	public void openInterface() {
		player.boxCurrentlyUsing = MYSTERY_BOX;
		// Reset interface
		player.sendMessage(":resetBox");
		for (int i=0; i<66; i++){
			player.getPA().mysteryBoxItemOnInterface(-1, 1, ITEM_FRAME, i);
		}
		spinNum = 0;
		// Open
		player.getPA().sendString("Ultra Mystery Box", 47002);
		player.getPA().showInterface(INTERFACE_ID);
	}

}