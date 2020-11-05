package ethos.model.npcs.bosses.zulrah;

import com.google.common.base.Stopwatch;

import ethos.Server;
import ethos.event.CycleEvent;
import ethos.event.CycleEventContainer;
import ethos.event.CycleEventHandler;
import ethos.model.content.instances.InstancedArea;
import ethos.model.content.instances.InstancedAreaManager;
import ethos.model.content.instances.SingleInstancedArea;
import ethos.model.content.instances.impl.SingleInstancedZulrah;
import ethos.model.npcs.NPC;
import ethos.model.npcs.bosses.zulrah.impl.*;
import ethos.model.players.Boundary;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.model.players.combat.CombatType;
import ethos.model.players.packets.commands.owner.Object;
import ethos.server.data.SerializablePair;
import ethos.util.Misc;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Zulrah {

	/**
	 * The minion snake npc id
	 */
	public static final int SNAKELING = 2045;

	/**
	 * The relative lock for this event
	 */
	private final Object EVENT_LOCK = new Object();

	/**
	 * The player associated with this event
	 */
	private final Player player;

	/**
	 * The single instance of zulrah
	 */
	private SingleInstancedArea zulrahInstance;

	/**
	 * The boundary of zulrah's location
	 */
	public static final Boundary BOUNDARY = new Boundary(2248, 3059, 2283, 3084);

	/**
	 * The zulrah npc
	 */
	private NPC npc;

	/**
	 * The current stage of zulrah
	 */
	private int stage;

	/**
	 * Determines if the npc is transforming or not.
	 */
	private boolean transforming;

	/**
	 * The stopwatch for tracking when the zulrah npc fight starts.
	 */
	private Stopwatch stopwatch = Stopwatch.createUnstarted();

	/**
	 * A mapping of all the stages
	 */
	private Map<Integer, ZulrahStage> stages = new HashMap<>();

	/**
	 * Creates a new Zulrah event for the player
	 * 
	 * @param player the player
	 */
	public Zulrah(Player player) {
		this.player = player;
		stages.put(0, new SpawnZulrahStageZero(this, player));
		stages.put(1, new CreateToxicStageOne(this, player));
		stages.put(2, new MeleeStageTwo(this, player));
		stages.put(3, new MageStageThree(this, player));
		stages.put(4, new RangeStageFour(this, player));
		stages.put(5, new MageStageFive(this, player));
		stages.put(6, new MeleeStageSix(this, player));
		stages.put(7, new RangeStageSeven(this, player));
		stages.put(8, new MageStageEight(this, player));
		stages.put(9, new RangeStageNine(this, player));
		stages.put(10, new MeleeStageTen(this, player));
		stages.put(11, new RangeStageEleven(this, player));
	}

	public void initialize() {
		if (zulrahInstance != null) {
			InstancedAreaManager.getSingleton().disposeOf(zulrahInstance);
		}
		int height = InstancedAreaManager.getSingleton().getNextOpenHeight(BOUNDARY);
		zulrahInstance = new SingleInstancedZulrah(player, BOUNDARY, height);
		InstancedAreaManager.getSingleton().add(height, zulrahInstance);
		if (zulrahInstance == null) {
			player.getDH().sendStatement("The zulrah boss is currently being played by a high amount", "of players. Please try again shortly.");
			player.nextChat = -1;
			return;
		}
		stage = 0;
		stopwatch = Stopwatch.createStarted();
		player.getPA().removeAllWindows();
		player.getPA().sendScreenFade("Welcome to Zulrah's shrine", 1, 5);
		CycleEventHandler.getSingleton().addEvent(EVENT_LOCK, stages.get(0), 1);
	}

	/**
	 * Determines if the player is standing in a toxic location
	 * 
	 * @return true of the player is in a toxic location
	 */
	public boolean isInToxicLocation() {
		for (int x = player.getX() - 1; x < player.getX() + 1; x++) {
			for (int y = player.getY() - 1; y < player.getY() + 1; y++) {
				if (Server.getGlobalObjects().exists(11700, x, y, player.heightLevel)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Stops the zulrah instance and concludes the events
	 */
	public void stop() {
		CycleEventHandler.getSingleton().stopEvents(EVENT_LOCK);
		if (stage < 1) {
			return;
		}
		stopwatch.stop();
		long time = stopwatch.elapsed(TimeUnit.MILLISECONDS);
		long best = player.getBestZulrahTime();
		String duration = best < (60_000 * 60) ? Misc.toFormattedMS(time) : Misc.toFormattedHMS(time);
		player.sendMessage("Fight duration: <col=CC0000>" + duration + "</col> " + (time < player.getBestZulrahTime() ? "(New personal best)" : "") + ".");
		if (time < player.getBestZulrahTime()) {
			player.setBestZulrahTime(time);
		}
		SerializablePair<String, Long> globalBest = Server.getServerData().getZulrahTime();
		if (globalBest.getFirst() == null || globalBest.getSecond() == null || time < globalBest.getSecond() && globalBest.getSecond() != 0) {
			PlayerHandler.executeGlobalMessage("<img=10></img>[<col=255>News</col>] <col=CC0000>" + Misc.capitalize(player.playerName)
					+ "</col> set the record for best time against zulrah with " + duration + ".");
			if (globalBest.getFirst() != null && globalBest.getSecond() != null) {
				PlayerHandler.executeGlobalMessage("<img=10></img>[<col=255>News</col>] The old record was set by <col=CC0000>" + globalBest.getFirst()
						+ "</col> with a time of <col=CC0000>" + Misc.toFormattedMS(globalBest.getSecond()) + "</col>.");
			}
			Server.getServerData().setSerializablePair(new SerializablePair<>(player.playerName, time));
		}
		zulrahInstance.onDispose();
		InstancedAreaManager.getSingleton().disposeOf(zulrahInstance);
		zulrahInstance = null;

	}

	public void changeStage(int stage, CombatType combatType, ZulrahLocation location) {
		this.stage = stage;
		CycleEventHandler.getSingleton().stopEvents(EVENT_LOCK);
		CycleEventHandler.getSingleton().addEvent(EVENT_LOCK, stages.get(stage), 1);
		if (stage == 1) {
			return;
		}
		int type = combatType == CombatType.MELEE ? 2043 : combatType == CombatType.MAGE ? 2044 : 2042;
		npc.startAnimation(5072);
		npc.attackTimer = 8;
		transforming = true;
		player.getCombat().resetPlayerAttack();
		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				player.getCombat().resetPlayerAttack();
				if (container.getTotalTicks() == 2) {
					npc.requestTransform(6709);
				} else if (container.getTotalTicks() == 3) {
					npc.absX = location.getLocation().x;
					npc.absY = location.getLocation().y;
					player.rebuildNPCList = true;
				} else if (container.getTotalTicks() == 5) {
					npc.requestTransform(type);
					npc.startAnimation(5071);
					npc.facePlayer(player.getIndex());
					transforming = false;
					container.stop();
				}
			}

		}, 1);
	}

	/**
	 * Determines if any of the events alive contains the event lock
	 * 
	 * @return true if any of the events are active with this as the owner
	 */
	public boolean isActive() {
		return CycleEventHandler.getSingleton().isAlive(EVENT_LOCK);
	}

	/**
	 * The {@link SingleInstancedArea} object for this class
	 * 
	 * @return the zulrah instance
	 */
	public InstancedArea getInstancedZulrah() {
		return zulrahInstance;
	}

	/**
	 * The reference to zulrah, the npc
	 * 
	 * @return the reference to zulrah
	 */
	public NPC getNpc() {
		return npc;
	}

	/**
	 * The instance of the Zulrah {@link NPC}
	 * 
	 * @param npc the zulrah npc
	 */
	public void setNpc(NPC npc) {
		this.npc = npc;
	}

	/**
	 * The stage of the zulrah event
	 * 
	 * @return the stage
	 */
	public int getStage() {
		return stage;
	}

	/**
	 * Determines if the NPC is transforming or not
	 * 
	 * @return {@code true} if the npc is in a transformation stage
	 */
	public boolean isTransforming() {
		return transforming;
	}

}
