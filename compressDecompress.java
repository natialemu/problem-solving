
/**
Your input is a compressed string of the format number[string] and the decompressed output form should be the string written number times. 
Number can have more than one digit. For example, 10[a] is allowed, and just means aaaaaaaaaa

One repetition can occur inside another. For example, 2[3[a]b] decompresses into aaabaaab
**/

public class Decompress{
	Sequence originalSequence;

	class Sequence{
		String actualSequence;
		List<Integer> countOfSequences = new ArrayList<>();
		List<Integer> sequences = new ArrayList<>();


		public void put(int count, Sequence sequence){
			countOfSequences.add(count);
			sequences.add(sequence);
		}
	}

	public Decompress(String initialString){
		originalSequence = new Sequence();
        //asumption is that string is valid
		originalSequence.actualSequence = initialString;

	}
	public String decompress(int countOfSequence, Sequence currentSequence){

        /*
        base case: if currentSequence.actualSequence is ONLY letters, return repeatString(countOfSequence, currentSequence.actualSequence)
                   otherse

        */

         if(areAllLetters(currentSequence.actualSequence)){
         	return repeatString(countOfSequence, currentSequence.actualSequence);
         }else{

         	int pointer1 = 0;
			int pointer2 = 0;//for determining the size of number

			for(int i = 0; i < originalSequence.actualSequence.length(); i++){
				if(isNumber(originalSequence.actualSequence.charAt(i)) && originalSequence.actualSequence.charAt(pointer1) == originalSequence.actualSequence.charAt(i)){
					pointer2 = i;
				}else if(originalSequence.actualSequence.charAt(i) == '['){
					int countOfCurrentSequence = Integer.parseInt(originalSequence.actualSequence.subString(pointer1,pointer2));
					pointer1 = pointer2;//both are at [
					String correspondingSequence = getSequence(pointer1, pointer2, originalSequence.actualSequence);
					Sequence sequence = new Sequence();
					sequence.actualSequence = correspondingSequence;
					currentSequence.put(countOfCurrentSequence,sequence);
					i = i + correspondingSequence.length() - 1;//might be 2
					pointer1 = currentSequence.actualSequence.indexOf(correspondingSequence + countOfSequences.length() + 1); //at the ]
					pointer2 = pointer1;
					//make sure both pointer2, pointer1 and i are all at the ] right after corresponding sequence

				}
			}
			/*
			  iterate through the sequence, for each sequence 

			*/

			  SringBuilder outputString = new StringBuilder();

			  for(int i = 0; i < currentSequence.countOfSequences.size()i++){
			  	outputString.append(decompress(currentSequence.countOfSequences.get(i), currentSequence.sequences.get(i)));

			  }
			  return outputString.toString();
         }

		

		
	}

	public String getSquence(int pointer1, int pointer2, String actualSequence){

        Stack<Character> stack = new Stack();
        stack.push('[');
        pointer2++;
        while(true){

        	if(actualSequence.charAt(pointer2) == ']'){
        		char poppedChar = stack.pop();
        		if(stack.isEmpty()){
        			return actualSequence.subString(pointer1, pointer2);
        		}
        	}else if(actualSequence.charAt(pointer2) == '['){
        		stack.push(actualSequence.charAt(pointer2));
        	}

        	if(pointer2 == actualSequence.size() - 1){
        		break;
        	}
        	pointer2++;

        }
        return null;
		//TODO
	}

	public boolean areAllLetters(String sequence){
		//TODO
		String lowerCaseSequence = sequence.toLowerCase();

		for(int i =0; i < sequence.length() ; i++){

			if(sequence.charAt(i)  < 'a' || sequence.charAt(i) > 'z'){
				return false;
			}
		}

		return true;
	}

	public String repeatString(int count, String input){
		//TOOD
		StringBuilder builder = new StringBuilder();
		builder.append(input);
		count--;
		for(int i = 0; i < count; i++){
			builder.append(input);
		}

		return builder.toString();
	}

	public static void main(String[] main){

		String testString = "";
		Decompress d = new Decompress(testString);
		String output = d.decompress();

		System.out.println(output);

	}

}