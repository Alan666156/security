package com.security.datastructure.tree;

import lombok.Data;

/**
 * 二叉树
 * 树有很多种，每个节点最多只能有两个子节点的一种形式称为二叉树。二叉树的子节点分为左节点和右节点
 * 前序遍历: 先输出父节点，再遍历左子树和右子树
 * 中序遍历: 先遍历左子树，再输出父节点，再遍历右子树
 * 后序遍历: 先遍历左子树，再遍历右子树，最后输出父节点
 * 小结: 看输出父节点的顺序，就确定是前序，中序还是后序
 *
 */
public class BinaryTreeDemo {

	public static void main(String[] args) {
		//先需要创建一颗二叉树
		BinaryTree binaryTree = new BinaryTree();

		//创建需要的节点
		HeroNode root = new HeroNode(1, "宋江");
		HeroNode node2 = new HeroNode(2, "吴用");
		HeroNode node3 = new HeroNode(3, "卢俊义");
		HeroNode node4 = new HeroNode(4, "林冲");
		HeroNode node5 = new HeroNode(5, "关胜");
		HeroNode node6 = new HeroNode(6, "sam");
		HeroNode node7 = new HeroNode(7, "smith");
		HeroNode node8 = new HeroNode(8, "tt");
		HeroNode node9 = new HeroNode(9, "ak");

		//说明，我们先手动创建该二叉树，后面我们学习递归的方式创建二叉树
		root.setLeft(node2);
		root.setRight(node3);
		node3.setLeft(node4);
		node3.setRight(node5);
		binaryTree.setRoot(root);
		System.out.println("======树的打印======");
		show(binaryTree);
		//测试
//		System.out.println("前序遍历"); // 1,2,3,5,4
//		binaryTree.preOrder();

		//测试
//		System.out.println("中序遍历");
//		binaryTree.infixOrder(); // 2,1,5,3,4
//
//		System.out.println("后序遍历");
//		binaryTree.postOrder(); // 2,5,4,3,1

		//前序遍历
		//前序遍历的次数 ：4
//		System.out.println("前序遍历方式~~~");
//		HeroNode resNode = binaryTree.preOrderSearch(5);
//		if (resNode != null) {
//			System.out.printf("找到了，信息为 no=%d name=%s", resNode.getNo(), resNode.getName());
//		} else {
//			System.out.printf("没有找到 no = %d 的英雄", 5);
//		}

		//中序遍历查找
		//中序遍历3次
//		System.out.println("中序遍历方式~~~");
//		HeroNode resNode = binaryTree.infixOrderSearch(5);
//		if (resNode != null) {
//			System.out.printf("找到了，信息为 no=%d name=%s", resNode.getNo(), resNode.getName());
//		} else {
//			System.out.printf("没有找到 no = %d 的英雄", 5);
//		}

		//后序遍历查找
		//后序遍历查找的次数  2次
//		System.out.println("后序遍历方式~~~");
//		HeroNode resNode = binaryTree.postOrderSearch(5);
//		if (resNode != null) {
//			System.out.printf("找到了，信息为 no=%d name=%s", resNode.getNo(), resNode.getName());
//		} else {
//			System.out.printf("没有找到 no = %d 的英雄", 5);
//		}

		System.out.println("删除前,前序遍历");
		binaryTree.preOrder(); //  1,2,3,5,4
		binaryTree.delNode(5);
		//binaryTree.delNode(3);
		System.out.println("删除后，前序遍历");
		binaryTree.preOrder(); // 1,2,3,4



	}
	/**
	 * 打印树
	 * @param binaryTree
	 */
	public static void show(BinaryTree binaryTree) {
		HeroNode root = binaryTree.getRoot();
		if (root == null) {
			System.out.println("EMPTY!");
		}
		// 得到树的深度
		int treeDepth = getTreeDepth(root);

		// 最后一行的宽度为2的（n - 1）次方乘3，再加1
		// 作为整个二维数组的宽度
		int arrayHeight = treeDepth * 2 - 1;
		int arrayWidth = (2 << (treeDepth - 2)) * 3 + 1;
		// 用一个字符串数组来存储每个位置应显示的元素
		String[][] res = new String[arrayHeight][arrayWidth];
		// 对数组进行初始化，默认为一个空格
		for (int i = 0; i < arrayHeight; i ++) {
			for (int j = 0; j < arrayWidth; j ++) {
				res[i][j] = " ";
			}
		}

		// 从根节点开始，递归处理整个树
		// res[0][(arrayWidth + 1)/ 2] = (char)(root.val + '0');
		writeArray(root, 0, arrayWidth/ 2, res, treeDepth);
		// 此时，已经将所有需要显示的元素储存到了二维数组中，将其拼接并打印即可
		for (String[] line: res) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < line.length; i ++) {
				sb.append(line[i]);
				if (line[i].length() > 1 && i <= line.length - 1) {
					i += line[i].length() > 4 ? 2: line[i].length() - 1;
				}
			}
			System.out.println(sb.toString());
		}
	}

	/**
	 * 用于获得树的层数
	 */
	public static int getTreeDepth(HeroNode root) {
		return root == null ? 0 : (1 + Math.max(getTreeDepth(root.getLeft()), getTreeDepth(root.getRight())));
	}
	private static void writeArray(HeroNode currentNode, int rowIndex, int columnIndex, String[][] res, int treeDepth) {
		// 保证输入的树不为空
		if (currentNode == null) {
			return;
		}
		// 先将当前节点保存到二维数组中
		res[rowIndex][columnIndex] = String.valueOf(currentNode.getNo());

		// 计算当前位于树的第几层
		int currentLevel = ((rowIndex + 1) / 2);
		// 若到了最后一层，则返回
		if (currentLevel == treeDepth) {
			return;
		}
		// 计算当前行到下一行，每个元素之间的间隔（下一行的列索引与当前元素的列索引之间的间隔）
		int gap = treeDepth - currentLevel - 1;

		// 对左子树进行判断，若有左子树，则记录相应的"/"与左子树的值
		if (currentNode.getLeft() != null) {
			res[rowIndex + 1][columnIndex - gap] = "/";
			writeArray(currentNode.getLeft(), rowIndex + 2, columnIndex - gap * 2, res, treeDepth);
		}

		// 对右子树进行判断，若有右子树，则记录相应的"\"与右子树的值
		if (currentNode.getRight() != null) {
			res[rowIndex + 1][columnIndex + gap] = "\\";
			writeArray(currentNode.getRight(), rowIndex + 2, columnIndex + gap * 2, res, treeDepth);
		}
	}


}

