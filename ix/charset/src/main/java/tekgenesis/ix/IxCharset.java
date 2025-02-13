
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.ix;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.util.HashMap;
import java.util.Map;

/**
 * User: emilio Date: 4/12/11 Time: 10:49
 */
@SuppressWarnings("MagicNumber")
class IxCharset extends Charset {

    //~ Constructors .................................................................................................................................

    private IxCharset() {
        super("IDEAFIX", null);
    }

    //~ Methods ......................................................................................................................................

    @Override public boolean contains(Charset cs) {
        return "US-ASCII".equals(cs.name()) || cs instanceof IxCharset;
    }

    @Override public CharsetDecoder newDecoder() {
        return new Decoder(this);
    }

    @Override public CharsetEncoder newEncoder() {
        return new Encoder(this);
    }

    //~ Static Fields ................................................................................................................................

    public static Charset IDEAFIX_CHARSET = new IxCharset();
    /*
     * Ideafix Drawing characters
     */

    private static final char[]               table   = new char[256];
    private static final Map<Character, Byte> reverse = new HashMap<>();

    static {
        for (int i = 0; i < table.length; i++)
            table[i] = (char) i;
        // Single Lines
        table[160] = 0x2503;  // Vertical
        table[161] = 0x2501;  // Horizontal
        table[162] = 0x2517;  // Lower Left
        table[163] = 0x253B;  // Bottom
        table[164] = 0x251B;  // Lower Right
        table[165] = 0x252B;  // Right
        table[166] = 0x254B;  // Center
        table[167] = 0x2523;  // Left
        table[168] = 0x250F;  // Upper Left
        table[169] = 0x2533;  // Top
        table[170] = 0x2513;  // Upper Right

        // Double Lines
        table[176] = 0x2551;  // Vertical
        table[177] = 0x2550;  // Horizontal
        table[178] = 0x255A;  // Lower Left
        table[179] = 0x2569;  // Bottom
        table[180] = 0x255D;  // Lower Right
        table[181] = 0x2563;  // Right
        table[182] = 0x256C;  // Center
        table[183] = 0x2560;  // Left
        table[184] = 0x2554;  // Upper Left
        table[185] = 0x2566;  // Top
        table[186] = 0x2557;  // Upper Right

        // Simple/Double
        table[188] = 0x2567;  // Bottom
        table[189] = 0x2562;  // Right
        table[190] = 0x255F;  // Left
        table[191] = 0x2564;  // Top

        for (int i = 0; i < table.length; i++)
            reverse.put(table[i], (byte) i);
    }

    //~ Inner Classes ................................................................................................................................

    static class Decoder extends CharsetDecoder {
        Decoder(Charset cs) {
            super(cs, 1.0f, 1.0f);
        }

        @Override protected CoderResult decodeLoop(ByteBuffer src, CharBuffer dst) {
            int mark = src.position();
            try {
                while (src.hasRemaining()) {
                    final byte b = src.get();
                    if (!dst.hasRemaining()) return CoderResult.OVERFLOW;
                    dst.put(table[b & 0xff]);
                    mark++;
                }
                return CoderResult.UNDERFLOW;
            }
            finally {
                src.position(mark);
            }
        }
    }

    static class Encoder extends CharsetEncoder {
        private Encoder(Charset cs) {
            super(cs, 1.0f, 1.0f);
        }

        public boolean canEncode(char c) {
            return c <= '\u00FF';
        }

        public boolean isLegalReplacement(byte[] r) {
            return true;
        }

        @Override protected CoderResult encodeLoop(CharBuffer src, ByteBuffer dst) {
            int mark = src.position();
            try {
                while (src.hasRemaining()) {
                    final char c = src.get();
                    final Byte b = reverse.get(c);
                    if (b == null) return CoderResult.unmappableForLength(1);
                    if (!dst.hasRemaining()) return CoderResult.OVERFLOW;
                    dst.put(b);
                    mark++;
                }
                return CoderResult.UNDERFLOW;
            }
            finally {
                src.position(mark);
            }
        }
    }  // end class Encoder
}  // end class IxCharset
