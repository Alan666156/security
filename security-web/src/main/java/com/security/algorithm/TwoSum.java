package com.security.algorithm;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 两数之和（leetcode第一题）
 * 给定一个整数数组 nums 和一个整数目标值 target，请你在该数组中找出 和为目标值 的那 两个 整数，并返回它们的数组下标。
 */
@Slf4j
public class TwoSum {

	/**
	 * 暴力解法
	 * 最容易想到的方法是枚举数组中的每一个数 x，寻找数组中是否存在 target - x。
	 * 当我们使用遍历整个数组的方式寻找 target - x 时，需要注意到每一个位于 x 之前的元素都已经和 x 匹配过，因此不需要再进行匹配。而每一个元素不能被使用两次，所以我们只需要在 x 后面的元素中寻找 target - x。
	 * 复杂度分析:
	 * 时间复杂度：O(N^2)O(N2)，其中 NN 是数组中的元素数量。最坏情况下数组中任意两个数都要被匹配一次。
	 * 空间复杂度：O(1)O(1)。
	 */
	public static int[] twoSum(int[] nums, int target) {
		int n = nums.length;
		for (int i = 0; i < n; ++i) {
			for (int j = i + 1; j < n; ++j) {
				if (nums[i] + nums[j] == target) {
					return new int[]{i, j};
				}
			}
		}
		return new int[0];
	}

	/**
	 *  哈希表实现
	 *  思路及算法：
	 * 注意到方法一的时间复杂度较高的原因是寻找 target - x 的时间复杂度过高。因此，我们需要一种更优秀的方法，能够快速寻找数组中是否存在目标元素。如果存在，我们需要找出它的索引。
	 * 使用哈希表，可以将寻找 target - x 的时间复杂度降低到从 O(N)O(N) 降低到 O(1)O(1)。
	 * 这样我们创建一个哈希表，对于每一个 x，我们首先查询哈希表中是否存在 target - x，然后将 x 插入到哈希表中，即可保证不会让 x 和自己匹配。
	 * @param nums
	 * @param target
	 * @return
	 */
	public static int[] twoSumByHash(int[] nums, int target) {
		Map<Integer, Integer> map = new HashMap<Integer, Integer>(10);
		for (int i = 0; i < nums.length; ++i) {
			if (map.containsKey(target - nums[i])) {
				return new int[]{map.get(target - nums[i]), i};
			}
			map.put(nums[i], i);
		}
		return new int[0];
	}

	public static void main(String[] args) {
		int[] nums = new int[]{2, 7, 11, 15};
		int target = 22;
		int[] index = twoSum(nums, target);
		for (int i : index) {
			System.out.println("index = " + i + ", arg= " + nums[i]);
		}
	}

}
