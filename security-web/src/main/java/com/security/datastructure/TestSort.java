package com.security.datastructure;

import java.util.Arrays;

/**
 * 排序算法
 * 排序分为内部排序和外部排序，内部排序是待排序的元素全部放在内存，并在内存中调整它们的顺序。外部排序是部分元素放到内存中，在内外存间调整元素的顺序。
 * 我们通常说的八大排序直接插入排序、希尔排序、简单选择排序、冒泡排序、快速排序、堆排序、归并排序、基数排序都是内部排序，下
 * 面来具体介绍这八种排序的如何用Java实现，以及它们所需的时间复杂度和空间复杂度。
 * 
 * @author Alan.Fu
 *
 */
public class TestSort {
	
	
	public static void main(String[] args) {
		int [] array = {10,2,3,7,8,6,4,1,9,5};
		System.out.println("冒泡排序前："+ Arrays.toString(array));
		bubbleSort(array);
		System.out.println("冒泡排序后："+ Arrays.toString(array));
		int [] quickArray = {10,2,3,1,8,6,4,7,9,5};
		System.out.println("快速排序前："+ Arrays.toString(quickArray));
		quickSort(quickArray, 0, quickArray.length - 1);
		System.out.println("快速排序后："+ Arrays.toString(quickArray));

		int [] insertArray = {10,2,3,1,8,6,4,7,9,5};
		System.out.println("快速排序前："+ Arrays.toString(insertArray));
		insertSort(insertArray);
		System.out.println("快速排序后："+ Arrays.toString(insertArray));
		
		int [] megeArray = {10,1,3,2,8,6,4,7,9,5};
		System.out.println("归并排序前："+ Arrays.toString(megeArray));
		mergeSort(megeArray, 0, megeArray.length - 1);
		System.out.println("归并排序后："+ Arrays.toString(megeArray));
		
		int [] radixArray = {23,6,189,45,9,287,56,1,798,34,65,652,5};
		System.out.println("字基数排序前："+ Arrays.toString(radixArray));
		radixSort(radixArray, Integer.MAX_VALUE);
		System.out.println("字基数排序后："+ Arrays.toString(radixArray));
	}
	
	/**
	 * 冒泡排序
	 * 基本思想： 从待排序元素的倒数第一位开始向前遍历，如果当前元素比前面元素小，则交换位置。这样一次遍历下来，最小的元素冒泡到第一个位置了，然后，从倒数第二位、第三位...
	 * 开始向前遍历，重复上面的过程，直到元素有序。
	 * 时间复杂度:冒泡排序是稳定的排序，时间复杂度是O(n^2)。
	 * @param element
	 */
	public static void bubbleSort(int[] element){
        int tmp;
        int len = element.length;
        for(int i = 0; i < len; i++ ) {
            for (int j = len -1; j - 1 >= i; j--) {
                if (element[j] < element[j - 1]) {
                    tmp = element[j];
                    element[j] = element[j - 1];
                    element[j - 1] = tmp;
                }
            }
        }
    }
	
	/**
	 * 直接插入排序
	 * 基本思想：将一个待排序的元素插入到已经排好序的序列中，如果待排序的元素与有序序列的中的某个元素相等，则把待排序元素插到该元素后面。
	 * 时间复杂度：直接插入排序是稳定的排序，其时间复杂度是O(n^2)。
	 * @param element
	 */
	public static void insertSort(int[] element) {
        for (int i = 1; i < element.length; i++) {
            int tmp = element[i];
            int j = i -1;
            for (; j >= 0; j--) {
                if (tmp < element[j]) {
                    element[j + 1] = element[j];
                } else {
                    break;
                }
            }
            //正确的插入位置是：j+1
            element[j+1] = tmp;
        }
    }
	
