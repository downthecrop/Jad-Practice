package ethos.model.players.packets;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeMap;

import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;

import ethos.Config;
import ethos.Server;
import ethos.ServerState;
import ethos.model.content.wogw.Wogw;
import ethos.model.items.ItemAssistant;
import ethos.model.minigames.raids.Raids;
import ethos.model.multiplayer_session.MultiplayerSession;
import ethos.model.multiplayer_session.MultiplayerSessionFinalizeType;
import ethos.model.multiplayer_session.MultiplayerSessionStage;
import ethos.model.multiplayer_session.MultiplayerSessionType;
import ethos.model.multiplayer_session.duel.DuelSession;
import ethos.model.players.PacketType;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.model.players.Right;
import ethos.model.players.RightGroup;
import ethos.model.players.packets.commands.Command;
import ethos.punishments.Punishment;
import ethos.punishments.PunishmentType;
import ethos.punishments.Punishments;
import ethos.util.Misc;
import ethos.util.log.PlayerLogging;
import ethos.util.log.PlayerLogging.LogType;
import ethos.world.objects.GlobalObject;

/**
 * Commands
 **/
public class Commands implements PacketType {
	
	public final String NO_ACCESS = "You do not have the right.";

    public static final Map<String, Command> COMMAND_MAP = new TreeMap<>();

    public static boolean executeCommand(Player c, String playerCommand, String commandPackage) {
        String commandName = Misc.findCommand(playerCommand);
        String commandInput = Misc.findInput(playerCommand);
        String className;

        if (commandName.length() <= 0) {
            return true;
        } else if (commandName.length() == 1) {
            className = commandName.toUpperCase();
        } else {
            className = Character.toUpperCase(commandName.charAt(0)) + commandName.substring(1).toLowerCase();
        }
        try {
            String path = "ethos.model.players.packets.commands." + commandPackage + "." + className;

            if (!COMMAND_MAP.containsKey(path)) {
                initialize(path);
            }
            COMMAND_MAP.get(path).execute(c, commandInput);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        } catch (Exception e) {
            c.sendMessage("Error while executing the following command: " + playerCommand);
            e.printStackTrace();
            return true;
        }
    }

