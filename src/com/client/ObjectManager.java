package com.client;

import com.client.definitions.FloorOverlayDefinition;
import com.client.definitions.FloorUnderlayDefinition;
import com.client.definitions.ObjectDefinition;

/**
 * Scene Objects
 *
 * @author trees Tile blending features && renaming
 */
final class ObjectManager {

	public ObjectManager(byte abyte0[][][], int ai[][][]) {
		maximumPlane = 99;
		regionSizeX = 104;
		regionSizeY = 104;
		tileHeights = ai;
		tileFlags = abyte0;
		underlays = new byte[4][regionSizeX][regionSizeY];
		overlays = new byte[4][regionSizeX][regionSizeY];
		overlayTypes = new byte[4][regionSizeX][regionSizeY];
		overlayOrientations = new byte[4][regionSizeX][regionSizeY];
		anIntArrayArrayArray135 = new int[4][regionSizeX + 1][regionSizeY + 1];
		shading = new byte[4][regionSizeX + 1][regionSizeY + 1];
		tileLighting = new int[regionSizeX + 1][regionSizeY + 1];
		hues = new int[regionSizeY];
		saturations = new int[regionSizeY];
		luminances = new int[regionSizeY];
		chromas = new int[regionSizeY];
		anIntArray128 = new int[regionSizeY];
	}

	/**
	 * Encodes the hue, saturation, and luminance into a colour value.
	 *
	 * @param hue
	 *            The hue.
	 * @param saturation
	 *            The saturation.
	 * @param luminance
	 *            The luminance.
	 * @return The colour.
	 */
	private int encode(int hue, int saturation, int luminance) {
		if (luminance > 179)
			saturation /= 2;
		if (luminance > 192)
			saturation /= 2;
		if (luminance > 217)
			saturation /= 2;
		if (luminance > 243)
			saturation /= 2;
		return (hue / 4 << 10) + (saturation / 32 << 7) + luminance / 2;
	}

	/**
	 * Returns the plane that actually contains the collision flag, to adjust for
	 * objects such as bridges. TODO better name
	 *
	 * @param x
	 *            The x coordinate.
	 * @param y
	 *            The y coordinate.
	 * @param z
	 *            The z coordinate.
	 * @return The correct z coordinate.
	 */
	private int getCollisionPlane(int y, int z, int x) {
		if ((tileFlags[z][x][y] & FORCE_LOWEST_PLANE) != 0) {
			return 0;
		}
		if (z > 0 && (tileFlags[1][x][y] & BRIDGE_TILE) != 0) {
			return z - 1;
		} else {
			return z;
		}
	}

	private static int calculateNoise(int i, int j) {
		int k = i + j * 57;
		k = k << 13 ^ k;
		int l = k * (k * k * 15731 + 0xc0ae5) + 0x5208dd0d & 0x7fffffff;
		return l >> 19 & 0xff;
	}

