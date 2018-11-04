package Verktyg;

import java.util.Arrays;

/**Klassen som hanterar dekompressionen av en BCI fil till en MTG fil 
 * @author Barry Al-Jawari
 
 */
public class Dekompression {
    private static final int RESERVED_FOR_COLORS = 1024;

    /**
     * Kör decompressionen
     * @param bytes compressed byte-array
     * @return decompressed byte-array
     */
    public static byte[] run(byte[] bytes) {
        byte[] out = new byte[(bytes.length - (RESERVED_FOR_COLORS)) * 3];

        int[] cole = Dekompression.createColors(bytes);

        int s = 0;
        //återställa färgerna genom att hämta rätt färg från
        //färg arrayen
        for (int i = RESERVED_FOR_COLORS; i < bytes.length; i++) {
            int c = cole[bytes[i] & 0xFF];

            out[s++] = (byte) ((c >>> 16) & 0xFF);
            out[s++] = (byte) ((c >>> 8)  & 0xFF);
            out[s++] = (byte)  (c & 0xFF);
        }

        return out;
    }

    /**
     * Återskapar arrayen av de använda färgerna som är sparad i början av byte[] från
     * compressed filen.
     * @param bytes compressed byte-array
     * @return the color array
     */
    private static int[] createColors(byte[] bytes) {
        bytes = Arrays.copyOfRange(bytes, 0, RESERVED_FOR_COLORS);
        int[] cole = new int[256];

        for (int i = 0, c = 0, b = 0; i < bytes.length; i += 4, c++) {
            int color = 0;

            color |= (bytes[b++] & 0xFF) << 24;
            color |= (bytes[b++] & 0xFF) << 16;
            color |= (bytes[b++] & 0xFF) << 8;
            color |= (bytes[b++] & 0xFF);

            cole[c] = color;
        }

        return cole;
    }
}
