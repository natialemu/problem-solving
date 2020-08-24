import java.util.*;
import java.io.*;

public class BFF {

	/***
	Algoritmm 2: The reversed graph is much simpler, preserves the cycle. can just traverse from 
	the cycles
	
	**/


	public static int maxCircleSize(int[] kidsToBffMap) {

		//PROBLEM:
		//Goal is to find the sum of the longest path to EACH vertex of a cycle

		//this is to map kids to cycles of length of > 2
		Map<Integer, Integer> cycleSizeToKidsMap = new HashMap<>();

		// this maps each vertex to the cycle info in its component such as the vertices in the cycle and length to from the
		//vertex to its cycle
		Map<Integer, Map<Integer, Integer>> vertexToCycleDistance = new HashMap<>();
		//an additional map 


	
		Set<Integer> visited = new HashSet<>();

		//get all kids who aren't anyone's best friend
		List<Integer> sources = findSources(kidsToBffMap);

		for (int kid = 0; kid < sources.size(); kid++) {
			if (!visited.contains(kid)) {

				//get the cycle length
				// if cycle length is 2:
				//	- vertexToCycleDistance has been updated appropriately
				int cycleLengthInComponent = cycleLength(kidsToBffMap, kid, visited, vertexToCycleDistance);
				if (cycleLengthInComponent > 2) {
					cycleSizeToKidsMap.put(cycleLengthInComponent, cycleLengthInComponent);
				} 
			}
		}

		int maxNumberOfKids = 0;

		//add up all kids in cycles of length > 2
		for (Integer i : cycleSizeToKidsMap.keySet()) {
			maxNumberOfKids += cycleSizeToKidsMap.get(i);
		}
		//from one of the vertices of v of a cycle
		Map<Integer, Integer> cycleToLengthMap = new HashMap<>();

		for (Integer v : vertexToCycleDistance.keySet()) {
			Map<Integer, Integer> c = vertexToCycleDistance.get(v);
			for (Integer cycleVertex : c.keySet()) {
				cycleToLengthMap.put(cycleVertex, Math.max(c.get(cycleVertex), cycleToLengthMap.getOrDefault(cycleVertex, -1)));
			}
			
		}

		for (Integer cycleVertex : cycleToLengthMap.keySet()) {
			maxNumberOfKids += cycleToLengthMap.get(cycleVertex) + 1;
		}
		return maxNumberOfKids;

	}
	private static int cycleLength(int[] graph, int curr, Set<Integer> visited, Map<Integer, Map<Integer, Integer>> vertexToCycleDistance) {
		//the structure of the graph is a linked list with cycles. 
		int slowPtr= curr;
		int lengthToCycle = 0;
		Set<Integer> cycleDetector = new HashMap<>();
		boolean visitedNodeFound = false;
		while (!visited(slowPntr) && slowPntr < graph.length) {
			
			if (!visited.contains(slowPntr)) {
				visited.add(slowPntr);
			} else {
				//this means slowPntr is not in a component of length 2 but has been visisted which means the number of cycles in this component has been visisted already
				visistedNodeFound = true; // component has already been explored
			}

			slowPntr = graph[slowPntr];
			fastPtr = graph[graph[fastPtr]];

			if (cycleDetector.contains(slowPntr)) { // cycle found
				int cycLength =  cycleLength(graph, slowPntr, visited);
				if (cycLength == 2) {
					int[] cycles = getCycles(graph, slowPntr);
					Map<Integer, Integer> newInfo = new HashMap<>();
					newInfo.put(slowPntr, lengthToCycle); // is this really right? Maybe use a different method of cycle detection.
					vertexToCycleDistance.put(curr, newInfo);
					return cycLength;
				} else {
					visistedNodeFound ? -1 : cycleLength;

				}
				
			}
			cycleDetector.add(slowPntr);
			lengthToCycle++;
		}

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

	public static int[] getCycles(int[] graph, int slow) {
		//TOOD: need to make sure 0th is first and 1st is second
		int[] cyclces = new int[2];
		cycles[0] = slow;
		cycles[1] = graph[slow];
		return cycles;
	} 

	private static int cycleLength(int[] graph, int slow, Set<Integer> visited) {

		int length = 0;
		int fast = slow;
		while (!visited(slowPntr) &&  fast < graph.length) {
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