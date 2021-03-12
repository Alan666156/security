package com.security.datastructure.tree;

/**
 * 顺序存储二叉树(数组存储方式和树的存储方式可以相互转换，即数组可以转换成树，树也可以转换成数组)
 * 特点：
 * 	顺序二叉树通常只考虑完全二叉树
 * 	第n个元素的左子节点为  2 * n + 1
 * 	第n个元素的右子节点为  2 * n + 2
 * 	第n个元素的父节点为  (n-1) / 2
 * @author fuhongxing
 */
public class ArrBinaryTreeDemo {

	public static void main(String[] args) {
		int[] arr = { 1, 2, 3, 4, 5, 6, 7 };
		//创建一个 ArrBinaryTree
		ArrBinaryTree arrBinaryTree = new ArrBinaryTree(arr);
		arrBinaryTree.preOrder(); // 1,2,4,5,3,6,7
	}

}

/**
 * 编写一个ArrayBinaryTree, 实现顺序存储二叉树遍历
 */
class ArrBinaryTree {
	/**
	 * 存储数据结点的数组
	 */
	private int[] arr;

	public ArrBinaryTree(int[] arr) {
		this.arr = arr;
	}

	/**
	 * 重载preOrder
	 */
	public void preOrder() {
		this.preOrder(0);
//		this.middleOrder(0);
//		this.postOrder(0);
	}

	//编写一个方法，完成顺序存储二叉树的前序遍历

	/**
	 * 二叉树的前序遍历
	 * 先输出父节点，再遍历左子树和右子树
	 * @param index 数组的下标
	 */
	public void preOrder(int index) {
		//如果数组为空，或者 arr.length = 0
		if(arr == null || arr.length == 0) {
			System.out.println("数组为空，不能按照二叉树的前序遍历");
		}

		//输出当前这个元素
		System.out.println(index + "-->" + arr[index]);

		//向左递归遍历
		if((index * 2 + 1) < arr.length) {
			preOrder(2 * index + 1 );
		}

		//向右递归遍历
		if((index * 2 + 2) < arr.length) {
			preOrder(2 * index + 2);
		}
	}

	/**
	 * 二叉树的中序遍历
	 * 先遍历左子树，再输出父节点，再遍历右子树
	 * @param index 数组的下标(根节点从 0 开始，数组下标默认从0开始)
	 */
	public void middleOrder(int index) {
		//如果数组为空，或者 arr.length = 0
		if(arr == null || arr.length == 0) {
			System.out.println("中序遍历-->数组为空，不能按照二叉树的前序遍历");
		}

		//输出当前这个元素
		System.out.println(index + "中序遍历-->" + arr[index]);

		//向左递归遍历
		if((index * 2 + 1) < arr.length) {
			middleOrder(2 * index + 1 );
		}

		//向右递归遍历
		if((index * 2 + 2) < arr.length) {
			middleOrder(2 * index + 2);
		}
	}

	/**
	 * 二叉树的后序遍历
	 * 先遍历左子树，再遍历右子树，最后输出父节点
	 * @param index 数组的下标
	 */
	public void postOrder(int index) {
		//如果数组为空，或者 arr.length = 0
		if(arr == null || arr.length == 0) {
			System.out.println("后序遍历-->数组为空，不能按照二叉树的前序遍历");
		}

		//输出当前这个元素
		System.out.println(arr[index]);

		//向左递归遍历
		if((index * 2 + 1) < arr.length) {
			postOrder(2 * index + 1 );
		}

		//向右递归遍历
		if((index * 2 + 2) < arr.length) {
			postOrder(2 * index + 2);
		}
	}
}