	public final void createRegionScene(CollisionMap aclass11[], WorldController worldController) {
		for (int j = 0; j < 4; j++) {
			for (int k = 0; k < 104; k++) {
				for (int i1 = 0; i1 < 104; i1++)
					if ((tileFlags[j][k][i1] & 1) == 1) {
						int k1 = j;
						if ((tileFlags[1][k][i1] & 2) == 2)
							k1--;
						if (k1 >= 0)
							aclass11[k1].method213(i1, k);
					}
			}

		}
		for (int l = 0; l < 4; l++) {
			byte abyte0[][] = shading[l];
			byte byte0 = 96;
			char c = '\u0300';
			byte byte1 = -50;
			byte byte2 = -10;
			byte byte3 = -50;
			int j3 = (int) Math.sqrt(byte1 * byte1 + byte2 * byte2 + byte3 * byte3);
			int l3 = c * j3 >> 8;
			for (int j4 = 1; j4 < regionSizeY - 1; j4++) {
				for (int j5 = 1; j5 < regionSizeX - 1; j5++) {
					int k6 = tileHeights[l][j5 + 1][j4] - tileHeights[l][j5 - 1][j4];
					int l7 = tileHeights[l][j5][j4 + 1] - tileHeights[l][j5][j4 - 1];
					int j9 = (int) Math.sqrt(k6 * k6 + 0x10000 + l7 * l7);
					int k12 = (k6 << 8) / j9;
					int l13 = 0x10000 / j9;
					int j15 = (l7 << 8) / j9;
					int j16 = byte0 + (byte1 * k12 + byte2 * l13 + byte3 * j15) / l3;
					int j17 = (abyte0[j5 - 1][j4] >> 2) + (abyte0[j5 + 1][j4] >> 3) + (abyte0[j5][j4 - 1] >> 2)
							+ (abyte0[j5][j4 + 1] >> 3) + (abyte0[j5][j4] >> 1);
					tileLighting[j5][j4] = j16 - j17;
				}
			}
			int[][] paletteIndices = new int[regionSizeX][regionSizeY];
			for (int z = 0; z < regionSizeY; z++) {
				hues[z] = 0;
				saturations[z] = 0;
				luminances[z] = 0;
				chromas[z] = 0;
				anIntArray128[z] = 0;
			}

			for (int x = -5; x < regionSizeX; x++) {
				for (int z = 0; z < regionSizeY; z++) {
					int xForwardOffset = x + 5;
					if (xForwardOffset < regionSizeX) {
						int underlayId = underlays[l][xForwardOffset][z] & 0xff;
						if (underlayId > 0) {
							FloorUnderlayDefinition flo = FloorUnderlayDefinition.underlays[underlayId - 1];
							hues[z] += flo.blendHue;
							saturations[z] += flo.saturation;
							luminances[z] += flo.luminance;
							chromas[z] += flo.blendHueMultiplier;
							anIntArray128[z]++;
						}
					}
					int xBackwardOffset = x - 5;
					if (xBackwardOffset >= 0) {
						int underlayId = underlays[l][xBackwardOffset][z] & 0xff;
						if (underlayId > 0) {
							FloorUnderlayDefinition flo_1 = FloorUnderlayDefinition.underlays[underlayId - 1];
							hues[z] -= flo_1.blendHue;
							saturations[z] -= flo_1.saturation;
							luminances[z] -= flo_1.luminance;
							chromas[z] -= flo_1.blendHueMultiplier;
							anIntArray128[z]--;
						}
					}
				}
				if (x >= 0) {
					int hueSum = 0;
					int saturationSum = 0;
					int lightnessSum = 0;
					int dividerSum = 0;
					int sizeSum = 0;
					for (int z = -5; z < regionSizeY; z++) {
						int zForwardOffset = z + 5;
						if (zForwardOffset < regionSizeY) {
							hueSum += hues[zForwardOffset];
							saturationSum += saturations[zForwardOffset];
							lightnessSum += luminances[zForwardOffset];
							dividerSum += chromas[zForwardOffset];
							sizeSum += anIntArray128[zForwardOffset];
						}
						int zBackwardOffset = z - 5;
						if (zBackwardOffset >= 0) {
							hueSum -= hues[zBackwardOffset];
							saturationSum -= saturations[zBackwardOffset];
							lightnessSum -= luminances[zBackwardOffset];
							dividerSum -= chromas[zBackwardOffset];
							sizeSum -= anIntArray128[zBackwardOffset];
						}
						if (z >= 0 && dividerSum > 0 && sizeSum > 0) {
							int hue = (hueSum * 256) / dividerSum;
							int sat = saturationSum / sizeSum;
							int light = lightnessSum / sizeSum;
							paletteIndices[x][z] = FloorOverlayDefinition.hsl24to16(hue, sat, light);
						}
					}
				}
			}
			for (int x = 0; x < regionSizeX; x++) {
				int nextX = x >= regionSizeX - 1 ? x : x + 1;
				for (int z = 0; z < regionSizeY; z++) {
					int nextZ = z >= regionSizeY - 1 ? z : z + 1;
					if ((!lowMem || (tileFlags[0][x][z] & 2) != 0
							|| (tileFlags[l][x][z] & 0x10) == 0 && getCollisionPlane(z, l, x) == anInt131)) {
						if (l < maximumPlane) {
							maximumPlane = l;
						}
						int underlayA = underlays[l][x][z] & 0xff;
						int underlayB = underlays[l][nextX][z] & 0xff;
						int underlayC = underlays[l][nextX][nextZ] & 0xff;
						int underlayD = underlays[l][x][nextZ] & 0xff;

						int overlayA = overlays[l][x][z] & 0xff;
						if (underlayA > 0 || overlayA > 0) {
							int tileHeightA = tileHeights[l][x][z];
							int tileHeightB = tileHeights[l][x + 1][z];
							int tileHeightC = tileHeights[l][x + 1][z + 1];
							int tileHeightD = tileHeights[l][x][z + 1];

							int tileShadowA = tileLighting[x][z];
							int tileShadowB = tileLighting[x + 1][z];
							int tileShadowC = tileLighting[x + 1][z + 1];
							int tileShadowD = tileLighting[x][z + 1];

							int paletteIndexA = -1;
							int paletteIndexB = -1;
							int paletteIndexC = -1;
							int paletteIndexD = -1;
							if (underlayA > 0) {
								paletteIndexA = paletteIndices[x][z];
								if (underlayB > 0) {
									paletteIndexB = paletteIndices[nextX][z];
								}
								if (underlayC > 0) {
									paletteIndexC = paletteIndices[nextX][nextZ];
								}
								if (underlayD > 0) {
									paletteIndexD = paletteIndices[x][nextZ];
								}

								if (paletteIndexB == -1) {
									paletteIndexB = paletteIndexA;
								}

								if (paletteIndexC == -1) {
									paletteIndexC = paletteIndexA;
								}

								if (paletteIndexD == -1) {
									paletteIndexD = paletteIndexA;
								}
							}
							if (l > 0) {
								boolean occlude = true;
								if (underlayA == 0 && overlayTypes[l][x][z] != 0) {
									occlude = false;
								}

								if (overlayA > 0 && !FloorOverlayDefinition.overlays[overlayA - 1].occlude) {
									occlude = false;
								}

								if (occlude && tileHeightA == tileHeightB && tileHeightA == tileHeightC
										&& tileHeightA == tileHeightD) {
									anIntArrayArrayArray135[l][x][z] |= 0x924;
								}
							}
							int minimapRgb = 0;
							boolean check = !Configuration.enableTileBlending || !Configuration.enableSmoothShading;
							if (paletteIndexA != -1) {
								minimapRgb = Rasterizer.hslToRgb[method187(paletteIndexA, 96)];
							}
							if (overlayA == 0) {
								worldController.addTile(l, x, z, 0, 0, -1, tileHeightA, tileHeightB, tileHeightC,
										tileHeightD, method187(paletteIndexA, tileShadowA),
										method187(check ? paletteIndexA : paletteIndexB, tileShadowB),
										method187(check ? paletteIndexA : paletteIndexC, tileShadowC),
										method187(check ? paletteIndexA : paletteIndexD, tileShadowD), 0, 0, 0, 0,
										minimapRgb, 0);
							} else {
								int shape = overlayTypes[l][x][z] + 1;
								byte angle = overlayOrientations[l][x][z];
								if (overlayA - 1 > FloorOverlayDefinition.overlays.length) {
									overlayA = FloorOverlayDefinition.overlays.length;
								}
								FloorOverlayDefinition def_over = FloorOverlayDefinition.overlays[overlayA - 1];
								int textureId = def_over.texture;
								int floorId;
								int minimapColor;
								if (textureId == 51) {
									textureId = 3;
								}
								if (textureId >= 0) {
									minimapColor = Rasterizer.getOverallColour(textureId);
									floorId = -1;
								} else if (def_over.rgb == 0xff00ff) {
									minimapColor = 0;
									floorId = -2;
									textureId = -1;
								} else if (def_over.rgb == 0x333333) {
									minimapColor = Rasterizer.hslToRgb[checkedLight(def_over.hsl16, 96)];
									floorId = -2;
									textureId = -1;
								} else {
									floorId = encode(def_over.hue, def_over.saturation, def_over.luminance);
									minimapColor = Rasterizer.hslToRgb[checkedLight(def_over.hsl16, 96)];
								}
								if (minimapColor == 0x000000 && def_over.anotherRgb != -1) {
									int newMinimapColor = encode(def_over.hue, def_over.saturation, def_over.luminance);
									minimapColor = Rasterizer.hslToRgb[checkedLight(newMinimapColor, 96)];
								}
								worldController.addTile(l, x, z, shape, angle, textureId, tileHeightA, tileHeightB,
										tileHeightC, tileHeightD, method187(paletteIndexA, tileShadowA),
										method187(paletteIndexA, tileShadowB), method187(paletteIndexA, tileShadowC),
										method187(paletteIndexA, tileShadowD), checkedLight(floorId, tileShadowA),
										checkedLight(floorId, tileShadowB), checkedLight(floorId, tileShadowC),
										checkedLight(floorId, tileShadowD), minimapRgb, minimapColor);
							}
						}
					}
				}
			}

			for (int j8 = 1; j8 < regionSizeY - 1; j8++) {
				for (int i10 = 1; i10 < regionSizeX - 1; i10++)
					worldController.method278(l, i10, j8, method182(j8, l, i10));
			}
		}
		worldController.method305(-10, -50, -50);
		for (int j1 = 0; j1 < regionSizeX; j1++) {
			for (int l1 = 0; l1 < regionSizeY; l1++)
				if ((tileFlags[1][j1][l1] & 2) == 2)
					worldController.method276(l1, j1);
		}
		int i2 = 1;
		int j2 = 2;
		int k2 = 4;
		for (int l2 = 0; l2 < 4; l2++) {
			if (l2 > 0) {
				i2 <<= 3;
				j2 <<= 3;
				k2 <<= 3;
			}
			for (int i3 = 0; i3 <= l2; i3++) {
				for (int k3 = 0; k3 <= regionSizeY; k3++) {
					for (int i4 = 0; i4 <= regionSizeX; i4++) {
						if ((anIntArrayArrayArray135[i3][i4][k3] & i2) != 0) {
							int k4 = k3;
							int l5 = k3;
							int i7 = i3;
							int k8 = i3;
							for (; k4 > 0 && (anIntArrayArrayArray135[i3][i4][k4 - 1] & i2) != 0; k4--)
								;
							for (; l5 < regionSizeY && (anIntArrayArrayArray135[i3][i4][l5 + 1] & i2) != 0; l5++)
								;
							label0: for (; i7 > 0; i7--) {
								for (int j10 = k4; j10 <= l5; j10++)
									if ((anIntArrayArrayArray135[i7 - 1][i4][j10] & i2) == 0)
										break label0;
							}
							label1: for (; k8 < l2; k8++) {
								for (int k10 = k4; k10 <= l5; k10++)
									if ((anIntArrayArrayArray135[k8 + 1][i4][k10] & i2) == 0)
										break label1;
							}
							int l10 = ((k8 + 1) - i7) * ((l5 - k4) + 1);
							if (l10 >= 8) {
								char c1 = '\360';
								int k14 = tileHeights[k8][i4][k4] - c1;
								int l15 = tileHeights[i7][i4][k4];
								WorldController.createNewSceneCluster(l2, i4 * 128, l15, i4 * 128, l5 * 128 + 128, k14,
										k4 * 128, 1);
								for (int l16 = i7; l16 <= k8; l16++) {
									for (int l17 = k4; l17 <= l5; l17++)
										anIntArrayArrayArray135[l16][i4][l17] &= ~i2;
								}
							}
						}
						if ((anIntArrayArrayArray135[i3][i4][k3] & j2) != 0) {
							int l4 = i4;
							int i6 = i4;
							int j7 = i3;
							int l8 = i3;
							for (; l4 > 0 && (anIntArrayArrayArray135[i3][l4 - 1][k3] & j2) != 0; l4--)
								;
							for (; i6 < regionSizeX && (anIntArrayArrayArray135[i3][i6 + 1][k3] & j2) != 0; i6++)
								;
							label2: for (; j7 > 0; j7--) {
								for (int i11 = l4; i11 <= i6; i11++)
									if ((anIntArrayArrayArray135[j7 - 1][i11][k3] & j2) == 0)
										break label2;
							}
							label3: for (; l8 < l2; l8++) {
								for (int j11 = l4; j11 <= i6; j11++)
									if ((anIntArrayArrayArray135[l8 + 1][j11][k3] & j2) == 0)
										break label3;
							}
							int k11 = ((l8 + 1) - j7) * ((i6 - l4) + 1);
							if (k11 >= 8) {
								char c2 = '\360';
								int l14 = tileHeights[l8][l4][k3] - c2;
								int i16 = tileHeights[j7][l4][k3];
								WorldController.createNewSceneCluster(l2, l4 * 128, i16, i6 * 128 + 128, k3 * 128, l14,
										k3 * 128, 2);
								for (int i17 = j7; i17 <= l8; i17++) {
									for (int i18 = l4; i18 <= i6; i18++)
										anIntArrayArrayArray135[i17][i18][k3] &= ~j2;
								}
							}
						}
						if ((anIntArrayArrayArray135[i3][i4][k3] & k2) != 0) {
							int i5 = i4;
							int j6 = i4;
							int k7 = k3;
							int i9 = k3;
							for (; k7 > 0 && (anIntArrayArrayArray135[i3][i4][k7 - 1] & k2) != 0; k7--)
								;
							for (; i9 < regionSizeY && (anIntArrayArrayArray135[i3][i4][i9 + 1] & k2) != 0; i9++)
								;
							label4: for (; i5 > 0; i5--) {
								for (int l11 = k7; l11 <= i9; l11++)
									if ((anIntArrayArrayArray135[i3][i5 - 1][l11] & k2) == 0)
										break label4;
							}
							label5: for (; j6 < regionSizeX; j6++) {
								for (int i12 = k7; i12 <= i9; i12++)
									if ((anIntArrayArrayArray135[i3][j6 + 1][i12] & k2) == 0)
										break label5;
							}
							if (((j6 - i5) + 1) * ((i9 - k7) + 1) >= 4) {
								int j12 = tileHeights[i3][i5][k7];
								WorldController.createNewSceneCluster(l2, i5 * 128, j12, j6 * 128 + 128, i9 * 128 + 128,
										j12, k7 * 128, 4);
								for (int k13 = i5; k13 <= j6; k13++) {
									for (int i15 = k7; i15 <= i9; i15++)
										anIntArrayArrayArray135[i3][k13][i15] &= ~k2;
								}
							}
						}
					}
				}
			}
		}
	}

