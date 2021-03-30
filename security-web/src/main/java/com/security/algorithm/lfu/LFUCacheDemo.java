package com.security.algorithm.lfu;

import lombok.Data;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * LFU，最近不经常使用，把数据加入到链表中，按频次排序，一个数据被访问过，把它的频次+1，发生淘汰的时候，把频次低的淘汰掉。
 * https://leetcode-cn.com/problems/lfu-cache/solution/lfuhuan-cun-by-leetcode-solution/
 *
 * 算法流程：
 * 比如有数据 1，1，1，2，2，3
 * 缓存中有（1(3次)，2(2次)）
 * 当3加入的时候，得把后面的2淘汰，变成（1(3次)，3(1次)）
 * 区别：LRU 是得把 1 淘汰。
 *
 *
 * 复杂度分析:
 * 时间复杂度：get 时间复杂度 O(1)O(1)，put 时间复杂度 O(1)O(1)。由于两个操作从头至尾都只利用了哈希表的插入删除还有链表的插入删除，且它们的时间复杂度均为 O(1)O(1)，所以保证了两个操作的时间复杂度均为 O(1)O(1)。
 * 空间复杂度：O(\textit{capacity})O(capacity)，其中 \textit{capacity}capacity 为 LFU 的缓存容量。哈希表中不会存放超过缓存容量的键值对。
 * @author fuhx
 */
class LFUCacheDemo {
    /**
     * 记录一个当前缓存最少使用的频率,为了删除操作服务的
     */
    int minfreq;
    /**
     * 容量
     */
    int capacity;
    /**
     * keyTable 以键值 key 为索引，每个索引存放对应缓存在 freqTable 中链表里的内存地址，这样我们就能利用两个哈希表来使得两个操作的时间复杂度均为 O(1)O(1)。同时需要记录一个当前缓存最少使用的频率 minFreq，这是为了删除操作服务的。
     */
    Map<Integer, LfuNode> keyTable;
    /**
     * freqTable 以频率 freq 为索引，每个索引存放一个双向链表，这个链表里存放所有使用频率为 freq 的缓存，缓存里存放三个信息，分别为键 key，值 value，以及使用频率 freq
     */
    Map<Integer, LinkedList<LfuNode>> freqTable;

    public LFUCacheDemo(int capacity) {
        this.minfreq = 0;
        this.capacity = capacity;
        keyTable = new HashMap<Integer, LfuNode>();;
        freqTable = new HashMap<Integer, LinkedList<LfuNode>>();
    }
    
    public int get(int key) {
        if (capacity == 0) {
            return -1;
        }
        if (!keyTable.containsKey(key)) {
            return -1;
        }
        LfuNode node = keyTable.get(key);
        int val = node.val, freq = node.freq;
        freqTable.get(freq).remove(node);
        // 如果当前链表为空，我们需要在哈希表中删除，且更新minFreq
        if (freqTable.get(freq).size() == 0) {
            freqTable.remove(freq);
            if (minfreq == freq) {
                minfreq += 1;
            }
        }
        // 插入到 freq + 1 中
        LinkedList<LfuNode> list = freqTable.getOrDefault(freq + 1, new LinkedList<LfuNode>());
        list.offerFirst(new LfuNode(key, val, freq + 1));
        freqTable.put(freq + 1, list);
        keyTable.put(key, freqTable.get(freq + 1).peekFirst());
        return val;
    }
    
    public void put(int key, int value) {
        if (capacity == 0) {
            return;
        }
        if (!keyTable.containsKey(key)) {
            // 缓存已满，需要进行删除操作
            if (keyTable.size() == capacity) {
                // 通过 minFreq 拿到 freq_table[minFreq] 链表的末尾节点
                LfuNode node = freqTable.get(minfreq).peekLast();
                keyTable.remove(node.key);
                freqTable.get(minfreq).pollLast();
                if (freqTable.get(minfreq).size() == 0) {
                    freqTable.remove(minfreq);
                }
            }
            LinkedList<LfuNode> list = freqTable.getOrDefault(1, new LinkedList<LfuNode>());
            list.offerFirst(new LfuNode(key, value, 1));
            freqTable.put(1, list);
            keyTable.put(key, freqTable.get(1).peekFirst());
            minfreq = 1;
        } else {
            // 与 get 操作基本一致，除了需要更新缓存的值
            LfuNode node = keyTable.get(key);
            int freq = node.freq;
            freqTable.get(freq).remove(node);
            if (freqTable.get(freq).size() == 0) {
                freqTable.remove(freq);
                if (minfreq == freq) {
                    minfreq += 1;
                }
            }
            LinkedList<LfuNode> list = freqTable.getOrDefault(freq + 1, new LinkedList<LfuNode>());
            list.offerFirst(new LfuNode(key, value, freq + 1));
            freqTable.put(freq + 1, list);
            keyTable.put(key, freqTable.get(freq + 1).peekFirst());
        }
    }
}
@Data
class LfuNode {
    int key;
    int val;
    /**使用频率*/
    int freq;

    LfuNode(int key, int val, int freq) {
        this.key = key;
        this.val = val;
        this.freq = freq;
    }
}
