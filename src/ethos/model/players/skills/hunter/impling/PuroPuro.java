package ethos.model.players.skills.hunter.impling;

import ethos.model.players.Player;

public class PuroPuro {
	
	/**
	 * Impling data
	 * id -> The id of the impling
	 * x -> The x coordinate of the impling
	 * y -> The y coordinate of the impling
	 */
	public static final int[][] IMPLINGS = {
			/**
			 * Baby imps
			 */
			{1635, 2612, 4318},
			{1635, 2602, 4314},
			{1635, 2610, 4338},
			{1635, 2582, 4344},
			{1635, 2578, 4344},
			{1635, 2568, 4311},
			{1635, 2583, 4295},
			{1635, 2582, 4330},
			{1635, 2600, 4303},
			{1635, 2611, 4301},
			{1635, 2618, 4329},

			/**
			 * Young imps
			 */
			{1636, 2591, 4332},
			{1636, 2600, 4338},
			{1636, 2595, 4345},
			{1636, 2610, 4327},
			{1636, 2617, 4314},
			{1636, 2619, 4294},
			{1636, 2599, 4294},
			{1636, 2575, 4303},
			{1636, 2570, 4299},

			/**
			 * Gourment imps
			 */
			{1637, 2573, 4339},
			{1637, 2567, 4328},
			{1637, 2593, 4297},
			{1637, 2618, 4305},
			{1637, 2605, 4316},
			{1637, 2596, 4333},

			/**
			 * Earth imps
			 */
			{1638, 2592, 4338},
			{1638, 2611, 4345},
			{1638, 2617, 4339},
			{1638, 2614, 4301},
			{1638, 2606, 4295},
			{1638, 2581, 4299},

			/**
			 * Essence imps
			 */
			{1639, 2602, 4328},
			{1639, 2608, 4333},
			{1639, 2609, 4296},
			{1639, 2581, 4304},
			{1639, 2570, 4318},

			/**
			 * Eclectic imps
			 */
			{1640, 2611, 4310},
			{1640, 2617, 4319},
			{1640, 2600, 4347},
			{1640, 2570, 4326},
			{1640, 2579, 4310},

			/**
			 * Spirit imps
			 */

			/**
			 * Nature imps
			 */
			{1641, 2581, 4310},
			{1641, 2581, 4310},
			{1641, 2603, 4333},
			{1641, 2576, 4335},
			{1641, 2588, 4345},

			/**
			 * Magpie imps
			 */
			{1642, 2612, 4324},
			{1642, 2602, 4323},
			{1642, 2587, 4348},
			{1642, 2564, 4320},
			{1642, 2566, 4295},

			/**
			 * Ninja imps
			 */
			{1643, 2570, 4347},
			{1643, 2572, 4327},
			{1643, 2578, 4318},
			{1643, 2610, 4312},
			{1643, 2594, 4341},

			/**
			 * Dragon imps
			 */
			{1654, 2613, 4341},
			{1654, 2585, 4337},
			{1654, 2576, 4319},
			{1654, 2576, 4294},
			{1654, 2592, 4305},
	};
	
	public static void magicalWheat(Player player) {
		int x = player.absX, x2 = x;
		int y = player.absY, y2 = y;
		
		switch (x) {
		case 2584:
			x2 = 2582;
			break;
		case 2582:
			x2 = 2584;
			break;
		case 2599:
			x2 = 2601;
			break;
		case 2601:
			x2 = 2599;
			break;
			
		case 2581:
			x2 = 2579;
			break;
		case 2579:
			x2 = 2581;
			break;
		case 2602:
			x2 = 2604;
			break;
		case 2604:
			x2 = 2602;
			break;
			
		case 2578:
			x2 = 2576;
			break;
		case 2576:
			x2 = 2578;
			break;
		case 2605:
			x2 = 2607;
			break;
		case 2607:
			x2 = 2605;
			break;
			
		case 2575:
			x2 = 2573;
			break;
		case 2573:
			x2 = 2575;
			break;
		case 2608:
			x2 = 2610;
			break;
		case 2610:
			x2 = 2608;
			break;
			
		case 2572:
			x2 = 2570;
			break;
		case 2570:
			x2 = 2572;
			break;
		case 2611:
			x2 = 2613;
			break;
		case 2613:
			x2 = 2611;
			break;
			
		case 2569:
			x2 = 2567;
			break;
		case 2567:
			x2 = 2569;
			break;
		case 2614:
			x2 = 2616;
			break;
		case 2616:
			x2 = 2614;
			break;
			
		case 2566:
			x2 = 2564;
			break;
		case 2564:
			x2 = 2566;
			break;
		case 2617:
			x2 = 2619;
			break;
		case 2619:
			x2 = 2617;
			break;
		}
		
		switch (y) {
		case 4312:
			y2 = 4310;
			break;
		case 4310:
			y2 = 4312;
			break;
		case 4327:
			y2 = 4329;
			break;
		case 4329:
			y2 = 4327;
			break;
			
		case 4309:
			y2 = 4307;
			break;
		case 4307:
			y2 = 4309;
			break;
		case 4330:
			y2 = 4332;
			break;
		case 4332:
			y2 = 4330;
			break;
			
		case 4306:
			y2 = 4304;
			break;
		case 4304:
			y2 = 4306;
			break;
		case 4333:
			y2 = 4335;
			break;
		case 4335:
			y2 = 4333;
			break;
			
		case 4303:
			y2 = 4301;
			break;
		case 4301:
			y2 = 4303;
			break;
		case 4336:
			y2 = 4338;
			break;
		case 4338:
			y2 = 4336;
			break;
			
		case 4300:
			y2 = 4298;
			break;
		case 4298:
			y2 = 4300;
			break;
		case 4339:
			y2 = 4341;
			break;
		case 4341:
			y2 = 4339;
			break;
			
		case 4297:
			y2 = 4295;
			break;
		case 4295:
			y2 = 4297;
			break;
			
		case 4345:
			y2 = 4347;
			break;
		case 4347:
			y2 = 4345;
			break;
		}
		x2 -= x;
		y2 -= y;
		if (System.currentTimeMillis() - player.lastWheatPass < 2000) {
			return;
		}
		player.sendMessage("You use your strength to push through the wheat.");
		final int goX = x2, goY = y2;
		
		player.getAgilityHandler().move(player, goX, goY, 6594, -1);
		player.lastWheatPass = System.currentTimeMillis();
	}
}
