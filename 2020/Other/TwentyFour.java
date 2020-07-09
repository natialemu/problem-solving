
import java.util.*;
import java.io.*;


/**
Given an array of 4 numbers, is it possible to combine them using the following four operators:
	{+, *, -, /}
	in a such a way that they add up to 24.


Input constaints:
- an integer array of size 4

output:
- print either true or false
- if true, print the expression that forms 24

**/
public class TwentyFour {

	public static Map<Integer, String> combineTo24(Integer[] arr) {

		//generate all permutations of the array: 24 of them for an array of size 4
		List<Integer[]> permutations = new ArrayList<>();
		generatePermutations(arr, new Integer[arr.length], 0, permutations, new HashMap<>());

		//for each permuatation
		for (Integer[] perm : permutations) {

			// see if it's possible to parenthesize the array to get it to combine to 24
			Map<Integer, String> allCombins = combineTo24(perm, 0, perm.length - 1, "");
			if (allCombins.containsKey(24)) {
				return allCombins;
			}

		}

		
		return new HashMap<>();
	}

	private static void generatePermutations(Integer[] arr, Integer[] permSoFar, int index, List<Integer[]> perms, Map<Integer, Boolean> usedIndices) {
		if (index == permSoFar.length) {
			perms.add(clone(permSoFar));
		} else {
			for (int i = 0; i < arr.length; i++) {
				if (!usedIndices.containsKey(i)) {
					//place element at index
					permSoFar[index] = arr[i];
					usedIndices.put(i, true);
					//recurse
					generatePermutations(arr, permSoFar, index + 1, perms, usedIndices);
					//undo
					permSoFar[index] = 0;
					usedIndices.remove(i);
				}
			}
		}

	}

	private static Integer[] clone(Integer[] arr) {
		Integer[] cloneArr = new Integer[arr.length];
		for (int i = 0; i < arr.length; i++) {
			cloneArr[i] = arr[i];
		}
		return cloneArr;
	}

	//f(low, end) -> a combination of all the results formed when combining all the elements of arr from [low, end] using
	// the operators {+,-,*, /}
	//f(low, end) = [ for each i in [low, end], f(low, i) U f(i+1, end) ] || [ arr[low] if low == end ]
	private static Map<Integer, String> combineTo24(Integer[] arr, int low, int end, String debug) {

		if (low == end) {
			Map<Integer, String> combinations = new HashMap<>();
			combinations.put(arr[low], Integer.toString(arr[low]));
			return combinations;
		}
		Map<Integer, String> allCombinations = new HashMap<>();
		for (int i = low; i < end; i++ ) {
			Map<Integer, String> firstsolution = combineTo24(arr, low, i, "	");
			Map<Integer, String> secondSolution = combineTo24(arr, i + 1, end, "   ");
			
			addCombinations(firstsolution, secondSolution, allCombinations);
		}
		return allCombinations; 
	}

	private static void addCombinations(Map<Integer, String> firstsolution, Map<Integer, String> secondSolution, Map<Integer, String> combinedSolution) {



		for (Integer a1 : firstsolution.keySet()) {
			for (Integer a2 : secondSolution.keySet()) {

				Integer product = a1*a2;
				if (product <= 24) {
					combinedSolution.put(product, getExpression(firstsolution.get(a1), secondSolution.get(a2), "*"));
				}
				Integer difference = a1 - a2;
				if (difference <= 24) {
					combinedSolution.put(difference, getExpression(firstsolution.get(a1), secondSolution.get(a2), "-"));
				}
				Integer sum = a1 + a2;
				if (sum <= 24) {
					combinedSolution.put(sum, getExpression(firstsolution.get(a1), secondSolution.get(a2), "+"));
				}
				Integer quotient = a2 != 0 && a1 % a2 == 0 ? a1 / a2 : null;
				if (quotient != null) {
					combinedSolution.put(sum, getExpression(firstsolution.get(a1), secondSolution.get(a2), "/"));
				}

			}
		}

	}

	private static String getExpression(String exp1, String exp2, String operator) {
		StringBuilder builder = new StringBuilder();
		if (exp1.length() == 1) {
			builder.append(exp1);
		} else {
			builder.append("(");
			builder.append(exp1);
			builder.append(")");
		}

		builder.append(operator);
		if (exp2.length() == 1) {
			builder.append(exp2);
		} else {
			builder.append("(");
			builder.append(exp2);
			builder.append(")");
		}
		return builder.toString();
	}


	public static void main(String[] args) {
		Scanner in = new Scanner(new BufferedReader(new InputStreamReader(System.in)));
        int t = in.nextInt(); 
        in.nextLine();
        for (int i = 1; i <= t; ++i) {
          String[] s = in.nextLine().split(" ");
          Integer[] arr = new Integer[s.length];
          for (int j = 0; j < s.length; j++) {
          	arr[j] = Integer.parseInt(s[j]);
          }
          Map<Integer, String> allCombinations = combineTo24(arr);
          System.out.println("Case #" + i + ": " + (allCombinations.containsKey(24)));
          if (allCombinations.containsKey(24)) 
          	System.out.println(allCombinations.get(24));
      }
	}


	
}