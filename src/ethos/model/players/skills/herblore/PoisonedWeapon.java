package ethos.model.players.skills.herblore;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import ethos.model.items.Item;
import ethos.model.items.ItemDefinition;
import ethos.model.players.Player;

/**
 * Enum containing all poisonable weapons.
 * 
 * @author Emiel
 *
 */
public enum PoisonedWeapon {

	ABYSSAL_DAGGER_P(PoisonLevel.NORMAL, 13265, 13267), 
	ABYSSAL_DAGGER_P_PLUS(PoisonLevel.PLUS, 13265, 13269), 
	ABYSSAL_DAGGER_P_PLUS_PLUS(PoisonLevel.PLUSPLUS, 13265, 13271), 
	ADAMANT_ARROW_P(PoisonLevel.NORMAL, 890, 891), 
	ADAMANT_ARROW_P_PLUS(PoisonLevel.PLUS, 890, 5620), 
	ADAMANT_ARROW_P_PLUS_PLUS(PoisonLevel.PLUSPLUS, 890, 5626), 
	ADAMANT_BOLTS_P(PoisonLevel.NORMAL, 9143, 9290), 
	ADAMANT_BOLTS_P_PLUS(PoisonLevel.PLUS, 9143, 9297), 
	ADAMANT_BOLTS_P_PLUS_PLUS(PoisonLevel.PLUSPLUS, 9143, 9304), 
	ADAMANT_DAGGER_P(PoisonLevel.NORMAL, 1211, 1227), 
	ADAMANT_DAGGER_P_PLUS(PoisonLevel.PLUS, 1211, 5676), 
	ADAMANT_DAGGER_P_PLUS_PLUS(PoisonLevel.PLUSPLUS, 1211, 5694), 
	ADAMANT_DART_P(PoisonLevel.NORMAL, 810, 816), 
	ADAMANT_DART_P_PLUS(PoisonLevel.PLUS, 810, 5633), 
	ADAMANT_DART_P_PLUS_PLUS(PoisonLevel.PLUSPLUS, 810, 5640), 
	ADAMANT_HASTA_P(PoisonLevel.NORMAL, 11375, 11407), 
	ADAMANT_HASTA_P_PLUS(PoisonLevel.PLUS, 11375, 11410), 
	ADAMANT_HASTA_P_PLUS_PLUS(PoisonLevel.PLUSPLUS, 11375, 11412), 
	ADAMANT_JAVELIN_P(PoisonLevel.NORMAL, 829, 835), 
	ADAMANT_JAVELIN_P_PLUS(PoisonLevel.PLUS, 829, 5646), 
	ADAMANT_JAVELIN_P_PLUS_PLUS(PoisonLevel.PLUSPLUS, 829, 5652), 
	ADAMANT_KNIFE_P(PoisonLevel.NORMAL, 867, 875), 
	ADAMANT_KNIFE_P_PLUS(PoisonLevel.PLUS, 867, 5659), 
	ADAMANT_KNIFE_P_PLUS_PLUS(PoisonLevel.PLUSPLUS, 867, 5666), 
	ADAMANT_SPEAR_P(PoisonLevel.NORMAL, 1245, 1259), 
	ADAMANT_SPEAR_P_PLUS(PoisonLevel.PLUS, 1245, 5712), 
	ADAMANT_SPEAR_P_PLUS_PLUS(PoisonLevel.PLUSPLUS, 1245, 5726), 
	BLACK_DAGGER_P(PoisonLevel.NORMAL, 1217, 1233), 
	BLACK_DAGGER_P_PLUS(PoisonLevel.PLUS, 1217, 5682), 
	BLACK_DAGGER_P_PLUS_PLUS(PoisonLevel.PLUSPLUS, 1217, 5700), 
	BLACK_DART_P(PoisonLevel.NORMAL, 3093, 3094), 
	BLACK_DART_P_PLUS(PoisonLevel.PLUS, 3093, 5631), 
	BLACK_DART_P_PLUS_PLUS(PoisonLevel.PLUSPLUS, 3093, 5638), 
	BLACK_KNIFE_P(PoisonLevel.NORMAL, 869, 874), 
	BLACK_KNIFE_P_PLUS(PoisonLevel.PLUS, 869, 5658), 
	BLACK_KNIFE_P_PLUS_PLUS(PoisonLevel.PLUSPLUS, 869, 5665), 
	BLACK_SPEAR_P(PoisonLevel.NORMAL, 4580, 4582), 
	BLACK_SPEAR_P_PLUS(PoisonLevel.PLUS, 4580, 5734), 
	BLACK_SPEAR_P_PLUS_PLUS(PoisonLevel.PLUSPLUS, 4580, 5736), 
	BLURITE_BOLTS_P(PoisonLevel.NORMAL, 9139, 9286), 
	BLURITE_BOLTS_P_PLUS(PoisonLevel.PLUS, 9139, 9293), 
	BLURITE_BOLTS_P_PLUS_PLUS(PoisonLevel.PLUSPLUS, 9139, 9300), 
	BONE_DAGGER_P(PoisonLevel.NORMAL, 8872, 8874), 
	BONE_DAGGER_P_PLUS(PoisonLevel.PLUS, 8872, 8876), 
	BONE_DAGGER_P_PLUS_PLUS(PoisonLevel.PLUSPLUS,
																																																																										8872,
																																																																										8878), BRONZE_ARROW_P(
																																																																												PoisonLevel.NORMAL,
																																																																												882,
																																																																												883), BRONZE_ARROW_P_PLUS(
																																																																														PoisonLevel.PLUS,
																																																																														882,
																																																																														5616), BRONZE_ARROW_P_PLUS_PLUS(
																																																																																PoisonLevel.PLUSPLUS,
																																																																																882,
																																																																																5622), BRONZE_DAGGER_P(
																																																																																		PoisonLevel.NORMAL,
																																																																																		1205,
																																																																																		1221), BRONZE_DAGGER_P_PLUS(
																																																																																				PoisonLevel.PLUS,
																																																																																				1205,
																																																																																				5670), BRONZE_DAGGER_P_PLUS_PLUS(
																																																																																						PoisonLevel.PLUSPLUS,
																																																																																						1205,
																																																																																						5688), BRONZE_DART_P(
																																																																																								PoisonLevel.NORMAL,
																																																																																								806,
																																																																																								812), BRONZE_DART_P_PLUS(
																																																																																										PoisonLevel.PLUS,
																																																																																										806,
																																																																																										5628), BRONZE_DART_P_PLUS_PLUS(
																																																																																												PoisonLevel.PLUSPLUS,
																																																																																												806,
																																																																																												5635), BRONZE_HASTA_P(
																																																																																														PoisonLevel.NORMAL,
																																																																																														11367,
																																																																																														11379), BRONZE_HASTA_P_PLUS(
																																																																																																PoisonLevel.PLUS,
																																																																																																11367,
																																																																																																11382), BRONZE_HASTA_P_PLUS_PLUS(
																																																																																																		PoisonLevel.PLUSPLUS,
																																																																																																		11367,
																																																																																																		11384), BRONZE_JAVELIN_P(
																																																																																																				PoisonLevel.NORMAL,
																																																																																																				825,
																																																																																																				831), BRONZE_JAVELIN_P_PLUS(
																																																																																																						PoisonLevel.PLUS,
																																																																																																						825,
																																																																																																						5642), BRONZE_JAVELIN_P_PLUS_PLUS(
																																																																																																								PoisonLevel.PLUSPLUS,
																																																																																																								825,
																																																																																																								5648), BRONZE_KNIFE_P(
																																																																																																										PoisonLevel.NORMAL,
																																																																																																										864,
																																																																																																										870), BRONZE_KNIFE_P_PLUS(
																																																																																																												PoisonLevel.PLUS,
																																																																																																												864,
																																																																																																												5654), BRONZE_KNIFE_P_PLUS_PLUS(
																																																																																																														PoisonLevel.PLUSPLUS,
																																																																																																														864,
																																																																																																														5661), BRONZE_SPEAR_P(
																																																																																																																PoisonLevel.NORMAL,
																																																																																																																1237,
																																																																																																																1251), BRONZE_SPEAR_P_PLUS(
																																																																																																																		PoisonLevel.PLUS,
																																																																																																																		1237,
																																																																																																																		5704), BRONZE_SPEAR_P_PLUS_PLUS(
																																																																																																																				PoisonLevel.PLUSPLUS,
																																																																																																																				1237,
																																																																																																																				5718), DRAGON_ARROW_P(
																																																																																																																						PoisonLevel.NORMAL,
																																																																																																																						11212,
																																																																																																																						11227), DRAGON_ARROW_P_PLUS(
																																																																																																																								PoisonLevel.PLUS,
																																																																																																																								11212,
																																																																																																																								11228), DRAGON_ARROW_P_PLUS_PLUS(
																																																																																																																										PoisonLevel.PLUSPLUS,
																																																																																																																										11212,
																																																																																																																										11229), DRAGON_DAGGER_P(
																																																																																																																												PoisonLevel.NORMAL,
																																																																																																																												1215,
																																																																																																																												1231), DRAGON_DAGGER_P_PLUS(
																																																																																																																														PoisonLevel.PLUS,
																																																																																																																														1215,
																																																																																																																														5680), DRAGON_DAGGER_P_PLUS_PLUS(
																																																																																																																																PoisonLevel.PLUSPLUS,
																																																																																																																																1215,
																																																																																																																																5698), DRAGON_DART_P(
																																																																																																																																		PoisonLevel.NORMAL,
																																																																																																																																		11230,
																																																																																																																																		11231), DRAGON_DART_P_PLUS(
																																																																																																																																				PoisonLevel.PLUS,
																																																																																																																																				11230,
																																																																																																																																				11233), DRAGON_DART_P_PLUS_PLUS(
																																																																																																																																						PoisonLevel.PLUSPLUS,
																																																																																																																																						11230,
																																																																																																																																						11234), DRAGON_SPEAR_P(
																																																																																																																																								PoisonLevel.NORMAL,
																																																																																																																																								1249,
																																																																																																																																								1263), DRAGON_SPEAR_P_PLUS(
																																																																																																																																										PoisonLevel.PLUS,
																																																																																																																																										1249,
																																																																																																																																										5716), DRAGON_SPEAR_P_PLUS_PLUS(
																																																																																																																																												PoisonLevel.PLUSPLUS,
																																																																																																																																												1249,
																																																																																																																																												5730), IRON_ARROW_P(
																																																																																																																																														PoisonLevel.NORMAL,
																																																																																																																																														884,
																																																																																																																																														885), IRON_ARROW_P_PLUS(
																																																																																																																																																PoisonLevel.PLUS,
																																																																																																																																																884,
																																																																																																																																																5617), IRON_ARROW_P_PLUS_PLUS(
																																																																																																																																																		PoisonLevel.PLUSPLUS,
																																																																																																																																																		884,
																																																																																																																																																		5623), IRON_BOLTS_P(
																																																																																																																																																				PoisonLevel.NORMAL,
																																																																																																																																																				9140,
																																																																																																																																																				9287), IRON_BOLTS_P_PLUS(
																																																																																																																																																						PoisonLevel.PLUS,
																																																																																																																																																						9140,
																																																																																																																																																						9294), IRON_BOLTS_P_PLUS_PLUS(
																																																																																																																																																								PoisonLevel.PLUSPLUS,
																																																																																																																																																								9140,
																																																																																																																																																								9301), IRON_DAGGER_P(
																																																																																																																																																										PoisonLevel.NORMAL,
																																																																																																																																																										1203,
																																																																																																																																																										1219), IRON_DAGGER_P_PLUS(
																																																																																																																																																												PoisonLevel.PLUS,
																																																																																																																																																												1203,
																																																																																																																																																												5668), IRON_DAGGER_P_PLUS_PLUS(
																																																																																																																																																														PoisonLevel.PLUSPLUS,
																																																																																																																																																														1203,
																																																																																																																																																														5686), IRON_DART_P(
																																																																																																																																																																PoisonLevel.NORMAL,
																																																																																																																																																																807,
																																																																																																																																																																813), IRON_DART_P_PLUS(
																																																																																																																																																																		PoisonLevel.PLUS,
																																																																																																																																																																		807,
																																																																																																																																																																		5629), IRON_DART_P_PLUS_PLUS(
																																																																																																																																																																				PoisonLevel.PLUSPLUS,
																																																																																																																																																																				807,
																																																																																																																																																																				5636), IRON_HASTA_P(
																																																																																																																																																																						PoisonLevel.NORMAL,
																																																																																																																																																																						11369,
																																																																																																																																																																						11386), IRON_HASTA_P_PLUS(
																																																																																																																																																																								PoisonLevel.PLUS,
																																																																																																																																																																								11369,
																																																																																																																																																																								11389), IRON_HASTA_P_PLUS_PLUS(
																																																																																																																																																																										PoisonLevel.PLUSPLUS,
																																																																																																																																																																										11369,
																																																																																																																																																																										11391), IRON_JAVELIN_P(
																																																																																																																																																																												PoisonLevel.NORMAL,
																																																																																																																																																																												826,
																																																																																																																																																																												832), IRON_JAVELIN_P_PLUS(
																																																																																																																																																																														PoisonLevel.PLUS,
																																																																																																																																																																														826,
																																																																																																																																																																														5643), IRON_JAVELIN_P_PLUS_PLUS(
																																																																																																																																																																																PoisonLevel.PLUSPLUS,
																																																																																																																																																																																826,
																																																																																																																																																																																5649), IRON_KNIFE_P(
																																																																																																																																																																																		PoisonLevel.NORMAL,
																																																																																																																																																																																		863,
																																																																																																																																																																																		871), IRON_KNIFE_P_PLUS(
																																																																																																																																																																																				PoisonLevel.PLUS,
																																																																																																																																																																																				863,
																																																																																																																																																																																				5655), IRON_KNIFE_P_PLUS_PLUS(
																																																																																																																																																																																						PoisonLevel.PLUSPLUS,
																																																																																																																																																																																						863,
																																																																																																																																																																																						5662), IRON_SPEAR_P(
																																																																																																																																																																																								PoisonLevel.NORMAL,
																																																																																																																																																																																								1239,
																																																																																																																																																																																								1253), IRON_SPEAR_P_PLUS(
																																																																																																																																																																																										PoisonLevel.PLUS,
																																																																																																																																																																																										1239,
																																																																																																																																																																																										5706), IRON_SPEAR_P_PLUS_PLUS(
																																																																																																																																																																																												PoisonLevel.PLUSPLUS,
																																																																																																																																																																																												1239,
																																																																																																																																																																																												5720), MITHRIL_ARROW_P(
																																																																																																																																																																																														PoisonLevel.NORMAL,
																																																																																																																																																																																														888,
																																																																																																																																																																																														889), MITHRIL_ARROW_P_PLUS(
																																																																																																																																																																																																PoisonLevel.PLUS,
																																																																																																																																																																																																888,
																																																																																																																																																																																																5619), MITHRIL_ARROW_P_PLUS_PLUS(
																																																																																																																																																																																																		PoisonLevel.PLUSPLUS,
																																																																																																																																																																																																		888,
																																																																																																																																																																																																		5625), MITHRIL_BOLTS_P(
																																																																																																																																																																																																				PoisonLevel.NORMAL,
																																																																																																																																																																																																				9142,
																																																																																																																																																																																																				9289), MITHRIL_BOLTS_P_PLUS(
																																																																																																																																																																																																						PoisonLevel.PLUS,
																																																																																																																																																																																																						9142,
																																																																																																																																																																																																						9296), MITHRIL_BOLTS_P_PLUS_PLUS(
																																																																																																																																																																																																								PoisonLevel.PLUSPLUS,
																																																																																																																																																																																																								9142,
																																																																																																																																																																																																								9303), MITHRIL_DAGGER_P(
																																																																																																																																																																																																										PoisonLevel.NORMAL,
																																																																																																																																																																																																										1209,
																																																																																																																																																																																																										1225), MITHRIL_DAGGER_P_PLUS(
																																																																																																																																																																																																												PoisonLevel.PLUS,
																																																																																																																																																																																																												1209,
																																																																																																																																																																																																												5674), MITHRIL_DAGGER_P_PLUS_PLUS(
																																																																																																																																																																																																														PoisonLevel.PLUSPLUS,
																																																																																																																																																																																																														1209,
																																																																																																																																																																																																														5692), MITHRIL_DART_P(
																																																																																																																																																																																																																PoisonLevel.NORMAL,
																																																																																																																																																																																																																809,
																																																																																																																																																																																																																815), MITHRIL_DART_P_PLUS(
																																																																																																																																																																																																																		PoisonLevel.PLUS,
																																																																																																																																																																																																																		809,
																																																																																																																																																																																																																		5632), MITHRIL_DART_P_PLUS_PLUS(
																																																																																																																																																																																																																				PoisonLevel.PLUSPLUS,
																																																																																																																																																																																																																				809,
																																																																																																																																																																																																																				5639), MITHRIL_HASTA_P(
																																																																																																																																																																																																																						PoisonLevel.NORMAL,
																																																																																																																																																																																																																						11373,
																																																																																																																																																																																																																						11400), MITHRIL_HASTA_P_PLUS(
																																																																																																																																																																																																																								PoisonLevel.PLUS,
																																																																																																																																																																																																																								11373,
																																																																																																																																																																																																																								11403), MITHRIL_HASTA_P_PLUS_PLUS(
																																																																																																																																																																																																																										PoisonLevel.PLUSPLUS,
																																																																																																																																																																																																																										11373,
																																																																																																																																																																																																																										11405), MITHRIL_JAVELIN_P(
																																																																																																																																																																																																																												PoisonLevel.NORMAL,
																																																																																																																																																																																																																												828,
																																																																																																																																																																																																																												834), MITHRIL_JAVELIN_P_PLUS(
																																																																																																																																																																																																																														PoisonLevel.PLUS,
																																																																																																																																																																																																																														828,
																																																																																																																																																																																																																														5645), MITHRIL_JAVELIN_P_PLUS_PLUS(
																																																																																																																																																																																																																																PoisonLevel.PLUSPLUS,
																																																																																																																																																																																																																																828,
																																																																																																																																																																																																																																5651), MITHRIL_KNIFE_P(
																																																																																																																																																																																																																																		PoisonLevel.NORMAL,
																																																																																																																																																																																																																																		866,
																																																																																																																																																																																																																																		873), MITHRIL_KNIFE_P_PLUS(
																																																																																																																																																																																																																																				PoisonLevel.PLUS,
																																																																																																																																																																																																																																				866,
																																																																																																																																																																																																																																				5657), MITHRIL_KNIFE_P_PLUS_PLUS(
																																																																																																																																																																																																																																						PoisonLevel.PLUSPLUS,
																																																																																																																																																																																																																																						866,
																																																																																																																																																																																																																																						5664), MITHRIL_SPEAR_P(
																																																																																																																																																																																																																																								PoisonLevel.NORMAL,
																																																																																																																																																																																																																																								1243,
																																																																																																																																																																																																																																								1257), MITHRIL_SPEAR_P_PLUS(
																																																																																																																																																																																																																																										PoisonLevel.PLUS,
																																																																																																																																																																																																																																										1243,
																																																																																																																																																																																																																																										5710), MITHRIL_SPEAR_P_PLUS_PLUS(
																																																																																																																																																																																																																																												PoisonLevel.PLUSPLUS,
																																																																																																																																																																																																																																												1243,
																																																																																																																																																																																																																																												5724), RUNE_ARROW_P(
																																																																																																																																																																																																																																														PoisonLevel.NORMAL,
																																																																																																																																																																																																																																														892,
																																																																																																																																																																																																																																														893), RUNE_ARROW_P_PLUS(
																																																																																																																																																																																																																																																PoisonLevel.PLUS,
																																																																																																																																																																																																																																																892,
																																																																																																																																																																																																																																																5621), RUNE_ARROW_P_PLUS_PLUS(
																																																																																																																																																																																																																																																		PoisonLevel.PLUSPLUS,
																																																																																																																																																																																																																																																		892,
																																																																																																																																																																																																																																																		5627), RUNE_BOLTS_P(
																																																																																																																																																																																																																																																				PoisonLevel.NORMAL,
																																																																																																																																																																																																																																																				9144,
																																																																																																																																																																																																																																																				9291), RUNE_BOLTS_P_PLUS(
																																																																																																																																																																																																																																																						PoisonLevel.PLUS,
																																																																																																																																																																																																																																																						9144,
																																																																																																																																																																																																																																																						9298), RUNE_BOLTS_P_PLUS_PLUS(
																																																																																																																																																																																																																																																								PoisonLevel.PLUSPLUS,
																																																																																																																																																																																																																																																								9144,
																																																																																																																																																																																																																																																								9305), RUNE_DAGGER_P(
																																																																																																																																																																																																																																																										PoisonLevel.NORMAL,
																																																																																																																																																																																																																																																										1213,
																																																																																																																																																																																																																																																										1229), RUNE_DAGGER_P_PLUS(
																																																																																																																																																																																																																																																												PoisonLevel.PLUS,
																																																																																																																																																																																																																																																												1213,
																																																																																																																																																																																																																																																												5678), RUNE_DAGGER_P_PLUS_PLUS(
																																																																																																																																																																																																																																																														PoisonLevel.PLUSPLUS,
																																																																																																																																																																																																																																																														1213,
																																																																																																																																																																																																																																																														5696), RUNE_DART_P(
																																																																																																																																																																																																																																																																PoisonLevel.NORMAL,
																																																																																																																																																																																																																																																																811,
																																																																																																																																																																																																																																																																817), RUNE_DART_P_PLUS(
																																																																																																																																																																																																																																																																		PoisonLevel.PLUS,
																																																																																																																																																																																																																																																																		811,
																																																																																																																																																																																																																																																																		5634), RUNE_DART_P_PLUS_PLUS(
																																																																																																																																																																																																																																																																				PoisonLevel.PLUSPLUS,
																																																																																																																																																																																																																																																																				811,
																																																																																																																																																																																																																																																																				5641), RUNE_HASTA_P(
																																																																																																																																																																																																																																																																						PoisonLevel.NORMAL,
																																																																																																																																																																																																																																																																						11377,
																																																																																																																																																																																																																																																																						11414), RUNE_HASTA_P_PLUS(
																																																																																																																																																																																																																																																																								PoisonLevel.PLUS,
																																																																																																																																																																																																																																																																								11377,
																																																																																																																																																																																																																																																																								11417), RUNE_HASTA_P_PLUS_PLUS(
																																																																																																																																																																																																																																																																										PoisonLevel.PLUSPLUS,
																																																																																																																																																																																																																																																																										11377,
																																																																																																																																																																																																																																																																										11419), RUNE_JAVELIN_P(
																																																																																																																																																																																																																																																																												PoisonLevel.NORMAL,
																																																																																																																																																																																																																																																																												830,
																																																																																																																																																																																																																																																																												836), RUNE_JAVELIN_P_PLUS(
																																																																																																																																																																																																																																																																														PoisonLevel.PLUS,
																																																																																																																																																																																																																																																																														830,
																																																																																																																																																																																																																																																																														5647), RUNE_JAVELIN_P_PLUS_PLUS(
																																																																																																																																																																																																																																																																																PoisonLevel.PLUSPLUS,
																																																																																																																																																																																																																																																																																830,
																																																																																																																																																																																																																																																																																5653), RUNE_KNIFE_P(
																																																																																																																																																																																																																																																																																		PoisonLevel.NORMAL,
																																																																																																																																																																																																																																																																																		868,
																																																																																																																																																																																																																																																																																		876), RUNE_KNIFE_P_PLUS(
																																																																																																																																																																																																																																																																																				PoisonLevel.PLUS,
																																																																																																																																																																																																																																																																																				868,
																																																																																																																																																																																																																																																																																				5660), RUNE_KNIFE_P_PLUS_PLUS(
																																																																																																																																																																																																																																																																																						PoisonLevel.PLUSPLUS,
																																																																																																																																																																																																																																																																																						868,
																																																																																																																																																																																																																																																																																						5667), RUNE_SPEAR_P(
																																																																																																																																																																																																																																																																																								PoisonLevel.NORMAL,
																																																																																																																																																																																																																																																																																								1247,
																																																																																																																																																																																																																																																																																								1261), RUNE_SPEAR_P_PLUS(
																																																																																																																																																																																																																																																																																										PoisonLevel.PLUS,
																																																																																																																																																																																																																																																																																										1247,
																																																																																																																																																																																																																																																																																										5714), RUNE_SPEAR_P_PLUS_PLUS(
																																																																																																																																																																																																																																																																																												PoisonLevel.PLUSPLUS,
																																																																																																																																																																																																																																																																																												1247,
																																																																																																																																																																																																																																																																																												5728), SILVER_BOLTS_P(
																																																																																																																																																																																																																																																																																														PoisonLevel.NORMAL,
																																																																																																																																																																																																																																																																																														9145,
																																																																																																																																																																																																																																																																																														9292), SILVER_BOLTS_P_PLUS(
																																																																																																																																																																																																																																																																																																PoisonLevel.PLUS,
																																																																																																																																																																																																																																																																																																9145,
																																																																																																																																																																																																																																																																																																9299), SILVER_BOLTS_P_PLUS_PLUS(
																																																																																																																																																																																																																																																																																																		PoisonLevel.PLUSPLUS,
																																																																																																																																																																																																																																																																																																		9145,
																																																																																																																																																																																																																																																																																																		9306), STEEL_ARROW_P(
																																																																																																																																																																																																																																																																																																				PoisonLevel.NORMAL,
																																																																																																																																																																																																																																																																																																				886,
																																																																																																																																																																																																																																																																																																				887), STEEL_ARROW_P_PLUS(
																																																																																																																																																																																																																																																																																																						PoisonLevel.PLUS,
																																																																																																																																																																																																																																																																																																						886,
																																																																																																																																																																																																																																																																																																						5618), STEEL_ARROW_P_PLUS_PLUS(
																																																																																																																																																																																																																																																																																																								PoisonLevel.PLUSPLUS,
																																																																																																																																																																																																																																																																																																								886,
																																																																																																																																																																																																																																																																																																								5624), STEEL_BOLTS_P(
																																																																																																																																																																																																																																																																																																										PoisonLevel.NORMAL,
																																																																																																																																																																																																																																																																																																										9141,
																																																																																																																																																																																																																																																																																																										9288), STEEL_BOLTS_P_PLUS(
																																																																																																																																																																																																																																																																																																												PoisonLevel.PLUS,
																																																																																																																																																																																																																																																																																																												9141,
																																																																																																																																																																																																																																																																																																												9295), STEEL_BOLTS_P_PLUS_PLUS(
																																																																																																																																																																																																																																																																																																														PoisonLevel.PLUSPLUS,
																																																																																																																																																																																																																																																																																																														9141,
																																																																																																																																																																																																																																																																																																														9302), STEEL_DAGGER_P(
																																																																																																																																																																																																																																																																																																																PoisonLevel.NORMAL,
																																																																																																																																																																																																																																																																																																																1207,
																																																																																																																																																																																																																																																																																																																1223), STEEL_DAGGER_P_PLUS(
																																																																																																																																																																																																																																																																																																																		PoisonLevel.PLUS,
																																																																																																																																																																																																																																																																																																																		1207,
																																																																																																																																																																																																																																																																																																																		5672), STEEL_DAGGER_P_PLUS_PLUS(
																																																																																																																																																																																																																																																																																																																				PoisonLevel.PLUSPLUS,
																																																																																																																																																																																																																																																																																																																				1207,
																																																																																																																																																																																																																																																																																																																				5690), STEEL_DART_P(
																																																																																																																																																																																																																																																																																																																						PoisonLevel.NORMAL,
																																																																																																																																																																																																																																																																																																																						808,
																																																																																																																																																																																																																																																																																																																						814), STEEL_DART_P_PLUS(
																																																																																																																																																																																																																																																																																																																								PoisonLevel.PLUS,
																																																																																																																																																																																																																																																																																																																								808,
																																																																																																																																																																																																																																																																																																																								5630), STEEL_DART_P_PLUS_PLUS(
																																																																																																																																																																																																																																																																																																																										PoisonLevel.PLUSPLUS,
																																																																																																																																																																																																																																																																																																																										808,
																																																																																																																																																																																																																																																																																																																										5637), STEEL_HASTA_P(
																																																																																																																																																																																																																																																																																																																												PoisonLevel.NORMAL,
																																																																																																																																																																																																																																																																																																																												11371,
																																																																																																																																																																																																																																																																																																																												11393), STEEL_HASTA_P_PLUS(
																																																																																																																																																																																																																																																																																																																														PoisonLevel.PLUS,
																																																																																																																																																																																																																																																																																																																														11371,
																																																																																																																																																																																																																																																																																																																														11396), STEEL_HASTA_P_PLUS_PLUS(
																																																																																																																																																																																																																																																																																																																																PoisonLevel.PLUSPLUS,
																																																																																																																																																																																																																																																																																																																																11371,
																																																																																																																																																																																																																																																																																																																																11398), STEEL_JAVELIN_P(
																																																																																																																																																																																																																																																																																																																																		PoisonLevel.NORMAL,
																																																																																																																																																																																																																																																																																																																																		827,
																																																																																																																																																																																																																																																																																																																																		833), STEEL_JAVELIN_P_PLUS(
																																																																																																																																																																																																																																																																																																																																				PoisonLevel.PLUS,
																																																																																																																																																																																																																																																																																																																																				827,
																																																																																																																																																																																																																																																																																																																																				5644), STEEL_JAVELIN_P_PLUS_PLUS(
																																																																																																																																																																																																																																																																																																																																						PoisonLevel.PLUSPLUS,
																																																																																																																																																																																																																																																																																																																																						827,
																																																																																																																																																																																																																																																																																																																																						5650), STEEL_KNIFE_P(
																																																																																																																																																																																																																																																																																																																																								PoisonLevel.NORMAL,
																																																																																																																																																																																																																																																																																																																																								865,
																																																																																																																																																																																																																																																																																																																																								872), STEEL_KNIFE_P_PLUS(
																																																																																																																																																																																																																																																																																																																																										PoisonLevel.PLUS,
																																																																																																																																																																																																																																																																																																																																										865,
																																																																																																																																																																																																																																																																																																																																										5656), STEEL_KNIFE_P_PLUS_PLUS(
																																																																																																																																																																																																																																																																																																																																												PoisonLevel.PLUSPLUS,
																																																																																																																																																																																																																																																																																																																																												865,
																																																																																																																																																																																																																																																																																																																																												5663), STEEL_SPEAR_P(
																																																																																																																																																																																																																																																																																																																																														PoisonLevel.NORMAL,
																																																																																																																																																																																																																																																																																																																																														1241,
																																																																																																																																																																																																																																																																																																																																														1255), STEEL_SPEAR_P_PLUS(
																																																																																																																																																																																																																																																																																																																																																PoisonLevel.PLUS,
																																																																																																																																																																																																																																																																																																																																																1241,
																																																																																																																																																																																																																																																																																																																																																5708), STEEL_SPEAR_P_PLUS_PLUS(
																																																																																																																																																																																																																																																																																																																																																		PoisonLevel.PLUSPLUS,
																																																																																																																																																																																																																																																																																																																																																		1241,
																																																																																																																																																																																																																																																																																																																																																		5722), WHITE_DAGGER_P(
																																																																																																																																																																																																																																																																																																																																																				PoisonLevel.NORMAL,
																																																																																																																																																																																																																																																																																																																																																				6591,
																																																																																																																																																																																																																																																																																																																																																				6593), WHITE_DAGGER_P_PLUS(
																																																																																																																																																																																																																																																																																																																																																						PoisonLevel.PLUS,
																																																																																																																																																																																																																																																																																																																																																						6591,
																																																																																																																																																																																																																																																																																																																																																						6595), WHITE_DAGGER_P_PLUS_PLUS(
																																																																																																																																																																																																																																																																																																																																																								PoisonLevel.PLUSPLUS,
																																																																																																																																																																																																																																																																																																																																																								6591,
																																																																																																																																																																																																																																																																																																																																																								6597);