	private int checkedLight(int color, int light) {
		if (color == -2)
			return 0xbc614e;
		if (color == -1) {
			if (light < 0)
				light = 0;
			else if (light > 127)
				light = 127;
			light = 127 - light;
			return light;
		}
		light = (light * (color & 0x7f)) / 128;
		if (light < 2)
			light = 2;
		else if (light > 126)
			light = 126;
		return (color & 0xff80) + light;
	}

	private static int calculateVertexHeight(int i, int j) {
		int mapHeight = (interpolatedNoise(i + 45365, j + 0x16713, 4) - 128)
				+ (interpolatedNoise(i + 10294, j + 37821, 2) - 128 >> 1) + (interpolatedNoise(i, j, 1) - 128 >> 2);
		mapHeight = (int) (mapHeight * 0.29999999999999999D) + 35;
		if (mapHeight < 10)
			mapHeight = 10;
		else if (mapHeight > 60)
			mapHeight = 60;
		return mapHeight;
	}

	public static void passiveRequestGameObjectModels(Stream stream, OnDemandFetcher class42_sub1) {
		label0: {
			int i = -1;
			do {
				int j = stream.readUSmart2();
				if (j == 0)
					break label0;
				i += j;
				ObjectDefinition class46 = ObjectDefinition.forID(i);
				class46.method574(class42_sub1);
				do {
					int k = stream.method422();
					if (k == 0)
						break;
					stream.readUnsignedByte();
				} while (true);
			} while (true);
		}
	}

	public final void initiateVertexHeights(int i, int j, int l, int i1) {
		for (int j1 = i; j1 <= i + j; j1++) {
			for (int k1 = i1; k1 <= i1 + l; k1++)
				if (k1 >= 0 && k1 < regionSizeX && j1 >= 0 && j1 < regionSizeY) {
					shading[0][k1][j1] = 127;
					if (k1 == i1 && k1 > 0)
						tileHeights[0][k1][j1] = tileHeights[0][k1 - 1][j1];
					if (k1 == i1 + l && k1 < regionSizeX - 1)
						tileHeights[0][k1][j1] = tileHeights[0][k1 + 1][j1];
					if (j1 == i && j1 > 0)
						tileHeights[0][k1][j1] = tileHeights[0][k1][j1 - 1];
					if (j1 == i + j && j1 < regionSizeY - 1)
						tileHeights[0][k1][j1] = tileHeights[0][k1][j1 + 1];
				}

		}
	}

