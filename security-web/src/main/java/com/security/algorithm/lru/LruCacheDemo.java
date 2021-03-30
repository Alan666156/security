package com.security.algorithm.lru;

import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 设计一种数据结构实现LRU算法，查询以及插入操作时间复杂度为O(1)，并且满足最近最少使用原则。如果一个数据在最近一段时间没有被访问到，那么在将来它被访问的可能性也很小。
 * 当限定的空间已存满数据时，应当把最久没有被访问到的数据淘汰。
 * LRU算法 (Least Recently Used) 的意思就是近期最少使用算法，它的核心思想就是会优先淘汰那些近期最少使用的缓存对象。（LinkedHashMap默认就实现了该策略，LinkedHashMap源码64行注释有说明）
 * https://leetcode-cn.com/problems/lru-cache/
 * 算法流程：
 * LRU，最近最少使用，把数据加入一个链表中，按访问时间排序，发生淘汰的时候，把访问时间最旧的淘汰掉。
 * 比如有数据 1，2，1，3，2
 * 此时缓存中已有（1，2）
 * 当3加入的时候，得把后面的2淘汰，变成（3，1）
 */
@Slf4j
public class LruCacheDemo<K, V> extends LinkedHashMap<K, V> {
    /**
     * 缓存容量
     */
    private int capacity;

    public LruCacheDemo(int capacity) {
        super(capacity, 0.75F, true);
        this.capacity = capacity;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return super.size() > capacity;
    }

    public static void main(String[] args) {
        LruCacheDemo<String, Object> lruCache = new LruCacheDemo(3);
        lruCache.put("lruCache1", "lruCache1");
        lruCache.put("lruCache2", "lruCache2");
        lruCache.put("lruCache3", "lruCache3");
    }
}
