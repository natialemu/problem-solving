
import java.util.*;
import java.io.*;
/**

Given a graph, the goal is to find all paths from a source to a target.
Given vertices 0 ... graph.length, index i of graph is connected to vertices in graph[i]
find all the paths from vertex 0 to vertex 'n-1


Test case structure:
NumTestcases
SizeOfGraph
Vertices linked to vertex 0
Vertices linked to vertex 1
Vertices linked to vertex 2
Vertices linked to vertex SizeOfGraph


Testcase:
1
4 2
1 2
3 -1
3 -1
-1 -1



**/

class Graph{
	List<Integer>[] adjList;
	List<Integer>[] reverseGraphAdjList;

	public Graph(int[][] graph) {

		adjList = (ArrayList<Integer>[]) new ArrayList[graph.length];
		reverseGraphAdjList = (ArrayList<Integer>[]) new ArrayList[graph.length];
		initialize(adjList);
		initialize(reverseGraphAdjList);

		for (int v = 0; v < graph.length; v++) {
			int[] neighbors = graph[v];
			for (int neighbor : neighbors) {
				if (neighbor != -1) {
					adjList[v].add(neighbor);
					reverseGraphAdjList[neighbor].add(v);
				}
				
			}
		}
	}

	public Graph() {}

	private void initialize(List<Integer>[] adjList) {
		for (int i = 0; i < adjList.length; i++) {
			adjList[i] = new ArrayList<>();
		}
	}

	public Graph reverseGraph() {
		Graph reverseGraph = new Graph();
		reverseGraph.adjList = reverseGraphAdjList;
		reverseGraph.reverseGraphAdjList = adjList;
		return reverseGraph;
	}

	public List<Integer> adj(int v) {
		return adjList[v];

	}

	public int size() {
		return adjList.length;

	}
}
class Path {
	List<Integer> path;

	public Path() {
		path = new ArrayList<>();
	}

	public void add(Integer v) {
		path.add(v);
	}

	public void print() {
		for (int i = 0;i < path.size() - 1; i++) {
			System.out.print(path.get(i) + " -> ");
		}
		System.out.print(path.get(path.size() - 1));
	}
}
public class GraphPath {

	public static List<Path> allPathsSourceTarget(int[][] graph) {

		Graph directedGraph = new Graph(graph);
		List<Path> allPaths = allPathsSourceTarget(directedGraph, 0, graph.length - 1, new HashMap<>());
		return allPaths;
	}

	private static List<Path> allPathsSourceTarget(Graph graph, int s, int t, Map<Integer, List<Path>> memo) {
		if (s == t) {
			Path path = new Path();
			path.add(t);
			return Arrays.asList(path);
		}
		Graph reverseGraph = graph.reverseGraph();
		List<Path> allPaths = new ArrayList<>();
		for (int incomingVertex : reverseGraph.adj(t)) {
			List<Path> pathsToVertex = memo.containsKey(t) ? memo.get(t) : allPathsSourceTarget(graph, s, incomingVertex, memo);
			for (Path path : pathsToVertex) {
				path.add(t);

			}
			allPaths.addAll(pathsToVertex);
		}

		memo.put(t, allPaths);

		return allPaths;

	}

	public static void main(String[] args) {
		Scanner in = new Scanner(new BufferedReader(new InputStreamReader(System.in)));
        int t = in.nextInt(); // test cases
        in.nextLine();
        for (int i = 1; i <= t; ++i) {
        	String[] graphDimensions = in.nextLine().split(" ");
        	int graphLength = Integer.parseInt(graphDimensions[0]);
        	int width = Integer.parseInt(graphDimensions[1]);
        	int[][] graph = new int[graphLength][width];
        	for (int k = 0; k < graphLength; k++) {
        		String[] neighbors = in.nextLine().split(" ");
        		for (int j = 0; j < neighbors.length; j++) {
        			graph[k][j] = Integer.parseInt(neighbors[j]);
        		}

        	}
     
          List<Path> paths = allPathsSourceTarget(graph);
          System.out.println("Case #" + i + ": " + (paths.size()));
          
          printPaths(paths);
        }
	}
	private static void printPaths(List<Path> paths) {
		for (Path path : paths) {
			path.print();
			System.out.println();
		}


	}
	
}