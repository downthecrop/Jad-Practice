package com.client; /**
 * RuneScape 2 Animated Image (GIF) Renderer
 * 
 * Copyright (C) 2011 Joseph Melsha (joe.melsha@live.com)
 * 
 * This program is free software: you can redistribute it and/or modify 
 * it under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License 
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

import java.io.*;
import java.awt.*;
import java.awt.image.*;
import java.util.*;
import java.net.*;

/**
 * @name RuneScape 2 Animated Image (GIF) Renderer
 * @author Joseph Melsha (joe.melsha@live.com)
 * @version 1.0, 3/13/2011
 */
public final class AnimatedSprite extends Sprite
{
	public AnimatedSprite(String file)
	{
		this(file, DUMMY_COMPONENT);
	}

	public AnimatedSprite(String file, Component component)
	{
		this(file, component, 0, 0);
	}

	public AnimatedSprite(File file)
	{
		this(file, DUMMY_COMPONENT);
	}

	public AnimatedSprite(File file, Component component)
	{
		this(loadFile(file), component);
	}

	public AnimatedSprite(byte[] data)
	{
		this(data, DUMMY_COMPONENT);
	}

	public AnimatedSprite(byte[] data, Component component)
	{
		this(loadImage(data, component), component);
	}

	public AnimatedSprite(Image image)
	{
		this(image, DUMMY_COMPONENT);
	}

	public AnimatedSprite(Image image, Component component)
	{
		this(image, component, 0, 0);
	}

	public AnimatedSprite(String file, int width, int height)
	{
		this(file, DUMMY_COMPONENT, width, height);
	}

	public AnimatedSprite(String file, Component component, int width, int height)
	{
		this(new File(file), component, width, height);
	}

	public AnimatedSprite(File file, int width, int height)
	{
		this(file, DUMMY_COMPONENT, width, height);
	}

	public AnimatedSprite(File file, Component component, int width, int height)
	{
		this(loadFile(file), component, width, height);
	}

	public AnimatedSprite(byte[] data, int width, int height)
	{
		this(data, DUMMY_COMPONENT, width, height);
	}

	public AnimatedSprite(byte[] data, Component component, int width, int height)
	{
		this(loadImage(data, component), component, width, height);
	}

	public AnimatedSprite(Image image, int width, int height)
	{
		this(image, DUMMY_COMPONENT, width, height);
	}

	public AnimatedSprite(URL url)
	{
		this(url, DUMMY_COMPONENT);
	}

	public AnimatedSprite(URL url, Component component)
	{
		this(url, component, 0, 0);
	}

	public AnimatedSprite(URL url, int width, int height)
	{
		this(url, DUMMY_COMPONENT, width, height);
	}

	public AnimatedSprite(URL url, Component component, int width, int height)
	{
		this(loadImage(url, component), component, width, height);
	}

	public AnimatedSprite(Image image, Component component, int width, int height)
	{
		if (component == null)
			component = DUMMY_COMPONENT;

		this.image = image;
		this.component = component;
		int newWidth = 0;
		int newHeight = 0;
		if (image != null)
		{
			newWidth = image.getWidth(component);
			newHeight = image.getHeight(component);
			if (newWidth <= 0 || newHeight <= 0)
				newWidth = newHeight = 0;

		}
		realWidth = newWidth;
		realHeight = newHeight;
		boolean scaled = newWidth > 0 && newHeight > 0 && width > 0 && height > 0 && (width != newWidth || height != newHeight);
		if (scaled)
		{
			newWidth = width;
			newHeight = height;
		}
		if (newWidth < 1)
			newWidth = 1;

		if (newHeight < 1)
			newHeight = 1;

		this.scaled = scaled;
		this.width = newWidth;
		this.height = newHeight;
		int pixelCount = newWidth * newHeight;
		int[] pixels = new int[pixelCount];
		this.pixels = pixels;
		DirectColorModel model = new DirectColorModel(32, 0xff0000, 0xff00, 0xff, 0xff000000);
		buffer = new BufferedImage(model, Raster.createWritableRaster(model.createCompatibleSampleModel(newWidth, newHeight), new DataBufferInt(pixels, pixelCount), null), false, new Hashtable());
		scaleDetail = 0;
		autoUpdate = true;
		update();
	}

