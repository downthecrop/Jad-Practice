package ethos.model.minigames.lighthouse;

import org.apache.commons.lang3.Range;

import ethos.Server;
import ethos.event.Event;
import ethos.model.content.achievement_diary.fremennik.FremennikDiaryEntry;
import ethos.model.content.instances.SingleInstancedArea;
import ethos.model.npcs.NPC;
import ethos.model.players.Boundary;
import ethos.model.players.Player;
import ethos.util.Misc;

/**
 * Lighthouse, dagannoth mother minigame
 * 
 * @author Matt
 */
public final class DagannothMother extends SingleInstancedArea {

	private static final int START_X = 2515, START_Y = 4632;

	public static final Range<Integer> RANGE_OF_IDS = Range.between(983, 988);

	private static final int SPAWN_X = 2521, SPAWN_Y = 4644;

	private static final int HITPOINTS = 150, ATTACK = 400, DEFENCE = 700, MAXHIT = 15;

	/**
	 * The {@link NPC#npcType} value of the transformed non-playable character.
	 */
	private int npcId = RANGE_OF_IDS.getMinimum();

	public DagannothMother(Player player, Boundary boundary, int height) {
		super(player, boundary, height);
	}

	/**
	 * Constructs the content by creating an event that will execute after 5 cycles (3 seconds).
	 */
	public void init() {
		player.getPA().movePlayer(START_X, START_Y, height);
		player.sendMessage("Prepare to fight the Dagannoth Mother..");

		Server.getEventHandler().submit(new Event<Player>("dagannoth_mother", player, 5) {

			@Override
			public void execute() {
				if (player == null) {
					super.stop();
					return;
				}
				Server.npcHandler.spawnNpc(player, 983, SPAWN_X, SPAWN_Y, height, 0, HITPOINTS, MAXHIT, ATTACK, DEFENCE, true, false);
				npcId = 983;
				super.stop();
			}

		});
	}

	/**
	 * Attempts to randomly transformation the non-playable character into another by randomly generating a value from the range of values defined by {@link #RANGE_OF_IDS}.
	 * 
	 * If the randomly generated value is that of the current transformed npc id then the transformation is aborted.
	 * 
	 * @param npc the non-playable character we are transforming.
	 */
	public final void transformation(NPC npc) {
		int probability = Misc.random(100);

		if (probability < 80) {
			return;
		}
		int randomTransformationId = Misc.random(RANGE_OF_IDS);

		if (randomTransformationId == npcId) {
			return;
		}
		npc.requestTransform(randomTransformationId);
	}

	/**
	 * Disposes of the content by moving the player and finalizing and or removing any left over content.
	 * 
	 * @param dispose the type of dispose
	 */
	public final void end(DisposeType dispose) {
		if (player == null) {
			return;
		}
		player.getPA().movePlayer(2509, 3639, 0);
		if (dispose == DisposeType.COMPLETE) {
			player.sendMessage("You have slain the dagannoth mother.");
			player.sendMessage("You have been rewarded a rusty casket.");
			player.getDiaryManager().getFremennikDiary().progress(FremennikDiaryEntry.KILL_DAGANNOTH_MOTHER);
			player.getItems().addItemUnderAnyCircumstance(3849, 1);
		} else if (dispose == DisposeType.INCOMPLETE) {
			player.sendMessage("You were unable to slay the dagannoth mother.");
		}
	}

	@Override
	public void onDispose() {
		end(DisposeType.INCOMPLETE);
	}

}