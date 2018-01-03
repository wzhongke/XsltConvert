package leetcode;

public class ContainerMostWater {

	public int maxArea(int[] height) {
		int left = 0, right = height.length -1;
		int max = 0;
		while (left < right) {
			max = Math.max(max, Math.min(height[left], height[right]) * (right - left));
			if (height[left] < height[right]) {
				int lh = height[left];
				left ++;
				while (height[left] < lh && left < right ) {
					left ++;
				}
			} else {
				int rh = height[right --];
				while (height[right] < rh && left < right ) {
					right--;
				}
			}
		}
		return max;
	}
}
