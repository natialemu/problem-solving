

/**
- long road from west to east.
- sequence of S mysterious road signs

int[] west
int[] east
int[] D


base case: i == 0, just return the legnth which is 1 &
    set of possible M, N points
solution:
for each point 1 ... |D| & set of possible M, N points so far
    evaluate that point:
        - either that point will be part of your contiguous set
          bc it one of the possible pair of M, N points is valid for it
         OR
        - you can start a new contigous set starting from that point with a 
        new set of possible M, N points
        
        // whichever of the two yields the best result wins
        
        

possible problems?
- how to get all possible M, N points
- how to efficiently check if i+1st sign is compatible with 
the pair of points formed by the contigous sequence ending @ i



------
criteria:
there exists some M & N that is applicable for every
member of the set.

if that element is 1, it's easy.

find two numbers i & j
        
        
**/
public class MysteriousRoadSigns {
	
}