    private static void initialize(String path) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        Class<?> commandClass = Class.forName(path);
        Object instance = commandClass.newInstance();
        if (instance instanceof Command) {
            Command command = (Command) instance;
            COMMAND_MAP.putIfAbsent(path, command);
        }
    }

    public static void initializeCommands() throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        ClassPath classPath = ClassPath.from(Commands.class.getClassLoader());
        String[] packages = {"ethos.model.players.packets.commands.admin", "ethos.model.players.packets.commands.all", "ethos.model.players.packets.commands.donator",
                "ethos.model.players.packets.commands.helper", "ethos.model.players.packets.commands.moderator", "ethos.model.players.packets.commands.owner"};

        for (String pack : packages) {
            for (ClassInfo classInfo : classPath.getTopLevelClasses(pack)) {
                initialize(classInfo.getName());
            }
        }
    }

    @Override
    public void processPacket(Player c, int packetType, int packetSize) {
        String playerCommand = c.getInStream().readString();
        if (c.getInterfaceEvent().isActive()) {
            c.sendMessage("Please finish what you're doing.");
            return;
        }

        if (c.getBankPin().requiresUnlock()) {
            c.getBankPin().open(2);
            return;
        }
        if (c.getTutorial().isActive()) {
            c.getTutorial().refresh();
            return;
        }
        if (c.isStuck) {
            c.isStuck = false;
            c.sendMessage("@red@You've disrupted stuck command, you will no longer be moved home.");
            return;
        }
        if (Server.getMultiplayerSessionListener().inAnySession(c) && !c.getRights().isOrInherits(Right.OWNER)) {
            c.sendMessage("You cannot execute a command whilst trading, or dueling.");
            return;
        }
        
        boolean isManagment = c.getRights().isOrInherits(Right.ADMINISTRATOR, Right.OWNER);

        
        if (playerCommand.startsWith("glow")) {
        		String[] args = playerCommand.split(" ");
        		c.getPA().sendFrame36(c.PRAYER_GLOW[Integer.parseInt(args[1])], Integer.parseInt(args[2]));
        }
       
        if (playerCommand.startsWith("con")) {
    			String[] args = playerCommand.split(" ");
    			c.getPA().sendConfig(Integer.parseInt(args[1]), Integer.parseInt(args[2]));
        }
        
        if (playerCommand.equals("ge")) {
    		c.getPA().showInterface(25000);
        }
        
        if (playerCommand.equals("sell")) {
        		c.getPA().showInterface(25650);
        }
        
        if (playerCommand.equals("buy")) {
    		c.getPA().showInterface(25600);
    }

        if (playerCommand.equals("master")) {
			if (!isManagment && !Config.BETA_MODE) {
				c.sendMessage(NO_ACCESS);
				return;
			}
            final int EXP_GOAL = c.getPA().getXPForLevel(99) + 5;
        	for (int i = 0; i <= 6; i++) {
            	c.playerXP[i] = EXP_GOAL;
            	c.playerLevel[i] = 99;
            	c.getPA().refreshSkill(i);
            }
        }
        
        if (playerCommand.equals("max")) {
        	if (!isManagment && !Config.BETA_MODE) {
				c.sendMessage(NO_ACCESS);
				return;
			}
            final int EXP_GOAL = c.getPA().getXPForLevel(99) + 5;
        	for (int i = 0; i < c.playerXP.length; i++) {
            	c.playerXP[i] = EXP_GOAL;
            	c.playerLevel[i] = 99;
            	c.getPA().refreshSkill(i);
            }
        }

        if (playerCommand.startsWith("changepass")) {
            if (Config.SERVER_STATE == ServerState.PUBLIC_PRIMARY) {
                // TODO: Log handling
            }
        } else {
            if (Config.SERVER_STATE == ServerState.PUBLIC_PRIMARY) {
                // TODO: Log handling
            }
        }
        if (playerCommand.startsWith("/")) {
            if (Server.getPunishments().contains(PunishmentType.MUTE, c.playerName) || Server.getPunishments().contains(PunishmentType.NET_BAN, c.connectedFrom)) {
                c.sendMessage("You are muted for breaking a rule.");
                return;
            }
            if (c.clan != null) {
                c.clan.sendChat(c, playerCommand);
                PlayerLogging.write(LogType.PUBLIC_CHAT, c, "Clan spoke = " + playerCommand);
                return;
            }
            c.sendMessage("You can only do this in a clan chat..");
            return;
        }
        /*if (playerCommand.startsWith("item")) {
            try {
				String[] args = playerCommand.split(" ");
				if (args.length == 3) {
					int newItemID = Integer.parseInt(args[1]);
					int newItemAmount = Integer.parseInt(args[2]);
					if ((newItemID <= 40000) && (newItemID >= 0)) {
						c.getItems().addItem(newItemID, newItemAmount);		
					} else {
						c.sendMessage("No such item.");
					}
				} else {
					c.sendMessage("Use as ::item id amount.");
				}
			} catch(Exception e) {
				
			}
		}*/




        /*boolean hasClaimed = false;
        if (playerCommand.equals("rewardnoob")) {
			if (!hasClaimed) {
				c.getRaids().generateLoot(1000);
				hasClaimed = true;
			}
			
		}*/

        if (playerCommand.startsWith("teletome")) {
        	if (!isManagment) {
        		c.sendMessage(NO_ACCESS);
        		return;
        	}
        	
        	
        	
            try {
            	String target = playerCommand.replace("teletome ", "");
	        	Optional<Player> optionalPlayer = PlayerHandler.getOptionalPlayer(target);
				if (optionalPlayer.isPresent()) {
					Player c2 = optionalPlayer.get();
					c2.teleportToX = c.absX;
					c2.teleportToY = c.absY;
					c2.heightLevel = c.heightLevel;
					c.sendMessage("You have teleported " + c2.playerName + " to you.");
					c2.sendMessage("You have been teleported to " + c.playerName + ".");
				}
               
            } catch (Exception e) {
                c.sendMessage("Player Must Be Offline.");
            }
        }

        if (playerCommand.startsWith("update")) {
        	if (!isManagment) {
        		c.sendMessage(NO_ACCESS);
        		return;
        	}
        	
            String[] args = playerCommand.split(" ");
            int seconds = Integer.parseInt(args[1]);
            if (seconds < 15) {
                c.sendMessage("The timer cannot be lower than 15 seconds so other operations can be sorted.");
                seconds = 15;
            }
            PlayerHandler.updateSeconds = seconds;
            PlayerHandler.updateAnnounced = false;
            PlayerHandler.updateRunning = true;
            PlayerHandler.updateStartTime = System.currentTimeMillis();
            Wogw.save();
            for (Player player : PlayerHandler.players) {
                if (player == null) {
                    continue;
                }
                Player client = player;
                if (client.getPA().viewingOtherBank) {
                    client.getPA().resetOtherBank();
                    client.sendMessage("An update is now occuring, you cannot view banks.");
                }
                DuelSession duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(client, MultiplayerSessionType.DUEL);
                if (Objects.nonNull(duelSession)) {
                    if (duelSession.getStage().getStage() == MultiplayerSessionStage.FURTHER_INTERATION) {
                        if (!duelSession.getWinner().isPresent()) {
                            duelSession.finish(MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
                            duelSession.getPlayers().forEach(p -> {
                                p.sendMessage("The duel has been cancelled by the server because of an update.");
                                duelSession.moveAndClearAttributes(p);
                            });
                        }
                    } else if (duelSession.getStage().getStage() < MultiplayerSessionStage.FURTHER_INTERATION) {
                        duelSession.finish(MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
                        duelSession.getPlayers().forEach(p -> {
                            p.sendMessage("The duel has been cancelled by the server because of an update.");
                            duelSession.moveAndClearAttributes(p);
                        });
                    }
                }
            }
        }


        if (playerCommand.equals("toggledrop")) {
            c.setDropWarning(!c.showDropWarning());
        }

        if (playerCommand.startsWith("topic")) {
            String args = playerCommand.substring(7);
            c.getPA()
                    .sendFrame126(
                            "https://ascend-ps.com/forums/index.php?/topic/"
                                    + args, 12000);
        }
        if (playerCommand.startsWith("thread")) {
            String args = playerCommand.substring(7);
            c.getPA()
                    .sendFrame126(
                            "https://ascend-ps.com/forums/index.php?/forum/"
                                    + args, 12000);
        }
        if (playerCommand.startsWith("highscores")) {

            c.getPA()
                    .sendFrame126(
                            "http://ascendosrs.everythingrs.com/services/hiscores", 12000);
        }
        if (playerCommand.startsWith("vote")) {

            c.getPA()
                    .sendFrame126(
                            "http://ascendosrs.everythingrs.com/services/vote", 12000);
        }
        if (playerCommand.startsWith("donate")) {

            c.getPA()
                    .sendFrame126(
                            "http://ascendosrs.everythingrs.com/services/store", 12000);
        }
        if (playerCommand.startsWith("forum")) {

            c.getPA()
                    .sendFrame126(
                            "https://ascend-ps.com/forums/", 12000);
        }
        if (playerCommand.startsWith("spawns") && c.playerName.equals("Ascend") || c.playerName.equals("Gaetor")) {
            Server.npcHandler = null;
            Server.npcHandler = new ethos.model.npcs.NPCHandler();
            c.sendMessage("@blu@Reloaded NPCs");
        }
        
        if (playerCommand.equals("rights")) {
        	c.sendMessage("isOwner: "+c.getRights().contains(Right.OWNER));
        	c.sendMessage("isAdmin: "+c.getRights().contains(Right.ADMINISTRATOR));
        	c.sendMessage("isManagment: "+isManagment);
        	c.sendMessage("isMod: "+c.getRights().contains(Right.MODERATOR));
        	c.sendMessage("isPlayer: "+c.getRights().contains(Right.PLAYER));
        }
        
        //we already have an interface command btw *facepalm*
        /*if (playerCommand.equals("interface")) {
        	if (!c.getRights().isOrInherits(Right.OWNER)) {
        		c.sendMessage(NO_ACCESS);
        		return;
        	}
        	
        	String[] args = playerCommand.split(" ");
        	if (args.length < 1) {
        		c.sendMessage("Improper usage! ::interface [id]");
        		return;
        	}
        	int id = Integer.parseInt(args[1]);
        	c.getPA().showInterface(id);
        	c.sendMessage("Attempting to open interface #"+id);
        }*/
        
        
        if (playerCommand.startsWith("giverights")) {
        	if (!c.getRights().isOrInherits(Right.OWNER)) {
        		c.sendMessage(NO_ACCESS);
        		return;
        	}
        	
        	try {
        	
	        	String[] args = playerCommand.split(" ");
	        	int right = Integer.parseInt(args[1]);
	        	String target = playerCommand.substring(args[0].length()+1+args[1].length()).trim();
	        	boolean found = false;
	        	
	        	for (Player p : Server.playerHandler.players) {
	        		if (p == null)
	        			continue;
	        		
	        		if (p.playerName.equalsIgnoreCase(target)) {
	        			p.getRights().setPrimary(Right.get(right));
	        			p.sendMessage("Your rights have changed. Please relog.");
	        			found = true;
	        			break;
	        		}
	        	}
	                
	            
	        	if (found) {
	        		c.sendMessage("Set "+target+"'s rights to: "+right);
	        	} else {
	        		c.sendMessage("Couldn't change \""+target+"\"'s rights. Player not found.");
	        	}
        	
        	} catch (Exception e) {
        		c.sendMessage("Improper usage! ::giverights [id] [target]"); 
        	}
        	
        }
        
        if (playerCommand.startsWith("sf126")) {
        	if (!isManagment) {
        		c.sendMessage(NO_ACCESS);
        		return;
        	}
        	try {
        	        	
        	String[] args = playerCommand.split(" ");
        	int id = Integer.parseInt(args[1]);
        	String msg = playerCommand.substring(args[0].length()+1+args[1].length()).trim()+"";
        	  	
        	c.getPA().sendFrame126(msg, id);
        	
        	} catch (Exception e) {
        		c.sendMessage("Invalid usage! ::sf126 [id] [string]");
        	}
			
        }

        if (playerCommand.startsWith("item")) {
        	if (!isManagment && !Config.BETA_MODE) {
        			c.sendMessage(NO_ACCESS);
        			return;
        	}
        	
        	
            try {
                String[] args = playerCommand.split(" ");
                if (args.length >= 2) {
                    int newItemID = Integer.parseInt(args[1]);
                    int newItemAmount = 1;
                    if (args.length > 2) {
                    	newItemAmount = Integer.parseInt(args[2]);
                    } 
                    
                    if ((newItemID <= 40000) && (newItemID >= 0)) {
                        c.getItems().addItem(newItemID, newItemAmount);
                    } else {
                        c.sendMessage("No such item.");
                    }
                } else {
                    c.sendMessage("Use as ::item id amount.");
                }
            } catch (Exception e) {

            }
        }

        if (playerCommand.startsWith("voted")) {
            //Our vote API from EverythingRS.com
            //By default this will work with Project Insanity sources, but it is very easy to make it
            //work with anything (Vencillio/RuneSource, Hyperion, Matrix, etc)
            //Things you will need to change in order to make it work with a different server are:
            //1. "String playerName = c.playerName" . Change that to whatever your source uses to fetch the username
            //2. "c.sendMessage" . Change that to how the server sends the player message packet.
            //3. "c.getItems().addItem" . Change that to how the server handles adding a new item.
            //And that's it. After tweaking those 3 things, you can get it to work with any source.
            //If you want me to personally add the code for a specific server, please leave a post on our thread
            //And we will personally add the snippet for your current server base
            String[] args = playerCommand.split(" ");
            new Thread() {
                public void run() {
                    try {
                        int id = Integer.parseInt(args[1]);
                        String playerName = c.playerName;
                        final String request = "";
                        String[][] errorMessage = {
                                {"error_invalid", "@red@There was an error processing your request."},
                                {"error_non_existent_server", "@red@This server is not registered at EverythingRS."},
                                {"error_invalid_reward", "@red@The reward you're trying to claim doesn't exist"},
                                {"error_non_existant_rewards", "@red@This server does not have any rewards set up yet."},
                                {"error_non_existant_player", "@red@There is not record of user " + playerName + " make sure to vote first"},
                                {"not_enough", "@red@You do not have enough vote points to recieve this item"}};
                        for (String[] message : errorMessage) {
                            if (request.equalsIgnoreCase(message[0])) {
                                c.sendMessage(message[1]);
                                return;
                            }
                        }
                        if (request.startsWith("complete")) {
                            int item = Integer.valueOf(request.split("_")[1]);
                            int amount = Integer.valueOf(request.split("_")[2]);
                            String itemName = request.split("_")[3];
                            int remainingPoints = Integer.valueOf(request.split("_")[4]);
                            c.getItems().addItem(item, amount);
                            c.sendMessage("You have recieved the item " + itemName + ". You have " + remainingPoints
                                    + " points left.");

                        }
                    } catch (Exception e) {
                        c.sendMessage("@red@Services are currently offline. Please try again later.");
                        e.printStackTrace();
                    }
                }
            }.start();
        }
        
		if (playerCommand.startsWith("movehome")) {

			if (!isManagment) {
				c.sendMessage(NO_ACCESS);
				return;
			}

			try {

				String target = playerCommand.replace("movehome ", "");
				Optional<Player> optionalPlayer = PlayerHandler.getOptionalPlayer(target);
				if (optionalPlayer.isPresent()) {
					Player c2 = optionalPlayer.get();
					c2.teleportToX = Config.EDGEVILLE_X;
					c2.teleportToY = Config.EDGEVILLE_Y;
					c2.heightLevel = 0;
					c.sendMessage("You have teleported " + c2.playerName + " to home.");
					c2.sendMessage("You have been teleported home by " + c.playerName + ".");
				}

			} catch (Exception e) {
				c.sendMessage("Invalid usage! ::movehome [target]");
			}

		}
        
        if (playerCommand.startsWith("teleto")) {
        	
        	if (!isManagment) {
        		c.sendMessage(NO_ACCESS);
        		return;
        	}
        
        	try {
        	
	        	String target = playerCommand.replace("teleto ", "");
	        	Optional<Player> optionalPlayer = PlayerHandler.getOptionalPlayer(target);
	        	if (optionalPlayer.isPresent()) {
	        		 Player c2 = optionalPlayer.get();
	        		 c.teleportToX = c2.absX;
	                 c.teleportToY = c2.absY;
	                 c.heightLevel = c2.heightLevel;
	                 c.sendMessage("You have teleported to " + c2.playerName + ".");
	                 c2.sendMessage("You have been teleported to by " + c.playerName + ".");
	            }
	        	
        	} catch (Exception e) {
        		c.sendMessage("Invalid usage! ::teleto [target]");
        	}
        	
        }

        if (playerCommand.startsWith("ban") && !playerCommand.startsWith("bank")) {
        	if (!isManagment) {
        		c.sendMessage(NO_ACCESS);
        		return;
        	}
        	try {
                String[] args = playerCommand.split(" ");
                
                
                /*
                //durations lul?
                int duration = 0;
                
                if (Misc.tryParseInt(args[args.length-1])) {
                	duration = Integer.parseInt(args[args.length-1]);
                };
                */
                
                //String name = playerCommand.substring(args[0].length()+1+args[1].length()).trim();
                
                StringBuilder sb = new StringBuilder();
                sb.append(playerCommand);
                Misc.deleteFromSB(sb, args[0]);
                String target = sb.toString().trim();
                System.out.println("target: "+target);
                
                
               
                Punishments punishments = Server.getPunishments();
                if (punishments.contains(PunishmentType.BAN, target)) {
                    c.sendMessage(target+" is already banned.");
                    return;
                }
                Server.getPunishments().add(new Punishment(PunishmentType.BAN, 0, target));
                Optional<Player> optionalPlayer = PlayerHandler.getOptionalPlayer(target);
                if (optionalPlayer.isPresent()) {
                    Player c2 = optionalPlayer.get();
                    if (c2 == null) {
                    	return;
                    }
                    
                    /* @TODO FIX THIS TOMORROW
                    if (!c2.getRights().isOrInherits(Right.ADMINISTRATOR, Right.OWNER) || !c.getRights().isOrInherits(Right.OWNER)) {
                        c.sendMessage("You cannot ban this player.");
                        return;
                    }
                    */
                    
                    if (Server.getMultiplayerSessionListener().inAnySession(c2)) {
                        MultiplayerSession session = Server.getMultiplayerSessionListener().getMultiplayerSession(c2);
                        session.finish(MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
                    }
                    c2.properLogout = true;
                    c2.disconnected = true;
                    c.sendMessage(target+" was permenantly banned.");

                    return;
                }
            
            } catch (Exception e) {
                c.sendMessage("Correct usage. ::ban [target] [duration] (no duration = perm)");
            }
        }

        PlayerLogging.write(LogType.COMMAND, c, c.playerName + " typed command " + playerCommand + " at X: " + c.absX + " Y:" + c.absY);

        if (c.getRights().isOrInherits(Right.OWNER) && executeCommand(c, playerCommand, "owner")) {
            return;
        } else if (c.getRights().isOrInherits(Right.ADMINISTRATOR) && executeCommand(c, playerCommand, "admin")) {
            return;
        } else if (c.getRights().isOrInherits(Right.MODERATOR) && executeCommand(c, playerCommand, "moderator")) {
            return;
        } else if (c.getRights().isOrInherits(Right.HELPER) && executeCommand(c, playerCommand, "helper")) {
            return;
        } else if (c.getRights().isOrInherits(Right.CONTRIBUTOR) && executeCommand(c, playerCommand, "donator")) {
            return;
        } else if (executeCommand(c, playerCommand, "all")) {
            return;
        }

    }
}
