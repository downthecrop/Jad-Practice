package ethos.event;

public abstract class DelayEvent extends Event<Object> {

	public DelayEvent(int ticks) {
		super(new Object(), ticks);
	}

	public abstract void onExecute();

	@Override
	public void execute() {
		try {
			onExecute();
			stop();
		} catch (Exception e) {
			System.out.println("DelayEvent - Check for error");
			e.printStackTrace();
			stop();
		}
	}
}
