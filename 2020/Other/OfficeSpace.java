
import java.util.*;
import java.io.*;
/**




Initialize a min PQ based on right end(x2) of the rectangles.
Initialize a min PQ based on left end(x1)
initialize a set to keep track of duplicate rectangles


for  each rectangle in the min PQ list by left :
	 
	while rectangle doesnt overlap with pq right peek and pq right is not empty, {
		pop from pq right. // if rectangle doesn't overlap with pq, then none of the others with further x1 will overlap
	
	}
	Stack temp
	while (pq is not empty and pq overlaps ) {

		add the intersection rectangle to a mapping
		add intersection to min pq based on left
		add intersection to duplicate rectangles
		pop from pq right and push to temp
	}

	add back everything in temp back to pq right

	add rectangle to pq right






- leaf rectangles are values in the map that are not keys

  Contested space: is just the number of dijoint overlapping rectangles. to be proven

Unallocated space: total are - (area of each rectangle) + contested space


map each original rectangle to a list of contested rectangles.

for each rectangle, 
	- get the number of disjoint spaces it is connected to.
	- get the total area of the disjoint spaces.
	original rectangle area - (sum of area of each contested space).

**/

public class OfficeSpace {

	class Rectangle {
		int x1, y1, x2, y2;

		public int area() {

		}

	}

	public static void printOfficeStats(List<String[]> officeSpace, int h, int w) {

		Map<String, Rectangle> employeeToCubicleMap = new LinkedHashMap<>();
		parse(officeSpace, employeeToCubicleMap);
		Map<Rectangle, Rectangle> overlappingCubiclesMap = new HashMap<>();
		Set<Rectangle> duplicates = new HashSet<>();
		PriorityQueue<Rectangle> minHeapRight = new PriorityQueue<>((a,b) -> (a.x2 - b.x2));
		PriorityQueue<Rectangle> minHeapLeft = new PriorityQueue<>((a,b) -> (a.x1 - b.x1));


		//insert all rectangles
		employeeToCubicleMap.forEach((employee, reactangle) -> minHeapLeft.add(rectangle));

		while (!minHeapLeft.isEmpty()) {
			//TODO
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
		})

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
		//TODO
	}
	private static List<Rectangle> disjointRectanglesOverlappingWith(Rectangle r, Map<Rectangle, Rectangle> overlapMap) {
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