package ethos.model.players.packets;


import ethos.model.players.PacketType;
import ethos.model.players.Player;

public class KeyEventPacketHandler implements PacketType {

	//private KeyEvent event;

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		//int key = c.getInStream().readUnsignedWord();
		
		if (c.isIdle)
			c.isIdle = false;
/*		final KeyEvent keyEvent = (KeyEvent) event;
		
		switch(key) {
		case KeyEvent.VK_A:
			c.sendMessage("tits - A");
			break;
		case KeyEvent.VK_B:
			c.sendMessage("tits - B");
			break;
		case KeyEvent.VK_C:
			c.sendMessage("tits - C");
			break;
		case KeyEvent.VK_D:
			c.sendMessage("tits - D");
			break;
		case KeyEvent.VK_E:
			c.sendMessage("tits - E");
			break;
		}*/
	}

}