	private void renderObject(int y, WorldController worldController, CollisionMap class11, int type, int z, int x,
			int id, int j1) {
		if (lowMem && (tileFlags[0][x][y] & 2) == 0) {
			if ((tileFlags[z][x][y] & 0x10) != 0)
				return;
			if (method182(y, z, x) != anInt131)
				return;
		}
		if (z < maximumPlane)
			maximumPlane = z;
		int k1 = tileHeights[z][x][y];
		int l1 = tileHeights[z][x + 1][y];
		int i2 = tileHeights[z][x + 1][y + 1];
		int j2 = tileHeights[z][x][y + 1];
		int k2 = k1 + l1 + i2 + j2 >> 2;
		ObjectDefinition definition = ObjectDefinition.forID(id);
		int key = x + (y << 7) + (id << 14) + 0x40000000;
		if (!definition.hasActions)
			key += 0x80000000;
		byte byte0 = (byte) ((j1 << 6) + type);
		if (type == 22) {
			if (lowMem && !definition.hasActions && !definition.aBoolean736)
				return;
			Object obj;
			if (definition.animation == -1 && definition.childrenIDs == null)
				obj = definition.modelAt(22, j1, k1, l1, i2, j2, -1);
			else
				obj = new Animable_Sub5(id, j1, 22, l1, i2, k1, j2, definition.animation, true);
			worldController.method280(z, k2, y, ((Renderable) (obj)), byte0, key, x);
			if (definition.aBoolean767 && definition.hasActions && class11 != null)
				class11.method213(y, x);
			return;
		}
		if (type == 10 || type == 11) {
			Object obj1;
			if (definition.animation == -1 && definition.childrenIDs == null)
				obj1 = definition.modelAt(10, j1, k1, l1, i2, j2, -1);
			else
				obj1 = new Animable_Sub5(id, j1, 10, l1, i2, k1, j2, definition.animation, true);
			if (obj1 != null) {
				int i5 = 0;
				if (type == 11)
					i5 += 256;
				int j4;
				int l4;
				if (j1 == 1 || j1 == 3) {
					j4 = definition.anInt761;
					l4 = definition.anInt744;
				} else {
					j4 = definition.anInt744;
					l4 = definition.anInt761;
				}
				if (worldController.method284(key, byte0, k2, l4, ((Renderable) (obj1)), j4, z, i5, y, x)
						&& definition.aBoolean779) {
					Model model;
					if (obj1 instanceof Model)
						model = (Model) obj1;
					else
						model = definition.modelAt(10, j1, k1, l1, i2, j2, -1);
					if (model != null) {
						for (int j5 = 0; j5 <= j4; j5++) {
							for (int k5 = 0; k5 <= l4; k5++) {
								int l5 = model.maxVertexDistanceXZPlane / 4;
								if (l5 > 30)
									l5 = 30;
								if (l5 > shading[z][x + j5][y + k5])
									shading[z][x + j5][y + k5] = (byte) l5;
							}

						}

					}
				}
			}
			if (definition.aBoolean767 && class11 != null)
				class11.method212(definition.aBoolean757, definition.anInt744, definition.anInt761, x, y, j1);
			return;
		}
		if (type >= 12) {
			Object obj2;
			if (definition.animation == -1 && definition.childrenIDs == null)
				obj2 = definition.modelAt(type, j1, k1, l1, i2, j2, -1);
			else
				obj2 = new Animable_Sub5(id, j1, type, l1, i2, k1, j2, definition.animation, true);
			worldController.method284(key, byte0, k2, 1, ((Renderable) (obj2)), 1, z, 0, y, x);
			if (type >= 12 && type <= 17 && type != 13 && z > 0)
				anIntArrayArrayArray135[z][x][y] |= 0x924;
			if (definition.aBoolean767 && class11 != null)
				class11.method212(definition.aBoolean757, definition.anInt744, definition.anInt761, x, y, j1);
			return;
		}
		if (type == 0) {
			Object obj3;
			if (definition.animation == -1 && definition.childrenIDs == null)
				obj3 = definition.modelAt(0, j1, k1, l1, i2, j2, -1);
			else
				obj3 = new Animable_Sub5(id, j1, 0, l1, i2, k1, j2, definition.animation, true);
			worldController.method282(anIntArray152[j1], ((Renderable) (obj3)), key, y, byte0, x, null, k2, 0, z);
			if (j1 == 0) {
				if (definition.aBoolean779) {
					shading[z][x][y] = 50;
					shading[z][x][y + 1] = 50;
				}
				if (definition.aBoolean764)
					anIntArrayArrayArray135[z][x][y] |= 0x249;
			} else if (j1 == 1) {
				if (definition.aBoolean779) {
					shading[z][x][y + 1] = 50;
					shading[z][x + 1][y + 1] = 50;
				}
				if (definition.aBoolean764)
					anIntArrayArrayArray135[z][x][y + 1] |= 0x492;
			} else if (j1 == 2) {
				if (definition.aBoolean779) {
					shading[z][x + 1][y] = 50;
					shading[z][x + 1][y + 1] = 50;
				}
				if (definition.aBoolean764)
					anIntArrayArrayArray135[z][x + 1][y] |= 0x249;
			} else if (j1 == 3) {
				if (definition.aBoolean779) {
					shading[z][x][y] = 50;
					shading[z][x + 1][y] = 50;
				}
				if (definition.aBoolean764)
					anIntArrayArrayArray135[z][x][y] |= 0x492;
			}
			if (definition.aBoolean767 && class11 != null)
				class11.method211(y, j1, x, type, definition.aBoolean757);
			if (definition.anInt775 != 16)
				worldController.method290(y, definition.anInt775, x, z);
			return;
		}
		if (type == 1) {
			Object obj4;
			if (definition.animation == -1 && definition.childrenIDs == null)
				obj4 = definition.modelAt(1, j1, k1, l1, i2, j2, -1);
			else
				obj4 = new Animable_Sub5(id, j1, 1, l1, i2, k1, j2, definition.animation, true);
			worldController.method282(anIntArray140[j1], ((Renderable) (obj4)), key, y, byte0, x, null, k2, 0, z);
			if (definition.aBoolean779)
				if (j1 == 0)
					shading[z][x][y + 1] = 50;
				else if (j1 == 1)
					shading[z][x + 1][y + 1] = 50;
				else if (j1 == 2)
					shading[z][x + 1][y] = 50;
				else if (j1 == 3)
					shading[z][x][y] = 50;
			if (definition.aBoolean767 && class11 != null)
				class11.method211(y, j1, x, type, definition.aBoolean757);
			return;
		}
		if (type == 2) {
			int i3 = j1 + 1 & 3;
			Object obj11;
			Object obj12;
			if (definition.animation == -1 && definition.childrenIDs == null) {
				obj11 = definition.modelAt(2, 4 + j1, k1, l1, i2, j2, -1);
				obj12 = definition.modelAt(2, i3, k1, l1, i2, j2, -1);
			} else {
				obj11 = new Animable_Sub5(id, 4 + j1, 2, l1, i2, k1, j2, definition.animation, true);
				obj12 = new Animable_Sub5(id, i3, 2, l1, i2, k1, j2, definition.animation, true);
			}
			worldController.method282(anIntArray152[j1], ((Renderable) (obj11)), key, y, byte0, x,
					((Renderable) (obj12)), k2, anIntArray152[i3], z);
			if (definition.aBoolean764)
				if (j1 == 0) {
					anIntArrayArrayArray135[z][x][y] |= 0x249;
					anIntArrayArrayArray135[z][x][y + 1] |= 0x492;
				} else if (j1 == 1) {
					anIntArrayArrayArray135[z][x][y + 1] |= 0x492;
					anIntArrayArrayArray135[z][x + 1][y] |= 0x249;
				} else if (j1 == 2) {
					anIntArrayArrayArray135[z][x + 1][y] |= 0x249;
					anIntArrayArrayArray135[z][x][y] |= 0x492;
				} else if (j1 == 3) {
					anIntArrayArrayArray135[z][x][y] |= 0x492;
					anIntArrayArrayArray135[z][x][y] |= 0x249;
				}
			if (definition.aBoolean767 && class11 != null)
				class11.method211(y, j1, x, type, definition.aBoolean757);
			if (definition.anInt775 != 16)
				worldController.method290(y, definition.anInt775, x, z);
			return;
		}
		if (type == 3) {
			Object obj5;
			if (definition.animation == -1 && definition.childrenIDs == null)
				obj5 = definition.modelAt(3, j1, k1, l1, i2, j2, -1);
			else
				obj5 = new Animable_Sub5(id, j1, 3, l1, i2, k1, j2, definition.animation, true);
			worldController.method282(anIntArray140[j1], ((Renderable) (obj5)), key, y, byte0, x, null, k2, 0, z);
			if (definition.aBoolean779)
				if (j1 == 0)
					shading[z][x][y + 1] = 50;
				else if (j1 == 1)
					shading[z][x + 1][y + 1] = 50;
				else if (j1 == 2)
					shading[z][x + 1][y] = 50;
				else if (j1 == 3)
					shading[z][x][y] = 50;
			if (definition.aBoolean767 && class11 != null)
				class11.method211(y, j1, x, type, definition.aBoolean757);
			return;
		}
		if (type == 9) {
			Object obj6;
			if (definition.animation == -1 && definition.childrenIDs == null)
				obj6 = definition.modelAt(type, j1, k1, l1, i2, j2, -1);
			else
				obj6 = new Animable_Sub5(id, j1, type, l1, i2, k1, j2, definition.animation, true);
			worldController.method284(key, byte0, k2, 1, ((Renderable) (obj6)), 1, z, 0, y, x);
			if (definition.aBoolean767 && class11 != null)
				class11.method212(definition.aBoolean757, definition.anInt744, definition.anInt761, x, y, j1);
			return;
		}
		if (definition.aBoolean762)
			if (j1 == 1) {
				int j3 = j2;
				j2 = i2;
				i2 = l1;
				l1 = k1;
				k1 = j3;
			} else if (j1 == 2) {
				int k3 = j2;
				j2 = l1;
				l1 = k3;
				k3 = i2;
				i2 = k1;
				k1 = k3;
			} else if (j1 == 3) {
				int l3 = j2;
				j2 = k1;
				k1 = l1;
				l1 = i2;
				i2 = l3;
			}
		if (type == 4) {
			Object obj7;
			if (definition.animation == -1 && definition.childrenIDs == null)
				obj7 = definition.modelAt(4, 0, k1, l1, i2, j2, -1);
			else
				obj7 = new Animable_Sub5(id, 0, 4, l1, i2, k1, j2, definition.animation, true);
			worldController.method283(key, y, j1 * 512, z, 0, k2, ((Renderable) (obj7)), x, byte0, 0,
					anIntArray152[j1]);
			return;
		}
		if (type == 5) {
			int i4 = 16;
			int k4 = worldController.method300(z, x, y);
			if (k4 > 0)
				i4 = ObjectDefinition.forID(k4 >> 14 & 0x7fff).anInt775;
			Object obj13;
			if (definition.animation == -1 && definition.childrenIDs == null)
				obj13 = definition.modelAt(4, 0, k1, l1, i2, j2, -1);
			else
				obj13 = new Animable_Sub5(id, 0, 4, l1, i2, k1, j2, definition.animation, true);
			worldController.method283(key, y, j1 * 512, z, anIntArray137[j1] * i4, k2, ((Renderable) (obj13)), x, byte0,
					anIntArray144[j1] * i4, anIntArray152[j1]);
			return;
		}
		if (type == 6) {
			Object obj8;
			if (definition.animation == -1 && definition.childrenIDs == null)
				obj8 = definition.modelAt(4, 0, k1, l1, i2, j2, -1);
			else
				obj8 = new Animable_Sub5(id, 0, 4, l1, i2, k1, j2, definition.animation, true);
			worldController.method283(key, y, j1, z, 0, k2, ((Renderable) (obj8)), x, byte0, 0, 256);
			return;
		}
		if (type == 7) {
			Object obj9;
			if (definition.animation == -1 && definition.childrenIDs == null)
				obj9 = definition.modelAt(4, 0, k1, l1, i2, j2, -1);
			else
				obj9 = new Animable_Sub5(id, 0, 4, l1, i2, k1, j2, definition.animation, true);
			worldController.method283(key, y, j1, z, 0, k2, ((Renderable) (obj9)), x, byte0, 0, 512);
			return;
		}
		if (type == 8) {
			Object obj10;
			if (definition.animation == -1 && definition.childrenIDs == null)
				obj10 = definition.modelAt(4, 0, k1, l1, i2, j2, -1);
			else
				obj10 = new Animable_Sub5(id, 0, 4, l1, i2, k1, j2, definition.animation, true);
			worldController.method283(key, y, j1, z, 0, k2, ((Renderable) (obj10)), x, byte0, 0, 768);
		}
	}

