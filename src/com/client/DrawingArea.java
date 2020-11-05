package com.client;


import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.image.Raster;

public class DrawingArea extends NodeSub {

	public static void initDrawingArea(int i, int j, int[] ai, float[] depth) {
		pixels = ai;
		width = j;
		height = i;
		setDrawingArea(i, 0, j, 0);
	}

	public static void drawTransparentBox(int leftX, int topY, int width, int height, int rgbColour, int opacity){
		if(leftX < DrawingArea.topX){
			width -= DrawingArea.topX - leftX;
			leftX = DrawingArea.topX;
		}
		if(topY < DrawingArea.topY){
			height -= DrawingArea.topY - topY;
			topY = DrawingArea.topY;
		}
		if(leftX + width > bottomX)
			width = bottomX - leftX;
		if(topY + height > bottomY)
			height = bottomY - topY;
		int transparency = 256 - opacity;
		int red = (rgbColour >> 16 & 0xff) * opacity;
		int green = (rgbColour >> 8 & 0xff) * opacity;
		int blue = (rgbColour & 0xff) * opacity;
		int leftOver = DrawingArea.width - width;
		int pixelIndex = leftX + topY * DrawingArea.width;
		for(int rowIndex = 0; rowIndex < height; rowIndex++){
			for(int columnIndex = 0; columnIndex < width; columnIndex++){
				int otherRed = (pixels[pixelIndex] >> 16 & 0xff) * transparency;
				int otherGreen = (pixels[pixelIndex] >> 8 & 0xff) * transparency;
				int otherBlue = (pixels[pixelIndex] & 0xff) * transparency;
				int transparentColour = ((red + otherRed >> 8) << 16) + ((green + otherGreen >> 8) << 8) + (blue + otherBlue >> 8);
				pixels[pixelIndex++] = transparentColour;
			}
			pixelIndex += leftOver;
		}
	}
	public static void clear(int color) {
		int length = width * height;
		int mod = length - (length & 0x7);
		int offset = 0;
		while (offset < mod) {
			pixels[(offset++)] = color;
			pixels[(offset++)] = color;
			pixels[(offset++)] = color;
			pixels[(offset++)] = color;
			pixels[(offset++)] = color;
			pixels[(offset++)] = color;
			pixels[(offset++)] = color;
			pixels[(offset++)] = color;
		}
		while (offset < length) {
			pixels[(offset++)] = color;
		}
	}

	public static void method336(int i, int j, int k, int l, int i1) {
		if (k < topX) {
			i1 -= topX - k;
			k = topX;
		}
		if (j < topY) {
			i -= topY - j;
			j = topY;
		}
		if (k + i1 > bottomX)
			i1 = bottomX - k;
		if (j + i > bottomY)
			i = bottomY - j;
		int k1 = width - i1;
		int l1 = k + j * width;
		for (int i2 = -i; i2 < 0; i2++) {
			for (int j2 = -i1; j2 < 0; j2++)
				pixels[l1++] = l;

			l1 += k1;
		}

	}
    public static void drawRoundedRectangle(int x, int y, int width, int height, int color,
                                            int alpha, boolean filled, boolean shadowed) {
        if (shadowed)
            drawRoundedRectangle(x + 1, y + 1, width, height, 0, alpha, filled,
                    false);
        if (alpha == -1) {
            if (filled) {
                drawHorizontalLine(y + 1, color, width - 4, x + 2);// method339
                drawHorizontalLine(y + height - 2, color, width - 4, x + 2);// method339
                drawPixels(height - 4, y + 2, x + 1, color, width - 2);// method336
            }
            drawHorizontalLine(y, color, width - 4, x + 2);// method339
            drawHorizontalLine(y + height - 1, color, width - 4, x + 2);// method339
            method341(y + 2, color, height - 4, x);// method341
            method341(y + 2, color, height - 4, x + width - 1);// method341
            drawPixels(1, y + 1, x + 1, color, 1);// method336
            drawPixels(1, y + 1, x + width - 2, color, 1);// method336
            drawPixels(1, y + height - 2, x + width - 2, color, 1);// method336
            drawPixels(1, y + height - 2, x + 1, color, 1);// method336
        } else if (alpha != -1) {
            if (filled) {
                method340(color, width - 4, y + 1, alpha, x + 2);// method340
                method340(color, width - 4, y + height - 2, alpha, x + 2);// method340
                method335(color, y + 2, width - 2, height - 4, alpha, x + 1);// method335
            }
            method340(color, width - 4, y, alpha, x + 2);// method340
            method340(color, width - 4, y + height - 1, alpha, x + 2);// method340
            method342(color, x, alpha, y + 2, height - 4);// method342
            method342(color, x + width - 1, alpha, y + 2, height - 4);// method342
            method335(color, y + 1, 1, 1, alpha, x + 1);// method335
            method335(color, y + 1, 1, 1, alpha, x + width - 2);// method335
            method335(color, y + height - 2, 1, 1, alpha, x + 1);// method335
            method335(color, y + height - 2, 1, 1, alpha, x + width - 2);// method335
        }
    }

