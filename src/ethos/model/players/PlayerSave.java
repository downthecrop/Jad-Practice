package ethos.model.players;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import ethos.Config;
import ethos.ServerState;
import ethos.model.content.LootingBag.LootingBagItem;
import ethos.model.content.Tutorial.Stage;
import ethos.model.content.achievement_diary.DifficultyAchievementDiary.EntryDifficulty;
import ethos.model.content.achievement_diary.ardougne.ArdougneDiaryEntry;
import ethos.model.content.achievement_diary.desert.DesertDiaryEntry;
import ethos.model.content.achievement_diary.falador.FaladorDiaryEntry;
import ethos.model.content.achievement_diary.fremennik.FremennikDiaryEntry;
import ethos.model.content.achievement_diary.kandarin.KandarinDiaryEntry;
import ethos.model.content.achievement_diary.karamja.KaramjaDiaryEntry;
import ethos.model.content.achievement_diary.lumbridge_draynor.LumbridgeDraynorDiaryEntry;
import ethos.model.content.achievement_diary.morytania.MorytaniaDiaryEntry;
import ethos.model.content.achievement_diary.varrock.VarrockDiaryEntry;
import ethos.model.content.achievement_diary.western_provinces.WesternDiaryEntry;
import ethos.model.content.achievement_diary.wilderness.WildernessDiaryEntry;
import ethos.model.content.barrows.brothers.Brother;
import ethos.model.content.dailytasks.TaskTypes;
import ethos.model.content.dailytasks.DailyTasks.PossibleTasks;
import ethos.model.content.kill_streaks.Killstreak;
import ethos.model.content.presets.Preset;
import ethos.model.content.presets.PresetContainer;
import ethos.model.content.titles.Title;
import ethos.model.items.GameItem;
import ethos.model.items.bank.BankItem;
import ethos.model.items.bank.BankTab;
import ethos.model.players.mode.Mode;
import ethos.model.players.mode.ModeType;
import ethos.model.players.skills.farming.Farming;
import ethos.model.players.skills.slayer.SlayerMaster;
import ethos.model.players.skills.slayer.Task;
import ethos.util.Misc;
import ethos.util.log.PlayerLogging;
import ethos.util.log.PlayerLogging.LogType;

import java.util.Objects;
import java.util.Optional;

public class PlayerSave {

	/**
	 * Tells us whether or not the player exists for the specified name.
	 * 
	 * @param name
	 * @return
	 */
	public static boolean playerExists(String name) {
		File file = new File("./Characters/" + name + ".txt");
		return file.exists();
	}

