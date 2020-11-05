package ethos.model.players.skills;

import ethos.Config;
import ethos.Server;
import ethos.event.Event;
import ethos.model.content.SkillcapePerks;
import ethos.model.content.achievement.AchievementType;
import ethos.model.content.achievement.Achievements;
import ethos.model.content.achievement_diary.lumbridge_draynor.LumbridgeDraynorDiaryEntry;
import ethos.model.content.achievement_diary.varrock.VarrockDiaryEntry;
import ethos.model.content.achievement_diary.wilderness.WildernessDiaryEntry;
import ethos.model.content.dailytasks.DailyTasks;
import ethos.model.content.dailytasks.DailyTasks.PossibleTasks;
import ethos.model.items.ItemAssistant;
import ethos.model.players.Boundary;
import ethos.model.players.Player;
import ethos.model.players.mode.ModeType;
import ethos.util.Misc;

public class Smithing {

	private final Player c;
	private final int[] SMELT_BARS = { 2349, 2351, 2355, 2353, 2357, 2359, 2361, 2363 };
	private final int[] SMELT_FRAME = { 2405, 2406, 2407, 2409, 2410, 2411, 2412, 2413 };
	private final int[] BAR_REQS = { 1, 15, 20, 30, 40, 50, 70, 85 };
	private final int[] ORE_1 = { 438, 440, -1, 440, 444, 447, 449, 451 };
	private final int[] ORE_2 = { 436, -1, -1, -1, -1, -1, -1, -1 };
	private final int[] SMELT_EXP = { 6, 13, -1, 18, 23, 30, 38, 50 };
	public static int item;
	public static double xp;
	public static int remove;
	public static int removeamount;
	public static int maketimes;
	private static int exp;
	private static int oreId;
	private int oreId2;
	private int barId;

	public Smithing(final Player c) {
		this.c = c;
	}

	public boolean canSmelt(final int barType) {
		for (int j = 0; j < SMELT_BARS.length; j++) {
			if (barType == SMELT_BARS[j]) {
				// c.sendMessage("" + c.playerLevel + " bar: " + BAR_REQS[j]);
				return c.playerLevel[Player.playerSmithing] >= BAR_REQS[j];
			}
		}
		return false;
	}

	private static void CheckAddy(final Player c, final int level, final int amounttomake, final String type) {
		remove = 2361;
		if (type.equalsIgnoreCase("1357") && level >= 71) { // Axe
			xp = 63;
			item = 1357;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equalsIgnoreCase("823") && level >= 73) {
			xp = 63;
			item = 823;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equalsIgnoreCase("1211") && level >= 70) { // Dagger
			xp = 63;
			item = 1211;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equals("1430") && level >= 72) { // Mace
			xp = 63;
			item = 1430;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equals("1145") && level >= 73) { // Med helm
			xp = 63;
			item = 1145;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equals("9380") && level >= 74) { // Dart tips
			xp = 63;
			item = 9380;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equals("1287") && level >= 74) { // Sword (s)
			xp = 63;
			item = 1287;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equals("19578") && level >= 76) { // Nails
			xp = 63;
			item = 19578;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equals("43") && level >= 75) { // Arrow tips
			xp = 63;
			item = 43;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equals("1331") && level >= 75) { // Scim
			xp = 125;
			item = 1331;
			removeamount = 2;
			maketimes = amounttomake;
			if (Boundary.isIn(c, Boundary.RESOURCE_AREA_BOUNDARY)) {
				c.getDiaryManager().getWildernessDiary().progress(WildernessDiaryEntry.SMITH_ADAMANT_SCIMITAR);
			}
		} else if (type.equals("1301") && level >= 76) { // Longsword
			xp = 125;
			item = 1301;
			removeamount = 2;
			maketimes = amounttomake;
		} else if (type.equals("867") && level >= 77) { // Knives
			xp = 63;
			item = 867;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equals("1161") && level >= 77) { // Full Helm
			xp = 125;
			item = 1161;
			removeamount = 2;
			maketimes = amounttomake;
		} else if (type.equals("1183") && level >= 78) { // Square shield
			xp = 125;
			item = 1183;
			removeamount = 2;
			maketimes = amounttomake;
		} else if (type.equals("1345") && level >= 79) { // Warhammer
			xp = 188;
			item = 1345;
			removeamount = 3;
			maketimes = amounttomake;
		} else if (type.equals("9429") && level >= 76) { // Limbs
			xp = 63;
			item = 9429;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equals("3100") && level >= 83) { // Claws
			xp = 63;
			item = 3100;
			removeamount = 2;
			maketimes = amounttomake;
		} else if (type.equals("9143") && level >= 73) // Dart tips
		{
			xp = 63;
			item = 9143;
			removeamount = 1;
			maketimes = amounttomake;

		} else if (type.equals("1371") && level >= 80) { // Battle axe
			xp = 188;
			item = 1371;
			removeamount = 3;
			maketimes = amounttomake;
		} else if (type.equals("1111") && level >= 81) { // Chain
			xp = 188;
			item = 1111;
			removeamount = 3;
			maketimes = amounttomake;
		} else if (type.equals("1199") && level >= 82) { // Kite
			xp = 188;
			item = 1199;
			removeamount = 3;
			maketimes = amounttomake;
		} else if (type.equals("1317") && level >= 84) { // 2h Sword
			xp = 188;
			item = 1317;
			removeamount = 3;
			maketimes = amounttomake;
		} else if (type.equals("1073") && level >= 86) { // Platelegs
			xp = 188;
			item = 1073;
			removeamount = 3;
			maketimes = amounttomake;
		} else if (type.equals("1091") && level >= 86) { // PlateSkirt
			xp = 188;
			item = 1091;
			removeamount = 3;
			maketimes = amounttomake;
		} else if (type.equals("1123") && level >= 88) { // Platebody
			xp = 313;
			item = 1123;
			removeamount = 5;
			maketimes = amounttomake;
		} else {
			c.sendMessage("You don't have a high enough level to make this Item!");
			return;
		}
		doAction(c, item, remove, removeamount, maketimes, -1, -1, xp);
	}

