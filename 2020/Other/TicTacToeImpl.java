import java.util.*;
/**
- Tic Tac Toe Design:

what is it?

- A game of tic tac toe played between two players
- you need to represent the grid somehow
- you need to efficiently determine a winning condition

some behaviors of the game:
-> placing a value in a cell
-> checking if the move resulted in a win. If placed at i,j
      -> is the ith row full of the current value? should be constant time
      -> is the jth row full of current value? should be constant time
      -> if the value along the two diagonals? should also be constant time

 there are two players. so we can add more maps. 
 Map<Player, Map<Integer, Integer>> -> the number of values set by the player for each row
 	- when placing a value, update this 
 Map<Player, Map<Integer, Integer>> -> the number of values set by the player for each column


  Map<Player, Map<Integer, Integer>> -> the number of values set by the player for each of the two diagonals starting at row i.



 how about the diagonals?
  - if the current value is along the diagnoal

**/
enum Player {
	FIRST_PLAYER(true),
	SECOND_PLAYER(false);

	private boolean value;
	Player(boolean value) {this.value = value;}
	public boolean getValue() {return value;}


}
interface TicTacToe {
	boolean move(int row, int col, Player player);
	void addPlayer(Player player);
}

public class TicTacToeImpl implements TicTacToe {
	boolean[][] grid;
	Map<Player, Map<Integer, Integer>> rowCountMap;
	Map<Player, Map<Integer, Integer>> colCountMap;
	Map<Player, Map<Integer, Integer>> diagonalCountMap;

	public TicTacToeImpl(int size) {
		grid = new boolean[size][size];
		rowCountMap = new HashMap<>();
		colCountMap = new HashMap<>();
		diagonalCountMap = new HashMap<>();
	}
	@Override
	public boolean move(int row, int col, Player player) {
		if (rowCountMap.size() < 2) throw new IllegalArgumentException("Not enough players have registers.");
		grid[row][col] = player.getValue();
		updatePlayerCount(row, col, player);
		return playerWon(row, col, player);
	}

	private boolean playerWon(int row, int col, Player player) {
		return rowCountMap.get(player).get(row) == grid.length ||
			   colCountMap.get(player).get(col) == grid.length ||
			   diagonalCountMap.get(player).get(row) == grid.length;
	}

	@Override
	public void addPlayer(Player player) {
		if (rowCountMap.size() == 2) throw new IllegalArgumentException("Only two players allowed");
		rowCountMap.put(player, new HashMap<>());
		colCountMap.put(player, new HashMap<>());
		diagonalCountMap.put(player, new HashMap<>());
	}

	private void updatePlayerCount(int row, int col, Player player) {
		Map<Integer, Integer> playerRowCount = rowCountMap.get(player);
		Map<Integer, Integer> playerColCount = colCountMap.get(player);
		Map<Integer, Integer> playerdiagonalCount = diagonalCountMap.get(player);

		playerRowCount.put(row, playerRowCount.getOrDefault(row, 0) + 1);
		playerColCount.put(col, playerColCount.getOrDefault(col, 0) + 1);
		if (row == col) playerdiagonalCount.put(row, playerdiagonalCount.getOrDefault(row, 0) + 1);
		if (col == (0-row) + (grid.length - 1)) playerdiagonalCount.put(row, playerdiagonalCount.getOrDefault(row, 0) + 1);

	}

	public static void main(String[] args) {
		TicTacToeImpl tictactoe = new TicTacToeImpl(3);

		//add players
		tictactoe.addPlayer(Player.FIRST_PLAYER);
		tictactoe.addPlayer(Player.SECOND_PLAYER);

		boolean status = tictactoe.move(0, 0, Player.FIRST_PLAYER);
		System.out.println(status);
		status = tictactoe.move(0, 2, Player.SECOND_PLAYER);
		System.out.println(status);
		status = tictactoe.move(2, 2, Player.FIRST_PLAYER);
		System.out.println(status);
		status = tictactoe.move(1, 1, Player.SECOND_PLAYER);
		System.out.println(status);
		status = tictactoe.move(2, 0, Player.FIRST_PLAYER);
		System.out.println(status);
		status = tictactoe.move(1, 0, Player.SECOND_PLAYER);
		System.out.println(status);
		status = tictactoe.move(2, 1, Player.FIRST_PLAYER);
		System.out.println(status);

	}

}
