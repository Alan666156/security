package com.security.algorithm;

import java.util.Random;

/**
 * 场景：在10亿个随机整数中找出前1000个最大的数
 * 可以把所有10亿个数据分组存放，比如分别放在1000个文件中。这样处理就可以分别在每个文件的10^6个数据中找出最大的10000个数，合并到一起在再找出最终的结果
 * 参照：https://blog.csdn.net/zyq522376829/article/details/47686867
 * @Author: fuhongxing
 * @Date: 2021/3/8
 **/
public class Top1000 {
    //最容易想到的方法是将数据全部排序，然后在排序后的集合中进行查找，最快的排序算法的时间复杂度一般为O（nlogn），如快速排序。但是在32位的机器上，每个float类型占4个字节，1亿个浮点数就要占用400MB的存储空间，对于一些可用内存小于400M的计算机而言，很显然是不能一次将全部数据读入内存进行排序的。其实即使内存能够满足要求（我机器内存都是8GB），该方法也并不高效，因为题目的目的是寻找出最大的10000个数即可，而排序却是将所有的元素都排序了，做了很多的无用功。
    //第二种方法为局部淘汰法，该方法与排序方法类似，用一个容器保存前10000个数，然后将剩余的所有数字——与容器内的最小数字相比，如果所有后续的元素都比容器内的10000个数还小，那么容器内这个10000个数就是最大10000个数。如果某一后续元素比容器内最小数字大，则删掉容器内最小元素，并将该元素插入容器，最后遍历完这1亿个数，得到的结果容器中保存的数即为最终结果了。此时的时间复杂度为O（n+m^2），其中m为容器的大小，即10000。
    //第三种方法是分治法，将10亿个数据分成100份，每份100万个数据，找到每份数据中最大的10000个，最后在剩下的100*10000个数据里面找出最大的10000个。如果100万数据选择足够理想，那么可以过滤掉1亿数据里面99%的数据。100万个数据里面查找最大的10000个数据的方法如下：用快速排序的方法，将数据分为2堆，如果大的那堆个数N大于10000个，继续对大堆快速排序一次分成2堆，如果大的那堆个数N大于10000个，继续对大堆快速排序一次分成2堆，如果大堆个数N小于10000个，就在小的那堆里面快速排序一次，找第10000-n大的数字；递归以上过程，就可以找到第1w大的数。参考上面的找出第1w大数字，就可以类似的方法找到前10000大数字了。此种方法需要每次的内存空间为10^6*4=4MB，一共需要101次这样的比较。
    //第四种方法是Hash法。如果这10亿个数里面有很多重复的数，先通过Hash法，把这1亿个数字去重复，这样如果重复率很高的话，会减少很大的内存用量，从而缩小运算空间，然后通过分治法或最小堆法查找最大的10000个数。
    //第五种方法采用最小堆。首先读入前10000个数来创建大小为10000的最小堆，建堆的时间复杂度为O（mlogm）（m为数组的大小即为10000），然后遍历后续的数字，并于堆顶（最小）数字进行比较。如果比最小的数小，则继续读取后续数字；如果比堆顶数字大，则替换堆顶元素并重新调整堆为最小堆。整个过程直至1亿个数全部遍历完为止。然后按照中序遍历的方式输出当前堆中的所有10000个数字。该算法的时间复杂度为O（nmlogm），空间复杂度是10000（常数）。

    public static int N = 1000;
    public static int LEN = 100000000;
    /**
     * 数据集
     */
    public static int arrs[] = new int[LEN];
    /**
     * 分拆后的堆数据
     */
    public static int result[] = new int[N];
    public static int len = result.length;
    public static int heapSize = len;

    public static void main(String[] args) {
        Random random = new Random();
        for (int i = 0; i < LEN; i++) {
            arrs[i] = random.nextInt(Integer.MAX_VALUE);
        }

        //构建初始堆
        for (int i = 0; i < N; i++) {
            result[i] = arrs[i];
        }

        //构建小顶堆
        long start = System.currentTimeMillis();
        buildMinHeap();

        for (int i = 0; i < N; i++) {
            if (arrs[i] > result[0]) {
                result[0] = arrs[i];
                minHeap(0);
            }
        }

        System.out.println(LEN + "个数，求top" + N + ",耗时" + (System.currentTimeMillis() - start) + "毫秒");
        print();
    }

    private static void print() {
        for (int a : result) {
            System.out.print(a + ",");
        }
        System.out.println();
    }


    /**
     * 自底向上构建小堆
     */
    private static void buildMinHeap() {
        int size = len / 2 - 1;
        for (int i = size; i >= 0; i--) {
            minHeap(i);
        }
    }

    /**
     * i节点为根及子树是一个小堆
     * @param i
     */
    private static void minHeap(int i) {
        int l = left(i);
        int r = right(i);
        int index = i;
        if (l < heapSize && result[i] < result[index]) {
            index = l;
        }
        if (r < heapSize && result[r] < result[index]) {
            index = r;
        }
        if (index != i) {
            int t = result[index];
            result[index] = result[i];
            //递归向下构建堆
            minHeap(index);
        }
    }

    private static int left(int i) {
        return 2 * i;
    }

    private static int right(int i) {
        return 2 * i + 1;
    }
}
