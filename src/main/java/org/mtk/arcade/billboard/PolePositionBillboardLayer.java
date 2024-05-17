package org.mtk.arcade.billboard;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import org.mtk.util.BitUtils;
import org.mtk.util.BitmapBuffer;
import org.mtk.util.ByteConverter;

public abstract class PolePositionBillboardLayer extends BillboardLayer {
	// ROM buffers
	BitmapBuffer largeSpriteBuffer;
	BitmapBuffer smallSpriteBuffer;

	// working buffer
	BitmapBuffer buffer;
	Map<String, Color[]> paletteMap;

	static final int ROWS_PER_BOARD_HEIGHT = 2; // board is two sections high
	static final int COLUMNS_PER_BOARD_WIDTH = 3; // board is divided into three sections horizontally
	// large sprite is 2 x 3 squares 32 lines per square
	// small sprite is 2 x 3 squares 16 lines per square

	Color[] primaryPalette;
	Color[] secondaryPalette;
	int spriteSize;
	int imageOffset;

	String romloc;

	public PolePositionBillboardLayer(String romloc, int spritesize) {
		super(spritesize * COLUMNS_PER_BOARD_WIDTH,
				spritesize * ROWS_PER_BOARD_HEIGHT);
		this.spriteSize = spritesize;
		this.romloc = romloc;
	}

	public void initPixels() {
		int x = 0;
		int y = 0;
		boolean done = false;
		int index = 0;
		byte[] bitmap = buffer.getBytes();
		// half byte per block
		while (!done) {
			while (x < w) {
				if (y >= h) {
					done = true;
					break;
				}
				byte b = bitmap[index++];
				byte c = bitmap[index++];

				// four bits make a color
				// so, 16 bits contains 4 colors
				// two bytes = 4 pixels
				for (int i = 0, n = 4; i < n; ++i) {
					Color pixel = makeColor(b, c, i,
							BillboardConstants.PRIMARY);
					Color pixel2 = makeColor(b, c, i,
							BillboardConstants.SECONDARY);
					color[x][y][0] = pixel;
					//			System.out.println("("+x+","+y+") = byte index "+(index-2)+","+(index-1));
					color[x][y][1] = pixel2;
					x++;
				}
				if ((bitmap.length == index) || (x > w)) {
					break;
				}
			}

			x = 0;
			y++;
			if (index >= bitmap.length) {
				done = true;
			}
		}
	}

	private Color makeColor(byte first, byte second, int i, int layer) {
		// four bits make a color
		String s1 = ByteConverter.toBinary(first);
		String s2 = ByteConverter.toBinary(second);
		// "" required to force chars to String
		String substring =
				"" + s1.charAt(i) + s1.charAt(i + 4) + s2.charAt(i) + s2.charAt(
						i + 4);

		// convert string to int
		Color use = getColor(substring, layer);
		if (use == null) {
			System.out.println("No color mapping for " + substring);
		}
		return use;
	}

	private Color getColor(String substring, int layer) {
		Color c = null;
		if ("0000".equals(substring)) {
			// outermost surround (skip??)
			c = Color.GRAY;
		}
		else if ("0001".equals(substring)) {
			// inner surround
			c = Color.CYAN;
		}
		else if ("0010".equals(substring)) {
			// inner lower right shadow surround
			c = ColorUtils.BLUE_GRAY;
		}
		else if ("0100".equals(substring)) {
			// purple
			c = ColorUtils.PURPLE;
		}

		if (c != null) {
			return c;
		}
		Color[] colors = paletteMap.get(substring);
		if (colors == null) {
			return null;
		}
		return paletteMap.get(substring)[layer];
	}

	abstract void initPaletteMap();

	@Override
	public void setCoord(int x, int y, Color targetColor, int layer) {
		if (x >= 0 && x < getLayerWidth() && y >= 0 && y < getLayerHeight()) {
			Color otherLayerColor = getCoord(x, y, Math.abs(layer - 1));
			String byteStr = getTargetWordColor(targetColor, otherLayerColor,
					layer);
			if (byteStr == null) {
				return;
			}
			color[x][y][layer] = targetColor;
		}
	}

