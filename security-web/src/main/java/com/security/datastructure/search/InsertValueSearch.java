package com.security.datastructure.search;

/**
 *
 * 插值查找原理介绍:
 * 插值查找算法类似于二分查找，不同的是插值查找每次从自适应mid处开始查找。插值查找算法要求数组是有序的
 * 将折半查找中的求mid 索引的公式 , low 表示左边索引left, high表示右边索引right.  key 就是前面我们讲的  findVal
 * int mid = low + (high - low) * (key - arr[low]) / (arr[high] - arr[low]);
 * 插值算法对应代码公式：int mid = left + (right – left) * (findVal – arr[left]) / (arr[right] – arr[left])举例说明插值查找算法 1-100 的数组
 * @author fuhongxing
 */
public class InsertValueSearch {

	public static void main(String[] args) {

//		int [] arr = new int[100];
//		for(int i = 0; i < 100; i++) {
//			arr[i] = i + 1;
//		}

		int arr[] = {1, 8, 10, 89, 1000, 1000, 1234};

		int index = insertValueSearch(arr, 0, arr.length - 1, 1234);
		//int index = binarySearch(arr, 0, arr.length, 1);
		System.out.println("index = " + index);

		//System.out.println(Arrays.toString(arr));
	}

	public static int binarySearch(int[] arr, int left, int right, int findVal) {
		System.out.println("二分查找被调用~");
		// 当 left > right 时，说明递归整个数组，但是没有找到
		if (left > right) {
			return -1;
		}
		int mid = (left + right) / 2;
		int midVal = arr[mid];

		if (findVal > midVal) { // 向 右递归
			return binarySearch(arr, mid + 1, right, findVal);
		} else if (findVal < midVal) { // 向左递归
			return binarySearch(arr, left, mid - 1, findVal);
		} else {

			return mid;
		}

	}

	/**
	 * 插值查找算法, 要求数组是有序的(根据公式1次就可以找到位置)
	 * @param arr 数组
	 * @param left 左边索引
	 * @param right 右边索引
	 * @param findValue 查找值
	 * @return 如果找到，就返回对应的下标，如果没有找到，返回-1
	 */
	public static int insertValueSearch(int[] arr, int left, int right, int findValue) {
		//注意：findVal < arr[0]  和  findVal > arr[arr.length - 1] 必须需要；否则我们得到的 mid 可能越界
		if (left > right || findValue < arr[0] || findValue > arr[arr.length - 1]) {
			return -1;
		}
		// 求出查找数的下标, 自适应
		int mid = left + (right - left) * (findValue - arr[left]) / (arr[right] - arr[left]);
		int midValue = arr[mid];
		// 说明应该向右边递归
		if (findValue > midValue) {
			return insertValueSearch(arr, mid + 1, right, findValue);
		} else if (findValue < midValue) { // 说明向左递归查找
			return insertValueSearch(arr, left, mid - 1, findValue);
		} else {
			return mid;
		}

	}
}

