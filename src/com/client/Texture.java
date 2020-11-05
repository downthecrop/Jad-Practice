package com.client;

public final class Texture extends DrawingArea {

    public static int TEXTURE_IDX = 7;

    private static Texture[] cache = new Texture[678];

    public int[][] mipmaps = new int[8][];

    private static double brightness;

    public static final Texture get(int index) {
        if (index < 0 || index >= cache.length){
            return null;
        }
        if (cache[index] == null) {
            Client.instance.onDemandFetcher.method558(TEXTURE_IDX - 1, index);
            return null;
        }
        return cache[index];
    }

    public static final void decode(int index, byte[] data) {
        Texture texture = cache[index] = new Texture();
        Stream buffer = new Stream(data);
        int width = buffer.readUnsignedWord();
        int height = buffer.readUnsignedWord();
        texture.mipmaps[0] = new int[16384];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = buffer.read3Bytes();
                if (width <= 64 && height <= 64) {
                    int x2 = x << 1;
                    int y2 = y << 1;
                    texture.set(x2, y2, rgb);
                    texture.set(x2 + 1, y2, rgb);
                    texture.set(x2 + 1, y2 + 1, rgb);
                    texture.set(x2, y2 + 1, rgb);
                } else {
                    texture.set(x, y, rgb);
                }
            }
        }
        texture.generate();
    }

    public static void setBrightness(double value) {
        brightness = value;
    }

    private void set(int x, int y, int rgb) {
        if (x < 128 && y < 128) {
            mipmaps[0][x + (y << 7)] = adjustBrightness(rgb, brightness / 1.25D);
        }
    }

    private static int adjustBrightness(int rgb, double brightness) {
        int r = ((int) (Math.pow((rgb >>> 16) / 256.0D, brightness) * 256.0D) << 16);
        int g = ((int) (Math.pow(((rgb >>> 8) & 0xff) / 256.0D, brightness) * 256.0D) << 8);
        int b = (int) (Math.pow((rgb & 0xff) / 256.0D, brightness) * 256.0D);
        return r | g | b;
    }

    private void generate() {
        for (int level = 1, size = 64; level < 8; level++) {
            int[] src = mipmaps[level - 1];
            int[] dst = mipmaps[level] = new int[size * size];
            for (int x = 0; x < size; x++) {
                for (int y = 0; y < size; y++) {
                    double r = 0, g = 0, b = 0;
                    int count = 0;
                    for (int rgb : new int[] { src[x + (y * size << 1) << 1], src[(x + (y * size << 1) << 1) + 1], src[(x + (y * size << 1) << 1) + (size << 1)], src[(x + (y * size << 1) << 1) + (size << 1) + 1] }) {
                        if (rgb != 0) {
                            double dr = (rgb >> 16 & 0xff) / 255d;
                            double dg = (rgb >> 8 & 0xff) / 255d;
                            double db = (rgb & 0xff) / 255d;
                            r += dr * dr;
                            g += dg * dg;
                            b += db * db;
                            count++;
                        }
                    }
                    if (count != 0) {
                        int ri = Math.round(255 * (float) Math.sqrt(r / count));
                        int gi = Math.round(255 * (float) Math.sqrt(g / count));
                        int bi = Math.round(255 * (float) Math.sqrt(b / count));
                        dst[x + y * size] = ri << 16 | gi << 8 | bi;
                    }
                }
            }
            size >>= 1;
            dst = src = null;
        }
    }

    public static final void reset() {
        cache = null;
    }
}
