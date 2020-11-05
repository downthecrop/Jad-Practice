package ethos;

import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import ethos.model.players.Player;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.util.HashedWheelTimer;

import ethos.clip.ObjectDef;
import ethos.clip.Region;
import ethos.clip.doors.DoorDefinition;
import ethos.event.CycleEventHandler;
import ethos.event.EventHandler;
import ethos.event.impl.BonusApplianceEvent;
import ethos.event.impl.SkeletalMysticEvent;
import ethos.event.impl.WheatPortalEvent;
import ethos.model.content.godwars.GodwarsEquipment;
import ethos.model.content.godwars.GodwarsNPCs;
import ethos.model.content.tradingpost.Listing;
import ethos.model.content.trails.CasketRewards;
import ethos.model.content.wogw.Wogw;
import ethos.model.holiday.HolidayController;
import ethos.model.items.ItemDefinition;
import ethos.model.minigames.FightPits;
import ethos.model.minigames.pk_arena.Highpkarena;
import ethos.model.minigames.pk_arena.Lowpkarena;
import ethos.model.multiplayer_session.MultiplayerSessionListener;
import ethos.model.npcs.NPCHandler;
import ethos.model.npcs.drops.DropManager;
import ethos.model.players.PlayerHandler;
import ethos.model.players.PlayerSave;
import ethos.model.players.combat.monsterhunt.MonsterHunt;
import ethos.model.players.packets.Commands;
import ethos.net.PipelineFactory;
import ethos.punishments.PunishmentCycleEvent;
import ethos.punishments.Punishments;
import ethos.server.data.ServerData;
import ethos.util.date.GameCalendar;
import ethos.util.log.Logger;
import ethos.world.ClanManager;
import ethos.world.ItemHandler;
import ethos.world.ShopHandler;
import ethos.world.objects.GlobalObjects;

/**
 * The main class needed to start the server.
 * 
 * @author Sanity
 * @author Graham
 * @author Blake
 * @author Ryan Lmctruck30 Revised by Shawn Notes by Shawn
 */
public class Server {

	private static final Punishments PUNISHMENTS = new Punishments();

	private static final DropManager dropManager = new DropManager();

	/**
	 * A class that will manage game events
	 */
	private static final EventHandler events = new EventHandler();

