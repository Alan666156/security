package com.security.proxy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * cglib动态代理
 * 当加入Spring中的target是接口的实现时，就使用JDK动态代理，否是就使用Cglib代理。Spring也可以通过<aop:config proxy-target-class="true">强制使用Cglib代理，使用Java字节码编辑类库ASM操作字节码来实现，
 * 直接以二进制形式动态地生成 stub 类或其他代理类，性能比JDK更强
 *
 * @author fuhongxing
 */
@Slf4j
public class AdminServiceCglibProxy implements MethodInterceptor {

    private Object target;

    public AdminServiceCglibProxy(Object target) {
        this.target = target;
    }

    /**
     * 给目标对象创建一个代理对象
     */
    public Object getProxyInstance() {
        //工具类
        Enhancer en = new Enhancer();
        //设置父类
        en.setSuperclass(target.getClass());
        //设置回调函数
        en.setCallback(this);
        //创建子类代理对象
        return en.create();
    }

    @Override
    public Object intercept(Object object, Method method, Object[] arg2, MethodProxy proxy) throws Throwable {
        log.info("判断用户是否有权限进行操作");
        Object obj = method.invoke(target);
        log.info("记录用户执行操作的用户信息、更改内容和时间等");
        return obj;
    }


}