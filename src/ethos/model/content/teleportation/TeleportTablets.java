package ethos.model.content.teleportation;

import ethos.Config;
import ethos.event.CycleEvent;
import ethos.event.CycleEventContainer;
import ethos.event.CycleEventHandler;
import ethos.model.players.Player;

/**
 * 
 * @author Mack
 *
 */
public class TeleportTablets {

	public enum TabType {
		HOME(8013, Config.RESPAWN_X, Config.RESPAWN_Y), 
		ANNAKARL(12775, Config.ANNAKARL_X, Config.ANNAKARL_Y), 
		CARRALLANGER(12776, Config.CARRALLANGAR_X, Config.CARRALLANGAR_Y), 
		DAREEYAK(12777, Config.DAREEYAK_X, Config.DAREEYAK_Y), 
		GHORROCK(12778, Config.GHORROCK_X, Config.GHORROCK_Y), 
		KHARYRLL(12779, Config.KHARYRLL_X, Config.KHARYRLL_Y), 
		LASSAR(12780, Config.LASSAR_X, Config.LASSAR_Y), 
		PADDEWWA(12781, Config.PADDEWWA_X, Config.PADDEWWA_Y), 
		SENNTISTEN(12782, Config.SENNTISTEN_X, Config.SENNTISTEN_Y), 
		WILDY_RESOURCE(12409, 3184, 3945), 
		PIRATE_HUT(12407, 3045, 3956), 
		MAGE_BANK(12410, 2538, 4716), 
		CALLISTO(12408, 3325, 3849), 
		KBD_LAIR(12411, 2271, 4681),
		
		//City teleports
		VARROCK(8007, Config.VARROCK_X, Config.VARROCK_Y),
		LUMBRIDGE(8008, Config.LUMBY_X, Config.LUMBY_Y),
		FALADOR(8009, Config.FALADOR_X, Config.FALADOR_Y),
		CAMELOT(8010, Config.CAMELOT_X, Config.CAMELOT_Y),
		ARDOUGNE(8011, Config.ARDOUGNE_X, Config.ARDOUGNE_Y),
		TROLLHEIM(11747, Config.TROLLHEIM_X, Config.TROLLHEIM_Y);
		
		
		private int tab;
		private int x;
		private int y;

		public int getTabId() {
			return tab;
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}

		TabType(int tab, int x, int y) {
			this.tab = tab;
			this.x = x;
			this.y = y;
		}
	};

	/**
	 * Operates the teleport tab
	 * 
	 * @param player
	 * @param item
	 */
	public static void operate(final Player player, int item) {
		for (TabType type : TabType.values()) {
			if (type.getTabId() == item) {
				if (System.currentTimeMillis() - player.lastTeleport < 3500)
					return;	
				if (!player.getPA().canTeleport("modern")) {
					return;
				}
				player.teleporting = true;
				player.getItems().deleteItem(type.getTabId(), 1);
				player.lastTeleport = System.currentTimeMillis();
				player.startAnimation(4731);
				player.gfx0(678);
				final int x = type.getX();
				final int y = type.getY();
				CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {

					@Override
					public void execute(CycleEventContainer container) {
						player.teleportToX = x;
						player.teleportToY = y;
						player.heightLevel = 0;
						player.teleporting = false;
						player.gfx0(-1);
						player.startAnimation(65535);
						container.stop();
					}

				}, 3);
			}
		}
	}

}
