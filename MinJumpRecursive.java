import java.util.*;
import java.io.*;

public class MinJumpRecursive {

	public static void main(String[] args){
		//test code
		int[] testCode = {2,3,1,1,4};
		int minJump = findMinJump(testCode, 0);
		printResult(testCode,minJump);

	}

	public static void printResult(int[] arr, int minJump){
		System.out.print("Min jump for array: ["+arr[0]);
		for(int i = 1; i < arr.length; i++){
			System.out.print(", "+arr[i]);
		}
		System.out.println("] is " + minJump);
	}

	public static int findMinJump(int[] input, int ptr){//ptr keeps track of where we currently are
		if(ptr == input.length - 1) { // we're at the end so no need to jump further
			return 0;
		} else {
			int minJump = Integer.MAX_VALUE;
			for(int j = 1; j <= input[ptr]; j++){ // go through all possible number of jumps
				if(ptr + j <= input.length){ // ask if the jump is valid. it is not valid if we're gonna go out of bounds
					ptr += j; // take the jump
					int currentMin = findMinJump(input, ptr) + 1; // recurse from the new location
					if(currentMin < minJump){ // compare the solution from the smaller sub-problem to our minJump
						minJump = currentMin;
					}
					ptr -= j; // undo the jump and go all over again with another possible jump
				}
			}
			return minJump;
		}
	}
}