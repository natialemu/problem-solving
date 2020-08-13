import java.util.*;
import java.io.*;


/**
 Problem description: https://leetcode.com/problems/critical-connections-in-a-network/
**/

class CriticalServerConnections {
    public List<List<Integer>> criticalConnections(int n, List<List<Integer>> connections) {
        /**

        Algorithm: This works solely bc the graph is undirected
          - The connected components form cycles
          - create representatives for each vertex. this representative would be the 
            first vertex encountered in the cycle
          - Then, traverse the graph again, and find an edge that connects two 
            vertices with different representatives.
        **/
        Map<Integer, Integer> representativeOf = new HashMap<>();
        identifyCycles(n, connections, representativeOf);
        List<List<Integer>> criticalEdges = new ArrayList<>();
        int source = randomNode(graph);
        Set<Integer> visited = new HashSet<>();
        revealCriticalEdges(source, connections, representativeOf, criticalEdges, visited);
        return criticalEdges;
    }
    
    private void revealCriticalEdges(int curr, List<List<Integer>> graph, Map<Integer, Integer> repOf, List<List<Integer>> criticalEdges,
                                Set<Integer> visited) {
        visited.add(curr);
        for (int next : adj(graph, curr)) {
            if (!visited.contains(next)) {
                if (repOf.get(curr) != repOf.get(next)) {
                    criticalEdges.add(Arrays.asList(curr, next));
                }
                revealCriticalEdges(next, graph, repOf, criticalEdges, visited);
            }
        }
    }
    
    private void identifyCycles(int n, List<List<Integer>> graph, Map<Integer, Integer> repOf) {
        /**

        visited
        EdgeTo
        
        BFS:
        traverse til you encounter a visited node.
        trace way back from visited node til and child til they meet
        
        **/
        
        int source = randomNode(graph);
        int[] edgeTo = new int[n];
        Set<Integer> visited = new HashSet<>();
        Queue<Integer> bfs = new LinkedList<>();
        bfs.add(source);
        edgeTo[source] = source;
        while (!bfs.isEmpty()) {
            int curr = bfs.poll();
            visited.add(curr);
            for (int next : adj(graph, curr)) {
                if (!visited.contains(next)) {
                    bfs.add(next);
                    edgeTo[next] = curr;
                } else {
                    int x = curr;
                    int y = next;                    
                    //trace your way back til you get to a common node
                    while (x != y) {
                        repof.put(x, curr);
                        repOf.put(y, curr);
                        x = edgeTo[x];
                        if (x == y) break;
                        y = edgeTo[y];
                    }
                    repOf.put(y, curr);
                }
                
            }
        }
    }
    
    private int randomNode(List<List<Integer>> graph) {
        return graph.get(0).get(0);
    }

    private List<Integer> adj(List<List<Integer>> graph, int curr) {
        List<Integer> nextNodes = new ArrayList<>();
        for (List<Integer> edge : graph) {
            if (edge.get(0) == curr) nextNodes.add(edge.get(1));
        }
        return nextNodes;
    }

    public static void main(String[] args) {
        
    }

}