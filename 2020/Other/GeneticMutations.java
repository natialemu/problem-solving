
import java.util.*;
import java.io.*;

/**

Problem definition:
The goal is to mutate the original sequence of genes, 'start', to 'end'. However, each mutated string
must be in the gene bank. 


Input constraints:
start & end: sequence of 'A', 'T', 'C', 'G'
bank: is a collection of strings of simillar structure to that of 'start' & 'end'
the length of start is equal to the length of end. Also each gene sequence in the bank has the same length (same as start & end).
Two requirements:
	1. return the minimum number of mutations. return -1 if it is not possible
	2. return the sequence of mutations that led to the final mutation. return an empty list if that's not possible.

Input structure:
T
start End
n
GeneInBank1
GeneInBank2
GeneInBankn

Test cases:
3
AACCGGTT AACCGGTA
1
AACCGGTA
AACCGGTT AAACGGTA
3
AACCGGTA
AACCGCTA
AAACGGTA
AAAAACCC AACCCCCC
3
AAAACCCC
AAACCCCC
AACCCCCC

**/

class Result {
	int numMutations;
	List<String> geneSequence = new ArrayList<>();

	public Result(int numMutations, List<String> geneSequence) {
		this.numMutations = numMutations;
		this.geneSequence = geneSequence;
	}
}
class TrieNode {
	char currentChar;
	boolean wordEnds;
	TrieNode forwardLink;
	TrieNode leftLink;
	TrieNode rightLink;
}
class GeneBank {
	TrieNode root;

	public GeneBank(String[] bank) {
		for (String word : bank) {
			root = insert(root, word, 0);
		}
		
	}

	private TrieNode insert(TrieNode current, String word, int currIndex) {
		char currentChar = word.charAt(currIndex);
		if (current == null) {
			current = new TrieNode();
			current.currentChar = currentChar;
		}

		if (currentChar < current.currentChar) {
			current.leftLink = insert(current.leftLink, word, currIndex);
		} else if (currentChar > current.currentChar) {
			current.rightLink = insert(current.rightLink, word, currIndex);
		} else if (currIndex == word.length() - 1) { // last char
			current.wordEnds = true;
		} else {
			current.forwardLink = insert(current.forwardLink, word, currIndex + 1);
		}
		return current;
	}


	//Can be further optimized by using a trie data structure to represent the bank
	public boolean contains(String geneSequence) {
		return find(root, geneSequence, 0);
	}

	private boolean find(TrieNode current, String word, int currIndex) {
		char currentChar = word.charAt(currIndex);
		if (current == null) {
			return false;
		}

		if (currentChar < current.currentChar) {
			return find(current.leftLink, word, currIndex);
		} else if (currentChar > current.currentChar) {
			return find(current.rightLink, word, currIndex);
		} else if (currIndex == word.length() - 1) { // last char
			return current.wordEnds;
		} else {
			return find(current.forwardLink, word, currIndex + 1);
		}
	}
}
public class GeneticMutations {
	private final static List<Character> GENES = Arrays.asList('A', 'T', 'C', 'G');
	public static Result minMutations(String start, String end, String[] bank) {
		GeneBank geneBank = new GeneBank(bank);
		Queue<String> bfs = new LinkedList<>();

		//add starting gene sequenece 
		bfs.add(start);

		int minMutations = 0;

		// keep track of how a gene mutated
		Map<String, String> geneMutatedFrom = new HashMap<>();
		Set<String> duplicateCheck = new HashSet<>(); // keeps track of all the mutations seen so far
		duplicateCheck.add(start);
		boolean targetFound = false;
		while (!bfs.isEmpty()) {
			int queueSize = bfs.size();
			for (int i = 0; i < queueSize; i++) {
				String currentGeneSequence = bfs.poll();
				if (currentGeneSequence.equals(end)) {
					targetFound = true;
					break;
				}
				for (String mutatedGene : retrieveValidGeneMutations(currentGeneSequence, geneBank, duplicateCheck)) {
					// 'mutatedGene' mutated from 'currentGeneSequence'
					geneMutatedFrom.put(mutatedGene, currentGeneSequence);
					bfs.add(mutatedGene);
					duplicateCheck.add(mutatedGene);

				}
			}
			if (targetFound) {
				break;
			}
			minMutations +=1;
		}
		List<String> geneMutations = new LinkedList<>();
		if (targetFound)
			buildGeneMutations(geneMutatedFrom, end, start, geneMutations);
		else 
			minMutations = -1;
		Result finalResult = new Result(minMutations, geneMutations);
		return finalResult;
	}

	private static List<String> retrieveValidGeneMutations(String geneSequnce, GeneBank bank, Set<String> duplicateCheck ) {
		List<String> allValidMutations = new ArrayList<>();

		// for each gene of the gene sequence, try to mutate the gene to all other valid mutations and see
		// if the newly mutated gene sequence is in the gene bank. if so, add it to the list which will eventually 
		// be returned
		for (int index = 0; index < geneSequnce.length(); index++) {
			char currentGene = geneSequnce.charAt(index);
			for (Character gene : GENES) {
				if (currentGene != gene) {
					String potentialMutatedGene = geneSequnce.substring(0, index)  + gene + geneSequnce.substring(index + 1);
					// if the mutated gene is in the bank
					if (bank.contains(potentialMutatedGene) && !duplicateCheck.contains(potentialMutatedGene)) {
						allValidMutations.add(potentialMutatedGene);
					}
				}
			}
		}

		return allValidMutations;
	}

	private static void buildGeneMutations(Map<String, String> geneMutatedFrom, String end, String start, List<String> geneMutations) {

		String currentMutation = end;
		int i = 0;
		while (!currentMutation.equals(start)) {
			geneMutations.add(0, currentMutation);
			currentMutation = geneMutatedFrom.get(currentMutation);
			i++;
			if (i > 4) {
				break;
			}
		}
		geneMutations.add(0, start);
	}

	public static void main(String[] args) {
		Scanner in = new Scanner(new BufferedReader(new InputStreamReader(System.in)));
        int t = in.nextInt(); // test cases
        in.nextLine();
        for (int i = 1; i <= t; ++i) {
          String[] genes = in.nextLine().split(" ");
          assert (genes.length == 2);
          String start = genes[0];
          String end = genes[1];
          int numGenesInBank = in.nextInt();
          in.nextLine();
          String[] bank = new String[numGenesInBank];
          for (int j = 0; j < numGenesInBank; j++) {
              String gene = in.nextLine().trim();
              bank[j] = gene;             
          }
          Result r = minMutations(start, end, bank);
          System.out.println("Case #" + i + ": " + (r.numMutations));
          if (r.numMutations != -1) {
          	printMutations(r.geneSequence);
          }
        }
	}

	private static void printMutations(List<String> mutations) {

		for (int i = 0; i < mutations.size() - 1; i++) {
			System.out.print(mutations.get(i) + " -> ");
		}
		System.out.println(mutations.get(mutations.size() - 1));
	}
	
}