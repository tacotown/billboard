package org.mtk.arcade.billboard;

import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Map;

public class ColorMenu extends JPanel {

	protected final Border unselectedBorder;
	protected final Border selectedBorder;
	protected final Border activeBorder;

	public Map<Color, ColorPane> panes;
	protected ColorPane selected;
	protected final JPanel panel;

	final ColorPane[] lastColor = new ColorPane[2];

	Color[] layer1;
	Color[] layer2;

	int layer;

	public ColorMenu(BillboardEditor primaryEdit,
			BillboardEditor secondaryEdit) {
		layer1 = primaryEdit.getColors();
		layer2 = secondaryEdit.getColors();
		unselectedBorder = new CompoundBorder(
				new MatteBorder(1, 1, 1, 1, getBackground()),
				new BevelBorder(BevelBorder.LOWERED, Color.white, Color.gray));
		selectedBorder = new CompoundBorder(
				new MatteBorder(2, 2, 2, 2, Color.red),
				new MatteBorder(1, 1, 1, 1, getBackground()));
		activeBorder = new CompoundBorder(
				new MatteBorder(2, 2, 2, 2, Color.blue),
				new MatteBorder(1, 1, 1, 1, getBackground()));

		panel = new JPanel();
		panel.setBorder(new EmptyBorder(5, 5, 5, 5));
		panel.setLayout(new GridLayout(8, 8));
		update(layer);
		add(panel);
	}

	public void update(int layer) {
		panel.removeAll();
		panes = new HashMap<>();
		Color[] colors;
		if (layer == BillboardConstants.SECONDARY) {
			colors = layer2;
		}
		else {
			colors = layer1;
		}

		selected = null;
		for (Color color : colors) {
			ColorPane pn = new ColorPane(color);
			panel.add(pn);
			panes.put(color, pn);
		}

		selected = lastColor[layer];
		if (selected == null) {
			selected = panes.get(colors[0]);
			lastColor[layer] = selected;
		}
		this.layer = layer;
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(150, 185);
	}

	public void setColor(Color c) {
		ColorPane obj = panes.get(c);
		if (obj == null) {
			return;
		}
		if (selected != null) {
			selected.setSelected(false);
		}
		selected = obj;
		selected.setSelected(true);
		lastColor[layer] = selected;
	}

	public Color getColor() {
		if (selected == null) {
			return null;
		}
		return selected.getColor();
	}

	public void doSelection() {
		this.firePropertyChange("click", selected.getColor(), null);
	}

	class ColorPane extends JPanel implements MouseListener {
		protected final Color color;
		protected boolean selected;

		public ColorPane(Color c) {
			this.color = c;
			setBackground(c);
			setBorder(unselectedBorder);
			addMouseListener(this);
		}

		public Color getColor() {
			return color;
		}

		@Override
		public Dimension getPreferredSize() {
			return new Dimension(15, 15);
		}

		@Override
		public Dimension getMaximumSize() {
			return getPreferredSize();
		}

		@Override
		public Dimension getMinimumSize() {
			return getPreferredSize();
		}

		public void setSelected(boolean selected) {
			this.selected = selected;
			if (this.selected) {
				setBorder(selectedBorder);
			}
			else {
				setBorder(unselectedBorder);
			}
		}

		public boolean isSelected() {
			return selected;
		}

		@Override
		public void mousePressed(MouseEvent e) {
		}

		@Override
		public void mouseClicked(MouseEvent e) {
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			setColor(color);
			doSelection();
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			setBorder(activeBorder);
		}

		@Override
		public void mouseExited(MouseEvent e) {
			setBorder(selected ? selectedBorder : unselectedBorder);
		}
	}
}