	private static void CheckBronze(final Player c, final int level, final int amounttomake, final String type) {
		if (type.equalsIgnoreCase("1351") && level >= 1) {
			xp = 13;
			item = 1351;
			remove = 2349;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equalsIgnoreCase("1205") && level >= 1) {
			xp = 13;
			item = 1205;
			remove = 2349;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equals("1422") && level >= 2) {
			xp = 13;
			item = 1422;
			remove = 2349;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equals("1139") && level >= 3) {
			xp = 13;
			item = 1139;
			remove = 2349;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equals("819") && level >= 4) {
			xp = 13;
			item = 819;
			remove = 2349;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equals("9375") && level >= 4) {
			xp = 13;
			item = 9375;
			remove = 2349;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equals("1277") && level >= 4) {
			xp = 13;
			item = 1277;
			remove = 2349;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equals("19570") && level >= 6) {
			xp = 13;
			item = 19570;
			remove = 2349;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equals("39") && level >= 5) {
			xp = 13;
			item = 39;
			remove = 2349;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equals("1321") && level >= 5) {
			xp = 25;
			item = 1321;
			remove = 2349;
			removeamount = 2;
			maketimes = amounttomake;
		} else if (type.equals("1291") && level >= 6) {
			xp = 25;
			item = 1291;
			remove = 2349;
			removeamount = 2;
			maketimes = amounttomake;
		} else if (type.equals("9420") && level >= 6) {
			xp = 13;
			item = 9420;
			remove = 2349;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equals("864") && level >= 7) {
			xp = 13;
			item = 864;
			remove = 2349;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equals("1155") && level >= 7) {
			xp = 25;
			item = 1155;
			remove = 2349;
			removeamount = 2;
			maketimes = amounttomake;
		} else if (type.equals("1173") && level >= 8) {
			xp = 25;
			item = 1173;
			remove = 2349;
			removeamount = 2;
			maketimes = amounttomake;
		} else if (type.equals("1337") && level >= 9) {
			xp = 38;
			item = 1337;
			remove = 2349;
			removeamount = 3;
			maketimes = amounttomake;
		} else if (type.equals("1375") && level >= 10) {
			xp = 38;
			item = 1375;
			remove = 2349;
			removeamount = 3;
			maketimes = amounttomake;
		} else if (type.equals("1103") && level >= 11) {
			xp = 38;
			item = 1103;
			remove = 2349;
			removeamount = 3;
			maketimes = amounttomake;
		} else if (type.equals("1189") && level >= 12) {
			xp = 38;
			item = 1189;
			remove = 2349;
			removeamount = 3;
			maketimes = amounttomake;
		} else if (type.equals("3095") && level >= 13) {
			xp = 25;
			item = 3095;
			remove = 2349;
			removeamount = 2;
			maketimes = amounttomake;
		} else if (type.equals("1307") && level >= 14) {
			xp = 38;
			item = 1307;
			remove = 2349;
			removeamount = 3;
			maketimes = amounttomake;
		} else if (type.equals("1075") && level >= 16) {
			xp = 38;
			item = 1075;
			remove = 2349;
			removeamount = 3;
			maketimes = amounttomake;
		} else if (type.equals("1087") && level >= 16) {
			xp = 38;
			item = 1087;
			remove = 2349;
			removeamount = 3;
			maketimes = amounttomake;
		} else if (type.equals("1117") && level >= 18) {
			xp = 63;
			item = 1117;
			remove = 2349;
			removeamount = 5;
			maketimes = amounttomake;
		} else {
			c.sendMessage("You don't have a high enough level to make this Item!");
			return;
		}
		doAction(c, item, remove, removeamount, maketimes, -1, -1, xp);
	}

