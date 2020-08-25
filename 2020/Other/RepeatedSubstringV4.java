

/**
Algorithm 4:  
	build an augmented suffix trie with repeitition info in each node. then find the longest repeated substring. compute overall repeeatitions and remove it.

**/

public class RepeatedSubstringV4 {


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

		public String longestRepeatedSubstring() {
			//TODO
		}

		public void removeSubstring(String str) {
			//TODO
		}
	}

	public static int allRepeatedSubstrings(String input) {
		SuffixTrie trie = new SuffixTrie(input);
		String longestRepeated = trie.longestRepeatedSubstring();
		int numRepeated = 0;
		while (longestRepeated.equals(EMPTY)) {
			numRepeated += (longestRepeated.length()*longestRepeated.length() - longestRepeated.length())/2;
			trie.removeSubstring(longestRepeated);
			longestRepeated = trie.longestRepeatedSubstring();
		}
		return numRepeated;
		
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