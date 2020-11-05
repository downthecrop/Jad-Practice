package ethos.net.outgoing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * Some outgoing packets don't need to be sent to the client because at times, information being sent hasn't changed. For example, in packet 126 we send a string of characters and
 * a component id. The string replaces the current string on the interface component. At times we send the same string to the client which is unnecessary.
 * 
 * When a packet is to be sent, we will determine under some circumstances if it should be dropped, or it if should be sent. We do this by matching up the credentials of the old
 * existing information last sent, and the message we are sending now.
 * 
 * @author Jason MacKeigan
 * @date November 15, 2014, 3:40:52 AM
 */
public class UnnecessaryPacketDropper {

	private HashMap<Integer, List<PacketMessage<?>>> messages = new HashMap<>();

	@SuppressWarnings("unchecked")
	private <T> void update(int opcode, Optional<PacketMessage<T>> remove, T add) {
		List<PacketMessage<?>> list;
		PacketMessage<T> message = (PacketMessage<T>) add;
		if (remove.isPresent()) {
			list = messages.get(opcode);
			list.remove(remove.get());
			list.add(message);
		} else {
			list = new ArrayList<>();
			list.add(message);
			messages.put(opcode, list);
		}
	}

	@SuppressWarnings("unchecked")
	public <T> boolean requiresUpdate(int opcode, T message) {
		if (!messages.containsKey(opcode)) {
			update(opcode, Optional.empty(), message);
			return true;
		}
		List<PacketMessage<?>> messageList = messages.get(opcode);
		List<PacketMessage<T>> list = listOf(messageList, message);
		Optional<PacketMessage<T>> match = list.stream().filter(m -> m.matches(message)).findFirst();
		if (match.isPresent()) {
			if (match.get().requiresUpdate(message)) {
				update(opcode, match, message);
				return true;
			}
		} else {
			messageList.add((PacketMessage<T>) message);
			return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	private <T> List<PacketMessage<T>> listOf(List<PacketMessage<?>> messageList, T type) throws ClassCastException {
		ArrayList<PacketMessage<T>> list = new ArrayList<>();
		messageList.stream().forEach(message -> {
			Class<T> c = (Class<T>) type.getClass();
			if (message.getClass().isAssignableFrom(c)) {
				list.add((PacketMessage<T>) message);
			}
		});
		return list;
	}
}
