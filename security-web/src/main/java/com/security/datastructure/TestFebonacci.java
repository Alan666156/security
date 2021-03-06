package com.security.datastructure;

import lombok.extern.slf4j.Slf4j;

/**
 * 婓波拉契数列(递归实现)
 * @author Alan.Fu
 *
 */
@Slf4j
public class TestFebonacci {

	public static void main(String[] args) {
		//婓波拉契数列 1,1,2,3,5,8,13,21...
		int index = 5;
		int i = febonacci(index);
		System.out.println("获取第" + index + "n项婓波拉契数列:" + i);
		log.info("获取第{}n项婓波拉契数列:{}", index, i);
	}
	
	/**
	 * 获取第n项婓波拉契数列
	 * @param index
	 */
	public static int febonacci(int index){
		if(index==1 || index==2){
			return 1;
		}else{
			//前一个数字 + 后一个数字 == 最新的数字
			return febonacci(index-1) + febonacci(index-2); 
		}
		
	}
}