/**
 * 定义BinaryTree 二叉树
 */
@Data
class BinaryTree {
	private HeroNode root;
	public BinaryTree() {
	}
	public BinaryTree(HeroNode root) {
		this.root = root;
	}

	/**
	 * 删除节点
	 * @param no
	 */
	public void delNode(int no) {
		if(root != null) {
			//如果只有一个root节点, 这里立即判断root是不是就是要删除节点
			if(root.getNo() == no) {
				root = null;
			} else {
				//递归删除
				root.delNode(no);
			}
		}else{
			System.out.println("空树，不能删除~");
		}
	}

	/**
	 * 前序遍历: 先输出父节点，再遍历左子树和右子树
	 */
	public void preOrder() {
		if(this.root != null) {
			this.root.preOrder();
		}else {
			System.out.println("二叉树为空，无法遍历");
		}
	}

	/**
	 * 中序遍历: 先遍历左子树，再输出父节点，再遍历右子树
	 */
	public void middleOrder() {
		if(this.root != null) {
			this.root.infixOrder();
		}else {
			System.out.println("二叉树为空，无法遍历");
		}
	}

	/**
	 * 后序遍历: 先遍历左子树，再遍历右子树，最后输出父节点
	 */
	public void postOrder() {
		if(this.root != null) {
			this.root.postOrder();
		}else {
			System.out.println("二叉树为空，无法遍历");
		}
	}

	/**
	 * 前序遍历查找
	 * @param no
	 * @return
	 */
	public HeroNode preOrderSearch(int no) {
		if(root != null) {
			return root.preOrderSearch(no);
		} else {
			return null;
		}
	}

	/**
	 * 中序遍历查找
	 * @param no
	 * @return
	 */
	public HeroNode middleOrderSearch(int no) {
		if(root != null) {
			return root.infixOrderSearch(no);
		}else {
			return null;
		}
	}

	/**
	 * 后序遍历查找
	 * @param no
	 * @return
	 */
	public HeroNode postOrderSearch(int no) {
		if(root != null) {
			return this.root.postOrderSearch(no);
		}else {
			return null;
		}
	}
}

/**
 * 先创建HeroNode 节点
 */
