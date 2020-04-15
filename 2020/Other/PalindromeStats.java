
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
public class PalindromeStats {
	private String input;

	public PalindromeStats(String input) {
		this.input = input;
	}
	
	public Set<String> longestPalindromicSubsrings() {
		return longestPalindromicSubsringV1();
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
		return null;

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
		return palindromicSubsringsV1();
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