    public static void drawPixelsWithOpacity(int color, int yPos, int pixelWidth, int pixelHeight, int opacityLevel,
											 int xPos) {
		if (xPos < topX) {
			pixelWidth -= topX - xPos;
			xPos = topX;
		}
		if (yPos < topY) {
			pixelHeight -= topY - yPos;
			yPos = topY;
		}
		if (xPos + pixelWidth > bottomX) {
			pixelWidth = bottomX - xPos;
		}
		if (yPos + pixelHeight > bottomY) {
			pixelHeight = bottomY - yPos;
		}
		int l1 = 256 - opacityLevel;
		int i2 = (color >> 16 & 0xFF) * opacityLevel;
		int j2 = (color >> 8 & 0xFF) * opacityLevel;
		int k2 = (color & 0xFF) * opacityLevel;
		int k3 = width - pixelWidth;
		int l3 = xPos + yPos * width;
		if (l3 > pixels.length - 1) {
			l3 = pixels.length - 1;
		}
		for (int i4 = 0; i4 < pixelHeight; i4++) {
			for (int j4 = -pixelWidth; j4 < 0; j4++) {
				int l2 = (pixels[l3] >> 16 & 0xFF) * l1;
				int i3 = (pixels[l3] >> 8 & 0xFF) * l1;
				int j3 = (pixels[l3] & 0xFF) * l1;
				int k4 = (i2 + l2 >> 8 << 16) + (j2 + i3 >> 8 << 8) + (k2 + j3 >> 8);
				pixels[(l3++)] = k4;
			}
			l3 += k3;
		}
	}

	public static void method338(int i, int j, int k, int l, int i1, int j1) {
		method340(l, i1, i, k, j1);
		method340(l, i1, (i + j) - 1, k, j1);
		if (j >= 3) {
			method342(l, j1, k, i + 1, j - 2);
			method342(l, (j1 + i1) - 1, k, i + 1, j - 2);
		}
	}

	public static void method339(int i, int j, int k, int l) {
		if (i < topY || i >= bottomY)
			return;
		if (l < topX) {
			k -= topX - l;
			l = topX;
		}
		if (l + k > bottomX)
			k = bottomX - l;
		int i1 = l + i * width;
		for (int j1 = 0; j1 < k; j1++)
			pixels[i1 + j1] = j;

	}

	private static void method340(int i, int j, int k, int l, int i1) {
		if (k < topY || k >= bottomY)
			return;
		if (i1 < topX) {
			j -= topX - i1;
			i1 = topX;
		}
		if (i1 + j > bottomX)
			j = bottomX - i1;
		int j1 = 256 - l;
		int k1 = (i >> 16 & 0xff) * l;
		int l1 = (i >> 8 & 0xff) * l;
		int i2 = (i & 0xff) * l;
		int i3 = i1 + k * width;
		for (int j3 = 0; j3 < j; j3++) {
			int j2 = (pixels[i3] >> 16 & 0xff) * j1;
			int k2 = (pixels[i3] >> 8 & 0xff) * j1;
			int l2 = (pixels[i3] & 0xff) * j1;
			int k3 = ((k1 + j2 >> 8) << 16) + ((l1 + k2 >> 8) << 8) + (i2 + l2 >> 8);
			pixels[i3++] = k3;
		}

	}

	public static void method341(int i, int j, int k, int l) {
		if (l < topX || l >= bottomX)
			return;
		if (i < topY) {
			k -= topY - i;
			i = topY;
		}
		if (i + k > bottomY)
			k = bottomY - i;
		int j1 = l + i * width;
		for (int k1 = 0; k1 < k; k1++)
			pixels[j1 + k1 * width] = j;

	}

