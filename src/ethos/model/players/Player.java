package ethos.model.players;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

import ethos.model.content.*;
import ethos.model.players.combat.monsterhunt.MonsterHunt;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;

import ethos.Config;
import ethos.PlayersOnline;
import ethos.Server;
import ethos.event.CycleEventHandler;
import ethos.event.Event;
import ethos.event.impl.IronmanRevertEvent;
import ethos.event.impl.MinigamePlayersEvent;
import ethos.event.impl.RunEnergyEvent;
import ethos.event.impl.SkillRestorationEvent;
import ethos.model.content.LootingBag.LootingBag;
import ethos.model.content.Tutorial.Stage;
import ethos.model.content.achievement.AchievementHandler;
import ethos.model.content.achievement.Achievements;
import ethos.model.content.achievement_diary.AchievementDiary;
import ethos.model.content.achievement_diary.AchievementDiaryManager;
import ethos.model.content.achievement_diary.RechargeItems;
import ethos.model.content.barrows.Barrows;
import ethos.model.content.barrows.TunnelEvent;
import ethos.model.content.dailytasks.DailyTasks;
import ethos.model.content.dailytasks.TaskTypes;
import ethos.model.content.dailytasks.DailyTasks.PossibleTasks;
import ethos.model.content.explock.ExpLock;
import ethos.model.content.godwars.God;
import ethos.model.content.godwars.Godwars;
import ethos.model.content.godwars.GodwarsEquipment;
import ethos.model.content.instances.InstancedAreaManager;
import ethos.model.content.kill_streaks.Killstreak;
import ethos.model.content.presets.Presets;
import ethos.model.content.prestige.PrestigeSkills;
import ethos.model.content.safebox.SafeBox;
import ethos.model.content.staff.PunishmentPanel;
import ethos.model.content.teleportation.TeleportHandler;
import ethos.model.content.teleportation.TeleportationInterface.TeleportData;
import ethos.model.content.teleportation.TeleportationInterface.TeleportType;
import ethos.model.content.titles.Titles;
import ethos.model.content.trails.RewardLevel;
import ethos.model.content.trails.TreasureTrails;
import ethos.model.entity.Entity;
import ethos.model.entity.HealthStatus;
import ethos.model.holiday.HolidayStages;
import ethos.model.holiday.christmas.ChristmasPresent;
import ethos.model.items.EquipmentSet;
import ethos.model.items.Item;
import ethos.model.items.ItemAssistant;
import ethos.model.items.ItemCombination;
import ethos.model.items.bank.Bank;
import ethos.model.items.bank.BankPin;
import ethos.model.minigames.bounty_hunter.BountyHunter;
import ethos.model.minigames.fight_cave.FightCave;
import ethos.model.minigames.inferno.Inferno;
import ethos.model.minigames.inferno.Tzkalzuk;
import ethos.model.minigames.lighthouse.DagannothMother;
import ethos.model.minigames.pest_control.PestControl;
import ethos.model.minigames.pest_control.PestControlRewards;
import ethos.model.minigames.pk_arena.Highpkarena;
import ethos.model.minigames.pk_arena.Lowpkarena;
import ethos.model.minigames.raids.Raids;
import ethos.model.minigames.rfd.DisposeTypes;
import ethos.model.minigames.rfd.RecipeForDisaster;
import ethos.model.minigames.warriors_guild.WarriorsGuild;
import ethos.model.miniquests.MageArena;
import ethos.model.multiplayer_session.MultiplayerSessionStage;
import ethos.model.multiplayer_session.MultiplayerSessionType;
import ethos.model.multiplayer_session.duel.Duel;
import ethos.model.multiplayer_session.duel.DuelSession;
import ethos.model.multiplayer_session.trade.Trade;
import ethos.model.npcs.NPC;
import ethos.model.npcs.NPCDeathTracker;
import ethos.model.npcs.NPCHandler;
import ethos.model.npcs.bosses.DemonicGorilla;
import ethos.model.npcs.bosses.KalphiteQueen;
import ethos.model.npcs.bosses.cerberus.Cerberus;
import ethos.model.npcs.bosses.cerberus.CerberusLostItems;
import ethos.model.npcs.bosses.skotizo.Skotizo;
import ethos.model.npcs.bosses.skotizo.SkotizoLostItems;
import ethos.model.npcs.bosses.vorkath.Vorkath;
import ethos.model.npcs.bosses.zulrah.Zulrah;
import ethos.model.npcs.bosses.zulrah.ZulrahLostItems;
import ethos.model.npcs.instance.InstanceSoloFight;
import ethos.model.npcs.pets.PetHandler;
import ethos.model.npcs.pets.PetHandler.Pets;
import ethos.model.players.combat.CombatAssistant;
import ethos.model.players.combat.CombatType;
import ethos.model.players.combat.DamageQueueEvent;
import ethos.model.players.combat.Degrade;
import ethos.model.players.combat.Hitmark;
import ethos.model.players.combat.magic.MagicData;
import ethos.model.players.combat.melee.QuickPrayers;
import ethos.model.players.mode.Mode;
import ethos.model.players.mode.ModeType;
import ethos.model.players.skills.Agility;
import ethos.model.players.skills.Cooking;
import ethos.model.players.skills.SkillInterfaces;
import ethos.model.players.skills.Skilling;
import ethos.model.players.skills.Smelting;
import ethos.model.players.skills.Smithing;
import ethos.model.players.skills.SmithingInterface;
import ethos.model.players.skills.agility.AgilityHandler;
import ethos.model.players.skills.agility.impl.BarbarianAgility;
import ethos.model.players.skills.agility.impl.GnomeAgility;
import ethos.model.players.skills.agility.impl.Lighthouse;
import ethos.model.players.skills.agility.impl.Shortcuts;
import ethos.model.players.skills.agility.impl.WildernessAgility;
import ethos.model.players.skills.agility.impl.rooftop.RooftopArdougne;
import ethos.model.players.skills.agility.impl.rooftop.RooftopFalador;
import ethos.model.players.skills.agility.impl.rooftop.RooftopSeers;
import ethos.model.players.skills.agility.impl.rooftop.RooftopVarrock;
import ethos.model.players.skills.crafting.Crafting;
import ethos.model.players.skills.farming.Farming;
import ethos.model.players.skills.fletching.Fletching;
import ethos.model.players.skills.herblore.Herblore;
import ethos.model.players.skills.hunter.Hunter;
import ethos.model.players.skills.mining.Mining;
import ethos.model.players.skills.prayer.Prayer;
import ethos.model.players.skills.runecrafting.Runecrafting;
import ethos.model.players.skills.slayer.Slayer;
import ethos.model.players.skills.thieving.Thieving;
import ethos.model.shops.ShopAssistant;
import ethos.net.Packet;
import ethos.net.Packet.Type;
import ethos.net.outgoing.UnnecessaryPacketDropper;
import ethos.util.Misc;
import ethos.util.SimpleTimer;
import ethos.util.Stopwatch;
import ethos.util.Stream;
import ethos.world.Clan;

public class Player extends Entity {

	public static int maRound = 0;
	public boolean maOption = false, maIndeedyOption = false;

	public int lastTeleportX, lastTeleportY, lastTeleportZ;

	public MageArena mageArena = new MageArena(this);

	public MageArena getMageArena() {
		return this.mageArena;
	}
	public int[][] raidReward ={{0,0}};
	public int raidCount;


	/**
	 * Overload variables
	 */

	public int homeTeleport = 50;

	public int boxCurrentlyUsing = 0;

	public int overloadTimer;
	public boolean overloadBoosted;

	public int infernoWaveId = 0;
	public int infernoWaveType = 0;

	/**
	 * Variables for trading post
	 */

	public boolean debugMessage = false;

	/**
	 * New Daily Task Variables
	 */

	public PossibleTasks currentTask;
	public TaskTypes playerChoice;
	public boolean dailyEnabled = false, completedDailyTask;
	public int dailyTaskDate, totalDailyDone = 0;

	public int item, uneditItem, quantity, price, pageId = 1, searchId;
	public String lookup;
	public List<Integer> saleResults;
	public ArrayList<Integer> saleItems = new ArrayList<Integer>();
	public ArrayList<Integer> saleAmount = new ArrayList<Integer>();
	public ArrayList<Integer> salePrice = new ArrayList<Integer>();
	public int[] historyItems = new int[15];
	public int[] historyItemsN = new int[15];
	public int[] historyPrice = new int[15];

	public boolean inSelecting = false, isListing = false;

	private RechargeItems rechargeItems = new RechargeItems(this);
	/**
	 * Classes
	 */
	private ExpLock explock = new ExpLock(this);
	private PrestigeSkills prestigeskills = new PrestigeSkills(this);
	private LootingBag lootingBag = new LootingBag(this);
	private SafeBox safeBox = new SafeBox(this);

	public RechargeItems getRechargeItems() {
		return rechargeItems;
	}

	private UltraMysteryBox ultraMysteryBox= new UltraMysteryBox(this);

	public UltraMysteryBox getUltraMysteryBox() {
		return ultraMysteryBox;
	}

	public TeleportType teleportType;
	public int teleSelected = 0;

	public TeleportData teleportData;
	public boolean placeHolderWarning = false;
	public int lastPlaceHolderWarning = 0;
	public boolean placeHolders = false;
	public final Stopwatch last_trap_layed = new Stopwatch();

	public List<Integer> searchList = new ArrayList<>();

	private final QuickPrayers quick = new QuickPrayers();

	private AchievementDiary<?> diary;
	private RunePouch runePouch = new RunePouch(this);
	private HerbSack herbSack = new HerbSack(this);
	private GemBag gemBag = new GemBag(this);
	private RandomEventInterface randomEventInterface = new RandomEventInterface(this);
	private DemonicGorilla demonicGorilla = null;
	private Mining mining = new Mining(this);
	private PestControlRewards pestControlRewards = new PestControlRewards(this);
	private WarriorsGuild warriorsGuild = new WarriorsGuild(this);
	private Zulrah zulrah = new Zulrah(this);
	private Raids raid = new Raids(this);
	private NPCDeathTracker npcDeathTracker = new NPCDeathTracker(this);
	private UnnecessaryPacketDropper packetDropper = new UnnecessaryPacketDropper();
	private DamageQueueEvent damageQueue = new DamageQueueEvent(this);
	private BountyHunter bountyHunter = new BountyHunter(this);
	private MysteryBox mysteryBox = new MysteryBox(this);
	private HourlyRewardBox hourlyRewardBox = new HourlyRewardBox(this);
	private PvmCasket pvmCasket = new PvmCasket(this);
	private SkillCasket skillCasket = new SkillCasket(this);
	private WildyCrate wildyCrate = new WildyCrate(this);
	private DailyGearBox dailyGearBox = new DailyGearBox(this);
	private DailySkillBox dailySkillBox = new DailySkillBox(this);
	private ChristmasPresent christmasPresent = new ChristmasPresent(this);
	private long lastContainerSearch;
	private HolidayStages holidayStages;
	private AchievementHandler achievementHandler;
	private PlayerKill playerKills;
	private String macAddress;
	private Duel duelSession = new Duel(this);
	private Player itemOnPlayer;
	private Presets presets = null;
	private Killstreak killstreaks;
	private PunishmentPanel punishmentPanel = new PunishmentPanel(this);
	private Tutorial tutorial = new Tutorial(this);
	private Mode mode;
	private Channel session;
	private Trade trade = new Trade(this);
	private ItemAssistant itemAssistant = new ItemAssistant(this);
	private ShopAssistant shopAssistant = new ShopAssistant(this);
	private PlayerAssistant playerAssistant = new PlayerAssistant(this);
	private CombatAssistant combat = new CombatAssistant(this);
	private ActionHandler actionHandler = new ActionHandler(this);
	private DialogueHandler dialogueHandler = new DialogueHandler(this);
	private Friends friend = new Friends(this);
	private Ignores ignores = new Ignores(this);
	private Queue<Packet> queuedPackets = new ConcurrentLinkedQueue<>();
	private Potions potions = new Potions(this);
	private PotionMixing potionMixing = new PotionMixing(this);
	private Food food = new Food(this);
	private Killstreak killingStreak = new Killstreak(this);
	private SkillInterfaces skillInterfaces = new SkillInterfaces(this);
	private ChargeTrident chargeTrident = new ChargeTrident(this);
	private PlayerAction playerAction = new PlayerAction(this);
	private TeleportHandler teleportHandler = new TeleportHandler(this);
	private Slayer slayer;
	private Runecrafting runecrafting = new Runecrafting();
	private AgilityHandler agilityHandler = new AgilityHandler();
	private PointItems pointItems = new PointItems(this);
	private GnomeAgility gnomeAgility = new GnomeAgility();
	private WildernessAgility wildernessAgility = new WildernessAgility();
	private Shortcuts shortcuts = new Shortcuts();
	private RooftopSeers rooftopSeers = new RooftopSeers();
	private RooftopFalador rooftopFalador = new RooftopFalador();
	private RooftopVarrock rooftopVarrock = new RooftopVarrock();
	private RooftopArdougne rooftopArdougne = new RooftopArdougne();
	private BarbarianAgility barbarianAgility = new BarbarianAgility();
	private Lighthouse lighthouse = new Lighthouse();
	private Agility agility = new Agility(this);
	private Cooking cooking = new Cooking();
	private Crafting crafting = new Crafting(this);
	private Prayer prayer = new Prayer(this);
	private Smithing smith = new Smithing(this);
	private FightCave fightcave = null;
	private DagannothMother dagannothMother = null;
	private RecipeForDisaster recipeForDisaster = null;
	private KalphiteQueen kq = null;
	private Cerberus cerberus = null;
	private Tzkalzuk tzkalzuk = null;
	private Skotizo skotizo = null;
	private InstanceSoloFight soloFight = null;
	private SmithingInterface smithInt = new SmithingInterface(this);
	private Herblore herblore = new Herblore(this);
	private Thieving thieving = new Thieving(this);
	private Fletching fletching = new Fletching(this);
	private Barrows barrows = new Barrows(this);
	private Godwars godwars = new Godwars(this);
	private TreasureTrails trails = new TreasureTrails(this);
	private Optional<ItemCombination> currentCombination = Optional.empty();
	private Skilling skilling = new Skilling(this);
	private Farming farming = new Farming(this);
	private ZulrahLostItems lostItemsZulrah;
	private CerberusLostItems lostItemsCerberus;
	private SkotizoLostItems lostItemsSkotizo;
	private List<God> equippedGodItems;
	private Titles titles = new Titles(this);
	protected RightGroup rights;
	protected static Stream playerProps;
	public static PlayerSave save;
	public static Player cliento2;
	public Player diceHost;
	public Clan clan;
	public Smelting.Bars bar = null;
	public byte buffer[] = null;
	public Stream inStream = null, outStream = null;
	public SimpleTimer potionTimer = new SimpleTimer();
	public int[] degradableItem = new int[Degrade.MAXIMUM_ITEMS];
	public boolean[] claimDegradableItem = new boolean[Degrade.MAXIMUM_ITEMS];
	private Entity targeted;

	private Equipment equipment;

	public Equipment getEquipment() {
		return equipment;
	}

	public Inferno inferno = new Inferno(this, Boundary.INFERNO, 0);

	public Inferno getInfernoMinigame() {
		return inferno;
	}

	public Inferno createInfernoInstance() {
		Boundary boundary = Boundary.INFERNO;

		int height = InstancedAreaManager.getSingleton().getNextOpenHeightCust(boundary, 4);

		inferno = new Inferno(this, boundary, height);

		return inferno;
	}

	/**
	 * Integers
	 */
	public int raidPoints, unfPotHerb, unfPotAmount, wrenchObject = 0, halloweenOrderNumber = 0, speed1 = -1,
			speed2 = -1, safeBoxSlots = 15, hostAmount = 3, corpDamage = 0, direction = -1, dialogueOptions = 0,
			sireHits = 0, lastMenuChosen = 0, dreamTime, unNoteItemId = 0, lootValue = 0, lowMemoryVersion = 0, emote,
			gfx, timeOutCounter = 0, returnCode = 2, currentRegion = 0, diceItem, page, specRestore = 0, gwdAltar = 0,
			lastClickedItem, slayerTasksCompleted, pestControlDamage, playerRank = 0, packetSize = 0, packetType = -1,
			makeTimes, event, ratsCaught, summonId, bossKills, droppedItem = -1, kbdCount, dagannothCount, krakenCount,
			chaosCount, armaCount, bandosCount, saraCount, zammyCount, barrelCount, moleCount, callistoCount,
			venenatisCount, vetionCount, rememberNpcIndex, diceMin, diceMax, otherDiceId, betAmount, totalProfit,
			betsWon, betsLost, slayerPoints = 0, playTime, killStreak, day, month, YEAR, totalLevel, xpTotal,
			smeltAmount = 0, smeltEventId = 5567, waveType, achievementsCompleted, achievementPoints, fireslit,
			crabsKilled, treesCut, pkp, killcount, deathcount, votePoints, bloodPoints, amDonated, level1 = 0,
			level2 = 0, level3 = 0, treeX, treeY, homeTele = 0, homeTeleDelay = 0, lastLoginDate, playerBankPin,
			recoveryDelay = 3, attemptsRemaining = 3, lastPinSettings = -1, setPinDate = -1, changePinDate = -1,
			deletePinDate = -1, firstPin, secondPin, thirdPin, fourthPin, bankPin1, bankPin2, bankPin3, bankPin4,
			pinDeleteDateRequested, saveDelay, playerKilled, totalPlayerDamageDealt, killedBy, lastChatId = 1,
			friendSlot = 0, dialogueId, specEffect, specBarId, attackLevelReq, defenceLevelReq, strengthLevelReq,
			rangeLevelReq, magicLevelReq, followId, skullTimer, votingPoints, nextChat = 0, talkingNpc = -1,
			dialogueAction = 0, autocastId, followDistance, followId2, barrageCount = 0, delayedDamage = 0,
			delayedDamage2 = 0, pcPoints = 0, donatorPoints = 0, magePoints = 0, lastArrowUsed = -1, clanId = -1,
			autoRet = 0, pcDamage = 0, xInterfaceId = 0, xRemoveId = 0, xRemoveSlot = 0, tzhaarToKill = 0,
			tzhaarKilled = 0, waveId, rfdWave = 0, rfdChat = 0, rfdGloves = 0, frozenBy = 0, teleAction = 0,
			newPlayerAct = 0, bonusAttack = 0, lastNpcAttacked = 0, killCount = 0, actionTimer, rfdRound = 0,
			roundNpc = 0, desertTreasure = 0, horrorFromDeep = 0, QuestPoints = 0, doricQuest = 0, teleGrabItem,
			teleGrabX, teleGrabY, duelCount, underAttackBy, underAttackBy2, wildLevel, teleTimer, respawnTimer,
			saveTimer = 0, teleBlockLength, focusPointX = -1, focusPointY = -1, WillKeepAmt1, WillKeepAmt2,
			WillKeepAmt3, WillKeepAmt4, WillKeepItem1, WillKeepItem2, WillKeepItem3, WillKeepItem4, WillKeepItem1Slot,
			WillKeepItem2Slot, WillKeepItem3Slot, WillKeepItem4Slot, EquipStatus, faceNPC = -1, DirectionCount = 0,
			itemUsing, attempts = 3, follow2 = 0, antiqueSelect = 0, leatherType = -1, DELAY = 1250, rangeEndGFX,
			boltDamage, teleotherType, playerTradeWealth, doAmount, woodcuttingTree, stageT, dfsCount, recoilHits,
			playerDialogue, clawDelay, previousDamage, prayerId = -1, headIcon = -1, bountyIcon = 0, headIconPk = -1,
			headIconHints, specMaxHitIncrease, freezeDelay, freezeTimer = -6, teleportTimer = 0, killerId, playerIndex,
			oldPlayerIndex, lastWeaponUsed, projectileStage, crystalBowArrowCount, playerMagicBook, teleGfx,
			teleEndAnimation, teleHeight, teleX, teleY, rangeItemUsed, killingNpcIndex, totalDamageDealt, oldNpcIndex,
			fightMode, attackTimer, npcIndex, npcClickIndex, npcType, castingSpellId, oldSpellId, spellId, hitDelay,
			bowSpecShot, clickNpcType, clickObjectType, objectId, objectX, objectY, objectXOffset, objectYOffset,
			objectDistance, itemX, itemY, itemId, myShopId, tradeStatus, tradeWith, amountGifted,
			playerAppearance[] = new int[13], apset, actionID, wearItemTimer, wearId, wearSlot, interfaceId,
			XremoveSlot, XinterfaceID, XremoveID, Xamount, fishTimer = 0, smeltType, smeltTimer = 0, attackAnim,
			animationRequest = -1, animationWaitCycles, combatLevel, wcTimer = 0, miningTimer = 0, castleWarsTeam,
			npcId2 = 0, playerStandIndex = 0x328, playerTurnIndex = 0x337, playerWalkIndex = 0x333,
			playerTurn180Index = 0x334, playerTurn90CWIndex = 0x335, playerTurn90CCWIndex = 0x336,
			playerRunIndex = 0x338, playerHat = 0, playerCape = 1, playerAmulet = 2, playerWeapon = 3, playerChest = 4,
			playerShield = 5, playerLegs = 7, playerHands = 9, playerFeet = 10, playerRing = 12, playerArrows = 13,
			playerAttack = 0, playerDefence = 1, playerStrength = 2, playerHitpoints = 3, playerRanged = 4,
			playerPrayer = 5, playerMagic = 6, playerCooking = 7, playerWoodcutting = 8, playerFletching = 9,
			playerFishing = 10, playerFiremaking = 11, playerMining = 14, playerHerblore = 15, playerAgility = 16,
			playerThieving = 17, playerSlayer = 18, playerFarming = 19, playerRunecrafting = 20, fletchingType,
			getHeightLevel, mapRegionX, mapRegionY, absX, absY, currentX, currentY, heightLevel, playerSE = 0x328,
			playerSEW = 0x333, playerSER = 0x334, npcListSize = 0, dir1 = -1, dir2 = -1, poimiX = 0, poimiY = 0,
			playerListSize = 0, wQueueReadPtr = 0, wQueueWritePtr = 0, teleportToX = -1, teleportToY = -1,
			pitsStatus = 0, safeTimer = 0, mask100var1 = 0, mask100var2 = 0, face = -1, FocusPointX = -1,
			FocusPointY = -1, newWalkCmdSteps = 0, tablet = 0, wellItem = -1, wellItemPrice = -1;
	private int chatTextColor = 0, chatTextEffects = 0, dragonfireShieldCharge, runEnergy = 100, lastEnergyRecovery,
			x1 = -1, y1 = -1, x2 = -1, y2 = -1, privateChat, shayPoints, arenaPoints, toxicStaffOfTheDeadCharge,
			toxicBlowpipeCharge, toxicBlowpipeAmmo, toxicBlowpipeAmmoAmount, serpentineHelmCharge, tridentCharge,
			toxicTridentCharge, arcLightCharge, runningDistanceTravelled, interfaceOpen;