	private static void CheckIron(final Player c, final int level, final int amounttomake, final String type) {
		remove = 2351;

		if (type.equalsIgnoreCase("1349") && level >= 16) // Axe
		{
			xp = 25;
			item = 1349;
			removeamount = 1;
			maketimes = amounttomake;
		}

		else if (type.equals("9140") && level >= 19) // Dart tips
		{
			xp = 25;
			item = 9140;
			removeamount = 1;
			maketimes = amounttomake;
		}

		else if (type.equalsIgnoreCase("1203") && level >= 15) // Dagger
		{
			xp = 25;
			item = 1203;
			removeamount = 1;
			maketimes = amounttomake;
		}

		else if (type.equals("1420") && level >= 17) // Mace
		{
			xp = 25;
			item = 1420;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equals("1137") && level >= 18) // Med helm
		{
			xp = 25;
			item = 1137;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equals("9377") && level >= 19) // Bolt tips
		{
			xp = 25;
			item = 9377;
			removeamount = 1;
			maketimes = amounttomake;
		}

		else if (type.equals("1279") && level >= 19) // Sword (s)
		{
			xp = 25;
			item = 1279;
			removeamount = 1;
			maketimes = amounttomake;
		}

		else if (type.equals("19572") && level >= 21) // Nails
		{
			xp = 25;
			item = 19572;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equals("820") && level >= 19) // Dart tips
		{
			xp = 25;
			item = 820;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equals("40") && level >= 20) // Arrow tips
		{
			xp = 25;
			item = 40;
			removeamount = 1;
			maketimes = amounttomake;
		}

		else if (type.equals("1323") && level >= 20) // Scim
		{
			xp = 50;
			item = 1323;
			removeamount = 2;
			maketimes = amounttomake;
		}

		else if (type.equals("1293") && level >= 21) // Longsword
		{
			xp = 50;
			item = 1293;
			removeamount = 2;
			maketimes = amounttomake;
		}

		else if (type.equals("863") && level >= 22) // Knives
		{
			xp = 25;
			item = 863;
			removeamount = 1;
			maketimes = amounttomake;
		}

		else if (type.equals("1153") && level >= 22) // Full Helm
		{
			xp = 50;
			item = 1153;
			removeamount = 2;
			maketimes = amounttomake;
		}

		else if (type.equals("1175") && level >= 23) // Square shield
		{
			xp = 50;
			item = 1175;
			removeamount = 2;
			maketimes = amounttomake;
		} else if (type.equals("9423") && level >= 23) // Crossbow limbs
		{
			xp = 25;
			item = 9423;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equals("1335") && level >= 24) // Warhammer
		{
			xp = 38;
			item = 1335;
			removeamount = 3;
			maketimes = amounttomake;
		}

		else if (type.equals("1363") && level >= 25) // Battle axe
		{
			xp = 75;
			item = 1363;
			removeamount = 3;
			maketimes = amounttomake;
		} else if (type.equals("1101") && level >= 26) // Chain
		{
			xp = 75;
			item = 1101;
			removeamount = 3;
			maketimes = amounttomake;
		} else if (type.equals("1191") && level >= 27) // Kite
		{
			xp = 75;
			item = 1191;
			removeamount = 3;
			maketimes = amounttomake;
		} else if (type.equals("3096") && level >= 28) // claws
		{
			xp = 50;
			item = 3096;
			removeamount = 2;
			maketimes = amounttomake;
		} else if (type.equals("1309") && level >= 29) // 2h Sword
		{
			xp = 75;
			item = 1309;
			removeamount = 3;
			maketimes = amounttomake;
		} else if (type.equals("1067") && level >= 31) // Platelegs
		{
			xp = 75;
			item = 1067;
			removeamount = 3;
			maketimes = amounttomake;
		} else if (type.equals("1081") && level >= 31) // PlateSkirt
		{
			xp = 75;
			item = 1081;
			removeamount = 3;
			maketimes = amounttomake;
		} else if (type.equals("1115") && level >= 33) // Platebody
		{
			xp = 125;
			item = 1115;
			removeamount = 5;
			maketimes = amounttomake;
			DailyTasks.increase(c, PossibleTasks.IRON_PLATEBODYS);
		} else {
			c.sendMessage("You don't have a high enough level to make this Item!");
			return;
		}

		doAction(c, item, remove, removeamount, maketimes, -1, -1, xp);

	}

