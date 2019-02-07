import java.util.*;
import java.io.*;

public class StringInterleave {
	public static void main(String[] args){
		String a = "aab";
		String b = "aacx";
        String c = "aacaaab";

        boolean areInterleaved = isInterleaved(a,b,c);
        System.out.println("are strings " + a + " and " + b +" interleaved? " + areInterleaved);

	}

	public static boolean isInterleaved(String a, String b, String c){
		if(a.length() == 0 && b.length() == 0){
			return true;
		}
		if(c.length() == 0){
			return false;
		}

		char cFirst = c.charAt(0);
		boolean result1 = false;
		boolean result2 = false;
		boolean result3 = false;
		if(a.length() != 0 && c.charAt(0) == a.charAt(0)) {
			result1 = isInterleaved(a.substring(1,a.length()),b,c.substring(1,c.length()));
			
		}
		if(b.length() != 0 && c.charAt(0) == b.charAt(0)) {
			result2 = isInterleaved(a,b.substring(1,b.length()),c.substring(1,c.length()));
			
		}
		if(a.length() == 0 && c.charAt(0) != b.charAt(0) ||
			b.length() == 0 && c.charAt(0) != a.charAt(0) ||
			c.charAt(0) != a.charAt(0) && c.charAt(0) != b.charAt(0)) {
			result3 = isInterleaved(a,b,c.substring(1,c.length()));
			
		}
		return result3 || result2 || result1;
		
	}
}