	public final int walkingQueueSize = 50;
	public static int playerCrafting = 12, playerSmithing = 13;
	protected int numTravelBackSteps = 0, packetsReceived;

	/**
	 * Arrays
	 */
	public ArrayList<int[]> coordinates;
	private int[] farmingSeedId = new int[14];
	private int[] farmingTime = new int[14];
	private int[] originalFarmingTime = new int[14];
	private int[] farmingState = new int[14];
	private int[] farmingHarvest = new int[14];
	public int[] halloweenRiddleGiven = new int[10], halloweenRiddleChosen = new int[10],
			masterClueRequirement = new int[4], waveInfo = new int[3], keepItems = new int[4], keepItemsN = new int[4],
			voidStatus = new int[5], itemKeptId = new int[4], pouches = new int[4], playerStats = new int[8],
			playerBonus = new int[12], death = new int[4], twoHundredMil = new int[21], woodcut = new int[7],
			farm = new int[2], playerEquipment = new int[14], playerEquipmentN = new int[14], playerLevel = new int[25],
			playerXP = new int[25], damageTaken = new int[Config.MAX_PLAYERS], purchasedTeleport = new int[3],
			runeEssencePouch = new int[3], pureEssencePouch = new int[3];
	public int[] prestigeLevel = new int[25];
	public boolean[] skillLock = new boolean[25];
	public int playerItems[] = new int[28], playerItemsN[] = new int[28], bankItems[] = new int[Config.BANK_SIZE],
			bankItemsN[] = new int[Config.BANK_SIZE];
	public int counters[] = new int[20], raidsDamageCounters[] = new int[10];

	public boolean maxCape[] = new boolean[5];

	public int walkingQueueX[] = new int[walkingQueueSize], walkingQueueY[] = new int[walkingQueueSize];
	private int newWalkCmdX[] = new int[walkingQueueSize], newWalkCmdY[] = new int[walkingQueueSize];
	protected int travelBackX[] = new int[walkingQueueSize], travelBackY[] = new int[walkingQueueSize];
	public static final int maxPlayerListSize = Config.MAX_PLAYERS, maxNPCListSize = NPCHandler.maxNPCs;
	public Player playerList[] = new Player[maxPlayerListSize];
	public int[][] playerSkillProp = new int[20][15];
	public final int[] POUCH_SIZE = { 3, 6, 9, 12 };
	public static int[] ranks = new int[11];

	public boolean receivedStarter = false;

	/**
	 * Strings
	 */
	public String CERBERUS_ATTACK_TYPE = "";

	public String getATTACK_TYPE() {
		return CERBERUS_ATTACK_TYPE;
	}

	public void setATTACK_TYPE(String aTTACK_TYPE) {
		CERBERUS_ATTACK_TYPE = aTTACK_TYPE;
	}

	public String wrenchUsername = "", wogwOption = "", forcedText = "null", connectedFrom = "", quizAnswer = "",
			globalMessage = "", playerName = null, playerName2 = null, playerPass = null, date, clanName, properName,
			bankPin = "", lastReported = "", currentTime, barType = "", playerTitle = "", rottenPotatoOption = "";
	private String lastClanChat = "", revertOption = "";
	public static String[] rankPpl = new String[11];

	/**
	 * Booleans
	 */
	public boolean[] invSlot = new boolean[28], equipSlot = new boolean[14], playerSkilling = new boolean[20],
			clanWarRule = new boolean[10], duelRule = new boolean[22];
	public boolean teleportingToDistrict = false, morphed = false, isIdle = false, boneOnAltar = false,
			dropRateInKills = true, droppingItem = false, acceptAid = false, settingUnnoteAmount = false,
			settingLootValue = false, didYouKnow = true, yellChannel = true, documentGraphic = false,
			documentAnimation = false, inProcess = false, isStuck = false, isBusyFollow = false, hasOverloadBoost,
			needsNewTask = false, keepTitle = false, killTitle = false, hide = false, settingMin, settingMax,
			settingBet, playerIsCrafting, viewingLootBag = false, addingItemsToLootBag = false,
			viewingRunePouch = false, hasFollower = false, updateItems = false, claimedReward, craftDialogue,
			battlestaffDialogue, braceletDialogue, isAnimatedArmourSpawned, isSmelting = false, hasEvent,
			expLock = false, buyingX, leverClicked = false, isBanking = true, inSafeBox = false, isCooking = false,
			initialized = false, disconnected = false, ruleAgreeButton = false, rebuildNPCList = false,
			isActive = false, isKicked = false, isSkulled = false, friendUpdate = false, newPlayer = false,
			hasMultiSign = false, saveCharacter = false, mouseButton = false, splitChat = false, chatEffects = true,
			nextDialogue = false, autocasting = false, usedSpecial = false, mageFollow = false, dbowSpec = false,
			craftingLeather = false, properLogout = false, secDbow = false, maxNextHit = false, ssSpec = false,
			vengOn = false, addStarter = false, startPack = false, accountFlagged = false, msbSpec = false,
			dtOption = false, dtOption2 = false, doricOption = false, doricOption2 = false, caOption2 = false,
			caOption2a = false, caOption4a = false, caOption4b = false, caOption4c = false, caPlayerTalk1 = false,
			horrorOption = false, rfdOption = false, inDt = false, inHfd = false, disableAttEvt = false,
			AttackEventRunning = false, npcindex, spawned = false, hasBankPin, enterdBankpin, firstPinEnter,
			requestPinDelete, secondPinEnter, thirdPinEnter, fourthPinEnter, hasBankpin,
			appearanceUpdateRequired = true, isDead = false, randomEvent = false, FirstClickRunning = false,
			WildernessWarning = false, storing = false, canChangeAppearance = false, mageAllowed, isFullBody = false,
			isFullHelm = false, isFullMask = false, isOperate, usingLamp = false, normalLamp = false,
			antiqueLamp = false, setPin = false, teleporting, isWc, wcing, usingROD = false, multiAttacking,
			rangeEndGFXHeight, playerFletch, playerIsFletching, playerIsMining, playerIsFiremaking, playerIsFishing,
			playerIsCooking, below459 = true, defaultWealthTransfer, updateInventory, oldSpec, stopPlayerSkill,
			playerStun, stopPlayerPacket, usingClaws, playerBFishing, finishedBarbarianTraining, ignoreDefence,
			secondFormAutocast, usingArrows, usingOtherRangeWeapons, usingCross, usingBallista, magicDef, spellSwap,
			recoverysSet, protectItem = false, doubleHit, usingSpecial, npcDroppingItems, usingRangeWeapon, usingBow,
			usingMagic, usingAirSpells, usingWaterSpells, usingFireSpells, usingMelee, magicFailed, oldMagicFailed,
			isMoving, walkingToItem, isShopping, updateShop, forcedChatUpdateRequired, inDuel, tradeAccepted, goodTrade,
			inTrade, tradeRequested, tradeResetNeeded, tradeConfirmed, tradeConfirmed2, canOffer, acceptTrade,
			acceptedTrade, smeltInterface, patchCleared, saveFile = false, usingGlory = false, usingSkills = false,
			fishing = false, isRunning2 = true, takeAsNote, inCwGame, inCwWait, isNpc, seedPlanted = false,
			seedWatered = false, patchCleaned = false, patchRaked = false, inPits = false, bankNotes = false,
			isRunning = true, inSpecMode = false, didTeleport = false, mapRegionDidChange = false, createItems = false,
			slayerHelmetEffect, inArdiCC, attackSkill = false, strengthSkill = false, defenceSkill = false,
			mageSkill = false, rangeSkill = false, prayerSkill = false, healthSkill = false, usingChin, infoOn = false,
			pkDistrict = false, crystalDrop = false, hourlyBoxToggle = true, fracturedCrystalToggle = true,
			boltTips = false, arrowTips = false, javelinHeads = false;
	private boolean incentiveWarning, dropWarning = true, chatTextUpdateRequired = false, newWalkCmdIsRunning = false,
			dragonfireShieldActive, forceMovement, invisible, godmode, safemode, trading, stopPlayer, isBusy = false,
			isBusyHP = false, forceMovementActive = false;

	public boolean insidePost = false;

	/**
	 * @return the forceMovement
	 */
	public boolean isForceMovementActive() {
		return forceMovementActive;
	}

	protected boolean graphicMaskUpdate0x100 = false, faceUpdateRequired = false, faceNPCupdate = false;

	private final AchievementDiaryManager diaryManager = new AchievementDiaryManager(this);

	public int visibility = 0;
	/**
	 * Longs
	 */
	public long wogwDonationAmount, lastAuthClaim, totalGorillaDamage, totalGorillaHitsDone, totalMissedGorillaHits,
			lastImpling, lastWheatPass, lastRoll, lastCloseOfInterface, lastPerformedEmote, lastPickup, lastTeleport,
			lastMarkDropped, lastObstacleFail, lastForceMovement, lastDropTableSelected, lastDropTableSearch,
			lastDamageCalculation, lastBankDeposit, lastBankDeposit2, buySlayerTimer, buyPestControlTimer,
			fightCaveLeaveTimer, infernoLeaveTimer, lastFire, lastMove, bonusXpTime, yellDelay, craftingDelay,
			lastSmelt = 0, lastMysteryBox, lastYell, diceDelay, lastChat, lastRandom, lastCaught = 0, lastAttacked,
			lastTargeted, homeTeleTime, lastDagChange = -1, reportDelay, lastPlant, objectTimer, npcTimer, lastEss,
			lastClanMessage, lastCast, miscTimer, lastFlower, waitTime, saveButton = 0, lastButton, jailEnd, muteEnd,
			marketMuteEnd, lastReport = 0, stopPrayerDelay, prayerDelay, lastAntifirePotion, antifireDelay,
			staminaDelay, lastRunRecovery, rangeDelay, stuckTime, friends[] = new long[200],
			lastUpdate = System.currentTimeMillis(), lastPlayerMove = System.currentTimeMillis(), lastHeart = 0,
			lastSpear = System.currentTimeMillis(), lastProtItem = System.currentTimeMillis(),
			dfsDelay = System.currentTimeMillis(), lastVeng = System.currentTimeMillis(),
			foodDelay = System.currentTimeMillis(), switchDelay = System.currentTimeMillis(),
			potDelay = System.currentTimeMillis(), teleGrabDelay = System.currentTimeMillis(),
			protMageDelay = System.currentTimeMillis(), protMeleeDelay = System.currentTimeMillis(),
			protRangeDelay = System.currentTimeMillis(), lastAction = System.currentTimeMillis(),
			lastThieve = System.currentTimeMillis(), lastLockPick = System.currentTimeMillis(),
			alchDelay = System.currentTimeMillis(), specCom = System.currentTimeMillis(),
			specDelay = System.currentTimeMillis(), duelDelay = System.currentTimeMillis(),
			teleBlockDelay = System.currentTimeMillis(), godSpellDelay = System.currentTimeMillis(),
			singleCombatDelay = System.currentTimeMillis(), singleCombatDelay2 = System.currentTimeMillis(),
			reduceStat = System.currentTimeMillis(), restoreStatsDelay = System.currentTimeMillis(),
			logoutDelay = System.currentTimeMillis(), buryDelay = System.currentTimeMillis(),
			cerbDelay = System.currentTimeMillis(), cleanDelay = System.currentTimeMillis(),
			wogwDelay = System.currentTimeMillis();
	private long revertModeDelay, experienceCounter, bestZulrahTime, lastIncentive, lastOverloadBoost, nameAsLong,
			lastDragonfireShieldAttack;

	/**
	 * Others
	 */
	public ArrayList<String> killedPlayers = new ArrayList<String>(), lastConnectedFrom = new ArrayList<String>();
	public double specAmount = 0, specAccuracy = 1, specDamage = 1, prayerPoint = 1.0, crossbowDamage;
	public byte playerInListBitmap[] = new byte[(Config.MAX_PLAYERS + 7) >> 3],
			npcInListBitmap[] = new byte[(NPCHandler.maxNPCs + 7) >> 3],
			cachedPropertiesBitmap[] = new byte[(Config.MAX_PLAYERS + 7) >> 3];
	private byte chatText[] = new byte[4096], chatTextSize = 0;
	public NPC npcList[] = new NPC[maxNPCListSize];
	public ArrayList<Integer> attackedPlayers = new ArrayList<Integer>();
	private Map<Integer, TinterfaceText> interfaceText = new HashMap<>();

	@Override
	public String toString() {
		return "player[" + playerName + "]";
	}

	public Position getPosition() {
		return new Position(absX, absY, heightLevel);
	}

	public Player(int index, String name, Channel channel) {
		super(index, name);
		this.session = channel;
		rights = new RightGroup(this, Right.PLAYER);

		for (int i = 0; i < playerItems.length; i++) {
			playerItems[i] = 0;
		}
		for (int i = 0; i < playerItemsN.length; i++) {
			playerItemsN[i] = 0;
		}

		for (int i = 0; i < playerLevel.length; i++) {
			if (i == 3) {
				playerLevel[i] = 10;
			} else {
				playerLevel[i] = 1;
			}
		}

		for (int i = 0; i < playerXP.length; i++) {
			if (i == 3) {
				playerXP[i] = 1300;
			} else {
				playerXP[i] = 0;
			}
		}
		for (int i = 0; i < Config.BANK_SIZE; i++) {
			bankItems[i] = 0;
		}

		for (int i = 0; i < Config.BANK_SIZE; i++) {
			bankItemsN[i] = 0;
		}

		playerAppearance[0] = 0; // gender
		playerAppearance[1] = 0; // head
		playerAppearance[2] = 18;// Torso
		playerAppearance[3] = 26; // arms
		playerAppearance[4] = 33; // hands
		playerAppearance[5] = 36; // legs
		playerAppearance[6] = 42; // feet
		playerAppearance[7] = 10; // beard
		playerAppearance[8] = 0; // hair colour
		playerAppearance[9] = 0; // torso colour
		playerAppearance[10] = 0; // legs colour
		playerAppearance[11] = 0; // feet colour
		playerAppearance[12] = 0; // skin colour

		apset = 0;
		actionID = 0;

		playerEquipment[playerHat] = -1;
		playerEquipment[playerCape] = -1;
		playerEquipment[playerAmulet] = -1;
		playerEquipment[playerChest] = -1;
		playerEquipment[playerShield] = -1;
		playerEquipment[playerLegs] = -1;
		playerEquipment[playerHands] = -1;
		playerEquipment[playerFeet] = -1;
		playerEquipment[playerRing] = -1;
		playerEquipment[playerArrows] = -1;
		playerEquipment[playerWeapon] = -1;

		heightLevel = 0;

		teleportToX = Config.START_LOCATION_X;
		teleportToY = Config.START_LOCATION_Y;

		absX = absY = -1;
		mapRegionX = mapRegionY = -1;
		currentX = currentY = 0;
		resetWalkingQueue();
		// synchronized(this) {
		outStream = new Stream(new byte[Config.BUFFER_SIZE]);
		outStream.currentOffset = 0;

		inStream = new Stream(new byte[Config.BUFFER_SIZE]);
		inStream.currentOffset = 0;
		buffer = new byte[Config.BUFFER_SIZE];
		// }
	}

	public Player getClient(String name) {
		for (Player p : PlayerHandler.players) {
			if (p != null && p.playerName.equalsIgnoreCase(name)) {
				return p;
			}
		}
		return null;
	}

	private Bank bank;

	public Bank getBank() {
		if (bank == null)
			bank = new Bank(this);
		return bank;
	}

	private BankPin pin;

	public BankPin getBankPin() {
		if (pin == null)
			pin = new BankPin(this);
		return pin;
	}

	public void sendMessage(String s, int color) {
		// synchronized (this) {
		if (getOutStream() != null) {
			s = "<col=" + color + ">" + s + "</col>";
			outStream.createFrameVarSize(253);
			outStream.writeString(s);
			outStream.endFrameVarSize();
		}
	}

	public Player getClient(int id) {
		return PlayerHandler.players[id];
	}

	public void flushOutStream() {
		if (!session.isConnected() || disconnected || outStream.currentOffset == 0)
			return;

		byte[] temp = new byte[outStream.currentOffset];
		System.arraycopy(outStream.buffer, 0, temp, 0, temp.length);
		Packet packet = new Packet(-1, Type.FIXED, ChannelBuffers.wrappedBuffer(temp));
		session.write(packet);
		outStream.currentOffset = 0;

	}

	public class TinterfaceText {
		public int id;
		public String currentState;

		public TinterfaceText(String s, int id) {
			this.currentState = s;
			this.id = id;
		}

	}

	public boolean checkPacket126Update(String text, int id) {
		if (interfaceText.containsKey(id)) {
			TinterfaceText t = interfaceText.get(id);
			if (text.equals(t.currentState)) {
				return false;
			}
		}
		interfaceText.put(id, new TinterfaceText(text, id));
		return true;
	}

	public void sendClan(String name, String message, String clan, int rights) {
		name = name.substring(0, 1).toUpperCase() + name.substring(1);
		message = message.substring(0, 1).toUpperCase() + message.substring(1);
		clan = clan.substring(0, 1).toUpperCase() + clan.substring(1);
		outStream.createFrameVarSizeWord(217);
		outStream.writeString(name);
		outStream.writeString(message);
		outStream.writeString(clan);
		outStream.writeWord(rights);
		outStream.endFrameVarSize();
	}

	public static final int PACKET_SIZES[] = { 0, 0, 0, 1, -1, 0, 0, 0, 4, 0, // 0
			0, 0, 0, 0, 8, 0, 6, 2, 2, 0, // 10
			0, 2, 0, 6, 0, 12, 0, 0, 0, 0, // 20
			0, 0, 0, 0, 0, 8, 4, 0, 0, 2, // 30
			2, 6, 0, 6, 0, -1, 0, 0, 0, 0, // 40 no they're different they change depending on direction
			0, 0, 0, 12, 0, 0, 0, 8, 8, 12, // 50
			8, 8, 0, 0, 0, 0, 0, 0, 0, 0, // 60
			6, 0, 2, 2, 8, 6, 0, -1, 0, 6, // 70
			0, 0, 0, 0, 0, 1, 4, 6, 0, 0, // 80
			0, 0, 0, 0, 0, 3, 0, 0, -1, 0, // 90
			0, 13, 0, -1, 0, 0, 0, 0, 0, 0, // 100
			0, 0, 0, 0, 0, 0, 0, 6, 0, 0, // 110
			1, 0, 6, 0, 16, 0, -1, -1, 2, 6, // 120
			0, 4, 6, 8, 0, 6, 0, 0, 0, 2, // 130
			6, 10, -1, 0, 0, 6, 0, 0, 0, 0, // 140
			0, 0, 1, 2, 0, 2, 6, 0, 0, 0, // 150
			0, 0, 0, 0, -1, -1, 0, 0, 0, 0, // 160
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 170
			0, 8, 0, 3, 0, 2, 2, 0, 8, 1, // 180
			0, 0, 12, 0, 0, 0, 0, 0, 0, 0, // 190
			2, 0, 0, 0, 0, 0, 0, 0, 4, 0, // 200
			4, 0, 0, /* 0 */4, 7, 8, 0, 0, 10, 0, // 210
			0, 0, 0, 0, 0, 0, -1, 0, 6, 0, // 220
			1, 0, 4, 0, 6, 0, 6, 8, 1, 0, // 230
			0, 4, 0, 0, 0, 0, -1, 0, -1, 4, // 240
			0, 0, 6, 6, 0, 0, 0 // 250
	};

	public int VERIFICATION = 0;

	public void resetRanks() {
		for (int i = 0; i < 10; i++) {
			ranks[i] = 0;
			rankPpl[i] = "";
		}
	}

	public void highscores() {
		getPA().sendFrame126("Ascend - Top PKers Online", 6399); // Title
		for (int i = 0; i < 10; i++) {
			if (ranks[i] > 0) {
				getPA().sendFrame126("Rank " + (i + 1) + ": " + rankPpl[i] + " - Kills: " + ranks[i] + "", 6402 + i);
			}
		}
		getPA().showInterface(6308);
		flushOutStream();
		resetRanks();
	}
	
	private boolean updatedHs = false;

