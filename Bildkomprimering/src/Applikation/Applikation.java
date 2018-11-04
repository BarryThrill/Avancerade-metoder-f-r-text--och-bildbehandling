package Applikation;

import javax.imageio.ImageIO;

import BemMtg.BEM;
import BemMtg.MTG;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author Barry Al-Jawari
 */
public class Applikation {
    /**
     * Körs applikationen.
     *     *
     */
    public static void run(String mode, String input, String output) {
        BufferedImage bild;
        try {
            switch (mode) {
                case "mtg2bem":
                    bild = MTG.read(input);
                    BEM.write(bild, output);
                    break;

                case "bem2mtg":
                    bild = BEM.read(input);
                    MTG.write(bild, output);
                    break;

                case "mtg2png":
                    bild = MTG.read(input);
                    ImageIO.write(bild, "PNG", new File(output));
                    break;
            }
        } catch (IOException e) {
            System.out.println("Prova återigen att köra RunProgram (Eventuellt fel navigering i filerna)");
        }
    }
}
