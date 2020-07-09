import java.util.*;
import java.io.*;

class Sequence {
	Map<Integer,Boolean> numbers;

    @Override
	public boolean equals(Object obj){
		Sequence s = (Sequence) obj;
		return s.numbers.entrySet().containsAll(numbers.entrySet());
	}

	@Override
	public int hashCode(){
        int hCode = 0;
		for(Integer i : numbers.keySet()){
			hCode += i.hashCode()%199;
		}
		return hCode%numbers.size();
	}

	public void add(int x){
		numbers.put(x,false);
	}

	public void remove(int x){
		numbers.remove(x);
	}
}
public class GenerateAllSums{

	public static void main(String[] args){
		int testCase1 = 4;
		int testCase2 = 5;
        
  //       System.out.println("-------Using non-memoized recursion ---");
		// //int sumNum2 = getAllIntSumsRecursive(testCase2);
		// int sumNum1 = getAllIntSumsRecursive(testCase1);
		// System.out.println("Number of ways to write " + testCase1 + " as a sum of positive integers: " + sumNum1);
		//System.out.println("Number of ways to write " + testCase2 + " as a sum of positive integers: " + sumNum2);

        System.out.println("-------Using Dynamic programming ---");
		int sumNum1 = getAllIntSumsBottomUpDP(testCase1);
		int sumNum2 = getAllIntSumsBottomUpDP(testCase2);
		System.out.println("Number of ways to write " + testCase1 + " as a sum of positive integers: " + sumNum1);
		System.out.println("Number of ways to write " + testCase2 + " as a sum of positive integers: " + sumNum2);

	}

	public static int getAllIntSumsRecursive(int n){
		List<Integer> numsLessThanN = new ArrayList<>();
		populateList(numsLessThanN, n);
		System.out.print("numbers less than " + n + " --> ");
		printList(numsLessThanN);
		List<Integer> counter = new ArrayList<>();
        counter.add(0);
		getAllIntSumsRecursive(numsLessThanN, counter, 0,n, new Sequence(), new HashSet<Sequence>());
		return counter.get(0);
	}

	private static void printList(List<Integer> l){
		if(l.size() == 0){
			System.out.print("[]");
			return;
		}
		System.out.print("["+l.get(0));
		for(int i = 1; i < l.size(); i++){
			System.out.print(", " + l.get(i));
		}
		System.out.println("]");
	}

	private static void populateList(List<Integer> l, int n){
		for(int i = 1; i <= n; i++){
			l.add(i);
		}
	}

	public static void getAllIntSumsRecursive(List<Integer> a, List<Integer> counter, int currentSum, int n, Sequence sequence, Set<Sequence> seqs){
		//System.out.println("currentSum --> "+ currentSum);
		if(currentSum == n){
			if(!seqs.contains(sequence)){
				counter.set(0,counter.get(0)+1);
				seqs.add(sequence);
			}
			
			return;
		} else if(a.size() == 0){
			return;
		}
		// List<Integer> aDuplicate = new ArrayList<>(a);
		// System.out.print("list before removal of "+aDuplicate.get(0) + " --> ");
		// printList(aDuplicate);
		// int num = aDuplicate.remove(0);
		// System.out.print("list after removal of "+num + " --> ");
		// printList(aDuplicate);
		// System.out.println();

		for(Integer i = 0; i < a.size(); i++){
			int num = a.get(i);
			System.out.print(num + ", ");
			if(currentSum + num <= n){
				sequence.add(a.get(i));
				getAllIntSumsRecursive(a,counter, currentSum + num, n, sequence,seqs);
				sequence.remove(a.get(i));
			}

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
		return L[n-1];
	}
}