	private static int interpolatedNoise(int i, int j, int k) {
		int l = i / k;
		int i1 = i & k - 1;
		int j1 = j / k;
		int k1 = j & k - 1;
		int l1 = method186(l, j1);
		int i2 = method186(l + 1, j1);
		int j2 = method186(l, j1 + 1);
		int k2 = method186(l + 1, j1 + 1);
		int l2 = method184(l1, i2, i1, k);
		int i3 = method184(j2, k2, i1, k);
		return method184(l2, i3, k1, k);
	}

	public static boolean method178(int i, int j) {
		ObjectDefinition class46 = ObjectDefinition.forID(i);
		if (j == 11)
			j = 10;
		if (j >= 5 && j <= 8)
			j = 4;
		return class46.method577(j);
	}

	public final void method179(int i, int j, CollisionMap aclass11[], int l, int i1, byte abyte0[], int j1, int k1,
			int l1) {
		for (int i2 = 0; i2 < 8; i2++) {
			for (int j2 = 0; j2 < 8; j2++)
				if (l + i2 > 0 && l + i2 < 103 && l1 + j2 > 0 && l1 + j2 < 103)
					aclass11[k1].anIntArrayArray294[l + i2][l1 + j2] &= 0xfeffffff;

		}
		Stream stream = new Stream(abyte0);
		for (int l2 = 0; l2 < 4; l2++) {
			for (int i3 = 0; i3 < 64; i3++) {
				for (int j3 = 0; j3 < 64; j3++)
					if (l2 == i && i3 >= i1 && i3 < i1 + 8 && j3 >= j1 && j3 < j1 + 8)
						method181(l1 + Class4.method156(j3 & 7, j, i3 & 7), 0, stream,
								l + Class4.method155(j, j3 & 7, i3 & 7), k1, j, 0);
					else
						method181(-1, 0, stream, -1, 0, 0, 0);

			}

		}

	}

