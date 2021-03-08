package com.security.algorithm;

import java.util.PriorityQueue;
import java.util.Scanner;

/**
 * 中位数查找
 * 题目描述：如何得到一个数据流中的中位数？如果从数据流中读出奇数个数值，那么中位数就是所有数值排序之后位于中间的数值。如果从数据流中读出偶数个数值，那么中位数就是所有数值排序之后中间两个数的平均值。我们使用Insert()方法读取数据流，使用GetMedian()方法获取当前读取数据的中位数。
 * 如果数据已经在容器中有序，并且如果容器中数据的个数为偶数，那么中位数可以由P1和P2指向的数求平均得到，如果为奇数，则中位数为P1指向的数
 * 我们可以发先容器被封分割成了两个部分。位于容器左边的数据比右边的数据小。P1指向的是左边的最大数，P2指向的是右边的最小数。基于以上思路：用一个最大堆实现左边的数据容器，用一个最小堆实现右边的数据容器。
 */
public class MiddleNumber {
    public static void main(String[] args) {
        int[] nums = new int[]{1, 3, 6, 7, 9, 10};
        MiddleNumber middleNumber = new MiddleNumber();
        Scanner sc = new Scanner(System.in);
        int c = 0;
        while (true) {
            System.out.println("请输入一个数字：");
            c = sc.nextInt();
            middleNumber.insert(c);
            System.out.println("当前的中位数为：" + middleNumber.getMedian());
        }
    }

    /**
     * PriorityQueue是基于堆实现的数据结构，其逻辑结构是一颗完全二叉树，存储结构其实是一个数组。PriorityQueue，也叫优先级队列，它是不同于先进先出队列的另一种队列。每次从队列中取出的是具有最高优先权的元素。
     * 优先队列默认为小顶堆(小顶堆只是保证了根节点不大于左右两个节点，但是左右两个节点谁比谁大并不能保证，也就是说队列的元素，在物理结构上是数组，是无序的)
     */
    PriorityQueue<Integer> minHeap = new PriorityQueue<Integer>();

    /**
     * 通过比较器，实现大顶堆(大顶堆可以通过比较器的方式实现，并且遍历出来的顺序也同样有序)
     */
    PriorityQueue<Integer> maxHeap = new PriorityQueue<Integer>(11, (i, j) -> j - i);

    /**
     * 将数据流中获取到的数字放入堆中
     *
     * @param num
     */
    public void insert(Integer num) {
        //如果已经读取到的数为偶数个，则下一个读取到的数将放入小顶堆中

        //已经读取到的数为偶数个，下一个读进来变为奇数个
        if (((minHeap.size() + maxHeap.size()) & 1) == 0) {
            //判断如果大顶堆不为空，并且插入的数字比大顶堆最大的数字小
            if (!maxHeap.isEmpty() && maxHeap.peek() > num) {
                //首先将数据插入到大顶堆中
                maxHeap.offer(num);
                num = maxHeap.poll();
            }
            //如果maxHeap.peek()<num,将新读取到的数字插入小顶堆中
            minHeap.offer(num);
        } else {
            //如果已经读取到的数为奇数个，即小顶堆数量比大顶堆多一，将新读取到的数插入到大顶堆中
            if (!minHeap.isEmpty() && minHeap.peek() < num) {
                minHeap.offer(num);
                num = minHeap.poll();
            }
            //如果MinHeap.peek() > num,则直接插入大顶堆中
            maxHeap.offer(num);
        }
    }

    /**
     * 获取中位数
     *
     * @return
     */
    public Double getMedian() {
        if ((minHeap.size() + maxHeap.size()) == 0) {
            throw new RuntimeException();
        }
        double median;
        //如果是偶数：中位数mid= (leftMax+right)/2
        //如果是奇数：中位数mid = leftMax 因为先插入到左边，再插入到右边，为奇数时，中位数就是mid
        if (((minHeap.size() + maxHeap.size()) & 1) == 0) {
            median = (minHeap.peek() + maxHeap.peek()) / 2.0;
        } else {
            median = minHeap.peek();
        }
        return median;
    }
}