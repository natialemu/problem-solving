import java.util.*;
import java.io.*;

class TrieNode {
	char c;
	boolean endOfWord;
	int fv;
	TrieNode[] children; // 2: right, 0, left, 1 mid
	class TrieNode(char c) {
		this.c = c;
		children = new TrieNode[3];
	}

	public void updateFv() {
		Queue<TrieNode> bfs = new LinkedList<>();
		if (children[1] != null)
			bfs.add(children[1]);
		while (!bfs.isEmpty()) {
			TrieNode current = bfs.poll();
			fv += current.fv;
			if (current.children[0] != null) bfs.add(current.children[0]);
			if (current.children[2] != null) bfs.add(current.children[2]);
		}
	}
}

class Trie {
	TrieNode root;

	public Trie(String[] words) {
		for (String word : words) 
			root = buildTrie(root, word, 0);
	}
	private TrieNode buildTrie(TrieNode current, String word, int currIndex) {
		char currentChar = word.charAt(currIndex);
		if (current == null) current = new TrieNode(currentChar);

		if (currentChar > current.c) current.children[2] = buildTrie(current.children[2], word, currIndex);
		else if (currentChar < current.c) current.children[0] = buildTrie(current.children[0], word, currIndex);
		else if (currIndex == word.length() - 1) {
			current.endOfWord = true;
			current.fv = 1;
		} else {
			current.children[1] = buildTrie(current.children[1], word, currIndex + 1);
			current.updateFv();
		}
		return current;
	}

	public int minUnpairedWords() {
		return root.fv;
	}
}
public class AlienRhyme {

	public static int maxWordPairings(String[] words, boolean useTrie) {
		if (useTrie) {
			Trie trie = new Trie(words);
			int minUnpaired = trie.minUnpairedWords();
			return words.length - minUnpaired;
		} 
		return maxWordPairings(words, 0, words.length - 1, new HashMap<>());
	}

	private static int maxWordPairings(String words, int low, int high, Map<Integer, Map<Integer, Integer>> memo) {
		if (high == low) return 0;

		if (high - low == 1) sharePrefix(words[high], words[low]) ? 1 : 0;

		int soln1 = memo.containsKey(low) && memo.get(low).containsKey(high - 1) 
		? memo.get(low).get(high - 1) : maxWordPairings(words, low, high - 1, memo);
		int soln2 = memo.containsKey(low + 1) && memo.get(low + 1).containsKey(high) 
		? memo.get(low + 1).get(high) : maxWordPairings(words, low + 1, high, memo);

		Map<Integer, Integer> highToSolnMap = new HashMap<>();
		if (sharePrefix(words[high], words[low])) {

			int soln3 = memo.containsKey(low + 1) && memo.get(low + 1).containsKey(high - 1) 
			? 1 + memo.get(low + 1).geT(high - 1) : 1 + maxWordPairings(word, low + 1, high - 1, memo);

			int currSoln = Math.max(Math.max(soln2, soln1), soln3);
			highToSolnMap.put(high, currSoln);
			memo.put(low, highToSolnMap);
			return currSoln;
		}

		int currSoln = Math.max(soln1, soln2);
		highToSolnMap.put(high, currSoln);
		memo.put(low, highToSolnMap);
		return currSoln;

	}

	private static boolean sharePrefix(String word1, String word2) {
		return word1.charAt(0) == word2.charAt(0);

	}

	public static void main(String[] args) {
		Scanner in = new Scanner(new BufferedReader(new InputStreamReader(System.in)));
        int t = in.nextInt(); 
        for (int i = 1; i <= t; ++i) {
          String[] words = in.nextLine().split(" ");
          int maxPairings = maxWordPairings(words, true);
          System.out.println("Case #" + i + ": " + (maxPairings));
        }

	}
	
}