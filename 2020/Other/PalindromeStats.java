
import java.util.*;
import java.io.*;
/**
Given a string(guaranteed to not be empty), create an API that will do the following:
	- retrieve the longest palindromic substring
		if there is more than one, get all of them


	- retrieve all palindromic substrings
**/

class Solution {
	boolean isPalindrome;
 	Set<String> longestPalindromes;
 	public Solution(boolean isPalindrome) {
 		this.isPalindrome = isPalindrome;
 		longestPalindromes = new HashSet<>();
 	}
}
class TrieNode {
	TrieNode leftLink;
	TrieNode rightLink;
	TrieNode forwardLink;
	char character;
	boolean wordEnds;
}

class SuffixTree {
	TrieNode root;

	public SuffixTree(String str) {
		for (int i = str.length() - 1; i >=0; i--) {
			String suffix = str.substring(i);
			root = insert(root, suffix, 0);
		}
	}

	private TrieNode insert(TrieNode current, String str, int currIndex) {
		char currentChar = str.charAt(currIndex);
		if (current == null) {
			current = new TrieNode();
			current.character = currentChar;
		}

		if (currentChar > current.character) {
			current.rightLink = insert(current.rightLink, str, currIndex);
		} else if (currentChar < current.character) {
			current.leftLink = insert(current.leftLink, str, currIndex);
		} else if (currIndex == str.length() - 1) {
			current.wordEnds = true;
		} else {
			current.forwardLink = insert(current.forwardLink, str, currIndex + 1);
		}
		return current;
	}

	public int match(String str, int index) {
		return match(root, str, index);
	}

	private int match(TrieNode current, String str, int currIndex) {
		char currentChar = str.charAt(currIndex);
		if (current == null) {
			return currIndex; // no match found for this word
		}

		if (currentChar > current.character) {
			return match(current.rightLink, str, currIndex);
		} else if (currentChar < current.character) {
			return match(current.leftLink, str, currIndex);
		} else if (currIndex == str.length() - 1) {
			return currIndex + 1; // matched everything
		} else {
			return match(current.forwardLink, str, currIndex + 1);
		}

	}
	public int allMatches(String str, int index, Set<String> everyMatch) {
		return allMatches(root, str, index, new StringBuilder(), everyMatch);
	}
	private int allMatches(TrieNode current, String str, int currIndex, StringBuilder builder, Set<String> everyMatch) {
		char currentChar = str.charAt(currIndex);
		if (current == null) {
			return currIndex; // no match found for this word
		}

		if (currentChar > current.character) {
			return allMatches(current.rightLink, str, currIndex, builder, everyMatch);
		} else if (currentChar < current.character) {
			return allMatches(current.leftLink, str, currIndex, builder, everyMatch);
		} else if (currIndex == str.length() - 1) {
			builder.append(currentChar);
			everyMatch.add(builder.toString());
			return currIndex + 1; // matched everything
		} else {
			builder.append(currentChar);
			everyMatch.add(builder.toString());
			return allMatches(current.forwardLink, str, currIndex + 1, builder, everyMatch);
		}

	}
}
public class PalindromeStats {
	private String input;

	public PalindromeStats(String input) {
		this.input = input;
	}
	
	public Set<String> longestPalindromicSubsrings() {
		return longestPalindromicSubsringV3();
		//return longestPalindromicSubsringV2(0, input.length() - 1, new HashMap<>());

	}

	private Set<String> longestPalindromicSubsringV1() {

		Set<String> longestPalindromes = new HashSet<>();
		int palindromeSize = 0;

		for (int i = 0; i < input.length(); i++) {


			//look for odd sized palindromes with i as center
			int low = i;
			int high = i;
			while (low >= 0 && high < input.length()) {
				if (input.charAt(low) != input.charAt(high)) {
					break;
				}
				if (high - low + 1 > palindromeSize) {
					palindromeSize = high - low + 1;
					longestPalindromes = new HashSet<>();
					longestPalindromes.add(input.substring(low, high+1));
				} else if (high - low + 1 == palindromeSize) {
					longestPalindromes.add(input.substring(low, high + 1));
				}

				low--;
				high++;
			}

			//look for odd sized palindromes with i as center
			if (i + 1 < input.length()) {
				low = i;
				high = i + 1;
				while (low >= 0 && high <= input.length() - 1) {
					if (input.charAt(low) != input.charAt(high)) {
						break;
					}
					if (high - low + 1 > palindromeSize) {
						palindromeSize = high - low + 1;
						longestPalindromes = new HashSet<>();
						longestPalindromes.add(input.substring(low, high+1));
					} else if (high - low + 1 == palindromeSize) {
						longestPalindromes.add(input.substring(low, high + 1));
					}

					low--;
					high++;
				}

			}
			

		}

		return longestPalindromes;
	}