	public void destruct() {
		Hunter.abandon(this, null, true);
		if (session == null) {
			return;
		}
		if (combatLevel >= 100) {
			if (Highpkarena.getState(this) != null) {
				Highpkarena.removePlayer(this, true);
			}
		} else if (combatLevel >= 80 && combatLevel <= 99) {
			if (Lowpkarena.getState(this) != null) {
				Lowpkarena.removePlayer(this, true);
			}
		}
		if (zulrah.getInstancedZulrah() != null) {
			InstancedAreaManager.getSingleton().disposeOf(zulrah.getInstancedZulrah());
		}
		if (dagannothMother != null) {
			InstancedAreaManager.getSingleton().disposeOf(dagannothMother);
		}
		if (recipeForDisaster != null) {
			InstancedAreaManager.getSingleton().disposeOf(recipeForDisaster);
		}
		if (cerberus != null) {
			InstancedAreaManager.getSingleton().disposeOf(cerberus);
		}
		if (tzkalzuk != null) {
			InstancedAreaManager.getSingleton().disposeOf(tzkalzuk);
		}
		if (skotizo != null) {
			InstancedAreaManager.getSingleton().disposeOf(skotizo);
		}
		if (Vorkath.inVorkath(this)) {
			getPA().movePlayer(2272, 4052, 0);
		}
		if (getPA().viewingOtherBank) {
			getPA().resetOtherBank();
		}
		if (Boundary.isIn(this, PestControl.GAME_BOUNDARY)) {
			PestControl.removeGameMember(this);
		}
		if (Boundary.isIn(this, PestControl.LOBBY_BOUNDARY)) {
			PestControl.removeFromLobby(this);
		}
		if (underAttackBy > 0 || underAttackBy2 > 0)
			return;

		if (disconnected == true) {
			saveCharacter = true;
		}
		Server.getMultiplayerSessionListener().removeOldRequests(this);
		if (clan != null) {
			clan.removeMember(this);
		}
		Server.getEventHandler().stop(this);
		CycleEventHandler.getSingleton().stopEvents(this);
		getFriends().notifyFriendsOfUpdate();
		Misc.println("[Logged out]: " + playerName);
		disconnected = true;
		// logoutDelay = Long.MAX_VALUE;
		session.close();
		session = null;
		inStream = null;
		outStream = null;
		isActive = false;
		buffer = null;
		playerListSize = 0;
		for (int i = 0; i < maxPlayerListSize; i++)
			playerList[i] = null;
		absX = absY = -1;
		mapRegionX = mapRegionY = -1;
		currentX = currentY = 0;
		resetWalkingQueue();
	}

	public void sendMessage(String s) {
		// synchronized (this) {
		if (getOutStream() != null) {
			outStream.createFrameVarSize(253);
			outStream.writeString(s);
			outStream.endFrameVarSize();
		}
	}

	public void setSidebarInterface(int menuId, int form) {
		// synchronized (this) {
		if (getOutStream() != null) {
			outStream.createFrame(71);
			outStream.writeWord(form);
			outStream.writeByteA(menuId);
		}

	}

	public int diaryAmount = 0;

	public int amountOfDiariesComplete() {
		if (getDiaryManager().getVarrockDiary().hasDoneAll())
			diaryAmount += 1;
		if (getDiaryManager().getArdougneDiary().hasDoneAll())
			diaryAmount += 1;
		if (getDiaryManager().getDesertDiary().hasDoneAll())
			diaryAmount += 1;
		if (getDiaryManager().getFaladorDiary().hasDoneAll())
			diaryAmount += 1;
		if (getDiaryManager().getFremennikDiary().hasDoneAll())
			diaryAmount += 1;
		if (getDiaryManager().getKandarinDiary().hasDoneAll())
			diaryAmount += 1;
		if (getDiaryManager().getKaramjaDiary().hasDoneAll())
			diaryAmount += 1;
		if (getDiaryManager().getLumbridgeDraynorDiary().hasDoneAll())
			diaryAmount += 1;
		if (getDiaryManager().getMorytaniaDiary().hasDoneAll())
			diaryAmount += 1;
		if (getDiaryManager().getWesternDiary().hasDoneAll())
			diaryAmount += 1;
		if (getDiaryManager().getWildernessDiary().hasDoneAll())
			diaryAmount += 1;

		return diaryAmount;
	}

	public void refreshQuestTab(int i) {

	}

	public void loadDiaryTab() {

	}

	private enum RankUpgrade {
		CONTRIBUTOR(Right.CONTRIBUTOR, 10), SPONSOR(Right.SPONSOR, 30), SUPPORTER(Right.SUPPORTER, 75), VIP(
				Right.DONATOR, 150), SUPER_VIP(Right.SUPER_DONATOR,
						300), MEGA_VIP(Right.EXTREME_DONATOR, 500), LEGENDARY(Right.LEGENDARY, 1000);

		/**
		 * The rights that will be appended if upgraded
		 */
		private final Right rights;

		/**
		 * The amount required for the upgrade
		 */
		private final int amount;

		private RankUpgrade(Right rights, int amount) {
			this.rights = rights;
			this.amount = amount;
		}
	}

	public void initialize() {
		try {
			loadDiaryTab();
			graceSum();
			Achievements.checkIfFinished(this);
			getPA().loadQuests();
			setStopPlayer(false);
			getPlayerAction().setAction(false);
			getPlayerAction().canWalk(true);
			getPA().sendFrame126(runEnergy + "%", 149);
			isFullHelm = Item.isFullHat(playerEquipment[playerHat]);
			isFullMask = Item.isFullMask(playerEquipment[playerHat]);
			isFullBody = Item.isFullBody(playerEquipment[playerChest]);
			getPA().sendFrame36(173, isRunning2 ? 1 : 0);
			getPA().setConfig(427, acceptAid ? 1 : 0);
			/**
			 * Welcome messages
			 */
			sendMessage("Welcome to " + Config.SERVER_NAME + ".");
			if (getSlayer().superiorSpawned) {
				getSlayer().superiorSpawned = false;
			}
			// checkWellOfGoodwillTimers();

			if (getRights().isOrInherits(Right.IRONMAN)) {
				ArrayList<RankUpgrade> orderedList = new ArrayList<>(Arrays.asList(RankUpgrade.values()));
				orderedList.sort((one, two) -> Integer.compare(two.amount, one.amount));
				orderedList.stream().filter(r -> amDonated >= r.amount).findFirst().ifPresent(rank -> {
					RightGroup rights = getRights();
					Right right = rank.rights;
					if (!rights.contains(right)) {
						sendMessage("@blu@Congratulations, your rank has been upgraded to " + right.toString() + ".");
						sendMessage("@blu@This rank is hidden, but you will have all it's perks.");
						rights.add(right);
					}
				});
			}
			//if (!Config.local) {
			//	PlayersOnline.createCon();
			//	PlayersOnline.online(this);
			//}
			combatLevel = calculateCombatLevel();
			outStream.createFrame(249);
			outStream.writeByteA(1); // 1 for members, zero for free
			outStream.writeWordBigEndianA(getIndex());
			for (int j = 0; j < PlayerHandler.players.length; j++) {
				if (j == getIndex())
					continue;
				if (PlayerHandler.players[j] != null) {
					if (PlayerHandler.players[j].playerName.equalsIgnoreCase(playerName))
						disconnected = true;
				}
			}
			for (int p = 0; p < PRAYER.length; p++) { // reset prayer glows
				prayerActive[p] = false;
				getPA().sendFrame36(PRAYER_GLOW[p], 0);
			}

			getPA().handleWeaponStyle();
			accountFlagged = getPA().checkForFlags();
			getPA().sendFrame36(108, 0);
			getPA().sendFrame36(172, 1);
			getPA().sendFrame107(); // reset screen
			setSidebarInterface(0, 2423);
			setSidebarInterface(1, 13917); // Skilltab > 3917
			setSidebarInterface(2, 10220); // 638
			setSidebarInterface(3, 3213);
			setSidebarInterface(4, 1644);
			setSidebarInterface(5, 15608);
			setSidebarInterface(13, 47500);
			switch (playerMagicBook) {
			case 0:
				setSidebarInterface(6, 938); // modern
				break;

			case 1:
				setSidebarInterface(6, 838); // ancient
				break;

			case 2:
				setSidebarInterface(6, 29999); // ancient
				break;
			}

			if (hasFollower) {
				if (summonId > 0) {
					Pets pet = PetHandler.forItem(summonId);
					if (pet != null) {
						PetHandler.spawn(this, pet, true, false);
					}
				}
			}
			if (splitChat) {
				getPA().sendFrame36(502, 1);
				getPA().sendFrame36(287, 1);
			}
			setSidebarInterface(7, 18128);
			setSidebarInterface(8, 5065);
			setSidebarInterface(9, 5715);
			setSidebarInterface(10, 2449);
			setSidebarInterface(11, 42500); // wrench tab
			setSidebarInterface(12, 147); // run tab
			getPA().showOption(4, 0, "Follow", 3);
			getPA().showOption(5, 0, "Trade with", 4);
			// getPA().showOption(6, 0, nu, 5);
			getItems().resetItems(3214);
			getItems().sendWeapon(playerEquipment[playerWeapon],
					ItemAssistant.getItemName(playerEquipment[playerWeapon]));
			getItems().resetBonus();
			getItems().getBonus();
			getItems().writeBonus();
			getItems().setEquipment(playerEquipment[playerHat], 1, playerHat);
			getItems().setEquipment(playerEquipment[playerCape], 1, playerCape);
			getItems().setEquipment(playerEquipment[playerAmulet], 1, playerAmulet);
			getItems().setEquipment(playerEquipment[playerArrows], playerEquipmentN[playerArrows], playerArrows);
			getItems().setEquipment(playerEquipment[playerChest], 1, playerChest);
			getItems().setEquipment(playerEquipment[playerShield], 1, playerShield);
			getItems().setEquipment(playerEquipment[playerLegs], 1, playerLegs);
			getItems().setEquipment(playerEquipment[playerHands], 1, playerHands);
			getItems().setEquipment(playerEquipment[playerFeet], 1, playerFeet);
			getItems().setEquipment(playerEquipment[playerRing], 1, playerRing);
			getItems().setEquipment(playerEquipment[playerWeapon], playerEquipmentN[playerWeapon], playerWeapon);
			getCombat().getPlayerAnimIndex(ItemAssistant.getItemName(playerEquipment[playerWeapon]).toLowerCase());
			getPlayerAssistant().updateQuestTab();
			if (getPrivateChat() > 2) {
				setPrivateChat(0);
			}

			outStream.createFrame(221);
			outStream.writeByte(2);

			outStream.createFrame(206);
			outStream.writeByte(0);
			outStream.writeByte(getPrivateChat());
			outStream.writeByte(0);
			getFriends().sendList();
			getIgnores().sendList();

			getItems().addSpecialBar(playerEquipment[playerWeapon]);
			saveTimer = Config.SAVE_TIMER;
			saveCharacter = true;
			Server.playerHandler.updatePlayer(this, outStream);
			Server.playerHandler.updateNPC(this, outStream);
			flushOutStream();
			totalLevel = getPA().totalLevel();
			xpTotal = getPA().xpTotal();
			updateQuestTab();
			getPA().sendFrame126("Combat Level: " + combatLevel + "", 3983);
			getPA().sendFrame126("Total level:", 19209);
			getPA().sendFrame126(totalLevel + "", 3984);
			getPA().resetFollow();
			getPA().clearClanChat();
			getPA().resetFollow();
			getPA().setClanData();
			updateRank();
			if (startPack == false) {
				getRights().remove(Right.IRONMAN);
				getRights().remove(Right.ULTIMATE_IRONMAN);
				startPack = true;
				Server.clanManager.getHelpClan().addMember(this);
				tutorial.setStage(Stage.START);
				mode = Mode.forType(ModeType.REGULAR);
			} else {
				if (mode == null && tutorial.getStage() == null) {
					mode = Mode.forType(ModeType.REGULAR);
					tutorial.autoComplete();
				}
				Server.clanManager.joinOnLogin(this);
			}
			if (tutorial.isActive()) {
				tutorial.refresh();
			}
			if (autoRet == 1)
				getPA().sendFrame36(172, 1);
			else
				getPA().sendFrame36(172, 0);
			addEvents();
			if (Config.BOUNTY_HUNTER_ACTIVE) {
				bountyHunter.updateTargetUI();
			}
			for (int i = 0; i < playerLevel.length; i++) {
				getPA().refreshSkill(i);
				getPA().setSkillLevel(i, playerLevel[i], playerXP[i]);
			}
			health.setMaximum(getPA().getLevelForXP(playerXP[playerHitpoints]));
			BankPin pin = getBankPin();
			if (pin.requiresUnlock()) {
				pin.open(2);
			}
			correctCoordinates();
			initialized = true;
			if (health.getAmount() < 10) {
				health.setAmount(10);
			}
			int[] ids = new int[playerLevel.length];
			for (int skillId = 0; skillId < ids.length; skillId++) {
				ids[skillId] = skillId;
			}
			if (experienceCounter > 0L) {
				playerAssistant.sendExperienceDrop(false, experienceCounter, ids);
			}
			rechargeItems.onLogin();
			DailyTasks.complete(this);
			DailyTasks.assignTask(this);
			for (int i = 0; i < getQuick().getNormal().length; i++) {
				if (getQuick().getNormal()[i]) {
					getPA().sendConfig(QuickPrayers.CONFIG + i, 1);
				} else {
					getPA().sendConfig(QuickPrayers.CONFIG + i, 0);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Player login - Check for error");
		}
	}
	public void updateQuestTab(){

		getPA().sendFrame126("@cr11@@or1@ Players online : @gre@"+PlayerHandler.getPlayers().size(),10407);
		getPA().sendFrame126("@cr22@@or1@ Wilderness count: @gre@"+Boundary.entitiesInArea(Boundary.WILDERNESS),10408);
		if(MonsterHunt.getCurrentLocation() != null){
			getPA().sendFrame126("@cr19@@or1@Current event : @gre@"+MonsterHunt.getName(),10409);
		}else{
			getPA().sendFrame126("@cr19@@or1@Current event : @red@None",10409);
		}
		long milliseconds = (long) playTime * 600;
		long days = TimeUnit.MILLISECONDS.toDays(milliseconds);
			long hours = TimeUnit.MILLISECONDS.toHours(milliseconds - TimeUnit.DAYS.toMillis(days));
			long minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds - TimeUnit.DAYS.toMillis(days) - TimeUnit.HOURS.toMillis(hours));
			long seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds - TimeUnit.DAYS.toMillis(days) - TimeUnit.HOURS.toMillis(hours) - TimeUnit.MINUTES.toMillis(minutes));
		String time = days + " days, " + hours + " hrs";
		getPA().sendFrame126("@or1@@cr20@Time Played = @gre@"+time,10225);
		getPA().sendFrame126("@or1@@cr1@ Player Rank = @gre@"+getRights().getPrimary().toString(),10226);
		getPA().sendFrame126("@or1@@cr15@ Player Title = @gre@ ",10227);
		getPA().sendFrame126("@or1@@cr21@ KDR = @gre@"+ (double)(this.deathcount == 0 ? this.killcount + this.deathcount : this.killcount/this.deathcount),10228);
		getPA().sendFrame126("@or1@@cr8@ Amount donated = @gre@$" + this.amDonated,10229);
		getPA().sendFrame126("@or1@@cr16@  PK Points = @gre@" +this.pkp,10230);
		getPA().sendFrame126("@or1@@cr22@  Slayer Points = @gre@" +this.slayerPoints,10231);
		getPA().sendFrame126("@or1@@cr17@ PC points = @gre@" +this.pcPoints,10232);
		getPA().sendFrame126("@or1@@cr4@ Shayzien points = @gre@" +this.shayPoints,10233);


		getPA().sendFrame126("@or1@View the forums",47514);
		getPA().sendFrame126("@or1@View vote page",47515);
		getPA().sendFrame126("@or1@View online store",47516);
		getPA().sendFrame126("@or1@View the rules",47517);
		getPA().sendFrame126("@or1@View community guides ",47518);

	}
	public void addEvents() {
		Server.getEventHandler().submit(new MinigamePlayersEvent(this));
		Server.getEventHandler().submit(new SkillRestorationEvent(this));
		Server.getEventHandler().submit(new IronmanRevertEvent(this, 50));
		Server.getEventHandler().submit(new RunEnergyEvent(this, 1));
		CycleEventHandler.getSingleton().addEvent(this, bountyHunter, 1);
		CycleEventHandler.getSingleton().addEvent(CycleEventHandler.Event.PLAYER_COMBAT_DAMAGE, this, damageQueue, 1,
				true);
	}

	public void update() {
		Server.playerHandler.updatePlayer(this, outStream);
		Server.playerHandler.updateNPC(this, outStream);
		flushOutStream();

	}

	public void wildyWarning() {
		getPA().showInterface(1908);
	}

	/**
	 * Update {@link #equippedGodItems}, which is a list of all gods of which the
	 * player has at least 1 item equipped.
	 */
	
	public void updateGodItems() {
		equippedGodItems = new ArrayList<>();
		for (God god : God.values()) {
			for (Integer itemId : GodwarsEquipment.EQUIPMENT.get(god)) {
				if (getItems().isWearingItem(itemId)) {
					equippedGodItems.add(god);
					break;
				}
			}
		}
	}

	public List<God> getEquippedGodItems() {
		return equippedGodItems;
	}

	public void logout() {
		if (this.clan != null) {
			this.clan.removeMember(this);
		}
		if (Vorkath.inVorkath(this)) {
			this.getPA().movePlayer(2272, 4052, 0);
		}
		if (getPA().viewingOtherBank) {
			getPA().resetOtherBank();
		}
		DuelSession duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(this,
				MultiplayerSessionType.DUEL);
		if (Objects.nonNull(duelSession) && duelSession.getStage().getStage() > MultiplayerSessionStage.REQUEST) {
			if (duelSession.getStage().getStage() >= MultiplayerSessionStage.FURTHER_INTERATION) {
				sendMessage("You are not permitted to logout during a duel. If you forcefully logout you will");
				sendMessage("lose all of your staked items, if any, to your opponent.");
			}
		}
		if (!isIdle && underAttackBy2 > 0) {
			sendMessage("You can't log out until 10 seconds after the end of combat.");
			return;
		}
		if (underAttackBy > 0) {
			sendMessage("You can't log out until 10 seconds after the end of combat.");
			return;
		}
		if (System.currentTimeMillis() - logoutDelay > 5000) {
			Hunter.abandon(this, null, true);
			outStream.createFrame(109);
			if (skotizo != null)
				skotizo.end(DisposeTypes.INCOMPLETE);
			CycleEventHandler.getSingleton().stopEvents(this);
			properLogout = true;
			disconnected = true;
			ConnectedFrom.addConnectedFrom(this, connectedFrom);
		}
	}

	public int totalRaidsFinished;

	public boolean hasClaimedRaidChest;

	public int[] SLAYER_HELMETS = { 11864, 11865, 19639, 19641, 19643, 19645, 19647, 19649,21888 };
	public int[] IMBUED_SLAYER_HELMETS = { 11865, 19641, 19645, 19649 };

	public int[] GRACEFUL = { 11850, 11852, 11854, 11856, 11858, 11860, 13579, 13581, 13583, 13585, 13587, 13589, 13591,
			13593, 13595, 13597, 13599, 13601, 13603, 13605, 13607, 13609, 13611, 13613, 13615, 13617, 13619, 13621,
			13623, 13625, 13627, 13629, 13631, 13633, 13635, 13637, 13667, 13669, 13671, 13673, 13675, 13677, 21061,
			21064, 21067, 21070, 21073, 21076 };

	private boolean wearingGrace() {
		return getItems().isWearingAnyItem(GRACEFUL);
	}

	public int graceSum = 0;

	public void graceSum() {
		graceSum = 0;
		for (int grace : GRACEFUL) {
			if (getItems().isWearingItem(grace)) {
				graceSum++;
			}
		}
		if (SkillcapePerks.AGILITY.isWearing(this) || SkillcapePerks.isWearingMaxCape(this)) {
			graceSum++;
		}
	}

	public int olmType, leftClawType, rightClawType;

	public boolean leftClawDead;
	public boolean rightClawDead;

	public boolean hasSpawnedOlm;

