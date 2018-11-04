package StringSearch;

/**
 * 
 * @author Barry Al-Jawari
 *
 */

// Kod för att läsa en komplett textfil till en char array.

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Read {

    public char[] readFile() throws IOException {
        String source = "src/Texter/C++.txt";
        InputStreamReader InputRead = new InputStreamReader(new FileInputStream(source));
        ArrayList<char[]> ListBlocks = new ArrayList<>();
        int bytes = 0;
		char[] buffering = new char[8192];
        int i = 0;
        while (true) {
            int bytesRead = InputRead.read(buffering, i, buffering.length-i);
            if (bytesRead < 0) { 
            	break; 
            	} 
            i += bytesRead;
            bytes += bytesRead;
            if (bytes < 0) {
                throw new ArrayIndexOutOfBoundsException("File too big");
            }
            if (i == buffering.length) {
                ListBlocks.add(buffering);
                buffering = new char[buffering.length];
                i = 0;
            }
        }
        char[] CharList = new char[bytes];
        int remember = 0;
        for (char[] CharListB : ListBlocks) {
            for (char a : CharListB) {
            	CharList[remember++] = a;
            }
        }
        for (int j = 0; j < i; j++) {
        	CharList[remember++] = buffering[j];
        }

        return CharList;

	}
}
