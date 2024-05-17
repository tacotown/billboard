package org.mtk.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class BitmapBuffer {

	byte[] bitmap;

	public BitmapBuffer() {
		bitmap = new byte[0];
	}

	public BitmapBuffer(byte[] b) {
		bitmap = b;
	}

	public static void writeFile(BitmapBuffer buffer, String s)
			throws IOException {
		File outputFile = new File(s);
		if (outputFile.exists()) {
			throw new IllegalArgumentException("File exists");
		}
		if (!outputFile.createNewFile()) {
			throw new IllegalArgumentException("File exists");
		}
		try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
			outputStream.write(buffer.getBytes());
		}
	}

	/**
	 */
	public void setBit(int offset, int i, boolean tf) {
		if (tf) {
			bitmap[offset] = (byte) (bitmap[offset] | (byte) Math.pow(2,
					i % 8));
		}
		else {
			bitmap[offset] = (byte) (bitmap[offset] ^ (byte) Math.pow(2,
					i % 8));
		}

	}

	/**
	 */
	public boolean getBit(int offset, int i) {
		return ((bitmap[offset] >> i & 0x01) == 0x01);
	}

	/**
	 */
	public byte[] getBytes(int offset, int buffersize) {
		byte[] temp = new byte[buffersize];
		System.arraycopy(bitmap, offset, temp, 0, buffersize);
		return temp;
	}

	public byte[] getBytes(int offset) {
		byte[] temp = new byte[bitmap.length - offset];
		System.arraycopy(bitmap, offset, temp, 0, temp.length);
		return temp;
	}

	public byte[] getBytes() {
		return bitmap;
	}

	/**
	 */
	public static BitmapBuffer loadFile(String string) throws IOException {
		File f = new File(string);
		return loadFile(f);
	}

	public static BitmapBuffer loadFile(File f) throws IOException {
		byte[] get = new byte[(int) f.length()];
		FileInputStream fis = new FileInputStream(f);
		fis.read(get);
		fis.close();
		BitmapBuffer buffer = new BitmapBuffer();
		buffer.setContents(get);
		return buffer;

	}

	/**
	 */
	private void setContents(byte[] get) {
		bitmap = get;
	}

	/**
	 *
	 */
	public int getSize() {
		return bitmap.length;

	}

	/**
	 *
	 */
	public String dumpBinary(int start, int length) {
		StringBuilder sb = new StringBuilder();
		for (int i = start; i < length; ++i) {
			sb.append(i).append(": ");
			for (int j = 7; j >= 0; --j) {
				boolean bitOn = ((bitmap[i] >> j & 0x01) == 0x01);
				if (bitOn) {
					sb.append("1");
				}
				else {
					sb.append("0");
				}

			}
		}
		return sb.toString();
	}

	public String dumpHex() {
		return dumpHex(0, getSize(), 8, false);
	}

	public String dumpHex(int start, int length, int width,
			boolean showAddress) {
		// each byte is treated as an address
		StringBuilder sb = new StringBuilder();
		boolean done = false;
		int index = 0;
		int linelength = 0;
		int linecount = 0;
		while (!done) {
			if (showAddress) {
				String num = "0000" + Integer.toHexString(linecount);
				sb.append(num.substring(num.length() - 4)).append(": ");
			}
			while (linelength < width) {
				sb.append(ByteConverter.toHex(bitmap[index]));
				index++;
				linelength++;
				if ((bitmap.length == index) || (linelength > width)) {
					break;
				}
			}

			sb.append("\n");
			linelength = 0;
			linecount += width;
			if (index >= bitmap.length) {
				done = true;
			}
		}

		return sb.toString();

	}

	public static BitmapBuffer weave(BitmapBuffer b1, BitmapBuffer b2,
			int size) {
		assert (b1.getSize() == b2.getSize());

		byte[] comp = new byte[b1.getSize() + b2.getSize()];

		byte[] bytes1 = b1.getBytes();
		byte[] bytes2 = b2.getBytes();
		int index = 0;
		for (int i = 0, n = bytes1.length / size; i < n; ++i) {
			for (int j = 0; j < size; ++j) {
				comp[index++] = bytes1[i];
			}
			for (int j = 0; j < size; ++j) {
				comp[index++] = bytes2[i];
			}
		}
		return new BitmapBuffer(comp);
	}

	/**
	 */
	public byte getByte(int offset) {
		return bitmap[offset];
	}

	public void setByte(int offset, byte b) {
		bitmap[offset] = b;
	}

	public static void unweave(BitmapBuffer input, BitmapBuffer part1,
			BitmapBuffer part2, int skipbytes) {
		int size = input.getSize();
		part1.setContents(new byte[size / 2]);
		part2.setContents(new byte[size / 2]);
		int index = 0;
		int half1index = 0;
		int half2index = 0;
		while (index < size) {
			for (int bytes = 0; bytes < skipbytes; bytes++) {
				part1.setByte(half1index++, input.getByte(index++));
			}
			for (int bytes = 0; bytes < skipbytes; bytes++) {
				part2.setByte(half2index++, input.getByte(index++));
			}
		}
	}

	public static BitmapBuffer join(BitmapBuffer firsthalf,
			BitmapBuffer secondhalf, int bytelength) {

		byte[] lbytes = firsthalf.getBytes();
		byte[] rbytes = secondhalf.getBytes();
		byte[] totalbytes = new byte[rbytes.length + lbytes.length];
		int index = 0;
		int lcount = 0;
		int rcount = 0;
		boolean done = false;
		while (!done) {
			for (int i = 0; i < bytelength; ++i) {
				totalbytes[index] = lbytes[lcount];
				index++;
				lcount++;
			}
			for (int i = 0; i < bytelength; ++i) {
				totalbytes[index] = rbytes[rcount];
				index++;
				rcount++;
			}

			if (lbytes.length * 2 == index) {
				done = true;
			}
		}
		return new BitmapBuffer(totalbytes);
	}

	public byte getChecksum() {
		byte b = 0;
		for (byte value : bitmap) {
			b = (byte) (b + value);
		}
		return b;
	}
}