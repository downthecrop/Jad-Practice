package ethos.model.multiplayer_session;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import ethos.model.players.Player;

/**
 * 
 * @author Jason MacKeigan
 * @date Oct 20, 2014, 4:42:11 PM
 */
public class MultiplayerSessionListener {

	/**
	 * List of all trade sessions active in the game at this time.
	 */
	List<MultiplayerSession> sessions = new ArrayList<>();

	/**
	 * Adds a new session to the list of active trade sessions.
	 * 
	 * @param session The trade session
	 */
	public void add(MultiplayerSession session) {
		sessions.add(session);
	}

	/**
	 * Removes a session from the list
	 * 
	 * @param session the session to be removed
	 */
	public void remove(MultiplayerSession session) {
		sessions.remove(session);
	}

	/**
	 * Determines if the session we want to append to the list is appendable.
	 * 
	 * @param sessionPending Session we are trying to add
	 * @return
	 */
	public boolean appendable(MultiplayerSession sessionPending) {
		return true;
	}

	/**
	 * Determines if the player is causing any possible trade interference
	 * 
	 * @param player the player possibly causing interference
	 * @return true if the player is causing interference, false if they are not
	 */
	public boolean containsSessionInconsistancies(Player player) {
		List<MultiplayerSession> playerMultiplayerSessions = sessions.stream()
				.filter(session -> session.getStage().getStage() > MultiplayerSessionStage.REQUEST && session.getPlayers().contains(player)).collect(Collectors.toList());
		if (playerMultiplayerSessions.size() > 1) {
			playerMultiplayerSessions.forEach(session -> session.finish(MultiplayerSessionFinalizeType.DISPOSE_ITEMS));
			player.sendMessage("Trade declined and items lost, you existed in one or more trades.");
			return true;
		}
		for (MultiplayerSession session : sessions) {
			if (session.getPlayers().size() > MultiplayerSession.PLAYER_LIMIT) {
				session.finish(MultiplayerSessionFinalizeType.DISPOSE_ITEMS);
				player.sendMessage("Trace declined, items lost, more than two players in this trade.");
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns a MultiplayerSession object if a trade is available.
	 * 
	 * @param sender the player that requested the trade
	 * @param receiver the player that received the request
	 * @return a MultiplayerSession object
	 */
	public MultiplayerSession requestAvailable(Player sender, Player receiver, MultiplayerSessionType type) {
		for (MultiplayerSession session : sessions) {
			if (session.getStage().getStage() != MultiplayerSessionStage.REQUEST) {
				continue;
			}
			if (!type.equals(session.type)) {
				continue;
			}
			if (session.getPlayers().containsAll(Arrays.asList(sender, receiver))) {
				if (session.getStage().getAttachment() == receiver) {
					return session;
				}
			}
		}
		return null;
	}

	public void removeOldRequests(Player player) {
		sessions.removeAll(Arrays
				.asList(sessions.stream().filter(session -> session.getPlayers().contains(player) && session.getStage().getStage() == MultiplayerSessionStage.REQUEST).toArray()));
	}

	/**
	 * Retrieves the trade session the player is in.
	 * 
	 * @param player the player in the trade session
	 * @return the trade session
	 */
	public MultiplayerSession getMultiplayerSession(Player player, MultiplayerSessionType type) {
		for (MultiplayerSession session : sessions) {
			if (session == null) {
				continue;
			}
			if (!session.type.name().equals(type.name())) {
				continue;
			}
			if (session.getPlayers().contains(player)) {
				return session;
			}
		}
		return null;
	}

	/**
	 * Retrieves the trade session the player is in.
	 * 
	 * @param player the player in the trade session
	 * @return the trade session
	 */
	public MultiplayerSession getMultiplayerSession(Player player) {
		for (MultiplayerSession session : sessions) {
			if (session == null) {
				continue;
			}
			if (session.getPlayers().contains(player)) {
				return session;
			}
		}
		return null;
	}

	/**
	 * Stops the trading procedure for each player in a trade session
	 * 
	 * @param player the player in a session
	 */
	public void finish(Player player, MultiplayerSessionFinalizeType type) {
		for (MultiplayerSession session : sessions) {
			if (session.getPlayers().contains(player)) {
				session.finish(type);
				break;
			}
		}
	}

	/**
	 * Determines if the player exists within any of the current trade sessions that exist.
	 * 
	 * @param player The player that we're examining
	 * @return
	 */
	public boolean inSession(Player player, MultiplayerSessionType type) {
		return sessions.stream()
				.anyMatch(session -> session.type == type && session.getPlayers().contains(player) && session.getStage().getStage() > MultiplayerSessionStage.REQUEST);
	}

	public boolean inAnySession(Player player) {
		return sessions.stream().anyMatch(session -> session.getPlayers().contains(player) && session.getStage().getStage() > MultiplayerSessionStage.REQUEST);
	}

	/**
	 * Determins if the list of sessions contains this session. If it does not, then stop.
	 * 
	 * @param session the session
	 * @return if the session is available
	 */
	public boolean contains(MultiplayerSession session) {
		return sessions.contains(session);
	}

}
