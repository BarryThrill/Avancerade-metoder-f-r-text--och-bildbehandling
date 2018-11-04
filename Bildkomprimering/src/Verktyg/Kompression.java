package Verktyg;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.util.*;

/**
 * Detta �r klassen som hanterar kompressionen av en MTG bild-fil til en BCI fil genom att �ndra f�rgernas djup
 * till 256 f�rger. Den anv�nder en array av 2556 bilder skapade fr�nbilden som e suppliad.
 * F�rg arrayen �r skriven i b�rjan av byte-arrayen som e skapad i denna klass.
 * @author Barry Al-Jawari
 */
public class Kompression {
    /**
     *
     * K�r kompressionen
     * @param bild the bild fil till compress
     * @return byte[] inneh�ller compressed bild
     */
    public static byte[] run(BufferedImage bild) {
        byte[] bytes = ((DataBufferByte)bild.getRaster().getDataBuffer()).getData();
        byte[] out = new byte[(256 * 4) + (bytes.length / 3)];
        int index = 0;
        //skapar f�rg paletten
        int[] cole = Kompression.createColors(bild);

        // skriver anv�nda f�rger till bilden
        for (int color : cole) {
            for (int i = 4; i > 0; i--) {
                byte b = (byte)((color >>> ((i - 1) * 8)) & 0xFF);
                out[index++] = b;
            }
        }
        // loopar genom byte[] bytes f�r d� orginal f�rgen och ers�tter det med den som skapades
        // fr�n color paletten
        for (int i = 0; i < bytes.length; i += 3) {
            int b = bytes[i + 2] & 0xFF;
            int g = bytes[i + 1] & 0xFF;
            int r = bytes[i]     & 0xFF;

            out[index++] = (byte) getEquivalentColor(b, g, r, cole);
        }

        return out;
    }

    /**
     * SKapar en array av de mest anv�nda f�rgenra i bilden.
     * @param bild the bild till compress
     * @return color array med integers
     */
    private static int[] createColors(BufferedImage bild) {
        int[] cole = new int[256];
        HashMap<Integer, Integer> map = new HashMap<>();
        Raster raster = bild.getRaster();

        // hitta alla f�rgenra som existerar i bilden och l�gger dem i en 
        // tempor�r mapp med f�rg som dens nyckel.
        int[] p = new int[3];
        // loopen loopar inte igenom alla pixlar, den hoppar 5 pixel/iteration 
        // i b�de bredden och h�jden f�r att spara p� tid.
        for (int x = 0, W = bild.getWidth(), color; x < W; x += 5) {
            for (int y = 0, H = bild.getHeight(); y < H; y += 5) {
                p = raster.getPixel(x, y, p);

                color = BildUtils.toInt(p[0], p[1], p[2]);

                int n = map.containsKey(color) ? map.get(color) + 1: 1;
                map.put(color, n);
            }
        }

        // skapar en tempor�r lista som inneh�ller nycklar av hashmappen.
        LinkedList<Integer> list = new LinkedList<>(map.keySet());

        // Sortera listan genom att de mest frekvent f�rekommande f�rger f�rst.
        list.sort((o1, o2) -> map.get(o2) - map.get(o1));

        // l�gger till f�rgerna som anv�nds mest och skiljer sig fr�n threshold
        // v�rden till den slutliga array av f�rger.
        cole[0] = list.get(0);

        for (int i = 0, index = 1; i < list.size() && index < 232; i++) {
            boolean enghDistance = true;

            for (int j = 0; j < index; j++) {
            	// Checkar ifall f�rgerna �r sepererade tillr�ckligt till att l�gga till,
            	// f�r att f� en bra spridning
                if (BildUtils.distanceInLAB(cole[j], list.get(i)) < 3) {
                    enghDistance = false;
                    break;
                }
            }

            // om ingen f�rg som var liknande hittas, s� inkluderas f�rgen.
            if (enghDistance) {
                cole[index++] = list.get(i);
            }
        }

        // l�gger till greyscale f�rgerna till listan av f�rger.
        for (int i = 232, intensity = 0; i <= 255; i++) {
            cole[i] = BildUtils.toInt(intensity, intensity, intensity);
            intensity = Math.min(255, intensity + 13);
        }
        return cole;
    }

    /** 
     * Returnerar f�rgen, n�rmaste f�rgen skickas till metod som parameter(int b,g,r)
     * fr�n f�rg paletten
     * @param b int value for blue color
     * @param g int value for green color
     * @param r int value for red color
     * @param cole array containing color palette
     * @return the equivalent color
     */
    private static int getEquivalentColor(int b, int g, int r, int[] cole) {
        int color = -1;
        int c = BildUtils.toInt(b, g, r);
        double minDistance = Double.MAX_VALUE;

        // loopar igenom f�rgernas array j�mf�r f�rgerna med f�rgerna.
        //fr�n original bilden och v�ljer den n�rmaste
        for (int p = 0; p < cole.length; p++) {
            double d = BildUtils.distanceInLAB(cole[p], c);
            if (d < minDistance) {
                color = p;
                minDistance = d;
            }
        }

        return color;
    }
}
