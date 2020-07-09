import java.util.*;
import java.io.*;

class LCSInstance {
		static String a;
		static String b;
		int i;
		int j;

		public LCSInstance(int i, int j){
			this.i = i;
			this.j = j;
		}

		@Override
        public boolean equals(Object that) {
        	LCSInstance other = (LCSInstance) that;
        	return other.i == this.i && other.j == this.j;
        }

    @Override
    public int hashCode() {
        return (i+j)%(199);
    }

}


class Edit{
	char token;
	int index;
	String action;

	public Edit(char token, int index, String action){
        this.token = token;
        this.index = index;
        this.action = action;
	}

}

public class LCS {
	public static void main(String[] args){
		String a1 = "abcdef";
		String b1 = "cbcgf";
		runLcs(a1,b1);
		// a1 = "abcdef";
		// b1 = "abcdef";
		// runLcs(a1,b1);
		// a1 = "abcdef";
		// b1 = "ghijklm";
		// runLcs(a1,b1);
	}

	public static void runLcs(String a1, String b1){
		//issue with memo 
		// System.out.println("----------Length of Longest Common Sub sequence using Top Down Approach ----------");
  //       int lcsLengthTopDown = getLCSTopDown(a1, b1);
  //       System.out.println("Length of LCS for strings " + a1 + " and " + b1 + " --> " + lcsLengthTopDown);


		System.out.println("----------Length of Longest Common Sub sequence using Bottom Up Approach ----------");
		int lcsLengthBottomUp = getLCSBottomUp(a1, b1);
		System.out.println("Length of LCS for strings " + a1 + " and " + b1 + " --> " + lcsLengthBottomUp);


		System.out.println("----------Longest Common Sub sequence ----------");
		List<Character> lcs = getLongestCommonSubsequence(a1, b1);
		Collections.reverse(lcs);
		System.out.print("LCS for strings " + a1 + " and " + b1 + " --> ");
		printList(lcs);

	}

	private static void printList(List<Character> l) { 
		if(l.size() == 0){
			return;
		}
        System.out.print("[" + l.get(0));
        for(int i = 1; i < l.size(); i++){
        	System.out.print(","+l.get(i));
        }
        System.out.println("]");
	}

	public static int getLCSTopDown(String a, String b){
		LCSInstance lcsInstance = new LCSInstance(0,0);
		lcsInstance.a = a;
		lcsInstance.b = b;
        return getLCSTopDown("",lcsInstance, new HashMap<LCSInstance, Integer>());		
	}
	private static int getLCSTopDown(String debugTool, LCSInstance lcsInstance, Map<LCSInstance, Integer> dict){

		
		if (lcsInstance.i >= lcsInstance.a.length()){
			return 0;
		} else if(lcsInstance.j >= lcsInstance.b.length()){
			return 0;
		} else {
			char aChar = lcsInstance.a.charAt(lcsInstance.i);
			char bChar = lcsInstance.b.charAt(lcsInstance.j);
			int lcsLength = 0;
			if(aChar == bChar){
				lcsInstance.i += 1;
				lcsInstance.j += 1;
				System.out.println(debugTool+ "compared " + aChar + " with " + bChar + " and result was " + (aChar == bChar));
				System.out.println(debugTool + "Single Path --> i="+lcsInstance.i + " j="+lcsInstance.j);
				lcsLength = dict.containsKey(lcsInstance) ? 1 + dict.get(lcsInstance) : 1 + getLCSTopDown("\t"+debugTool,lcsInstance, dict);
				lcsInstance.i -= 1;
				lcsInstance.j -= 1;
				//System.out.println("incremented length to " + lcsLength + " for subproblem with i="+lcsInstance.i + "and j="+lcsInstance.j);

			} else {
				lcsInstance.i += 1;
				System.out.println(debugTool+ "compared " + aChar + " with " + bChar + " and result was " + (aChar == bChar));
				System.out.println(debugTool+ "Option 1 --> i="+lcsInstance.i + " j="+lcsInstance.j);
				System.out.println(debugTool + "is instance in dictionary? " + dict.containsKey(lcsInstance));
				int lcsLength1 = dict.containsKey(lcsInstance) ? dict.get(lcsInstance) : getLCSTopDown("\t"+debugTool,lcsInstance, dict);
				lcsInstance.i -= 1;

				lcsInstance.j += 1;
				System.out.println(debugTool+ "compared " + aChar + " with " + bChar + " and result was " + (aChar == bChar));
				System.out.println(debugTool+ "Option 2 --> i="+lcsInstance.i + " j="+lcsInstance.j);
                System.out.println(debugTool + "is instance in dictionary? " + dict.containsKey(lcsInstance));
				int lcsLength2 = dict.containsKey(lcsInstance) ? dict.get(lcsInstance) : getLCSTopDown("\t"+debugTool,lcsInstance, dict);
				lcsInstance.j -= 1;

				lcsLength = Math.max(lcsLength1, lcsLength2);
			}
			System.out.println(debugTool + "adding sub-problem with i="+lcsInstance.i +" j="+lcsInstance.j + " to dict. LCS = " + lcsLength);
			addInstanceToDict(lcsInstance,dict, lcsLength);
			return lcsLength;
		}

	}
	private static void addInstanceToDict(LCSInstance lcsInstance, Map<LCSInstance, Integer> dict, Integer lcsLength){
		if(!dict.containsKey(lcsInstance)){
			dict.put(lcsInstance, lcsLength);
		}
	}
	private static void addInstanceToDict(LCSInstance lcsInstance, Map<LCSInstance, List<Character>> dict, List<Character> lcsSoFar){
		if(!dict.containsKey(lcsInstance)){
			dict.put(lcsInstance, lcsSoFar);
		}
	}

