package org.mtk.arcade.billboard;

import javax.swing.JScrollPane;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

public class BillboardEditor extends JScrollPane
		implements MouseListener, MouseMotionListener {

	private final int MAX_MAG = 10;
	private Color color;

	BillboardLayer billboardLayer;

	BillboardLayerPanel pixelpanel;
	final int layer;

	public BillboardEditor(BillboardLayer billboardLayer, int layer,
			int magnification, boolean mini, boolean enableEdit) {
		init(billboardLayer, layer, magnification, mini);
		this.layer = layer;
		if (enableEdit) {
			addMouseListener(this);
			addMouseMotionListener(this);
		}
	}

	public void init(BillboardLayer billboardLayer, int layer,
			int magnification, boolean mini) {
		this.billboardLayer = billboardLayer;
		pixelpanel = new BillboardLayerPanel(billboardLayer, layer,
				magnification, mini);
		setViewportView(pixelpanel);
		setPreferredSize(pixelpanel.getSize());
	}

	public void increaseMagnification() {
		int magnification = pixelpanel.getMagnification();
		if (magnification == MAX_MAG) {
			return;
		}
		pixelpanel.setMagnification(magnification + 1);
		revalidate();
		repaint();
	}

	public void decreaseMagnification() {
		int magnification = pixelpanel.getMagnification();
		if (magnification == 1) {
			return;
		}

		pixelpanel.setMagnification(magnification - 1);
		revalidate();
		repaint();
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		int magnification = pixelpanel.getMagnification();
		int x = arg0.getX();
		int y = arg0.getY();
		int modx = x / magnification;
		int mody = y / magnification;
		billboardLayer.setCoord(modx, mody, color, layer);
		pixelpanel.renderShape();
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		int x = arg0.getX();
		int y = arg0.getY();
		int magnification = pixelpanel.getMagnification();
		int modx = x / magnification;
		int mody = y / magnification;
		int extenty =
				this.verticalScrollBar.getModel().getValue() / magnification;
		int extentx =
				this.horizontalScrollBar.getModel().getValue() / magnification;
		int adjustx = modx + extentx;
		int adjusty = mody + extenty;
		billboardLayer.setCoord(adjustx, adjusty, color, layer);

		pixelpanel.renderShape();
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Color[] getColors() {
		return billboardLayer.getColorPalette(layer);
	}

	public void clear() {
		billboardLayer.clear(color, layer);
		pixelpanel.renderShape();
	}

	public void importImage(BufferedImage image) {
		billboardLayer.importImage(image, layer);
		pixelpanel.renderShape();
	}
}
