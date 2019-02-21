import java.util.*;
import java.io.*;

public class UglyNumber {
	public static void main(String[] args){
		// int n = 7;
		// int uglyNumber = findNthUglyNumber(n);
		//System.out.println(n +"th ugly number is " + uglyNumber);
		int n = 10;
		int uglyNumber =findNthUglyNumber(n);
		System.out.println(n + "th ugly number is " + uglyNumber);
		n = 15;
		uglyNumber = findNthUglyNumber(n);
		System.out.println(n + "th ugly number is " + uglyNumber);
		n = 150;
		uglyNumber = findNthUglyNumber(n);
		System.out.println(n + "th ugly number is " + uglyNumber);

	}

	public static int findNthUglyNumber(int n){
		int uglyNums = 1; // 1 is considered an ugly number
		int counter = 2;
		List<Integer> primes = new ArrayList<>();
		Set<Integer> validPrimes = new HashSet<>();
		validPrimes.add(2);
		validPrimes.add(3);
		validPrimes.add(5);
		while(true){
			if(isPrime(counter) && !validPrimes.contains(counter)){
				primes.add(counter);
			} else if(isDivisibleBy235(counter) && notDivisibleByPrimes(primes,counter)){
				uglyNums ++;
				if(uglyNums >= n){
					break;
				}
			}
			counter++;
		}
		return counter;
	}
	public static boolean isPrime(int n){
		int accuracy = 200;
		if (n <= 1 || n == 4) return false; 
        if (n <= 3) return true; 
      
    
	    while (accuracy > 0) 
	    { 
	        // Pick a random number in [2..n-2]      
	        // Above corner cases make sure that n > 4 
	        int a = 2 + (int)(Math.random() % (n - 4));  
	      
	        // Fermat's little theorem 
	        if (power(a, n - 1, n) != 1) 
	            return false; 
	      
	        accuracy--; 
	    } 
      
        return true; 
	}

        /**
        source of method implementation: GeekforGeeks
    	**/
	public static int power(int a,int n, int p) 
    { 

        int res = 1; 
          
        // Update 'a' if 'a' >= p 
        a = a % p;  
      
        while (n > 0) 
        { 
            // If n is odd, multiply 'a' with result 
            if ((n & 1) == 1) 
                res = (res * a) % p; 
      
            // n must be even now 
            n = n >> 1; // n = n/2 
            a = (a * a) % p; 
        } 
        return res; 
    } 

	public static boolean isDivisibleBy235(int num){
		return num%2 == 0 || num%3 == 0  || num%5 == 0;
	}

	public static boolean notDivisibleByPrimes(List<Integer> primes, int num){
		for(Integer prime : primes){
			if(num%prime == 0){
				return false;
			}
		}
		return true;
	}
}