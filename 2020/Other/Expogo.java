
import java.util.*;
import java.io.*;


class Move {
	int x;
	int y;
	int jump;
	String direction;

	public Move(int x, int y, String direction) {
		this.x = x;
		this.y = y;
		this.direction = direction;
	}
	public Move(int x, int y, String direction, int jump) {
		this.x = x;
		this.y = y;
		this.direction = direction;
		this.jump = jump;
	}
	@Override
	public boolean equals(Object o) {
		Move other = (Move) o;
		return Objects.equals(x, other.x) && Objects.equals(y, other.y)  && Objects.equals(jump, other.jump);
	}
	@Override
	public int hashCode() {
		return Objects.hash(x, y, jump);
	}
}
/**
Which of the algorithms below would work on both first and second test cases is the problem is bounded by 100?

which algorithm perform better when there is a cyle?
What issues will come up if a vertex's uniqueness is only considered based on x and y values and not i?
**/
public class Expogo {

	public static String isReachable(int xTarget, int yTarget) {
		if (xTarget == 0 && yTarget == 0) {
			return new String();
		}
		String[] solution = isReachable(0, 0, xTarget, yTarget, 1, new HashMap<>());
		return solution[1];
	}

	private static String[] isReachable(int currX, int currY, int xTarget, int yTarget, int i,
											 Map<Integer, Map<Integer, Map<Integer, String[]>>> memo) {
		if (currX == xTarget && currY == yTarget) {
			return new String[] {"0", ""};
		}

		if (Math.abs(currX) > 100 ||
			Math.abs(currY) > 100) {
			return new String[] {Integer.toString(Integer.MAX_VALUE), "IMPOSSIBLE"};
		}

		int currMinJump = Integer.MAX_VALUE;
		String newDirection = "IMPOSSIBLE";
		for (Move move : nextMoves(currX, currY, i)) {
			String[] subProblemSoln = memo.containsKey(move.x) && memo.get(move.x).containsKey(move.y) && memo.get(move.x).get(move.y).containsKey(i + 1)  ?
			 memo.get(move.x).get(move.y).get(i + 1) : isReachable(move.x, move.y, xTarget, yTarget, i + 1, memo);
			if (!subProblemSoln[1].equals("IMPOSSIBLE")) {
				Integer minJumpSubproblem = Integer.parseInt(subProblemSoln[0]);
				newDirection = minJumpSubproblem + 1 < currMinJump ? move.direction + subProblemSoln[1] : newDirection;
				currMinJump = Math.min(minJumpSubproblem + 1, currMinJump);
			}		
		}

		String[] currentSolution = new String[] {Integer.toString(currMinJump), newDirection};
		Map<Integer, String[]> iToSolutionMap = new HashMap<>();
		iToSolutionMap.put(i, currentSolution);
		Map<Integer, Map<Integer, String[]>> yToSolutionMap = new HashMap<>();
		yToSolutionMap.put(currY, iToSolutionMap);
		memo.put(currX, yToSolutionMap);
		return currentSolution;
	}
	private static String isReachableBFS(int xTarget, int yTarget) {
		/**
		If there is a cycle in the graph, how does BFS behave?
		**/
		if (xTarget == 0 && yTarget == 0) {
			return new String();
		}

		Queue<Move> bfs = new LinkedList<>();
		bfs.add(new Move(0, 0, "", 0));
		Map<Move, Move> edgeTo = new HashMap<>(); // how did i get to current loc and what direction did i take
		int i = 1;
		Move target = null;
		while (!bfs.isEmpty() && target == null) {
			int queuSize = bfs.size();
			for (int j = 0; j < queuSize && target == null; j++ ){
				Move currLoc = bfs.poll();
				for (Move move : nextMoves(currLoc.x, currLoc.y, i)) {
					move.jump = i;
					
					if (move.x == xTarget && move.y == yTarget) {
						target = move;
						edgeTo.put(move, currLoc);
						break;
					}
					if (Math.abs(move.x) < 100 && Math.abs(move.y) < 100) {
						edgeTo.put(move, currLoc);
						bfs.add(move);
					}
				}
			}
			i += 1;
		}
		if (target != null) {
			String dxnTaken = getDxn(edgeTo, target);
			return dxnTaken;
		}
		return "IMPOSSIBLE";
	}
	private static String getDxn(Map<Move, Move> edgeTo, Move target) {
		StringBuilder pathBuilder = new StringBuilder();
		Move runner = target;
		pathBuilder.append(runner.direction);
		int i =0;
		// System.out.print("(" + target.x + ", " + target.y + ") --> ");
		while(runner.x != 0 || runner.y != 0) {
			runner = edgeTo.get(runner);
			pathBuilder.append(runner.direction);
		}
		return pathBuilder.reverse().toString();
	}
	private static List<Move> nextMoves(int currX, int currY, int i) {
		int jump =  (int)Math.pow(2, i - 1);
		Move nextMoveEast = new Move(currX + jump, currY, "E"); // move east
		Move nextMoveNorth = new Move(currX, currY + jump, "N"); // move north
		Move nextMoveWest = new Move(currX - jump, currY, "W"); // move west
		Move nextMoveSouth = new Move(currX, currY - jump, "S"); // move south
		return Arrays.asList(nextMoveEast, nextMoveSouth, nextMoveWest, nextMoveNorth);
	}

	public static void main(String[] args) {
		Scanner in = new Scanner(new BufferedReader(new InputStreamReader(System.in)));
        int t = in.nextInt(); 
        in.nextLine();
        for (int i = 1; i <= t; ++i) {
          String[] targets = in.nextLine().split(" ");
          String sol = isReachableBFS(Integer.parseInt(targets[0]), Integer.parseInt(targets[1]));

          System.out.println("Case #" + i + ": " + (sol));
      }
	}
	

}