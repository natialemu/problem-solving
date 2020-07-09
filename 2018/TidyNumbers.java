import java.util.*;
public class TidyNumbers{

     public static void main(String[] args){
     	   System.out.println("Provide testCases: ");
     	   Scanner scan = new Scanner(System.in);
     	   int testCases = Integer.parseInt(scan.nextLine());
     	   System.out.println("Read in testCases: ");
     	   Set<Long> numbers = new HashSet<>();
     	   for(int i = 0; i < testCases; i++){
     	   	   long N = Long.parseLong(scan.nextLine());

     	   	   long lastTindyNumber = N;

     	   	   for(long k = N; k >=0; k++){
     	   	   	   if(isIncreasing(k,numbers)){
     	   	   	   	 lastTindyNumber = k;
     	   	   	   	 break;
     	   	   	   }

     	   	   }


     	   }
     	
     }


     //so this method is simply going to check if k is in increaing order.
     //to optimize the process, it will check using dynamic programming
     //and also by checking if a sub problem being considered is in the Set of numbers
     //passed into the argument. if that is the case and the already processed sub problem is true, then the method returns true
     public static boolean isIncreasing(long k, Set<Long> numbers){

        StringBuilder rightSide = new StringBuilder();//this will store the right subproblem
        boolean[] M = new boolean[Long.toString(k).length()]; // M[i] represents weather the number starting from i til the end is tidy or not

        //base case:
        M[M.length - 1] = true; //the last digit by itself is considered tidy
        rightSide.append(k%10);   //the first sub problem
        k = k/10;// k, the remaining sub problem, is one less digit 

     	
     	  while(k != 0){
     	  	      k = k/10;// the new sub problem
     	  	      long removedDigit = k%10;// newly removed digit
	              
	              rightSide.append(removedDigit);// append the new digit
	              
	              //you have the left side and the right side
	              

                  // if the remaining sub problem is in the set numbers, the previous sub problem is true and the current removed digit is less or equal to the first number of the previous sub problem
	              if(numbers.contains(Long.parseLong(rightSide.toString())) && M[Long.toString(k).length() - 1] && removedDigit <= ((long)rightSide.charAt(0)) && removedDigit >= k%10) {
	              	  //the whole of k is is increasing
	                  return true;    
	              }else if(M[Long.toString(k).length() - 1] && removedDigit <= ((long)rightSide.charAt(0))){//the previous sub problem is increasing and the new digit is valid
                         M[Long.toString(k).length() - 2] = true;
                         numbers.add(Long.parseLong(rightSide.toString()));

	              }else{
	              	   return false;
	              }

	              	  


	              
     	  }

     	  return false;
	              
	              

     	
     }

}