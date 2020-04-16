import java.util.*;
import java.io.*;

/**

Description: Based on a slightly modified version of https://leetcode.com/problems/time-based-key-value-store/ 

**/

interface Values<V> {
	V get(int timestamp);// either V with timestamp the next smallest
	boolean add(V value, int timestamp);
}


class Node<V> {
	V value;
	int timestamp;
	Node<V> right;
	Node<V> left;

	public Node(V value, int timestamp) {
		this.value = value;
		this.timestamp = timestamp;
	}

}

class ValuesBST<V> implements Values<V> {

	Node<V> root;

	@Override
	public V get(int timestamp) {
		if (root == null) {
			return null;
		}
		return get(root, timestamp, -1, null);
	}

	private V get(Node<V> current, int target, int bestTimeSoFar, V bestSoFar) {
		if (current.left == null && current.right == null) {
			if (current.timestamp <= target) {
				return current.value;
			}
			return current.timestamp > target && bestTimeSoFar >= 0 ? bestSoFar : null;
		}

		int currBestTimeSofar = current.timestamp < target && current.timestamp > bestTimeSoFar ? current.timestamp : bestTimeSoFar;
		V currBestValue = current.timestamp < target && current.timestamp > bestTimeSoFar ? current.value : bestSoFar;

		if (current.right != null && target > current.timestamp) {
			return get(current.right, target, bestTimeSoFar, bestSoFar);
		}

		if (current.left != null && target  < current.timestamp) {
			return get(current.left, target, bestTimeSoFar, bestSoFar);
		}
		return null;
	}

	@Override
	public boolean add(V value, int timestamp) {
		Node<V> newNode = new Node<>(value, timestamp);
		if (root == null) {
			root = newNode;
			return true;
		} 
		return add(root, newNode);
	} 

	private boolean add(Node<V> current, Node<V> node) {
		if (current.left == null && current.right == null && node.timestamp > current.timestamp) {
			current.right = node;
			return true;
		}

		if (current.left == null && current.right == null && node.timestamp < current.timestamp) { 
			current.left = node;
			return true;
		}

		if (node.right != null && node.timestamp > current.timestamp) {
			return add(current.right, node);
		}

		if (node.left != null && node.timestamp < current.timestamp) {
			return add(current.left, node);
		}

		return false;


	}
}

interface KeyValueStore<K, V> {
	Boolean set(K key, V value, int timestamp);
	V get(K key, int timestamp);
}

class KeyValueStoreImpl<K, V> implements KeyValueStore<K, V>{

	Map<K, Values<V>> keyValueMap;

	public KeyValueStoreImpl() {
		keyValueMap = new HashMap<>();
	}

	@Override
	public Boolean set(K key, V value, int timestamp) {
		Values<V> values = keyValueMap.getOrDefault(key, new ValuesBST<>());
		boolean result = values.add(value, timestamp);
		keyValueMap.put(key, values);
		return result;
	}

	@Override
	public V get(K key, int timestamp) {
		Values<V> values =  keyValueMap.getOrDefault(key, null);
		if (values == null) {
			return null;
		}
		return values.get(timestamp);
	}
}


public class TimeBasedKeyStore {
	public static void main(String[] args) {
		KeyValueStore<String, String> keyValueStore = new KeyValueStoreImpl<>();
		keyValueStore.set("foo", "bar", 1); // store the key "foo" and value "bar" along with timestamp = 1   
		String value = keyValueStore.get("foo", 1);  // output "bar"
		System.out.println(value);   
		value = keyValueStore.get("foo", 3); // output "bar" since there is no value corresponding to foo at timestamp 3 and timestamp 2, then the only value is at timestamp 1 ie "bar"   
		System.out.println(value);
		keyValueStore.set("foo", "bar2", 4);   
		value = keyValueStore.get("foo", 4); // output "bar2"   
		System.out.println(value);
		value = keyValueStore.get("foo", 5); //output "bar2"   
		System.out.println(value);
	}
}