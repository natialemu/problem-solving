
import java.util.*;
import java.io.*;
/**
Bellmand ford & Floyd Warshall implementations. This program uses a slight variation of this problem to demostrate the implementation
Problem: https://leetcode.com/problems/cheapest-flights-within-k-stops/

**/

interface Graph {

	List<Integer> adj(int vertex);
	Integer weight(int src, int destination);
	boolean areNeighbors(int src, int destination);

}

class GraphImpl implements Graph {

	Map<Integer, Integer>[] adjMap;

	public GraphImpl(int size, int[][] edges) {
		adjMap = (HashMap<Integer, Integer>[]) new Map[size];
		for (int i = 0; i < adjMap.length; i++) {
			adjMap[i] = new HashMap<>();
		}

		populateGraph(edges);
	}

	private void populateGraph(int[][] edges) {
		for (int[] edge : edges) {
			assert (edge.length == 3);

			int src = edge[0];
			int dst = edge[1];
			int weight = edge[2];

			adjMap[src].put(dst, weight);
		}
	}

	@Override
	public List<Integer> adj(int vertex) {
		Set<Integer> neighbors = adjMap[vertex].keySet();
		return new ArrayList<>(neighbors);
	}

	@Override
	public Integer weight(int src, int destination) {
		return adjMap[src].get(destination);
	}
	@Override
	public boolean areNeighbors(int src, int destination) {
		return adjMap[src].containsKey(destination);
	}
}

public class CheapestFlight {
	//Finds the cheapest price from src to dst with up to k stops
	public static int findCheapestPrice(int n, int[][] flights, int src, int dst, int K) {
		Graph directedGraph = new GraphImpl(n, flights);
		return findCheapestPrice(directedGraph, src, dst, K + 1, new HashMap<>());
    }
    //Finds the cheapest price between any two vertices with at most K vertices in between
	public static Map<Integer, Map<Integer, Integer>> findCheapestPriceAllPairs(int n, int src, int dst,  int[][] flights, int K) {
		Graph directedGraph = new GraphImpl(n, flights);
		Map<Integer, Map<Integer, Map<Integer, Integer>>> allPairShortestPaths = new HashMap<>();
		findCheapestPriceAllPairs(directedGraph, src, dst, K, allPairShortestPaths);
		return transform(allPairShortestPaths, K);
    }

    private static void transform(Map<Integer, Map<Integer, Map<Integer, Integer>>> allPairShortestPaths, int K ) {
    	Map<Integer, Map<Integer, Map<Integer, Integer>>> transformedMap = new HashMap<>();

    	for ( Integer src : allPairShortestPaths.keySet()) {
    		for (Int dst : allPairShortestPaths.get(src).keySet()) {
    			Map<Integer, Integer> destMap = new HashMap<>();
    			destMap.put(dst, allPairShortestPaths.get(src).get(dst).get(K));
    			transformedMap.put(src, destMap);
    		}
    	} 

    	return transformedMap;

    }

    // Floyd-warshall
    private static int findCheapestPriceAllPairs(Graph graph, int src, int dst, int K, Map<Integer, Map<Integer, Map<Integer, Integer>>> memo) {
    	if (K == 0) {
    		return graph.areNeighbors(src, dst) ? graph.weight(src, dst) : Integer.MAX_VALUE;
    	}

    	if (src == dst) {
    		return 0;
    	}

    	int solution1 = memo.containsKey(src) && memo.get(src).containsKey(dst) && memo.get(src).get(dst).containsKey(K - 1) ? 
    			 memo.get(src).get(dst).get(K - 1) : findCheapestPrice(graph, src, dst, K - 1, memo); 

    	int minSolution = Integer.MAX_VALUE;
    	for (Integer neighbor : graph.adj(src)) {
    		int subProblemSolution1 = memo.containsKey(src) && memo.get(src).containsKey(neighbor) && memo.get(src).get(neighbor).containsKey(K - 1) ?
    		 memo.get(src).get(neighbor).get(K - 1) : findCheapestPrice(graph, src, neighbor, K - 1, memo);
    		int subProblemSolution2 = memo.containsKey(neighbor) && memo.get(src).containsKey(dst) && memo.get(neighbor).get(dst).containsKey(K - 1) ?
    		 memo.get(neighbor).get(dst).get(K - 1) : findCheapestPrice(graph, neighbor, dst, K - 1, memo);

    	    int currMinSolution = subProblemSolution2 + subProblemSolution1;

    		minSolution = Math.min(minSolution, currMinSolution);
    	}

    	int currentSolution = Math.min(solution1, minSolution);

    	Map<Integer, Integer> kCheapestPriceMap = new HashMap<>();
    	kCheapestPriceMap.put(K, currentSolution);
    	Map<Integer, Map<Integer, Integer>> destCheapestPriceMap = new HashMap<>();
    	destCheapestPriceMap.put(dst, kCheapestPriceMap);
    	memo.put(src, destCheapestPriceMap);
    	return currentSolution;

    }

    // bellman ford 
    private static int findCheapestPrice(Graph graph, int src, int dst, int K, Map<Integer, Map<Integer, Integer>> memo) {
    	if (K == 0) {
    		return src == dst ? 0 : Integer.MAX_VALUE;
    	}

    	if (src == dst) {
    		return 0;
    	}

    	int solution1 = memo.containsKey(src) && memo.get(src).containsKey(K - 1) ? memo.get(src).get(K - 1) :
    						findCheapestPrice(graph, src, dst, K - 1, memo); 

    	int minSolution = Integer.MAX_VALUE;
    	for (Integer neighbor : graph.adj(src)) {
    		int subProblemSolution = memo.containsKey(neighbor) && memo.get(neighbor).containsKey(K - 1) ? memo.get(neighbor).get(K - 1) :
    		findCheapestPrice(graph, neighbor, dst, K - 1, memo) + graph.weight(src, neighbor);
    		minSolution = Math.min(minSolution, subProblemSolution);
    	}

    	int currentSolution = Math.min(solution1, minSolution);

    	Map<Integer, Integer> kCheapestPriceMap = new HashMap<>();
    	kCheapestPriceMap.put(K, currentSolution);
    	memo.put(src, kCheapestPriceMap);
    	return currentSolution;

    }

    public static void main(String[] args) {
    	Scanner in = new Scanner(new BufferedReader(new InputStreamReader(System.in)));
        int t = in.nextInt(); 
        in.nextLine();
        for (int i = 1; i <= t; ++i) {
        	//TODO

      	}

    }
	
}