
import java.util.*;
import java.io.*;
import java.util.stream.Collectors; 
/**
Longest duplicate substring: https://leetcode.com/problems/longest-duplicate-substring/

**/


/**
Each node contans the count of a prefix ending at at node in the suffix tree. This is equivalent to the number of 
occurrences of a substring starting at the root of the suffix tree ending at the node. Any prefix of a suffix tree is a substring
of the string represented by the suffix tree.
**/
class TrieNode {
	int count;
	char currentChar;
	TrieNode[] links;
	public TrieNode(char currentChar) {
		this.currentChar = currentChar;
		links = new TrieNode[3];
	}
}

public class StringDuplicate {
	TrieNode root;
	public String longestDuplicateSubstring(String s) {
		root = null;
		// insert each suffix of s into the trie.
		for (int i = 0; i < s.length(); i++) {
			root = insert(root, s.substring(i), 0);
		}
		String longestDupSubstring = getLds(root, new ArrayList<>());
		return  longestDupSubstring;
	}

	private TrieNode insert(TrieNode current, String str, int index) {
		// current character about to be inserted into the trie
		char currentChar = str.charAt(index);
		// if there's no node to hold the current character, create one
		if (current == null) {
			current = new TrieNode(currentChar);
		} 
		// if all the characters of the str have been inserted
		if (current.currentChar == currentChar && index == str.length() - 1){
			// update count to 1 since the inserted suffix(which is also substring) occurs at least once
			current.count += 1;
			return current;
		}
		if (currentChar < current.currentChar) {
			// if the current character about to be inserted is less than the character in the current node,
			// traverse the left sub strie and continue to insert the current node
			current.links[0] = insert(current.links[0], str, index);
		} else if (currentChar > current.currentChar) {
			// if the current character about to be inserted is greater than the character in the current node,
			// traverse the right sub trie and continue to insert the current node
			current.links[2] = insert(current.links[2], str, index);
		} else {
			// if the current character about to be inserted is the same as the character in the current node,
			// traverse forward and match the next character of the string
			current.links[1] = insert(current.links[1], str, index + 1);
			//update the count of all prefixes of the suffix that was just inserted.
			// this is because each prefix of the suffix is a substring of the string
			// only nodes backtracking from forward edges need to have their counts incremented because
			// only forward edges represent character matches.
			current.count += 1;
		}
		return current;
	}

	/**
	Traverse the suffix trie to get the longest prefix in the suffix trie(aka substring of the originals string) that occurs
	at least twice. 
	path: keeps track of the 'active' prefixes.
	**/
	private String getLds(TrieNode current, List<Character> path) {
		// leaves in the suffix tree
		if (current.links[0]  == null && current.links[2] == null && current.links[1] == null) {
			String substringSofar = getSubstring(path, current.currentChar);//convert the path to a string
			// if the string represented by the path occurs at least once, return it as a possible solution. Otherwise return ""
			String bestSoFar = current.count > 1 ? substringSofar : ""; 
			//remove the character
			path.remove(path.size() - 1);
			return bestSoFar;
		}
		String leftSoln = null; // longest duplicate substring from left sub trie
		//travese left sub trie. nothing is appended here since paths are ONLY build by going forward
		if (current.links[0] != null) {
			leftSoln = getLds(current.links[0], path);
		}
		String rightSoln = null; // longest duplicate substring from right sub trie
		//travese right sub trie. nothing is appended here since paths are ONLY build by going forward
		if (current.links[2] != null) {
			rightSoln = getLds(current.links[2], path);
		}
		String forwardSoln = null; // longest duplicate substring from forward sub trie
		if (current.links[1] != null) {
			path.add(current.currentChar);
			forwardSoln = getLds(current.links[1], path);
			path.remove(path.size() - 1);
		}
		// the substring ending in the current node
		String substringSofar = getSubstring(path, current.currentChar);
		// list of possible longest duplicate substrings ending at least at the current node
		List<String> possibleSolutions = Arrays.asList(leftSoln, rightSoln, forwardSoln, current.count > 1 ? substringSofar : null);
		// find the substring with max length out of the possible solutions
		String currSoln = getMaxLengthedString(possibleSolutions);
		//remove added character from current node
		path.remove(path.size() - 1);
		return currSoln;
	}

	//append current char to path and return string form
	private String getSubstring(List<Character> path, char currentChar) {
		path.add(currentChar);
		return path.stream().map(String::valueOf).collect(Collectors.joining());
	}
	private String getMaxLengthedString(List<String> substrings) {
		String maxLengthedString = "";
		// check with of the non null possible solutions has max length 
		for (int i = 0; i < substrings.size(); i++) {
			if (substrings.get(i) != null && substrings.get(i).length() > maxLengthedString.length()) {
				maxLengthedString = substrings.get(i);
			}
		}
		return maxLengthedString;
	}

	public static void main(String[] args) {
		StringDuplicate duplicate = new StringDuplicate();
		Scanner in = new Scanner(new BufferedReader(new InputStreamReader(System.in)));
        int t = in.nextInt(); 
        in.nextLine();
        for (int i = 1; i <= t; ++i) {
          String str = in.nextLine().trim();
          //System.out.println(str);
          String sol = duplicate.longestDuplicateSubstring(str);
          System.out.println("Case #" + i + ": " + (sol));
      }
	}

}