	private PoisonLevel level;
	private int weapon;
	private int result;

	/**
	 * @param level The {@link PoisonLevel} of the poisoned weapon.
	 * @param weapon The weapon id of the un-poisoned weapon.
	 * @param result The weapon id of the poisoned weapon.
	 */
	PoisonedWeapon(PoisonLevel level, int weapon, int result) {
		this.level = level;
		this.weapon = weapon;
		this.result = result;
	}

	/**
	 * Unmodifiable Sets of both enums.
	 */
	private static final Set<PoisonedWeapon> WEAPON_VALUES = Collections.unmodifiableSet(EnumSet.allOf(PoisonedWeapon.class));

	private static final Set<PoisonLevel> POISON_VALUES = Collections.unmodifiableSet(EnumSet.allOf(PoisonLevel.class));

	/**
	 * Attempt to find a poisoned weapon which matches given poison and un-poisoned weapon.
	 * 
	 * @param poison The item id of the poison.
	 * @param weapon The item id of the weapon.
	 * @return An {@link Optional} Integer, representing the poisoned weapon id, which is present if there is a match.
	 */
	public static Optional<Integer> getResult(int poison, int weapon) {
		return WEAPON_VALUES.stream().filter(w -> w.level.poisonId == poison && w.weapon == weapon).map(w -> w.result).findFirst();
	}

