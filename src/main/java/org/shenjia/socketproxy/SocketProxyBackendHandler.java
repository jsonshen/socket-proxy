package org.shenjia.socketproxy;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundByteHandlerAdapter;

public class SocketProxyBackendHandler extends ChannelInboundByteHandlerAdapter {

    private final Channel inboundChannel;

    public SocketProxyBackendHandler(Channel inboundChannel) {
        this.inboundChannel = inboundChannel;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.read();
        ctx.flush();
    }

    @Override
    public void inboundBufferUpdated(final ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        ByteBuf out = inboundChannel.outboundByteBuffer();
        out.writeBytes(in);
        inboundChannel.flush().addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    ctx.channel().read();
                } else {
                    future.channel().close();
                }
            }
        });
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        SocketProxyFrontendHandler.closeOnFlush(inboundChannel);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        SocketProxyFrontendHandler.closeOnFlush(ctx.channel());
    }
}
