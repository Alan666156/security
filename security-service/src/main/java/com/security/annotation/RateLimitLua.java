package com.security.annotation;


import java.lang.annotation.*;

/**
 * lua + redis限流实现
 * @author Alan Fu
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimitLua {
	
	String remark() default "";

}
