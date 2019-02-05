import java.util.*;
import java.io.*;

     /**
     Algorithm description:
       --> an ugly number is a number whose only prime factors are either 2,3,5
       --> another way of defining ugly numbers: any multiiple, x, of 2,3,5  such that x is not a prime other than 2,3,5
           this means ugly numbers are just ugly number multiples of ugly numbers. so if we know that 1 is an ugly number, we can 
           recursively build up to find the nth ugly number.
     **/
public class UglyNumberDP {

    public static void main(String[] args){
    	//testing code will go here
    	int n = 10;
		int uglyNumber =getUglyNumber(n);
		System.out.println(n + "th ugly number is " + uglyNumber);
		n = 15;
		uglyNumber = getUglyNumber(n);
		System.out.println(n + "th ugly number is " + uglyNumber);
		n = 150;
		uglyNumber = getUglyNumber(n);
		System.out.println(n + "th ugly number is " + uglyNumber);
    }

    public static Integer getUglyNumber(int n){
    	int[] uglyNumbers = new int[n]; // an array for holding the first n ugly numbers
    	uglyNumbers[0] = 1; // initialize first ugly number

    	int uglyPointer2, uglypointer3, uglyPointer5 = 0;  // this will determine what multiple x(which previous ugly number) to multiple 2,3 or 5 with.
        int[] uglyPointers = new int[3];
        uglyPointers[0] = uglyPointer2;
        uglyPointers[1] = uglypointer3;
        uglyPointers[2] = uglyPointer5;

    	for(int i = 1; i < uglyNumbers.length; i++){
    		int minuglyNumber = Integer.MAX_VALUE;
    		int minUglyPointer = 0;
    		// the next ugly number is going to be the smallest product of the previous ugly number and either 2,3, or 5.
    		for(int j = 0; j < uglyPointers.length; j++){
    			int potentialUglyNumber = uglyPointers[j]*uglyNumbers[i-1];
    			if(potentialUglyNumber < minuglyNumber){
    				minuglyNumber = potentialUglyNumber;
    				minUglyPointer = j;
    			}
    		}
    		uglyPointers[minUglyPointer] += 1;// update the prime(either 2,3 or 5) that resulted in the next ugly pointer. so that this ugly number will be the next multiple used in finding the next ugly number with the prime
    	}

        return uglyNumbers[n-1];
    }
}