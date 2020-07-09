import java.util.*;
import java.io.*;
class Node {
    	Node next;
    	int value;

    	public Node(int value, Node next){
    		this.value = value;
    		this.next = next;
    	}
}
public class MergeKListDivideConquer{


	public static void main(String[] args){
		//Test go here
		Node list1 = new Node(1,
			new Node(4,
				new Node(5,null)));

		Node list2 = new Node(1,
			new Node(3,
				new Node(4, null)));
		Node list3 = new Node(2,
			new Node(6, null));

		List<Node> lists = new ArrayList<>();
		lists.add(list1);
		lists.add(list2);
		lists.add(list3);

		Node mergedNode = mergeKLists(lists, 0, lists.size() - 1);

		printMergedList(mergedNode);

	}

	public static void printMergedList(Node mergedNode){
		assert(mergedNode != null);

        Node runner = mergedNode;
        System.out.print("[" + runner.value);
        runner = runner.next;
		while(runner != null){
			System.out.print(", "+runner.value);
			runner = runner.next;
		}
		System.out.println("]");
	}

	public static Node mergeKLists(List<Node> lists, int low, int high){ //[]
		if (high - low < 1){ // base case:  we only have one node in this sub problem
			return lists.get(high); // return the node
		}

		int mid = (high + low)/2;

		Node a = mergeKLists(lists,low,mid); //recurse on the first half
		Node b = mergeKLists(lists,mid+1,high); // recurse on the other half

		return mergeTwoLists(a,b); // merge the two lists returned by two recursive calls
	}

	public static Node mergeTwoLists(Node a, Node b){
		if(a == null){ // if you've reached the end of a, just return all of b since b is sorted.
			return b;
		}

		if(b == null) {
			return a; //. if you've reached the end of b, just return whatever of a is left. a is sorted
		}

		Node mergedList = null;
		if(a.value < b.value){// if a is smaller, select it and append it to the next smallest
			mergedList = new Node(a.value,mergeTwoLists(a.next, b));
		}else {
			mergedList = new Node(b.value,mergeTwoLists(a, b.next));
		}
		return mergedList;
	}
}