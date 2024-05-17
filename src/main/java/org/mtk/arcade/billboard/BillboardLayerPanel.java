package org.mtk.arcade.billboard;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

public class BillboardLayerPanel extends JPanel {
	final BillboardLayer billboardLayer;
	int magnification;
	final int layer;
	boolean mini;

	public BillboardLayerPanel(BillboardLayer billboardLayer, int layer,
			int magnification, boolean mini) {
		this.billboardLayer = billboardLayer;
		Dimension d = getDimension();
		this.layer = layer;
		this.mini = mini;
		this.magnification = magnification;
		super.setSize(d);
		this.setPreferredSize(d);
	}

	public Dimension getDimension() {
		return new Dimension(billboardLayer.getLayerWidth() * magnification,
				billboardLayer.getLayerHeight() * magnification);
	}

	public void renderShape() {
		repaint();
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(Color.black);
		g.fillRect(0, 0, billboardLayer.getLayerWidth() * magnification,
				billboardLayer.getLayerHeight() * magnification);
		if (mini) {
			for (int y = 0; y < billboardLayer.getLayerHeight() / 2; ++y) {
				for (int x = 0; x < billboardLayer.getLayerWidth() / 2; ++x) {
					Color c = billboardLayer.getCoord(x * 2 + 1, y * 2 + 1,
							layer);
					if (c == null) {
						c = Color.black;
					}
					g.setColor(c);
					g.fillRect(x * magnification, y * magnification,
							magnification, magnification);
				}
			}
		}
		else {
			for (int y = 0; y < billboardLayer.getLayerHeight(); ++y) {
				for (int x = 0; x < billboardLayer.getLayerWidth(); ++x) {
					Color c = billboardLayer.getCoord(x, y, layer);
					if (c == null) {
						c = Color.black;
					}
					g.setColor(c);
					g.fillRect(x * magnification, y * magnification,
							magnification, magnification);
				}
			}
		}
	}

	public void setMagnification(int mag) {
		magnification = mag;
		setSize(getDimension());
		renderShape();
	}

	public int getMagnification() {
		return magnification;
	}
}
