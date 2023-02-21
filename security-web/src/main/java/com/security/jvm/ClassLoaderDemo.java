package com.security.jvm;

import com.sun.crypto.provider.DESKeyFactory;

/**
 * 类加载器
 * @author fuhongxing
 */
public class ClassLoaderDemo {

    public static void main(String[] args) {
        //获取系统类加载器sun.misc.Launcher$AppClassLoader
        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
        System.out.println("系统类加载器：" + systemClassLoader);

        //获取其上层：扩展类加载器
        ClassLoader extClassLoader = systemClassLoader.getParent();
        System.out.println("扩展类加载器：" + systemClassLoader);

        //根类加载器:获取不到引导类加载器
        ClassLoader bootstarpClassLoader = extClassLoader.getParent();
        System.out.println("根类加载器：" + bootstarpClassLoader);

        //String类使用的根类加载器进行加载的，所以这里获取不到
        System.out.println(String.class.getClassLoader());
        //扩展类加载器: sun.misc.Launcher$ExtClassLoader
        //从 java.ext.dirs 系统属性所指定的目录中加载类库，或从 JDK 的安装目录的 jre/lib/ext 子目录下加载类库
        System.out.println("扩展类加载器" + DESKeyFactory.class.getClassLoader().getClass().getName());
        System.out.println("自定义类加载器:" + ClassLoaderDemo.class.getClassLoader().getClass().getName());


    }

}
