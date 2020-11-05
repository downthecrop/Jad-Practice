package ethos.model.players.packets.commands;

import java.io.IOException;
import java.util.Optional;

import ethos.Config;
import ethos.Server;
import ethos.clip.doors.DoorDefinition;
import ethos.model.items.ItemDefinition;
import ethos.model.multiplayer_session.MultiplayerSession;
import ethos.model.multiplayer_session.MultiplayerSessionFinalizeType;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.model.players.Right;
import ethos.punishments.Punishment;
import ethos.punishments.PunishmentType;
import ethos.punishments.Punishments;

/**
 * New staff action command class
 *
 * @author trees
 */
public class newStaff {

    static Punishments punishments = Server.getPunishments();
    static Punishment punishment;
    static Long banEnd = Long.MAX_VALUE;
    static String username_being_punished;

    public static boolean runCommand(Player player, String command, String arg) {
        switch (command) {
        	case "int":
        		try {
        			int a = Integer.parseInt(arg);
        			player.getPA().showInterface(a);
        		} catch (Exception e) {
        			player.sendMessage("::interface #### Error");
        		}
            break;
            case "commands":
                player.sendMessage("Most are used for reloading, after you ban, do ::punishments");
                player.sendMessage("So you can reset the punishments system, in other words update.");
                player.sendMessage("@red@Usage: ::reload doors, drops, items, objects, shops, npcs");
                player.sendMessage("@red@Usage: ::punishments, looting, unmute, unban, ban, mute,");
                player.sendMessage("@red@Usage: ::netmute, macban, teleto");
                break;

            case "doors":
                try {
                    DoorDefinition.load();
                    player.sendMessage("@blu@Reloaded Doors.");
                } catch (IOException e) {
                    e.printStackTrace();
                    player.sendMessage("@blu@Unable to reload doors, check console.");
                }
                break;

            case "drops":
                Server.getDropManager().read();
                player.sendMessage("@blu@Reloaded Drops.");
                break;

            case "items":
                Server.itemHandler.loadItemList("item_config.cfg");
                Server.itemHandler.loadItemPrices("item_prices.txt");
                try {
                    ItemDefinition.load();
                } catch (IOException e) {
                    player.sendMessage("@blu@Unable to reload items, check console.");
                    e.printStackTrace();
                }
                player.sendMessage("@blu@Reloaded Items.");
                break;

            case "objects":
                try {
                    Server.getGlobalObjects().reloadObjectFile(player);
                    player.sendMessage("@blu@Reloaded Objects.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case "shops":
                Server.shopHandler = new ethos.world.ShopHandler();
                player.sendMessage("@blu@Reloaded Shops");
                break;

            case "npcs":
                Server.npcHandler = null;
                Server.npcHandler = new ethos.model.npcs.NPCHandler();
                player.sendMessage("@blu@Reloaded NPCs");
                break;

            case "punishments":
                try {
                    Server.getPunishments().initialize();
                    player.sendMessage("@blu@Reloaded Punishments.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case "looting":
                Config.BAG_AND_POUCH_PERMITTED = !Config.BAG_AND_POUCH_PERMITTED;
                player.sendMessage("" + (Config.BAG_AND_POUCH_PERMITTED ? "Enabled" : "Disabled" + "") + " bag and pouch.");
                break;

            case "unmute":
                username_being_punished = arg;
                punishment = punishments.getPunishment(PunishmentType.MUTE, username_being_punished);

                if (!punishments.contains(PunishmentType.MUTE, username_being_punished) || punishment == null) {
                    player.sendMessage("This account is not muted.");
                    return true;
                }

                punishments.remove(punishment);
                player.sendMessage("The account punishment was removed from the list, they will have acess on next reload.");
                return true;
            case "unban":
                username_being_punished = arg;
                punishment = punishments.getPunishment(PunishmentType.BAN, username_being_punished);

                if (!punishments.contains(PunishmentType.BAN, username_being_punished) || punishment == null) {
                    player.sendMessage("This account is not banned.");
                    return true;
                }

                punishments.remove(punishment);
                player.sendMessage("The account punishment was removed from the list, they will have acess on next reload.");
                return true;
            case "netmute":
                username_being_punished = arg;
                if (punishments.contains(PunishmentType.NET_MUTE, username_being_punished)) {
                    player.sendMessage("This player is already banned.");
                    return true;
                }
                Server.getPunishments().add(new Punishment(PunishmentType.NET_MUTE, banEnd, username_being_punished));
                Optional<Player> optionalPlayer = PlayerHandler.getOptionalPlayer(username_being_punished);
                if (optionalPlayer.isPresent()) {
                    Player c2 = optionalPlayer.get();
                    if (!player.getRights().isOrInherits(Right.OWNER) && c2.getRights().isOrInherits(Right.ADMINISTRATOR)) {
                        player.sendMessage("You cannot ban this player.");
                        return true;
                    }
                    if (Server.getMultiplayerSessionListener().inAnySession(c2)) {
                        MultiplayerSession session = Server.getMultiplayerSessionListener().getMultiplayerSession(c2);
                        session.finish(MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
                    }
                    c2.playerPass = "fuckingmoron";
                    c2.properLogout = true;
                    c2.disconnected = true;
                    c2.logout();

                    return true;
                }
                player.sendMessage(username_being_punished + " has been permanently banned.");
                return true;
            case "macban":
                username_being_punished = arg;
                banEnd = Long.MAX_VALUE;
                if (punishments.contains(PunishmentType.MAC_BAN, username_being_punished)) {
                    player.sendMessage("This player is already banned.");
                    return true;
                }
                Server.getPunishments().add(new Punishment(PunishmentType.MAC_BAN, banEnd, username_being_punished));
                Optional<Player> optional3Player = PlayerHandler.getOptionalPlayer(username_being_punished);
                if (optional3Player.isPresent()) {
                    Player c2 = optional3Player.get();
                    if (!player.getRights().isOrInherits(Right.OWNER) && c2.getRights().isOrInherits(Right.ADMINISTRATOR)) {
                        player.sendMessage("You cannot ban this player.");
                        return true;
                    }
                    if (Server.getMultiplayerSessionListener().inAnySession(c2)) {
                        MultiplayerSession session = Server.getMultiplayerSessionListener().getMultiplayerSession(c2);
                        session.finish(MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
                    }
                    c2.playerPass = "fuckingmoron";
                    c2.properLogout = true;
                    c2.disconnected = true;
                    c2.logout();

                    return true;
                }
                player.sendMessage(username_being_punished + " has been permanently banned.");
                return true;
            case "ban":
                username_being_punished = arg;
                banEnd = Long.MAX_VALUE;
                if (punishments.contains(PunishmentType.BAN, username_being_punished)) {
                    player.sendMessage("This player is already banned.");
                    return true;
                }
                Server.getPunishments().add(new Punishment(PunishmentType.BAN, banEnd, username_being_punished));
                Optional<Player> optional4Player = PlayerHandler.getOptionalPlayer(username_being_punished);
                if (optional4Player.isPresent()) {
                    Player c2 = optional4Player.get();
                    if (!player.getRights().isOrInherits(Right.OWNER) && c2.getRights().isOrInherits(Right.ADMINISTRATOR)) {
                        player.sendMessage("You cannot ban this player.");
                        return true;
                    }
                    if (Server.getMultiplayerSessionListener().inAnySession(c2)) {
                        MultiplayerSession session = Server.getMultiplayerSessionListener().getMultiplayerSession(c2);
                        session.finish(MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
                    }
                    c2.playerPass = "fuckingmoron";
                    c2.properLogout = true;
                    c2.disconnected = true;
                    c2.logout();

                    return true;
                }
                player.sendMessage(username_being_punished + " has been permanently banned.");
                return true;
            case "mute":
                long time_end;
                Optional<Player> optional2Player = PlayerHandler.getOptionalPlayer(arg);
                if (optional2Player.isPresent()) {
                    Player c2 = optional2Player.get();

                    time_end = System.currentTimeMillis() + 555555555 * 1000 * 60;

                    c2.marketMuteEnd = time_end;
                    c2.sendMessage("@red@You have been permanently muted by: " + player.playerName + ". on the market channel.");
                    player.sendMessage("Successfully muted " + c2.playerName + "");
                } else {
                    player.sendMessage(arg + " is not online. You can only marketmute online players.");
                }
                return true;
            case "teleto":
                if (command.startsWith("teleto")) {
                    for (int i = 0; i < Config.MAX_PLAYERS; i++) {
                        if (Server.playerHandler.players[i] != null) {
                            if (Server.playerHandler.players[i].playerName.equalsIgnoreCase(arg)) {
                                player.getPA().movePlayer(Server.playerHandler.players[i].getX(), Server.playerHandler.players[i].getY(), Server.playerHandler.players[i].heightLevel);
                            }
                        }
                    }
                }
                return true;
        }
        return false;
    }
}
