package StringSearch;

/**
 * 
 * @author Barry Al-Jawari
 *
 */

public class NaiveKMP {
	/**
	 * Metod som får två paramters. Har två variabler A och B.
	 * Den för slingan går genom A variabeln minus B variabeln.
	 * Sedan en annan för slingan går igenom patternString.
	 * Om inte strängen är lika med patternString finns ingen match.
	 * Om de är lika det skriver ut "träff".
	 * Då returnerar metoden körtiden i nano sekunder.
	 * @param string
	 * @param patternString
	 *
	 */
	public void naiveMatching(char[] string, char[] patternString) {
		int A = string.length;
		int B = patternString.length;
		for (int i = 0; i < A - B + 1; i++) {
			int j;
			for (j = 0; j < B; j++) {
				if (string[i + j] != patternString[j])
					break;
			}
			if (j == B) {
				System.out.println("Match found at index " + i);
			}
		}
	}

	/**
	 * Metod som får en char array som en parameter.
	 * Medan currentIndex är mindre än det sökta mönstret och
	 * Om patternString currentIndex är lika med patternStrings längd finns en match.
	 * Annars om längden inte är lika med 0 den partialMatch längden är minus ett.
	 * Else den currentIndex är lika med 0 och currentIndex ökar med ett.
	 * @param patternString
	 * @return an array of int.
	 */
	private int[] KMP(char[] patternString) {
		int PatternLenght = patternString.length;
		int DelvisMatch[] = new int[PatternLenght];

		DelvisMatch[0] = 0;

		int length = 0;
		int currentIndex = 1;
		while (currentIndex < PatternLenght) {
			if (patternString[currentIndex] == patternString[length]) {
				length = length + 1;
				DelvisMatch[currentIndex] = length;
				currentIndex = currentIndex + 1;
			} else {
				if (length != 0) {
					length = DelvisMatch[length - 1];
				} else {
					DelvisMatch[currentIndex] = 0;
					currentIndex = currentIndex + 1;
				}
			}
		}
		return DelvisMatch;
	}
	/**
	 * Metod som söker efter mönster med hjälp av metoden KMP och utskrifter om vi hittade en match.
	 * Om det grundar en match den håller på att jämföra tills en obalans. När obalans hittas det tar ett steg bakåt.
	 *
	 * @param string
	 * @param patternString
	 */
	public void indexPatternKMP(char[] string, char[] patternString) {

		int textLength = string.length;
		int PatternLenght = patternString.length;
		int DelvisMatch[] = KMP(patternString);
		int currentIndexText = 0;
		int currentIndexPattern = 0;

		while (currentIndexText < textLength) {
			if (string[currentIndexText] == patternString[currentIndexPattern]) {
				currentIndexPattern = currentIndexPattern + 1;
				currentIndexText = currentIndexText + 1;
			}
			if (currentIndexPattern == PatternLenght) {
				System.out.println("Match found at index " + (currentIndexText - PatternLenght));
				currentIndexPattern = DelvisMatch[currentIndexPattern - 1];
			} else if (currentIndexText < textLength && string[currentIndexText] != patternString[currentIndexPattern]) {
				if (currentIndexPattern != 0) {	
					currentIndexPattern = DelvisMatch[currentIndexPattern - 1];
				} else {
					currentIndexText = currentIndexText + 1;
				}
			}
		}
	}
}