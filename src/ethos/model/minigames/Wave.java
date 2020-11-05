package ethos.model.minigames;

import ethos.model.players.Player;
import ethos.util.Misc;

/**
 * 
 * @author Jason http://www.rune-server.org/members/jason
 * @date Oct 17, 2013
 */
public class Wave {

	public static int[][] SPAWN_DATA = { { 2403, 5079 }, { 2396, 5074 }, { 2387, 5072 }, { 2388, 5085 }, { 2389, 5096 }, { 2403, 5097 }, { 2410, 5087 } };

	public static int[][] getWaveForType(Player player) {
		if (player.waveType == 1)
			return LEVEL_1;
		else if (player.waveType == 2)
			return LEVEL_2;
		else if (player.waveType == 3)
			return LEVEL_3;
		else
			return LEVEL_1;
	}

	public static final int TZ_KIH = 3116, TZ_KEK_SPAWN = 3118, TZ_KEK = 3120, TOK_XIL = 3121, YT_MEJKOT = 3123, KET_ZEK = 3125, TZTOK_JAD = 3127;

	/*
	 * public static final int TZ_KIH = 2627, TZ_KEK_SPAWN = 2738, TZ_KEK = 2630, TOK_XIL = 2631, YT_MEJKOT = 2741, KET_ZEK = 2743, TZTOK_JAD = 2745;
	 */

	public static int getHp(int npc) {
		switch (npc) {
		case TZ_KIH:
		case TZ_KEK_SPAWN:
			return 22;
		case TZ_KEK:
			return 45;
		case TOK_XIL:
			return 90;
		case YT_MEJKOT:
			return 180;
		case KET_ZEK:
			return 360;
		case TZTOK_JAD:
			return 720;
		}
		return 50 + Misc.random(50);
	}

	public static int getMax(int npc) {
		switch (npc) {
		case TZ_KIH:
		case TZ_KEK_SPAWN:
			return 4;
		case TZ_KEK:
			return 7;
		case TOK_XIL:
			return 13;
		case YT_MEJKOT:
			return 20;
		case KET_ZEK:
			return 30;
		case TZTOK_JAD:
			return 97;
		}
		return 5 + Misc.random(5);
	}

	public static int getAtk(int npc) {
		switch (npc) {
		case TZ_KIH:
		case TZ_KEK_SPAWN:
			return 30;
		case TZ_KEK:
			return 50;
		case TOK_XIL:
			return 80;
		case YT_MEJKOT:
			return 100;
		case KET_ZEK:
			return 200;
		case TZTOK_JAD:
			return 450;
		}
		return 50 + Misc.random(50);
	}

	public static int getDef(int npc) {
		switch (npc) {
		case TZ_KIH:
		case TZ_KEK_SPAWN:
			return 30;
		case TZ_KEK:
			return 75;
		case TOK_XIL:
			return 100;
		case YT_MEJKOT:
			return 100;
		case KET_ZEK:
			return 200;
		case TZTOK_JAD:
			return 500;
		}
		return 50 + Misc.random(50);
	}

	public static final int[][] LEVEL_1 = { { KET_ZEK, TOK_XIL, TZ_KEK, TZ_KEK }, { KET_ZEK, TOK_XIL, TOK_XIL }, { KET_ZEK, YT_MEJKOT }, { KET_ZEK, YT_MEJKOT, TZ_KIH },
			{ KET_ZEK, YT_MEJKOT, TZ_KIH, TZ_KIH }, { KET_ZEK, YT_MEJKOT, TZ_KEK }, { KET_ZEK, YT_MEJKOT, TZ_KEK, TZ_KIH }, { KET_ZEK, YT_MEJKOT, TZ_KEK, TZ_KIH, TZ_KIH },
			{ KET_ZEK, YT_MEJKOT, TZ_KEK, TZ_KEK }, { KET_ZEK, YT_MEJKOT, TOK_XIL }, { KET_ZEK, YT_MEJKOT, TOK_XIL, TZ_KIH }, { KET_ZEK, YT_MEJKOT, TOK_XIL, TZ_KIH, TZ_KIH },
			{ KET_ZEK, YT_MEJKOT, TOK_XIL, TZ_KEK }, { KET_ZEK, YT_MEJKOT, TOK_XIL, TZ_KEK, TZ_KIH }, { KET_ZEK, YT_MEJKOT, TOK_XIL, TZ_KEK, TZ_KIH, TZ_KIH },
			{ KET_ZEK, YT_MEJKOT, TOK_XIL, TZ_KEK, TZ_KEK }, { KET_ZEK, YT_MEJKOT, TOK_XIL, TOK_XIL }, { KET_ZEK, YT_MEJKOT, YT_MEJKOT }, { KET_ZEK, KET_ZEK }, { TZTOK_JAD } };

