
import java.util.concurrent.*;
import java.util.*;
/**
What's left:
		- Finish up  all unimplemented methods
		- walk through various use cases to make sure it works
		- implement consistent hashing to select appropriate cache
		- make the necessary behaviors atomic and thread safe
		- implement the client with the appropriate test cases.
		- extend the code so that an LRU eviction policy is used as a tie breaker.
		- make sure there aren't any compilation and run time errors
		- push code to github

**/

interface CacheCluster<K, V> {
	Void put(K key, V value); 
	V get(K key);
	Void addCachesFromCluster(int num);
	Void removeCachesFromCluster(int num);
}

/**
Each cache in the cluster
**/
interface Cache<K, V> {
	Void put(K key, V value); 
	V get(K key);
	List<K> keys();
	Void delete(K key);
	V highestPriorityValue();
}

class CachedObject<K, V> {
	K key;
	V value;
	int count;
	public CachedObject(K key, V value, int count) {
		this.key = key;
		this.value = value;
		this.count = count;
	}
}
class LRUCache<K> implements Cache<K, CachedObject> {
	class Node {
		CachedObject object;
		Node next, prev;
		public Node(CachedObject obj) { object = obj; }
	}
	Node head;
	Node tail;
	Map<Key, Node> keyToObjectMap;
	int currSize;
	int maxSize;

	public LRUCache(int maxSize) {
		this.maxSize = maxSize;
		keyToObjectMap = new HashMap<>();
		currSize = 0;
	}


	@Override
	public Void put(K key, CachedObject value) {
		CachedObject clonedObject = clone(value);
		Node newNode = new Node(clonedObject);
		keyToIndexMap.put(key, newNode);
		if (maxSize == currSize) {
			evict();
		}

		
		if (head == null && tail == null) {
			head = newNode;
			tail = newNode;
		} else  {
			newNode.next = head.next;
			newNode.prev = head.prev;

			head = newNode;
		}
		return null;
	}

	private CachedObject clone(CachedObject original) {
		CachedObject cloned = new CachedObject(original.key, original.value, original.count);
		return cloned;
	}

	private void evit() {
		Node aboutToBeEvicted = tail;
		tail = tail.prev;
		tail.next.prev = null;
		tail.next = head;
		keyToIndexMap.remove(aboutToBeEvicted.object.key);
		currSize--;

	}

	@Override
	public CachedObject get(K key) {
		validate(key);
		Node desiredNode = keyToObjectMap.get(key);
		if (desiredNode.prev != null)
			desiredNode.prev.next = desiredNode.next;
		if (desiredNode.next != null)
			desiredNode.next.prev = desiredNode.prev;

		desiredNode.next = head;
		desiredNode.prev = tail;
		head = desiredNode;
	}

	@Override
	public List<K> keys() {
		return new ArrayList<>(keyToObjectMap.keySet());
	}

	@Override
	public Void delete(K key){
		Node desiredNode = keyToObjectMap.get(key);

		if (desiredNode.prev != null)
			desiredNode.prev.next = desiredNode.next;
		if (desiredNode.next != null)
			desiredNode.next.prev = desiredNode.prev;

		desiredNode.next = null;
		desiredNode.prev = null;
		keyToObjectMap.remove(key);
		currSize--;
	}

	@Override
	public CachedObject highestPriorityValue() {
		return head == null ? null : head.object;
	}
}

class CacheImpl<K, V> implements Cache<K, V> {
	CachedObject[] minHeap;
	int currSize;
	Map<Key, Integer> keyToIndexMap;
	LRUCache<K> lruCache;
	final Object lock = new Object();

	public CacheImpl(int maxSize) {
		minHeap = new CacheObject[maxSize];
		keyToIndexMap = new HashMap<>();
		lruCache = new LRUCache<>(maxSize);

	}
	@Override
	public synchronized Void put(K key, V value) {
		if (currSize == minHeap.length) evict();
		CacheObject cachedObject = wrap(key, value);
		minHeap[currSize] = cachedObject;
		keyToIndexMap.put(key, currSize);
		currSize++;
		lruCache.put(key, cachedObject);
	}

	private CacheObject wrap(K key, V value) {
		CachedObject newObject  = new CachedObject(key, value, 1);
		return newObject;
	}

	private void evict() {
		CachedObject lfuObject = minHeap[minHeap.length - 1];
		CachedObject lruObject = lruCache.highestPriorityValue();
		currSize--;
		int indexToRemove = 0;
		if (lfuObject.count == lruObject.count) {
			K lruKey = lruObject.key;
			indexToRemove = keyToIndexMap.get(K);
		} else {
			K lfuKey = lfuObject.key;
			lruCache.delete(lfuKey);
		}
		
		swap(indexToRemove, minHeap.length - 1);
		minHeap[currSize] = null;
		bubbleDownIfNecessary(indexToRemove);
	}

	private void swap(int i, int j) {
		CacheObject temp = minHeap[j];
		minHeap[j] = minHeap[i];
		minHeap[i] = temp;
	}

