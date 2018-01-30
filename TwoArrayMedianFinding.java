

public class TwoArrayMedianFinding{
	public static void main(String[] args){
		int[] array1 ={};
		int[] array2 = {};

		findMedian(array1,array2);
		binaryMedianSearch(array1, array2,0,0,array1.length -1. array2.length-1);
	}

	//O(logn) algorithm

	public static int binaryMedianSearch(int[] a1, int[] a2, int a1Low, int a2Low, int a1high, int a2High){

		if(a1.length == 2 && a2.length == 2){
			return (Math.max(a1[a1Low],a2[a2Low]) + Math.min(a1[a1high],a2[a2High])/2;
		}else{

			/*get median of first possible array*/
			int medianIndex = (a1[a1Low] + a1[a1high])/2;

			int median1 = a1[medianIndex];
			if(a1[a1high] - a1[a1Low]%2 == 0){
				median1 = (a1[medianIndex] + a1[medianIndex+1])/2;
			}

			/*get median of second possible array*/

			int medianIndex2 = (a2[a2Low] + a2[a2high])/2;
			int median2 = a2[medianIndex2];
			if(a2[a2high] - a2[a2Low]%2 == 0){
				median2 = (a2[medianIndex2] + a2[medianIndex2+1])/2;
			}



			/* find new sub arrays by comparing the two medians*/
			if(median1 == median2){
				return median1;
			} else if(median1 > median2){
				binaryMedianSearch(a1,a2,a1Low,medianIndex2,medianIndex,a2High);
			} else{
				binaryMedianSearch(a1,a2,medianIndex,a2Low,a1high,medianIndex2);
			}

		}
	}

	//O(n) algorithm
	public static int findMedian(int[] a1, int[] a2){
		Arrays.sort(a1);
		Arrays.sort(a2);

		/*

		Algorithm 1: two pointer
		*/

		int p1 = 0;
		int p2 = 0;

        int medianCounter = 0;
        //handle cases when sum is even or odd
		while(p1 < a1.length || p2 < a2.length){

			if(a1[p1] > a2[p2]){
				p2++;
				medianCounter++;
				if(medianCounter == (a1.length + a2.length)/2){
					return a2[p2];
				}
			}else if(a1[p1] == a2[p2]){
				p1++;
				p2++;
				medianCounter++;
				if(medianCounter == (a1.length + a2.length)/2){
					return a2[p2];
				}

			}else{
				p1++;
				medianCounter++;
				if(medianCounter == (a1.length + a2.length)/2){
					return a1[p1];
				}


			}
		}

	}
}