import java.util.*;
import java.io.*;





/**
Problem is https://leetcode.com/problems/design-excel-sum-formula/
**/
interface ExcelSum {
	//validation on the row and the columns. throw validation exception
	// failure could result in some exception being thrown
	void set(int row, char column, int value);
	// same validation should be done
	int get(int row, char column);
	//numbers.size() == 1 ? ColRow : ColRowi:ColRow+1:ColRow+2...
	int sum(int row, char column, List<String> numbers);
}

class QuadNode {

	// the region represented by this quad tree
	int x1, x2;
	char y1, y2;

	// the sum of all the cells within the region represented by this QuadNode
	int sum;

	QuadNode[] children; // of size 4

	public QuadNode(int sum, int x1, char y1, int x2, char y2) {
		this.sum = sum;
		this.y1 = y1;
		this.y2 = y2;
		this.x1 = x1;
		this.x2 = x2;
		children = new QuadNode[4];
	}

	/**
	Given a rectangular region, it checks if that rectangular region overlaps with this Node's region
	**/
	public boolean overlaps(int x1, char y1, int x2, char y2, String debug) {		
		boolean result = false;
		
		// the column divides the region into left and right region. So it's analogous to the x coordinate in a
		// cartesian coordiante system. While the row(x1 & x2) are analogus to that of the y-coorindates.

		if (this.y1 <= y1) {
			result = y1 <= this.y2 && ((x1 <= this.x2 && x1 >= this.x1) || 
									   (x2 <= this.x2 && x2 >= this.x1));
		} else {
			result = this.y1 <= y2 && ((this.x2 <= x2 && this.x2 >= x1) ||
									   (this.x1 <= x2 && this.x1 >= x1)); 
		}
		return result;
	}

	/**
	Given a rectangular region, returns the overlapping rectangle between the input region and the Node's region
	**/
	public List<String> overlappingRegion(int x1, char y1, int x2, char y2) {
		List<String> overlappingRegion = new ArrayList<>();
		if (this.x1 <= x2) {	
			overlappingRegion.addAll(Arrays.asList("" + Math.max(x1, this.x1), ""+ ((char)Math.max(y1, this.y1)), "" + Math.min(x2, this.x2),  ""+((char)Math.min(y2, this.y2) )));
		} else {
			overlappingRegion.addAll(Arrays.asList(""+Math.min(x1, this.x1), ""+((char)Math.min(y1, this.y1)), ""+Math.max(x2, this.x2), "" +((char)Math.max(y2, this.y2) )));
		}
		return overlappingRegion;
	}
	
}

class ExcelSumImpl implements ExcelSum {
	QuadNode root;

	public ExcelSumImpl(int rowMax, char colMax) {
		root = buildTree(root, 1, rowMax, 'A', colMax, "");
	}

	/*
	*	Builds a quad tree that represents the dimensions of the excel
	**/	
	private QuadNode buildTree(QuadNode current, int startRow, int endRow, char startCol, char endCol, String debug) {

		// create a new quad node if there's not a region to represnt the current region
		if (current == null) {
			current = new QuadNode(0, startRow, startCol, endRow, endCol);
		}

		// base case is a region that represents only a single cell.
		if (startRow == endRow && startCol == endCol) {
			return current;
		}
		// There's no region where startRow > endRow or startCol is greater than endCol. This could happen
		// in situations where the dimensions of row and col aren't the same.
		if (startRow > endRow || startCol > endCol) {
			return null;
		}

		// generate four new regions based on the mid point of the current region
		int rowMid = (startRow + endRow) / 2;
		char colMid = (char)((startCol + endCol) / 2);
		// the first child node will be responsible for the new region in Quadrant I
		current.children[0] = buildTree(current.children[0], startRow, rowMid, (char)(colMid + 1), endCol, debug + "	");
		// the second child node will be responsible for the new region in Quadrant II
		current.children[1] = buildTree(current.children[1], startRow, rowMid, startCol, colMid, debug + "	 ");
		// the third child node will be responsible for the new region in Quadrant III
		current.children[2] = buildTree(current.children[2], rowMid + 1, endRow, startCol, colMid, debug + "	");
		// the fourth child node will be responsible for the new region in Quadrant IV
		current.children[3] = buildTree(current.children[3], rowMid + 1, endRow, (char)(colMid + 1), endCol, debug + "	 ");

		//after sub quad tree representing the current region is built, return the current quad node
		return current;
	}


	@Override
	public void set(int row, char column, int value) {
		if (root != null) {
			// update the value of the root quad node with the difference
			// between the new value and the old value at (row, col)
			root.sum += set(root, row, column, value);
		}
	}

	//returns the difference between the old value and  the new value
	private int set(QuadNode current, int row, char col, int value) {

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
		int rowMid = (current.x1 + current.x2) / 2;
		char colMid = (char)((current.y1 + current.y2) / 2);

		if (row <= rowMid && col > colMid) {
			int diff  = set(current.children[0], row, col, value);
			current.sum += diff;
			return diff;
		} else if (row <= rowMid && col <= colMid) {
			int diff  = set(current.children[1], row, col, value);
			current.sum += diff;
			return diff;
		} else if (row > rowMid && col <= colMid) {
			int diff  = set(current.children[2], row, col, value);
			current.sum += diff;
			return diff;
		} else {
			int diff  = set(current.children[3], row, col, value);
			current.sum += diff;
			return diff;
		}

	}

