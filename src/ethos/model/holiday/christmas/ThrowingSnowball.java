package ethos.model.holiday.christmas;

import ethos.event.CycleEvent;
import ethos.event.CycleEventContainer;
import ethos.event.CycleEventHandler;
import ethos.model.players.Player;

public class ThrowingSnowball extends CycleEvent {

	public static void throwSnowball(final Player thrower, final Player victim) {
		thrower.getItems().deleteEquipment();
		thrower.startAnimation(5063);
		thrower.turnPlayerTo(victim.getX(), victim.getY());
		thrower.getPA().createPlayersProjectile2(thrower.getX(), thrower.getY(), (thrower.getY() - victim.getY()) * -1, (thrower.getX() - victim.getX()) * -1, 50, 100, 861, 30,
				30, -thrower.oldPlayerIndex - 1, 30, 5);
		CycleEventHandler.getSingleton().addEvent(thrower, new ThrowingSnowball(thrower, victim), 2);
	}

	private Player thrower;
	private Player victim;

	private ThrowingSnowball(Player thrower, Player victim) {
		this.thrower = thrower;
		this.victim = victim;
	}

	@Override
	public void execute(CycleEventContainer container) {
		container.stop();
	}

	@Override
	public void stop() {
		CycleEventHandler.getSingleton().addEvent(thrower, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				thrower.sendMessage(victim.playerName + " got hit by the snowball!");
				victim.gfx100(862);
				victim.sendMessage(thrower.playerName +" threw a snowball at you!");
				victim.forcedChat("Ow! That's cold..");
				container.stop();
			}

			@Override
			public void stop() {

			}
		}, 2);
	}

}