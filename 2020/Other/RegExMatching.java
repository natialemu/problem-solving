import java.util.*;

/**
Input:
s = "aa"
p = "a"
Output: false
Explanation: "a" does not match the entire string "aa".


Example 2:
Input:
s = "aa"
p = "a*"
Output: true
Explanation: '*' means zero or more of the preceding element, 'a'. Therefore, by repeating 'a' once, it becomes "aa".
 
Input:
s = "ab"
p = ".*"
Output: true
Explanation: ".*" means "zero or more (*) of any character (.)".


Input:
s = "aab"
p = "c*a*b"
Output: true
Explanation: c can be repeated 0 times, a can be repeated 1 time. Therefore, it matches "aab".


Input:
s = "mississippi"
p = "mis*is*p*."
Output: false
 

 .   is a wildard for a single character
 * matches 0 or more of the preceding character


 aaabc
 a*abc

 we can reduce the pattern

 if * is in between identical characters, remove any succedding characters

 true

**/



public class RegExMatching {


	public static void main(String[] args) {
		String s = "aa";
		String p = "a";
		System.out.println(s + " matches " + p + " : " + matches(s,p));


		String s = "aa";
		String p = "a*";
		System.out.println(s + " matches " + p + " : " + matches(s,p));

		String s = "aa";
		String p = "a";
		System.out.println(s + " matches " + p + " : " + matches(s,p));

		String s = "aa";
		String p = "a";
		System.out.println(s + " matches " + p + " : " + matches(s,p));

		String s = "aa";
		String p = "a";
		System.out.println(s + " matches " + p + " : " + matches(s,p));
	}

	public static boolean matches(String s, String p) {


		int originalStringPointer = 0;
		int patternPointer = 0;

		index prevChar  = -1;
		while (originalStringPointer < s.length() && patternPointer < p.length()) {

			boolean wildCard = p.charAt(patternPointer) == '*';

			if (wildard && p.charAt(prevChar) == '.') {
				return true;
			}
			while (p.charAt(patternPointer) == '*' && prevChar >= 0 &&
					compare(s.charAt(originalStringPointer), p.charAt(prevChar))) {

				originalStringPointer++;
			}
			if (wildard) {
				patternPointer++;
			}

			if (originalStringPointer >= s.length() || patternPointer >= p.length()) {
				break;
			}

			if (compare(s.charAt(originalStringPointer), p.charAt(patternPointer))) {
				originalStringPointer++;
				patternPointer++;
			} else if (! wildard && compare(s.charAt(originalStringPointer), p.charAt(patternPointer))) {
				return false
			}

	
			prevChar++;
		}

		return originalStringPointer == s.length() && patternPointer == p.length();
	}

	private static boolean compare(char a, char b) {
		if (a == '.' || b == '.') {
			return true;
		}

		return a == b;
	}

}