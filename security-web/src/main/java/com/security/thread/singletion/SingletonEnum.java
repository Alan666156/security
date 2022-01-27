package com.security.thread.singletion;

/**
 * 单例枚举
 * 默认枚举实例的创建是线程安全的，并且在任何情况下都是单例。实际上枚举类隐藏了私有的构造器, 枚举类的域 是相应类型的一个实例对象
 * @author fuhx
 */
public enum SingletonEnum {
    INSTANCE;

    /**
     * 可以省略此方法，通过Singleton.INSTANCE进行操作
     * @return 单例
     */
    public static SingletonEnum getInstance() {
        return SingletonEnum.INSTANCE;
    }
}