	private void setColorWordToBytes(int index, int offset, String byteStr,
			BitmapBuffer buffer) {
		for (int j = 0; j < 2; ++j) {
			byte orig = buffer.getByte(index + j);
			if (byteStr.charAt((j * 2)) == '0') {
				orig = BitUtils.clearBit(orig, 7 - offset);
			}
			else {
				orig = BitUtils.setBit(orig, 7 - offset);
			}
			if (byteStr.charAt(1 + (j * 2)) == '0') {
				orig = BitUtils.clearBit(orig, 3 - offset);
			}
			else {
				orig = BitUtils.setBit(orig, 3 - offset);
			}
			//	String post = ByteConverter.toBinary(orig);
			//	System.out.println("Was " + temp + ", now " + post);
			buffer.setByte(index + j, orig);
		}

	}

	public String getTargetWordColor(Color targetColor, Color existingAltColor,
			int layer) {
		for (Map.Entry<String, Color[]> entry : paletteMap.entrySet()) {
			if (entry.getValue()[layer] == targetColor
					&& entry.getValue()[Math.abs(layer - 1)]
					== existingAltColor) {
				return entry.getKey();
			}
		}
		// see if default
		if (targetColor.equals(Color.GRAY) && existingAltColor.equals(
				Color.GRAY)) {
			return "0000";
		}
		else if (targetColor.equals(Color.CYAN) && existingAltColor.equals(
				Color.CYAN)) {
			return "0001";
		}
		else if (targetColor.equals(ColorUtils.BLUE_GRAY)
				&& existingAltColor.equals(ColorUtils.BLUE_GRAY)) {
			return "0010";
		}
		else if (targetColor.equals(ColorUtils.PURPLE)
				&& existingAltColor.equals(ColorUtils.PURPLE)) {
			return "0100";
		}
		//		log.warn(
		//				"color " + existingAltColor + " has no mapping for chosen "
		//						+ targetColor + ", layer=" + layer);
		return null;
	}

	@Override
	public Color[] getColorPalette(int layer) {
		Color[] colors;
		if (layer != 0) {
			colors = secondaryPalette;
		}
		else {
			colors = primaryPalette;
		}
		return colors;
	}

	@Override
	public Color getCoord(int x, int y, int layer) {
		return color[x][y][layer];
	}

	public void loadBinaryData() throws IOException {

		String firstRom = spriteSize == 16 ? getMiniFirstRom() : getFirstRom();
		String secondRom =
				spriteSize == 16 ? getMiniSecondRom() : getSecondRom();
		imageOffset = spriteSize == 16 ? getMiniOffset() : getOffset();
		// specific files for now
		BitmapBuffer b1 = BitmapBuffer.loadFile(
				romloc + File.separator + firstRom);
		BitmapBuffer b2 = BitmapBuffer.loadFile(
				romloc + File.separator + secondRom);
		largeSpriteBuffer = BitmapBuffer.weave(b1, b2, 1);

		//		System.out.println(
		//				largeSpriteBuffer.dumpHex(0, largeSpriteBuffer.getSize(), 8,
		//						true));

		if (spriteSize == 32) {
			// specific files for now
			b1 = BitmapBuffer.loadFile(
					romloc + File.separator + getMiniFirstRom());
			b2 = BitmapBuffer.loadFile(
					romloc + File.separator + getMiniSecondRom());
			smallSpriteBuffer = BitmapBuffer.weave(b1, b2, 1);
			//			System.out.println(
			//					smallSpriteBuffer.dumpHex(0, smallSpriteBuffer.getSize(), 8,
			//							true));

		}
		buffer = extractSprite(largeSpriteBuffer, spriteSize, imageOffset);
		initPaletteMap();
		initPixels();
	}

