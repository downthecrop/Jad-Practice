package ethos.util;

import java.util.concurrent.TimeUnit;

/**
 *
 * @author Albin
 */
public class SimpleTimer {

	private long time = System.currentTimeMillis();

	public SimpleTimer reset() {
		time = System.currentTimeMillis();
		return this;
	}

	public long elapsed() {
		return System.currentTimeMillis() - time;
	}

	public long elapsed(TimeUnit unit) {
		return unit.convert(elapsed(), TimeUnit.MILLISECONDS);
	}
}
