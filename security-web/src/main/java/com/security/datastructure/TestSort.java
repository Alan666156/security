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
}