	private BitmapBuffer extractSprite(BitmapBuffer originalBuffer,
			int spriteSize, int imageOffset) {
		byte[] bytes = originalBuffer.getBytes();
		byte[] newBytes = new byte[bytes.length];
		int newByteIndex = 0;
		int bytesPerLine = spriteSize / 2;
		for (int n = 0; n < ROWS_PER_BOARD_HEIGHT; ++n) {
			for (int m = 0; m < spriteSize; ++m) {
				for (int l = 0; l < COLUMNS_PER_BOARD_WIDTH; ++l) {
					for (int j = 0; j < bytesPerLine; ++j) {
						int bufferindex =
								imageOffset + j + (bytesPerLine * (m + (
										spriteSize * (l + n
												* COLUMNS_PER_BOARD_WIDTH))));
						if (newByteIndex >= 16384 || bufferindex >= 16384) {
							System.out.println(
									"oops " + (newByteIndex - 1) + " gets "
											+ bufferindex);
						}
						else {
							newBytes[newByteIndex++] = bytes[bufferindex];
						}
					}
				}
			}
		}

		return new BitmapBuffer(newBytes);
	}

	protected abstract int getOffset();

	@Override
	public void save(String romloc) throws IOException {
		Color[][][] smallImage;
		if (spriteSize == 32) {
			// convert color matrix to bytes
			saveColorMatrixToBuffer(color, largeSpriteBuffer, getOffset(),
					spriteSize);
			System.out.println(
					largeSpriteBuffer.dumpHex(0, largeSpriteBuffer.getSize(), 8,
							true));
			BitmapBuffer largePart1 = new BitmapBuffer();
			BitmapBuffer largePart2 = new BitmapBuffer();
			BitmapBuffer.unweave(largeSpriteBuffer, largePart1, largePart2, 1);
			// auto generate smaller image
			smallImage = new Color[w / 2][h / 2][2];
			for (int k = 0; k < 2; ++k) {
				for (int y = 0; y < h / 2; ++y) {
					for (int x = 0; x < w / 2; ++x) {
						smallImage[x][y][k] = getCoord(x * 2 + 1, y * 2 + 1, k);
					}
				}
			}
			BitmapBuffer.writeFile(largePart1,
					romloc + File.separator + getFirstRom() + "_new");
			BitmapBuffer.writeFile(largePart2,
					romloc + File.separator + getSecondRom() + "_new");
			saveColorMatrixToBuffer(smallImage, smallSpriteBuffer,
					getMiniOffset(), 16);

		}
		else {
			smallImage = color;
			saveColorMatrixToBuffer(smallImage, largeSpriteBuffer,
					getMiniOffset(), spriteSize);
			smallSpriteBuffer = largeSpriteBuffer;
		}

		BitmapBuffer smallPart1 = new BitmapBuffer();
		BitmapBuffer smallPart2 = new BitmapBuffer();
		BitmapBuffer.unweave(smallSpriteBuffer, smallPart1, smallPart2, 1);

		BitmapBuffer.writeFile(smallPart1,
				romloc + File.separator + getMiniFirstRom() + "_new");
		BitmapBuffer.writeFile(smallPart2,
				romloc + File.separator + getMiniSecondRom() + "_new");
	}

	private void saveColorMatrixToBuffer(Color[][][] smallImage,
			BitmapBuffer targetBuffer, int imageOffset, int spriteSize) {
		// generate small version
		//		int size = (ROWS_PER_BOARD_HEIGHT * spriteSize
		//				+ COLUMNS_PER_BOARD_WIDTH * spriteSize);
		int size = targetBuffer.getSize();
		BitmapBuffer smallWorkingBuffer = new BitmapBuffer(new byte[size]);
		// ok, we have color matrix.
		// to turn to bytes...
		for (int x = 0; x < COLUMNS_PER_BOARD_WIDTH * spriteSize; ++x) {
			for (int y = 0; y < ROWS_PER_BOARD_HEIGHT * spriteSize; ++y) {
				Color primary = smallImage[x][y][0];
				Color secondary = smallImage[x][y][1];
				String byteStr = getTargetWordColor(primary, secondary,
						BillboardConstants.PRIMARY);
				int pixelsByRowY = y * (COLUMNS_PER_BOARD_WIDTH * spriteSize);
				// important math; don't try to simplify
				int index = ((pixelsByRowY + x) / 4) * 2;
				int bitOffset = x % 4;
				setColorWordToBytes(index, bitOffset, byteStr,
						smallWorkingBuffer);
			}
		}
		// unmap bytes from original small sprite buffer
		insertSprite(smallWorkingBuffer, targetBuffer, imageOffset, spriteSize);
	}

