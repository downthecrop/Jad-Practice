package ethos.model.npcs.animations;

import ethos.model.minigames.warriors_guild.AnimatedArmour;
import ethos.model.npcs.NPCHandler;
import ethos.model.npcs.bosses.skotizo.Skotizo;
import ethos.model.npcs.bosses.zulrah.Zulrah;
import ethos.model.players.combat.CombatType;

public class DeathAnimation extends NPCHandler {

	public static int handleEmote(int i) {
		if (AnimatedArmour.isAnimatedArmourNpc(npcs[i].npcType)) {
			return 836;
		}
		switch (npcs[i].npcType) {
			                                                                         //Inferno Npcs
			case 7691:
				return 7576;
			case 7692:
				return 7580;
			case 7693:
				return 7584;
			case 7694:
				return 7584;
			case 7695:
				return 7584;
			case 7696:
				return 7584;
			case 7697:
				return 7599;
			case 7699:
				return 7613;
			case 7700:
					return 7594;
			case 7702:
				return 7606;
			case 7706:
				return 7562;
			case 7707:
				return 7569;
			case 7708:
				return 2865;

			case 7527:
			case 7528:
			case 7529:
			return 7345;
			case 1605:
				return 714;
			case 1606:
				return 131;
			case 1607:
				return 5321;
			case 1608:
				return 843;
			case 1609:
				return 68;
		case 8028:
			return 7949;
		case 7566:
			return 7415;
		case 7573:
			return 7196;
		case 7563:
			return 7426;
		case 7585:
			return 6949;
		case Skotizo.SKOTIZO_ID:
			return 4624;
		case Skotizo.REANIMATED_DEMON:
			return 67;
		case 7604: // Skeletal mystic
		case 7605: // Skeletal mystic
		case 7606: // Skeletal mystic
			return 5491;
		case 2085:
			return 4673;
		case 7544: // Tekton
			return 7495;
		case 5916:
			return 4521;
		case 5890:
			return 7100;
		case 1635:
		case 1636:
		case 1637:
		case 1638:
		case 1639:
		case 1640:
		case 1641:
		case 1642:
		case 1643:
		case 1654:
		case 7302:
			return 6615;
		case 6604:
			return 307;
		case 6594:
			return 7146;
		case 5816:
			return 5784;

		case 5744:
			return 5098;
		case 5762:
			return 5397;

		case 955:
		case 957:
		case 959:
			return 6229;
		case 7144:
		case 7145:
		case 7146:
			return 7229;
		case 458:
			return 2778;
		case 3544:
			return 278;
		case 1267:
			return 2021;
		case 2064:
			return 1013;
		case 2067:
			return 6791;

		case 5129:
			return 6502;

		case 2593:
			return 6537;

		case 963:
			return 6229;

		case 965:
			return 6233;

		case 5548:
		case 5549:
		case 5550: // Hunter birds
		case 5551:
		case 5552:
			return 6780;

		case 1505: // Hunter ferret
			return 5188;

		case 2910:
		case 2911: // Hunter chinchompas
		case 2912:
			return 5182;

		case 5867:
		case 5868:
		case 5869:
			return 4503;

		case 5862:
			return 4495;

		case 465: // Skeletal wyverns
			return 2987;

		case 6766: // Lizardman shaman
		case 6768:
		case 6914:
		case 6915:
		case 6916:
		case 6917:
		case 6918:
		case 6919:
			return 7196;

		case 6615: // Scorpia
		case 6616:
		case 6617:
			return 6256;
		case 2527: // Ghosts
			return 5542;
		case 2528:
		case 2529:
		case 2530:
			return 5534;
		case 6462: // Zombies
		case 6465:
			return 5575;
		case 320: // Dark core
			return 1688;
		case 6367: // Evil chicken
			return 2301;
		case 6369: // Agrith-na-na
			return 3503;
		case 6370: // Flambeed
			return 1752;
		case 6372: // Dessourt
			return 3509;

		case 2084: // Fire giant
			return 4668;
		case 6373:
		case 6374:
		case 6375:
		case 6376:
		case 6377:
		case 6378:
			return 1342;
		case 970: // Dagannoths
		case 975:
		case 983:
		case 984:
		case 985:
		case 986:
		case 987:
		case 988:
			return 1342;
		case 5944: // Rock lobster
			return 2862;
		case 5938:// Wallasalki
			return 2367;

		case 319: // Corporeal Beast
			return 1676;

		case 406: // Cave Crawler
		case 7389:
			return 228;
		case 481:
			return 6081;
		case 498: // Smoke Devil
		case 499:
		case 7406:
			return 3849;
		case 1610:
		case 1611:
		case 6618:
		case 6619:
			return 836;
		case 1612:
			return 196;
		case 2805:
			return 5851;

		case 2211:
		case 2212:
			return 2304;
		case 2233:
			return 361;
		case 2234:
			return 2938;
		case 2235:
			return 4653;
		case 2237:
			return 4321;
		case 2241:
			return 167;
		case 2242:
		case 2243:
		case 2244:
			return 4321;
		case 2245:
			return 6182;
		case 3133:
			return 6567;
		case 3134:
			return 172;
		case 3135:
			return 6537;
		case 3137:
			return 2304;
		case 3138:
			return 1553;
		case 3139:
		case 3140:
			return 1580;
		case 3141:
			return 4302;
		case 3160:
		case 3159:
		case 3161:
			return 2304;
		case 3162:
			return 6979;
		case 3163:
		case 3164:
		case 3165:
		case 3166:
		case 3167:
		case 3168:
		case 3174:
		case 3169:
			return 6959;

		case 2042:
		case 2043:
		case 2044:
			return -1;

		case Zulrah.SNAKELING:
			return 2408;
		case 6601:
		case 6602:
			return 65535;
		case 1739:
		case 1740:
		case 1741:
		case 1742:
			return 65535;
		case 1734:
			return 3894;
		case 1724:
			return 3922;
		case 1709:
			return 3910;
		case 1704:
			return 3917;
		case 1699:
			return 3901;
		case 1689:
			return 3888;
		case 2037:// Skeleton
		case 70:
			return 5491;
		case 437: // Jelly
		case 7277:
			return -1;
		case 7400:
		case 7399:
			return 1587;
		case 3209: // Cave Horror
		case 7401:
			return 4233;
		case 411: // Kurask
			return 1513;
		case 7405:
			return 4233;
		case 5054:
			return 6564;
		case 6611:
		case 6612:
			return 5491;
		case 6610:
			return 5329;
		case 494:
		case 492:
			return 3987;
		case 871:// Ogre Shaman
			return 361;
		case 6609: // Castillo
			return 4929;
		case 73:
		case 5399:
		case 77:
			return 5569;
		case 438:
		case 439:
		case 440:
		case 441:
		case 442: // Tree spirit
		case 443:
			return 97;
		case 391:
		case 392:
		case 393:
		case 394:
		case 395:// river troll
		case 396:
			return 287;
		case 421: // Rockslug
		case 7392:
			return 1568;
		case 420:
		case 422:
			// case 423:
		case 28:
			return 5569;
		// begin new updates
		case 891: // moss
			return 4659;
		case 85: // ghost
		case 7258:
			return 5542;
		case 2834: // bats
			return 4917;
		// end
		case 2206:
			return 6377;
		case 2207:
			return 7034;
		case 2216:
		case 2217:
		case 2218:
			return 6156;
		case 3129:
		case 3130:
		case 3131:
		case 3132:
			return 67;
		case 6142:
			return 1;
		case 6143:
			return 1;
		case 6144:
			return 1;
		case 6145:
			return 1;
		case 5935:// sand crab
			return 1314;
		case 100:// rc
			return 1314;
		case 662:
			return 6182;
		case 414:// Battle Mage
		case 7272:
		case 7391:
			return 1524;
		case 7390:
			return -1;
		case 3742:// Ravager
		case 3743:
		case 3744:
		case 3745:
		case 3746:
			return 3916;
		case 3772:// Brawler
		case 3773:
		case 3774:
		case 3775:
		case 3776:
			return 3894;
		case 5779: // giant mole
			return 3313;
		case 135:
			case 7935:
			return 6567;
		case 2205:
			return 6968;
		case 2215:
			return 7020;
		case 6267:
			return 357;
		case 6268:
			return 2938;
		case 6269:
			return 4653;
		case 6270:
			return 4653;
		case 6271:
			return 4321;
		case 6272:
			return 4321;
		case 6273:
			return 4321;
		case 6274:
			case 7937:
			return 4321;
		case 2098:
		case 2463:
			return 4653;
		case 1459:
			return 1404;
		case 2559:
		case 2560:
		case 2561:
			return 6956;
		case 3121:
			return 2630;
		case 3118:
			return 2627;
		case 2167:
			return 2607;
		case 3116:
			return 2620;
		case 3120:
			return 2627;
		case 3123:
			return 2638;
		case 2746:
			return 2638;
		case 3125:
			return 2646;
		case 3127:
			return 2654;
		case 3777:
		case 3778:
		case 3779:
		case 3780:
			return -1;
		case 6342:
			return 5898;
		case 2054:
			return 3147;
		case 2035: // spider
			return 146;
		case 2033: // rat
			return 141;
		case 2031: // bloodvel
			return 2073;
		case 1769:
		case 1770:
		case 1771:
		case 1772:
			case 7931:
		case 1773:
		case 101: // goblin
			return 6182;
		case 1767:
		case 397:
		case 1766:
		case 1768:
		case 81: // cow
			return 5851;
		case 41: // chicken
			return 57;
		case 1338: // dagannoth
		case 1340:
		case 1342:
			return 1342;
		case 2265:
		case 2266:
		case 2267:
			return 2856;
		case 111: // ice giant
			case 7934:
			return 131;
		case 2841: // ice warrior
			return 843;
		case 751:// Zombies!!
			return 302;
		case 1626:
		case 1627:
		case 1628:
		case 1629:
		case 1630:
		case 1631:
		case 1632: // turoth!
			return 1597;
		case 417: // basilisk
		case 7395:
			return 1548;
		case 1653: // hand
			return 1590;
		case 2006:// demons
		case 7244:
		case 2026:
		case 2025:
		case 1432:
			case 7936:
			return 67;
		case 6:// abby spec
		case 7279: // Deviant spectre
		case 7403:
			return 1508;
		case 51:// baby drags
		case 52:
		case 1589:
		case 3376:
			return 28;
		case 1543: // Gargoyle
		case 7407:
			return 1518;
		case 484: // Bloodveld
		case 1619:
		case 7276:
		case 7398:
		case 7397:
			return 1553;
		case 419: // Cockatrice
		case 1621:
		case 7393:
			return 1563;
		case 4005: // Dark Beast
			case 7938:
			return 2732;
		case 7409:
			return 2733;
		case 415: // Abby Demon
		case 7410:
			return 1538;
		case 423: // Dust devil
		case 7404:
			return 1558;
		case 11: // Nechryael
		case 7411:
		case 7278:
			return 1530;
		case 435: // Pyrefiend
		case 1634:
			case 7932:
		case 7394:
			return 1580;
		case 7293:
			return 1563;
		case 448: // Crawling hand
		case 7388:
		case 1649:
		case 1650:
		case 1651:
		case 1652:
		case 1655:
		case 1656:
		case 1657:
			return 1590;
		case 102:
			return 313;
		case 105:
		case 106:
			return 44;
		case 412:
			// case 2834:
			return 36;
		case 122:
		case 123:
			case 7933:
			return 167;
		case 58:
		case 59:
		case 60:
		case 61:
		case 62:
		case 63:
		case 64:
		case 3021:
			return 5329;
		case 1153:
		case 1154:
		case 1155:
		case 1156:
		case 1157:
			return 1190;
		case 104:
			return 5534;
		case 118:
		case 291:
			return 102;
		case 239:// drags
			case 7940:
			return 92;
		case 247:
		case 259:
		case 268:
		case 240:
		case 241:
		case 242:
		case 243:
		case 244:
		case 245:
		case 246:
		case 264:
		case 270:
		case 2919:
		case 1270:
		case 273:
		case 274:
		case 6593:
		case 7273:
		case 7274:
		case 7275:
			return 92;
		case 1680:
			return 4935;
		case 1679:
			return 241;
		case 1678:
			return 2072;
		case 1683:
		case 1684:
			return 6246;
		case 1685:
			return 5491;
		default:
			return 2304;
		}
	}
}
