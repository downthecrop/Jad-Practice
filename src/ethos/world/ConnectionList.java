package ethos.world;

import java.util.HashMap;

public class ConnectionList {

	static HashMap<String, Integer> connections = new HashMap<String, Integer>();

	static final int MAX_CONNECTIONS = 4;

	public static boolean allowedToConnect(String ip) {
		if (!connections.containsKey(ip)) {
			connections.put(ip, 1);
			return true;
		} else {
			int connectionsAmt = connections.get(ip);
			if (connectionsAmt < MAX_CONNECTIONS) {
				connections.put(ip, connectionsAmt + 1);
				return true;
			} else
				return false;
		}
	}

	public static void removeConnection(String ip) {
		int connectionsAmt = connections.get(ip);
		if (connectionsAmt <= 1)
			connections.remove(ip);
		else
			connections.put(ip, connectionsAmt - 1);
	}

}