	/**
	 * 
	 * 快速排序
	 * 基本思想:选择一个基准元素（通常选择第一个元素或者最后一个元素），通过一次排序将待排序列分为两部分，一部分都比基准元素小，另一部分都比基准元素大，
	 * 然后再按此方法对这两组数据分别进行快速排序，直到待排序列有序。
	 * 时间复杂度:快速排序是不稳定排序，时间复杂度是O(nlogn)。
	 * @param element
	 * @param low：查找范围的下限位置（开始位置）
	 * @param high：查找范围的上限位置（结束位置）
	 */
	public static void quickSort(int[] element, int low, int high) {
		//如果开始位置小于结束位置说明存在
        if (low < high) {
        	//获取基准数字
            int mid = partition(element, low, high);
            //比基准元素小的数字
            quickSort(element, low, mid - 1);
            //比基准元素大的数字
            quickSort(element, mid + 1, high);
        }
    }
    public static int partition(int[] element, int low, int high){
    	//获取第一个数字作为基准数
       int baseElement = element[low];
       //循环找比标准数大的数和比标准数小的数
        while (low < high) {
            while (low < high && baseElement <= element[high]){ 
            	high--;
            };
            //使用右边的数字替换左边的数字
            element[low] = element[high];
            while (low < high && baseElement >= element[low]){
            	low++;
            }
            //使用左边的数字替换右边的数字
            element[high] = element[low];
        }
        element[low] = baseElement;
        return  low;
    }
    /**
     * 快速排序方式2
     * @param element
     * @param start
     * @param end
     */
    public static void quickSort2(int[] element, int start, int end) {
    	if(start < end){
    		//数组中的第0个元素作为标准数
        	int base = element[start];
        	//记录需要排序的下标
        	int low = start;
        	int high = end;
        	//循环找比标准数大的数和比标准数小的数
        	while(low < high){
        		//右边的数字比标准数大
        		while(low < high && base <= element[high]){
        			high--;
        		}
        		//使用右边的数字替换左边的数字
        		element[low] = element[high];
        		//如果左边的数字比标准数小
        		while(low < high && base >= element[low]){
        			low++;
        		}
        		//使用左边的数字替换右边的数字
        		element[high] = element[low];
        	}
        	//把标准数赋给小的数字所在的位置的元素
        	element[low] = base;
        	//递归调用
        	//比基准元素小的数字
            quickSort2(element, start, low);
            //比基准元素大的数字
            quickSort2(element, low + 1, end);
    	}
    }
    
    /**
     * 归并排序
     * 基本思想：是把待排序序列分为若干个子序列，每个子序列是有序的，然后再把有序子序列合并为整体有序序列。
     * 时间复杂度:归并排序是稳定的排序，其时间复杂度为O(nlogn)。
     * @param element
     * @param left 开始位置
     * @param right 结束位置
     */
    public static void mergeSort(int[] element, int left, int right) {
        if (left < right) {
        	//获取中间位置
            int mid = (left + right) / 2;
            //左边进行递归排序
            mergeSort(element, left, mid);
            //右边进行递归排序
            mergeSort(element, mid + 1, right);
            //左右两部分进行合并处理
            merge(element, left, mid, right);
        }
    }
    public static void merge(int[] element, int left, int middle, int right) {
    	//用于存储归并后的临时数组
        int[] tmpElement = new int[element.length];
        //记录第一个数组中需要遍历的下标
        int index = left;
        //记录第二个数组中需要遍历的下标
        int mid = middle + 1;
        //用于记录在临时数组中存放的下标
        int tmpIndex = left;
        //遍历两个数组取出小的数字，放入临时数组中
        while (left <= middle && mid <= right) {
            if (element[left] < element[mid]) {
                tmpElement[index++] = element[left++];
            } else {
                tmpElement[index++] = element[mid++];
            }
        }
        //处理多余的数据
        while (left <= middle) {
            tmpElement[index++] = element[left++];
        }
        while (mid <= right) {
            tmpElement[index++] = element[mid++];
        }
        //把临时数组中的数据重新存入原数组中
        while (tmpIndex <= right){
            element[tmpIndex] = tmpElement[tmpIndex ++];
        }
    }
    
    /**
     * 字基数排序
     * 基本思想： 将所有待排序列(正整数)统一为同样的数位长度，数位较短的数前面补零。然后 ，从最低位开始，依次进行一次排序。这样，从最低位一直到最高位排序完成以后， 数列就变成一个有序序列。
     * 时间复杂度:基数排序是稳定的排序，其时间复杂度为O(d(n+r))，d为位数，r为基数范围。
     * @param element
     * @param max 表示最大的数有多少位
     */
	public static void radixSort(int[] element, int max) {
		// 记录取的元素需要放的位置
		int index = 0;
		int n = 1;
		// 控制键值排序依据在哪一位
		int m = 1;
		// 用于临时存储数据的数组，数组的第一维表示可能的余数0-9
		int[][] temp = new int[10][element.length];
		// 用于记录在temp中相应的数组中的存放的数字的数量
		int[] count = new int[10];
		while (m <= max) {
			// 根据最大长度的数字决定比较的次数
			// 把每一个数字计算余数
			for (int i = 0; i < element.length; i++) {
				// 计算余数
				int ys = ((element[i] / n) % 10);
				// 把当前遍历的数据放入临时数组中
				temp[ys][count[ys]] = element[i];
				// 记录数量
				count[ys]++;
			}

			// 去除数字
			for (int i = 0; i < 10; i++) {
				// 记录数量的数组中当前余数记录的数量不为0
				if (count[i] != 0)
					// 循环去除元素
					for (int j = 0; j < count[i]; j++) {
						// 取出元素放入原数组中
						element[index] = temp[i][j];
						index++;
					}
				count[i] = 0;
			}
			n *= 10;
			index = 0;
			m++;
		}
	}
	
