package LeetCode;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Stack;

/**
 * Implement regular expression matching with support for '.' and '*'.
    '.' Matches any single character.
	 '*' Matches zero or more of the preceding element.

	 The matching should cover the entire input string (not partial).

	 The function prototype should be:
	 bool isMatch(const char *s, const char *p)

	 Some examples:
	 isMatch("aa","a") → false
	 isMatch("aa","aa") → true
	 isMatch("aaa","aa") → false
	 isMatch("aa", "a*") → true
	 isMatch("aa", ".*") → true
	 isMatch("ab", ".*") → true
	 isMatch("aab", "c*a*b") → true
 */
public class RegularExpressionMatching {


	// 递归方式匹配
	public static boolean isMatch(String s, String p) {

		// 如果 s 和 p 都是空，表明匹配完成
		if (p.isEmpty()) return s.isEmpty();
		// p 的第一个字符不能为 *
		if (p.charAt(0) == '*') return false;

		char first = p.charAt(0);
		// 如果 p 的长度为1， 那么判断这一个字符是否匹配s
		if (p.length() == 1) {
			return s.length() == 1 && (first == '.' || first == s.charAt(0));
		}

		char second = p.charAt(1);

		if (second == '*') {
			boolean result = isMatch(s, p.substring(2));
			int i=1, len = s.length();

			if (len == 0) return isMatch(s, p.substring(2));
			if (first != '.' && first != s.charAt(0)) return isMatch(s, p.substring(2));
			for (; i<len && !result; i++) {
				if (first != '.' && first != s.charAt(i)) break;
				result = isMatch(s.substring(i), p.substring(2));
			}
			return result || isMatch(s.substring(i), p.substring(2));
		} else {
			return !s.isEmpty() && (first == '.' || first == s.charAt(0) ) && isMatch(s.substring(1), p.substring(1));
		}
	}

	// 动态规划方式匹配

	/**
	 * if p.charAt(j) == s.charAt(i) dp[i][j] = dp[i-1][j-1]
	 * if p.charAt(j) == '.' dp[i][j] = dp[i-1][j-1]
	 * if p.charAt(j) == '*'
	 *          1 if p.charAt(j-1) != s.charAr(i) dp[i][j] = dp[i][j-2]  //in this case, a* only counts as empty
	 *          2 if p.charAt(i-1) == s.charAt(i) or p.charAt(i-1)='.'
	 *                  dp[i][j] = dp[i-1][j]
	 *              or dp[i][j] = dp[i][j-1]
	 *              or dp[i][j] = dp[i][j-2]
	 * @param s
	 * @param p
	 * @return
	 */

	public static boolean isMatchDP(String s, String p) {
		if (s == null || p == null) return false;

		boolean [][] dp = new boolean[s.length() + 1][p.length() + 1];
		dp[0][0] = true;
		for (int i=0; i<p.length(); i++) {
			if (p.charAt(i) == '*' && dp[0][i-1]) {
				dp[0][i+1] = true;
			}
		}

		for (int i=0; i<s.length(); i++) {
			for (int j=0; j<p.length(); j++) {
				if (p.charAt(j) == '.' || p.charAt(j) == s.charAt(i)) {
					dp[i+1][j+1] = dp[i][j];
				} else if (p.charAt(j) == '*') {
					if (p.charAt(j-1) != s.charAt(i) && p.charAt(j-1) != '.') {
						dp[i+1][j+1] = dp[i+1][j-1];
					} else {
						dp[i+1][j+1] = (dp[i+1][j] || dp[i][j+1] || dp[i+1][j-1]);
					}
				}
			}
		}

		return dp[s.length()][p.length()];
	}

	public static void main(String [] args) {
		System.out.println(isMatchDP("a", ".*a*"));
	}
}
