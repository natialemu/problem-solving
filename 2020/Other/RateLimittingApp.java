import java.util.concurrent.*;
import java.util.*;
/**

Problem: Creat a Rate Limitter

- 	Requests will be sent to some API at some rate. The goal is to introduce some rate limitting functionality prior to processing the request.
	If a client doesn't pass the THRESHOLD, allow the request to be processed, and return true.
	Otherwise, return false.

-   The size of the window that holds the requests is determined by  the relative time of the most recent request to the earliest request on record.
	The number of requests a client sends should not exceed the number of requests within this time window.

-> Behaviors of the Rate Limitter:
	- Need to add the requests to some sort of data strucuture 
	- Need to retrieve the earliest request in the data structure
	- May need to remove the earliest request in the data strucutre to maintain proper window size.

Some Data strucutres that efficiently support these behaviors:
	1. Priority Queue:
		Add -> O(log(N)) where N is max size per window
		getEarliestRequest -> O(l) with a min heap whose priority is based on time
		removeEarliest -> O(logN)
	2. Queue: new requests pushed into the queue and old ones popped
		Add -> O(1)
		Remove earliest -> O(1)
		get Earliest -> O(1)

**/


//a utility class to help with simulation of timestamp of requests
class NumberGenerator {
	private static int currTime = 1;
	public static int getTime() {
		currTime++;
		return currTime;
	}
}

//An interface that represents the clients
interface Client {
	int clientId(); // returns client id
	int time(); //returns timestamp in mins
}

interface RateLimitter {
	boolean process(Client client);
}

class RequestProcessor implements Runnable {
	Client request;

	public RequestProcessor(Client request) {
		this.request = request;
	}

	@Override
	public void run() {
		System.out.println("Processed request from client " + request.clientId());
	}


}

class RateLimittingApp implements RateLimitter {
	// each client has a queue to store their requests
	Map<Integer, Queue<Integer>> clientToRequestMap;
	// the allowed rate of requests is max/threshold requests per second
	int threshold, max; 
	//Used to lock the data structure 
	Object lock  = new Object();
	// a fixed thread pool to process allowed requests
	ExecutorService service = Executors.newFixedThreadPool(2);
	public RateLimittingApp(int threshold, int max) {
		clientToRequestMap = new HashMap<>();	
		this.threshold = threshold;
		this.max = max;
	}

	@Override
	public boolean process(Client client) {
		synchronized(lock) {
			// Get the Queue that holds the client's request
			Queue<Integer> clientRequests = clientToRequestMap.getOrDefault(client.clientId(), new LinkedList<>());
			boolean requestIsAllowed = false;
			int timeDifference = clientRequests.size() == 0 ? 0 : client.time() - clientRequests.peek();
			// if max number of requests is reached. Don't allow the incoming request
			if (clientRequests.size() < max) {
				clientRequests.add(client.time());
				clientToRequestMap.put(client.clientId(), clientRequests);
				requestIsAllowed = true;
				service.submit(new RequestProcessor(client));
			}
			// Adjust the size of the window of requests if the time difference is less than the threshold
			while (timeDifference > threshold) {
				clientRequests.poll();
				timeDifference = client.time() - clientRequests.peek();
			}
			return requestIsAllowed;
		}
	}

	public static void main(String[] args) throws InterruptedException {
		RateLimittingApp rateLimitter = new RateLimittingApp(10, 10);

		ExecutorService service = Executors.newFixedThreadPool(2);

		// request simulation from client 4
		service.submit(new Runnable() {
			@Override
			public void run() {
				int currTime = NumberGenerator.getTime();
				int numRequests = 0;
				while (numRequests < 30) {
					Client client = getClient(4, currTime);
					rateLimitter.process(client);
					currTime = NumberGenerator.getTime();
					numRequests++;
				}				
			}

			private Client getClient(int id, int currTime) {
				return new Client(){
					@Override
					public int clientId() {
						return id;
					}

					@Override
					public int time() {
						return currTime;
					}
				};
			}
		});



		// request simulation from client 4
		service.submit(new Runnable() {
			@Override
			public void run() {
				int currTime = NumberGenerator.getTime();
				int numRequests = 0;
				while (numRequests < 30) {
					Client client = getClient(5, currTime);
					rateLimitter.process(client);
					currTime = NumberGenerator.getTime();
					numRequests++;
				}				
			}

			private Client getClient(int id, int currTime) {
				return new Client(){
					@Override
					public int clientId() {
						return id;
					}

					@Override
					public int time() {
						return currTime;
					}
				};
			}
		});


	}
	
}