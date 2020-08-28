import java.util.*;
import java.io.*;


/**
Pseudocode:
base case:
 if scoreSoFar != 0:
 	greedily populate the remaining strings of both.
 	return netScoreSoFar
 Otherwise:
 	if (ith of vagueScore1 is '?' && ith of valueScore2 == '?')
 		- set both to zero and recurse  with currentPntr + 1 and new score
 		- set the first one to 1 and recurse 
 		- set only the second one to 1 and recurse
 		- retun Math.min(soln1, soln2, soln3);
 	else :
 		- set the one with '?' to known value & recurse
 		- set to one over known if possible and recurse
 		- set to one less if possible and recurse
 		return Math.min(sonl1, soln2, soln3)
**/
public class CloseMatch {

	public static String[] findBestScores(String[] incompleteScores) {
		String vagueScore1 = incompleteScores[0];
		char[] firstVagueScore = parse(vagueScore1);
		String vagueScore2 = incompleteScores[1];
		char[] secondVagueScore = parse(vagueScore2);
		int scoreSoFar = 0;

		Map<Integer, List<Integer>> decisions = new HashMap<>();

		int bestScore = findBestScores(firstVagueScore, secondVagueScore, 0, decisions);
		String[] solutions = parseToStringArray(decisions);
		return solutions;
		
	}

	private static char[] parse(String input) {

	}

	private static String[] parseToStringArray(Map<Integer, List<Integer> decisions) {

	}

	private static int findBestScores(char[] first, char[] second, int curr, int scoreSoFar, Map<Integer, List<Integer>> decisions) {
		if (curr == first.length) return Math.abs(scoreSoFar);
		if (scoreSoFar != 0) {
			return greedilyFillScores(first, second, curr, scoreSoFar);
		}
		char firstChar = first[curr];
		char secondChar = second[curr];
		if (firstChar == '?' && secondChar == '?') {

			//option 1
			first[curr] = '0';
			second[curr] = '0';
			int bestSoln1 = findBestScores(first, second, curr + 1, scoreSoFar);

			//option 2
			first[curr] = '1';
			int bestSoln2 = findBestScores(first, second, curr + 1, scoreSoFar + 1);


			//option 3
			first[curr] = '0';
			second[curr] = '1';
			int bestSoln3 = findBestScores(first, second, curr + 1, scoreSoFar - 1);

			if (bestSoln1 < Math.min(bestSoln2, bestSoln3)) {
				decisions.put(curr, Arrays.asList(0, 0));
				return bestSoln1;
			}
			else if (bestSoln2 < bestSoln3) {
				decisions.put(curr, Arrays.asList(1, 0));
				return bestSoln2;
			}
			else{
			 	decisions.put(curr, Arrays.asList(0, 1));
			 	return bestSoln3;
			}
			

		} else if (firstChar == '?' || secondChar == '?'){

			//option 1
			first[curr] = firstChar == '?' ? secondChar : firstChar;
			second[curr] = secondChar == '?' ? firstChar : secondChar;
			int bestSoln1 = findBestScores(first, second, curr + 1, scoreSoFar);

			//option 2
			first[curr] = firstChar == '?' && Character.intValue(secondChar) < 9 ? secondChar + 1 : firstChar;
			second[curr] = secondChar == '?' && Character.intValue(firstChar) < 9  ? firstChar + 1: secondChar;
			int bestSoln2 = findBestScores(first, second, curr + 1, scoreSoFar + 1);


			//option 3
			first[curr] = firstChar == '?' && Character.intValue(secondChar) > 1 ? secondChar - 1 : firstChar;
			second[curr] = secondChar == '?' && Character.intValue(firstChar) > 1  ? firstChar - 1: secondChar;
			int bestSoln3 = findBestScores(first, second, curr + 1, scoreSoFar - 1);

			//what to return


			if (bestSoln1 < Math.min(bestSoln2, bestSoln3)) {
				decisions.put(curr, Arrays.asList(firstChar == '?' ? secondChar : firstChar, secondChar == '?' ? firstChar : secondChar));
				return bestSoln1;
			}
			else if (bestSoln2 < bestSoln3) {
				decisions.put(curr, Arrays.asList(firstChar == '?' && Character.intValue(secondChar) < 9 ? secondChar + 1 : firstChar, 
					secondChar == '?' && Character.intValue(firstChar) < 9  ? firstChar + 1: secondChar));
				return bestSoln2;
			}
			else{
			 	decisions.put(curr, Arrays.asList(firstChar == '?' && Character.intValue(secondChar) > 1 ? secondChar - 1 : firstChar, 
			 		secondChar == '?' && Character.intValue(firstChar) > 1  ? firstChar - 1: secondChar));
			 	return bestSoln3;
			}


		} else {
			int diff = Character.intValue(firstChar) - Character.intValue(secondChar);
			int soln = findBestScores(first, second, curr + 1, scoreSoFar + diff);
			decisions.put(Character.intValue(firstChar), Character.intValue(secondChar));
			return soln;
		}
	}

	private static int greedilyFillScores(char[] first, char[] second, int currScore) {
		return Math.abs(smt);
	}

	public static void main(String[] args) {
		Scanner in = new Scanner(new BufferedReader(new InputStreamReader(System.in)));
        int t = in.nextInt(); 
        for (int i = 1; i <= t; ++i) {
          String[] scores = in.nextLine().split(" ");
          String[] revealedScores = findBestScores(scores);
          System.out.println("Case #" + i + ": ");
          print(scores);
        }
	}

	private static void print(String[] soln) {
		for (String s : soln) {
			System.out.print(s + " ");
		}
		System.out.println();
	}
	
}