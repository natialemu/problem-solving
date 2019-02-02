import java.util.*;
import java.io.*;

public class ThreeSumClosest{
	public static void main(String[] args){
		//Tests
		int[] testArr1 = {-1, 2, 1, -4};
		int target1 = 1;
		int threeSum = threeSumClosestFinder(testArr1, target1);
		printResult(testArr1, threeSum, target1);


	}


	public static void printResult(int[] arr, int threeSum, int target){
		System.out.print("A sum of three numbers that add to "+ target +" for array: ["+arr[0]);
		for(int i = 1; i < arr.length; i++){
			System.out.print(", "+arr[i]);
		}
		System.out.println("] is " + threeSum);
	}

	public static void printArray(int[] arr){
		System.out.print("["+arr[0]);
		for(int i = 1; i < arr.length; i++){
			System.out.print(", "+arr[i]);
		}
		System.out.println("]");
	}

	public static int threeSumClosestFinder(int[] A, int target){
		Arrays.sort(A); 
		System.out.print("Sorted array: "); 
		printArray(A);
		int min3Sum = Integer.MAX_VALUE;
		for(int i = 0; i < A.length; i++){
			int small = i + 1;
			int big = A.length - 1;
			int currentSum = 0;
			while(small != big) {
				currentSum = A[i] + big + small;
				if(currentSum > target) {
					big--;
				} else if (currentSum > target) {
					small++;
				} else {
					return currentSum;
				}
			}
			if(currentSum <= min3Sum){
				min3Sum = currentSum;
			}
		}
		return min3Sum;
	}
}