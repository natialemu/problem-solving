

import java.util.*;
import java.io.*;
/**

Student attendance record:
Generate all 'rewardable' attendance records(strings made of only 'L', 'A', 'P') of size 'n'. 
Rewardable if:
	-> Max 1 As
	-> Max of 2 consecutive Ls


Full problem description: https://leetcode.com/problems/student-attendance-record-ii/

**/

public class StudentAttendanceRecord {
    private static final List<Character> records = Arrays.asList('A', 'L', 'P');

    // represents a partially built permutation of size < n
	class PartialPerm {

		int numAs;					//num of As for this partial permutation
		int numConsecutiveLs;		// num of consecutive Ls for this partial permutation
		char[] records;				// the valid partial rewardable record built so far
		int position;				// position to decide on next
		public PartialPerm(int n) {
			records = new char[n];
		}

		// create a new PartialPerm object identical to this one in content
		public PartialPerm clone() {
			PartialPerm clonePerm = new PartialPerm(records.length);
			for (int i = 0; i < records.length; i++) {
				clonePerm.records[i] = records[i];
			}
			clonePerm.position = position;
			clonePerm.numAs = numAs;
			clonePerm.numConsecutiveLs = numConsecutiveLs; 
			return clonePerm;
		}
	}
	public long rewardableRecords(int n, boolean bfs) {
		long desiredRecords = bfs ? rewardableRecordsBFS(n) : rewardableRecordsDFS(n);
		
		return (desiredRecords % (((int)Math.pow(10, 9)) + 7));
	}

	/**
	Algorithm 1: BFS
	slowly build all valid rewardable records via breadth first search
	**/
	private long rewardableRecordsBFS(int n) {

		long rewardableRecords = 0;
		//start with a partial permutation which contains an empty string and slowly build the full permutation
		Queue<PartialPerm> q = new LinkedList<>();
		q.add(new PartialPerm(n));
		while (!q.isEmpty()) {
			PartialPerm current = q.poll();
			for (Character record : records) {
				// the 'record' is allowed to be placed at 'position' of the current partial permutation
				if (allowed(current.numAs, current.numConsecutiveLs, record)) {

					// generate a new partial permutation of bigger size
					PartialPerm newPermutation = current.clone();
					newPermutation.records[newPermutation.position] = record;
					if (record == 'A') newPermutation.numAs += 1;
					if (record == 'L') newPermutation.numConsecutiveLs += 1;
					if (record == 'P') newPermutation.numConsecutiveLs = 0;
					newPermutation.records[newPermutation.position] = record;
						
					// if the size of the partial perm is n, then the desired full rewardble record has been constructed,
					// otherwise add it to the queue for further processing
					if(newPermutation.position < n - 1) q.add(newPermutation);
					else rewardableRecords += 1;
					newPermutation.position += 1;
				}
			}
		}
		return rewardableRecords;

	}

	//Only one A and less than 3 consecutive Ls are allowed
	private boolean allowed(int numAs, int numConsecutiveLs, Character c) {
		return (c == 'A' && numAs == 0) || (c == 'L' && numConsecutiveLs < 2) || c == 'P';
	}

	private long rewardableRecordsDFS(int n) {
		char[] records = new char[n];
		return rewardableRecordsDFS(records, 0, n, 0, 0);
	}
	private long rewardableRecordsDFS(char[] c, int position, int n, int numAs, int numConsecutiveLs) {

		//base case: finished building one rewardable record. return 1
		if (position == n) {
		 	return 1;
		}

		long currentRewardableRecords = 0;
		for (Character record : records) {
			if (allowed(numAs, numConsecutiveLs,  record)) {
				//place valid record on the current location in the array and continue to recurse
				// and decide for the remaining positions
				c[position] = record;
				long subProblemSoln = 0;
				if (record == 'A') subProblemSoln = rewardableRecordsDFS(c, position + 1, n, numAs + 1, numConsecutiveLs);
				if (record == 'L') subProblemSoln = rewardableRecordsDFS(c, position + 1, n, numAs, numConsecutiveLs + 1);
				if (record == 'P') subProblemSoln = rewardableRecordsDFS(c, position + 1, n, numAs, 0) ;
				currentRewardableRecords += subProblemSoln;
				c[position] = ' '; // undo decision to assign 'record' to 'position' and explore other options
			}
		}
		return currentRewardableRecords;
	}
	public static void main(String[] args) {

		Scanner in = new Scanner(new BufferedReader(new InputStreamReader(System.in)));
        int t = in.nextInt(); 
        for (int i = 1; i <= t; ++i) {
          int n = in.nextInt();
          StudentAttendanceRecord attendanceRecord = new StudentAttendanceRecord();
          long r = attendanceRecord.rewardableRecords(n, false);
          System.out.println("Case #" + i + ": " + (r));
        }

	}
}