	private static void method342(int i, int j, int k, int l, int i1) {
		if (j < topX || j >= bottomX)
			return;
		if (l < topY) {
			i1 -= topY - l;
			l = topY;
		}
		if (l + i1 > bottomY)
			i1 = bottomY - l;
		int j1 = 256 - k;
		int k1 = (i >> 16 & 0xff) * k;
		int l1 = (i >> 8 & 0xff) * k;
		int i2 = (i & 0xff) * k;
		int i3 = j + l * width;
		for (int j3 = 0; j3 < i1; j3++) {
			int j2 = (pixels[i3] >> 16 & 0xff) * j1;
			int k2 = (pixels[i3] >> 8 & 0xff) * j1;
			int l2 = (pixels[i3] & 0xff) * j1;
			int k3 = ((k1 + j2 >> 8) << 16) + ((l1 + k2 >> 8) << 8) + (i2 + l2 >> 8);
			pixels[i3] = k3;
			i3 += width;
		}
	}
	public static void drawBox(int leftX, int topY, int width, int height, int rgbColour) {
		if (leftX < DrawingArea.topX) {
			width -= DrawingArea.topX - leftX;
			leftX = DrawingArea.topX;
		}
		if (topY < DrawingArea.topY) {
			height -= DrawingArea.topY - topY;
			topY = DrawingArea.topY;
		}
		if (leftX + width > bottomX)
			width = bottomX - leftX;
		if (topY + height > bottomY)
			height = bottomY - topY;
		int leftOver = DrawingArea.width - width;
		int pixelIndex = leftX + topY * DrawingArea.width;
		for (int rowIndex = 0; rowIndex < height; rowIndex++) {
			for (int columnIndex = 0; columnIndex < width; columnIndex++)
				pixels[pixelIndex++] = rgbColour;
			pixelIndex += leftOver;
		}
	}

	public static void drawVerticalLine2(int xPosition, int yPosition, int height, int rgbColour){
		if(xPosition < topX || xPosition >= bottomX)
			return;
		if(yPosition < topY){
			height -= topY - yPosition;
			yPosition = topY;
		}
		if(yPosition + height > bottomY)
			height = bottomY - yPosition;
		int pixelIndex = xPosition + yPosition * width;
		for(int rowIndex = 0; rowIndex < height; rowIndex++)
			pixels[pixelIndex + rowIndex * width] = rgbColour;
	}

	public static void drawHorizontalLine2(int xPosition, int yPosition, int width, int rgbColour){
		if(yPosition < topY || yPosition >= bottomY)
			return;
		if(xPosition < topX){
			width -= topX - xPosition;
			xPosition = topX;
		}
		if(xPosition + width > bottomX)
			width = bottomX - xPosition;
		int pixelIndex = xPosition + yPosition * DrawingArea.width;
		for(int i = 0; i < width; i++)
			pixels[pixelIndex + i] = rgbColour;
	}

	public static void drawBoxOutline(int leftX, int topY, int width, int height, int rgbColour){
		drawHorizontalLine2(leftX, topY, width, rgbColour);
		drawHorizontalLine2(leftX, (topY + height) - 1, width, rgbColour);
		drawVerticalLine2(leftX, topY, height, rgbColour);
		drawVerticalLine2((leftX + width) - 1, topY, height, rgbColour);
	}

	public static void drawVerticalLine(int xPosition, int yPosition, int height, int rgbColour) {
		if (xPosition < topX || xPosition >= bottomX)
			return;
		if (yPosition < topY) {
			height -= topY - yPosition;
			yPosition = topY;
		}
		if (yPosition + height > bottomY)
			height = bottomY - yPosition;
		int pixelIndex = xPosition + yPosition * width;
		for (int rowIndex = 0; rowIndex < height; rowIndex++)
			pixels[pixelIndex + rowIndex * width] = rgbColour;
	}