	private static void CheckMith(final Player c, final int level, final int amounttomake, final String type) {
		remove = 2359;

		if (type.equalsIgnoreCase("1355") && level >= 51) // Axe
		{
			xp = 50;
			item = 1355;
			removeamount = 1;
			maketimes = amounttomake;
			if (Boundary.isIn(c, Boundary.RESOURCE_AREA_BOUNDARY)) {
				c.getDiaryManager().getWildernessDiary().progress(WildernessDiaryEntry.SMITH_MITHRIL_AXE);
			}
		}

		else if (type.equals("9142") && level >= 54) // Dart tips
		{
			xp = 50;
			item = 9142;
			removeamount = 1;
			maketimes = amounttomake;
		}

		else if (type.equalsIgnoreCase("1209") && level >= 50) // Dagger
		{
			xp = 50;
			item = 1209;
			removeamount = 1;
			maketimes = amounttomake;
		}

		else if (type.equals("1428") && level >= 52) // Mace
		{
			xp = 50;
			item = 1428;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equals("1143") && level >= 53) // Med helm
		{
			xp = 50;
			item = 1143;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equals("9379") && level >= 54) // Dart tips
		{
			xp = 50;
			item = 9379;
			removeamount = 1;
			maketimes = amounttomake;
		}

		else if (type.equals("1285") && level >= 54) // Sword (s)
		{
			xp = 50;
			item = 1285;
			removeamount = 1;
			maketimes = amounttomake;
		}

		else if (type.equals("19576") && level >= 56) // Nails
		{
			xp = 50;
			item = 19576;
			removeamount = 1;
			maketimes = amounttomake;
		}

		else if (type.equals("42") && level >= 55) // Arrow tips
		{
			xp = 50;
			item = 42;
			removeamount = 1;
			maketimes = amounttomake;
		}

		else if (type.equals("1329") && level >= 55) // Scim
		{
			xp = 100;
			item = 1329;
			removeamount = 2;
			maketimes = amounttomake;
		}

		else if (type.equals("1299") && level >= 56) // Longsword
		{
			xp = 100;
			item = 1299;
			removeamount = 2;
			maketimes = amounttomake;
		}

		else if (type.equals("866") && level >= 57) // Knives
		{
			xp = 50;
			item = 866;
			removeamount = 1;
			maketimes = amounttomake;
		}

		else if (type.equals("1159") && level >= 57) // Full Helm
		{
			xp = 100;
			item = 1159;
			removeamount = 2;
			maketimes = amounttomake;
		}

		else if (type.equals("1181") && level >= 58) // Square shield
		{
			xp = 100;
			item = 1181;
			removeamount = 2;
			maketimes = amounttomake;
		}

		else if (type.equals("1343") && level >= 59) // Warhammer
		{
			xp = 150;
			item = 1343;
			removeamount = 3;
			maketimes = amounttomake;
		}

		else if (type.equals("1369") && level >= 60) // Battle axe
		{
			xp = 150;
			item = 1369;
			removeamount = 3;
			maketimes = amounttomake;
		} else if (type.equals("1109") && level >= 61) // Chain
		{
			xp = 150;
			item = 1109;
			removeamount = 3;
			maketimes = amounttomake;
		}

		else if (type.equals("1197") && level >= 62) // Kite
		{
			xp = 150;
			item = 1197;
			removeamount = 3;
			maketimes = amounttomake;
		} else if (type.equals("1315") && level >= 64) // 2h Sword
		{
			xp = 150;
			item = 1315;
			removeamount = 3;
			maketimes = amounttomake;
		} else if (type.equals("1071") && level >= 66) // Platelegs
		{
			xp = 150;
			item = 1071;
			removeamount = 3;
			maketimes = amounttomake;
		} else if (type.equals("1085") && level >= 66) // PlateSkirt
		{
			xp = 150;
			item = 1085;
			removeamount = 3;
			maketimes = amounttomake;
		} else if (type.equals("1121") && level >= 68) // Platebody
		{
			xp = 250;
			item = 1121;
			removeamount = 5;
			maketimes = amounttomake;
		} else if (type.equals("9427") && level >= 54) // Limbs
		{
			xp = 50;
			item = 9427;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equals("3099") && level >= 54) // Limbs
			{
				xp = 50;
				item = 3099;
				removeamount = 1;
				maketimes = amounttomake;
		} else if (type.equals("822") && level >= 54) // Platebody
		{
			xp = 50;
			item = 822;
			removeamount = 1;
			maketimes = amounttomake;
		} else {
			c.sendMessage("You don't have a high enough level to make this Item!");
			return;
		}

		doAction(c, item, remove, removeamount, maketimes, -1, -1, xp);

	}

