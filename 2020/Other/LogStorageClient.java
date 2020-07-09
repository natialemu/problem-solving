


class Log {
	int count;
	int id;
	public Log(int id) {
		this.id = id;
	}
}
enum Granularity {
	YEAR,
	MONTH,
	DAY,
	HOUR,
	MINUTE,
	SECOND;
}
interface LogStorage {

	void put(int logId, long timestamp);
	List<Integer> get(long timeStampStart, long timeStampEnd, Granulary granularity);


	/**
	Data structure design 1:
		Map<Granulary, Logs>

		Logs will be implemented as a BST.


		privateFindAll(BSTNode curr, int timestamp1, int timestamp2 List<Log> desiredLogs) {
			
			if (curr node is not in range || current node is null)
				return

			recurse to the left if available
			add log to desiredLogs
			recurse to the right if available



		}

	Data structure design 2:
		Using a trie to implement the log storage

		TrieNode {
			Log l;
			The three links;
			Granularity info;
		}
		//inserting a log is pretty straight forward.

		retrieval of all nodes within a certain time range per granularity:

		findAll(timestamp1, timestamp2, Granularity end);

		// finding a subset of nodes within a well ordered linked structure:
			-> BSTs
			-> Trie
			-> DAG

		
		//When this particular subroutine is called, desiredLogs will be populated with all logs having a timestamp within range of
		//timestamp1 and timestamp2 
		private findAll(TrieNode current, int[] timestamp1, int[] timestamp2, Granularity end, int i, List<Log> desiredLogs) {
			//Initialize the queue with current in it. this is to go through all its siblings.
			// if root is not in range, find first sibling that is.
			while the queu is not empty {
				poll from queue
				add the appropriate sibling in range
				
				// if at end granularity, add the log from current node to list of desired logs
				//recurse for the polled sibling if not yet at end granularity
				findAll(current.forwardLink, timeStamp1, timestamp2, end, i + 1, desiredLogs);
	
			}
		}


		two behaviors for this are:

		void put(Log log, long timestamp);

		List<Log> get(long timestamp1, long timestamp2, Granularity granularity);



		Key learnings here:
		-> Finding a subset of nodes within a well ordered linked structure such as BST, Trie
		-> Similarities in representing a String in a trie vs Representing a Log with varying timestamps per granuarity in a Trie.


	**/
}
class TimeStampSpliter {

	public Map<Granularity,Long> splitByGranularity(long timeStamp) {

	}

	public Map<Granularity, Long> splitByGranularityV2(long timeStamp) {

	}

}


class LogStorageTrie implements LogStorage {
	TimeStampSplitter splitter;
	TrieNode root;

	class TrieNode {
		Log log;
		long timeStamp;
		TrieNode[] family;
		public TrieNode(int logId, long timeStamp) {
			log = new Log(logId);
			this.timeStamp = timeStamp;
		}
		public void increment() {
			log.count+=1;
		}

		public int logId() {
			return log.id;
		}

	}
	private static final Granularity[] granularities = 
			new Granularity[] {
				Granularity.YEAR,
				Granularity.MONTH,
				Granularity.DAY,
				Granularity.HOUR,
				Granularity.MINUTE,
				Granularity.SECOND
			}

	public LogStorageTrie() {
		splitter = new TimeStampSplitter();

	}

	@Override
	public void put(int logId, long timeStamp) {
		Map<Granularity, Long> timeStampPerGranularity = 
		splitter.splitByGranularity(timeStamp);
		root = insert(root, logId, granularities, timeStampPerGranularity, 0);
	}

	private TrieNode insert(TrieNode current, int logId, Granularity[] granularities, Map<Granularity, Long> timeStampMap, int curr) {
		Granularity currentGranularity = granularities[curr];
		long currentTimeStamp = timeStampMap.get(currentGranularity);
		if (current == null) current = new TrieNode(logId, currentTimeStamp);

		if (current.timeStamp > currentTimeStamp) current.family[0] = insert(current.family[0], logId, granularities, timeStampMap, curr);
		else if (current.timeStamp < currentTimeStamp) current.family[2] = insert(current.family[2], logId, granularities, timeStampMap, curr);
		else if (curr == granularities.length - 1) current.increment(); 
		else current.family[1] = insert(current.family[1], logId, granularities, timeStampMap, curr+1);
		return current; 
	}

	@Override
	public List<Integer> get(long timeStampStart, long timeStampEnd, Granulary granularity) {
		Map<Granularity, Long> timeStampPerGranularityStart = 
		splitter.splitByGranularity(timeStampStart);
		Map<Granularity, Long> timeStampPerGranularityEnd = 
		splitter.splitByGranularity(timeStampEnd);
		List<Integer> desiredLogs = new ArrayList<>();
		TrieNode firstValidNode = findFirstValidNode(timeStampStart, timeStampEnd);
		findAll(firstValidNode, timeStampStart, timeStampPerGranularityStart, timeStampPerGranularityStartEnd, 0, desiredLogs);
		return desiredLogs;
	}

	private void findAll(TrieNode current, Map<Granularity, Long> timeStampMapStart, Map<Granularity, Long> timeStampMapEnd, 
		Granularity[] granularities, int curr, List<Integer> desiredLogs) {

		Queue<TrieNode> bfsSiblingTraversal = new LinkedList<>();
		long timeStampStart = timeStampMapStart.get(granularites[curr]);
		long timeStampEnd = timeStampMapEnd.get(granularites[curr]);


		bfsSiblingTraversal.add(current);
		while (!bfsSiblingTraversal.isEmpty()) {
			TrieNode current = bfsSiblingTraversal.poll();

			if (current.family[0] != null && current.family[0].timeStamp >= timeStampStart && current.family[0].timeStamp <= timeStampEnd) {
				bfsSiblingTraversal.add(current.family[0]);
			}
			if (current.family[2] != null && current.family[2].timeStamp >= timeStampStart && current.family[2].timeStamp <= timeStampEnd) {
				bfsSiblingTraversal.add(current.family[2]);
			}

			if (curr == granularites.length - 1) {
				desiredLogs.add(current.logId());
			} else {
				findAll(current.family[1], timeStampMapStart, timeStampMapEnd, granularities, curr + 1, desiredLogs);
			}
			
		}

	}


}

class LogStorageBst implements LogStorage { 
	TimeStampSplitter splitter;

}

public class LogStorageClient {
	
}