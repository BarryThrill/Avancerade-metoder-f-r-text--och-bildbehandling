package RunProgram;

import Applikation.Applikation;

/**
 * BEM2MTG
 *
 * @author Barry Al-Jawari
 */
public class BEM2MTG {
    public static void main(String[] args) {
        String mode = "bem2mtg";
        String in = RunProgram.FilePlace + RunProgram.Imagefile + ".BEM";
        String ut = RunProgram.FilePlace + RunProgram.Imagefile + "-dekomprimeras.mtg";

        Applikation.run(mode, in, ut);
    }
}