	private static void CheckRune(final Player c, final int level, final int amounttomake, final String type) {
		remove = 2363;

		if (type.equalsIgnoreCase("1359") && level >= 86) // Axe
		{
			xp = 75;
			item = 1359;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equalsIgnoreCase("3101") && level >= 86) // claws
		{
			xp = 75;
			item = 3101;
			removeamount = 2;
			maketimes = amounttomake;
		} else if (type.equalsIgnoreCase("824") && level >= 89) {
			xp = 75;
			item = 824;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equals("9381") && level >= 89) // Dart tips
		{

			xp = 75;
			item = 9381;
			removeamount = 1;
			maketimes = amounttomake;
	} else if (type.equals("9431") && level >= 89) // Limbs
	{

		xp = 75;
		item = 9431;
		removeamount = 1;
		maketimes = amounttomake;
	} else if (type.equals("3121") && level >= 89) // Claws
	{

		xp = 75;
		item = 3121;
		removeamount = 2;
		maketimes = amounttomake;
	}

		else if (type.equalsIgnoreCase("1213") && level >= 85) // Dagger
		{
			xp = 75;
			item = 1213;
			removeamount = 1;
			maketimes = amounttomake;
		}

		else if (type.equals("1432") && level >= 87) // Mace
		{
			xp = 75;
			item = 1432;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equals("1147") && level >= 88) // Med helm
		{
			xp = 75;
			item = 1147;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equals("9381") && level >= 89) // Dart tips
		{

			xp = 75;
			item = 9381;
			removeamount = 1;
			maketimes = amounttomake;
		}

		else if (type.equals("1289") && level >= 89) // Sword (s)
		{
			xp = 75;
			item = 1289;
			removeamount = 1;
			maketimes = amounttomake;
		}

		else if (type.equals("19580") && level >= 91) // Nails
		{
			xp = 75;
			item = 19580;
			removeamount = 1;
			maketimes = amounttomake;
		}

		else if (type.equals("44") && level >= 90) // Arrow tips
		{
			xp = 75;
			item = 44;
			removeamount = 1;
			maketimes = amounttomake;
		}

		else if (type.equals("1333") && level >= 90) // Scim
		{
			xp = 150;
			item = 1333;
			removeamount = 2;
			maketimes = amounttomake;
			if (Boundary.isIn(c, Boundary.RESOURCE_AREA_BOUNDARY)) {
				c.getDiaryManager().getWildernessDiary().progress(WildernessDiaryEntry.SMITH_RUNE_SCIM_WILD);
			}
		}

		else if (type.equals("1303") && level >= 91) // Longsword
		{
			xp = 150;
			item = 1303;
			removeamount = 2;
			maketimes = amounttomake;
		}

		else if (type.equals("868") && level >= 92) // Knives
		{
			xp = 75;
			item = 868;
			removeamount = 1;
			maketimes = amounttomake;
		}

		else if (type.equals("1163") && level >= 92) // Full Helm
		{
			xp = 150;
			item = 1163;
			removeamount = 2;
			maketimes = amounttomake;
		}

		else if (type.equals("1185") && level >= 93) // Square shield
		{
			xp = 150;
			item = 1185;
			removeamount = 2;
			maketimes = amounttomake;
		}

		else if (type.equals("1347") && level >= 94) // Warhammer
		{
			xp = 225;
			item = 1347;
			removeamount = 3;
			maketimes = amounttomake;
		}

		else if (type.equals("1373") && level >= 95) // Battle axe
		{
			xp = 225;
			item = 1373;
			removeamount = 3;
			maketimes = amounttomake;
		} else if (type.equals("1113") && level >= 96) // Chain
		{
			xp = 225;
			item = 1113;
			removeamount = 3;
			maketimes = amounttomake;
		}

		else if (type.equals("1201") && level >= 97) // Kite
		{
			xp = 225;
			item = 1201;
			removeamount = 3;
			maketimes = amounttomake;
		} else if (type.equals("1319") && level >= 99) // 2h Sword
		{
			xp = 225;
			item = 1319;
			removeamount = 3;
			maketimes = amounttomake;
		} else if (type.equals("1079") && level >= 99) // Platelegs
		{
			xp = 225;
			item = 1079;
			removeamount = 3;
			maketimes = amounttomake;
		} else if (type.equals("1093") && level >= 99) // PlateSkirt
		{
			xp = 225;
			item = 1093;
			removeamount = 3;
			maketimes = amounttomake;
		} else if (type.equals("1127") && level >= 99) // Platebody
		{
			xp = 375;
			item = 1127;
			removeamount = 5;
			maketimes = amounttomake;
			if (Boundary.isIn(c, Boundary.DRAYNOR_DUNGEON_BOUNDARY)) {
				c.getDiaryManager().getLumbridgeDraynorDiary().progress(LumbridgeDraynorDiaryEntry.RUNE_PLATE_LUM);
			}
		} else {
			c.sendMessage("You don't have a high enough level to make this Item!");
			return;
		}

		doAction(c, item, remove, removeamount, maketimes, -1, -1, xp);

	}

