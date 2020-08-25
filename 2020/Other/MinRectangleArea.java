import java.util.*;
import java.io.*;
public class MinRectangleArea {
	class Point {
		int x, y;
		public Point(int x, int y) {
			this.x = x;
			this.y = y;
		}

	}

	class Points {

		List<Point> points = new ArrayList<>();
		int area = Integer.MAX_VALUE;
		int numPointsInX, numPointsInY = 0;


		public void add(Point p) {
			//add point if valid. valid if:
			points.add(p);
			if (points.size() == 4) computeArea();

		}

		private void computeArea() {
			//TODO
		}

		public boolean canAddPoint(Point p) {
			/**
			- no points already or only 1 point, means just add the point
			- if there is only one point, you can add it anywhere
			- if there are two points:
					- if they share a co-orindate: third point can be anywere along the other co-orindate from the two points
					- if they don't share a co-ordinate: there are only two valid solutions
			- if there are three points: There is only one unique point that is valid
			**/
			if (points.size() == 2 && numPointsInY == numPointsInX) { // don't share co-ordinate

				return (p.x == points.get(0).x && p.y == points.get(1).y ) ||
						(p.x == points.get(0).y && p.y == points.get(1).x);


			} else if (points.size() == 2 && numPointsInY != numPointsInX) { // share co-ordinate
				//TODO
			} else if (points.size() == 3) {
				// take a point from each dimension
				// out of the two valid points, the one that's not in the list of points.
			}

		}

		public Point clone() {

		}

		public int size() {
			return points.size();

		}

		public int area() {
			return area;

		}

		//Override equals, hashcode

	}
	
	public static int minAreaRect(int[][] points) {
		//Parse points
		Point[] allPoints = new Point[points.length];
		for (int i = 0; i < points.length; i++) allPoints[i] = new Point(points[i][0], points[i][1]);

		Pionts soln = new Points();
		return minAreaRect(allPoints, soln, 0);
		
	}

	private static int minAreaRect(Point[] points, Point subset, int curr) {
		// if less than four points are left, return 
		if (points.size() >= 4) {
			return subset.area();
		}

		if (curr == points.length) return Integer.MAX_VALUE;

		// point doesn't go into the sub set
		int soln1 = minAreaRect(points, subset, curr + 1, memo);

		int soln2 = Integer.MAX_VALUE;
		if (subset.canAddPoint(points[curr])) {
			Points cloneSubset = subset.clone();
			cloneSubset.add(points[curr]);
		    soln2 = minAreaRect(points, cloneSubset, curr + 1, memo);
		}
		
		return Math.min(soln1, soln2);
	}
}