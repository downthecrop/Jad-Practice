package ethos.model.npcs.animations;

import ethos.model.minigames.warriors_guild.AnimatedArmour;
import ethos.model.npcs.NPCHandler;
import ethos.model.npcs.bosses.skotizo.Skotizo;
import ethos.model.npcs.bosses.zulrah.Zulrah;
import ethos.model.players.Player;
import ethos.model.players.PlayerAssistant;
import ethos.model.players.combat.CombatType;
import ethos.util.Misc;

/*
 * Handles all NPC Attack Emotes
 */

public class AttackAnimation extends NPCHandler {

	public static int handleEmote(int i) {
		if (AnimatedArmour.isAnimatedArmourNpc(npcs[i].npcType)) {
			return 412;
		}
		
		switch (NPCHandler.npcs[i].npcType) {
			//Inferno Npcs
			case 7691:
				return 7574;
			case 7692:
				return 7578;
			case 7693:
				return 7582;
			case 7694:
				return 7582;
			case 7695:
				return 7582;
			case 7696:
				return 7582;
			case 7697:
				return 7597;
			case 7699:
				return npcs[i].attackType == CombatType.MAGE ? 7610 : 7612;
			case 7700:
				if (npcs[i].attackType == CombatType.MAGE)
					return 7592;
				else if (npcs[i].attackType == CombatType.RANGE)
					return 7593;
				else if (npcs[i].attackType == CombatType.MELEE)
					return 7590;
			case 7702:
				return 7604;
			case 7706:
				return 7566;
			case 7708:
				return 2868;

			case 1605:
				return 811;
			case 1606:
				return 128;
			case 1607:
				return 5319;
			case 1608:
				return 394;
			case 1609:
				return 64;

		case 8028:
			return 7952;
		case 7527:
			return 7441;
		case 7566:
			return 7410;
		case 7573:
			return 7193;
		case 7563:
			return 7420;
		case Skotizo.SKOTIZO_ID:
			return npcs[i].attackType == CombatType.MAGE ? 69 : 4680;
		case 7585:
		case Skotizo.REANIMATED_DEMON:
			return 64;
		case 5916:
			return 4522;

		case 7604: // Skeletal mystic
		case 7605: // Skeletal mystic
		case 7606: // Skeletal mystic
			return 5528;

		case 7544: // Tekton
			return Misc.random(1) == 0 ? 7492 : 7494;

		case 6604:
			return 304;
		case 6594:
			return 7145;

		case 5816:
			return 5782;

		case 7144:
		case 7145:
		case 7146:
			return npcs[i].attackType == CombatType.MAGE ? 7225
					: npcs[i].attackType == CombatType.RANGE ? 7227
							: npcs[i].attackType == CombatType.SPECIAL ? 7225 : 7239;

		case 5890: // Abyssal sire
			return npcs[i].attackType == CombatType.MELEE ? Misc.random(1) == 0 ? 5366 : 5369
					: npcs[i].attackType == CombatType.MAGE ? 5751
							: npcs[i].attackType == CombatType.SPECIAL ? 5367 : -1;

		case 458:
			return 2776;
		case 3544:
			return 275;
		case 1267:
			return 2019;
		case 2064:
			return 1010;
		case 2067:
			return 6790;

		case 5129:
			return 6501;
		case 4922:
			return 1978;

		case 2593:
			return 6536;

		case 963:
			if (npcs[i].attackType == CombatType.MAGE || npcs[i].attackType == CombatType.RANGE) {
				return 6231;
			} else if (npcs[i].attackType == CombatType.MELEE) {
				return Misc.random(1) == 0 ? 6224 : 6225;
			}

		case 965:
			if (npcs[i].attackType == CombatType.MAGE || npcs[i].attackType == CombatType.RANGE) {
				return 6234;
			} else if (npcs[i].attackType == CombatType.MELEE) {
				return Misc.random(1) == 0 ? 6235 : 1178;
			}

		case 955:
		case 957:
		case 959:
			return Misc.random(1) == 0 ? 6224 : 6225;
		case 5867:
		case 5868:
		case 5869:
			return 4504;

		case 5862:
			if (npcs[i].attackType == CombatType.MAGE || npcs[i].attackType == CombatType.RANGE) {
				return 4490;
			} else if (npcs[i].attackType == CombatType.MELEE) {
				return 4491;
			} else if (npcs[i].attackType == CombatType.SPECIAL) {
				return 4492;
			} else {
				return 4491;
			}

		case 2085:
			return 4672;

		case 465: // Skeletal wyverns
			return npcs[i].attackType == CombatType.MELEE ? Misc.random(1) == 1 ? 2985 : 2986
					: npcs[i].attackType == CombatType.RANGE ? 2989
							: npcs[i].attackType == CombatType.SPECIAL ? 2988 : 2985;

		case 6618: // Crazy Archaeologist
			return npcs[i].attackType == CombatType.MELEE ? 423 : 2614;

		case 6766: // Lizardman shaman
		case 6768:
		case 6914:
		case 6915:
		case 6916:
		case 6917:
		case 6918:
		case 6919:
			return npcs[i].attackType == CombatType.MELEE ? 7192 : npcs[i].attackType == CombatType.MAGE ? 7193 : 7157;

		case 6619:
			return 811;

		case 5744:
			return 5097;
		case 5762:
			return 5395;

		case 6615: // Scorpia
		case 6616:
		case 6617:
			return 6254;
		case 1277:
		case 2527: // Ghosts
			return 5540;
		case 2528:
		case 2529:
		case 2530:
			return 5532;
		case 6462: // Zombies
		case 6465:
			return 5571;
		case 1798:// White knights
		case 1799:
		case 1800:
			return 407;
		case 6367: // Evil chicken
			return 2302;
		case 6368: // Culinaromancer
			return 1979;
		case 6369: // Agrith-na-na
			return 3501;
		case 6370: // Flambeed
			return 1750;
		case 6371: // Karamel
			return npcs[i].attackType == CombatType.RANGE ? 2075 : 1979;
		case 6372: // Dessourt
			return 3508;
		case 6373:
		case 6374:
		case 6375:
		case 6376:
		case 6377:
		case 6378:
			return 1341;

		case 970: // Dagannoths
		case 975:
		case 983:
		case 984:
		case 985:
		case 986:
		case 987:
		case 988:
			return 1341;
		case 5944: // Rock lobster
			return 2859;
		case 5938:// Wallasalki
			return 2365;

		case 319: // Corporeal Beast
			return npcs[i].attackType == CombatType.MAGE ? 1680
					: npcs[i].attackType == CombatType.SPECIAL ? 1680 : 1682;
		case 320:
			return 1689;

		case 406: // Cave Crawler
		case 7389:
			return 227;
		case 498: // Smoke Devil
		case 499:
		case 7406:
			return 3847;
		case 1610:
		case 1611:
			return 729;
		case 1612:
			return 198;
		case 2805:
			return 5849;
		case Zulrah.SNAKELING:
			return 1741;
		case 6600:
			return 153;
		case 2043:
			return 5806;
		case 411: // Kurask
			return 1512;
		case 7405:
			return 4232;
		case 1734:
			return 3897;
		case 1724:
			return 3920;
		case 1709:
			return 3908;
		case 1704:
			return 3915;
		case 1699:
			return 3908;
		case 1689:
			return 3891;
		case 437:
		case 7277: // Warped Jelly
			return -1;
		case 7400:
		case 7399:
			return 1586;
		case 7394:
			return 1582;
		case 7393:
			return 1562;
		case 3209: // Cave Horror
		case 7401:
			if (npcs[i].attackType == CombatType.MELEE)
				return 4234;
			else if (npcs[i].attackType == CombatType.MAGE)
				return 4237;
		case 2037:// Skeleton
		case 70:
			return 5485;
		case 435: // Pyrefiend
		case 7349:
			case 7932:
			return 1582;

		// God wars
		case 2205:
			return npcs[i].attackType == CombatType.MAGE ? 6970 : 6967;
		case 2206:
			return 6376;
		case 2207:
			return 7036;
		case 2208:
			return 4311;
		case 2209:
			return 811;
		case 2211:
			return 426;
		case 3428:
			return npcs[i].attackType == CombatType.RANGE ? 426 : 426;
		case 3429:
			return 428;
		case 2212:
			return 711;
		case 2215:
			return npcs[i].attackType == CombatType.MELEE ? 7018 : 7021;
		case 2216:
		case 2217:
		case 2218:
			return 6154;
		case 2233:
			return 359;
		case 2234:
			return 2930;
		case 2235:
			return 4652;
		case 2237:
			return 4320;
		case 2241:
			return 164;
		case 2242:
			return 4320;
		case 2243:
			return 4320;
		case 2244:
			return 4322;
		case 2245:
			return 6185;
		case 3129:
		case 3130:
		case 3131:
		case 3132:
			return 64;
		case 3133:
			return 6562;
		case 3134:
			return 169;
		case 3135:
			return 6536;
		case 3137:
			return 810;
		case 3138:
			return 1552;
		case 3139:
			return 1582;
		case 3140:
			return 1582;
		case 3141:
			return 4300;
		case 3159:
			return 390;
		case 3160:
			return 426;
		case 3161:
			return 811;
		case 3162:
			return 6980;
		case 3163:
		case 3164:
			return 6956;
		case 3165:
		case 3166:
		case 3169:
			return 6957;
		case 3167:
		case 3174:
			return 6956;
		case 3168:
			return 6956;

		case 2042:
		case 2044:
			return 5069;
		case 5054:
			return 6562;
		case 1046:
		case 1049:
			return 711;
		case 6611:
		case 6612:
			return Misc.random(1) == 0 ? 5485 : 5487;
		case 6610:
			return 5327;
		case 419: // Cockatrice
			return -1;
		case 494:
		case 492:
			return 3991;
		case 6609: // Callisto
			return 4925;
		case 73:
		case 5399:
		case 751: // zombie
		case 77:
			return 5568;
		case 5535:
			return 3618;
		case 484: // Bloodveld
		case 7276:
		case 7398:
		case 7397:
			return 1552;
		case 1678:
			return 2070;
		case 1679:
			return 240;
		case 1680:
			return 4933;
		case 1683:
			return 6249;
		case 1684:
			return 5327;
		case 1685:
			return 5485;

		case 421: // Rockslug
		case 7392:
			return 1567;

		case 28:
		case 420:
		case 422:
			// case 423:
			return 5568;
		case 5779: // giant mole
			return 3312;
		case 438:
		case 439:
		case 440:
		case 441:
		case 442: // Tree spirit
		case 443:
			return 94;
		case 391:
		case 392:
		case 393:
		case 394:
		case 395:// river troll
		case 396:
			return 284;
		case 891: // moss
			return 4658;
		case 85: // ghost
		case 7258:
			return 5540;
		case 2834: // bats
			return 4915;
		case 414: // banshee
		case 7272:
		case 7391:
			return 1523;
		case 7390:
			return 7547;
		case 4005: // dark beast
		case 7409:
			case 7938:
			return 2731;
		case 135:
			case 7935:
			return 6562;
		case 6267:
			return 359;
		case 6268:
			return 2930;
		case 6269:
			return 4652;
		case 6270:
			return 4652;
		case 6271:
			return 4320;
		case 6272:
			return 4320;
		case 6273:
			return 4320;
		case 6274:
			case 7937:
			return 4320;
		case 1459:
			return 1402;

		case 86:
		case 87:
			return 4933;
		case 871:// Ogre Shaman
		case 5181:// Ogre Shaman
		case 5184:// Ogre Shaman
		case 5187:// Ogre Shaman
		case 5190:// Ogre Shaman
		case 5193:// Ogre Shaman
			return 359;

		case 2892:
		case 2894:
			return 2868;
		case 3116:
			return 2621;
		case 3120:
			return 2625;
		case 3123:
			return 2637;
		case 2746:
			return 2637;
		case 3121:
			return 2633;
		case 3118:
			return 2625;

		case 2167:
			return 2611;
		case 3125:// 360
			return npcs[i].attackType == CombatType.MAGE ? 2647 : 2644;
		case 5247:
			return 5411;
		case 13: // wizards
			return 711;

		case 655:
			return 5532;

		case 423: // Dust Devil
		case 7404:
			return 1557;

		case 448: // Crawling hand
			return 1590;
		case 7388:
			return 1592;

		case 415: // abby demon
		case 7410: // Greater Abby demon
			return 1537;

		case 11: // nechryael
		case 7278:
			return 1528;
		case 7411:
			return 4652;

		case 1543: // Gargoyle
		case 7407:
			return 1517;

		case 417: // basilisk
		case 7395:
			return 1546;

		// case 924: //skele
		// return 260;
		case 481:
			return 6079;

		case 239:// drags
			case 7940:
			return npcs[i].attackType == CombatType.MELEE ? 80 : 81;
		case 240:
		case 241:
		case 242:
		case 243:
		case 244:
		case 245:
		case 246:
			return 80;
		case 247:
		case 259:
		case 268:
		case 264:
		case 2919:
		case 270:
		case 1270:
		case 273:
		case 274:
		case 6593:
		case 7273:
		case 7274:
		case 7275:
			if (npcs[i].attackType == CombatType.DRAGON_FIRE) {
				return 84;
			} else if (npcs[i].attackType == CombatType.MELEE) {
				return 80;
			}
		case 2840: // earth warrior
			return 390;

		case 803: // monk
			return 422;

		case 52: // baby drag
			return 25;

		case 58: // Shadow Spider
		case 59: // Giant Spider
		case 60: // Giant Spider
		case 61: // Spider
		case 62: // Jungle Spider
		case 63: // Deadly Red Spider
		case 64: // Ice Spider
		case 3021:
			return 5327;

		case 105: // Bear
		case 106:// Bear
			return 41;

		case 412:
			// case 2834:
			return 30;

		case 2033: // rat
			return 138;

		case 2031: // bloodworm
			return 2070;

		case 1769:
		case 1770:
		case 1771:
		case 1772:
			case 7931:
		case 1773:
		case 101: // goblin
			return 6184;

		case 1767:
		case 397:
		case 1766:
		case 1768:
		case 81: // cow
			return 5849;

		case 21: // hero
			return 451;

		case 41: // chicken
			return 55;

		case 9: // guard
		case 32: // guard
		case 20: // paladin
			return 451;

		case 1338: // dagannoth
		case 1340:
		case 1342:

			return 1341;

		case 19: // white knight
			return 406;
		case 7538:
			if (npcs[i].attackType == CombatType.MAGE)
				return 7540;
		case 2084:
		case 111: // ice giant
		case 2098:
			case 7934:
		case 2463:
			return 4652;
		case 3127:
			if (npcs[i].attackType == CombatType.MAGE) {
				//jadPlayer.getPA().sendMid("mage");
				return 2656;}
			else if (npcs[i].attackType == CombatType.RANGE) {
				//jadPlayer.getPA().sendMid("range");
				return 2652;}
			else if (npcs[i].attackType == CombatType.MELEE) {
				return 2655;}
		case 2452:
			return 1312;

		case 2889:
			return 2859;

		case 118:
		case 291:
			return 99;

		case 2006:// Lesser Demon
		case 2026:// Greater Demon
		case 7244:
			case 7936:
		case 1432:// Black Demon
		case 1472:// jungle demon
			return 64;
		case 5935:// sand crabs
			return 1312;
		case 100:// rock crabs
			return 1312;
		case 662:// Goblins
			return 6183;
		case 2841: // ice warrior
		case 178:
			return 451;

		case 1153: // Kalphite Worker
		case 1154: // Kalphite Soldier
		case 1155: // Kalphite guardian
		case 1156: // Kalphite worker
		case 1157: // Kalphite guardian
			return 1184;

		case 123:
		case 7933:
		case 122:
			return 164;

		case 1675: // karil
			return 2075;

		case 1672: // ahrim
			return 729;

		case 1673: // dharok
			return 2067;

		case 1674: // guthan
			return 2080;

		case 1676: // torag
			return 0x814;

		case 1677: // verac
			return 2062;

		case 2265: // supreme
			return 2855;

		case 2266: // prime
			return 2854;

		case 2267: // rex
			return 2851;

		case 6342:
			int test = Misc.random(2);
			if (test == 2) {
				return 5895;
			} else if (test == 1) {
				return 5894;
			} else {
				return 5896;
			}

		case 2054:
			return 3146;

		default:
			return 0x326;
		}
	}

}
