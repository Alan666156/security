package com.security.io.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.Charset;

/**
 * NettyServerHandle，负责具体的业务逻辑
 *
 * @Author: fuhongxing
 * @Date: 2021/3/16
 **/
public class NettyClientHandle extends ChannelInboundHandlerAdapter {

    /**
     * 客户端连接服务器完成就会触发
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端连接服务器完成" + ctx.getClass().getName());
        //写入消息
//        ByteBuf buf = Unpooled.copiedBuffer("hello".getBytes(StandardCharsets.UTF_8));
//        ctx.writeAndFlush(buf);
        for (int i = 0; i < 1000; i++) {
            ByteBuf buffer = getByteBuf(ctx);
            ctx.channel().writeAndFlush(buffer);
        }
//        ByteBuf buf = Unpooled.buffer();
//        super.channelActive(ctx);
    }
    private ByteBuf getByteBuf(ChannelHandlerContext ctx) {
        byte[] bytes = "客户端数据写入服务端".getBytes(Charset.forName("utf-8"));
        ByteBuf buffer = ctx.alloc().buffer();
        buffer.writeBytes(bytes);
        return buffer;
    }
    /**
     * 当通道有读取事件发生时触发，即服务端发送数据给客户端
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf)msg;
        System.out.println("收到服务器消息：" + buf.toString());
        super.channelRead(ctx, msg);
    }

}