	/**
	 * Represents our calendar with a given delay using the TimeUnit class
	 */
	private static GameCalendar calendar = new ethos.util.date.GameCalendar(
			new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"), "GMT-3:00");

	private static HolidayController holidayController = new HolidayController();

	private static MultiplayerSessionListener multiplayerSessionListener = new MultiplayerSessionListener();

	private static GlobalObjects globalObjects = new GlobalObjects();

	/**
	 * ClanChat Added by Valiant
	 */
	public static ClanManager clanManager = new ClanManager();

	/**
	 * Sleep mode of the server.
	 */
	public static boolean sleeping;
	/**
	 * The test thingy
	 */
	public static boolean canGiveReward;

	public static long lastReward = 0;
	/**
	 * Calls the rate in which an event cycles.
	 */
	public static final int cycleRate;

	/**
	 * Server updating.
	 */
	public static boolean UpdateServer = false;

	/**
	 * Calls in which the server was last saved.
	 */
	public static long lastMassSave = System.currentTimeMillis();

	private static long sleepTime;

	/**
	 * Forced shutdowns.
	 */
	public static boolean shutdownServer = false;
	public static boolean shutdownClientHandler;

	public static boolean canLoadObjects = false;

	/**
	 * Used to identify the server port.
	 */
	public static int serverlistenerPort;

	/**
	 * Contains data which is saved between sessions.
	 */
	public static ServerData serverData = new ServerData();

	/**
	 * Calls the usage of player items.
	 */
	public static ItemHandler itemHandler = new ItemHandler();

	/**
	 * Handles logged in players.
	 */
	public static PlayerHandler playerHandler = new PlayerHandler();

	/**
	 * Handles global NPCs.
	 */
	public static NPCHandler npcHandler = new NPCHandler();

	/**
	 * Handles global shops.
	 */
	public static ShopHandler shopHandler = new ShopHandler();

	/**
	 * Handles the fightpits minigame.
	 */
	public static FightPits fightPits = new FightPits();

	/**
	 * Handles the main game processing.
	 */
	private static final ScheduledExecutorService GAME_THREAD = Executors.newSingleThreadScheduledExecutor();

	private static final ScheduledExecutorService IO_THREAD = Executors.newSingleThreadScheduledExecutor();

	static {
		serverlistenerPort = Config.SERVER_STATE.getPort();
		cycleRate = 600;
		shutdownServer = false;
		sleepTime = 0;
	}

	private static final Runnable SERVER_TASKS = () -> {
		try {
			itemHandler.process();
			playerHandler.process();
			npcHandler.process();
			shopHandler.process();
			Highpkarena.process();
			Lowpkarena.process();
			globalObjects.pulse();
			CycleEventHandler.getSingleton().process();
			events.process();
			serverData.processQueue();
		} catch (Throwable t) {
			t.printStackTrace();
			t.getCause();
			t.getMessage();
			t.fillInStackTrace();
			System.out.println("Server tasks - Check for error");
			PlayerHandler.stream().filter(Objects::nonNull).forEach(PlayerSave::save);
		}
	};

	private static final Runnable IO_TASKS = () -> {
		try {
			// TODO tasks(players online, etc)
		} catch (Throwable t) {
			t.printStackTrace();
		}
	};

	public static final String AttackAnimation = null;

	public static void main(java.lang.String args[]) {
		try {
			long startTime = System.currentTimeMillis();
			System.setOut(extracted());

			PUNISHMENTS.initialize();
			// events.submit(new DidYouKnowEvent());
			events.submit(new WheatPortalEvent());
			events.submit(new BonusApplianceEvent());
			events.submit(new SkeletalMysticEvent());
			events.submit(new PunishmentCycleEvent(PUNISHMENTS, 50));
			Listing.loadNextSale();
			Wogw.init();
			ItemDefinition.load();
			DoorDefinition.load();
			GodwarsEquipment.load();
			GodwarsNPCs.load();
			dropManager.read();
			CasketRewards.read();
			ObjectDef.loadConfig();
			globalObjects.loadGlobalObjectFile();
			Region.load();
			bindPorts();
			MonsterHunt.spawnNPC();
			holidayController.initialize();
			Runtime.getRuntime().addShutdownHook(new ShutdownHook());
			Commands.initializeCommands();
			long endTime = System.currentTimeMillis();
			long elapsed = endTime - startTime;
			System.out.println(Config.SERVER_NAME + " has successfully started up in " + elapsed + " milliseconds.");
			GAME_THREAD.scheduleAtFixedRate(SERVER_TASKS, 0, 600, TimeUnit.MILLISECONDS);
			IO_THREAD.scheduleAtFixedRate(IO_TASKS, 0, 30, TimeUnit.SECONDS);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static Logger extracted() {
		return new Logger(System.out);
	}

	/**
	 * Gets the sleep mode timer and puts the server into sleep mode.
	 */
	public static long getSleepTimer() {
		return sleepTime;
	}

	public static MultiplayerSessionListener getMultiplayerSessionListener() {
		return multiplayerSessionListener;
	}

	/**
	 * Java connection. Ports.
	 */
	private static void bindPorts() {
		ServerBootstrap serverBootstrap = new ServerBootstrap(
				new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
		serverBootstrap.setPipelineFactory(new PipelineFactory(new HashedWheelTimer()));
		serverBootstrap.bind(new InetSocketAddress(serverlistenerPort));
	}

	public static GameCalendar getCalendar() {
		return calendar;
	}

	public static HolidayController getHolidayController() {
		return holidayController;
	}

	public static ServerData getServerData() {
		return serverData;
	}

	public static GlobalObjects getGlobalObjects() {
		return globalObjects;
	}

	public static EventHandler getEventHandler() {
		return events;
	}

	public static DropManager getDropManager() {
		return dropManager;
	}

	public static Punishments getPunishments() {
		return PUNISHMENTS;
	}

	public static String getStatus() {
		return "IO_THREAD\n" + "\tShutdown? " + IO_THREAD.isShutdown() + "\n" + "\tTerminated? "
				+ IO_THREAD.isTerminated();
	}
}