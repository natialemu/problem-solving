
import java.util.*;
import java.io.*;
/**


**/

public class OfficeSpace {

	class Rectangle {
		int x1, y1, x2, y2;
		public (int x1, int y1, int x2, int y2) {
			this.x1 = x1;
			this.x2 = x2;
			this.x3 = x3;
			this.x4 = x4;
		}

		public int area() {
			return (x2 - x1)* (y2 - y1);
		}

		public boolean overlaps(Rectangle other) {

		}

		public Rectangle overlappingRegion(Rectangle other) {
			if (!overlaps(this, other)) return null;

		}


	}

	public static void printOfficeStats(List<String[]> officeSpace, int h, int w) {

		Map<String, Rectangle> employeeToCubicleMap = new LinkedHashMap<>();
		parse(officeSpace, employeeToCubicleMap);
		Map<Rectangle, List<Rectangle>> overlappingCubiclesMap = new HashMap<>();
		Set<Rectangle> duplicates = new HashSet<>();
		PriorityQueue<Rectangle> minHeapRight = new PriorityQueue<>((a,b) -> (a.x2 - b.x2));
		PriorityQueue<Rectangle> minHeapLeft = new PriorityQueue<>((a,b) -> (a.x1 - b.x1));


		//insert all rectangles
		employeeToCubicleMap.forEach((employee, reactangle) -> minHeapLeft.add(rectangle));

		while (!minHeapLeft.isEmpty()) {
			Rectangle current = minHeapLeft.pop();
			while (!minHeapRight.isEmpty() && !current.overlaps(minHeapRight.peek())) minHeapRight.pop();
			Stack<Rectangle> temp = new Stack<>();
			while (!minHeapRight.isEmpty() && current.overlaps(minHeapRight.peek())) {
				Rectangle top = minHeapRight.pop();
				temp.push(top);

				Rectangle overlapping = current.overlappingRegion(top);
				minHeapLeft.add(overlapping);
				duplicate.add(overlapping);
				List<Rectangle> overlapRectangles = overlappingCubiclesMap.getOrDefault(current, new ArrayList<>());

				overlapRectangles.add(overlapping); 
				overlappingCubiclesMap.put(current, overlapRectangles);
			}
		}

		System.out.println("Total: " + (h*w));
		Map<String, List<Rectangle>> employeeToContenstedSpaceMap = new LinkedHashMap<>();
		employeeToCubicleMap.forEach((employee, reactangle) -> {
			employeeToContenstedSpaceMap.put(employee, disjointRectanglesOverlappingWith(rectangle, overlappingCubiclesMap));
		});

		int totalContestedSpace = 0;
		employeeToContenstedSpaceMap.forEach((employee, spaces) ->{
			spaces.forEach(space -> {
				totalContestedSpace += space.area();
			});

		});

		int totalCubicleSpace = 0;
		employeeToCubicleMap.forEach((employee, cubicle) -> {
			totalCubicleSpace += cubicle.area();
		});

		int availableSpace = (h*w) - totalCubicleSpace + totalCubicleSpace;

		System.out.println("Unallocated: " + availableSpace);

		System.out.println("Contested: " + totalContestedSpace);

		employeeToContenstedSpaceMap.forEach((employee, contestedSpaces) -> {
			int contestedPerEmployee = 0;
			contestedSpaces.forEach(space -> contestedPerEmployee += space.area());
			int availablePerEmployee = employeeToCubicleMap.get(employee).area() - contestedPerEmployee;
			System.out.println(employee +" " + availablePerEmployee);

		})
	}


	private static void parse(List<String[]> from, Map<String, Rectangle> to) {
		for (int i = 0; i < from.size(); i++) {
			String[] cubicleInfo = from.get(i);
			Rectangle r = new (Integer.parseInt(cubicleInfo[1]), Integer.parseInt(cubicleInfo[2]), Integer.parseInt(cubicleInfo[3]), Integer.parseInt(cubicleInfo[4]))
			to.put(cubicleInfo[0], r);
		}
	}
	private static List<Rectangle> disjointRectanglesOverlappingWith(Rectangle r, Map<Rectangle, List<Rectangle>> overlapMap) {
		//TODO
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