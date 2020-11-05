package ethos.model.players;

public interface PacketType {
	public void processPacket(Player c, int packetType, int packetSize);
}