	public AnimatedSprite(AnimatedSprite sprite, int width, int height, boolean absScale)
	{
		if (sprite == null)
			throw new NullPointerException();

		image = sprite.image;
		component = sprite.component;
		int newWidth = sprite.realWidth;
		int newHeight = sprite.realHeight;
		realWidth = newWidth;
		realHeight = newHeight;
		boolean scaled = newWidth > 0 && newHeight > 0 && width > 0 && height > 0 && (width != newWidth || height != newHeight);
		if (scaled)
		{
			if (!absScale)
			{
				newWidth = newWidth * width / sprite.width;
				newHeight = newHeight * height / sprite.height;
			}
			else
			{
				newWidth = width;
				newHeight = height;
			}
		}
		else if (!absScale)
		{
			newWidth = sprite.width;
			newHeight = sprite.height;
		}
		if (newWidth < 1)
			newWidth = 1;

		if (newHeight < 1)
			newHeight = 1;

		this.scaled = scaled;
		this.width = newWidth;
		this.height = newHeight;
		int pixelCount = newWidth * newHeight;
		int[] pixels = new int[pixelCount];
		this.pixels = pixels;
		DirectColorModel model = new DirectColorModel(32, 0xff0000, 0xff00, 0xff, 0xff000000);
		buffer = new BufferedImage(model, Raster.createWritableRaster(model.createCompatibleSampleModel(newWidth, newHeight), new DataBufferInt(pixels, pixelCount), null), false, new Hashtable());
		scaleDetail = sprite.scaleDetail;
		autoUpdate = sprite.autoUpdate;
		update();
	}

	public AnimatedSprite getInstance(int width, int height)
	{
		return getInstance(width, height, false);
	}

	public AnimatedSprite getInstance(int width, int height, boolean absScale)
	{
		return new AnimatedSprite(this, width, height, absScale);
	}

	private static int blend(int dst, int src)
	{
		int alpha = src >>> 24;
		if (alpha == 0)
			return dst;

		if (alpha == 255)
			return src;

		int delta = 255 - alpha;
		return ((src & 0xff000000 | ((src & 0xff00ff) * alpha + (dst & 0xff00ff) * delta & 0xff00ff00 | (src & 0xff00) * alpha + (dst & 0xff00) * delta & 0xff0000) >>> 8)) & 0xffffff;
	}

	private static int blend(int dst, int src, int alpha)
	{
		alpha = (src >>> 24) * alpha / 255;
		if (alpha <= 0)
			return dst;

		if (alpha >= 255)
			return src;

		int delta = 255 - alpha;
		return ((src & 0xff000000 | ((src & 0xff00ff) * alpha + (dst & 0xff00ff) * delta & 0xff00ff00 | (src & 0xff00) * alpha + (dst & 0xff00) * delta & 0xff0000) >>> 8)) & 0xffffff;
	}

	@Override
	public void method355(int[] src, int scanSize, byte[] dir, int height, int dst[], int tmp, int dstOff, int dstPtr, int srcOff, int srcPtr)
	{
		int scanSizeHigh = -(scanSize >> 2);
		scanSize = -(scanSize & 3);
		int i;
		for (int y = -height; y != 0; y++)
		{
			for (i = scanSizeHigh; i != 0; ++i)
			{
				if (dir[dstPtr] == 0)
					dst[dstPtr] = blend(dst[dstPtr++], src[srcPtr++]);
				else
					dstPtr++;

				if (dir[dstPtr] == 0)
					dst[dstPtr] = blend(dst[dstPtr++], src[srcPtr++]);
				else
					dstPtr++;

				if (dir[dstPtr] == 0)
					dst[dstPtr] = blend(dst[dstPtr++], src[srcPtr++]);
				else
					dstPtr++;

				if (dir[dstPtr] == 0)
					dst[dstPtr] = blend(dst[dstPtr++], src[srcPtr++]);
				else
					dstPtr++;

			}

			for (i = scanSize; i != 0; ++i)
			{
				if (dir[dstPtr] == 0)
					dst[dstPtr] = blend(dst[dstPtr++], src[srcPtr++]);
				else
					dstPtr++;

			}

			dstPtr += dstOff;
			srcPtr += srcOff;
		}

	}