	public static void drawAlphaBox(int x, int y, int lineWidth, int lineHeight, int color, int alpha) {// drawAlphaHorizontalLine
		if (y < topY) {
			if (y > (topY - lineHeight)) {
				lineHeight -= (topY - y);
				y += (topY - y);
			} else {
				return;
			}
		}
		if (y + lineHeight > bottomY) {
			lineHeight -= y + lineHeight - bottomY;
		}
		//if (y >= bottomY - lineHeight)
		//return;
		if (x < topX) {
			lineWidth -= topX - x;
			x = topX;
		}
		if (x + lineWidth > bottomX)
			lineWidth = bottomX - x;
		for(int yOff = 0; yOff < lineHeight; yOff++) {
			int i3 = x + (y + (yOff)) * width;
			for (int j3 = 0; j3 < lineWidth; j3++) {
				//int alpha2 = (lineWidth-j3) / (lineWidth/alpha);
				int j1 = 256 - alpha;//alpha2 is for gradient
				int k1 = (color >> 16 & 0xff) * alpha;
				int l1 = (color >> 8 & 0xff) * alpha;
				int i2 = (color & 0xff) * alpha;
				int j2 = (pixels[i3] >> 16 & 0xff) * j1;
				int k2 = (pixels[i3] >> 8 & 0xff) * j1;
				int l2 = (pixels[i3] & 0xff) * j1;
				int k3 = ((k1 + j2 >> 8) << 16) + ((l1 + k2 >> 8) << 8)
						+ (i2 + l2 >> 8);
				pixels[i3++] = k3;
			}
		}
	}

	public static void defaultDrawingAreaSize() {
		topX = 0;
		topY = 0;
		bottomX = width;
		bottomY = height;
		centerX = bottomX;
		centerY = bottomX / 2;
	}

	public void drawAlphaGradient(int x, int y, int gradientWidth,
								  int gradientHeight, int startColor, int endColor, int alpha) {
		int k1 = 0;
		int l1 = 0x10000 / gradientHeight;
		if (x < topX) {
			gradientWidth -= topX - x;
			x = topX;
		}
		if (y < topY) {
			k1 += (topY - y) * l1;
			gradientHeight -= topY - y;
			y = topY;
		}
		if (x + gradientWidth > bottomX)
			gradientWidth = bottomX - x;
		if (y + gradientHeight > bottomY)
			gradientHeight = bottomY - y;
		int i2 = width - gradientWidth;
		int result_alpha = 256 - alpha;
		int total_pixels = x + y * width;
		for (int k2 = -gradientHeight; k2 < 0; k2++) {
			int gradient1 = 0x10000 - k1 >> 8;
			int gradient2 = k1 >> 8;
			int gradient_color = ((startColor & 0xff00ff) * gradient1
					+ (endColor & 0xff00ff) * gradient2 & 0xff00ff00)
					+ ((startColor & 0xff00) * gradient1 + (endColor & 0xff00)
					* gradient2 & 0xff0000) >>> 8;
			int color = ((gradient_color & 0xff00ff) * alpha >> 8 & 0xff00ff)
					+ ((gradient_color & 0xff00) * alpha >> 8 & 0xff00);
			for (int k3 = -gradientWidth; k3 < 0; k3++) {
				int colored_pixel = pixels[total_pixels];
				colored_pixel = ((colored_pixel & 0xff00ff) * result_alpha >> 8 & 0xff00ff)
						+ ((colored_pixel & 0xff00) * result_alpha >> 8 & 0xff00);
				pixels[total_pixels++] = color + colored_pixel;
			}
			total_pixels += i2;
			k1 += l1;
		}
	}

	public void drawAlphaGradientOnSprite(Sprite sprite, int x, int y, int gradientWidth,
										  int gradientHeight, int startColor, int endColor, int alpha) {
		int k1 = 0;
		int l1 = 0x10000 / gradientHeight;
		if (x < topX) {
			gradientWidth -= topX - x;
			x = topX;
		}
		if (y < topY) {
			k1 += (topY - y) * l1;
			gradientHeight -= topY - y;
			y = topY;
		}
		if (x + gradientWidth > bottomX)
			gradientWidth = bottomX - x;
		if (y + gradientHeight > bottomY)
			gradientHeight = bottomY - y;
		int i2 = width - gradientWidth;
		int result_alpha = 256 - alpha;
		int total_pixels = x + y * width;
		for (int k2 = -gradientHeight; k2 < 0; k2++) {
			int gradient1 = 0x10000 - k1 >> 8;
			int gradient2 = k1 >> 8;
			int gradient_color = ((startColor & 0xff00ff) * gradient1
					+ (endColor & 0xff00ff) * gradient2 & 0xff00ff00)
					+ ((startColor & 0xff00) * gradient1 + (endColor & 0xff00)
					* gradient2 & 0xff0000) >>> 8;
			int color = ((gradient_color & 0xff00ff) * alpha >> 8 & 0xff00ff)
					+ ((gradient_color & 0xff00) * alpha >> 8 & 0xff00);
			for (int k3 = -gradientWidth; k3 < 0; k3++) {
				int colored_pixel = pixels[total_pixels];
				colored_pixel = ((colored_pixel & 0xff00ff) * result_alpha >> 8 & 0xff00ff)
						+ ((colored_pixel & 0xff00) * result_alpha >> 8 & 0xff00);
				pixels[total_pixels++] = color + colored_pixel;
			}
			total_pixels += i2;
			k1 += l1;
		}
	}



