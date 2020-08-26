
import java.util.*;
import java.io.*;
/**

Problem description: https://open.kattis.com/problems/officespace


**/

	/**
	A class that encapsulates employee cubicles in the office space
	**/
	class Rectangle {

		// (x1, y1) is the bottom left co-ordinate of a the rectangle(cubicle)
		//(x2, y2) is the top right co-cordinate of the rectangle(cubicle)
		int x1, y1, x2, y2;
		public Rectangle(int x1, int y1, int x2, int y2) {
			this.x1 = x1;
			this.x2 = x2;
			this.y1 = y1;
			this.y2 = y2;
		}

		public int area() {
			return (x2 - x1)* (y2 - y1);
		}

		public boolean overlaps(Rectangle other) {
			//this rectangle has x1 < other.x1
			if (x1 > other.x1) throw new IllegalArgumentException("Input rectangle must come later in the x axis than this rectangle ");

			return other.x1 >= x1 && other.x1 < x2 && (
				(other.y2 < y2 && other.y2 > y1) ||
				(other.y1 > y1 && other.y1 < y2)
				);

		}

		public Rectangle overlappingRegion(Rectangle other) {
			if (!overlaps(other)) return null;

			int newY1 = Math.max(y1, other.y1);
			int newX1 = Math.max(x1, other.x1);
			int newX2 = Math.min(x2, other.x2);
			int newY2 = Math.min(y2, other.y2);
			return new Rectangle(newX1, newY1, newX2, newY2);

		}

		@Override
		public boolean equals(Object o) {
			Rectangle other = (Rectangle) o;
			return Objects.equals(x1, other.x1) && Objects.equals(x2, other.x2)
			&& Objects.equals(y1, other.y1) && Objects.equals(y2, other.y2);
		}

		@Override
		public int hashCode() {
			return Objects.hash(x1, y1, x2, y2);
		}

	}

public class OfficeSpace {

