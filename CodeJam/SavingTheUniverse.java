import java.util.*;
import java.lang.*;
import java.io.*;

/**

Problem:

The urban legend goes that if you go to the Google homepage and search for "Google", the universe will implode. We have a secret to share... It is true! Please don't try it, or tell anyone. All right, maybe not. We are just kidding.

The same is not true for a universe far far away. In that universe, if you search on any search engine for that search engine's name, the universe does implode!

To combat this, people came up with an interesting solution. All queries are pooled together. They are passed to a central system that decides which query goes to which search engine. The central system sends a series of queries to one search engine, and can switch to another at any time. Queries must be processed in the order they're received. The central system must never send a query to a search engine whose name matches the query. In order to reduce costs, the number of switches should be minimized.

Your task is to tell us how many times the central system will have to switch between search engines, assuming that we program it optimally.

Input

The first line of the input file contains the number of cases, N. N test cases follow.

Each case starts with the number S -- the number of search engines. The next S lines each contain the name of a search engine. Each search engine name is no more than one hundred characters long and contains only uppercase letters, lowercase letters, spaces, and numbers. There will not be two search engines with the same name.

The following line contains a number Q -- the number of incoming queries. The next Q lines will each contain a query. Each query will be the name of a search engine in the case.

**/
public class SavingTheUniverse {
	public static void main (String[] args) throws java.lang.Exception
	{
	    try{
	        Scanner scan = new Scanner(System.in);
	       
	        int testCaseNum = Integer.parseInt(scan.nextLine().trim());
	        for(int i = 0; i < testCaseNum; i++){
	              
                //read in search engnines
                int searchEngineNum = Integer.parseInt(scan.nextLine().trim());
	            List<String> searchEngines = new ArrayList<>();
	            parseQueryAndSearchInput(searchEngineNum,searchEngines,scan);
	               
	            //read in Queries
	            int queriesNum = Integer.parseInt(scan.nextLine().trim());
	            List<String> queries = new ArrayList<>();
	            parseQueryAndSearchInput(queriesNum,queries,scan);
	               
	            int minimumSwitch = findMinSwitch(searchEngines,queries);
	               
	            codeJamFormattedPrint(i,minimumSwitch); 
	                
	            }
	    }catch(Exception e){
	    	e.printStackTrace();
	    }

		    
	}
	
	public static void parseQueryAndSearchInput(int numOccurences, List<String> resultContainer, Scanner scan){
	   for(int j = 0; j < numOccurences; j++){
	       String entry = scan.nextLine().trim();
	       resultContainer.add(entry);
	   }    
	}
	
	public static int findMinSwitch(List<String> S,List<String> Q){
		//as a recursive algorithm
		// descision set O : where to send the queries
		//what's affected: query size decreases, S is always the same
		//computation: for each combination of routing, calculate the no. of switches & compare at base case :
		
		/**
		 * Dynamic programming:
		 * 
		 * K[Q,s] = the min # of switches  given a query of Q and router currently pointing to s
		 *   you can route q to any of the possible Ss. that is one context switch.
		 * assume you know K[Q-1,S]
		 * 
		 * what if you add the Qth one: 
		 * 
		 * K[Q,S] = min for each i (K[Q-1,Si]+ possible context switch)
		 * 
		 * let's formulate the base case:
		 * 
		 * 1 querty = => K[1,Si] = 0 if Si
		 * Queries = Google, Bing, yahoo
		 *
		 * 
		 * --->
		 * 
		 * engins = {Google, Bing, Ask.com, Yahoo, ecosia, duckgo}
      for just gooogle: -      0     0        0     0        0   
      index 2(Bing):    1      -      0        0     0         0
      yahoo:         
      
      for each current word:
      ideally stay:
      
      base case is starting point which is K[1,S]
      
      when looking at K[2,s], you look the 2nd query and mark K[2, 2nd query] as false
      then you find the min(continuing vs coming from K[1,rest])
		 * /
		 * 
		 * 
		**/
		int[][] K = new int[Q.size()][S.size()];
		
		//base case set up
		//-1 indicates impossible to route q query to s search engine
		for(int i =0; i < S.size();i++){
		    if(Q.get(0).equals(S.get(i))){//if first query is equal to any of engines
		        K[0][i] = -1;//can't be starting point for next round
		    }
		    K[0][i] = 0;
		    
		}
		
		
		for(int q = 1; q < Q.size();q++){
		    for(int s=0; s< S.size(); s++){
		       
		        if(Q.get(q).equals(S.get(s))){//if first query is equal to any of engines
		            K[q][s] = -1;//can't be starting point for next round
		        }else{
		            
		            //find search engine to route to
		            int previousMin = 100;
		            for(int i = 0 ; i < S.size();i++){
		            	/**
		            	i == s ==> no cost in routing from i => s, if possible
		            	K[q-1,i] != -1. ==> previous sub problem must not have resulted in universe explosion
		            	K[q-1,i] < previousMin ==> min number of turns when prev query is placed at current engine i
		            	**/
		                if(i!=s && K[q-1][i] != -1 && K[q-1][i] < previousMin){
		                    previousMin = K[q-1][i]; 
		                }
		            }
		            K[q][s] = Math.min(K[q-1][s],previousMin + 1);  
		        }
		    }
		        
		}
		return K[Q.size()-1][S.size()-1];

	}
	
	public static void  codeJamFormattedPrint(int X,int Y){
	    System.out.println("Case #"+X+": "+Y); 
	}
}
