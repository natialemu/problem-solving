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
		//TODO
		Tree
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
			areMirrorImages(root1.left, root2.right);
			areMirrorImages(root1.right, root2.left);
			return true;
		}
	}
}