	public static int getLCSBottomUp(String a, String b){

        //memoTable[i,j] --> LCS for the first i chars of A and j chars of B
		int[][] memoTable = new int[a.length() + 1][b.length() + 1];

       
		//baseCase
		for(int i = 0; i < a.length(); i++){
			memoTable[i][0] = 0;
		}
		for(int j = 0; j < b.length(); j++) {
			memoTable[0][j] = 0;
		}

		for(int i = 1; i <= a.length(); i ++){
			for(int j = 1; j <= b.length(); j++){
				if(a.charAt(i-1) == b.charAt(j-1)){
					memoTable[i][j] = 1 + memoTable[i-1][j-1];
				}else{
					memoTable[i][j] = Math.max(memoTable[i-1][j],memoTable[i][j-1]);
				}
				System.out.println("LCS so far: " + memoTable[i][j] + " with ith character " + a.charAt(i-1) + " and jth character " + b.charAt(j-1));
			}
		}
		return memoTable[a.length()][b.length()];

	}

	public static List<Character> getLongestCommonSubsequence(String a, String b){
		LCSInstance lcsInstance = new LCSInstance(0,0);
		lcsInstance.a = a;
		lcsInstance.b = b;
		return getLongestCommonSubsequence(lcsInstance, new HashMap<LCSInstance, List<Character>>());
	}

	public static List<Character> getLongestCommonSubsequence(LCSInstance lcsInstance, Map<LCSInstance, List<Character>> dict){
		if (lcsInstance.i >= lcsInstance.a.length()){
			return new ArrayList<Character>();
		} else if(lcsInstance.j >= lcsInstance.b.length()){
			return new ArrayList<Character>();
		} else {
			char aChar = lcsInstance.a.charAt(lcsInstance.i);
			char bChar = lcsInstance.b.charAt(lcsInstance.j);
			List<Character> lcsSoFar = null;
			if(aChar == bChar){
				lcsInstance.i += 1;
				lcsInstance.j += 1;
				lcsSoFar = dict.containsKey(lcsInstance) ? dict.get(lcsInstance) : getLongestCommonSubsequence(lcsInstance, dict);
				lcsSoFar.add(aChar);
				lcsInstance.i -= 1;
				lcsInstance.j -= 1;
				System.out.println("added " + aChar + " for subproblem with i="+lcsInstance.i + "and j="+lcsInstance.j);
			} else {
				lcsInstance.i += 1;
				List<Character> lcsSoFar1 = dict.containsKey(lcsInstance) ? dict.get(lcsInstance) : getLongestCommonSubsequence(lcsInstance, dict);
				lcsInstance.i -= 1;

				lcsInstance.j += 1;
				List<Character> lcsSoFar2 = dict.containsKey(lcsInstance) ? dict.get(lcsInstance) : getLongestCommonSubsequence(lcsInstance, dict);
				lcsInstance.j -= 1;

				lcsSoFar = lcsSoFar1.size() > lcsSoFar2.size() ? lcsSoFar1 : lcsSoFar2;
			}
			addInstanceToDict(lcsInstance,dict, lcsSoFar);
			return lcsSoFar;
		}
	}

	public static int getLCSGivenEditDistance(List<Edit> edits, String a, String b){
        int lcs = 0;
		for(int i = 0; i < a.length(); i++) {
			boolean isAnEdit = false;
			for(Edit e : edits){
				if(e.token == a.charAt(i) && e.index == i){
					isAnEdit = true;
				}
			}
			if(!isAnEdit){
				lcs += 1;
			}
		}
		return lcs;
	}
}