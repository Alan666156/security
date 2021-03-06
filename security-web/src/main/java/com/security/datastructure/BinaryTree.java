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
	
	public void frontShow() {
		root.frontShow();
	}

	public void midShow() {
		root.midShow();
	}
	public void afterShow() {
		root.afterShow();
	}
}
