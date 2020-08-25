

/**

Link to problem: https://open.kattis.com/problems/substrings
Goal is to find the number of repeated substring in a string.
- There can be at most N - 1 length repeated substring.

if a substring of length l is repeated then each of the substrings of the repeated substring are also repeated.


Question: can we reduce this to the longest repeated substring?


Algorithm 1:

find the longest repeated substring. compute the number of repeated substrings as a result of that. then take that substring and all its repetitions out. continue
as long as there is a repeated substring.
	Implementation details:
	   using a Trie data structure:
	   	1. removal of a substring: takes max O(L) time where L is the length of the substring
	   	2. Finding the longest repeated substring: O(N) where N is the length of the trie
	   	3

Pseudocode:
->





**/
public class RepeatedSubstringV1 {
	private static final String EMPTY = "";
	public static int allRepeatedSubstrings(String input) {
		String inputString = new String(input);
		String longestSubstring = getLongestSubstring(inputString);
		int numRepeated = 0;

		/**
		Somethin to test: what about overlapping repetitions?
		**/
		while (!longestSubstring.equals(EMPTY)) {
			inputString = inputString.remove(longestSubstring); // may not be the actual syntax
			numRepeated += (longestSubstring.length()*longestSubstring.length() - longestSubstring.length())/2;
		}

		return numRepeated;
	}

	public static String longestSubstring(String input) {

	}

	public static void main(String[] args) {
		Scanner in = new Scanner(new BufferedReader(new InputStreamReader(System.in)));
        int t = in.nextInt(); 
        in.nextLine();
        for (int i = 1; i <= t; ++i) {
        	String input = in.nextLine(); 
            int n = allRepeatedSubstrings(input);
            System.out.println("Case #" + i + ": " + (n));
        }

	}
	
}