	/**
	 * 
	 * 堆排序
	 * 基本思想:
	 * 堆的概念：n个元素的序列{k1，k2，…,kn}当且仅当满足下列关系之一时，称之为堆。
	 * 情形1：ki <= k2i 且ki <= k2i+1 （最小堆）
	 * 情形2：ki >= k2i 且ki >= k2i+1 （最大堆）
	 * 其中i=1,2,…,n/2向下取整;
	 * 
	 * 堆排序： 把待排序的序列看作是一棵顺序存储的二叉树，调整它们的存储顺序，使之成为一个最大堆，这时堆的根节点数最大。然后，将根节点与堆的最后一个节点交换，并对前面n-1个数重新调整使之成为堆，依此类推，最后得到有n个节点的有序序列。
	 * 从算法描述来看，堆排序需要两个过程，一是建立堆，二是堆结果。
	 * 说明：若想得到升序序列，则建立最大堆，若想得到降序序列，则建立最小堆。
	 * 时间复杂度： 堆排序是不稳定的排序，其时间复杂度是O(nlogn)。
	 * @param element
	 */
	public static void heapSort(int[] element) {
		// step1:建堆
		int length = element.length;
		for (int i = length / 2 - 1; i >= 0; i--) {
			adjustHeap(element, i, length - 1);
		}
		// step2:交换位置,调整堆结构
		int tmp;
		for (int j = length - 1; j >= 0; j--) {
			tmp = element[j];
			element[j] = element[0];
			element[0] = tmp;
			adjustHeap(element, 0, j - 1);
		}
	}

	public static void adjustHeap(int[] element, int start, int end) {
		int tmp = element[start];
		for (int i = 2 * start + 1; i <= end; i = 2 * i + 1) {
			// 定位父节点的左右孩子值较大的节点
			if (i < end && element[i] < element[i + 1]) {
				i++;
			}
			// 父节点比左右孩子值都大,则跳出循环
			if (tmp > element[i]) {
				break;
			}
			// 进行下一轮的筛选
			element[start] = element[i];
			start = i;
		}
		element[start] = tmp;
	}

	/**
	 *简单选择排序
	 * 基本思想：
	 * 在n个待排序的元素中找取最小的元素与第一个元素交换位置，然后在n-1个元素中找取最小的元素与第二元素交换位置，直到n=1为止。
	 * 时间复杂度:
	 * 简单选择排序是不稳定的排序，其时间复杂度是O(n^2)。
	 * 不稳定说明：
	 * 假设待排元素序列是：6，4，6，7，2，9，第一次排序后，序列变成了2，4，6，7，6，9，我们可以发现，经过一次排序后，位置一的6调整到位置三的6的后面，所以简单选择排序是不稳定的排序。
	 * @param element
	 */
	public static void selectSort(int[] element){
		int minPos;
		int tmp;
		for(int i = 0 ; i < element.length; i++ ){
			minPos = i;
			//遍历当前所有
			for(int j = i+1; j < element.length; j++){
				//如果后面比较的数比记录的最小的数小
				if(element[j] < element[minPos]){
					//记录最小的那个数的下标
					minPos = j;
				}
			}
			tmp = element[minPos];
			element[minPos] = element[i];
			element[i] = tmp;
		}
	}

	/**
	 *希尔排序
	 * 基本思想：
	 * 希尔排序实质上是一种分组插入排序，其先将整个待排元素序列分割成若干个子序列（由距离为d的元素组成）分别进行直接插入排序，然后依次减少距离d再进行排序，当距离为1时，再对全体元素进行一次直接插入排序。
	 * 时间复杂度：
	 * 希尔排序中相同的元素可能在各自组的插入排序中移动，最后其稳定性会被打乱，所以希尔排序是不稳定的，其时间复杂度是O(nlogn)。
	 * @param element
	 */
	public static void hellSort(int[] element){
		int d = element.length;
		while (true){
			d = d/2;
			for(int i = 0; i < d; i++){
				for(int j = i+d; j < element.length; j+=d){
					int tmp = element[j];
					int k = j-d;
					for(;k >= 0; k-=d){
						if(tmp < element[k]){
							element[k + d ] = element[k];
						}
						else{
							break;
						}
					}
					element[k + d] = tmp;
				}
			}
			if(d == 1) {
				break;
			}

		}
	}
	public static void hellSort2(int[] element){
		for(int d = element.length/2; d < 0; d/=2){
			for(int i = 0; i < element.length; i++){
				for(int j = i+d; j < element.length; j+=d){
					for(int k = i-d;k >= 0; k-=d){
						//如果当前元素大于加上步长后的那个元素
						if(element[k] > element[k + d]){
							int tmp = element[k];
							element[k] = element[k + d];
							element[k + d] = tmp;
						}

					}
				}
			}
		}
	}
}
