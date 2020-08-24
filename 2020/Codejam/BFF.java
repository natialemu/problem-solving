import java.util.*;
import java.io.*;

public class BFF {

	class Cycle {
		int x, y, length;

		@Override
		public boolean equals(Object o) {
			Cycle other = (Cycle) o;
			return Objects.equals(x, other.x) && Objects.equals(y, other.y);
		}

		@Override
		public int hashCode() {
			return Objects.hash(x, y);
		}


	}

	public static int maxCircleSize(int[] kidsToBffMap) {
		Map<Integer, Integer> cycleSizeToKidsMap = new HashMap<>();
		Map<Integer, Cycle> vertexToCycleDistance = new HashMap<>();
		//an additional map 


		// stores the length from verex i to cycle of length 2
		// at end of day, solution here will be the sum of all visited nodes
		Map<Integer, Integer> vertexToCyclePathLength = new HashMap<>(); 
		Set<Integer> visited = new HashSet<>();
		List<Integer> sources = findSources(kidsToBffMap);
		for (int kid = 0; kid < sources.size(); kid++) {
			if (!visited.contains(kid)) {
				int cycleLengthInComponent = cycleLength(kidsToBffMap, kid, visited, vertexToCycleDistance);
				if (cycleLengthInComponent > 2) {
					cycleSizeToKidsMap.put(cycleLengthInComponent, cycleLengthInComponent);
				} 
			}
		}

		int maxNumberOfKids = 0;
		for (Integer i : cycleSizeToKidsMap.keySet()) {
			maxNumberOfKids += cycleSizeToKidsMap.get(i);
		}
		Map<Cycle, Integer> cycleToLengthMap = new HashMap<>();

		for (Integer v : vertexToCycleDistance.keySet()) {
			Cycle c = vertexToCycleDistance.get(v);
			cycleToLengthMap.put(c, Math.max(c.length, cycleToLengthMap.getOrDefault(c, -1)));
		}

		for (Cycle c : cycleToLengthMap.keySet()) {
			maxNumberOfKids += cycleToLengthMap.get(c);
		}
		return maxNumberOfKids;

	}

	private static List<Integer> findSources(int[] graph) {
		List<Integer> soures = new ArrayList<>();
		boolean[] mark = new boolean[graph.length];
		for (int i = 0; i < graph.length; i++) {
			if (mark[i]) continue;
			int bff = graph[i];
			while (bff >= 0 && bff < graph && !mark[bff]) {
				mark[bff];
				bff = graph[bff];
			}
		}

		for (int kid = 0; kid < graph.length; kid++) {
			if (!mark[kid]) sources.add(kid);
		}
		return sources;
	}
	private static int cycleLength(int[] graph, int curr, Set<Integer> visited, Map<Integer, Cycle> vertexToCycleDistance) {
		int slowPtr= curr;
		int fastPtr = graph[graph[curr]];
		int lengthToCycle = 0;
		while (!visited(slowPntr) && slowPntr < graph.length && graph[curr] < graph.length) {

			if (vertexToCycleDistance.containsKey(slowPntr)) {
				Cycle cycleInfo = vertexToCycleDistance.get(slowPntr);
				Cycle newInfo = new Cycle(cycleInfo.x, cycleInfo.y, cycleInfo.length + lengthToCycle);
				vertexToCycleDistance.put(curr, newInfo);
				return 2; 
			}
			
			if (!visited.contains(slowPntr)) {
				visited.add(slowPntr);
			}

			slowPntr = graph[slowPntr];
			fastPtr = graph[graph[fastPtr]];

			if (slowPntr == fastPtr) {
				int cycLength =  cycleLength(graph, slowPntr, fastPtr, visited);
				Cycle newInfo = new Cycle(cycleInfo.x, cycleInfo.y, cycleInfo.length + lengthToCycle);
				vertexToCycleDistance.put(curr, newInfo);
				if (cycLength == 2) {
					Cycle newInfo = new Cycle(cycleInfo.x, cycleInfo.y, lengthToCycle);
					vertexToCycleDistance.put(curr, newInfo);
				}
				return cycLength;
			}
			lengthToCycle;
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