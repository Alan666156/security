package com.security.datastructure;

import lombok.extern.slf4j.Slf4j;

/**
 * 双向链表
 * @author Alan.Fu
 */
@Slf4j
public class TestDoubleNode {
    @Slf4j
    static class DoubleNode {
        /**
         * 节点数据
         */
        private Object data;
        /**
         * 下一个节点
         */
        private DoubleNode preNode = this;
        /**
         * 下一个节点
         */
        private DoubleNode nextNode = this;

        public DoubleNode() {
        }

        public DoubleNode(Object data) {
            this.data = data;
        }

        public DoubleNode next(){
            return this.nextNode;
        }
        public DoubleNode pre(){
            return this.preNode;
        }
        public Object getData(){
            return this.data;
        }

        /**
         * 插入一个节点作为当前节点的下一个节点
         * @return
         */
        public void after(DoubleNode node){
            //获取原来的下一个节点
            DoubleNode nextNext  = nextNode;
            //把当前节点作为新节点的下一个节点
            node.preNode = this;
            //把新节点作为当前接地那的下一个节点
            this.nextNode = node;
            //让原来的下一个节点的上一个节点为新节点
            node.nextNode = nextNext;
        }
    }

    public static void main(String[] args) {
        DoubleNode node1 = new DoubleNode(1);
        DoubleNode node2 = new DoubleNode(2);
        DoubleNode node3 = new DoubleNode(3);
        //插入节点
        node1.after(node2);
        node2.after(node3);
        log.info("双向链表:{}", node2.preNode.getData());
        log.info("双向链表:{}", node2.getData());
        log.info("双向链表:{}", node2.nextNode.getData());
        log.info("双向链表:{}", node3.nextNode.getData());
    }

}
