import java.util.*;
import java.io.*;
/**
Given a list of strings, generate a the smallest possible string 's' such that every input string is a substring of s.

No string is the subtring of another.
The output should be a string.
**/

class ProblemInstance {
	int start;
	List<Integer> remaining = new LinkedList<>();

	public ProblemInstance(int i) {
		this.start = i;
	}

	@Override
	public boolean equals(Object o) {
		ProblemInstance other = (ProblemInstance) o;
		return Objects.equals(start, other.start) &&
		Objects.equals(remaining, other.remaining);

	}

	@Override
	public int hashCode() {
		return Objects.hash(start, remaining);
	}
}
public class ShortestSuperstring {

	public static String shortestSuperString(String[] A) {

		String superString = "";
		int smallestLength = Integer.MAX_VALUE;

		for (int index = 0; index < A.length; index++) {
			ProblemInstance mainProblem = new ProblemInstance(index);
			populate(mainProblem.remaining, A, index); // populate s with all indices of A without element at index
			String potentialSuperString = shortestSuperString(A, mainProblem, new HashMap<>());

			superString = potentialSuperString.length() < smallestLength ? potentialSuperString : superString;
			smallestLength = potentialSuperString.length() < smallestLength ? potentialSuperString.length() : smallestLength;
		}
		return superString;

	}

	private static void populate(List<Integer> s, String[] A, int without) {
		for (int i = 0; i < A.length; i++){
			if (i != without) {
				s.add(i);
			}
		}
	}

	private static String shortestSuperString(String[] A, ProblemInstance currentInstance, Map<ProblemInstance, String> memo) {
		if (currentInstance.remaining.size() == 0) { //base case
			return A[currentInstance.start]; // just return the starting point
		}

		String smallestString = "";
		int smallestLength = Integer.MAX_VALUE;
		String utilizedSubProblem = "";
		int setSize = currentInstance.remaining.size();
		for (int i = 0; i < setSize; i++) {

			// create new subproblem to recurse on
			int oldStart = currentInstance.start;
			currentInstance.start = currentInstance.remaining.get(i); 
			currentInstance.remaining.remove(i);
			String subProblemSolution = memo.containsKey(currentInstance) ? memo.get(currentInstance) : shortestSuperString(A, currentInstance, memo); 

			// undo changes made to create the new subproblem
			currentInstance.remaining.add(i, currentInstance.start);
			currentInstance.start = oldStart;


			String potentialCurrentSolution = merge(A[currentInstance.start], subProblemSolution);
			
			if (potentialCurrentSolution.length() < smallestLength) {
				smallestString = potentialCurrentSolution;
				smallestLength = potentialCurrentSolution.length();
				utilizedSubProblem = subProblemSolution;
			}
		}
		memo.put(currentInstance, smallestString);
		return smallestString;
	}

	private static String merge(String s1, String s2) {
		String commonString1 = prefixIsSuffixOf(s1, s2);
		String commonString2 = prefixIsSuffixOf(s2, s1);

		
		if (commonString2.equals("") && commonString1.equals("")) {
			return s1 + s2;
		}

		if (commonString2.length() < commonString1.length()) {
			


			String mergedResult = s2 + s1.substring(s1.indexOf(commonString1)+ commonString1.length());

			return mergedResult;

		}
		String mergedResult = s1 + s2.substring(s2.indexOf(commonString2) + commonString2.length());
		return mergedResult;

	}

	private static String prefixIsSuffixOf(String s1, String s2) {

		int end = s2.length() - 1;
		for (int i = s1.length() - 1; i >= 0 ; i--) {
			if (s1.charAt(i) == s2.charAt(end)) {
				int index = i;
				while (end >= 0 && s1.charAt(index) == s2.charAt(end) ) {
					if (index == 0) {
						return s1.substring(0, i+1);
					}
					index--;
					end--;

				}
			}
			end = s2.length() - 1;
		} 
		return "";
	}


	public static void main(String[] args) {

		Scanner in = new Scanner(new BufferedReader(new InputStreamReader(System.in)));
        int t = in.nextInt(); // test cases
        in.nextLine();
        for (int i = 1; i <= t; ++i) {
          String[] A = in.nextLine().trim().split(" ");
          String superString = shortestSuperString(A);
          System.out.println("Case #" + i + ": " + (superString));
        }

	}
	
}