
import java.io.*;
import java.util.*;
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

	class RollingHash {
		int base;
		int hash;
		final int prime = 7;

		public RollingHash(int alphabetSize) {
			base = alphabetSize;
		}


		public void append(char c) {
			int ord = (int) c;
			// change 12 to 123 -> multiply the hash by length of the hash + ord
			int hashLength = Integer.toString(hash).length();
			hash = hash*(Math.pow(base, hashLength)) + ord;
		}
		public void removeFirst(char c) {
			// change 123 to 23 = 123 - 100 -> subtract  length of the hash + ord
			hash = hash - Math.pow(base, hashLength);

		}

		public int hash() {
			return hash % prime;
		}
	}

	public static int allRepeatedSubstrings(String input) {
		String inputString = new String(input);
		List<String> longestSubstring = getLongestSubstring(inputString);
		int numRepeated = 0;

		/**
		Somethin to test: what about overlapping repetitions?
		**/
		while (longestSubstring.size() != 0) {
			inputString = inputString.replaceAll(longestSubstring, ""); // may not be the actual syntax
			for (String substring : longestSubstring) {
				numRepeated += (substring.length()*substring.length() - substring.length())/2;
			}
			longestSubstring = getLongestSubstring(inputString);
		}
		return numRepeated;
	}

	public static List<String> getLongestSubstring(String input) {
		int low = 1;
		int high = input.length() - 1;
		List<String> reapSubstrings;
		while (high > low) {
			int midWindow = (low + high) /2;
			reapSubstrings = repeatedSubstringsOfSize(input, midWindow);

			//conditionals
			if (reapSubstrings.size() > 0) {
				low = mid;
			} else {
				high = mid;
			}

		}
		return reapSubstrings;
	}

	private static List<String> repeatedSubstringsOfSize(String s, int window) {
		Map<Integer, Integer> hashToCountMap = new HashMap<>();
		Map<Integer, String> hashToSubstringMap = new HashMap<>();
		List<String> allReapSubstrings = new ArrayList<>();
		RollingHash rollingHash = new RollingHash(26);

		int start = 0;
		for(int i = 0; i < s.length(); i++) {
			rollingHash.append(s.charAt(s));
			if (i >= window) {
				rollingHash.removeFirst(start);
				hashToCountMap.put(rollingHash.hash(), hashToCountMap.getOrDefault(rollingHash.hash(), 0) + 1);
				//vertification may be needed
				hashToSubstringMap.put(rollingHash.hash(), s.substring(start, i + 1));
				start++;
			}
		}

		for (int hash : hashToCountMap.keySet()) {
			if (hashToCountMap.get(hash) > 1) allReapSubstrings.add(hashToSubstringMap.get(hash));
			
		}
		return allReapSubstrings;
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