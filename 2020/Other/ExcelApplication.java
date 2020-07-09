import java.util.*;
import java.io.*;

/**
Problem is https://leetcode.com/problems/design-excel-sum-formula/
**/
interface ExcelSum {
	//validation on the row and the columns. throw validation exception
	// failure could result in some exception being thrown
	synchronized void set(int row, char column, int value);
	// same validation should be done
	int get(int row, char column);
	//numbers.size() == 1 ? ColRow : ColRowi:ColRow+1:ColRow+2...
	synchronized int sum(int row, char column, List<String> numbers);
}
interface Graph<T> {

	void addEdge(T v, T w); // also modifies the inDegree map
	Map<T, Integer> inDegree(T c);
	List<T> adj(T u);
}

class Cell extends Region {
	public (int row, char col) {
		this.x1 = row;
		this.x2 = row;
		this.y1 = col;
		this.y2 = col;
	}

	public int row() {return x1;}

	public char col() {return y1;}

	@Override
	public boolean equals(Object o) {
		Cell other = (Cell) o;
		return Objects.equals(row, other.row) && Objects.equals(col, other.col);
	}

	@Override
	public int hashCode() {
		return Objects.hash(row, col);
	}
}

class DirectedGraph<T> implements Graph<T> {

	Map<T, List<T>> adjList;
	Map<T, Integer> inDegree;
	public DirectedGraph() {
		adjList = new HashMap<>();
		inDegree = new HashMap<>();
	}

	@Override
	public void addEdge(T u, T v) {
		List<T> to = adjList.getOrDefault(u, new ArrayList<>());
		to.add(v);
		adjList.put(u, to);

		//update inDegree
		inDegree.put(u, inDegree.getOrDefault(u, 0));
		inDegree.put(v, inDegree.getOrDefault(v, 0) + 1);
	}
	@Override
	public Map<T, Integer> inDegree() {
		return new HashMap<>(inDegree);
	}

	@Override
	public List<T> adj(T u) {
		return adjList.getOrDefault(u, new ArrayList<>());
	}
}

class Region {
	int x1, x2;
	char y1, y2;
	public Region(int x1, char y1, int x2, char y2) {
		this.y1 = y1;
		this.y2 = y2;
		this.x1 = x1;
		this.x2 = x2;
	}

	/**
	Given a rectangular region, it checks if that rectangular region overlaps with this Node's region
	**/
	public boolean overlaps(Region other) {
		// the column divides the region into left and right region. So it's analogous to the x coordinate in a
		// cartesian coordiante system. While the row(x1 & x2) are analogus to that of the y-coorindates.
		if (this.y1 <= other.y1) return other.y1 <= this.y2 && ((other.x1 <= this.x2 && other.x1 >= this.x1) || 
									   (other.x2 <= this.x2 && other.x2 >= this.x1));
		else return this.y1 <= other.y2 && ((this.x2 <= other.x2 && this.x2 >= other.x1) ||
									   (this.x1 <= other.x2 && this.x1 >= other.x1)); 
		
	}

	/**
	Given a rectangular region, returns the overlapping rectangle between the input region and the Node's region
	**/
	public Region overlappingRegion(Region other) {
		if (this.x1 <= other.x2) return new Region( Math.max(other.x1, this.x1, ((char)Math.max(other.y1, this.y1)), Math.min(other.x2, this.x2), ((char)Math.min(other.y2, this.y2)) ));
		return new Region( Math.min(other.x1, this.x1, ((char)Math.min(other.y1, this.y1)), Math.max(other.x2, this.x2), ((char)Math.max(other.y2, this.y2)) ));
	}

	public List<Cell> cellsInRegion() {
		//TODO
	}
}
class QuadNode {

	// the region represented by this quad tree
	Region region;
	// the sum of all the cells within the region represented by this QuadNode
	int sum;

	QuadNode[] children; // of size 4

	public QuadNode(int sum, int x1, char y1, int x2, char y2) {
		region = new Region(x1, y1, x2, y2);
		children = new QuadNode[4];
	}

	public QuadNode(Region region) {
		this.region = region;
	}
	public boolean overlaps(Region r) {
		return region.overlaps(r);
	}

	public Region overlappingRegion(Region r) {
		return region.overlappingRegion(r);
	}

