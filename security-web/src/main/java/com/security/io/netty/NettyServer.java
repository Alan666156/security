package com.security.io.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * netty服务
 * @Author: fuhongxing
 * @Date: 2021/3/16
 **/
public class NettyServer {
    /**
     * 端口
     */
    private int port = 9000;
    public NettyServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        new NettyServer(9000).start();
    }

    /**
     * 执行流程:
     * 1、创建ServerBootStrap实例
     * 2、设置并绑定Reactor线程池：EventLoopGroup，EventLoop就是处理所有注册到本线程的Selector上面的Channel
     * 3、设置并绑定服务端的channel
     * 4、创建处理网络事件的ChannelPipeline和handler，网络时间以流的形式在其中流转，handler完成多数的功能定制：比如编解码 SSl安全认证
     * 5、绑定并启动监听端口
     * 6、当轮训到准备就绪的channel后，由Reactor线程：NioEventLoop执行pipline中的方法，最终调度并执行channelHandler
     *
     * @throws Exception
     */
    public void start() throws Exception {
        //1、创建服务端启动对象
        ServerBootstrap bootstrap = new ServerBootstrap();
        //2、创建两个线程组bossGroup和workerGroup，含有的子线程NioEventLoopGroup的个数默认为cpu核数的两倍
        /**
         * NioEventLoopGroup()可以理解为死循环。不断的接受客户端发起的连接，连接进来之后对连接进行处理，紧接着循环继续运行
         * bossGroup只是处理链接请求，真正和客户端业务处理会交给workerGroup完成
         * boosGroup --> 线程组不断的从客户端那边接受连接，但是不对连接做任何的处理，直接传给worker
         * workerGroup --> 线程组接收到boosGroup传过来的连接，真正的完成对连接的处理。例如获取连接的参数，进行实际的业务处理，把结果返回给客户端
         */
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        bootstrap.group(bossGroup, workerGroup)
                //3、使用NioServerSocketChannel作为服务器的通道实现
                .channel(NioServerSocketChannel.class)
                //4、TCP链路建立时创建
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        System.out.println("initChannel ch:" + ch.getClass().getName());
                        /**
                         * HttpRequestDecoder，用于解码request
                         * HttpResponseEncoder，用于编码response
                         * aggregator，消息聚合器（重要）。为什么能有FullHttpRequest这个东西，就是因为有他，HttpObjectAggregator，如果没有他，就不会有那个消息是FullHttpRequest的那段Channel，同样也不会有FullHttpResponse。
                         * HttpObjectAggregator(512 * 1024)的参数含义是消息合并的数据大小，如此代表聚合的消息内容长度不超过512kb。
                         * 添加我们自己的业务处理接口
                         */
                        ch.pipeline()
//                                .addLast("decoder", new HttpRequestDecoder())
//                                .addLast("encoder", new HttpResponseEncoder())
//                                .addLast("aggregator", new HttpObjectAggregator(512 * 1024))
                                .addLast("handler", new NettyServerHandle());
                    }
                })
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, Boolean.TRUE);
        System.out.println("netty server start...");
        bootstrap.bind(port).sync();
    }
}
