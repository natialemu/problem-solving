import java.util.*;
import java.io.*;


/**

Problem: https://codingcompetitions.withgoogle.com/codejam/round/0000000000201bef/0000000000201c8b


**/
class Senators {
    List<Integer> senators = new ArrayList<>();
    public void add(int senator) {
        senators.add(senator);
    }

    public List<Integer> senators() {
        return senators;
    }
}
class Senator {
    int id;
    int party;

    public int id() {return id;}
    public int party() {return party;}
}
class RemainingSenators {


    public RemainingSenators(int[] senators) {
        //TODO
    }
    public Senator evacuateSenator() {
        return null;//TODO

    }
    public int size() {
        return -1; //TODO

    }

    public boolean majorityExists() {
        return true; //TODO

    }

    public void putBackSenator(Senator s) {

    }

    @Override
    public boolean equals(Object o) {
        return true; //TODO
    }

    @Override
    public int hashCode() {
        return -1; //TODO

    }
}
public class SenateEvacuation {

    public static List<Senators> evacuateSenatorsDP(int[] senators, int N) {
        RemainingSenators remainingSenators = new RemainingSenators(senators);
        return evacuateSenatorsDP(remainingSenators, new HashMap<>());
    }

    private static List<Senators> evacuateSenatorsDP(RemainingSenators remainingSenators, Map<RemainingSenators, List<Senators>> memo) {
        if (remainingSenators.size() == 1) return null;
        
        if (remainingSenators.size() == 0) return new ArrayList<>();

        Senator firstEvacuatedSenator = remainingSenators.evacuateSenator();
        Senators currentEvacuatedSenators = new Senators();
        List<Senators> evacuatedSenators;
        if (!remainingSenators.majorityExists()) {
            evacuatedSenators = memo.containsKey(remainingSenators) ? memo.get(remainingSenators) :evacuateSenatorsDP(remainingSenators, memo);
            
            if (evacuatedSenators != null) {
                currentEvacuatedSenators.add(firstEvacuatedSenator.id());
                evacuatedSenators.add(currentEvacuatedSenators);
                memo.put(remainingSenators, evacuatedSenators);
                return evacuatedSenators;
            }
        }
        Senator secondEvacuatedSenator = remainingSenators.evacuateSenator();
        if (!remainingSenators.majorityExists()) {
            evacuatedSenators = memo.containsKey(remainingSenators) ? memo.get(remainingSenators) :evacuateSenatorsDP(remainingSenators, memo);
            if (evacuatedSenators != null) {
                currentEvacuatedSenators.add(secondEvacuatedSenator.id());
                evacuatedSenators.add(currentEvacuatedSenators);
                memo.put(remainingSenators, evacuatedSenators);
                return evacuatedSenators;
            }
        }
        remainingSenators.putBackSenator(firstEvacuatedSenator);
        if (!remainingSenators.majorityExists()) {
            evacuatedSenators = memo.containsKey(remainingSenators) ? memo.get(remainingSenators) :evacuateSenatorsDP(remainingSenators, memo);
            if (evacuatedSenators != null) {
                currentEvacuatedSenators.add(firstEvacuatedSenator.id());
                currentEvacuatedSenators.add(secondEvacuatedSenator.id());
                evacuatedSenators.add(currentEvacuatedSenators);
                memo.put(remainingSenators, evacuatedSenators);
                return evacuatedSenators;
            }
        }

        remainingSenators.putBackSenator(secondEvacuatedSenator);
        memo.put(remainingSenators, null);

        return null;

    }

    public static List<Senators> evacuateSenatorsGreedy(int[] senators, int N) {
        Map<Integer, Integer> partySenateCount = new HashMap<>();
        for (int i = 0; i < senators.length; i++) {
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

            int[] senators= new int[N];
            String[] input = in.nextLine().split(" ");
            for (int j = 0; j < N; j++) {
                senators[j] = Integer.parseInt(input[j]);
            }
            List<Senators> evacOrder = evacuateSenatorsGreedy(senators, N);
            System.out.println("Case #" + i + ": ");
            print(evacOrder); //TODO: implement
        }
    }

    private static void print(List<Senators> evacOrder) {
        for (Senators senators : evacOrder) {
            for (Integer senator : senators.senators()) {
                System.out.print(senator + " ");
            }
            System.out.println();
        }
    }
    
}
