package org.mtk.arcade.billboard;

import java.awt.Color;
import java.io.IOException;
import java.util.HashMap;

public class PepsiBoardLayer extends PolePositionBillboardLayer {
	public PepsiBoardLayer(String romloc, int spriteSize) throws IOException {
		super(romloc, spriteSize);
		loadBinaryData();
		primaryPalette = new Color[] { Color.BLUE, Color.WHITE, Color.RED,
				Color.CYAN };
		secondaryPalette = new Color[] { Color.YELLOW, Color.BLACK, Color.RED };
	}

	public int getOffset() {
		return 13312;
	}

	public String getFirstRom() {
		return "pp1_19.4n";
	}

	public String getSecondRom() {
		return "pp1_20.4m";
	}

	@Override
	protected int getMiniOffset() {
		return 7424;
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
		paletteMap = new HashMap<>();
		// 0000, 0001, 0010, 0100 already defaulted
		paletteMap.put("0011",
				new Color[] { Color.BLUE, Color.BLUE }); // upper left outline
//		paletteMap.put("1111",
//				new Color[] { Color.WHITE, Color.RED }); // upper left outline
		// original
		paletteMap.put("0101",
				new Color[] { Color.RED, Color.BLACK }); // upper left outline
		paletteMap.put("0110",
				new Color[] { Color.RED, Color.YELLOW }); // left side of pepsi
		paletteMap.put("0111",
				new Color[] { Color.BLUE, Color.YELLOW }); // bottom blue pepsi
		paletteMap.put("1000",
				new Color[] { Color.WHITE, Color.YELLOW }); // pepsi outline
		paletteMap.put("1001",
				new Color[] { Color.WHITE, Color.BLACK }); // Agip
		paletteMap.put("1010", new Color[] { Color.BLUE, Color.BLACK }); // Agip
		paletteMap.put("1011", new Color[] { Color.RED, Color.RED }); // flame
		paletteMap.put("1100",
				new Color[] { Color.WHITE, Color.RED }); // dot of the 'i'
		paletteMap.put("1101", new Color[] { Color.CYAN,
				Color.YELLOW }); // right side of pepsi
		paletteMap.put("1110",
				new Color[] { Color.CYAN, Color.BLACK }); // upper right outline
		//	paletteMap.put("1111", new Color[] { Color.RED, Color.PINK}); //

	}
}
