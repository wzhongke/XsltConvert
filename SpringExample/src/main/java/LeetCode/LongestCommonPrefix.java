package LeetCode;

import java.util.Arrays;

/**
 * Write a function to find the longest common prefix string amongst an array of strings.
 */

public class LongestCommonPrefix {
	public String longestCommonPrefix(String[] strs) {
		if (strs == null || strs.length ==0) {
			return "";
		}
		String result = "";
		int minLen = strs[0].length();
		for (int i=1; i<strs.length; i++) {
			if (strs[i].length() < minLen) {
				minLen = strs[i].length();
			}
		}

		char firstChar;
		for (int i=0; i<minLen; i++) {
			firstChar = strs[0].charAt(i);
			for (int j=1; j<strs.length; j++) {
				if (strs[j].charAt(i) != firstChar) {
					return result;
				}
			}
			result += firstChar;
		}
		return result;
	}

	public String longestCommonPrefixSort(String[] strs) {
		StringBuilder result = new StringBuilder();

        if (strs!= null && strs.length > 0) {

	        Arrays.sort(strs);

	        char[] a = strs[0].toCharArray();
	        char[] b = strs[strs.length - 1].toCharArray();

	        for (int i = 0; i < a.length; i++) {
		        if (b.length > i && b[i] == a[i]) {
			        result.append(b[i]);
		        } else {
			        return result.toString();
		        }
	        }
        }
        return result.toString();
    }

	public static void main (String [] args) {
		String [] strs = {
			"abcad",
			"abaca",
			"abdcedd",
			""
		};
		long time = System.currentTimeMillis();
		System.out.println(new LongestCommonPrefix().longestCommonPrefix(strs));
		System.out.println(System.currentTimeMillis() - time);
	}
}
