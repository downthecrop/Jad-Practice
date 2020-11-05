package com.client.definitions;

import com.client.Class36;
import com.client.Stream;
import com.client.StreamLoader;

public final class AnimationDefinition {

	public static void unpackConfig(StreamLoader streamLoader) {
		Stream stream = new Stream(streamLoader.getDataForName("seq.dat"));
		int length = stream.readUnsignedWord();
		if (anims == null)
			anims = new AnimationDefinition[length];
		for (int j = 0; j < length; j++) {
			if (anims[j] == null)
				anims[j] = new AnimationDefinition();
				anims[j].readValues(stream);
			if (j == 719) {
				anims[j].anInt361 = 28232;
				anims[j].anInt359 = 6;
				anims[j].anInt360 = 0;
				anims[j].anInt365 = 1;
				anims[j].anInt356 = -1;
				anims[j].aBoolean358 = false;
				anims[j].anInt359 = 5;
				anims[j].anInt362 = 99;
				anims[j].anInt363 = -1;
				anims[j].anInt364 = -1;
			}
			switch (j) {

			case 7156:
				anims[j] = new AnimationDefinition();
				anims[j].anIntArray355 = new int[] { 3, 3, 3, 3, 3, 3, 3, 4, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 3, 3, 4,
						4, 4, 4, 3, 3, 3, };
				anims[j].anIntArray353 = new int[] { 158990394, 158990411, 158990398, 158990429, 158990418, 158990393,
						158990407, 158990402, 158990397, 158990428, 158990409, 158990401, 158990425, 158990406,
						158990417, 158990403, 158990420, 158990395, 158990399, 158990435, 158990414, 158990405,
						158990432, 158990410, 158990415, 158990426, 158990430, 158990394, };
				anims[j].anInt352 = 28;
				anims[j].aBoolean358 = false;
				anims[j].anInt359 = 10;
				anims[j].anInt362 = 99;
				anims[j].anInt365 = 2;
				break;

			case 7155:
				anims[j] = new AnimationDefinition();
				anims[j].anIntArray355 = new int[] { 18, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 1, 1, 1,
						1, 3, 3, 1, 1, 1, 1, 1, 1, 1, 6, 4, 1, 1, 1, 1, 1, 1, 1, 1, };
				anims[j].anIntArray353 = new int[] { 168558682, 168558693, 168558686, 168558670, 168558665, 168558702,
						168558703, 168558669, 168558690, 168558705, 168558692, 168558694, 168558671, 168558679,
						168558672, 168558667, 168558675, 168558683, 168558706, 168558681, 168558704, 168558673,
						168558698, 168558680, 168558695, 168558677, 168558700, 168558701, 168558699, 168558668,
						168558689, 168558684, 168558678, 168558696, 168558688, 168558666, 168558687, 168558674,
						168558691, 168558676, 168558685, 168558697, };
				anims[j].anInt352 = 42;
				anims[j].aBoolean358 = false;
				anims[j].anInt359 = 5;
				anims[j].anInt362 = 99;
				anims[j].anInt365 = 2;
				break;

			case 7154:
				// fileID = 1845
				anims[j].anIntArray355 = new int[] { 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5,
						5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5,
						5, 5, 5, 5, 5, 5, 5, 5, 5, 5, };
				anims[j].anIntArray353 = new int[] { 120913948, 120913967, 120913935, 120913942, 120913982, 120913929,
						120913947, 120913937, 120913951, 120913932, 120913966, 120913931, 120913960, 120913924,
						120913973, 120913976, 120913972, 120913936, 120913920, 120913981, 120913921, 120913945,
						120913964, 120913968, 120913943, 120913944, 120913954, 120913970, 120913939, 120913949,
						120913963, 120913974, 120913983, 120913938, 120913926, 120913956, 120913940, 120913923,
						120913958, 120913965, 120913946, 120913971, 120913953, 120913969, 120913977, 120913980,
						120913930, 120913975, 120913978, 120913928, 120913961, 120913952, 120913955, 120913950,
						120913925, 120913959, 120913957, 120913962, 120913922, 120913934, 120913927, 120913979,
						120913941, 120913933, };
				anims[j].anInt352 = 64;
				anims[j].aBoolean358 = false;
				anims[j].anInt359 = 5;
				anims[j].anInt362 = 99;
				anims[j].anInt365 = 2;
				anims[j].anInt356 = 64;
				break;

			case 1134:
				// fileID = 464
				anims[j].anInt352 = 3;
				anims[j].anIntArray353 = new int[] { 30408704, 30408708, 30408707, };
				anims[j].anIntArray355 = new int[] { 14, 14, 12, };
				break;

			case 1674:
				// fileID = 472
				anims[j].anInt352 = 4;
				anims[j].anIntArray353 = new int[] { 30932992, 30932995, 30932994, 30932993, };
				anims[j].anIntArray355 = new int[] { 9, 9, 9, 9, };
				break;

			case 1675:
				// fileID = 470
				anims[j].anInt352 = 4;
				anims[j].anIntArray353 = new int[] { 30801923, 30801922, 30801920, 30801921, };
				anims[j].anIntArray355 = new int[] { 10, 9, 11, 10, };
				break;

			case 1676:
				// fileID = 474
				anims[j].anInt352 = 13;
				anims[j].anIntArray353 = new int[] { 31064066, 31064073, 31064070, 31064075, 31064071, 31064068,
						31064074, 31064065, 31064072, 31064064, 31064069, 31064067, 31064076, };
				anims[j].anIntArray355 = new int[] { 9, 8, 5, 7, 7, 12, 4, 17, 12, 9, 10, 16, 11, };
				anims[j].anInt356 = 1;
				anims[j].anInt359 = 10;
				break;

			case 1677:
				// fileID = 466
				anims[j].anInt352 = 8;
				anims[j].anIntArray353 = new int[] { 30539776, 30539782, 30539783, 30539779, 30539778, 30539781,
						30539780, 30539777, };
				anims[j].anIntArray355 = new int[] { 8, 7, 7, 7, 7, 7, 7, 10, };
				break;

			case 1678:
				// fileID = 471
				anims[j].anInt352 = 8;
				anims[j].anIntArray353 = new int[] { 30867457, 30867459, 30867458, 30867456, 30867463, 30867460,
						30867461, 30867462, };
				anims[j].anIntArray355 = new int[] { 11, 16, 12, 14, 18, 17, 17, 15, };
				break;

			case 1679:
				// fileID = 465
				anims[j].anInt352 = 7;
				anims[j].anIntArray353 = new int[] { 30474245, 30474240, 30474246, 30474243, 30474241, 30474242,
						30474244, };
				anims[j].anIntArray355 = new int[] { 7, 8, 17, 8, 6, 6, 8, };
				anims[j].anInt359 = 6;
				break;

			case 1682:
				// fileID = 462
				anims[j].anInt352 = 5;
				anims[j].anIntArray353 = new int[] { 30277634, 30277635, 30277633, 30277636, 30277632, };
				anims[j].anIntArray355 = new int[] { 8, 8, 11, 8, 8, };
				anims[j].anInt359 = 6;
				break;

			case 1683:
				// fileID = 467
				anims[j].anInt352 = 6;
				anims[j].anIntArray353 = new int[] { 30605316, 30605313, 30605317, 30605315, 30605314, 30605312, };
				anims[j].anIntArray355 = new int[] { 8, 4, 6, 7, 9, 6, };
				anims[j].anInt359 = 6;
				break;

			case 1684:
				// fileID = 463
				anims[j].anInt352 = 9;
				anims[j].anIntArray353 = new int[] { 30343170, 30343168, 30343171, 30343175, 30343173, 30343176,
						30343177, 30343174, 30343172, };
				anims[j].anIntArray355 = new int[] { 10, 10, 10, 10, 10, 10, 10, 10, 10, };
				break;

			case 1685:
				// fileID = 469
				anims[j].anInt352 = 4;
				anims[j].anIntArray353 = new int[] { 30736384, 30736385, 30736386, 30736387, };
				anims[j].anIntArray355 = new int[] { 5, 5, 5, 5, };
				break;

			case 1687:
				// fileID = 461
				anims[j].anInt352 = 5;
				anims[j].anIntArray353 = new int[] { 30212100, 30212097, 30212099, 30212098, 30212096, };
				anims[j].anIntArray355 = new int[] { 5, 8, 9, 9, 9, };
				break;

			case 1688:
				// fileID = 475
				anims[j].anInt352 = 8;
				anims[j].anIntArray353 = new int[] { 31129606, 31129605, 31129601, 31129607, 31129604, 31129602,
						31129603, 31129600, };
				anims[j].anIntArray355 = new int[] { 5, 5, 5, 5, 5, 5, 5, 5, };
				break;

			case 1689:
				// fileID = 476
				anims[j].anInt352 = 14;
				anims[j].anIntArray353 = new int[] { 31195148, 31195150, 31195144, 31195139, 31195136, 31195143,
						31195145, 31195147, 31195137, 31195142, 31195151, 31195140, 31195146, 31195138, };
				anims[j].anIntArray355 = new int[] { 5, 5, 5, 5, 4, 4, 4, 3, 8, 8, 8, 8, 12, 12, };
				break;

			case 5061:
				anims[j].anInt352 = 13;
				anims[j].anIntArray353 = new int[] { 19267601, 19267602, 19267603, 19267604, 19267605, 19267606,
						19267607, 19267606, 19267605, 19267604, 19267603, 19267602, 19267601, };
				anims[j].anIntArray355 = new int[] { 4, 3, 3, 4, 10, 10, 15, 10, 10, 4, 3, 3, 4, };
				// anims[j].anIntArray357 = new int[] { 1, 2, 9, 11, 13, 15, 17,
				// 19, 37, 39, 41, 43, 45, 164, 166, 168, 170, 172, 174, 176,
				// 178, 180, 182, 183, 185, 191, 192, 9999999, };
				anims[j].anInt359 = 6;
				anims[j].anInt360 = 0;
				anims[j].anInt361 = 13438;
				anims[j].anInt365 = 1;
				anims[j].anInt356 = -1;
				anims[j].aBoolean358 = false;
				anims[j].anInt359 = 5;
				anims[j].anInt362 = 99;
				anims[j].anInt363 = -1;
				anims[j].anInt364 = -1;
				break;

			case 5460:
				anims[j].anInt352 = 19;
				anims[j].anIntArray353 = new int[] { 93650945, 93650955, 93650956, 93650957, 93650958, 93650959,
						93650960, 93650961, 93650962, 93650944, 93650946, 93650947, 93650948, 93650949, 93650950,
						93650951, 93650952, 93650953, 93650954, };
				anims[j].anIntArray355 = new int[] { 15, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, };
				break;

			case 5461:
				anims[j].anInt352 = 8;
				anims[j].anIntArray353 = new int[] { 93716480, 93716481, 93716482, 93716483, 93716484, 93716485,
						93716486, 93716487, };
				anims[j].anIntArray355 = new int[] { 4, 4, 4, 4, 4, 4, 4, 4, };
				break;

			case 5462:
				anims[j].anInt352 = 8;
				anims[j].anIntArray353 = new int[] { 93782016, 93782017, 93782018, 93782019, 93782020, 93782021,
						93782022, 93782023, };
				anims[j].anIntArray355 = new int[] { 5, 5, 5, 5, 5, 5, 5, 5, };
				break;

			case 2406:
				anims[j].anIntArray353 = new int[] { 12058690, 12058638, 12058631, 12058683, 12058655, 12058660,
						12058710, 12058634, 12058680, 12058706, };
				anims[j].anIntArray355 = new int[] { 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, };
				anims[j].anInt352 = 10;
				break;

			case 2407:
				anims[j].anIntArray353 = new int[] { 12058713, 12058717, 12058647, 12058643, 12058651, 12058687,
						12058700, 12058716, 12058699, 12058667, };
				anims[j].anIntArray355 = new int[] { 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, };
				anims[j].anInt352 = 10;
				break;

			case 1741:// Block
				anims[j].anIntArray353 = new int[] { 12058703, 12058695, 12058666, 12058719, 12058665, 12058632,
						12058669, 12058718, 12058689, 12058721, };
				anims[j].anIntArray355 = new int[] { 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, };
				anims[j].anInt352 = 10;
				break;

			case 2413:// Spawn
				anims[j].anIntArray353 = new int[] { 12058654, 12058649, 12058673, 12058633, 12058626, 12058664,
						12058653, 12058694, 12058705, 12058624, 12058691, 12058696, 12058662, };
				anims[j].anIntArray355 = new int[] { 4, 4, 4, 4, 4, 5, 5, 5, 5, 5, 5, 5, 5, };
				anims[j].anInt352 = 13;
				break;

			case 2408:// death
				anims[j].anIntArray353 = new int[] { 12058672, 12058709, 12058639, 12058688, 12058641, 12058677,
						12058671, 12058659, 12058657, 12058668, 12058676, 12058663, 12058645, 12058650, 12058625,
						12058644, 12058720, 12058707, };
				anims[j].anIntArray355 = new int[] { 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 9, 3, 3, 3, 3, 3, };
				anims[j].anInt352 = 18;
				break;

			case 2405:
				anims[j].anIntArray353 = new int[] { 12058670, 12058674, 12058636, 12058701, 12058678, 12058704,
						12058640, 12058629, 12058681, 12058697, };
				anims[j].anIntArray355 = new int[] { 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, };
				anims[j].anInt352 = 10;
				break;

			case 1721:
				anims[j].anIntArray353 = new int[] { 12058711, 12058684, 12058635, 12058658, 12058715, 12058628,
						12058630, 12058675, };
				anims[j].anIntArray355 = new int[] { 3, 4, 3, 4, 4, 4, 4, 4, };
				anims[j].anInt352 = 8;
				break;

			case 5358:
				anims[j].anInt352 = 8;
				anims[j].anIntArray353 = new int[] { 4390912, 4390913, 4390914, 4390915, 4390916, 4390915, 4390914,
						4390913, };
				anims[j].anIntArray355 = new int[] { 5, 5, 5, 5, 5, 5, 5, 5, };
				anims[j].anInt356 = 8;
				break;

			case 6648:
				anims[j].anInt352 = 6;
				anims[j].anIntArray353 = new int[] { 110755840, 110755841, 110755842, 110755843, 110755844,
						110755845, };
				anims[j].anIntArray355 = new int[] { 2, 2, 2, 2, 2, 2, };
				break;

			case 5068:
				// fileID = 182
				anims[j].anIntArray353 = new int[] { 11927613, 11927599, 11927574, 11927659, 11927676, 11927562,
						11927626, 11927628, 11927684, 11927647, 11927602, 11927576, 11927586, 11927653, 11927616,
						11927653, 11927586, 11927576, 11927602, 11927576, 11927586, 11927653, 11927616, 11927653,
						11927586, 11927576, 11927602, 11927576, 11927586, 11927653, 11927616, 11927653, 11927586,
						11927576, 11927554, 11927602, };
				anims[j].anIntArray355 = new int[] { 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5,
						5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, };
				anims[j].anInt352 = 36;
				break;

			case 5069:
				// fileID = 182
				anims[j].anIntArray353 = new int[] { 11927613, 11927599, 11927574, 11927659, 11927676, 11927562,
						11927626, 11927628, 11927684, 11927647, 11927602, 11927576, 11927586, 11927653, 11927616, };
				anims[j].anIntArray355 = new int[] { 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 5, 5, 5, 5, 5, };
				anims[j].anInt352 = 15;
				break;

			case 5070:
				// fileID = 182
				anims[j].anIntArray353 = new int[] { 11927608, 11927625, 11927598, 11927678, 11927582, 11927600,
						11927669, 11927597, 11927678, };
				anims[j].anIntArray355 = new int[] { 5, 4, 4, 4, 5, 5, 5, 4, 4, };
				anims[j].anInt352 = 9;
				break;

			case 5071:
				// fileID = 182
				anims[j].anIntArray353 = new int[] { 11927640, 11927643, 11927695, 11927630, 11927556, 11927667,
						11927692, 11927588, 11927555, 11927624, 11927633, 11927673, 11927661, 11927666, 11927664,
						11927579, 11927670, 11927636, 11927685, 11927595, 11927623, };
				anims[j].anIntArray355 = new int[] { 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, };
				anims[j].anInt352 = 21;
				break;

			case 5072:
				// fileID = 182
				anims[j].anIntArray353 = new int[] { 11927623, 11927595, 11927685, 11927636, 11927670, 11927579,
						11927664, 11927666, 11927661, 11927673, 11927633, 11927624, 11927555, 11927588, 11927692,
						11927667, 11927556, 11927630, 11927695, 11927643, 11927640, };
				anims[j].anIntArray355 = new int[] { 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, };
				anims[j].anInt352 = 21;
				break;

			case 5073:
				// fileID = 182
				anims[j].anIntArray353 = new int[] { 11927640, 11927643, 11927695, 11927630, 11927556, 11927667,
						11927692, 11927588, 11927555, 11927624, 11927633, 11927673, 11927661, 11927666, 11927664,
						11927579, 11927670, 11927636, 11927685, 11927595, 11927623, };
				anims[j].anIntArray355 = new int[] { 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 2, 2, 2, };
				anims[j].anInt352 = 21;
				break;

			case 5804:
				// fileID = 182
				anims[j].anIntArray353 = new int[] { 11927638, 11927632, 11927590, 11927580, 11927568, 11927641,
						11927652, 11927594, 11927620, 11927646, 11927658, 11927679, 11927585, 11927596, 11927559,
						11927565, 11927593, 11927584, 11927665, 11927553, 11927587, 11927682, 11927566, 11927645,
						11927592, 11927650, 11927634, 11927674, 11927689, 11927686, 11927671, 11927609, 11927603,
						11927663, 11927619, 11927572, 11927589, 11927581, 11927570, 11927662, 11927558, 11927631,
						11927617, 11927655, 11927561, 11927642, 11927575, 11927578, 11927648, };
				anims[j].anIntArray355 = new int[] { 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
						3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 6, };
				anims[j].anInt352 = 49;
				break;

			case 5806:
				// fileID = 182
				anims[j].anIntArray353 = new int[] { 11927612, 11927677, 11927615, 11927573, 11927618, 11927567,
						11927564, 11927606, 11927675, 11927657, 11927690, 11927583, 11927672, 11927552, 11927563,
						11927683, 11927639, 11927635, 11927668, 11927614, 11927627, 11927610, 11927693, 11927644,
						11927656, 11927660, 11927629, 11927635, 11927668, 11927614, 11927627, 11927610, 11927693,
						11927644, 11927656, 11927660, 11927635, 11927668, 11927614, 11927560, 11927687, 11927577,
						11927569, 11927557, 11927569, 11927577, 11927687, 11927560, 11927651, 11927611, 11927680,
						11927622, 11927691, 11927571, 11927601, };
				anims[j].anIntArray355 = new int[] { 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4,
						4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4,
						3, };
				anims[j].anInt352 = 55;
				break;

			case 5807:
				// fileID = 182
				anims[j].anIntArray353 = new int[] { 11927612, 11927677, 11927615, 11927573, 11927618, 11927567,
						11927564, 11927606, 11927675, 11927657, 11927690, 11927583, 11927672, 11927552, 11927563,
						11927683, 11927639, 11927635, 11927668, 11927614, 11927627, 11927610, 11927693, 11927644,
						11927656, 11927660, 11927629, 11927635, 11927668, 11927614, 11927627, 11927610, 11927693,
						11927644, 11927656, 11927604, 11927637, 11927688, 11927696, 11927681, 11927605, 11927681,
						11927696, 11927688, 11927637, 11927604, 11927656, 11927611, 11927680, 11927622, 11927691,
						11927571, 11927601, };
				anims[j].anIntArray355 = new int[] { 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4,
						4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 3, };
				anims[j].anInt352 = 53;
				break;

			case 5808:
				// fileID = 182
				anims[j].anIntArray353 = new int[] { 11927694, 11927654, 11927591, 11927649, 11927607, 11927649,
						11927591, 11927654, 11927694, };
				anims[j].anIntArray355 = new int[] { 3, 3, 3, 3, 6, 3, 3, 3, 3, };
				anims[j].anInt352 = 9;
				break;

			/*
			 * case 7156: anims[j].anIntArray355 = new int[] { 3, 3, 3, 3, 3, 3,
			 * 3, 4, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 3, 3, 4, 4, 4, 4, 3, 3, 3,
			 * }; anims[j].anIntArray353 = new int[] { 158990394, 158990411,
			 * 158990398, 158990429, 158990418, 158990393, 158990407, 158990402,
			 * 158990397, 158990428, 158990409, 158990401, 158990425, 158990406,
			 * 158990417, 158990403, 158990420, 158990395, 158990399, 158990435,
			 * 158990414, 158990405, 158990432, 158990410, 158990415, 158990426,
			 * 158990430, 158990394, }; anims[j].anInt352 = 28;
			 * anims[j].anInt356 = -1; anims[j].aBoolean358 = false;
			 * anims[j].anInt359 = 5; anims[j].anInt360 = -1; anims[j].anInt361
			 * = -1; anims[j].anInt362 = 99; anims[j].anInt363 = -1;
			 * anims[j].anInt364 = -1; anims[j].anInt365 = 2; break;
			 */

			case 7291:
				// fileID = 2572
				anims[j].anIntArray355 = new int[] { 18, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 1, 1, 1,
						1, 3, 3, 1, 1, 1, 1, 1, 1, 1, 6, 4, 1, 1, 1, 1, 1, 1, 1, 1, };
				anims[j].anIntArray353 = new int[] { 168558682, 168558693, 168558686, 168558670, 168558665, 168558702,
						168558703, 168558669, 168558690, 168558705, 168558692, 168558694, 168558671, 168558679,
						168558672, 168558667, 168558675, 168558683, 168558706, 168558681, 168558704, 168558673,
						168558698, 168558680, 168558695, 168558677, 168558700, 168558701, 168558699, 168558668,
						168558689, 168558684, 168558678, 168558696, 168558688, 168558666, 168558687, 168558674,
						168558691, 168558676, 168558685, 168558697, };
				anims[j].anInt352 = 42;
				anims[j].anInt356 = -1;
				anims[j].aBoolean358 = false;
				anims[j].anInt359 = 5;
				anims[j].anInt360 = -1;
				anims[j].anInt361 = -1;
				anims[j].anInt362 = 99;
				anims[j].anInt363 = -1;
				anims[j].anInt364 = -1;
				anims[j].anInt365 = 2;
				break;

			case 7292:
				// fileID = 2572
				anims[j].anIntArray355 = new int[] { 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
						3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, };
				anims[j].anIntArray353 = new int[] { 168558648, 168558597, 168558594, 168558633, 168558664, 168558599,
						168558645, 168558615, 168558595, 168558636, 168558640, 168558607, 168558658, 168558647,
						168558631, 168558628, 168558655, 168558656, 168558613, 168558611, 168558633, 168558664,
						168558599, 168558645, 168558615, 168558595, 168558636, 168558640, 168558607, 168558658,
						168558647, 168558631, 168558619, 168558643, 168558652, 168558638, 168558637, 168558637, };
				anims[j].anInt352 = 38;
				anims[j].anInt356 = -1;
				anims[j].aBoolean358 = false;
				anims[j].anInt359 = 5;
				anims[j].anInt360 = -1;
				anims[j].anInt361 = -1;
				anims[j].anInt362 = 99;
				anims[j].anInt363 = -1;
				anims[j].anInt364 = -1;
				anims[j].anInt365 = 2;
				break;

			case 7293:
				// fileID = 2572
				anims[j].anIntArray355 = new int[] { 24, 3, 3, 2, 2, 2, 2, 2, 2, 2, };
				anims[j].anIntArray353 = new int[] { 168558612, 168558616, 168558592, 168558660, 168558600, 168558662,
						168558617, 168558614, 168558624, 168558644, };
				anims[j].anInt352 = 10;
				anims[j].anInt356 = -1;
				anims[j].aBoolean358 = false;
				anims[j].anInt359 = 5;
				anims[j].anInt360 = -1;
				anims[j].anInt361 = -1;
				anims[j].anInt362 = 99;
				anims[j].anInt363 = -1;
				anims[j].anInt364 = -1;
				anims[j].anInt365 = 2;
				break;

			case 7294:
				// fileID = 2571
				anims[j].anIntArray355 = new int[] { 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
						3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, };
				anims[j].anIntArray353 = new int[] { 168493151, 168493160, 168493066, 168493116, 168493073, 168493202,
						168493179, 168493140, 168493166, 168493161, 168493157, 168493154, 168493099, 168493212,
						168493172, 168493096, 168493215, 168493190, 168493163, 168493066, 168493116, 168493073,
						168493202, 168493179, 168493140, 168493166, 168493161, 168493157, 168493154, 168493099,
						168493212, 168493172, 168493096, 168493215, 168493190, 168493163, 168493056, 168493151, };
				anims[j].anInt352 = 38;
				anims[j].anInt356 = -1;
				anims[j].aBoolean358 = false;
				anims[j].anInt359 = 5;
				anims[j].anInt360 = -1;
				anims[j].anInt361 = -1;
				anims[j].anInt362 = 99;
				anims[j].anInt363 = -1;
				anims[j].anInt364 = -1;
				anims[j].anInt365 = 2;
				break;

			case 7295:
				// fileID = 2571
				anims[j].anIntArray355 = new int[] { 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, };
				anims[j].anIntArray353 = new int[] { 168493102, 168493119, 168493200, 168493113, 168493153, 168493194,
						168493134, 168493213, 168493149, 168493122, 168493098, 168493170, 168493170, 168493188,
						168493205, 168493196, 168493165, 168493102, };
				anims[j].anInt352 = 18;
				anims[j].anInt356 = -1;
				anims[j].aBoolean358 = false;
				anims[j].anInt359 = 5;
				anims[j].anInt360 = -1;
				anims[j].anInt361 = -1;
				anims[j].anInt362 = 99;
				anims[j].anInt363 = -1;
				anims[j].anInt364 = -1;
				anims[j].anInt365 = 2;
				break;

			case 7296:
				// fileID = 2571
				anims[j].anIntArray355 = new int[] { 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, };
				anims[j].anIntArray353 = new int[] { 168493148, 168493076, 168493137, 168493077, 168493086, 168493081,
						168493121, 168493126, 168493143, 168493176, 168493109, 168493131, 168493063, 168493080,
						168493217, 168493189, 168493093, 168493199, 168493106, 168493175, 168493148, };
				anims[j].anInt352 = 21;
				anims[j].anInt356 = -1;
				anims[j].aBoolean358 = false;
				anims[j].anInt359 = 5;
				anims[j].anInt360 = -1;
				anims[j].anInt361 = -1;
				anims[j].anInt362 = 99;
				anims[j].anInt363 = -1;
				anims[j].anInt364 = -1;
				anims[j].anInt365 = 2;

				break;
			}
		}
	}