	public final void method180(byte abyte0[], int i, int j, int k, int l, CollisionMap aclass11[]) {
		for (int i1 = 0; i1 < 4; i1++) {
			for (int j1 = 0; j1 < 64; j1++) {
				for (int k1 = 0; k1 < 64; k1++)
					if (j + j1 > 0 && j + j1 < 103 && i + k1 > 0 && i + k1 < 103)
						aclass11[i1].anIntArrayArray294[j + j1][i + k1] &= 0xfeffffff;

			}

		}

		Stream stream = new Stream(abyte0);
		for (int l1 = 0; l1 < 4; l1++) {
			for (int i2 = 0; i2 < 64; i2++) {
				for (int j2 = 0; j2 < 64; j2++)
					method181(j2 + i, l, stream, i2 + j, l1, 0, k);

			}

		}
	}

	private void method181(int i, int j, Stream stream, int k, int l, int i1, int k1) {
		try {
			if (k >= 0 && k < 104 && i >= 0 && i < 104) {
				int absX = (k1 + k);
				int absY = (j + i);
				int absZ = (i1 + l);
				tileFlags[l][k][i] = 0;
				do {
					int l1 = stream.readUnsignedByte();
					if (l1 == 0)
						if (l == 0) {
							tileHeights[0][k][i] = -calculateVertexHeight(0xe3b7b + k + k1, 0x87cce + i + j) * 8;
							return;
						} else {
							tileHeights[l][k][i] = tileHeights[l - 1][k][i] - 240;
							return;
						}
					if (l1 == 1) {
						int j2 = stream.readUnsignedByte();
						if (j2 == 1)
							j2 = 0;
						if (l == 0) {
							tileHeights[0][k][i] = -j2 * 8;
							return;
						} else {
							tileHeights[l][k][i] = tileHeights[l - 1][k][i] - j2 * 8;
							return;
						}
					}
					/**
					 * Halloween floor at edgeville
					 */
					if (absX >= 2576 && absX <= 2622 && absY >= 4749 && absY <= 4798 && absZ == 0
							&& Configuration.HALLOWEEN) {
						overlays[l][k][i] = 108; // 108
						// Orange/brown
						overlayTypes[l][k][i] = 0; // tile shape
					}
					if (absX >= 3083 && absX <= 3104 && absY >= 3483 && absY <= 3504 && absZ == 0
							&& Configuration.HALLOWEEN) {
						overlays[l][k][i] = 108; // 108
						// Orange/brown
						overlayTypes[l][k][i] = 0; // tile shape
					}
					/**
					 * Legendary donator zone
					 */
					if (absX >= 3367 && absX <= 3370 && absY >= 9637 && absY <= 9643 && absZ == 1) {
						overlays[l][k][i] = 100; // 103 - 16 =
						// Orange/brown
						overlayTypes[l][k][i] = 0; // tile shape
					}
					/**
					 * Free for all 1v1 to multi boundary
					 */
					if (absX >= 3340 && absX <= 3340 && absY >= 4760 && absY <= 4851 && absZ == 0) {
						overlays[l][k][i] = 44; // 44 = Gray
						overlayTypes[l][k][i] = 0; // tile shape
					}
					if (l1 <= 49) {
						overlays[l][k][i] = stream.readSignedByte();
						overlayTypes[l][k][i] = (byte) ((l1 - 2) / 4);
						overlayOrientations[l][k][i] = (byte) ((l1 - 2) + i1 & 3);
					} else if (l1 <= 81)
						tileFlags[l][k][i] = (byte) (l1 - 49);
					else
						underlays[l][k][i] = (byte) (l1 - 81);
				} while (true);
			}
			do {
				int i2 = stream.readUnsignedByte();
				if (i2 == 0)
					break;
				if (i2 == 1) {
					stream.readUnsignedByte();
					return;
				}
				if (i2 <= 49)
					stream.readUnsignedByte();
			} while (true);
		} catch (Exception e) {
		}
	}

	private int method182(int i, int j, int k) {
		if ((tileFlags[j][k][i] & 8) != 0)
			return 0;
		if (j > 0 && (tileFlags[1][k][i] & 2) != 0)
			return j - 1;
		else
			return j;
	}

	public final void method183(CollisionMap aclass11[], WorldController worldController, int i, int j, int k, int l,
			byte abyte0[], int i1, int j1, int k1) {
		label0: {
			Stream stream = new Stream(abyte0);
			int l1 = -1;
			do {
				int i2 = stream.readUSmart2();
				if (i2 == 0)
					break label0;
				l1 += i2;
				int j2 = 0;
				do {
					int k2 = stream.method422();
					if (k2 == 0)
						break;
					j2 += k2 - 1;
					int l2 = j2 & 0x3f;
					int i3 = j2 >> 6 & 0x3f;
					int j3 = j2 >> 12;
					int k3 = stream.readUnsignedByte();
					int l3 = k3 >> 2;
					int i4 = k3 & 3;
					if (j3 == i && i3 >= i1 && i3 < i1 + 8 && l2 >= k && l2 < k + 8) {
						ObjectDefinition class46 = ObjectDefinition.forID(l1);
						int j4 = j + Class4.method157(j1, class46.anInt761, i3 & 7, l2 & 7, class46.anInt744);
						int k4 = k1 + Class4.method158(l2 & 7, class46.anInt761, j1, class46.anInt744, i3 & 7);
						if (j4 > 0 && k4 > 0 && j4 < 103 && k4 < 103) {
							int l4 = j3;
							if ((tileFlags[1][j4][k4] & 2) == 2)
								l4--;
							CollisionMap class11 = null;
							if (l4 >= 0)
								class11 = aclass11[l4];
							renderObject(k4, worldController, class11, l3, l, j4, l1, i4 + j1 & 3);
						}
					}
				} while (true);
			} while (true);
		}
	}