	public int midX() {
		return (region.x1 + region.x2) / 2;
	}
	
	public char midY() {
		return (char)((region.y1 + region.y2) / 2);

	}
}

class ExcelSumImpl implements ExcelSum {
	QuadNode root;
	Graph<Cell> diGraph;

	public ExcelSumImpl(int rowMax, char colMax) {
		Region mainRegion = new Region(1, 'A', rowMax, colMax);
		root = buildTree(root, mainRegion "");
		diGraph = new DirectedGraph<>();
	}

	/*
	*	Builds a quad tree that represents the dimensions of the excel
	**/	
	private QuadNode buildTree(QuadNode current, Region currentRegion, String debug) {
		// create a new quad node if there's not a region to represnt the current region
		if (current == null) {
			current = new QuadNode(currentRegion);
		}

		// base case is a region that represents only a single cell.
		if (currentRegion.x1 == currentRegion.x2 && currentRegion.y1 == currentRegion.y2) {
			return current;
		}
		// There's no region where startRow > endRow or startCol is greater than endCol. This could happen
		// in situations where the dimensions of row and col aren't the same.
		if (currentRegion.x1 > currentRegion.x2 || currentRegion.y1 > currentRegion.y2) {
			return null;
		}

		// generate four new regions based on the mid point of the current region
		int rowMid = current.midX();
		char colMid = current.midY();
		// the first child node will be responsible for the new region in Quadrant I
		current.children[0] = buildTree(current.children[0], new Region(currentRegion.x1, rowMid, (char) (colMid + 1), currentRegion.y2), debug + "	");
		// the second child node will be responsible for the new region in Quadrant II
		current.children[1] = buildTree(current.children[1], new Region(currentRegion.x1, rowMid, currentRegion.y1, colMid), debug + "	 ");
		// the third child node will be responsible for the new region in Quadrant III
		current.children[2] = buildTree(current.children[2], new Region(rowMid + 1, currentRegion.x2, currentRegion.y1, colMid), debug + "	");
		// the fourth child node will be responsible for the new region in Quadrant IV
		current.children[3] = buildTree(current.children[3], new Region(rowMid + 1, currentRegion.x2, (char)(colMid + 1), currentRegion.y2), debug + "	 ");

		//after sub quad tree representing the current region is built, return the current quad node
		return current;
	}


	@Override
	public synchronized void set(int row, char column, int value) {

		//this represents the cell 
		Cell source = new Cell(row, column);

		//represents the total amount that a cell's value has changed.
		//if a cell has 'v' neighbors, then the cell will change by the sum of the changes 
		// for each of the 'v' vertices.
		Map<Cell, Integer> cellToChangeMap = new HashMap<>();
		//get current value of source - the new value to find the delta for current cell.
		cellToChangeMap.put(source, get(row, column) - value);

		//topologically sort the graph starting from source
		Queue<Cell> bfs = new LinkedList<>();
		Map<Cell, Integer> inDegree = clone(diGraph.inDegree()); //returns a new copy of the indegree object
		bfs.add(source);
		while (!bfs.isEmpty()) {
			int queueSize = bfs.size();
			for (int i = 0 ; i < queueSize; i++) {
				Cell current = bfs.poll();
				// update the value of the root quad node with the difference
				// between the new value and the old value at (row, col)
				int cellDelta = cellToChangeMap.get(current);

				//find the new value that the cell should have
				int newCellValue = get(current.row(), current.col()) + cellDelta;

				//set the new value and see by how much it's value changed
				int delta = set(root, current, newCellValue);

				root.sum += delta; // update the total sum of the excel

				for (Cell adj : diGraph.adj(current)) {// for each directly dependent cell
					inDegree.put(adj, inDegree.get(adj) - 1); // reduce the indegree

					// by how much did adj change? by however many it changed so far + by how much 'current' changed
					cellToChangeMap.put(adj, cellToChangeMap.getOrDefault(adj, 0) + cellToChangeMap.get(current));
					if (inDegree.get(adj) == 0) { // if there is no indegree, then it's a source and can be visited
						bfs.add(adj);
					}
				}
			}
			
		}
	}

