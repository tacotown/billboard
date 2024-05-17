package org.mtk.arcade.billboard;

import java.awt.Color;
import java.io.IOException;
import java.util.HashMap;

public class MartiniBillboardLayer extends PolePositionBillboardLayer {
	public MartiniBillboardLayer(String romloc, int spriteSize)
			throws IOException {
		super(romloc, spriteSize);
		loadBinaryData();
		primaryPalette = new Color[] { Color.RED, Color.WHITE, Color.BLACK,
				Color.YELLOW };
		secondaryPalette = new Color[] { Color.RED, Color.WHITE, Color.BLACK };
	}

	protected int getOffset() {
		return 0;
	}

	@Override
	String getFirstRom() {
		return "pp1_21.3n";
	}

	@Override
	String getSecondRom() {
		return "pp1_22.3m";
	}

	@Override
	protected int getMiniOffset() {
		return 8192;
	}

	@Override
	String getMiniFirstRom() {
		return "pp1_25.1n";
	}

	@Override
	String getMiniSecondRom() {
		return "pp1_26.1m";
	}

	@Override
	public void initPaletteMap() {
		// byte to colors map
		paletteMap = new HashMap<>();
		paletteMap.put("0011",
				new Color[] { Color.WHITE, Color.BLACK }); // left side
		paletteMap.put("1111",
				new Color[] { Color.WHITE, Color.WHITE }); // bow tie
		paletteMap.put("0101",
				new Color[] { Color.WHITE, Color.BLACK }); // martini
		paletteMap.put("0110",
				new Color[] { Color.WHITE, Color.RED }); // left champion red
		paletteMap.put("0111", new Color[] { Color.RED,
				Color.RED }); // upper left martini circle
		paletteMap.put("1000",
				new Color[] { Color.RED, Color.WHITE }); // upper martini circle
		paletteMap.put("1001",
				new Color[] { Color.BLACK, Color.BLACK });//champion text
		paletteMap.put("1010",
				new Color[] { Color.YELLOW, Color.BLACK }); // right edges
		paletteMap.put("1011",
				new Color[] { Color.YELLOW, Color.WHITE }); // champion outline
		paletteMap.put("1100",
				new Color[] { Color.YELLOW, Color.RED }); // left edges
		paletteMap.put("1101",
				new Color[] { Color.BLACK, Color.WHITE }); // martini black
		paletteMap.put("1110", new Color[] { Color.RED,
				Color.BLACK }); // upper right martini circle

	}

}
