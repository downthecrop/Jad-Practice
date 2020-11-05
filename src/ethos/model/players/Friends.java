package ethos.model.players;

import java.util.ArrayList;
import java.util.List;

/**
 * A more improved system for handling a user's friends. Takes into account of the privacy setting
 * 
 * @author Mack
 *
 */
public class Friends {

	public static final byte WORLD_ID = 1;
	public static final int ONLINE = 0;
	public static final int FRIENDS = 1;
	public static final int OFFLINE = 2;

	private Player client;
	private List<Long> friends = new ArrayList<Long>();

	public Friends(Player client) {
		this.client = client;
	}

	public void add(long name) {
		if (getFriends().size() >= 200) {
			client.sendMessage("Friends list is currently full.");
			return;
		}
		if (has(name)) {
			client.sendMessage("That player is already on your friends list.");
			return;
		}
		if (client.getIgnores().has(name)) {
			client.sendMessage("Please remove the player from your ignore list first.");
			return;
		}
		getFriends().add(name);
		Player friend = PlayerHandler.getPlayerByLongName(name);
		if (friend != null && !friend.getIgnores().has(client.getNameAsLong())) {
			boolean online = false;
			if (friend.getPrivateChat() == ONLINE) {
				online = true;
			} else if (friend.getPrivateChat() == FRIENDS && friend.getFriends().has(client.getNameAsLong())) {
				online = true;
			}

			if (online) {
				client.getPA().sendFriend(name, 1);
			}

			if (client.getPrivateChat() == FRIENDS && friend.getFriends().has(client.getNameAsLong())) {
				friend.getPA().sendFriend(client.getNameAsLong(), getWorldId(online));
			}
		}
	}

	public void remove(long name) {
		if (!has(name)) {
			client.sendMessage("That player is not on your friends list.");
			return;
		}
		getFriends().remove(name);
		if (client.getPrivateChat() == FRIENDS) {
			Player friend = PlayerHandler.getPlayerByLongName(name);
			if (friend != null && friend.getFriends().has(name)) {
				friend.getPA().sendFriend(client.getNameAsLong(), getWorldId(true));
			}
		}
	}

	public void sendPrivateMessage(long name, byte[] packed) {
		if (!has(name)) {
			client.sendMessage("That player is not on your friends list.");
			return;
		}
		boolean send = true;
		Player friend = PlayerHandler.getPlayerByLongName(name);
		if (friend != null) {
			if (friend.getPrivateChat() == OFFLINE) {
				send = false;
			} else if (friend.getPrivateChat() == FRIENDS && !friend.getFriends().has(client.getNameAsLong())) {
				send = false;
			} else if (friend.getIgnores().has(client.getNameAsLong())) {
				send = false;
			}
			if (send) {
				friend.getPA().sendPM(client.getNameAsLong(), (byte) client.getRights().getPrimary().getValue(), packed);
			}
		} else {
			send = false;
		}
		if (!send) {
			client.sendMessage("That player is currently offline.");
		}
	}

	public void notifyFriendsOfUpdate() {
		for (Player plr : PlayerHandler.players) {
			if (plr == null) {
				continue;
			}
			if (plr == client) {
				continue;
			}
			Player friend = plr;
			if (friend.getFriends().has(client.getNameAsLong())) {
				if (client.getIgnores().has(friend.getNameAsLong())) {
					continue;
				}
				boolean online = true;
				if (client.disconnected) {
					online = false;
				} else if (client.getPrivateChat() == OFFLINE) {
					online = false;
				} else if (client.getPrivateChat() == FRIENDS && !client.getFriends().has(friend.getNameAsLong())) {
					online = false;
				}
				friend.getPA().sendFriend(client.getNameAsLong(), getWorldId(online));
			}
		}
	}

	public int getWorldId(boolean online) {
		return (online ? 1 : 0);
	}

	public void sendList() {
		for (long l : getFriends()) {
			boolean online = false;
			Player friend = PlayerHandler.getPlayerByLongName(l);

			if (friend != null && !friend.getIgnores().has(client.getNameAsLong())) {
				if (friend.getPrivateChat() == ONLINE) {
					online = true;
				} else if (friend.getPrivateChat() == FRIENDS && friend.getFriends().has(client.getNameAsLong())) {
					online = true;
				}
			}
			client.getPA().sendFriend(l, getWorldId(online));
		}
		notifyFriendsOfUpdate();
	}

	public boolean has(long name) {
		return getFriends().contains(name);
	}

	public Player getPlayer() {
		return client;
	}

	public List<Long> getFriends() {
		return friends;
	}
}
