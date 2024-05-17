package org.mtk.util;

public class ByteConverter {

	public static int toUnsignedByteValue(byte b) {
		if ((int) b >= 0) {
			return b;
		}
		return 256 + (int) b;
	}

	static public String toBinary(byte b) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 8; ++i) {
			sb.append((((b << i) & 0x80) == 0x80) ? "1" : "0");
		}
		return sb.toString();
	}

	static public String toHex(byte b) {
		String ba = Integer.toHexString(b);
		ba = ba.trim();
		int start = (ba.length() > 1) ? ba.length() - 2 : 0;
		String ta = ba.substring(start);
		ta = (ta.length() < 2) ? "0" + ta : ta;
		ta = ta.toUpperCase();
		return ta;

	}

	static public String toHex(int i) {
		byte b = (byte) (i & 0x000000FF);
		return toHex(b);
	}

	static public byte convertBinaryString(String str) {
		if (str == null || str.length() != 8) {
			throw new IllegalArgumentException(
					"String must be 8 chars to represent bits");
		}
		return Byte.parseByte(str, 2);
	}
}
