package com.security.datastructure.tree;

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
	
}
