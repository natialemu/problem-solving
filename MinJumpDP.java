import java.util.*;
import java.io.*;
/**

Problem: Given an array of non-negative integers, you're initially positioned at the first index of the array. Each element in the array represents your max jump length
         at that position. your goal is to reach the last index in the minimum number of jumps.

**/
public class MinJumpDP {
	public static void main(String[] args){
		int[] testCode = {2,3,1,1,4};
		int minJump = findMinJumpDP(testCode);
		printResult(testCode,minJump);
		//test code here
	}

	public static void printResult(int[] arr, int minJump){
		System.out.print("Min jump for array: ["+arr[0]);
		for(int i = 1; i < arr.length; i++){
			System.out.print(", "+arr[i]);
		}
		System.out.println("] is " + minJump);
	}

	public static int findMinJumpDP(int[] input){
		int[] L = new int[input.length]; // for any k, j such that j < k < input.length and j - k is within input[j], L[j] = Min(L[k])
		L[L.length - 1] = 0; //Base case: if we're at the end already(at index L.length - 1), then minJump is 0

		for(int k = input.length - 2; k >= 0; k--) { // iterate through positions before the end
			L[k] = Integer.MAX_VALUE;

			for(int i = k + 1; i < L.length; i++) { // for all sub problems visited before
				if(i - k <= input[k] && L[i] + 1 < L[k]) { // check if it is possible to reach that sub problem from our current subproblem
					L[k] = L[i] + 1; // update value of the current subproblem 
				}
			}
		}
		return L[0];

	}
}