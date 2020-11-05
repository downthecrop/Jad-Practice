package ethos.util.log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import ethos.model.players.Player;


/**
 * Logs player related data
 * 
 * @author Mobster
 *
 */
public class PlayerLogging {

	/**
	 * Different types of logs
	 * 
	 * @author Mobster
	 *
	 */
	public enum LogType {
		COMMAND, PRIVATE_CHAT, CHANGE_IDENTITY, CHANGE_IP_ADDRESS, CHANGE_PASSWORD, CHANGE_MAC_ADDRESS, PUBLIC_CHAT, DONATION_TRANSACTION, TRADE_LOG, DEATH_LOG, DIED_LOG, IRON_KILLED_PLAYER, STAKE_LOG, VOTE_LOG, SHOP_LOG, DC_LOG
	}

	/**
	 * The log directory which we can use to log data
	 */
	public static final File LOG_DIRECTORY = new File("./data/logs/");

	/**
	 * A thread pool to handle logging queries, no need to keep synchronization
	 * of anything as we're simply logging data, not modifying
	 */
	private static final Executor service = Executors.newCachedThreadPool();

	/**
	 * Writes a log
	 * 
	 * @param log
	 * @param file
	 */
	public static void write(LogType type, Player player, String message, String message2) {
		
		Path path = Paths.get(LOG_DIRECTORY.getPath(), player.playerName.charAt(0) + File.separator + player.playerName + File.separator + type.toString().toLowerCase() + ".txt");
		
		if (!Files.exists(path)) {
			try {
				Files.createDirectories(path.getParent());
				Files.createFile(path);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		service.execute(() -> {
					try (BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.APPEND )) {
						DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
						Date date = new Date();
						writer.write("[" + dateFormat.format(date) + "] " + message);
						writer.newLine();
						if (message2.length() > 5) {
							writer.write(message2);
							writer.newLine();
						}	
						writer.write("----------------------------------------------");
						writer.newLine();
						writer.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
		});

	}
	
	public static void write(LogType type, Player player, String message) {
		 write(type, player, message, "");
	}

}
