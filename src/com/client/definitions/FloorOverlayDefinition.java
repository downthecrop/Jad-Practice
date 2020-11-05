package com.client.definitions;

import com.client.utilities.FileOperations;
import com.client.Stream;
import com.client.StreamLoader;
import com.client.sign.Signlink;

public class FloorOverlayDefinition {

    public static void unpackConfig(StreamLoader streamLoader) {
        Stream stream = new Stream(FileOperations.readFile(Signlink.getCacheDirectory() + "/data/flo2.dat"));
        int cacheSize = stream.readUnsignedWord();
        if (overlays == null)
            overlays = new FloorOverlayDefinition[cacheSize];
        for (int j = 0; j < cacheSize; j++) {
            if (overlays[j] == null) {
                overlays[j] = new FloorOverlayDefinition();
            }
            overlays[j].readValues(stream);
        }
    }

    private void readValues(Stream stream) {
        do {
            int opcode = stream.readUnsignedByte();
            if (opcode == 0)
                return;
            else if (opcode == 1) {
                rgb = stream.read3Bytes();
                rgbToHsl(rgb);
            } else if (opcode == 2) {
                texture = stream.readUnsignedByte();
            } else if (opcode == 5) {
                occlude = false;
            } else if (opcode == 7) {
                anotherRgb = stream.read3Bytes();
                rgbToHsl(anotherRgb);
            } else {
                System.out.println("Error unrecognised floor overlay config code: " + opcode);
            }
        } while (true);
    }

    private void rgbToHsl(int rgb) {
        double r = (rgb >> 16 & 0xff) / 256.0;
        double g = (rgb >> 8 & 0xff) / 256.0;
        double b = (rgb & 0xff) / 256.0;
        double min = r;
        if (g < min) {
            min = g;
        }
        if (b < min) {
            min = b;
        }
        double max = r;
        if (g > max) {
            max = g;
        }
        if (b > max) {
            max = b;
        }
        double h = 0.0;
        double s = 0.0;
        double l = (min + max) / 2.0;
        if (min != max) {
            if (l < 0.5) {
                s = (max - min) / (max + min);
            }
            if (l >= 0.5) {
                s = (max - min) / (2.0 - max - min);
            }
            if (r == max) {
                h = (g - b) / (max - min);
            } else if (g == max) {
                h = 2.0 + (b - r) / (max - min);
            } else if (b == max) {
                h = 4.0 + (r - g) / (max - min);
            }
        }
        h /= 6.0;
        hue = (int) (h * 256.0);
        saturation = (int) (s * 256.0);
        luminance = (int) (l * 256.0);
        if (saturation < 0) {
            saturation = 0;
        } else if (saturation > 255) {
            saturation = 255;
        }
        if (luminance < 0) {
            luminance = 0;
        } else if (luminance > 255) {
            luminance = 255;
        }
        if (l > 0.5) {
            blendHueMultiplier = (int) ((1.0 - l) * s * 512.0);
        } else {
            blendHueMultiplier = (int) (l * s * 512.0);
        }
        if (blendHueMultiplier < 1) {
            blendHueMultiplier = 1;
        }
        blendHue = (int) (h * blendHueMultiplier);
        hsl16 = hsl24to16(hue, saturation, luminance);
    }

    public final static int hsl24to16(int h, int s, int l) {
        if (l > 179) {
            s /= 2;
        }
        if (l > 192) {
            s /= 2;
        }
        if (l > 217) {
            s /= 2;
        }
        if (l > 243) {
            s /= 2;
        }
        return (h / 4 << 10) + (s / 32 << 7) + l / 2;
    }

    private FloorOverlayDefinition() {
        texture = -1;
        occlude = true;
        anotherRgb = -1;
    }

    public static FloorOverlayDefinition overlays[];
    public int rgb;
    public int texture;
    public boolean occlude;
    public int hue;
    public int saturation;
    public int luminance;
    public int blendHue;
    public int blendHueMultiplier;
    public int hsl16;
    public int anotherRgb;

}
