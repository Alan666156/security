package com.security.datastructure.sort;

/**
 * 归并排序（MERGE-SORT）是利用归并的思想实现的排序方法，是把待排序序列分为若干个子序列，每个子序列是有序的，然后再把有序子序列合并为整体有序序列。
 * 该算法采用经典的分治（divide-and-conquer）策略（分治法将问题分(divide)成一些小的问题然后递归求解，而治(conquer)的阶段则将分的阶段得到的各答案"修补"在一起，即分而治之)。
 * 时间复杂度：O(nlogn)，归并排序是稳定的排序
 */
public class MergetSort {

	public static void main(String[] args) {
		//int arr[] = { 8, 4, 5, 7, 1, 3, 6, 2 };

		//测试快排的执行速度
		// 创建要给80000个的随机的数组
		int[] arr = new int[8000000];
		for (int i = 0; i < 8000000; i++) {
			// 生成一个[0, 8000000) 数
			arr[i] = (int) (Math.random() * 8000000);
		}
		long start = System.currentTimeMillis();
		System.out.println("排序前的时间是=" + start);

		//归并排序需要一个额外空间
		int temp[] = new int[arr.length];
		mergeSort(arr, 0, arr.length - 1, temp);

		System.out.println("排序后的时间是=" + (System.currentTimeMillis() - start) + "毫秒");

		//System.out.println("归并排序后=" + Arrays.toString(arr));
	}


	/**
	 * 分+合方法
	 * @param arr
	 * @param left
	 * @param right
	 * @param temp
	 */
	public static void mergeSort(int[] arr, int left, int right, int[] temp) {
		if(left < right) {
			//中间索引
			int middle = (left + right) / 2;
			//向左递归进行分解
			mergeSort(arr, left, middle, temp);
			//向右递归进行分解
			mergeSort(arr, middle + 1, right, temp);
			//左右两部分进行合并处理
			merge(arr, left, middle, right, temp);

		}
	}

	/**
	 * 合并的方法
	 * @param arr 排序的原始数组
	 * @param left 左边有序序列的初始索引
	 * @param middle 中间索引
	 * @param right 右边索引
	 * @param temp 做中转的数组
	 */
	public static void merge(int[] arr, int left, int middle, int right, int[] temp) {
		// 初始化i, 左边有序序列的初始索引
		int i = left;
		//初始化j, 右边有序序列的初始索引
		int j = middle + 1;
		// 指向temp数组的当前索引
		int index = 0;

		//(一)先把左右两边(有序)的数据按照规则填充到temp数组
		//直到左右两边的有序序列，有一边处理完毕为止
		while (i <= middle && j <= right) {
			//如果左边的有序序列的当前元素，小于等于右边有序序列的当前元素
			//即将左边的当前元素，填充到 temp数组
			//然后 t++, i++
			if(arr[i] <= arr[j]) {
				temp[index] = arr[i];
				index += 1;
				i += 1;
			} else { //反之,将右边有序序列的当前元素，填充到temp数组
				temp[index] = arr[j];
				index += 1;
				j += 1;
			}
		}

		//(二)把有剩余数据的一边的数据依次全部填充到temp
		//左边的有序序列是否还有剩余的元素，就全部填充到temp
		while( i <= middle) {
			temp[index] = arr[i];
			index += 1;
			i += 1;
		}

		//右边的有序序列是否还有剩余的元素，就全部填充到temp
		while( j <= right) {
			temp[index] = arr[j];
			index += 1;
			j += 1;
		}


		//(三)将temp数组的元素拷贝到arr，ps:并不是每次都拷贝所有
		index = 0;
		int tempLeft = left;
		//第一次合并 tempLeft = 0 , right = 1 //  tempLeft = 2  right = 3 // tempLeft=0 right=3
		//最后一次 tempLeft = 0  right = 7
		while(tempLeft <= right) {
			arr[tempLeft] = temp[index];
			index += 1;
			tempLeft += 1;
		}

	}

}
