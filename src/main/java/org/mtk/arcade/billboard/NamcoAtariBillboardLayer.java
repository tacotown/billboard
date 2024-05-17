package org.mtk.arcade.billboard;

import java.awt.Color;
import java.io.IOException;
import java.util.HashMap;

public class NamcoAtariBillboardLayer extends PolePositionBillboardLayer {
	public NamcoAtariBillboardLayer(String romloc, int spriteSize)
			throws IOException {
		super(romloc, spriteSize);
		loadBinaryData();
		primaryPalette = new Color[] { Color.RED, Color.WHITE, Color.BLUE,
				Color.BLACK, ColorUtils.DARK_RED };
		secondaryPalette = new Color[] { Color.YELLOW, ColorUtils.DARK_PURPLE };
	}

	@Override
	protected int getOffset() {
		return 0;
	}

	@Override
	String getFirstRom() {
		return "136014.154";
	}

	@Override
	String getSecondRom() {
		return "136014.155";
	}

	@Override
	protected int getMiniOffset() {
		return 8192;
	}

	@Override
	String getMiniFirstRom() {
		return "136014.156";
	}

	@Override
	String getMiniSecondRom() {
		return "136014.157";
	}

	@Override
	public void initPaletteMap() {
		// byte to colors map
		paletteMap = new HashMap<>();
		paletteMap.put("0101", new Color[] { Color.RED, Color.YELLOW });
		paletteMap.put("0110",
				new Color[] { ColorUtils.DARK_RED, ColorUtils.DARK_PURPLE });
		paletteMap.put("0111", new Color[] { Color.WHITE, Color.YELLOW });
		paletteMap.put("1000",
				new Color[] { Color.BLUE, ColorUtils.DARK_PURPLE });
		paletteMap.put("1001", new Color[] { Color.BLUE, Color.YELLOW });
		paletteMap.put("1010",
				new Color[] { Color.WHITE, ColorUtils.DARK_PURPLE });
		paletteMap.put("1011",
				new Color[] { Color.RED, ColorUtils.DARK_PURPLE });
		paletteMap.put("1100",
				new Color[] { ColorUtils.DARK_RED, Color.YELLOW });
		paletteMap.put("1101",
				new Color[] { Color.BLACK, ColorUtils.DARK_PURPLE });
		paletteMap.put("1110", new Color[] { Color.BLACK, Color.YELLOW });

	}

}
