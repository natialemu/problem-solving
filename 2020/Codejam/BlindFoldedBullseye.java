import java.util.*;
import java.io.*;


/**
Bull's eye

**/

public class BlindFoldedBullseye {

	// this is the size of the board 
	public static final long BOARD_SIZE = 20000000; 
	public static long circleRadius = -1L;
	public static long xcenter = -1L;
	public static long ycenter = -1L;

	public static int numThrowsToCenter(long r) {
		// change the scale so that the center is top left
		long[][] wall = new long[BOARD_SIZE][BOARD_SIZE];

		//returns some x,y co-ordinate that is inside the dart region
		long[] inDartCoordinates = findDartRange(wall, r);

		long[] leftEdgePoint = binarySearch(0, inDartCoordinates[0], inDartCoordinates[1], true);
		long[] rightEdgePoint = binarySearch(inDartCoordinates[0], BOARD_SIZE, inDartCoordinates[1], true);


		long[] upperEdgePoint = binarySearch(0, inDartCoordinates[1], inDartCoordinates[0], false);

		long[] lowerEdgePoint = binarySearch(inDartCoordinates[1], BOARD_SIZE, inDartCoordinates[0], false);

		long[] circleCenter = new long[] {(leftEdgePoint[0] + rightEdgePoint[0])/2, (upperEdgePoint[1] + lowerEdgePoint[1])/2};
		return circleCenter;
		
	}

	private static long[] binarySearch(long low, long high, long other, boolean horizontal) {
		while (high > low) {
			long mid = (high + 2) / 2;
			if (horizontal) { // movement is horizontal
				throwAt(mid, other);
				if (isInRange(mid, other)) {
					high = mid;
				} else {
					low = mid;
				}
			} else {
				throwAt(other, mid);
				if (isInRange(other, mid)) {
					high = mid;
				} else {
					low = mid;
				}
			}
		}
		return horitontal ? new long[] {high, other} : new long[] {other, high};
	}

	private static long[] findDartRange(long[][] wall, long radius) {
		long initialX = radius - 1;
		long initialY = radius;

		for (int x = initialX; x < wall.length; x += (radius - 1) ) {
			for (int y = initialY; y < wall.length; y += radius) {
				throwAt(x, y);
				if (isInRange(x, y)) return new long[] {x, y};
			}
		}

		return new long[] {-1, -1};

	}
	private static void throwAt(long x, long y) {
		System.out.println("Dart thrown at: ["+x+","+y+"]");
	}

	
	private static boolean isInRange(long x, long y) {
		int val = (x - xcenter)*(x - xcenter) + (y - ycenter)*(y - ycenter);
		return val < circleRadius*circleRadius;
	}

	public static void main(String[] args) {
		Scanner in = new Scanner(new BufferedReader(new InputStreamReader(System.in)));
        int t = in.nextInt(); 
        in.nextLine();
        for (int i = 1; i <= t; ++i) {
        	String[] s = in.nextLine().split(" ");
        	xcenter = Long.parseLong(s[0]);
        	ycenter = Long.parseLong(s[1]);
            circleRadius = in.nextLong();
            in.nextLine();
            int numThrows = numThrowsToCenter(circleRadius);
            System.out.println("Case #" + i + ": " + (numThrows));
        }
	}
}