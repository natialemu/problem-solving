
import java.util.*;

class PileNode {
	int pile;
	PileNode next;

	public PileNode(int pile) {
		this.pile = pile;
	}

	@Override
	public boolean equals(Object o) {
		PileNode other = (PileNode) o;
		PileNode thisNode = (this);
		while (other != null && thisNode != null)  {
			if (!Integer.equals(other.pile, thisNode.pile)) {
				return false;
			}
			other = other.next;
			thisNode = thisNode.next;
		}

		return other == thisNode; // both should be null to make sure they're of the same size
	}

	@Override
	public int hashCode() {
		PileNode thisNode = (this);
		int hashSum = 0;
		while (thisNode != null)  {
			hashSum += Objects.hash(thisNode.pile);
			thisNode = thisNode.next;
		}

		return Objects.hash(hashSum);
	}
}
public class MergeStones {
	public static void mergeSones(int[] piles, int k) {

		PileNode pileList = new PileNode(piles[0]);
		PileNode current = pileList;
		for (int i = 1; i < piles.length; i++) {
			PileNode newNode = PileNode(piles[i]);
			current.next = newNode;
			current = current.next;
		}

		int minCost = mergeStones(pileList, piles.length, k, new HashMap<>());
	}

	private static int mergeStones(PileNode node, int listLength, int k, Map<Pilenode, Integer> memo) {

		if (listLength == 1) {
			return 0;
		}
		PileNode wholeListStart = node;
		PileNode prevNode = null; // the node right before start node
		PileNode startNode = node; 
		int startIndex = 0;
		int windowSum = 0;

		int minCost = Integer.MAX_VALUE;
		for (int endIndex = 0; endIndex < listLength; endIndex++) {
			PileNode endNode = startNode.next;
			windowSum += endNode.pile;

			if (endIndex - startIndex + 1 == k) {
				PileNode mergedNode = new PileNode(windowSum);
				prevNode.next = mergedNode;
				mergedNode.next = endNode.next;
				if (startIndex == 0) {
					wholeListStart = mergedNode;
				}
				int currMinCost = memo.containsKey(wholeListStart) ? memo.get(wholeListStart) : mergeStones(wholeListStart, listLength - k, k) + windowSum;
				minCost = Math.min(currMinCost, minCost);

			}



			if (endIndex - startIndex + 1 > k) {
				// move startIndex forward
				// shift startNode
				//adjust prevNode
				//adjust sum

				prevNode = startNode;
				startNode = startNode.next;
				startIndex++;
				windowSum -= prevNode.pile;
			}

		}

		memo.put(wholeListStart, minCost);

		return minCost;



	}
}