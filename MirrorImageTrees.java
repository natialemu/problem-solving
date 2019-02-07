import java.util.*;
import java.io.*;

class Tree {
	int value;
	Tree left;
	Tree right;
	public Tree(int value, Tree left, Tree right){
		this.value = value;
		this.left = left;
		this.right = right;
	}
}
public class MirrorImageTrees{
	public static void main(String[] args){
		//check testcase from cracking coding interview

		Tree tree1 = new Tree(3,
			new Tree(2,
				new Tree(7, null, null),
				new Tree(8, null, null)),
			new Tree(1,
				new Tree(10, null, null),
				new Tree(0, null, null)));

		Tree tree2 = new Tree(3,
			new Tree(1,
				new Tree(0, null, null),
				new Tree(10, null, null)),
			new Tree(2,
				new Tree(8, null, null),
				new Tree(7, null, null)));

		Tree tree3 = new Tree(3,
			new Tree(1,
				new Tree(8, null, null),
				new Tree(7, null, null)),
			new Tree(2,
				new Tree(0, null, null),
				new Tree(10, null, null)));

		boolean areMirrorImages12 = areMirrorImages(tree1, tree2);
		boolean areMirrorImages13 = areMirrorImages(tree1, tree3);

		System.out.println("are tree 1 and 2 mirror images? " + areMirrorImages12);
		System.out.println("are tree 1 and 3 mirror images? " + areMirrorImages13);

	}

	public static boolean areMirrorImages(Tree root1, Tree root2){
		if(root1 == null && root2 == null){
			return true;
		} else if(root1 == null || root2 == null){
			return false;
		} else { 
			if(root1.value != root2.value){
				return false;
			}
			boolean result1 = areMirrorImages(root1.left, root2.right);
			boolean result2 = areMirrorImages(root1.right, root2.left);
			return result1 && result2;
		}
	}
}