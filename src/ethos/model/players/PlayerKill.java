package ethos.model.players;

import java.util.ArrayList;
import java.util.List;

public class PlayerKill {

	static final int HOST_INDEX_POPOFF = 1;

	private List<String> hostList = new ArrayList<>();

	public void add(String host) {
		hostList.add(0, host);
		removeOldElements();
	}

	public boolean killedRecently(String host) {
		removeOldElements();
		return hostList.contains(host);
	}

	private void removeOldElements() {
		if (hostList.size() > HOST_INDEX_POPOFF) {
			hostList.removeAll(hostList.subList(HOST_INDEX_POPOFF, hostList.size()));
		}
	}

	public List<String> getList() {
		return hostList;
	}

}