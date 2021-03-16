package com.security.io.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.AsciiString;

import java.nio.charset.StandardCharsets;

/**
 * 自定义处理器，负责具体的业务逻辑
 * SimpleChannelInboundHandler<FullHttpRequest>
 * @Author: fuhongxing
 * @Date: 2021/3/16
 **/
public class NettyServerHandle extends ChannelInboundHandlerAdapter {

    private AsciiString contentType = HttpHeaderValues.TEXT_PLAIN;

    /**
     * 读取客户端发送的数据
     * @param ctx 上下文对象，含有通道channel，管道pipeline
     * @param msg 客户端发送的消息
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf)msg;
        System.out.println("收到客户端消息：" + buf.toString());
        super.channelRead(ctx, msg);
    }

    /**
     * 读取客户端发送的数据
     * @param ctx
     * @param msg
     * @throws Exception
     */
//    @Override
//    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
//        //获取通道
//        System.out.println("class:" + msg.getClass().getName());
//        //构建响应
//        DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
//                HttpResponseStatus.OK,
//                Unpooled.wrappedBuffer("test".getBytes()));
//
//        HttpHeaders heads = response.headers();
//        heads.add(HttpHeaderNames.CONTENT_TYPE, contentType + "; charset=UTF-8");
//        heads.add(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
//        heads.add(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
//        //返回给客户端的response对象
//        ctx.writeAndFlush(response);
//    }

    /**
     * 数据读取完毕触发
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("数据读取完毕触发");
        super.channelReadComplete(ctx);
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("exceptionCaught");
        if(null != cause){
            cause.printStackTrace();
        }
        if (null != ctx) {
            ctx.close();
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ByteBuf buf = Unpooled.copiedBuffer("hello client".getBytes(StandardCharsets.UTF_8));
        ctx.writeAndFlush(buf);
    }
}
