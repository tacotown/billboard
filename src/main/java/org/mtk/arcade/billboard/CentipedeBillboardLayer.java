package org.mtk.arcade.billboard;

import java.awt.Color;
import java.io.IOException;
import java.util.HashMap;

public class CentipedeBillboardLayer extends PolePositionBillboardLayer {
	public CentipedeBillboardLayer(String romloc, int spriteSize)
			throws IOException {
		super(romloc, spriteSize);
		loadBinaryData();
		primaryPalette = new Color[] { Color.GREEN, Color.WHITE, Color.RED,
				Color.BLACK };
		secondaryPalette = new Color[] { Color.RED, Color.WHITE, Color.BLUE };
	}

	@Override
	protected int getOffset() {
		return 13312;
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
		return 7424;
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
		paletteMap.put("0011",
				new Color[] { Color.BLACK, Color.BLUE }); // centipede outline
		paletteMap.put("0101",
				new Color[] { Color.BLACK, Color.WHITE }); // centipede shadow
		paletteMap.put("0110",
				new Color[] { Color.GREEN, Color.RED }); // inner centipede
		paletteMap.put("0111",
				new Color[] { Color.GREEN, Color.BLUE }); // centipede
		paletteMap.put("1000",
				new Color[] { Color.BLACK, Color.RED }); // inner usa
		paletteMap.put("1001", new Color[] { Color.RED, Color.RED });
		paletteMap.put("1010",
				new Color[] { Color.WHITE, Color.WHITE }); // background
		paletteMap.put("1011", new Color[] { Color.WHITE, Color.BLUE }); // USA
		paletteMap.put("1100",
				new Color[] { Color.WHITE, Color.RED }); // inner usa
		paletteMap.put("1101",
				new Color[] { Color.GREEN, Color.WHITE }); // centipede
		paletteMap.put("1110",
				new Color[] { Color.RED, Color.BLUE }); // outline
		paletteMap.put("1111",
				new Color[] { Color.RED, Color.WHITE }); // antenna, outline

	}
}
