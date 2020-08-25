
import java.util.*;
import java.io.*;
/**
Algorithm 3:
	- build an augmented suffix trie(as a ternary trie) with repeitition info in each node. then query the whole trie to count.

**/


public class RepeatedSubstringV3 {

	class SuffixTrie {
		TrieNode root;

		class TrieNode {
			int repetition;
			char c;
			TrieNode left, forward, right;
			public TrieNode(char c) {
				this.c = c;
			}
		}

		public SuffixTrie(String s) {
			for (int i = s.length() - 1; i >= 0; i--) {
				insert(root, s.substring(i), 0);
			}
		}

		private int insert(TrieNode node, String str, int curr) {
			//TODO: Make sure to augment with repeatead
			char currentChar = str.charAt(curr);
			if (node == null) node = new TrieNode(currentChar);

			// it's important the number of repetitions of the siblings. even though it won't affect the number of repetitions of siblings.
			if (currentChar > node.c) return node.repetition + insert(node.right, str, curr);
			else if (currentChar < node.c) return node.repetition + insert(node.left, str, curr);
			else if (curr == str.length() - 1) {
				node.repetition = 1;
				return node.repetition;
			}
			else {
				int numRepeated = insert(node.forward, str, curr + 1)
				node.repetition += numRepeated;
				return numRepeated
			}
		}

		public int traverse() {
			return traverse(root);
		}
		private int traverse(TrieNode current) {
			Queue<TrieNode> siblingIterator = new LinkedList<>();

			siblingIterator.add(current);
			int numRepeated = 0;
			while (!siblingIterator.isEmpty()) {
				TrieNode current = siblingIterator.poll();
				if (current.repetition > 1) numRepeated += 1;
				if (current.left != null) siblingIterator.add(current.left);
				if (current.right != null) siblingIterator.add(current.right);
				if (current.forward != null) numRepeated += traverse(current.forward);
			}
			return numRepeated;
		}
	}

	public static int allRepeatedSubstrings(String input) {

		SuffixTrie trie = new SuffixTrie(input);
		int numReated = trie.traverse();
		return numReated;
		
	}

	public static void main(String[] args) {
		Scanner in = new Scanner(new BufferedReader(new InputStreamReader(System.in)));
        int t = in.nextInt(); 
        in.nextLine();
        for (int i = 1; i <= t; ++i) {
        	String input = in.nextLine(); 
            int n = allRepeatedSubstrings(input);
            System.out.println("Case #" + i + ": " + (n));
        }

	}
	
}