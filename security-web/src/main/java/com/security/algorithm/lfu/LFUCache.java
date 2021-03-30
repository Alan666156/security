package com.security.algorithm.lfu;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

/**
 * LFU,全称是:Least Frequently Used，最不经常使用策略,在一段时间内,数据被使用频次最少的,优先被淘汰。最少使用（LFU）是一种用于管理计算机内存的缓存算法。
 * 主要是记录和追踪内存块的使用次数,当缓存已满并且需要更多空间时，系统将以最低内存块使用频率清除内存.采用LFU算法的最简单方法是为每个加载到缓存的块分配一个计数器。
 * 每次引用该块时，计数器将增加一。当缓存达到容量并有一个新的内存块等待插入时，系统将搜索计数器最低的块并将其从缓存中删除
 * LRU和LFU的区别以及LFU的缺点:
 * LRU和LFU侧重点不同，LRU主要体现在对元素的使用时间上,而LFU主要体现在对元素的使用频次上。LFU的缺陷是：在短期的时间内，对某些缓存的访问频次很高，这些缓存会立刻晋升为热点数据，而保证不会淘汰，这样会驻留在系统内存里面。而实际上，这部分数据只是短暂的高频率访问，之后将会长期不访问,瞬时的高频访问将会造成这部分数据的引用频率加快，而一些新加入的缓存很容易被快速删除，因为它们的引用频率很低。
 * https://leetcode-cn.com/problems/lfu-cache/solution/lfuhuan-cun-by-leetcode-solution/
 * 复杂度分析:
 * 时间复杂度：get 时间复杂度 O(\log n)O(logn)，put 时间复杂度 O(\log n)O(logn)，操作的时间复杂度瓶颈在于平衡二叉树的插入删除均需要 O(\log n)O(logn) 的时间。
 * 空间复杂度：O(\textit{capacity})O(capacity)，其中 \textit{capacity}capacity 为 LFU 的缓存容量。哈希表和平衡二叉树不会存放超过缓存容量的键值对。
 * @author fuhx
 */
public class LFUCache {
    /** 缓存容量，时间戳*/
    int capacity, time;
    /**哈希表 map 以键 key 为索引存储缓存，建立一个平衡二叉树 S 来保持缓存根据 (cnt，time) 双关键字*/
    Map<Integer, Node> map;
    TreeSet<Node> S;

    public LFUCache(int capacity) {
        this.capacity = capacity;
        this.time = 0;
        map = new HashMap<Integer, Node>();
        S = new TreeSet<Node>();
    }
    
    public int get(int key) {
        if (capacity == 0) {
            return -1;
        }
        // 如果哈希表中没有键 key，返回 -1
        if (!map.containsKey(key)) {
            return -1;
        }
        // 从哈希表中得到旧的缓存
        Node cache = map.get(key);
        // 从平衡二叉树中删除旧的缓存
        S.remove(cache);
        // 将旧缓存更新
        cache.cnt += 1;
        cache.time = ++time;
        // 将新缓存重新放入哈希表和平衡二叉树中
        S.add(cache);
        map.put(key, cache);
        return cache.value;
    }
    
    public void put(int key, int value) {
        if (capacity == 0) {
            return;
        }
        if (!map.containsKey(key)) {
            // 如果到达缓存容量上限
            if (map.size() == capacity) {
                // 从哈希表和平衡二叉树中删除最近最少使用的缓存
                map.remove(S.first().key);
                S.remove(S.first());
            }
            // 创建新的缓存
            Node cache = new Node(1, ++time, key, value);
            // 将新缓存放入哈希表和平衡二叉树中
            map.put(key, cache);
            S.add(cache);
        } else {
            // 这里和 get() 函数类似
            Node cache = map.get(key);
            S.remove(cache);
            cache.cnt += 1;
            cache.time = ++time;
            cache.value = value;
            S.add(cache);
            map.put(key, cache);
        }
    }
}

/**
 * 链表节点
 */
class Node implements Comparable<Node> {
    /**缓存使用的频率*/
    int cnt;
    /**缓存的使用时间*/
    int time;
    int key;
    int value;

    public Node() {
    }

    Node(int cnt, int time, int key, int value) {
        this.cnt = cnt;
        this.time = time;
        this.key = key;
        this.value = value;
    }

    @Override
    public boolean equals(Object anObject) {
        if (this == anObject) {
            return true;
        }
        if (anObject instanceof Node) {
            Node rhs = (Node) anObject;
            return this.cnt == rhs.cnt && this.time == rhs.time;
        }
        return false;
    }

    @Override
    public int compareTo(Node rhs) {
        return cnt == rhs.cnt ? time - rhs.time : cnt - rhs.cnt;
    }

    @Override
    public int hashCode() {
        return cnt * 1000000007 + time;
    }
}
