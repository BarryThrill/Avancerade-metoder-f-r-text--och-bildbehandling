package BemMtg;

import java.awt.image.*;
import java.io.*;

import Verktyg.*;

/**
 * Denna klass är till för att kompressa en bild till den Seriously High-tech bild typen (BEM) formatet.
 *
 * @author Barry Al-Jawari
 */
public class BEM {
    private final static byte[] WATERMARK = "BEM_file".getBytes();

    /**
     * Skriver en BEM bild till en fil.
     *
     * @param bild Bild
     * @param filnamn Filnamn
     * @throws IOException
     */
    public static void write(BufferedImage bild, String filnamn) throws IOException {
        int W = bild.getWidth();
        int H = bild.getHeight();

        OutputStream out = new FileOutputStream(filnamn);

        // Skriver the watermark till filen
        out.write(WATERMARK);

        // Skriv höjden och bredden till filen.
        write4bytes(W, out);
        write4bytes(H, out);

        // Kör kompression
        byte[] bytes = Kompression.run(bild);

        for (int i = 0; i < bytes.length; i++) {
            out.write(bytes[i]);
        }

        out.close();
    }

    /**
     * Läser en BEM bild från filen.
     *
     * @param filnamn Filnamn
     * @return BufferedImage
     * @throws IOException
     */
    public static BufferedImage read(String filnamn) throws IOException {
        InputStream in = new FileInputStream(filnamn);
        BufferedImage bild;

        // Läser BEM watermark från filen.
        for (int i = 0; i < WATERMARK.length; i++) {
            if (in.read() != WATERMARK[i]) { throw new IOException(); }
        }

        // Läser bredden och höjden från filen
        int W  = read4bytes(in);
        int H = read4bytes(in);

        byte[] buffer = new byte[4096];

        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        int length = -1;
        while ((length = in.read(buffer)) != -1) {
            bos.write(buffer, 0, length);
        }

        byte[] bytes = bos.toByteArray();

        // Kör dekompressionen
        bytes = Dekompression.run(bytes);

        bild = new BufferedImage(W, H, BufferedImage.TYPE_INT_RGB);
        WritableRaster raster = bild.getRaster();

        for (int y = 0, i = 0; y < H; y++) {
            for (int x = 0; x < W; x++) {
                raster.setPixel(x, y, new int[] {
                        bytes[i++] & 0xFF,
                        bytes[i++] & 0xFF,
                        bytes[i++] & 0xFF,
                });
            }
        }

        return bild;
    }

    /**
     * Skriver 4 bytes till den specifika output streamen.
     *
     * @param v Value
     * @param out Output stream
     * @throws IOException
     */
    private static void write4bytes(int v, OutputStream out) throws IOException {
        out.write(v>>>3*8);
        out.write(v>>>2*8 & 255);
        out.write(v>>>1*8 & 255);
        out.write(v       & 255);
    }

    /**
     * Läser 4 bytes från input streamen.
     *
     * @param in Input stream
     * @return Value
     * @throws IOException
     */
    private static int read4bytes(InputStream in) throws IOException {
        int b, v = 0;

        for (int i = 3; i >= 0; i--) {
            b = in.read();
            if (b < 0) {
                throw new EOFException();
            }
            v |= b << i * 8;
        }

        return v;
    }
}
