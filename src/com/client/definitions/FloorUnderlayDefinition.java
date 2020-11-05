package com.client.definitions;

import com.client.Client;
import com.client.utilities.FileOperations;
import com.client.Stream;
import com.client.StreamLoader;
import com.client.sign.Signlink;

public final class FloorUnderlayDefinition {

	public static void unpackConfig(StreamLoader streamLoader) {
		Stream stream = new Stream(FileOperations.readFile(Signlink.getCacheDirectory() + "/data/flo.dat"));
		int cacheSize = stream.readUnsignedWord();
		if (underlays == null)
			underlays = new FloorUnderlayDefinition[cacheSize];
		for (int j = 0; j < cacheSize; j++) {
			if (underlays[j] == null) {
				underlays[j] = new FloorUnderlayDefinition();
			}
			underlays[j].readValues(stream);
		}
	}

	private void readValues(Stream stream) {
		anInt390 = stream.read3Bytes();
		if (Client.snowVisible) {
			if (anInt390 == 0x35720A || 
				anInt390 == 0x50680B || 
				anInt390 == 0x78680B || 
				anInt390 == 0x6CAC10 || 
				anInt390 == 0x819531 || 
				anInt390 == 0x4C5610 ||
				anInt390 == 0x6A3C00 || 
				anInt390 == 0x58680B) {
				anInt390 = 0xffffff;
			}
		}
		method262(anInt390);
	}

	private void method262(int i) {
		double d = (i >> 16 & 0xff) / 256D;
		double d1 = (i >> 8 & 0xff) / 256D;
		double d2 = (i & 0xff) / 256D;
		double d3 = d;
		if (d1 < d3)
			d3 = d1;
		if (d2 < d3)
			d3 = d2;
		double d4 = d;
		if (d1 > d4)
			d4 = d1;
		if (d2 > d4)
			d4 = d2;
		double d5 = 0.0D;
		double d6 = 0.0D;
		double d7 = (d3 + d4) / 2D;
		if (d3 != d4) {
			if (d7 < 0.5D)
				d6 = (d4 - d3) / (d4 + d3);
			if (d7 >= 0.5D)
				d6 = (d4 - d3) / (2D - d4 - d3);
			if (d == d4)
				d5 = (d1 - d2) / (d4 - d3);
			else if (d1 == d4)
				d5 = 2D + (d2 - d) / (d4 - d3);
			else if (d2 == d4)
				d5 = 4D + (d - d1) / (d4 - d3);
		}
		d5 /= 6D;
		hue = (int) (d5 * 256D);
		saturation = (int) (d6 * 256D);
		luminance = (int) (d7 * 256D);
		if (saturation < 0)
			saturation = 0;
		else if (saturation > 255)
			saturation = 255;
		if (luminance < 0)
			luminance = 0;
		else if (luminance > 255)
			luminance = 255;
		if (d7 > 0.5D)
			blendHueMultiplier = (int) ((1.0D - d7) * d6 * 512D);
		else
			blendHueMultiplier = (int) (d7 * d6 * 512D);
		if (blendHueMultiplier < 1)
			blendHueMultiplier = 1;
		blendHue = (int) (d5 * blendHueMultiplier);
		int k = (hue + (int) (Math.random() * 16D)) - 8;
		if (k < 0)
			k = 0;
		else if (k > 255)
			k = 255;
		int l = (saturation + (int) (Math.random() * 48D)) - 24;
		if (l < 0)
			l = 0;
		else if (l > 255)
			l = 255;
		int i1 = (luminance + (int) (Math.random() * 48D) - 24);
		if (i1 < 0)
			i1 = 0;
		else if (i1 > 255)
			i1 = 255;
		hsl16 = hsl24to16(k, l, i1);
	}

	private int hsl24to16(int i, int j, int k) {
		if (k > 179)
			j /= 2;
		if (k > 192)
			j /= 2;
		if (k > 217)
			j /= 2;
		if (k > 243)
			j /= 2;
		return (i / 4 << 10) + (j / 32 << 7) + k / 2;
	}

	private FloorUnderlayDefinition() {
		
	}

	public static FloorUnderlayDefinition underlays[];
	public int anInt390;
	public int hue;
	public int saturation;
	public int luminance;
	public int blendHue;
	public int blendHueMultiplier;
	public int hsl16;
}
