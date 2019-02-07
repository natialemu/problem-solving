import java.util.*;
import java.io.*;

public class GenerateAllSums{

	public static void main(String[] args){
		int testCase1 = 4;
		int testCase2 = 5;
        
        System.out.println("-------Using non-memoized recursion ---");
		int sumNum2 = getAllIntSumsRecursive(testCase2);
		int sumNum1 = getAllIntSumsRecursive(testCase1);
		System.out.println("Number of ways to write " + testCase1 + " as a sum of positive integers: " + sumNum1);
		System.out.println("Number of ways to write " + testCase2 + " as a sum of positive integers: " + sumNum2);

        System.out.println("-------Using Dynamic programming ---");
		sumNum1 = getAllIntSumsBottomUpDP(testCase1);
		sumNum2 = getAllIntSumsBottomUpDP(testCase2);
		System.out.println("Number of ways to write " + testCase1 + " as a sum of positive integers: " + sumNum1);
		System.out.println("Number of ways to write " + testCase2 + " as a sum of positive integers: " + sumNum2);

	}

	public static int getAllIntSumsRecursive(int n){
		List<Integer> numsLessThanN = new ArrayList<>();
		populateList(numsLessThanN, n);
		List<Integer> counter = new ArrayList<>();
        counter.add(0);
		getAllIntSumsRecursive(numsLessThanN, counter, 0,0);
		return counter.get(0);


	}

	private static void populateList(List<Integer> l, int n){
		for(int i = 1; i < n; i++){
			l.add(i);
		}
	}

	public static void getAllIntSumsRecursive(List<Integer> a, List<Integer> counter, int currentSum, int n){
		if(a.size() == 0 && currentSum == n){
			counter.add(counter.get(0)+1);
			return;
		} else if(a.size() == 0){
			return;
		}
		int num = a.remove(0);
		if(currentSum + num < n){
			getAllIntSumsRecursive(a,counter,currentSum+num, n);
		}else if(currentSum + num > n){
			getAllIntSumsRecursive(a,counter,currentSum,n);
		} else{
			counter.add(counter.get(0)+1);
		}

	}

	public static Integer getAllIntSumsBottomUpDP(int n){
		int[] L = new int[n]; //let L[i] represent the number of ways to write i as a sum of positive numbers less than it
		L[0] = 0;
		L[1] = 0;

		for(int l = 2; l < n; l++){
			int overallSum = 0;
			for(int k = 0; k < l; k++){
				overallSum += L[k] + (l - k);
			}
			L[l] = overallSum;
		}
		return L[n];
	}
}