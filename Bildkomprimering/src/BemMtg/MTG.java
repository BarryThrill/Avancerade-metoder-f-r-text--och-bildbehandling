package BemMtg;

import java.awt.image.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import javax.imageio.*;

public class MTG {
    /** Magic startar string för att kolla MTG fil format. */
    final static byte[] magic = "mEgaMADNZ!".getBytes(StandardCharsets.US_ASCII);

    public final static class InvalidMegatronFileException extends IOException { }

    public static void write(BufferedImage bild, String drake) throws IOException {
        int width  = bild.getWidth();
        int height = bild.getHeight();
        int[] pixel = new int[3];
        Raster bildq  = bild.getRaster();
        OutputStream out = new FileOutputStream(drake);
        out.write(magic);
        write4bytes(width, out);
        write4bytes(height, out);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                bildq.getPixel(i, j, pixel);
                out.write(pixel[0]);
                out.write(pixel[1]);
                out.write(pixel[2]);
            }
        }
        out.close();
    }

    public static BufferedImage read(String drake) throws IOException {
        InputStream in = new FileInputStream(drake);

        // Checkar magic värdet.
        for (int i = 0; i < magic.length; i++) {
            if (in.read() != magic[i]) { throw new InvalidMegatronFileException(); }
        }

        int width  = read4bytes(in);
        int height = read4bytes(in);

        BufferedImage bild = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);

        byte[] pixelBytes = new byte[3];
        int[] pixel = new int[3];

        WritableRaster bildq  = bild.getRaster();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (in.read(pixelBytes) != 3) { throw new EOFException(); }
                pixel[0] = pixelBytes[0];
                pixel[1] = pixelBytes[1];
                pixel[2] = pixelBytes[2];
                bildq.setPixel(i, j, pixel);
            }
        }

        in.close();
        return bild;
    }

    /** Skriver en int som 4 bitar. */
    private static void write4bytes(int v, OutputStream out) throws IOException {
        out.write(v>>>3*8);
        out.write(v>>>2*8 & 255);
        out.write(v>>>1*8 & 255);
        out.write(v       & 255);
    }

    /** Läser en int som 4 bitar. */
    private static int read4bytes(InputStream in) throws IOException {
        int b, v = 0;

        b = in.read(); if (b < 0) { throw new EOFException(); }
        v = b<<3*8;
        b = in.read(); if (b < 0) { throw new EOFException(); }
        v |= b<<2*8;
        b = in.read(); if (b < 0) { throw new EOFException(); }
        v |= b<<1*8;
        b = in.read(); if (b < 0) { throw new EOFException(); }
        v |= b;

        return v;
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 3 || !"mtg2png".equals(args[0]) && !"makemtg".equals(args[0])) {
            System.err.println("Expected arguments: mtg2png infile.mtg outfile.png\n"+
                    "                or: makemtg infile.whatever outfile.mtg");
            System.exit(1);
        }
        if ("makemtg".equals(args[0])) {
            BufferedImage bild  = ImageIO.read(new File(args[1]));
            MTG.write(bild, args[2]);
        } else {
            BufferedImage bild = MTG.read(args[1]);
            ImageIO.write(bild, "PNG", new File(args[2]));
        }
    }
}