	/**
	 * Attempt to find the poison level for given poisoned weapon.
	 * 
	 * @param weapon The item id of the poisoned weapon.
	 * @return An {@link Optional} PoisonLevel, representing the poison level for given weapon in case it's present.
	 */
	public static Optional<PoisonLevel> getPoisonLevel(int weapon) {
		return WEAPON_VALUES.stream().filter(w -> w.result == weapon).map(w -> w.level).findFirst();
	}

	/**
	 * Attempt to find a potion out of any number of given item ids.
	 * 
	 * @param poison The id of the item which is to be tested.
	 * @return True in case the given id is a poison, False otherwise.
	 */
	public static Optional<Integer> getPoison(Integer... poison) {
		List<Integer> items = Arrays.asList(poison);
		return POISON_VALUES.stream().filter(p -> items.contains(p.poisonId)).map(p -> p.poisonId).findFirst();
	}

	/**
	 * Attempt to find the un-poisoned counterpart of a poisoned weapon.
	 * 
	 * @param weapon The item id of the poisoned weapon.
	 * @return An {@link Optional} item id of the un-poisoned weapon.
	 */
	public static Optional<Integer> getOriginal(int poisonedWeapon) {
		return WEAPON_VALUES.stream().filter(w -> w.result == poisonedWeapon).map(w -> w.weapon).findFirst();
	}

