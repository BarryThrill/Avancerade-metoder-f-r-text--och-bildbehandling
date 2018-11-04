package StringSearch;

/**
 * 
 * @author Barry Al-Jawari
 *
 */

import java.io.IOException;

class Start {

    public static void main(String[] args) throws IOException {
    	Read file = new Read();
		GUI panel = new GUI(file);
	}
}