	@Override
	public void method351(int srcPtr, int scanSize, int[] dst, int[] src, int srcOff, int height, int dstOff, int alpha, int dstPtr)
	{
		int scanSizeHigh = -(scanSize >> 2);
		scanSize = -(scanSize & 3);
		int i;
		for (int y = -height; y != 0; ++y)
		{
			for (i = scanSizeHigh; i != 0; ++i)
			{
				dst[dstPtr] = blend(dst[dstPtr++], src[srcPtr++], alpha);
				dst[dstPtr] = blend(dst[dstPtr++], src[srcPtr++], alpha);
				dst[dstPtr] = blend(dst[dstPtr++], src[srcPtr++], alpha);
				dst[dstPtr] = blend(dst[dstPtr++], src[srcPtr++], alpha);
			}

			for (i = scanSize; i != 0; ++i)
				dst[dstPtr] = blend(dst[dstPtr++], src[srcPtr++], alpha);

			dstPtr += dstOff;
			srcPtr += srcOff;
		}

	}

	@Override
	public void method347(int dstPtr, int scanSize, int height, int srcOff, int srcPtr, int dstOff, int[] src, int[] dst)
	{
		int scanSizeHigh = -(scanSize >> 2);
		scanSize = -(scanSize & 3);
		int i;
		for (int y = -height; y != 0; y++)
		{
			for (i = scanSizeHigh; i != 0; ++i)
			{
				dst[dstPtr] = blend(dst[dstPtr++], src[srcPtr++]);
				dst[dstPtr] = blend(dst[dstPtr++], src[srcPtr++]);
				dst[dstPtr] = blend(dst[dstPtr++], src[srcPtr++]);
				dst[dstPtr] = blend(dst[dstPtr++], src[srcPtr++]);
			}

			for (i = scanSize; i != 0; ++i)
				dst[dstPtr] = blend(dst[dstPtr++], src[srcPtr++]);

			dstPtr += dstOff;
			srcPtr += srcOff;
		}

	}

	@Override
	public void method349(int[] dst, int[] src, int srcPtr, int dstPtr, int scanSize, int height, int dstOff, int srcOff)
	{
		int scanSizeHigh = -(scanSize >> 2);
		scanSize = -(scanSize & 3);
		int i;
		for (int y = -height; y != 0; ++y)
		{
			for (i = scanSizeHigh; i != 0; ++i)
			{
				dst[dstPtr] = blend(dst[dstPtr++], src[srcPtr++]);
				dst[dstPtr] = blend(dst[dstPtr++], src[srcPtr++]);
				dst[dstPtr] = blend(dst[dstPtr++], src[srcPtr++]);
				dst[dstPtr] = blend(dst[dstPtr++], src[srcPtr++]);
			}

			for (i = scanSize; i != 0; ++i)
				dst[dstPtr] = blend(dst[dstPtr++], src[srcPtr++]);

			dstPtr += dstOff;
			srcPtr += srcOff;
		}

	}

	private static Object getScaleDetail(int scaleDetail)
	{
		if (scaleDetail == SCALE_DETAIL_QUALITY)
			return RenderingHints.VALUE_RENDER_SPEED;

		if (scaleDetail == SCALE_DETAIL_SPEED)
			return RenderingHints.VALUE_RENDER_QUALITY;

		return RenderingHints.VALUE_RENDER_DEFAULT;
	}

	@Override
	public void autoUpdate()
	{
		if (autoUpdate)
			update();

	}

	public void update()
	{
		try
		{
			Graphics g = buffer.getGraphics();
			if (g != null)
				try
				{
					if (!scaled)
						g.drawImage(image, 0, 0, component);

					else
					{
						if (g instanceof Graphics2D)
							((Graphics2D) g).setRenderingHint(RenderingHints.KEY_RENDERING, getScaleDetail(scaleDetail));

						g.drawImage(image, 0, 0, width, height, component);
					}
				}
				finally
				{
					g.dispose();
				}

		}
		catch (Exception x) { }
		anInt1442 = anInt1443 = 0;
		myWidth = maxWidth = width;
		myHeight = maxHeight = height;
		myPixels = pixels;
	}

