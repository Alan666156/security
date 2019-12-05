package com.security.datastructure;

import java.util.Arrays;

/**
 * 队列(栈是一种按照先进后出的数据存储结构(LIFO)，它检索元素的顺序与存储元素的顺序是相反的)
 * @author Alan.Fu
 *
 */
public class TestQueue {

	public static void main(String[] args) {
		TestQueue testStack = new TestQueue();
		testStack.add("1");
		testStack.add("2");
		testStack.add("3");
		System.out.println("队列元素："+Arrays.toString(testStack.elements));
		testStack.poll();
		testStack.add("4");
		System.out.println("队列元素："+Arrays.toString(testStack.elements));


	}
	Object [] elements;
	
	public TestQueue(){
		elements = new Object[0];
	}
	/**
	 * 出队
	 * @param obj 
	 * @throws Exception 
	 */
	public Object poll() throws RuntimeException{
		if(elements.length==0){
			throw new RuntimeException("stack is empty");
		}
		//取出数组的第一个元素
		Object obj = elements[0];
		//创建一个新的数组
		Object[] newArray = new Object [elements.length-1];
		//原数组中除了第一个其余的copy到时新数组中
		for (int i = 0; i < elements.length - 1; i++) {
			newArray[i] = elements[i + 1];
		}
		elements = newArray;
		return obj;
	}
	
	/**
	 * 入队
	 * @param obj 
	 */
	public void add(Object obj){
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
	 * 查看队列元素
	 * @return
	 */
	public Object show(){
		if(elements.length==0){
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
