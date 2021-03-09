package com.security.datastructure;

import lombok.Data;

/**
 * 树节点
 * @author fhx
 * @date 2019年12月5日
 */
@Data
public class TreeNode {
	public TreeNode() {
		
	}
	public TreeNode(Object value) {
		this.value = value;
	}
	/**
	 * 节点的权
	 */
	private Object value;
	/**
	 * 左树
	 */
	private TreeNode leftNode;
	/**
	 * 左树
	 */
	private TreeNode rightNode;

	private boolean result = true;
	/**
	 * 前序遍历(递归调用)
	 */
	public void frontShow() {
		//先遍历当前节点内容
		System.out.println("当前节点:"+value);
		if(leftNode != null) {
			leftNode.frontShow();	
		}
		if(rightNode != null) {
			rightNode.frontShow();	
		}
	}
	
	/**
	 * 前序查找
	 */
	public TreeNode frontSearch(Object obj) {
		//先遍历当前节点内容
		TreeNode target = null;
		if(this.value == obj) {
			return this;
		}else {
			//查找左子节点
			if(leftNode != null) {
				//有可能可以查到，也可能查不到，查不到的话target还是一个null
				target = leftNode.frontSearch(obj);
			}
			//如果不为空说明左子节点已经找到
			if(target != null) {
				return target;
			}
			//查找右子节点
			if(rightNode != null) {
				target = rightNode.frontSearch(obj);
			}
		}
		return target;
	}
	/**
	 * 中序遍历(递归调用)
	 */
	public void midShow() {
		if(leftNode != null) {
			leftNode.midShow();	
		}
		System.out.println("当前节点:"+value);
		if(rightNode != null) {
			rightNode.midShow();	
		}
	}
	/**
	 * 后序遍历(递归调用)
	 */
	public void afterShow() {
		if(leftNode != null) {
			leftNode.afterShow();	
		}
		
		if(rightNode != null) {
			rightNode.afterShow();	
		}
		System.out.println("当前节点:"+value);
	}

	/**
	 * 判断一棵树是否是平衡二叉树(什么是平衡二叉树: 就是每一个结点的左右子树的高度差不超过1。)
	 * @param node
	 * @return
	 */
	public boolean isBalanced(TreeNode node){
		height(node);
		return result;
	}

	/**
	 * 在求树的高度的时候达到判断是否为平衡树的目的。所以一旦确定不是平衡二叉树了，我也就不关心树的高度了。当不是平衡二叉树的时候，我就直接返回高度是0，这样就省去了求高度的时间。
	 * @param node
	 * @return
	 */
	public int height(TreeNode node){

		if (node == null) {
			return 0;
		}
		int left = height(node.leftNode);
		int right = height(node.rightNode);
		if (Math.abs(left - right) > 0){
			result = false;
		}

		return Math.max(left, right) + 1;
	}
}