	private Set<String> longestPalindromicSubsringV3() {
		Set<String> allLongestPalindromes = new HashSet<>();
		int maxLength = 0;
		int startIndex = 0;
		String reversedString = new StringBuilder(input).reverse().toString();
		SuffixTree st = new SuffixTree(input);

		while (startIndex < reversedString.length()) {
			int endIndex = st.match(reversedString, startIndex);
			if (startIndex == endIndex) { // nothing was matched
				startIndex +=1;
			} else {
				String palindrome = reversedString.substring(startIndex, endIndex);

				if (palindrome.length() > maxLength) {
					allLongestPalindromes = new HashSet<>();
					allLongestPalindromes.add(palindrome);
					maxLength = palindrome.length();
				} else if(palindrome.length() == maxLength){
					allLongestPalindromes.add(palindrome);
				}
				startIndex = endIndex;

			}
		}
		return allLongestPalindromes;

	}
	private Set<String> longestPalindromicSubsringV2(int low, int high, Map<Integer, Map<Integer, Solution>> palindromes) {

		// if only one element is left, return that element
		if (low == high) {
			//base case
			Set<String> baseCaseSolution = new HashSet<>();
			baseCaseSolution.add(String.valueOf(input.charAt(low)));
			addToMemo(low, high, baseCaseSolution, true, palindromes);
			return baseCaseSolution;
		}
		// return an empty set otherwise
		if (high < low) {
			Set<String> baseCaseSolution = new HashSet<>();
			baseCaseSolution.add("");
			addToMemo(low, high, baseCaseSolution, true, palindromes);

			return baseCaseSolution;
			
		}

		// if the ends are the same
		if (input.charAt(low) == input.charAt(high)) { // if the two ends are the same
			Set<String> longestPalindromes = palindromes.containsKey(low + 1) && palindromes.get(low + 1).containsKey(high - 1) ?
				palindromes.get(low+1).get(high - 1).longestPalindromes : longestPalindromicSubsringV2(low + 1, high - 1, palindromes);

			// if the inner part is a palindrome, then there must have been only one longest palindrome in previous subproblem and the new one
				// will have length of 2 more
				// otherwise, return the solution of the previous subproblem
			if (palindromes.get(low + 1).get(high - 1).isPalindrome) { // constant time check for isPalindrome
				Set<String> currentSolution = new HashSet<>();
				currentSolution.add(input.charAt(low) + firstElement(longestPalindromes) + input.charAt(high));
				addToMemo(low, high, currentSolution, true, palindromes);
				return currentSolution;
			}
			Set<String> longestPalindromes1 = palindromes.containsKey(low + 1) && palindromes.get(low + 1).containsKey(high) ?
				palindromes.get(low+1).get(high).longestPalindromes : longestPalindromicSubsringV2(low + 1, high, palindromes);
			Set<String> longestPalindromes2 = palindromes.containsKey(low) && palindromes.get(low).containsKey(high - 1) ?
				palindromes.get(low).get(high - 1).longestPalindromes : longestPalindromicSubsringV2(low, high - 1, palindromes);

			Set<String> currentSolution = union(longestPalindromes2, longestPalindromes1);
			addToMemo(low, high, currentSolution, false, palindromes);
			return longestPalindromes;
		} else {
			Set<String> longestPalindromes1 = palindromes.containsKey(low + 1) && palindromes.get(low + 1).containsKey(high) ?
				palindromes.get(low+1).get(high).longestPalindromes : longestPalindromicSubsringV2(low + 1, high, palindromes);
			Set<String> longestPalindromes2 = palindromes.containsKey(low) && palindromes.get(low).containsKey(high - 1) ?
				palindromes.get(low).get(high - 1).longestPalindromes : longestPalindromicSubsringV2(low, high - 1, palindromes);

			// check which of the two lists contain palindromes of larger size:
			// find the union if they have palindromes of simillar length
			if (firstElement(longestPalindromes1).length() > firstElement(longestPalindromes2).length()) {
				addToMemo(low, high, longestPalindromes1, false, palindromes);
				return longestPalindromes1;
			} else if (firstElement(longestPalindromes1).length() < firstElement(longestPalindromes2).length()) {
				addToMemo(low, high, longestPalindromes2, false, palindromes);
				return longestPalindromes2;
			} else {
				Set<String> unionSoln = union(longestPalindromes1, longestPalindromes2);
				addToMemo(low, high, unionSoln, false, palindromes);
				return unionSoln;
			}
		}
	}

	private void addToMemo(int low, int high, Set<String> soln, boolean isPalindrome, Map<Integer, Map<Integer, Solution>> memo) {
		Map<Integer, Solution> highSolnMap = new HashMap<>();
		Solution solution = new Solution(isPalindrome);
		solution.longestPalindromes = soln;
		highSolnMap.put(high, solution);
		memo.put(low, highSolnMap);
	}

	private String firstElement(Set<String> elements) {
		Iterator<String> setIterator = elements.iterator();
		return setIterator.next(); // will always have at least one element

	}

