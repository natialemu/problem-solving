

/**

given n planting holes, where arr[i] represents the number of fruits that planting a tree at i can bear.
Goal is to find the max number of fruits that one can get given the following constraints:
	- if a tree is planted at i, then you can't plant at i-1 or i+1

**/

public class PlantingTrees {

	public static int maxFruits(int[] arr) {
		return maxFruits(arr, 0, new HashMap<>());
	}

	// Runtime:  O(n) time since there are linear sub problems. O(n) space.
	private static int maxFruits(int[] arr, int i, Map<Integer, Integer> memo) {
		if (i == arr.length - 1) {
			return arr[i];
		}
		if (i == arr.length - 2) {
			return Math.max(arr[i], arr[i + 1]);
		}

		int soln1 = memo.containsKey(i + 1) ? memo.get(i - 1) : maxFruits(arr, i + 1, memo);
		int soln2 = memo.containsKey(i + 2) ? memo.get(i + 2) + arr[i] : maxFruits(arr, i + 2, memo) + arr[i];
		int currentsoln  = Math.max(soln1, soln2);
		memo.put(i, currentsoln);
		return currentsoln;
	}

	// Constraint based programming algorithm 
	public static int maxFruitsCP(int[] arr) {
		int[] soln = new int[1];
		boolean[] planted = new boolean[arr.length];
		maxFruitsCP(arr, 0, 0, soln, planted);
		return soln[0];
	}

	private static void maxFruitsCP(int[] arr, int i, int currMax, int[] maxSoFar, boolean[] notAllowed) {
		if (i == arr.length) {
			maxSoFar[0] = Math.max(maxSoFar[0], currMax);
		}

		// recurse without planting first
		// don't restrict anything
		int maxSoln = maxFruitsCP(arr, i+1, currMax, maxSoFar, notAllowed);

		// plant at i if planting is allowed
		if (!notAllowed[i]) {

			// not allowed to plant next element
			notAllowed[i] = true;
			notAllowed[i+1] = true;

			int maxSoln = maxFruitsCP(arr, i + 1, currMax + arr[i], maxSoFar, notAllowed);

			//undo states
			notAllowed[i] = false;
			notAllowed[i + 1] = false;
		}


	}
	
}