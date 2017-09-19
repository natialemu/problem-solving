import java.util.*;
import java.io.*;

public class BathroomStalls{


	public static void main(String[] args){
		Scanner scan = new Scanner(System.in);
		int testCases = Integer.parseInt(scan.nextLine());

		for(int i =0; i< testCases; i++){
			String[] inputs = scan.nextLine().split(" ");
			assert(inputs.length == 2);
			int N = Integer.parseInt(inputs[0]);
			int K = Integer.parseInt(inputs[1]);
			int[] stalls = new int[N+2];
			stalls[0] = -1;
		    stalls[stalls.length -1] = -1;
		    Map<Integer,String> mapIndexToLsRs = new HashMap<>();

		    int lastIndex = -1;

		    while(K >= 1) {

		    	int indexToMark = processTestCase(stalls, mapIndexToLsRs);
		    	stalls[indexToMark] = -1;
		    	mapIndexToLsRs = new HashMap<>();
		    	
		    	if(K != 1){
		    		//update(ma);
		    		lastIndex = indexToMark;

		    	}

		    	
		    	K --;

		    }
		    //print something about 
		    int max = Math.max(Integer.parseInt(mapIndexToLsRs.get(lastIndex)),Integer.parseInt(mapIndexToLsRs.get(lastIndex)));
		    int min = Math.min(Integer.parseInt(mapIndexToLsRs.get(lastIndex)),Integer.parseInt(mapIndexToLsRs.get(lastIndex)));
		    System.out.println("Case #" + i + ": " + max + " " + min);

			
		}

	}

	public static int processTestCase(int[] stalls, Map<Integer,String> mapIndexToLsRs){
		
		// first and last stalls are taken
		

		

		calculateRsLs(stalls,mapIndexToLsRs);

		System.out.println("Size of hashtag is " + mapIndexToLsRs.size());

        List<String> maxOfMin = new ArrayList<>();
        int max = -1;
		for(String s : mapIndexToLsRs.values()){
			int minOfLsRs = Math.min( ((int)s.charAt(0)), ((int)s.charAt(1)) );
			if(minOfLsRs > max){
				max = minOfLsRs;
			}

		}

		for(String s: mapIndexToLsRs.values()){
			int minOfLsRs = Math.min(((int)s.charAt(0)), ((int)s.charAt(1)) );
			if(minOfLsRs == max){
				maxOfMin.add(s);
			}
		}

		if(maxOfMin.size() > 1){
			List<String> maxOfMax = new ArrayList<>();
			int maxMax = -1;
			for(String s : maxOfMin){
				int maxOfLsRs = Math.max(((int)s.charAt(0)), ((int)s.charAt(1)) );
				if(maxOfLsRs > maxMax){
					maxMax = maxOfLsRs;
				}

			}

			for(String s : maxOfMin){
				int maxOfLsRs = Math.max(((int)s.charAt(0)), ((int)s.charAt(1)) );
				if(maxOfLsRs == maxMax){
					maxOfMax.add(s);
				}
				

			}
			
			String lsrs = maxOfMax.get(0);

			return ((int)lsrs.charAt(2));

			

			//calculate the max of min
		}else if(maxOfMin.size() == 1){
			String lsrs = maxOfMin.get(0);
			return ((int)lsrs.charAt(2));
			
			//take the remaining one
		}else{
			System.out.println("HUGE MISTAKE!!");
		}
		return -1;
	}

	public static void calculateRsLs(int[] stalls, Map<Integer, String> mapIndexToLsRs){
		int[] LS = new int[stalls.length];

        int[] RS = new int[stalls.length];
		//base case

		for(int i =0;  i < stalls.length; i++){
			if(stalls[i] == -1){
				LS[i] = 0; //LS value of index i
				RS[i] = 0;
			}
		}

        //find LS values
		for(int i  =0;  i < LS.length; i++){
			if(LS[i] != 0){
				LS[i] = LS[i-1] + 1;
				

			}
		}

		//find RS Values
		for(int i  =RS.length - 1;  i >= 0; i--){
			if(RS[i] != 0){
				RS[i] = RS[i+1] + 1;
				

			}
		}

		//map index to LS and RS values

        for(int i  = 0;  i < stalls.length; i++){
			StringBuilder builder = new StringBuilder();
			builder.append(LS[i]);
			builder.append(RS[i]);
			builder.append(i);
			System.out.println("Currently being added to hashmap is " + builder.toString());
			mapIndexToLsRs.put(i,builder.toString());
		}

		

		


	}
}