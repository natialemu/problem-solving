
import java.util.*;
import java.io.*;

public class BathroomStalls {

	// 2 -> 3
	// 1 -> 1

	public static int[] simulateStalls(int N, int k) {
		//TODO
		PriorityQueue<Integer> maxHeap = new PriorityQueue<>((a,b) -> (b - a));
		Map<Integer, Integer> lengthToCountMap = new HashMap<>();
		maxHeap.add(N - 2);
		int i = 1;
		while (!maxHeap.isEmpty()) {
			int currLength = maxHeap.poll();
			int x1 =  currLength / 2;
			int x2 = (currLength - 1) / 2;

			// this works bc of the fact that the newly generated lengths are at most half of currLength.
			// since no new length > currLength is being generated, currLength will continue to be the max Length.
			i += lengthToCountMap.get(currLength); 
			lengthToCountMap.remove(currLength);
			if (i >= k) {
				return new int[] {x1, x2};
			} else {
				lengthToCountMap.put(x1, lengthToCountMap.getOrDefault(x1, 0) + 1);
				if (lengthToCountMap.get(x1) == 1) maxHeap.add(x1);

				lengthToCountMap.put(x2, lengthToCountMap.getOrDefault(x2, 0) + 1);
				if (lengthToCountMap.get(x2) == 1) maxHeap.add(x2);
			}
			i++;
		}
	}


	public static void main(String[] args) {
		Scanner in = new Scanner(new BufferedReader(new InputStreamReader(System.in)));
        int t = in.nextInt(); 
        in.nextLine();
        for (int i = 1; i <= t; ++i) {
          String[] line = in.nextLine().split(" ");
          int N = Integer.parseInt(line[0]);
          int k = Integer.parseInt(line[1]);
          int[] sol = simulateStalls(N, k);
          System.out.println("Case #" + i + ": " + (sol));
      }

	}
	
}