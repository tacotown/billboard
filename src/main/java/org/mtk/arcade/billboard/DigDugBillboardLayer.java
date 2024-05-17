package org.mtk.arcade.billboard;

import java.awt.Color;
import java.io.IOException;
import java.util.HashMap;

public class DigDugBillboardLayer extends PolePositionBillboardLayer {
	public DigDugBillboardLayer(String romloc, int spriteSize)
			throws IOException {
		super(romloc, spriteSize);
		loadBinaryData();
		secondaryPalette = new Color[] { Color.WHITE, Color.CYAN, Color.ORANGE,
				ColorUtils.DARK_RED, Color.BLACK };
		primaryPalette = new Color[] { Color.RED, Color.WHITE };
	}

	@Override
	public void initPaletteMap() {
		// byte to colors map
		paletteMap = new HashMap<>();
		paletteMap.put("0101", new Color[] { Color.WHITE, Color.WHITE });
		paletteMap.put("0110", new Color[] { Color.RED, Color.WHITE });
		paletteMap.put("0111", new Color[] { Color.WHITE, Color.CYAN });
		paletteMap.put("1000", new Color[] { Color.RED, Color.ORANGE });
		paletteMap.put("1001", new Color[] { Color.RED, Color.BLACK });
		paletteMap.put("1010", new Color[] { Color.RED, Color.CYAN });
		paletteMap.put("1011", new Color[] { Color.WHITE, Color.ORANGE });
		paletteMap.put("1100",
				new Color[] { Color.WHITE, ColorUtils.DARK_RED });
		paletteMap.put("1101", new Color[] { Color.WHITE, Color.BLACK });
		paletteMap.put("1110", new Color[] { Color.RED, ColorUtils.DARK_RED });

	}

	@Override
	protected int getOffset() {
		return 10240;
	}

	@Override
	String getFirstRom() {
		return "136014.152";
	}

	@Override
	String getSecondRom() {
		return "136014.153";
	}

	@Override
	protected int getMiniOffset() {
		return 6656;
	}

	@Override
	String getMiniFirstRom() {
		return "136014.156";
	}

	@Override
	String getMiniSecondRom() {
		return "136014.157";
	}
}