	/**
	 * Attempt to poison a weapon with 2 given items.
	 * 
	 * @param player The player which will attempt to poison the weapon.
	 * @param item1 A random item, which may or may not be a poison vial or un-poisoned weapon.
	 * @param item2 A random item, which may or may not be a poison vial or un-poisoned weapon.
	 * @return True in case a weapon was poisoned, False otherwise.
	 */
	public static boolean poisonWeapon(Player player, int item1, int item2) {
		Optional<Integer> poison = PoisonedWeapon.getPoison(item1, item2);
		if (!poison.isPresent()) {
			return false;
		}
		int weapon;
		try {
			weapon = getOther(poison.get(), item1, item2);
		} catch (IllegalArgumentException e) {
			// Should be impossible to reach this line, but you can't be too safe.
			return false;
		}
		Optional<Integer> poisonedWeapon = PoisonedWeapon.getResult(poison.get(), weapon);
		if (poisonedWeapon.isPresent()) {
			int amount = 1;
			if (Item.itemStackable[weapon]) {
				if (player.getItems().freeSlots() <= 0 && !player.getItems().playerHasItem(poisonedWeapon.get())) {
					player.sendMessage("You need at least one free inventory space to do this.");
					return false;
				}
				amount = Integer.min(player.getItems().getItemAmount(weapon), 5);
			}
			player.getItems().deleteItem(poison.get(), 1);
			player.getItems().addItem(229, 1);
			player.getItems().deleteItem(weapon, amount);
			player.getItems().addItem(poisonedWeapon.get(), amount);
			player.sendMessage("You poison the " + ItemDefinition.forId(weapon).getName() + ".");
		}
		return false;
	}