	private Map<Cell, Integer> clone(Map<Cell, Integer> inDegree) {
		Map<Cell, Integer> cloneInDegreeMap = new HashMap<>();
		for(Cell cell : inDegree.keySet()) {
			cloneInDegreeMap.put(new Cell(cell.row(), cell.col()), inDegree.get(cell));
		}
		return cloneInDegreeMap;
	}
	//returns the difference between the old value and  the new value
	private int set(QuadNode current, Cell cell, int value) {

		/**
		While traversing down the quad tree, if a leave node that represents the cell is reached,
		update the cell with the new value and return the difference for other bigger regions that enclose this node 
		to update their value
		**/
		if (current.children[0] == null && current.children[1] == null && current.children[2] == null & current.children[3] == null) {
			int difference = value - current.sum; // find the difference
			current.sum = value; // override the old value
			return difference;
		}

		// identify the mid point and see which of the four regions contain the cell (row, col). Then traverse down that 
		// quad node that's responsible for that region
		int rowMid = current.midX();
		char colMid = currnet.midY();

		if (cell.row() <= rowMid && cell.col() > colMid) {
			int diff  = set(current.children[0], cell, value);
			current.sum += diff;
			return diff;
		} else if (cell.row() <= rowMid && cell.col() <= colMid) {
			int diff  = set(current.children[1], cell, value);
			current.sum += diff;
			return diff;
		} else if (cell.row() > rowMid && cell.col() <= colMid) {
			int diff  = set(current.children[2], cell, value);
			current.sum += diff;
			return diff;
		} else {
			int diff  = set(current.children[3], cell, value);
			current.sum += diff;
			return diff;
		}

	}

	@Override
	public  int get(int row, char column) {
		return get(root, new Cell(row, column));
	}
	public int get(QuadNode current, Cell cell) {
			/**
		While traversing down the quad tree, if a leave node that represents the cell is reached,
		return the value that's in that cell
		**/
		if (current.children[0] == null && current.children[1] == null && current.children[2] == null & current.children[3] == null) {
			return current.sum;
		}
		// divide and recurse. should depend on curr
		int rowMid = current.midX();
		char colMid = current.midY();
		if (cell.row() <= rowMid && cell.col() > colMid) {
			return get(current.children[0], cell);
		} else if (cell.row() <= rowMid && cell.col() <= colMid) {
			return get(current.children[1], cell);
		} else if (cell.row() > rowMid && cell.col() <= colMid) {
			return get(current.children[2], cell);
		} else {
			return get(current.children[3], cell);
		}
	}

	@Override
	public synchronized int sum(int row, char column, List<String> numbers) {

		// TODO: create an edge in the graph from numbers to cell(row, col)

		// parse the input to get all the regions to sum over
		List<Region> regions = parseRegion(numbers);
		buildGraph(regions);
		int allSumRegions = 0;
		for (Region region : regions) {
			// find the sum of the region
			addEdge(row, col, region.cellsInRegion());
			int regionSum = sum(root, region, "");
			allSumRegions += regionSum;
		}
		set(row, column, allSumRegions);
		
		return allSumRegions;
	}
	private void addEdge(int row, char col, List<Cell> cells) {
		for (Cell cell : cells) {
			diGraph.addEdge(new Cell(cell.row(), cell.col()), new Cell(row, col));
		}
	}

	private List<Region> parseRegion(List<String> numbers) {
		try {
			List<Region> regions = new ArrayList<>();
			for (String number : numbers) {
				String[] query = number.split(":");
				if (query.length == 1) {
					regions.add(new Region(
						Integer.parseInt(query[0].substring(1)),    
						query[0].substring(0, 1).charAt(0),   
						Integer.parseInt(query[0].substring(1)), 
						query[0].substring(0, 1).charAt(0) 
					));
				} else {
					regions.add(new Region(
						Integer.parseInt(query[0].substring(1)),    
						query[0].substring(0, 1).charAt(0),   
						Integer.parseInt(query[1].substring(1)), 
						query[1].substring(0, 1).charAt(0) 
					));
				}
			}
			return regions;
		} catch (NumberFormatException nfe) {
			nfe.printStackTrace();
			return null;
		}

	}

