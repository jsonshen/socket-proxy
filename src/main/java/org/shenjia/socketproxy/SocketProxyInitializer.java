package org.shenjia.socketproxy;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class SocketProxyInitializer extends ChannelInitializer<SocketChannel> {

    private final String remoteHost;
    private final int remotePort;

    public SocketProxyInitializer(String remoteHost, int remotePort) {
        this.remoteHost = remoteHost;
        this.remotePort = remotePort;
    }

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast(
//                new LoggingHandler(LogLevel.INFO),
                new SocketProxyFrontendHandler(remoteHost, remotePort));
    }
}
