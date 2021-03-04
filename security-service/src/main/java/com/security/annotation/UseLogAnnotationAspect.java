package com.security.annotation;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/** 
 * 日志切面类 
 */
@Slf4j
@Component
@Aspect  //定义切面类  
public class UseLogAnnotationAspect {  
	

    //定义切入点，提供一个方法，这个方法的名字就是改切入点的id  
	@Pointcut("@annotation(com.security.annotation.UseLog)")
    //@Pointcut("execution(* com.jl.service.*.*(..))")  
    private void allMethod(){}
    
    //针对指定的切入点表达式选择的切入点应用前置通知  
//    @Before("execution(* com.jl.service.*.*(..))")  
	@AfterReturning("allMethod()") 
    public void before(JoinPoint call) {  
        String className = call.getTarget().getClass().getName();  
        String methodName = call.getSignature().getName();
        log.info("【注解-前置通知】:{},类的:{} 方法开始了", className, methodName);
    }  
    
    /**
     * 访问命名切入点来应用后置通知  
     * @param call
     */
    @AfterReturning("allMethod()")  
    public void afterReturn(JoinPoint call) {  
    	log.info("【注解-后置通知】:方法正常结束了");
    	log.info("remork: {}", getRemark(call));
    }  
    
    /**
     * 应用最终通知  
     */
    @After("allMethod()")  
    public void after(){  
    	log.info("【注解-最终通知】:不管方法有没有正常执行完成," + "一定会返回的");
    }  
    
    /**
     * 应用异常抛出后通知  
     */
    @AfterThrowing("allMethod()")  
    public void afterThrowing() { 
    	log.error("【注解-异常抛出后通知】:方法执行时出异常了");
    }  
    
    /**
     * 应用周围通知  
     * @param call
     * @return
     * @throws Throwable
     */
    //@Around("allMethod()")  
    public Object doAround(ProceedingJoinPoint call) throws Throwable{  
        Object result = null;  
        this.before(call);//相当于前置通知  
        try {  
            result = call.proceed();  
//            this.afterReturn(); //相当于后置通知  
        } catch (Throwable e) {  
        	log.error("around环绕通知异常...");
            this.afterThrowing();  //相当于异常抛出后通知  
            throw e;  
        }finally{  
            this.after();  //相当于最终通知  
        }  
        return result;  
    }
    /**
	 * 
	 * @description 获取备注
	 * @param jp 连接点
	 * @return 备注
	 * @author Alan.Fu
	 */
	public String getRemark(JoinPoint jp) {
		UseLog useLog = getAnnotation(jp);
		if (useLog != null) {
			return useLog.remark();
		}
		return "";
	}
	
	private UseLog getAnnotation(JoinPoint jp) {
		MethodSignature signature = (MethodSignature) jp.getSignature();
		Method method = signature.getMethod();
		UseLog useLog = method.getAnnotation(UseLog.class);
		return useLog;
	}
}  