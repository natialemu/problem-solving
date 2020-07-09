
/**
    Given an initial state and a final state, determine if a there is a path along the graph that leads to the final state assuming there is an edge between a state A and all the 
    possible states that can be achived by flipping the pancakes using the flipper of size K.

    pseudocode:  store the original state of the pancakes in a list
                 create a method called adjacentStates that calculates all possible next moves given the state of the current board



**/
public class PancakeFlipper{
	  PancakeBoard pancakeBoard = null;

	  class PancakeBoard{
             List<PancakeRange> pancakes = new ArrayList<>();
             int K;
             int totalPancakes;

             class PancakeRange{
             	int count;
             	char value;
             }


            public void fillBoard(String p, int K){

             	//TODO:
             	int pointer1 = 0;
             	int pointer2 = 0;
             	int reset = false;;

                char currentChar = '';
                int currentCount = 0;
             	while(pointer2 < p.length()){
             		if(p.charAt(pointer2) == p.charAt(pointer1)){
             			currentCount++;
             			p.charAt(pointer2);
             			pointer2++;
             			reset = true;
             		}else if(p.charAt(pointer2) != p.charAt(pointer1) && reset){
             			PancakeRange pancakeRange = new PancakeRange();
             			pancakeRange.count = currentCount;
             			pancakeRange.value =currentChar;
             			pancakes.add(pancakeRange);

             			pointer1 = pointer2;
             		    currentCount = 1;
             		    currentChar = p.charAt(pointer2);
             		    pointer2++;
             		}

             	}

             	this.K = K;
             	totalPancakes = p.length();
             }
             public Iterator<PancakeBoard> getAdjacentStates(){
             

                    //PancakeBoard adjacentBoard = flip(startingPos);
                    List<PancakeBoard> adjStates = new ArrayList<>();
                    int count  = 0;
                    while(totalPancakes - count >= K){
                    	PancakeBoard adjacentBoard = flip(count);
                    	adjStates.add(adjacentBoard);
                    	count++;
                    }

                    return adjStates.iterator();


             }

             public boolean equals(Object o){
             	PancakeBoard otherBoard = (PancakeBoard) o;

             	//TODO
             }

             // given the intersecting number, the range intersected and index of the range in the list
             // goal is to break the range into two smaller ranges and add them to the list
             public void breakApart(int middle, PancakeRange pancakeRange, int index, boolean left){
             	PancakeRange leftPancakeRange = new PancakeRange();
             	leftPancakeRange.count = middle;


             	PancakeRange rightPancakeRange = new PancakeRange();

             	rightPancakeRange.count = pancakeRange.count - middle;

             	if(left){
             		if(rightPancakeRange.value == '+')
             			rightPancakeRange.value = '-'
             		else
             			rightPancakeRange.value = '+'
             	}else{
             		if(leftPancakeRange.value == '+')
             			leftPancakeRange.value = '-'
             		else
             			leftPancakeRange.value = '+'
             	}


             	pancakes.remove(pancakeRange);
             	pancakes.add(index,rightPancakeRange);
             	pancakes.add(index, leftPancakeRange);

             }

             //flips the current board, starting from  the given position
             public PancakeBoard flip(int startingPos){
             	PancakeBoard copyboard = copyBoard(this);

             	int count = 0;
             	int start = 0;

             	boolean brokeInitialRange = false;

                    for(int i = 0; i < copyboard.size(); i++){

                    	count += copyboard.get(i).count;

                    	if(count > startingPos){//start flipping
                    		if(!brokeInitialRange && startingPos%K != 0){// nothing's been lipped yet and it is not at the end
                    			brokeInitialRange = true;
                    			breakApart(startPos,copyboard.get(i), i, true);//breaks apart the range and inserts it into the 
                    		}else if(count > K){
                    			breakApart(copyboard.get(i) - (K - count),copyboard.get(i),i, false);
                    			break;
                    		}else{
                    			flipCake(copyboard.get(i)); //flip the entire thing
                    			if( count == K)
                    				break;
                    		}
                    		
                    	}
                    	
                    }

             }


	  }

      int flipperSize;

      public PancakeFlipper(int flipperSize, String pancakes){
      	this.flipperSize = flipperSize;
      	pancakeBoard = new ArrayList<>();
      	pancakeBoard.fillBoard(pancakes,flipperSize);
      	//
      }

      public int flipCount(){

      	//provide an explanation of DFS non-recursive

        /*
            use the stack version of DFS to get the count!!!
            stack
            push the source

            while(stack is not empty){
            	
            	Iterator<Board> = stackPeek.getAdjacentStates
            	if(iterator has next){
	                  //get the value from the iterator
	                  //check if it is equal to the target if so, return count
	                  //push it on the stack
	                  //add count
	                  //
            	}else{
            		decrement count
	               
            	}
	

            }

        */
            Stack<PancakeBoard> boardStack = new Stack<>();

            boardStack.push(pancakeBoard);
            int count = 0;
            while(!boardStack.isEmpty()){
            	Iterator<PancakeBoard> boardIterator = boardStack.peek();
            	if(boardIterator.hasNext()){
            		PancakeBoard currentBoard = boardIterator.next();
            		if(currentBoard.isTarget()){
            			return count;
            		}else{
            			boardStack.push(currentBoard);
            			count++;
            		}

            	}else{
            		boardStack.pop();
            		count--;

            	}
            }
      }





}