	public void process() {
		farming.farmingProcess();


		if (isRunning && runEnergy <= 0) {
			isRunning = false;
			isRunning2 = false;
			playerAssistant.sendFrame126(Integer.toString(runEnergy) + "%", 149);
			playerAssistant.setConfig(173, 0);
		}

		if (staminaDelay > 0) {
			staminaDelay--;
		}

		if (gwdAltar > 0) {
			gwdAltar--;
		}
		if (gwdAltar == 1) {
			sendMessage("You can now operate the godwars prayer altar again.");
		}

		if (isRunning && runningDistanceTravelled > (wearingGrace() ? 1 + graceSum : staminaDelay != -1 ? 3 : 1)) {
			runningDistanceTravelled = 0;
			runEnergy -= 1;
			playerAssistant.sendFrame126(Integer.toString(runEnergy) + "%", 149);
		}

		if (isRunning && runningDistanceTravelled > (wearingGrace() ? 1 + graceSum : staminaDelay != -1 ? 3 : 1)) {
			runningDistanceTravelled = 0;
			runEnergy -= 1;
			playerAssistant.sendFrame126(Integer.toString(runEnergy) + "%", 149);
		}

		if (updateItems) {
			itemAssistant.updateItems();
		}
		if (isDead && respawnTimer == -6) {
			getPA().applyDead();
		}
		if (bonusXpTime > 0) {
			bonusXpTime--;
		}
		if (bonusXpTime == 1) {
			sendMessage("@blu@Your time is up. Your XP is no longer boosted by the voting reward.");
		}
		if (respawnTimer == 7) {
			respawnTimer = -6;
			getPA().giveLife();
		} else if (respawnTimer == 12) {
			respawnTimer--;
			startAnimation(0x900);
		}
		if (Boundary.isIn(this, Zulrah.BOUNDARY) && getZulrahEvent().isInToxicLocation()) {
			appendDamage(1 + Misc.random(3), Hitmark.VENOM);
		}

		if (respawnTimer > -6) {
			respawnTimer--;
		}
		if (hitDelay > 0) {
			hitDelay--;
		}

		getAgilityHandler().agilityProcess(this);

		if (specRestore > 0) {
			specRestore--;
		}

		if (rangeDelay > 0) {
			rangeDelay--;
		}
		if (playTime < Integer.MAX_VALUE && !isIdle) {
			playTime++;
		}

		//getPA().sendFrame126("@or1@Players Online: @gre@" + PlayerHandler.getPlayerCount() + "", 10222);
		if (System.currentTimeMillis() - specDelay > Config.INCREASE_SPECIAL_AMOUNT) {
			specDelay = System.currentTimeMillis();
			if (specAmount < 10) {
				specAmount += 1;
				if (specAmount > 10)
					specAmount = 10;
				getItems().addSpecialBar(playerEquipment[playerWeapon]);
			}
		}

		getCombat().handlePrayerDrain();
		if (System.currentTimeMillis() - singleCombatDelay > 5000) {
			underAttackBy = 0;
		}
		if (System.currentTimeMillis() - singleCombatDelay2 > 5000) {
			underAttackBy2 = 0;
		}
		if (hasOverloadBoost) {
			if (System.currentTimeMillis() - lastOverloadBoost > 15000) {
				getPotions().doOverloadBoost();
				lastOverloadBoost = System.currentTimeMillis();
			}
		}
		if (inWild() && Boundary.isIn(this, Boundary.SAFEPK)) {
			int modY = absY > 6400 ? absY - 6400 : absY;
			wildLevel = (((modY - 3520) / 8) + 1);
			if (Config.SINGLE_AND_MULTI_ZONES) {
				getPA().sendFrame126("@yel@Level: " + wildLevel, 199);
			} else {
				getPA().multiWay(-1);
				getPA().sendFrame126("@yel@Level: " + wildLevel, 199);
			}
			getPA().showOption(3, 0, "Attack", 1);
			if (Config.BOUNTY_HUNTER_ACTIVE && !inClanWars()) {
				getPA().walkableInterface(28000);
				getPA().sendFrame171(1, 28070);
				getPA().sendFrame171(0, 196);
			} else {
				getPA().walkableInterface(197);
			}
		} else if (inWild() && !inClanWars() && !Boundary.isIn(this, Boundary.SAFEPK)) {
			int modY = absY > 6400 ? absY - 6400 : absY;
			wildLevel = (((modY - 3520) / 8) + 1);
			if (Config.SINGLE_AND_MULTI_ZONES) {
				getPA().sendFrame126("@yel@Level: " + wildLevel, 199);
			} else {
				getPA().multiWay(-1);
				getPA().sendFrame126("@yel@Level: " + wildLevel, 199);
			}
			getPA().showOption(3, 0, "Attack", 1);
			if (Config.BOUNTY_HUNTER_ACTIVE && !inClanWars()) {
				getPA().walkableInterface(28000);
				getPA().sendFrame171(1, 28070);
				getPA().sendFrame171(0, 196);
			} else {
				getPA().walkableInterface(197);
			}

			// } else if (Boundary.isIn(this, Boundary.SKELETAL_MYSTICS)) {
			// getPA().walkableInterface(42300);
		} else if (inClanWars() && inWild()) {
			getPA().showOption(3, 0, "Attack", 1);
			getPA().walkableInterface(197);
			getPA().sendFrame126("@yel@3-126", 199);
			wildLevel = 126;
		} else if (Boundary.isIn(this, Boundary.SCORPIA_LAIR)) {
			getPA().sendFrame126("@yel@Level: 54", 199);
			// getPA().walkableInterface(197);
			wildLevel = 54;
		} else if (getItems().isWearingItem(10501, 3) && !inWild()) {
			getPA().showOption(3, 0, "Throw-At", 1);
		} else if (inEdgeville()) {
			if (Config.BOUNTY_HUNTER_ACTIVE) {
				if (bountyHunter.hasTarget()) {
					getPA().walkableInterface(28000);
					getPA().sendFrame171(0, 28070);
					getPA().sendFrame171(1, 196);
					bountyHunter.updateOutsideTimerUI();
				} else {
					getPA().walkableInterface(-1);
				}
			} else {
				getPA().sendFrame99(0);
				getPA().walkableInterface(-1);
				getPA().showOption(3, 0, "Null", 1);
			}
			getPA().showOption(3, 0, "null", 1);
		} else if (Boundary.isIn(this, PestControl.LOBBY_BOUNDARY)) {
			getPA().walkableInterface(21119);
			PestControl.drawInterface(this, "lobby");
		} else if (Boundary.isIn(this, PestControl.GAME_BOUNDARY)) {
			getPA().walkableInterface(21100);
			PestControl.drawInterface(this, "game");
		} else if ((inDuelArena() || Boundary.isIn(this, Boundary.DUEL_ARENA))) {
			getPA().walkableInterface(201);
			if (Boundary.isIn(this, Boundary.DUEL_ARENA)) {
				getPA().showOption(3, 0, "Attack", 1);
			} else {
				getPA().showOption(3, 0, "Challenge", 1);
			}
			wildLevel = 126;
		} else if (barrows.inBarrows()) {
			barrows.drawInterface();
			getPA().walkableInterface(27500);
		} else if (inGodwars()) {
			godwars.drawInterface();
			getPA().walkableInterface(16210);
		} else if (inCwGame || inPits) {
			getPA().showOption(3, 0, "Attack", 1);
		} else if (getPA().inPitsWait()) {
			getPA().showOption(3, 0, "Null", 1);
		} else if (Boundary.isIn(this, Boundary.SKOTIZO_BOSSROOM)) {
			getPA().walkableInterface(29230);
		} else {
			getPA().walkableInterface(-1);
			getPA().showOption(3, 0, "Null", 1);
		}
		if (Boundary.isIn(this, Barrows.TUNNEL)) {
			if (!Server.getEventHandler().isRunning(this, "barrows_tunnel")) {
				Server.getEventHandler().submit(new TunnelEvent("barrows_tunnel", this, 1));
			}
			getPA().sendFrame99(2);
		} else {
			if (Server.getEventHandler().isRunning(this, "barrows_tunnel")) {
				Server.getEventHandler().stop(this, "barrows_tunnel");
			}
			getPA().sendFrame99(0);
		}

		if (Boundary.isIn(this, Boundary.PURO_PURO)) {
			getPA().sendFrame99(2);
		}

		if (Boundary.isIn(this, Boundary.ICE_PATH)) {
			getPA().sendFrame99(2);
			if (getRunEnergy() > 0)
				setRunEnergy(0);
			if (heightLevel > 0)
				getPA().icePath();
		}

		if (!inWild()) {
			wildLevel = 0;
		}
		if(Boundary.isIn(this, Boundary.EDGEVILLE_PERIMETER) && !Boundary.isIn(this, Boundary.EDGE_BANK) && getHeight() == 8){
			wildLevel=126;
		}
		if (!hasMultiSign && inMulti()) {
			hasMultiSign = true;
			getPA().multiWay(1);
		}

		if (hasMultiSign && !inMulti()) {
			hasMultiSign = false;
			getPA().multiWay(-1);
		}
		if (!inMulti() && inWild())
			getPA().sendFrame70(30, 0, 196);
		else if (inMulti() && inWild())
			getPA().sendFrame70(0, 0, 196);
		if (this.skullTimer > 0) {
			--skullTimer;
			if (skullTimer == 1) {
				isSkulled = false;
				attackedPlayers.clear();
				headIconPk = -1;
				skullTimer = -1;
				getPA().requestUpdates();
			}
		}

		if (freezeTimer > -6) {
			freezeTimer--;
			if (frozenBy > 0) {
				if (PlayerHandler.players[frozenBy] == null) {
					freezeTimer = -1;
					frozenBy = -1;
				} else if (!goodDistance(absX, absY, PlayerHandler.players[frozenBy].absX,
						PlayerHandler.players[frozenBy].absY, 20)) {
					freezeTimer = -1;
					frozenBy = -1;
				}
			}
		}

		if (teleTimer > 0) {
			teleTimer--;
			if (!isDead) {
				if (teleTimer == 1) {
					teleTimer = 0;
				}
				if (teleTimer == 5) {
					teleTimer--;
					getPA().processTeleport();
				}
				if (teleTimer == 9 && teleGfx > 0) {
					teleTimer--;
					gfx100(teleGfx);
				}
			} else {
				teleTimer = 0;
			}
		}

		if (attackTimer > 0) {
			attackTimer--;
		}

		if (followId > 0) {
			getPA().followPlayer();
		} else if (followId2 > 0) {
			getPA().followNpc();
		}
		if (targeted != null) {
			if (distanceToPoint(targeted.getX(), targeted.getY()) > 10) {
				getPA().sendEntityTarget(0, targeted);
				targeted = null;
			}
		}
		if (attackTimer <= 1) {
			if (npcIndex > 0 && clickNpcType == 0) {
				getCombat().attackNpc(npcIndex);
			}
			if (playerIndex > 0) {
				getCombat().attackPlayer(playerIndex);
			}
		}
		if (underAttackBy <= 0 && underAttackBy2 <= 0 && !inMulti() && lastAttacked < System.currentTimeMillis() - 4000
				&& lastTargeted < System.currentTimeMillis() - 4000) {
			NPC closestNPC = null;
			int closestDistance = Integer.MAX_VALUE;
			if (!isIdle) {
				for (NPC npc : NPCHandler.npcs) {
					if (npc == null || !isTargetableBy(npc) || npc.killerId == index) {
						continue;
					}
					int distance = Misc.distanceToPoint(absX, absY, npc.absX, npc.absY);
					if (distance < closestDistance && distance <= Server.npcHandler.distanceRequired(npc.getIndex())
							+ Server.npcHandler.followDistance(npc.getIndex())) {
						closestDistance = distance;
						closestNPC = npc;
					}
				}
				if (closestNPC != null) {
					closestNPC.killerId = getIndex();
					underAttackBy = closestNPC.getIndex();
					underAttackBy2 = closestNPC.getIndex();
					lastTargeted = System.currentTimeMillis();
				}
			}
		}
	}

	public boolean isTargetableBy(NPC npc) {
		return !npc.isDead && Server.npcHandler.isAggressive(npc.getIndex(), false) && !npc.underAttack
				&& npc.killerId <= 0 && npc.getHeight() == heightLevel;
	}

	public Stream getInStream() {
		return inStream;
	}

	public int getPacketType() {
		return packetType;
	}

	public int getPacketSize() {
		return packetSize;
	}

	public Stream getOutStream() {
		return outStream;
	}

	public ItemAssistant getItems() {
		return itemAssistant;
	}

	public PlayerAssistant getPA() {
		return playerAssistant;
	}

	public DialogueHandler getDH() {
		return dialogueHandler;
	}

	public ChargeTrident getCT() {
		return chargeTrident;
	}

	public ShopAssistant getShops() {
		return shopAssistant;
	}

	public CombatAssistant getCombat() {
		return combat;
	}

	public ActionHandler getActions() {
		return actionHandler;
	}

	public Killstreak getStreak() {
		return killingStreak;
	}

	public Channel getSession() {
		return session;
	}

	public Potions getPotions() {
		return potions;
	}

	public PotionMixing getPotMixing() {
		return potionMixing;
	}

	public Food getFood() {
		return food;
	}

	public boolean checkBusy() {
		/*
		 * if (getCombat().isFighting()) { return true; }
		 */
		if (isBusy) {
			// actionAssistant.sendMessage("You are too busy to do that.");
		}
		return isBusy;
	}

	public boolean checkBusyHP() {
		return isBusyHP;
	}

	public boolean checkBusyFollow() {
		return isBusyFollow;
	}

	public void setBusy(boolean isBusy) {
		this.isBusy = isBusy;
	}

	public boolean isBusy() {
		return isBusy;
	}

	public void setBusyFollow(boolean isBusyFollow) {
		this.isBusyFollow = isBusyFollow;
	}

	public void setBusyHP(boolean isBusyHP) {
		this.isBusyHP = isBusyHP;
	}

	public boolean isBusyHP() {
		return isBusyHP;
	}

	public boolean isBusyFollow() {
		return isBusyFollow;
	}

	public PlayerAssistant getPlayerAssistant() {
		return playerAssistant;
	}

	public SkillInterfaces getSI() {
		return skillInterfaces;
	}

	public int getRuneEssencePouch(int index) {
		return runeEssencePouch[index];
	}

	public void setRuneEssencePouch(int index, int runeEssencePouch) {
		this.runeEssencePouch[index] = runeEssencePouch;
	}

	public int getPureEssencePouch(int index) {
		return pureEssencePouch[index];
	}

	public void setPureEssencePouch(int index, int pureEssencePouch) {
		this.pureEssencePouch[index] = pureEssencePouch;
	}

	public Slayer getSlayer() {
		if (slayer == null) {
			slayer = new Slayer(this);
		}
		return slayer;
	}

	public Runecrafting getRunecrafting() {
		return runecrafting;
	}

	public Cooking getCooking() {
		return cooking;
	}

	public Agility getAgility() {
		return agility;
	}

	public Crafting getCrafting() {
		return crafting;
	}

	public Thieving getThieving() {
		return thieving;
	}

	public Herblore getHerblore() {
		return herblore;
	}

	public Barrows getBarrows() {
		return barrows;
	}

	public Godwars getGodwars() {
		return godwars;
	}

	public TreasureTrails getTrails() {
		return trails;
	}

	public GnomeAgility getGnomeAgility() {
		return gnomeAgility;
	}

	public PointItems getPoints() {
		return pointItems;
	}

	public PlayerAction getPlayerAction() {
		return playerAction;
	}

	public WildernessAgility getWildernessAgility() {
		return wildernessAgility;
	}

	public Shortcuts getAgilityShortcuts() {
		return shortcuts;
	}

	public RooftopSeers getRoofTopSeers() {
		return rooftopSeers;
	}

	public RooftopFalador getRoofTopFalador() {
		return rooftopFalador;
	}

	public RooftopVarrock getRoofTopVarrock() {
		return rooftopVarrock;
	}

	public RooftopArdougne getRoofTopArdougne() {
		return rooftopArdougne;
	}

	public Lighthouse getLighthouse() {
		return lighthouse;
	}

	public BarbarianAgility getBarbarianAgility() {
		return barbarianAgility;
	}

	public AgilityHandler getAgilityHandler() {
		return agilityHandler;
	}

	public Smithing getSmithing() {
		return smith;
	}

	public FightCave getFightCave() {
		if (fightcave == null)
			fightcave = new FightCave(this);
		return fightcave;
	}

	public DagannothMother getDagannothMother() {
		return dagannothMother;
	}

	public DemonicGorilla getDemonicGorilla() {
		return demonicGorilla;
	}

	public RecipeForDisaster getrecipeForDisaster() {
		return recipeForDisaster;
	}

	public Cerberus getCerberus() {
		return cerberus;
	}

	public Raids getRaids() {
		return raid;
	}

	public Tzkalzuk getInferno() {
		return tzkalzuk;
	}

	public Skotizo getSkotizo() {
		return skotizo;
	}

	public InstanceSoloFight getSoloFight() {
		return soloFight;
	}

	public DagannothMother createDagannothMotherInstance() {
		Boundary boundary = Boundary.LIGHTHOUSE;

		int height = InstancedAreaManager.getSingleton().getNextOpenHeight(boundary);

		dagannothMother = new DagannothMother(this, boundary, height);

		return dagannothMother;
	}

	public RecipeForDisaster createRecipeForDisasterInstance() {
		Boundary boundary = Boundary.RFD;

		int height = InstancedAreaManager.getSingleton().getNextOpenHeightCust(boundary, 2);

		recipeForDisaster = new RecipeForDisaster(this, boundary, height);

		return recipeForDisaster;
	}

	public Cerberus createCerberusInstance() {
		Boundary boundary = Boundary.BOSS_ROOM_WEST;

		int height = InstancedAreaManager.getSingleton().getNextOpenHeightCust(boundary, 4);

		cerberus = new Cerberus(this, boundary, height);

		return cerberus;
	}

	public Tzkalzuk createTzkalzukInstance() {
		Boundary boundary = Boundary.INFERNO;

		int height = InstancedAreaManager.getSingleton().getNextOpenHeightCust(boundary, 4);

		tzkalzuk = new Tzkalzuk(this, boundary, height);

		return tzkalzuk;
	}

	public Skotizo createSkotizoInstance() {
		Boundary boundary = Boundary.SKOTIZO_BOSSROOM;

		int height = InstancedAreaManager.getSingleton().getNextOpenHeightCust(boundary, 4);

		skotizo = new Skotizo(this, boundary, height);

		return skotizo;
	}

	public InstanceSoloFight createSoloFight() {
		Boundary boundary = Boundary.FIGHT_ROOM;

		int height = InstancedAreaManager.getSingleton().getNextOpenHeightCust(boundary, 4);

		soloFight = new InstanceSoloFight(this, boundary, height);

		return soloFight;
	}

	public SmithingInterface getSmithingInt() {
		return smithInt;
	}

	public int getPrestigePoints() {
		return prestigePoints;
	}
	/*
	 * public Fletching getFletching() { return fletching; }
	 */

	public Prayer getPrayer() {
		return prayer;
	}

	/**
	 * End of Skill Constructors
	 */

	public void queueMessage(Packet arg1) {
		packetsReceived++;
		queuedPackets.add(arg1);
	}

	public boolean processQueuedPackets() {
		Packet p = null;
		int processed = 0;
		packetsReceived = 0;
		while ((p = queuedPackets.poll()) != null) {
			if (processed > Config.MAX_INCOMING_PACKETS_PER_CYCLE) {
				break;
			}
			inStream.currentOffset = 0;
			packetType = p.getOpcode();
			packetSize = p.getLength();
			inStream.buffer = p.getPayload().array();
			if (packetType > 0) {
				PacketHandler.processPacket(this, packetType, packetSize);
				processed++;
			}
		}
		return true;
	}

	public void correctCoordinates() {
		final Boundary pc = PestControl.GAME_BOUNDARY;
		final Boundary fc = Boundary.FIGHT_CAVE;
		final Boundary zulrah = Zulrah.BOUNDARY;
		int x = teleportToX;
		int y = teleportToY;
		if (x > pc.getMinimumX() && x < pc.getMaximumX() && y > pc.getMinimumY() && y < pc.getMaximumY()) {
			teleportToX = 2657;
			teleportToY = 2639;
			heightLevel = 0;
		}
		if (x > fc.getMinimumX() && x < fc.getMaximumX() && y > fc.getMinimumY() && y < fc.getMaximumY()) {
			heightLevel = getIndex() * 4;
			sendMessage("Wave " + (this.waveId + 1) + " will start in approximately 5-10 seconds. ");
			getFightCave().spawn();
		}
		if (x > zulrah.getMinimumX() && x < zulrah.getMaximumX() && y > zulrah.getMinimumY()
				&& y < zulrah.getMaximumY()) {
			teleportToX = Config.EDGEVILLE_X;
			teleportToY = Config.EDGEVILLE_Y;
			heightLevel = 0;
		}
	}




	public void updateRank() {
		if (amDonated <= 0) {
			amDonated = 0;
		}
		if (amDonated >= 10 && amDonated < 50) {
			if (getRights().isOrInherits(Right.IRONMAN) || getRights().isOrInherits(Right.ULTIMATE_IRONMAN) || getRights().isOrInherits(Right.OSRS) || getRights().isOrInherits(Right.HELPER) || getRights().isOrInherits(Right.MODERATOR)) {
				getRights().add(Right.CONTRIBUTOR);
				sendMessage("Your hidden donator rank is now active.");
			} else {
				getRights().setPrimary(Right.CONTRIBUTOR);
				sendMessage("Please relog to receive your donator rank.");
			}
		}
		if (amDonated >= 50 && amDonated < 150) {
			if (getRights().isOrInherits(Right.IRONMAN) || getRights().isOrInherits(Right.ULTIMATE_IRONMAN) || getRights().isOrInherits(Right.OSRS) || getRights().isOrInherits(Right.HELPER) || getRights().isOrInherits(Right.MODERATOR)) {
				getRights().add(Right.SPONSOR);
				sendMessage("Your hidden super donator rank is now active.");
			} else {
				getRights().setPrimary(Right.SPONSOR);
				sendMessage("Please relog to receive your super donator rank.");
			}
		}
		if (amDonated >= 150 && amDonated < 300) {
			if (getRights().isOrInherits(Right.IRONMAN) || getRights().isOrInherits(Right.ULTIMATE_IRONMAN) || getRights().isOrInherits(Right.OSRS) || getRights().isOrInherits(Right.HELPER) || getRights().isOrInherits(Right.MODERATOR)) {
				getRights().add(Right.SUPPORTER);
				sendMessage("Your hidden extreme donator rank is now active.");
			} else {
				getRights().setPrimary(Right.SUPPORTER);
				sendMessage("Please relog to receive your extreme donator rank.");
			}
		}
		if (amDonated >= 300 && amDonated < 500) {
			if (getRights().isOrInherits(Right.IRONMAN) || getRights().isOrInherits(Right.ULTIMATE_IRONMAN) || getRights().isOrInherits(Right.OSRS) || getRights().isOrInherits(Right.HELPER) || getRights().isOrInherits(Right.MODERATOR)) {
				getRights().add(Right.DONATOR);
				sendMessage("Your hidden legendary donator rank is now active.");
			} else {
				getRights().setPrimary(Right.DONATOR);
				sendMessage("Please relog to receive your legendary donator rank.");
			}
		}
		if (amDonated >= 500 && amDonated < 1000) {
			if (getRights().isOrInherits(Right.IRONMAN) || getRights().isOrInherits(Right.ULTIMATE_IRONMAN) || getRights().isOrInherits(Right.OSRS) || getRights().isOrInherits(Right.HELPER) || getRights().isOrInherits(Right.MODERATOR)) {
				getRights().add(Right.SUPER_DONATOR);
				sendMessage("Your hidden legendary donator rank is now active.");
			} else {
				getRights().setPrimary(Right.SUPER_DONATOR);
				sendMessage("Please relog to receive your legendary donator rank.");
			}
		}
		if (amDonated >= 1000 && amDonated < 2500) {
			if (getRights().isOrInherits(Right.IRONMAN) || getRights().isOrInherits(Right.ULTIMATE_IRONMAN) || getRights().isOrInherits(Right.OSRS) || getRights().isOrInherits(Right.HELPER) || getRights().isOrInherits(Right.MODERATOR)) {
				getRights().add(Right.EXTREME_DONATOR);
				sendMessage("Your hidden legendary donator rank is now active.");
			} else {
				getRights().setPrimary(Right.EXTREME_DONATOR);
				sendMessage("Please relog to receive your legendary donator rank.");
			}
		}
		if (amDonated >= 2500) {
			if (getRights().isOrInherits(Right.IRONMAN) || getRights().isOrInherits(Right.ULTIMATE_IRONMAN) || getRights().isOrInherits(Right.OSRS) || getRights().isOrInherits(Right.HELPER) || getRights().isOrInherits(Right.MODERATOR)) {
				getRights().add(Right.LEGENDARY);
				sendMessage("Your hidden legendary donator rank is now active.");
			} else {
				getRights().setPrimary(Right.LEGENDARY);
				sendMessage("Please relog to receive your legendary donator rank.");
			}
		}
		sendMessage("Your updated total amount donated is now $" + amDonated + ".");
	}

