import java.util.*;
import java.io.*;
import java.util.concurrent.*;


interface Graph<T extends Colorable> {
	void addVertex(T u);
	int size();
	List<T> V();
	void addEdge(T u, T v);
}
class UndirectedGraph<T extends Colorable> implements Graph<T> {
	Map<T, List<T>> adjList;

	public UndirectedGraph() {
		adjList = new HashMap<>();
	}

	@Override
	public void addVertex(T u) {
		if (!adjList.containsKey(u)) adjList.put(u, new ArrayList<>());
	}

	@Override
	public int size() {
		return adjList.size();
	}

	@Override
	public List<T> V() {
		return new ArrayList<>(adjList.keySet());
	}

	public List<T> adj(T u) {
		validate(u);
		return adjList.get(u);
	}


	@Override
	public void addEdge(T u, T v) {
		validate(u);
		validate(v);

		List<T> adjOfU = adjList.getOrDefault(u, new ArrayList<());
		adjOfU.add(v);
		adjList.put(u, adjOfU);

		List<T> adjOfV = adjList.getOrDefault(v, new ArrayList<());
		adjOfU.add(u);
		adjList.put(v, adjOfV);
	}
}

class Board {
	//Define the behaviors of the board
	char[][] stage;
	String type;

	public Board() {type = "GENERAL"; }

	public Board(char[][] stage) {
		this.stage = stage;
		this.type = "GENERAL";
	}

	public Board cloneWith(char c, char other) {
		char[][] cloneStage = new char[stage.length][stage.length];
		for (int i = 0; i < stage.length; i++) {
			for (int j = 0; j  < stage[i].length; j++) {
				if (stage[i][j] != other) cloneStage[i][j] = stage[i][j] == ' ' ? stage[i][j] : c;
			}
		}
		return new Board(cloneStage);
	}
	public int size() {
		return stage.length;
	}

	public List<Cell> prohibitedCells() {
		//TODO
	}

	public void setType(String type)  {
		this.type = type;
	}
	public void set(int i, int j, char piece) {
		validate(i, j);
		stage[i][j] = piece;
	}
	public char get(int i, int j) {
		return stage[i][j];
	}
}

interface Colorable {
	boolean color();
}
class Line implements Colorable{
	// fields: m, b
	//behavior: int[] cell(Line other)
	int slope;
	int b;
	int xIntercept;
	boolean colour;

	public Line(int slope, int b, boolean color) {
		this.slope = slope;
		this.b = b;
		colour = color;
	}

	public int[] cellFormedWith(Line other) {
		//this logic only applies for the use case of this problem
		if (slope == Integer.MAX_VALUE && other.slope == 0) return new int[] {xIntercept, other.b};
		if (slope == 0 && other.slope == Integer.MAX_VALUE) return new int[] {other.xIntercept, b};

		int x = (int) ((b - other.b) / (slope - other.slope));
		int y = sope*x  + b;
		return new int[] {x, y};
	}

	@Override
	public boolean color() {
		return colour;
	}

}

class Cell {
	// co-ordinates 
	int i, j;
	public Cell(int i, int j) {
		this.i = i;
		this.j = j;
	}

}

class BipartiteSolver<T extends Colorable> {
	Graph<T> bipartiteGraph;
	Map<T,T> mate; // matching.get()
	Map<T,T> edgeTo;
	T lastVertex

	public BipartiteSolver(Graph<T> biGraph, boolean fordFulkerson) {
		bipartiteGraph = biGraph;

		mate = new HashMap<>();
		edgeTo = new HashMap<>();
		if (fordFulkerson) maxFlowViaFordFulkerson();
		else maxFlowViaAlternatingPath();
	}

	private void maxFlowViaAlternatingPath() {
		while (alternatingPathExists()) {
			//put into the matching the edges along the alternating path that weren't in the matching 
			// the last edge should always be in the new matching by the definition of an alternating path. i.e
			// an alternating path starts and ends with an unmatched vertex.
			for (T curr = lastVertex; curr != edgeTo.get(curr); curr = edgeTo.get(edgeTo.get(curr))) {
				T prev = edgeTo.get(curr); // the edge that led to curr needs to be put into the matching
				mate.put(prev, curr);
				mate.put(curr, prev);
			}

			// that out the edges along the alternating path that used to be in the matching that used to be 
			for (T curr = edgeTo.get(lastVertex); curr != edgeTo.get(curr); curr = edgeTo.get(edgeTo.get(curr))) {
				T prev = edgeTo.get(curr);
				mate.remove(prev);
				mate.remove(curr);
			}

		}

	}
	private void maxFlowViaFordFulkerson() {
		//TODO
	}

