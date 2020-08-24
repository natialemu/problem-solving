import java.util.*;
import java.io.*;

public class BFFV2 {
	 public static int maxCircleSize(int[] graph) {

	 	/**
	 	Problem to fix: 
	 	the reversed graph will not be a list with a cyle. it's a full fledged grpah
	 	**/
	 	List<Integer>[] reversedGraph = reverse(graph);
	 	List<List<Integer>> cycles = getCycles(reversedGraph);
	 	int maxKidsInCircle = 0;
	 	for (List<Integer> cycle : cycles) {
	 		if (cycle.size() == 2) {
	 			int a = cycle.get(0);
	 			int b = cycle.get(1);
	 			int longestPath = longestPathFrom(a, reversedGraph);
	 			longestPath += longestPathFrom(b, reversedGraph);
	 			maxKidsInCircle += longestPath;
	 		} else {
	 			maxKidsInCircle += cycle.size();
	 		}
	 	}
	 	return maxKidsInCircle;
	 }

	 private static List<Integer>[] reverse(int[] graph) {
	 	List<Integer>[] adjGraph = (ArrayList<Integer>[]) new List[graph.length];
	 	//initialize
	 	for (int i = 0; i < adjGraph.length; i++) {
	 		adjGraph[i] = new ArrayList<>();
	 	}
	 	for (int kid = 0; kid < graph.length; kid++) {
	 		//graph[kid] -> kid
	 		adjGraph[graph[kid]].add(kid);
	 	}
	 	return adjGraph;
	 }

	 private static List<List<Integer>> getCycles(List<Integer>[] graph) {
	 	List<List<Integer>> allCycles = new ArrayList<>();
	 	Set<Integer> visisted = new HashSet<>();

	 	for (int v = 0; v < graph.length; v++) {
	 		if (!visisted.contains(v)) {
	 			Set<Integer> cycleDetector = new HashSet<>();
	 			List<Integer> cycle = new ArrayList<>();
	 			cycles(v, graph, visisted, cycleDetector, cycle);
	 			if (cycle.size() != 0) {
	 				allCycles.add(cycle);
	 			}
	 		}
	 	}
	 	return allCycles;

	 }

	 private static boolean cycles(int curr, List<Integer>[] graph, Set<Integer> visisted, Set<Integer> cycleDetector, List<Integer> cycle) {
	 	if (visisted.contains(curr)) return false;
	 	if (cycleDetector.contains(curr)) {
	 		cycle.add(curr);
	 		return true;
	 	}
	 	cycleDetector.add(curr);
	 	visisted.add(curr);

	 	for (Integer adj : graph[curr]) {
	 		if (cycles(adj, graph, visisted, cycleDetector, cycle)) {
	 			cycle.add(curr);
	 			return true;
	 		}
	 	}
	 	return false;
	 }


	 private static int longestPathFrom(int v, List<Integer>[] graph) {
	 	if (graph[v].size() == 0) {
	 		return 1;
	 	}

	 	int currLongest = 0;
	 	for (Integer adj : graph[v]) {
	 		int path = longestPathFrom(adj, graph);
	 		currLongest = Math.max(path + 1, currLongest);
	 	}
	 	return currLongest;
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
            System.out.println("Case #" + i + ": " + (maxKidsInCircle));
        }

	}

}