import java.util.*;
import java.io.*;


/**

DP pseudocode:

Base case:
if 1 senator left, return false
if 0 senator left, return true

recursive case:
inputs: List of senators S
memo -> Maps <RemainingSenator, Solution>

decision 1: take 0th out of S and if S/0th valid then recurse and find the solution

decision 2: take 1st out S, and if S/{0, 1} is valid then recurse and find solution

decision 3: put back 0th, and if S/{1} is valid, then recurse and find solution

if either one of the three is true, return true
if current config is not valid: return false;


Greedy algorithm pseudocode:
Map<Party, Senator> 
List<Senators> evacPolicy;

PriorityQueue<Party> qp
if the number of parties is > 2:
    until two parties remain:
        get the party with the most senators.
        remove a senator and add to evac policy
        put the party back. if there aren't any senators remove it

if the number of parties is 2:
        get the two parties
        remove one senator from each party and add to evac Policy
        put them back



**/
class Senators {
    List<Integer> senators = new ArrayList<>();
    public void add(int senator) {
        senators.add(senator);
    }
}
public class SenateEvacuation {

    public static List<Senators> evacuateSenatorsDP() {

    }

    public static List<Senators> evacuateSenatorsGreedy(int[] senators, in N) {
        Map<Integer, Integer> partySenateCount = new HashMap<>();
        for (int i = 0; i < senators; i++) {
            partySenateCount.put(senators[i], partySenateCount.getOrDefault(senators[i], 0) + 1);
        }

        PriorityQueue<Integer> maxHeap = new PriorityQueue<>((a,b) -> (partySenateCount.get(b)- partySenateCount.get(a)));
        List<Senators> evacPlan = new ArrayList<>();

        for (Integer party : partySenateCount.keySet()) {
            maxHeap.add(party);
        }
        while (!maxHeap.isEmpty()) {
            Senators s = new Senators();
            int largestParty = maxHeap.poll();
            s.add(largestParty);
            partySenateCount.put(largestParty, partySenateCount.get(largestParty) -1);
            if (partySenateCount.get(largestParty) == 0) partySenateCount.remove(largestParty);
            else maxHeap.add(largestParty);
            
            if (maxHeap.size() == 2) {
                int party2 = maxHeap.poll();
                s.add(party2);
                partySenateCount.put(party2, partySenateCount.get(party2) -1);
                if (partySenateCount.get(party2) == 0) partySenateCount.remove(party2);
                else maxHeap.add(party2);
            }
            evacPlan.add(s);

        }

        return evacPlan;
    }


    public static void main(String[] args) {
        Scanner in = new Scanner(new BufferedReader(new InputStreamReader(System.in)));
        int t = in.nextInt(); 
        in.nextLine();
        for (int i = 1; i <= t; ++i) {
            int N = in.nextInt(); 
            in.nextLine();

            int[] senators= new int[N]
            String[] input = in.nextLine().split(" ");
            for (int j = 0; j < N; j++) {
                senators[j] = Integer.parseInt(input[j]);
            }
            List<Senators> evacOrder = evacuateSenatorsGreedy(senators, N);
            System.out.println("Case #" + i + ": ");
            print(evacOrder); //TODO: implement
        }
    }
    
}
