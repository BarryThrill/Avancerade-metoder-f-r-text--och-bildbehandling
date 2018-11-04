package RunProgram;

import Applikation.Applikation;

/**
 * MTG2PNG
 *
 * @author Barry Al-Jawari
 */
public class MTG2PNG {
    public static void main(String[] args) {
        String mode = "mtg2png";
        String in = RunProgram.FilePlace + RunProgram.Imagefile + "-dekomprimeras.mtg";
        String ut = RunProgram.FilePlace + RunProgram.Imagefile + "-ny.png";

        Applikation.run(mode, in, ut);
    }
}
