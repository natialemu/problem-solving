

public class StringShuffling {
	public static boolean isInterleaved(String x, String y, String z) {
		return isInterleaved(x, 0, y, 0, z, 0, new HashMap<>());
	}


	private static boolean isInterleaved(String x, int xi, String y, int yi, String z, int zi,
					Map<Integer, Map<Integer, Map<Integer, Boolean>>> memo) {
		
		if (oneIsDone(x, xi, y, yi, z, zi)) {
			return allAreDone(x, xi, y, yi, z, zi);
		}

		if (z.charAt(zi) == x.charAt(xi) && z.charAt(zi) == y.charAt(yi)) {
			boolean soln1 = memo.containsKey(xi + 1) && memo.get(xi + 1).containsKey(yi) && memo.get(xi).get(yi).containsKey(zi + 1) ?
			memo.get(xi + 1).get(yi).get(zi + 1) : isInterleaved(x, xi + 1, y, yi, z, zi + 1);
			boolean soln2 = memo.containsKey(xi) && memo.get(xi).containsKey(yi) && memo.get(xi).get(yi + 1).containsKey(zi + 1) ?
			memo.get(xi).get(yi + 1).get(zi + 1) : isInterleaved(x, xi + 1, y, yi, z, zi + 1);
			boolean currSoln  = soln2 || soln1;

			// add to memo
			Map<Integer, Map<Integer, Boolean>> yzMap = new HashMap<>();
			Map<Integer, Boolean> zMap = new HashMap<>();
			zMap.put(zi, currSoln);
			yzMap.put(yi, zMap);
			memo.put(xi, yzMap);
			return soln2  || soln1;
		} else if (z.charAt(zi) == x.charAt(xi)) {
			return isInterleaved(x, xi + 1, y, yi, z, zi + 1);
		} else if (z.charAt(zi) == y.charAt(yi)) {
			return isInterleaved(x, xi, y, yi + 1, z, zi + 1);
		} else {
			return false;
		}
	}
}