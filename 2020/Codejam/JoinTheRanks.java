
import java.util.*;
import java.io.*;

/**
Goal of this algorithm is as follows:
	-> Highest priority given to code quality & aesthetics(readibility, code abstraction and so forth).
	-> Actual algorithm should be straight forward.
**/

class Deck {
	List<Integer> cards;
	Pairs pairs;
	public Deck() {
		cards = new LinkedList<>();

	}

	public Dec

	public void add(Integer card ) {
		cards.add(card);
	}

	public Deck clone(int i) {
		Deck newDeck = new Deck();
		List<Integer> lastSubDeck = subDeck(i, cards.size() - 1);
		newDeck.append(lastSubDeck);
		return newDeck;
	}

	public void append(List<Integer> ranks) {
		for (Integer card : ranks ) {
			cards.add(0, card);
		}
	} 
	public boolean isSorted() {
		//TODO
		if (cards.size() == 0) return true;
		Integer prevCard = cards.get(0);
		for (int i = 1; i < cards.size(); i++) {
			Integer currCard = cards.get(i);
			if (currCard < prevCard) return false;
		}
		return true;
	}

	@Override
	public boolean equals(Object o) {
		Deck other = (Deck) o;
		return Objects.equals(other.cards, cards);
	}

	public List<Integer> subDeck(int i, int j) {
		return cards.subList(i, j + 1);
	}

	Override
	public int hashCode() {
		return Objects.hash(cards);

	}
	public int size() {
		return cards.size();

	}
}

public class JoinTheRanks {

	public static int sortByRank(int[][] cards) {
		Deck deck = parse(cards); //pards cards into a deck of only ranks
		Queue<Deck> bfs = new LinkedList<>();
		Set<Deck> visited = new HashSet<>();
		bfs.add(deck);
		int numRounds = 1;
		while (!bfs.isEmpty()) {
			int queueSize = bfs.size();
			for (int i = 0; i < queueSize; i++) {
				Deck currState = bfs.poll();
				visited.add(currState);
				for (Deck nextState : nextStates(currState)) {
					if (nextState.isSorted()) return numRounds;
					if (!visited.contains(deck)) bfs.add(nextState);
				}
			}
		}
		return -1;
	}

	private static List<Deck> nextSates(Deck currState) {
		List<Deck> nextStates = new ArrayList<>();
		for (int i = 0; i < currState.size() - 1; i++) {
			for (int j = i + 1; j  < currState.size(); j++) {
				List<Integer> subDeckA = currState.subDeck(0, i);
				List<Integer> subDeckB = currState.subDeck(i + 1, j);
				Deck newDeck = currState.clone(j + 1);
				newDeck.append(subDeckA);
				newDeck.append(subDeckB);
				nextStates.add(newDeck);
			}
		}
		return nextStates;
	}

	public static void main(String[] args) {
		//TODO

	}
}