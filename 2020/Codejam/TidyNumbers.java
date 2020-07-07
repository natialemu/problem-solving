import java.util.*;
import java.io.*;
public class TidyNumbers {
	public static long tidyNumLessThan(long input) {
		long low = 0;
		long high = input - 1;
		long prevTidyNum = -1;
		while (high >= high) {
			long mid = (low + high) / 2;
			long nextTidyNum = tidyNumLargerThan(mid);
			if (nextTidyNum >= input) {
				high = mid;
			} else {
				low = mid;
			}
			prevTidyNum = nextTidyNum;
		}
		return prevTidyNum;
	}

	private static long tidyNumLargerThan(long num) {
		String numString = Long.toString(num);
		int problemPoint = -1;
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < numString.length() - 1; i++) {
			if (problemPoint == - 1) {
				builder.append(numString.charAt(i));
			} else {
				builder.append(numString.charAt(problemPoint));
			}
			if (problemPoint == - 1 && Character.intValue(numString.charAt(i)) > Character.intValue(numString.charAt(i + 1))) {
				problemPoint = i;
			} 
		}
		return Long.parseLong(builder.toString());		
	}

	public static void main(String[] args) {
		Scanner in = new Scanner(new BufferedReader(new InputStreamReader(System.in)));
        int t = in.nextInt(); 
        in.nextLine();
        for (int i = 1; i <= t; ++i) {
          long input = in.nextLong();
          long sol = tidyNumLessThan(input);
          System.out.println("Case #" + i + ": " + (sol));
      }

	}
}