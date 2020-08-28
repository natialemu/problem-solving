import java.util.*;
import java.io.*;


/**
Pseudocode:
input:
	- net score so far
	- int currentPntr
	- vagueScore1 and vagueScore2


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

	public static int[] findBestScores(String[] incompleteScores) {
		String vagueScore1 = incompleteScores[0];
		String vagueScore2 = incompleteScores[1];
		int scoreSoFar = 0;
		
		//TODO

	}

	public static void main(String[] args) {
		Scanner in = new Scanner(new BufferedReader(new InputStreamReader(System.in)));
        int t = in.nextInt(); 
        for (int i = 1; i <= t; ++i) {
          String[] scores = in.nextLine().split(" ");
          int[] revealedScores = findBestScores(scores);
          System.out.println("Case #" + i + ": " + (r));
        }

	}
	
}