	public int method258(int i) {
		int j = anIntArray355[i];
		if (j == 0) {
			Class36 class36 = Class36.forId(anIntArray353[i]);
			if (class36 != null)
				j = anIntArray355[i] = class36.anInt636;
		}
		if (j == 0)
			j = 1;
		return j;
	}

	private void readValues(Stream stream) {
		int i;
		while ((i = stream.readUnsignedByte()) != 0) {

			if (i == 1) {
				anInt352 = stream.readUnsignedWord();
				anIntArray353 = new int[anInt352];
				anIntArray354 = new int[anInt352];
				anIntArray355 = new int[anInt352];
				for (int j = 0; j < anInt352; j++) {
					anIntArray353[j] = stream.readDWord();
					anIntArray354[j] = -1;
				}
				for (int j = 0; j < anInt352; j++)
					anIntArray355[j] = stream.readUnsignedByte();

			} else if (i == 2)
				anInt356 = stream.readUnsignedWord();
			else if (i == 3) {
				int k = stream.readUnsignedByte();
				anIntArray357 = new int[k + 1];
				for (int l = 0; l < k; l++)
					anIntArray357[l] = stream.readUnsignedByte();
				anIntArray357[k] = 9999999;
			} else if (i == 4)
				aBoolean358 = true;
			else if (i == 5)
				anInt359 = stream.readUnsignedByte();
			else if (i == 6)
				anInt360 = stream.readUnsignedWord();
			else if (i == 7)
				anInt361 = stream.readUnsignedWord();
			else if (i == 8)
				anInt362 = stream.readUnsignedByte();
			else if (i == 9)
				anInt363 = stream.readUnsignedByte();
			else if (i == 10)
				anInt364 = stream.readUnsignedByte();
			else if (i == 11)
				anInt365 = stream.readUnsignedByte();
			else if (i == 12)
				stream.readDWord();
			else
				System.out.println("Error unrecognised seq config code: " + i);
		}
		if (anInt352 == 0) {
			anInt352 = 1;
			anIntArray353 = new int[1];
			anIntArray353[0] = -1;
			anIntArray354 = new int[1];
			anIntArray354[0] = -1;
			anIntArray355 = new int[1];
			anIntArray355[0] = -1;
		}
		if (anInt363 == -1)
			if (anIntArray357 != null)
				anInt363 = 2;
			else
				anInt363 = 0;
		if (anInt364 == -1) {
			if (anIntArray357 != null) {
				anInt364 = 2;
				return;
			}
			anInt364 = 0;
		}
	}

	public AnimationDefinition() {
		anInt356 = -1;
		aBoolean358 = false;
		anInt359 = 5;
		anInt360 = -1;
		anInt361 = -1;
		anInt362 = 99;
		anInt363 = -1;
		anInt364 = -1;
		anInt365 = 2;
	}

	public static AnimationDefinition anims[];
	public int anInt352;
	public int anIntArray353[];
	public int anIntArray354[];
	public int[] anIntArray355;
	public int anInt356;
	public int anIntArray357[];
	public boolean aBoolean358;
	public int anInt359;
	public int anInt360;
	public int anInt361;
	public int anInt362;
	public int anInt363;
	public int anInt364;
	public int anInt365;
	public static int anInt367;
}