	private void insertSprite(BitmapBuffer source, BitmapBuffer target,
			int imageOffset, int spriteSize) {
		byte[] newBytes = source.getBytes();
		// put it back together
		int newByteIndex = 0;
		int bytesPerLine = spriteSize / 2;
		for (int n = 0; n < ROWS_PER_BOARD_HEIGHT; ++n) {
			for (int m = 0; m < spriteSize; ++m) {
				for (int l = 0; l < COLUMNS_PER_BOARD_WIDTH; ++l) {
					for (int j = 0; j < bytesPerLine; ++j) {
						int bufferindex =
								imageOffset + j + (bytesPerLine * (m + (
										spriteSize * (l + n
												* COLUMNS_PER_BOARD_WIDTH))));
						target.setByte(bufferindex, newBytes[newByteIndex++]);
					}
				}
			}
		}
	}

	abstract String getFirstRom();

	abstract String getSecondRom();

	protected abstract int getMiniOffset();

	abstract String getMiniFirstRom();

	abstract String getMiniSecondRom();

	public static Color findNearestColor(Color[] palette, Color targetColor) {
		Color nearestColor = palette[0];
		double minDistance = Double.MAX_VALUE;

		for (Color paletteColor : palette) {
			double distance = colorDistance(targetColor, paletteColor);
			if (distance < minDistance) {
				minDistance = distance;
				nearestColor = paletteColor;
			}
		}

		return nearestColor;
	}

	// Function to calculate Euclidean distance between two colors
	public static double colorDistance(Color c1, Color c2) {
		double redDiff = c1.getRed() - c2.getRed();
		double greenDiff = c1.getGreen() - c2.getGreen();
		double blueDiff = c1.getBlue() - c2.getBlue();

		return Math.sqrt(redDiff * redDiff + greenDiff * greenDiff
				+ blueDiff * blueDiff);
	}

	@Override
	public void importImage(BufferedImage image, int layer) {
		// Get the dimensions of the image
		int width = image.getWidth();
		int height = image.getHeight();
		int exactWidth = spriteSize * COLUMNS_PER_BOARD_WIDTH;
		int exactHeight = spriteSize * ROWS_PER_BOARD_HEIGHT;
		if (width != exactWidth || height != exactHeight) {
			throw new IllegalArgumentException(
					"Image must be " + exactWidth + " pixels wide and "
							+ exactHeight + " pixels high.");
		}

		// Iterate through each pixel of the image
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				// Get the color of the current pixel
				Color pixelColor = new Color(image.getRGB(x, y));
				Color finalColor = findNearestColor(
						layer == 0 ? primaryPalette : secondaryPalette,
						pixelColor);
				setCoord(x,y,finalColor,layer);
			}
		}
	}

	@Override
	public BufferedImage convertToBufferedImage(int layer) {
		BufferedImage image = new BufferedImage(
				COLUMNS_PER_BOARD_WIDTH * spriteSize,
				ROWS_PER_BOARD_HEIGHT * spriteSize, BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < COLUMNS_PER_BOARD_WIDTH * spriteSize; x++) {
			for (int y = 0; y < ROWS_PER_BOARD_HEIGHT * spriteSize; y++) {
				image.setRGB(x, y, color[x][y][layer].getRGB());
			}
		}
		return image;
	}
}