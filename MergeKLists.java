import java.util.*;
import java.io.*;
/**
   Problem: Merge k sorted linked lists and return it as one sorted list. 
**/
//Complexity: O(kN) where N is the overall number of elements of all lists.
public class MergeKLists{

	public static void main(String[] args){
		//test code 
		List<Integer> list1 = new ArrayList<>();
		list1.add(1);
		list1.add(4);
		list1.add(5);
		Collections.sort(list1);

		List<Integer> list2 = new ArrayList<>();
		list2.add(1);
		list2.add(3);
		list2.add(4);
		Collections.sort(list2);

		List<Integer> list3 = new ArrayList<>();
		list3.add(2);
		list3.add(6);
		Collections.sort(list3);

		List<List<Integer>> lists = new ArrayList<>();
		lists.add(list1);
		lists.add(list2);
		lists.add(list3);

		List<Integer> mergedList = mergeLists(lists);
		printList(mergedList);

	}

	public static void printList(List<Integer> l){
		System.out.print("[" + l.get(0));
		for(int i = 1; i < l.size(); i++){
			System.out.print(", " + l.get(i));
		}
		System.out.println("]");
	}

	public static List<Integer> mergeLists(List<List<Integer>> lists){
		int[] pointers = new int[lists.size()];
		boolean[] flags = new boolean[lists.size()];
		int counter = 0;
		List<Integer> mergedList = new ArrayList<>();
		while(counter < pointers.length){
			int k = findSmallestKthElement(pointers, flags, lists);
			
			mergedList.add(lists.get(k).get(pointers[k]));
			pointers[k] = pointers[k] + 1;
			if(pointers[k] > lists.get(k).size() - 1){
				flags[k] = true;
				counter++;
			}

		}
		return mergedList;
	}

	public static int findSmallestKthElement(int[] ptrs, boolean[] flags, List<List<Integer>> lists){
		assert(ptrs.length == flags.length);
		int smallest = Integer.MAX_VALUE;
		int smallestValue = Integer.MAX_VALUE;
		for(int i = 0; i < ptrs.length; i++){
			if(!flags[i] && lists.get(i).get(ptrs[i]) < smallestValue){
				smallest = i;
				smallestValue = lists.get(i).get(ptrs[i]);
			}
		}
		return smallest;

	}

}