	public Map<T,T> maxMatching() {
		return mate;
	}

	private boolean alternatingPathExists() {
		//Convention: Colored vetices are 
		List<T> validSources = new ArrayList<>();
		for (T vertex : bipartiteGraph.V()) {
			if (vertex.color() && !mate.containsKey(vertex)) validSources.add(vertex);
		}
		Set<T> visited = new HashSet<>();
		for (T vertex : validSources) {
			if (visited.contains(vertex)) continue;
			Queue<T> bfs = new LinkedList<>();
			bfs.add(vertex);
			while (!bfs.isEmpty()) {
				T current = bfs.poll();
				visited.add(current);
				for (T next : bipartiteGraph.adj(current)) {
					if (validEdge(current, next)) {
						edgeTo.put(next, current);
						if (!mate.containsKey(next)) {
							lastVertex = next;
							return true;
						}
						bfs.add(next);
					}
				}			
		    }
		}

		return false;
	}

	// if the source is colored, by convention, it must not be in the matching. 
	// if the source is not colored, by convension, it must be in the matching.
	private boolean validEdge(T u, T v) {
		return (u.color() && !mate.containsKey(u)) || (!u.color() && mate.containsKey(u));
	}
}
class FashionShow {

	public static Board maxStylePoints(char[][] stage) {
		/**
		Step 1: Parse the stage into a board object
		Step 2: divide the board into + only board, white X only board, Black X only board
				- return a maping betwen the new boards and the dimensions of each new board
		Step 3: For each new board:
			- create a mapping between the board type and prohibited cells
		Step 3: build a graph for each board based on the following:
				- the dimensions
				- prohibited cells

		Step 4: put the graph into a bipartite solver
		Step 5: retrieve the max matching for each graph
		Step 6: convert the matching pairs to cells
		Step 7: Merge the results
		**/

		Board board = new Board(stage);
		Map<Board, List<Line>> boardToDimensionMapping = divide(board);

		Map<Board, List<Cell>> prohibitedCells = retriveProhibitedCells(boardToDimensionMapping.keySet());

		Map<Board, Graph<Line>> graphPerBoard = buildAndPopuLateGraph(boardToDimensionMapping, prohibitedCells);

		Map<Board, Map<Line, Line>> maxMatchingsPerBoard = findMaximalMatchingInParallel(graphPerBoard);

		List<Board> optimallyPlacedBoards = placePiecesOptimally(maxMatchingsPerBoard);

		Board finalBoard = combineBoards(optimallyPlacedBoards);

		return finalBoard;
	}

	private Map<Board, Graph<Line>> buildAndPopuLateGraph(Map<Board, List<Line>> boardToDimensionMap, Map<Board, List<Cell> prohibitedCells) {
		//asser that the size of prohibited cells and boardToDimensionMap is the same
		Map<Board, Graph<Line>> graphPerBoard = new ConcurrentHashMap<>();
		for (Board b : boardToDimensionMap.keySet()) {
			List<Cells> invalidCells = prohibitedCells.get(b);
			List<Line> dimensions = boardToDimensionMap.get(b);

			Graph<Line> ug = new UndirectedGraph();
			for (Line l : dimensions)  ug.addVertex(l);

			for (Line l1 : dimensions)
				for (Line l2 : dimensions)
					if (isAllowed(l1.cellFormedWith(l2), prohibitedCells)) ug.addEdge(l1, l2);

			graphPerBoard.put(b, ug);
		}
		return graphPerBoard;
	}

	private Map<Board, Map<Line, Line>> findMaximalMatchingInParallel(Map<Board, Graph<Line>> biGraphPerBoard) {

		//:threads can be created in the main method and then th findMaxMatchingInParalel behavior could be encapsulated as a callable task.
		Map<Board, Map<Line, Line>> matchingPerBoard = new ConcurrentHashMap<>();
		biGraphPerBoard.parallelStream.map( (board, graph)-> {
			BipartiteSolver<Line> bpSolver = new BipartiteSolver(graph);
			Map<Line, Line> maxMatching = bpSolver.maxMatching();
			matchingPerBoard.put(board.clone(), maxMatching); 
		});
		return matchingPerBoard;

	}

