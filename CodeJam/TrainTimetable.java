

import java.io.*;
import java.util.*;

public class TrainTimeTable{

	class Time implements Comparable{
		String hr;
		String min;
		char sourcePlatform; //A or B

		public Time(String hr, String min, char sourcePlatform){
			this.hr = hr;
			this.min = min;
			this.sourcePlatform = sourcePlatform;

			assert(hr.length() == 2);
			assert(min.length() == 2);
			assert((int)hr.getCharAt(0)  < 3); //24 hr format

		}

        @Override
   		public boolean equals(Object o){
   			Time other = (Time) o;
   			if(other instanceof this && Integer.parseInt(other.hr) == Integer.parseInt(hr) && Integer.parseInt(other.min) == Integer.parseInt(min) && 
   				sourcePlatform == other.sourcePlatform){
   				return true;
   			}
   			return false;
   		}
   		@Override
   		public int hashCode(){
   			return Objects.hash(Integer.parseInt(hr), Integer.parseInt(min));
   		}

   		@Override
   		public int compareTo(Object o){
   			Time other = (Time) o;
   			if(Integer.parseInt(hr) > Integer.parseInt(other.hr)){
   				return 1;
   			}else if(Integer.parseInt(hr) < Integer.parseInt(other.hr)) {
   				return 1-;
   			}else{
   				if(Integer.parseInt(min) > Integer.parseInt(other.min)){
   					return 1;
   				}else if(Integer.parseInt(min) < Integer.parseInt(other.min)){
   					return -1;
   				}else{
   					return 0;
   				}
   			}

   		}

   		public Time add(int minutesToAdd){

            int totalMin = Integer.parseInt(min) + minutesToAdd;
            String newMin = timeFormatted(Integer.toString(totalMin%60));
            String newhr = timeFormatted(Integer.parseInt(Integer.parseInt(hr) + totalMin/60));
   			Time newTime = new Time(newhr, newMin,sourcePlatform);
   			return newTime;

   		}

	}

	public static void main (String[] args) throws java.lang.Exception
	{
	    try{
	        Scanner scan = new Scanner(System.in);
	       
	        int testCaseNum = Integer.parseInt(scan.nextLine().trim());
	        for(int i = 0; i < testCaseNum; i++){
	              
                //read in search engnines
                int turnAroundTime = Integer.parseInt(scan.nextLine().trim());
                String[] numDepartures = scan.nextLine().trim().split(" ");
                int NA = Integer.parseInt(numDepartures[0]);
                int NB = Integer.parseInt(numDepartures[1]);

                List<Time> departureTimes = new ArrayList<>();
                Map<Time,Time> departureToArrivalMap = new HashMap<>();

                for(int i =0; i < NA; i++){
                	String[] departureAndArrivalTimes = scan.nextLine().trim().split(" ");
                	String departure = departureAndArrivalTimes[0];
                	String arrival = departureAndArrivalTimes[1];
                	
                	Time departureTime = new Time(departure.split(":")[0], departure.split(":")[1],'A');
                	Time arrivalTime = new Time(arrival.split(":")[0], arrival.split(":")[1],'A');

                	departureToArrivalMap.put(departureTime,arrivalTime);
                	departureTimes.add(departureTime);


                }

                 for(int i =0; i < NB; i++){
                	String[] departureAndArrivalTimes = scan.nextLine().trim().split(" ");
                	String departure = departureAndArrivalTimes[0];
                	String arrival = departureAndArrivalTimes[1];
                	
                	Time departureTime = new Time(departure.split(":")[0], departure.split(":")[1],'B');
                	Time arrivalTime = new Time(arrival.split(":")[0], arrival.split(":")[1],'B');

                	departureToArrivalMap.put(departureTime,arrivalTime);
                	departureTimes.add(departureTime);


                }

                Collections.sort(departureTimes);
                Collections.reverse(departureTimes);

                int numTrains = findNumTrains(departureToArrivalMap,turnAroundTime,departureTimes);

                codeJamFormattedPrint(i,numTrains);

              
            }
                
                /*
                - idea is to put all the time of departure objects in a set 
                - sort them
                - while true:
                -    add a train
                -    take out the first one in the current set and then call the simulate object(). simulate takes the set & the turnaround time
                -    check the size of set and if zero, exit loop


                */

                
	               
	             //for printing solution
	                
	            
	    }catch(Exception e){
	    	e.printStackTrace();
	    }

		    
	}

	public static int findNumTrains(Map<Time, Time> departureToArrivalMap,int turnAroundTime,List<Time> departureTimes){
		int numTrains=0;
		while(true){
			numTrains++;
			Time trainDeparture = departureTimes.remove(departureTimes.size()-1);
			if(departureToArrivalMap.containsKey(trainDeparture)){
				simulateTrainPath(departureToArrivalMap,trainDeparture,turnAroundTime);
			}
			if(departureToArrivalMap.size() == 0){
				break;
			}

		}
		return numTrains;

	}

	public static void simulateTrainPath(Map<Time, Time> departureToArrivalMap,Time trainDepartureTime, int turnAroundTime){
		Time newDepartureTime = trainDepartureTime;

		while(departureToArrivalMap.containsKey(newDepartureTime)){
			departureToArrivalMap.removeKey(newDepartureTime);
			Time trainArrivalTime = departureToArrivalMap.get(newDepartureTime);
			newDepartureTime = trainArrivalTime.add(turnAroundTime);
		}

	}

	public static void  codeJamFormattedPrint(int X,int Y){
	    System.out.println("Case #"+X+": "+Y); 
	}
	

}