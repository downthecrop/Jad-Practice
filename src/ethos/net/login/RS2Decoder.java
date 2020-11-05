package ethos.net.login;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

import ethos.model.players.Player;
import ethos.net.Packet;
import ethos.net.Packet.Type;
import ethos.util.ISAACCipher;

public class RS2Decoder extends FrameDecoder {

	private final ISAACCipher cipher;

	private int opcode = -1;
	private int size = -1;

	public RS2Decoder(ISAACCipher cipher) {
		this.cipher = cipher;
	}

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
		if (opcode == -1) {
			if (buffer.readableBytes() >= 1) {
				opcode = buffer.readByte() & 0xFF;
				opcode = (opcode - cipher.getNextValue()) & 0xFF;
				size = Player.PACKET_SIZES[opcode];
			} else {
				return null;
			}
		}
		if (size == -1) {
			if (buffer.readableBytes() >= 1) {
				size = buffer.readByte() & 0xFF;
			} else {
				return null;
			}
		}
		if (buffer.readableBytes() >= size) {
			final byte[] data = new byte[size];
			buffer.readBytes(data);
			final ChannelBuffer payload = ChannelBuffers.buffer(size);
			payload.writeBytes(data);
			try {
				return new Packet(opcode, Type.FIXED, payload);
			} finally {
				opcode = -1;
				size = -1;
			}
		}
		return null;
	}

}
