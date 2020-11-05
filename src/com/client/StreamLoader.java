package com.client;
import java.io.*;
import java.util.zip.*;

public class StreamLoader {

	public StreamLoader(byte[] b, String s) {
		try {
			// if (s.contains("2d"))
			// b = getBytesFromFile(new File("./data.dat"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		a(b);
	}

	public static byte[] getBytesFromFile(File file) throws IOException {
		InputStream is = new FileInputStream(file);
		long length = file.length();
		byte[] bytes = new byte[(int) length];

		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length
				&& (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
			offset += numRead;
		}

		if (offset < bytes.length) {
			is.close();
			throw new IOException("Could not completely read file "
					+ file.getName());
		}

		is.close();
		return bytes;
	}

	public void a(byte abyte0[]) {
		Stream stream = new Stream(abyte0);
		int i = stream.read3Bytes();
		int j = stream.read3Bytes();
		if (j == 0) {
			byte[] abyte1 = new byte[i];
			byte[] abyte3 = new byte[i];
			System.arraycopy(abyte0, 6, abyte1, 0, abyte0.length - 6);
			try {
				DataInputStream datainputstream = new DataInputStream(
						new GZIPInputStream(new ByteArrayInputStream(abyte1)));
				datainputstream.readFully(abyte3, 0, abyte3.length);
			} catch (Exception exception) {
				exception.printStackTrace();
			}
			aByteArray726 = abyte3;
			stream = new Stream(aByteArray726);
			aBoolean732 = true;
		} else if (j != i) {
			byte abyte1[] = new byte[i];
			Class13.method225(abyte1, i, abyte0, j, 6);
			aByteArray726 = abyte1;
			stream = new Stream(aByteArray726);
			aBoolean732 = true;
		} else {
			aByteArray726 = abyte0;
			aBoolean732 = false;
		}
		dataSize = stream.readUnsignedWord();
		anIntArray728 = new int[dataSize];
		anIntArray729 = new int[dataSize];
		anIntArray730 = new int[dataSize];
		anIntArray731 = new int[dataSize];
		int k = stream.currentOffset + dataSize * 10;
		for (int l = 0; l < dataSize; l++) {
			anIntArray728[l] = stream.readDWord();
			anIntArray729[l] = stream.read3Bytes();
			anIntArray730[l] = stream.read3Bytes();
			anIntArray731[l] = k;
			k += anIntArray730[l];
		}
	}

	public byte[] getDataForName(String s) {
		byte abyte0[] = null; // was a parameter
		int i = 0;
		s = s.toUpperCase();
		for (int j = 0; j < s.length(); j++)
			i = (i * 61 + s.charAt(j)) - 32;

		for (int k = 0; k < dataSize; k++)
			if (anIntArray728[k] == i) {
				if (abyte0 == null)
					abyte0 = new byte[anIntArray729[k]];
				if (!aBoolean732) {
					Class13.method225(abyte0, anIntArray729[k], aByteArray726,
							anIntArray730[k], anIntArray731[k]);
				} else {
					System.arraycopy(aByteArray726, anIntArray731[k], abyte0,
							0, anIntArray729[k]);

				}
				return abyte0;
			}

		return null;
	}

	public byte[] aByteArray726;
	public int dataSize;
	public int[] anIntArray728;
	public int[] anIntArray729;
	public int[] anIntArray730;
	public int[] anIntArray731;
	public boolean aBoolean732;
}
