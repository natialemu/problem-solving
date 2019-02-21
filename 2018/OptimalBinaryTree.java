import java.util.*;
import java.io.*;

class BSTInstance{
	List<Integer> keys;
	int i;
	int j;

	public BSTInstance(int i, int j){
		this.i = i ; 
		this.j = j;
	}

}
public class OptimalBinaryTree {

	public static void main(String[] args){
		//Test case 
	}

	public static void getOptimalCostOfBST(List<Integer> keys, Map<Integer, Double> probabilities){
		BSTInstance bstInstance = new BSTInstance(0,keys.size() - 1);
		bstInstance.keys = new ArrayList<Integer>(keys);
		return getOptimalCostOfBSTTopDown(keys, dict);
	}

	public static void getOptimalCostOfBSTTopDown(BSTInstance bstInstance, Map<BSTInstance, Integer> dict, Map<Integer,Double> probabilities){
        // base case --> only one key left
		if(bstInstance.i == bstInstance.j){
			return probabilities.get(bstInstance.keys.get(bstInstance.i));
		}
        double minCost = Double.MAX_VALUE;
		for(int k = bstInstance.i; k <= bstInstance.j; k++){
			// let k be root
			BSTInstance leftInstance = new BSTInstance(bstInstance.i,k-1);
			leftInstance.keys = new ArrayList<Integer>(key);

			BSTInstance rightInstance = new BSTInstance(bstInstance.k+1,bstInstance.j);
			leftInstance.keys = new ArrayList<Integer>(key);
            
            // if k = bstInstance.i , then cost1 = 0
            // if k = bstInstance.j, then cost2 = 0
            double cost1 = 0;
            double cost2 = 0;
            if(k != bstInstance.i){
            	cost1 = dict.containsKey(leftInstance) ? dict.get(leftInstance) : getOptimalCostOfBSTTopDown(leftInstance,dict,probabilities);
            }
		 
		    if(k != bstInstance.j){
		    	cost2 = dict.containsKey(rightInstance) ? dict.get(rightInstance) : getOptimalCostOfBSTTopDown(rightInstance,dict, probabilities);
		    }

			double combinedCost = getCost(cost1, cost2, probabilities);

			if(combinedCost < minCost) {
				minCost = combinedCost
			}
		}
		dict.put(bstInstance,minCost);
		return minCost;

		
	}
	
	private static double getCost(double leftcost, double rigthCost, Map<Integer,Double> probabilities){
		double sumProb = 0;
		for(Double prob : probabilities.values()){
			sumProb += prob;
		}
		return leftcost + rigthCost + sumProb;
	}

	public static double getOptimalCostOfBSTBottomUp(List<Integer> keys, Map<Integer, Double> probabilities){
		int[][] memoTable = new int[keys.size()][keys.size()];
		// memoTable[i,j] --> min cost of a BST with keys [i,j]
		//base case: one key, i== j
		for(int i = 0; i < keys.size(); i++){
			memoTable[i][i] = probabilities.get(keys.get(i));
		}
		for(int i = 0; i < memoTable.length; i++){
			for(int j = i+1; j < memoTable[i].length; j++){
                double minCost = Double.MAX_VALUE;
				for(int k = i; k <= j; k++){
					double cost1 = 0;
            		double cost2 = 0;
            		if(k != i){
            			cost1 = memoTable[i][k-1];
            		}

            		if(k != j) {
            			cost2 = memoTable[k+1][j];
            		}
            		double combinedCost = getCost(cost1, cost2, probabilities);
            		if (combinedCost < minCost) {
            			minCost = combinedCost;
            		}
				}
				memoTable[i][j] = minCost; 
			}
		}
		return memoTable[0][keys.size()-1];
	}

}
