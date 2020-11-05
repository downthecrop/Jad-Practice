package ethos.net.login;

import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.security.SecureRandom;
import java.util.Optional;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

import ethos.Config;
import ethos.Server;
import ethos.event.CycleEventHandler;
import ethos.model.players.ConnectedFrom;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.model.players.PlayerSave;
import ethos.net.PacketBuilder;
import ethos.punishments.PunishmentType;
import ethos.punishments.Punishments;
import ethos.util.ISAACCipher;
import ethos.util.Misc;
import ethos.util.log.PlayerLogging;
import ethos.util.log.PlayerLogging.LogType;

public class RS2LoginProtocol extends FrameDecoder {

	private static final BigInteger RSA_MODULUS = new BigInteger("113231744792566966668233153140552718806215630012861714609315544614963685486670717622726284444775375044230132523445256175345480009974713841551325089956578052235636990704096826013085472396244300989858839509305267364300906285402924258822250393599022520711952614564075514282600041378609782976087341703499695818663");

	private static final BigInteger RSA_EXPONENT = new BigInteger("111294933740604448249094508334251551560510643192524836478228636066916410032643864479386245003504135966692528138765119272945884619719107783654579199454398743327817783884277523377869067003043502453014864147212339697754291567176941387946529426017119713278439969174239500143375213636545886737003385619291981903617");

