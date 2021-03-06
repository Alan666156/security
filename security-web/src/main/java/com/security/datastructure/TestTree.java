package com.security.datastructure;

public class TestTree {

	public static void main(String[] args) {
		//创建一棵树
		BinaryTree binaryTree = new BinaryTree();
		//创建一个根节点root
		TreeNode root = new TreeNode(1);
		//根节点赋给树
		binaryTree.setRoot(root);
		
		//创建一个左树节点
		TreeNode leftRoot = new TreeNode(2);
		root.setLeftNode(leftRoot);
		//创建一个右树节点
		TreeNode rightRoot = new TreeNode(3);
		root.setRightNode(rightRoot);
		
		//创建第二层的子节点
		leftRoot.setLeftNode(new TreeNode(4));
		leftRoot.setRightNode(new TreeNode(5));
		
		rightRoot.setLeftNode(new TreeNode(6));
		rightRoot.setRightNode(new TreeNode(7));
		
		//前序遍历(1 2 4 5 3 6 7)
		binaryTree.frontShow();
		System.out.println("===================");
		//中序遍历(4 2 5 1 6 3 7)
		binaryTree.midShow();
		System.out.println("===================");
		//后序遍历(4 5 2 6 7 3 1)
		binaryTree.afterShow();
	}

}