	public static void addItemToFile(String name, int itemId, int itemAmount) {
		if (itemId < 0 || itemAmount < 0) {
			Misc.println("Illegal operation: Item id or item amount cannot be negative.");
			return;
		}
		BankItem item = new BankItem(itemId + 1, itemAmount);
		if (!playerExists(name)) {
			Misc.println("Illegal operation: Player account does not exist, validate name.");
			return;
		}
		if (PlayerHandler.isPlayerOn(name)) {
			Misc.println("Illegal operation: Attempted to modify the account of a player online.");
			return;
		}
		File character = new File("./Characters/" + name + ".txt");
		List<String> lines = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new FileReader(character))) {
			String line;
			while ((line = reader.readLine()) != null) {
				lines.add(line);
			}
			reader.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		BankTab[] tabs = new BankTab[] { new BankTab(0), new BankTab(1), new BankTab(2), new BankTab(3), new BankTab(4), new BankTab(5), new BankTab(6), new BankTab(7),
				new BankTab(8), };
		String token, token2;
		String[] token3 = new String[3];
		int spot = 0;
		for (String line : lines) {
			if (line == null) {
				continue;
			}
			line = line.trim();
			spot = line.indexOf("=");
			if (spot == -1) {
				continue;
			}
			token = line.substring(0, spot);
			token = token.trim();
			token2 = line.substring(spot + 1);
			token2 = token2.trim();
			token3 = token2.split("\t");
			if (token.equals("bank-tab")) {
				int tabId = Integer.parseInt(token3[0]);
				int id = Integer.parseInt(token3[1]);
				int amount = Integer.parseInt(token3[2]);
				tabs[tabId].add(new BankItem(id, amount));
			}
		}
		boolean inserted = false;
		for (BankTab tab : tabs) {
			if (tab.contains(item) && tab.spaceAvailable(item)) {
				tab.add(item);
				inserted = true;
				break;
			}
		}
		if (!inserted) {
			for (BankTab tab : tabs) {
				if (tab.freeSlots() > 0) {
					tab.add(item);
					inserted = true;
					break;
				}
			}
		}
		if (!inserted) {
			Misc.println("Item could not be added to offline account, no free space in bank.");
			return;
		}
		int startIndex = Misc.indexOfPartialString(lines, "bank-tab");
		int lastIndex = Misc.lastIndexOfPartialString(lines, "bank-tab");
		if (lastIndex != startIndex && startIndex > 0 && lastIndex > 0) {
			List<String> cutout = lines.subList(startIndex, lastIndex);
			List<String> bankData = new ArrayList<>(lastIndex - startIndex);
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < Config.BANK_SIZE; j++) {
					if (j > tabs[i].size() - 1)
						break;
					BankItem bankItem = tabs[i].getItem(j);
					if (bankItem == null) {
						continue;
					}
					bankData.add("bank-tab = " + i + "\t" + bankItem.getId() + "\t" + bankItem.getAmount());
				}
			}
			lines.removeAll(cutout);
			lines.addAll(startIndex, bankData);
		}
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(character))) {
			for (String line : lines) {
				writer.write(line);
				writer.newLine();
			}
			writer.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	/**
	 * Tells us whether or not the specified name has the friend added.
	 * 
	 * @param name
	 * @param friend
	 * @return
	 */
	public static boolean isFriend(String name, String friend) {
		long nameLong = Misc.playerNameToInt64(friend);
		long[] friends = getFriends(name);
		if (friends != null && friends.length > 0) {
			for (int index = 0; index < friends.length; index++) {
				if (friends[index] == nameLong) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Returns a characters friends in the form of a long array.
	 * 
	 * @param name
	 * @return
	 */
	public static long[] getFriends(String name) {
		String line = "";
		String token = "";
		String token2 = "";
		long[] readFriends = new long[200];
		long[] friends = null;
		int totalFriends = 0;
		int index = 0;
		File input = new File("./Characters/" + name + ".txt");
		if (!input.exists()) {
			return null;
		}
		try (BufferedReader reader = new BufferedReader(new FileReader(input))) {
			while ((line = reader.readLine()) != null) {
				line = line.trim();
				int spot = line.indexOf("=");
				if (spot > -1) {
					token = line.substring(0, spot);
					token = token.trim();
					token2 = line.substring(spot + 1);
					token2 = token2.trim();
					if (token.equals("character-friend")) {
						try {
							readFriends[index] = Long.parseLong(token2);
							totalFriends++;
						} catch (NumberFormatException nfe) {
						}
					}
				}
			}
			reader.close();
			if (totalFriends > 0) {
				friends = new long[totalFriends];
				for (int i = 0; i < totalFriends; i++) {
					friends[i] = readFriends[i];
				}
				return friends;
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return null;
	}

	/**
	 * Loading
	 **/
	//@SuppressWarnings("resource")
	public static int loadGame(Player p, String playerName, String playerPass) {
		String line = "";
		String token = "";
		String token2 = "";
		String[] token3 = new String[3];
		boolean EndOfFile = false;
		int ReadMode = 0;
		BufferedReader characterfile = null;
		boolean File1 = false;
		try {
			characterfile = new BufferedReader(new FileReader("./Characters/" + playerName + ".txt"));
			File1 = true;
		} catch (FileNotFoundException fileex1) {
		}

		if (File1) {
			// new File ("./characters/"+playerName+".txt");
		} else {
			Misc.println(playerName + ": character file not found.");
			p.newPlayer = false;
			return 0;
		}
		try {
			line = characterfile.readLine();
		} catch (IOException ioexception) {
			Misc.println(playerName + ": error loading file.");
			return 3;
		}
		while (EndOfFile == false && line != null) {
			line = line.trim();
			int spot = line.indexOf("=");
			if (spot > -1) {
				token = line.substring(0, spot);
				token = token.trim();
				token2 = line.substring(spot + 1);
				token2 = token2.trim();
				token3 = token2.split("\t");
				switch (ReadMode) {
				case 1:
					if (token.equals("character-password")) {
						if (playerPass.equalsIgnoreCase(token2) || Misc.basicEncrypt(playerPass).equals(token2) || Misc.md5Hash(playerPass).equals(token2)) {
							playerPass = token2;
						} else {
							return 3;
						}
					}
					break;
				case 2:
					if (token.equals("character-height")) {
						p.heightLevel = Integer.parseInt(token2);
					} else if (token.equals("character-hp")) {
						p.getHealth().setAmount(Integer.parseInt(token2));
						if (p.getHealth().getAmount() <= 0) {
							p.getHealth().setAmount(10);
						}
					}else if (token.equals("character-mac-address")) {
						if (!p.getMacAddress().equalsIgnoreCase(token2)) {
							PlayerLogging.write(LogType.CHANGE_MAC_ADDRESS, p, "Mac Address Changed: previous = " + token2 + ", new = " + p.getMacAddress());
						}
						p.setMacAddress(p.getMacAddress());
					} else if (token.equals("character-ip-address")) {
						if (!p.getIpAddress().equalsIgnoreCase(token2)) {
							PlayerLogging.write(LogType.CHANGE_IP_ADDRESS, p, "Ip Address Changed: previous = " + token2 + ", new = " + p.getIpAddress());
						}
						p.setIpAddress(p.getIpAddress());
					} else if (token.equals("play-time")) {
						p.playTime = Integer.parseInt(token2);
					} else if (token.equals("last-clan")) {
						p.setLastClanChat(token2);
					} else if (token.equals("character-specRestore")) {
						p.specRestore = Integer.parseInt(token2);
					} else if (token.equals("character-posx")) {
						p.teleportToX = (Integer.parseInt(token2) <= 0 ? 3210 : Integer.parseInt(token2));
					} else if (token.equals("character-posy")) {
						p.teleportToY = (Integer.parseInt(token2) <= 0 ? 3424 : Integer.parseInt(token2));
					} else if (token.equals("character-rights")) {
						p.getRights().setPrimary(Right.get(Integer.parseInt(token2)));
					} else if (token.equals("character-rights-secondary")) { // sound like an activist group
						Arrays.stream(token3).forEach(right -> p.getRights().add(Right.get(Integer.parseInt(right))));
					} else if (token.equals("revert-option")) {
						p.setRevertOption(token2);
					} else if (token.equals("revert-delay")) {
						p.setRevertModeDelay(Long.parseLong(token2));
					} else if (token.equals("mode")) {
						ModeType type = null;
						try {
							if (token2.equals("NONE")) {
								token2 = "REGULAR";
							}
							type = Enum.valueOf(ModeType.class, token2);
						} catch (NullPointerException | IllegalArgumentException e) {
							break;
						}
						Mode mode = Mode.forType(type);
						p.setMode(mode);
					} else if (token.equals("tutorial-stage")) {
						Stage stage = null;
						try {
							stage = Enum.valueOf(Stage.class, token2);
						} catch (IllegalArgumentException | NullPointerException e) {
							break;
						}
						p.getTutorial().setStage(stage);
					} else if (token.equals("character-title-updated")) {
						p.getTitles().setCurrentTitle(token2);
					} else if (token.equals("experience-counter")) {
						p.setExperienceCounter(Long.parseLong(token2));
					} else if (token.equals("killed-players")) {
						if (!p.getPlayerKills().getList().contains(token2))
							p.getPlayerKills().getList().add(token2);
					} else if (token.equals("connected-from")) {
						p.lastConnectedFrom.add(token2);
					} else if (token.equals("horror-from-deep")) {
						p.horrorFromDeep = Integer.parseInt(token2);
					} else if (token.equals("run-energy")) {
						p.setRunEnergy(Integer.parseInt(token2));
					} else if (token.equals("bank-pin")) {
						p.getBankPin().setPin(token2);
					} else if (token.equals("bank-pin-cancellation")) {
						p.getBankPin().setAppendingCancellation(Boolean.parseBoolean(token2));
					} else if (token.equals("bank-pin-cancellation-delay")) {
						p.getBankPin().setCancellationDelay(Long.parseLong(token2));
					} else if (token.equals("bank-pin-unlock-delay")) {
						p.getBankPin().setUnlockDelay(Long.parseLong(token2));
					} else if (token.equals("placeholders")) {
						p.placeHolders = Boolean.parseBoolean(token2);
					} else if (token.equals("newStarter")) {
						p.newStarter = Boolean.parseBoolean(token2);
					} else if (token.equals("dailyTaskDate")) {
						p.dailyTaskDate = Integer.parseInt(token2);
					} else if (token.equals("totalDailyDone")) {
						p.totalDailyDone = Integer.parseInt(token2);
					} else if (token.equals("currentTask")) {
						if(token2 != null && token2.equals("") == false)
							p.currentTask = PossibleTasks.valueOf(token2); //Integer.parseInt(token2);
					} else if (token.equals("completedDailyTask")) {
						p.completedDailyTask = Boolean.parseBoolean(token2);
					} else if (token.equals("playerChoice")) {
						if(token2 != null && token2.equals("") == false)
							p.playerChoice = TaskTypes.valueOf(token2); //Integer.parseInt(token2);
					} else if (token.equals("dailyEnabled")) {
						p.dailyEnabled = Boolean.parseBoolean(token2);
					} else if (token.equals("show-drop-warning")) {
						p.setDropWarning(Boolean.parseBoolean(token2));
					} else if (token.equals("hourly-box-toggle")) {
						p.setHourlyBoxToggle(Boolean.parseBoolean(token2));
					} else if (token.equals("fractured-crystal-toggle")) {
						p.setFracturedCrystalToggle(Boolean.parseBoolean(token2));
					} else if (token.equals("accept-aid")) {
						p.acceptAid = Boolean.parseBoolean(token2);
					} else if (token.equals("did-you-know")) {
						p.didYouKnow = Boolean.parseBoolean(token2);
					} else if (token.equals("raidPoints")) {
						p.setRaidPoints(Integer.parseInt(token2));
					}else if (token.equals("maRound")) {
						p.maRound = (Integer.parseInt(token2));
					}else if (token.equals("raidCount")) {
						p.raidCount = (Integer.parseInt(token2));
					} else if (token.equals("lootvalue")) {
						p.lootValue = Integer.parseInt(token2);
					} else if (token.equals("startPack")) {
						p.startPack = Boolean.parseBoolean(token2);
					} else if (token.equals("crystalDrop")) {
						p.crystalDrop = Boolean.parseBoolean(token2);
					} else if (token.equals("lastLoginDate")) {
						p.lastLoginDate = Integer.parseInt(token2);
					} else if (token.equals("summonId")) {
						p.summonId = Integer.parseInt(token2);
					} else if (token.equals("has-npc")) {
						p.hasFollower = Boolean.parseBoolean(token2);
					} else if (token.equals("setPin")) {
						p.setPin = Boolean.parseBoolean(token2);
					} else if (token.equals("hasBankpin")) {
						p.hasBankpin = Boolean.parseBoolean(token2);
					} else if (token.equals("rfd-gloves")) {
						p.rfdGloves = Integer.parseInt(token2);
					} else if (token.equals("wave-id")) {
						p.waveId = Integer.parseInt(token2);
					} else if (token.equals("wave-type")) {
						p.waveType = Integer.parseInt(token2);
					} else if (token.equals("wave-info")) {
						for (int i = 0; i < p.waveInfo.length; i++)
							p.waveInfo[i] = Integer.parseInt(token3[i]);
					} else if (token.equals("counters")) {
						for (int i = 0; i < p.counters.length; i++)
							p.counters[i] = Integer.parseInt(token3[i]);
					} else if (token.equals("max-cape")) {
						for (int i = 0; i < p.maxCape.length; i++)
							p.maxCape[i] = Boolean.parseBoolean(token3[i]);
					} else if (token.equals("master-clue-reqs")) {
						for (int i = 0; i < p.masterClueRequirement.length; i++)
							p.masterClueRequirement[i] = Integer.parseInt(token3[i]);
					} else if (token.equals("quickprayer")) {
						for (int j = 0; j < token3.length; j++) {
							p.getQuick().getNormal()[j] = Boolean.parseBoolean(token3[j]);
						}
					} else if (token.equals("zulrah-best-time")) {
						p.setBestZulrahTime(Long.parseLong(token2));
					} else if (token.equals("toxic-staff")) {
						p.setToxicStaffOfTheDeadCharge(Integer.parseInt(token2));
					} else if (token.equals("toxic-pipe-ammo")) {
						p.setToxicBlowpipeAmmo(Integer.parseInt(token2));
					} else if (token.equals("toxic-pipe-amount")) {
						p.setToxicBlowpipeAmmoAmount(Integer.parseInt(token2));
					} else if (token.equals("toxic-pipe-charge")) {
						p.setToxicBlowpipeCharge(Integer.parseInt(token2));
					} else if (token.equals("serpentine-helm")) {
						p.setSerpentineHelmCharge(Integer.parseInt(token2));
					} else if (token.equals("trident-of-the-seas")) {
						p.setTridentCharge(Integer.parseInt(token2));
					} else if (token.equals("trident-of-the-swamp")) {
						p.setToxicTridentCharge(Integer.parseInt(token2));
					} else if (token.equals("arclight-charge")) {
						p.setArcLightCharge(Integer.parseInt(token2));
					} else if (token.equals("crystal-bow-shots")) {
						p.crystalBowArrowCount = Integer.parseInt(token2);
					} else if (token.equals("skull-timer")) {
						p.skullTimer = Integer.parseInt(token2);
					} else if (token.equals("magic-book")) {
						p.playerMagicBook = Integer.parseInt(token2);
					} else if (token.equals("slayer-recipe") || token.equals("slayer-helmet")) {
						p.getSlayer().setHelmetCreatable(Boolean.parseBoolean(token2));
					} else if (token.equals("slayer-imbued-helmet")) {
						p.getSlayer().setHelmetImbuedCreatable(Boolean.parseBoolean(token2));
					} else if (token.equals("bigger-boss-tasks")) {
						p.getSlayer().setBiggerBossTasks(Boolean.parseBoolean(token2));
					} else if (token.equals("cerberus-route")) {
						p.getSlayer().setCerberusRoute(Boolean.parseBoolean(token2));
					} else if (token.equals("superior-slayer")) {
						p.getSlayer().setBiggerAndBadder(Boolean.parseBoolean(token2));
					} else if (token.equals("slayer-tasks-completed")) {
						p.slayerTasksCompleted = Integer.parseInt(token2);
					} else if (token.equals("claimedReward")) {
						p.claimedReward = Boolean.parseBoolean(token2);
					} else if (p.getBarrows().getBrother(token).isPresent()) {
						p.getBarrows().getBrother(token).get().setDefeated(Boolean.parseBoolean(token2));
					} else if (token.equals("barrows-final-brother")) {
						p.getBarrows().setLastBrother(token2);
					} else if (token.equals("barrows-monsters-killcount")) {
						p.getBarrows().setMonstersKilled(Integer.parseInt(token2));
					} else if (token.equals("barrows-completed")) {
						p.getBarrows().setCompleted(Boolean.valueOf(token2));
					} else if (token.equals("special-amount")) {
						p.specAmount = Double.parseDouble(token2);
					} else if (token.equals("prayer-amount")) {
						p.prayerPoint = Double.parseDouble(token2);
					} else if (token.equals("dragonfire-shield-charge")) {
						p.setDragonfireShieldCharge(Integer.parseInt(token2));
					} else if (token.equals("pkp")) {
						p.pkp = Integer.parseInt(token2);
					} else if (token.equals("votePoints")) {
						p.votePoints = Integer.parseInt(token2);
					} else if (token.equals("bloodPoints")) {
						p.bloodPoints = Integer.parseInt(token2);
					} else if (token.equals("donP")) {
						p.donatorPoints = Integer.parseInt(token2);
					} else if (token.equals("donA")) {
						p.amDonated = Integer.parseInt(token2);
					} else if (token.equals("prestige-points")) {
						p.prestigePoints = Integer.parseInt(token2);
					}  else if (token.equals("xpLock")) {
						p.expLock = Boolean.parseBoolean(token2);
					} else if (line.startsWith("KC")) {
						p.killcount = Integer.parseInt(token2);
					} else if (line.startsWith("DC")) {
						p.deathcount = Integer.parseInt(token2);
					} else if (line.startsWith("last-incentive")) {
						p.setLastIncentive(Long.parseLong(token2));
					} else if (token.equals("teleblock-length")) {
						p.teleBlockDelay = System.currentTimeMillis();
						p.teleBlockLength = Integer.parseInt(token2);
					} else if (token.equals("pc-points")) {
						p.pcPoints = Integer.parseInt(token2);
					} else if (token.equals("total-raids")) {
						p.totalRaidsFinished = Integer.parseInt(token2);
					} else if (token.equals("total-rogue-kills")) {
						p.getBH().setTotalRogueKills(Integer.parseInt(token2));
					} else if (token.equals("total-hunter-kills")) {
						p.getBH().setTotalHunterKills(Integer.parseInt(token2));
					} else if (token.equals("target-time-delay")) {
						p.getBH().setDelayedTargetTicks(Integer.parseInt(token2));
					} else if (token.equals("bh-penalties")) {
						p.getBH().setWarnings(Integer.parseInt(token2));
					} else if (token.equals("bh-bounties")) {
						p.getBH().setBounties(Integer.parseInt(token2));
					} else if (token.equals("statistics-visible")) {
						p.getBH().setStatisticsVisible(Boolean.parseBoolean(token2));
					} else if (token.equals("spell-accessible")) {
						p.getBH().setSpellAccessible(Boolean.parseBoolean(token2));
					} else if (token.equals("killStreak")) {
						p.killStreak = Integer.parseInt(token2);
					} else if (token.equals("achievement-points")) {
						p.getAchievements().setPoints(Integer.parseInt(token2));
					} else if (token.equals("achievement-items")) {
						for (int i = 0; i < token3.length; i++)
							p.getAchievements().setBoughtItem(i, Integer.parseInt(token3[i]));
						//Varrock claimed
					} else if (token.equals("VarrockClaimedDiaries")) {
						String[] claimedRaw = token2.split(",");
						for (String claim : claimedRaw) {
							EntryDifficulty.forString(claim).ifPresent(diff -> p.getDiaryManager().getVarrockDiary().claim(diff));
						}
					} else if (token.equals("ArdougneClaimedDiaries")) {
						String[] claimedRaw = token2.split(",");
						for (String claim : claimedRaw) {
							EntryDifficulty.forString(claim).ifPresent(diff -> p.getDiaryManager().getArdougneDiary().claim(diff));
						}
					} else if (token.equals("DesertClaimedDiaries")) {
						String[] claimedRaw = token2.split(",");
						for (String claim : claimedRaw) {
							EntryDifficulty.forString(claim).ifPresent(diff -> p.getDiaryManager().getDesertDiary().claim(diff));
						}
					} else if (token.equals("FaladorClaimedDiaries")) {
						String[] claimedRaw = token2.split(",");
						for (String claim : claimedRaw) {
							EntryDifficulty.forString(claim).ifPresent(diff -> p.getDiaryManager().getFaladorDiary().claim(diff));
						}
					} else if (token.equals("FremennikClaimedDiaries")) {
						String[] claimedRaw = token2.split(",");
						for (String claim : claimedRaw) {
							EntryDifficulty.forString(claim).ifPresent(diff -> p.getDiaryManager().getFremennikDiary().claim(diff));
						}
					} else if (token.equals("KandarinClaimedDiaries")) {
						String[] claimedRaw = token2.split(",");
						for (String claim : claimedRaw) {
							EntryDifficulty.forString(claim).ifPresent(diff -> p.getDiaryManager().getKandarinDiary().claim(diff));
						}
					} else if (token.equals("KaramjaClaimedDiaries")) {
						String[] claimedRaw = token2.split(",");
						for (String claim : claimedRaw) {
							EntryDifficulty.forString(claim).ifPresent(diff -> p.getDiaryManager().getKaramjaDiary().claim(diff));
						}
					} else if (token.equals("LumbridgeClaimedDiaries")) {
						String[] claimedRaw = token2.split(",");
						for (String claim : claimedRaw) {
							EntryDifficulty.forString(claim).ifPresent(diff -> p.getDiaryManager().getLumbridgeDraynorDiary().claim(diff));
						}
					} else if (token.equals("MorytaniaClaimedDiaries")) {
						String[] claimedRaw = token2.split(",");
						for (String claim : claimedRaw) {
							EntryDifficulty.forString(claim).ifPresent(diff -> p.getDiaryManager().getMorytaniaDiary().claim(diff));
						}
					} else if (token.equals("WesternClaimedDiaries")) {
						String[] claimedRaw = token2.split(",");
						for (String claim : claimedRaw) {
							EntryDifficulty.forString(claim).ifPresent(diff -> p.getDiaryManager().getWesternDiary().claim(diff));
						}
					} else if (token.equals("WildernessClaimedDiaries")) {
						String[] claimedRaw = token2.split(",");
						for (String claim : claimedRaw) {
							EntryDifficulty.forString(claim).ifPresent(diff -> p.getDiaryManager().getWildernessDiary().claim(diff));
						}
					} else if (token.equals("diaries")) {
						try {
						String raw = token2;
						String[] components = raw.split(",");
						for (String comp : components) {
							if (comp.isEmpty()) {
								continue;
							}
								// Varrock
								Optional<VarrockDiaryEntry> varrock = VarrockDiaryEntry.fromName(comp);
								if (varrock.isPresent()) {
									p.getDiaryManager().getVarrockDiary().nonNotifyComplete(varrock.get());
								}
								// Ardougne
								Optional<ArdougneDiaryEntry> ardougne = ArdougneDiaryEntry.fromName(comp);
								if (ardougne.isPresent()) {
									p.getDiaryManager().getArdougneDiary().nonNotifyComplete(ardougne.get());
								}
								// Desert
								Optional<DesertDiaryEntry> desert = DesertDiaryEntry.fromName(comp);
								if (desert.isPresent()) {
									p.getDiaryManager().getDesertDiary().nonNotifyComplete(desert.get());
								}
								// Falador
								Optional<FaladorDiaryEntry> falador = FaladorDiaryEntry.fromName(comp);
								if (falador.isPresent()) {
									p.getDiaryManager().getFaladorDiary().nonNotifyComplete(falador.get());
								}
								// Fremennik
								Optional<FremennikDiaryEntry> fremennik = FremennikDiaryEntry.fromName(comp);
								if (fremennik.isPresent()) {
									p.getDiaryManager().getFremennikDiary().nonNotifyComplete(fremennik.get());
								}
								// Kandarin
								Optional<KandarinDiaryEntry> kandarin = KandarinDiaryEntry.fromName(comp);
								if (kandarin.isPresent()) {
									p.getDiaryManager().getKandarinDiary().nonNotifyComplete(kandarin.get());
								}
								// Karamja
								Optional<KaramjaDiaryEntry> karamja = KaramjaDiaryEntry.fromName(comp);
								if (karamja.isPresent()) {
									p.getDiaryManager().getKaramjaDiary().nonNotifyComplete(karamja.get());
								}
								// Lumbridge
								Optional<LumbridgeDraynorDiaryEntry> lumbridge = LumbridgeDraynorDiaryEntry.fromName(comp);
								if (lumbridge.isPresent()) {
									p.getDiaryManager().getLumbridgeDraynorDiary().nonNotifyComplete(lumbridge.get());
								}
								// Morytania
								Optional<MorytaniaDiaryEntry> morytania = MorytaniaDiaryEntry.fromName(comp);
								if (morytania.isPresent()) {
									p.getDiaryManager().getMorytaniaDiary().nonNotifyComplete(morytania.get());
								}
								// Western
								Optional<WesternDiaryEntry> western = WesternDiaryEntry.fromName(comp);
								if (western.isPresent()) {
									p.getDiaryManager().getWesternDiary().nonNotifyComplete(western.get());
								}
								// Wilderness
								Optional<WildernessDiaryEntry> wilderness = WildernessDiaryEntry.fromName(comp);
								if (wilderness.isPresent()) {
									p.getDiaryManager().getWildernessDiary().nonNotifyComplete(wilderness.get());
								}
						}
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else if (token.equals("partialDiaries")) {
						String raw = token2;
						String[] components = raw.split(",");
						try {
						for (String comp : components) {
							if (comp.isEmpty()) {
								continue;
							}
							String[] part = comp.split(":");
							int stage = Integer.parseInt(part[1]);
							//Varrock
							Optional<VarrockDiaryEntry> varrock = VarrockDiaryEntry.fromName(part[0]);
							if (varrock.isPresent()) {
								p.getDiaryManager().getVarrockDiary().setAchievementStage(varrock.get(), stage, false);
							}
							//Ardougne
							Optional<ArdougneDiaryEntry> ardougne = ArdougneDiaryEntry.fromName(part[0]);
							if (ardougne.isPresent()) {
								p.getDiaryManager().getArdougneDiary().setAchievementStage(ardougne.get(), stage, false);
							}
							//Desert
							Optional<DesertDiaryEntry> desert = DesertDiaryEntry.fromName(part[0]);
							if (desert.isPresent()) {
								p.getDiaryManager().getDesertDiary().setAchievementStage(desert.get(), stage, false);
							}
							//Falador
							Optional<FaladorDiaryEntry> falador = FaladorDiaryEntry.fromName(part[0]);
							if (falador.isPresent()) {
								p.getDiaryManager().getFaladorDiary().setAchievementStage(falador.get(), stage, false);
							}
							//Fremennik
							Optional<FremennikDiaryEntry> fremennik = FremennikDiaryEntry.fromName(part[0]);
							if (fremennik.isPresent()) {
								p.getDiaryManager().getFremennikDiary().setAchievementStage(fremennik.get(), stage, false);
							}
							//Kandarin
							Optional<KandarinDiaryEntry> kandarin = KandarinDiaryEntry.fromName(part[0]);
							if (kandarin.isPresent()) {
								p.getDiaryManager().getKandarinDiary().setAchievementStage(kandarin.get(), stage, false);
							}
							//Karamja
							Optional<KaramjaDiaryEntry> karamja = KaramjaDiaryEntry.fromName(part[0]);
							if (karamja.isPresent()) {
								p.getDiaryManager().getKaramjaDiary().setAchievementStage(karamja.get(), stage, false);
							}
							//Lumbridge
							Optional<LumbridgeDraynorDiaryEntry> lumbridge = LumbridgeDraynorDiaryEntry.fromName(part[0]);
							if (lumbridge.isPresent()) {
								p.getDiaryManager().getLumbridgeDraynorDiary().setAchievementStage(lumbridge.get(), stage, false);
							}
							//Morytania
							Optional<MorytaniaDiaryEntry> morytania = MorytaniaDiaryEntry.fromName(part[0]);
							if (morytania.isPresent()) {
								p.getDiaryManager().getMorytaniaDiary().setAchievementStage(morytania.get(), stage, false);
							}
							//Western
							Optional<WesternDiaryEntry> western = WesternDiaryEntry.fromName(part[0]);
							if (western.isPresent()) {
								p.getDiaryManager().getWesternDiary().setAchievementStage(western.get(), stage, false);
							}
							//Wilderness
							Optional<WildernessDiaryEntry> wilderness = WildernessDiaryEntry.fromName(part[0]);
							if (wilderness.isPresent()) {
								p.getDiaryManager().getWildernessDiary().setAchievementStage(wilderness.get(), stage, false);
							}
						}
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else if (token.equals("bonus-end")) {
						p.bonusXpTime = Long.parseLong(token2);
					} else if (token.equals("jail-end")) {
						p.jailEnd = Long.parseLong(token2);
					} else if (token.equals("mute-end")) {
						p.muteEnd = Long.parseLong(token2);
					} else if (token.equals("last-yell")) {
						p.lastYell = Long.parseLong(token2);
					} else if (token.equals("marketmute-end")) {
						p.marketMuteEnd = Long.parseLong(token2);
					} else if (token.equals("splitChat")) {
						p.splitChat = Boolean.parseBoolean(token2);
					} else if (token.equals("slayer-task")) {
						Optional<Task> task = SlayerMaster.get(token2);
						p.getSlayer().setTask(task);
					} else if (token.equals("slayer-master")) {
						p.getSlayer().setMaster(Integer.parseInt(token2));
					} else if (token.equals("slayerPoints")) {
						p.getSlayer().setPoints(Integer.parseInt(token2));
					} else if (token.equals("slayer-task-amount")) {
						p.getSlayer().setTaskAmount(Integer.parseInt(token2));
					} else if (token.equals("consecutive-tasks")) {
						p.getSlayer().setConsecutiveTasks(Integer.parseInt(token2));
					} else if (token.equals("mage-arena-points")) {
						p.setArenaPoints(Integer.parseInt(token2));
					} else if (token.equals("shayzien-assault-points")) {
						p.setShayPoints(Integer.parseInt(token2));
					} else if (token.equals("autoRet")) {
						p.autoRet = Integer.parseInt(token2);
					} else if (token.equals("flagged")) {
						p.accountFlagged = Boolean.parseBoolean(token2);
					} else if (token.equals("keepTitle")) {
						p.keepTitle = Boolean.parseBoolean(token2);
					} else if (token.equals("killTitle")) {
						p.killTitle = Boolean.parseBoolean(token2);
					} else if (token.equals("character-historyItems")) {
						for (int j = 0; j < token3.length; j++) {
							p.historyItems[j] = Integer.parseInt(token3[j]);
							p.saleItems.add(Integer.parseInt(token3[j]));
						}
					} else if (token.equals("character-historyItemsN")) {
						for (int j = 0; j < token3.length; j++) {
							p.historyItemsN[j] = Integer.parseInt(token3[j]);		
							p.saleAmount.add(Integer.parseInt(token3[j]));
						}
					} else if (token.equals("character-historyPrice")) {
						for (int j = 0; j < token3.length; j++) {
							p.historyPrice[j] = Integer.parseInt(token3[j]);		
							p.salePrice.add(Integer.parseInt(token3[j]));
						}
					} else if (token.equals("removed-slayer-tasks")) {
						if (token3.length < 4) {
							String[] backing = Misc.nullToEmpty(4);
							int index = 0;
							for (; index < token3.length; index++) {
								backing[index] = token3[index];
							}
							p.getSlayer().setRemoved(backing);
						} else if (token3.length == 4) {
							p.getSlayer().setRemoved(token3);
						}
					} else if (token.startsWith("removedTask")) {
						int value = Integer.parseInt(token2);
						if (value > -1) {
							p.getSlayer().setPoints(p.getSlayer().getPoints() + 100);
						}
					} else if (token.equals("wave")) {
						p.waveId = Integer.parseInt(token2);
					} else if (token.equals("void")) {
						for (int j = 0; j < token3.length; j++) {
							p.voidStatus[j] = Integer.parseInt(token3[j]);
						}
					} else if (token.equals("pouch-rune")) {
						for (int j = 0; j < token3.length; j++) {
							p.setRuneEssencePouch(j, Integer.parseInt(token3[j]));
						}
					} else if (token.equals("pouch-pure")) {
						for (int j = 0; j < token3.length; j++) {
							p.setPureEssencePouch(j, Integer.parseInt(token3[j]));
						}
					} else if (token.equals("gwkc")) {
						p.killCount = Integer.parseInt(token2);
					} else if (token.equals("fightMode")) {
						p.fightMode = Integer.parseInt(token2);
					} else if (token.equals("privatechat")) {
						p.setPrivateChat(Integer.parseInt(token2));
					} else if (token.equals("farming-poison-berry")) {
						p.getFarming().setLastBerryFarm(Long.parseLong(token2));
					}  else if (token.equals("herb-patch 0")) { //Tried removing the numbers and adding a loop (didn't work?)
							p.setFarmingState(0, Integer.parseInt(token3[0]));
							p.setFarmingSeedId(0, Integer.parseInt(token3[1]));
							p.setFarmingTime(0, Integer.parseInt(token3[2]));
							p.setOriginalFarmingTime(0, Integer.parseInt(token3[3]));
							p.setFarmingHarvest(0, Integer.parseInt(token3[4]));
					} else if (token.equals("herb-patch 1")) {
							p.setFarmingState(1, Integer.parseInt(token3[0]));
							p.setFarmingSeedId(1, Integer.parseInt(token3[1]));
							p.setFarmingTime(1, Integer.parseInt(token3[2]));
							p.setOriginalFarmingTime(1, Integer.parseInt(token3[3]));
							p.setFarmingHarvest(1, Integer.parseInt(token3[4]));
					} else if (token.equals("herb-patch 2")) {
							p.setFarmingState(2, Integer.parseInt(token3[0]));
							p.setFarmingSeedId(2, Integer.parseInt(token3[1]));
							p.setFarmingTime(2, Integer.parseInt(token3[2]));
							p.setOriginalFarmingTime(2, Integer.parseInt(token3[3]));
							p.setFarmingHarvest(2, Integer.parseInt(token3[4]));
					} else if (token.equals("herb-patch 3")) {
							p.setFarmingState(3, Integer.parseInt(token3[0]));
							p.setFarmingSeedId(3, Integer.parseInt(token3[1]));
							p.setFarmingTime(3, Integer.parseInt(token3[2]));
							p.setOriginalFarmingTime(3, Integer.parseInt(token3[3]));
							p.setFarmingHarvest(3, Integer.parseInt(token3[4]));
					} else if (token.equals("herb-patch 4")) {
							p.setFarmingState(4, Integer.parseInt(token3[0]));
							p.setFarmingSeedId(4, Integer.parseInt(token3[1]));
							p.setFarmingTime(4, Integer.parseInt(token3[2]));
							p.setOriginalFarmingTime(4, Integer.parseInt(token3[3]));
							p.setFarmingHarvest(4, Integer.parseInt(token3[4]));
					} else if (token.equals("herb-patch 5")) {
							p.setFarmingState(5, Integer.parseInt(token3[0]));
							p.setFarmingSeedId(5, Integer.parseInt(token3[1]));
							p.setFarmingTime(5, Integer.parseInt(token3[2]));
							p.setOriginalFarmingTime(5, Integer.parseInt(token3[3]));
							p.setFarmingHarvest(5, Integer.parseInt(token3[4]));
					} else if (token.equals("herb-patch 6")) {
							p.setFarmingState(6, Integer.parseInt(token3[0]));
							p.setFarmingSeedId(6, Integer.parseInt(token3[1]));
							p.setFarmingTime(6, Integer.parseInt(token3[2]));
							p.setOriginalFarmingTime(6, Integer.parseInt(token3[3]));
							p.setFarmingHarvest(6, Integer.parseInt(token3[4]));
					} else if (token.equals("herb-patch 7")) {
							p.setFarmingState(7, Integer.parseInt(token3[0]));
							p.setFarmingSeedId(7, Integer.parseInt(token3[1]));
							p.setFarmingTime(7, Integer.parseInt(token3[2]));
							p.setOriginalFarmingTime(7, Integer.parseInt(token3[3]));
							p.setFarmingHarvest(7, Integer.parseInt(token3[4]));
					} else if (token.equals("compostBin")) {
						p.compostBin = Integer.parseInt(token2);
                }else if (token.equals("halloweenOrderGiven")) {
						for (int i = 0; i < p.halloweenRiddleGiven.length; i++)
							p.halloweenRiddleGiven[i] = Integer.parseInt(token3[i]);
					} else if (token.equals("halloweenOrderChosen")) {
						for (int i = 0; i < p.halloweenRiddleChosen.length; i++)
							p.halloweenRiddleChosen[i] = Integer.parseInt(token3[i]);
					} else if (token.equals("halloweenOrderNumber")) {
						p.halloweenOrderNumber = Integer.parseInt(token2);
					} else if (token.equals("inDistrict")) {
						p.pkDistrict = Boolean.parseBoolean(token2);
					} else if (token.equals("safeBoxSlots")) {
						p.safeBoxSlots = Integer.parseInt(token2);
					} else if (token.equals("district-levels")) {
						for (int i = 0; i < p.playerStats.length; i++)
							p.playerStats[i] = Integer.parseInt(token3[i]);
					} else if (token.equals("lost-items")) {
						if (token3.length > 1) {
							for (int i = 0; i < token3.length; i += 2) {
								int itemId = Integer.parseInt(token3[i]);
								int itemAmount = Integer.parseInt(token3[i + 1]);
								p.getZulrahLostItems().add(new GameItem(itemId, itemAmount));
							}
						}
					} else if (token.equals("lost-items-cerberus")) {
						if (token3.length > 1) {
							for (int i = 0; i < token3.length; i += 2) {
								int itemId = Integer.parseInt(token3[i]);
								int itemAmount = Integer.parseInt(token3[i + 1]);
								p.getCerberusLostItems().add(new GameItem(itemId, itemAmount));
							}
						}
					} else if (token.equals("lost-items-skotizo")) {
						if (token3.length > 1) {
							for (int i = 0; i < token3.length; i += 2) {
								int itemId = Integer.parseInt(token3[i]);
								int itemAmount = Integer.parseInt(token3[i + 1]);
								p.getSkotizoLostItems().add(new GameItem(itemId, itemAmount));
							}
						}
					}
					break;
				case 3:
					if (token.equals("character-equip")) {
						p.playerEquipment[Integer.parseInt(token3[0])] = Integer.parseInt(token3[1]);
						p.playerEquipmentN[Integer.parseInt(token3[0])] = Integer.parseInt(token3[2]);
					}
					break;
				case 4:
					if (token.equals("character-look")) {
						p.playerAppearance[Integer.parseInt(token3[0])] = Integer.parseInt(token3[1]);
					}
					break;
				case 5:
					if (token.equals("character-skill")) {
						p.playerLevel[Integer.parseInt(token3[0])] = Integer.parseInt(token3[1]);
						p.playerXP[Integer.parseInt(token3[0])] = Integer.parseInt(token3[2]);
						if (token3.length > 3) {
							p.skillLock[Integer.parseInt(token3[0])] = Boolean.parseBoolean(token3[3]);
							p.prestigeLevel[Integer.parseInt(token3[0])] = Integer.parseInt(token3[4]);
						}
					}
					break;
				case 6:
					if (token.equals("character-item")) {
						p.playerItems[Integer.parseInt(token3[0])] = Integer.parseInt(token3[1]);
						p.playerItemsN[Integer.parseInt(token3[0])] = Integer.parseInt(token3[2]);
					}
					break;
				case 46:
					if (token.equals("bag-item")) {
						int id = Integer.parseInt(token3[1]);
						int amt = Integer.parseInt(token3[2]);
						p.getLootingBag().items.add(new LootingBagItem(id, amt));
					}
					break;
				case 52:
					if (token.equals("item")) {
						int itemId = Integer.parseInt(token3[0]);
						int value = Integer.parseInt(token3[1]);
						String date = token3[2];
						p.getRechargeItems().loadItem(itemId, value, date);
					}
					break;
				case 55:
					if (token.equals("pouch-item")) {
						int id = Integer.parseInt(token3[1]);
						int amt = Integer.parseInt(token3[2]);
						p.getRunePouch().items.add(new LootingBagItem(id, amt));
					}
					break;
				case 56:
					if (token.equals("sack-item")) {
						int id = Integer.parseInt(token3[1]);
						int amt = Integer.parseInt(token3[2]);
						p.getHerbSack().items.add(new LootingBagItem(id, amt));
					}
					break;
				case 57:
					if (token.equals("bag-item")) {
						int id = Integer.parseInt(token3[1]);
						int amt = Integer.parseInt(token3[2]);
						p.getGemBag().items.add(new LootingBagItem(id, amt));
					}
					break;
				case 58:
					if (token.equals("safebox-item")) {
						int id = Integer.parseInt(token3[1]);
						int amt = Integer.parseInt(token3[2]);
						p.getSafeBox().items.add(new LootingBagItem(id, amt));
					}
					break;
				case 7:
					if (token.equals("character-bank")) {
						p.bankItems[Integer.parseInt(token3[0])] = Integer.parseInt(token3[1]);
						p.bankItemsN[Integer.parseInt(token3[0])] = Integer.parseInt(token3[2]);
						p.getBank().getBankTab()[0].add(new BankItem(Integer.parseInt(token3[1]), Integer.parseInt(token3[2])));
					} else if (token.equals("bank-tab")) {
						int tabId = Integer.parseInt(token3[0]);
						int itemId = Integer.parseInt(token3[1]);
						int itemAmount = Integer.parseInt(token3[2]);
						p.getBank().getBankTab()[tabId].add(new BankItem(itemId, itemAmount));
					}
					break;
				case 8:
					if (token.equals("character-friend")) {
						p.getFriends().add(Long.parseLong(token3[0]));
					}
					break;

				case 9:
					if (token3.length < 2)
						continue;
					p.getAchievements().read(token, 0, Integer.parseInt(token3[0]), Boolean.parseBoolean(token3[1]));
					break;
				case 10:
					if (token3.length < 2)
						continue;
					p.getAchievements().read(token, 1, Integer.parseInt(token3[0]), Boolean.parseBoolean(token3[1]));
					break;
				case 11:
					if (token3.length < 2)
						continue;
					p.getAchievements().read(token, 2, Integer.parseInt(token3[0]), Boolean.parseBoolean(token3[1]));
					break;
				case 12:
					if (token.equals("character-ignore")) {
						p.getIgnores().add(Long.parseLong(token3[0]));
					}
					break;

				case 13:
					if (token.equals("stage")) {
						p.getHolidayStages().setStage(token3[0], Integer.parseInt(token3[1]));
					}
					break;

				case 14:
					if (token.equals("item")) {
						p.degradableItem[Integer.parseInt(token3[0])] = Integer.parseInt(token3[1]);
					} else if (token.equals("claim-state")) {
						for (int i = 0; i < token3.length; i++) {
							p.claimDegradableItem[i] = Boolean.parseBoolean(token3[i]);
						}
					}
					break;

				case 15:
					if (token.startsWith("Names") && token3.length > 0) {
						for (int i = 0; i < token3.length; i++) {
							if (token3[i].equalsIgnoreCase("null")) {
								token3[i] = "New slot";
							}
							Preset preset = p.getPresets().getPresets().get(i);
							preset.setAlias(token3[i]);
						}
					} else if (token.startsWith("Inventory") || token.startsWith("Equipment")) {
						if (token3.length > 2) {
							int presetId = Integer.parseInt(token.split("#")[1]);
							for (int i = 0; i < token3.length; i += 3) {
								int slot = Integer.parseInt(token3[i]);
								int itemId = Integer.parseInt(token3[i + 1]);
								int amount = Integer.parseInt(token3[i + 2]);
								if (token.startsWith("Inventory")) {
									p.getPresets().getPresets().get(presetId).getInventory().getItems().put(slot, new GameItem(itemId, amount));
								} else {
									p.getPresets().getPresets().get(presetId).getEquipment().getItems().put(slot, new GameItem(itemId, amount));
								}
							}
						}
					}
					break;

				case 16:
					try {
						Killstreak.Type type = Killstreak.Type.get(token);
						int value = Integer.parseInt(token2);
						p.getKillstreak().getKillstreaks().put(type, value);
					} catch (NullPointerException | NumberFormatException e) {
						e.printStackTrace();
					}
					break;

				case 17:
					try {
						Title title = Title.valueOf(token2);
						if (title != null) {
							p.getTitles().getPurchasedList().add(title);
						}
					} catch (Exception e) {
						if (Config.SERVER_STATE == ServerState.PRIVATE) {
							e.printStackTrace();
						}
					}
					break;

				case 18:
					if (token != null && token.length() > 0) {
						p.getNpcDeathTracker().getTracker().put(token, Integer.parseInt(token2));
					}
					break;
				}
			} else {
				if (line.equals("[ACCOUNT]")) {
					ReadMode = 1;
				} else if (line.equals("[CHARACTER]")) {
					ReadMode = 2;
				} else if (line.equals("[EQUIPMENT]")) {
					ReadMode = 3;
				} else if (line.equals("[LOOK]")) {
					ReadMode = 4;
				} else if (line.equals("[SKILLS]")) {
					ReadMode = 5;
				} else if (line.equals("[ITEMS]")) {
					ReadMode = 6;
				} else if (line.equals("[LOOTBAG]")) {
					ReadMode = 46;
				} else if (line.equals("[RECHARGEITEMS]")) {
					ReadMode = 52;
				} else if (line.equals("[RUNEPOUCH]")) {
					ReadMode = 55;
				} else if (line.equals("[HERBSACK]")) {
					ReadMode = 56;
				} else if (line.equals("[GEMBAG]")) {
					ReadMode = 57;
				} else if (line.equals("[SAFEBOX]")) {
					ReadMode = 58;
				} else if (line.equals("[BANK]")) {
					ReadMode = 7;
				} else if (line.equals("[FRIENDS]")) {
					ReadMode = 8;
				} else if (line.equals("[ACHIEVEMENTS-TIER-1]")) {
					ReadMode = 9;
				} else if (line.equals("[ACHIEVEMENTS-TIER-2]")) {
					ReadMode = 10;
				} else if (line.equals("[ACHIEVEMENTS-TIER-3]")) {
					ReadMode = 11;
				} else if (line.equals("[IGNORES]")) {
					ReadMode = 12;
				} else if (line.equals("[HOLIDAY-EVENTS]")) {
					ReadMode = 13;
				} else if (line.equals("[DEGRADEABLES]")) {
					ReadMode = 14;
				} else if (line.equals("[PRESETS]")) {
					ReadMode = 15;
				} else if (line.equals("[KILLSTREAKS]")) {
					ReadMode = 16;
				} else if (line.equals("[TITLES]")) {
					ReadMode = 17;
				} else if (line.equals("[NPC-TRACKER]")) {
					ReadMode = 18;
				} else if (line.equals("[EOF]")) {
					try {
						characterfile.close();
					} catch (IOException ioexception) {
						if (Config.SERVER_STATE == ServerState.PRIVATE) {
							ioexception.printStackTrace();
						}
					}
					return 1;
				}
			}
			try {
				line = characterfile.readLine();
			} catch (IOException ioexception1) {
				EndOfFile = true;
				if (Config.SERVER_STATE == ServerState.PRIVATE) {
					ioexception1.printStackTrace();
				}
			}
		}
		try {
			characterfile.close();
		} catch (IOException ioexception) {
			if (Config.SERVER_STATE == ServerState.PRIVATE) {
				ioexception.printStackTrace();
			}
		}
		return 13;
	}

	public static void save(Player p) {
		saveGame(p);
	}

	/**
	 * Saving
	 **/
	//@SuppressWarnings("resource")
	public static boolean saveGame(Player p) {
		if (!p.saveFile || p.newPlayer || !p.saveCharacter) {
			// System.out.println("first");
			return false;
		}
		if (p.playerName == null || PlayerHandler.players[p.getIndex()] == null) {
			// System.out.println("second");
			return false;
		}
		p.playerName = p.playerName2;
		int tbTime = (int) (p.teleBlockDelay - System.currentTimeMillis() + p.teleBlockLength);
		if (tbTime > 300000 || tbTime < 0) {
			tbTime = 0;
		}

		BufferedWriter characterfile = null;
		try {
			characterfile = new BufferedWriter(new FileWriter(Config.CHARACTER_SAVE_DIRECTORY + p.playerName + ".txt"));

			/* ACCOUNT */
			characterfile.write("[ACCOUNT]", 0, 9);
			characterfile.newLine();
			characterfile.write("character-username = ", 0, 21);
			characterfile.write(p.playerName, 0, p.playerName.length());
			characterfile.newLine();
			characterfile.write("character-password = ", 0, 21);
			String passToWrite = Misc.md5Hash(p.playerPass);
			characterfile.write(passToWrite, 0, passToWrite.length());
			characterfile.newLine();
			characterfile.newLine();

			/* CHARACTER */
			characterfile.write("[CHARACTER]", 0, 11);
			characterfile.newLine();
			characterfile.write("character-rights = " + p.getRights().getPrimary().getValue());
			characterfile.newLine();
			StringBuilder sb = new StringBuilder();
			p.getRights().getSet().stream().forEach(r -> sb.append(r.getValue() + "\t"));
			characterfile.write("character-rights-secondary = " + sb.substring(0, sb.length() - 1));
			characterfile.newLine();
			characterfile.write("character-mac-address = " + p.getMacAddress());
			characterfile.newLine();
			characterfile.write("character-ip-address = " + p.getIpAddress());
			characterfile.newLine();
			characterfile.write("revert-option = " + p.getRevertOption());
			characterfile.newLine();
			if (p.getRevertModeDelay() > 0) {
				characterfile.write("revert-delay = " + p.getRevertModeDelay());
				characterfile.newLine();
			}
			if (p.getMode() != null) {
				characterfile.write("mode = " + p.getMode().getType().name());
				characterfile.newLine();
			}
			if (p.getTutorial().getStage() != null) {
				characterfile.write("tutorial-stage = " + p.getTutorial().getStage().name());
				characterfile.newLine();
			}
			characterfile.write("character-height = ", 0, 19);
			characterfile.write(Integer.toString(p.heightLevel), 0, Integer.toString(p.heightLevel).length());
			characterfile.newLine();
			characterfile.write("character-hp = " + p.getHealth().getAmount());
			characterfile.newLine();
			characterfile.write("play-time = ", 0, 12);
			characterfile.write(Integer.toString(p.playTime), 0, Integer.toString(p.playTime).length());
			characterfile.newLine();
			characterfile.write("last-clan = ", 0, 12);
			characterfile.write(p.getLastClanChat(), 0, p.getLastClanChat().length());
			characterfile.newLine();
			characterfile.write("character-specRestore = ", 0, 24);
			characterfile.write(Integer.toString(p.specRestore), 0, Integer.toString(p.specRestore).length());
			characterfile.newLine();
			characterfile.write("character-posx = ", 0, 17);
			characterfile.write(Integer.toString(p.absX), 0, Integer.toString(p.absX).length());
			characterfile.newLine();
			characterfile.write("character-posy = ", 0, 17);
			characterfile.write(Integer.toString(p.absY), 0, Integer.toString(p.absY).length());
			characterfile.newLine();
			characterfile.write("bank-pin = " + p.getBankPin().getPin());
			characterfile.newLine();
			characterfile.write("bank-pin-cancellation = " + p.getBankPin().isAppendingCancellation());
			characterfile.newLine();
			characterfile.write("bank-pin-unlock-delay = " + p.getBankPin().getUnlockDelay());
			characterfile.newLine();
			characterfile.write("placeholders = " + p.placeHolders);
			characterfile.newLine();
			characterfile.write("newStarter = " + p.newStarter);
			characterfile.newLine();
			characterfile.write("bank-pin-cancellation-delay = " + p.getBankPin().getCancellationDelay());
			characterfile.newLine();
			characterfile.write("dailyTaskDate = " + p.dailyTaskDate);
			characterfile.newLine();
			characterfile.write("totalDailyDone = " + p.totalDailyDone);
			characterfile.newLine();
			characterfile.write("currentTask = ", 0, 14);
			if(p.currentTask != null)
                characterfile.write(p.currentTask.name(), 0, p.currentTask.name().length());
			characterfile.newLine();
			characterfile.write("completedDailyTask = " + p.completedDailyTask);
			characterfile.newLine();
			characterfile.write("playerChoice = ", 0, 15);
			if(p.playerChoice != null)
                characterfile.write(p.playerChoice.name(), 0, p.playerChoice.name().length());
			characterfile.newLine();
			characterfile.write("dailyEnabled = " + p.dailyEnabled);
			characterfile.newLine();
			characterfile.write("show-drop-warning = " + p.showDropWarning());
			characterfile.newLine();
			characterfile.write("hourly-box-toggle = " + p.getHourlyBoxToggle());
			characterfile.newLine();
			characterfile.write("fractured-crystal-toggle = " + p.getFracturedCrystalToggle());
			characterfile.newLine();
			characterfile.write("accept-aid = " + p.acceptAid);
			characterfile.newLine();
			characterfile.write("did-you-know = " + p.didYouKnow);
			characterfile.newLine();
			characterfile.write("lootvalue = " + p.lootValue);
			characterfile.newLine();
			characterfile.write("raidPoints = " + p.getRaidPoints());
			characterfile.newLine();
			characterfile.write("maRound = " + p.maRound);
			characterfile.newLine();
			characterfile.write("raidCount = " + p.raidCount);
			characterfile.newLine();
			characterfile.write("experience-counter = " + p.getExperienceCounter());
			characterfile.newLine();
			characterfile.write("character-title-updated = " + p.getTitles().getCurrentTitle());
			characterfile.newLine();
			/*for (int i = 0; i < p.lastConnectedFrom.size(); i++) {
				characterfile.write("connected-from = ", 0, 17);
				characterfile.write(p.lastConnectedFrom.get(i), 0, p.lastConnectedFrom.get(i).length());
				characterfile.newLine();
			}*/
			for (int i = 0; i < p.getPlayerKills().getList().size(); i++) {
				characterfile.write("killed-players = " + p.getPlayerKills().getList().get(i));
				characterfile.newLine();
			}
			String[] removed = p.getSlayer().getRemoved();
			characterfile.write("removed-slayer-tasks = ");
			for (int index = 0; index < removed.length; index++) {
				characterfile.write(removed[index]);
				if (index < removed.length - 1) {
					characterfile.write("\t");
				}
			}
			characterfile.newLine();
			characterfile.write("last-incentive = " + p.getLastIncentive());
			characterfile.newLine();
			characterfile.write("rfd-round = ", 0, 12);
			characterfile.write(Integer.toString(p.rfdRound), 0, Integer.toString(p.rfdRound).length());
			characterfile.newLine();
			characterfile.write("run-energy = " + p.getRunEnergy());
			characterfile.newLine();
			
			for(int i = 0; i < p.historyItems.length; i++) {
				if(p.saleItems.size() > 0)
					p.historyItems[i] = p.saleItems.get(i).intValue();
			}
			characterfile.write("character-historyItems = ", 0, 25);
			String toWrite = "";
			for(int i1 = 0; i1 < p.historyItems.length; i1++) {
				toWrite += p.historyItems[i1] +"\t";
			}
			characterfile.write(toWrite);
			characterfile.newLine();
			for(int i = 0; i < p.historyItemsN.length; i++) {
				if(p.saleItems.size() > 0)
					p.historyItemsN[i] = p.saleAmount.get(i).intValue();
			}
			characterfile.write("character-historyItemsN = ", 0, 26);
			String toWrite2 = "";
			for(int i1 = 0; i1 < p.historyItemsN.length; i1++) {
				toWrite2 += p.historyItemsN[i1] +"\t";
			}
			characterfile.write(toWrite2);
			characterfile.newLine();

			for(int i = 0; i < p.historyPrice.length; i++) {
				if(p.salePrice.size() > 0)
					p.historyPrice[i] = p.salePrice.get(i).intValue();
			}
			characterfile.write("character-historyPrice = ", 0, 25);
			String toWrite3 = "";
			for(int i1 = 0; i1 < p.historyPrice.length; i1++) {
				toWrite3 += p.historyPrice[i1] +"\t";
			}
			characterfile.write(toWrite3);
			characterfile.newLine();
			
			characterfile.write("lastLoginDate = ", 0, 16);
			characterfile.write(Integer.toString(p.lastLoginDate), 0, Integer.toString(p.lastLoginDate).length());
			characterfile.newLine();
			characterfile.write("has-npc = ", 0, 10);
			characterfile.write(Boolean.toString(p.hasFollower), 0, Boolean.toString(p.hasFollower).length());
			characterfile.newLine();
			characterfile.write("summonId = ", 0, 11);
			characterfile.write(Integer.toString(p.summonId), 0, Integer.toString(p.summonId).length());
			characterfile.newLine();
			characterfile.write("startPack = ", 0, 12);
			characterfile.write(Boolean.toString(p.startPack), 0, Boolean.toString(p.startPack).length());
			characterfile.newLine();
			characterfile.write("crystalDrop = ", 0, 14);
			characterfile.write(Boolean.toString(p.crystalDrop), 0, Boolean.toString(p.crystalDrop).length());
			characterfile.newLine();
			characterfile.write("setPin = ", 0, 9);
			characterfile.write(Boolean.toString(p.setPin), 0, Boolean.toString(p.setPin).length());
			characterfile.newLine();
			characterfile.write("slayer-helmet = " + p.getSlayer().isHelmetCreatable());
			characterfile.newLine();
			characterfile.write("slayer-imbued-helmet = " + p.getSlayer().isHelmetImbuedCreatable());
			characterfile.newLine();
			characterfile.write("bigger-boss-tasks = " + p.getSlayer().isBiggerBossTasks());
			characterfile.newLine();
			characterfile.write("cerberus-route = " + p.getSlayer().isCerberusRoute());
			characterfile.newLine();
			characterfile.write("superior-slayer = " + p.getSlayer().isBiggerAndBadder());
			characterfile.newLine();
			characterfile.write("slayer-tasks-completed = " + p.slayerTasksCompleted);
			characterfile.newLine();
			characterfile.write("claimedReward = ", 0, 16);
			characterfile.write(Boolean.toString(p.claimedReward), 0, Boolean.toString(p.claimedReward).length());
			characterfile.newLine();
			characterfile.write("dragonfire-shield-charge = " + p.getDragonfireShieldCharge());
			characterfile.newLine();
			characterfile.write("rfd-gloves = " + p.rfdGloves);
			characterfile.newLine();
			characterfile.write("wave-id = " + p.waveId);
			characterfile.newLine();
			characterfile.write("wave-type = " + p.waveType);
			characterfile.newLine();
			characterfile.write("wave-info = " + p.waveInfo[0] + "\t" + p.waveInfo[1] + "\t" + p.waveInfo[2]);
			characterfile.newLine();
			characterfile.write("master-clue-reqs = " + p.masterClueRequirement[0] + "\t" + p.masterClueRequirement[1] + "\t" + p.masterClueRequirement[2] + "\t" + p.masterClueRequirement[3]);
			characterfile.newLine();
			characterfile.write("counters = ");
			for (int i = 0; i < p.counters.length; i++)
				characterfile.write("" + p.counters[i] + ((i == p.counters.length - 1) ? "" : "\t"));
			characterfile.newLine();
			characterfile.write("max-cape = ");
			for (int i = 0; i < p.maxCape.length; i++)
				characterfile.write("" + p.maxCape[i] + ((i == p.maxCape.length - 1) ? "" : "\t"));
			characterfile.newLine();
			characterfile.write("zulrah-best-time = " + p.getBestZulrahTime());
			characterfile.newLine();
			characterfile.write("toxic-staff = " + p.getToxicStaffOfTheDeadCharge());
			characterfile.newLine();
			characterfile.write("toxic-pipe-ammo = " + p.getToxicBlowpipeAmmo());
			characterfile.newLine();
			characterfile.write("toxic-pipe-amount = " + p.getToxicBlowpipeAmmoAmount());
			characterfile.newLine();
			characterfile.write("toxic-pipe-charge = " + p.getToxicBlowpipeCharge());
			characterfile.newLine();
			characterfile.write("serpentine-helm = " + p.getSerpentineHelmCharge());
			characterfile.newLine();
			characterfile.write("trident-of-the-seas = " + p.getTridentCharge());
			characterfile.newLine();
			characterfile.write("trident-of-the-swamp = " + p.getToxicTridentCharge());
			characterfile.newLine();
			characterfile.write("arclight-charge = " + p.getArcLightCharge());
			characterfile.newLine();
			characterfile.write("slayerPoints = " + p.getSlayer().getPoints());
			characterfile.newLine();
			characterfile.write("crystal-bow-shots = ", 0, 20);
			characterfile.write(Integer.toString(p.crystalBowArrowCount), 0, Integer.toString(p.crystalBowArrowCount).length());
			characterfile.newLine();
			characterfile.write("skull-timer = ", 0, 14);
			characterfile.write(Integer.toString(p.skullTimer), 0, Integer.toString(p.skullTimer).length());
			characterfile.newLine();
			characterfile.write("magic-book = ", 0, 13);
			characterfile.write(Integer.toString(p.playerMagicBook), 0, Integer.toString(p.playerMagicBook).length());
			characterfile.newLine();
			for (Brother brother : p.getBarrows().getBrothers()) {
				characterfile.write(brother.getName().toLowerCase() + " = " + Boolean.toString(brother.isDefeated()));
				characterfile.newLine();
			}
			if (p.getBarrows().getLastBrother().isPresent()) {
				characterfile.write("barrows-final-brother = " + p.getBarrows().getLastBrother().get().getName());
				characterfile.newLine();
			}
			characterfile.write("barrows-monsters-killcount = " + p.getBarrows().getMonsterKillCount());
			characterfile.newLine();
			characterfile.write("barrows-completed = " + Boolean.toString(p.getBarrows().isCompleted()));
			characterfile.newLine();
			characterfile.write("special-amount = ", 0, 17);
			characterfile.write(Double.toString(p.specAmount), 0, Double.toString(p.specAmount).length());
			characterfile.newLine();
			characterfile.write("prayer-amount = ", 0, 16);
			characterfile.write(Double.toString(p.prayerPoint), 0, Double.toString(p.prayerPoint).length());
			characterfile.newLine();
			characterfile.write("KC = ", 0, 4);
			characterfile.write(Integer.toString(p.killcount), 0, Integer.toString(p.killcount).length());
			characterfile.newLine();
			characterfile.write("DC = ", 0, 4);
			characterfile.write(Integer.toString(p.deathcount), 0, Integer.toString(p.deathcount).length());
			characterfile.newLine();
			characterfile.write("total-hunter-kills = " + p.getBH().getTotalHunterKills());
			characterfile.newLine();
			characterfile.write("total-rogue-kills = " + p.getBH().getTotalRogueKills());
			characterfile.newLine();
			characterfile.write("target-time-delay = " + p.getBH().getDelayedTargetTicks());
			characterfile.newLine();
			characterfile.write("bh-penalties = " + p.getBH().getWarnings());
			characterfile.newLine();
			characterfile.write("bh-bounties = " + p.getBH().getBounties());
			characterfile.newLine();
			characterfile.write("statistics-visible = " + p.getBH().isStatisticsVisible());
			characterfile.newLine();
			characterfile.write("spell-accessible = " + p.getBH().isSpellAccessible());
			characterfile.newLine();
			characterfile.write("zerkAmount = ", 0, 13);
			characterfile.newLine();
			characterfile.write("pkp = ", 0, 6);
			characterfile.write(Integer.toString(p.pkp), 0, Integer.toString(p.pkp).length());
			characterfile.newLine();
			characterfile.write("donP = ", 0, 6);
			characterfile.write(Integer.toString(p.donatorPoints), 0, Integer.toString(p.donatorPoints).length());
			characterfile.newLine();
			characterfile.write("donA = ", 0, 6);
			characterfile.write(Integer.toString(p.amDonated), 0, Integer.toString(p.amDonated).length());
			characterfile.newLine();
			characterfile.write("prestige-points = ", 0, 18);
			characterfile.write(Integer.toString(p.prestigePoints), 0, Integer.toString(p.prestigePoints).length());
			characterfile.newLine();
			characterfile.write("votePoints = ", 0, 13);
			characterfile.write(Integer.toString(p.votePoints), 0, Integer.toString(p.votePoints).length());
			characterfile.newLine();
			characterfile.write("bloodPoints = ", 0, 14);
			characterfile.write(Integer.toString(p.bloodPoints), 0, Integer.toString(p.bloodPoints).length());
			characterfile.newLine();
			characterfile.write("achievement-points = " + p.getAchievements().getPoints());
			characterfile.newLine();
			characterfile.write("achievement-items = ");
			for (int i = 0; i < p.getAchievements().getBoughtItems().length; i++)
				characterfile.write("" + p.getAchievements().getBoughtItems()[i][1] + ((i == p.getAchievements().getBoughtItems().length - 1) ? "" : "\t"));
			characterfile.newLine();
			characterfile.write("xpLock = ", 0, 9);
			characterfile.write(Boolean.toString(p.expLock), 0, Boolean.toString(p.expLock).length());
			characterfile.newLine();
			characterfile.write("teleblock-length = ", 0, 19);
			characterfile.write(Integer.toString(tbTime), 0, Integer.toString(tbTime).length());
			characterfile.newLine();
			
			//Varrock
	        String varrockClaimed = "VarrockClaimedDiaries = ";
	        characterfile.write(varrockClaimed, 0, varrockClaimed.length());
	        {
	            String prefix = "";
	        StringBuilder bldr = new StringBuilder();
	            for (EntryDifficulty entry : p.getDiaryManager().getVarrockDiary().getClaimed()) {
	                bldr.append(prefix);
	                prefix = ",";
	                bldr.append(entry.name());
	            }
	        characterfile.write(bldr.toString(), 0, bldr.toString().length());
	        }
	        characterfile.newLine();
	        //Ardougne
	        String ardougneClaimed = "ArdougneClaimedDiaries = ";
	        characterfile.write(ardougneClaimed, 0, ardougneClaimed.length());
	        {
	            String prefix = "";
	        StringBuilder bldr = new StringBuilder();
	            for (EntryDifficulty entry : p.getDiaryManager().getArdougneDiary().getClaimed()) {
	                bldr.append(prefix);
	                prefix = ",";
	                bldr.append(entry.name());
	            }
	        characterfile.write(bldr.toString(), 0, bldr.toString().length());
	        }
	        characterfile.newLine();
	        //Desert
	        String desertClaimed = "DesertClaimedDiaries = ";
	        characterfile.write(desertClaimed, 0, desertClaimed.length());
	        {
	            String prefix = "";
	        StringBuilder bldr = new StringBuilder();
	            for (EntryDifficulty entry : p.getDiaryManager().getDesertDiary().getClaimed()) {
	                bldr.append(prefix);
	                prefix = ",";
	                bldr.append(entry.name());
	            }
	        characterfile.write(bldr.toString(), 0, bldr.toString().length());
	        }
	        characterfile.newLine();
	        //Falador
	        String faladorClaimed = "FaladorClaimedDiaries = ";
	        characterfile.write(faladorClaimed, 0, faladorClaimed.length());
	        {
	            String prefix = "";
	        StringBuilder bldr = new StringBuilder();
	            for (EntryDifficulty entry : p.getDiaryManager().getFaladorDiary().getClaimed()) {
	                bldr.append(prefix);
	                prefix = ",";
	                bldr.append(entry.name());
	            }
	        characterfile.write(bldr.toString(), 0, bldr.toString().length());
	        }
	        characterfile.newLine();
	        //Fremennik
	        String fremennikClaimed = "FremennikClaimedDiaries = ";
	        characterfile.write(fremennikClaimed, 0, fremennikClaimed.length());
	        {
	            String prefix = "";
	        StringBuilder bldr = new StringBuilder();
	            for (EntryDifficulty entry : p.getDiaryManager().getFremennikDiary().getClaimed()) {
	                bldr.append(prefix);
	                prefix = ",";
	                bldr.append(entry.name());
	            }
	        characterfile.write(bldr.toString(), 0, bldr.toString().length());
	        }
	        characterfile.newLine();
	        //Kandarin
	        String kandarinClaimed = "KandarinClaimedDiaries = ";
	        characterfile.write(kandarinClaimed, 0, kandarinClaimed.length());
	        {
	            String prefix = "";
	        StringBuilder bldr = new StringBuilder();
	            for (EntryDifficulty entry : p.getDiaryManager().getKandarinDiary().getClaimed()) {
	                bldr.append(prefix);
	                prefix = ",";
	                bldr.append(entry.name());
	            }
	        characterfile.write(bldr.toString(), 0, bldr.toString().length());
	        }
	        characterfile.newLine();
	        //Karamja
	        String karamjaClaimed = "KaramjaClaimedDiaries = ";
	        characterfile.write(karamjaClaimed, 0, karamjaClaimed.length());
	        {
	            String prefix = "";
	        StringBuilder bldr = new StringBuilder();
	            for (EntryDifficulty entry : p.getDiaryManager().getKaramjaDiary().getClaimed()) {
	                bldr.append(prefix);
	                prefix = ",";
	                bldr.append(entry.name());
	            }
	        characterfile.write(bldr.toString(), 0, bldr.toString().length());
	        }
	        characterfile.newLine();
	        //Lumbridge
	        String lumbridgeClaimed = "LumbridgeClaimedDiaries = ";
	        characterfile.write(lumbridgeClaimed, 0, lumbridgeClaimed.length());
	        {
	            String prefix = "";
	        StringBuilder bldr = new StringBuilder();
	            for (EntryDifficulty entry : p.getDiaryManager().getLumbridgeDraynorDiary().getClaimed()) {
	                bldr.append(prefix);
	                prefix = ",";
	                bldr.append(entry.name());
	            }
	        characterfile.write(bldr.toString(), 0, bldr.toString().length());
	        }
	        characterfile.newLine();
	        //Morytania
	        String morytaniaClaimed = "MorytaniaClaimedDiaries = ";
	        characterfile.write(morytaniaClaimed, 0, morytaniaClaimed.length());
	        {
	            String prefix = "";
	        StringBuilder bldr = new StringBuilder();
	            for (EntryDifficulty entry : p.getDiaryManager().getMorytaniaDiary().getClaimed()) {
	                bldr.append(prefix);
	                prefix = ",";
	                bldr.append(entry.name());
	            }
	        characterfile.write(bldr.toString(), 0, bldr.toString().length());
	        }
	        characterfile.newLine();
	        //Western
	        String westernClaimed = "WesternClaimedDiaries = ";
	        characterfile.write(westernClaimed, 0, westernClaimed.length());
	        {
	            String prefix = "";
	        StringBuilder bldr = new StringBuilder();
	            for (EntryDifficulty entry : p.getDiaryManager().getWesternDiary().getClaimed()) {
	                bldr.append(prefix);
	                prefix = ",";
	                bldr.append(entry.name());
	            }
	        characterfile.write(bldr.toString(), 0, bldr.toString().length());
	        }
	        characterfile.newLine();
	        //Wilderness
	        String wildernessClaimed = "WildernessClaimedDiaries = ";
	        characterfile.write(wildernessClaimed, 0, wildernessClaimed.length());
	        {
	            String prefix = "";
	        StringBuilder bldr = new StringBuilder();
	            for (EntryDifficulty entry : p.getDiaryManager().getWildernessDiary().getClaimed()) {
	                bldr.append(prefix);
	                prefix = ",";
	                bldr.append(entry.name());
	            }
	        characterfile.write(bldr.toString(), 0, bldr.toString().length());
	        }
	        characterfile.newLine();
			
			String diary = "diaries = ";
			characterfile.write(diary, 0, diary.length());
			{
				String prefix = "";
			StringBuilder bldr = new StringBuilder();
			
				// Varrock
				for (VarrockDiaryEntry entry : p.getDiaryManager().getVarrockDiary().getAchievements()) {
					bldr.append(prefix);
					prefix = ",";
					bldr.append(entry.name());
				}
				// Ardougne
				for (ArdougneDiaryEntry entry : p.getDiaryManager().getArdougneDiary().getAchievements()) {
					bldr.append(prefix);
					prefix = ",";
					bldr.append(entry.name());
				}
				// Desert
				for (DesertDiaryEntry entry : p.getDiaryManager().getDesertDiary().getAchievements()) {
					bldr.append(prefix);
					prefix = ",";
					bldr.append(entry.name());
				}
				// Falador
				for (FaladorDiaryEntry entry : p.getDiaryManager().getFaladorDiary().getAchievements()) {
					bldr.append(prefix);
					prefix = ",";
					bldr.append(entry.name());
				}
				// Fremennik
				for (FremennikDiaryEntry entry : p.getDiaryManager().getFremennikDiary().getAchievements()) {
					bldr.append(prefix);
					prefix = ",";
					bldr.append(entry.name());
				}
				// Kandarin
				for (KandarinDiaryEntry entry : p.getDiaryManager().getKandarinDiary().getAchievements()) {
					bldr.append(prefix);
					prefix = ",";
					bldr.append(entry.name());
				}
				// Karamja
				for (KaramjaDiaryEntry entry : p.getDiaryManager().getKaramjaDiary().getAchievements()) {
					bldr.append(prefix);
					prefix = ",";
					bldr.append(entry.name());
				}
				// Lumbridge
				for (LumbridgeDraynorDiaryEntry entry : p.getDiaryManager().getLumbridgeDraynorDiary()
						.getAchievements()) {
					bldr.append(prefix);
					prefix = ",";
					bldr.append(entry.name());
				}
				// Morytania
				for (MorytaniaDiaryEntry entry : p.getDiaryManager().getMorytaniaDiary().getAchievements()) {
					bldr.append(prefix);
					prefix = ",";
					bldr.append(entry.name());
				}
				// Western
				for (WesternDiaryEntry entry : p.getDiaryManager().getWesternDiary().getAchievements()) {
					bldr.append(prefix);
					prefix = ",";
					bldr.append(entry.name());
				}
				// Wilderness
				for (WildernessDiaryEntry entry : p.getDiaryManager().getWildernessDiary().getAchievements()) {
					bldr.append(prefix);
					prefix = ",";
					bldr.append(entry.name());
				}
			characterfile.write(bldr.toString(), 0, bldr.toString().length());
			}
			characterfile.newLine();
			
			String partialDiary = "partialDiaries = ";
			//forEachPartial
			characterfile.write(partialDiary, 0, partialDiary.length()); //Saw that earlier but forgot lol, ahh ty
			{
				StringBuilder bldr = new StringBuilder();
				String prefix = "";
				//Varrock
				for (Entry<VarrockDiaryEntry, Integer> keyval : p.getDiaryManager().getVarrockDiary().getPartialAchievements().entrySet()) {
					VarrockDiaryEntry entry = keyval.getKey();
					int stage = keyval.getValue();
					bldr.append(prefix);
					prefix = ",";
					 bldr.append(entry.name() + ":" + stage);
				}
				//Ardougne
				for (Entry<ArdougneDiaryEntry, Integer> keyval : p.getDiaryManager().getArdougneDiary().getPartialAchievements().entrySet()) {
					ArdougneDiaryEntry entry = keyval.getKey();
					int stage = keyval.getValue();
					bldr.append(prefix);
					prefix = ",";
					 bldr.append(entry.name() + ":" + stage);
				}
				//Desert
				for (Entry<DesertDiaryEntry, Integer> keyval : p.getDiaryManager().getDesertDiary().getPartialAchievements().entrySet()) {
					DesertDiaryEntry entry = keyval.getKey();
					int stage = keyval.getValue();
					bldr.append(prefix);
					prefix = ",";
					 bldr.append(entry.name() + ":" + stage);
				}
				//Falador
				for (Entry<FaladorDiaryEntry, Integer> keyval : p.getDiaryManager().getFaladorDiary().getPartialAchievements().entrySet()) {
					FaladorDiaryEntry entry = keyval.getKey();
					int stage = keyval.getValue();
					bldr.append(prefix);
					prefix = ",";
					 bldr.append(entry.name() + ":" + stage);
				}
				//Fremennik
				for (Entry<FremennikDiaryEntry, Integer> keyval : p.getDiaryManager().getFremennikDiary().getPartialAchievements().entrySet()) {
					FremennikDiaryEntry entry = keyval.getKey();
					int stage = keyval.getValue();
					bldr.append(prefix);
					prefix = ",";
					 bldr.append(entry.name() + ":" + stage);
				}
				//Kandarin
				for (Entry<KandarinDiaryEntry, Integer> keyval : p.getDiaryManager().getKandarinDiary().getPartialAchievements().entrySet()) {
					KandarinDiaryEntry entry = keyval.getKey();
					int stage = keyval.getValue();
					bldr.append(prefix);
					prefix = ",";
					 bldr.append(entry.name() + ":" + stage);
				}
				//Karamja
				for (Entry<KaramjaDiaryEntry, Integer> keyval : p.getDiaryManager().getKaramjaDiary().getPartialAchievements().entrySet()) {
					KaramjaDiaryEntry entry = keyval.getKey();
					int stage = keyval.getValue();
					bldr.append(prefix);
					prefix = ",";
					 bldr.append(entry.name() + ":" + stage);
				}
				//Lumbridge
				for (Entry<LumbridgeDraynorDiaryEntry, Integer> keyval : p.getDiaryManager().getLumbridgeDraynorDiary().getPartialAchievements().entrySet()) {
					LumbridgeDraynorDiaryEntry entry = keyval.getKey();
					int stage = keyval.getValue();
					bldr.append(prefix);
					prefix = ",";
					 bldr.append(entry.name() + ":" + stage);
				}
				//Morytania
				for (Entry<MorytaniaDiaryEntry, Integer> keyval : p.getDiaryManager().getMorytaniaDiary().getPartialAchievements().entrySet()) {
					MorytaniaDiaryEntry entry = keyval.getKey();
					int stage = keyval.getValue();
					bldr.append(prefix);
					prefix = ",";
					 bldr.append(entry.name() + ":" + stage);
				}
				//Western
				for (Entry<WesternDiaryEntry, Integer> keyval : p.getDiaryManager().getWesternDiary().getPartialAchievements().entrySet()) {
					WesternDiaryEntry entry = keyval.getKey();
					int stage = keyval.getValue();
					bldr.append(prefix);
					prefix = ",";
					 bldr.append(entry.name() + ":" + stage);
				}
				//Wilderness
				for (Entry<WildernessDiaryEntry, Integer> keyval : p.getDiaryManager().getWildernessDiary().getPartialAchievements().entrySet()) {
					WildernessDiaryEntry entry = keyval.getKey();
					int stage = keyval.getValue();
					bldr.append(prefix);
					prefix = ",";
					 bldr.append(entry.name() + ":" + stage);
				}
				characterfile.write(bldr.toString(), 0, bldr.toString().length());
			}
			
			characterfile.newLine();
			
			characterfile.write("pc-points = ", 0, 12);
			characterfile.write(Integer.toString(p.pcPoints), 0, Integer.toString(p.pcPoints).length());
			characterfile.newLine();
			characterfile.write("total-raids = ", 0, 14);
			characterfile.write(Integer.toString(p.totalRaidsFinished), 0, Integer.toString(p.totalRaidsFinished).length());
			characterfile.newLine();
			characterfile.write("killStreak = ", 0, 13);
			characterfile.write(Integer.toString(p.killStreak), 0, Integer.toString(p.killStreak).length());
			characterfile.newLine();
			characterfile.write("bonus-end = ", 0, 12);
			characterfile.write(Long.toString(p.bonusXpTime), 0, Long.toString(p.bonusXpTime).length());
			characterfile.newLine();
			characterfile.write("jail-end = ", 0, 11);
			characterfile.write(Long.toString(p.jailEnd), 0, Long.toString(p.jailEnd).length());
			characterfile.newLine();
			characterfile.write("mute-end = ", 0, 11);
			characterfile.write(Long.toString(p.muteEnd), 0, Long.toString(p.muteEnd).length());
			characterfile.newLine();
			characterfile.write("marketmute-end = ", 0, 17);
			characterfile.write(Long.toString(p.marketMuteEnd), 0, Long.toString(p.marketMuteEnd).length());
			characterfile.newLine();
			characterfile.write("last-yell = " + p.lastYell);
			characterfile.newLine();
			characterfile.write("splitChat = ", 0, 12);
			characterfile.write(Boolean.toString(p.splitChat), 0, Boolean.toString(p.splitChat).length());
			characterfile.newLine();
			if (p.getSlayer().getTask().isPresent()) {
				Task task = p.getSlayer().getTask().get();
				characterfile.write("slayer-task = " + task.getPrimaryName());
				characterfile.newLine();
				characterfile.write("slayer-task-amount = " + p.getSlayer().getTaskAmount());
				characterfile.newLine();
			}
			characterfile.write("slayer-master = " + p.getSlayer().getMaster());
			characterfile.newLine();
			characterfile.write("consecutive-tasks = " + p.getSlayer().getConsecutiveTasks());
			characterfile.newLine();
			characterfile.write("mage-arena-points = " + p.getArenaPoints());
			characterfile.newLine();
			characterfile.write("shayzien-assault-points = " + p.getShayPoints());
			characterfile.newLine();
			characterfile.write("autoRet = ", 0, 10);
			characterfile.write(Integer.toString(p.autoRet), 0, Integer.toString(p.autoRet).length());
			characterfile.newLine();
			characterfile.write("flagged = ", 0, 10);
			characterfile.write(Boolean.toString(p.accountFlagged), 0, Boolean.toString(p.accountFlagged).length());
			characterfile.newLine();
			characterfile.write("keepTitle = ", 0, 12);
			characterfile.write(Boolean.toString(p.keepTitle), 0, Boolean.toString(p.keepTitle).length());
			characterfile.newLine();
			characterfile.write("killTitle = ", 0, 12);
			characterfile.write(Boolean.toString(p.killTitle), 0, Boolean.toString(p.killTitle).length());
			characterfile.newLine();
			characterfile.write("wave = ", 0, 7);
			characterfile.write(Integer.toString(p.waveId), 0, Integer.toString(p.waveId).length());
			characterfile.newLine();
			characterfile.write("gwkc = ", 0, 7);
			characterfile.write(Integer.toString(p.killCount), 0, Integer.toString(p.killCount).length());
			characterfile.newLine();
			characterfile.write("fightMode = ", 0, 12);
			characterfile.write(Integer.toString(p.fightMode), 0, Integer.toString(p.fightMode).length());
			characterfile.newLine();
			characterfile.write("privatechat = ", 0, 14);
			characterfile.write(Integer.toString(p.getPrivateChat()), 0, Integer.toString(p.getPrivateChat()).length());
			characterfile.newLine();
			characterfile.write("void = ", 0, 7);
			String toWrite55 = p.voidStatus[0] + "\t" + p.voidStatus[1] + "\t" + p.voidStatus[2] + "\t" + p.voidStatus[3] + "\t" + p.voidStatus[4];
			characterfile.write(toWrite55);
			characterfile.newLine();
			characterfile.write("quickprayer = ", 0, 14);
			String quick = "";
			for(int i = 0; i < p.getQuick().getNormal().length; i++) {
				quick += p.getQuick().getNormal()[i]+"\t";
			}
			characterfile.write(quick);
			characterfile.newLine();
			characterfile.write("pouch-rune = " + p.getRuneEssencePouch(0) + "\t" + p.getRuneEssencePouch(1) + "\t" + p.getRuneEssencePouch(2));
			characterfile.newLine();
			characterfile.write("pouch-pure = " + p.getPureEssencePouch(0) + "\t" + p.getPureEssencePouch(1) + "\t" + p.getPureEssencePouch(2));
			characterfile.newLine();
			characterfile.write("crabsKilled = ", 0, 14);
			characterfile.write(Integer.toString(p.crabsKilled), 0, Integer.toString(p.crabsKilled).length());
			characterfile.newLine();
			characterfile.write("farming-poison-berry = " + p.getFarming().getLastBerryFarm());
			characterfile.newLine();
			for (int i = 0; i < Farming.MAX_PATCHES; i++) {
				characterfile.write("herb-patch "+i+" = "+p.getFarmingState(i)+"\t"+p.getFarmingSeedId(i)+"\t"+p.getFarmingTime(i)+"\t"+p.getOriginalFarmingTime(i)+"\t"+p.getFarmingHarvest(i));
				characterfile.newLine();
				}
			characterfile.write("compostBin = " + p.compostBin);
			characterfile.newLine();
			characterfile.write("halloweenOrderGiven = ");
			for (int i = 0; i < p.halloweenRiddleGiven.length; i++)
				characterfile.write("" + p.halloweenRiddleGiven[i] + ((i == p.halloweenRiddleGiven.length - 1) ? "" : "\t"));
			characterfile.newLine();
			characterfile.write("halloweenOrderChosen = ");
			for (int i = 0; i < p.halloweenRiddleChosen.length; i++)
				characterfile.write("" + p.halloweenRiddleChosen[i] + ((i == p.halloweenRiddleChosen.length - 1) ? "" : "\t"));
			characterfile.newLine();
			characterfile.write("halloweenOrderNumber = ", 0, 23);
			characterfile.write(Integer.toString(p.halloweenOrderNumber), 0, Integer.toString(p.halloweenOrderNumber).length());
			characterfile.newLine();
			characterfile.write("district-levels = ");
			for (int i = 0; i < p.playerStats.length; i++)
				characterfile.write("" + p.playerStats[i] + ((i == p.playerStats.length - 1) ? "" : "\t"));
			characterfile.newLine();
			characterfile.write("inDistrict = ", 0, 13);
			characterfile.write(Boolean.toString(p.pkDistrict), 0, Boolean.toString(p.pkDistrict).length());
			characterfile.newLine();
			characterfile.write("safeBoxSlots = ", 0, 15);
			characterfile.write(Integer.toString(p.safeBoxSlots), 0, Integer.toString(p.safeBoxSlots).length());
			characterfile.newLine();
			characterfile.write("lost-items = ");
			for (GameItem item : p.getZulrahLostItems()) {
				if (item == null) {
					continue;
				}
				characterfile.write(item.getId() + "\t" + item.getAmount() + "\t");
			}
			characterfile.newLine();
			characterfile.write("lost-items-cerberus = ");
			for (GameItem item : p.getCerberusLostItems()) {
				if (item == null) {
					continue;
				}
				characterfile.write(item.getId() + "\t" + item.getAmount() + "\t");
			}
			characterfile.newLine();
			characterfile.write("lost-items-skotizo = ");
			for (GameItem item : p.getSkotizoLostItems()) {
				if (item == null) {
					continue;
				}
				characterfile.write(item.getId() + "\t" + item.getAmount() + "\t");
			}
			characterfile.newLine();
			characterfile.newLine();

			/* EQUIPMENT */
			characterfile.write("[EQUIPMENT]", 0, 11);
			characterfile.newLine();
			for (int i = 0; i < p.playerEquipment.length; i++) {
				characterfile.write("character-equip = ", 0, 18);
				characterfile.write(Integer.toString(i), 0, Integer.toString(i).length());
				characterfile.write("	", 0, 1);
				characterfile.write(Integer.toString(p.playerEquipment[i]), 0, Integer.toString(p.playerEquipment[i]).length());
				characterfile.write("	", 0, 1);
				characterfile.write(Integer.toString(p.playerEquipmentN[i]), 0, Integer.toString(p.playerEquipmentN[i]).length());
				characterfile.write("	", 0, 1);
				characterfile.newLine();
			}
			characterfile.newLine();

			/* LOOK */
			characterfile.write("[LOOK]", 0, 6);
			characterfile.newLine();
			for (int i = 0; i < p.playerAppearance.length; i++) {
				characterfile.write("character-look = ", 0, 17);
				characterfile.write(Integer.toString(i), 0, Integer.toString(i).length());
				characterfile.write("	", 0, 1);
				characterfile.write(Integer.toString(p.playerAppearance[i]), 0, Integer.toString(p.playerAppearance[i]).length());
				characterfile.newLine();
			}
			characterfile.newLine();

			/* SKILLS */
			characterfile.write("[SKILLS]", 0, 8);
			characterfile.newLine();
			for (int i = 0; i < p.playerLevel.length; i++) {
				characterfile.write("character-skill = ", 0, 18);
				characterfile.write(Integer.toString(i), 0, Integer.toString(i).length());
				characterfile.write("	", 0, 1);
				characterfile.write(Integer.toString(p.playerLevel[i]), 0, Integer.toString(p.playerLevel[i]).length());
				characterfile.write("	", 0, 1);
				characterfile.write(Integer.toString(p.playerXP[i]), 0, Integer.toString(p.playerXP[i]).length());
				characterfile.write("	", 0, 1);
				characterfile.write(Boolean.toString(p.skillLock[i]), 0, Boolean.toString(p.skillLock[i]).length());
				characterfile.write("	", 0, 1);
				characterfile.write(Integer.toString(p.prestigeLevel[i]), 0, Integer.toString(p.prestigeLevel[i]).length());
				characterfile.newLine();
			}
			characterfile.newLine();

			/* ITEMS */
			characterfile.write("[ITEMS]", 0, 7);
			characterfile.newLine();
			for (int i = 0; i < p.playerItems.length; i++) {
				if (p.playerItems[i] > 0) {
					characterfile.write("character-item = ", 0, 17);
					characterfile.write(Integer.toString(i), 0, Integer.toString(i).length());
					characterfile.write("	", 0, 1);
					characterfile.write(Integer.toString(p.playerItems[i]), 0, Integer.toString(p.playerItems[i]).length());
					characterfile.write("	", 0, 1);
					characterfile.write(Integer.toString(p.playerItemsN[i]), 0, Integer.toString(p.playerItemsN[i]).length());
					characterfile.newLine();
				}
			}
			characterfile.newLine();
			
			/* ITEMVALUES */
			characterfile.write("[RECHARGEITEMS]", 0, 15);
			characterfile.newLine();
			for (int itemId : p.getRechargeItems().getItemValues().keySet()) {
				int value = p.getRechargeItems().getChargesLeft(itemId);
				
				String itemIdString = Integer.toString(itemId);
				String valueString = Integer.toString(value);
				String lastUsed = p.getRechargeItems().getItemLastUsed(itemId);
				
				characterfile.write("item = ", 0, 7);
				characterfile.write("	", 0, 1);
				characterfile.write(itemIdString, 0, itemIdString.length());
				characterfile.write("	", 0, 1);
				characterfile.write(valueString, 0, valueString.length());
				characterfile.write("	", 0, 1);
				characterfile.write(lastUsed, 0, lastUsed.length());
				characterfile.newLine();
			}
			characterfile.newLine();

			/* BANK */
			characterfile.write("[BANK]", 0, 6);
			characterfile.newLine();
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < Config.BANK_SIZE; j++) {
					if (j > p.getBank().getBankTab()[i].size() - 1)
						break;
					BankItem item = p.getBank().getBankTab()[i].getItem(j);
					if (item == null) {
						continue;
					}
					
					characterfile.write("bank-tab = " + i + "\t" + item.getId() + "\t" + item.getAmount());
					characterfile.newLine();
				}
			}

			characterfile.newLine();
			characterfile.newLine();
			
			/* LOOTBAG */
			characterfile.write("[LOOTBAG]", 0, 9);
			characterfile.newLine();
			for (int i = 0; i < p.getLootingBag().items.size(); i++) {
				if (p.getLootingBag().items.get(i).getId() > 0) {
					characterfile.write("bag-item = ", 0, 11);
					characterfile.write(Integer.toString(i), 0, Integer.toString(i).length());
					characterfile.write("	", 0, 1);
					int id = p.getLootingBag().items.get(i).getId();
					int amt = p.getLootingBag().items.get(i).getAmount();
					characterfile.write(Integer.toString(id), 0, Integer.toString(id).length());
					characterfile.write("	", 0, 1);
					characterfile.write(Integer.toString(amt), 0, Integer.toString(amt).length());
					characterfile.newLine();
				}
			}
			characterfile.newLine();

			/* RUNEPOUCH */
			characterfile.write("[RUNEPOUCH]", 0, 11);
			characterfile.newLine();
			for (int i = 0; i < p.getRunePouch().items.size(); i++) {
				if (p.getRunePouch().items.get(i).getId() > 0) {
					characterfile.write("pouch-item = ", 0, 13);
					characterfile.write(Integer.toString(i), 0, Integer.toString(i).length());
					characterfile.write("	", 0, 1);
					int id = p.getRunePouch().items.get(i).getId();
					int amt = p.getRunePouch().items.get(i).getAmount();
					characterfile.write(Integer.toString(id), 0, Integer.toString(id).length());
					characterfile.write("	", 0, 1);
					characterfile.write(Integer.toString(amt), 0, Integer.toString(amt).length());
					characterfile.newLine();
				}
			}
			characterfile.newLine();
			
			/* HERBSACK */
			characterfile.write("[HERBSACK]", 0, 10);
			characterfile.newLine();
			for (int i = 0; i < p.getHerbSack().items.size(); i++) {
				if (p.getHerbSack().items.get(i).getId() > 0) {
					characterfile.write("sack-item = ", 0, 12);
					characterfile.write(Integer.toString(i), 0, Integer.toString(i).length());
					characterfile.write("	", 0, 1);
					int id = p.getHerbSack().items.get(i).getId();
					int amt = p.getHerbSack().items.get(i).getAmount();
					characterfile.write(Integer.toString(id), 0, Integer.toString(id).length());
					characterfile.write("	", 0, 1);
					characterfile.write(Integer.toString(amt), 0, Integer.toString(amt).length());
					characterfile.newLine();
				}
			}
			characterfile.newLine();
			
			/* GEMBAG */
			characterfile.write("[GEMBAG]", 0, 8);
			characterfile.newLine();
			for (int i = 0; i < p.getGemBag().items.size(); i++) {
				if (p.getGemBag().items.get(i).getId() > 0) {
					characterfile.write("bag-item = ", 0, 11);
					characterfile.write(Integer.toString(i), 0, Integer.toString(i).length());
					characterfile.write("	", 0, 1);
					int id = p.getGemBag().items.get(i).getId();
					int amt = p.getGemBag().items.get(i).getAmount();
					characterfile.write(Integer.toString(id), 0, Integer.toString(id).length());
					characterfile.write("	", 0, 1);
					characterfile.write(Integer.toString(amt), 0, Integer.toString(amt).length());
					characterfile.newLine();
				}
			}
			characterfile.newLine();
			
			/* SAFEBOX */
			characterfile.write("[SAFEBOX]", 0, 9);
			characterfile.newLine();
			for (int i = 0; i < p.getSafeBox().items.size(); i++) {
				if (p.getSafeBox().items.get(i).getId() > 0) {
					characterfile.write("safebox-item = ", 0, 15);
					characterfile.write(Integer.toString(i), 0, Integer.toString(i).length());
					characterfile.write("	", 0, 1);
					int id = p.getSafeBox().items.get(i).getId();
					int amt = p.getSafeBox().items.get(i).getAmount();
					characterfile.write(Integer.toString(id), 0, Integer.toString(id).length());
					characterfile.write("	", 0, 1);
					characterfile.write(Integer.toString(amt), 0, Integer.toString(amt).length());
					characterfile.newLine();
				}
			}
			characterfile.newLine();
			
			/* FRIENDS */
			characterfile.write("[FRIENDS]", 0, 9);
			characterfile.newLine();
			for (Long friend : p.getFriends().getFriends()) {
				characterfile.write("character-friend = ", 0, 19);
				characterfile.write(Long.toString(friend), 0, Long.toString(friend).length());
				characterfile.newLine();
			}
			characterfile.newLine();

			characterfile.newLine();
			characterfile.write("[HOLIDAY-EVENTS]");
			characterfile.newLine();
			for (Entry<String, Integer> entry : p.getHolidayStages().getStages().entrySet()) {
				String key = entry.getKey();
				int value = entry.getValue();
				if (Objects.isNull(key)) {
					continue;
				}
				characterfile.write("stage = " + key + "\t" + value);
				characterfile.newLine();
			}
			characterfile.newLine();

			characterfile.write("[DEGRADEABLES]");
			characterfile.newLine();
			characterfile.write("claim-state = ");
			for (int i = 0; i < p.claimDegradableItem.length; i++) {
				characterfile.write(Boolean.toString(p.claimDegradableItem[i]));
				if (i != p.claimDegradableItem.length - 1) {
					characterfile.write("\t");
				}
			}
			characterfile.newLine();
			for (int i = 0; i < p.degradableItem.length; i++) {
				if (p.degradableItem[i] > 0) {
					characterfile.write("item = " + i + "\t" + p.degradableItem[i]);
					characterfile.newLine();
				}
			}
			characterfile.newLine();

			characterfile.newLine();
			characterfile.write("[ACHIEVEMENTS-TIER-1]");
			characterfile.newLine();
			p.getAchievements().print(characterfile, 0);
			characterfile.newLine();

			characterfile.newLine();
			characterfile.write("[ACHIEVEMENTS-TIER-2]");
			characterfile.newLine();
			p.getAchievements().print(characterfile, 1);
			characterfile.newLine();

			characterfile.newLine();
			characterfile.write("[ACHIEVEMENTS-TIER-3]");
			characterfile.newLine();
			p.getAchievements().print(characterfile, 2);
			characterfile.newLine();

			/* IGNORES */
			characterfile.write("[IGNORES]", 0, 9);
			characterfile.newLine();
			for (Long ignore : p.getIgnores().getIgnores()) {
				characterfile.write("character-ignore = ", 0, 19);
				characterfile.write(Long.toString(ignore), 0, Long.toString(ignore).length());
				characterfile.newLine();
			}
			characterfile.newLine();

			characterfile.write("[PRESETS]");
			characterfile.newLine();

			characterfile.write("Names = ");
			Map<Integer, Preset> presets = p.getPresets().getPresets();
			for (Entry<Integer, Preset> entry : presets.entrySet()) {
				characterfile.write(entry.getValue().getAlias() + "\t");
			}
			characterfile.newLine();
			for (Entry<Integer, Preset> entry : presets.entrySet()) {
				if (entry != null) {
					Preset preset = entry.getValue();
					PresetContainer inventory = preset.getInventory();
					characterfile.write("Inventory#" + entry.getKey() + " = ");
					for (Entry<Integer, GameItem> item : inventory.getItems().entrySet()) {
						characterfile.write(item.getKey() + "\t" + item.getValue().getId() + "\t" + item.getValue().getAmount() + "\t");
					}
					characterfile.newLine();
					PresetContainer equipment = preset.getEquipment();
					characterfile.write("Equipment#" + entry.getKey() + " = ");
					for (Entry<Integer, GameItem> item : equipment.getItems().entrySet()) {
						characterfile.write(item.getKey() + "\t" + item.getValue().getId() + "\t" + item.getValue().getAmount() + "\t");
					}
					characterfile.newLine();
				}
			}
			characterfile.newLine();

			characterfile.write("[KILLSTREAKS]");
			characterfile.newLine();
			for (Entry<Killstreak.Type, Integer> entry : p.getKillstreak().getKillstreaks().entrySet()) {
				characterfile.write(entry.getKey().name() + " = " + entry.getValue());
				characterfile.newLine();
			}
			characterfile.newLine();

			characterfile.write("[TITLES]");
			characterfile.newLine();
			for (Title title : p.getTitles().getPurchasedList()) {
				characterfile.write("title = " + title.name());
				characterfile.newLine();
			}
			characterfile.newLine();

			characterfile.write("[NPC-TRACKER]");
			characterfile.newLine();
			for (Entry<String, Integer> entry : p.getNpcDeathTracker().getTracker().entrySet()) {
				if (entry != null) {
					if (entry.getValue() > 0) {
						characterfile.write(entry.getKey().toString() + " = " + entry.getValue());
						characterfile.newLine();
					}
				}
			}
			characterfile.newLine();

			characterfile.write("[EOF]", 0, 5);
			characterfile.newLine();
			characterfile.newLine();
			characterfile.close();
		} catch (IOException ioexception) {
			Misc.println(p.playerName + ": error writing file.");
			if (Config.SERVER_STATE == ServerState.PRIVATE) {
				ioexception.printStackTrace();
			}
			return false;
		}
		return true;
	}

}