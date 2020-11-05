package ethos.event.impl;

import java.util.concurrent.TimeUnit;

import ethos.event.Event;
import ethos.model.players.Player;
import ethos.util.Misc;

public class MinigamePlayersEvent extends Event<Object> {
	
	/**
	 * The amount of time in game cycles (600ms) that the event pulses at
	 */
	private static final int INTERVAL = Misc.toCyclesOrDefault(5, 5, TimeUnit.SECONDS);

	/**
	 * Creates a new event to cycle through messages for the entirety of the runtime
	 */
	public MinigamePlayersEvent(Player attachment) {
		super(attachment, INTERVAL);
	}

	@Override
	public void execute() {
		//((Player) attachment).getPA().sendFrame126("@or1@- PK District: @whi@" + (Boundary.entitiesInArea(Boundary.CLAN_WARS) + Boundary.entitiesInArea(Boundary.CLAN_WARS_SAFE)), 29178);
		//((Player) attachment).getPA().sendFrame126("@or1@- Wilderness: @whi@" + (Boundary.entitiesInArea(Boundary.WILDERNESS) + Boundary.entitiesInArea(Boundary.WILDERNESS_UNDERGROUND) + Boundary.entitiesInArea(Boundary.WILDERNESS_GOD_WARS_BOUNDARY)), 29179);
		//((Player) attachment).getPA().sendFrame126("@or1@- Pest Control: @whi@" + Boundary.entitiesInArea(Boundary.PEST_CONTROL_AREA), 29180);
	}
}
