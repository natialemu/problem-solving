/* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
        /*
        step 1: try to find all subsets of the set of natural numbers less than A that add up to A
        step 2: create a function Q(int X, subset) and pass each subset into this function
        step 3: try to find subsets of size m less than X that add up to X
        step 3: create a function computeQ(Xsubset, Asubuset) that performs the computation
        
        
        */


public class WeightedProducts{
	int X;
	int A;
	int m;

	Configuration initialConfiguration;

	class Configuration {
		List<Integer> originalSet;
		List<Integer> subset;
		int constraint;

		public Configuration(int constraint){
			this.constraint = constraint;

			for(int i = 1; i < constraint; i++){
				originalSet.add(i);
			}
		}

		public List<Integer> getOriginalSet(){
			return originalSet;
		}
		public List<Integer> getSubset(){
			return subset;
		}
		public void addTosubset(int i ){
			subset.add(i);
			
		}
		public void removeFromOriginalSet(int index){
			originalSet.remove(index);
		}

	}

	public WeightedProducts(int X, int A, int m){
		this.X = X;
		this.A = A;
		this.m = m;
		//By default it's A first
		initialConfiguration = new Configuration(A);
		
	}

	public List<List<Integer>> subsets(){
		List<List<Integer>> sets = new ArrayList<>();
		subsets(initialConfiguration,sets);
		return sets;
	}

	public void resetConfiguration(int constraint){
		initialConfiguration = new Configuration(constraint, m);
	}


    //Find all subsets whose sum is equal to the constraint of the configuration
	public void subsets(Configuration initialConfiguration, List<List<Integer>> sets, subsetLength){


		if(initialConfiguration.getOriginalSet.size() == 0){
			if(constraint == 0 &&  subsetLength == initialConfiguration.getSubset().size()){
				sets.add(initialConfiguration.getSubset());
			}
			
			 return;
		}
		else{	
			int x = initialConfiguration.getOriginalSet().get(i);
			
			initialConfiguration.removeFromOriginalSet(i);

			subsets(initialConfiguration, sets, subsetLength);
            if(initialConfiguration.constraint > x && initialConfiguration.getSubset().size() > subsetLength()){
				initialConfiguration.addTosubset(x);
				initialConfiguration.constraint = initialConfiguration.constraint - x;

				subsets(initialConfiguration, sets, subsetLength);
		    } 


		}


	}

	public long computeQ(List<List<Integer>> Xsubset, List<Integer> Asubset){

		/*
		   maxQ
           for each xusbet:
               sort it in reserve
               comput Q
               if(computed > maxQ) maxQ = computed

            return maxQ
		*/
        long maxQ = -1;
		for(List<Integer> x: Xsubset){
                long computed = computeQ(x,Asubset);
                if(computed > maxQ) maxQ = computed;
		}

		return maxQ;
        

	}

	private long computeQ(List<Integer> Xsubset, List<Integer> Asubset){
		long product  = 1;
		for(int i = 0; i < Xsubset.size(); i++){
			product *= Math.pow(Xsubset.get(i),Asubset.get(i));

		}

		return product%(Math.pow(10,9) + 7);

	}

	public static void main(String[] args){
		WeightedProducts products = new WeightedProducts(6,3,2);
		List<List<Integer>> Asubsets = products.subsets();// a1, a2, ...
		products.resetConfiguration(6);
		List<List<Integer>> Xsubsets = products.subsets();// X1, X2

		
		long totalProduct = 0
        for( List<Integer> i: Asubsets){
        	totalProduct += products.computeQ(Xsubsets,i);
        }

        System.out.println(totalProduct);
		

	}
}