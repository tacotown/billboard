package org.mtk.arcade.billboard;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;

public abstract class BillboardLayer {
	final Color[][][] color;
	final int h;
	final int w;

	public BillboardLayer(int w, int h) {
		color = new Color[w][h][2];
		this.w = w;
		this.h = h;
	}

	public int getLayerHeight() {
		return h;
	}

	public int getLayerWidth() {
		return w;
	}

	public abstract void setCoord(int x, int y, Color tf, int layer);

	public abstract Color getCoord(int x, int y, int layer);

	public abstract Color[] getColorPalette(int layer);

	public abstract void save(String loc) throws IOException;

	public void clear(Color c, int layer) {
		for (int x = 0; x < w; ++x) {
			for (int y = 0; y < h; ++y) {
				setCoord(x, y, c, layer);
			}
		}
	}

	public abstract void importImage(BufferedImage image, int layer);

	public abstract BufferedImage convertToBufferedImage(int layer);
}
