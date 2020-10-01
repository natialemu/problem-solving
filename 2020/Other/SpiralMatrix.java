 import java.util.*;
 import java.util.function.*;
 public class SpiralMatrix {
 	public static int[] printInSpiralOrder(int[][] arr, int r, int c) {
 		if (arr == null) throw new IllegalArgumentException("Invalid input");

 		Deque<Integer> iq = new LinkedList<>();
 		Deque<Integer> jq = new LinkedList<>();

 		//add all indices
 		for (int i = 0; i < r; i++) iq.addLast(i);
 		for (int j = 0; j < c; j++) jq.addLast(j);

 		int[] soln = new int[r*c];
 		int curr = 0;
 		while (!iq.isEmpty() && !jq.isEmpty()) {
 			int currI = iq.pollFirst(); // 0
 			printInOneDim(jq, currI, curr, soln, (a, b) -> arr[a][b]);
 			curr+=1;
 			int currJ = jq.pollLast();
 			printInOneDim(iq, currJ, curr, soln, (a, b) -> arr[b][a]);
 			curr+=1;
 		}
 		return soln;
 	}

 	private static void printInOneDim(Deque<Integer> dq, int first, int curr, int[] soln, BiFunction<Integer, Integer, Integer> fun) {

 	 	int dqSize = dq.size();
 		for (int i = 0; i < dqSize; i++) {
 			int second = dq.pollFirst();
 			soln[curr] = fun.apply(first, second);
 			dq.addLast(second);
 		}
 	}

 }

 	