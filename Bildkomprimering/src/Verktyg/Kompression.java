package Verktyg;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.util.*;

/**
 * Detta är klassen som hanterar kompressionen av en MTG bild-fil til en BCI fil genom att ändra färgernas djup
 * till 256 färger. Den använder en array av 2556 bilder skapade frånbilden som e suppliad.
 * Färg arrayen är skriven i början av byte-arrayen som e skapad i denna klass.
 * @author Barry Al-Jawari
 */
public class Kompression {
    /**
     *
     * Kör kompressionen
     * @param bild the bild fil till compress
     * @return byte[] innehåller compressed bild
     */
    public static byte[] run(BufferedImage bild) {
        byte[] bytes = ((DataBufferByte)bild.getRaster().getDataBuffer()).getData();
        byte[] out = new byte[(256 * 4) + (bytes.length / 3)];
        int index = 0;
        //skapar färg paletten
        int[] cole = Kompression.createColors(bild);

        // skriver använda färger till bilden
        for (int color : cole) {
            for (int i = 4; i > 0; i--) {
                byte b = (byte)((color >>> ((i - 1) * 8)) & 0xFF);
                out[index++] = b;
            }
        }
        // loopar genom byte[] bytes får då orginal färgen och ersätter det med den som skapades
        // från color paletten
        for (int i = 0; i < bytes.length; i += 3) {
            int b = bytes[i + 2] & 0xFF;
            int g = bytes[i + 1] & 0xFF;
            int r = bytes[i]     & 0xFF;

            out[index++] = (byte) getEquivalentColor(b, g, r, cole);
        }

        return out;
    }

    /**
     * SKapar en array av de mest använda färgenra i bilden.
     * @param bild the bild till compress
     * @return color array med integers
     */
    private static int[] createColors(BufferedImage bild) {
        int[] cole = new int[256];
        HashMap<Integer, Integer> map = new HashMap<>();
        Raster raster = bild.getRaster();

        // hitta alla färgenra som existerar i bilden och lägger dem i en 
        // temporär mapp med färg som dens nyckel.
        int[] p = new int[3];
        // loopen loopar inte igenom alla pixlar, den hoppar 5 pixel/iteration 
        // i både bredden och höjden för att spara på tid.
        for (int x = 0, W = bild.getWidth(), color; x < W; x += 5) {
            for (int y = 0, H = bild.getHeight(); y < H; y += 5) {
                p = raster.getPixel(x, y, p);

                color = BildUtils.toInt(p[0], p[1], p[2]);

                int n = map.containsKey(color) ? map.get(color) + 1: 1;
                map.put(color, n);
            }
        }

        // skapar en temporär lista som innehåller nycklar av hashmappen.
        LinkedList<Integer> list = new LinkedList<>(map.keySet());

        // Sortera listan genom att de mest frekvent förekommande färger först.
        list.sort((o1, o2) -> map.get(o2) - map.get(o1));

        // lägger till färgerna som används mest och skiljer sig från threshold
        // värden till den slutliga array av färger.
        cole[0] = list.get(0);

        for (int i = 0, index = 1; i < list.size() && index < 232; i++) {
            boolean enghDistance = true;

            for (int j = 0; j < index; j++) {
            	// Checkar ifall färgerna är sepererade tillräckligt till att lägga till,
            	// för att få en bra spridning
                if (BildUtils.distanceInLAB(cole[j], list.get(i)) < 3) {
                    enghDistance = false;
                    break;
                }
            }

            // om ingen färg som var liknande hittas, så inkluderas färgen.
            if (enghDistance) {
                cole[index++] = list.get(i);
            }
        }

        // lägger till greyscale färgerna till listan av färger.
        for (int i = 232, intensity = 0; i <= 255; i++) {
            cole[i] = BildUtils.toInt(intensity, intensity, intensity);
            intensity = Math.min(255, intensity + 13);
        }
        return cole;
    }

    /** 
     * Returnerar färgen, närmaste färgen skickas till metod som parameter(int b,g,r)
     * från färg paletten
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

        // loopar igenom färgernas array jämför färgerna med färgerna.
        //från original bilden och väljer den närmaste
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
