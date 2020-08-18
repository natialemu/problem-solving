import java.util.*;
import java.io.*;

public class BFF {

	public static int maxCircleSize(int[] kidsToBffMap) {
		//TODO

	}
	public static void main(String[] args) {
		Scanner in = new Scanner(new BufferedReader(new InputStreamReader(System.in)));
        int t = in.nextInt(); 
        in.nextLine();
        for (int i = 1; i <= t; ++i) {
        	int N = in.nextInt(); 
        	in.nextLine();

        	String[] bffsString= in.nextLine().split(" ");
        	int[] bffs = new int[bffsString.length];
        	for (int j = 0; j < bffsString.length; j++) {
        		bffs[j] = Integer.parseInt(bffsString[j]);
        	}
            int maxKidsInCircle = maxCircleSize(bffs);
            System.out.println("Case #" + i + ": " + (numThrows));
        }

	}
	
}