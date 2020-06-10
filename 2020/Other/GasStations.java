import java.util.*;
import java.io.*;


/**
based on the following problem: https://leetcode.com/problems/minimize-max-distance-to-gas-station/
**/
public class GasStations {

	public static double minDistanceBetweenGasStations(Integer[] stations, int k) {

		Map<Integer, Double> stationToPairDistanceMap = new HashMap<>();

		for (int i = 0; i < stations.length - 1; i++) {
			stationToPairDistanceMap.put(stations[i], (double) stations[i + 1] - stations[i]);
		}

		PriorityQueue<Integer> maxHeap = new PriorityQueue<>((a,b)-> (stationToPairDistanceMap.get(b).compareTo(stationToPairDistanceMap.get(a))));
		PriorityQueue<Integer> minHeap = new PriorityQueue<>((a,b) -> (stationToPairDistanceMap.get(a).compareTo(stationToPairDistanceMap.get(b))));

		for (Integer station : stationToPairDistanceMap.keySet()) {
			maxHeap.add(station);
		}

		for (int i = 0; i < k; i++) {
			int stationWithMaxDistance = maxHeap.poll();
			double newDistance = stationToPairDistanceMap.get(stationWithMaxDistance) / 2.0;
			stationToPairDistanceMap.put(i, newDistance);
			stationToPairDistanceMap.put(stationWithMaxDistance, newDistance);
			maxHeap.add(stationWithMaxDistance);
			maxHeap.add(i);
		}

		maxHeap.forEach(station -> {
			minHeap.add(station);
		});
		return stationToPairDistanceMap.get(minHeap.peek());
	}

	public static void main(String[] args) {
		Scanner in = new Scanner(new BufferedReader(new InputStreamReader(System.in)));
        int t = in.nextInt();  // num test cases
        in.nextLine();
        for (int i = 1; i <= t; ++i) { // for each test case
          String[] s = in.nextLine().split(" ");
          Integer[] arr = new Integer[s.length];
          for (int j = 0; j < s.length; j++) {
          	arr[j] = Integer.parseInt(s[j]);
          }
          int k = in.nextInt();
          in.nextLine();
          double minMaxDistance = minDistanceBetweenGasStations(arr, k);
          System.out.println("Case #" + i + ": " + (minMaxDistance));
      }
	}
	
}