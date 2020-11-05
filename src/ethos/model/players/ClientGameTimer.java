package ethos.model.players;

public enum ClientGameTimer {
	VENGEANCE(0), OVERLOAD(1), ANTIFIRE(2), ANTIVENOM(3), ANTIPOISON(4), TELEBLOCK(5), STAMINA(6), FARMING(7), EXPERIENCE(8), PEST_CONTROL(9), DROPS(10);

	private final int timerId;

	private ClientGameTimer(int timerId) {
		this.timerId = timerId;
	}

	public int getTimerId() {
		return timerId;
	}
}
