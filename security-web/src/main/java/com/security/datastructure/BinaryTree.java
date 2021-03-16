package com.security.datastructure;

import lombok.Data;
/**
 * 二叉树(二分查找也称折半查找（Binary Search），它是一种效率较高的查找方法。但是，折半查找要求线性表必须采用顺序存储结构，而且表中元素按关键字有序排列。)
 * @author fhx
 * @date 2019年12月5日
 */
@Data
public class BinaryTree {
	/**
	 * 根节点
	 */
	private TreeNode root;
	/**
	 * 前序遍历
	 */
	public void frontShow() {
		root.frontShow();
	}

	/**
	 * 中序遍历
	 */
	public void midShow() {
		root.midShow();
	}
	/**
	 * 后序遍历
	 */
	public void afterShow() {
		root.afterShow();
	}


}
