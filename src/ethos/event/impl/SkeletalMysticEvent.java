package ethos.event.impl;

import java.util.concurrent.TimeUnit;

import ethos.Server;
import ethos.event.Event;
import ethos.model.npcs.bosses.raids.SkeletalMystic;
import ethos.util.Misc;

public class SkeletalMysticEvent extends Event<Object> {
	
	/**
	 * The amount of time in game cycles (600ms) that the event pulses at
	 */
	private static final int INTERVAL = Misc.toCyclesOrDefault(2, 2, TimeUnit.SECONDS);

	/**
	 * Creates a new event to cycle through messages for the entirety of the runtime
	 */
	public SkeletalMysticEvent() {
		super(new String(), new Object(), INTERVAL);
	}

	@Override
	public void execute() {
		if (SkeletalMystic.respawnTimer > 0) {
			SkeletalMystic.respawnTimer--;
		}
		if (SkeletalMystic.respawnTimer == 0 && SkeletalMystic.needRespawn) {
			Server.npcHandler.newNPC(7604, 3338, 5260, 1, 1, 500, 30, 240, 150);
			Server.npcHandler.newNPC(7606, 3347, 5261, 1, 1, 500, 30, 240, 150);
			Server.npcHandler.newNPC(7605, 3347, 5269, 1, 1, 500, 30, 240, 150);
			Server.npcHandler.newNPC(7605, 3338, 5269, 1, 1, 500, 30, 240, 150);
			SkeletalMystic.needRespawn = false;
		}
	}
}
