
/**
Algorithm 2: 
 for each window of size 1 to N -1:
 	use karp rabin to see repetition

**/

public class RepeatedSubstringV2 {
	public static int allRepeatedSubstrings(String input) {
		int N = input.length();

		int numRepeated = 0;
		for (int window = 1; window < N; window++) {
			numRepeated += reatedSubstringsOfSize(input, window);
		}
		return numRepeated;
		
	}

	private static int reatedSubstringsOfSize(String s, int windowLength) {

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