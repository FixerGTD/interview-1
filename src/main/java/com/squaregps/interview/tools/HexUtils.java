package com.squaregps.interview.tools;

import java.nio.ByteBuffer;

/** HEX utils */
public final class HexUtils {

  private static int hexToBin(char ch) {
    if ('0' <= ch && ch <= '9') {
      return ch - '0';
    }
    if ('A' <= ch && ch <= 'F') {
      return ch - 'A' + 10;
    }
    if ('a' <= ch && ch <= 'f') {
      return ch - 'a' + 10;
    }
    return -1;
  }

  public static byte[] parseHexBinary(String s) {
    int len = s.length();
    if (len % 2 != 0) {
      throw new IllegalArgumentException("hexBinary needs to be even-length: " + s);
    }

    byte[] out = new byte[len / 2];

    for (int i = 0; i < len; i += 2) {
      int high = hexToBin(s.charAt(i));
      int low = hexToBin(s.charAt(i + 1));
      if (high == -1 || low == -1) {
        throw new IllegalArgumentException("contains illegal character for hexBinary: " + s);
      }
      out[i / 2] = (byte) (high * 16 + low);
    }

    return out;
  }

  public static ByteBuffer hexToByteBuffer(String s) {
    return ByteBuffer.wrap(parseHexBinary(s));
  }

  private HexUtils() {}
}
