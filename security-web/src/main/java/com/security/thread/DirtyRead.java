package com.security.thread;
/**
 * 脏读
 * 业务整体需要使用完整的synchronized，保持业务的原子性。
 * @author alienware
 *
 */
public class DirtyRead {

	private String username = "test";
	private String password = "123";
	
	public synchronized void setValue(String username, String password){
		this.username = username;
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		this.password = password;
		
		System.out.println("setValue最终结果：username = " + username + " , password = " + password);
	}
	
	/** 需要加synchronized,保证数据的完整性 */
	public void getValue(){
		System.out.println("getValue方法得到：username = " + this.username + " , password = " + this.password);
	}
	
	
	public static void main(String[] args) throws Exception{
		
		final DirtyRead dr = new DirtyRead();
		//线程修改赋值，休眠了2秒，main线程执行了getvalue出现了脏读
		new Thread(() -> dr.setValue("z3", "456")).start();
		Thread.sleep(1000);
		dr.getValue();
	}
	
	
	
}