	private int sum(QuadNode current, Region queryRegion, String debug) {

		// if the current node's region exactly matches the query region, return the sum stored in the quad node for that region
		if (current.region.equals(queryRegion)) {
			return current.sum;
		}

		int overallSum = 0;
		/**
		- Check if the query region overlaps with any of the four sub regions reprsented by the four children of the current node. 
		- Then find the corresponding overlapping region betwen the query region & the sub regions, and find the sum of each overlapping region. 
		- Then add all the sums up and return it
		**/
		if (current.children[0] != null && current.children[0].overlaps(queryRegion)) {
			Region currOverlappingRegion = current.children[0].overlappingRegion(queryRegion);
			overallSum += sum(current.children[0], currOverlappingRegion, debug + "	");
		}
		if (current.children[1] != null && current.children[1].overlaps(queryRegion)) {
			Region currOverlappingRegion = current.children[1].overlappingRegion(queryRegion);
			overallSum += sum(current.children[1], currOverlappingRegion, debug + "	");
		}

		if (current.children[2] != null && current.children[2].overlaps(queryRegion)) {
			Region currOverlappingRegion = current.children[2].overlappingRegion(queryRegion);
			overallSum += sum(current.children[2], overlappingRegion, debug + "	");
		}

		if (current.children[3] != null && current.children[3].overlaps(queryRegion)) {
			Region currOverlappingRegion = current.children[3].overlappingRegion(queryRegion);
			overallSum += sum(current.children[3], currOverlappingRegion, debug + "	");
		}
		return overallSum;
	}
}

public class ExcelApplication {
	public static void main(String[] args) {

		ExcelSum excel = new ExcelSumImpl(3, 'C');

		class ExcelModifierTask implements Runnable {
			int newValue, row;
			char col;
			ExcelSum excel;
			public ExcelModifierTask(int n, ExcelSum excel, int row, char col) {
			 	newValue = n;
			 	this.row = row;
			 	this.col = col;
			 	this.excel = excel;
			}

			@Override
			public void run() {
				excel.set(row, col, newValue);
			}
		}

		class ExceValueRetriever implements Callable<Integer> {
			int row;
			char col;
			ExcelSum excel;
			public ExceValueRetriever(ExcelSum excel, int row, char col) {
			 	this.row = row;
			 	this.col = col;
			 	this.excel = excel;
			}

			@Override
			public Integer call() {
				return excel.get(row, col, newValue);
			}
		}

		ExecutorService threadPool = Execcutors.newFixedThreadPool(4);
		ExecutorCompletionService ecs = new ExecutorCompletionService(threadPool);

		ecs.submit(new ExcelModifierTask(2, excel, 1, 'A'), new Integer(0));
		ecs.submit(new ExcelModifierTask(4, excel, 2, 'B'), new Integer(1));
		ecs.submit(new ExcelModifierTask(8, excel, 3, 'B'), new Integer(2));
		ecs.submit(new ExcelModifierTask(4, excel, 2, 'C'), new Integer(3));

		ecs.submit(new ExceValueRetriever(excel, 1, 'A'), new Integer(3));
		ecs.submit(new ExceValueRetriever(excel, 2, 'B'), new Integer(3));
		ecs.submit(new ExceValueRetriever(excel, 3, 'A'), new Integer(3));

		int count = 3;
		while (count != 0) {
			try {
				Future<Integer> f = ecs.take();
				while (!f.isDone()) {}
				System.out.println(f.get());
				//how to know which query resulted in what
				// what does this show about when to use ExecutionCompletion service?
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			

		}

		// excel.set(1, 'A', 2);
		// System.out.println("Set A1 to 2");
		// int val = excel.get(1, 'A');
		// System.out.println("A1: " +val);
		// excel.sum(3, 'C', Arrays.asList("A1"));
		// System.out.println("Found sum at A1 & set to C3");
		// System.out.println("A1: " +excel.get(1, 'A'));
		// System.out.println("3C: " + excel.get(3, 'C'));
		// val = excel.get(2, 'B');
		// System.out.println("2B: " + val);
		// excel.set(2, 'B', 4);
		// System.out.println("Set 2B to 4");
		// val = excel.get(2, 'B');
		// excel.set(3, 'B', 8);
		// excel.set(2, 'C', 4);
		// excel.sum(1, 'A', Arrays.asList("B2:C3"));
		// System.out.println("A1: " + excel.get(1, 'A'));


	}
}