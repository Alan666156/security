package com.security.io.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.IOException;

/**
 * @Author: fuhongxing
 * @Date: 2021/3/16
 **/
public class NettyClient {
    public static void main(String[] args) throws IOException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            //创建服务端启动对象
            Bootstrap bootstrap = new Bootstrap();
            //创建两个线程组bossGroup和workerGroup，含有的子线程NioEventLoopGroup的个数默认为cpu核数的两倍
            //bossGroup只是处理链接请求，真正和客户端业务处理会交给workerGroup完成
            bootstrap.group(group)
                    //使用NioSocketChannel作为客户端的通道实现
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer() {
                @Override
                protected void initChannel(Channel channel) throws Exception {

                    /**
                     * HttpRequestDecoder，用于解码request
                     * HttpResponseEncoder，用于编码response
                     * aggregator，消息聚合器（重要）。为什么能有FullHttpRequest这个东西，就是因为有他，HttpObjectAggregator，如果没有他，就不会有那个消息是FullHttpRequest的那段Channel，同样也不会有FullHttpResponse。
                     * 如果我们将z'h
                     * HttpObjectAggregator(512 * 1024)的参数含义是消息合并的数据大小，如此代表聚合的消息内容长度不超过512kb。
                     * 添加我们自己的业务处理接口
                     */
                    channel.pipeline()
//                            .addLast("decoder", new HttpRequestDecoder())
//                            .addLast("encoder", new HttpResponseEncoder())
//                            .addLast("aggregator", new HttpObjectAggregator(512 * 1024))
                            .addLast("handler", new NettyClientHandle());
                }
            });
            System.out.println("netty client start...");
            //启动客户端去连接服务器
            ChannelFuture cf = bootstrap.connect("127.0.0.1", 9000).sync();
            //对通道关闭进行监听
            cf.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }
}