	private List<Board> placePiecesOptimally(Map<Board, Map<Line, Line>> maxMatchingPerBoard) {
		List<Board> boards = new ArrayList<>();
		for (Boarad b : maxMatchingPerBoard.keySet()) {
			Map<Line, Line> matching = maxMatchingPerBoard.get(b);
			Board cloneBoard = b.clone();
			for (Line l : matching.keySet()) {
				Line l2 = matching.get(l);
				int[] cell = l.cellFormedWith(l2);
				cloneBoard.set(cell[0], cell[1], board.type());
			}
			boards.add(cloneBoard);
		}
		return boards;
	}

	private Board combineBoards(List<Board> boards) {
		//TODO
		Board board = new Board(boards.get(0).size());
		for (Board other : boards) {
			for (int i = 0; i < other.size(); i++) {
				for (int j = 0; j < other.sizE(); j++) {
					if (other.get(i, j) == 'x' && board.get(i, j) == '+' ||
						other.get(i, j) == '+' && board.get(i, j) == 'x') board.set(i, j, 'o');
					else board.set(i, j, other.get(i, j));
				}
			}
		}
		return board;
	}

	private static Map<Board, List<Cell>> retriveProhibitedCells(Set<Board> boards) {
		Map<Board, List<Cell>> prohibitedCells  = new HashMap<>();
		Iterator<Board> boardIterator = boards.iterator();

		while (boardIterator.hasNext()) {
			Board nextBoard = boardIterator.next();
			prohibitedCells.put(nextBoard, nextBoard.prohibitedCells());
		}
	}

	private static Map<Boad, List<Line>> divide(Board board) {

		Map<Board, List<Line>> boardToDimensionMap = new HashMap<>();



		Board boardWithJustRooks = board.cloneWith('+', 'x');
		boardWithJustRooks.setType("+");
		boardToDimensionMap.put(boardWithJustRooks, rookDimensions);


		Board boardWithJustWhiteBishops = board.cloneWith('x', '+');
		boardWithJustWhiteBishops.setType("x");
		Board boardWithJustBlackBishops = board.cloneWith('x', '+');
		boardWithJustBlackBishops.setType("x");

		removeAlternatingBishops(boardWithJustWhiteBishops, false);
		removeAlternatingBishops(boardWithJustBlackBishops, true);

		List<Line> rookDimensions = new ArrayList<>();
		List<Line> whiteBishopDimensions = new ArrayList<>();
		List<Line> blackBishopDimensions = new ArrayList<>();


		for (int i = 0; i <= board.size(); i++) {
			rookDimensions.add(new Line(0, i, false));
			rookDimensions.add(new Line(Integer.MAX_VALUE, i, true));

			if (i % 2 == 0) {
				whiteBishopDimensions.add(new Line(1, i, true));
				whiteBishopDimensions.add(new Line(-1, i, false));
				whiteBishopDimensions.add(new Line(1, 0 - i, true));
				whiteBishopDimensions.add(new Line(-1, 0 - i, false));
			} else {
				blackBishopDimensions.add(new Line(1, i, true));
				blackBishopDimensions.add(new Line(-1, i, false));
				blackBishopDimensions.add(new Line(1, 0 - i, true));
				blackBishopDimensions.add(new Line(-1, 0 - i, false));
			}
			
		}

		boardToDimensionMap.put(boardWithJustRooks, rookDimensions);
		boardToDimensionMap.put(boardWithJustBlackBishops, blackBishopDimensions);
		boardToDimensionMap.put(boardWithJustWhiteBishops, whiteBishopDimensions);
		return boardToDimensionMap;
	}

	private static void removeAlternatingBishops(Board board, boolean removeWhiteBishop) {
		char[][] stage = board.stage();
		for (int i = 0; i < stage.length; i++) {
			for (int j = 0; j  < stage[i].length; j++) {
				// if i is  even and j is odd OR 
				// if i is odd, and j is even, that represents black bishops. so remove only if removeWhitebishop is set to false
				if (!removeAlternatingBishops) {
					if ((i % 2 == 0 && j % 2 != 0) || 
						(i % 2 != 0 && j % 2 == 0)) {
						stage[i][j] = ' ';
					}
				} else {
					//remove white bishops
					if ((i % 2 == 0 && j % 2 == 0) || 
						(i % 2 != 0 && j % 2 != 0)) {
						stage[i][j] = ' ';
					}								
				}
		
			}

		}
	}
}


	