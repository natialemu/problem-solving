

/**
   The way to solve this problem is through an inorder traversal of a BST:
       Keep track of a hashmap mapping depth of the BST with the list of possibilities at for forming a BST of that depth
       1. while traversing the BST in order:
            keep track of the height of the tree and when the height switches:
                 get a list of all the siblings and get a list of all their permutations
                 copy all the list of possibilities from the prior level
                 append all these prior possibilities with the permutations of current level's siblings
                 put the new list of lists onto the map at the current level

          the solution will then be the value of the map at maximum depth D

**/

public class BSTSequence {

	TreeNode root = null;

	class TreeNode{
		int value;
		TreeNode leftChild;
		TreeNode rightChild;
	}

	public BSTSequence(){
		randomlybuildBST();
	}


	private void randomlybuildBST(){

		//assume this method is implemented
	}

	public List<List<Integer>>  getBSTSequence(){
		return levelOrderTraversal();

	}
 

	public List<List<Integer>>  levelOrderTraversal(){
		Map<Integer, List<List<Integer>> mapLevelToBSTSequence = new HashMap<>();
		//assume root is not null
		if(root != null){

			
			List<TreeNode> queue = new ArrayList<>();
			int depth = 0;
			queue.add(root);
			while(!queue.isEmpty()){
				 List<Integer> siblings = new ArrayList<>();
			     
			     int nodeCount = queue.size();

			     if(nodeCount  != 0){
	                    depth++;
			     }

			     while(nodeCount > 0){
			     	TreeNode dequeued = queue.remove(0);
			     	siblings.add(dequeued);
			     	if(dequeued.leftChild != null){
			     		queue.add(dequeued.leftChild);
			     	}

			     	if(dequeued.rightChild != null){

			     		queue.add(dequeued.rightChild);
			     	}

			     	nodeCount--;
			     }
			     mapLevelToBSTSequence.put(depth,getListOfPermutations(siblings));
			}
			
			return mapLevelToBSTSequence.get(depth);
			     
		}
		return null;

	}








	public List<List<Integer>> getListOfPermutations(List<Integer> siblings){
		List<List<Integer>> permutations = new ArrayList<>();

		getPermutations(new ArrayList<>(),siblings, permutations, siblings.size());
		return permutations;
		/*

		*/
	}

	public void getPermutations(List<Integer> left, List<Integer> siblings, permutations, int siblingSize){
		if(left.size() == siblingSize){
			permutations.add(left);
			return;
		}else{

			for(int i = 0; i < siblings.size(); i++){
				int removedSibling = siblings.remove(i);
				left.add(removedSibling)
				getPermutations(left,siblings,permutations,siblingSize);
			}

		}
	}

	public static void main(String[] args){
		BSTSequence bstSequeuce = new BSTSequence();
		List<List<Integer>> possibleBSTSequences = bstSequeuce.getBSTSequence();

		for(int i = 0; i < possibleBSTSequences.size(); i++){

			for(Intger j : possibleBSTSequences.get(i)){
				System.out.print(j + " ");
			}
			System.out.println();
		}
	}


}