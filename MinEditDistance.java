import java.util.*;
import java.io.*;

class MEDInstance {
		static String a;
		static String b;
		int i;
		int j;

		public MEDInstance(int i,j){
			this.i = i;
			this.j = j;
		}
}

class Edit{
	char token;
	int index;
	String action;
}

public class MinEditDistance{

	public static void main(String[] args){
		String a1 = "abcdefg";
		String b1 = "cbcgxf";
		runMED(a1,b1);
		a1 = "abcdef";
		b1 = "abcdef";
		runMED(a1,b1);
		a1 = "abcdef";
		b1 = "ghijklm";
		runMED(a1,b1);
	}

	public static void runMED(String a, String b){
		System.out.println("----------Length of Minimum Edit Distance using Top Down Approach ----------");
        int lcsLengthTopDown = getMinEditDistanceTopDown(a1, b1);
        System.out.println("Length of MED to convert " + a + " to " + b + " --> " + lcsLengthTopDown);


		System.out.println("----------Length of Minimum Edit distance using Bottom Up Approach ----------");
		int lcsLengthBottomUp = getMinEditDistanceBottomUp(a1, b1);
		System.out.println("Length of MED to convert " + a + " to " + b + " --> " + lcsLengthBottomUp);


		System.out.println("----------Minimum Edits ----------");
		List<Edit> lcs = getMinEditDistanceChars(a1, b1);
		Collections.reverse(lcs);
		System.out.print("Minimum Edit required to convert string " + a + " into " + b + " --> ");
		printList(lcs);

	}

	public static int getMinEditDistanceTopDown(String a, String b) {
		MEDInstance medInstance = new MEDInstance(0,0);
		medInstance.a = a;
		medInstance.b = b;
		return getMinEditDistanceTopDown(medInstance, new HashMap<MEDInstance, Integer>());


	}

	private static void printList(List<Edit> l) { 
		if(l.size() == 0){
			return;
		}
        System.out.print("[("+ l.get(0).action+ " " + l.get(0).token +" at index "+l.get(0).index+")");
        for(int i = 0; i < l.size(); i++){
        	System.out.print(", ("+ l.get(i).action+ " " + l.get(i).token +" at index "+l.get(i).index+")");
        }
        System.out.println("]");
	}
	private static int getMinEditDistanceTopDown(MEDInstance medInstance, Map<MEDInstance, Integer> dict){
		if(medInstance.a.charAt(medInstance.i) == a.length() - 1){
			return medInstance.b.length() - medInstance.j - 1;
		} else if(medInstance.b.charAt(medInstance.j) == b.length() - 1) {
			return medInstance.a.length() - medInstance.i - 1;
		} else {
			char aChar = medInstance.a.charAt(0);
			char bChar = medInstance.b.charAt(0);

            int minEdit = 0;
			if (aChar == bChar){
				medInstance.i += 1;
				medInstance.j += 1;
				minEdit = dict.containsKey(medInstance) ? dict.get(medInstance) :getMinEditDistanceTopDown(medInstance, dict);
				medInstance.i -= 1;
				medInstance.j -= 1;
			} else {
				medInstance.i += 1;
				int minEdit1 = dict.containsKey(medInstance) ? dict.get(medInstance) :getMinEditDistanceTopDown(medInstance, dict);
				medInstance.i -= 1;

				medInstance.j += 1;
				int minEdit2 = dict.containsKey(medInstance) ? dict.get(medInstance) :getMinEditDistanceTopDown(medInstance, dict);
				medInstance.j -= 1;
				minEdit = 1 + Math.min(minEdit1, minEdit2);
			}

			dict.put(medInstance, minEdit);
		}

	}

	public static int getMinEditDistanceBottomUp(String a, String b) {

		int[][] memoTable = int[a.length() + 1][b.length() + 1];
		//basecase
		for(int i = 0; i < memoTable.length; i++){
			memoTable[i][0] = i;
		}

		for(int j = 0; j < memoTable[0].length; j++){
			memoTable[0][j] = j;
		}

		//recursive cases
		for(int i = 1; i < memoTable.length; i++) {
			for(int j = 1; j < memoTable[i].length; j++) {
				if(a.charAt(i-1) == b.charAt(b)){
					memoTable[i][j] = memoTable[i-1][j-1];
				} else {
					memoTable[i][j] = 1 + Math.min(memoTable[i-1][j], memoTable[i][j-1]);
				}
			}
		}
		return memoTable[a.length()][b.length()];

	}

    public static List<Edit> getMinEditDistanceChars(String a, String b) {
    	int[][] memoTable = int[a.length() + 1][b.length() + 1];
		//basecase
		for(int i = 0; i < memoTable.length; i++){
			memoTable[i][0] = i;
		}

		for(int j = 0; j < memoTable[0].length; j++){
			memoTable[0][j] = j;
		}

		//recursive cases
		for(int i = 1; i < memoTable.length; i++) {
			for(int j = 1; j < memoTable[i].length; j++) {
				if(a.charAt(i-1) == b.charAt(b)){
					memoTable[i][j] = memoTable[i-1][j-1];
				} else {
					memoTable[i][j] = 1 + Math.min(memoTable[i-1][j], memoTable[i][j-1]);
				}
			}
		}
		return traceBacksteps(memoTable, a, b);
	}

	private static List<Edit> traceBacksteps(int[][] memoTable, String a, String b) {

		List<Edit> minEdits = new ArrayList<>();

        int i = a.length();
        int j = b.length();
        while(i >= 0 && j >= 0){
        	if(memoTable[i][j] == memoTable[i-1][j-1]){
        		i -= 1;
        		j -= 1;
        	} else if(memoTable[i-1][j] < memoTable[i][j-1]){
        		Edit edit = new Edit(a.charAt(i - 1), i-1,"DELETE");
        		minEdits.add(edit);
        		i -= 1;
        	} else {
        		Edit edit = new Edit(b.charAt(j - 1), j-1,"ADD");
        		minEdits.add(edit);
        		j -= 1;
        	}
        }
        return minEdits;
	}
}