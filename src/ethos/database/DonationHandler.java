package ethos.database;


import java.sql.*;
import java.util.Optional;

import ethos.model.players.Player;
import ethos.model.players.packets.commands.Command;

/**
 * Opens the store page in the default web browser.
 *
 * @author Emiel
 */
public class DonationHandler implements Runnable {

    private static final String HOST = "162.218.48.74"; // website ip address
    private static final String USER = "ascendps_master";
    private static final String PASS = "Ascendfromashes317*";
    private static final String DATABASE = "ascendps_donation";

    private Player player;
    private Connection conn;
    private Statement stmt;


    public DonationHandler(Player player) {
        this.player = player;
    }

    @Override
    public void run(){
        try {
            if (!connect(HOST, DATABASE, USER, PASS)) {
                return;
            }

            String name = player.playerName.replace("_", " ");
            ResultSet rs = executeQuery("SELECT * FROM payments WHERE player_name='" + name + "' AND status='Completed' AND claimed=0");

            while (rs.next()) {
                int item_number = 0;

                item_number = rs.getInt("item_number");

                double paid = rs.getDouble("amount");
                int quantity = rs.getInt("quantity");

                switch (item_number) {// add products according to their ID in the ACP

                    case 86: // example
                        player.getItems().addItem(6306, quantity);
                        break;

                }

                rs.updateInt("claimed", 1); // do not delete otherwise they can reclaim!
                rs.updateRow();
            }

            destroy();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     *
     * @param host the host ip address or url
     * @param database the name of the database
     * @param user the user attached to the database
     * @param pass the users password
     * @return true if connected
     */
    public boolean connect(String host, String database, String user, String pass) {
        try {
            this.conn = DriverManager.getConnection("jdbc:mysql://"+host+":3306/"+database, user, pass);
            System.out.print("Connected");
            return true;
        } catch (SQLException e) {
            System.out.println("Failing connecting to database!");
            return false;
        }
    }

    /**
     * Disconnects from the MySQL server and destroy the connection
     * and statement instances
     */
    public void destroy() {
        try {
            conn.close();
            conn = null;
            if (stmt != null) {
                stmt.close();
                stmt = null;
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Executes an update query on the database
     * @param query
     * @see {@link Statement#executeUpdate}
     */
    public int executeUpdate(String query) {
        try {
            this.stmt = this.conn.createStatement(1005, 1008);
            int results = stmt.executeUpdate(query);
            return results;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    /**
     * Executres a query on the database
     * @param query
     * @see {@link Statement#executeQuery(String)}
     * @return the results, never null
     */
    public ResultSet executeQuery(String query) {
        try {
            this.stmt = this.conn.createStatement(1005, 1008);
            ResultSet results = stmt.executeQuery(query);
            return results;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
