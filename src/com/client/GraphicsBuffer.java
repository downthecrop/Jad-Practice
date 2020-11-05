package com.client;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;


public final class GraphicsBuffer {
	public final int[] canvasRaster;
	public final int canvasWidth;
	public final int canvasHeight;
	public BufferedImage bufferedImage;
	public final float[] depthBuffer;

	public void resetDepthBuffer() {
		if (this.depthBuffer == null) {
			return;
		}
		int length = this.depthBuffer.length;
		int loops = length - (length & 0x7);
		int position = 0;
		while (position < loops) {
			this.depthBuffer[(position++)] = 2.14748365E9F;
			this.depthBuffer[(position++)] = 2.14748365E9F;
			this.depthBuffer[(position++)] = 2.14748365E9F;
			this.depthBuffer[(position++)] = 2.14748365E9F;
			this.depthBuffer[(position++)] = 2.14748365E9F;
			this.depthBuffer[(position++)] = 2.14748365E9F;
			this.depthBuffer[(position++)] = 2.14748365E9F;
			this.depthBuffer[(position++)] = 2.14748365E9F;
		}
		while (position < length) {
			this.depthBuffer[(position++)] = 2.14748365E9F;
		}
	}

	public GraphicsBuffer(int canvasWidth, int canvasHeight) {
		this.canvasWidth = canvasWidth;
		this.canvasHeight = canvasHeight;
		this.bufferedImage = new BufferedImage(canvasWidth, canvasHeight, 1);
		this.canvasRaster = ((DataBufferInt) this.bufferedImage.getRaster().getDataBuffer()).getData();
		this.depthBuffer = new float[canvasWidth * canvasHeight];
		setCanvas();
	}

	public void drawGraphics(int x, Graphics graphics, int y) {
	//	GL11.glDrawPixels(x, y, 0, 0, this.bufferedImage);
		graphics.drawImage(this.bufferedImage, y, x, null);
	}

	public void setCanvas() {
		DrawingArea.initDrawingArea(this.canvasHeight, this.canvasWidth, this.canvasRaster, this.depthBuffer);
	}
}
