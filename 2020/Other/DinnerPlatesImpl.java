import java.util.*;

/**
	Problem based on https://leetcode.com/problems/dinner-plate-stacks/
**/

// an infinite collection of stacks with each stack having a fixed capacity
interface DinnerPlates<V> {

	void push(V value); // pushes to the left most non-full stack. if value is null, throw an exception
	V pop(); // pop from the right most non-empty stack. if there's nothing to pop, return null
	V popAtStack(int index); // pop from the stack at index 'index'. if that stack is empty, return null
}

public class DinnerPlatesImpl<V> implements DinnerPlates<V> {

	// the capacity of each stack	
	int capacity;
	List<Stack<V>> stacks;

	//holds the indices of stacks that are not full
	PriorityQueue<Integer> indexMinHeap;

	// the index of the right most non-empty stack
	int popIndex;

	public DinnerPlatesImpl(int capacity) {
		this.capacity = capacity;
		stacks = new ArrayList<>();
		indexMinHeap = new PriorityQueue<>();

		// add one empty stack at the beginning
		stacks.add(new Stack<>());
		indexMinHeap.add(0);

		popIndex = -1; // nothing to pop at the beginning
	}

	//runtime: Amortized runtime should be constant time. it takes O(logn) everytime the stack is full
	@Override
	public void push(V item) {
		if (item == null) {
			throw new IllegalArgumentException("Invalid input");
		}

		// get the index of the left most non full stack
		int pushIndex = indexMinHeap.peek();

		Stack<V> desiredStack = stacks.get(pushIndex);
		desiredStack.push(item);

		// if the stack fills up after pushing content, remove it's index from PQ
		if (desiredStack.size() == capacity) {
			indexMinHeap.poll();
		}

		//if there is no empty stack left, add a new empty stack into the list along
		//with its index
		if (indexMinHeap.size() == 0) {
			stacks.add(new Stack<>());
			indexMinHeap.add(pushIndex + 1);
		}

		//if there is an item in the current stack & its index is bigger than
		// the pop index, increment the pop index
		if (pushIndex > popIndex && !desiredStack.isEmpty()) {
			popIndex+=1;
		}	
	}

	//O(1) time
	@Override
	public V pop() {
		if (popIndex == -1) {
			return null;
		}

		V item = stacks.get(popIndex).pop();

		// if stack is empty, adjust pop index
		if (stacks.get(popIndex).isEmpty()) {
			
			// always leave one empty stack at the end after pop index
			if (stacks.get(popIndex + 1).isEmpty()) 
				stacks.remove(popIndex + 1);
			popIndex--;			
		}
		return item;
	}

	//Amortized runtime: O(1) 
	@Override
	public V popAtStack(int index) {
		if (index < 0 || index >= stacks.size()) {
			throw new IllegalArgumentException("invalid input");
		}

		if (index == popIndex) {
			return pop();
		}

		if (stacks.get(index).isEmpty()) {
			return null;
		}

		// if stack size at 'index' is at capacity,
		// add the index of the stack to PQ. Otherwise it should already be there
		if (stacks.get(index).size() == capacity) 
			indexMinHeap.add(index);

		return stacks.get(index).pop();
	}

	public static void main(String[] args) {
		DinnerPlatesImpl<Integer> dinnerStacks = new DinnerPlatesImpl<>(2);
		dinnerStacks.push(1);
		dinnerStacks.push(2);
		dinnerStacks.push(3);
		dinnerStacks.push(4);
		dinnerStacks.push(5);

		Integer value = dinnerStacks.popAtStack(0);
		System.out.println(value);

		dinnerStacks.push(20);

		dinnerStacks.push(21);

		value = dinnerStacks.popAtStack(0);
		System.out.println(value);
		value = dinnerStacks.popAtStack(2);
		System.out.println(value);

		value = dinnerStacks.pop();
		System.out.println(value);
		value = dinnerStacks.pop();
		System.out.println(value);
		value = dinnerStacks.pop();
		System.out.println(value);
		value = dinnerStacks.pop();
		System.out.println(value);
		value = dinnerStacks.pop();
		System.out.println(value);
	}
}