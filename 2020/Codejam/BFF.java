import java.util.*;
import java.io.*;

public class BFF {

	public static int maxCircleSize(int[] kidsToBffMap) {
		/**
		Possible cases while traversing hte graph in dfs:
		 1. traverse from one source and leads to a cycle of length  > 2
		 			then break and stop
		 2. traverse from one source and it leads to a cyle of length 2
		 		return the length of the path to the one of the two cycles


		**/

		Map<Integer, Integer> cycleSizeToKidsMap = new HashMap<>();

		// stores the length from verex i to cycle of length 2
		// at end of day, solution here will be the sum of all visited nodes
		Map<Integer, Integer> vertexToCyclePathLength = new HashMap<>(); 
		Set<Integer> visited = new HashSet<>();
		List<Integer> sources = findSources(kidsToBffMap);
		for (int kid = 0; kid < sources.size(); kid++) {
			if (!visited.contains(kid)) {
				int cycleLengthInComponent = cycleLength(kidsToBffMap, kid, visited);
				if (cycleLengthInComponent > 2) {
					cycleSizeToKidsMap.put(cycleLengthInComponent, cycleLengthInComponent);
				} else {
					//		 3. traverse from one source and it leads to a visisted node, however that node is in a component with cycle length of 2:
		 		    //.         continue to traverse

					// update map or add new entry to the map
		 		    findPathToCycle(kidsToBffMap, kid, vertexToCyclePathLength);



				}
			}
		}

	}

	private static List<Integer> findSources(int[] graph) {
		//TODO
	}
	private static int cycleLength(int[] graph, int curr, Set<Integer> visited) {
		int slowPtr= curr;
		int fastPtr = graph[graph[curr]];
		while (!visited(slowPntr) && slowPntr < graph.length && graph[curr] < graph.length) {
			
			if (!visited.contains(slowPntr)) {
				visited.add(slowPntr);
			}
			slowPntr = graph[slowPntr];
			fastPtr = graph[graph[fastPtr]];

			if (slowPntr == fastPtr) {
				return cycleLength(graph, slowPntr, fastPtr, visited);
			}
		}

	}

	private static int cycleLength(int[] graph, int slow, int fast, Set<Integer> visited) {

		int length = 0;
		while (!visited(slowPntr) && slowPntr < graph.length && graph[curr] < graph.length) {
			fast = graph[fast];
			length += 1;
			if (fast == slow) { 
				break;
			}
		}
		return length;

	}
 	public static void main(String[] args) {
		Scanner in = new Scanner(new BufferedReader(new InputStreamReader(System.in)));
        int t = in.nextInt(); 
        in.nextLine();
        for (int i = 1; i <= t; ++i) {
        	int N = in.nextInt(); 
        	in.nextLine();

        	String[] bffsString= in.nextLine().split(" ");
        	int[] bffs = new int[bffsString.length];
        	for (int j = 0; j < bffsString.length; j++) {
        		bffs[j] = Integer.parseInt(bffsString[j]);
        	}
            int maxKidsInCircle = maxCircleSize(bffs);
            System.out.println("Case #" + i + ": " + (numThrows));
        }

	}
	
}