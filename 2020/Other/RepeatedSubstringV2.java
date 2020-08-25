
/**
Link to problem: https://open.kattis.com/problems/substrings

Algorithm 2: 
 for each window of size 1 to N -1:
 	use karp rabin to find repeated substrings of fixed window size.

**/

public class RepeatedSubstringV1 {

	class RollingHash {
		int base;
		int hash;
		final int prime = 7;

		public RollingHash(int alphabetSize) {
			base = alphabetSize;
		}


		public void append(char c) {
			int ord = (int) c;
			// change 12 to 123 -> multiply the hash by length of the hash + ord
			int hashLength = Integer.toString(hash).length();
			hash = hash*(Math.pow(base, hashLength)) + ord;
		}
		public void removeFirst(char c) {
			// change 123 to 23 = 123 - 100 -> subtract  length of the hash + ord
			hash = hash - Math.pow(base, hashLength);

		}

		public int hash() {
			return hash % prime;
		}
}


public class RepeatedSubstringV2 {
	public static int allRepeatedSubstrings(String input) {
		int N = input.length();

		int numRepeated = 0;
		for (int window = 1; window < N; window++) {
			numRepeated += repeatedSubstringsOfSize(input, window);
			
		}
		return numRepeated;
		
	}

	private static int repeatedSubstringsOfSize(String s, int windowLength) {
		Map<Integer, Integer> hashToCountMap = new HashMap<>();
		Map<Integer, String> hashToSubstringMap = new HashMap<>();
		int numRepeated = 0;
		RollingHash rollingHash = new RollingHash(26);

		int start = 0;
		for(int i = 0; i < s.length(); i++) {
			rollingHash.append(s.charAt(s));
			if (i >= window) {
				rollingHash.removeFirst(start);
				hashToCountMap.put(rollingHash.hash(), hashToCountMap.getOrDefault(rollingHash.hash(), 0) + 1);
				//vertification may be needed
				hashToSubstringMap.put(rollingHash.hash(), s.substring(start, i + 1));
				start++;
			}
		}

		for (int hash : hashToCountMap.keySet()) {
			if (hashToCountMap.get(hash) > 1) numRepeated += 1;
			
		}
		return numRepeated;

	}

	public static void main(String[] args) {
		Scanner in = new Scanner(new BufferedReader(new InputStreamReader(System.in)));
        int t = in.nextInt(); 
        in.nextLine();
        for (int i = 1; i <= t; ++i) {
        	String input = in.nextLine(); 
            int n = allRepeatedSubstrings(input);
            System.out.println("Case #" + i + ": " + (n));
        }

	}
	
}