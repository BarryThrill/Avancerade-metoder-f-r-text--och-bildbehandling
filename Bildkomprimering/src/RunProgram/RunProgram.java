package RunProgram;

/**
 * Kör programmet klass
 *
 * @author Barry Al-Jawari
 */
public class RunProgram {
		 
	public final static String FilePlace  = "C:/Users/Barry-PC/Desktop/megatronfiles/";
	public final static String Imagefile = "cartoon";
		    
    public static void main(String[] args) {
        MTG2BEM.main(args);
        BEM2MTG.main(args);
        MTG2PNG.main(args);
    }
}
