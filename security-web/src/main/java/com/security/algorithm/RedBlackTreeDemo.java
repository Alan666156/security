package com.security.algorithm;

import lombok.Data;

import java.util.Scanner;

/**
 * 红黑树本质上就是一个二叉搜索树,应用比较广泛，主要是用它来存储有序的数据，它的时间复杂度是O(logN)，效率非常之高。
 * 1、每个节点要么是黑色要么红色
 * 2、根节点是黑色
 * 3、每个叶子节点（nil）是黑色 [注意：这里叶子节点，是指为空(NIL或NULL)的叶子节点！]
 * 4、每个红色节点的两个子节点一定都是黑色，不能有两个红色节点相连
 * 5、任意一节点到每个叶子节点的路径都包含数量相同的黑色节点，俗称：黑高
 * 红黑树的自平衡靠的是什么？三种操作：左旋（以某个节点作为支点旋转接点，其右子节点变为旋转）、右旋、变色（节点的颜色有红色变成黑色）
 * @Author: fuhongxing
 * @Date: 2021/3/8
 **/
@Data
public class RedBlackTreeDemo<K extends Comparable<K>, V> {
    //1、创建红黑色，定义颜色
    //2、创建红黑色节点
    private static final boolean RED = true;
    private static final boolean BLACK = false;
    private RedBlackNode blackNode;
    private RedBlackNode redNode;

    /**
     * 根节点
     */
    private RedBlackNode root;
    /**
     * 获取当前节点的父节点
     * @param node
     * @return
     */
    private RedBlackNode parentOf(RedBlackNode node){
        if (node != null) {
            return node.parent;
        }
        return null;
    }

    public static boolean isRED(RedBlackNode node) {
        if (node != null) {
            return node.color = RED;
        }
        return false;
    }
    public static boolean isBlack(RedBlackNode node) {
        if (node != null) {
            return node.color = BLACK;
        }
        return false;
    }

    public void inOrderPrint(){
        inOrderPrint(root);
    }

    /**
     * 校验红黑树合法性
     */
    public void check() {
        if (root == null)
            return ;

        checkRedRed(root);
    }

    //校验规则3
    private void checkRedRed(RedBlackNode node) {
        if (node == null){
            return;
        }
        if (node.getLeft() == null || node.getRight() == null){
            return;
        }
        if (isRED(node) && isRED(node.getLeft())) {
            System.out.println(node.getKey() + "与左子节点都为红色");
        } else if (isRED(node) && isRED(node.getRight())) {
            System.out.println(node.getKey() + "与右子节点都为红色");
        } else {
            checkRedRed(node.getLeft());
            checkRedRed(node.getRight());
        }
    }
    /**
     * 中序遍历
     * @param node
     */
    public void inOrderPrint(RedBlackNode node){
        if (node != null) {
            inOrderPrint(node.left);
            System.out.println("key: " + node.key + ",value:" + node.value);
            inOrderPrint(node.right);
        }
    }

    /**
     * 左旋
     * 1). 进行一次左旋，A节点旋转到其父节点B的位置上，B节点变为A的左子节点(符合B<A的条件)。
     * 2). 如果旋转前 B节点不为根节点(存在父节点R)，则A的父节点从B转变为R，B的父节点从R转为A； 
     * 3). 如果旋转前 A有一个左子节点D，旋转之后A的左子节点为变为B，D变为B的右子节点，始终符合B<D<A的条件。
     * 4). 图中C节点在旋转前后均为B节点的左子节点，P节点均为A节点的右子节点，所以C、P两个节点不需要变动。
     * 综上： 在进行一次左旋时，需要改变的引用关系为 A、B，若存在 R、D，则R、D也需要进行变动。
     *         B                          A
     *     C       A       ----->     B       P
     *           D    P             C   D
     * @param x
     */
    public void leftRotate(RedBlackNode x){
        RedBlackNode y = x.right;
        x.right = y.left;
        if (y.left != null) {
            y.left.parent = x;
        }
        if (x.parent != null) {
            y.parent = x.parent;
            if (x.parent.left != null) {
                x.parent.left = y;
            }else {
                x.parent.right = y;
            }
        }else {
            this.root = y;
            this.root.parent = null;
        }
        x.parent = y;
        y.left = x;
    }

