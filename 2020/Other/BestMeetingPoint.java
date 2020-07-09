import java.util.*;
import java.util.concurrent.*;
import java.io.*;


/**
A group of 2 or more ppl want to meet at some location. the location they pick
is the one that minimizes the total distance they  each travel to get to the chosen
location. Given a 2d grid which represents the locations of each of the homes
of the ppl and valid meeting points, find the best meeting point.

Algorithm:

perform Dijkstra from each home to find the distance of each location from each home. then pick
the location with min total distance.

**/

/**

Represents each location and home's co-ordindate

**/
class Cell {
	int i;
	int j;
	public Cell(int i, int j) {
		this.i = i;
		this.j = j;
	}

	@Override
	public boolean equals(Object o) {
		Cell other = (Cell) o;
		return Objects.equals(i, other.i) && Objects.equals(j, other.j);
	}

	@Override
	public int hashCode() {
		return Objects.hash(i, j);
	}
	@Override
	public String toString() {
		return "[" + i + ", " + j + "]";
	}
}

/**
An undirected graph of representing locations connected to each other.
**/
class Graph {
	Map<Cell, List<Cell>> adjList;
	List<Cell> homeLocations; // locations that are homes
	public Graph() {
		adjList = new HashMap<>();
		homeLocations = new ArrayList<>();
	}

	public void addEdge(Integer[] cell1, Integer[] cell2) {
		if (cell1.length != 3 && cell2.length != 3) throw new IllegalArgumentException("Can't add edge. Invalid size for vertices. size=" + cell1.length);
		Cell firstCell = new Cell(cell1[0], cell1[1]);
		Cell secondCell = new Cell(cell2[0], cell2[1]);
		if ( !adjList.containsKey(firstCell) && cell1[2] == 1) homeLocations.add(firstCell); 
		
		List<Cell> firstCellAdj  = adjList.getOrDefault(firstCell, new ArrayList<Cell>());
		// add edge
		firstCellAdj.add(secondCell);
		adjList.put(firstCell, firstCellAdj);
	}

	public List<Cell> homes() {
		return homeLocations;
	}

	public List<Cell> adj(Cell c) {
		if (c == null) throw new IllegalArgumentException("Failed to retrieve adjacent cells. Invalid input. Input=" + c);
		if (!adjList.containsKey(c)) throw new IllegalArgumentException("Failed to retrieve adjacent cells. Vertex not found in graph. Input=" + c);
		
		return adjList.get(c);
	}


}

/**
A runnable task that finds the shortest path in a graph starting from a source location.
**/
class ShortestPathTask implements Runnable {
	Graph g;					// the graph to traverse
	int[][] distTo;				// distTo[i][j] is the distance to get to location i,j from source
	Map<Cell, Boolean> marked;	// keep track of visited cells while traversing
	Cell source;				
	public ShortestPathTask(Graph g, int[][] distTo, Cell source) {
		this.g = g;
		this.distTo = distTo;
		this.source = source;
		marked = new HashMap<>();
	}
	@Override
	public void run() {
		bfs();
	}
	private void bfs() {
		//priority queue based on how close a location is to the source
		PriorityQueue<Cell> pq = new PriorityQueue<>((a,b) -> (dist(a,source) - dist(b, source)));
		pq.add(source);
		mark(source);
		while(!pq.isEmpty()) {
			Cell currentLoc = pq.poll();
			mark(currentLoc);
			int currDist = dist(currentLoc, source); // shortest distance from source to currentLoc
			distTo[currentLoc.i][currentLoc.j] += currDist;
			for (Cell neighbor : g.adj(currentLoc)) {// add each unvisited neighbor of currentLoc
				if (!isMarked(neighbor)) pq.add(neighbor);
			}
		}
	}

	private int dist(Cell cell1, Cell cell2) {
		return Math.abs(cell1.i - cell2.i) + Math.abs(cell1.j - cell2.j);
	}

