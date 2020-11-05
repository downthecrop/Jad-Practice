package ethos.model.content;

import java.util.*;

import ethos.event.CycleEvent;
import ethos.event.CycleEventContainer;
import ethos.event.CycleEventHandler;
import ethos.model.items.GameItem;
import ethos.model.items.ItemAssistant;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.model.players.skills.hunter.impling.ItemRarity;
import ethos.util.Misc;

/**
 * Revamped a simple means of receiving a random item based on chance.
 *
 * @author Jason MacKeigan
 * @date Oct 29, 2014, 1:43:44 PM
 */
public class MysteryBox extends CycleEvent {

    /**
     * The item id of the mystery box required to trigger the event
     */
    private static final int MYSTERY_BOX = 6199;

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
                		new GameItem(4566),
                        new GameItem(452, 45),
                        new GameItem(6739, 1),
                        new GameItem(71, 20),
                        new GameItem(12439),
                        new GameItem(12397),
                        new GameItem(452, 500),
                        new GameItem(2364, 350),
                        new GameItem(12412),
                        new GameItem(12357),
                        new GameItem(13442, 75),
                        new GameItem(11937, 75),
                        new GameItem(12351),
                        new GameItem(1419),
                        new GameItem(1305, 1),
                        new GameItem(11235, 1),
                        new GameItem(4084),
                        new GameItem(4212),
                        new GameItem(4708),
                        new GameItem(4710),
                        new GameItem(4712),
                        new GameItem(4714),
                        new GameItem(11840),
                        new GameItem(11731, 1),
                        new GameItem(12848),
                        new GameItem(2513),
                        new GameItem(9740),
                        new GameItem(11230, 40),
                        new GameItem(811, 50),
                        new GameItem(11128),
                        new GameItem(11130),
                        new GameItem(4716),
                        new GameItem(4718),
                        new GameItem(4720),
                        new GameItem(4722),
                        new GameItem(4724),
                        new GameItem(4726),
                        new GameItem(4728),
                        new GameItem(4730),
                        new GameItem(4732),
                        new GameItem(4734),
                        new GameItem(4736),
                        new GameItem(4738),
                        new GameItem(4742),
                        new GameItem(4745),
                        new GameItem(7158),
                        new GameItem(4747),
                        new GameItem(4749),
                        new GameItem(1514, 50),
                        new GameItem(1516, 50),
                        new GameItem(1518, 60),
                        new GameItem(11944, 25),
                        new GameItem(1520, 70),
                        new GameItem(4751),
                        new GameItem(4753),
                        new GameItem(19670, 25),
                        new GameItem(4755),
                        new GameItem(4757),
                        new GameItem(4759),
                        new GameItem(4224),
                        new GameItem(12432),
                        new GameItem(12422),
                        new GameItem(12424),
                        new GameItem(12426),
                        new GameItem(12379),
                        new GameItem(220, 25),
                        new GameItem(12391),
                        new GameItem(12849),
                        new GameItem(4151),
                        new GameItem(12786),
                        new GameItem(12783),
                        new GameItem(1615, 1),
                        new GameItem(12798),
                        new GameItem(13307, 200),
                        new GameItem(2996, 400),
                        new GameItem(12337))
        );

        items.put(Rarity.UNCOMMON,
                Arrays.asList(
                        new GameItem(4566),
                        new GameItem(452, 45),
                        new GameItem(6739, 1),
                        new GameItem(71, 20),
                        new GameItem(12439),
                        new GameItem(12397),
                        new GameItem(452, 500),
                        new GameItem(2364, 350),
                        new GameItem(12412),
                        new GameItem(12357),
                        new GameItem(13442, 75),
                        new GameItem(11937, 75),
                        new GameItem(12351),
                        new GameItem(1419),
                        new GameItem(1305, 1),
                        new GameItem(11235, 1),
                        new GameItem(4084),
                        new GameItem(4212),
                        new GameItem(4708),
                        new GameItem(4710),
                        new GameItem(4712),
                        new GameItem(4714),
                        new GameItem(11840),
                        new GameItem(11731, 1),
                        new GameItem(12848),
                        new GameItem(2513),
                        new GameItem(9740),
                        new GameItem(11230, 40),
                        new GameItem(811, 50),
                        new GameItem(11128),
                        new GameItem(11130),
                        new GameItem(4716),
                        new GameItem(4718),
                        new GameItem(4720),
                        new GameItem(4722),
                        new GameItem(4724),
                        new GameItem(4726),
                        new GameItem(4728),
                        new GameItem(4730),
                        new GameItem(4732),
                        new GameItem(4734),
                        new GameItem(4736),
                        new GameItem(4738),
                        new GameItem(4742),
                        new GameItem(4745),
                        new GameItem(7158),
                        new GameItem(4747),
                        new GameItem(4749),
                        new GameItem(1514, 50),
                        new GameItem(1516, 50),
                        new GameItem(1518, 60),
                        new GameItem(11944, 25),
                        new GameItem(1520, 70),
                        new GameItem(4751),
                        new GameItem(4753),
                        new GameItem(19670, 25),
                        new GameItem(4755),
                        new GameItem(4757),
                        new GameItem(4759),
                        new GameItem(4224),
                        new GameItem(12432),
                        new GameItem(12422),
                        new GameItem(12424),
                        new GameItem(12426),
                        new GameItem(12379),
                        new GameItem(220, 25),
                        new GameItem(12391),
                        new GameItem(12849),
                        new GameItem(4151),
                        new GameItem(12786),
                        new GameItem(12783),
                        new GameItem(1615, 1),
                        new GameItem(12798),
                        new GameItem(13307, 200),
                        new GameItem(2996, 400),
                        new GameItem(12337))
        );

        items.put(Rarity.RARE,
                Arrays.asList(
                        new GameItem(11818, 1),
                        new GameItem(995, 10000000),
                        new GameItem(2996, 600),
                        new GameItem(12437, 1),
                        new GameItem(12424, 1),
                        new GameItem(12426, 1),
                        new GameItem(12422, 1),
                        new GameItem(12359, 1),
                        new GameItem(12849, 1),
                        new GameItem(12802, 1),
                        new GameItem(12800, 1),
                        new GameItem(12798, 1),
                        new GameItem(2697, 1),
                        new GameItem(12373, 1)));
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
    public MysteryBox(Player player) {
        this.player = player;
    }

    /**
     * Opens a mystery box if possible, and ultimately triggers and event, if possible.
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
            player.sendMessage("You need a mystery box to do this.");
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
        int coins = 200_000 + Misc.random(1_500_000);
        int coinsDouble = 200_000 + Misc.random(1_500_000);
        int random = Misc.random(200);
        List<GameItem> itemList = random < 105 ? items.get(Rarity.COMMON) : random >= 105 && random <= 190 ? items.get(Rarity.UNCOMMON) : items.get(Rarity.RARE);
        GameItem item = Misc.getRandomItem(itemList);
        GameItem itemDouble = Misc.getRandomItem(itemList);

        if (Misc.random(200) == 2 && player.getItems().getItemCount(19730, true) == 0 && player.summonId != 19730) {
            PlayerHandler.executeGlobalMessage("@red@" + player.playerName
                    + " has found a Bloodhound!");
            player.getItems().addItemUnderAnyCircumstance(19730, 1);
        }

        if (Misc.random(10) == 0) {
            player.getItems().addItem(995, coins + coinsDouble);
            player.getItems().addItem(item.getId(), item.getAmount());
            player.getItems().addItem(itemDouble.getId(), itemDouble.getAmount());
            player.sendMessage("You receive " + item.getAmount() + " x " + ItemAssistant.getItemName(item.getId()) + ", and "
                    + Misc.insertCommas(Integer.toString(coins)) + " coins.");
            player.sendMessage("You receive " + itemDouble.getAmount() + " x " + ItemAssistant.getItemName(itemDouble.getId()) + ", and "
                    + Misc.insertCommas(Integer.toString(coins)) + " coins.");
           
        } else {
            player.getItems().addItem(995, coins);
            player.getItems().addItem(item.getId(), item.getAmount());
            player.sendMessage("You receive " + item.getAmount() + " x " + ItemAssistant.getItemName(item.getId()) + ", and "
                    + Misc.insertCommas(Integer.toString(coins)) + " coins.");
            for(Map.Entry<Rarity, List<GameItem>> gift : items.entrySet())
                for(GameItem gift_item : gift.getValue())
                    if(gift_item == item)
                        if(gift.getKey() == Rarity.RARE)
                            PlayerHandler.executeGlobalMessage(
                                    "@red@" + Misc.capitalize(player.playerName) + " received a rare item: "
                                            + (item.getAmount() > 1 ? (item.getAmount() + "x ") : ItemAssistant.getItemName(item.getId()) + " from a mystery box."));
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