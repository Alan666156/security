package com.security.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * NIO 同时支持阻塞与非阻塞模式
 * NIO基于Reactor，当socket有流可读或可写入socket时，操作系统会相应的通知引用程序进行处理，应用再将流读取到缓冲区或写入操作系统
 * 三大核心Channel(通道)，Buffer(缓冲区)，Selector(选择器)；数据总是从通道读取到缓冲区中，或者从缓冲区写入到通道中，Selector用于监听多个通道的事件。
 * 可以自定义协议,比如redis底层也是大量采用nio，dubbo也是等等
 *
 * 流程:
 * 1、多个Client同时注册到多路复用器selector上；
 * 2、selector遍历所有注册的通道；
 * 3、查看通道状态（包括Connect、Accept、Read、Write）；
 * 4、根据状态执行相应状态的操作；
 * @Author: fuhongxing
 * @Date: 2021/3/16
 **/
public class NioSelectServer {
    private static List<SocketChannel> channelList = new ArrayList<>();
    public static void main(String[] args) throws IOException {
        //创建NIO
        ServerSocketChannel sockerServer = ServerSocketChannel.open();
        sockerServer.socket().bind(new InetSocketAddress(9000));
        //设置为非阻塞
        sockerServer.configureBlocking(false);
        //打开selector处理channel，即创建epoll
        Selector selector = Selector.open();
        /**
         * SelectionKey注册到selector上，SelectionKey是通道和选择器交互的核心组件
         * 虽然已经有了ACCEPT事件的KEY，但select()默认并不会去调用
         * 而是要等待有其它感兴趣事件被select()捕获之后，才会去调用ACCEPT的SelectionKey这时候ServerSocketChannel才开始进行循环监听
         */
        SelectionKey register = sockerServer.register(selector, SelectionKey.OP_ACCEPT);

        System.out.println("NIO 服务启动成功");
        while (true){
            //阻塞等待需要处理的事件发生
            selector.select();
            //获取selector中注册的全部事件的 selectedKey
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()){
                try {
                    SelectionKey key = iterator.next();
                    //如果OP_ACCEPT事件，则进行连接获取和事件注册
                    if(key.isAcceptable()){
                        ServerSocketChannel server = (ServerSocketChannel) key.channel();
                        SocketChannel accept = server.accept();
                        accept.configureBlocking(false);
                        //这里如果只注册了读事件，如果需要客户端发送数据可以注册写事件
                        SelectionKey selectionKey = accept.register(selector, SelectionKey.OP_READ);
                        System.out.println("客户端连接成功");
                    }else if (key.isReadable()){
                        SocketChannel server = (SocketChannel) key.channel();
                        ByteBuffer byteBuffer = ByteBuffer.allocate(128);
                        int len = server.read(byteBuffer);
                        if (len > 0) {
                            System.out.println("接收到的数据：" + new String(byteBuffer.array(), "utf-8"));
                        }else if (len == -1){
                            server.close();
                            System.out.println("客户端断开链接");
                        }
                    }
                    //从事件集合中删除本次处理的key，防止下次selector重复处理
                    iterator.remove();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
//            channelList.stream().forEach(entry -> {
//                ByteBuffer byteBuffer = ByteBuffer.allocate(128);
//                try {
//                    //非阻塞模式read方法不会阻塞，否则会阻塞
//                    int len = entry.read(byteBuffer);
//                    if (len > 0) {
//                        System.out.println("接收到的数据：" + new String(byteBuffer.array()));
//                    }else{
//                        entry.close();
//                        System.out.println("客户端断开链接");
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            });
        }
    }

    public static void handler(Socket socket) throws IOException {
        byte[] bytes = new byte[1024];
        System.out.println("准备读取数据");
        int read = socket.getInputStream().read(bytes);
        System.out.println("数据读取结束");
        if(read != 1){
            System.out.println("接收到客户端的数据: " + new String(bytes, 0, read));
        }
    }
}
