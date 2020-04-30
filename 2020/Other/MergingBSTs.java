import java.util.*;
/**
Merging two BSTs

Problem: Given the roots to two BSTs, print the nodes of the merged BSTs in order.

option 1: 
- add each element of the first BST into BST 2 or viceversa. 
- Then traverse the merged BST in order & print the value of the nodes.
- runtime: nlogn where n is the total number of elements of both BSTs. No extra space needed

option 2: 
- traverse both BSTs in-order independently using two stacks and 'merge' when appropriate
  simillar to merging two sorted arrays. Don't progress the in-order traversal of a BST 'b' 
  unless the current node of 'b' that's being compared to the current node of BST 'a' is smaller or equal.
  The same rule applies for BST 'a' as well.
- Hard to achieve such co-ordination between the two BSTs if they both share the same stack (attempting to use recursion).

- Runtime : O(n) with O(n) additional space to keep track of visited nodes (although not neccessary).


Pseudocode for in-order traversal of a single tree  without recursion
- Initialize a stack
- push root to stack
while stack is not empty
	- while the node at the top of the stack has a left child and the left child hasnt been visited:
		push left child to the stack
	- visit the node at the top of the stack
	- push the right child of the visited element if it exists.

Runtime:O(n) time where n is the total number of nodes in both BSTs. additional space of O(n).
**/
class TreeNode {

	int value;
	TreeNode left;
	TreeNode right;
	public TreeNode() {}
	public TreeNode(int value, TreeNode left, TreeNode right) {
		this.value = value;
		this.left = left;
		this.right = right;
	}
}

public class MergingBSTs {

	public static void merge(TreeNode node1, TreeNode node2) {

		// stack for the in-order traverseal of BST rooted at node1
		Stack<TreeNode> stackForFirstBST = new Stack<TreeNode>();

		// keep track of visited nodes while traversing the BST rooted at node1
		Set<TreeNode> visitedFirstBST = new HashSet<>();

		//stack for the in-order traversal of BST rooted at node2
		Stack<TreeNode> stackForSecondBST = new Stack<TreeNode>();

		// keep track of visited nodes while traversing the BST rooted at node2
		Set<TreeNode> visitedSecondBST = new HashSet<>();
		stackForFirstBST.add(node1);
		stackForSecondBST.add(node2);

		while (!stackForFirstBST.isEmpty() || !stackForSecondBST.isEmpty()) {
			//progress down the left sub tree for both trees until there is no left sub tree
			runDownLeftSubTree(stackForFirstBST, visitedFirstBST);
			runDownLeftSubTree(stackForSecondBST, visitedSecondBST);


			TreeNode firstBSTvalue = stackForFirstBST.isEmpty() ? null : stackForFirstBST.peek();
			TreeNode secondBSTvalue = stackForSecondBST.isEmpty() ? null : stackForSecondBST.peek();

			// if the current node's value from the first BST is less than that of the node of the second
			//BST, print node1's value and push it's right child if it exists. Pause traversal of BST two 
			//until its current value is less than that of BST one's
			if (firstBSTvalue == null || 
				(firstBSTvalue != null && secondBSTvalue != null &&  secondBSTvalue.value < firstBSTvalue.value)) {
				progress(stackForSecondBST, visitedSecondBST);
			}  else if (secondBSTvalue == null || 
				((firstBSTvalue != null && secondBSTvalue != null) && firstBSTvalue.value < secondBSTvalue.value) ) {
				progress(stackForFirstBST, visitedFirstBST);
			} else {
				progress(stackForSecondBST, visitedSecondBST);
				progress(stackForFirstBST, visitedFirstBST);
			}
		}
	}

	//print the value of the node at the top of the stack & add it's right child
	//to the stack if it exists
	private static void progress(Stack<TreeNode> nodes, Set<TreeNode> visited) {
		TreeNode bstNode = nodes.pop();
		visited.add(bstNode);
		System.out.println(bstNode.value);
		if (bstNode.right != null) {
			nodes.push(bstNode.right);
		}
	}



	private static void runDownLeftSubTree(Stack<TreeNode> treeStack, Set<TreeNode> visited) {

		// while the node at the top of the stack has a left child that hasn't been visited yet
		// push the left child to the stack
		while (!treeStack.isEmpty() && treeStack.peek().left != null 
			&&  !visited.contains(treeStack.peek().left)) {
			treeStack.push(treeStack.peek().left);
		} 
	}

	public static void main(String[] args) {
		TreeNode root1 = new TreeNode(
			3,
			new TreeNode(
				1,
				null,
				null),
			new TreeNode(
				5,
				null,
				null));


		TreeNode root2 = new TreeNode(
			4,
			new TreeNode(
				2,
				null,
				null),
			new TreeNode(
				6,
				null,
				null));
	   	    merge(root1, root2); 
	}
}
