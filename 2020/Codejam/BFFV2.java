import java.util.*;
import java.io.*;

public class BFFV2 {
	 public static int maxCircleSize(int[] graph) {

	 	/**
	 	Reverse the graph. the reversed graph is not going to be a linked list
	 	**/
	 	ArrayList<Integer>[] reversedGraph = reverse(graph);

	 	// a list of all the cycles in the graph.
	 	List<List<Integer>> cycles = getCycles(reversedGraph);
	 	System.out.println("Num cycles: "  + cycles.size());
	 	int maxKidsInCircle = 0;

	 	/**
	 	for each cycle, if the length is two, find the longest path from each vertex that's part of the cycle
	 	Otherwise, just take the number of kids that make up the cycle.
	 	**/
	 	for (List<Integer> cycle : cycles) {
	 		
	 		if (cycle.size() == 2) {
	 			int a = cycle.get(0);
	 			int b = cycle.get(1);
	 			int longestPath = longestPathFrom(a, reversedGraph, b);
	 			longestPath += longestPathFrom(b, reversedGraph, a);
	 			maxKidsInCircle += longestPath;
	 		} else {
	 			maxKidsInCircle += cycle.size();
	 		}
	 	}
	 	return maxKidsInCircle;
	 }

	 private static ArrayList<Integer>[] reverse(int[] graph) {
	 	ArrayList<Integer>[] adjGraph = (ArrayList<Integer>[]) new ArrayList[graph.length];
	 	//initialize
	 	for (int i = 0; i < adjGraph.length; i++) {
	 		adjGraph[i] = new ArrayList<>();
	 	}
	 	for (int kid = 0; kid < graph.length; kid++) {
	 		//original graph: kid - > graph[kid]
	 		//reversed graph: graph[kid] -> kid
	 		adjGraph[graph[kid]].add(kid);
	 	}
	 	return adjGraph;
	 }

	 private static List<List<Integer>> getCycles(ArrayList<Integer>[] graph) {
	 	List<List<Integer>> allCycles = new ArrayList<>();

	 	//keep track of visisted vertices thoughout all DFSes
	 	Set<Integer> visisted = new HashSet<>();

	 	for (int v = 0; v < graph.length; v++) {
	 		if (!visisted.contains(v)) {
	 			//keep track of visisted vertices per dfs
	 			Set<Integer> cycleDetector = new HashSet<>();
	 			List<Integer> cycle = new ArrayList<>();
	 			Stack<Integer> path = new Stack<>();
	 			cycles(v, graph, visisted, cycleDetector, cycle, path);
	 			if (cycle.size() != 0) {
	 				allCycles.add(cycle);
	 			}
	 		}
	 	}
	 	return allCycles;

	 }

	 /**
	 if a visisted node is en thought dfs:
	 **/
	 private static void cycles(int curr, ArrayList<Integer>[] graph, Set<Integer> visisted, Set<Integer> cycleDetector, List<Integer> cycle, Stack<Integer> path) {
	 	path.push(curr);
	 	
	 	if (cycleDetector.contains(curr)) {
	 		populateCycleList(cycle, curr, path);
	 		return;
	 	}

	 	if (visisted.contains(curr)) {
	 		if (!path.isEmpty()) path.pop();
	 		return;
	 	}
	 	
	 	cycleDetector.add(curr);
	 	visisted.add(curr);

	 	for (Integer adj : graph[curr]) {
	 		cycles(adj, graph, visisted, cycleDetector, cycle, path);
	 	}

	 }

	 private static void populateCycleList(List<Integer> cycleList, int v, Stack<Integer> path) {
	 	cycleList.add(path.pop());
	 	while (!path.isEmpty() && path.peek() != v) {
	 		cycleList.add(path.pop());
	 	}
	 }

	 private static int longestPathFrom(int v, ArrayList<Integer>[] graph, int other) {
	 	if (graph[v].size() == 0) {
	 		return 1;
	 	}

	 	int currLongest = 1;
	 	for (Integer adj : graph[v]) {
	 		if (adj != other) {
	 			int path = longestPathFrom(adj, graph, other);
	 			currLongest = Math.max(path + 1, currLongest);
	 		}
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