	public static final int[][] LEVEL_2 = { { KET_ZEK }, { KET_ZEK, TZ_KIH }, { KET_ZEK, TZ_KIH, TZ_KIH }, { KET_ZEK, TZ_KEK }, { KET_ZEK, TZ_KEK, TZ_KIH },
			{ KET_ZEK, TZ_KEK, TZ_KIH, TZ_KIH }, { KET_ZEK, TZ_KEK, TZ_KEK }, { KET_ZEK, TOK_XIL }, { KET_ZEK, TOK_XIL, TZ_KIH }, { KET_ZEK, TOK_XIL, TZ_KIH, TZ_KIH },
			{ KET_ZEK, TOK_XIL, TZ_KEK }, { KET_ZEK, TOK_XIL, TZ_KEK, TZ_KIH }, { KET_ZEK, TOK_XIL, TZ_KEK, TZ_KIH, TZ_KIH }, { KET_ZEK, TOK_XIL, TZ_KEK, TZ_KEK },
			{ KET_ZEK, TOK_XIL, TOK_XIL }, { KET_ZEK, YT_MEJKOT }, { KET_ZEK, YT_MEJKOT, TZ_KIH }, { KET_ZEK, YT_MEJKOT, TZ_KIH, TZ_KIH }, { KET_ZEK, YT_MEJKOT, TZ_KEK },
			{ KET_ZEK, YT_MEJKOT, TZ_KEK, TZ_KIH }, { KET_ZEK, YT_MEJKOT, TZ_KEK, TZ_KIH, TZ_KIH }, { KET_ZEK, YT_MEJKOT, TZ_KEK, TZ_KEK }, { KET_ZEK, YT_MEJKOT, TOK_XIL },
			{ KET_ZEK, YT_MEJKOT, TOK_XIL, TZ_KIH }, { KET_ZEK, YT_MEJKOT, TOK_XIL, TZ_KIH, TZ_KIH }, { KET_ZEK, YT_MEJKOT, TOK_XIL, TZ_KEK },
			{ KET_ZEK, YT_MEJKOT, TOK_XIL, TZ_KEK, TZ_KIH }, { KET_ZEK, YT_MEJKOT, TOK_XIL, TZ_KEK, TZ_KIH, TZ_KIH }, { KET_ZEK, YT_MEJKOT, TOK_XIL, TZ_KEK, TZ_KEK },
			{ KET_ZEK, YT_MEJKOT, TOK_XIL, TOK_XIL }, { KET_ZEK, YT_MEJKOT, YT_MEJKOT }, { KET_ZEK, KET_ZEK }, { TZTOK_JAD } };

