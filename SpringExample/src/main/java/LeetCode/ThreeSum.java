package LeetCode;

import java.util.*;

/**
 * Given an array S of n integers, are there elements a, b, c in S such that a + b + c = 0?
 * Find all unique triplets in the array which gives the sum of zero.
 * Note: The solution set must not contain duplicate triplets.
 * <p>
 * For example, given array S = [-1, 0, 1, 2, -1, -4],
 * <p>
 * A solution set is:
 * [
 * [-1, 0, 1],
 * [-1, -1, 2]
 * ]
 */
public class ThreeSum {
	public List<List<Integer>> threeSum(int[] nums) {

		List<List<Integer>> result = new ArrayList<>();
		if (nums == null || nums.length < 3) {
			return result;
		}

		Arrays.sort(nums);

		for (int i=0; nums[i] <= 0 && i<nums.length - 2; i++) {
			if (i > 0 && nums[i] == nums[i - 1]) {              // skip same result
				continue;
			}
			int j = i + 1, k = nums.length - 1;
			int target = -nums[i];
			while (j<k) {
				if (nums[j] + nums[k] == target) {
					result.add(Arrays.asList(nums[i], nums[j], nums[k]));
					j++;
					k--;
					while (j < k && nums[j] == nums[j - 1]) j++;  // skip same result
					while (j < k && nums[k] == nums[k + 1]) k--;  // skip same result
				} else if (nums[j] + nums[k] > target) {
					k--;
				} else {
					j++;
				}
			}
		}

		return  result;
	}

	/**
	 * Given an array S of n integers, find three integers in S such that the sum is closest to a given number, target. Return the sum of the three integers. You may assume that each input would have exactly one solution.

	 * For example, given array S = {-1 2 1 -4}, and target = 1.

	 *The sum that is closest to the target is 2. (-1 + 2 + 1 = 2).
	 */

	public int threeSumClosest(int[] nums, int target) {
		int closest = Integer.MAX_VALUE, sum=0;
		if (nums == null || nums.length < 3) {
			return closest;
		}

		Arrays.sort(nums);
		int len = nums.length, tmpP;
		for (int i=0; i<len-2; i++) {
			for (int j=i+1; j<len-1; j++) {
				for (int k=(j+len)/2, start=j, end=len; k>start && k<end;) {
					int sub = nums[i] + nums[j] + nums[k] - target;
					if (Math.abs(sub) < closest) {
						closest = Math.abs(sub);
						sum = nums[i] + nums[j] + nums[k];
					}
					tmpP = k;
					if (sub < 0) {
						k = (end + k) / 2;
						start = tmpP;
					} else if (sub > 0){
						k = (start + k) / 2;
						end = tmpP;
					} else {
						break;
					}
				}
				while (j<len-1 && nums[j] == nums[j+1]) j++;
			}
			while (i<len-2 && nums[i] == nums[i+1]) i++;
		}
		return sum;
	}

	public static void main(String[] args) {
		int[] nums = {1,6,6,6,6,9,14,16,70};
		System.out.println(new ThreeSum().threeSumClosest(nums, 81));
	}
}
