package com.security.io.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * bio阻塞模型
 *
 * BIO（Blocking IO）,如何理解这个Blocking呢？
 * 客户端监听（Listen）时，Accept是阻塞的，只有新连接来了，Accept才会返回，主线程才能继
 * 读写socket时，Read是阻塞的，只有请求消息来了，Read才能返回，子线程才能继续处理
 * 读写socket时，Write是阻塞的，只有客户端把消息收了，Write才能返回，子线程才能继续读取下一个请求
 * 传统的BIO模式下，从头到尾的所有线程都是阻塞的，这些线程就干等着，占用系统的资源，什么事也不干。
 *
 * @Author: fuhongxing
 * @Date: 2021/3/16
 **/
public class SockerServer {

    /**
     * 1、创建一个ServerSocket，监听并绑定一个端口
     * 2、一系列客户端来请求这个端口
     * 3、服务器使用Accept，获得一个来自客户端的Socket连接对象
     * 4、启动一个新线程处理连接
     *  4.1、读Socket，得到字节流
     *  4.2、解码协议，得到Http请求对象
     *  4.3、处理Http请求，得到一个结果，封装成一个HttpResponse对象
     *  4.4、编码协议，将结果序列化字节流 写Socket，将字节流发给客户端
     */

    /**
     * 创建线程池
     */
    private static ExecutorService executorService = new ThreadPoolExecutor(
            2,
            5,
            1,
            TimeUnit.SECONDS,
            //指定一种队列 （有界队列）
            new ArrayBlockingQueue<Runnable>(3),
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.AbortPolicy()
    );

    public static void main(String[] args) throws IOException {
        //服务端的主线程是用来循环监听客户端请求
        //创建一个服务端且端口为9000
        ServerSocket server = new ServerSocket(9000);
        Socket client = null;
        //循环监听
        while (true){
            //服务端监听到一个客户端请求，阻塞，如果进来新的客户端才会往下执行
            client = server.accept();
            System.out.println(client.getRemoteSocketAddress() + "地址的客户端连接成功!");
            //将该客户端请求通过线程池放入HandlMsg线程中进行处理
            executorService.submit(new HandleMsg(client));
        }
    }

    /**
     * 一旦有新的客户端请求，创建这个线程进行处理
     */
    static class HandleMsg implements Runnable{
        //创建一个客户端
        Socket client;
        /**
         * 构造传参绑定
         * @param client
         */
        public HandleMsg(Socket client){
            this.client = client;
        }
        @Override
        public void run() {
            //创建字符缓存输入流
            BufferedReader bufferedReader = null;
            //创建字符写入流
            PrintWriter printWriter = null;
            try {
                //获取客户端的输入流
                bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                //获取客户端的输出流，true是随时刷新
                printWriter = new PrintWriter(client.getOutputStream(),true);
                String inputLine = null;
                long start = System.currentTimeMillis();
                while ((inputLine = bufferedReader.readLine())!=null){
                    printWriter.println(inputLine);
                }
                System.out.println("当前线程处理花费了：" + (System.currentTimeMillis() - start) + "毫秒！");
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    bufferedReader.close();
                    printWriter.close();
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
