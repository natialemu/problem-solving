
import java.util.*;
import java.io.*;

class ExpogoV2 {
	//walk through this algorithm for non Q1 targets.
	public static String isReachable(long xTarget, long yTarget) {
		//if the sum of the targets is not odd, then there is no solution.
		// this is because the first move is the only odd move. everything else is even(1,2,4,8...)
		if ((xTarget + yTarget) % 2 == 0) {
			return new String("IMPOSSIBLE");
		}
		// after making a single move, both X and y should be even
		// otherwise since each move after the first one is even, there is no way of getting to the target point
		// so the fist move should be in the direction of the co-ordinate that is odd to even it out.
		if (xTarget % 2 != 0) { // is odd. this is the direction where the first move should be made
			
			yTarget = yTarget / 2L;

			// if shifting the points by 1 along the horizontal axis and scaling down by 2(since all subsequent moves are multiples of 2
		    //and so are the target points after being sifted) results in new target point of 0,0 then the base case is reached.
			if (yTarget == 0L && xTarget + 1L == 0) {
				return "W";
			}

			if (yTarget == 0L && xTarget - 1L == 0) {
				return "E";
			}

			// the new xTarget must be one that makes the sum of the new targets(xTarget + yTarget) odd.
			String dxnToTake = (yTarget + (xTarget + 1L)/2L) % 2L == 0 ?  "E" : "W";
			xTarget = (yTarget + (xTarget + 1L)/2L) % 2 == 0 ? (xTarget - 1L) / 2L : (xTarget + 1L)/2L ;
			return dxnToTake + isReachable(xTarget, yTarget);
		} else {
			//same reasoning as above except the shift by 1 is going to be along the vertical direction since yTarget is odd.
			xTarget = xTarget / 2L;

			if (xTarget == 0 && yTarget + 1L == 0L) {
				return "S";
			}

			if (xTarget == 0 && yTarget - 1L == 0L) {
				return "N";
			}
			String dxnToTake = (xTarget + (yTarget + 1L)/2L) % 2 == 0 ? "N" : "S";
			yTarget = (xTarget + (yTarget + 1L)/2L) % 2 == 0 ? (yTarget - 1L) / 2L : (yTarget + 1L)/2L ;
			return dxnToTake + isReachable(xTarget, yTarget);
		}
	}

	public static void main(String[] args) {
		Scanner in = new Scanner(new BufferedReader(new InputStreamReader(System.in)));
        int t = in.nextInt(); 
        in.nextLine();
        for (int i = 1; i <= t; ++i) {
          String[] targets = in.nextLine().split(" ");
          String sol = isReachable(Long.parseLong(targets[0]), Long.parseLong(targets[1]));
          System.out.println("Case #" + i + ": " + (sol));
      }
	}
	
}