	public static void printOfficeStats(List<String[]> officeSpace, int h, int w) {

		// each employee is mapped to the cubicle they requested.
		Map<String, Rectangle> employeeToCubicleMap = new LinkedHashMap<>();
		parse(officeSpace, employeeToCubicleMap);

		//Each cubicle, x, is mapped to a list of any other cubicle formed
		// if x overlaps with any other cubicle(including cubicals formed due to overlappings)
		Map<Rectangle, List<Rectangle>> overlappingCubiclesMap = new HashMap<>();

		//to filter out any duplicate overlapping cubicals formed
		Set<Rectangle> duplicates = new HashSet<>();

		//priority of this min heap is based on x2
		PriorityQueue<Rectangle> minHeapRight = new PriorityQueue<>((a,b) -> (a.x2 - b.x2));

		//priority of this min heap is based on x1
		PriorityQueue<Rectangle> minHeapLeft = new PriorityQueue<>((a,b) -> (a.x1 - b.x1));


		//insert all cubicles
		employeeToCubicleMap.forEach((employee, rectangle) -> minHeapLeft.add(rectangle));

		while (!minHeapLeft.isEmpty()) {
			Rectangle current = minHeapLeft.poll();

			/**
			find the first cubicle seen so far that overlaps with current
			**/
			while (!minHeapRight.isEmpty() && !current.overlaps(minHeapRight.peek())) minHeapRight.poll();

			Stack<Rectangle> temp = new Stack<>();
			/*
			if there is any cubicle seen so far that overlaps with current, 
				- find the intersection and add it back to the minHeapLeft pq. this is because it may overlap with other cubicles as well.
				- try to find other cubicles that overlap with current.
			*/
			while (!minHeapRight.isEmpty() && current.overlaps(minHeapRight.peek())) {
				Rectangle top = minHeapRight.poll();
				temp.push(top);

				Rectangle overlapping = current.overlappingRegion(top);
				minHeapLeft.add(overlapping);
				duplicates.add(overlapping);
				List<Rectangle> overlapRectangles = overlappingCubiclesMap.getOrDefault(current, new ArrayList<>());

				overlapRectangles.add(overlapping); 
				overlappingCubiclesMap.put(current, overlapRectangles);
			}
			while (!temp.isEmpty()) minHeapRight.add(temp.pop());
		}

		System.out.println("Total " + (h*w));
		// Map's each employee to the list of unavailable spaces within their requested cubicle
		Map<String, List<Rectangle>> employeeToContenstedSpaceMap = new LinkedHashMap<>();
		employeeToCubicleMap.forEach((employee, rectangle) -> {
			employeeToContenstedSpaceMap.put(employee, disjointRectanglesOverlappingWith(rectangle, overlappingCubiclesMap));
		});

		int totalContestedSpace = 0;
		for (String employee : employeeToContenstedSpaceMap.keySet()) {
			List<Rectangle> spaces = employeeToContenstedSpaceMap.get(employee);
			for (Rectangle cubicle : spaces) {
				totalContestedSpace += cubicle.area();
			}

		}

		int totalCubicleSpace = 0;
		for (String employee : employeeToCubicleMap.keySet()) {
			Rectangle cubicle = employeeToCubicleMap.get(employee);
			totalCubicleSpace += cubicle.area();

		}

		int availableSpace = (h*w) - totalCubicleSpace + totalCubicleSpace;

		System.out.println("Unallocated " + availableSpace);

		System.out.println("Contested " + totalContestedSpace);

		for (String employee : employeeToContenstedSpaceMap.keySet()) {
			List<Rectangle> contestedSpaces = employeeToContenstedSpaceMap.get(employee);
			int contestedPerEmployee = 0;
			for (Rectangle r : contestedSpaces) {
				contestedPerEmployee += r.area();
			}
			int availablePerEmployee = employeeToCubicleMap.get(employee).area() - contestedPerEmployee;
			System.out.println(employee + " " + availablePerEmployee);
		}
	}


	private static void parse(List<String[]> from, Map<String, Rectangle> to) {
		for (int i = 0; i < from.size(); i++) {
			String[] cubicleInfo = from.get(i);
			Rectangle r = new Rectangle(Integer.parseInt(cubicleInfo[1]), Integer.parseInt(cubicleInfo[2]), 
				Integer.parseInt(cubicleInfo[3]), Integer.parseInt(cubicleInfo[4]));
			to.put(cubicleInfo[0], r);
		}
	}
	private static List<Rectangle> disjointRectanglesOverlappingWith(Rectangle r, Map<Rectangle, List<Rectangle>> overlapMap) {
		Queue<Rectangle> bfs = new LinkedList<>();
		bfs.add(r);
		List<Rectangle> disjointRects = new ArrayList<>();
		while (!bfs.isEmpty()) {
			Rectangle curr = bfs.poll();
			for (Rectangle overlappingRect : overlapMap.get(curr)) {
				if (overlapMap.containsKey(overlappingRect)) {
					bfs.add(overlappingRect);
				} else {
					disjointRects.add(overlappingRect);
				}
			}
		}
		return disjointRects;
	}

	public static void main(String[] args) {
		Scanner in = new Scanner(new BufferedReader(new InputStreamReader(System.in)));
        int t = in.nextInt(); 
        in.nextLine();
        for (int i = 1; i <= t; ++i) {
        	String[] office = in.nextLine().split(" ");
        	int h = Integer.parseInt(office[0]);
        	int w = Integer.parseInt(office[1]);
        	int n = in.nextInt();
        	in.nextLine();
        	List<String[]> employeeCubicles = new ArrayList<>();
        	for (int j = 0; j < n; j++) {
        		String[] cubicles = in.nextLine().split(" ");
        		employeeCubicles.add(cubicles);

        	}
            printOfficeStats(employeeCubicles, h, w);
        }
	}
	
}