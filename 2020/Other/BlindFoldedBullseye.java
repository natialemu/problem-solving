import java.util.*;
import java.io.*;


/**
Bull's eye


1. illegal moves:
	- out of the bounds of the wall
	- ending up out of the dart region from within the region.

if initially out of the dart region:
	continue to go wall wall length - (A) in any one of the valid directions


Part I: fixed radius, R.
Once in the region:
alternate between valid horizontal and valid vertical moves. initially start by making a move of R per direction. At each point, one of three should happen, per direction(horizontal, for ex):

1. taking R steps in either direction will result in out of bound. half num Steps to take and try again. if num steps is 1 and this still happens, then the vertical symetry line of the circle is found. try the same thing along horizontal line.
2. taking R steps in either direction will result in in bounds. increase by half the value and try again. if num steps is equal to R and both are still in bound. then the center is found. no need to try again.
3. move to the new location that is in bounds.
-

basically, finding the right num moves to take is 


three steps:
-> making sure to land in the dart region
-> once in the dart region finding the vertical symetry line
-> once the vertical symetry line is found, find the horizontal symetry line.

Part II: What if the radius is unknown. Instead, the range is what is known.
- What is my initial jump is Max of (a, b) and will slowly perform binary 


Using binary search to find the symetry line:

1. Given at some Xi, and initial stepSize = R,

i want to make a move somewhere from 0 to R.
check R/2.
 if R/2 results in both out of bounds. i wanna take smaller steps.
 next try R/4. if R/4 results in only one valid direction, move to that direction. and retry this whole process. if the binary search ends with no move made, one of two things has happened:
 	- or symetry is found


So you can have a method that takes a point, the board/circle, and dimesion,
will either return the new point to move to or will not return anything
if the current point is a symetry point.

call the method again with different dimesions.


Phase 1: 
	-if A == B
Phase 2: if A != B && Actual Solution online


What's still left:
Commenting shit out.
making it interactive
walk through example


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