	private static void CheckSteel(final Player c, final int level, final int amounttomake, final String type) {
		remove = 2353;

		if (type.equalsIgnoreCase("1353") && level >= 31) // Axe
		{
			xp = 38;
			item = 1353;
			removeamount = 1;
			maketimes = amounttomake;
		}

		else if (type.equals("9141") && level >= 34) // Bolts unf
		{
			xp = 38;
			item = 9141;
			removeamount = 1;
			maketimes = amounttomake;
		}

		else if (type.equals("2370") && level >= 34) // Studs
		{
			xp = 38;
			item = 2370;
			removeamount = 1;
			maketimes = amounttomake;
		}

		else if (type.equals("821") && level >= 34) // Dart tips
		{
			xp = 38;
			item = 821;
			removeamount = 1;
			maketimes = amounttomake;
		}

		else if (type.equals("9425") && level >= 34) // Dart tips
		{
			xp = 38;
			item = 9425;
			removeamount = 1;
			maketimes = amounttomake;
		}

		else if (type.equalsIgnoreCase("1207") && level >= 30) // Dagger
		{
			xp = 38;
			item = 1207;
			removeamount = 1;
			maketimes = amounttomake;
		}

		else if (type.equals("1424") && level >= 32) // Mace
		{
			xp = 38;
			item = 1424;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equals("1141") && level >= 33) // Med helm
		{
			xp = 38;
			item = 1141;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equals("9378") && level >= 34) // Dart tips
		{
			xp = 38;
			item = 9378;
			removeamount = 1;
			maketimes = amounttomake;
		}

		else if (type.equals("1281") && level >= 34) // Sword (s)
		{
			xp = 38;
			item = 1281;
			removeamount = 1;
			maketimes = amounttomake;
		}

		else if (type.equals("19574") && level >= 36) // Nails
		{
			xp = 38;
			item = 19574;
			removeamount = 1;
			maketimes = amounttomake;
		}

		else if (type.equals("41") && level >= 35) // Arrow tips
		{
			xp = 38;
			item = 41;
			removeamount = 1;
			maketimes = amounttomake;
		}

		else if (type.equals("1325") && level >= 35) // Scim
		{
			xp = 75;
			item = 1325;
			removeamount = 2;
			maketimes = amounttomake;
		}

		else if (type.equals("1295") && level >= 36) // Longsword
		{
			xp = 75;
			item = 1295;
			removeamount = 2;
			maketimes = amounttomake;
		}

		else if (type.equals("865") && level >= 37) // Knives
		{
			xp = 38;
			item = 865;
			removeamount = 1;
			maketimes = amounttomake;
			if (Boundary.isIn(c, Boundary.VARROCK_BOUNDARY)) {
				c.getDiaryManager().getVarrockDiary().progress(VarrockDiaryEntry.SMITH_STEEL_KNIFES);
			}
		}

		else if (type.equals("1157") && level >= 37) // Full Helm
		{
			xp = 75;
			item = 1157;
			removeamount = 2;
			maketimes = amounttomake;
		}

		else if (type.equals("1177") && level >= 38) // Square shield
		{
			xp = 75;
			item = 1177;
			removeamount = 2;
			maketimes = amounttomake;
		}

		else if (type.equals("1339") && level >= 39) // Warhammer
		{
			xp = 113;
			item = 1339;
			removeamount = 3;
			maketimes = amounttomake;
		}

		else if (type.equals("1365") && level >= 40) // Battle axe
		{
			xp = 113;
			item = 1365;
			removeamount = 3;
			maketimes = amounttomake;
		} else if (type.equals("1105") && level >= 41) // Chain
		{
			xp = 113;
			item = 1105;
			removeamount = 3;
			maketimes = amounttomake;
		}

		else if (type.equals("1193") && level >= 42) // Kite
		{
			xp = 113;
			item = 1193;
			removeamount = 3;
			maketimes = amounttomake;
		} else if (type.equals("1311") && level >= 44) // 2h Sword
		{
			xp = 113;
			item = 1311;
			removeamount = 3;
			maketimes = amounttomake;
		} else if (type.equals("1069") && level >= 46) // Platelegs
		{
			xp = 113;
			item = 1069;
			removeamount = 3;
			maketimes = amounttomake;
		} else if (type.equals("1083") && level >= 46) // PlateSkirt
		{
			xp = 113;
			item = 1083;
			removeamount = 3;
			maketimes = amounttomake;
		} else if (type.equals("1119") && level >= 48) // Platebody
		{
			xp = 188;
			item = 1119;
			removeamount = 5;
			maketimes = amounttomake;
		} else {
			c.sendMessage("You don't have a high enough level to make this Item!");
			return;
		}

		doAction(c, item, remove, removeamount, maketimes, -1, -1, xp);

	}