	private static int method184(int i, int j, int k, int l) {
		int i1 = 0x10000 - Rasterizer.anIntArray1471[(k * 1024) / l] >> 1;
		return (i * (0x10000 - i1) >> 16) + (j * i1 >> 16);
	}

	private static int method186(int i, int j) {
		int k = calculateNoise(i - 1, j - 1) + calculateNoise(i + 1, j - 1) + calculateNoise(i - 1, j + 1)
				+ calculateNoise(i + 1, j + 1);
		int l = calculateNoise(i - 1, j) + calculateNoise(i + 1, j) + calculateNoise(i, j - 1)
				+ calculateNoise(i, j + 1);
		int i1 = calculateNoise(i, j);
		return k / 16 + l / 8 + i1 / 4;
	}

	private static int method187(int i, int j) {
		if (i == -1)
			return 0xbc614e;
		j = (j * (i & 0x7f)) / 128;
		if (j < 2)
			j = 2;
		else if (j > 126)
			j = 126;
		return (i & 0xff80) + j;
	}

	public static void method188(WorldController worldController, int i, int j, int k, int l, CollisionMap class11,
			int ai[][][], int i1, int j1, int k1) {
		int l1 = ai[l][i1][j];
		int i2 = ai[l][i1 + 1][j];
		int j2 = ai[l][i1 + 1][j + 1];
		int k2 = ai[l][i1][j + 1];
		int l2 = l1 + i2 + j2 + k2 >> 2;
		ObjectDefinition class46 = ObjectDefinition.forID(j1);
		int i3 = i1 + (j << 7) + (j1 << 14) + 0x40000000;
		if (!class46.hasActions)
			i3 += 0x80000000;
		byte byte1 = (byte) ((i << 6) + k);
		if (k == 22) {
			Object obj;
			if (class46.animation == -1 && class46.childrenIDs == null)
				obj = class46.modelAt(22, i, l1, i2, j2, k2, -1);
			else
				obj = new Animable_Sub5(j1, i, 22, i2, j2, l1, k2, class46.animation, true);
			worldController.method280(k1, l2, j, ((Renderable) (obj)), byte1, i3, i1);
			if (class46.aBoolean767 && class46.hasActions)
				class11.method213(j, i1);
			return;
		}
		if (k == 10 || k == 11) {
			Object obj1;
			if (class46.animation == -1 && class46.childrenIDs == null)
				obj1 = class46.modelAt(10, i, l1, i2, j2, k2, -1);
			else
				obj1 = new Animable_Sub5(j1, i, 10, i2, j2, l1, k2, class46.animation, true);
			if (obj1 != null) {
				int j5 = 0;
				if (k == 11)
					j5 += 256;
				int k4;
				int i5;
				if (i == 1 || i == 3) {
					k4 = class46.anInt761;
					i5 = class46.anInt744;
				} else {
					k4 = class46.anInt744;
					i5 = class46.anInt761;
				}
				worldController.method284(i3, byte1, l2, i5, ((Renderable) (obj1)), k4, k1, j5, j, i1);
			}
			if (class46.aBoolean767)
				class11.method212(class46.aBoolean757, class46.anInt744, class46.anInt761, i1, j, i);
			return;
		}
		if (k >= 12) {
			Object obj2;
			if (class46.animation == -1 && class46.childrenIDs == null)
				obj2 = class46.modelAt(k, i, l1, i2, j2, k2, -1);
			else
				obj2 = new Animable_Sub5(j1, i, k, i2, j2, l1, k2, class46.animation, true);
			worldController.method284(i3, byte1, l2, 1, ((Renderable) (obj2)), 1, k1, 0, j, i1);
			if (class46.aBoolean767)
				class11.method212(class46.aBoolean757, class46.anInt744, class46.anInt761, i1, j, i);
			return;
		}
		if (k == 0) {
			Object obj3;
			if (class46.animation == -1 && class46.childrenIDs == null)
				obj3 = class46.modelAt(0, i, l1, i2, j2, k2, -1);
			else
				obj3 = new Animable_Sub5(j1, i, 0, i2, j2, l1, k2, class46.animation, true);
			worldController.method282(anIntArray152[i], ((Renderable) (obj3)), i3, j, byte1, i1, null, l2, 0, k1);
			if (class46.aBoolean767)
				class11.method211(j, i, i1, k, class46.aBoolean757);
			return;
		}
		if (k == 1) {
			Object obj4;
			if (class46.animation == -1 && class46.childrenIDs == null)
				obj4 = class46.modelAt(1, i, l1, i2, j2, k2, -1);
			else
				obj4 = new Animable_Sub5(j1, i, 1, i2, j2, l1, k2, class46.animation, true);
			worldController.method282(anIntArray140[i], ((Renderable) (obj4)), i3, j, byte1, i1, null, l2, 0, k1);
			if (class46.aBoolean767)
				class11.method211(j, i, i1, k, class46.aBoolean757);
			return;
		}
		if (k == 2) {
			int j3 = i + 1 & 3;
			Object obj11;
			Object obj12;
			if (class46.animation == -1 && class46.childrenIDs == null) {
				obj11 = class46.modelAt(2, 4 + i, l1, i2, j2, k2, -1);
				obj12 = class46.modelAt(2, j3, l1, i2, j2, k2, -1);
			} else {
				obj11 = new Animable_Sub5(j1, 4 + i, 2, i2, j2, l1, k2, class46.animation, true);
				obj12 = new Animable_Sub5(j1, j3, 2, i2, j2, l1, k2, class46.animation, true);
			}
			worldController.method282(anIntArray152[i], ((Renderable) (obj11)), i3, j, byte1, i1,
					((Renderable) (obj12)), l2, anIntArray152[j3], k1);
			if (class46.aBoolean767)
				class11.method211(j, i, i1, k, class46.aBoolean757);
			return;
		}
		if (k == 3) {
			Object obj5;
			if (class46.animation == -1 && class46.childrenIDs == null)
				obj5 = class46.modelAt(3, i, l1, i2, j2, k2, -1);
			else
				obj5 = new Animable_Sub5(j1, i, 3, i2, j2, l1, k2, class46.animation, true);
			worldController.method282(anIntArray140[i], ((Renderable) (obj5)), i3, j, byte1, i1, null, l2, 0, k1);
			if (class46.aBoolean767)
				class11.method211(j, i, i1, k, class46.aBoolean757);
			return;
		}
		if (k == 9) {
			Object obj6;
			if (class46.animation == -1 && class46.childrenIDs == null)
				obj6 = class46.modelAt(k, i, l1, i2, j2, k2, -1);
			else
				obj6 = new Animable_Sub5(j1, i, k, i2, j2, l1, k2, class46.animation, true);
			worldController.method284(i3, byte1, l2, 1, ((Renderable) (obj6)), 1, k1, 0, j, i1);
			if (class46.aBoolean767)
				class11.method212(class46.aBoolean757, class46.anInt744, class46.anInt761, i1, j, i);
			return;
		} // try
		if (class46.aBoolean762)
			if (i == 1) {
				int k3 = k2;
				k2 = j2;
				j2 = i2;
				i2 = l1;
				l1 = k3;
			} else if (i == 2) {
				int l3 = k2;
				k2 = i2;
				i2 = l3;
				l3 = j2;
				j2 = l1;
				l1 = l3;
			} else if (i == 3) {
				int i4 = k2;
				k2 = l1;
				l1 = i2;
				i2 = j2;
				j2 = i4;
			}
		if (k == 4) {
			Object obj7;
			if (class46.animation == -1 && class46.childrenIDs == null)
				obj7 = class46.modelAt(4, 0, l1, i2, j2, k2, -1);
			else
				obj7 = new Animable_Sub5(j1, 0, 4, i2, j2, l1, k2, class46.animation, true);
			worldController.method283(i3, j, i * 512, k1, 0, l2, ((Renderable) (obj7)), i1, byte1, 0, anIntArray152[i]);
			return;
		}
		if (k == 5) {
			int j4 = 16;
			int l4 = worldController.method300(k1, i1, j);
			if (l4 > 0)
				j4 = ObjectDefinition.forID(l4 >> 14 & 0x7fff).anInt775;
			Object obj13;
			if (class46.animation == -1 && class46.childrenIDs == null)
				obj13 = class46.modelAt(4, 0, l1, i2, j2, k2, -1);
			else
				obj13 = new Animable_Sub5(j1, 0, 4, i2, j2, l1, k2, class46.animation, true);
			worldController.method283(i3, j, i * 512, k1, anIntArray137[i] * j4, l2, ((Renderable) (obj13)), i1, byte1,
					anIntArray144[i] * j4, anIntArray152[i]);
			return;
		}
		if (k == 6) {
			Object obj8;
			if (class46.animation == -1 && class46.childrenIDs == null)
				obj8 = class46.modelAt(4, 0, l1, i2, j2, k2, -1);
			else
				obj8 = new Animable_Sub5(j1, 0, 4, i2, j2, l1, k2, class46.animation, true);
			worldController.method283(i3, j, i, k1, 0, l2, ((Renderable) (obj8)), i1, byte1, 0, 256);
			return;
		}
		if (k == 7) {
			Object obj9;
			if (class46.animation == -1 && class46.childrenIDs == null)
				obj9 = class46.modelAt(4, 0, l1, i2, j2, k2, -1);
			else
				obj9 = new Animable_Sub5(j1, 0, 4, i2, j2, l1, k2, class46.animation, true);
			worldController.method283(i3, j, i, k1, 0, l2, ((Renderable) (obj9)), i1, byte1, 0, 512);
			return;
		}
		if (k == 8) {
			Object obj10;
			if (class46.animation == -1 && class46.childrenIDs == null)
				obj10 = class46.modelAt(4, 0, l1, i2, j2, k2, -1);
			else
				obj10 = new Animable_Sub5(j1, 0, 4, i2, j2, l1, k2, class46.animation, true);
			worldController.method283(i3, j, i, k1, 0, l2, ((Renderable) (obj10)), i1, byte1, 0, 768);
		}
	}