    /**
     * 右旋
     * 1). 进行一次右旋，B节点旋转到其父节点A的位置上，A节点变为B的右子节点(符合B<A的条件)。
     * 2). 如果旋转前 A节点不为根节点(存在父节点R)，则B的父节点从A转变为R，A的父节点从R转为B； 
     * 3). 如果旋转前 B有一个右子节点D，旋转之后B的右子节点为变为A，D变为A的左子节点，始终符合B<D<A的条件。
     * 4). C、P两个节点仍无需变动。
     * 综上： 在进行一次右旋时，需要改变的引用关系为 A、B、R、D，左旋和右旋过程其实正好相反。
     **            A                          B
     *        B       P       ----->      C      A
     *      C   D                              D   P
     * @param y
     */
    public void rightRotate(RedBlackNode y){
        RedBlackNode x = y.right;
        //1、将y的左子节点指向x的右子节点，并且更新x的右子节点的父节点为y
        y.right = x.left;
        if (x.left != null) {
            x.left.parent = y;
        }
        //2、当y的父节点不为空时，更新x的父节点为y的父节点，并将x的父节点制定子树（当前x的子树节点）指定为y
        if (x.parent != null) {
            x.parent = y.parent;
            if (x.parent.left != null) {
                y.parent.left = x;
            }else {
                y.parent.right = x;
            }
        }else {
            this.root = x;
            this.root.parent = null;
        }
        y.parent = x;
        y.left = y;
    }
    /**
     * 写入数据
     * @param key
     * @param value
     */
    public void put(K key, V value){
        RedBlackNode node = new RedBlackNode();
        node.setKey(key);
        node.setValue(value);
        //新节点一定是红色
        node.setColor(RED);
    }
    /**
     * 查找
     * @param node
     */
    public void getNode(RedBlackNode node){
        RedBlackNode parent = null;
        RedBlackNode x = this.root;
        while (x != null){
            parent =x;
            //cmp > 0 说明node.key大于 x.key需要到x的右子树查找
            //cmp = 0 说明node.key等于 x.key查找到结束
            //cmp < 0 说明node.key小于 x.key需要到x的左子树查找
            int cmp = node.key.compareTo(x.key);
            if(cmp > 0){
                x = x.right;
            }else if(cmp == 0){
                x.setValue(node.getValue());
                return;
            }else {
                x = x.left;
            }
        }

        node.parent = parent;
        if (parent != null) {
            int tmp = node.key.compareTo(parent.key);
            if(tmp > 0){
                parent.right = node;
            }else {
                parent.left = node;
            }
        }else {
            this.root = node;
        }

        //需要调用修复红黑树平衡方法
        //putFixup(node);
    }

    /**
     * 插入后修复红黑树平衡的方法
     */
    public void putFixup(RedBlackNode node){
        this.root.setColor(BLACK);
        RedBlackNode parent = parentOf(node);
        RedBlackNode gparent = parentOf(parent);

        //情景4，插入节点的发父节点是红色
        if (parent != null && isRED(parent)) {
            //如果父节点是红色，那么一定存在爷爷节点，因为根节点不可能是红色
            RedBlackNode uncle = null;
            if(parent == gparent.left){
                uncle = gparent.right;
                //4.1叔叔节点存在，并且为红色（父-叔 双红色）
                if (uncle != null && isRED(uncle)) {
                    setBlackNode(parent);
                    setBlackNode(uncle);
                    setRedNode(gparent);
                    putFixup(gparent);
                    return;
                }

                //4.2 叔叔节点存在，或者为黑色
                if (uncle == null || isBlack(uncle)) {
                    if (node == parent.left) {
                        setBlackNode(parent);
                        setRedNode(gparent);
                        putFixup(gparent);
                        return;
                    }
                    //插入节点为其父节点的右子节点（LR情况）
                    if (node == parent.right){
                        leftRotate(parent);
                        putFixup(parent);
                        return;
                    }
                }

            }else {//父节点为爷爷节点的右子树
                //获取叔叔节点
                uncle = gparent.left;
                //叔叔节点存在，并且为红色
                if (uncle != null && isRED(uncle)) {
                    setBlackNode(parent);
                    setBlackNode(uncle);
                    setRedNode(gparent);
                    putFixup(gparent);
                    return;
                }

                //叔叔节点不存在，或者为黑色
                if (uncle == null || isBlack(uncle)) {
                    if(node == parent.right){
                        setBlackNode(parent);
                        setRedNode(gparent);
                        leftRotate(gparent);
                        return;
                    }
                    //插入节点为其父节点的左子节点（RL情况）
                    if(node == parent.left){
                        setBlackNode(parent);
                        setRedNode(gparent);
                        rightRotate(gparent);
                        return;
                    }

                }

            }
        }
    }


    @Data
    static class RedBlackNode <K extends Comparable<K>, V> {
        private RedBlackNode parent;

        private RedBlackNode left;
        private RedBlackNode right;
        private boolean color;
        private K key;
        private V value;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        RedBlackTreeDemo<String, Object> redBlackTreeDemo = new RedBlackTreeDemo<>();
        while (true){
            String next = scanner.next();
            redBlackTreeDemo.put(next, "a");
        }

    }
}
