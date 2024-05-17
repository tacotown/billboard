package org.mtk.util;

public class BitUtils {
	public static boolean isSet(byte value, int bit) {
		int b = (int) value & 0xff;
		b >>= bit;
		b &= 0x01;
		return b != 0;
	}

	public static byte setBit(byte value, int bit) {
		int b = (int) value;

		b |= (1 << bit);
		return (byte) (b & 0xff);
	}

	public static byte clearBit(byte value, int bit) {
		int b = (int) value;

		b &= ~(1 << bit);
		return (byte) (b & 0xff);
	}
}