	public static void setDrawingArea(int i, int j, int k, int l) {
		if (j < 0)
			j = 0;
		if (l < 0)
			l = 0;
		if (k > width)
			k = width;
		if (i > height)
			i = height;
		topX = j;
		topY = l;
		bottomX = k;
		bottomY = i;
		centerX = bottomX;
		centerY = bottomX / 2;
		anInt1387 = bottomY / 2;
	}

	public static void setAllPixelsToZero() {
		int i = width * height;
		for (int j = 0; j < i; j++)
			pixels[j] = 0;
	}

	public static boolean drawHorizontalLine(int yPos, int lineColor, int lineWidth, int xPos) {// method339
		if (yPos < topY || yPos >= bottomY) {
			return false;
		}
		if (xPos < topX) {
			lineWidth -= topX - xPos;
			xPos = topX;
		}
		if (xPos + lineWidth > bottomX)
			lineWidth = bottomX - xPos;
		int i1 = xPos + yPos * width;
		for (int j1 = 0; j1 < lineWidth; j1++)
			pixels[i1 + j1] = lineColor;
		return true;
	}

	public static void method335(int i, int j, int k, int l, int i1, int k1) {
		if (k1 < topX) {
			k -= topX - k1;
			k1 = topX;
		}
		if (j < topY) {
			l -= topY - j;
			j = topY;
		}
		if (k1 + k > bottomX)
			k = bottomX - k1;
		if (j + l > bottomY)
			l = bottomY - j;
		int l1 = 256 - i1;
		int i2 = (i >> 16 & 0xff) * i1;
		int j2 = (i >> 8 & 0xff) * i1;
		int k2 = (i & 0xff) * i1;
		int k3 = width - k;
		int l3 = k1 + j * width;
		for (int i4 = 0; i4 < l; i4++) {
			for (int j4 = -k; j4 < 0; j4++) {
				int l2 = (pixels[l3] >> 16 & 0xff) * l1;
				int i3 = (pixels[l3] >> 8 & 0xff) * l1;
				int j3 = (pixels[l3] & 0xff) * l1;
				int k4 = ((i2 + l2 >> 8) << 16) + ((j2 + i3 >> 8) << 8)
						+ (k2 + j3 >> 8);
				pixels[l3++] = k4;
			}

			l3 += k3;
		}
	}

	public static void drawPixels(int i, int j, int k, int l, int i1) {
		if (k < topX) {
			i1 -= topX - k;
			k = topX;
		}
		if (j < topY) {
			i -= topY - j;
			j = topY;
		}
		if (k + i1 > bottomX)
			i1 = bottomX - k;
		if (j + i > bottomY)
			i = bottomY - j;
		int k1 = width - i1;
		int l1 = k + j * width;
		for (int i2 = -i; i2 < 0; i2++) {
			for (int j2 = -i1; j2 < 0; j2++)
				pixels[l1++] = l;

			l1 += k1;
		}

	}

	public static void fillRectangle(int x, int y, int width, int height, int colour) {
		if (x < topX) {
			width -= topX - x;
			x = topX;
		}
		if (y < topY) {
			height -= topY - y;
			y = topY;
		}
		if (x + width > bottomX)
			width = bottomX - x;
		if (y + height > bottomY)
			height = bottomY - y;
		int k1 = DrawingArea.width - width;
		int l1 = x + y * DrawingArea.width;
		if (l1 > pixels.length - 1) {
			l1 = pixels.length - 1;
		}
		for (int i2 = -height; i2 < 0; i2++) {
			for (int j2 = -width; j2 < 0; j2++)
				pixels[l1++] = colour;

			l1 += k1;
		}

	}

	public static void fillPixels(int i, int j, int k, int l, int i1) {
		method339(i1, l, j, i);
		method339((i1 + k) - 1, l, j, i);
		method341(i1, l, k, i);
		method341(i1, l, k, (i + j) - 1);
	}


	DrawingArea() {
	}

	public static int pixels[];
	public static int width;
	public static int height;
	public static int topY;
	public static int bottomY;
	public static int topX;
	public static int bottomX;
	public static int centerX;
	public static int centerY;
	public static int anInt1387;

}
