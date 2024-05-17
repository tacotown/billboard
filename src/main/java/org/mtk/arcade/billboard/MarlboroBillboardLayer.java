package org.mtk.arcade.billboard;

import java.awt.Color;
import java.io.IOException;
import java.util.HashMap;

public class MarlboroBillboardLayer extends PolePositionBillboardLayer {
	public MarlboroBillboardLayer(String romloc, int spriteSize)
			throws IOException {
		super(romloc, spriteSize);
		loadBinaryData();
		secondaryPalette = new Color[] { Color.WHITE, Color.YELLOW,
				Color.BLACK };
		primaryPalette = new Color[] { Color.RED, Color.WHITE, Color.BLACK };
	}

	protected int getOffset() {
		return 10240;
	}

	@Override
	String getFirstRom() {
		return "pp1_19.4n";
	}

	@Override
	String getSecondRom() {
		return "pp1_20.4m";
	}

	@Override
	protected int getMiniOffset() {
		return 6656;
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
		// 0000, 0001, 0010, 0100 in default palette
		// not used in original
		//	paletteMap.put("0011", new Color[] { Color.BLUE,
		//				Color.YELLOW });
		//	paletteMap.put("1111", new Color[] { Color.BLACK,
		//	Color.YELLOW });
		paletteMap.put("0101", new Color[] { Color.WHITE,
				Color.WHITE }); // marlboro background
		paletteMap.put("0110",
				new Color[] { Color.RED, Color.WHITE }); // marlboro red arch
		paletteMap.put("0111",
				new Color[] { Color.RED, Color.BLACK }); // cat ears
		paletteMap.put("1000",
				new Color[] { Color.RED, Color.WHITE }); // upper right checkered
		paletteMap.put("1001",
				new Color[] { Color.WHITE, Color.BLACK }); // cat face
		paletteMap.put("1010",
				new Color[] { Color.BLACK, Color.WHITE }); // Marlboro
		paletteMap.put("1011",
				new Color[] { Color.WHITE, Color.YELLOW }); // marchal
		paletteMap.put("1100",
				new Color[] { Color.BLACK, Color.YELLOW }); // remainder of eyes
		paletteMap.put("1101",
				new Color[] { Color.BLACK, Color.BLACK }); // bottom cats face
		paletteMap.put("1110", new Color[] { Color.BLACK,
				Color.WHITE }); // bottom of flag white

	}
}