	public static final int[][] LEVEL_3 = { { TZ_KIH }, { TZ_KIH, TZ_KIH }, { TZ_KEK }, { TZ_KEK, TZ_KIH }, { TZ_KEK, TZ_KIH, TZ_KIH }, { TZ_KEK, TZ_KEK }, { TOK_XIL },
			{ TOK_XIL, TZ_KIH }, { TOK_XIL, TZ_KIH, TZ_KIH }, { TOK_XIL, TZ_KEK }, { TOK_XIL, TZ_KEK, TZ_KIH }, { TOK_XIL, TZ_KEK, TZ_KIH, TZ_KIH }, { TOK_XIL, TZ_KEK, TZ_KEK },
			{ TOK_XIL, TOK_XIL }, { YT_MEJKOT }, { YT_MEJKOT, TZ_KIH }, { YT_MEJKOT, TZ_KIH, TZ_KIH }, { YT_MEJKOT, TZ_KEK }, { YT_MEJKOT, TZ_KEK, TZ_KIH },
			{ YT_MEJKOT, TZ_KEK, TZ_KIH, TZ_KIH }, { YT_MEJKOT, TZ_KEK, TZ_KEK }, { YT_MEJKOT, TOK_XIL }, { YT_MEJKOT, TOK_XIL, TZ_KIH }, { YT_MEJKOT, TOK_XIL, TZ_KIH, TZ_KIH },
			{ YT_MEJKOT, TOK_XIL, TZ_KEK }, { YT_MEJKOT, TOK_XIL, TZ_KEK, TZ_KIH }, { YT_MEJKOT, TOK_XIL, TZ_KEK, TZ_KIH, TZ_KIH }, { YT_MEJKOT, TOK_XIL, TZ_KEK, TZ_KEK },
			{ YT_MEJKOT, TOK_XIL, TOK_XIL }, { YT_MEJKOT, YT_MEJKOT }, { KET_ZEK }, { KET_ZEK, TZ_KIH }, { KET_ZEK, TZ_KIH, TZ_KIH }, { KET_ZEK, TZ_KEK },
			{ KET_ZEK, TZ_KEK, TZ_KIH }, { KET_ZEK, TZ_KEK, TZ_KIH, TZ_KIH }, { KET_ZEK, TZ_KEK, TZ_KEK }, { KET_ZEK, TOK_XIL }, { KET_ZEK, TOK_XIL, TZ_KIH },
			{ KET_ZEK, TOK_XIL, TZ_KIH, TZ_KIH }, { KET_ZEK, TOK_XIL, TZ_KEK }, { KET_ZEK, TOK_XIL, TZ_KEK, TZ_KIH }, { KET_ZEK, TOK_XIL, TZ_KEK, TZ_KIH, TZ_KIH },
			{ KET_ZEK, TOK_XIL, TZ_KEK, TZ_KEK }, { KET_ZEK, TOK_XIL, TOK_XIL }, { KET_ZEK, YT_MEJKOT }, { KET_ZEK, YT_MEJKOT, TZ_KIH }, { KET_ZEK, YT_MEJKOT, TZ_KIH, TZ_KIH },
			{ KET_ZEK, YT_MEJKOT, TZ_KEK }, { KET_ZEK, YT_MEJKOT, TZ_KEK, TZ_KIH }, { KET_ZEK, YT_MEJKOT, TZ_KEK, TZ_KIH, TZ_KIH }, { KET_ZEK, YT_MEJKOT, TZ_KEK, TZ_KEK },
			{ KET_ZEK, YT_MEJKOT, TOK_XIL }, { KET_ZEK, YT_MEJKOT, TOK_XIL, TZ_KIH }, { KET_ZEK, YT_MEJKOT, TOK_XIL, TZ_KIH, TZ_KIH }, { KET_ZEK, YT_MEJKOT, TOK_XIL, TZ_KEK },
			{ KET_ZEK, YT_MEJKOT, TOK_XIL, TZ_KEK, TZ_KIH }, { KET_ZEK, YT_MEJKOT, TOK_XIL, TZ_KEK, TZ_KIH, TZ_KIH }, { KET_ZEK, YT_MEJKOT, TOK_XIL, TZ_KEK, TZ_KEK },
			{ KET_ZEK, YT_MEJKOT, TOK_XIL, TOK_XIL }, { KET_ZEK, YT_MEJKOT, YT_MEJKOT }, { KET_ZEK, KET_ZEK }, { TZTOK_JAD } };

}
