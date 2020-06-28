import java.util.*;
import java.io.*;


interface Graph {
	List<Integer> V();
	List<Integer> adj(int u);
	void addEdge(int u, int v);
}

class UndirectedGraph implements Graph {
	List<Intger>[] adjList;

	public UndirectedGraph(int size) {
		adjList = (ArrayList<Integer>[]) new ArrayList[size];
		//initialize
		for (int i = 0; i < size; i++) {
			adjList[i] = new ArrayList<>();
		} 
	}

	@Override
	public List<Integer> V() {
		List<Integer> vertices = new ArrayList<>();
		for (int vertex = 0; vertex < adjList.length; vertex++) {
			vertices.add(vertex);
		}
		return vertices;
	}

	@Override
	public List<Integer> adj(int u) {
		validate(u);
		return adjList[u];

	}

	@Override
	public void addEdge(int u, int v) {
		validate(u);
		validate(v);
		adjList[u].add(v);
		adjList[v].add(u);
	} 

	private void validate(int vertex) {
		if (vertex < 0 || vertex > adjList.length) throw new IllegalArgumentException("Invalid value for vertex");
	}
}

/**
This is a union find data structure that's used to represent each component of the bipartite graph.
The components initially not need be complete bipartite graphs. However, any merge operation will only result in 
a complete bipartite graph.
**/
interface DisjointSet {
	int find(int i); // given a vertex, find which component it belongs to(returns the root of the component)
	void merge(int i, int j); // given two vertices, if they're in different components, merge the two components. 
	boolean completeBipartiteComponent(int i); // the component that i is in is a complete bipartite graph
	void setCompleteBipartite(int i, boolean val); // set the component that i is in to be complete bipartite
	List<Integer> parents(); // returns root of each component. the data structure will need to keep track of that
	int size(int i);// return the size of component that 'i' is in
	int numEdgesIn(int i); //number of edges in component i
	void setNumEdgesIn(int i, int n); // set the number of edges in component i to n
	DisjointSet clone();
}


class UnionFind implements DisjointSet {
	int[] parentOf; // if parentRootedTree[i] == i, then i is a parent
	Map<Integer, Integer> parents; // maps each component to its size
	Map<Integer, Boolean> completeBipartiteMap;
	Map<Integer, Integer> componentToEdgeNumMap;

	public UnionFind(int size) {
		parentOf = new int[size];
		parents = new ArrayList<>();
		completeBipartiteMap = new HashMap<>();
		componentToEdgeNumMap = new HashMap<>(); 
		//initially there are no edges in the component since each vertex is in its own component.
		for (int i = 0; i < parentsOf; i++) {
			componentToEdgeNumMap.put(i, 0);
		}
	}

	@Override
	public int find(int i) {
		while (parentOf[i] != i) {
			parent[i] = parent[parent[i]]; // set i's parent to be its grand parent
			i = parent[i];
		}
		return i;
	} 

	@Override 
	public void merge(int i, int j) {
		int parentOfI = find(i);
		int parentOfJ = find(j);

		if (parents.get(parentOfJ).size() > parents.get(parentOfI)) {
			parentOf[parentOfI] = parentOfJ;
		} else  {
			parentOf[parentOfJ] = parentofI;
		}
	}

	@Override
	public boolean completeBipartiteComponent(int i) {
		return completeBipartiteMap.containsKey(i) && completeBipartiteMap.get(i);
	}

	@Override
	public void setCompleteBipartite(int i, boolean value) {
		if (!completeBipartiteMap.containsKey(find(i))) throw new IllegalArgumentException("P");
		completeBipartiteMap.put(i, value);
	}

	@Override
	public List<Integer> parents() {
		return new ArrayList<>(parents.keySet());
	}

	@Override
	public int size(int i) {
		if (!completeBipartiteMap.containsKey(find(i))) throw new IllegalArgumentException("vertex not found in any component");
		return parents.get(find(i)).size();
	}

	@Override
	public int numEdgesIn(int i, int n) {
		if (!componentToEdgeNumMap.containsKey(find(i))) throw new IllegalArgumentException("vertex not found in any component");
		return componentToEdgeNumMap.get(find(i));
	}

	@Override
	public void setNumEdgesIn(int i, int n) {
		if (!componentToEdgeNumMap.containsKey(find(i))) throw new IllegalArgumentException("vertex not found in any component");
		componentToEdgeNumMap.put(find(i), n);
	}

	@Override
	public DisjointSet clone() {
		DisjointSet newDisJointSet = new DisjointSet(parentsOf.length);

		for (int i = 0; i < parentsOf.length; i++) {
			newDisJointSet.parentsOf[i] = parentsOf[i];
		}
		//clone and set the other fields too.
		return newDisJointSet;

	}


}


