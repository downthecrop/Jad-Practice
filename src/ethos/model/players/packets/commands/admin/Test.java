package ethos.model.players.packets.commands.admin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import ethos.Config;
import ethos.event.CycleEvent;
import ethos.event.CycleEventContainer;
import ethos.event.CycleEventHandler;
import ethos.model.content.kill_streaks.Killstreak;
import ethos.model.content.wogw.Wogw;
import ethos.model.holiday.halloween.HalloweenRandomOrder;
import ethos.model.items.Item;
import ethos.model.npcs.NPC;
import ethos.model.npcs.NPCDumbPathFinder;
import ethos.model.npcs.NPCHandler;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.model.players.Right;
import ethos.model.players.RightGroup;
import ethos.model.players.packets.commands.Command;
import ethos.util.Misc;

public class Test extends Command {
	
	private static final int[] rewards = { 20773, 20775, 20777, 20779 };
	
	private enum RankUpgrade {
		CONTRIBUTOR(Right.CONTRIBUTOR, 10), 
		SPONSOR(Right.SPONSOR, 30), 
		SUPPORTER(Right.SUPPORTER, 75), 
		VIP(Right.DONATOR, 150), 
		SUPER_VIP(Right.SUPER_DONATOR, 300), 
		MEGA_VIP(Right.EXTREME_DONATOR, 500), 
		LEGENDARY(Right.LEGENDARY, 1000);

		/**
		 * The rights that will be appended if upgraded
		 */
		private final Right rights;

		/**
		 * The amount required for the upgrade
		 */
		private final int amount;

		private RankUpgrade(Right rights, int amount) {
			this.rights = rights;
			this.amount = amount;
		}
	}

