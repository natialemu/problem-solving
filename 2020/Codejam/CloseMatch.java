import java.util.*;
import java.io.*;


/**

Problem identified so far:
 - when saving decisions, something goes wrong. This is because the actual score returned is right for each sub problem.
     - to fix this, associate the decisions not only with the indices but also the scores. this way only if the score is smaller would you update
        the decision.
 - tie breaking rules are not right. in case of equal score, prioritize one with smaller first and if that's same, then
   prioritize the one with smaller second.
**/
public class CloseMatch {

	public static String[] findBestScores(String[] incompleteScores) {
		String vagueScore1 = incompleteScores[0];
		char[] firstVagueScore = parse(vagueScore1);
		String vagueScore2 = incompleteScores[1];
		char[] secondVagueScore = parse(vagueScore2);
		int scoreSoFar = 0;

		Map<Integer, List<Integer>> decisions = new TreeMap<>();

		int bestScore = findBestScores(firstVagueScore, secondVagueScore, 0, 0, decisions, "");
		System.out.println(decisions.size());
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
			//System.out.println("Index=" + index + "[" + decisions.get(index).get(0) + ", " + decisions.get(index).get(1) +"]");
			soln1.append(decisions.get(index).get(0));
			soln2.append(decisions.get(index).get(1));
		}
		solns[0] = soln1.toString();
		solns[1] = soln2.toString();
		return solns;
	}

	private static int findBestScores(char[] first, char[] second, int curr, int scoreSoFar, Map<Integer, List<Integer>> decisions, String debug) {
		if (curr == first.length) return Math.abs(scoreDiff(first, second));
		if (scoreSoFar != 0) {
			return greedilyFillScores(first, second, curr, scoreSoFar, decisions);

		}
		char firstChar = first[curr];
		char secondChar = second[curr];
		if (firstChar == '?' && secondChar == '?') {

			//option 1
			first[curr] = '0';
			second[curr] = '0';
			int bestSoln1 = findBestScores(first, second, curr + 1, scoreSoFar, decisions, "	");
			System.out.println(debug + "curr=" + curr + " first[curr]=" + first[curr] + " second[curr]=" + second[curr] + " soln=" + bestSoln1);



			//option 2
			first[curr] = '1';
			int bestSoln2 = findBestScores(first, second, curr + 1, scoreSoFar + 1, decisions, "	");
			System.out.println(debug + "curr=" + curr + " first[curr]=" + first[curr] + " second[curr]=" + second[curr] + " soln=" + bestSoln2);


			//option 3
			first[curr] = '0';
			second[curr] = '1';
			int bestSoln3 = findBestScores(first, second, curr + 1, scoreSoFar - 1, decisions, "	");
			System.out.println(debug + "curr=" + curr + " first[curr]=" + first[curr] + " second[curr]=" + second[curr] + " soln=" + bestSoln3);

			if (bestSoln1 < Math.min(bestSoln2, bestSoln3)) {

				System.out.println(debug + "decisions.put(curr, Arrays.asList(0, 0));");
				decisions.put(curr, Arrays.asList(0, 0));
				return bestSoln1;
			}
			else if (bestSoln2 < bestSoln3) {
				System.out.println(debug + "decisions.put(curr, Arrays.asList(1, 0));");
				decisions.put(curr, Arrays.asList(1, 0));
				return bestSoln2;
			}
			else{
				System.out.println(debug + "decisions.put(curr, Arrays.asList(0, 1));");
			 	decisions.put(curr, Arrays.asList(0, 1));
			 	return bestSoln3;
			}
			

		} else if (firstChar == '?' || secondChar == '?'){

			char originFirst = first[curr];
			char originSecond = second[curr];

			//option 1
			first[curr] = firstChar == '?' ? secondChar : firstChar;
			second[curr] = secondChar == '?' ? firstChar : secondChar;
			int bestSoln1 = findBestScores(first, second, curr + 1, scoreSoFar, decisions, "	");
			System.out.println(debug + "curr=" + curr + " first[curr]=" + first[curr] + " second[curr]=" + second[curr] + " soln=" + bestSoln1);

			//option 2
			first[curr] = originFirst == '?' && Character.getNumericValue(originSecond) < 9 ? (char)(originSecond + 1) : originFirst;
			second[curr] = originSecond == '?' && Character.getNumericValue(originFirst) < 9  ? (char)(originFirst + 1): originSecond;
			int bestSoln2 = findBestScores(first, second, curr + 1, scoreSoFar + 1, decisions, "	");
			System.out.println(debug + "curr=" + curr + " first[curr]=" + first[curr] + " second[curr]=" + second[curr] + " soln=" + bestSoln2);


			//option 3
			first[curr] = originFirst == '?' && Character.getNumericValue(originSecond) > 1 ? (char)(originSecond - 1): originFirst;
			second[curr] = originSecond == '?' && Character.getNumericValue(originFirst) > 1  ? (char)(originFirst - 1): originSecond;
			int bestSoln3 = findBestScores(first, second, curr + 1, scoreSoFar - 1, decisions, "	");
			System.out.println(debug + "curr=" + curr + " first[curr]=" + first[curr] + " second[curr]=" + second[curr] + " soln=" + bestSoln3);

			//what to return


			if (bestSoln1 < Math.min(bestSoln2, bestSoln3)) {
				System.out.println(debug + "bestSoln1 is smaller for curr=" + curr);

				decisions.put(curr, Arrays.asList(originFirst == '?' ? Character.getNumericValue(originSecond) : Character.getNumericValue(originFirst), 
					originSecond == '?' ? Character.getNumericValue(originFirst) : Character.getNumericValue(originSecond)));
				System.out.println("Index=" + curr + "[" + decisions.get(curr).get(0) + ", " + decisions.get(curr).get(1) +"]");
				return bestSoln1;
			}
			else if (bestSoln2 < bestSoln3) {
				System.out.println(debug + "bestSoln2 is smaller for curr=" + curr);
				decisions.put(curr, Arrays.asList(firstChar == '?' && Character.getNumericValue(secondChar) < 9 ? Character.getNumericValue(secondChar) + 1 
					: Character.getNumericValue(firstChar), 
					secondChar == '?' && Character.getNumericValue(firstChar) < 9  ? Character.getNumericValue(firstChar) + 1: Character.getNumericValue(secondChar)));
				return bestSoln2;
			}
			else{
				System.out.println(debug + "bestSoln3 is smaller for curr=" + curr);
			 	decisions.put(curr, Arrays.asList(firstChar == '?' && Character.getNumericValue(secondChar) > 1 ? Character.getNumericValue(secondChar) - 1 : Character.getNumericValue(firstChar), 
			 		secondChar == '?' && Character.getNumericValue(firstChar) > 1  ? Character.getNumericValue(firstChar) - 1: Character.getNumericValue(secondChar)));
			 	return bestSoln3;
			}


		} else {
			int diff = Character.getNumericValue(firstChar) - Character.getNumericValue(secondChar);
			int soln = findBestScores(first, second, curr + 1, scoreSoFar + diff, decisions, "	");
			decisions.put(curr, Arrays.asList(Character.getNumericValue(firstChar), Character.getNumericValue(secondChar)));
			return soln;
		}
	}

	private static int greedilyFillScores(char[] first, char[] second, int curr, int currScore,
		Map<Integer, List<Integer>> decisions) {

		for (int i = curr; i < first.length; i++) {

			if (first[i] == '?' && currScore > 0) first[i] = '0';
			else if (first[i] == '?' && currScore < 0) first[i] = '9';
			if (second[i] == '?' && currScore > 0) second[i] = '9';
			else if (second[i] == '?' && currScore < 0) second[i] = '0';
			
			int diff = Character.getNumericValue(first[i]) - Character.getNumericValue(second[i]);
			decisions.put(i, Arrays.asList(Character.getNumericValue(first[i]), Character.getNumericValue(second[i])));

		}

		return Math.abs(scoreDiff(first, second));
	}

	private static int scoreDiff(char[] first, char[] second) {
		int overallScore = 0;
		for (int i = 1; i <= first.length; i++) {
			int diff = Character.getNumericValue(first[i - 1]) - Character.getNumericValue(second[i - 1]);
			overallScore += (Math.pow(10, first.length - i)*diff);
		}
		return overallScore;
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