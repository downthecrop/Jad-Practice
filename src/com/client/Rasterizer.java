package com.client;

public final class Rasterizer extends DrawingArea {

    public static boolean saveDepth;
    public static float[] depthBuffer;
    private static int mipMapLevel;
    public static int textureAmount = 60;
    static boolean aBoolean1462;
    private static boolean aBoolean1463;
    public static boolean aBoolean1464 = true;
    public static int anInt1465;
    public static int textureInt1;
    public static int textureInt2;
    public static int textureInt3;
    public static int textureInt4;
    private static int[] anIntArray1468;
    public static final int[] anIntArray1469;
    public static int anIntArray1470[];
    public static int anIntArray1471[];
    public static int anIntArray1472[];
    private static int textureCount;
    public static Background textures[] = new Background[textureAmount];
    private static boolean[] textureIsTransparant = new boolean[textureAmount];
    private static int[] averageTextureColours = new int[textureAmount];
    private static int textureRequestBufferPointer;
    private static int[][] anIntArrayArray1478;
    private static int[][] texturesPixelBuffer = new int[textureAmount][];
    public static int textureLastUsed[] = new int[textureAmount];
    public static int anInt1481;
    public static int hslToRgb[] = new int[0x10000];
    private static int[][] currentPalette = new int[textureAmount][];

    static {
        anIntArray1468 = new int[512];
        anIntArray1469 = new int[2048];
        anIntArray1470 = new int[2048];
        anIntArray1471 = new int[2048];
        for (int i = 1; i < 512; i++) {
            anIntArray1468[i] = 32768 / i;
        }
        for (int j = 1; j < 2048; j++) {
            anIntArray1469[j] = 0x10000 / j;
        }
        for (int k = 0; k < 2048; k++) {
            anIntArray1470[k] = (int) (65536D * Math.sin((double) k * 0.0030679614999999999D));
            anIntArray1471[k] = (int) (65536D * Math.cos((double) k * 0.0030679614999999999D));
        }
    }

    public static void nullLoader() {
        anIntArray1468 = null;
        anIntArray1468 = null;
        anIntArray1470 = null;
        anIntArray1471 = null;
        anIntArray1472 = null;
        textures = null;
        textureIsTransparant = null;
        averageTextureColours = null;
        anIntArrayArray1478 = null;
        texturesPixelBuffer = null;
        textureLastUsed = null;
        hslToRgb = null;
        currentPalette = null;
    }

    public static void method364() {
        anIntArray1472 = new int[DrawingArea.height];
        for (int j = 0; j < DrawingArea.height; j++) {
            anIntArray1472[j] = DrawingArea.width * j;
        }
        textureInt1 = DrawingArea.width / 2;
        textureInt2 = DrawingArea.height / 2;
    }

    public static void method365(int width, int height) {
        anIntArray1472 = new int[height];
        for (int l = 0; l < height; l++) {
            anIntArray1472[l] = width * l;
        }
        textureInt1 = width / 2;
        textureInt2 = height / 2;
    }

    public static void drawFog(int rgb, int begin, int end) {
        float length = end - begin;// Store as a float for division later
        for (int index = 0; index < pixels.length; index++) {
            float factor = (depthBuffer[index] - begin) / length;
            pixels[index] = blend(pixels[index], rgb, factor);
        }
    }

    private static int blend(int c1, int c2, float factor) {
        if (factor >= 1f) {
            return c2;
        }
        if (factor <= 0f) {
            return c1;
        }

        int r1 = (c1 >> 16) & 0xff;
        int g1 = (c1 >> 8) & 0xff;
        int b1 = (c1) & 0xff;

        int r2 = (c2 >> 16) & 0xff;
        int g2 = (c2 >> 8) & 0xff;
        int b2 = (c2) & 0xff;

        int r3 = r2 - r1;
        int g3 = g2 - g1;
        int b3 = b2 - b1;

        int r = (int) (r1 + (r3 * factor));
        int g = (int) (g1 + (g3 * factor));
        int b = (int) (b1 + (b3 * factor));

        return (r << 16) + (g << 8) + b;
    }

    public static int texelPos(int defaultIndex) {
        int x = (defaultIndex & 127) >> mipMapLevel;
        int y = (defaultIndex >> 7) >> mipMapLevel;
        return x + (y << (7 - mipMapLevel));
    }

    public static boolean enableMipmapping = true;
    public static boolean enableDistanceFog = true;

    private static final int[] ids = { 17, 31, 34, 40, 53, 54, 56, 57, 58, 59 };

    public static void setMipmapLevel(int y1, int y2, int y3, int x1, int x2, int x3, int tex) {
        if (!enableMipmapping) {
            if (mipMapLevel != 0) {
                mipMapLevel = 0;
            }
            return;
        }
        for (int tex2 : ids) {
            if (tex == tex2) {
                mipMapLevel = 0;
                return;
            }
        }
        int textureArea = x1 * (y2 - y3) + x2 * (y3 - y1) + x3 * (y1 - y2) >> 1;
        if (textureArea < 0) {
            textureArea = -textureArea;
        }
        if (textureArea > 16384) {
            mipMapLevel = 0;
        } else if (textureArea > 4096) {
            mipMapLevel = 1;
        } else if (textureArea > 1024) {
            mipMapLevel = 1;
        } else if (textureArea > 256) {
            mipMapLevel = 2;
        } else if (textureArea > 64) {
            mipMapLevel = 3;
        } else if (textureArea > 16) {
            mipMapLevel = 4;
        } else if (textureArea > 4) {
            mipMapLevel = 5;
        } else if (textureArea > 1) {
            mipMapLevel = 6;
        } else {
            mipMapLevel = 7;
        }
    }

    public static void method366() {
        anIntArrayArray1478 = null;
        for (int j = 0; j < textureAmount; j++) {
            texturesPixelBuffer[j] = null;
        }
    }

    public static void method367() {
        if (anIntArrayArray1478 == null) {
            textureRequestBufferPointer = 20;
            anIntArrayArray1478 = new int[textureRequestBufferPointer][0x10000];
            for (int k = 0; k < textureAmount; k++) {
                texturesPixelBuffer[k] = null;
            }
        }
    }

    public static void method368(StreamLoader streamLoader) {
        textureCount = 0;
        for (int index = 0; index < textureAmount; index++) {
            try {
                textures[index] = new Background(streamLoader, String.valueOf(index), 0);
                textures[index].method357();
                textureCount++;
            } catch (Exception ex) {
            }
        }
    }

    public static int method369(int texture) {
        if (averageTextureColours[texture] != 0) {
            return averageTextureColours[texture];
        }
        int r = 0;
        int g = 0;
        int b = 0;
        final int textureColorCount = currentPalette[texture].length;
        for (int index = 0; index < textureColorCount; index++) {
            r += currentPalette[texture][index] >> 16 & 0xff;
            g += currentPalette[texture][index] >> 8 & 0xff;
            b += currentPalette[texture][index] & 0xff;
        }
        int color = (r / textureColorCount << 16) + (g / textureColorCount << 8) + b / textureColorCount;
        color = adjustBrightness(color, 1.3999999999999999D);
        if (color == 0) {
            color = 1;
        }
        averageTextureColours[texture] = color;
        return color;
    }