	public static void doAction(final Player c, final int toadd, final int toremove, final int toremove2, final int timestomake, final int NOTUSED, final int NOTUSED2,
			final double xp) {
		c.event = 3;
		c.makeTimes = timestomake;
		c.getPA().closeAllWindows();

		if (!c.getItems().playerHasItem(toremove, toremove2)) {
			c.sendMessage("You don't have enough bars to make this item!");
			return;
		}
		if (!c.getItems().playerHasItem(2347, 1)) {
			c.sendMessage("You don't have a hammer with you!");
			return;
		}
		c.startAnimation(898);
		if (c.playerSkilling[Player.playerSmithing]) {
			return;
		}
		c.getSkilling().stop();
		c.getSkilling().setSkill(Skill.SMITHING);
		c.playerSkilling[Player.playerSmithing] = true;
		Server.getEventHandler().submit(new Event<Player>("skilling", c, 3) {
			@Override
			public void execute() {
				if (!c.playerSkilling[Player.playerSmithing]) {
					stop();
					return;
				}
				if (!c.getItems().playerHasItem(toremove, toremove2)) {
					c.sendMessage("You have ran out of supplies");
					stop();
					return;
				}
				if (!c.getItems().playerHasItem(2347, 1)) {
					c.sendMessage("You don't have a hammer with you!");
					stop();
					return;
				}
				if (c.makeTimes == 0) {
					stop();
					return;
				}
				
				/**
				 * Chance of saving a bar while wearing herblore or max cape
				 */
				if (SkillcapePerks.SMITHING.isWearing(c) || SkillcapePerks.isWearingMaxCape(c)) {
					if (Misc.random(4) == 2) {
						c.sendMessage("You manage to save a bar.");
					} else {
						c.getItems().deleteItem2(toremove, toremove2);
					}
				} else {
					c.getItems().deleteItem2(toremove, toremove2);
				}
				
				if (ItemAssistant.getItemName(toadd).contains("bolt")) {
					c.getItems().addItem(toadd, 10);
				} else {
					if (ItemAssistant.getItemName(toadd).contains("head")) {
						c.getItems().addItem(toadd, 5);
					} else {
						if (ItemAssistant.getItemName(toadd).contains("arrow")) {
							c.getItems().addItem(toadd, 15);
						} else {
							if (ItemAssistant.getItemName(toadd).contains("dart")) {
								c.getItems().addItem(toadd, 10);
							} else {
								if (ItemAssistant.getItemName(toadd).contains("knife")) {
									c.getItems().addItem(toadd, 5);
								} else {
									if (ItemAssistant.getItemName(toadd).contains("cannon")) {
										c.getItems().addItem(toadd, 4);
									} else {
										c.getItems().addItem(toadd, 1);
									}
								}
							}
						}
					}
				}
				if (toadd == 868) {
					if (Boundary.isIn(c, Boundary.VARROCK_BOUNDARY)) {
						c.getDiaryManager().getVarrockDiary().progress(VarrockDiaryEntry.SMITH_RUNE_KNIFES);
					}
				}
				c.getPA().addSkillXP((int) (xp * (c.getMode().getType().equals(ModeType.OSRS) ? 1 : Config.SMITHING_EXPERIENCE)), 13, true);
				c.getPA().refreshSkill(13);
				Achievements.increase(c, AchievementType.SMITH, 1);
				c.sendMessage("You make a " + ItemAssistant.getItemName(toadd) + ".");
				if (c.makeTimes != 1) {
					c.startAnimation(898);
				}
				c.makeTimes--;
			}

			@Override
			public void stop() {
				super.stop();
				if (!c.disconnected&&c.getSession()!=null) {
					c.playerSkilling[Player.playerSmithing] = false;
					c.makeTimes = 0;
				}
			}
		});
	}

