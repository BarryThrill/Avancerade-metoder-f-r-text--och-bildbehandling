package RunProgram;

import Applikation.Applikation;

/**
 * MTG2BEM
 *
 * @author Barry Al-Jawari
 */
public class MTG2BEM {
    public static void main(String[] args) {
        String mode = "mtg2bem";
        String in = RunProgram.FilePlace + RunProgram.Imagefile + ".mtg";
        String ut = RunProgram.FilePlace + RunProgram.Imagefile + ".BEM";

        Applikation.run(mode, in, ut);
    }
}
