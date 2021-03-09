package com.security.datastructure;

import lombok.Data;
/**
 * 二叉树
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
