package com.security.datastructure.sort;

import java.util.Arrays;

/**
 * 内部排序法-->冒泡排序
 * 基本思想是：
 * 通过对待排序序列从前向后（从下标较小的元素开始）,依次比较相邻元素的值，若发现逆序则交换，使值较大的元素逐渐从前移向后部，就象水底下的气泡一样逐渐向上冒。
 *
 * 时间复杂度 O(n^2)
 * @author fuhongxing
 */
public class BubbleSort {

	public static void main(String[] args) {
		int arr[] = {3, 9, -1, 10, 20};
//		
//		System.out.println("排序前");
//		System.out.println(Arrays.toString(arr));

		//为了容量理解，我们把冒泡排序的演变过程，给大家展示

		//测试一下冒泡排序的速度O(n^2), 给80000个数据测试
		//创建要给80000个的随机的数组
//		int[] arr = new int[80000];
//		for(int i =0; i < 80000;i++) {
//			arr[i] = (int)(Math.random() * 8000000); //生成一个[0, 8000000) 数
//		}

		long start = System.currentTimeMillis();
		System.out.println("排序前的时间是=" + start);
		//测试冒泡排序
		bubbleSort(arr);
		System.out.println("排序后的时间是=" + (System.currentTimeMillis() - start) + "毫秒");
		System.out.println("冒泡排序结果:" + Arrays.toString(arr));


		/*

		// 第二趟排序，就是将第二大的数排在倒数第二位

		for (int j = 0; j < arr.length - 1 - 1 ; j++) {
			// 如果前面的数比后面的数大，则交换
			if (arr[j] > arr[j + 1]) {
				temp = arr[j];
				arr[j] = arr[j + 1];
				arr[j + 1] = temp;
			}
		}

		System.out.println("第二趟排序后的数组");
		System.out.println(Arrays.toString(arr));


		// 第三趟排序，就是将第三大的数排在倒数第三位

		for (int j = 0; j < arr.length - 1 - 2; j++) {
			// 如果前面的数比后面的数大，则交换
			if (arr[j] > arr[j + 1]) {
				temp = arr[j];
				arr[j] = arr[j + 1];
				arr[j + 1] = temp;
			}
		}

		System.out.println("第三趟排序后的数组");
		System.out.println(Arrays.toString(arr));

		// 第四趟排序，就是将第4大的数排在倒数第4位

		for (int j = 0; j < arr.length - 1 - 3; j++) {
			// 如果前面的数比后面的数大，则交换
			if (arr[j] > arr[j + 1]) {
				temp = arr[j];
				arr[j] = arr[j + 1];
				arr[j + 1] = temp;
			}
		}

		System.out.println("第四趟排序后的数组");
		System.out.println(Arrays.toString(arr)); */

	}

	/**
	 * 冒泡排序算法
	 * 时间复杂度 O(n^2)
	 * @param arr
	 */
	public static void bubbleSort(int[] arr) {
		// 临时变量
		int temp = 0;
		// 标识变量，表示是否进行过交换
		boolean flag = false;
		for (int i = 0; i < arr.length - 1; i++) {
			for (int j = 0; j < arr.length - 1 - i; j++) {
				// 如果前面的数比后面的数大，则交换
				if (arr[j] > arr[j + 1]) {
					flag = true;
					temp = arr[j];
					arr[j] = arr[j + 1];
					arr[j + 1] = temp;
				}
			}
			//System.out.println("第" + (i + 1) + "趟排序后的数组");
			//System.out.println(Arrays.toString(arr));
			// 在一趟排序中，一次交换都没有发生过
			if (!flag) {
				break;
			} else {
				// 重置flag!!!, 进行下次判断
				flag = false;
			}
		}

	}
}

