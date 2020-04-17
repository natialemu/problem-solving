import java.util.*;
import java.io.*;


/**
Ning and Evan are playing a game, where there are n cards in a line. The cards are all face-up (so
they can both see all cards in the line) and numbered 2–9. Ning and Evan take turns. Whoever’s
turn it is can take one card from either the right end or the left end of the line. The goal for each
player is to maximize the sum of the cards they’ve collected
**/

enum Player {
	EVAN(1),
	NING(2);

	private int playerId;

	Player(int playerId) {
		this.playerId = playerId;
	}
}

public class GreedyCards {

	
	public static int maxEvanScore(int[] cards) {

		Player[] players = new Player[2];
		players[0] = Player.NING;
		players[1] = Player.EVAN;

		return maxEvanScore(cards, 0, cards.length - 1, players, 0, new HashMap<>());
	}


	/**
	Calculate Evan's max possible score assuming Ning follows a Greedy approach of just selecting max of the two options
	Additional constraint: there are no duplicates in the card
	**/
	public static int maxEvanScore(int[] cards, int low, int high, Map<Integer, Map<Integer, Integer>> memo) {
		if (high - low == 1) {
			return Math.max(cards[high], cards[low]);
		}
		if (low == high) {
			return cards[low];
		}

		if (low > high) {
			return 0;
		}


		//if low card is chosen by evan
		int firstSubProblemSolution = -1;
		if (card[low + 1] < cards[high]) {
			firstSubProblemSolution = memo.containsKey(low + 1) && memo.get(low + 1).containsKey(high - 1) ? 
					memo.get(low+1).get(high-1) + cards[low] : cards[low] + maxEvanScore(cards, low + 1, high - 1, memo);
		} else {
			firstSubProblemSolution = memo.containsKey(low + 2) && memo.get(low + 2).containsKey(high) ? 
					memo.get(low+2).get(high) + cards[low] : cards[low] + maxEvanScore(cards, low + 2, high, memo);
		}

		// if high card is chosen by evan
		int secondSubProblemSolution = -1;
		if (card[low] < cards[high - 1]) {
			secondSubProblemSolution = memo.containsKey(low) && memo.get(low).containsKey(high - 2) ? 
					memo.get(low).get(high-2) + cards[high] : cards[high] + maxEvanScore(cards, low, high - 2, memo);
		} else {
			secondSubProblemSolution = memo.containsKey(low + 1) && memo.get(low + 1).containsKey(high - 1) ? 
					memo.get(low + 1).get(high-1) + cards[high] : cards[high] + maxEvanScore(cards, low + 1, high - 1, memo);
		}

		int currentSolution = Math.max(firstSubProblemSolution, secondSubProblemSolution);
		Map<Integer, Integer> highSolutionMap = new HashMap<>();
		highSolutionMap.put(high, currentSolution);
		memo.put(low, highSolutionMap);
		return currentSolution;

	}
}