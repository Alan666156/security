package com.security.datastructure.search;

import java.util.ArrayList;
import java.util.List;

/**
 * 二分查找
 * 注意：使用二分查找的前提是 该数组是有序的.
 */
public class BinarySearch {

	public static void main(String[] args) {
		//int arr[] = { 1, 8, 10, 89,1000,1000, 1234 };

		int arr[] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20};
//		int resIndex = binarySearch(arr, 0, arr.length - 1, 1000);
//		System.out.println("index=" + resIndex);

		List<Integer> indexList = binarySearch2(arr, 0, arr.length - 1, 1);
		System.out.println("indexList=" + indexList);
	}

	/**
	 * 二分查找算法
	 * @param arr 数组
	 * @param left 左边的索引
	 * @param right 右边的索引
	 * @param findValue 要查找的值
	 * @return 如果找到就返回下标，如果没有找到，就返回 -1
	 */
	public static int binarySearch(int[] arr, int left, int right, int findValue) {
		// 当 left > right 时，说明递归整个数组，但是没有找到
		if (left > right) {
			return -1;
		}
		//中间数下标
		int mid = (left + right) / 2;
		//中间数
		int midValue = arr[mid];
		// 向右递归
		if (findValue > midValue) {
			return binarySearch(arr, mid + 1, right, findValue);
		} else if (findValue < midValue) { // 向左递归
			return binarySearch(arr, left, mid - 1, findValue);
		} else {
			return mid;
		}

	}

	/**
	 * 课后思考题： {1,8, 10, 89, 1000, 1000，1234} 当一个有序数组中，
	 * 有多个相同的数值时，如何将所有的数值都查找到，比如这里的 1000
	 *
	 * 思路分析:
	 * 1. 在找到mid 索引值，不要马上返回
	 * 2. 向mid 索引值的左边扫描，将所有满足 1000， 的元素的下标，加入到集合ArrayList
	 * 3. 向mid 索引值的右边扫描，将所有满足 1000， 的元素的下标，加入到集合ArrayList
	 * 4. 将Arraylist返回
	 *
	 * @param arr 数组
	 * @param left 左边的索引
	 * @param right 右边的索引
	 * @param findValue 要查找的值
	 * @return 如果找到就返回下标，如果没有找到，就返回 空集合
	 */
	public static List<Integer> binarySearch2(int[] arr, int left, int right, int findValue) {
		// 当 left > right 时，说明递归整个数组，但是没有找到
		if (left > right) {
			return new ArrayList<Integer>();
		}
		//中间数下标
		int mid = (left + right) / 2;
		//中间数
		int midValue = arr[mid];

		// 向 右递归
		if (findValue > midValue) {
			return binarySearch2(arr, mid + 1, right, findValue);
		} else if (findValue < midValue) { // 向左递归
			return binarySearch2(arr, left, mid - 1, findValue);
		} else {
			List<Integer> indexlist = new ArrayList<Integer>();
			//向mid 索引值的左边扫描，将所有满足 1000， 的元素的下标，加入到集合ArrayList
			int temp = mid - 1;
			while(true) {
				//退出,temp < 0说明已经扫描到最左边没有值了；arr[temp] != findValue说明查找到值左边一位不相等
				if (temp < 0 || arr[temp] != findValue) {
					break;
				}
				//否则，就temp 放入到 resIndexlist
				indexlist.add(temp);
				//temp左移
				temp -= 1;
			}
			//查找到的值放进集合
			indexlist.add(mid);


			//向mid 索引值的右边扫描，将所有满足 1000， 的元素的下标，加入到集合ArrayList
			temp = mid + 1;
			while(true) {
				//退出,temp > arr.length - 1说明已经扫描到最右边了
				if (temp > arr.length - 1 || arr[temp] != findValue) {
					break;
				}
				//否则，就temp 放入到 resIndexlist
				indexlist.add(temp);
				//temp右移
				temp += 1;
			}
			return indexlist;
		}

	}
}
