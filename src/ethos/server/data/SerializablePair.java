package ethos.server.data;

import java.io.Serializable;

public class SerializablePair<A extends Serializable, B extends Serializable> implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * The first value in the pair of values
	 */
	private A first;

	/**
	 * The second value in the pair of values
	 */
	private B second;

	public SerializablePair() {
	}

	/**
	 * Creates a new object that is {@link Serializable}
	 * 
	 * @param first
	 * @param second
	 */
	public SerializablePair(A first, B second) {
		this.first = first;
		this.second = second;
	}

	/**
	 * Returns the first value in the pair
	 * 
	 * @return the first value
	 */
	public A getFirst() {
		return first;
	}

	/**
	 * Returns the second value in the pair
	 * 
	 * @return the second value
	 */
	public B getSecond() {
		return second;
	}

}
