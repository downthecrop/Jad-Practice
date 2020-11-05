package ethos.model.players;

import java.util.ArrayList;
import java.util.List;

/**
 * A system to handle users on the ignore list.
 * 
 * @author Mack
 *
 */
public class Ignores {

	private Player client;
	private List<Long> ignores = new ArrayList<Long>();

	public Ignores(Player client) {
		this.client = client;
	}

	public boolean has(long name) {
		return ignores.contains(name);
	}

	public void sendList() {
		client.getOutStream().createFrameVarSizeWord(214);
		for (long ignore : ignores) {
			client.getOutStream().writeQWord(ignore);
		}
		client.getOutStream().endFrameVarSizeWord();
		client.flushOutStream();
	}

	public void add(long name) {
		if (getIgnores().size() >= 100) {
			client.sendMessage("Your ignore list is full.");
			return;
		}
		if (client.getFriends().has(name)) {
			client.sendMessage("Please remove that player from your friends list first.");
			return;
		}
		if (has(name)) {
			client.sendMessage("That player is already on your ignore list.");
			return;
		}
		getIgnores().add(name);
		if (client.getPrivateChat() == Friends.ONLINE) {
			Player ignore = PlayerHandler.getPlayerByLongName(name);
			if (ignore != null && ignore.getFriends().has(client.getNameAsLong())) {
				ignore.getPA().sendFriend(client.getNameAsLong(), (byte) 0);
			}
		}
	}

	public void remove(long name) {
		if (!has(name)) {
			client.sendMessage("That player is not on your ignore list.");
			return;
		}
		getIgnores().remove(name);
		if (client.getPrivateChat() == Friends.ONLINE) {
			Player ignore = PlayerHandler.getPlayerByLongName(name);
			if (ignore != null && ignore.getFriends().has(client.getNameAsLong())) {
				ignore.getPA().sendFriend(client.getNameAsLong(), (byte) 1);
			}
		}
	}

	public Player getPlayer() {
		return client;
	}

	public List<Long> getIgnores() {
		return ignores;
	}
}
