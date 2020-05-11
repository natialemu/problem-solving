import java.util.*;


/**
Design a search autocomplete system:
- Users type a sentence(at least one word and their sentence ends with #). While typing the sentence, for each character except '#', return the 
top 3 historical hot senteces that have prefix the same as part of the sentence the user already typed. when you read '#', sentence is done. so no 
need to remember what they typed so far.

Full problem description: https://leetcode.com/problems/design-search-autocomplete-system/

**/
interface AutoCompleteSystem {	
	List<String> input(char c);
}


public class AutoCompleteSystemImpl implements AutoCompleteSystem {

	// A ternary trie node that represents each character in the words stored in the trie
	class TrieNode {
		// each node stores sentences that have a prefix ending the this node's character along with the count
		Map<String, Integer> sentenceToHotnessMap; 

		//to better keep track of the order of sentences based on their count
		PriorityQueue<String> maxHeap; 
		boolean endOfWord; // this node's char marks the end of a sentence
		char currentChar;
		TrieNode forwardLink;
		TrieNode leftLink;
		TrieNode rightLink;
		
		public TrieNode(char currentChar) {
			sentenceToHotnessMap = new HashMap<>();

			//instantiate and set the appropriate priority of the priority queue
			maxHeap = new PriorityQueue<>((a,b) -> {
				if (sentenceToHotnessMap.get(a) != sentenceToHotnessMap.get(b)) return sentenceToHotnessMap.get(b) - sentenceToHotnessMap.get(a);
				return a.compareTo(b);
			});
			this.currentChar = currentChar;
		}

		public void add(String sentence, int hotness) {
			// if this is a new sentence, add it and set its count to 1. Otherwise, update its count
			sentenceToHotnessMap.put(sentence, sentenceToHotnessMap.getOrDefault(sentence, 0) + hotness);

			// update the priority of the new or updated word
			Stack<String> temp = new Stack<>();
			while (!maxHeap.isEmpty() && !maxHeap.peek().equals(sentence)) temp.push(maxHeap.poll());
			if (!maxHeap.isEmpty()) maxHeap.poll();
			while (!temp.isEmpty()) maxHeap.add(temp.pop());
			maxHeap.add(sentence);
		}

		public List<String> topSentences() {
			List<String> topThreeSentences = new ArrayList<>();
			int count = 0;
			Stack<String> temp = new Stack<>();

			// retrieve the top three sentences from the PQ
			while ( !maxHeap.isEmpty() && count < 3)  {
				temp.push(maxHeap.poll());
				count+=1;
			}
			while (!temp.isEmpty()) {
				String s = temp.pop();
				topThreeSentences.add(s);
				maxHeap.add(s);
			}
			Collections.reverse(topThreeSentences);
			return topThreeSentences;
		}



	}

	TrieNode root; // Represents the root of the trie
	TrieNode current; // will point to the current node in the trie as clients are typing 
	StringBuilder typedSoFar; // keep track of the string typed so far by the client

	public AutoCompleteSystemImpl(String[] sentences, int[] times) {
		typedSoFar = new StringBuilder();
		if (sentences.length != times.length) throw new IllegalArgumentException("Input input. Array sizes of sentences and times don't match.");

		//insert each sentence along with its count
		for (int i = 0; i < sentences.length; i++) root = insert(root, sentences[i], times[i], 0); 
		current = root;
	}

    // Insert 'sentence' into the trie and increase its count by 'count'
	private TrieNode insert(TrieNode node, String sentence, int count, int index) {
		// current character being inserted
		char currentChar = sentence.charAt(index);
		if (node == null) {
			node = new TrieNode(currentChar);
		}
		// follow the appropriate link depending on the current character's value
		if (currentChar > node.currentChar) node.rightLink =  insert(node.rightLink, sentence, count, index);
		else if (currentChar < node.currentChar) node.leftLink = insert(node.leftLink, sentence, count, index);
		else if (index == sentence.length() - 1) {
			// if inserting the last character. then a setence has just formed
			node.endOfWord = true;
			// add the new sentence and increment its count by 'count'
			node.add(sentence, count);
		} else  {
			node.forwardLink = insert(node.forwardLink, sentence, count, index + 1);
			// either update or add the new sentence and increment its count by 'count'
			node.add(sentence, count);
		}
		return node;
	}
	


	@Override
	public List<String> input(char c) {
		if (c == '#')  {
			// if client is done typing, either update the count of the typed sentence by 1 or 
			// insert the sentence if it is a new one with 1
			root = insert(root, typedSoFar.toString(), 1, 0);
			current = root; // reset the pointer that tracks the client
			typedSoFar = new StringBuilder(); // reset the string
			return new ArrayList<>();
		}
		typedSoFar.append(c); 
		List<String> allMatches = find(current, c);

		// moves the current node forwrad in anticipation of the next character the client will type
		current = current.forwardLink;
		return allMatches;
	}

	// finds the node out of all siblings of 'node' that has a character of  'c' and returns the
	// top sentences of that node
	public List<String> find(TrieNode node, char c) {
			Queue<TrieNode> bfs = new LinkedList<>();
			bfs.add(node);
			while(!bfs.isEmpty()) {
				TrieNode curr = bfs.poll();
				if (curr.currentChar == c) return curr.topSentences();
			
				if (curr.leftLink != null) bfs.add(curr.leftLink);
				if (curr.rightLink != null) bfs.add(curr.rightLink);
			}
			return new ArrayList<>();
		}

	public static void main(String[] args) {
		AutoCompleteSystem autocomplete = new AutoCompleteSystemImpl(new String[] {"i love you", "island", "ironman", "i love leetcode"}, new int[] {5, 3, 2,2});
		List<String> suggestions = autocomplete.input('i');
		print("Suggestions after typing 'i': ", suggestions);
		suggestions = autocomplete.input(' ');
		print("Suggestions after typing 'i ': ", suggestions);
		suggestions = autocomplete.input('a');
		print("Suggestions after typing 'i a': ", suggestions);
		suggestions = autocomplete.input('#');
		print("Suggestions after typing '#': ", suggestions);
	}
	private static void print(String msg, List<String> suggestions) {
		System.out.println(msg);
		for(String suggestion : suggestions) {
			System.out.println("-" + suggestion);
		}
		System.out.println("------------------------------");
	}
}