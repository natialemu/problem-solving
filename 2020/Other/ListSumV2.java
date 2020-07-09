
class ListNode {
	int value;
	ListNode next;

	public ListNode(int value) {
		this.value = value;
	}
}
public class ListSumV2 {

	public static ListNode add(ListNode l1, ListNode l2) {

		if ( l1 == null || l2 == null) {
			throw new IllegalAccessException("Invalid arguments provided");
		}
		Stack<ListNode> firstStack = new Stack<>();
		Stack<ListNode> secondStack = new Stack<>();
		ListNode secondList = l2;
		ListNode firstList = l1;
		while (firstList !== null) {
			firstStack.push(firstList);
			firstList = firstList.next;
		}

		while (secondList ! = null) {
			secondStack.push(secondList);
			secondList = secondList.next;
		}
		int carryOver = 0;
		ListNode sentinel = new ListNode();
		while (!firstStack.isEmpty() || !secondStack.isEmpty()) {
			int currDigit1 = firstStack.isEmpty() ? 0  : firstStack.pop();
			int currDigit2 = secondStack.isEmpty() ? 0 : secondStack.pop();

			int newDigit  = (currDigit2 + currDigit1 + carryOver) / 10;
			carryOver = (currDigit1 + currDigit2 + carryOver) % 10;

			ListNode newNode = new ListNode(newDigit);
			newNode.next = sentinel.next;
			sentinel.next = newNode;

		}

		return sentinel.next;
	}
	
}