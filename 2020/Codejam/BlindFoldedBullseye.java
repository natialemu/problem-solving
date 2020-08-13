import java.util.*;
import java.io.*;


/**
Bull's eye

**/

public class BlindFoldedBullseye {

	// this is the size of the board 
	public static final long BOARD_SIZE = 2000000000; 

	public static int numThrowsToCenter(long A, long B) {
		//Assume A == B for 
		// change the scale so that the center is top left
		long[][] wall = new long[BOARD_SIZE][BOARD_SIZE];

		//returns some x,y co-ordinate that is inside the dart region
		long[] inDartCoordinates = findDartRange(wall);

		long[] leftEdgePoint = binarySearch(0, inDartCoordinates[0], inDartCoordinates[1], true, wall);
		long[] rightEdgePoint = binarySearch(inDartCoordinates[0], BOARD_SIZE, inDartCoordinates[1], true, wall);


		long[] upperEdgePoint = binarySearch(0, inDartCoordinates[1], inDartCoordinates[0], false, wall);

		long[] lowerEdgePoint = binarySearch(inDartCoordinates[1], BOARD_SIZE, inDartCoordinates[0], false, wall);

		long[] circleCenter = new long[] {(leftEdgePoint[0] + rightEdgePoint[0])/2, (upperEdgePoint[1] + lowerEdgePoint[1])/2};
		return circleCenter;
		
	}

	private static long[] binarySearch(long low, long high, long other, boolean horizontal, long[][] wall) {
		while (high > low) {
			long mid = (high + 2) / 2;
			if (horizontal) { // movement is horizontal
				if (isInRange(mid, other, wall)) {
					high = mid;
				} else {
					low = mid;
				}
			} else {
				if (isInRange(other, mid, wall)) {
					high = mid;
				} else {
					low = mid;
				}
			}
		}
		return horitontal ? new long[] {high, other} : new long[] {other, high};
	}

	private static long[] findDartRange(long[][] wall, int A) {
		long initialX = 0;
		long initialY = 0;

		for (int x = initialX; x < wall.length; x += (A - 1) ) {
			for (int y = initialY; y < wall.length; y += A) {
				if (isInRange(x, y, wall)) return new long[] {x, y};
			}
		}

		return new long[] {-1, -1};

	}
	private static long transform(long x) {

	}

	
	private static boolean isInRange(long x, long y, wall) {
		// given two points, wil return true if the points are in the circle. false otherwise
	}

	public static void main(String[] args) {

	}
	
}