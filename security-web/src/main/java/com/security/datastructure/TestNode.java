package com.security.datastructure;

import lombok.extern.slf4j.Slf4j;

/**
 * 单向链表
 * @author Alan.Fu
 */
@Slf4j
public class TestNode {
    @Slf4j
    static class Node{

        private Object data;
        /**
         * 下一个节点
         */
        private Node nextNode;

        public Node() {
        }

        public Node(Object data) {
            this.data = data;
        }

        /**
         * 追加节点
         * @param node
         */
        public Node append(Node node){
            Node currentNode = this;
            //循环向后查找，是否有下一个节点
            while (true){
                //获取下一个节点
                Node nextNode = currentNode.nextNode;
                //如果下一个节点为null，说明当前节点已经是最后一个节点
                if (nextNode == null){
                    break;
                }
                //赋值给当前节点
                currentNode = nextNode;
            }
            //把需要追加的节点的添加到当前节点的下一个节点
            currentNode.nextNode = node;
            return this;
        }

        public Node next(){
            return this.nextNode;
        }

        public Object getData(){
            return this.data;
        }

        /**
         * 判断节点是否为最后一个节点
         * @return
         */
        public Boolean isLast(){
            return nextNode == null;
        }
        /**
         * 删除下一个节点
         * @return
         */
        public void removeNextNode(){
            //获取下下一个节点
            Node node  = nextNode.nextNode;
            //把下下一个节点设置为当前节点的下一个节点
            this.nextNode = node;
        }
        /**
         * 插入一个节点作为当前节点的下一个节点
         * @return
         */
        public void after(Node node){
            //获取下一个节点，作为下下一个节点
            Node nextNext  = nextNode;
            //把新节点作为当前接地那的下一个节点
            this.nextNode = node;
            //把下下一个节点设置为当前节点的下一个节点
            node.nextNode = nextNext;
        }
        /**
         * 显示所有信息
         * @return
         */
        public void show(){
            Node currentNode  = this;
            while (true){
                log.info("{}", currentNode.getData());
                //取出下一个节点
                currentNode = currentNode.nextNode;
                if(currentNode == null){
                    break;
                }
            }
        }

        /**
         * 长度
         * @param head
         * @return
         */
        public int countLength(Node head) {
            int length = 1;
            while (head.nextNode != null) {
                length += 1;
                head = head.nextNode;
            }
            return length;
        }

        /**
         * 链表中间数,如果有两个中间结点，则返回第二个中间结点
         * 遍历的方式 时间复杂度：O(N)  空间复杂度：O(N)
         * @param head
         * @return
         */
        public Node middleNode(Node head) {
            Node[] ints = new Node[countLength(head)];

            int i = 0;
            ints[0] = head;
            while (head.nextNode != null) {
                i += 1;
                ints[i] = head.nextNode;
                head = head.nextNode;
            }
            return ints[ints.length / 2];
        }

        /**
         * 链表中间数,如果有两个中间结点，则返回第二个中间结点
         * 快慢指针方式 时间复杂度：O(N) 空间复杂度：O(1)
         * @param head
         * @return
         */
        public Node middleNode2(Node head) {
            Node p = head, q = head;
            while (p != null && q.nextNode!= null) {
                p = p.nextNode;
                q = q.nextNode.nextNode;
            }
            return p;
        }
    }

    public static void main(String[] args) {
        Node node1 = new Node(1);
        Node node2 = new Node(2);
        Node node3 = new Node(3);
        Node node4 = new Node(4);
        //追加
        node1.append(node2).append(node3).append(node4);
        log.info("data:{}", node1.next().next().getData());
        log.info("is last:{}", node1.next().isLast());
        log.info("链表中间数：{}", node1.middleNode(node1).getData());
        //给定一个带有头结点 head 的非空单链表，返回链表的中间结点。

        node1.show();
        //单向链表，删除的是当前节点的下一个节点node3
//        node1.nextNode.removeNextNode();
        //插入节点
        node1.nextNode.after(new Node(4));
        node1.show();
    }

}
