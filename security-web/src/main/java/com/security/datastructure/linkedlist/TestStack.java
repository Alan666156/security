package com.security.datastructure.linkedlist;

import java.util.Stack;

/**
 * 栈Stack的基本使用
 * 栈是一个先进后出(FILO-First In Last Out)的有序列表。
 * 栈(stack)是限制线性表中元素的插入和删除只能在线性表的同一端进行的一种特殊线性表。允许插入和删除的一端，为变化的一端，称为栈顶(Top)，另一端为固定的一端，称为栈底(Bottom)。
 * 根据栈的定义可知，最先放入栈中元素在栈底，最后放入的元素在栈顶，而删除元素刚好相反，最后放入的元素最先删除，最先放入的元素最后删除
 */
public class TestStack {

	public static void main(String[] args) {
		Stack<String> stack = new Stack();
		// 入栈
		stack.add("jack");
		stack.add("tom");
		stack.add("smith");
//		stack.push("alan");
		// 出栈
		// smith, tom , jack
		while (stack.size() > 0) {
			//pop就是将栈顶的数据取出
			System.out.println(stack.pop());
			//从栈顶获取一个元素，但不会将该元素从栈中删除
//			stack.peek();
		}
	}

}
