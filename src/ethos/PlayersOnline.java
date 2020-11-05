package ethos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;

public class PlayersOnline {

	public static Connection con = null;
	public static Statement stm;

	public static void createCon() {
		if (Config.BLOCK_SQL) {
			return;
		}
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			con = DriverManager.getConnection("jdbc:mysql://212.1.208.242/ospsorg_world", "ospsorg_julius", "ballehora");
			stm = con.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static ResultSet query(String s) throws SQLException {
		try {
			if (s.toLowerCase().startsWith("select")) {
				ResultSet rs = stm.executeQuery(s);
				return rs;
			} else {
				stm.executeUpdate(s);
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void destroyCon() {
		try {
			stm.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	

	public static boolean offline(Player c) {
		try {
			query("REPLACE INTO `online` (id, currentlyonline) VALUES('1','"+PlayerHandler.getPlayerCount()+"');");

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static boolean online(Player c) {
		try {
			query("REPLACE INTO `online` (id, currentlyonline) VALUES('1','"+PlayerHandler.getPlayerCount()+"');");

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}