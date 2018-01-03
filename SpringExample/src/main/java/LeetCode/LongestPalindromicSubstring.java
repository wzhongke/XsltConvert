package leetcode;

import java.util.*;

/**
 * Given a string s, find the longest palindromic substring in s. You may assume that the maximum length of s is 1000.
 * Example:
 * Input: "babad"
 * Output: "bab"
 * Note: "aba" is also a valid answer.
 * Example:
 * Input: "cbbd"
 * Output: "bb"
 */

public class LongestPalindromicSubstring {
	public static String longestPalindrome(String s) {
		if (s == null || s.isEmpty()) return "";
		HashMap<Byte, List<Integer>> positionMap = new HashMap<>(s.length());

		int maxLen = 0, start = 0, end = 0;
		byte[] bytes = s.getBytes();
		List<Integer> pList = new ArrayList<>();
		pList.add(0);
		positionMap.put(bytes[0], pList);
		for (int i = 1; i < bytes.length; i++) {
			if (i == end + 1 && start - 1 > 0 && bytes[i] == bytes[start - 1]) {
				maxLen += 1;
				start--;
				end++;
				continue;
			}

			if (positionMap.containsKey(bytes[i])) {
				for (int p : positionMap.get(bytes[i])) {
					if (maxLen > i - p) break;
					int x = p + 1, k = i - 1;
					for (; x <= k; x++, k--) {
						if (bytes[x] != bytes[k]) break;
					}

					if (x >= k && i - p > maxLen) {
						maxLen = i - p;
						start = p;
						end = i;
						break;
					}
				}
				positionMap.get(bytes[i]).add(i);
			} else {
				List<Integer> list = new ArrayList<>();
				list.add(i);
				positionMap.put(bytes[i], list);
			}
		}

		return s.substring(start, end + 1);
	}


	/**
	 * Example: "xxxbcbxxxxxa", (x is random character, not all x are equal) now we
	 * are dealing with the last character 'a'. The current longest palindrome
	 * is "bcb" with length 3.
	 * 1. check "xxxxa" so if it is palindrome we could get a new palindrome of length 5.
	 * 2. check "xxxa" so if it is palindrome we could get a new palindrome of length 4.
	 * 3. do NOT check "xxa" or any shorter string since the length of the new string is
	 * no bigger than current longest length.
	 * 4. do NOT check "xxxxxa" or any longer string because if "xxxxxa" is palindrome
	 * then "xxxx" got  from cutting off the head and tail is also palindrom. It has
	 * length > 3 which is impossible.'
	 *
	 * @param s
	 * @return
	 */

	private static byte[] sByte;

	public static String alongestPalindrome(String s) {
		int currLength = 0, start = 0, end = 0, len = s.length();
		sByte = s.getBytes();
		for (int i = 0; i < len; i++) {
			if (isPalindrome(i - currLength - 1, i)) {
				start = i - currLength - 1;
				end = i + 1;
				currLength = currLength + 2;
			} else if (isPalindrome(i - currLength, i)) {
				start = i - currLength;
				end = i + 1;
				currLength = currLength + 1;
			}
		}
		return s.substring(start, end);
	}

	public static boolean isPalindrome(int begin, int end) {
		if (begin < 0) return false;
		while (begin < end) {
			if ((sByte[begin++] ^ sByte[end--]) > 0) return false;
		}
		return true;
	}

	public static void main(String[] args) {
		long start = new Date().getTime();
		System.out.println(alongestPalindrome("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"));
		System.out.println(new Date().getTime() - start);
	}
}
