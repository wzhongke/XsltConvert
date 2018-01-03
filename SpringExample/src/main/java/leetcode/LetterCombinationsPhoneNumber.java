package leetcode;

import java.util.*;

/**
 * Given a digit string, return all possible letter combinations that the number could represent.

    A mapping of digit to letters (just like on the telephone buttons) is given below.

	 Input:Digit string "23"
	 Output: ["ad", "ae", "af", "bd", "be", "bf", "cd", "ce", "cf"].
 */
public class LetterCombinationsPhoneNumber {

	private static HashMap<Character, String[]> map = new HashMap<>();

	static {
		map.put('2', new String[] {"a", "b", "c"});
		map.put('3', new String[] {"d", "e", "f"});
		map.put('4', new String[] {"g", "h", "i"});
		map.put('5', new String[] {"j", "k", "l"});
		map.put('6', new String[] {"m", "n", "o"});
		map.put('7', new String[] {"p", "q", "r", "s"});
		map.put('8', new String[] {"t", "u", "v"});
		map.put('9', new String[] {"w", "x", "y", "z"});
	}

	public List<String> letterCombinations(String digits) {
		Set<String> resultSet = new HashSet<>();
		for (char c: digits.toCharArray()) {
			if (resultSet.size() == 0) {
				resultSet.addAll(Arrays.asList(map.get(c)));
				continue;
			}
			if (map.containsKey(c)) {
				resultSet = combine(resultSet, c);
			}
		}
		List<String> result = new ArrayList<>();
		result.addAll(resultSet);
		return result;
	}

	public Set<String> combine (Set<String> set, char n) {
		Set<String> comSet = new HashSet<>();
		for (String s: set) {
			for (String t: map.get(n)) {
				String tmp = s + t;
				if (!comSet.contains(tmp)) {
					comSet.add(tmp);
				}
			}
		}
		return comSet;
	}

	public static void main(String [] args) {
		System.out.println(new LetterCombinationsPhoneNumber().letterCombinations("234"));
	}
}
