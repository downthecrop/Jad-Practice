package ethos.net.outgoing;

/**
 * 
 * @author Jason MacKeigan
 * @date Nov 30, 2014, 5:38:29 AM
 */
public interface PacketMessage<T> {

	/**
	 * Determines if certain information regarding a packet message matches another. For example; When sending packet 171 (interface visibility) we send a specific integer value
	 * that represents an interface id, and a byte that represents the state of visibility. In this case, we would compare the id's to obtain the match.
	 * 
	 * @param message the message to be compared
	 * @return true if certain information regarding the sent message matches
	 */
	public boolean matches(T message);

	/**
	 * Determines if the message sent contains different information then what already exists. Using packet 171 described in the matches function as an example; We could require an
	 * update if the state of the same interface has changed.
	 * 
	 * @param message the message to be compared
	 * @return true if an update is required
	 */
	public boolean requiresUpdate(T message);

}