	public int getExp(final int barType) {
		for (int j = 0; j < SMELT_BARS.length; j++) {
			if (barType == SMELT_BARS[j]) {
				return SMELT_EXP[j];
			}
		}
		return 0;
	}

	public int getOre(final int barType) {
		for (int j = 0; j < SMELT_BARS.length; j++) {
			if (barType == SMELT_BARS[j]) {
				// c.sendMessage("" + ORE_1[j]);
				return ORE_1[j];
			}
		}
		return 0;
	}

	public int getOre2(final int barType) {
		for (int j = 0; j < SMELT_BARS.length; j++) {
			if (barType == SMELT_BARS[j]) {
				// c.sendMessage("" + ORE_2[j]);
				return ORE_2[j];
			}
		}
		return 0;
	}

	public boolean hasOres(final int barType) {
		if (getOre2(barType) > 0) {
			return c.getItems().playerHasItem(getOre(barType)) && c.getItems().playerHasItem(getOre2(barType));
		} else {
			return c.getItems().playerHasItem(getOre(barType));
		}
	}

	public static void readInput(final int level, final String type, final Player c, final int amounttomake) {
		if (c.debugMessage)
			c.sendMessage("[SMITH] Reading Input. Level: " + level + " Type: " + type + " AmountToMake: " + amounttomake);
		if (ItemAssistant.getItemName(Integer.parseInt(type)).contains("Bronze")) {
			CheckBronze(c, level, amounttomake, type);
		} else {
			if (ItemAssistant.getItemName(Integer.parseInt(type)).contains("Iron")) {
				CheckIron(c, level, amounttomake, type);
			} else {
				if (ItemAssistant.getItemName(Integer.parseInt(type)).contains("Steel")) {
					CheckSteel(c, level, amounttomake, type);
				} else {
					if (ItemAssistant.getItemName(Integer.parseInt(type)).contains("Mith")) {
						CheckMith(c, level, amounttomake, type);
					} else {
						if (ItemAssistant.getItemName(Integer.parseInt(type)).contains("Adam") || ItemAssistant.getItemName(Integer.parseInt(type)).contains("Addy")) {
							CheckAddy(c, level, amounttomake, type);
						} else {
							if (ItemAssistant.getItemName(Integer.parseInt(type)).contains("Rune") || ItemAssistant.getItemName(Integer.parseInt(type)).contains("Runite")) {
								CheckRune(c, level, amounttomake, type);
							}
						}
					}
				}
			}
		}
	}

	public void sendSmelting() {
		for (int j = 0; j < SMELT_FRAME.length; j++) {
			c.getPA().sendFrame246(SMELT_FRAME[j], 150, SMELT_BARS[j]);
		}
		c.getPA().sendFrame164(2400);
		c.smeltInterface = true;
	}

	public void smelt(final int barType) {
		if (c.smeltAmount > 0) {
			c.getPA().closeAllWindows();
			if (hasOres(barType)) {
				c.getItems().deleteItem(oreId, c.getItems().getItemSlot(oreId), 1);
				if (oreId2 > 0) {
					c.getItems().deleteItem(oreId2, c.getItems().getItemSlot(oreId2), 1);
				}
				c.getItems().addItem(barId, 1);
				c.getPA().addSkillXP(exp * (c.getMode().getType().equals(ModeType.OSRS) ? 1 : Config.SMITHING_EXPERIENCE), Player.playerSmithing, true);
				c.getPA().refreshSkill(Player.playerSmithing);
				c.smeltAmount--;
				c.smeltTimer = 1;
			} else {
				c.sendMessage("You do not have the required ores to smelt this.");
				c.getPA().removeAllWindows();
			}
		} else {
			c.getPA().resetVariables();
		}
	}

	public void startSmelting(final int barType) {
		if (canSmelt(barType)) {
			// c.sendMessage("We canSmelt");
			if (hasOres(barType)) {
				// c.sendMessage("We have ores");
				exp = getExp(barType);
				oreId = getOre(barType);
				oreId2 = getOre2(barType);
				barId = barType;
				c.smeltAmount = c.getItems().getItemAmount(getOre(barType));
				smelt(barType);
			} else {
				c.sendMessage("You do not have the required ores to smelt this.");
				c.getPA().resetVariables();
			}
		} else {
			c.sendMessage("You must have a higher smithing level to smith this.");
			c.getPA().resetVariables();
		}
	}

}
