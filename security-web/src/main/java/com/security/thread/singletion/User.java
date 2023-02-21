package com.security.thread.singletion;

/**
 * 单例枚举特点：
 * 1、构造方法私有化；
 * 2、实例化的变量引用私有化；
 * 3、获取实例的方法共有。
 *
 * @author fuhognxing
 */
public class User {
    /**
     * 私有化构造函数
     */
    private User() {
    }

    public static void main(String[] args) {
        System.out.println(User.getInstance());
        System.out.println(User.getInstance());
        System.out.println(User.getInstance() == User.getInstance());
    }

    /**
     * 定义一个静态枚举类
     */
    enum SingletonEnum {
        /**
         * 创建一个枚举对象，该对象天生为单例
         */
        INSTANCE;
        private User user;

        /**
         * 私有化枚举的构造函数
         */
        SingletonEnum() {
            user = new User();
        }

        public User getInstnce() {
            return user;
        }
    }

    /**
     * 对外暴露一个获取User对象的静态方法
     *
     * @return
     */
    public static User getInstance() {
        return SingletonEnum.INSTANCE.getInstnce();
    }
}