	private void mark(Cell c) {
		marked.put(c, true);
	}
	private boolean isMarked(Cell c) {
		return marked.containsKey(c) && marked.get(c);
	}
}
public class BestMeetingPoint {
	public static Integer[] minTotalDistance(int[][] grid) {

		if (grid == null || grid.length == 0 || grid[0].length == 0) throw new IllegalArgumentException("Failed to compute best meeting point. Invalid input.");
		int[][] distTo = new int[grid.length][grid[0].length];
		Graph graph = new Graph();
		buildGraph(graph, grid);
		List<Cell> homes = graph.homes();
		ExecutorService threadPool = Executors.newFixedThreadPool(homes.size());
		ExecutorCompletionService<Integer> service =
                new ExecutorCompletionService<Integer>(threadPool);

		 // Submit shortest path traversal tasks.
        for (Integer i = 0; i < homes.size(); i += 1) {
            service.submit(new ShortestPathTask(graph, distTo, homes.get(i)), i);
        }
        int completedTasks = 0;
        while (completedTasks < homes.size()) {
        	Future<Integer> f = service.poll();
            if (f != null) {
                completedTasks++;
            }

        }
        threadPool.shutdown();
        Integer[] bestMeetingPoint = findBestMeetingPoint(distTo, grid);
        return bestMeetingPoint;
	}

	private static Integer[] findBestMeetingPoint(int[][] distTo, int[][] grid) {
		int bestI = -1;
		int bestJ = -1;
		int bestSoFar = Integer.MAX_VALUE;;
		for (int i = 0; i < distTo.length; i++) {
			for (int j = 0; j < distTo[i].length; j++) {
				bestI = grid[i][j] != 1 && distTo[i][j] < bestSoFar ? i : bestI;
				bestJ = grid[i][j] != 1 && distTo[i][j] < bestSoFar ? j : bestJ;
				bestSoFar = grid[i][j] != 1 && distTo[i][j] < bestSoFar ? distTo[i][j] : bestSoFar;
				
			}
		}
		return new Integer[] {bestI, bestJ, bestSoFar};
	}

	private static void buildGraph(Graph graph, int[][] grid) {

		//for each cell in the grid
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				List<Integer[]> adjCells = adj(grid, i, j);
				// add an edge from the current location(i,j) to all adjacent locations
				for (Integer[] neighbor: adjCells) {
					graph.addEdge(new Integer[] {i, j, grid[i][j]}, neighbor);
				}
			}
		}
	}

	// given a location (i, j) and a grid, it returns all valid adjacent locations along with the value at those locations in the grid
	private static List<Integer[]> adj(int[][] grid, int i, int j) {
		List<Integer[]> adjCells = new ArrayList<>();
		if (i + 1 < grid.length) {
			adjCells.add(new Integer[] {i + 1, j, grid[i + 1][j]});
		}
		if (i - 1 >= 0) {
			adjCells.add(new Integer[] {i  - 1, j, grid[i - 1][j]});
		}

		if (grid[i].length > 0 && j + 1 < grid[i].length) {
			adjCells.add(new Integer[] {i, j + 1, grid[i][j + 1]});
		}

		if (j - 1 >= 0 ) {
			adjCells.add(new Integer[] {i, j - 1, grid[i][j - 1]});
		}
		return adjCells;

	}

	public static void main(String[] args) {
		Scanner in = new Scanner(new BufferedReader(new InputStreamReader(System.in)));
        int t = in.nextInt(); 
        for (int i = 1; i <= t; ++i) {
          int n = in.nextInt();
          int m = in.nextInt();
          in.nextLine();
          int[][] grid = new int[n][m];
          for (int j = 0; j < n; j++) {
              String[] line = in.nextLine().split(" ");
              // System.out.println("Case #" + i + ": size of line is " + line.length);
              for (int k = 0; k < line.length; k++) {
                  grid[j][k] = Integer.parseInt(line[k]);
              }
          }
          Integer[] bmp = minTotalDistance(grid);
          System.out.println("Case #" + i + ": " + " [" + (bmp[0]) + ", " + (bmp[1]) + "] " + (bmp[2]));
        }
	}
}