class FreeFormFactory {
	public static int minCostToPerfectMatching(int[][] adjMatrix) {
		Graph undirectedGraph = new UndirectedGraph(adjMatrix.length);
		for (int i = 0; i < adjMatrix.length; i++) {
			for (int j = 0; j < adjMatrix[i].length; j++) {
				if (adjMatrix[i][j] == 1) undirectedGraph.addEdge(i,j);
			}
		}
		return minCostToTeachEmployees(undirectedGraph);

	}

	public static int minCostToTeachEmployees(Graph g) {
		DisjointSet set = new UnionFind(g.V()); // number of initial disjoint components
		for (Integer u : g.V()) {
			boolean[] visited = new boolean[g.V()];
			dfs(g, u, set, visited);
			if (isCompleteBipartite(u, set)) set.setCompleteBipartite(u, true);
		}
		List<Integer> parents = set.parents();
		return minCostToTeachEmployees(g, parents, 0, parents.size() - 1, new HashMap<>());
	}

	// a complete is a complete bipartite component iff, the number of edges between the vertices in the component
	// if equal to the max number of edges there can be in a bipartite graph of component's size. This is only true, if 
	// the graph is a bipartite graph which means edges exist between vertices of different colors only and there is no odd lengthed cycle.
	private static boolean isCompleteBipartite(int v, DisjointSet set) {
		int numElementsInComponentV = set.size(v);
		int maxNumEdges = (numElementsInComponentV/2)*(numElementsInComponentV/2); // the number of edges the component should have if it's a complete bipartite component.
		return maxNumEdges == set.numEdgesIn(v);
	}

	private static int minCostToTeachEmployees(Graph g, List<Integer> parents, DisjointSet set, int i, int j, Map<Integer, Map<Integer, Integer>> memo) {
		if ( i >= j ) return 0;

		int source1 = parents.get(i);
		int source2 = parents.get(j);

		//if both i and j components are complete bipartite graphs, try i, j-1 & i + 1, j and take the one with min cost
		int soln1 = memo.containsKey(i + 1) && memo.get(i + 1).containsKey(j) ? memo.get(i + 1).get(j) : minCostToTeachEmployees(g, parents, set, i + 1, j, memo);
		int soln2 = memo.containsKey(i) && memo.get(i).containsKey(j - 1) ? memo.get(i).get(j - 1) : minCostToTeachEmployees(g, parents, set, i, j - 1, memo);
		if (set.completeBipartiteComponent(source1) && set.completeBipartiteComponent(soruce2)) return Math.min(soln1, soln2);
		else {
			DistjointSet clone = clone(set);
			int newEdges = merge(g, source1, source2, clone);
			int soln3 = memo.containsKey(i + 1) && memo.get(i + 1).containsKey(j - 1) ? memo.get(i + 1).get(j - 1) : minCostToTeachEmployees(g, parents, clone, i + 1, j - 1);
			return Math.min(Math.min(soln1, soln2), soln3 + newEdges);
		}
	}

	private static int merge(Graph g, int source1, int source2, DisjointSet set) {
		//TODO'/
		// number of edges: num components in source1 * num in source 2
		// num edges in 
		int maxEdgeNum = set.size(source1) + set.size(source2);
		int newlyAddedEdges = (maxEdgeNum/2)*(maxEdgeNum/2) - set.numEdgesIn(source1) - set.numEdgesIn(source2);
		set.merge(source1, source2);
		set.setCompleteBipartite(source1);//
		set.setNumEdgesIn(source1, maxEdgeNum);
		return newlyAddedEdges;

	}

	/**
	perform DFS from curr, and any reachable node from curr is in the same component.
	**/
	private static void dfs(Graph g, int curr, DisjointSet set, boolean[] visited) {
		visited[curr] = true;
		for (Int w : g.adj(curr)) {
			if (!visited[w]) {
				// w is in same component as curr since it's reacha
				set.merge(curr, w);
				dfs(g, w, set, visited);
			}
			set.setNumEdgesIn(curr, set.numEdgesIn(curr) + 1); // the number of edges in the component

		}
	}



	public static void main(String[] args) {
		Scanner in = new Scanner(new BufferedReader(new InputStreamReader(System.in)));
        int t = in.nextInt(); 
        in.nextLine();
        for (int i = 1; i <= t; ++i) {
           int N = in.nextInt(); // number of works and machines
           in.nextLine();
           int[][] matrix = new int[N][N];
           for (int k = 0; k < N; k++) {
           	   String line = in.nextLine();
           	   for (int j = 0; j < line.length(); j++) {
           	   	   matrix[k][j] = Integer.parseInt(line.charAt(j));
           	   }
           }
           int minEmployeesTaught = minCostToPerfectMatching(matrix);
           System.out.println("Case #" + i + ": " + (sol));
      }
	} 
	
}