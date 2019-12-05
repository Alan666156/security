package com.security.datastructure;
/**
 * 汉诺塔问题(递归实现)
 * https://blog.csdn.net/yangming2466/article/details/82915824
 * @author Alan.Fu
 *
 */
public class TestHanoi {
//	假设有三个圆片，最初从小到大放在A棒，移动之后，要从小到大依次放在C棒，圆片根据小到大命名：1,2,3
//	第一步，将1从A棒移动到C棒
//
//	第二步，将2从A棒移动到B棒
//
//	第三步，将1从C棒移动到B棒
//
//	第四步，将3从A棒移动到C棒
//
//	第五步，将1从B棒移动到A棒
//
//	第六步，将2从B棒移动到C棒
//
//	第七步，将1从A棒移动到C棒
//	此时，圆盘1,2,3按从小到大依次排列依次在C棒上，移动的过程中没有违背规则：不管在哪根棒上，小片必须在大片上面
	public static void main(String[] args) {
		hanoi(5, "A", "B","C");
	}
	
	/**
	 * 无论有多少个盘子，都认为只有两个，上面 的所有盘子和最下面一个盘子
	 * @param n 汉诺塔的层数
	 * @param from 承载最初圆盘的柱子
	 * @param in 起到中转作用的柱子
	 * @param to 移动到的目标柱子
	 */
	public static void hanoi(int n, String from, String in, String to){
		if(n < 0){
			return;
		}
		//如果只有1个盘子
		if(n == 1){
			System.out.println("第1个盘子从"+ from + "移到"+ to);
		}else{//无论有多少个盘子，都认为只有2个；上面的所有盘子和最下面一个盘子
			//移动上面所有的盘子到中间位置
			hanoi(n-1, from, to, in);
			System.out.println("第"+n+"个盘子从"+ from + "移到"+ to);
			//移动下面的盘子
			hanoi(n-1, in, from, to);

		}
		
	}
}
