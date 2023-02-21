package com.security.thread.singletion;

/**
 * 单例枚举（每个枚举实例都是static final类型的，也就表明只能被实例化一次。在调用构造方法时，我们的单例被实例化；
 * 也就是说，因为enum中的实例被保证只会被实例化一次，所以我们的INSTANCE也被保证实例化一次）
 * 默认枚举实例的创建是线程安全的，并且在任何情况下都是单例。实际上枚举类隐藏了私有的构造器, 枚举类的域 是相应类型的一个实例对象
 *
 * @author fuhx
 */
public enum SingletonEnum {
    /**
     *
     */
    INSTANCE;

    /**
     * 可以省略此方法，通过Singleton.INSTANCE进行操作
     *
     * @return 单例
     */
    public static SingletonEnum getInstance() {
        return SingletonEnum.INSTANCE;
    }

    /**
     * 最终形态枚举访问
     */
    public void doSomething() {
        System.out.println("doSomething");
    }
}