	@Override
	public void execute(Player player, String input) {
		
		switch (input) {
		
		case "tekton":
			player.getPA().startTeleport(3309, 5277, 1, "modern", false);
			break;
		case "mystics":
			player.getPA().startTeleport(3343, 5248, 1, "modern", false);
			break;
		
		case "upgrade":
			ArrayList<RankUpgrade> orderedList = new ArrayList<>(Arrays.asList(RankUpgrade.values()));
			orderedList.sort((one, two) -> Integer.compare(two.amount, one.amount));
			orderedList.stream().filter(r -> player.amDonated >= r.amount).findFirst().ifPresent(rank -> {
				RightGroup rights = player.getRights();
				Right right = rank.rights;
				if (!rights.contains(right)) {
					player.sendMessage("Congratulations, your rank has been upgraded to " + right.toString() + ".");
					player.sendMessage("This rank is hidden, but you will have all it's perks.");
					rights.add(right);
				}
			});
		break;
		
		case "tek":
			NPCHandler.tektonAttack = "SPECIAL";
			System.out.println("Setting attack: " + NPCHandler.tektonAttack);
			break;
			
		case "walk":
			NPC TEKTON = NPCHandler.getNpc(7544);
			NPCDumbPathFinder.walkTowards(TEKTON, 3308, 5296);
			break;
		
		case "placeholder"://might conflict with the other placeholder cmd
			player.placeHolders = !player.placeHolders;
			player.sendMessage("placeholder: " + player.placeHolders);
			break;
		
		case "save":
			Wogw.save();
			break;
			
		case "load":
			Wogw.init();
			break;
		
		case "i":
			player.getPA().loadQuests();
			break;
			
		case "corp":
			player.getPA().walkableInterface(38000);
			break;
			
		case "tele":
			player.getPA().sendFrame126("Slayer Tower", 62112);
			player.getPA().sendFrame126("Lletya", 62119);
			player.getPA().sendFrame126("Mithril Dragons", 62120);
			player.getPA().sendFrame126("Demonic Gorillas", 62121);
			player.getPA().sendFrame126("@cr10@@bla@Boss Locations @cr22@ = @red@Wilderness", 62122);
			player.getPA().sendFrame126("@cr22@King Black Dragon", 62123);
			player.getPA().sendFrame126("@cr22@Chaos Elemental", 62124);
			player.getPA().sendFrame126("@cr22@Kraken", 62125);
			player.getPA().sendFrame126("@cr22@Venenatis", 62126);
			player.getPA().sendFrame126("@cr22@Vetion", 62127);
			player.getPA().sendFrame126("@cr22@Callisto", 62128);
			player.getPA().sendFrame126("@cr22@Giant mole", 62129);
			player.getPA().sendFrame126("@cr22@Barrelchest", 62130);
			player.getPA().sendFrame126("Godwars Dungeon", 62131);
			player.getPA().sendFrame126("Dagannoth Cave", 62132);
			player.getPA().sendFrame126("Lizardman Canyon", 62133);
			player.getPA().sendFrame126("Abyssal Sire", 62134);
			player.getPA().sendFrame126("@cr10@@bla@Minigame Locations", 62135);
			player.getPA().sendFrame126("Pest Control", 62136);
			player.getPA().sendFrame126("Duel Arena", 62137);
			player.getPA().sendFrame126("Fight Caves", 62138);
			player.getPA().sendFrame126("Barrows", 62139);
			player.getPA().sendFrame126("Warriors Guild", 62140);
			player.getPA().sendFrame126("Mage Arena", 62141);
			player.getPA().sendFrame126("Lighthouse", 62142);
			player.getPA().sendFrame126("Recipe For Disaster", 62143);
			player.getPA().sendFrame126("@cr18@@bla@Skill Locations", 62144);
			player.getPA().sendFrame126("Skilling Area (Falador)", 62145);
			player.getPA().sendFrame126("Hunting Grounds (Feldip Hills)", 62146);
			player.getPA().sendFrame126("Woodcutting Guild (Zeah)", 62147);
			player.getPA().sendFrame126("@cr22@@bla@Player Killing Locations", 62148);
			player.getPA().sendFrame126("Wilderness Portals", 62149);
			player.getPA().sendFrame126("West Dragons", 62150);
			player.getPA().sendFrame126("East Dragons", 62151);
			player.getPA().sendFrame126("Hill Giants", 62152);
			player.getPA().sendFrame126("", 62153);
			player.getPA().sendFrame126("", 62154);
			player.getPA().sendFrame126("", 62155);
			player.getPA().showInterface(62100);
			break;
			
		case "pk":
			player.sendMessage("Killstreaks of players online:");
			PlayerHandler.nonNullStream().filter(Objects::nonNull).forEach(a -> {
				//if (a.getKillstreak().getTotalKillstreak() > 0) {
					player.sendMessage("" + a.playerName + " -> " + a.getKillstreak().getTotalKillstreak() +" streak.");
				//}
			});
			break;
			
		case "pkkills":
			player.sendMessage("Kills of players online:");
			PlayerHandler.nonNullStream().filter(Objects::nonNull).forEach(a -> {
					player.sendMessage("" + a.playerName + " -> " + a.killcount +" kills.");
			});
			break;
			
		case "ks":
			player.getKillstreak().increase(Killstreak.Type.HUNTER);
			player.getBH().upgradePlayerEmblem();
			player.getBH().setTotalHunterKills(player.getBH().getTotalHunterKills() + 1);
			break;
		
		case "s":
			for (int i = 0; i < 100; i++) {
				int random = Misc.random(Config.NOT_SHAREABLE.length - 1);
				player.getPA().itemOnInterface(Config.NOT_SHAREABLE[random], i + 1, 36183, i);
			}
			break;
			
		case "check":
			player.getRechargeItems().checkCharges(4151);
			break;
			
		case "use":
			player.getRechargeItems().useItem(4151);
			break;
			
		case "b":
			for (int i = 31011; i < 31069; i++) {
				player.getPA().sendChangeSprite(i, (byte) 0);
				player.getPA().sendFrame126("S:" + (i - 1) + " T:"+ i +"", i);
			}
//			player.getPA().sendChangeSprite(31052, (byte) 1);
			//TeleportationInterface.open(player)
//			player.getPA().sendFrame246(1734, 150, 8007);
//			player.getPA().sendFrame246(1735, 150, 8008);
//			player.getPA().sendFrame246(1736, 150, 8009);
//			player.getPA().sendFrame246(1737, 150, 8010);
//			player.getPA().sendFrame246(1738, 150, 8011);
//			player.getPA().sendFrame246(15348, 150, 8012);
//			player.getPA().sendFrame126("What teleport would you like to create?", 1732);
//			player.getPA().showInterface(205);
			break;
			
		case "bar":
			player.getBarrows().test();
			break;
			
		case "spawn":
			player.getPA().showInterface(62000);
			break;
			
		case "w":
			//int xOffset1, int yOffset1, int xOffset, int yOffset, int speedOne, int speedTwo, String directionSet, int animation
			//player.setForceMovement(3200, 3200, 3200, 3205, 0, 100, "NORTH", 762);
			break;
			
		case "st":
			player.setSidebarInterface(2, 45000);
			break;
			
		case "hat":
			System.out.println("isFullHat; " + Item.isFullHat(3385));
			System.out.println("isFullMask; " + Item.isFullMask(3385));
			break;
			
		case "scare":
			player.getPA().showInterface(18681);
			player.sendMessage("@red@Mwuhahahaha..");
			CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					if (player.disconnected) {
						container.stop();
						return;
					}
					player.getPA().closeAllWindows();
					container.stop();
				}
				@Override
				public void stop() {
					
				}
			}, 5);
			break;
			
		case "hall":
			HalloweenRandomOrder.generateOrder(player);
			break;
			
		case "hallcheck":
			HalloweenRandomOrder.checkOrder(player);
			break;
			
		case "win":
			player.getItems().addItem(rewards[Misc.random(rewards.length - 1)], 1);
			break;
		
		}
	}
}
