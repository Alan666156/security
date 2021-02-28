package com.security.datastructure;

import lombok.extern.slf4j.Slf4j;

/**
 * 循环链表
 * @author Alan.Fu
 */
@Slf4j
public class TestLoopNode {
    @Slf4j
    static class LoopNode {

        private Object data;
        /**
         * 下一个节点
         */
        private LoopNode nextNode = this;

        public LoopNode() {
        }

        public LoopNode(Object data) {
            this.data = data;
        }

        public LoopNode next(){
            return this.nextNode;
        }

        public Object getData(){
            return this.data;
        }

        /**
         * 插入一个节点作为当前节点的下一个节点
         * @return
         */
        public void after(LoopNode node){
            //获取下一个节点，作为下下一个节点
            LoopNode nextNext  = nextNode;
            //把新节点作为当前接地那的下一个节点
            this.nextNode = node;
            //把下下一个节点设置为当前节点的下一个节点
            node.nextNode = nextNext;
        }
    }

    public static void main(String[] args) {
        LoopNode node1 = new LoopNode(1);
        LoopNode node2 = new LoopNode(2);
        LoopNode node3 = new LoopNode(3);
        //插入节点
        node1.after(node2);
        node2.after(node3);
        log.info("循环链表:{}", node1.nextNode.getData());
        log.info("循环链表:{}", node3.nextNode.getData());
    }

}
