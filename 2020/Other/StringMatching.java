
import java.util.*;
import java.io.*;

public class StringMatching {
	
	/**
	-> Pattern contains:
	'?' Matches any single character.
'*' Matches any sequence of characters (including the empty sequence).
	*/
	public static boolean matchWithWildcards(String original, String pattern) {

		return matchWithWildcards(original, 0, pattern, 0, new HashMap<>());
	}

	private static boolean matchWithWildcards(String original, int i, String pattern, int j, Map<Integer, Map<Integer, Boolean>> memo) {
		if (i >= original.length() || j >= pattern.length()) {
			return false;
		}

		char patternChar = pattern.charAt(j);
		char originalChar = original.charAt(i);

		if (i == original.length() - 1 && j == pattern.length() - 1) {
			return compare(originalChar, patternChar);
		}

		boolean currentSoln = false;
		if (patternChar == '*') {
			boolean soln1 = memo.containsKey(i) && memo.get(i).containsKey(j + 1) ? memo.get(i).get(j + 1) :
							matchWithWildcards(original, i, pattern, j + 1, memo);
			boolean soln2  = memo.containsKey(i + 1) && memo.get(i + 1).containsKey(j) ? memo.get(i + 1).get(j) : 
							matchWithWildcards(original, i + 1, pattern, j, memo);
			currentSoln = soln2 || soln1;
		} else {
			currentSoln = compare(patternChar, originalChar)  && (memo.containsKey(i+1) && memo.get(i+1).containsKey(j + 1) ? memo.get(i+1)
																 .get(j + 1) : matchWithWildcards(original, i + 1, pattern, j + 1, memo));
		}

		Map<Integer, Boolean> jToSolnMap = new HashMap<>();
		jToSolnMap.put(j, currentSoln);
		memo.put(i, jToSolnMap);
		return currentSoln;
	}

	private static boolean compare(char c1, char c2) {
		return c1 == c2 || c2 == '?' || c2 == '*';
	}


	public static void main(String[] args) {
		Scanner in = new Scanner(new BufferedReader(new InputStreamReader(System.in)));
        int t = in.nextInt(); 
        in.nextLine();
        for (int i = 1; i <= t; ++i) {
          String[] s = in.nextLine().split(" ");
          String original = s[0];
          String pattern = s[1];
          System.out.println("Case #" + i + ": " + (matchWithWildcards(original, pattern)));
      }
	}
}

