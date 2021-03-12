package com.security.algorithm.lru;


import java.util.HashMap;

/**
 * 双向+HashMap来实现(基本实现思路：利用HashMap以及双端队列来实现，利用Map来保存key与Node节点的对应关系，实现O(1)查询操作；利用双端队列来保存Node节点链表，当查询命中时，将Node节点移动到链表头部；当插入时，如果容量未满，则直接插入到链表头部，如果容量已满则删除链表最后元素，将新元素插入到链表头部，这样链表头部一直保存最新的Node节点，尾部保存最老的Node节点)
 *
 * 如果一个数据在最近一段时间没有被访问到，那么在将来它被访问的可能性也很小。当限定的空间已存满数据时，应当把最久没有被访问到的数据淘汰。
 * 查询数据时，如果命中将数据移动到链表头部
 * 添加数据时，如果容量未满直接插入到链表头部，如果容量已满则删除尾部元素，插入到链表头部
 * 当存在热点数据时，LRU的效率很好，但偶发性的、周期性的批量操作会导致LRU命中率急剧下降，缓存污染情况比较严重。
 * @author fuhongxing
 *
 */
public class LRUCache {

	private Node head;
	private Node tail;
	/**
	 * map负责查找数据
	 */
	private HashMap<Integer, Node> map;
	/**
	 * 容量
	 */
	private int capacity;
	/**
	 * 链表节点
	 */
	static class Node {
		int key;
		int value;
		/**
		 * 上一个
		 */
		Node prev;
		/**
		 * 下一个
		 */
		Node next;

		Node(int key, int value) {
			this.key = key;
			this.value = value;
		}
	}

	public LRUCache(int capacity) {
		map = new HashMap<>();
		this.capacity = capacity;
		head = new Node(0, 0);
		tail = new Node(0, 0);
		//双向链接
		head.next = tail;
		tail.prev = head;
	}

	public int get(int key) {
		//如果key不存在返回-1
		if (!map.containsKey(key)) {
			return -1;
		}
		//如果key存在，则将节点删除，然后重新插入到链表头部
		Node n = map.get(key);
		remove(n);
		addToHead(n);
		return n.value;
	}

	/**
	 * 写入
	 * @param key
	 * @param value
	 */
	public void put(int key, int value) {
		Node node = new Node(key, value);
		//如果key不存在
		if (!map.containsKey(key)) {
			//如果容量已满，则将链表尾部元素删除  与查询操作将元素置于头部相呼应
			if (map.size() == capacity) {
				map.remove(tail.prev.key);
				remove(tail.prev);
			}
		} else {
			remove(map.get(key));
		}
		//保存Node,将Node移动到链表头部
		map.put(key, node);
		addToHead(node);
	}

	/**
	 * 将Node移动到链表头部
	 * @param node
	 */
	private void addToHead(Node node) {
		Node hnext = head.next;
		node.next = hnext;
		hnext.prev = node;
		head.next = node;
		node.prev = head;
	}

	/**
	 * 删除某个Node
	 * @param node 节点
	 */
	private void remove(Node node) {
		node.prev.next = node.next;
		node.next.prev = node.prev;
	}

	public static void main(String[] args) {
		LRUCache lruCache = new LRUCache(3);
		lruCache.put(1, 1);
		lruCache.put(2, 2);
		lruCache.put(3, 3);
	}
}