	public static boolean method189(int i, byte[] is, int i_250_) {
		boolean bool = true;
		Stream stream = new Stream(is);
		int i_252_ = -1;
		for (;;) {
			int i_253_ = stream.method422();
			if (i_253_ == 0)
				break;
			i_252_ += i_253_;
			int i_254_ = 0;
			boolean bool_255_ = false;
			for (;;) {
				if (bool_255_) {
					int i_256_ = stream.method422();
					if (i_256_ == 0)
						break;
					stream.readUnsignedByte();
				} else {
					int i_257_ = stream.method422();
					if (i_257_ == 0)
						break;
					i_254_ += i_257_ - 1;
					int i_258_ = i_254_ & 0x3f;
					int i_259_ = i_254_ >> 6 & 0x3f;
					int i_260_ = stream.readUnsignedByte() >> 2;
					int i_261_ = i_259_ + i;
					int i_262_ = i_258_ + i_250_;
					if (i_261_ > 0 && i_262_ > 0 && i_261_ < 103 && i_262_ < 103) {
						ObjectDefinition class46 = ObjectDefinition.forID(i_252_);
						if (i_260_ != 22 || !lowMem || class46.hasActions || class46.aBoolean736) {
							bool &= class46.method579();
							bool_255_ = true;
						}
					}
				}
			}
		}
		return bool;
	}

	public final void method190(int i, CollisionMap aclass11[], int j, WorldController worldController, byte abyte0[]) {
		label0: {
			Stream stream = new Stream(abyte0);
			int l = -1;
			do {
				int i1 = stream.method422();
				if (i1 == 0)
					break label0;
				l += i1;
				int j1 = 0;
				do {
					int k1 = stream.method422();
					if (k1 == 0)
						break;
					j1 += k1 - 1;
					int l1 = j1 & 0x3f;
					int i2 = j1 >> 6 & 0x3f;
					int j2 = j1 >> 12;
					int k2 = stream.readUnsignedByte();
					int l2 = k2 >> 2;
					int i3 = k2 & 3;
					int j3 = i2 + i;
					int k3 = l1 + j;
					if (j3 > 0 && k3 > 0 && j3 < 103 && k3 < 103 && j2 >= 0 && j2 < 4) {
						int l3 = j2;
						if ((tileFlags[1][j3][k3] & 2) == 2)
							l3--;
						CollisionMap class11 = null;
						if (l3 >= 0)
							class11 = aclass11[l3];
						try {
							renderObject(k3, worldController, class11, l2, j2, j3, l, i3);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} while (true);
			} while (true);
		}
	}

	private final int[] hues;
	private final int[] saturations;
	private final int[] luminances;
	private final int[] chromas;
	private final int[] anIntArray128;
	private final int[][][] tileHeights;
	private final byte[][][] overlays;
	static int anInt131;
	private final byte[][][] shading;
	private final int[][][] anIntArrayArrayArray135;
	private final byte[][][] overlayTypes;
	private static final int anIntArray137[] = { 1, 0, -1, 0 };
	private final int[][] tileLighting;
	private static final int anIntArray140[] = { 16, 32, 64, 128 };
	private final byte[][][] underlays;
	private static final int anIntArray144[] = { 0, -1, 0, 1 };
	static int maximumPlane = 99;
	private final int regionSizeX;
	private final int regionSizeY;
	private final byte[][][] overlayOrientations;
	private final byte[][][] tileFlags;
	static boolean lowMem = true;
	private static final int anIntArray152[] = { 1, 2, 4, 8 };
	public static final int BRIDGE_TILE = 2;
	private static final int FORCE_LOWEST_PLANE = 8;
}