	private static byte[] loadFile(File file)
	{
		if (file == null || !file.exists() || !file.isFile() || !file.canRead())
			return null;

		long size = file.length();
		if (size < 0L || size > 2147483647L)
			return null;

		int count = (int) size;
		byte[] data = null;
		try
		{
			FileInputStream input = new FileInputStream(file);
			try
			{
				data = new byte[count];
				int offset = 0;
				for (int read; count != 0; count -= read)
				{
					read = input.read(data, offset, count);
					if (read < 0)
						break;

					if (read > count)
						read = count;

					offset += read;
				}

			}
			finally
			{
				input.close();
			}
		}
		catch (Exception x) { }
		return count == 0 ? data:null;
	}

	private static Image loadImage(byte[] data, Component component)
	{
		if (data != null)
			try
			{
				Toolkit toolkit = null;
				if (component == null)
					component = DUMMY_COMPONENT;

				try
				{
					toolkit = component.getToolkit();
				}
				catch (Exception x) { }
				if (toolkit == null)
					toolkit = Toolkit.getDefaultToolkit();

				if (toolkit != null)
				{
					Image image = toolkit.createImage(data);
					if (image != null)
					{
						MediaTracker tracker = new MediaTracker(component);
						tracker.addImage(image, 0);
						tracker.waitForAll();
						if (!tracker.isErrorAny() && image.getWidth(component) >= 0 && image.getHeight(component) >= 0)
							return image;

					}
				}
			}
			catch (Exception x) { }

		return null;
	}

	private static Image loadImage(URL url, Component component)
	{
		if (url != null)
			try
			{
				Toolkit toolkit = null;
				if (component == null)
					component = DUMMY_COMPONENT;

				try
				{
					toolkit = component.getToolkit();
				}
				catch (Exception x) { }
				if (toolkit == null)
					toolkit = Toolkit.getDefaultToolkit();

				if (toolkit != null)
				{
					Image image = toolkit.createImage(url);
					if (image != null)
					{
						MediaTracker tracker = new MediaTracker(component);
						tracker.addImage(image, 0);
						tracker.waitForAll();
						if (!tracker.isErrorAny() && image.getWidth(component) >= 0 && image.getHeight(component) >= 0)
							return image;

					}
				}
			}
			catch (Exception x) { }

		return null;
	}

	public void setScaleDetailDefault()
	{
		scaleDetail = SCALE_DETAIL_DEFAULT;
	}

	public void setScaleDetailQuality()
	{
		scaleDetail = SCALE_DETAIL_QUALITY;
	}

	public void setScaleDetailSpeed()
	{
		scaleDetail = SCALE_DETAIL_SPEED;
	}

	public void setScaleDetail(int scaleDetail)
	{
		if (scaleDetail != SCALE_DETAIL_DEFAULT && scaleDetail != SCALE_DETAIL_QUALITY && scaleDetail != SCALE_DETAIL_SPEED)
			throw new IllegalArgumentException("Invalid scale detail (" + scaleDetail + ")!");

		this.scaleDetail = scaleDetail;
	}

	public boolean isScaleDetailDefault()
	{
		int scaleDetail = this.scaleDetail;
		return scaleDetail != SCALE_DETAIL_QUALITY && scaleDetail != SCALE_DETAIL_SPEED;
	}

	public boolean isScaleDetailQuality()
	{
		return scaleDetail == SCALE_DETAIL_QUALITY;
	}

	public boolean isScaleDetailSpeed()
	{
		return scaleDetail == SCALE_DETAIL_SPEED;
	}

	public int getScaleDetail()
	{
		return scaleDetail;
	}

	public void setAutoUpdate(boolean autoUpdate)
	{
		autoUpdate = this.autoUpdate;
	}

	public boolean getAutoUpdate()
	{
		return autoUpdate;
	}

	public boolean isValid()
	{
		return realWidth > 0 && realHeight > 0;
	}

	public static final int SCALE_DETAIL_DEFAULT = 0;
	public static final int SCALE_DETAIL_QUALITY = 1;
	public static final int SCALE_DETAIL_SPEED = 2;
	private int scaleDetail;
	private boolean autoUpdate;
	public final int realWidth;
	public final int realHeight;
	public final boolean scaled;
	public final Image image;
	public final Component component;
	public final int width;
	public final int height;
	public final int[] pixels;
	public final BufferedImage buffer;
	public static final Component DUMMY_COMPONENT = new Canvas();
}