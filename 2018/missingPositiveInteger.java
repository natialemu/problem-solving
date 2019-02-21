import java.util.*;
import java.io.*;

/**
Problem: Given an unsorted integer array, find the smallest missing positive integer.

Runtime of algorithm: O(n)
**/ 
public class MissingPositiveInteger {

	public static void main(String[] args){
		Integer[] testCase1 = {-1,1,2,3};
		Integer[] testCase2 = {-4,2,3};
		Integer[] testCase3 = {1,2,3,5,6,7};
		Integer[] testCase4 = {};

		int missingMin1 = findMissingMin(testCase1);
		int missingMin2 = findMissingMin(testCase2);
		int missingMin3 = findMissingMin(testCase3);
		int missingMin4 = findMissingMin(testCase4);

		System.out.println("The missing minimum for testcase1 is: " + missingMin1);
		System.out.println("The missing minimum for testcase2 is: " + missingMin2);
		System.out.println("The missing minimum for testcase3 is: " + missingMin3);
		System.out.println("The missing minimum for testcase4 is: " + missingMin4);

	}

	public static int findMissingMin(Integer[] input){
		List<Integer> filteredInput = new ArrayList<>();

		for(Integer i : input){
			if(i >0){
				filteredInput.add(i);
			}
		}
		return findMissingMin(filteredInput,0);
	}

	public static int findMissingMin(List<Integer> input, int frame){

		if(input.size() == 0){
			return frame + 1;
		}
		List<Integer> left = new ArrayList<>();
		List<Integer> right = new ArrayList<>();

		int randomPivotIndex = (int) (Math.random()*input.size());
		int randomPivot = input.get(randomPivotIndex);
		System.out.println("Selected pivot: " + randomPivot);
		partitionOnlyPositives(input,left,right,randomPivot);
		if(input.size() == 1 && left.size() == (randomPivot - frame - 1)){
			return randomPivot + 1;
		}else if(input.size() == 1 && left.size() < (randomPivot - frame - 1)){
			return randomPivot - 1;
		}else if(left.size() == (randomPivot - frame - 1)){
			return findMissingMin(right,randomPivot);
		}else{
			return findMissingMin(left,frame);
		}
	}

	public static void partitionOnlyPositives(List<Integer> input, List<Integer> left, 
		List<Integer> right, int randomPivot){
		for(Integer i:input){
			if(i < randomPivot){
				left.add(i);
			}else if(i > randomPivot) {
				right.add(i);
			}
		}
	}

}