    public static void method370(int texture) {
        try {
            if (texturesPixelBuffer[texture] == null) {
                return;
            }
            anIntArrayArray1478[textureRequestBufferPointer++] = texturesPixelBuffer[texture];
            texturesPixelBuffer[texture] = null;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public static int getOverallColour(int textureId) {
        if (averageTextureColours[textureId] != 0)
            return averageTextureColours[textureId];
        int totalRed = 0;
        int totalGreen = 0;
        int totalBlue = 0;
        int colourCount = currentPalette[textureId].length;
        for (int ptr = 0; ptr < colourCount; ptr++) {
            totalRed += currentPalette[textureId][ptr] >> 16 & 0xff;
            totalGreen += currentPalette[textureId][ptr] >> 8 & 0xff;
            totalBlue += currentPalette[textureId][ptr] & 0xff;
        }

        int avgPaletteColour = (totalRed / colourCount << 16) + (totalGreen / colourCount << 8) + totalBlue / colourCount;
        avgPaletteColour = adjustBrightness(avgPaletteColour, 1.3999999999999999D);
        if (avgPaletteColour == 0)
            avgPaletteColour = 1;
        averageTextureColours[textureId] = avgPaletteColour;
        return avgPaletteColour;
    }

    public static void requestTextureUpdate(int i) {
        if (i > 50 && i != 59){
            return;
        }
        if (texturesPixelBuffer[i] == null)
            return;
        anIntArrayArray1478[textureRequestBufferPointer++] = texturesPixelBuffer[i];
        texturesPixelBuffer[i] = null;
    }

    private static int[] getTexturePixels(int textureId) {
        textureLastUsed[textureId] = anInt1481++;
        if (texturesPixelBuffer[textureId] != null)
            return texturesPixelBuffer[textureId];
        int texturePixels[];
        if (textureRequestBufferPointer > 0) {
            texturePixels = anIntArrayArray1478[--textureRequestBufferPointer];
            anIntArrayArray1478[textureRequestBufferPointer] = null;
        } else {
            int lastUsed = 0;
            int target = -1;
            for (int l = 0; l < textureCount; l++)
                if (texturesPixelBuffer[l] != null
                        && (textureLastUsed[l] < lastUsed || target == -1)) {
                    lastUsed = textureLastUsed[l];
                    target = l;
                }

            texturePixels = texturesPixelBuffer[target];
            texturesPixelBuffer[target] = null;
        }
        texturesPixelBuffer[textureId] = texturePixels;
        Background background = textures[textureId];
        int ai1[] = currentPalette[textureId];
            if (background.width == 64) {
                for (int j1 = 0; j1 < 128; j1++) {
                    for (int j2 = 0; j2 < 128; j2++)
                        texturePixels[j2 + (j1 << 7)] = ai1[background.palettePixels[(j2 >> 1)
                                + ((j1 >> 1) << 6)]];

                }

            } else {
                for (int k1 = 0; k1 < 16384; k1++)
                    texturePixels[k1] = ai1[background.palettePixels[k1]];

            }
            textureIsTransparant[textureId] = false;
            for (int l1 = 0; l1 < 16384; l1++) {
                texturePixels[l1] &= 0xf8f8ff;
                int colour = texturePixels[l1];
                if (colour == 0)
                    textureIsTransparant[textureId] = true;
                texturePixels[16384 + l1] = colour - (colour >>> 3) & 0xf8f8ff;
                texturePixels[32768 + l1] = colour - (colour >>> 2) & 0xf8f8ff;
                texturePixels[49152 + l1] = colour - (colour >>> 2) - (colour >>> 3) & 0xf8f8ff;
            }
        return texturePixels;
    }

    public static void setBrightness(double d) {
    		Texture.setBrightness(d * 2);
    		int j = 0;
        for (int k = 0; k < 512; k++) {
            double d1 = k / 8 / 64D + 0.0078125D;
            double d2 = (k & 7) / 8D + 0.0625D;
            for (int k1 = 0; k1 < 128; k1++) {
                double d3 = k1 / 128D;
                double d4 = d3;
                double d5 = d3;
                double d6 = d3;
                if (d2 != 0.0D) {
                    double d7;
                    if (d3 < 0.5D)
                        d7 = d3 * (1.0D + d2);
                    else
                        d7 = (d3 + d2) - d3 * d2;
                    double d8 = 2D * d3 - d7;
                    double d9 = d1 + 0.33333333333333331D;
                    if (d9 > 1.0D)
                        d9--;
                    double d10 = d1;
                    double d11 = d1 - 0.33333333333333331D;
                    if (d11 < 0.0D)
                        d11++;
                    if (6D * d9 < 1.0D)
                        d4 = d8 + (d7 - d8) * 6D * d9;
                    else if (2D * d9 < 1.0D)
                        d4 = d7;
                    else if (3D * d9 < 2D)
                        d4 = d8 + (d7 - d8) * (0.66666666666666663D - d9) * 6D;
                    else
                        d4 = d8;
                    if (6D * d10 < 1.0D)
                        d5 = d8 + (d7 - d8) * 6D * d10;
                    else if (2D * d10 < 1.0D)
                        d5 = d7;
                    else if (3D * d10 < 2D)
                        d5 = d8 + (d7 - d8) * (0.66666666666666663D - d10) * 6D;
                    else
                        d5 = d8;
                    if (6D * d11 < 1.0D)
                        d6 = d8 + (d7 - d8) * 6D * d11;
                    else if (2D * d11 < 1.0D)
                        d6 = d7;
                    else if (3D * d11 < 2D)
                        d6 = d8 + (d7 - d8) * (0.66666666666666663D - d11) * 6D;
                    else
                        d6 = d8;
                }
                int byteR = (int) (d4 * 256D);
                int byteG = (int) (d5 * 256D);
                int byteB = (int) (d6 * 256D);
                int rgb = (byteR << 16) + (byteG << 8) + byteB;
                rgb = adjustBrightness(rgb, d);
                if (rgb == 0)
                    rgb = 1;
                hslToRgb[j++] = rgb;
            }

        }

        for (int textureId = 0; textureId < textureAmount; textureId++)
            if (textures[textureId] != null) {
                int originalPalette[] = textures[textureId].palette;
                currentPalette[textureId] = new int[originalPalette.length];
                for (int colourId = 0; colourId < originalPalette.length; colourId++) {
                    currentPalette[textureId][colourId] = adjustBrightness(originalPalette[colourId], d);
                    if ((currentPalette[textureId][colourId] & 0xf8f8ff) == 0 && colourId != 0)
                        currentPalette[textureId][colourId] = 1;
                }

            }

        for (int textureId = 0; textureId < textureAmount + 1; textureId++)
            requestTextureUpdate(textureId);
    }
    static int adjustBrightness(int color, double amt) {
        double red = (color >> 16) / 256D;
        double green = (color >> 8 & 0xff) / 256D;
        double blue = (color & 0xff) / 256D;
        red = Math.pow(red, amt);
        green = Math.pow(green, amt);
        blue = Math.pow(blue, amt);
        final int red2 = (int) (red * 256D);
        final int green2 = (int) (green * 256D);
        final int blue2 = (int) (blue * 256D);
        return (red2 << 16) + (green2 << 8) + blue2;
    }

    public static boolean enableHDTextures = false;

    public static void drawMaterializedTriangle(int y1, int y2, int y3, int x1, int x2, int x3, int hsl1, int hsl2,
                                                int hsl3, int tx1, int tx2, int tx3, int ty1, int ty2, int ty3, int tz1, int tz2, int tz3, int tex, float z1,
                                                float z2, float z3) {
        if (!enableHDTextures || Texture.get(tex) == null) {
            drawGouraudTriangle(y1, y2, y3, x1, x2, x3, hsl1, hsl2, hsl3, z1, z2, z3);
            return;
        }
        setMipmapLevel(y1, y2, y3, x1, x2, x3, tex);
        int[] texels = Texture.get(tex).mipmaps[mipMapLevel];
        tx2 = tx1 - tx2;
        ty2 = ty1 - ty2;
        tz2 = tz1 - tz2;
        tx3 -= tx1;
        ty3 -= ty1;
        tz3 -= tz1;
        int l4 = (tx3 * ty1 - ty3 * tx1) * WorldController.focalLength << 5;
        int i5 = ty3 * tz1 - tz3 * ty1 << 8;
        int j5 = tz3 * tx1 - tx3 * tz1 << 5;
        int k5 = (tx2 * ty1 - ty2 * tx1) * WorldController.focalLength << 5;
        int l5 = ty2 * tz1 - tz2 * ty1 << 8;
        int i6 = tz2 * tx1 - tx2 * tz1 << 5;
        int j6 = (ty2 * tx3 - tx2 * ty3) * WorldController.focalLength << 5;
        int k6 = tz2 * ty3 - ty2 * tz3 << 8;
        int l6 = tx2 * tz3 - tz2 * tx3 << 5;
        int i7 = 0;
        int j7 = 0;
        if (y2 != y1) {
            i7 = (x2 - x1 << 16) / (y2 - y1);
            j7 = (hsl2 - hsl1 << 15) / (y2 - y1);
        }
        int k7 = 0;
        int l7 = 0;
        if (y3 != y2) {
            k7 = (x3 - x2 << 16) / (y3 - y2);
            l7 = (hsl3 - hsl2 << 15) / (y3 - y2);
        }
        int i8 = 0;
        int j8 = 0;
        if (y3 != y1) {
            i8 = (x1 - x3 << 16) / (y1 - y3);
            j8 = (hsl1 - hsl3 << 15) / (y1 - y3);
        }

        float x21 = x2 - x1;
        float y32 = y2 - y1;
        float x31 = x3 - x1;
        float y31 = y3 - y1;
        float z21 = z2 - z1;
        float z31 = z3 - z1;

        float div = x21 * y31 - x31 * y32;
        float depthSlope = (z21 * y31 - z31 * y32) / div;
        float depthScale = (z31 * x21 - z21 * x31) / div;

        if (y1 <= y2 && y1 <= y3) {
            if (y1 >= DrawingArea.bottomY) {
                return;
            }
            if (y2 > DrawingArea.bottomY) {
                y2 = DrawingArea.bottomY;
            }
            if (y3 > DrawingArea.bottomY) {
                y3 = DrawingArea.bottomY;
            }
            z1 = z1 - depthSlope * x1 + depthSlope;
            if (y2 < y3) {
                x3 = x1 <<= 16;
                hsl3 = hsl1 <<= 15;
                if (y1 < 0) {
                    x3 -= i8 * y1;
                    x1 -= i7 * y1;
                    z1 -= depthScale * y1;
                    hsl3 -= j8 * y1;
                    hsl1 -= j7 * y1;
                    y1 = 0;
                }
                x2 <<= 16;
                hsl2 <<= 15;
                if (y2 < 0) {
                    x2 -= k7 * y2;
                    hsl2 -= l7 * y2;
                    y2 = 0;
                }
                int k8 = y1 - textureInt2;
                l4 += j5 * k8;
                k5 += i6 * k8;
                j6 += l6 * k8;
                if (y1 != y2 && i8 < i7 || y1 == y2 && i8 > k7) {
                    y3 -= y2;
                    y2 -= y1;
                    y1 = anIntArray1472[y1];
                    while (--y2 >= 0) {
                        drawMaterializedScanline(DrawingArea.pixels, texels, y1, x3 >> 16, x1 >> 16, hsl3 >> 7,
                                hsl1 >> 7, l4, k5, j6, i5, l5, k6, z1, depthSlope);
                        x3 += i8;
                        x1 += i7;
                        z1 += depthScale;
                        hsl3 += j8;
                        hsl1 += j7;
                        y1 += DrawingArea.width;
                        l4 += j5;
                        k5 += i6;
                        j6 += l6;
                    }
                    while (--y3 >= 0) {
                        drawMaterializedScanline(DrawingArea.pixels, texels, y1, x3 >> 16, x2 >> 16, hsl3 >> 7,
                                hsl2 >> 7, l4, k5, j6, i5, l5, k6, z1, depthSlope);
                        x3 += i8;
                        x2 += k7;
                        z1 += depthScale;
                        hsl3 += j8;
                        hsl2 += l7;
                        y1 += DrawingArea.width;
                        l4 += j5;
                        k5 += i6;
                        j6 += l6;
                    }
                    texels = null;
                    return;
                }
                y3 -= y2;
                y2 -= y1;
                y1 = anIntArray1472[y1];
                while (--y2 >= 0) {
                    drawMaterializedScanline(DrawingArea.pixels, texels, y1, x1 >> 16, x3 >> 16, hsl1 >> 7, hsl3 >> 7,
                            l4, k5, j6, i5, l5, k6, z1, depthSlope);
                    x3 += i8;
                    x1 += i7;
                    z1 += depthScale;
                    hsl3 += j8;
                    hsl1 += j7;
                    y1 += DrawingArea.width;
                    l4 += j5;
                    k5 += i6;
                    j6 += l6;
                }
                while (--y3 >= 0) {
                    drawMaterializedScanline(DrawingArea.pixels, texels, y1, x2 >> 16, x3 >> 16, hsl2 >> 7, hsl3 >> 7,
                            l4, k5, j6, i5, l5, k6, z1, depthSlope);
                    x3 += i8;
                    x2 += k7;
                    z1 += depthScale;
                    hsl3 += j8;
                    hsl2 += l7;
                    y1 += DrawingArea.width;
                    l4 += j5;
                    k5 += i6;
                    j6 += l6;
                }
                texels = null;
                return;
            }
            x2 = x1 <<= 16;
            hsl2 = hsl1 <<= 15;
            if (y1 < 0) {
                x2 -= i8 * y1;
                x1 -= i7 * y1;
                z1 -= depthScale * y1;
                hsl2 -= j8 * y1;
                hsl1 -= j7 * y1;
                y1 = 0;
            }
            x3 <<= 16;
            hsl3 <<= 15;
            if (y3 < 0) {
                x3 -= k7 * y3;
                hsl3 -= l7 * y3;
                y3 = 0;
            }
            int l8 = y1 - textureInt2;
            l4 += j5 * l8;
            k5 += i6 * l8;
            j6 += l6 * l8;
            if (y1 != y3 && i8 < i7 || y1 == y3 && k7 > i7) {
                y2 -= y3;
                y3 -= y1;
                y1 = anIntArray1472[y1];
                while (--y3 >= 0) {
                    drawMaterializedScanline(DrawingArea.pixels, texels, y1, x2 >> 16, x1 >> 16, hsl2 >> 7, hsl1 >> 7,
                            l4, k5, j6, i5, l5, k6, z1, depthSlope);
                    x2 += i8;
                    x1 += i7;
                    z1 += depthScale;
                    hsl2 += j8;
                    hsl1 += j7;
                    y1 += DrawingArea.width;
                    l4 += j5;
                    k5 += i6;
                    j6 += l6;
                }
                while (--y2 >= 0) {
                    drawMaterializedScanline(DrawingArea.pixels, texels, y1, x3 >> 16, x1 >> 16, hsl3 >> 7, hsl1 >> 7,
                            l4, k5, j6, i5, l5, k6, z1, depthSlope);
                    x3 += k7;
                    x1 += i7;
                    z1 += depthScale;
                    hsl3 += l7;
                    hsl1 += j7;
                    y1 += DrawingArea.width;
                    l4 += j5;
                    k5 += i6;
                    j6 += l6;
                }
                texels = null;
                return;
            }
            y2 -= y3;
            y3 -= y1;
            y1 = anIntArray1472[y1];
            while (--y3 >= 0) {
                drawMaterializedScanline(DrawingArea.pixels, texels, y1, x1 >> 16, x2 >> 16, hsl1 >> 7, hsl2 >> 7, l4,
                        k5, j6, i5, l5, k6, z1, depthSlope);
                x2 += i8;
                x1 += i7;
                z1 += depthScale;
                hsl2 += j8;
                hsl1 += j7;
                y1 += DrawingArea.width;
                l4 += j5;
                k5 += i6;
                j6 += l6;
            }
            while (--y2 >= 0) {
                drawMaterializedScanline(DrawingArea.pixels, texels, y1, x1 >> 16, x3 >> 16, hsl1 >> 7, hsl3 >> 7, l4,
                        k5, j6, i5, l5, k6, z1, depthSlope);
                x3 += k7;
                x1 += i7;
                z1 += depthScale;
                hsl3 += l7;
                hsl1 += j7;
                y1 += DrawingArea.width;
                l4 += j5;
                k5 += i6;
                j6 += l6;
            }
            texels = null;
            return;
        }
        if (y2 <= y3) {
            if (y2 >= DrawingArea.bottomY) {
                return;
            }
            if (y3 > DrawingArea.bottomY) {
                y3 = DrawingArea.bottomY;
            }
            if (y1 > DrawingArea.bottomY) {
                y1 = DrawingArea.bottomY;
            }
            z2 = z2 - depthSlope * x2 + depthSlope;
            if (y3 < y1) {
                x1 = x2 <<= 16;
                hsl1 = hsl2 <<= 15;
                if (y2 < 0) {
                    x1 -= i7 * y2;
                    x2 -= k7 * y2;
                    z2 -= depthScale * y2;
                    hsl1 -= j7 * y2;
                    hsl2 -= l7 * y2;
                    y2 = 0;
                }
                x3 <<= 16;
                hsl3 <<= 15;
                if (y3 < 0) {
                    x3 -= i8 * y3;
                    hsl3 -= j8 * y3;
                    y3 = 0;
                }
                int i9 = y2 - textureInt2;
                l4 += j5 * i9;
                k5 += i6 * i9;
                j6 += l6 * i9;
                if (y2 != y3 && i7 < k7 || y2 == y3 && i7 > i8) {
                    y1 -= y3;
                    y3 -= y2;
                    y2 = anIntArray1472[y2];
                    while (--y3 >= 0) {
                        drawMaterializedScanline(DrawingArea.pixels, texels, y2, x1 >> 16, x2 >> 16, hsl1 >> 7,
                                hsl2 >> 7, l4, k5, j6, i5, l5, k6, z2, depthSlope);
                        x1 += i7;
                        x2 += k7;
                        z2 += depthScale;
                        hsl1 += j7;
                        hsl2 += l7;
                        y2 += DrawingArea.width;
                        l4 += j5;
                        k5 += i6;
                        j6 += l6;
                    }
                    while (--y1 >= 0) {
                        drawMaterializedScanline(DrawingArea.pixels, texels, y2, x1 >> 16, x3 >> 16, hsl1 >> 7,
                                hsl3 >> 7, l4, k5, j6, i5, l5, k6, z2, depthSlope);
                        x1 += i7;
                        x3 += i8;
                        z2 += depthScale;
                        hsl1 += j7;
                        hsl3 += j8;
                        y2 += DrawingArea.width;
                        l4 += j5;
                        k5 += i6;
                        j6 += l6;
                    }
                    texels = null;
                    return;
                }
                y1 -= y3;
                y3 -= y2;
                y2 = anIntArray1472[y2];
                while (--y3 >= 0) {
                    drawMaterializedScanline(DrawingArea.pixels, texels, y2, x2 >> 16, x1 >> 16, hsl2 >> 7, hsl1 >> 7,
                            l4, k5, j6, i5, l5, k6, z2, depthSlope);
                    x1 += i7;
                    x2 += k7;
                    z2 += depthScale;
                    hsl1 += j7;
                    hsl2 += l7;
                    y2 += DrawingArea.width;
                    l4 += j5;
                    k5 += i6;
                    j6 += l6;
                }
                while (--y1 >= 0) {
                    drawMaterializedScanline(DrawingArea.pixels, texels, y2, x3 >> 16, x1 >> 16, hsl3 >> 7, hsl1 >> 7,
                            l4, k5, j6, i5, l5, k6, z2, depthSlope);
                    x1 += i7;
                    x3 += i8;
                    z2 += depthScale;
                    hsl1 += j7;
                    hsl3 += j8;
                    y2 += DrawingArea.width;
                    l4 += j5;
                    k5 += i6;
                    j6 += l6;
                }
                texels = null;
                return;
            }
            x3 = x2 <<= 16;
            hsl3 = hsl2 <<= 15;
            if (y2 < 0) {
                x3 -= i7 * y2;
                x2 -= k7 * y2;
                z2 -= depthScale * y2;
                hsl3 -= j7 * y2;
                hsl2 -= l7 * y2;
                y2 = 0;
            }
            x1 <<= 16;
            hsl1 <<= 15;
            if (y1 < 0) {
                x1 -= i8 * y1;
                hsl1 -= j8 * y1;
                y1 = 0;
            }
            int j9 = y2 - textureInt2;
            l4 += j5 * j9;
            k5 += i6 * j9;
            j6 += l6 * j9;
            if (i7 < k7) {
                y3 -= y1;
                y1 -= y2;
                y2 = anIntArray1472[y2];
                while (--y1 >= 0) {
                    drawMaterializedScanline(DrawingArea.pixels, texels, y2, x3 >> 16, x2 >> 16, hsl3 >> 7, hsl2 >> 7,
                            l4, k5, j6, i5, l5, k6, z2, depthSlope);
                    x3 += i7;
                    x2 += k7;
                    z2 += depthScale;
                    hsl3 += j7;
                    hsl2 += l7;
                    y2 += DrawingArea.width;
                    l4 += j5;
                    k5 += i6;
                    j6 += l6;
                }
                while (--y3 >= 0) {
                    drawMaterializedScanline(DrawingArea.pixels, texels, y2, x1 >> 16, x2 >> 16, hsl1 >> 7, hsl2 >> 7,
                            l4, k5, j6, i5, l5, k6, z2, depthSlope);
                    x1 += i8;
                    x2 += k7;
                    z2 += depthScale;
                    hsl1 += j8;
                    hsl2 += l7;
                    y2 += DrawingArea.width;
                    l4 += j5;
                    k5 += i6;
                    j6 += l6;
                }
                texels = null;
                return;
            }
            y3 -= y1;
            y1 -= y2;
            y2 = anIntArray1472[y2];
            while (--y1 >= 0) {
                drawMaterializedScanline(DrawingArea.pixels, texels, y2, x2 >> 16, x3 >> 16, hsl2 >> 7, hsl3 >> 7, l4,
                        k5, j6, i5, l5, k6, z2, depthSlope);
                x3 += i7;
                x2 += k7;
                z2 += depthScale;
                hsl3 += j7;
                hsl2 += l7;
                y2 += DrawingArea.width;
                l4 += j5;
                k5 += i6;
                j6 += l6;
            }
            while (--y3 >= 0) {
                drawMaterializedScanline(DrawingArea.pixels, texels, y2, x2 >> 16, x1 >> 16, hsl2 >> 7, hsl1 >> 7, l4,
                        k5, j6, i5, l5, k6, z2, depthSlope);
                x1 += i8;
                x2 += k7;
                z2 += depthScale;
                hsl1 += j8;
                hsl2 += l7;
                y2 += DrawingArea.width;
                l4 += j5;
                k5 += i6;
                j6 += l6;
            }
            texels = null;
            return;
        }
        if (y3 >= DrawingArea.bottomY) {
            return;
        }
        if (y1 > DrawingArea.bottomY) {
            y1 = DrawingArea.bottomY;
        }
        if (y2 > DrawingArea.bottomY) {
            y2 = DrawingArea.bottomY;
        }
        z3 = z3 - depthSlope * x3 + depthSlope;
        if (y1 < y2) {
            x2 = x3 <<= 16;
            hsl2 = hsl3 <<= 15;
            if (y3 < 0) {
                x2 -= k7 * y3;
                x3 -= i8 * y3;
                z3 -= depthScale * y3;
                hsl2 -= l7 * y3;
                hsl3 -= j8 * y3;
                y3 = 0;
            }
            x1 <<= 16;
            hsl1 <<= 15;
            if (y1 < 0) {
                x1 -= i7 * y1;
                hsl1 -= j7 * y1;
                y1 = 0;
            }
            int k9 = y3 - textureInt2;
            l4 += j5 * k9;
            k5 += i6 * k9;
            j6 += l6 * k9;
            if (k7 < i8) {
                y2 -= y1;
                y1 -= y3;
                y3 = anIntArray1472[y3];
                while (--y1 >= 0) {
                    drawMaterializedScanline(DrawingArea.pixels, texels, y3, x2 >> 16, x3 >> 16, hsl2 >> 7, hsl3 >> 7,
                            l4, k5, j6, i5, l5, k6, z3, depthSlope);
                    x2 += k7;
                    x3 += i8;
                    z3 += depthScale;
                    hsl2 += l7;
                    hsl3 += j8;
                    y3 += DrawingArea.width;
                    l4 += j5;
                    k5 += i6;
                    j6 += l6;
                }
                while (--y2 >= 0) {
                    drawMaterializedScanline(DrawingArea.pixels, texels, y3, x2 >> 16, x1 >> 16, hsl2 >> 7, hsl1 >> 7,
                            l4, k5, j6, i5, l5, k6, z3, depthSlope);
                    x2 += k7;
                    x1 += i7;
                    z3 += depthScale;
                    hsl2 += l7;
                    hsl1 += j7;
                    y3 += DrawingArea.width;
                    l4 += j5;
                    k5 += i6;
                    j6 += l6;
                }
                texels = null;
                return;
            }
            y2 -= y1;
            y1 -= y3;
            y3 = anIntArray1472[y3];
            while (--y1 >= 0) {
                drawMaterializedScanline(DrawingArea.pixels, texels, y3, x3 >> 16, x2 >> 16, hsl3 >> 7, hsl2 >> 7, l4,
                        k5, j6, i5, l5, k6, z3, depthSlope);
                x2 += k7;
                x3 += i8;
                z3 += depthScale;
                hsl2 += l7;
                hsl3 += j8;
                y3 += DrawingArea.width;
                l4 += j5;
                k5 += i6;
                j6 += l6;
            }
            while (--y2 >= 0) {
                drawMaterializedScanline(DrawingArea.pixels, texels, y3, x1 >> 16, x2 >> 16, hsl1 >> 7, hsl2 >> 7, l4,
                        k5, j6, i5, l5, k6, z3, depthSlope);
                x2 += k7;
                x1 += i7;
                z3 += depthScale;
                hsl2 += l7;
                hsl1 += j7;
                y3 += DrawingArea.width;
                l4 += j5;
                k5 += i6;
                j6 += l6;
            }
            texels = null;
            return;
        }
        x1 = x3 <<= 16;
        hsl1 = hsl3 <<= 15;
        if (y3 < 0) {
            x1 -= k7 * y3;
            x3 -= i8 * y3;
            z3 -= depthScale * y3;
            hsl1 -= l7 * y3;
            hsl3 -= j8 * y3;
            y3 = 0;
        }
        x2 <<= 16;
        hsl2 <<= 15;
        if (y2 < 0) {
            x2 -= i7 * y2;
            hsl2 -= j7 * y2;
            y2 = 0;
        }
        int l9 = y3 - textureInt2;
        l4 += j5 * l9;
        k5 += i6 * l9;
        j6 += l6 * l9;
        if (k7 < i8) {
            y1 -= y2;
            y2 -= y3;
            y3 = anIntArray1472[y3];
            while (--y2 >= 0) {
                drawMaterializedScanline(DrawingArea.pixels, texels, y3, x1 >> 16, x3 >> 16, hsl1 >> 7, hsl3 >> 7, l4,
                        k5, j6, i5, l5, k6, z3, depthSlope);
                x1 += k7;
                x3 += i8;
                z3 += depthScale;
                hsl1 += l7;
                hsl3 += j8;
                y3 += DrawingArea.width;
                l4 += j5;
                k5 += i6;
                j6 += l6;
            }
            while (--y1 >= 0) {
                drawMaterializedScanline(DrawingArea.pixels, texels, y3, x2 >> 16, x3 >> 16, hsl2 >> 7, hsl3 >> 7, l4,
                        k5, j6, i5, l5, k6, z3, depthSlope);
                x2 += i7;
                x3 += i8;
                z3 += depthScale;
                hsl2 += j7;
                hsl3 += j8;
                y3 += DrawingArea.width;
                l4 += j5;
                k5 += i6;
                j6 += l6;
            }
            texels = null;
            return;
        }
        y1 -= y2;
        y2 -= y3;
        y3 = anIntArray1472[y3];
        while (--y2 >= 0) {
            drawMaterializedScanline(DrawingArea.pixels, texels, y3, x3 >> 16, x1 >> 16, hsl3 >> 7, hsl1 >> 7, l4, k5,
                    j6, i5, l5, k6, z3, depthSlope);
            x1 += k7;
            x3 += i8;
            z3 += depthScale;
            hsl1 += l7;
            hsl3 += j8;
            y3 += DrawingArea.width;
            l4 += j5;
            k5 += i6;
            j6 += l6;
        }
        while (--y1 >= 0) {
            drawMaterializedScanline(DrawingArea.pixels, texels, y3, x3 >> 16, x2 >> 16, hsl3 >> 7, hsl2 >> 7, l4, k5,
                    j6, i5, l5, k6, z3, depthSlope);
            x2 += i7;
            x3 += i8;
            z3 += depthScale;
            hsl2 += j7;
            hsl3 += j8;
            y3 += DrawingArea.width;
            l4 += j5;
            k5 += i6;
            j6 += l6;
        }
        texels = null;
    }

    private static final void drawMaterializedScanline(int[] dest, int[] texels, int offset, int x1, int x2, int hsl1,
                                                       int hsl2, int t1, int t2, int t3, int t4, int t5, int t6, float z1, float z2) {
        if (x2 <= x1) {
            return;
        }
        int texPos = 0;
        int rgb = 0;
        if (aBoolean1462) {
            if (x2 > DrawingArea.centerX) {
                x2 = DrawingArea.centerX;
            }
            if (x1 < 0) {
                x1 = 0;
            }
        }
        if (x1 < x2) {
            offset += x1;
            z1 += z2 * (float) x1;
            int n = x2 - x1 >> 2;
            int dhsl = 0;
            if (n > 0) {
                dhsl = (hsl2 - hsl1) * anIntArray1468[n] >> 15;
            }
            int dist = x1 - textureInt1;
            t1 += (t4 >> 3) * dist;
            t2 += (t5 >> 3) * dist;
            t3 += (t6 >> 3) * dist;
            int i_57_ = t3 >> 14;
            int i_58_;
            int i_59_;
            if (i_57_ != 0) {
                i_58_ = t1 / i_57_;
                i_59_ = t2 / i_57_;
            } else {
                i_58_ = 0;
                i_59_ = 0;
            }
            t1 += t4;
            t2 += t5;
            t3 += t6;
            i_57_ = t3 >> 14;
            int i_60_;
            int i_61_;
            if (i_57_ != 0) {
                i_60_ = t1 / i_57_;
                i_61_ = t2 / i_57_;
            } else {
                i_60_ = 0;
                i_61_ = 0;
            }
            texPos = (i_58_ << 18) + i_59_;
            int dtexPos = (i_60_ - i_58_ >> 3 << 18) + (i_61_ - i_59_ >> 3);
            n >>= 1;
            int light;
            if (n > 0) {
                do {
                    hsl1 += dhsl;
                    rgb = texels[texelPos((texPos & 0x3f80) + (texPos >>> 25))];
                    light = ((hsl1 >> 8 & 0x7f) << 1) * (((rgb >> 16 & 0xff) + (rgb >> 8 & 0xff) + (rgb & 0xff)) / 3)
                            / 384;
                    if (light > 127) {
                        light = 127;
                    }
                    texPos += dtexPos;
                    dest[offset] = hslToRgb[(hsl1 >> 8 & 0xff80) | light];
                    if (saveDepth) {
                        depthBuffer[offset] = z1;
                    }
                    z1 += z2;
                    offset++;
                    rgb = texels[texelPos((texPos & 0x3f80) + (texPos >>> 25))];
                    light = ((hsl1 >> 8 & 0x7f) << 1) * (((rgb >> 16 & 0xff) + (rgb >> 8 & 0xff) + (rgb & 0xff)) / 3)
                            / 384;
                    if (light > 127) {
                        light = 127;
                    }
                    texPos += dtexPos;
                    dest[offset] = hslToRgb[(hsl1 >> 8 & 0xff80) | light];
                    if (saveDepth) {
                        depthBuffer[offset] = z1;
                    }
                    z1 += z2;
                    offset++;
                    rgb = texels[texelPos((texPos & 0x3f80) + (texPos >>> 25))];
                    light = ((hsl1 >> 8 & 0x7f) << 1) * (((rgb >> 16 & 0xff) + (rgb >> 8 & 0xff) + (rgb & 0xff)) / 3)
                            / 384;
                    if (light > 127) {
                        light = 127;
                    }
                    texPos += dtexPos;
                    dest[offset] = hslToRgb[(hsl1 >> 8 & 0xff80) | light];
                    if (saveDepth) {
                        depthBuffer[offset] = z1;
                    }
                    z1 += z2;
                    offset++;
                    rgb = texels[texelPos((texPos & 0x3f80) + (texPos >>> 25))];
                    light = ((hsl1 >> 8 & 0x7f) << 1) * (((rgb >> 16 & 0xff) + (rgb >> 8 & 0xff) + (rgb & 0xff)) / 3)
                            / 384;
                    if (light > 127) {
                        light = 127;
                    }
                    texPos += dtexPos;
                    dest[offset] = hslToRgb[(hsl1 >> 8 & 0xff80) | light];
                    if (saveDepth) {
                        depthBuffer[offset] = z1;
                    }
                    z1 += z2;
                    offset++;
                    hsl1 += dhsl;
                    rgb = texels[texelPos((texPos & 0x3f80) + (texPos >>> 25))];
                    light = ((hsl1 >> 8 & 0x7f) << 1) * (((rgb >> 16 & 0xff) + (rgb >> 8 & 0xff) + (rgb & 0xff)) / 3)
                            / 384;
                    if (light > 127) {
                        light = 127;
                    }
                    texPos += dtexPos;
                    dest[offset] = hslToRgb[(hsl1 >> 8 & 0xff80) | light];
                    if (saveDepth) {
                        depthBuffer[offset] = z1;
                    }
                    z1 += z2;
                    offset++;
                    rgb = texels[texelPos((texPos & 0x3f80) + (texPos >>> 25))];
                    light = ((hsl1 >> 8 & 0x7f) << 1) * (((rgb >> 16 & 0xff) + (rgb >> 8 & 0xff) + (rgb & 0xff)) / 3)
                            / 384;
                    if (light > 127) {
                        light = 127;
                    }
                    texPos += dtexPos;
                    dest[offset] = hslToRgb[(hsl1 >> 8 & 0xff80) | light];
                    if (saveDepth) {
                        depthBuffer[offset] = z1;
                    }
                    z1 += z2;
                    offset++;
                    rgb = texels[texelPos((texPos & 0x3f80) + (texPos >>> 25))];
                    light = ((hsl1 >> 8 & 0x7f) << 1) * (((rgb >> 16 & 0xff) + (rgb >> 8 & 0xff) + (rgb & 0xff)) / 3)
                            / 384;
                    if (light > 127) {
                        light = 127;
                    }
                    texPos += dtexPos;
                    dest[offset] = hslToRgb[(hsl1 >> 8 & 0xff80) | light];
                    if (saveDepth) {
                        depthBuffer[offset] = z1;
                    }
                    z1 += z2;
                    offset++;
                    rgb = texels[texelPos((texPos & 0x3f80) + (texPos >>> 25))];
                    light = ((hsl1 >> 8 & 0x7f) << 1) * (((rgb >> 16 & 0xff) + (rgb >> 8 & 0xff) + (rgb & 0xff)) / 3)
                            / 384;
                    if (light > 127) {
                        light = 127;
                    }
                    texPos += dtexPos;
                    dest[offset] = hslToRgb[(hsl1 >> 8 & 0xff80) | light];
                    if (saveDepth) {
                        depthBuffer[offset] = z1;
                    }
                    z1 += z2;
                    offset++;
                    i_58_ = i_60_;
                    i_59_ = i_61_;
                    t1 += t4;
                    t2 += t5;
                    t3 += t6;
                    i_57_ = t3 >> 14;
                    if (i_57_ != 0) {
                        i_60_ = t1 / i_57_;
                        i_61_ = t2 / i_57_;
                    } else {
                        i_60_ = 0;
                        i_61_ = 0;
                    }
                    texPos = (i_58_ << 18) + i_59_;
                    dtexPos = (i_60_ - i_58_ >> 3 << 18) + (i_61_ - i_59_ >> 3);
                } while (--n > 0);
            }
            n = x2 - x1 & 7;
            if (n > 0) {
                do {
                    if ((n & 3) == 0) {
                        hsl1 += dhsl;
                    }
                    rgb = texels[texelPos((texPos & 0x3f80) + (texPos >>> 25))];
                    light = ((hsl1 >> 8 & 0x7f) << 1) * (((rgb >> 16 & 0xff) + (rgb >> 8 & 0xff) + (rgb & 0xff)) / 3)
                            / 384;
                    if (light > 127) {
                        light = 127;
                    }
                    texPos += dtexPos;
                    dest[offset] = hslToRgb[(hsl1 >> 8 & 0xff80) | light];
                    if (saveDepth) {
                        depthBuffer[offset] = z1;
                    }
                    z1 += z2;
                    offset++;
                } while (--n > 0);
            }
        }
        texels = dest = null;
    }

    public static void drawGouraudTriangle(int y1, int y2, int y3, int x1, int x2, int x3, int hsl1, int hsl2, int hsl3,
    		float z1, float z2, float z3) {
        if (Configuration.enableSmoothShading && aBoolean1464) {
            drawHDGouraudTriangle(y1, y2, y3, x1, x2, x3, hsl1, hsl2, hsl3, z1, z2, z3);
        } else {
            drawLDGouraudTriangle(y1, y2, y3, x1, x2, x3, hsl1, hsl2, hsl3, z1, z2, z3);
        }
    }

    public static void drawLDGouraudTriangle(int y1, int y2, int y3, int x1, int x2, int x3, int hsl1, int hsl2,
                                             int hsl3, float z1, float z2, float z3) {
        if (!saveDepth) {
            z1 = z2 = z3 = 0;
        }
        int dx1 = 0;
        int dhsl1 = 0;
        if (y2 != y1) {
            dx1 = (x2 - x1 << 16) / (y2 - y1);
            dhsl1 = (hsl2 - hsl1 << 15) / (y2 - y1);
        }
        int dx2 = 0;
        int dhsl2 = 0;
        if (y3 != y2) {
            dx2 = (x3 - x2 << 16) / (y3 - y2);
            dhsl2 = (hsl3 - hsl2 << 15) / (y3 - y2);
        }
        int dx3 = 0;
        int dhsl3 = 0;
        if (y3 != y1) {
            dx3 = (x1 - x3 << 16) / (y1 - y3);
            dhsl3 = (hsl1 - hsl3 << 15) / (y1 - y3);
        }

        float x21 = x2 - x1;
        float y32 = y2 - y1;
        float x31 = x3 - x1;
        float y31 = y3 - y1;
        float z21 = z2 - z1;
        float z31 = z3 - z1;

        float div = x21 * y31 - x31 * y32;
        float depthSlope = (z21 * y31 - z31 * y32) / div;
        float depthScale = (z31 * x21 - z21 * x31) / div;

        if (y1 <= y2 && y1 <= y3) {
            if (y1 >= DrawingArea.bottomY) {
                return;
            }
            if (y2 > DrawingArea.bottomY) {
                y2 = DrawingArea.bottomY;
            }
            if (y3 > DrawingArea.bottomY) {
                y3 = DrawingArea.bottomY;
            }
            z1 = z1 - depthSlope * x1 + depthSlope;
            if (y2 < y3) {
                x3 = x1 <<= 16;
                hsl3 = hsl1 <<= 15;
                if (y1 < 0) {
                    y1 -= 0;
                    x3 -= dx3 * y1;
                    x1 -= dx1 * y1;
                    z1 -= depthScale * y1;
                    hsl3 -= dhsl3 * y1;
                    hsl1 -= dhsl1 * y1;
                    y1 = 0;
                }
                x2 <<= 16;
                hsl2 <<= 15;
                if (y2 < 0) {
                    y2 -= 0;
                    x2 -= dx2 * y2;
                    hsl2 -= dhsl2 * y2;
                    y2 = 0;
                }
                if (y1 != y2 && dx3 < dx1 || y1 == y2 && dx3 > dx2) {
                    y3 -= y2;
                    y2 -= y1;
                    for (y1 = anIntArray1472[y1]; --y2 >= 0; y1 += DrawingArea.width) {
                        drawLDGouraudScanline(DrawingArea.pixels, y1, x3 >> 16, x1 >> 16, hsl3 >> 7, hsl1 >> 7, z1,
                                depthSlope);
                        z1 += depthScale;
                        x3 += dx3;
                        x1 += dx1;
                        hsl3 += dhsl3;
                        hsl1 += dhsl1;
                    }
                    while (--y3 >= 0) {
                        drawLDGouraudScanline(DrawingArea.pixels, y1, x3 >> 16, x2 >> 16, hsl3 >> 7, hsl2 >> 7, z1,
                                depthSlope);
                        z1 += depthScale;
                        x3 += dx3;
                        x2 += dx2;
                        hsl3 += dhsl3;
                        hsl2 += dhsl2;
                        y1 += DrawingArea.width;
                    }
                    return;
                }
                y3 -= y2;
                y2 -= y1;
                for (y1 = anIntArray1472[y1]; --y2 >= 0; y1 += DrawingArea.width) {
                    drawLDGouraudScanline(DrawingArea.pixels, y1, x1 >> 16, x3 >> 16, hsl1 >> 7, hsl3 >> 7, z1,
                            depthSlope);
                    z1 += depthScale;
                    x3 += dx3;
                    x1 += dx1;
                    hsl3 += dhsl3;
                    hsl1 += dhsl1;
                }
                while (--y3 >= 0) {
                    drawLDGouraudScanline(DrawingArea.pixels, y1, x2 >> 16, x3 >> 16, hsl2 >> 7, hsl3 >> 7, z1,
                            depthSlope);
                    z1 += depthScale;
                    x3 += dx3;
                    x2 += dx2;
                    hsl3 += dhsl3;
                    hsl2 += dhsl2;
                    y1 += DrawingArea.width;
                }
                return;
            }
            x2 = x1 <<= 16;
            hsl2 = hsl1 <<= 15;
            if (y1 < 0) {
                y1 -= 0;
                x2 -= dx3 * y1;
                x1 -= dx1 * y1;
                z1 -= depthScale * y1;
                hsl2 -= dhsl3 * y1;
                hsl1 -= dhsl1 * y1;
                y1 = 0;
            }
            x3 <<= 16;
            hsl3 <<= 15;
            if (y3 < 0) {
                y3 -= 0;
                x3 -= dx2 * y3;
                hsl3 -= dhsl2 * y3;
                y3 = 0;
            }
            if (y1 != y3 && dx3 < dx1 || y1 == y3 && dx2 > dx1) {
                y2 -= y3;
                y3 -= y1;
                for (y1 = anIntArray1472[y1]; --y3 >= 0; y1 += DrawingArea.width) {
                    drawLDGouraudScanline(DrawingArea.pixels, y1, x2 >> 16, x1 >> 16, hsl2 >> 7, hsl1 >> 7, z1,
                            depthSlope);
                    z1 += depthScale;
                    x2 += dx3;
                    x1 += dx1;
                    hsl2 += dhsl3;
                    hsl1 += dhsl1;
                }
                while (--y2 >= 0) {
                    drawLDGouraudScanline(DrawingArea.pixels, y1, x3 >> 16, x1 >> 16, hsl3 >> 7, hsl1 >> 7, z1,
                            depthSlope);
                    z1 += depthScale;
                    x3 += dx2;
                    x1 += dx1;
                    hsl3 += dhsl2;
                    hsl1 += dhsl1;
                    y1 += DrawingArea.width;
                }
                return;
            }
            y2 -= y3;
            y3 -= y1;
            for (y1 = anIntArray1472[y1]; --y3 >= 0; y1 += DrawingArea.width) {
                drawLDGouraudScanline(DrawingArea.pixels, y1, x1 >> 16, x2 >> 16, hsl1 >> 7, hsl2 >> 7, z1, depthSlope);
                z1 += depthScale;
                x2 += dx3;
                x1 += dx1;
                hsl2 += dhsl3;
                hsl1 += dhsl1;
            }
            while (--y2 >= 0) {
                drawLDGouraudScanline(DrawingArea.pixels, y1, x1 >> 16, x3 >> 16, hsl1 >> 7, hsl3 >> 7, z1, depthSlope);
                z1 += depthScale;
                x3 += dx2;
                x1 += dx1;
                hsl3 += dhsl2;
                hsl1 += dhsl1;
                y1 += DrawingArea.width;
            }
            return;
        }
        if (y2 <= y3) {
            if (y2 >= DrawingArea.bottomY) {
                return;
            }
            if (y3 > DrawingArea.bottomY) {
                y3 = DrawingArea.bottomY;
            }
            if (y1 > DrawingArea.bottomY) {
                y1 = DrawingArea.bottomY;
            }
            z2 = z2 - depthSlope * x2 + depthSlope;
            if (y3 < y1) {
                x1 = x2 <<= 16;
                hsl1 = hsl2 <<= 15;
                if (y2 < 0) {
                    y2 -= 0;
                    x1 -= dx1 * y2;
                    x2 -= dx2 * y2;
                    z1 -= depthScale * y2;
                    hsl1 -= dhsl1 * y2;
                    hsl2 -= dhsl2 * y2;
                    y2 = 0;
                }
                x3 <<= 16;
                hsl3 <<= 15;
                if (y3 < 0) {
                    y3 -= 0;
                    x3 -= dx3 * y3;
                    hsl3 -= dhsl3 * y3;
                    y3 = 0;
                }
                if (y2 != y3 && dx1 < dx2 || y2 == y3 && dx1 > dx3) {
                    y1 -= y3;
                    y3 -= y2;
                    for (y2 = anIntArray1472[y2]; --y3 >= 0; y2 += DrawingArea.width) {
                        drawLDGouraudScanline(DrawingArea.pixels, y2, x1 >> 16, x2 >> 16, hsl1 >> 7, hsl2 >> 7, z2,
                                depthSlope);
                        z2 += depthScale;
                        x1 += dx1;
                        x2 += dx2;
                        hsl1 += dhsl1;
                        hsl2 += dhsl2;
                    }

                    while (--y1 >= 0) {
                        drawLDGouraudScanline(DrawingArea.pixels, y2, x1 >> 16, x3 >> 16, hsl1 >> 7, hsl3 >> 7, z2,
                                depthSlope);
                        z2 += depthScale;
                        x1 += dx1;
                        x3 += dx3;
                        hsl1 += dhsl1;
                        hsl3 += dhsl3;
                        y2 += DrawingArea.width;
                    }
                    return;
                }
                y1 -= y3;
                y3 -= y2;
                for (y2 = anIntArray1472[y2]; --y3 >= 0; y2 += DrawingArea.width) {
                    drawLDGouraudScanline(DrawingArea.pixels, y2, x2 >> 16, x1 >> 16, hsl2 >> 7, hsl1 >> 7, z2,
                            depthSlope);
                    z2 += depthScale;
                    x1 += dx1;
                    x2 += dx2;
                    hsl1 += dhsl1;
                    hsl2 += dhsl2;
                }

                while (--y1 >= 0) {
                    drawLDGouraudScanline(DrawingArea.pixels, y2, x3 >> 16, x1 >> 16, hsl3 >> 7, hsl1 >> 7, z2,
                            depthSlope);
                    z2 += depthScale;
                    x1 += dx1;
                    x3 += dx3;
                    hsl1 += dhsl1;
                    hsl3 += dhsl3;
                    y2 += DrawingArea.width;
                }
                return;
            }
            x3 = x2 <<= 16;
            hsl3 = hsl2 <<= 15;
            if (y2 < 0) {
                y2 -= 0;
                x3 -= dx1 * y2;
                x2 -= dx2 * y2;
                z2 -= depthScale * y2;
                hsl3 -= dhsl1 * y2;
                hsl2 -= dhsl2 * y2;
                y2 = 0;
            }
            x1 <<= 16;
            hsl1 <<= 15;
            if (y1 < 0) {
                y1 -= 0;
                x1 -= dx3 * y1;
                hsl1 -= dhsl3 * y1;
                y1 = 0;
            }
            if (dx1 < dx2) {
                y3 -= y1;
                y1 -= y2;
                for (y2 = anIntArray1472[y2]; --y1 >= 0; y2 += DrawingArea.width) {
                    drawLDGouraudScanline(DrawingArea.pixels, y2, x3 >> 16, x2 >> 16, hsl3 >> 7, hsl2 >> 7, z2,
                            depthSlope);
                    z2 += depthScale;
                    x3 += dx1;
                    x2 += dx2;
                    hsl3 += dhsl1;
                    hsl2 += dhsl2;
                }
                while (--y3 >= 0) {
                    drawLDGouraudScanline(DrawingArea.pixels, y2, x1 >> 16, x2 >> 16, hsl1 >> 7, hsl2 >> 7, z2,
                            depthSlope);
                    z2 += depthScale;
                    x1 += dx3;
                    x2 += dx2;
                    hsl1 += dhsl3;
                    hsl2 += dhsl2;
                    y2 += DrawingArea.width;
                }
                return;
            }
            y3 -= y1;
            y1 -= y2;
            for (y2 = anIntArray1472[y2]; --y1 >= 0; y2 += DrawingArea.width) {
                drawLDGouraudScanline(DrawingArea.pixels, y2, x2 >> 16, x3 >> 16, hsl2 >> 7, hsl3 >> 7, z2, depthSlope);
                z2 += depthScale;
                x3 += dx1;
                x2 += dx2;
                hsl3 += dhsl1;
                hsl2 += dhsl2;
            }

            while (--y3 >= 0) {
                drawLDGouraudScanline(DrawingArea.pixels, y2, x2 >> 16, x1 >> 16, hsl2 >> 7, hsl1 >> 7, z2, depthSlope);
                z2 += depthScale;
                x1 += dx3;
                x2 += dx2;
                hsl1 += dhsl3;
                hsl2 += dhsl2;
                y2 += DrawingArea.width;
            }
            return;
        }
        if (y3 >= DrawingArea.bottomY) {
            return;
        }
        if (y1 > DrawingArea.bottomY) {
            y1 = DrawingArea.bottomY;
        }
        if (y2 > DrawingArea.bottomY) {
            y2 = DrawingArea.bottomY;
        }
        z3 = z3 - depthSlope * x3 + depthSlope;
        if (y1 < y2) {
            x2 = x3 <<= 16;
            hsl2 = hsl3 <<= 15;
            if (y3 < 0) {
                y3 -= 0;
                x2 -= dx2 * y3;
                x3 -= dx3 * y3;
                z3 -= depthScale * y3;
                hsl2 -= dhsl2 * y3;
                hsl3 -= dhsl3 * y3;
                y3 = 0;
            }
            x1 <<= 16;
            hsl1 <<= 15;
            if (y1 < 0) {
                y1 -= 0;
                x1 -= dx1 * y1;
                hsl1 -= dhsl1 * y1;
                y1 = 0;
            }
            if (dx2 < dx3) {
                y2 -= y1;
                y1 -= y3;
                for (y3 = anIntArray1472[y3]; --y1 >= 0; y3 += DrawingArea.width) {
                    drawLDGouraudScanline(DrawingArea.pixels, y3, x2 >> 16, x3 >> 16, hsl2 >> 7, hsl3 >> 7, z3,
                            depthSlope);
                    z3 += depthScale;
                    x2 += dx2;
                    x3 += dx3;
                    hsl2 += dhsl2;
                    hsl3 += dhsl3;
                }
                while (--y2 >= 0) {
                    drawLDGouraudScanline(DrawingArea.pixels, y3, x2 >> 16, x1 >> 16, hsl2 >> 7, hsl1 >> 7, z3,
                            depthSlope);
                    z3 += depthScale;
                    x2 += dx2;
                    x1 += dx1;
                    hsl2 += dhsl2;
                    hsl1 += dhsl1;
                    y3 += DrawingArea.width;
                }
                return;
            }
            y2 -= y1;
            y1 -= y3;
            for (y3 = anIntArray1472[y3]; --y1 >= 0; y3 += DrawingArea.width) {
                drawLDGouraudScanline(DrawingArea.pixels, y3, x3 >> 16, x2 >> 16, hsl3 >> 7, hsl2 >> 7, z3, depthSlope);
                z3 += depthScale;
                x2 += dx2;
                x3 += dx3;
                hsl2 += dhsl2;
                hsl3 += dhsl3;
            }

            while (--y2 >= 0) {
                drawLDGouraudScanline(DrawingArea.pixels, y3, x1 >> 16, x2 >> 16, hsl1 >> 7, hsl2 >> 7, z3, depthSlope);
                z3 += depthScale;
                x2 += dx2;
                x1 += dx1;
                hsl2 += dhsl2;
                hsl1 += dhsl1;
                y3 += DrawingArea.width;
            }
            return;
        }
        x1 = x3 <<= 16;
        hsl1 = hsl3 <<= 15;
        if (y3 < 0) {
            y3 -= 0;
            x1 -= dx2 * y3;
            x3 -= dx3 * y3;
            z3 -= depthScale * y3;
            hsl1 -= dhsl2 * y3;
            hsl3 -= dhsl3 * y3;
            y3 = 0;
        }
        x2 <<= 16;
        hsl2 <<= 15;
        if (y2 < 0) {
            y2 -= 0;
            x2 -= dx1 * y2;
            hsl2 -= dhsl1 * y2;
            y2 = 0;
        }
        if (dx2 < dx3) {
            y1 -= y2;
            y2 -= y3;
            for (y3 = anIntArray1472[y3]; --y2 >= 0; y3 += DrawingArea.width) {
                drawLDGouraudScanline(DrawingArea.pixels, y3, x1 >> 16, x3 >> 16, hsl1 >> 7, hsl3 >> 7, z3, depthSlope);
                z3 += depthScale;
                x1 += dx2;
                x3 += dx3;
                hsl1 += dhsl2;
                hsl3 += dhsl3;
            }
            while (--y1 >= 0) {
                drawLDGouraudScanline(DrawingArea.pixels, y3, x2 >> 16, x3 >> 16, hsl2 >> 7, hsl3 >> 7, z3, depthSlope);
                z3 += depthScale;
                x2 += dx1;
                x3 += dx3;
                hsl2 += dhsl1;
                hsl3 += dhsl3;
                y3 += DrawingArea.width;
            }
            return;
        }
        y1 -= y2;
        y2 -= y3;
        for (y3 = anIntArray1472[y3]; --y2 >= 0; y3 += DrawingArea.width) {
            drawLDGouraudScanline(DrawingArea.pixels, y3, x3 >> 16, x1 >> 16, hsl3 >> 7, hsl1 >> 7, z3, depthSlope);
            z3 += depthScale;
            x1 += dx2;
            x3 += dx3;
            hsl1 += dhsl2;
            hsl3 += dhsl3;
        }
        while (--y1 >= 0) {
            drawLDGouraudScanline(DrawingArea.pixels, y3, x3 >> 16, x2 >> 16, hsl3 >> 7, hsl2 >> 7, z3, depthSlope);
            z3 += depthScale;
            x2 += dx1;
            x3 += dx3;
            hsl2 += dhsl1;
            hsl3 += dhsl3;
            y3 += DrawingArea.width;
        }
    }

    private static void drawLDGouraudScanline(int dest[], int offset, int x1, int x2, int hsl1, int hsl2, float z1,
                                              float z2) {
        int rgb;
        int div;
        int dhsl;
        if (aBoolean1464) {
            if (aBoolean1462) {
                if (x2 - x1 > 3) {
                    dhsl = (hsl2 - hsl1) / (x2 - x1);
                } else {
                    dhsl = 0;
                }
                if (x2 > DrawingArea.centerX) {
                    x2 = DrawingArea.centerX;
                }
                if (x1 < 0) {
                    hsl1 -= x1 * dhsl;
                    x1 = 0;
                }
                if (x1 >= x2) {
                    return;
                }
                offset += x1 - 1;
                div = x2 - x1 >> 2;
                z1 += z2 * x1;
                dhsl <<= 2;
            } else {
                if (x1 >= x2) {
                    return;
                }
                offset += x1 - 1;
                div = x2 - x1 >> 2;
                z1 += z2 * x1;
                if (div > 0) {
                    dhsl = (hsl2 - hsl1) * anIntArray1468[div] >> 15;
                } else {
                    dhsl = 0;
                }
            }
            if (anInt1465 == 0) {
                while (--div >= 0) {
                    rgb = hslToRgb[hsl1 >> 8];
                    hsl1 += dhsl;
                    offset++;
                    dest[offset] = rgb;
                    if (saveDepth) {
                        depthBuffer[offset] = z1;
                    }
                    z1 += z2;
                    offset++;
                    dest[offset] = rgb;
                    if (saveDepth) {
                        depthBuffer[offset] = z1;
                    }
                    z1 += z2;
                    offset++;
                    dest[offset] = rgb;
                    if (saveDepth) {
                        depthBuffer[offset] = z1;
                    }
                    z1 += z2;
                    offset++;
                    dest[offset] = rgb;
                    if (saveDepth) {
                        depthBuffer[offset] = z1;
                    }
                    z1 += z2;
                }
                div = x2 - x1 & 3;
                if (div > 0) {
                    rgb = hslToRgb[hsl1 >> 8];
                    do {
                        offset++;
                        dest[offset] = rgb;
                        if (saveDepth) {
                            depthBuffer[offset] = z1;
                        }
                        z1 += z2;
                    } while (--div > 0);
                    return;
                }
            } else {
                int a1 = anInt1465;
                int a2 = 256 - anInt1465;
                while (--div >= 0) {
                    rgb = hslToRgb[hsl1 >> 8];
                    hsl1 += dhsl;
                    rgb = ((rgb & 0xff00ff) * a2 >> 8 & 0xff00ff) + ((rgb & 0xff00) * a2 >> 8 & 0xff00);
                    dest[offset] = rgb + ((dest[offset] & 0xff00ff) * a1 >> 8 & 0xff00ff)
                            + ((dest[offset] & 0xff00) * a1 >> 8 & 0xff00);
                    if (saveDepth) {
                        depthBuffer[offset] = z1;
                    }
                    offset++;
                    z1 += z2;
                    dest[offset] = rgb + ((dest[offset] & 0xff00ff) * a1 >> 8 & 0xff00ff)
                            + ((dest[offset] & 0xff00) * a1 >> 8 & 0xff00);
                    if (saveDepth) {
                        depthBuffer[offset] = z1;
                    }
                    offset++;
                    z1 += z2;
                    dest[offset] = rgb + ((dest[offset] & 0xff00ff) * a1 >> 8 & 0xff00ff)
                            + ((dest[offset] & 0xff00) * a1 >> 8 & 0xff00);
                    if (saveDepth) {
                        depthBuffer[offset] = z1;
                    }
                    offset++;
                    z1 += z2;
                    dest[offset] = rgb + ((dest[offset] & 0xff00ff) * a1 >> 8 & 0xff00ff)
                            + ((dest[offset] & 0xff00) * a1 >> 8 & 0xff00);
                    if (saveDepth) {
                        depthBuffer[offset] = z1;
                    }
                    offset++;
                    z1 += z2;
                }
                div = x2 - x1 & 3;
                if (div > 0) {
                    rgb = hslToRgb[hsl1 >> 8];
                    rgb = ((rgb & 0xff00ff) * a2 >> 8 & 0xff00ff) + ((rgb & 0xff00) * a2 >> 8 & 0xff00);
                    do {
                        dest[offset] = rgb + ((dest[offset] & 0xff00ff) * a1 >> 8 & 0xff00ff)
                                + ((dest[offset] & 0xff00) * a1 >> 8 & 0xff00);
                        if (saveDepth) {
                            depthBuffer[offset] = z1;
                        }
                        offset++;
                        z1 += z2;
                    } while (--div > 0);
                }
            }
            return;
        }
        if (x1 >= x2) {
            return;
        }
        int dhsl2 = (hsl2 - hsl1) / (x2 - x1);
        if (aBoolean1462) {
            if (x2 > DrawingArea.centerX) {
                x2 = DrawingArea.centerX;
            }
            if (x1 < 0) {
                hsl1 -= x1 * dhsl2;
                x1 = 0;
            }
            if (x1 >= x2) {
                return;
            }
        }
        offset += x1;
        div = x2 - x1;
        if (anInt1465 == 0) {
            do {
                dest[offset] = hslToRgb[hsl1 >> 8];
                if (saveDepth) {
                    depthBuffer[offset] = z1;
                }
                offset++;
                z1 += z2;
                hsl1 += dhsl2;
            } while (--div > 0);
            return;
        }
        int a1 = anInt1465;
        int a2 = 256 - anInt1465;
        do {
            rgb = hslToRgb[hsl1 >> 8];
            hsl1 += dhsl2;
            rgb = ((rgb & 0xff00ff) * a2 >> 8 & 0xff00ff) + ((rgb & 0xff00) * a2 >> 8 & 0xff00);
            dest[offset] = rgb + ((dest[offset] & 0xff00ff) * a1 >> 8 & 0xff00ff)
                    + ((dest[offset] & 0xff00) * a1 >> 8 & 0xff00);
            if (saveDepth) {
                depthBuffer[offset] = z1;
            }
            offset++;
            z1 += z2;
        } while (--div > 0);
    }

    public static void drawHDGouraudTriangle(int y1, int y2, int y3, int x1, int x2, int x3, int hsl1, int hsl2,
                                             int hsl3, float z1,float z2, float z3) {
        if (!saveDepth) {
            z1 = z2 = z3 = 0;
        }
        int rgb1 = hslToRgb[hsl1];
        int rgb2 = hslToRgb[hsl2];
        int rgb3 = hslToRgb[hsl3];
        int r1 = rgb1 >> 16 & 0xff;
        int g1 = rgb1 >> 8 & 0xff;
        int b1 = rgb1 & 0xff;
        int r2 = rgb2 >> 16 & 0xff;
        int g2 = rgb2 >> 8 & 0xff;
        int b2 = rgb2 & 0xff;
        int r3 = rgb3 >> 16 & 0xff;
        int g3 = rgb3 >> 8 & 0xff;
        int b3 = rgb3 & 0xff;
        int dx1 = 0;
        int dr1 = 0;
        int dg1 = 0;
        int db1 = 0;
        if (y2 != y1) {
            dx1 = (x2 - x1 << 16) / (y2 - y1);
            dr1 = (r2 - r1 << 16) / (y2 - y1);
            dg1 = (g2 - g1 << 16) / (y2 - y1);
            db1 = (b2 - b1 << 16) / (y2 - y1);
        }
        int dx2 = 0;
        int dr2 = 0;
        int dg2 = 0;
        int db2 = 0;
        if (y3 != y2) {
            dx2 = (x3 - x2 << 16) / (y3 - y2);
            dr2 = (r3 - r2 << 16) / (y3 - y2);
            dg2 = (g3 - g2 << 16) / (y3 - y2);
            db2 = (b3 - b2 << 16) / (y3 - y2);
        }
        int dx3 = 0;
        int dr3 = 0;
        int dg3 = 0;
        int db3 = 0;
        if (y3 != y1) {
            dx3 = (x1 - x3 << 16) / (y1 - y3);
            dr3 = (r1 - r3 << 16) / (y1 - y3);
            dg3 = (g1 - g3 << 16) / (y1 - y3);
            db3 = (b1 - b3 << 16) / (y1 - y3);
        }

        float x21 = x2 - x1;
        float y32 = y2 - y1;
        float x31 = x3 - x1;
        float y31 = y3 - y1;
        float z21 = z2 - z1;
        float z31 = z3 - z1;

        float div = x21 * y31 - x31 * y32;
        float depthSlope = (z21 * y31 - z31 * y32) / div;
        float depthScale = (z31 * x21 - z21 * x31) / div;

        if (y1 <= y2 && y1 <= y3) {
            if (y1 >= DrawingArea.bottomY) {
                return;
            }
            if (y2 > DrawingArea.bottomY) {
                y2 = DrawingArea.bottomY;
            }
            if (y3 > DrawingArea.bottomY) {
                y3 = DrawingArea.bottomY;
            }
            z1 = z1 - depthSlope * x1 + depthSlope;
            if (y2 < y3) {
                x3 = x1 <<= 16;
                r3 = r1 <<= 16;
                g3 = g1 <<= 16;
                b3 = b1 <<= 16;
                if (y1 < 0) {
                    x3 -= dx3 * y1;
                    x1 -= dx1 * y1;
                    r3 -= dr3 * y1;
                    g3 -= dg3 * y1;
                    b3 -= db3 * y1;
                    r1 -= dr1 * y1;
                    g1 -= dg1 * y1;
                    b1 -= db1 * y1;
                    z1 -= depthScale * y1;
                    y1 = 0;
                }
                x2 <<= 16;
                r2 <<= 16;
                g2 <<= 16;
                b2 <<= 16;
                if (y2 < 0) {
                    x2 -= dx2 * y2;
                    r2 -= dr2 * y2;
                    g2 -= dg2 * y2;
                    b2 -= db2 * y2;
                    y2 = 0;
                }
                if (y1 != y2 && dx3 < dx1 || y1 == y2 && dx3 > dx2) {
                    y3 -= y2;
                    y2 -= y1;
                    for (y1 = anIntArray1472[y1]; --y2 >= 0; y1 += DrawingArea.width) {
                        drawHDGouraudScanline(DrawingArea.pixels, y1, x3 >> 16, x1 >> 16, r3, g3, b3, r1, g1, b1, z1,
                                depthSlope);
                        x3 += dx3;
                        x1 += dx1;
                        r3 += dr3;
                        g3 += dg3;
                        b3 += db3;
                        r1 += dr1;
                        g1 += dg1;
                        b1 += db1;
                        z1 += depthScale;
                    }
                    while (--y3 >= 0) {
                        drawHDGouraudScanline(DrawingArea.pixels, y1, x3 >> 16, x2 >> 16, r3, g3, b3, r2, g2, b2, z1,
                                depthSlope);
                        x3 += dx3;
                        x2 += dx2;
                        r3 += dr3;
                        g3 += dg3;
                        b3 += db3;
                        r2 += dr2;
                        g2 += dg2;
                        b2 += db2;
                        y1 += DrawingArea.width;
                        z1 += depthScale;
                    }
                    return;
                }
                y3 -= y2;
                y2 -= y1;
                for (y1 = anIntArray1472[y1]; --y2 >= 0; y1 += DrawingArea.width) {
                    drawHDGouraudScanline(DrawingArea.pixels, y1, x1 >> 16, x3 >> 16, r1, g1, b1, r3, g3, b3, z1,
                            depthSlope);
                    x3 += dx3;
                    x1 += dx1;
                    r3 += dr3;
                    g3 += dg3;
                    b3 += db3;
                    r1 += dr1;
                    g1 += dg1;
                    b1 += db1;
                    z1 += depthScale;
                }
                while (--y3 >= 0) {
                    drawHDGouraudScanline(DrawingArea.pixels, y1, x2 >> 16, x3 >> 16, r2, g2, b2, r3, g3, b3, z1,
                            depthSlope);
                    x3 += dx3;
                    x2 += dx2;
                    r3 += dr3;
                    g3 += dg3;
                    b3 += db3;
                    r2 += dr2;
                    g2 += dg2;
                    b2 += db2;
                    y1 += DrawingArea.width;
                    z1 += depthScale;
                }
                return;
            }
            x2 = x1 <<= 16;
            r2 = r1 <<= 16;
            g2 = g1 <<= 16;
            b2 = b1 <<= 16;
            if (y1 < 0) {
                x2 -= dx3 * y1;
                x1 -= dx1 * y1;
                r2 -= dr3 * y1;
                g2 -= dg3 * y1;
                b2 -= db3 * y1;
                r1 -= dr1 * y1;
                g1 -= dg1 * y1;
                b1 -= db1 * y1;
                z1 -= depthScale * y1;
                y1 = 0;
            }
            x3 <<= 16;
            r3 <<= 16;
            g3 <<= 16;
            b3 <<= 16;
            if (y3 < 0) {
                x3 -= dx2 * y3;
                r3 -= dr2 * y3;
                g3 -= dg2 * y3;
                b3 -= db2 * y3;
                y3 = 0;
            }
            if (y1 != y3 && dx3 < dx1 || y1 == y3 && dx2 > dx1) {
                y2 -= y3;
                y3 -= y1;
                for (y1 = anIntArray1472[y1]; --y3 >= 0; y1 += DrawingArea.width) {
                    drawHDGouraudScanline(DrawingArea.pixels, y1, x2 >> 16, x1 >> 16, r2, g2, b2, r1, g1, b1, z1,
                            depthSlope);
                    x2 += dx3;
                    x1 += dx1;
                    r2 += dr3;
                    g2 += dg3;
                    b2 += db3;
                    r1 += dr1;
                    g1 += dg1;
                    b1 += db1;
                    z1 += depthScale;
                }
                while (--y2 >= 0) {
                    drawHDGouraudScanline(DrawingArea.pixels, y1, x3 >> 16, x1 >> 16, r3, g3, b3, r1, g1, b1, z1,
                            depthSlope);
                    x3 += dx2;
                    x1 += dx1;
                    r3 += dr2;
                    g3 += dg2;
                    b3 += db2;
                    r1 += dr1;
                    g1 += dg1;
                    b1 += db1;
                    y1 += DrawingArea.width;
                    z1 += depthScale;
                }
                return;
            }
            y2 -= y3;
            y3 -= y1;
            for (y1 = anIntArray1472[y1]; --y3 >= 0; y1 += DrawingArea.width) {
                drawHDGouraudScanline(DrawingArea.pixels, y1, x1 >> 16, x2 >> 16, r1, g1, b1, r2, g2, b2, z1,
                        depthSlope);
                x2 += dx3;
                x1 += dx1;
                r2 += dr3;
                g2 += dg3;
                b2 += db3;
                r1 += dr1;
                g1 += dg1;
                b1 += db1;
                z1 += depthScale;
            }
            while (--y2 >= 0) {
                drawHDGouraudScanline(DrawingArea.pixels, y1, x1 >> 16, x3 >> 16, r1, g1, b1, r3, g3, b3, z1,
                        depthSlope);
                x3 += dx2;
                x1 += dx1;
                r3 += dr2;
                g3 += dg2;
                b3 += db2;
                r1 += dr1;
                g1 += dg1;
                b1 += db1;
                y1 += DrawingArea.width;
                z1 += depthScale;
            }
            return;
        }
        if (y2 <= y3) {
            if (y2 >= DrawingArea.bottomY) {
                return;
            }
            if (y3 > DrawingArea.bottomY) {
                y3 = DrawingArea.bottomY;
            }
            if (y1 > DrawingArea.bottomY) {
                y1 = DrawingArea.bottomY;
            }
            z2 = z2 - depthSlope * x2 + depthSlope;
            if (y3 < y1) {
                x1 = x2 <<= 16;
                r1 = r2 <<= 16;
                g1 = g2 <<= 16;
                b1 = b2 <<= 16;
                if (y2 < 0) {
                    x1 -= dx1 * y2;
                    x2 -= dx2 * y2;
                    r1 -= dr1 * y2;
                    g1 -= dg1 * y2;
                    b1 -= db1 * y2;
                    r2 -= dr2 * y2;
                    g2 -= dg2 * y2;
                    b2 -= db2 * y2;
                    z2 -= depthScale * y2;
                    y2 = 0;
                }
                x3 <<= 16;
                r3 <<= 16;
                g3 <<= 16;
                b3 <<= 16;
                if (y3 < 0) {
                    x3 -= dx3 * y3;
                    r3 -= dr3 * y3;
                    g3 -= dg3 * y3;
                    b3 -= db3 * y3;
                    y3 = 0;
                }
                if (y2 != y3 && dx1 < dx2 || y2 == y3 && dx1 > dx3) {
                    y1 -= y3;
                    y3 -= y2;
                    for (y2 = anIntArray1472[y2]; --y3 >= 0; y2 += DrawingArea.width) {
                        drawHDGouraudScanline(DrawingArea.pixels, y2, x1 >> 16, x2 >> 16, r1, g1, b1, r2, g2, b2, z2,
                                depthSlope);
                        x1 += dx1;
                        x2 += dx2;
                        r1 += dr1;
                        g1 += dg1;
                        b1 += db1;
                        r2 += dr2;
                        g2 += dg2;
                        b2 += db2;
                        z2 += depthScale;
                    }
                    while (--y1 >= 0) {
                        drawHDGouraudScanline(DrawingArea.pixels, y2, x1 >> 16, x3 >> 16, r1, g1, b1, r3, g3, b3, z2,
                                depthSlope);
                        x1 += dx1;
                        x3 += dx3;
                        r1 += dr1;
                        g1 += dg1;
                        b1 += db1;
                        r3 += dr3;
                        g3 += dg3;
                        b3 += db3;
                        y2 += DrawingArea.width;
                        z2 += depthScale;
                    }
                    return;
                }
                y1 -= y3;
                y3 -= y2;
                for (y2 = anIntArray1472[y2]; --y3 >= 0; y2 += DrawingArea.width) {
                    drawHDGouraudScanline(DrawingArea.pixels, y2, x2 >> 16, x1 >> 16, r2, g2, b2, r1, g1, b1, z2,
                            depthSlope);
                    x1 += dx1;
                    x2 += dx2;
                    r1 += dr1;
                    g1 += dg1;
                    b1 += db1;
                    r2 += dr2;
                    g2 += dg2;
                    b2 += db2;
                    z2 += depthScale;
                }
                while (--y1 >= 0) {
                    drawHDGouraudScanline(DrawingArea.pixels, y2, x3 >> 16, x1 >> 16, r3, g3, b3, r1, g1, b1, z2,
                            depthSlope);
                    x1 += dx1;
                    x3 += dx3;
                    r1 += dr1;
                    g1 += dg1;
                    b1 += db1;
                    r3 += dr3;
                    g3 += dg3;
                    b3 += db3;
                    y2 += DrawingArea.width;
                    z2 += depthScale;
                }
                return;
            }
            x3 = x2 <<= 16;
            r3 = r2 <<= 16;
            g3 = g2 <<= 16;
            b3 = b2 <<= 16;
            if (y2 < 0) {
                x3 -= dx1 * y2;
                x2 -= dx2 * y2;
                r3 -= dr1 * y2;
                g3 -= dg1 * y2;
                b3 -= db1 * y2;
                r2 -= dr2 * y2;
                g2 -= dg2 * y2;
                b2 -= db2 * y2;
                z2 -= depthScale * y2;
                y2 = 0;
            }
            x1 <<= 16;
            r1 <<= 16;
            g1 <<= 16;
            b1 <<= 16;
            if (y1 < 0) {
                x1 -= dx3 * y1;
                r1 -= dr3 * y1;
                g1 -= dg3 * y1;
                b1 -= db3 * y1;
                y1 = 0;
            }
            if (dx1 < dx2) {
                y3 -= y1;
                y1 -= y2;
                for (y2 = anIntArray1472[y2]; --y1 >= 0; y2 += DrawingArea.width) {
                    drawHDGouraudScanline(DrawingArea.pixels, y2, x3 >> 16, x2 >> 16, r3, g3, b3, r2, g2, b2, z2,
                            depthSlope);
                    x3 += dx1;
                    x2 += dx2;
                    r3 += dr1;
                    g3 += dg1;
                    b3 += db1;
                    r2 += dr2;
                    g2 += dg2;
                    b2 += db2;
                    z2 += depthScale;
                }
                while (--y3 >= 0) {
                    drawHDGouraudScanline(DrawingArea.pixels, y2, x1 >> 16, x2 >> 16, r1, g1, b1, r2, g2, b2, z2,
                            depthSlope);
                    x1 += dx3;
                    x2 += dx2;
                    r1 += dr3;
                    g1 += dg3;
                    b1 += db3;
                    r2 += dr2;
                    g2 += dg2;
                    b2 += db2;
                    y2 += DrawingArea.width;
                    z2 += depthScale;
                }
                return;
            }
            y3 -= y1;
            y1 -= y2;
            for (y2 = anIntArray1472[y2]; --y1 >= 0; y2 += DrawingArea.width) {
                drawHDGouraudScanline(DrawingArea.pixels, y2, x2 >> 16, x3 >> 16, r2, g2, b2, r3, g3, b3, z2,
                        depthSlope);
                x3 += dx1;
                x2 += dx2;
                r3 += dr1;
                g3 += dg1;
                b3 += db1;
                r2 += dr2;
                g2 += dg2;
                b2 += db2;
                z2 += depthScale;
            }
            while (--y3 >= 0) {
                drawHDGouraudScanline(DrawingArea.pixels, y2, x2 >> 16, x1 >> 16, r2, g2, b2, r1, g1, b1, z2,
                        depthSlope);
                x1 += dx3;
                x2 += dx2;
                r1 += dr3;
                g1 += dg3;
                b1 += db3;
                r2 += dr2;
                g2 += dg2;
                b2 += db2;
                y2 += DrawingArea.width;
                z2 += depthScale;
            }
            return;
        }
        if (y3 >= DrawingArea.bottomY) {
            return;
        }
        if (y1 > DrawingArea.bottomY) {
            y1 = DrawingArea.bottomY;
        }
        if (y2 > DrawingArea.bottomY) {
            y2 = DrawingArea.bottomY;
        }
        z3 = z3 - depthSlope * x3 + depthSlope;
        if (y1 < y2) {
            x2 = x3 <<= 16;
            r2 = r3 <<= 16;
            g2 = g3 <<= 16;
            b2 = b3 <<= 16;
            if (y3 < 0) {
                x2 -= dx2 * y3;
                x3 -= dx3 * y3;
                r2 -= dr2 * y3;
                g2 -= dg2 * y3;
                b2 -= db2 * y3;
                r3 -= dr3 * y3;
                g3 -= dg3 * y3;
                b3 -= db3 * y3;
                z3 -= depthScale * y3;
                y3 = 0;
            }
            x1 <<= 16;
            r1 <<= 16;
            g1 <<= 16;
            b1 <<= 16;
            if (y1 < 0) {
                x1 -= dx1 * y1;
                r1 -= dr1 * y1;
                g1 -= dg1 * y1;
                b1 -= db1 * y1;
                y1 = 0;
            }
            if (dx2 < dx3) {
                y2 -= y1;
                y1 -= y3;
                for (y3 = anIntArray1472[y3]; --y1 >= 0; y3 += DrawingArea.width) {
                    drawHDGouraudScanline(DrawingArea.pixels, y3, x2 >> 16, x3 >> 16, r2, g2, b2, r3, g3, b3, z3,
                            depthSlope);
                    x2 += dx2;
                    x3 += dx3;
                    r2 += dr2;
                    g2 += dg2;
                    b2 += db2;
                    r3 += dr3;
                    g3 += dg3;
                    b3 += db3;
                    z3 += depthScale;
                }
                while (--y2 >= 0) {
                    drawHDGouraudScanline(DrawingArea.pixels, y3, x2 >> 16, x1 >> 16, r2, g2, b2, r1, g1, b1, z3,
                            depthSlope);
                    x2 += dx2;
                    x1 += dx1;
                    r2 += dr2;
                    g2 += dg2;
                    b2 += db2;
                    r1 += dr1;
                    g1 += dg1;
                    b1 += db1;
                    y3 += DrawingArea.width;
                    z3 += depthScale;
                }
                return;
            }
            y2 -= y1;
            y1 -= y3;
            for (y3 = anIntArray1472[y3]; --y1 >= 0; y3 += DrawingArea.width) {
                drawHDGouraudScanline(DrawingArea.pixels, y3, x3 >> 16, x2 >> 16, r3, g3, b3, r2, g2, b2, z3,
                        depthSlope);
                x2 += dx2;
                x3 += dx3;
                r2 += dr2;
                g2 += dg2;
                b2 += db2;
                r3 += dr3;
                g3 += dg3;
                b3 += db3;
                z3 += depthScale;
            }
            while (--y2 >= 0) {
                drawHDGouraudScanline(DrawingArea.pixels, y3, x1 >> 16, x2 >> 16, r1, g1, b1, r2, g2, b2, z3,
                        depthSlope);
                x2 += dx2;
                x1 += dx1;
                r2 += dr2;
                g2 += dg2;
                b2 += db2;
                r1 += dr1;
                g1 += dg1;
                b1 += db1;
                z3 += depthScale;
                y3 += DrawingArea.width;
            }
            return;
        }
        x1 = x3 <<= 16;
        r1 = r3 <<= 16;
        g1 = g3 <<= 16;
        b1 = b3 <<= 16;
        if (y3 < 0) {
            x1 -= dx2 * y3;
            x3 -= dx3 * y3;
            r1 -= dr2 * y3;
            g1 -= dg2 * y3;
            b1 -= db2 * y3;
            r3 -= dr3 * y3;
            g3 -= dg3 * y3;
            b3 -= db3 * y3;
            z3 -= depthScale * y3;
            y3 = 0;
        }
        x2 <<= 16;
        r2 <<= 16;
        g2 <<= 16;
        b2 <<= 16;
        if (y2 < 0) {
            x2 -= dx1 * y2;
            r2 -= dr1 * y2;
            g2 -= dg1 * y2;
            b2 -= db1 * y2;
            y2 = 0;
        }
        if (dx2 < dx3) {
            y1 -= y2;
            y2 -= y3;
            for (y3 = anIntArray1472[y3]; --y2 >= 0; y3 += DrawingArea.width) {
                drawHDGouraudScanline(DrawingArea.pixels, y3, x1 >> 16, x3 >> 16, r1, g1, b1, r3, g3, b3, z3,
                        depthSlope);
                x1 += dx2;
                x3 += dx3;
                r1 += dr2;
                g1 += dg2;
                b1 += db2;
                r3 += dr3;
                g3 += dg3;
                b3 += db3;
                z3 += depthScale;
            }
            while (--y1 >= 0) {
                drawHDGouraudScanline(DrawingArea.pixels, y3, x2 >> 16, x3 >> 16, r2, g2, b2, r3, g3, b3, z3,
                        depthSlope);
                x2 += dx1;
                x3 += dx3;
                r2 += dr1;
                g2 += dg1;
                b2 += db1;
                r3 += dr3;
                g3 += dg3;
                b3 += db3;
                z3 += depthScale;
                y3 += DrawingArea.width;
            }
            return;
        }
        y1 -= y2;
        y2 -= y3;
        for (y3 = anIntArray1472[y3]; --y2 >= 0; y3 += DrawingArea.width) {
            drawHDGouraudScanline(DrawingArea.pixels, y3, x3 >> 16, x1 >> 16, r3, g3, b3, r1, g1, b1, z3, depthSlope);
            x1 += dx2;
            x3 += dx3;
            r1 += dr2;
            g1 += dg2;
            b1 += db2;
            r3 += dr3;
            g3 += dg3;
            b3 += db3;
            z3 += depthScale;
        }
        while (--y1 >= 0) {
            drawHDGouraudScanline(DrawingArea.pixels, y3, x3 >> 16, x2 >> 16, r3, g3, b3, r2, g2, b2, z3, depthSlope);
            x2 += dx1;
            x3 += dx3;
            r2 += dr1;
            g2 += dg1;
            b2 += db1;
            r3 += dr3;
            g3 += dg3;
            b3 += db3;
            y3 += DrawingArea.width;
            z3 += depthScale;
        }
    }

    public static void drawHDGouraudScanline(int[] dest, int offset, int x1, int x2, int r1, int g1, int b1, int r2,
                                             int g2, int b2, float z1, float z2) {
        int n = x2 - x1;
        if (n <= 0) {
            return;
        }
        r2 = (r2 - r1) / n;
        g2 = (g2 - g1) / n;
        b2 = (b2 - b1) / n;
        if (aBoolean1462) {
            if (x2 > DrawingArea.centerX) {
                n -= x2 - DrawingArea.centerX;
                x2 = DrawingArea.centerX;
            }
            if (x1 < 0) {
                n = x2;
                r1 -= x1 * r2;
                g1 -= x1 * g2;
                b1 -= x1 * b2;
                x1 = 0;
            }
        }
        if (x1 < x2) {
            offset += x1;
            z1 += z2 * x1;
            if (anInt1465 == 0) {
                while (--n >= 0) {
                    dest[offset] = (r1 & 0xff0000) | (g1 >> 8 & 0xff00) | (b1 >> 16 & 0xff);
                    if (saveDepth) {
                        depthBuffer[offset] = z1;
                    }
                    z1 += z2;
                    r1 += r2;
                    g1 += g2;
                    b1 += b2;
                    offset++;
                }
            } else {
                final int a1 = anInt1465;
                final int a2 = 256 - anInt1465;
                int rgb;
                while (--n >= 0) {
                    rgb = r1 & 0xff0000 | g1 >> 8 & 0xff00 | b1 >> 16 & 0xff;
                    rgb = ((rgb & 0xff00ff) * a2 >> 8 & 0xff00ff) + ((rgb & 0xff00) * a2 >> 8 & 0xff00);
                    int dst = dest[offset];
                    dest[offset] = rgb + ((dst & 0xff00ff) * a1 >> 8 & 0xff00ff) + ((dst & 0xff00) * a1 >> 8 & 0xff00);
                    if (saveDepth) {
                        z1 = depthBuffer[offset];
                    }
                    offset++;
                    z1 += z2;
                    r1 += r2;
                    g1 += g2;
                    b1 += b2;
                }
            }
        }
    }

    public static void drawFlatTriangle(int y1, int y2, int y3, int x1, int x2, int x3, int rgb, float z1, float z2,
    		float z3) {
        if (!saveDepth) {
            z1 = z2 = z3 = 0;
        }
        int dx1 = 0;
        if (y2 != y1) {
            final int d = (y2 - y1);
            dx1 = (x2 - x1 << 16) / d;
        }
        int dx2 = 0;
        if (y3 != y2) {
            final int d = (y3 - y2);
            dx2 = (x3 - x2 << 16) / d;
        }
        int dx3 = 0;
        if (y3 != y1) {
            final int d = (y1 - y3);
            dx3 = (x1 - x3 << 16) / d;
        }

        float x21 = x2 - x1;
        float y32 = y2 - y1;
        float x31 = x3 - x1;
        float y31 = y3 - y1;
        float z21 = z2 - z1;
        float z31 = z3 - z1;

        float div = x21 * y31 - x31 * y32;
        float depthSlope = (z21 * y31 - z31 * y32) / div;
        float depthScale = (z31 * x21 - z21 * x31) / div;

        if (y1 <= y2 && y1 <= y3) {
            if (y1 >= DrawingArea.bottomY) {
                return;
            }
            if (y2 > DrawingArea.bottomY) {
                y2 = DrawingArea.bottomY;
            }
            if (y3 > DrawingArea.bottomY) {
                y3 = DrawingArea.bottomY;
            }
            z1 = z1 - depthSlope * x1 + depthSlope;
            if (y2 < y3) {
                x3 = x1 <<= 16;
                if (y1 < 0) {
                    x3 -= dx3 * y1;
                    x1 -= dx1 * y1;
                    z1 -= depthScale * y1;
                    y1 = 0;
                }
                x2 <<= 16;
                if (y2 < 0) {
                    x2 -= dx2 * y2;
                    y2 = 0;
                }
                if (y1 != y2 && dx3 < dx1 || y1 == y2 && dx3 > dx2) {
                    y3 -= y2;
                    y2 -= y1;
                    for (y1 = anIntArray1472[y1]; --y2 >= 0; y1 += DrawingArea.width) {
                        drawFlatScanline(DrawingArea.pixels, y1, rgb, x3 >> 16, x1 >> 16, z1, depthSlope);
                        z1 += depthScale;
                        x3 += dx3;
                        x1 += dx1;
                    }
                    while (--y3 >= 0) {
                        drawFlatScanline(DrawingArea.pixels, y1, rgb, x3 >> 16, x2 >> 16, z1, depthSlope);
                        z1 += depthScale;
                        x3 += dx3;
                        x2 += dx2;
                        y1 += DrawingArea.width;
                    }
                    return;
                }
                y3 -= y2;
                y2 -= y1;
                for (y1 = anIntArray1472[y1]; --y2 >= 0; y1 += DrawingArea.width) {
                    drawFlatScanline(DrawingArea.pixels, y1, rgb, x1 >> 16, x3 >> 16, z1, depthSlope);
                    z1 += depthScale;
                    x3 += dx3;
                    x1 += dx1;
                }
                while (--y3 >= 0) {
                    drawFlatScanline(DrawingArea.pixels, y1, rgb, x2 >> 16, x3 >> 16, z1, depthSlope);
                    z1 += depthScale;
                    x3 += dx3;
                    x2 += dx2;
                    y1 += DrawingArea.width;
                }
                return;
            }
            x2 = x1 <<= 16;
            if (y1 < 0) {
                x2 -= dx3 * y1;
                x1 -= dx1 * y1;
                z1 -= depthScale * y1;
                y1 = 0;
            }
            x3 <<= 16;
            if (y3 < 0) {
                x3 -= dx2 * y3;
                y3 = 0;
            }
            if (y1 != y3 && dx3 < dx1 || y1 == y3 && dx2 > dx1) {
                y2 -= y3;
                y3 -= y1;
                for (y1 = anIntArray1472[y1]; --y3 >= 0; y1 += DrawingArea.width) {
                    drawFlatScanline(DrawingArea.pixels, y1, rgb, x2 >> 16, x1 >> 16, z1, depthSlope);
                    z1 += depthScale;
                    x2 += dx3;
                    x1 += dx1;
                }
                while (--y2 >= 0) {
                    drawFlatScanline(DrawingArea.pixels, y1, rgb, x3 >> 16, x1 >> 16, z1, depthSlope);
                    z1 += depthScale;
                    x3 += dx2;
                    x1 += dx1;
                    y1 += DrawingArea.width;
                }
                return;
            }
            y2 -= y3;
            y3 -= y1;
            for (y1 = anIntArray1472[y1]; --y3 >= 0; y1 += DrawingArea.width) {
                drawFlatScanline(DrawingArea.pixels, y1, rgb, x1 >> 16, x2 >> 16, z1, depthSlope);
                z1 += depthScale;
                x2 += dx3;
                x1 += dx1;
            }
            while (--y2 >= 0) {
                drawFlatScanline(DrawingArea.pixels, y1, rgb, x1 >> 16, x3 >> 16, z1, depthSlope);
                z1 += depthScale;
                x3 += dx2;
                x1 += dx1;
                y1 += DrawingArea.width;
            }
            return;
        }
        if (y2 <= y3) {
            if (y2 >= DrawingArea.bottomY) {
                return;
            }
            if (y3 > DrawingArea.bottomY) {
                y3 = DrawingArea.bottomY;
            }
            if (y1 > DrawingArea.bottomY) {
                y1 = DrawingArea.bottomY;
            }
            z2 = z2 - depthSlope * x2 + depthSlope;
            if (y3 < y1) {
                x1 = x2 <<= 16;
                if (y2 < 0) {
                    x1 -= dx1 * y2;
                    x2 -= dx2 * y2;
                    z2 -= depthScale * y2;
                    y2 = 0;
                }
                x3 <<= 16;
                if (y3 < 0) {
                    x3 -= dx3 * y3;
                    y3 = 0;
                }
                if (y2 != y3 && dx1 < dx2 || y2 == y3 && dx1 > dx3) {
                    y1 -= y3;
                    y3 -= y2;
                    for (y2 = anIntArray1472[y2]; --y3 >= 0; y2 += DrawingArea.width) {
                        drawFlatScanline(DrawingArea.pixels, y2, rgb, x1 >> 16, x2 >> 16, z2, depthSlope);
                        z2 += depthScale;
                        x1 += dx1;
                        x2 += dx2;
                    }
                    while (--y1 >= 0) {
                        drawFlatScanline(DrawingArea.pixels, y2, rgb, x1 >> 16, x3 >> 16, z2, depthSlope);
                        z2 += depthScale;
                        x1 += dx1;
                        x3 += dx3;
                        y2 += DrawingArea.width;
                    }
                    return;
                }
                y1 -= y3;
                y3 -= y2;
                for (y2 = anIntArray1472[y2]; --y3 >= 0; y2 += DrawingArea.width) {
                    drawFlatScanline(DrawingArea.pixels, y2, rgb, x2 >> 16, x1 >> 16, z2, depthSlope);
                    z2 += depthScale;
                    x1 += dx1;
                    x2 += dx2;
                }
                while (--y1 >= 0) {
                    drawFlatScanline(DrawingArea.pixels, y2, rgb, x3 >> 16, x1 >> 16, z2, depthSlope);
                    z2 += depthScale;
                    x1 += dx1;
                    x3 += dx3;
                    y2 += DrawingArea.width;
                }
                return;
            }
            x3 = x2 <<= 16;
            if (y2 < 0) {
                x3 -= dx1 * y2;
                x2 -= dx2 * y2;
                z2 -= depthScale * y2;
                y2 = 0;
            }
            x1 <<= 16;
            if (y1 < 0) {
                x1 -= dx3 * y1;
                y1 = 0;
            }
            if (dx1 < dx2) {
                y3 -= y1;
                y1 -= y2;
                for (y2 = anIntArray1472[y2]; --y1 >= 0; y2 += DrawingArea.width) {
                    drawFlatScanline(DrawingArea.pixels, y2, rgb, x3 >> 16, x2 >> 16, z2, depthSlope);
                    z2 += depthScale;
                    x3 += dx1;
                    x2 += dx2;
                }
                while (--y3 >= 0) {
                    drawFlatScanline(DrawingArea.pixels, y2, rgb, x1 >> 16, x2 >> 16, z2, depthSlope);
                    z2 += depthScale;
                    x1 += dx3;
                    x2 += dx2;
                    y2 += DrawingArea.width;
                }
                return;
            }
            y3 -= y1;
            y1 -= y2;
            for (y2 = anIntArray1472[y2]; --y1 >= 0; y2 += DrawingArea.width) {
                drawFlatScanline(DrawingArea.pixels, y2, rgb, x2 >> 16, x3 >> 16, z2, depthSlope);
                z2 += depthScale;
                x3 += dx1;
                x2 += dx2;
            }
            while (--y3 >= 0) {
                drawFlatScanline(DrawingArea.pixels, y2, rgb, x2 >> 16, x1 >> 16, z2, depthSlope);
                z2 += depthScale;
                x1 += dx3;
                x2 += dx2;
                y2 += DrawingArea.width;
            }
            return;
        }
        if (y3 >= DrawingArea.bottomY) {
            return;
        }
        if (y1 > DrawingArea.bottomY) {
            y1 = DrawingArea.bottomY;
        }
        if (y2 > DrawingArea.bottomY) {
            y2 = DrawingArea.bottomY;
        }
        z3 = z3 - depthSlope * x3 + depthSlope;
        if (y1 < y2) {
            x2 = x3 <<= 16;
            if (y3 < 0) {
                x2 -= dx2 * y3;
                x3 -= dx3 * y3;
                z3 -= depthScale * y3;
                y3 = 0;
            }
            x1 <<= 16;
            if (y1 < 0) {
                x1 -= dx1 * y1;
                y1 = 0;
            }
            if (dx2 < dx3) {
                y2 -= y1;
                y1 -= y3;
                for (y3 = anIntArray1472[y3]; --y1 >= 0; y3 += DrawingArea.width) {
                    drawFlatScanline(DrawingArea.pixels, y3, rgb, x2 >> 16, x3 >> 16, z3, depthSlope);
                    z3 += depthScale;
                    x2 += dx2;
                    x3 += dx3;
                }
                while (--y2 >= 0) {
                    drawFlatScanline(DrawingArea.pixels, y3, rgb, x2 >> 16, x1 >> 16, z3, depthSlope);
                    z3 += depthScale;
                    x2 += dx2;
                    x1 += dx1;
                    y3 += DrawingArea.width;
                }
                return;
            }
            y2 -= y1;
            y1 -= y3;
            for (y3 = anIntArray1472[y3]; --y1 >= 0; y3 += DrawingArea.width) {
                drawFlatScanline(DrawingArea.pixels, y3, rgb, x3 >> 16, x2 >> 16, z3, depthSlope);
                z3 += depthScale;
                x2 += dx2;
                x3 += dx3;
            }
            while (--y2 >= 0) {
                drawFlatScanline(DrawingArea.pixels, y3, rgb, x1 >> 16, x2 >> 16, z3, depthSlope);
                z3 += depthScale;
                x2 += dx2;
                x1 += dx1;
                y3 += DrawingArea.width;
            }
            return;
        }
        x1 = x3 <<= 16;
        if (y3 < 0) {
            x1 -= dx2 * y3;
            x3 -= dx3 * y3;
            z3 -= depthScale * y3;
            y3 = 0;
        }
        x2 <<= 16;
        if (y2 < 0) {
            x2 -= dx1 * y2;
            y2 = 0;
        }
        if (dx2 < dx3) {
            y1 -= y2;
            y2 -= y3;
            for (y3 = anIntArray1472[y3]; --y2 >= 0; y3 += DrawingArea.width) {
                drawFlatScanline(DrawingArea.pixels, y3, rgb, x1 >> 16, x3 >> 16, z3, depthSlope);
                z3 += depthScale;
                x1 += dx2;
                x3 += dx3;
            }
            while (--y1 >= 0) {
                drawFlatScanline(DrawingArea.pixels, y3, rgb, x2 >> 16, x3 >> 16, z3, depthSlope);
                z3 += depthScale;
                x2 += dx1;
                x3 += dx3;
                y3 += DrawingArea.width;
            }
            return;
        }
        y1 -= y2;
        y2 -= y3;
        for (y3 = anIntArray1472[y3]; --y2 >= 0; y3 += DrawingArea.width) {
            drawFlatScanline(DrawingArea.pixels, y3, rgb, x3 >> 16, x1 >> 16, z3, depthSlope);
            z3 += depthScale;
            x1 += dx2;
            x3 += dx3;
        }
        while (--y1 >= 0) {
            drawFlatScanline(DrawingArea.pixels, y3, rgb, x3 >> 16, x2 >> 16, z3, depthSlope);
            z3 += depthScale;
            x2 += dx1;
            x3 += dx3;
            y3 += DrawingArea.width;
        }
    }

    private static void drawFlatScanline(int[] dest, int offset, int rgb, int x1, int x2, float z1, float z2) {
        if (x1 >= x2) {
            return;
        }
        if (aBoolean1462) {
            if (x2 > DrawingArea.centerX) {
                x2 = DrawingArea.centerX;
            }
            if (x1 < 0) {
                x1 = 0;
            }
        }
        if (x1 >= x2) {
            return;
        }
        offset += x1;
        z1 += z2 * x1;
        int n = x2 - x1;
        if (anInt1465 == 0) {
            while (--n >= 0) {
                dest[offset] = rgb;
                if (saveDepth) {
                    depthBuffer[offset] = z1;
                }
                z1 += z2;
                offset++;
            }
        } else {
            final int a1 = anInt1465;
            final int a2 = 256 - anInt1465;
            rgb = ((rgb & 0xff00ff) * a2 >> 8 & 0xff00ff) + ((rgb & 0xff00) * a2 >> 8 & 0xff00);
            while (--n >= 0) {
                dest[offset] = rgb + ((dest[offset] & 0xff00ff) * a1 >> 8 & 0xff00ff)
                        + ((dest[offset] & 0xff00) * a1 >> 8 & 0xff00);
                if (saveDepth) {
                    depthBuffer[offset] = z1;
                }
                z1 += z2;
                offset++;
            }
        }
    }

    public static void drawTexturedTriangle317(int y1, int y2, int y3, int x1, int x2, int x3, int c1, int c2, int c3,
                                               int t1, int t2, int t3, int t4, int t5, int t6, int t7, int t8, int t9, int tex, float z1, float z2, float z3) {
        if (!saveDepth) {
            z1 = z2 = z3 = 0;
        }
        int[] texels = getTexturePixels(tex);
        aBoolean1463 = !textureIsTransparant[tex];
        t2 = t1 - t2;
        t5 = t4 - t5;
        t8 = t7 - t8;
        t3 -= t1;
        t6 -= t4;
        t9 -= t7;
        int l4 = (t3 * t4 - t6 * t1) * WorldController.focalLength << 5;
        int i5 = t6 * t7 - t9 * t4 << 8;
        int j5 = t9 * t1 - t3 * t7 << 5;
        int k5 = (t2 * t4 - t5 * t1) * WorldController.focalLength << 5;
        int l5 = t5 * t7 - t8 * t4 << 8;
        int i6 = t8 * t1 - t2 * t7 << 5;
        int j6 = (t5 * t3 - t2 * t6) * WorldController.focalLength << 5;
        int k6 = t8 * t6 - t5 * t9 << 8;
        int l6 = t2 * t9 - t8 * t3 << 5;
        int i7 = 0;
        int j7 = 0;
        if (y2 != y1) {
            i7 = (x2 - x1 << 16) / (y2 - y1);
            j7 = (c2 - c1 << 16) / (y2 - y1);
        }
        int k7 = 0;
        int l7 = 0;
        if (y3 != y2) {
            k7 = (x3 - x2 << 16) / (y3 - y2);
            l7 = (c3 - c2 << 16) / (y3 - y2);
        }
        int i8 = 0;
        int j8 = 0;
        if (y3 != y1) {
            i8 = (x1 - x3 << 16) / (y1 - y3);
            j8 = (c1 - c3 << 16) / (y1 - y3);
        }

        float x21 = x2 - x1;
        float y32 = y2 - y1;
        float x31 = x3 - x1;
        float y31 = y3 - y1;
        float z21 = z2 - z1;
        float z31 = z3 - z1;

        float div = x21 * y31 - x31 * y32;
        float depthSlope = (z21 * y31 - z31 * y32) / div;
        float depthScale = (z31 * x21 - z21 * x31) / div;

        if (y1 <= y2 && y1 <= y3) {
            if (y1 >= DrawingArea.bottomY) {
                return;
            }
            if (y2 > DrawingArea.bottomY) {
                y2 = DrawingArea.bottomY;
            }
            if (y3 > DrawingArea.bottomY) {
                y3 = DrawingArea.bottomY;
            }
            z1 = z1 - depthSlope * x1 + depthSlope;
            if (y2 < y3) {
                x3 = x1 <<= 16;
                c3 = c1 <<= 16;
                if (y1 < 0) {
                    y1 -= 0;
                    x3 -= i8 * y1;
                    x1 -= i7 * y1;
                    z1 -= depthScale * y1;
                    c3 -= j8 * y1;
                    c1 -= j7 * y1;
                    y1 = 0;
                }
                x2 <<= 16;
                c2 <<= 16;
                if (y2 < 0) {
                    y2 -= 0;
                    x2 -= k7 * y2;
                    c2 -= l7 * y2;
                    y2 = 0;
                }
                int k8 = y1 - textureInt2;
                l4 += j5 * k8;
                k5 += i6 * k8;
                j6 += l6 * k8;
                if (y1 != y2 && i8 < i7 || y1 == y2 && i8 > k7) {
                    y3 -= y2;
                    y2 -= y1;
                    y1 = anIntArray1472[y1];
                    while (--y2 >= 0) {
                        drawTexturedScanline317(DrawingArea.pixels, texels, y1, x3 >> 16, x1 >> 16, c3 >> 8, c1 >> 8,
                                l4, k5, j6, i5, l5, k6, z1, depthSlope);
                        z1 += depthScale;
                        x3 += i8;
                        x1 += i7;
                        c3 += j8;
                        c1 += j7;
                        y1 += DrawingArea.width;
                        l4 += j5;
                        k5 += i6;
                        j6 += l6;
                    }
                    while (--y3 >= 0) {
                        drawTexturedScanline317(DrawingArea.pixels, texels, y1, x3 >> 16, x2 >> 16, c3 >> 8, c2 >> 8,
                                l4, k5, j6, i5, l5, k6, z1, depthSlope);
                        z1 += depthScale;
                        x3 += i8;
                        x2 += k7;
                        c3 += j8;
                        c2 += l7;
                        y1 += DrawingArea.width;
                        l4 += j5;
                        k5 += i6;
                        j6 += l6;
                    }
                    return;
                }
                y3 -= y2;
                y2 -= y1;
                y1 = anIntArray1472[y1];
                while (--y2 >= 0) {
                    drawTexturedScanline317(DrawingArea.pixels, texels, y1, x1 >> 16, x3 >> 16, c1 >> 8, c3 >> 8, l4,
                            k5, j6, i5, l5, k6, z1, depthSlope);
                    z1 += depthScale;
                    x3 += i8;
                    x1 += i7;
                    c3 += j8;
                    c1 += j7;
                    y1 += DrawingArea.width;
                    l4 += j5;
                    k5 += i6;
                    j6 += l6;
                }
                while (--y3 >= 0) {
                    drawTexturedScanline317(DrawingArea.pixels, texels, y1, x2 >> 16, x3 >> 16, c2 >> 8, c3 >> 8, l4,
                            k5, j6, i5, l5, k6, z1, depthSlope);
                    z1 += depthScale;
                    x3 += i8;
                    x2 += k7;
                    c3 += j8;
                    c2 += l7;
                    y1 += DrawingArea.width;
                    l4 += j5;
                    k5 += i6;
                    j6 += l6;
                }
                return;
            }
            x2 = x1 <<= 16;
            c2 = c1 <<= 16;
            if (y1 < 0) {
                y1 -= 0;
                x2 -= i8 * y1;
                z1 -= depthScale * y1;
                x1 -= i7 * y1;
                c2 -= j8 * y1;
                c1 -= j7 * y1;
                y1 = 0;
            }
            x3 <<= 16;
            c3 <<= 16;
            if (y3 < 0) {
                y3 -= 0;
                x3 -= k7 * y3;
                c3 -= l7 * y3;
                y3 = 0;
            }
            int l8 = y1 - textureInt2;
            l4 += j5 * l8;
            k5 += i6 * l8;
            j6 += l6 * l8;
            if (y1 != y3 && i8 < i7 || y1 == y3 && k7 > i7) {
                y2 -= y3;
                y3 -= y1;
                y1 = anIntArray1472[y1];
                while (--y3 >= 0) {
                    drawTexturedScanline317(DrawingArea.pixels, texels, y1, x2 >> 16, x1 >> 16, c2 >> 8, c1 >> 8, l4,
                            k5, j6, i5, l5, k6, z1, depthSlope);
                    z1 += depthScale;
                    x2 += i8;
                    x1 += i7;
                    c2 += j8;
                    c1 += j7;
                    y1 += DrawingArea.width;
                    l4 += j5;
                    k5 += i6;
                    j6 += l6;
                }
                while (--y2 >= 0) {
                    drawTexturedScanline317(DrawingArea.pixels, texels, y1, x3 >> 16, x1 >> 16, c3 >> 8, c1 >> 8, l4,
                            k5, j6, i5, l5, k6, z1, depthSlope);
                    z1 += depthScale;
                    x3 += k7;
                    x1 += i7;
                    c3 += l7;
                    c1 += j7;
                    y1 += DrawingArea.width;
                    l4 += j5;
                    k5 += i6;
                    j6 += l6;
                }
                return;
            }
            y2 -= y3;
            y3 -= y1;
            y1 = anIntArray1472[y1];
            while (--y3 >= 0) {
                drawTexturedScanline317(DrawingArea.pixels, texels, y1, x1 >> 16, x2 >> 16, c1 >> 8, c2 >> 8, l4, k5,
                        j6, i5, l5, k6, z1, depthSlope);
                z1 += depthScale;
                x2 += i8;
                x1 += i7;
                c2 += j8;
                c1 += j7;
                y1 += DrawingArea.width;
                l4 += j5;
                k5 += i6;
                j6 += l6;
            }
            while (--y2 >= 0) {
                drawTexturedScanline317(DrawingArea.pixels, texels, y1, x1 >> 16, x3 >> 16, c1 >> 8, c3 >> 8, l4, k5,
                        j6, i5, l5, k6, z1, depthSlope);
                z1 += depthScale;
                x3 += k7;
                x1 += i7;
                c3 += l7;
                c1 += j7;
                y1 += DrawingArea.width;
                l4 += j5;
                k5 += i6;
                j6 += l6;
            }
            return;
        }
        if (y2 <= y3) {
            if (y2 >= DrawingArea.bottomY) {
                return;
            }
            if (y3 > DrawingArea.bottomY) {
                y3 = DrawingArea.bottomY;
            }
            if (y1 > DrawingArea.bottomY) {
                y1 = DrawingArea.bottomY;
            }
            z2 = z2 - depthSlope * x2 + depthSlope;
            if (y3 < y1) {
                x1 = x2 <<= 16;
                c1 = c2 <<= 16;
                if (y2 < 0) {
                    y2 -= 0;
                    x1 -= i7 * y2;
                    x2 -= k7 * y2;
                    z2 -= depthScale * y2;
                    c1 -= j7 * y2;
                    c2 -= l7 * y2;
                    y2 = 0;
                }
                x3 <<= 16;
                c3 <<= 16;
                if (y3 < 0) {
                    y3 -= 0;
                    x3 -= i8 * y3;
                    c3 -= j8 * y3;
                    y3 = 0;
                }
                int i9 = y2 - textureInt2;
                l4 += j5 * i9;
                k5 += i6 * i9;
                j6 += l6 * i9;
                if (y2 != y3 && i7 < k7 || y2 == y3 && i7 > i8) {
                    y1 -= y3;
                    y3 -= y2;
                    y2 = anIntArray1472[y2];
                    while (--y3 >= 0) {
                        drawTexturedScanline317(DrawingArea.pixels, texels, y2, x1 >> 16, x2 >> 16, c1 >> 8, c2 >> 8,
                                l4, k5, j6, i5, l5, k6, z2, depthSlope);
                        z2 += depthScale;
                        x1 += i7;
                        x2 += k7;
                        c1 += j7;
                        c2 += l7;
                        y2 += DrawingArea.width;
                        l4 += j5;
                        k5 += i6;
                        j6 += l6;
                    }
                    while (--y1 >= 0) {
                        drawTexturedScanline317(DrawingArea.pixels, texels, y2, x1 >> 16, x3 >> 16, c1 >> 8, c3 >> 8,
                                l4, k5, j6, i5, l5, k6, z2, depthSlope);
                        z2 += depthScale;
                        x1 += i7;
                        x3 += i8;
                        c1 += j7;
                        c3 += j8;
                        y2 += DrawingArea.width;
                        l4 += j5;
                        k5 += i6;
                        j6 += l6;
                    }
                    return;
                }
                y1 -= y3;
                y3 -= y2;
                y2 = anIntArray1472[y2];
                while (--y3 >= 0) {
                    drawTexturedScanline317(DrawingArea.pixels, texels, y2, x2 >> 16, x1 >> 16, c2 >> 8, c1 >> 8, l4,
                            k5, j6, i5, l5, k6, z2, depthSlope);
                    z2 += depthScale;
                    x1 += i7;
                    x2 += k7;
                    c1 += j7;
                    c2 += l7;
                    y2 += DrawingArea.width;
                    l4 += j5;
                    k5 += i6;
                    j6 += l6;
                }
                while (--y1 >= 0) {
                    drawTexturedScanline317(DrawingArea.pixels, texels, y2, x3 >> 16, x1 >> 16, c3 >> 8, c1 >> 8, l4,
                            k5, j6, i5, l5, k6, z2, depthSlope);
                    z2 += depthScale;
                    x1 += i7;
                    x3 += i8;
                    c1 += j7;
                    c3 += j8;
                    y2 += DrawingArea.width;
                    l4 += j5;
                    k5 += i6;
                    j6 += l6;
                }
                return;
            }
            x3 = x2 <<= 16;
            c3 = c2 <<= 16;
            if (y2 < 0) {
                y2 -= 0;
                x3 -= i7 * y2;
                z2 -= depthScale * y2;
                x2 -= k7 * y2;
                c3 -= j7 * y2;
                c2 -= l7 * y2;
                y2 = 0;
            }
            x1 <<= 16;
            c1 <<= 16;
            if (y1 < 0) {
                y1 -= 0;
                x1 -= i8 * y1;
                c1 -= j8 * y1;
                y1 = 0;
            }
            int j9 = y2 - textureInt2;
            l4 += j5 * j9;
            k5 += i6 * j9;
            j6 += l6 * j9;
            if (i7 < k7) {
                y3 -= y1;
                y1 -= y2;
                y2 = anIntArray1472[y2];
                while (--y1 >= 0) {
                    drawTexturedScanline317(DrawingArea.pixels, texels, y2, x3 >> 16, x2 >> 16, c3 >> 8, c2 >> 8, l4,
                            k5, j6, i5, l5, k6, z2, depthSlope);
                    z2 += depthScale;
                    x3 += i7;
                    x2 += k7;
                    c3 += j7;
                    c2 += l7;
                    y2 += DrawingArea.width;
                    l4 += j5;
                    k5 += i6;
                    j6 += l6;
                }
                while (--y3 >= 0) {
                    drawTexturedScanline317(DrawingArea.pixels, texels, y2, x1 >> 16, x2 >> 16, c1 >> 8, c2 >> 8, l4,
                            k5, j6, i5, l5, k6, z2, depthSlope);
                    z2 += depthScale;
                    x1 += i8;
                    x2 += k7;
                    c1 += j8;
                    c2 += l7;
                    y2 += DrawingArea.width;
                    l4 += j5;
                    k5 += i6;
                    j6 += l6;
                }
                return;
            }
            y3 -= y1;
            y1 -= y2;
            y2 = anIntArray1472[y2];
            while (--y1 >= 0) {
                drawTexturedScanline317(DrawingArea.pixels, texels, y2, x2 >> 16, x3 >> 16, c2 >> 8, c3 >> 8, l4, k5,
                        j6, i5, l5, k6, z2, depthSlope);
                z2 += depthScale;
                x3 += i7;
                x2 += k7;
                c3 += j7;
                c2 += l7;
                y2 += DrawingArea.width;
                l4 += j5;
                k5 += i6;
                j6 += l6;
            }
            while (--y3 >= 0) {
                drawTexturedScanline317(DrawingArea.pixels, texels, y2, x2 >> 16, x1 >> 16, c2 >> 8, c1 >> 8, l4, k5,
                        j6, i5, l5, k6, z2, depthSlope);
                z2 += depthScale;
                x1 += i8;
                x2 += k7;
                c1 += j8;
                c2 += l7;
                y2 += DrawingArea.width;
                l4 += j5;
                k5 += i6;
                j6 += l6;
            }
            return;
        }
        if (y3 >= DrawingArea.bottomY) {
            return;
        }
        if (y1 > DrawingArea.bottomY) {
            y1 = DrawingArea.bottomY;
        }
        if (y2 > DrawingArea.bottomY) {
            y2 = DrawingArea.bottomY;
        }
        z3 = z3 - depthSlope * x3 + depthSlope;
        if (y1 < y2) {
            x2 = x3 <<= 16;
            c2 = c3 <<= 16;
            if (y3 < 0) {
                y3 -= 0;
                x2 -= k7 * y3;
                z3 -= depthScale * y3;
                x3 -= i8 * y3;
                c2 -= l7 * y3;
                c3 -= j8 * y3;
                y3 = 0;
            }
            x1 <<= 16;
            c1 <<= 16;
            if (y1 < 0) {
                y1 -= 0;
                x1 -= i7 * y1;
                c1 -= j7 * y1;
                y1 = 0;
            }
            final int k9 = y3 - textureInt2;
            l4 += j5 * k9;
            k5 += i6 * k9;
            j6 += l6 * k9;
            if (k7 < i8) {
                y2 -= y1;
                y1 -= y3;
                y3 = anIntArray1472[y3];
                while (--y1 >= 0) {
                    drawTexturedScanline317(DrawingArea.pixels, texels, y3, x2 >> 16, x3 >> 16, c2 >> 8, c3 >> 8, l4,
                            k5, j6, i5, l5, k6, z3, depthSlope);
                    z3 += depthScale;
                    x2 += k7;
                    x3 += i8;
                    c2 += l7;
                    c3 += j8;
                    y3 += DrawingArea.width;
                    l4 += j5;
                    k5 += i6;
                    j6 += l6;
                }
                while (--y2 >= 0) {
                    drawTexturedScanline317(DrawingArea.pixels, texels, y3, x2 >> 16, x1 >> 16, c2 >> 8, c1 >> 8, l4,
                            k5, j6, i5, l5, k6, z3, depthSlope);
                    z3 += depthScale;
                    x2 += k7;
                    x1 += i7;
                    c2 += l7;
                    c1 += j7;
                    y3 += DrawingArea.width;
                    l4 += j5;
                    k5 += i6;
                    j6 += l6;
                }
                return;
            }
            y2 -= y1;
            y1 -= y3;
            y3 = anIntArray1472[y3];
            while (--y1 >= 0) {
                drawTexturedScanline317(DrawingArea.pixels, texels, y3, x3 >> 16, x2 >> 16, c3 >> 8, c2 >> 8, l4, k5,
                        j6, i5, l5, k6, z3, depthSlope);
                z3 += depthScale;
                x2 += k7;
                x3 += i8;
                c2 += l7;
                c3 += j8;
                y3 += DrawingArea.width;
                l4 += j5;
                k5 += i6;
                j6 += l6;
            }
            while (--y2 >= 0) {
                drawTexturedScanline317(DrawingArea.pixels, texels, y3, x1 >> 16, x2 >> 16, c1 >> 8, c2 >> 8, l4, k5,
                        j6, i5, l5, k6, z3, depthSlope);
                z3 += depthScale;
                x2 += k7;
                x1 += i7;
                c2 += l7;
                c1 += j7;
                y3 += DrawingArea.width;
                l4 += j5;
                k5 += i6;
                j6 += l6;
            }
            return;
        }
        x1 = x3 <<= 16;
        c1 = c3 <<= 16;
        if (y3 < 0) {
            y3 -= 0;
            x1 -= k7 * y3;
            z3 -= depthScale * y3;
            x3 -= i8 * y3;
            c1 -= l7 * y3;
            c3 -= j8 * y3;
            y3 = 0;
        }
        x2 <<= 16;
        c2 <<= 16;
        if (y2 < 0) {
            y2 -= 0;
            x2 -= i7 * y2;
            c2 -= j7 * y2;
            y2 = 0;
        }
        int l9 = y3 - textureInt2;
        l4 += j5 * l9;
        k5 += i6 * l9;
        j6 += l6 * l9;
        if (k7 < i8) {
            y1 -= y2;
            y2 -= y3;
            y3 = anIntArray1472[y3];
            while (--y2 >= 0) {
                drawTexturedScanline317(DrawingArea.pixels, texels, y3, x1 >> 16, x3 >> 16, c1 >> 8, c3 >> 8, l4, k5,
                        j6, i5, l5, k6, z3, depthSlope);
                z3 += depthScale;
                x1 += k7;
                x3 += i8;
                c1 += l7;
                c3 += j8;
                y3 += DrawingArea.width;
                l4 += j5;
                k5 += i6;
                j6 += l6;
            }
            while (--y1 >= 0) {
                drawTexturedScanline317(DrawingArea.pixels, texels, y3, x2 >> 16, x3 >> 16, c2 >> 8, c3 >> 8, l4, k5,
                        j6, i5, l5, k6, z3, depthSlope);
                z3 += depthScale;
                x2 += i7;
                x3 += i8;
                c2 += j7;
                c3 += j8;
                y3 += DrawingArea.width;
                l4 += j5;
                k5 += i6;
                j6 += l6;
            }
            return;
        }
        y1 -= y2;
        y2 -= y3;
        y3 = anIntArray1472[y3];
        while (--y2 >= 0) {
            drawTexturedScanline317(DrawingArea.pixels, texels, y3, x3 >> 16, x1 >> 16, c3 >> 8, c1 >> 8, l4, k5, j6,
                    i5, l5, k6, z3, depthSlope);
            z3 += depthScale;
            x1 += k7;
            x3 += i8;
            c1 += l7;
            c3 += j8;
            y3 += DrawingArea.width;
            l4 += j5;
            k5 += i6;
            j6 += l6;
        }
        while (--y1 >= 0) {
            drawTexturedScanline317(DrawingArea.pixels, texels, y3, x3 >> 16, x2 >> 16, c3 >> 8, c2 >> 8, l4, k5, j6,
                    i5, l5, k6, z3, depthSlope);
            z3 += depthScale;
            x2 += i7;
            x3 += i8;
            c2 += j7;
            c3 += j8;
            y3 += DrawingArea.width;
            l4 += j5;
            k5 += i6;
            j6 += l6;
        }
    }

    private static void drawTexturedScanline317(int[] dest, int[] src, int offset, int x1, int x2, int hsl1, int hsl2,
                                                int t1, int t2, int t3, int t4, int t5, int t6, float z1, float z2) {
        int rgb = 0;
        int pos = 0;
        if (x1 >= x2) {
            return;
        }
        int rotation;
        int n;
        if (aBoolean1462) {
            rotation = (hsl2 - hsl1) / (x2 - x1);
            if (x2 > DrawingArea.centerX) {
                x2 = DrawingArea.centerX;
            }
            if (x1 < 0) {
                x1 -= 0;
                hsl1 -= x1 * rotation;
                x1 = 0;
            }
            if (x1 >= x2) {
                return;
            }
            n = x2 - x1 >> 3;
            rotation <<= 12;
            hsl1 <<= 9;
        } else {
            if (x2 - x1 > 7) {
                n = x2 - x1 >> 3;
                rotation = (hsl2 - hsl1) * anIntArray1468[n] >> 6;
            } else {
                n = 0;
                rotation = 0;
            }
            hsl1 <<= 9;
        }
        offset += x1;
        z1 += z2 * x1;
        int j4 = 0;
        int l4 = 0;
        int l6 = x1 - textureInt1;
        t1 += (t4 >> 3) * l6;
        t2 += (t5 >> 3) * l6;
        t3 += (t6 >> 3) * l6;
        int l5 = t3 >> 14;
        if (l5 != 0) {
            rgb = t1 / l5;
            pos = t2 / l5;
            if (rgb < 0) {
                rgb = 0;
            } else if (rgb > 16256) {
                rgb = 16256;
            }
        }
        t1 += t4;
        t2 += t5;
        t3 += t6;
        l5 = t3 >> 14;
        if (l5 != 0) {
            j4 = t1 / l5;
            l4 = t2 / l5;
            if (j4 < 7) {
                j4 = 7;
            } else if (j4 > 16256) {
                j4 = 16256;
            }
        }
        int pixelBrightness = j4 - rgb >> 3;
        int pixelPos = l4 - pos >> 3;
        rgb += hsl1 & 0x600000;
        int brightness = hsl1 >> 23;
        if (aBoolean1463) {
            while (n-- > 0) {
                dest[offset] = src[(pos & 0x3f80) + (rgb >> 7)] >>> brightness;
                if (saveDepth) {
                    depthBuffer[offset] = z1;
                }
                offset++;
                z1 += z2;
                rgb += pixelBrightness;
                pos += pixelPos;
                dest[offset] = src[(pos & 0x3f80) + (rgb >> 7)] >>> brightness;
                if (saveDepth) {
                    depthBuffer[offset] = z1;
                }
                offset++;
                z1 += z2;
                rgb += pixelBrightness;
                pos += pixelPos;
                dest[offset] = src[(pos & 0x3f80) + (rgb >> 7)] >>> brightness;
                if (saveDepth) {
                    depthBuffer[offset] = z1;
                }
                offset++;
                z1 += z2;
                rgb += pixelBrightness;
                pos += pixelPos;
                dest[offset] = src[(pos & 0x3f80) + (rgb >> 7)] >>> brightness;
                if (saveDepth) {
                    depthBuffer[offset] = z1;
                }
                offset++;
                z1 += z2;
                rgb += pixelBrightness;
                pos += pixelPos;
                dest[offset] = src[(pos & 0x3f80) + (rgb >> 7)] >>> brightness;
                if (saveDepth) {
                    depthBuffer[offset] = z1;
                }
                offset++;
                z1 += z2;
                rgb += pixelBrightness;
                pos += pixelPos;
                dest[offset] = src[(pos & 0x3f80) + (rgb >> 7)] >>> brightness;
                if (saveDepth) {
                    depthBuffer[offset] = z1;
                }
                offset++;
                z1 += z2;
                rgb += pixelBrightness;
                pos += pixelPos;
                dest[offset] = src[(pos & 0x3f80) + (rgb >> 7)] >>> brightness;
                if (saveDepth) {
                    depthBuffer[offset] = z1;
                }
                offset++;
                z1 += z2;
                rgb += pixelBrightness;
                pos += pixelPos;
                dest[offset] = src[(pos & 0x3f80) + (rgb >> 7)] >>> brightness;
                if (saveDepth) {
                    depthBuffer[offset] = z1;
                }
                offset++;
                z1 += z2;
                rgb = j4;
                pos = l4;
                t1 += t4;
                t2 += t5;
                t3 += t6;
                final int i6 = t3 >> 14;
                if (i6 != 0) {
                    j4 = t1 / i6;
                    l4 = t2 / i6;
                    if (j4 < 7) {
                        j4 = 7;
                    } else if (j4 > 16256) {
                        j4 = 16256;
                    }
                }
                pixelBrightness = j4 - rgb >> 3;
                pixelPos = l4 - pos >> 3;
                hsl1 += rotation;
                rgb += hsl1 & 0x600000;
                brightness = hsl1 >> 23;
            }
            for (n = x2 - x1 & 7; n-- > 0;) {
                dest[offset] = src[(pos & 0x3f80) + (rgb >> 7)] >>> brightness;
                if (saveDepth) {
                    depthBuffer[offset] = z1;
                }
                z1 += z2;
                offset++;
                rgb += pixelBrightness;
                pos += pixelPos;
            }
            return;
        }
        while (n-- > 0) {
            int color;
            if ((color = src[(pos & 0x3f80) + (rgb >> 7)] >>> brightness) != 0) {
                dest[offset] = color;
                if (saveDepth) {
                    depthBuffer[offset] = z1;
                }
            }
            z1 += z2;
            offset++;
            rgb += pixelBrightness;
            pos += pixelPos;
            if ((color = src[(pos & 0x3f80) + (rgb >> 7)] >>> brightness) != 0) {
                dest[offset] = color;
                if (saveDepth) {
                    depthBuffer[offset] = z1;
                }
            }
            z1 += z2;
            offset++;
            rgb += pixelBrightness;
            pos += pixelPos;
            if ((color = src[(pos & 0x3f80) + (rgb >> 7)] >>> brightness) != 0) {
                dest[offset] = color;
                if (saveDepth) {
                    depthBuffer[offset] = z1;
                }
            }
            z1 += z2;
            offset++;
            rgb += pixelBrightness;
            pos += pixelPos;
            if ((color = src[(pos & 0x3f80) + (rgb >> 7)] >>> brightness) != 0) {
                dest[offset] = color;
                if (saveDepth) {
                    depthBuffer[offset] = z1;
                }
            }
            z1 += z2;
            offset++;
            rgb += pixelBrightness;
            pos += pixelPos;
            if ((color = src[(pos & 0x3f80) + (rgb >> 7)] >>> brightness) != 0) {
                dest[offset] = color;
                if (saveDepth) {
                    depthBuffer[offset] = z1;
                }
            }
            z1 += z2;
            offset++;
            rgb += pixelBrightness;
            pos += pixelPos;
            if ((color = src[(pos & 0x3f80) + (rgb >> 7)] >>> brightness) != 0) {
                dest[offset] = color;
                if (saveDepth) {
                    depthBuffer[offset] = z1;
                }
            }
            z1 += z2;
            offset++;
            rgb += pixelBrightness;
            pos += pixelPos;
            if ((color = src[(pos & 0x3f80) + (rgb >> 7)] >>> brightness) != 0) {
                dest[offset] = color;
                if (saveDepth) {
                    depthBuffer[offset] = z1;
                }
            }
            z1 += z2;
            offset++;
            rgb += pixelBrightness;
            pos += pixelPos;
            if ((color = src[(pos & 0x3f80) + (rgb >> 7)] >>> brightness) != 0) {
                dest[offset] = color;
                if (saveDepth) {
                    depthBuffer[offset] = z1;
                }
            }
            z1 += z2;
            offset++;
            rgb = j4;
            pos = l4;
            t1 += t4;
            t2 += t5;
            t3 += t6;
            final int j6 = t3 >> 14;
            if (j6 != 0) {
                j4 = t1 / j6;
                l4 = t2 / j6;
                if (j4 < 7) {
                    j4 = 7;
                } else if (j4 > 16256) {
                    j4 = 16256;
                }
            }
            pixelBrightness = j4 - rgb >> 3;
            pixelPos = l4 - pos >> 3;
            hsl1 += rotation;
            rgb += hsl1 & 0x600000;
            brightness = hsl1 >> 23;
        }
        for (int l3 = x2 - x1 & 7; l3-- > 0;) {
            int color;
            if ((color = src[(pos & 0x3f80) + (rgb >> 7)] >>> brightness) != 0) {
                dest[offset] = color;
                if (saveDepth) {
                    depthBuffer[offset] = z1;
                }
            }
            z1 += z2;
            offset++;
            rgb += pixelBrightness;
            pos += pixelPos;
        }
    }

    public static void drawTexturedTriangle(int y1, int y2, int y3, int x1, int x2, int x3, int c1, int c2, int c3,
                                            int tx1, int tx2, int tx3, int ty1, int ty2, int ty3, int tz1, int tz2, int tz3, int tex, float z1, float z2,
                                            float z3) {
        if (!saveDepth) {
            z1 = z2 = z3 = 0;
        }
        if ((tex == 8 || tex == 30 || tex == 33 || tex == 41) || !aBoolean1464) {
            drawTexturedTriangle317(y1, y2, y3, x1, x2, x3, c1, c2, c3, tx1, tx2, tx3, ty1, ty2, ty3, tz1, tz2, tz3,
                    tex, z1, z2, z3);
            return;
        }
        c1 = 0x7f - c1 << 1;
        c2 = 0x7f - c2 << 1;
        c3 = 0x7f - c3 << 1;
        int texels[] = getTexturePixels(tex);
        aBoolean1463 = !textureIsTransparant[tex];
        tx2 = tx1 - tx2;
        ty2 = ty1 - ty2;
        tz2 = tz1 - tz2;
        tx3 -= tx1;
        ty3 -= ty1;
        tz3 -= tz1;
        int l4 = (tx3 * ty1 - ty3 * tx1) * WorldController.focalLength << 5;
        int i5 = ty3 * tz1 - tz3 * ty1 << 8;
        int j5 = tz3 * tx1 - tx3 * tz1 << 5;
        int k5 = (tx2 * ty1 - ty2 * tx1) * WorldController.focalLength << 5;
        ;
        int l5 = ty2 * tz1 - tz2 * ty1 << 8;
        int i6 = tz2 * tx1 - tx2 * tz1 << 5;
        int j6 = (ty2 * tx3 - tx2 * ty3) * WorldController.focalLength << 5;
        ;
        int k6 = tz2 * ty3 - ty2 * tz3 << 8;
        int l6 = tx2 * tz3 - tz2 * tx3 << 5;
        int i7 = 0;
        int j7 = 0;
        if (y2 != y1) {
            i7 = (x2 - x1 << 16) / (y2 - y1);
            j7 = (c2 - c1 << 16) / (y2 - y1);
        }
        int k7 = 0;
        int l7 = 0;
        if (y3 != y2) {
            k7 = (x3 - x2 << 16) / (y3 - y2);
            l7 = (c3 - c2 << 16) / (y3 - y2);
        }
        int i8 = 0;
        int j8 = 0;
        if (y3 != y1) {
            i8 = (x1 - x3 << 16) / (y1 - y3);
            j8 = (c1 - c3 << 16) / (y1 - y3);
        }

        float x21 = x2 - x1;
        float y32 = y2 - y1;
        float x31 = x3 - x1;
        float y31 = y3 - y1;
        float z21 = z2 - z1;
        float z31 = z3 - z1;

        float div = x21 * y31 - x31 * y32;
        float depthSlope = (z21 * y31 - z31 * y32) / div;
        float depthScale = (z31 * x21 - z21 * x31) / div;

        if (y1 <= y2 && y1 <= y3) {
            if (y1 >= DrawingArea.bottomY) {
                return;
            }
            if (y2 > DrawingArea.bottomY) {
                y2 = DrawingArea.bottomY;
            }
            if (y3 > DrawingArea.bottomY) {
                y3 = DrawingArea.bottomY;
            }
            z1 = z1 - depthSlope * x1 + depthSlope;
            if (y2 < y3) {
                x3 = x1 <<= 16;
                c3 = c1 <<= 16;
                if (y1 < 0) {
                    x3 -= i8 * y1;
                    x1 -= i7 * y1;
                    z1 -= depthScale * y1;
                    c3 -= j8 * y1;
                    c1 -= j7 * y1;
                    y1 = 0;
                }
                x2 <<= 16;
                c2 <<= 16;
                if (y2 < 0) {
                    x2 -= k7 * y2;
                    c2 -= l7 * y2;
                    y2 = 0;
                }
                int k8 = y1 - textureInt2;
                l4 += j5 * k8;
                k5 += i6 * k8;
                j6 += l6 * k8;
                if (y1 != y2 && i8 < i7 || y1 == y2 && i8 > k7) {
                    y3 -= y2;
                    y2 -= y1;
                    y1 = anIntArray1472[y1];
                    while (--y2 >= 0) {
                        drawTexturedScanline(DrawingArea.pixels, texels, y1, x3 >> 16, x1 >> 16, c3, c1, l4, k5, j6, i5,
                                l5, k6, z1, depthSlope);
                        z1 += depthScale;
                        x3 += i8;
                        x1 += i7;
                        c3 += j8;
                        c1 += j7;
                        y1 += DrawingArea.width;
                        l4 += j5;
                        k5 += i6;
                        j6 += l6;
                    }
                    while (--y3 >= 0) {
                        drawTexturedScanline(DrawingArea.pixels, texels, y1, x3 >> 16, x2 >> 16, c3, c2, l4, k5, j6, i5,
                                l5, k6, z1, depthSlope);
                        z1 += depthScale;
                        x3 += i8;
                        x2 += k7;
                        c3 += j8;
                        c2 += l7;
                        y1 += DrawingArea.width;
                        l4 += j5;
                        k5 += i6;
                        j6 += l6;
                    }
                    return;
                }
                y3 -= y2;
                y2 -= y1;
                y1 = anIntArray1472[y1];
                while (--y2 >= 0) {
                    drawTexturedScanline(DrawingArea.pixels, texels, y1, x1 >> 16, x3 >> 16, c1, c3, l4, k5, j6, i5, l5,
                            k6, z1, depthSlope);
                    z1 += depthScale;
                    x3 += i8;
                    x1 += i7;
                    c3 += j8;
                    c1 += j7;
                    y1 += DrawingArea.width;
                    l4 += j5;
                    k5 += i6;
                    j6 += l6;
                }
                while (--y3 >= 0) {
                    drawTexturedScanline(DrawingArea.pixels, texels, y1, x2 >> 16, x3 >> 16, c2, c3, l4, k5, j6, i5, l5,
                            k6, z1, depthSlope);
                    z1 += depthScale;
                    x3 += i8;
                    x2 += k7;
                    c3 += j8;
                    c2 += l7;
                    y1 += DrawingArea.width;
                    l4 += j5;
                    k5 += i6;
                    j6 += l6;
                }
                return;
            }
            x2 = x1 <<= 16;
            c2 = c1 <<= 16;
            if (y1 < 0) {
                x2 -= i8 * y1;
                z1 -= depthScale * y1;
                x1 -= i7 * y1;
                c2 -= j8 * y1;
                c1 -= j7 * y1;
                y1 = 0;
            }
            x3 <<= 16;
            c3 <<= 16;
            if (y3 < 0) {
                x3 -= k7 * y3;
                c3 -= l7 * y3;
                y3 = 0;
            }
            int l8 = y1 - textureInt2;
            l4 += j5 * l8;
            k5 += i6 * l8;
            j6 += l6 * l8;
            if (y1 != y3 && i8 < i7 || y1 == y3 && k7 > i7) {
                y2 -= y3;
                y3 -= y1;
                y1 = anIntArray1472[y1];
                while (--y3 >= 0) {
                    drawTexturedScanline(DrawingArea.pixels, texels, y1, x2 >> 16, x1 >> 16, c2, c1, l4, k5, j6, i5, l5,
                            k6, z1, depthSlope);
                    z1 += depthScale;
                    x2 += i8;
                    x1 += i7;
                    c2 += j8;
                    c1 += j7;
                    y1 += DrawingArea.width;
                    l4 += j5;
                    k5 += i6;
                    j6 += l6;
                }
                while (--y2 >= 0) {
                    drawTexturedScanline(DrawingArea.pixels, texels, y1, x3 >> 16, x1 >> 16, c3, c1, l4, k5, j6, i5, l5,
                            k6, z1, depthSlope);
                    z1 += depthScale;
                    x3 += k7;
                    x1 += i7;
                    c3 += l7;
                    c1 += j7;
                    y1 += DrawingArea.width;
                    l4 += j5;
                    k5 += i6;
                    j6 += l6;
                }
                return;
            }
            y2 -= y3;
            y3 -= y1;
            y1 = anIntArray1472[y1];
            while (--y3 >= 0) {
                drawTexturedScanline(DrawingArea.pixels, texels, y1, x1 >> 16, x2 >> 16, c1, c2, l4, k5, j6, i5, l5, k6,
                        z1, depthSlope);
                z1 += depthScale;
                x2 += i8;
                x1 += i7;
                c2 += j8;
                c1 += j7;
                y1 += DrawingArea.width;
                l4 += j5;
                k5 += i6;
                j6 += l6;
            }
            while (--y2 >= 0) {
                drawTexturedScanline(DrawingArea.pixels, texels, y1, x1 >> 16, x3 >> 16, c1, c3, l4, k5, j6, i5, l5, k6,
                        z1, depthSlope);
                z1 += depthScale;
                x3 += k7;
                x1 += i7;
                c3 += l7;
                c1 += j7;
                y1 += DrawingArea.width;
                l4 += j5;
                k5 += i6;
                j6 += l6;
            }
            return;
        }
        if (y2 <= y3) {
            if (y2 >= DrawingArea.bottomY) {
                return;
            }
            if (y3 > DrawingArea.bottomY) {
                y3 = DrawingArea.bottomY;
            }
            if (y1 > DrawingArea.bottomY) {
                y1 = DrawingArea.bottomY;
            }
            z2 = z2 - depthSlope * x2 + depthSlope;
            if (y3 < y1) {
                x1 = x2 <<= 16;
                c1 = c2 <<= 16;
                if (y2 < 0) {
                    x1 -= i7 * y2;
                    x2 -= k7 * y2;
                    z2 -= depthScale * y2;
                    c1 -= j7 * y2;
                    c2 -= l7 * y2;
                    y2 = 0;
                }
                x3 <<= 16;
                c3 <<= 16;
                if (y3 < 0) {
                    x3 -= i8 * y3;
                    c3 -= j8 * y3;
                    y3 = 0;
                }
                int i9 = y2 - textureInt2;
                l4 += j5 * i9;
                k5 += i6 * i9;
                j6 += l6 * i9;
                if (y2 != y3 && i7 < k7 || y2 == y3 && i7 > i8) {
                    y1 -= y3;
                    y3 -= y2;
                    y2 = anIntArray1472[y2];
                    while (--y3 >= 0) {
                        drawTexturedScanline(DrawingArea.pixels, texels, y2, x1 >> 16, x2 >> 16, c1, c2, l4, k5, j6, i5,
                                l5, k6, z2, depthSlope);
                        z2 += depthScale;
                        x1 += i7;
                        x2 += k7;
                        c1 += j7;
                        c2 += l7;
                        y2 += DrawingArea.width;
                        l4 += j5;
                        k5 += i6;
                        j6 += l6;
                    }
                    while (--y1 >= 0) {
                        drawTexturedScanline(DrawingArea.pixels, texels, y2, x1 >> 16, x3 >> 16, c1, c3, l4, k5, j6, i5,
                                l5, k6, z2, depthSlope);
                        z2 += depthScale;
                        x1 += i7;
                        x3 += i8;
                        c1 += j7;
                        c3 += j8;
                        y2 += DrawingArea.width;
                        l4 += j5;
                        k5 += i6;
                        j6 += l6;
                    }
                    return;
                }
                y1 -= y3;
                y3 -= y2;
                y2 = anIntArray1472[y2];
                while (--y3 >= 0) {
                    drawTexturedScanline(DrawingArea.pixels, texels, y2, x2 >> 16, x1 >> 16, c2, c1, l4, k5, j6, i5, l5,
                            k6, z2, depthSlope);
                    z2 += depthScale;
                    x1 += i7;
                    x2 += k7;
                    c1 += j7;
                    c2 += l7;
                    y2 += DrawingArea.width;
                    l4 += j5;
                    k5 += i6;
                    j6 += l6;
                }
                while (--y1 >= 0) {
                    drawTexturedScanline(DrawingArea.pixels, texels, y2, x3 >> 16, x1 >> 16, c3, c1, l4, k5, j6, i5, l5,
                            k6, z2, depthSlope);
                    z2 += depthScale;
                    x1 += i7;
                    x3 += i8;
                    c1 += j7;
                    c3 += j8;
                    y2 += DrawingArea.width;
                    l4 += j5;
                    k5 += i6;
                    j6 += l6;
                }
                return;
            }
            x3 = x2 <<= 16;
            c3 = c2 <<= 16;
            if (y2 < 0) {
                x3 -= i7 * y2;
                z2 -= depthScale * y2;
                x2 -= k7 * y2;
                c3 -= j7 * y2;
                c2 -= l7 * y2;
                y2 = 0;
            }
            x1 <<= 16;
            c1 <<= 16;
            if (y1 < 0) {
                x1 -= i8 * y1;
                c1 -= j8 * y1;
                y1 = 0;
            }
            int j9 = y2 - textureInt2;
            l4 += j5 * j9;
            k5 += i6 * j9;
            j6 += l6 * j9;
            if (i7 < k7) {
                y3 -= y1;
                y1 -= y2;
                y2 = anIntArray1472[y2];
                while (--y1 >= 0) {
                    drawTexturedScanline(DrawingArea.pixels, texels, y2, x3 >> 16, x2 >> 16, c3, c2, l4, k5, j6, i5, l5,
                            k6, z2, depthSlope);
                    z2 += depthScale;
                    x3 += i7;
                    x2 += k7;
                    c3 += j7;
                    c2 += l7;
                    y2 += DrawingArea.width;
                    l4 += j5;
                    k5 += i6;
                    j6 += l6;
                }
                while (--y3 >= 0) {
                    drawTexturedScanline(DrawingArea.pixels, texels, y2, x1 >> 16, x2 >> 16, c1, c2, l4, k5, j6, i5, l5,
                            k6, z2, depthSlope);
                    z2 += depthScale;
                    x1 += i8;
                    x2 += k7;
                    c1 += j8;
                    c2 += l7;
                    y2 += DrawingArea.width;
                    l4 += j5;
                    k5 += i6;
                    j6 += l6;
                }
                return;
            }
            y3 -= y1;
            y1 -= y2;
            y2 = anIntArray1472[y2];
            while (--y1 >= 0) {
                drawTexturedScanline(DrawingArea.pixels, texels, y2, x2 >> 16, x3 >> 16, c2, c3, l4, k5, j6, i5, l5, k6,
                        z2, depthSlope);
                z2 += depthScale;
                x3 += i7;
                x2 += k7;
                c3 += j7;
                c2 += l7;
                y2 += DrawingArea.width;
                l4 += j5;
                k5 += i6;
                j6 += l6;
            }
            while (--y3 >= 0) {
                drawTexturedScanline(DrawingArea.pixels, texels, y2, x2 >> 16, x1 >> 16, c2, c1, l4, k5, j6, i5, l5, k6,
                        z2, depthSlope);
                z2 += depthScale;
                x1 += i8;
                x2 += k7;
                c1 += j8;
                c2 += l7;
                y2 += DrawingArea.width;
                l4 += j5;
                k5 += i6;
                j6 += l6;
            }
            return;
        }
        if (y3 >= DrawingArea.bottomY) {
            return;
        }
        if (y1 > DrawingArea.bottomY) {
            y1 = DrawingArea.bottomY;
        }
        if (y2 > DrawingArea.bottomY) {
            y2 = DrawingArea.bottomY;
        }
        z3 = z3 - depthSlope * x3 + depthSlope;
        if (y1 < y2) {
            x2 = x3 <<= 16;
            c2 = c3 <<= 16;
            if (y3 < 0) {
                x2 -= k7 * y3;
                x3 -= i8 * y3;
                z3 -= depthScale * y3;
                c2 -= l7 * y3;
                c3 -= j8 * y3;
                y3 = 0;
            }
            x1 <<= 16;
            c1 <<= 16;
            if (y1 < 0) {
                x1 -= i7 * y1;
                c1 -= j7 * y1;
                y1 = 0;
            }
            int k9 = y3 - textureInt2;
            l4 += j5 * k9;
            k5 += i6 * k9;
            j6 += l6 * k9;
            if (k7 < i8) {
                y2 -= y1;
                y1 -= y3;
                y3 = anIntArray1472[y3];
                while (--y1 >= 0) {
                    drawTexturedScanline(DrawingArea.pixels, texels, y3, x2 >> 16, x3 >> 16, c2, c3, l4, k5, j6, i5, l5,
                            k6, z3, depthSlope);
                    z3 += depthScale;
                    x2 += k7;
                    x3 += i8;
                    c2 += l7;
                    c3 += j8;
                    y3 += DrawingArea.width;
                    l4 += j5;
                    k5 += i6;
                    j6 += l6;
                }
                while (--y2 >= 0) {
                    drawTexturedScanline(DrawingArea.pixels, texels, y3, x2 >> 16, x1 >> 16, c2, c1, l4, k5, j6, i5, l5,
                            k6, z3, depthSlope);
                    z3 += depthScale;
                    x2 += k7;
                    x1 += i7;
                    c2 += l7;
                    c1 += j7;
                    y3 += DrawingArea.width;
                    l4 += j5;
                    k5 += i6;
                    j6 += l6;
                }
                return;
            }
            y2 -= y1;
            y1 -= y3;
            y3 = anIntArray1472[y3];
            while (--y1 >= 0) {
                drawTexturedScanline(DrawingArea.pixels, texels, y3, x3 >> 16, x2 >> 16, c3, c2, l4, k5, j6, i5, l5, k6,
                        z3, depthSlope);
                z3 += depthScale;
                x2 += k7;
                x3 += i8;
                c2 += l7;
                c3 += j8;
                y3 += DrawingArea.width;
                l4 += j5;
                k5 += i6;
                j6 += l6;
            }
            while (--y2 >= 0) {
                drawTexturedScanline(DrawingArea.pixels, texels, y3, x1 >> 16, x2 >> 16, c1, c2, l4, k5, j6, i5, l5, k6,
                        z3, depthSlope);
                z3 += depthScale;
                x2 += k7;
                x1 += i7;
                c2 += l7;
                c1 += j7;
                y3 += DrawingArea.width;
                l4 += j5;
                k5 += i6;
                j6 += l6;
            }
            return;
        }
        x1 = x3 <<= 16;
        c1 = c3 <<= 16;
        if (y3 < 0) {
            x1 -= k7 * y3;
            x3 -= i8 * y3;
            z3 -= depthScale * y3;
            c1 -= l7 * y3;
            c3 -= j8 * y3;
            y3 = 0;
        }
        x2 <<= 16;
        c2 <<= 16;
        if (y2 < 0) {
            x2 -= i7 * y2;
            c2 -= j7 * y2;
            y2 = 0;
        }
        int l9 = y3 - textureInt2;
        l4 += j5 * l9;
        k5 += i6 * l9;
        j6 += l6 * l9;
        if (k7 < i8) {
            y1 -= y2;
            y2 -= y3;
            y3 = anIntArray1472[y3];
            while (--y2 >= 0) {
                drawTexturedScanline(DrawingArea.pixels, texels, y3, x1 >> 16, x3 >> 16, c1, c3, l4, k5, j6, i5, l5, k6,
                        z3, depthSlope);
                z3 += depthScale;
                x1 += k7;
                x3 += i8;
                c1 += l7;
                c3 += j8;
                y3 += DrawingArea.width;
                l4 += j5;
                k5 += i6;
                j6 += l6;
            }
            while (--y1 >= 0) {
                drawTexturedScanline(DrawingArea.pixels, texels, y3, x2 >> 16, x3 >> 16, c2, c3, l4, k5, j6, i5, l5, k6,
                        z3, depthSlope);
                z3 += depthScale;
                x2 += i7;
                x3 += i8;
                c2 += j7;
                c3 += j8;
                y3 += DrawingArea.width;
                l4 += j5;
                k5 += i6;
                j6 += l6;
            }
            return;
        }
        y1 -= y2;
        y2 -= y3;
        y3 = anIntArray1472[y3];
        while (--y2 >= 0) {
            drawTexturedScanline(DrawingArea.pixels, texels, y3, x3 >> 16, x1 >> 16, c3, c1, l4, k5, j6, i5, l5, k6, z3,
                    depthSlope);
            z3 += depthScale;
            x1 += k7;
            x3 += i8;
            c1 += l7;
            c3 += j8;
            y3 += DrawingArea.width;
            l4 += j5;
            k5 += i6;
            j6 += l6;
        }
        while (--y1 >= 0) {
            drawTexturedScanline(DrawingArea.pixels, texels, y3, x3 >> 16, x2 >> 16, c3, c2, l4, k5, j6, i5, l5, k6, z3,
                    depthSlope);
            z3 += depthScale;
            x2 += i7;
            x3 += i8;
            c2 += j7;
            c3 += j8;
            y3 += DrawingArea.width;
            l4 += j5;
            k5 += i6;
            j6 += l6;
        }
    }

    private static void drawTexturedScanline(int dest[], int src[], int offset, int x1, int x2, int hsl1, int hsl2,
                                             int t1, int t2, int t3, int t4, int t5, int t6, float z1, float z2) {
        int darken = 0;
        int srcPos = 0;
        if (x1 >= x2) {
            return;
        }
        int dl = (hsl2 - hsl1) / (x2 - x1);
        int n;
        if (aBoolean1462) {
            if (x2 > DrawingArea.centerX) {
                x2 = DrawingArea.centerX;
            }
            if (x1 < 0) {
                hsl1 -= x1 * dl;
                x1 = 0;
            }
        }
        if (x1 >= x2) {
            return;
        }
        n = x2 - x1 >> 3;
        offset += x1;
        z1 += z2 * x1;
        int j4 = 0;
        int l4 = 0;
        int l6 = x1 - textureInt1;
        t1 += (t4 >> 3) * l6;
        t2 += (t5 >> 3) * l6;
        t3 += (t6 >> 3) * l6;
        int l5 = t3 >> 14;
        if (l5 != 0) {
            darken = t1 / l5;
            srcPos = t2 / l5;
            if (darken < 0) {
                darken = 0;
            } else if (darken > 16256) {
                darken = 16256;
            }
        }
        t1 += t4;
        t2 += t5;
        t3 += t6;
        l5 = t3 >> 14;
        if (l5 != 0) {
            j4 = t1 / l5;
            l4 = t2 / l5;
            if (j4 < 7) {
                j4 = 7;
            } else if (j4 > 16256) {
                j4 = 16256;
            }
        }
        int j7 = j4 - darken >> 3;
        int l7 = l4 - srcPos >> 3;
        if (aBoolean1463) {
            while (n-- > 0) {
                int rgb;
                int l;
                rgb = src[(srcPos & 0x3f80) + (darken >> 7)];
                l = hsl1 >> 16;
                dest[offset] = ((rgb & 0xff00ff) * l & ~0xff00ff) + ((rgb & 0xff00) * l & 0xff0000) >> 8;
                if (saveDepth) {
                    depthBuffer[offset] = z1;
                }
                offset++;
                z1 += z2;
                darken += j7;
                srcPos += l7;
                hsl1 += dl;
                rgb = src[(srcPos & 0x3f80) + (darken >> 7)];
                l = hsl1 >> 16;
                dest[offset] = ((rgb & 0xff00ff) * l & ~0xff00ff) + ((rgb & 0xff00) * l & 0xff0000) >> 8;
                if (saveDepth) {
                    depthBuffer[offset] = z1;
                }
                offset++;
                z1 += z2;
                darken += j7;
                srcPos += l7;
                hsl1 += dl;
                rgb = src[(srcPos & 0x3f80) + (darken >> 7)];
                l = hsl1 >> 16;
                dest[offset] = ((rgb & 0xff00ff) * l & ~0xff00ff) + ((rgb & 0xff00) * l & 0xff0000) >> 8;
                if (saveDepth) {
                    depthBuffer[offset] = z1;
                }
                offset++;
                z1 += z2;
                darken += j7;
                srcPos += l7;
                hsl1 += dl;
                rgb = src[(srcPos & 0x3f80) + (darken >> 7)];
                l = hsl1 >> 16;
                dest[offset] = ((rgb & 0xff00ff) * l & ~0xff00ff) + ((rgb & 0xff00) * l & 0xff0000) >> 8;
                if (saveDepth) {
                    depthBuffer[offset] = z1;
                }
                offset++;
                z1 += z2;
                darken += j7;
                srcPos += l7;
                hsl1 += dl;
                rgb = src[(srcPos & 0x3f80) + (darken >> 7)];
                l = hsl1 >> 16;
                dest[offset] = ((rgb & 0xff00ff) * l & ~0xff00ff) + ((rgb & 0xff00) * l & 0xff0000) >> 8;
                if (saveDepth) {
                    depthBuffer[offset] = z1;
                }
                offset++;
                z1 += z2;
                darken += j7;
                srcPos += l7;
                hsl1 += dl;
                rgb = src[(srcPos & 0x3f80) + (darken >> 7)];
                l = hsl1 >> 16;
                dest[offset] = ((rgb & 0xff00ff) * l & ~0xff00ff) + ((rgb & 0xff00) * l & 0xff0000) >> 8;
                if (saveDepth) {
                    depthBuffer[offset] = z1;
                }
                offset++;
                z1 += z2;
                darken += j7;
                srcPos += l7;
                hsl1 += dl;
                rgb = src[(srcPos & 0x3f80) + (darken >> 7)];
                l = hsl1 >> 16;
                dest[offset] = ((rgb & 0xff00ff) * l & ~0xff00ff) + ((rgb & 0xff00) * l & 0xff0000) >> 8;
                if (saveDepth) {
                    depthBuffer[offset] = z1;
                }
                offset++;
                z1 += z2;
                darken += j7;
                srcPos += l7;
                hsl1 += dl;
                rgb = src[(srcPos & 0x3f80) + (darken >> 7)];
                l = hsl1 >> 16;
                dest[offset] = ((rgb & 0xff00ff) * l & ~0xff00ff) + ((rgb & 0xff00) * l & 0xff0000) >> 8;
                if (saveDepth) {
                    depthBuffer[offset] = z1;
                }
                offset++;
                z1 += z2;
                darken += j7;
                srcPos += l7;
                hsl1 += dl;
                t1 += t4;
                t2 += t5;
                t3 += t6;
                int i6 = t3 >> 14;
                if (i6 != 0) {
                    j4 = t1 / i6;
                    l4 = t2 / i6;
                    if (j4 < 7) {
                        j4 = 7;
                    } else if (j4 > 16256) {
                        j4 = 16256;
                    }
                }
                j7 = j4 - darken >> 3;
                l7 = l4 - srcPos >> 3;
                hsl1 += dl;
            }
            for (n = x2 - x1 & 7; n-- > 0;) {
                int rgb;
                int l;
                rgb = src[(srcPos & 0x3f80) + (darken >> 7)];
                l = hsl1 >> 16;
                dest[offset] = ((rgb & 0xff00ff) * l & ~0xff00ff) + ((rgb & 0xff00) * l & 0xff0000) >> 8;
                if (saveDepth) {
                    depthBuffer[offset] = z1;
                }
                z1 += z2;
                offset++;
                darken += j7;
                srcPos += l7;
                hsl1 += dl;
            }
            return;
        }
        while (n-- > 0) {
            int i9;
            int l;
            if ((i9 = src[(srcPos & 0x3f80) + (darken >> 7)]) != 0) {
                l = hsl1 >> 16;
                dest[offset] = ((i9 & 0xff00ff) * l & ~0xff00ff) + ((i9 & 0xff00) * l & 0xff0000) >> 8;
                if (saveDepth) {
                    depthBuffer[offset] = z1;
                }
            }
            z1 += z2;
            offset++;
            darken += j7;
            srcPos += l7;
            hsl1 += dl;
            if ((i9 = src[(srcPos & 0x3f80) + (darken >> 7)]) != 0) {
                l = hsl1 >> 16;
                dest[offset] = ((i9 & 0xff00ff) * l & ~0xff00ff) + ((i9 & 0xff00) * l & 0xff0000) >> 8;
                if (saveDepth) {
                    depthBuffer[offset] = z1;
                }
            }
            z1 += z2;
            offset++;
            darken += j7;
            srcPos += l7;
            hsl1 += dl;
            if ((i9 = src[(srcPos & 0x3f80) + (darken >> 7)]) != 0) {
                l = hsl1 >> 16;
                dest[offset] = ((i9 & 0xff00ff) * l & ~0xff00ff) + ((i9 & 0xff00) * l & 0xff0000) >> 8;
                if (saveDepth) {
                    depthBuffer[offset] = z1;
                }
            }
            z1 += z2;
            offset++;
            darken += j7;
            srcPos += l7;
            hsl1 += dl;
            if ((i9 = src[(srcPos & 0x3f80) + (darken >> 7)]) != 0) {
                l = hsl1 >> 16;
                dest[offset] = ((i9 & 0xff00ff) * l & ~0xff00ff) + ((i9 & 0xff00) * l & 0xff0000) >> 8;
                if (saveDepth) {
                    depthBuffer[offset] = z1;
                }
            }
            z1 += z2;
            offset++;
            darken += j7;
            srcPos += l7;
            hsl1 += dl;
            if ((i9 = src[(srcPos & 0x3f80) + (darken >> 7)]) != 0) {
                l = hsl1 >> 16;
                dest[offset] = ((i9 & 0xff00ff) * l & ~0xff00ff) + ((i9 & 0xff00) * l & 0xff0000) >> 8;
                if (saveDepth) {
                    depthBuffer[offset] = z1;
                }
            }
            z1 += z2;
            offset++;
            darken += j7;
            srcPos += l7;
            hsl1 += dl;
            if ((i9 = src[(srcPos & 0x3f80) + (darken >> 7)]) != 0) {
                l = hsl1 >> 16;
                dest[offset] = ((i9 & 0xff00ff) * l & ~0xff00ff) + ((i9 & 0xff00) * l & 0xff0000) >> 8;
                if (saveDepth) {
                    depthBuffer[offset] = z1;
                }
            }
            z1 += z2;
            offset++;
            darken += j7;
            srcPos += l7;
            hsl1 += dl;
            if ((i9 = src[(srcPos & 0x3f80) + (darken >> 7)]) != 0) {
                l = hsl1 >> 16;
                dest[offset] = ((i9 & 0xff00ff) * l & ~0xff00ff) + ((i9 & 0xff00) * l & 0xff0000) >> 8;
                if (saveDepth) {
                    depthBuffer[offset] = z1;
                }
            }
            z1 += z2;
            offset++;
            darken += j7;
            srcPos += l7;
            hsl1 += dl;
            if ((i9 = src[(srcPos & 0x3f80) + (darken >> 7)]) != 0) {
                l = hsl1 >> 16;
                dest[offset] = ((i9 & 0xff00ff) * l & ~0xff00ff) + ((i9 & 0xff00) * l & 0xff0000) >> 8;
                if (saveDepth) {
                    depthBuffer[offset] = z1;
                }
            }
            z1 += z2;
            offset++;
            darken += j7;
            srcPos += l7;
            hsl1 += dl;
            t1 += t4;
            t2 += t5;
            t3 += t6;
            int j6 = t3 >> 14;
            if (j6 != 0) {
                j4 = t1 / j6;
                l4 = t2 / j6;
                if (j4 < 7) {
                    j4 = 7;
                } else if (j4 > 16256) {
                    j4 = 16256;
                }
            }
            j7 = j4 - darken >> 3;
            l7 = l4 - srcPos >> 3;
            hsl1 += dl;
        }
        for (int l3 = x2 - x1 & 7; l3-- > 0;) {
            int j9;
            int l;
            if ((j9 = src[(srcPos & 0x3f80) + (darken >> 7)]) != 0) {
                l = hsl1 >> 16;
                dest[offset] = ((j9 & 0xff00ff) * l & ~0xff00ff) + ((j9 & 0xff00) * l & 0xff0000) >> 8;
                if (saveDepth) {
                    depthBuffer[offset] = z1;
                }
            }
            z1 += z2;
            offset++;
            darken += j7;
            srcPos += l7;
            hsl1 += dl;
        }
    }
}
