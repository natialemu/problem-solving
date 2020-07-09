
/**
	create a graph out of the provided dependencies by doing the following:

	   1. read the file and load the dependencies into an adjacency list
	   2. create  a method that returns the adjacent nodes of the graph
	create a method that performs a post order reverse dfs:
	create a method that checks for cycles in the directed graph
	**/
public class BuildOrder{

	private int sizeOfProject;
	private List<Integer>[] adjacencyList;
	private int source;
	private Stack<Integer> linearizedDag;
	private boolean[] visited;

	private boolean[] pushedOnStack;   //helpful for detecting cycles
	private boolean[] poppedOffStack;  //helpful for detecting cycles

	public BuildOrder(File f){
		Scanner scan = new Scanner(f);
		int sizeOfProject = Integer.parseInt(scan.nextLine());
		adjacencyList = new (List<Character>[]) ArrayList<>[sizeOfProject];
		visited = new boolean[sizeOfProject];

		pushedOnStack = new boolean[sizeOfProject];
		poppedOffStack = new boolean[sizeOfProject];

		for(int i = 0; i < adjacencyList.length; i++){
			adjacencyList[i] = new ArrayList();
		}

		String[] dependency = scan.nextLine().split(", ");
		for(int i = 0; i < dependency.length; i++){
			char firstProject = dependency[i].charAt(1);
			char secondProject = dependency[i].charAt(dependency[i].length() - 2);
			if(i == 0){
				source = mapCharToIndex(firstProject);
			}



			adjacencyList[mapCharToIndex(firstProject)].add(mapCharToIndex(secondProject));



		}
		linearizedDag = new Stack();
		if(!cyclesExist(source)){
			postOrderDFS(source, linearizedDag);
		}else{
			linearizedDag = null;
		}




	}

	public int mapCharToIndex(char character){
		return ((int)('a' - Character.toString(character).toLowerCase().charAt(0)));

	}

	public char mapIndexToChar(int index) {
		return  ((char)(index + 'a'));
	}

	public List<Integer> adjacentStates(int vertex){
		return adjacencyList[vertex];
	}

	public void postOrderDFS(int source, Stack<Integer> sortedVertices){

		for(int s: adjacentStates(source)){

			postOrderDFS(s);
		}

		sortedVertices.push(source);

	}

	public boolean cyclesExist(int source){
		pushedOnStack[source] = true;

		visited[source] = true;

		for(int s: adjacentStates(source)){

			if(visited[s] && verifyCycle(source, s)){
				return true;
			}
			visited[s] = true;
			cyclesExist(s);
		}
		poppedOffStack[source] = true;
		return false;
		/*

		    perform DFS

		*/

	}

	public boolean verifyCycle(int source, int destination){

		return pushedOnStack[source] && pushedOnStack[destination] && poppedOffStack[source] && poppedOffStack[destination];
	
	}

	public List<Character> GetBuildOrder(){

		List<Character> buildOrder = new ArrayList();
		if(linearizedDag == null){
			buildOrder = null;
			return buildOrder;
		}

		for(int i: linearizedDag){
			buildOrder.add(mapIndexToChar(i));
		}

		return buildOrder;

	}

	public static void main(String[] args){

		String dependencyFilename = "";


		BuildOrder buildOrder = new BuildOrder(new File(dependencyFilename));
		List<Character> buildOrder = ListbuildOrder.GetBuildOrder();
		if(buildOrder == null){
			System.out.println("There is no build order. ");
		}else{
			System.out.pritnln("A build order has been found. Here it is: ")
			for(char c : buildOrder){
				System.out.print(c + " ");
			}

			System.out.println();
		}
	}


	
}