	@Override
	public int get(int row, char column) {
		return get(root, row, column);
	}
	public int get(QuadNode current, int row, char col) {
			/**
		While traversing down the quad tree, if a leave node that represents the cell is reached,
		return the value that's in that cell
		**/
		if (current.children[0] == null && current.children[1] == null && current.children[2] == null & current.children[3] == null) {
			return current.sum;
		}
		// divide and recurse. should depend on curr
		int rowMid = (current.x1 + current.x2) / 2;
		char colMid = (char)((current.y1 + current.y2) / 2);
		if (row <= rowMid && col > colMid) {
			return get(current.children[0], row, col);
		} else if (row <= rowMid && col <= colMid) {
			return get(current.children[1], row, col);
		} else if (row > rowMid && col <= colMid) {
			return get(current.children[2], row, col);
		} else {
			return get(current.children[3], row, col);
		}
	}

	@Override
	public int sum(int row, char column, List<String> numbers) {
		// parse the input to get all the regions to sum over
		List<List<String>> regions = parseRegion(numbers);
		int allSumRegions = 0;
		for (List<String> region : regions) {
			// find the sum of the region
			int regionSum = sum(root, Integer.parseInt(region.get(0)), region.get(1).charAt(0), Integer.parseInt(region.get(2)), region.get(3).charAt(0), "");
			allSumRegions += regionSum;
		}
		set(row, column, allSumRegions);
		
		return allSumRegions;
	}

	private List<List<String>> parseRegion(List<String> numbers) {
		List<List<String>> regions = new ArrayList<>();
		for (String number : numbers) {
			String[] query = number.split(":");
			if (query.length == 1) {
				regions.add(Arrays.asList(query[0].substring(1), query[0].substring(0, 1), query[0].substring(1), query[0].substring(0, 1)));
			} else {
				regions.add(Arrays.asList(query[0].substring(1), query[0].substring(0, 1), query[1].substring(1), query[1].substring(0, 1)));
			}
		}
		return regions;
	}

	private int sum(QuadNode current, int x1, char y1, int x2, char y2, String debug) {

		// if the current node's region exactly matches the query region, return the sum stored in the quad node for that region
		if (current.x1 == x1 && current.x2 == x2 && current.y1 == y1 && current.y2 == y2) {
			return current.sum;
		}

		int overallSum = 0;
		/**
		- Check if the query region overlaps with any of the four sub regions reprsented by the four children of the current node. 
		- Then find the corresponding overlapping region betwen the query region & the sub regions, and find the sum of each overlapping region. 
		- Then add all the sums up and return it
		**/
		if (current.children[0] != null && current.children[0].overlaps(x1, y1, x2, y2, debug)) {
			List<String> overlap = current.children[0].overlappingRegion(x1, y1, x2, y2);
			overallSum += sum(current.children[0], Integer.parseInt(overlap.get(0)), overlap.get(1).charAt(0), Integer.parseInt(overlap.get(2)), overlap.get(3).charAt(0), debug + "	");
		}
		if (current.children[1] != null && current.children[1].overlaps(x1, y1, x2, y2, debug)) {
			List<String> overlap = current.children[1].overlappingRegion(x1, y1, x2, y2);
			overallSum += sum(current.children[1], Integer.parseInt(overlap.get(0)), overlap.get(1).charAt(0), Integer.parseInt(overlap.get(2)), overlap.get(3).charAt(0), debug + "	");
		}

		if (current.children[2] != null && current.children[2].overlaps(x1, y1, x2, y2, debug)) {
			List<String> overlap = current.children[2].overlappingRegion(x1, y1, x2, y2);
			overallSum += sum(current.children[2], Integer.parseInt(overlap.get(0)), overlap.get(1).charAt(0), Integer.parseInt(overlap.get(2)), overlap.get(3).charAt(0), debug + "	");
		}

		if (current.children[3] != null && current.children[3].overlaps(x1, y1, x2, y2, debug)) {
			List<String> overlap = current.children[3].overlappingRegion(x1, y1, x2, y2);
			overallSum += sum(current.children[3], Integer.parseInt(overlap.get(0)), overlap.get(1).charAt(0), Integer.parseInt(overlap.get(2)), overlap.get(3).charAt(0), debug + "	");
		}
		return overallSum;
	}
}

public class ExcelApplication {
	public static void main(String[] args) {

		ExcelSum excel = new ExcelSumImpl(3, 'C');
		excel.set(1, 'A', 2);
		System.out.println("Set A1 to 2");
		int val = excel.get(1, 'A');
		System.out.println("A1: " +val);
		excel.sum(3, 'C', Arrays.asList("A1"));
		System.out.println("Found sum at A1 & set to C3");
		System.out.println("A1: " +excel.get(1, 'A'));
		System.out.println("3C: " + excel.get(3, 'C'));
		val = excel.get(2, 'B');
		System.out.println("2B: " + val);
		excel.set(2, 'B', 4);
		System.out.println("Set 2B to 4");
		val = excel.get(2, 'B');
		excel.set(3, 'B', 8);
		excel.set(2, 'C', 4);
		excel.sum(1, 'A', Arrays.asList("B2:C3"));
		System.out.println("A1: " + excel.get(1, 'A'));


	}
}