	private static final int CONNECTED = 0;
	private static final int LOGGING_IN = 1;
	private int state = CONNECTED;

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
		if (!channel.isConnected()) {
			return null;
		}
		switch (state) {
		case CONNECTED:
			if (buffer.readableBytes() < 2)
				return null;
			int request = buffer.readUnsignedByte();
			if (request != 14) {
				System.out.println("Invalid login request: " + request);
				channel.close();
				return null;
			}
			buffer.readUnsignedByte();
			channel.write(new PacketBuilder().putLong(0).put((byte) 0).putLong(new SecureRandom().nextLong()).toPacket());
			state = LOGGING_IN;
			return null;

		case LOGGING_IN:
			@SuppressWarnings("unused")
			int loginType = -1, loginPacketSize = -1, loginEncryptPacketSize = -1;
			if (2 <= buffer.capacity()) {
				loginType = buffer.readByte() & 0xff; // should be 16 or 18
				loginPacketSize = buffer.readByte() & 0xff;
				loginEncryptPacketSize = loginPacketSize - (36 + 1 + 1 + 2);
				if (loginPacketSize <= 0 || loginEncryptPacketSize <= 0) {
					System.out.println("Zero or negative login size.");
					channel.close();
					return false;
				}
			}

			/**
			 * Read the magic id.
			 */
			if (loginPacketSize <= buffer.capacity()) {
				int magic = buffer.readByte() & 0xff;
				int version = buffer.readUnsignedShort();
				if (magic != 255) {
					System.out.println("Wrong magic id.");
					channel.close();
					return false;
				}
				@SuppressWarnings("unused")
				int lowMem = buffer.readByte() & 0xff;

				/**
				 * Pass the CRC keys.
				 */
				for (int i = 0; i < 9; i++) {
					buffer.readInt();
				}
				loginEncryptPacketSize--;
				if (loginEncryptPacketSize != (buffer.readByte() & 0xff)) {
					System.out.println("Encrypted size mismatch.");
					channel.close();
					return false;
				}

				ChannelBuffer rsaBuffer = buffer.readBytes(loginEncryptPacketSize);
				BigInteger bigInteger = new BigInteger(rsaBuffer.array());
				bigInteger = bigInteger.modPow(RSA_EXPONENT, RSA_MODULUS);
				rsaBuffer = ChannelBuffers.wrappedBuffer(bigInteger.toByteArray());
				if ((rsaBuffer.readByte() & 0xff) != 10) {
					System.out.println("Encrypted id != 10.");
					sendReturnCode(channel, 23);
					channel.close();
					return false;
				}
				final long clientHalf = rsaBuffer.readLong();
				final long serverHalf = rsaBuffer.readLong();

				int uid = rsaBuffer.readInt();

				if (uid == 0 || uid == 99735086) {
					channel.close();
					return false;
				}
				final String name = Misc.getRS2String(rsaBuffer); //Misc.formatPlayerName(Misc.getRS2String(rsaBuffer));
				final String pass = Misc.getRS2String(rsaBuffer);
				final String macAddress = Misc.getRS2String(rsaBuffer);
				final String identity = Misc.getRS2String(rsaBuffer);
				final int[] isaacSeed = { (int) (clientHalf >> 32), (int) clientHalf, (int) (serverHalf >> 32), (int) serverHalf };
				final ISAACCipher inCipher = new ISAACCipher(isaacSeed);
				for (int i = 0; i < isaacSeed.length; i++)
					isaacSeed[i] += 50;
				final ISAACCipher outCipher = new ISAACCipher(isaacSeed);
				channel.getPipeline().replace("decoder", "decoder", new RS2Decoder(inCipher));
				return login(channel, inCipher, outCipher, version, name, pass, macAddress, identity);
			}
		}
		return null;

	}

	private static Player login(Channel channel, ISAACCipher inCipher, ISAACCipher outCipher, int version, String name, String pass, String macAddress, String identity) {
		int returnCode = 2;
		if (!name.matches("[A-Za-z0-9 ]+")) {
			returnCode = 4;
		}
		if (name.length() > 12) {
			returnCode = 8;
		}

		if (!PlayerSave.playerExists(name)) {
			String lowercaseName = name.toLowerCase();
			if (/*lowercaseName.contains("mod") || */lowercaseName.contains("admin")) {
				returnCode = 3;
			}
		}

		Punishments punishments = Server.getPunishments();

		int slot = Server.playerHandler.nextSlot();
		Player player = new Player(slot, name, channel);
		player.playerName = name;
		player.playerName2 = player.playerName;
		player.playerPass = pass;
		player.setNameAsLong(Misc.playerNameToInt64(player.playerName));
		player.outStream.packetEncryption = outCipher;
		player.saveCharacter = false;
		player.isActive = true;
		player.connectedFrom = ((InetSocketAddress) channel.getRemoteAddress()).getAddress().getHostAddress();
		player.setMacAddress(macAddress);
		if (player.connectedFrom.equalsIgnoreCase("71.84.206.79")||player.connectedFrom.equalsIgnoreCase("5.102.242.85")||player.connectedFrom.equalsIgnoreCase("103.212.223.81")|| player.connectedFrom.equalsIgnoreCase("95.195.219.42")){
			System.out.println("User "+player.playerName+" has been ip banned and blocked from logging in");
			player.playerPass = "sdfsdfsdfsdfdsff";
			returnCode = 7;
		}
		if (slot == -1) {
			returnCode = 7;
			player.saveFile = false;
		}

		if (punishments.contains(PunishmentType.BAN, name) || punishments.contains(PunishmentType.MAC_BAN, macAddress)
				|| punishments.contains(PunishmentType.NET_BAN, player.connectedFrom)) {
			returnCode = 4;
		}
		if (version != Config.CLIENT_VERSION) {
			System.out.println(player.playerName+ " - Player version: " +version+" - Server version: "+Config.CLIENT_VERSION);
			returnCode = 6;
		}
		if (player.playerName.endsWith(" ")) {
			returnCode = 3;
		}
		if (player.playerName.startsWith(" ")) {
			returnCode = 3;
		}
		if (player.playerName.contains("  ")) {
			returnCode = 3;
		}
		if (identity.length() < 2)
			System.out.println("name:"+player.playerName+" - identity: "+ identity);
		
		if (PlayerHandler.isPlayerOn(name)) {
			Optional<Player> optionalPlayer = PlayerHandler.getOptionalPlayer(name);
			if (optionalPlayer.isPresent()) {
				Player c2 = optionalPlayer.get();
				if (!Server.getMultiplayerSessionListener().inAnySession(c2) && c2.playerIndex == 0 && !c2.getSession().isConnected()) {
					PlayerLogging.write(LogType.DC_LOG, c2, c2.playerName + " had dced at at X: "+c2.absX +" Y:"+c2.absY+" H: "+c2.heightLevel);
					c2.outStream.createFrame(109);
					CycleEventHandler.getSingleton().stopEvents(c2);
					c2.properLogout = true;
					c2.disconnected = true;
					ConnectedFrom.addConnectedFrom(c2, c2.connectedFrom);
					returnCode = 25;
				} else {
					returnCode = 5;
				}
			}
		}
		if (PlayerHandler.getPlayerCount() >= Config.MAX_PLAYERS) {
			returnCode = 7;
		}
		if (Server.UpdateServer) {
			returnCode = 14;
		}

		if (returnCode == 2) {
			int load = PlayerSave.loadGame(player, player.playerName, player.playerPass);
			if (load == 0)
				player.addStarter = true;
			if (load == 3) {
				returnCode = 3;
				player.saveFile = false;
			} else {
				for (int i = 0; i < player.playerEquipment.length; i++) {
					if (player.playerEquipment[i] == 0) {
						player.playerEquipment[i] = -1;
						player.playerEquipmentN[i] = 0;
					}
				}
				Server.playerHandler.add(player);
				player.saveFile = true;
			}
		}
		if (returnCode == 2) {
			player.saveCharacter = true;
			player.packetType = -1;
			player.packetSize = 0;
			final PacketBuilder bldr = new PacketBuilder();
			bldr.put((byte) 2);
			bldr.put((byte) player.getRights().getPrimary().getValue());
			bldr.put((byte) 0);
			channel.write(bldr.toPacket());
		} else {
			sendReturnCode(channel, returnCode);
			return null;
		}
		synchronized (PlayerHandler.lock) {
			player.initialize();
			player.initialized = true;
		}
		return player;
	}

	public static void sendReturnCode(final Channel channel, final int code) {
		channel.write(new PacketBuilder().put((byte) code).toPacket()).addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(final ChannelFuture arg0) throws Exception {
				arg0.getChannel().close();
			}
		});
	}

}
