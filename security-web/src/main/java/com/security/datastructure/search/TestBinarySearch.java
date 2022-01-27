package com.security.datastructure.search;

import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;

/**
 * 二分查找(二分查找也称折半查找（Binary Search），它是一种效率较高的查找方法。但是，折半查找要求线性表必须采用顺序存储结构，而且表中元素按关键字有序排列。)
 * 非递归实现
 * @author fuhongxing
 */
@Slf4j
public class TestBinarySearch {

	public static void main(String[] args) {
		int[] array = {1, 2, 3, 4, 5, 6, 7, 8, 9};
		int target = 8;
		int result = binarySearch(array, target);
		System.out.println("二分查找目标值的位置:" + result);
		int res = binarySearch(array, target, 0, array.length-1);
		System.out.println("二分查找目标值的位置:" + res);

		int resIndex = binarySearchByRecursion(array, 0, array.length - 1, target);
		System.out.println("二分查找目标值的位置(递归实现):" + res);

		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();
		int k = sc.nextInt();
		log.info("n:{}, k:{}", n, k);
		for (int i = 0; i < n; i++) {
			int a = sc.nextInt();
			if (i != k - 1) {
				System.out.print(a);
			}
			if (i != n - 1){
				System.out.print("-->");
			}


		}
	}
	
	/**
	 * 获取查找数的下标位置(二分查找非递归)
	 * @param array 
	 * @param target 目标值
	 * @return 目标值的位置
	 */
	public static int binarySearch(int [] array, int target){
		//中间位置,>>右位移相当于num除2，<<左位移相当于num乘2
		int mid = array.length >> 1; 
		//开始位置
		int begin = 0;
		//结束位置
		int end =  array.length -1;
		//目标位置
		int index = -1;
		while(true){
			//元素不存在的情况,如果开始位置大于或等于结束位置说明不存在
			if(begin >= end){
				return -1;
			}
			//判断中间的这个元素是不是就是要查找的元素
			if(array[mid] == target){
				index = mid;
				break;
			}else{
				//判断中间这个元素是不是比目标元素大
				if(array[mid] > target){
					//把结束位置调整到中间位置的前一个位置
					end = mid - 1;
				}else{//中间这个元素比目标元素小
					//把开始位置调整到中间位置的后一个位置
					begin = mid + 1;
				}
				mid = (begin + end ) >> 1;
			}
		}
		//查找失败返回-1
		return index;
	}

	/**
	 * 二分查找算法（递归实现）
	 * @param arr 数组
	 * @param left 左边的索引
	 * @param right 右边的索引
	 * @param targetValue 要查找的值
	 * @return 如果找到就返回下标，如果没有找到，就返回 -1
	 */
	public static int binarySearchByRecursion(int[] arr, int left, int right, int targetValue) {
		// 当 left > right 时，说明递归整个数组，但是没有找到
		if (left > right) {
			return -1;
		}
		//中间数下标
		int mid = (left + right) / 2;
		//中间数
		int midValue = arr[mid];
		// 向右递归
		if (targetValue > midValue) {
			return binarySearch(arr, mid + 1, right, targetValue);
		} else if (targetValue < midValue) { // 向左递归
			return binarySearch(arr, left, mid - 1, targetValue);
		} else {
			return mid;
		}

	}

	/**
	 * 二分查找算法
	 * @param array：数组首地址
	 * @param target：查找的目标元素
	 * @param low：查找范围的下限位置（开始位置）
	 * @param high：查找范围的上限位置（结束位置）
	 * @return: 若查找成功，则返回目标元素的位置，若查找失败，则返回-1
	 */
	public static int binarySearch(int [] array, int target, int low, int high){
		//每步迭代可能都要做两次比较判断，有三个分支
		while(low <= high){
			//以中心点为轴点
			int mid = (low + high) >> 1;
			//正常情况下第一次不可能会立马找到匹配的值
			if(array[mid] > target){
				//前半段继续查找
				high = mid -1;
			}else if(array[mid] < target){
				//后半段继续查找
				low = mid + 1;
			}else{
				//命中
				return mid;
			}
		}
		//查找失败返回-1
		return -1;
	}
}