	/**
	 * Find the integer which does not match the notThis integer.
	 * 
	 * @param notThis The integer which is not to be returned.
	 * @param i1 Integer which may or may not equal notThis.
	 * @param i2 Integer which may or may not equal notThis.
	 * @return The integer which does
	 * @throws IllegalArgumentException Thrown in case neither of the 2 integers matches the notThis integer.
	 */
	private static int getOther(int notThis, int i1, int i2) throws IllegalArgumentException {
		if (notThis == i1) {
			return i2;
		} else if (notThis == i2) {
			return i1;
		} else {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * An enumeration representing each poison level, each containing the item id of the associated poison.
	 */
	public enum PoisonLevel {
		NORMAL(187, 30, 4), PLUS(5937, 20, 5), PLUSPLUS(5940, 10, 6);

		private final int poisonId;

		private final int poisonProbability;

		private final int poisonDamage;

		/**
		 * @param poisonId The item id of the poison.
		 * @param poisonProbability The probability that an entity will be poisoned by the weapon upon contact
		 * @param poisonDamage The initial damage dealt upon poison
		 */
		PoisonLevel(int poisonId, int poisonProbability, int poisonDamage) {
			this.poisonId = poisonId;
			this.poisonProbability = poisonProbability;
			this.poisonDamage = poisonDamage;
		}

		/**
		 * The probability that the poison will infect the target on contact
		 * 
		 * @return the probability that it will infect
		 */
		public int getPoisonProbability() {
			return poisonProbability;
		}

		/**
		 * Returns the item id of the poison vial.
		 * 
		 * @return item id of the poison vial.
		 */
		public int getPoisonId() {
			return poisonId;
		}

		/**
		 * The initial poison damage dealt
		 * 
		 * @return the damage amount
		 */
		public int getPoisonDamage() {
			return poisonDamage;
		}
	}

}
