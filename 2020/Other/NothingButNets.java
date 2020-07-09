
import java.util.*;
import java.io.*;

/**

War story: Nothing but nets
Given a set of 2D points, cluster the points into k clusters


input format:

t
N

**/
	class Point {
		double x;
		double y;

		public Point(double x, double y) {
			this.x = x;
			this.y = y;
		}

		public double distance(Point other) {
			return Math.sqrt((other.x - x)(other.x - x)  + (other.y - y)*(other.y - y));
		}

		@Override
		public String toString() {
			return "(" + x + ", " + y + ")";
		}
			@Override
		public boolean equals(Object o) {
			Point other = (Point) o;
			return Objects.equals(other.x, x) && Objects.equals(other.y, y);
		}

		@Override
		public int hashCode() {
			return Objects.hash(x, y);
		}
	}
	class Cluster {
		List<Point> points = new ArrayList<>();

		public void add(Point p) {
			points.add(points);
		}

		@Override
		public String toString() {
			for (int i = 0; i < points.size() - 1; i++) {
				System.out.print(points.get(i) + ", ");
			}
			System.out.println(points.get(points.size() - 1));
		}
	}
public class NothingButNets {

	class UnionFind {
		//size();
		//clusters();
		//find();
		//merge(); //doesn't do anythign if points are in the same component

		Point[] parentOf;
		Map<Point, Integer> componentSizeMap;
		int disjointSets;

		public UnionFind(int size, Set<Point> points) {
			parentOf = new Point[size];
			componentSizeMap = new HashMap<>();
			int i = 0;
			Iterator<Point> iterator = points.iterator;
			while(iterator.hasNext()) {
				Point next = iterator.next();
				parentOf[i] = next;
				componentSizeMap.put(next, 1);
				i++;
			}
			disjointSets = size;
		}

		public int size() {
			return disjointSets;
		}

		public Point find(Point p) {
			Point current = p;
			while(parentOf[current].equals(current)) {
				parentOf[current] = parentOf[parentOf[current]];
				current = parentOf[current];
			}
			return current;
		}

		public Point merge(Point p1, Point p2) {
			Point parentOfP1 = find(p1);
			Point parentOfP2 = find(p2);
			if (parentOfP1.equals(parentOfP2)) return null;
			if (componentSizeMap.get(parentOfP1) > componentSizeMap.get(parentOfP2)) {
				//move p2;
				parentOf[parentOfP2] = parentOfP1;
				componentSizeMap.put(parentOfP1, componentSizeMap.get(parentOfP1) + componentSizeMap.get(parentOfP2));
				componentSizeMap.remove(parentOfP2);
				return parentOfP1;

			} else {
				parentOf[parentOfP1] = parentOfP2;
				componentSizeMap.put(parentOfP2, componentSizeMap.get(parentOfP1) + componentSizeMap.get(parentOfP2));
				componentSizeMap.remove(parentOfP1);
				return parentOfP2;
			}

		}

		public List<Cluster> clusters() {
			Map<Point, Cluster> clustersByParent;
			for (Point p : parentOf) {
				Point parent = parentOf[p];
				Cluster cluster = clustersByParent.getOrDefault(parent, new Cluster());
				cluster.add(p);
				clustersByParent.put(parent,  cluster);
			} 
			return new ArrayList<>(clustersByParent.values());
		}
	}
	class Edge {
		Point to;
		Point from;
		double weight;
		public Edge(Point to, Point from) {
			this.to = to;
			this.from = from;
		}

		@Override
		public boolean equals(Object o) {
			Edge other = (Edge) o;
			return Objects.equals(other.to, to) && Objects.equals(other.from, from) ||
			Objects.equals(other.from, to) && Objects.equals(other.to, from);
		}

		@Override
		public int hashCode() {
			return Objects.hash(to, weight) + Objects.hash(from, weight);
		}
	}


	private static List<Cluster> clusterPoints(double[][] points, int k) {
		List<Edge> allEdges = new ArrayList<>();
		Set<Edge> distinctEdges = new HashSet<>();
		Set<Point> distinctPoints = new HashSet<>();

		//parse all edges
		for (double[] point : points) {
			for (double[] secondPoint : points) {
				Edge edge = new Edge(new Point(secondPoint[0], secondPoint[1]), new Point(point[0], point[1]));
				if (!distinctEdges.contains(edge) && !areSame(point, secondPoint) && ) {
					allEdges.add(edge);
					distinctEdges.add(edge);
					distinctPoints.add(edge.to);
					distinctPoints.add(edge.from);
				}
			}
		}

		Collections.sort(allEdges, (a, b) -> (a.weight - b.weight));

		UnionFind unionFind = new UnionFind(points.length, distinctPoints);
		int edgePointer = clusterViaKruskal(unionFind, allEdges, k); // returns the point where kruskal terminated.
		List<Cluster> disjointClusters = unionFind.clusters();
		for (int i = edgePointer; i < allEdges.size(); i++) {
			Edge currentEdge = allEdges.get(i);

			Point from = currentEdge.from;
			Point to = currentEdge.to;

			if (!unionFind.find(from).equals(unionFind.find(to))) {
				Cluster newCluster = new Cluster();
				newCluster.addAll(Arrays.asList(from, to));
			}
		}

		return disjointClusters;
	}

	private static int clusterViaKruskal(UnionFind uf, List<Edge> edges, int maxSize) {
		int disjointSets = uf.size();
		int i = 0;
		while (disjointSets == maxSize) {
			Edge currentEdge = edges.get(i);
			Point from = currentEdge.from;
			Point to = currentEdge.to;
			uf.merge(from, to);
			disjointSets = uf.size();
			i++;
		}
		return i;

	}

	public static void main(String[] args) {
		Scanner in = new Scanner(new BufferedReader(new InputStreamReader(System.in)));
        int t = in.nextInt(); 
        in.nextLine();
        for (int i = 1; i <= t; ++i) {
           int N = in.nextInt(); // number of works and machines
           in.nextLine();
           double[][] points = new double[N][2];
           for (int k = 0; k < N; k++) {
           	   String[] point = in.nextLine().split(" ");
           	   points[k][0] = Double.parseDouble(point[0]);
           	   points[k][1] = Double.parseDouble(point[1]);
           }
           
           List<Cluster> clusters = clusterPoints(points);
           System.out.println("Case #" + i + ": ");
           printClusters(clusters);
      }

	}
	private static void printClusters(List<Cluster> clusters) {
		for (Cluster cluster : clusters) {
			System.out.println(cluster);
		}
	}



	
}