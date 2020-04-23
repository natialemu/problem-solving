import java.util.*;
import java.io.*;

/**
An ADT that represents the hash value of a string of characters
**/
class RollingHash {
	int hash, base, size, prime;
	public RollingHash(int base) {
		this.base = base;
		this.prime = 17;
	}

	//computes the new hash of the string with 'c' appended at the end in constant time
	// Eg: if current hash is 123, the base is 10, & c is represented by the number 4. the new hash should be 1234.
	// step one would be to shift 123 to the left to make space for 4. this means converting 123 to 1230(multiple by the base).
	// then add 4 aka 'c'.
	public void append(char c) {
		// if the right base is not used,
		// when shifting to the left by base,
		// enough space may not be created to add up c
		// so the hash will be distorted
		hash =  hash*base + (c);
		size += 1;
	}

	// computes the new hash of the string with 'c' removed from the front of the string
	// Eg: if current hash is 123, the base is 10,the new hash should be 23.
	// 23 is the same as 123 - 100. this means subtracting 1*10^2 from 123.
	// then add 4 aka 'c'.
	public void removeHead(char c) {
		int shift = (int)(Math.pow(base, size - 1));
		hash =  hash - (c)*shift;
		size -= 1;
	}
	public int hash() {
		return hash % prime;
	}
}

/**
Problem: https://leetcode.com/problems/longest-duplicate-substring/
	// if a dulicate string 'sd' of lengh K is found, then every
	// smaller substring of 'sd' is also a duplicate. The maximum duplicate 
	//substring is of length n-1: substrings [0, n - 2] && [1, n-1].
**/
public class LDSV2 {
	public static String longestDuplicateSubstring(String s) {
		// Use binary search to find the right window size to search.
		// find the mid window size and see if a duplicate substring of that size is found.
		// If so, smaller sized substrings should be completely disregarded. Otherwise, only
		// search for smaller sized substrings.
		int low = 1;
		int high =s.length() - 1;
		String lds = "";
		while (high > low) {
			int mid = (low + high) / 2;
			String current = duplicateSubstring(s, mid);
			if (current.equals("")) {
				high = mid;
			} else {
				low = mid + 1;
			}

			//Update the substring length if necessary.
			lds = current.length() > lds.length() ?  current : lds;
		}
		return lds;
	}

	/**
	if a duplicate substring exists of length windowLength, return the first occurence of the string.
	otherwise, return ""
	**/
	private static String duplicateSubstring(String s, int windowLength) {
		int start = 0;
		RollingHash windowHash = new RollingHash(256);
		Set<Integer> hashes = new HashSet<>();
		for (int end = 0; end < s.length(); end++) {
			windowHash.append(s.charAt(end));
			int currentWindowLength = end - start + 1;
			if (currentWindowLength >= windowLength) {
				// Adjust window size if it gets too big
				if (currentWindowLength >  windowLength) {
					windowHash.removeHead(s.charAt(start));
					start+= 1;
				} 
				// get hash of substring represented by the rolling hash ADT
				int hash = windowHash.hash();
				// duplicate is found
				if (hashes.contains(hash)) {
					return s.substring(start, end + 1);
				}
				hashes.add(hash);
			}
		}
		return "";
	}

	public static void main(String[] args) {
		Scanner in = new Scanner(new BufferedReader(new InputStreamReader(System.in)));
        int t = in.nextInt(); 
        in.nextLine();
        for (int i = 1; i <= t; ++i) {
          String str = in.nextLine().trim();
          //System.out.println(str);
          String sol = longestDuplicateSubstring(str);
          System.out.println("Case #" + i + ": " + (sol));
      }
	}
}