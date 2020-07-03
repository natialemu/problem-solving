


interface Graph<T> {
	//Behaviors of the graph

}
class UndirectedGraph implements Graph<T> {
	Map<T, List<T>> adjList;
	Map<T, Boolean> colorOf;



	@Override
	public void addVertex(T u) {
		//TODO
	}

	@Override
	public int size() {

	}

	@Override
	public Map<T, Boolean> V() {
		// return all vertices along with their color
		return colorOf;

	}

	public List<T> adj(T u) {

	}


	@Override
	public void addEdge(T u, T v) {

	}
}

class Board {
	//Define the behaviors of the board
	char[][] stage;
	String type;

	public Board(char[][] stage) {
		this.stage = stage;
		this.type = "GENERAL_BOARD";
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

	}

	public char[][] stage() {


	}

	public List<Cell> prohibitedCells() {

	}

	public void setType(String type)  {
		this.type = type;
	}
}

class Line {
	// fields: m, b
	//behavior: int[] cell(Line other)
	int slope;
	int b;
	int xIntercept;

	public Line(int slope, int b) {
		this.slope = slope;
		this.b = b;
	}

	public int[] cellFormedWith(Line other) {
		//this logic only applies for the use case of this problem
		if (slope == Integer.MAX_VALUE && other.slope == 0) return new int[] {xIntercept, other.b};
		if (slope == 0 && other.slope == Integer.MAX_VALUE) return new int[] {other.xIntercept, b};

		int x = (int) ((b - other.b) / (slope - other.slope));
		int y = sope*x  + b;
		return new int[] {x, y};
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

class BipartiteSolver<T> {
	Graph<T> bipartiteGraph;
	Map<T,T> mate; // matching.get()


	public Map<T,T> maxMatching() {
		//TODO
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

		Map<Board, Map<Line, Line> maxMatchingsPerBoard = findMaximalMatchingInParallel(graphPerBoard);

		List<Board> optimallyPlacedBoards = placePiecesOptimally(maxMatchingsPerBoard);

		Board finalBoard = combineBoards(optimallyPlacedBoards);

		return finalBoard;
	}

	private Map<Board, Graph<Line>>buildAndPopuLateGraph(Map<Board, List<Line>> boardToDimensionMap, Map<Board, List<Cell> prohibitedCells) {

	}

	private Map<Board, Map<Line, Line>> findMaximalMatchingInParallel(Map<Board, Graph<Line>> biGraphPerBoard) {
		//TODO
	}

	private List<Board> placePiecesOptimally(Map<Board, Map<Line, Line>> maxMatchingPerBoard) {
		//TODO
	}

	private Board combineBoards(List<Board> boards) {
		//TODO
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
		boardWithJustRooks.setType("ROOKS");
		boardToDimensionMap.put(boardWithJustRooks, rookDimensions);


		Board boardWithJustWhiteBishops = board.cloneWith('x', '+');
		boardWithJustWhiteBishops.setType("WHITE_BISHOPS");
		Board boardWithJustBlackBishops = board.cloneWith('x', '+');
		boardWithJustBlackBishops.setType("BLACK_BISHOPS");

		removeAlternatingBishops(boardWithJustWhiteBishops, false);
		removeAlternatingBishops(boardWithJustBlackBishops, true);

		List<Line> rookDimensions = new ArrayList<>();
		List<Line> whiteBishopDimensions = new ArrayList<>();
		List<Line> blackBishopDimensions = new ArrayList<>();


		for (int i = 0; i <= board.size(); i++) {
			rookDimensions.add(new Line(0, i));
			rookDimensions.add(new Line(Integer.MAX_VALUE, i));

			if (i % 2 == 0) {
				whiteBishopDimensions.add(new Line(1, i));
				whiteBishopDimensions.add(new Line(-1, i));
				whiteBishopDimensions.add(new Line(1, 0 - i));
				whiteBishopDimensions.add(new Line(-1, 0 - i));
			} else {
				blackBishopDimensions.add(new Line(1, i));
				blackBishopDimensions.add(new Line(-1, i));
				blackBishopDimensions.add(new Line(1, 0 - i));
				blackBishopDimensions.add(new Line(-1, 0 - i));
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


	