package com.client.definitions;

import com.client.Stream;
import com.client.StreamLoader;

public final class VarBit {

	public static void unpackConfig(StreamLoader streamLoader) {
		Stream stream = new Stream(streamLoader.getDataForName("varbit.dat"));
		int cacheSize = stream.readUnsignedWord();
		if (cache == null)
			cache = new VarBit[cacheSize];
		for (int j = 0; j < cacheSize; j++) {
			if (cache[j] == null)
				cache[j] = new VarBit();
			cache[j].readValues(stream);
		}

		if (stream.currentOffset != stream.buffer.length)
			System.out.println("varbit load mismatch");
	}

	private void readValues(Stream stream) {
		anInt648 = stream.readUnsignedWord();
		anInt649 = stream.readUnsignedByte();
		anInt650 = stream.readUnsignedByte();
	}

	private VarBit() {
		
	}

	public static VarBit cache[];
	public int anInt648;
	public int anInt649;
	public int anInt650;
	
}