	public int getPrivateChat() {
		return privateChat;
	}

	public Friends getFriends() {
		return friend;
	}

	public Ignores getIgnores() {
		return ignores;
	}

	public void setPrivateChat(int option) {
		this.privateChat = option;
	}

	public Trade getTrade() {
		return trade;
	}

	public int localX() {
		return this.getX() - this.getMapRegionX() * 8;
	}

	public int localY() {
		return this.getY() - this.getMapRegionY() * 8;
	}

	public AchievementHandler getAchievements() {
		if (achievementHandler == null)
			achievementHandler = new AchievementHandler(this);
		return achievementHandler;
	}

	public HolidayStages getHolidayStages() {
		if (holidayStages == null) {
			holidayStages = new HolidayStages();
		}
		return holidayStages;
	}

	public long getLastContainerSearch() {
		return lastContainerSearch;
	}

	public void setLastContainerSearch(long lastContainerSearch) {
		this.lastContainerSearch = lastContainerSearch;
	}

	public MysteryBox getMysteryBox() {
		return mysteryBox;
	}

	public HourlyRewardBox getHourlyRewardBox() {
		return hourlyRewardBox;
	}

	public PvmCasket getPvmCasket() {
		return pvmCasket;
	}

	public SkillCasket getSkillCasket() {
		return skillCasket;
	}

	public WildyCrate getWildyCrate() {
		return wildyCrate;
	}

	public DailyGearBox getDailyGearBox() {
		return dailyGearBox;
	}

	public DailySkillBox getDailySkillBox() {
		return dailySkillBox;
	}

	public ChristmasPresent getChristmasPresent() {
		return christmasPresent;
	}

	public DamageQueueEvent getDamageQueue() {
		return damageQueue;
	}

