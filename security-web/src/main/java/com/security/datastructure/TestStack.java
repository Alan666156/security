package com.security.datastructure;

import java.util.Arrays;

/**
 * 栈(栈是一种按照先进后出的数据存储结构(LIFO)，它检索元素的顺序与存储元素的顺序是相反的)
 * 
 * @author Alan.Fu
 *
 */
public class TestStack {

	public static void main(String[] args) {
		TestStack testStack = new TestStack();
		testStack.push("1");
		testStack.push("2");
		testStack.push("3");
		System.out.println("栈元素："+Arrays.toString(testStack.elements));
		System.out.println("栈顶元素："+testStack.peek());
		testStack.pop();
		System.out.println("栈顶元素："+testStack.peek());

	}
	Object [] elements;
	
	public TestStack(){
		elements = new Object[0];
	}
	/**
	 * 出栈(剔除最后一个)
	 * @param
	 * @throws Exception 
	 */
	public Object pop() throws RuntimeException{
		if(elements.length == 0){
			throw new RuntimeException("stack is empty");
		}
		//取出数组的最后一个元素
		Object obj = elements[elements.length - 1];
		//创建一个新的数组
		Object[] newArray = new Object [elements.length-1];
		//原数组中除了最后一个其余的copy到时新数组中
		for (int i = 0; i < elements.length - 1; i++) {
			newArray[i] = elements[i];
		}
		elements = newArray;
		return obj;
	}
	
	/**
	 * 入栈
	 * @param obj 
	 */
	public void push(Object obj){
		//创建一个新的数组
		Object[] newArray = new Object [elements.length +1];
		//把原数组中的元素copy到新数组中
		for (int i = 0; i < elements.length; i++) {
			newArray[i] = elements[i];
		}
		//新增加的元素放入栈中
		newArray[elements.length] = obj;
		elements = newArray;
	}
	/**
	 * 查看栈顶元素
	 * @return
	 */
	public Object peek(){
		if(elements.length == 0){
			throw new RuntimeException("stack is empty");
		}
		return elements[elements.length - 1];
	}
	/**
	 * 
	 * @return
	 */
	public Boolean isEmpty(){
		return elements.length == 0 ? false : true;
	}
}
