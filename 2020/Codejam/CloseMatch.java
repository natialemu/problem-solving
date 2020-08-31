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

		Map<Integer, List<Integer>> decisions = new TreeMap<>();

		int bestScore = findBestScores(firstVagueScore, secondVagueScore, 0, 0, decisions);
		String[] solutions = parseToStringArray(decisions);
		return solutions;
		
	}

	private static char[] parse(String input) {
		char[] parsedContent = new char[input.length()];
		for (int i = 0; i < input.length(); i++) {
			parsedContent[i] = input.charAt(i);
		}
		return parsedContent;

	}

	private static String[] parseToStringArray(Map<Integer, List<Integer>> decisions) {
		String[] solns = new String[2];
		StringBuilder soln1 = new StringBuilder();
		StringBuilder soln2 = new StringBuilder();

		for (Integer index : decisions.keySet()) {
			soln1.append(decisions.get(index).get(0));
			soln2.append(decisions.get(index).get(1));
		}
		solns[0] = soln1.toString();
		solns[1] = soln2.toString();
		return solns;
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
			int bestSoln1 = findBestScores(first, second, curr + 1, scoreSoFar, decisions);

			//option 2
			first[curr] = '1';
			int bestSoln2 = findBestScores(first, second, curr + 1, scoreSoFar + 1, decisions);


			//option 3
			first[curr] = '0';
			second[curr] = '1';
			int bestSoln3 = findBestScores(first, second, curr + 1, scoreSoFar - 1, decisions);

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
			int bestSoln1 = findBestScores(first, second, curr + 1, scoreSoFar, decisions);

			//option 2
			first[curr] = firstChar == '?' && Character.getNumericValue(secondChar) < 9 ? (char)(secondChar + 1) : firstChar;
			second[curr] = secondChar == '?' && Character.getNumericValue(firstChar) < 9  ? (char)(firstChar + 1): secondChar;
			int bestSoln2 = findBestScores(first, second, curr + 1, scoreSoFar + 1, decisions);


			//option 3
			first[curr] = firstChar == '?' && Character.getNumericValue(secondChar) > 1 ? (char)(secondChar - 1): firstChar;
			second[curr] = secondChar == '?' && Character.getNumericValue(firstChar) > 1  ? (char)(firstChar - 1): secondChar;
			int bestSoln3 = findBestScores(first, second, curr + 1, scoreSoFar - 1, decisions);

			//what to return


			if (bestSoln1 < Math.min(bestSoln2, bestSoln3)) {
				decisions.put(curr, Arrays.asList(firstChar == '?' ? Character.getNumericValue(secondChar) 
					: Character.getNumericValue(firstChar), secondChar == '?' ? Character.getNumericValue(firstChar)
					 : Character.getNumericValue(secondChar)));
				return bestSoln1;
			}
			else if (bestSoln2 < bestSoln3) {
				decisions.put(curr, Arrays.asList(firstChar == '?' && Character.getNumericValue(secondChar) < 9 ? Character.getNumericValue(secondChar) + 1 
					: Character.getNumericValue(firstChar), 
					secondChar == '?' && Character.getNumericValue(firstChar) < 9  ? Character.getNumericValue(firstChar) + 1: Character.getNumericValue(secondChar)));
				return bestSoln2;
			}
			else{
			 	decisions.put(curr, Arrays.asList(firstChar == '?' && Character.getNumericValue(secondChar) > 1 ? Character.getNumericValue(secondChar) - 1 : Character.getNumericValue(firstChar), 
			 		secondChar == '?' && Character.getNumericValue(firstChar) > 1  ? Character.getNumericValue(firstChar) - 1: Character.getNumericValue(secondChar)));
			 	return bestSoln3;
			}


		} else {
			int diff = Character.getNumericValue(firstChar) - Character.getNumericValue(secondChar);
			int soln = findBestScores(first, second, curr + 1, scoreSoFar + diff, decisions);
			decisions.put(curr, Arrays.asList(Character.getNumericValue(firstChar), Character.getNumericValue(secondChar)));
			return soln;
		}
	}

	private static int greedilyFillScores(char[] first, char[] second, int curr, int currScore) {
		int overallScore = currScore;
		for (int i = curr; i < first.length; i++) {

			if (first[i] == '?' && currScore > 0) first[i] = 0;
			else if (first[i] == '?' && currScore < 0) first[i] = 9;
			if (second[i] == '?' && currScore > 0) second[i] = 9;
			else if (second[i] == '?' && currScore < 0) second[i] = 0;
			
			int diff = Character.getNumericValue(first[i]) - Character.getNumericValue(second[i]);
			overallScore += diff; 
		}
		return Math.abs(overallScore);
	}

	public static void main(String[] args) {
		Scanner in = new Scanner(new BufferedReader(new InputStreamReader(System.in)));
        int t = in.nextInt(); 
        in.nextLine();
        for (int i = 1; i <= t; ++i) {
          String[] scores = in.nextLine().split(" ");
          if (scores.length != 2) throw new IllegalArgumentException("Invalid number of scores");
          String[] revealedScores = findBestScores(scores);
          System.out.println("Case #" + i + ": ");
          print(revealedScores);
        }
	}

	private static void print(String[] soln) {
		for (String s : soln) {
			System.out.print(s + " ");
		}
		System.out.println();
	}
	
}