	public final int[] BOWS = { 19481, 19478, 12788, 9185, 11785, 21012, 839, 845, 847, 851, 855, 859, 841, 843, 849,
			853, 857, 12424, 861, 4212, 4214, 4215, 12765, 12766, 12767, 12768, 11235, 4216, 4217, 4218, 4219, 4220,
			4221, 4222, 4223, 4734, 6724, 20997 };
	public final int[] ARROWS = { 9341, 4160, 11959, 10033, 10034, 882, 883, 884, 885, 886, 887, 888, 889, 890, 891,
			892, 893, 4740, 5616, 5617, 5618, 5619, 5620, 5621, 5622, 5623, 5624, 5625, 5626, 5627, 9139, 9140, 9141,
			9142, 9143, 11875, 21316, 21326, 9144, 9145, 9240, 9241, 9242, 9243, 9244, 9245, 9286, 9287, 9288, 9289,
			9290, 9291, 9292, 9293, 9294, 9295, 9296, 9297, 9298, 9299, 9300, 9301, 9302, 9303, 9304, 9305, 9306, 11212,
			11227, 11228, 11229 };
	public final int[] CRYSTAL_BOWS = { 4212, 4214, 4215, 4216, 4217, 4218, 4219, 4220, 4221, 4222, 4223 };
	public final int[] NO_ARROW_DROP = { 11959, 10033, 10034, 4212, 4214, 4215, 4216, 4217, 4218, 4219, 4220, 4221,
			4222, 4223, 4734, 4934, 4935, 4936, 4937 };
	public final int[] OTHER_RANGE_WEAPONS = { 11959, 10033, 10034, 800, 801, 802, 803, 804, 805, 20849, 806, 807, 808,
			809, 810, 811, 812, 813, 814, 815, 816, 817, 825, 826, 827, 828, 829, 830, 831, 832, 833, 834, 835, 836,
			863, 864, 865, 866, 867, 868, 869, 870, 871, 872, 873, 874, 875, 876, 4934, 4935, 4936, 4937, 5628, 5629,
			5630, 5632, 5633, 5634, 5635, 5636, 5637, 5639, 5640, 5641, 5642, 5643, 5644, 5645, 5646, 5647, 5648, 5649,
			5650, 5651, 5652, 5653, 5654, 5655, 5656, 5657, 5658, 5659, 5660, 5661, 5662, 5663, 5664, 5665, 5666, 5667,
			6522, 11230 };
	public int compostBin = 0;
	public int reduceSpellId;
	public final int[] REDUCE_SPELL_TIME = { 250000, 250000, 250000, 500000, 500000, 500000 };
	public long[] reduceSpellDelay = new long[6];
	public final int[] REDUCE_SPELLS = { 1153, 1157, 1161, 1542, 1543, 1562 };
	public boolean[] canUseReducingSpell = { true, true, true, true, true, true };
	public boolean usingPrayer;
	public final int[] PRAYER_DRAIN_RATE = { 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500,
			500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500 };
	public final int[] PRAYER_LEVEL_REQUIRED = { 1, 4, 7, 8, 9, 10, 13, 16, 19, 22, 25, 26, 27, 28, 31, 34, 37, 40, 43,
			44, 45, 46, 49, 52, 55, 60, 70, 74, 77 };
	public final int[] PRAYER = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23,
			24, 25, 26, 27, 28 };
	public final String[] PRAYER_NAME = { "Thick Skin", "Burst of Strength", "Clarity of Thought", "Sharp Eye",
			"Mystic Will", "Rock Skin", "Superhuman Strength", "Improved Reflexes", "Rapid Restore", "Rapid Heal",
			"Protect Item", "Hawk Eye", "Mystic Lore", "Steel Skin", "Ultimate Strength", "Incredible Reflexes",
			"Protect from Magic", "Protect from Missiles", "Protect from Melee", "Eagle Eye", "Mystic Might",
			"Retribution", "Redemption", "Smite", "Preserve", "Chivalry", "Piety", "Rigour", "Augury" };
	public final int[] PRAYER_GLOW = { 83, 84, 85, 700, 701, 86, 87, 88, 89, 90, 91, 702, 703, 92, 93, 94, 95, 96, 97,
			704, 705, 98, 99, 100, 708, 706, 707, 710, 712 };
	public boolean isSelectingQuickprayers = false;
	public final int[] PRAYER_HEAD_ICONS = { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 2, 1, 0,
			-1, -1, 3, 5, 4, -1, -1, -1, -1, -1 };
	public boolean[] prayerActive = { false, false, false, false, false, false, false, false, false, false, false,
			false, false, false, false, false, false, false, false, false, false, false, false, false, false, false,
			false, false, false };

	// Used by farming processor to not update the object every click
	// Created an array of booleans based on the patch number, not using an array
	// for each patch creates graphic glitches. - Tyler
	public boolean[] farmingLagReducer = new boolean[Farming.MAX_PATCHES];
	public boolean[] farmingLagReducer2 = new boolean[Farming.MAX_PATCHES];
	public boolean[] farmingLagReducer3 = new boolean[Farming.MAX_PATCHES];
	public boolean[] farmingLagReducer4 = new boolean[Farming.MAX_PATCHES];

	public Farming getFarming() {
		return farming;
	}

	public int getFarmingSeedId(int index) {
		return farmingSeedId[index];
	}

	public void setFarmingSeedId(int index, int farmingSeedId) {
		this.farmingSeedId[index] = farmingSeedId;
	}

	public int getFarmingTime(int index) {
		return this.farmingTime[index];
	}

	public int getOriginalFarmingTime(int index) { // originalFarming
		return this.originalFarmingTime[index];
	}

	public void setFarmingTime(int index, int farmingTime) {
		this.farmingTime[index] = farmingTime;
	}

	public void setOriginalFarmingTime(int index, int originalFarmingTime) {// originalFarmingTime
		this.originalFarmingTime[index] = originalFarmingTime;
	}

	public int getFarmingState(int index) {
		return farmingState[index];
	}

	public void setFarmingState(int index, int farmingState) {
		this.farmingState[index] = farmingState;
	}

	public int getFarmingHarvest(int index) {
		return farmingHarvest[index];
	}

	public void setFarmingHarvest(int index, int farmingHarvest) {
		this.farmingHarvest[index] = farmingHarvest;
	}

	/**
	 * Retrieves the bounty hunter instance for this client object. We use lazy
	 * initialization because we store values from the player save file in the
	 * bountyHunter object upon login. Without lazy initialization the value would
	 * be overwritten.
	 * 
	 * @return the bounty hunter object
	 */
	public BountyHunter getBH() {
		if (Objects.isNull(bountyHunter)) {
			bountyHunter = new BountyHunter(this);
		}
		return bountyHunter;
	}

	public UnnecessaryPacketDropper getPacketDropper() {
		return packetDropper;
	}

	public Optional<ItemCombination> getCurrentCombination() {
		return currentCombination;
	}

	public void setCurrentCombination(Optional<ItemCombination> combination) {
		this.currentCombination = combination;
	}

	public PlayerKill getPlayerKills() {
		if (Objects.isNull(playerKills)) {
			playerKills = new PlayerKill();
		}
		return playerKills;
	}

	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	public String getIpAddress() {
		return connectedFrom;
	}

	public void setIpAddress(String ipAddress) {
		this.connectedFrom = ipAddress;
	}

	public int getMaximumHealth() {
		int base = getLevelForXP(playerXP[3]);
		if (EquipmentSet.GUTHAN.isWearingBarrows(this) && getItems().isWearingItem(12853)) {
			base += 10;
		}
		return base;
	}

	public int getMaximumPrayer() {
		return getLevelForXP(playerXP[playerPrayer]);
	}

	public Duel getDuel() {
		return duelSession;
	}

	public void setItemOnPlayer(Player player) {
		this.itemOnPlayer = player;
	}

	public Player getItemOnPlayer() {
		return itemOnPlayer;
	}

	public Skilling getSkilling() {
		return skilling;
	}

	public Presets getPresets() {
		if (presets == null) {
			presets = new Presets(this);
		}
		return presets;
	}

	public Killstreak getKillstreak() {
		if (killstreaks == null) {
			killstreaks = new Killstreak(this);
		}
		return killstreaks;
	}

	/**
	 * Returns the single instance of the {@link NPCDeathTracker} class for this
	 * player.
	 * 
	 * @return the tracker clas
	 */
	public NPCDeathTracker getNpcDeathTracker() {
		return npcDeathTracker;
	}

	/**
	 * The zulrah event
	 * 
	 * @return event
	 */
	public Zulrah getZulrahEvent() {
		return zulrah;
	}

	/**
	 * The single {@link WarriorsGuild} instance for this player
	 * 
	 * @return warriors guild
	 */
	public WarriorsGuild getWarriorsGuild() {
		return warriorsGuild;
	}

	/**
	 * The single instance of the {@link PestControlRewards} class for this player
	 * 
	 * @return the reward class
	 */
	public PestControlRewards getPestControlRewards() {
		return pestControlRewards;
	}

	public Mining getMining() {
		return mining;
	}

	public PunishmentPanel getPunishmentPanel() {
		return punishmentPanel;
	}

	public void faceNPC(int index) {
		faceNPC = index;
		faceNPCupdate = true;
		updateRequired = true;
	}

	public void appendFaceNPCUpdate(Stream str) {
		str.writeWordBigEndian(faceNPC);
	}

	public void ResetKeepItems() {
		WillKeepAmt1 = -1;
		WillKeepItem1 = -1;
		WillKeepAmt2 = -1;
		WillKeepItem2 = -1;
		WillKeepAmt3 = -1;
		WillKeepItem3 = -1;
		WillKeepAmt4 = -1;
		WillKeepItem4 = -1;
	}

	public void StartBestItemScan(Player c) {
		if (c.isSkulled && !c.prayerActive[10]) {
			ItemKeptInfo(c, 0);
			return;
		}
		FindItemKeptInfo(c);
		ResetKeepItems();
		BestItem1(c);
	}

	public void FindItemKeptInfo(Player c) {
		if (isSkulled && c.prayerActive[10])
			ItemKeptInfo(c, 1);
		else if (!isSkulled && !c.prayerActive[10])
			ItemKeptInfo(c, 3);
		else if (!isSkulled && c.prayerActive[10])
			ItemKeptInfo(c, 4);
	}

	public void ItemKeptInfo(Player c, int Lose) {
		for (int i = 17109; i < 17131; i++) {
			c.getPA().sendFrame126("", i);
		}
		c.getPA().sendFrame126("Items you will keep on death:", 17104);
		c.getPA().sendFrame126("Items you will lose on death:", 17105);
		c.getPA().sendFrame126("Player Information", 17106);
		c.getPA().sendFrame126("Max items kept on death:", 17107);
		c.getPA().sendFrame126("~ " + Lose + " ~", 17108);
		c.getPA().sendFrame126("The normal amount of", 17111);
		c.getPA().sendFrame126("items kept is three.", 17112);
		switch (Lose) {
		case 0:
		default:
			c.getPA().sendFrame126("Items you will keep on death:", 17104);
			c.getPA().sendFrame126("Items you will lose on death:", 17105);
			c.getPA().sendFrame126("You're marked with a", 17111);
			c.getPA().sendFrame126("@red@skull. @lre@This reduces the", 17112);
			c.getPA().sendFrame126("items you keep from", 17113);
			c.getPA().sendFrame126("three to zero!", 17114);
			break;
		case 1:
			c.getPA().sendFrame126("Items you will keep on death:", 17104);
			c.getPA().sendFrame126("Items you will lose on death:", 17105);
			c.getPA().sendFrame126("You're marked with a", 17111);
			c.getPA().sendFrame126("@red@skull. @lre@This reduces the", 17112);
			c.getPA().sendFrame126("items you keep from", 17113);
			c.getPA().sendFrame126("three to zero!", 17114);
			c.getPA().sendFrame126("However, you also have", 17115);
			c.getPA().sendFrame126("the @red@Protect @lre@Items prayer", 17116);
			c.getPA().sendFrame126("active, which saves you", 17117);
			c.getPA().sendFrame126("one extra item!", 17118);
			break;
		case 3:
			c.getPA().sendFrame126("Items you will keep on death(if not skulled):", 17104);
			c.getPA().sendFrame126("Items you will lose on death(if not skulled):", 17105);
			c.getPA().sendFrame126("You have no factors", 17111);
			c.getPA().sendFrame126("affecting the items you", 17112);
			c.getPA().sendFrame126("keep.", 17113);
			break;
		case 4:
			c.getPA().sendFrame126("Items you will keep on death(if not skulled):", 17104);
			c.getPA().sendFrame126("Items you will lose on death(if not skulled):", 17105);
			c.getPA().sendFrame126("You have the @red@Protect", 17111);
			c.getPA().sendFrame126("@red@Item @lre@prayer active,", 17112);
			c.getPA().sendFrame126("which saves you one", 17113);
			c.getPA().sendFrame126("extra item!", 17114);
			break;
		}
	}

	public void BestItem1(Player c) {
		int BestValue = 0;
		int NextValue = 0;
		int ItemsContained = 0;
		WillKeepItem1 = 0;
		WillKeepItem1Slot = 0;
		for (int ITEM = 0; ITEM < 28; ITEM++) {
			if (playerItems[ITEM] > 0) {
				ItemsContained += 1;
				NextValue = (int) Math.floor(ShopAssistant.getItemShopValue(playerItems[ITEM] - 1));
				if (NextValue > BestValue) {
					BestValue = NextValue;
					WillKeepItem1 = playerItems[ITEM] - 1;
					WillKeepItem1Slot = ITEM;
					if (playerItemsN[ITEM] > 2 && !c.prayerActive[10]) {
						WillKeepAmt1 = 3;
					} else if (playerItemsN[ITEM] > 3 && c.prayerActive[10]) {
						WillKeepAmt1 = 4;
					} else {
						WillKeepAmt1 = playerItemsN[ITEM];
					}
				}
			}
		}
		for (int EQUIP = 0; EQUIP < 14; EQUIP++) {
			if (playerEquipment[EQUIP] > 0) {
				ItemsContained += 1;
				NextValue = (int) Math.floor(ShopAssistant.getItemShopValue(playerEquipment[EQUIP]));
				if (NextValue > BestValue) {
					BestValue = NextValue;
					WillKeepItem1 = playerEquipment[EQUIP];
					WillKeepItem1Slot = EQUIP + 28;
					if (playerEquipmentN[EQUIP] > 2 && !c.prayerActive[10]) {
						WillKeepAmt1 = 3;
					} else if (playerEquipmentN[EQUIP] > 3 && c.prayerActive[10]) {
						WillKeepAmt1 = 4;
					} else {
						WillKeepAmt1 = playerEquipmentN[EQUIP];
					}
				}
			}
		}
		if (!isSkulled && ItemsContained > 1 && (WillKeepAmt1 < 3 || (c.prayerActive[10] && WillKeepAmt1 < 4))) {
			BestItem2(c, ItemsContained);
		}
	}

	public void BestItem2(Player c, int ItemsContained) {
		int BestValue = 0;
		int NextValue = 0;
		WillKeepItem2 = 0;
		WillKeepItem2Slot = 0;
		for (int ITEM = 0; ITEM < 28; ITEM++) {
			if (playerItems[ITEM] > 0) {
				NextValue = (int) Math.floor(ShopAssistant.getItemShopValue(playerItems[ITEM] - 1));
				if (NextValue > BestValue && !(ITEM == WillKeepItem1Slot && playerItems[ITEM] - 1 == WillKeepItem1)) {
					BestValue = NextValue;
					WillKeepItem2 = playerItems[ITEM] - 1;
					WillKeepItem2Slot = ITEM;
					if (playerItemsN[ITEM] > 2 - WillKeepAmt1 && !c.prayerActive[10]) {
						WillKeepAmt2 = 3 - WillKeepAmt1;
					} else if (playerItemsN[ITEM] > 3 - WillKeepAmt1 && c.prayerActive[10]) {
						WillKeepAmt2 = 4 - WillKeepAmt1;
					} else {
						WillKeepAmt2 = playerItemsN[ITEM];
					}
				}
			}
		}
		for (int EQUIP = 0; EQUIP < 14; EQUIP++) {
			if (playerEquipment[EQUIP] > 0) {
				NextValue = (int) Math.floor(ShopAssistant.getItemShopValue(playerEquipment[EQUIP]));
				if (NextValue > BestValue
						&& !(EQUIP + 28 == WillKeepItem1Slot && playerEquipment[EQUIP] == WillKeepItem1)) {
					BestValue = NextValue;
					WillKeepItem2 = playerEquipment[EQUIP];
					WillKeepItem2Slot = EQUIP + 28;
					if (playerEquipmentN[EQUIP] > 2 - WillKeepAmt1 && !c.prayerActive[10]) {
						WillKeepAmt2 = 3 - WillKeepAmt1;
					} else if (playerEquipmentN[EQUIP] > 3 - WillKeepAmt1 && c.prayerActive[10]) {
						WillKeepAmt2 = 4 - WillKeepAmt1;
					} else {
						WillKeepAmt2 = playerEquipmentN[EQUIP];
					}
				}
			}
		}
		if (!isSkulled && ItemsContained > 2
				&& (WillKeepAmt1 + WillKeepAmt2 < 3 || (c.prayerActive[10] && WillKeepAmt1 + WillKeepAmt2 < 4))) {
			BestItem3(c, ItemsContained);
		}
	}

	public void BestItem3(Player c, int ItemsContained) {
		int BestValue = 0;
		int NextValue = 0;
		WillKeepItem3 = 0;
		WillKeepItem3Slot = 0;
		for (int ITEM = 0; ITEM < 28; ITEM++) {
			if (playerItems[ITEM] > 0) {
				NextValue = (int) Math.floor(ShopAssistant.getItemShopValue(playerItems[ITEM] - 1));
				if (NextValue > BestValue && !(ITEM == WillKeepItem1Slot && playerItems[ITEM] - 1 == WillKeepItem1)
						&& !(ITEM == WillKeepItem2Slot && playerItems[ITEM] - 1 == WillKeepItem2)) {
					BestValue = NextValue;
					WillKeepItem3 = playerItems[ITEM] - 1;
					WillKeepItem3Slot = ITEM;
					if (playerItemsN[ITEM] > 2 - (WillKeepAmt1 + WillKeepAmt2) && !c.prayerActive[10]) {
						WillKeepAmt3 = 3 - (WillKeepAmt1 + WillKeepAmt2);
					} else if (playerItemsN[ITEM] > 3 - (WillKeepAmt1 + WillKeepAmt2) && c.prayerActive[10]) {
						WillKeepAmt3 = 4 - (WillKeepAmt1 + WillKeepAmt2);
					} else {
						WillKeepAmt3 = playerItemsN[ITEM];
					}
				}
			}
		}
		for (int EQUIP = 0; EQUIP < 14; EQUIP++) {
			if (playerEquipment[EQUIP] > 0) {
				NextValue = (int) Math.floor(ShopAssistant.getItemShopValue(playerEquipment[EQUIP]));
				if (NextValue > BestValue
						&& !(EQUIP + 28 == WillKeepItem1Slot && playerEquipment[EQUIP] == WillKeepItem1)
						&& !(EQUIP + 28 == WillKeepItem2Slot && playerEquipment[EQUIP] == WillKeepItem2)) {
					BestValue = NextValue;
					WillKeepItem3 = playerEquipment[EQUIP];
					WillKeepItem3Slot = EQUIP + 28;
					if (playerEquipmentN[EQUIP] > 2 - (WillKeepAmt1 + WillKeepAmt2) && !c.prayerActive[10]) {
						WillKeepAmt3 = 3 - (WillKeepAmt1 + WillKeepAmt2);
					} else if (playerEquipmentN[EQUIP] > 3 - WillKeepAmt1 && c.prayerActive[10]) {
						WillKeepAmt3 = 4 - (WillKeepAmt1 + WillKeepAmt2);
					} else {
						WillKeepAmt3 = playerEquipmentN[EQUIP];
					}
				}
			}
		}
		if (!isSkulled && ItemsContained > 3 && c.prayerActive[10]
				&& ((WillKeepAmt1 + WillKeepAmt2 + WillKeepAmt3) < 4)) {
			BestItem4(c);
		}
	}

	public void BestItem4(Player c) {
		int BestValue = 0;
		int NextValue = 0;
		WillKeepItem4 = 0;
		WillKeepItem4Slot = 0;
		for (int ITEM = 0; ITEM < 28; ITEM++) {
			if (playerItems[ITEM] > 0) {
				NextValue = (int) Math.floor(ShopAssistant.getItemShopValue(playerItems[ITEM] - 1));
				if (NextValue > BestValue && !(ITEM == WillKeepItem1Slot && playerItems[ITEM] - 1 == WillKeepItem1)
						&& !(ITEM == WillKeepItem2Slot && playerItems[ITEM] - 1 == WillKeepItem2)
						&& !(ITEM == WillKeepItem3Slot && playerItems[ITEM] - 1 == WillKeepItem3)) {
					BestValue = NextValue;
					WillKeepItem4 = playerItems[ITEM] - 1;
					WillKeepItem4Slot = ITEM;
				}
			}
		}
		for (int EQUIP = 0; EQUIP < 14; EQUIP++) {
			if (playerEquipment[EQUIP] > 0) {
				NextValue = (int) Math.floor(ShopAssistant.getItemShopValue(playerEquipment[EQUIP]));
				if (NextValue > BestValue
						&& !(EQUIP + 28 == WillKeepItem1Slot && playerEquipment[EQUIP] == WillKeepItem1)
						&& !(EQUIP + 28 == WillKeepItem2Slot && playerEquipment[EQUIP] == WillKeepItem2)
						&& !(EQUIP + 28 == WillKeepItem3Slot && playerEquipment[EQUIP] == WillKeepItem3)) {
					BestValue = NextValue;
					WillKeepItem4 = playerEquipment[EQUIP];
					WillKeepItem4Slot = EQUIP + 28;
				}
			}
		}
	}

	/**
	 * A method for updating the items a player keeps on death
	 */
	public void updateItemsOnDeath() {
		if (!isSkulled) { // what items to keep
			itemAssistant.keepItem(0, true);
			itemAssistant.keepItem(1, true);
			itemAssistant.keepItem(2, true);
		}
		if (prayerActive[10] && System.currentTimeMillis() - lastProtItem > 700) {
			itemAssistant.keepItem(3, true);
		}
	}

	/**
	 * Determines if the player should keep the item on death
	 * 
	 * @param itemId
	 *            the item to be kept
	 * @return true if the player keeps the item on death, otherwise false
	 */
	public boolean keepsItemOnDeath(int itemId) {
		return WillKeepItem1 == itemId || WillKeepItem2 == itemId || WillKeepItem3 == itemId || WillKeepItem4 == itemId;
	}

	public boolean isAutoButton(int button) {
		for (int j = 0; j < MagicData.autocastIds.length; j += 2) {
			if (MagicData.autocastIds[j] == button)
				return true;
		}
		return false;
	}

	public void assignAutocast(int button) {
		for (int j = 0; j < MagicData.autocastIds.length; j++) {
			if (MagicData.autocastIds[j] == button) {
				Player c = PlayerHandler.players[this.getIndex()];
				autocasting = true;
				autocastId = MagicData.autocastIds[j + 1];
				c.getPA().sendFrame36(108, 1);
				c.setSidebarInterface(0, 328);
				c = null;
				break;
			}
		}
	}

	public int getLocalX() {
		return getX() - 8 * getMapRegionX();
	}

	public int getLocalY() {
		return getY() - 8 * getMapRegionY();
	}

	public String getSpellName(int id) {
		switch (id) {
		case 0:
			return "Air Strike";
		case 1:
			return "Water Strike";
		case 2:
			return "Earth Strike";
		case 3:
			return "Fire Strike";
		case 4:
			return "Air Bolt";
		case 5:
			return "Water Bolt";
		case 6:
			return "Earth Bolt";
		case 7:
			return "Fire Bolt";
		case 8:
			return "Air Blast";
		case 9:
			return "Water Blast";
		case 10:
			return "Earth Blast";
		case 11:
			return "Fire Blast";
		case 12:
			return "Air Wave";
		case 13:
			return "Water Wave";
		case 14:
			return "Earth Wave";
		case 15:
			return "Fire Wave";
		case 32:
			return "Shadow Rush";
		case 33:
			return "Smoke Rush";
		case 34:
			return "Blood Rush";
		case 35:
			return "Ice Rush";
		case 36:
			return "Shadow Burst";
		case 37:
			return "Smoke Burst";
		case 38:
			return "Blood Burst";
		case 39:
			return "Ice Burst";
		case 40:
			return "Shadow Blitz";
		case 41:
			return "Smoke Blitz";
		case 42:
			return "Blood Blitz";
		case 43:
			return "Ice Blitz";
		case 44:
			return "Shadow Barrage";
		case 45:
			return "Smoke Barrage";
		case 46:
			return "Blood Barrage";
		case 47:
			return "Ice Barrage";
		default:
			return "Select Spell";
		}
	}

	public boolean fullVoidRange() {
		// return playerEquipment[playerHat] == 11664 && playerEquipment[playerLegs] ==
		// 8840 || playerEquipment[playerLegs] == 13073 && playerEquipment[playerChest]
		// == 8839
		// || playerEquipment[playerChest] == 13072 && playerEquipment[playerHands] ==
		// 8842;

		if (getItems().isWearingItem(11664) && getItems().isWearingItem(8840) && getItems().isWearingItem(8839)
				&& getItems().isWearingItem(8842)) {
			return true;
		}
		if (getItems().isWearingItem(11664) && getItems().isWearingItem(13073) && getItems().isWearingItem(13072)
				&& getItems().isWearingItem(8842)) {
			return true;
		}
		return false;
	}

	public boolean fullVoidMage() {
		// return playerEquipment[playerHat] == 11663 && playerEquipment[playerLegs] ==
		// 8840 || playerEquipment[playerLegs] == 13073 && playerEquipment[playerChest]
		// == 8839
		// || playerEquipment[playerChest] == 13072 && playerEquipment[playerHands] ==
		// 8842;

		if (getItems().isWearingItem(11663) && getItems().isWearingItem(8840) && getItems().isWearingItem(8839)
				&& getItems().isWearingItem(8842)) {
			return true;
		}
		if (getItems().isWearingItem(11663) && getItems().isWearingItem(13073) && getItems().isWearingItem(13072)
				&& getItems().isWearingItem(8842)) {
			return true;
		}
		return false;
	}

	public boolean fullVoidMelee() {
		if (getItems().isWearingItem(11665) && getItems().isWearingItem(8840) && getItems().isWearingItem(8839)
				&& getItems().isWearingItem(8842)) {
			return true;
		}
		if (getItems().isWearingItem(11665) && getItems().isWearingItem(13073) && getItems().isWearingItem(13072)
				&& getItems().isWearingItem(8842)) {
			return true;
		}
		return false;
	}

	/**
	 * SouthWest, NorthEast, SouthWest, NorthEast
	 */
	public boolean inArea(int x, int y, int x1, int y1) {
		if (absX > x && absX < x1 && absY < y && absY > y1) {
			return true;
		}
		return false;
	}

	public boolean Area(final int x1, final int x2, final int y1, final int y2) {
		return (absX >= x1 && absX <= x2 && absY >= y1 && absY <= y2);
	}

	public boolean inBank() {
		return Area(3090, 3099, 3487, 3500) || Area(3089, 3090, 3492, 3498) || Area(3248, 3258, 3413, 3428)
				|| Area(3179, 3191, 3432, 3448) || Area(2944, 2948, 3365, 3374) || Area(2942, 2948, 3367, 3374)
				|| Area(2944, 2950, 3365, 3370) || Area(3008, 3019, 3352, 3359) || Area(3017, 3022, 3352, 3357)
				|| Area(3203, 3213, 3200, 3237) || Area(3212, 3215, 3200, 3235) || Area(3215, 3220, 3202, 3235)
				|| Area(3220, 3227, 3202, 3229) || Area(3227, 3230, 3208, 3226) || Area(3226, 3228, 3230, 3211)
				|| Area(3227, 3229, 3208, 3226) || Area(3025, 3032, 3374, 3384);
	}

	public boolean isInJail() {
		if (absX >= 2066 && absX <= 2108 && absY >= 4452 && absY <= 4478) {
			return true;
		}
		return false;
	}

	public boolean inClanWars() {
		if (absX > 3272 && absX < 3391 && absY > 4759 && absY < 4863) {
			return true;
		}
		return false;
	}

	public boolean inClanWarsSafe() {
		if (absX > 3263 && absX < 3390 && absY > 4735 && absY < 4761) {
			return true;
		}
		return false;
	}
	public boolean inRaids() {
		return (absX > 3210 && absX < 3368 && absY > 5137 && absY < 5759);
	}

	public boolean inRaidsMountain() {
		return (absX > 1219 && absX < 1259 && absY > 3542 && absY < 3577);

	}
	public boolean inWild() {
		if (inClanWars())
			return true;
		if(Boundary.isIn(this, Boundary.EDGEVILLE_PERIMETER) && !Boundary.isIn(this, Boundary.EDGE_BANK) && getHeight() == 8){
			return true;
		}
		if (Boundary.isIn(this, Boundary.SAFEPK))
			return true;
		if (Boundary.isIn(this, Boundary.WILDERNESS_PARAMETERS)) {
			return true;
		}
		return false;
	}

	public boolean inEdgeville() {
		return (absX > 3040 && absX < 3200 && absY > 3460 && absY < 3519);
	}

	public boolean maxRequirements(Player c) {
		int amount = 0;
		for (int i = 0; i <= 21; i++) {
			if (getLevelForXP(c.playerXP[i]) >= 99) {
				amount++;
			}
			if (amount == 22) {
				return true;
			}
		}
		return false;
	}

	public boolean maxedCertain(Player c, int min, int max) {
		int amount = 0;
		int total = min + max;
		for (int i = min; i <= max; i++) {
			if (getLevelForXP(c.playerXP[i]) >= 99) {
				amount++;
			}
			if (amount == total) {
				return true;
			}
		}
		return false;
	}

	public boolean maxedSkiller(Player c) {
		int amount = 0;
		for (int id = 0; id <= 6; id++) {
			if (c.playerLevel[id] <= 1 && id != 3) {
				amount++;
			}
		}
		for (int i = 7; i <= 22; i++) {
			if (c.playerLevel[i] >= 99) {
				amount++;
			}
		}
		if (amount == 22) {
			return true;
		}
		return false;
	}

	public boolean arenas() {
		if (absX > 3331 && absX < 3391 && absY > 3242 && absY < 3260) {
			return true;
		}
		return false;
	}

	public boolean inDuelArena() {
		if ((absX > 3322 && absX < 3394 && absY > 3195 && absY < 3291)
				|| (absX > 3311 && absX < 3323 && absY > 3223 && absY < 3248)) {
			return true;
		}
		return false;
	}
	public boolean inRevs() {
		return (absX > 3143 && absX < 3262 && absY > 10053 && absY < 10231);
	}

	public boolean inMulti() {
		if (Boundary.isIn(this, Zulrah.BOUNDARY) || Boundary.isIn(this, Boundary.CORPOREAL_BEAST_LAIR)
				|| Boundary.isIn(this, Boundary.KRAKEN_CAVE) || Boundary.isIn(this, Boundary.SCORPIA_LAIR)
				|| Boundary.isIn(this, Boundary.CERBERUS_BOSSROOMS) || Boundary.isIn(this, Boundary.INFERNO)
				|| Boundary.isIn(this, Boundary.SKOTIZO_BOSSROOM) || Boundary.isIn(this, Boundary.LIZARDMAN_CANYON)
				|| Boundary.isIn(this, Boundary.BANDIT_CAMP_BOUNDARY) || Boundary.isIn(this, Boundary.COMBAT_DUMMY)
				|| Boundary.isIn(this, Boundary.TEKTON) || Boundary.isIn(this, Boundary.SKELETAL_MYSTICS)
				|| Boundary.isIn(this, Boundary.RAIDS) || Boundary.isIn(this, Boundary.OLM)
				|| Boundary.isIn(this, Boundary.ICE_DEMON) || Boundary.isIn(this, Boundary.CATACOMBS)) {
			return true;
		}
		if(inRevs()) {
			return true;
		}
		if (Boundary.isIn(this, Boundary.KALPHITE_QUEEN) && heightLevel == 0) {
			return true;
		}
		if ((absX >= 3136 && absX <= 3327 && absY >= 3519 && absY <= 3607)
				|| (absX >= 3190 && absX <= 3327 && absY >= 3648 && absY <= 3839)
				|| (absX >= 3200 && absX <= 3390 && absY >= 3840 && absY <= 3967)
				|| (absX >= 2992 && absX <= 3007 && absY >= 3912 && absY <= 3967)
				|| (absX >= 2946 && absX <= 2959 && absY >= 3816 && absY <= 3831)
				|| (absX >= 3008 && absX <= 3199 && absY >= 3856 && absY <= 3903)
				|| (absX >= 2824 && absX <= 2944 && absY >= 5258 && absY <= 5369)
				|| (absX >= 3008 && absX <= 3071 && absY >= 3600 && absY <= 3711)
				|| (absX >= 3072 && absX <= 3327 && absY >= 3608 && absY <= 3647)
				|| (absX >= 2624 && absX <= 2690 && absY >= 2550 && absY <= 2619)
				|| (absX >= 2371 && absX <= 2422 && absY >= 5062 && absY <= 5117)
				|| (absX >= 2896 && absX <= 2927 && absY >= 3595 && absY <= 3630)
				|| (absX >= 2892 && absX <= 2932 && absY >= 4435 && absY <= 4464)
				|| (absX >= 2256 && absX <= 2287 && absY >= 4680 && absY <= 4711)
				|| (absX >= 2962 && absX <= 3006 && absY >= 3621 && absY <= 3659)
				|| (absX >= 3155 && absX <= 3214 && absY >= 3755 && absY <= 3803)
				|| (absX >= 1889 && absX <= 1912 && absY >= 4396 && absY <= 4413)
				|| (absX >= 3717 && absX <= 3772 && absY >= 5765 && absY <= 5820)
				|| (absX >= 3341 && absX <= 3378 && absY >= 4760 && absY <= 4853)) {
			return true;
		}
		return false;
	}

	public boolean inGodwars() {
		return Boundary.isIn(this, Godwars.GODWARS_AREA);
	}

	public boolean checkFullGear(Player c) {
		int amount = 0;
		for (int i = 0; i < c.playerEquipment.length; i++) {
			if (c.playerEquipment[0] >= 0) {
				amount++;
			}
			if (amount == c.playerEquipment.length) {
				return true;
			}
		}
		return false;
	}

	public void updateshop(int i) {
		Player p = PlayerHandler.players[getIndex()];
		p.getShops().resetShop(i);
	}

	public void println_debug(String str) {
		System.out.println("[player-" + getIndex() + "][User: " + playerName + "]: " + str);
	}

	public void println(String str) {
		System.out.println("[player-" + getIndex() + "][User: " + playerName + "]: " + str);
	}

	public boolean WithinDistance(int objectX, int objectY, int playerX, int playerY, int distance) {
		for (int i = 0; i <= distance; i++) {
			for (int j = 0; j <= distance; j++) {
				if ((objectX + i) == playerX
						&& ((objectY + j) == playerY || (objectY - j) == playerY || objectY == playerY)) {
					return true;
				} else if ((objectX - i) == playerX
						&& ((objectY + j) == playerY || (objectY - j) == playerY || objectY == playerY)) {
					return true;
				} else if (objectX == playerX
						&& ((objectY + j) == playerY || (objectY - j) == playerY || objectY == playerY)) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean isWithinDistance() {
		for (int i = 0; i < Config.MAX_PLAYERS; i++) {
			if (PlayerHandler.players[i] != null) {
				Player other = (Player) PlayerHandler.players[i];

				int deltaX = other.absX - absX, deltaY = other.absY - absY;
				return deltaX <= 15 && deltaX >= -16 && deltaY <= 15 && deltaY >= -16;
			}
		}
		return false;
	}

	public boolean withinDistance(Player otherPlr) {
		if (heightLevel != otherPlr.heightLevel)
			return false;
		int deltaX = otherPlr.absX - absX, deltaY = otherPlr.absY - absY;
		return deltaX <= 15 && deltaX >= -16 && deltaY <= 15 && deltaY >= -16;
	}

	public boolean withinDistance(NPC npc) {
		if (heightLevel != npc.heightLevel)
			return false;
		if (npc.needRespawn == true)
			return false;
		int deltaX = npc.absX - absX, deltaY = npc.absY - absY;
		return deltaX <= 15 && deltaX >= -16 && deltaY <= 15 && deltaY >= -16;
	}

	public boolean withinDistance(int absX, int getY, int getHeightLevel) {
		if (this.getHeightLevel() != getHeightLevel)
			return false;
		int deltaX = this.getX() - absX, deltaY = this.getY() - getY;
		return deltaX <= 15 && deltaX >= -16 && deltaY <= 15 && deltaY >= -16;
	}

	public int getHeightLevel() {
		return getHeightLevel;
	}

	public int distanceToPoint(int pointX, int pointY) {
		return (int) Math.sqrt(Math.pow(absX - pointX, 2) + Math.pow(absY - pointY, 2));
	}

	public int distanceToPoint(int pointX, int pointY, int pointZ) {
		return (int) Math.sqrt(
				Math.pow(absX - pointX, 2) + Math.pow(absY - pointY, 2) + Math.pow(Math.abs(heightLevel) - pointZ, 2));
	}

	public void resetWalkingQueue() {
		wQueueReadPtr = wQueueWritePtr = 0;

		for (int i = 0; i < walkingQueueSize; i++) {
			walkingQueueX[i] = currentX;
			walkingQueueY[i] = currentY;
		}
	}

	public void addToWalkingQueue(int x, int y) {
		// if (VirtualWorld.I(heightLevel, absX, absY, x, y, 0)) {
		int next = (wQueueWritePtr + 1) % walkingQueueSize;
		if (next == wQueueWritePtr)
			return;
		walkingQueueX[wQueueWritePtr] = x;
		walkingQueueY[wQueueWritePtr] = y;
		wQueueWritePtr = next;
		// }
	}

	public boolean goodDistance(int objectX, int objectY, int playerX, int playerY, int distance) {
		return Misc.goodDistance(objectX, objectY, playerX, playerY, distance);
	}

	public boolean isWithinDistance(Position other, int dist) {
		int deltaX = other.getX() - x, deltaY = other.getY() - y;
		return deltaX <= dist && deltaX >= -dist && deltaY <= dist && deltaY >= -dist;
	}

	/**
	 * Checks the combat distance to see if the player is in an appropriate location
	 * based on the attack style.
	 * 
	 * @param attacker
	 * @param target
	 * @return
	 */
	public boolean checkCombatDistance(Player attacker, Player target) {
		int distance = Misc.distanceBetween(attacker, target);
		int required_distance = this.getDistanceRequired();
		return (this.usingMagic || this.usingRangeWeapon || this.usingBow || this.autocasting || this.usingBallista)
				&& distance <= required_distance
						? true
						: (this.usingMelee && this.isMoving && distance <= required_distance ? true
								: distance == 1 && (this.freezeTimer <= 0 || this.getX() == target.getX()
										|| this.getY() == target.getY()));
	}

	public int getDistanceRequired() {
		return !this.usingMagic && !this.usingBallista && !this.usingRangeWeapon && !usingBow && !this.autocasting
				? (this.isMoving ? 3 : 1)
				: 9;
	}

	public int otherDirection;
	public boolean invincible;

	public int getNextWalkingDirection() {
		if (wQueueReadPtr == wQueueWritePtr)
			return -1;
		int dir;
		do {
			dir = Misc.direction(currentX, currentY, walkingQueueX[wQueueReadPtr], walkingQueueY[wQueueReadPtr]);
			if (dir == -1 && otherDirection != dir) {
				otherDirection = dir;
			}
			if (dir == -1) {
				wQueueReadPtr = (wQueueReadPtr + 1) % walkingQueueSize;
			} else if ((dir & 1) != 0) {
				println_debug("Invalid waypoint in walking queue!");
				resetWalkingQueue();
				return -1;
			}
		} while ((dir == -1) && (wQueueReadPtr != wQueueWritePtr));
		if (dir == -1) {
			return -1;
		}
		dir >>= 1;
		currentX += Misc.directionDeltaX[dir];
		currentY += Misc.directionDeltaY[dir];
		absX += Misc.directionDeltaX[dir];
		absY += Misc.directionDeltaY[dir];
		/*
		 * if (isRunning()) { Client c = (Client) this; if (runEnergy > 0) {
		 * runEnergy--; c.getPA().sendFrame126(runEnergy + "%", 149); } else {
		 * isRunning2 = false; c.getPA().setConfig(173, 0); } }
		 */
		return dir;
	}

	public boolean isRunning() {
		return isNewWalkCmdIsRunning() || (isRunning2 && isMoving);
	}

	public void getNextPlayerMovement() {
		mapRegionDidChange = false;
		didTeleport = false;
		dir1 = dir2 = -1;
		if (teleportToX != -1 && teleportToY != -1) {
			mapRegionDidChange = true;
			if (mapRegionX != -1 && mapRegionY != -1) {
				int relX = teleportToX - mapRegionX * 8, relY = teleportToY - mapRegionY * 8;
				if (relX >= 2 * 8 && relX < 11 * 8 && relY >= 2 * 8 && relY < 11 * 8)
					mapRegionDidChange = false;
			}
			if (mapRegionDidChange) {
				mapRegionX = (teleportToX >> 3) - 6;
				mapRegionY = (teleportToY >> 3) - 6;
			}
			currentX = teleportToX - 8 * mapRegionX;
			currentY = teleportToY - 8 * mapRegionY;
			absX = teleportToX;
			absY = teleportToY;

			resetWalkingQueue();
			teleportToX = teleportToY = -1;
			didTeleport = true;
			postTeleportProcessing();
		} else {
			dir1 = getNextWalkingDirection();
			if (dir1 == -1)
				return;
			if (isRunning) {
				dir2 = getNextWalkingDirection();
				runningDistanceTravelled++;
			}
			int deltaX = 0, deltaY = 0;
			if (currentX < 2 * 8) {
				deltaX = 4 * 8;
				mapRegionX -= 4;
				mapRegionDidChange = true;
			} else if (currentX >= 11 * 8) {
				deltaX = -4 * 8;
				mapRegionX += 4;
				mapRegionDidChange = true;
			}
			if (currentY < 2 * 8) {
				deltaY = 4 * 8;
				mapRegionY -= 4;
				mapRegionDidChange = true;
			} else if (currentY >= 11 * 8) {
				deltaY = -4 * 8;
				mapRegionY += 4;
				mapRegionDidChange = true;
			}

			if (mapRegionDidChange) {
				currentX += deltaX;
				currentY += deltaY;
				for (int i = 0; i < walkingQueueSize; i++) {
					walkingQueueX[i] += deltaX;
					walkingQueueY[i] += deltaY;
				}
			}

		}
	}

	public void postTeleportProcessing() {
		if (inGodwars()) {
			if (equippedGodItems == null) {
				updateGodItems();
			}
		} else if (equippedGodItems != null) {
			equippedGodItems = null;
			godwars.initialize();
		}
	}

	public void updateThisPlayerMovement(Stream str) {
		// synchronized(this) {
		if (mapRegionDidChange) {
			str.createFrame(73);
			str.writeWordA(mapRegionX + 6);
			str.writeWord(mapRegionY + 6);
		}

		if (didTeleport) {
			str.createFrameVarSizeWord(81);
			str.initBitAccess();
			str.writeBits(1, 1);
			str.writeBits(2, 3);
			str.writeBits(2, heightLevel);
			str.writeBits(1, 1);
			str.writeBits(1, (updateRequired) ? 1 : 0);
			str.writeBits(7, currentY);
			str.writeBits(7, currentX);
			return;
		}

		if (dir1 == -1) {
			// don't have to update the character position, because we're just
			// standing
			str.createFrameVarSizeWord(81);
			str.initBitAccess();
			isMoving = false;
			if (updateRequired) {
				// tell client there's an update block appended at the end
				str.writeBits(1, 1);
				str.writeBits(2, 0);
			} else {
				str.writeBits(1, 0);
			}
			if (DirectionCount < 50) {
				DirectionCount++;
			}
		} else {
			DirectionCount = 0;
			str.createFrameVarSizeWord(81);
			str.initBitAccess();
			str.writeBits(1, 1);

			if (dir2 == -1) {
				isMoving = true;
				str.writeBits(2, 1);
				str.writeBits(3, Misc.xlateDirectionToClient[dir1]);
				if (updateRequired)
					str.writeBits(1, 1);
				else
					str.writeBits(1, 0);
			} else {
				isMoving = true;
				str.writeBits(2, 2);
				str.writeBits(3, Misc.xlateDirectionToClient[dir1]);
				str.writeBits(3, Misc.xlateDirectionToClient[dir2]);
				if (updateRequired)
					str.writeBits(1, 1);
				else
					str.writeBits(1, 0);
			}
		}

	}

	public void updatePlayerMovement(Stream str) {
		// synchronized(this) {
		if (dir1 == -1) {
			if (updateRequired || isChatTextUpdateRequired()) {

				str.writeBits(1, 1);
				str.writeBits(2, 0);
			} else
				str.writeBits(1, 0);
		} else if (dir2 == -1) {

			str.writeBits(1, 1);
			str.writeBits(2, 1);
			str.writeBits(3, Misc.xlateDirectionToClient[dir1]);
			str.writeBits(1, (updateRequired || isChatTextUpdateRequired()) ? 1 : 0);
		} else {

			str.writeBits(1, 1);
			str.writeBits(2, 2);
			str.writeBits(3, Misc.xlateDirectionToClient[dir1]);
			str.writeBits(3, Misc.xlateDirectionToClient[dir2]);
			str.writeBits(1, (updateRequired || isChatTextUpdateRequired()) ? 1 : 0);
		}

	}

	public void addNewNPC(NPC npc, Stream str, Stream updateBlock) {
		// synchronized(this) {
		int id = npc.getIndex();
		npcInListBitmap[id >> 3] |= 1 << (id & 7);
		npcList[npcListSize++] = npc;

		str.writeBits(14, id);

		int z = npc.absY - absY;
		if (z < 0)
			z += 32;
		str.writeBits(5, z);
		z = npc.absX - absX;
		if (z < 0)
			z += 32;
		str.writeBits(5, z);

		str.writeBits(1, 0);
		str.writeBits(14, npc.npcType);

		boolean savedUpdateRequired = npc.updateRequired;
		npc.updateRequired = true;
		npc.appendNPCUpdateBlock(updateBlock);
		npc.updateRequired = savedUpdateRequired;
		str.writeBits(1, 1);
	}

	public void addNewPlayer(Player plr, Stream str, Stream updateBlock) {
		if (playerListSize >= 79) {
			return;
		}
		int id = plr.getIndex();
		playerInListBitmap[id >> 3] |= 1 << (id & 7);
		playerList[playerListSize++] = plr;
		str.writeBits(11, id);
		str.writeBits(1, 1);
		boolean savedFlag = plr.isAppearanceUpdateRequired();
		boolean savedUpdateRequired = plr.updateRequired;
		plr.setAppearanceUpdateRequired(true);
		plr.updateRequired = true;
		plr.appendPlayerUpdateBlock(updateBlock);
		plr.setAppearanceUpdateRequired(savedFlag);
		plr.updateRequired = savedUpdateRequired;
		str.writeBits(1, 1);
		int z = plr.absY - absY;
		if (z < 0)
			z += 32;
		str.writeBits(5, z);
		z = plr.absX - absX;
		if (z < 0)
			z += 32;
		str.writeBits(5, z);
	}

	protected void appendPlayerAppearance(Stream str) {
		playerProps.currentOffset = 0;
		playerProps.writeByte(playerAppearance[0]);
		StringBuilder sb = new StringBuilder(titles.getCurrentTitle());
		if (titles.getCurrentTitle().equalsIgnoreCase("None")) {
			sb.delete(0, sb.length());
		}
		playerProps.writeString(sb.toString());
		sb = new StringBuilder(rights.getPrimary().getColor());
		if (titles.getCurrentTitle().equalsIgnoreCase("None")) {
			sb.delete(0, sb.length());
		}
		playerProps.writeString(sb.toString());
		playerProps.writeByte(getHealth().getStatus().getMask());
		playerProps.writeByte(headIcon);
		playerProps.writeByte(headIconPk);
		if (isNpc == false) {
			if (playerEquipment[playerHat] > 1) {
				playerProps.writeWord(0x200 + playerEquipment[playerHat]);
			} else {
				playerProps.writeByte(0);
			}

			if (playerEquipment[playerCape] > 1) {
				playerProps.writeWord(0x200 + playerEquipment[playerCape]);
			} else {
				playerProps.writeByte(0);
			}

			if (playerEquipment[playerAmulet] > 1) {
				playerProps.writeWord(0x200 + playerEquipment[playerAmulet]);
			} else {
				playerProps.writeByte(0);
			}

			if (playerEquipment[playerWeapon] > 1) {
				playerProps.writeWord(0x200 + playerEquipment[playerWeapon]);
			} else {
				playerProps.writeByte(0);
			}

			if (playerEquipment[playerChest] > 1) {
				playerProps.writeWord(0x200 + playerEquipment[playerChest]);
			} else {
				playerProps.writeWord(0x100 + playerAppearance[2]);
			}

			if (playerEquipment[playerShield] > 1) {
				playerProps.writeWord(0x200 + playerEquipment[playerShield]);
			} else {
				playerProps.writeByte(0);
			}

			if (!Item.isFullBody(playerEquipment[playerChest])) {
				playerProps.writeWord(0x100 + playerAppearance[3]);
			} else {
				playerProps.writeByte(0);
			}

			if (playerEquipment[playerLegs] > 1) {
				playerProps.writeWord(0x200 + playerEquipment[playerLegs]);
			} else {
				playerProps.writeWord(0x100 + playerAppearance[5]);
			}

			if (!Item.isFullHat(playerEquipment[playerHat]) && !Item.isFullMask(playerEquipment[playerHat])) {
				playerProps.writeWord(0x100 + playerAppearance[1]);
			} else {
				playerProps.writeByte(0);
			}

			if (playerEquipment[playerHands] > 1) {
				playerProps.writeWord(0x200 + playerEquipment[playerHands]);
			} else {
				playerProps.writeWord(0x100 + playerAppearance[4]);
			}

			if (playerEquipment[playerFeet] > 1) {
				playerProps.writeWord(0x200 + playerEquipment[playerFeet]);
			} else {
				playerProps.writeWord(0x100 + playerAppearance[6]);
			}

			if (playerAppearance[0] != 1 && !Item.isFullMask(playerEquipment[playerHat])) {
				playerProps.writeWord(0x100 + playerAppearance[7]);
			} else {
				playerProps.writeByte(0);
			}
		} else {
			playerProps.writeWord(-1);
			playerProps.writeWord(npcId2);
		}
		playerProps.writeByte(playerAppearance[8]);
		playerProps.writeByte(playerAppearance[9]);
		playerProps.writeByte(playerAppearance[10]);
		playerProps.writeByte(playerAppearance[11]);
		playerProps.writeByte(playerAppearance[12]);
		playerProps.writeWord(playerStandIndex); // standAnimIndex
		playerProps.writeWord(playerTurnIndex); // standTurnAnimIndex
		playerProps.writeWord(playerWalkIndex); // walkAnimIndex
		playerProps.writeWord(playerTurn180Index); // turn180AnimIndex
		playerProps.writeWord(playerTurn90CWIndex); // turn90CWAnimIndex
		playerProps.writeWord(playerTurn90CCWIndex); // turn90CCWAnimIndex
		playerProps.writeWord(playerRunIndex); // runAnimIndex
		playerProps.writeQWord(Misc.playerNameToInt64(playerName));
		playerProps.writeByte(invisible ? 1 : 0);
		combatLevel = calculateCombatLevel();
		playerProps.writeByte(combatLevel); // combat level
		playerProps.writeByte(rights.getPrimary().getValue());
		playerProps.writeWord(0);
		str.writeByteC(playerProps.currentOffset);
		str.writeBytes(playerProps.buffer, playerProps.currentOffset, 0);
	}

	public int calculateCombatLevel() {
		int j = getLevelForXP(playerXP[playerAttack]);
		int k = getLevelForXP(playerXP[playerDefence]);
		int l = getLevelForXP(playerXP[playerStrength]);
		int i1 = getLevelForXP(playerXP[playerHitpoints]);
		int j1 = getLevelForXP(playerXP[playerPrayer]);
		int k1 = getLevelForXP(playerXP[playerRanged]);
		int l1 = getLevelForXP(playerXP[playerMagic]);
		int combatLevel = (int) (((k + i1) + Math.floor(j1 / 2)) * 0.24798D) + 1;
		double d = (j + l) * 0.32500000000000001D;
		double d1 = Math.floor(k1 * 1.5D) * 0.32500000000000001D;
		double d2 = Math.floor(l1 * 1.5D) * 0.32500000000000001D;
		if (d >= d1 && d >= d2) {
			combatLevel += d;
		} else if (d1 >= d && d1 >= d2) {
			combatLevel += d1;
		} else if (d2 >= d && d2 >= d1) {
			combatLevel += d2;
		}
		return combatLevel;
	}

	public int getLevelForXP(int exp) {
		int points = 0;
		int output = 0;

		for (int lvl = 1; lvl <= 99; lvl++) {
			points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
			output = (int) Math.floor(points / 4);
			if (output >= exp)
				return lvl;
		}
		return 99;
	}

	protected void appendPlayerChatText(Stream str) {
		str.writeWordBigEndian(((getChatTextColor() & 0xFF) << 8) + (getChatTextEffects() & 0xFF));
		str.writeByte(rights.getPrimary().getValue());
		str.writeByteC(getChatTextSize());
		str.writeBytes_reverse(getChatText(), getChatTextSize(), 0);

	}

	public void forcedChat(String text) {
		forcedText = text;
		forcedChatUpdateRequired = true;
		updateRequired = true;
		setAppearanceUpdateRequired(true);
	}

	public void appendForcedChat(Stream str) {
		// synchronized(this) {
		str.writeString(forcedText);
	}

	public void appendMask100Update(Stream str) {
		// synchronized(this) {
		str.writeWordBigEndian(mask100var1);
		str.writeDWord(mask100var2);

	}

	public void gfx(int gfx, int height) {
		mask100var1 = gfx;
		mask100var2 = 65536 * height;
		graphicMaskUpdate0x100 = true;
		updateRequired = true;
	}

	public void gfx100(int gfx) {
		mask100var1 = gfx;
		mask100var2 = 6553600;
		graphicMaskUpdate0x100 = true;
		updateRequired = true;
	}

	public void gfx0(int gfx) {
		mask100var1 = gfx;
		mask100var2 = 65536;
		graphicMaskUpdate0x100 = true;
		updateRequired = true;
	}

	public boolean wearing2h() {
		Player c = this;
		String s = ItemAssistant.getItemName(c.playerEquipment[c.playerWeapon]);
		if (s.contains("2h"))
			return true;
		if (s.contains("bulwark") || s.contains("elder maul"))
			return true;
		if (s.contains("godsword"))
			return true;

		return false;
	}

	/**
	 * Animations
	 **/
	public void startAnimation(int animId) {
		// if (wearing2h() && animId == 829)
		// return;
		animationRequest = animId;
		animationWaitCycles = 0;
		updateRequired = true;
	}

	public void startAnimation(int animId, int time) {
		animationRequest = animId;
		animationWaitCycles = time;
		updateRequired = true;
	}

	public void stopAnimation() {
		animationRequest = 65535;
		animationWaitCycles = 0;
		updateRequired = true;
	}

	public void appendAnimationRequest(Stream str) {
		// synchronized(this) {
		str.writeWordBigEndian((animationRequest == -1) ? 65535 : animationRequest);
		str.writeByteC(animationWaitCycles);
	}

	public void faceUpdate(int index) {
		face = index;
		faceUpdateRequired = true;
		updateRequired = true;
	}

	public void appendFaceUpdate(Stream str) {
		// synchronized(this) {
		str.writeWordBigEndian(face);

	}

	public void turnPlayerTo(int pointX, int pointY) {
		FocusPointX = 2 * pointX + 1;
		FocusPointY = 2 * pointY + 1;
		updateRequired = true;
	}

	private void appendSetFocusDestination(Stream str) {
		// synchronized(this) {
		str.writeWordBigEndianA(FocusPointX);
		str.writeWordBigEndian(FocusPointY);

	}

	@Override
	public void appendDamage(int damage, Hitmark h) {
		lastAttacked = System.currentTimeMillis();
		if (damage < 0) {
			damage = 0;
			h = Hitmark.MISS;
		}
		if (getHealth().getAmount() - damage < 0) {
			damage = getHealth().getAmount();
		}
		if (teleTimer <= 0) {
			if (!invincible)
				getHealth().reduce(damage);
			if (!hitUpdateRequired) {
				hitUpdateRequired = true;
				hitDiff = damage;
				hitmark1 = h;
			} else if (!hitUpdateRequired2) {
				hitUpdateRequired2 = true;
				hitDiff2 = damage;
				hitmark2 = h;
			}
		} else {
			if (hitUpdateRequired) {
				hitUpdateRequired = false;
			}
			if (hitUpdateRequired2) {
				hitUpdateRequired2 = false;
			}
		}
		updateRequired = true;
	}

	@Override
	protected void appendHitUpdate(Stream str) {
		str.writeByte(hitDiff);
		if (hitmark1 == null) {
			str.writeByteA(0);
		} else {
			str.writeByteA(hitmark1.getId());
		}
		if (getHealth().getAmount() <= 0) {
			isDead = true;
		}
		str.writeByteC(getHealth().getAmount());
		str.writeByte(getHealth().getMaximum());
	}

	@Override
	protected void appendHitUpdate2(Stream str) {
		str.writeByte(hitDiff2);
		if (hitmark2 == null) {
			str.writeByteS(0);
		} else {
			str.writeByteS(hitmark2.getId());
		}
		if (getHealth().getAmount() <= 0) {
			isDead = true;
		}
		str.writeByte(getHealth().getAmount());
		str.writeByteC(getHealth().getMaximum());
	}

	/**
	 * Direction, 2 = South, 0 = North, 3 = West, 2 = East?
	 * 
	 * @param xOffset
	 * @param yOffset
	 * @param speed1
	 * @param speed2
	 * @param direction
	 * @param emote
	 */
	private int xOffsetWalk, yOffsetWalk;
	public int dropSize = 0;
	public boolean canUpdateHighscores = true;
	public boolean zukDead = false;
	public boolean sellingX;
	public boolean firstBankLogin = true;
	public int currentPrestigeLevel, prestigeNumber;
	public boolean canPrestige = false;
	public int prestigePoints;
	public boolean newStarter = false;

	/**
	 * 0 North 1 East 2 South 3 West
	 */
	public void setForceMovement(int xOffset, int yOffset, int speedOne, int speedTwo, String directionSet,
			int animation) {
		if (isForceMovementActive() || forceMovement) {
			return;
		}
		stopMovement();
		xOffsetWalk = xOffset - absX;
		yOffsetWalk = yOffset - absY;
		playerStandIndex = animation;
		playerRunIndex = animation;
		playerWalkIndex = animation;
		forceMovementActive = true;
		getPA().requestUpdates();
		setAppearanceUpdateRequired(true);
		Server.getEventHandler().submit(new Event<Player>("force_movement", this, 2) {

			@Override
			public void execute() {
				if (attachment == null || attachment.disconnected) {
					super.stop();
					return;
				}
				attachment.updateRequired = true;
				attachment.forceMovement = true;
				attachment.x1 = currentX;
				attachment.y1 = currentY;
				attachment.x2 = currentX + xOffsetWalk;
				attachment.y2 = currentY + yOffsetWalk;
				attachment.speed1 = speedOne;
				attachment.speed2 = speedTwo;
				attachment.direction = directionSet == "NORTH" ? 0
						: directionSet == "EAST" ? 1 : directionSet == "SOUTH" ? 2 : directionSet == "WEST" ? 3 : 0;
				super.stop();
			}
		});
		Server.getEventHandler()
				.submit(new Event<Player>("force_movement", this, Math.abs(xOffsetWalk) + Math.abs(yOffsetWalk)) {

					@Override
					public void execute() {
						if (attachment == null || attachment.disconnected) {
							super.stop();
							return;
						}
						forceMovementActive = false;
						attachment.getPA().movePlayer(xOffset, yOffset, attachment.heightLevel);
						if (attachment.playerEquipment[attachment.playerWeapon] == -1) {
							attachment.playerStandIndex = 0x328;
							attachment.playerTurnIndex = 0x337;
							attachment.playerWalkIndex = 0x333;
							attachment.playerTurn180Index = 0x334;
							attachment.playerTurn90CWIndex = 0x335;
							attachment.playerTurn90CCWIndex = 0x336;
							attachment.playerRunIndex = 0x338;
						} else {
							attachment.getCombat().getPlayerAnimIndex(Item
									.getItemName(attachment.playerEquipment[attachment.playerWeapon]).toLowerCase());
						}
						forceMovement = false;
						super.stop();
					}
				});
	}

	public void appendMask400Update(Stream str) {
		str.writeByteS(x1);
		str.writeByteS(y1);
		str.writeByteS(x2);
		str.writeByteS(y2);
		str.writeWordBigEndianA(speed1);
		str.writeWordA(speed2);
		str.writeByteS(direction);
	}

	public void appendPlayerUpdateBlock(Stream str) {
		if (!updateRequired && !isChatTextUpdateRequired())
			return;
		int updateMask = 0;

		if (forceMovement) {
			updateMask |= 0x400;
		}

		if (graphicMaskUpdate0x100) {
			updateMask |= 0x100;
		}

		if (animationRequest != -1) {
			updateMask |= 8;
		}

		if (forcedChatUpdateRequired) {
			updateMask |= 4;
		}

		if (isChatTextUpdateRequired()) {
			updateMask |= 0x80;
		}

		if (isAppearanceUpdateRequired()) {
			updateMask |= 0x10;
		}

		if (faceUpdateRequired) {
			updateMask |= 1;
		}

		if (FocusPointX != -1) {
			updateMask |= 2;
		}

		if (hitUpdateRequired) {
			updateMask |= 0x20;
		}

		if (hitUpdateRequired2) {
			updateMask |= 0x200;
		}

		if (updateMask >= 0x100) {
			updateMask |= 0x40;
			str.writeByte(updateMask & 0xFF);
			str.writeByte(updateMask >> 8);
		} else {
			str.writeByte(updateMask);
		}

		if (forceMovement) {
			appendMask400Update(str);
		}

		if (graphicMaskUpdate0x100) {
			appendMask100Update(str);
		}

		if (animationRequest != -1) {
			appendAnimationRequest(str);
		}

		if (forcedChatUpdateRequired) {
			appendForcedChat(str);
		}

		if (isChatTextUpdateRequired()) {
			appendPlayerChatText(str);
		}

		if (faceUpdateRequired) {
			appendFaceUpdate(str);
		}

		if (isAppearanceUpdateRequired()) {
			appendPlayerAppearance(str);
		}

		if (FocusPointX != -1) {
			appendSetFocusDestination(str);
		}

		if (hitUpdateRequired) {
			appendHitUpdate(str);
		}

		if (hitUpdateRequired2) {
			appendHitUpdate2(str);
		}

	}

	public void clearUpdateFlags() {
		updateRequired = false;
		setChatTextUpdateRequired(false);
		setAppearanceUpdateRequired(false);
		hitUpdateRequired = false;
		hitUpdateRequired2 = false;
		forcedChatUpdateRequired = false;
		graphicMaskUpdate0x100 = false;
		animationRequest = -1;
		FocusPointX = -1;
		FocusPointY = -1;
		faceUpdateRequired = false;
		forceMovement = false;
		face = 65535;
	}

	public void stopMovement() {
		if (teleportToX <= 0 && teleportToY <= 0) {
			teleportToX = absX;
			teleportToY = absY;
		}
		newWalkCmdSteps = 0;
		getNewWalkCmdX()[0] = getNewWalkCmdY()[0] = travelBackX[0] = travelBackY[0] = 0;
		getNextPlayerMovement();
	}

	public void preProcessing() {
		newWalkCmdSteps = 0;
	}

	public int setPacketsReceived(int packetsReceived) {
		return packetsReceived;
	}

	public int getPacketsReceived() {
		return packetsReceived;
	}

	public void postProcessing() {
		if (newWalkCmdSteps > 0) {
			int firstX = getNewWalkCmdX()[0], firstY = getNewWalkCmdY()[0];

			int lastDir = 0;
			boolean found = false;
			numTravelBackSteps = 0;
			int ptr = wQueueReadPtr;
			int dir = Misc.direction(currentX, currentY, firstX, firstY);
			if (dir != -1 && (dir & 1) != 0) {
				do {
					lastDir = dir;
					if (--ptr < 0)
						ptr = walkingQueueSize - 1;

					travelBackX[numTravelBackSteps] = walkingQueueX[ptr];
					travelBackY[numTravelBackSteps++] = walkingQueueY[ptr];
					dir = Misc.direction(walkingQueueX[ptr], walkingQueueY[ptr], firstX, firstY);
					if (lastDir != dir) {
						found = true;
						break;
					}

				} while (ptr != wQueueWritePtr);
			} else
				found = true;

			if (!found)
				println_debug("Fatal: couldn't find connection vertex! Dropping packet.");
			else {
				wQueueWritePtr = wQueueReadPtr;

				addToWalkingQueue(currentX, currentY);

				if (dir != -1 && (dir & 1) != 0) {

					for (int i = 0; i < numTravelBackSteps - 1; i++) {
						addToWalkingQueue(travelBackX[i], travelBackY[i]);
					}
					int wayPointX2 = travelBackX[numTravelBackSteps - 1],
							wayPointY2 = travelBackY[numTravelBackSteps - 1];
					int wayPointX1, wayPointY1;
					if (numTravelBackSteps == 1) {
						wayPointX1 = currentX;
						wayPointY1 = currentY;
					} else {
						wayPointX1 = travelBackX[numTravelBackSteps - 2];
						wayPointY1 = travelBackY[numTravelBackSteps - 2];
					}

					dir = Misc.direction(wayPointX1, wayPointY1, wayPointX2, wayPointY2);
					if (dir == -1 || (dir & 1) != 0) {
						println_debug("Fatal: The walking queue is corrupt! wp1=(" + wayPointX1 + ", " + wayPointY1
								+ "), " + "wp2=(" + wayPointX2 + ", " + wayPointY2 + ")");
					} else {
						dir >>= 1;
						found = false;
						int x = wayPointX1, y = wayPointY1;
						while (x != wayPointX2 || y != wayPointY2) {
							x += Misc.directionDeltaX[dir];
							y += Misc.directionDeltaY[dir];
							if ((Misc.direction(x, y, firstX, firstY) & 1) == 0) {
								found = true;
								break;
							}
						}
						if (!found) {
							println_debug("Fatal: Internal error: unable to determine connection vertex!" + "  wp1=("
									+ wayPointX1 + ", " + wayPointY1 + "), wp2=(" + wayPointX2 + ", " + wayPointY2
									+ "), " + "first=(" + firstX + ", " + firstY + ")");
						} else
							addToWalkingQueue(wayPointX1, wayPointY1);
					}
				} else {
					for (int i = 0; i < numTravelBackSteps; i++) {
						addToWalkingQueue(travelBackX[i], travelBackY[i]);
					}
				}

				for (int i = 0; i < newWalkCmdSteps; i++) {
					addToWalkingQueue(getNewWalkCmdX()[i], getNewWalkCmdY()[i]);
				}

			}

			isRunning = isNewWalkCmdIsRunning() || isRunning2;
		}
	}

	public int getMapRegionX() {
		return mapRegionX;
	}

	public int getMapRegionY() {
		return mapRegionY;
	}

	public int getX() {
		return absX;
	}

	public int getY() {
		return absY;
	}

	public Coordinate getCoordinate() {
		return new Coordinate(absX, absY, heightLevel);
	}

	public boolean inPcBoat() {
		return absX >= 2660 && absX <= 2663 && absY >= 2638 && absY <= 2643;
	}

	public boolean inPcGame() {
		return absX >= 2624 && absX <= 2690 && absY >= 2550 && absY <= 2619;
	}

	public void setHitDiff(int hitDiff) {
		this.hitDiff = hitDiff;
	}

	public void setHitDiff2(int hitDiff2) {
		this.hitDiff2 = hitDiff2;
	}

	public int getHitDiff() {
		return hitDiff;
	}

	public void setAppearanceUpdateRequired(boolean appearanceUpdateRequired) {
		this.appearanceUpdateRequired = appearanceUpdateRequired;
	}

	public boolean isAppearanceUpdateRequired() {
		return appearanceUpdateRequired;
	}

	public void setChatTextEffects(int chatTextEffects) {
		this.chatTextEffects = chatTextEffects;
	}

	public int getChatTextEffects() {
		return chatTextEffects;
	}

	public void setChatTextSize(byte chatTextSize) {
		this.chatTextSize = chatTextSize;
	}

	public byte getChatTextSize() {
		return chatTextSize;
	}

	public void setChatTextUpdateRequired(boolean chatTextUpdateRequired) {
		this.chatTextUpdateRequired = chatTextUpdateRequired;
	}

	public boolean isChatTextUpdateRequired() {
		return chatTextUpdateRequired;
	}

	public void setChatText(byte chatText[]) {
		this.chatText = chatText;
	}

	public byte[] getChatText() {
		return chatText;
	}

	public void setChatTextColor(int chatTextColor) {
		this.chatTextColor = chatTextColor;
	}

	public int getChatTextColor() {
		return chatTextColor;
	}

	public void setNewWalkCmdX(int newWalkCmdX[]) {
		this.newWalkCmdX = newWalkCmdX;
	}

	public int[] getNewWalkCmdX() {
		return newWalkCmdX;
	}

	public void setNewWalkCmdY(int newWalkCmdY[]) {
		this.newWalkCmdY = newWalkCmdY;
	}

	public int[] getNewWalkCmdY() {
		return newWalkCmdY;
	}

	public void setNewWalkCmdIsRunning(boolean newWalkCmdIsRunning) {
		this.newWalkCmdIsRunning = newWalkCmdIsRunning;
	}

	public boolean isNewWalkCmdIsRunning() {
		return newWalkCmdIsRunning;
	}

	public boolean getRingOfLifeEffect() {
		return maxCape[0];
	}

	public boolean setRingOfLifeEffect(boolean effect) {
		return maxCape[0] = effect;
	}

	public boolean getFishingEffect() {
		return maxCape[1];
	}

	public boolean setFishingEffect(boolean effect) {
		return maxCape[1] = effect;
	}

	public boolean getMiningEffect() {
		return maxCape[2];
	}

	public boolean setMiningEffect(boolean effect) {
		return maxCape[2] = effect;
	}

	public boolean getWoodcuttingEffect() {
		return maxCape[3];
	}

	public boolean setWoodcuttingEffect(boolean effect) {
		return maxCape[3] = effect;
	}

	public int getSkeletalMysticDamageCounter() {
		return raidsDamageCounters[0];
	}

	public void setSkeletalMysticDamageCounter(int damage) {
		this.raidsDamageCounters[0] = damage;
	}

	public int getTektonDamageCounter() {
		return raidsDamageCounters[1];
	}

	public void setTektonDamageCounter(int damage) {
		this.raidsDamageCounters[1] = damage;
	}

	public int getIceDemonDamageCounter() {
		return raidsDamageCounters[2];
	}

	public void setIceDemonDamageCounter(int damage) {
		this.raidsDamageCounters[2] = damage;
	}

	public int getGlodDamageCounter() {
		return raidsDamageCounters[3];
	}

	public void setGlodDamageCounter(int damage) {
		this.raidsDamageCounters[3] = damage;
	}

	public int getIceQueenDamageCounter() {
		return raidsDamageCounters[4];
	}

	public void setIceQueenDamageCounter(int damage) {
		this.raidsDamageCounters[4] = damage;
	}

	public void assignClueCounter(RewardLevel rewardLevel) {
		switch (rewardLevel) {
		case EASY:
			counters[0]++;
		case MEDIUM:
			counters[1]++;
		case HARD:
			counters[2]++;
		case MASTER:
			counters[3]++;
		default:
			break;
		}
	}

	public int getClueCounter(RewardLevel rewardLevel) {
		switch (rewardLevel) {
		case EASY:
			return counters[0];
		case MEDIUM:
			return counters[1];
		case HARD:
			return counters[2];
		case MASTER:
			return counters[3];
		default:
			return 0;
		}
	}

	public int getEasyClueCounter() {
		return counters[0];
	}

	public void setEasyClueCounter(int counters) {
		this.counters[0] = counters;
	}

	public int getMediumClueCounter() {
		return counters[1];
	}

	public void setMediumClueCounter(int counters) {
		this.counters[1] = counters;
	}

	public int getHardClueCounter() {
		return counters[2];
	}

	public void setHardClueCounter(int counters) {
		this.counters[2] = counters;
	}

	public int getMasterClueCounter() {
		return counters[3];
	}

	public void setMasterClueCounter(int counters) {
		this.counters[3] = counters;
	}

	public int getBarrowsChestCounter() {
		return counters[4];
	}

	public void setBarrowsChestCounter(int counters) {
		this.counters[4] = counters;
	}

	public int getDuelWinsCounter() {
		return counters[5];
	}

	public void setDuelWinsCounter(int counters) {
		this.counters[5] = counters;
	}

	public int getDuelLossCounter() {
		return counters[6];
	}

	public void setDuelLossCounter(int counters) {
		this.counters[6] = counters;
	}

	public int getHalloweenOrderCount() {
		return counters[7];
	}

	public void setHalloweenOrderCount(int counters) {
		this.counters[7] = counters;
	}

	public boolean samePlayer() {
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (j == getIndex())
				continue;
			if (PlayerHandler.players[j] != null) {
				if (PlayerHandler.players[j].playerName.equalsIgnoreCase(playerName)) {
					disconnected = true;
					return true;
				}
			}
		}
		return false;
	}

	public void putInCombat(int attacker) {
		underAttackBy = attacker;
		logoutDelay = System.currentTimeMillis();
		singleCombatDelay = System.currentTimeMillis();
	}

	public String getLastClanChat() {
		return lastClanChat;
	}

	public void setLastClanChat(String founder) {
		lastClanChat = founder;
	}

	public long getNameAsLong() {
		return nameAsLong;
	}

	public void setNameAsLong(long hash) {
		this.nameAsLong = hash;
	}

	public boolean isStopPlayer() {
		return stopPlayer;
	}

	public void setStopPlayer(boolean stopPlayer) {
		this.stopPlayer = stopPlayer;
	}

	public int getFace() {
		return this.getIndex() + '\u8000';
	}

	public int getLockIndex() {
		return -this.getIndex() - 1;
	}

	public int getHeight() {
		return this.heightLevel;
	}

	public boolean isDead() {
		return getHealth().getAmount() <= 0 || this.isDead;
	}

	public void healPlayer(int heal) {
		getHealth().increase(heal);
	}

	int maxLevel() {
		return 99;
	}

	public void sendGraphic(int id, int height) {
		if (height == 0) {
			this.gfx0(id);
		}

		if (height == 100) {
			this.gfx100(id);
		}

	}

	public boolean protectingRange() {
		return this.prayerActive[17];
	}

	public boolean protectingMagic() {
		return this.prayerActive[16];
	}

	public boolean protectingMelee() {
		return this.prayerActive[18];
	}

	public void setTrading(boolean trading) {
		this.trading = trading;
	}

	public boolean isTrading() {
		return this.trading;
	}

	public boolean isInvisible() {
		return invisible;
	}

	public void setInvisible(boolean invisible) {
		this.invisible = invisible;
	}

	public boolean inGodmode() {
		return godmode;
	}

	public void setGodmode(boolean godmode) {
		this.godmode = godmode;
	}

	public boolean inSafemode() {
		return safemode;
	}

	public void setSafemode(boolean safemode) {
		this.safemode = safemode;
	}

	public TeleportHandler getTeleport() {
		return teleportHandler;
	}

	public void setDragonfireShieldCharge(int charge) {
		this.dragonfireShieldCharge = charge;
	}

	public int getDragonfireShieldCharge() {
		return dragonfireShieldCharge;
	}

	public void setLastDragonfireShieldAttack(long lastAttack) {
		this.lastDragonfireShieldAttack = lastAttack;
	}

	public long getLastDragonfireShieldAttack() {
		return lastDragonfireShieldAttack;
	}

	public boolean isDragonfireShieldActive() {
		return dragonfireShieldActive;
	}

	public void setDragonfireShieldActive(boolean dragonfireShieldActive) {
		this.dragonfireShieldActive = dragonfireShieldActive;
	}

	/**
	 * Retrieves the rights for this player.
	 * 
	 * @return the rights
	 */
	public RightGroup getRights() {
		if (rights == null) {
			rights = new RightGroup(this, Right.PLAYER);
		}
		return rights;
	}

	/**
	 * Returns a single instance of the Titles class for this player
	 * 
	 * @return the titles class
	 */
	public Titles getTitles() {
		if (titles == null) {
			titles = new Titles(this);
		}
		return titles;
	}

	public RandomEventInterface getInterfaceEvent() {
		return randomEventInterface;
	}

	/**
	 * Modifies the current interface open
	 * 
	 * @param interfaceOpen
	 *            the interface id
	 */
	public void setInterfaceOpen(int interfaceOpen) {
		this.interfaceOpen = interfaceOpen;
	}

	/**
	 * The interface that is opened
	 * 
	 * @return the interface id
	 */
	public int getInterfaceOpen() {
		return interfaceOpen;
	}

	/**
	 * Determines whether a warning will be shown when dropping an item.
	 * 
	 * @return True if it's the case, False otherwise.
	 */
	public boolean showDropWarning() {
		return dropWarning;
	}

	/**
	 * Change whether a warning will be shown when dropping items.
	 * 
	 * @param shown
	 *            True in case a warning must be shown, False otherwise.
	 */
	public void setDropWarning(boolean shown) {
		dropWarning = shown;
	}

	public boolean getHourlyBoxToggle() {
		return hourlyBoxToggle;
	}

	public void setHourlyBoxToggle(boolean toggle) {
		hourlyBoxToggle = toggle;
	}

	public boolean getFracturedCrystalToggle() {
		return fracturedCrystalToggle;
	}

	public void setFracturedCrystalToggle(boolean toggle1) {
		fracturedCrystalToggle = toggle1;
	}

	public long setBestZulrahTime(long bestZulrahTime) {
		return this.bestZulrahTime = bestZulrahTime;
	}

	public long getBestZulrahTime() {
		return bestZulrahTime;
	}

	public ZulrahLostItems getZulrahLostItems() {
		if (lostItemsZulrah == null) {
			lostItemsZulrah = new ZulrahLostItems(this);
		}
		return lostItemsZulrah;
	}

	public CerberusLostItems getCerberusLostItems() {
		if (lostItemsCerberus == null) {
			lostItemsCerberus = new CerberusLostItems(this);
		}
		return lostItemsCerberus;
	}

	public SkotizoLostItems getSkotizoLostItems() {
		if (lostItemsSkotizo == null) {
			lostItemsSkotizo = new SkotizoLostItems(this);
		}
		return lostItemsSkotizo;
	}

	public int getArcLightCharge() {
		return arcLightCharge;
	}

	public void setArcLightCharge(int chargeArc) {
		this.arcLightCharge = chargeArc;
	}

	public int getToxicBlowpipeCharge() {
		return toxicBlowpipeCharge;
	}

	public void setToxicBlowpipeCharge(int charge) {
		this.toxicBlowpipeCharge = charge;
	}

	public int getToxicBlowpipeAmmo() {
		return toxicBlowpipeAmmo;
	}

	public int getToxicBlowpipeAmmoAmount() {
		return toxicBlowpipeAmmoAmount;
	}

	public void setToxicBlowpipeAmmoAmount(int amount) {
		this.toxicBlowpipeAmmoAmount = amount;
	}

	public void setToxicBlowpipeAmmo(int ammo) {
		this.toxicBlowpipeAmmo = ammo;
	}

	public int getSerpentineHelmCharge() {
		return this.serpentineHelmCharge;
	}

	public void setSerpentineHelmCharge(int charge) {
		this.serpentineHelmCharge = charge;
	}

	public int getTridentCharge() {
		return tridentCharge;
	}

	public void setTridentCharge(int tridentCharge) {
		this.tridentCharge = tridentCharge;
	}

	public int getToxicTridentCharge() {
		return toxicTridentCharge;
	}

	public void setToxicTridentCharge(int toxicTridentCharge) {
		this.toxicTridentCharge = toxicTridentCharge;
	}

	public Fletching getFletching() {
		return fletching;
	}

	public long getLastIncentive() {
		return lastIncentive;
	}

	public void setLastIncentive(long lastIncentive) {
		this.lastIncentive = lastIncentive;
	}

	public boolean receivedIncentiveWarning() {
		return this.incentiveWarning;
	}

	public void updateIncentiveWarning() {
		this.incentiveWarning = true;
	}

	public Tutorial getTutorial() {
		return tutorial;
	}

	public Mode getMode() {
		return mode;
	}

	public Mode setMode(Mode mode) {
		return this.mode = mode;
	}

	public String getRevertOption() {
		return revertOption;
	}

	public void setRevertOption(String revertOption) {
		this.revertOption = revertOption;
	}

	public long getRevertModeDelay() {
		return revertModeDelay;
	}

	public void setRevertModeDelay(long revertModeDelay) {
		this.revertModeDelay = revertModeDelay;
	}

	/**
	 * 
	 * @param skillId
	 * @param amount
	 */
	public void replenishSkill(int skillId, int amount) {
		if (skillId < 0 || skillId > playerLevel.length - 1) {
			return;
		}
		int maximum = getLevelForXP(playerXP[skillId]);
		if (playerLevel[skillId] == maximum) {
			return;
		}
		playerLevel[skillId] += amount;
		if (playerLevel[skillId] > maximum) {
			playerLevel[skillId] = maximum;
		}
		playerAssistant.refreshSkill(skillId);
	}

	public void setArenaPoints(int arenaPoints) {
		this.arenaPoints = arenaPoints;
	}

	public int getArenaPoints() {
		return arenaPoints;
	}

	public void setShayPoints(int shayPoints) {
		this.shayPoints = shayPoints;
	}

	public int getShayPoints() {
		return shayPoints;
	}

	public void setRaidPoints(int raidPoints) {
		this.raidPoints = raidPoints;
	}

	public int getRaidPoints() {
		return raidPoints;
	}

	static {
		playerProps = new Stream(new byte[100]);
	}

	@Override
	public boolean susceptibleTo(HealthStatus status) {
		if (getItems().isWearingItem(12931, playerHat) || getItems().isWearingItem(13199, playerHat)
				|| getItems().isWearingItem(13197, playerHat)) {
			return false;
		}
		return true;
	}

	public int getToxicStaffOfTheDeadCharge() {
		return toxicStaffOfTheDeadCharge;
	}

	public void setToxicStaffOfTheDeadCharge(int toxicStaffOfTheDeadCharge) {
		this.toxicStaffOfTheDeadCharge = toxicStaffOfTheDeadCharge;
	}

	public long getExperienceCounter() {
		return experienceCounter;
	}

	public void setExperienceCounter(long experienceCounter) {
		this.experienceCounter = experienceCounter;
	}

	public int getRunEnergy() {
		return runEnergy;
	}

	public void setRunEnergy(int runEnergy) {
		this.runEnergy = runEnergy;
	}

	public int getLastEnergyRecovery() {
		return lastEnergyRecovery;
	}

	public void setLastEnergyRecovery(int lastEnergyRecovery) {
		this.lastEnergyRecovery = lastEnergyRecovery;
	}

	public Entity getTargeted() {
		return targeted;
	}

	public void setTargeted(Entity targeted) {
		this.targeted = targeted;
	}

	public LootingBag getLootingBag() {
		return lootingBag;
	}

	public PrestigeSkills getPrestige() {
		return prestigeskills;
	}

	public ExpLock getExpLock() {
		return explock;
	}

	public void setLootingBag(LootingBag lootingBag) {
		this.lootingBag = lootingBag;
	}

	public SafeBox getSafeBox() {
		return safeBox;
	}

	public void setSafeBox(SafeBox safeBox) {
		this.safeBox = safeBox;
	}

	public RunePouch getRunePouch() {
		return runePouch;
	}

	public void setRunePouch(RunePouch runePouch) {
		this.runePouch = runePouch;
	}

	public HerbSack getHerbSack() {
		return herbSack;
	}

	public void setHerbSack(HerbSack herbSack) {
		this.herbSack = herbSack;
	}

	public GemBag getGemBag() {
		return gemBag;
	}

	public void setGemBag(GemBag gemBag) {
		this.gemBag = gemBag;
	}

	public AchievementDiary<?> getDiary() {
		return diary;
	}

	public void setDiary(AchievementDiary<?> diary) {
		this.diary = diary;
	}

	public AchievementDiaryManager getDiaryManager() {
		return diaryManager;
	}

	public KalphiteQueen getKq() {
		return kq;
	}

	public void setKq(KalphiteQueen kq) {
		this.kq = kq;
	}

	public QuickPrayers getQuick() {
		return quick;
	}

}
