package ethos.model.npcs.animations;

import ethos.Server;
import ethos.model.npcs.bosses.skotizo.Skotizo;

public class BlockAnimation {

	public static int handleEmote(int i) {
		switch (Server.npcHandler.getNPCs()[i].npcType) {
			//Inferno Npcs
			case 7691:
				return 7575;
			case 7692:
				return 7579;
			case 7693:
				return 7585;
			case 7694:
				return 7585;
			case 7695:
				return 7585;
			case 7696:
				return 7585;
			case 7697:
				return 7598;
			case 7699:
				return 7609;
			case 7700:
				return 7591;
			case 7702:
				return 7607;
			case 7706:
				return 7565;
			case 7707:
				return 7568;
			case 7708:
				return 2869;





		// case 7573:
		// return 7196;
		case 7563:
			return 7421;
		case Skotizo.SKOTIZO_ID:
			return 4676;
		case 7585:
		case Skotizo.REANIMATED_DEMON:
			return 65;
		case 7604: // Skeletal mystic
		case 7605: // Skeletal mystic
		case 7606: // Skeletal mystic
			return 5489;
		case 2085:
			return 4671;
		case 7544: // Tekton
			return 7489;
		case 5916:
			return 4523;
		case 5890:
			return 5755;
		case 955:
		case 957:
		case 959:
			return 6232;
		case 7144:
		case 7145:
		case 7146:
			return 7228;
		case 458:
			return 2777;
		case 3544:
			return 276;
		case 1267:
			return 2020;
		case 2064:
			return 1012;
		case 2067:
			return 6792;

		case 2593:
			return 6538;

		case 963:
			return 6232;

		case 965:
			return 6237;

		case 5862:
			return 4489;

		case 465: // Skeletal wyverns
			return 2983;

		case 6766: // Lizardman shaman
		case 6768:
		case 6914:
		case 6915:
		case 6916:
		case 6917:
		case 6918:
		case 6919:
			return 7194;

		case 6618: // Crazy Archaeologist
		case 6619:
			return 424;

		case 6615: // Scorpia
		case 6616:
		case 6617:
			return 6255;
		case 2527: // Ghosts
			return 5541;
		case 2528:
		case 2529:
		case 2530:
			return 5533;
		// case 6611:
		// return 5489;
		case 6462: // Zombies
		case 6465:
			return 5574;
		case 6367: // Evil chicken
			return 2299;
		case 6369: // Agrith-na-na
			return 3500;
		case 6370: // Flambeed
			return 1751;
		case 6372: // Dessourt
			return 3505;
		case 6373:
		case 6374:
		case 6375:
		case 6376:
		case 6377:
		case 6378:
			return 1340;
		case 437:
		case 7277: // Jelly
		case 7400:
		case 7399:
		case 411: // Kurask
		case 7405:
		case 3209: // Cave Horror
		case 7401:
			return 4235;
		case 70:
			return 5489;
		case 419: // Cockatrice
		case 7393:
			return -1;
		case 101:
			return 6183;
		case 3835:
			return 6232;
		case 2037:
			return 5489;
		case 5529:
			return 5783;
		case 5219:
		case 5218:
			return 5096;
		case 5235:
			return 5395;
		case 10127:
			return 13170;
		case 10057:
			return 10818;
		case 5904:
			return 6330;
		case 5779:
			return 3311;
		case 5903:
			return 6346;
		case 9463:
		case 9465:
		case 9467:
			return 12792;
		case 6624:
			return 7413;
		case 6649:
			return 7469;
		case 6646:
			return 7462;
		case 3836:
			return 6237;
		case 4005: // Dark Beast
		case 7409:
			return 2732;
		case 8133:
			return 10058;
		case 10141:
			return 13601;
		case 8349:
			return 10923;
		case 9947:
			return 13771;
		case 2215:
			return 7019;
		case 2216:
		case 2217:
		case 2218:
			return 6155;
		case 3162:
			return 6978;
		case 3163:
		case 3164:
		case 3165:
		case 3169:
			return 6952;
		case 6576:
			return 6944;
		case 3130:
		case 3131:
		case 3132:
			return 65;
		case 6575:
			return 6966;
		case 6342:
			return 5897;
		/*
		 * case 2006: return 6375;
		 */
		case 2007:
			return 7017;
		case 2008:
			return 4311;
		case 6229:
		case 6230:
		case 6231:
		case 6232:
		case 6233:
		case 6234:
		case 6235:
		case 6236:
		case 6237:
		case 6238:
		case 6239:
		case 6240:
		case 6241:
		case 6242:
		case 6243:
		case 6244:
		case 6245:
		case 6246:
			return 6952;
		case 6267:
			return 360;
		case 6268:
			return 2933;
		case 6269:
		case 6270:
			return 4651;
		case 6271:
		case 6272:
		case 6273:
		case 6274:
			return 4322;
		case 6275:
			return 165;
		case 6276:
		case 6277:
		case 6278:
			return 4322;
		case 6279:
		case 6280:
			return 6183;
		case 6281:
			return 6136;
		case 6282:
			return 6189;
		case 6283:
			return 6183;
		case 6210:
			return 6578;
		case 6211:
			return 170;
		case 6212:
		case 6213:
			return 6538;
		case 6215:
			return 1550;
		case 6216:
		case 6217:
			return 1581;
		case 6218:
			return 4301;
		case 6258:
			return 2561;
		case 10775:
			return 13154;
		case 113:
			return 129;
		case 114:
			return 360;
		case 3058:
			return 2937;
		case 3061:
			return 2961;
		case 3063:
			return 2937;
		case 3065:
			return 2720;
		case 3066:
			return 2926;
		case 5935:// sand crabs
			return 1313;
		case 100:// rc
			return 1313;
		case 662:// goblins
			return 6186;
		case 118:
			return 100;
		case 2263:
			return 2181;
		case 2006:
		case 1432:
		case 752:
		case 3064:
		case 2026: // lesser
			return 65;
		case 3347:
		case 3346:
			return 3325;
		case 1192:
			return 1244;
		case 3062:
			return 2953;
		case 3060:
			return 2964;
		case 2892: // Spinolyp
		case 2894: // Spinolyp
		case 2896: // Spinolyp
			return 2869;
		case 423: // Dust Devil
		case 7404:
			return 1555;
		case 2054:
			return 3148;
		case 1354:
		case 1341:
		case 2455:// dagannoth
		case 2454:
		case 2456:
		case 983:
		case 984:
		case 985:
		case 986:
		case 987:
		case 988:
			return 1340;
		case 127:
			return 186;
		case 291:
			return 100;
		case 2267: // supreme
		case 2266: // prime
		case 2265: // rex
			return 2852;
		case 3452:// penance queen
			return 5413;
		case 2745:
			return 2653;
		case 2743:
			return 2645;
		case 1270:// metal dragon
		case 273:
		case 274:
		case 6593:
			return 89;
		case 2598:
		case 2599:
		case 2600:
		case 2610:
		case 2601:
		case 2602:
		case 2603:
		case 2606:// tzhaar-xil
		case 2591:
		case 2604:// tzhar-hur
			return 2606;
		case 3121:
			return 2629;
		case 66:
		case 67:
		case 168:
		case 169:
		case 162:
		case 68:// gnomes
			return 193;
		case 160:
		case 161:
			return 194;
		case 163:
		case 164:
			return 193;
		case 438:
		case 439:
		case 440:
		case 441:
		case 442: // Tree spirit
		case 443:
			return 95;
		case 391:
		case 392:
		case 393:
		case 394:
		case 395:// river troll
		case 396:
			return 285;

		case 1153:
		case 1154:
		case 1155:
		case 1156:
		case 1157:
		case 1158: // kalphite
			return 1186;
		case 1160: // kalphite
			return 1179;
		case 2734:
		case 2627:// tzhaar
			return 2622;
		case 2630:
		case 2629:
		case 2736:
		case 2738:
			return 2626;
		case 2631:
		case 2632:
			return 2629;
		case 2741:
			return 2635;

		case 908:
			return 129;
		case 909:
			return 147;
		case 911:
			return 65;

		case 1459:// monkey guard
			return 1403;

		case 435: // pyrefiend
		case 3406:
		case 7394:
			return 1581;

		case 414:// banshee
		case 7272:
		case 7391:
		case 7390:
			return 1525;

		case 448:
		case 7388:
		case 1649:
		case 1650:
		case 1651:
		case 1652:
		case 1653:
		case 1654:
		case 1655:
		case 1656:
		case 1657:// crawling hand
			return 1591;

		case 484:
		case 7276:
		case 1619:// bloodveld
		case 7398:
		case 7397:
			return 1550;

		case 446:
		case 7396:
		case 1644:
		case 1645:
		case 1646:
		case 1647:// infernal mage
			return 430;

		case 11:// nechryael
		case 7411:
		case 7278:
			return 1529;

		case 1543:
		case 1611:// gargoyle
		case 7407:
			return 1519;

		case 415:// abyssal demon
		case 7410:
			return 1537;

		case 1770:
		case 1771:
		case 1772:
		case 1773:
		case 2678:
		case 2679:
		case 1774:
		case 1775:
		case 1776:// goblins
			return 312;

		case 132: // monkey
			return 221;

		case 1030:
		case 1031:
		case 1032:
		case 1033:
		case 1034:
		case 1035: // wolfman
			return 6538;

		case 1456:// monkey archer
			return 1395;

		case 108:// scorpion
		case 1477:
			return 247;
		case 107:
		case 144:
			return 6255;

		case 1125:// dad
			return 285;

		case 1096:
		case 1097:
		case 1098:
		case 1942:
		case 1101:// troll
		case 1106:
			return 285;
		case 1095:
			return 285;

		case 123:
		case 122:// hobgoblin
			return 165;

		case 135:// hellhound
		case 142:
		case 95:
			return 6578;

		case 1593:
		case 152:
		case 45:
		case 1558: // wolf
		case 1954:
			return 76;

		case 89:
			return 6375;
		case 133: // unicorns
			return 290;

		case 105:// bear
			return 4921;

		case 74:
		case 75:
		case 76:
			return 5574;

		case 73:
		case 5399:
		case 751: // zombie
		case 77:
			return 5574;

		case 60:
		case 64:
		case 59:
		case 61:
		case 63:
		case 3021:
		case 2035: // spider
		case 62:
		case 1009:
			return 5328;

		case 2534:
		case 85:
		case 7258:
		case 749:
		case 104:
		case 655:
		case 491: // ghost
			return 124;

		case 1585:
		case 2084: // giant
			return 4671;
		case 111:
			return 4671;
		case 2098:
		case 2463:
		case 116:
		case 891:
			return 4651;

		case 239: // kbd
			return 89;
		case 264:// green dragon
		case 268:
		case 2919:
		case 270:
		case 742:
		case 1589:
		case 247:
		case 52:
		case 7273:
		case 7274:
		case 259:
		case 7275:
			return 89;
		case 2889:
			return 2860;
		case 81: // cow
		case 397:
			return 5850;

		case 708: // imp
			return 170;

		case 86:
		case 87:
			return 139;
		case 47:// rat
			return 2706;
		case 2457:
			return 2366;
		case 128: // snake
		case 1479:
			return 276;

		case 1017:
		case 2693:
		case 41: // chicken
			return 55;

		case 90:
		case 91:
		case 93: // skeleton
			return 261;
		case 1:
			return 424;
		default:
			return -1;
		}
	}
}