class HeroNode {
	/**
	 * 雇员编号
	 */
	private int no;
	/**
	 * 雇员姓名
	 */
	private String name;
	/**
	 * //默认null
	 */
	private HeroNode left;
	private HeroNode right;
	public HeroNode(int no, String name) {
		this.no = no;
		this.name = name;
	}
	public int getNo() {
		return no;
	}
	public void setNo(int no) {
		this.no = no;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public HeroNode getLeft() {
		return left;
	}
	public void setLeft(HeroNode left) {
		this.left = left;
	}
	public HeroNode getRight() {
		return right;
	}
	public void setRight(HeroNode right) {
		this.right = right;
	}
	@Override
	public String toString() {
		return "HeroNode [no=" + no + ", name=" + name + "]";
	}

	/**
	 * 递归删除节点
	 * 1.如果删除的节点是叶子节点，则删除该节点
	 * 2.如果删除的节点是非叶子节点，则删除该子树
	 * @param no
	 */
	public void delNode(int no) {

		/**
		 * 思路：
		 * 	1. 因为我们的二叉树是单向的，所以我们是判断当前节点的子节点是否需要删除节点，而不能去判断当前这个节点是不是需要删除节点.
			2. 如果当前节点的左子节点不为空，并且左子节点 就是要删除节点，就将this.left = null; 并且就返回(节束递归删除)
			3. 如果当前节点的右子节点不为空，并且右子节点 就是要删除节点，就将this.right= null ;并且就返回(节束递归删除)
			4. 如果第2和第3步没有删除节点，那么我们就需要向左子树进行递归删除
			5.  如果第4步也没有删除节点，则应当向右子树进行递归删除.
		 */

		//2. 如果当前节点的左子节点不为空，并且左子节点 就是要删除节点，就将this.left = null; 并且就返回(节束递归删除)
		if(this.left != null && this.left.no == no) {
			this.left = null;
			return;
		}
		//3.如果当前节点的右子节点不为空，并且右子节点 就是要删除节点，就将this.right= null ;并且就返回(节束递归删除)
		if(this.right != null && this.right.no == no) {
			this.right = null;
			return;
		}
		//4.我们就需要向左子树进行递归删除
		if(this.left != null) {
			this.left.delNode(no);
		}
		//5.则应当向右子树进行递归删除
		if(this.right != null) {
			this.right.delNode(no);
		}
	}

	/**
	 * 编写前序遍历的方法
	 */
	public void preOrder() {
		//先输出父节点
		System.out.println(this);
		//递归向左子树前序遍历
		if(this.left != null) {
			this.left.preOrder();
		}
		//递归向右子树前序遍历
		if(this.right != null) {
			this.right.preOrder();
		}
	}

	/**
	 * 中序遍历
	 */
	public void infixOrder() {
		//递归向左子树中序遍历
		if(this.left != null) {
			this.left.infixOrder();
		}
		//输出父节点
		System.out.println(this);
		//递归向右子树中序遍历
		if(this.right != null) {
			this.right.infixOrder();
		}
	}

	/**
	 * 后序遍历
	 */
	public void postOrder() {
		if(this.left != null) {
			this.left.postOrder();
		}
		if(this.right != null) {
			this.right.postOrder();
		}
		System.out.println(this);
	}

	/**
	 * 前序遍历查找
	 * @param no 查找no
	 * @return 如果找到就返回该Node ,如果没有找到返回 null
	 */
	public HeroNode preOrderSearch(int no) {
		System.out.println("===>进入前序遍历");
		//比较当前节点是不是
		if(this.no == no) {
			return this;
		}
		//1.则判断当前节点的左子节点是否为空，如果不为空，则递归前序查找
		//2.如果左递归前序查找，找到节点，则返回
		HeroNode resultNode = null;
		if(this.left != null) {
			resultNode = this.left.preOrderSearch(no);
		}
		//说明我们左子树找到
		if(resultNode != null) {
			return resultNode;
		}
		//1.左递归前序查找，找到节点，则返回，否继续判断，
		//2.当前的节点的右子节点是否为空，如果不空，则继续向右递归前序查找
		if(this.right != null) {
			resultNode = this.right.preOrderSearch(no);
		}
		return resultNode;
	}

	/**
	 * 中序遍历查找
	 * @param no
	 * @return
	 */
	public HeroNode infixOrderSearch(int no) {
		//判断当前节点的左子节点是否为空，如果不为空，则递归中序查找
		HeroNode resNode = null;
		if(this.left != null) {
			resNode = this.left.infixOrderSearch(no);
		}
		if(resNode != null) {
			return resNode;
		}
		System.out.println("进入中序查找");
		//如果找到，则返回，如果没有找到，就和当前节点比较，如果是则返回当前节点
		if(this.no == no) {
			return this;
		}
		//否则继续进行右递归的中序查找
		if(this.right != null) {
			resNode = this.right.infixOrderSearch(no);
		}
		return resNode;

	}

	/**
	 * 后序遍历查找
	 * @param no
	 * @return
	 */
	public HeroNode postOrderSearch(int no) {

		//判断当前节点的左子节点是否为空，如果不为空，则递归后序查找
		HeroNode resNode = null;
		if(this.left != null) {
			resNode = this.left.postOrderSearch(no);
		}
		if(resNode != null) {//说明在左子树找到
			return resNode;
		}

		//如果左子树没有找到，则向右子树递归进行后序遍历查找
		if(this.right != null) {
			resNode = this.right.postOrderSearch(no);
		}
		if(resNode != null) {
			return resNode;
		}
		System.out.println("进入后序查找");
		//如果左右子树都没有找到，就比较当前节点是不是
		if(this.no == no) {
			return this;
		}
		return resNode;
	}

}



