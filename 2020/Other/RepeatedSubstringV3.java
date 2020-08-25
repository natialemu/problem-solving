

/**
Algorithm 3:
	- build an augmented suffix trie(as a ternary trie) with repeitition info in each node. then query the whole trie to count.

**/

public class RepeatedSubstringV3 {

	class SuffixTrie {
		TrieNode root;

		class TrieNode {
			int repeatition;
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

		private void insert(TrieNode node, String str, int curr) {
			//TODO: Make sure to augment with repeatead
		}

		public int traverse() {
			return traverse(root);
		}
		private int traverse(TrieNode current) {
			//TODO
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