	private void bubbleDownIfNecessary(int startingIndex) {
		int currIndex = startingIndex;
		while (!nodeIsLeaf(currIndex)) {
			CachedObject firstChild = 2*currIndex < currSize ? minHeap[2*currIndex] : null;
			CachedObject secondChild = 2*currIndex + 1 < currSize ? minHeap[2*currIndex + 1] : null;
			int minChildIndex = secondChild == null || firstChild.count < secondChild.count ? 2*currIndex : 2*currIndex + 1;
			swap(currIndex, minChildIndex);
			// update new index in map every time a swap is made
			keyToIndexMap.put(minHeap[minChildIndex].key, minChildIndex);
			keyToIndexMap.put(minHeap[currIndex].key, currIndex);
			currIndex = minChildIndex;
		}
	}
	
	private boolean nodeIsLeaf(int index) {
		return (2*index >= minHeap.length || minHeap[2*index] == null) && (2*index + 1 >= minHeap.length || minHeap[2*index + 1] == null);
	}

	@Override
	public V get(K key) throws InterruptedException{
		try {
			synchronized (lock) {
				// no need to wait on a condition
				int objectIndex = keyToIndexMap.getOrDefault(key, -1);
				if (objectIndex == -1) return null;
				minHeap[objectIndex].count +=1;
				bubbleDownIfNecessary(objectIndex);
				CachedObject value = lruCache.get(key);
				value.count += 1;
			}
		} catch (InterruptedException e) {
			throw e;
		}
		

	}

	@Override
	public List<K> keys() {
		return new ArrayList<>(keyToIndexMap.keySet());
	}

	@Override
	public Void delete(K key) throws InterruptedException {

		try {
			Lock lock = new ReentrantLock();
			Condition conditionVariable = lock.newCondition();

			lock.lock();
			validate(key);
			Integer objectIndex = keyToIndexMap.get(key);
			swap(objectIndex, currSize - 1);
			bubbleDownIfNecessary(objectIndex);
			keyToIndexMap.remove(objectIndex);
			currSize -= 1;
			minHeap[currSize] = null;
			lruCache.delete(key);
			lock.unlock();
		} catch (InterruptedException e) {
			throw e;
		}
	}

	@Override
	public V highestPriorityValue() {
		return minHeap[0].value;
	}
}



class CacheClusterImpl<K, V> implements CacheCluster<K, V> {
	Map<Integer, Cache> caches;
	int[] consistentHashingDimension; // 128 bit space 
	Semaphore semaphore;


	public CacheCluster(int clusterSize, int cacheSize) {
		semaphore = new Semaphore(clusterSize);
		validate(clusterSize, cacheSize); // cluster size must be less than threshold.
		caches = new HashMap<>();
		initialize(clusterSize, cacheSize); // initialize the right number of caches with the right capacity
		consistentHashingDimension = new int[128];
	}

	private void initialize(int clusterSize, int cacheSize) {
		for (int i = 0; i < clusterSize; i++) {
			Cache cache = new CacheImpl(cacheSize);
			caches.put(i, new CacheImpl(cacheSize));

			//hash each cache
			int hash = cache.hashCode()%consistentHashingDimension.length;
			consistentHashingDimension[hash] = i;
		}
	}

	@Override
	public Void put(K key, V value) throws InterruptedException{
		try {
			semaphore.acquire();
			int cacheKey = selectCache(key);
			Cache cache = caches.get(cacheKey);
			cache.put(key, value);
		} catch (InterruptedException e) {
			throw e;
		} finally {
			semaphore.release();
		}
		return null;
	}
		

	@Override
	public V get(K key) throws InterruptedException {
		try {
			semaphore.acquire();
			int cacheKey =  (key);
			Cache cache = caches.get(cacheKey);
			return cache.get(key);
		} catch (InterruptedException e) {
			throw e
		} finally {
			semaphore.release();
		}
		return null;
		
	}
	private int selectCache(K key) {
		// is this thread safe?
		int keyHash = k.hashCode() % consistentHashingDimension.length;
		while (!caches.containsKey(keyHash)) {
			keyHash = (keyHash + 1) % consistentHashingDimension.length;
		}
		return keyHash
	}


	@Override
	public Void addCachesFromCluster(int num) {
		//TODO

	}

	@Override
	public Void removeCachesFromCluster(int num) {
		//TODO

	}

}

public class CacheClient {

	public static void main(String[] args) {
		//TODO
		ExecutorService threadPool = Executors.newFixedThreadPool(5);
		ExecutorCompletionService ecs = new ExecutorCompletionService(threadPool);

		// Submit 10 trivial tasks.
		CacheCluster<Integer, String> cluster = new CacheCluster<>(5, 10);


		// create a runnable task that, given the cache cluster, and a key, value
		// it will request to add the key,value pair to the cluster.


		//create a callable task that, given a cluster and a key, it will retrieve it


		// submit multiple instances of the task that adds keys


		// submit multiple instances of the task taht retrieves keys.


		// log what's going on in each cache. when somethign is added. when something is evicted...

		List<Integer> keys = generateKeys(8);
        for (int i = 0; i < 10; i++) {
            ecs.submit(
            	new Callable<>(){
            		@Override
            		public 
            	}, 
            	new Integer(i));
        }

	}
	
}