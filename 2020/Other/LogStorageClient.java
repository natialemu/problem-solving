import java.util.*;
import java.util.concurrent.*;


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
		Key learnings:
		-> Finding a range of nodes within a well ordered linked structure such as BST, Trie, Heap, etc.
		-> Similarities between representing a String in a trie and that of representing a Log with varying timestamps per granuarity in a Trie.
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
			};

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
	Map<Granularity, Logs> logsPerGranularity;
	private static final Granularity[] granularities = 
			new Granularity[] {
				Granularity.YEAR,
				Granularity.MONTH,
				Granularity.DAY,
				Granularity.HOUR,
				Granularity.MINUTE,
				Granularity.SECOND
			};
	class Logs {
		class BstNode {
			Log log;
			long timeStamp;
			BstNode left, right;
			public BstNode(int logId, long timeStamp) {
				log = new Log(logId);
				this.timeStamp = timeStamp;
			}
			public void increment() {log.count += 1;}
		}
		BstNode root;

		public void add(int logId, long timestamp) {
			BstNode runner = root;
			while (runner.left != null || runner.right != null) {
				if (runner.timestamp > timestamp) runner = runner.left;
				else if (runner.timestamp <= timestamp) runner = runner.right;
			}
			if (timestamp < runner.timestamp) runner.left = new BstNode(logId, timestamp);
			else runner.right = new BstNode(logId, timestamp);
 		}

 		public List<Integer> findAll(long timeStampStart, long timeStampEnd) {

 			//TODO
 			List<Integer> allNodes = new ArrayList<>();
 			findAll(root, timeStampStart, timeStampEnd, allNodes);
 		}
 		private void findAll(BstNode current, long timeStampStart, long timeStampEnd, List<Integer> desiredLogs) {
 			if (current != null) {

 				if (current.timeStamp >= timeStampStart && current.timeStamp <= timeStampEnd) {
 					desiredLogs.add(current.log.id);
 				}
 				if (current.left != null && current.timeStamp >= timeStampStart) findAll(current.left, timeStampStart, timeStampEnd, desiredLogs);
 				if (current.right != null && current.timeStamp <= timeStampEnd) findAll(current.right, timeStampStart, timeStampEnd, desiredLogs);
 			}

 		}

	}

	@Override
	public void put(int logId, long timeStamp) {
		Map<Granularity, Long> timeStampPerGranularity = 
		splitter.splitByGranularityV2(timeStamp);
		for (Granularity granularity : granularities) {
			long timestamp = timeStampPerGranularity.get(granularity);
			Logs logs = logsPerGranularity.get(granularity);
			logs.add(logId, timestamp);
		}
	}

	@Override
	public List<Integer> get(long timeStampStart, long timeStampEnd, Granulary granularity)  {
		Map<Granularity, Long> timeStampPerGranularityStart = 
		splitter.splitByGranularityV2(timeStampStart);
			Map<Granularity, Long> timeStampPerGranularityEnd = 
		splitter.splitByGranularityV2(timeStampEnd);

		long timestamp = timeStampPerGranularity.get(granularity);
		Logs logs = logsPerGranularity.get(granularity);
		List<Integer> desiredLogs = logs.findAll(timeStampPerGranularityStart.get(granularity), timeStampPerGranularityEnd.get(granularity));
		return desiredLogs;
	}

}

public class LogStorageClient {
	public static void main(String[] args) {

		//Log storage implemented via a Trie
		LogStorage logStorageTrie = new LogStorageTrie();



		//Log storage system implemented via a Map and BST
		LogStorage logStorageBst = new LogStorageBst();

	}
	
}