	private Set<String> union(Set<String> set1, Set<String> set2) {
		Set<String> unionSet = new HashSet<>();


		unionSet.addAll(set1);
		unionSet.addAll(set2);
		return unionSet;
	}

	public Set<String> palindromicSubsrings() {
		return palindromicSubsringsV3();
		// return palindromicSubsringsV2(0, input.length() - 1, new HashMap<>());
	}

	private Set<String> palindromicSubsringsV1() {
		Set<String> allPalindromes = new HashSet<>();
		for (int i = 0; i < input.length(); i++) {
			//look for odd sized palindromes with i as center
			int low = i;
			int high = i;
			while (low >= 0 && high <= input.length() - 1) {
				if (input.charAt(low) != input.charAt(high)) {
					break;
				}
				allPalindromes.add(input.substring(low, high + 1));
				low--;
				high++;
			}

			if (i + 1 < input.length()) {
				low = i;
				high = i + 1;
				while (low >= 0 && high <= input.length() - 1) {
					if (input.charAt(low) != input.charAt(high)) {
						break;
					}
					allPalindromes.add(input.substring(low, high+1));
					low--;
					high++;
				}

			}
			

		}

		return allPalindromes;
	}

	private Set<String> palindromicSubsringsV2(int low, int high, Map<Integer, Map<Integer, Solution>> palindromes) {


		// if only one element is left, return that element
		if (low == high) {
			//base case
			Set<String> baseCaseSolution = new HashSet<>();
			baseCaseSolution.add(String.valueOf(input.charAt(low)));
			addToMemo(low, high, baseCaseSolution, true, palindromes);
			return baseCaseSolution;
		}
		// return an empty set otherwise
		if (high < low) {
			Set<String> baseCaseSolution = new HashSet<>();
			baseCaseSolution.add("");
			addToMemo(low, high, baseCaseSolution, true, palindromes);
			return baseCaseSolution;
			
		}

		Set<String> longestPalindromes1 = palindromicSubsringsV2(low + 1, high, palindromes);
		Set<String> longestPalindromes2 = palindromicSubsringsV2(low, high - 1, palindromes);
		// if the ends are the same
		if (input.charAt(low) == input.charAt(high)) { // if the two ends are the same
			Set<String> longestPalindromes = palindromes.containsKey(low + 1) && palindromes.get(low + 1).containsKey(high - 1) ?
				palindromes.get(low+1).get(high - 1).longestPalindromes : palindromicSubsringsV2(low + 1, high - 1, palindromes);

			// if the inner part is a palindrome, then there must have been only one longest palindrome in previous subproblem and the new one
			// will have length of 2 more
			// otherwise, return the solution of the previous subproblem
			Set<String> currentSolution = new HashSet<>();
			if (palindromes.get(low + 1).get(high - 1).isPalindrome) { 
				currentSolution.add(input.charAt(low) + firstElement(longestPalindromes) + input.charAt(high));
			} 
			currentSolution.addAll(union(longestPalindromes1, longestPalindromes2));
			addToMemo(low, high, currentSolution, false, palindromes);
			return currentSolution;
		} 
		Set<String> currentSolution = union(longestPalindromes2, longestPalindromes1);
		addToMemo(low, high, currentSolution, false, palindromes);
		return currentSolution;
			
		

	}

	public Set<String> palindromicSubsringsV3() {
		Set<String> allPalindromes = new HashSet<>();
		int startIndex = 0;
		String reversedString = new StringBuilder(input).reverse().toString();
		SuffixTree st = new SuffixTree(input);

		while (startIndex < reversedString.length()) {
			int endIndex = st.allMatches(reversedString, startIndex, allPalindromes);
			if (startIndex == endIndex) { // nothing was matched
				startIndex +=1;
			} else {
				startIndex = endIndex;
			}
		}
		return allPalindromes;
	}


	public static void main(String[] args) {
		Scanner in = new Scanner(new BufferedReader(new InputStreamReader(System.in)));
        int t = in.nextInt(); 
        in.nextLine();
        for (int i = 1; i <= t; ++i) {
          String s = in.nextLine().trim();
          PalindromeStats stats = new PalindromeStats(s);
          Set<String> longestPalindromes = stats.longestPalindromicSubsrings();
          Set<String> allPalindromes = stats.palindromicSubsrings();
          System.out.println("Case #" + i + ": " + (longestPalindromes.size()) + " " + (allPalindromes.size()));
          System.out.print("Longest palindromes: ");
          print(longestPalindromes);
          System.out.print("All palindromes: ");
          print(allPalindromes);
      }

	}
	public static void print(Set<String> elements) {
		Iterator<String> setIterator = elements.iterator();
		while(setIterator.hasNext()) {
			String element = setIterator.next();
			System.out.print(element +" ");
		}
		System.out.println();
	}
}