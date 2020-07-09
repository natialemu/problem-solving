import java.util.*;
import java.io.*;

/**
Problem description: https://leetcode.com/problems/brace-expansion-ii/
**/

	// this represents the operation to apply on two expressions.
	enum Operator {
		UNION(),
		MERGE();
		Operator() {}
	}
	// this class represents the result from parsing a string and separting it into a set of expressions
	// the operator that is used to separate the expressions is 'operator' field
	class ParsedResult {
		List<Integer[]> subStrings = new ArrayList<>();
		Operator operator;
		public ParsedResult(List<Integer[]> subStrings, Operator operator) {
			this.subStrings = subStrings;
			this.operator = operator;
		}
	}

public class BraceExpansion {





	public static String expandExpression(String s) {
		//return the expanded expression enclosed within curly braces
		return "{" + expandExpression(s, 0, s.length() - 1, new HashMap<>()) + "}";
	}

	public static String expandExpression(String s, int i, int j, Map<Integer, Map<Integer, String>> memo) {
		// if the expression is only a single characer, the expanded
		//form is just the character
		if (i == j) return s.charAt(i) + "";

		//parse the string within i and j. the string to parse may or may not be enclosed within curly braces 
		ParsedResult results = s.charAt(i) == '{' && s.charAt(j) == '}' ? parse(s, i + 1, j - 1) : parse(s, i, j);


		if (results == null || results.subStrings.size() == 0) throw new IllegalArgumentException("Input string " + s.substring(i, j + 1) + " couldn't be parsed");
		
		//first parsed sub problem
		Integer[] currentParsed = results.subStrings.get(0);
		// the expanded string of the first sub problem
		String currentParsedResult = memo.containsKey(currentParsed[0]) && memo.get(currentParsed[0]).containsKey(currentParsed[1]) ?
					memo.get(currentParsed[0]).get(currentParsed[1]) : expandExpression(s, currentParsed[0], currentParsed[1], memo);

		for (int k = 1; k < results.subStrings.size(); k++) {
			Integer[] nextParsed = results.subStrings.get(k);
			// get the expanded result of the next sub problem
			String nextParsedResult = memo.containsKey(nextParsed[0]) && memo.get(nextParsed[0]).containsKey(nextParsed[1]) ?
					memo.get(nextParsed[0]).get(nextParsed[1]) : expandExpression(s, nextParsed[0], nextParsed[1], memo);

			// apply the appropriate operator on the two expanded expressions
			if (results.operator.equals(Operator.UNION)) currentParsedResult = union(currentParsedResult, nextParsedResult);
			if (results.operator.equals(Operator.MERGE)) currentParsedResult = merge(currentParsedResult, nextParsedResult);
		}
		Map<Integer, String> solnMemo = new HashMap<>();
		solnMemo.put(j, currentParsedResult);
		memo.put(1, solnMemo);
		return currentParsedResult;
	}

	// returns the union of the expressions in s1 and s2 while preserving order.
	private static String union(String s1, String s2) {
		Set<String> deduper = new HashSet<>();
		String[] s1Array = s1.split(",");
		String[] s2Array = s2.split(",");
		StringBuilder unionBuilder = new StringBuilder();
		for (String str : s1Array) {
			unionBuilder.append(str + ",");
			deduper.add(str);
		}
		for (String str : s2Array) {
			if (!deduper.contains(str)) unionBuilder.append(str + ",");
			deduper.add(str);
		}
		String unionString =unionBuilder.toString();
		return unionString.substring(0, unionString.length() - 1);

	}

	// merges s1 and s2 by returning all combinations of one expr in s1 with another in s2.
	private static String merge(String s1, String s2) {
		String[] s1Array = s1.split(",");
		String[] s2Array = s2.split(",");
		StringBuilder mergeBuilder = new StringBuilder();
		for (String str1 : s1Array) {
			for (String str2 : s2Array) {
				mergeBuilder.append(str1 + str2 + ",");
			}
		}

		String mergedString = mergeBuilder.toString();
		return mergedString.substring(0, mergedString.length() - 1);
	}

	private static ParsedResult parse(String s, int i, int j) {
		
		// if 's' is a comma separted set of expressions, parse while applying union. 
		// Otherwise apply the merge operator
		if (exprIsSeparatedByComma(s, i, j)) return new ParsedResult(parse(s, i, j, ',', 1), Operator.UNION);
		else return new ParsedResult(parse(s, i, j, ' ', 0), Operator.MERGE);
	}

	private static List<Integer[]> parse(String s, int i, int j, char exprSeparator, int shift) {
			Stack<Character> braceStack = new Stack<Character>();
			Integer startIndex = i;
			List<Integer[]> parsedResult = new ArrayList<>();
			for (Integer k = i; k <= j; k++) {
				if (s.charAt(k) == '{') braceStack.push(s.charAt(k));
				else if (s.charAt(k) == '}') braceStack.pop();

				if (braceStack.isEmpty() && k + 1 <= j && s.charAt(k + 1) == exprSeparator) {
					parsedResult.add(new Integer[] {startIndex + 1, k - 1});
					startIndex = k + 1 + shift;
				}
			}
			return parsedResult;

	}

	private static boolean exprIsSeparatedByComma(String s, int i, int j) {
			Stack<Character> braceStack = new Stack<Character>();
			for (int k = i; k <= j; k++) {
				if (s.charAt(k) == '{') braceStack.push(s.charAt(k));
				else if (s.charAt(k) == '}') braceStack.pop();
				if (braceStack.isEmpty() && k + 1 <= j && s.charAt(k + 1) == ',') return true;
			}
			return false;
	}

	public static void main(String[] args) {
		Scanner in = new Scanner(new BufferedReader(new InputStreamReader(System.in)));
        int t = in.nextInt(); 
        in.nextLine();
        for (int i = 1; i <= t; ++i) {
          String expr = in.nextLine();
          String expandedExpr = expandExpression(expr);
          System.out.println("Case #" + i + ": " + (expandedExpr));
     	}
	}
	
}