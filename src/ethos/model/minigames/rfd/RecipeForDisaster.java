package ethos.model.minigames.rfd;

import org.apache.commons.lang3.Range;

import ethos.Server;
import ethos.event.Event;
import ethos.model.content.instances.SingleInstancedArea;
import ethos.model.npcs.NPC;
import ethos.model.players.Boundary;
import ethos.model.players.Player;
import ethos.util.Misc;

/**
 * RecipeForDisaster, minigame
 * 
 * @author Matt
 */
public final class RecipeForDisaster extends SingleInstancedArea {

	/**
	 * Player variables, start coordinates.
	 */
	private static final int START_X = 1900, START_Y = 5346;

	/**
	 * Non-playable character variables, {@link SPAWN_X, SPAWN_Y} and {@link RANGE_OF_IDS} for Gelatinnoth mother .
	 */
	private static final int SPAWN_X = 1903, SPAWN_Y = 5356;
	public static final Range<Integer> NPCS = Range.between(6369, 6378);
	public static final Range<Integer> RANGE_OF_GELATINNOTHS = Range.between(6373, 6378);

	/**
	 * Wave data ID, HP, MAX, ATTACK, DEFENCE
	 */
	public static int[][] WAVE_DATA = { { 6367, 100, 5, 100, 120 }, { 6369, 200, 8, 100, 150 }, { 6370, 210, 9, 120, 170 }, { 6371, 240, 10, 150, 200 },
			{ 6372, 140, 11, 150, 200 }, { 6373, 250, 9, 170, 220 }
			, {6368, 180, 25, 170, 240}
	};

	/**
	 * The {@link NPC#npcType} value of the transformed non-playable character.
	 */
	private int npcId = RANGE_OF_GELATINNOTHS.getMinimum();

	public RecipeForDisaster(Player player, Boundary boundary, int height) {
		super(player, boundary, height);
	}

	/**
	 * Constructs the content by creating an event that will execute after 5 cycles (3 seconds).
	 */

	public void init() {
		player.getCombat().resetPrayers();
		//Server.npcHandler.spawnNpc(player, 6368, 1899, 5356, height, 0, 180, 25, 170, 240, false, false);
		player.getPA().movePlayer(START_X, START_Y, height);
		player.sendMessage("Prepare to fight...");
		player.getDH().sendNpcChat1("You DARE come HERE?!?!", 6368, "Culinaromancer");
		player.nextChat = -1;
		wave();
	}

	public void wave() {
		Server.getEventHandler().submit(new Event<Player>("rfd", player, 5) {

			@Override
			public void execute() {
				if (player == null) {
					super.stop();
					return;
				}
				Server.npcHandler.spawnNpc(player, WAVE_DATA[player.rfdWave][0], SPAWN_X, SPAWN_Y, height, 0, WAVE_DATA[player.rfdWave][1], WAVE_DATA[player.rfdWave][2],
						WAVE_DATA[player.rfdWave][3], WAVE_DATA[player.rfdWave][4], true, false);
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
		int randomTransformationId = Misc.random(RANGE_OF_GELATINNOTHS);

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
	public final void end(DisposeTypes dispose) {
		if (player == null) {
			return;
		}
		player.getPA().movePlayer(3097, 3512, 0);
		if (dispose == DisposeTypes.COMPLETE) {
			player.sendMessage("You have completed the minigame.");
			player.sendMessage("You now have full access to Culinaromancer's Chest.");
			player.rfdWave = 0;
		} else if (dispose == DisposeTypes.INCOMPLETE) {
			player.sendMessage("You were unable to complete the minigame.");
		}
	}

	@Override
	public void onDispose() {
		end(DisposeTypes.INCOMPLETE);
	}

}