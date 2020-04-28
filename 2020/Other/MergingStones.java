
import java.util.*;
import java.io.*;
/**
https://leetcode.com/problems/minimum-cost-to-merge-stones/


given n stone piles,
- you are allowed to make the following move:
	- merge k consecutive piles into 1 with a cost of the overall number of stones in the k piles.

what is the minimum cost for merging all the stones into 1 pile.

if size of the piles is not divisible by K, no solution.

Approach 1:
Single source shortest path from stones of size k to stones of size 1.
given stone a --(merge k starting from i)--> outputs b. the edge has weight equal to the cost.


Run time:
 - linear. but getting adjacent states costs k so overall it's k*m where m is the number of possible states the stone piles can be in.


Approach 2:
would greedy work? pick the min cost state all the time. if multiple states have the same cost, pick first one.

why this could work: the current cost directly affects the future costs proportionally. so picking the minimum cost greedily should work.

**/

class Node {
	int[] stones;
	int cost;
}

/**
Class that represents the stone piles as a linked list.
Eg:

3 -> 2 -> 1 -> 4
**/
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
			if (!Objects.equals(other.pile, thisNode.pile)) {
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
public class MergingStones {
	/**
	Greedy algorithm attempt.
	Runtime:
	**/
	public static int mergeStones(int[] stones, int K) {
		if (stones.length == 1) {
			return 0;
		}
		if (K > stones.length) {
			return -1;
		}


		// represents the set of stone piles with minimum cost with source stones.
		Node optimalPiles = nextOptimalPile(stones, K);
		System.out.print("Optimal Cost of " + optimalPiles.cost + " for ");
		printArray(optimalPiles.stones);
		int prevCost = mergeStones(optimalPiles.stones, K);
		if (prevCost == -1) {
			return prevCost;
		}

		return optimalPiles.cost + prevCost;
	}

	/**
	Given a collection of stone piles represented as arrays, find return a new pile
	with min cost after merge some k consecutive piles
	**/
	private static Node nextOptimalPile(int[] stones, int K) {

		int start = 0; 
		int sum = 0;// keeps track of the sum of every window of size k

		Node optimalNode = new Node(); // keeps track of the optimal state formed from stones
		optimalNode.cost = Integer.MAX_VALUE;
		for (int end = 0; end < stones.length; end++) {


			// an array to hold a new state formed
			// the size of the new state is always going to be old state length - window size  + 1
			int[] newStones = new int[stones.length - K + 1]; 
			sum += stones[end]; // add to the sum of the window

			while (end - start + 1 > K) {
				sum -= stones[start];
				start += 1;
			}

			if (end - start + 1 == K) {
				// adjust window size if it gets bigger than K

				
				//populate everything before the beginning of the window
				for (int i = 0; i < start; i++) {
					newStones[i] = stones[i];
				}

				// set value at index 'start' to be the sum of the window of size k starting at 'start'
				newStones[start] = sum;

				//fill remaining elements after the window
				for (int j = end + 1; j < stones.length; j++) {
					newStones[j - K + 1] = stones[j];
				}

				// if the cost of forming the new stone piles is smaller what's been seen so far
				// update optimal node
				if (sum < optimalNode.cost) {
					optimalNode.stones = newStones;
					optimalNode.cost = sum;
				}
			}
			
		}
		return optimalNode;
	}

	/**
	Shortest path algorithm
	**/
	public static int mergeStonesV2(int[] piles, int k) {

		// convert the array of piles into a linked list of piles
		PileNode pileList = new PileNode(piles[0]);
		PileNode current = pileList;
		for (int i = 1; i < piles.length; i++) {
			PileNode newNode = new PileNode(piles[i]);
			current.next = newNode;
			current = current.next;
		}
		// what's the shortest path starting from pileList to reach to any node of length 1
		int minCost = mergeStonesV2(pileList, piles.length, k, new HashMap<>(), "");
		return minCost;
	}
	/**
	Given a list of stone piles represented as a linked list find the min cost to merge into one
	assuming it's possible. shortest path alg.
	**/
	private static int mergeStonesV2(PileNode node, int listLength, int k, Map<PileNode, Integer> memo, String debug) {
		

		// if the length of the list starting from 'node' is 1
		if (listLength == 1) {
			return 0;
		}

		if (k > listLength) {
			return -1;
		}

		// this will represent the beginning of the new list after merging 'k' lists
		PileNode wholeListStart = node;

		//represents the node that points to the start of the window
		// should be null at the beginning since there's nothing pointing to the start of the node at first
		PileNode prevNode = null; 

		// this node represents the beginning of the window
		PileNode startNode = node; 
		int startIndex = 0;

		// keeps track of the sum of numbers in the window
		int windowSum = 0;

		int minCost = Integer.MAX_VALUE;
		PileNode endNode = startNode;
		for (int endIndex = 0; endIndex < listLength; endIndex++) {

			// this points to the node at the end of the window
			windowSum += endNode.pile;
			// if window gets too big, readjust the size
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

			// if the window size is equal to k
			if (endIndex - startIndex + 1 == k) {

				// create a new node to hold the sum of the window
				PileNode mergedNode = new PileNode(windowSum);

				// replace all the nodes that are within the window with 'merged node'
				if (prevNode != null)
					prevNode.next = mergedNode;
				mergedNode.next = endNode.next;

				// if the start of the list was in the window, then the new node should be the start of the new list
				// since it's also replaced
				PileNode listStartsAt = wholeListStart;// keeping track of it so that it's easy to rollback while backtracking
				if (startIndex == 0) {
					wholeListStart = mergedNode;
				}
				printList(wholeListStart, debug);
				int prevSolution =  mergeStonesV2(wholeListStart, listLength - k + 1, k, memo, debug + "	"); //memo.containsKey(wholeListStart) ? memo.get(wholeListStart) :
				if (prevSolution == -1) {
					return prevSolution; // is there a different way of knowing if there is no solution?
				}
				//recurse with the new list
				int currMinCost = prevSolution + windowSum;
				minCost = Math.min(currMinCost, minCost);

				// undo node re-wiring to explore other states a.k.a replace the newly created node with the nodes in the current window
				if (prevNode != null)
					prevNode.next = startNode;
				endNode.next = mergedNode.next;
				mergedNode = null;
				wholeListStart = listStartsAt;

			}
			endNode = endNode.next;
		}

		memo.put(wholeListStart, minCost);

		return minCost;
	}

	private static void printList(PileNode head, String debug) {
		System.out.print(debug);
		while (head.next != null) {
			System.out.print(head.pile + "->");
			head = head.next;
		}

		System.out.println(head.pile);
	}

	private static void printArray(int[] arr) {
		for (int a : arr) {
			System.out.print(a + " ");
		}
		System.out.println();

	}

	public static void main(String[] args) {
		Scanner in = new Scanner(new BufferedReader(new InputStreamReader(System.in)));
        int t = in.nextInt(); 
        in.nextLine();
        for (int i = 1; i <= t; ++i) {
          String windowLength = in.nextLine().trim();
          int k = Integer.parseInt(windowLength);
          String[] stonePiles = in.nextLine().split(" ");
          int[] piles = new int[stonePiles.length];
          for(int j = 0; j < stonePiles.length; j++) {
          	piles[j] = Integer.parseInt(stonePiles[j]);

          }
          Integer sol = mergeStones(piles, k);
          System.out.println("Case #" + i + ": " + (sol));
     	}

	}
}