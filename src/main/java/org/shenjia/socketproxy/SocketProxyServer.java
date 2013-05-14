package org.shenjia.socketproxy;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class SocketProxyServer {

    private final int localPort;
    private final String remoteHost;
    private final int remotePort;

    public SocketProxyServer(int localPort, String remoteHost, int remotePort) {
        this.localPort = localPort;
        this.remoteHost = remoteHost;
        this.remotePort = remotePort;
    }

    public void run() throws Exception {
        System.err.println(
                "Proxying *:" + localPort + " to " +
                remoteHost + ':' + remotePort + " ...");

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
        	new ServerBootstrap().group(bossGroup, workerGroup)
             .channel(NioServerSocketChannel.class)
             .childHandler(new SocketProxyInitializer(remoteHost, remotePort))
             .childOption(ChannelOption.AUTO_READ, false)
             .bind(localPort).sync().channel().closeFuture().sync();
        } finally {
        	workerGroup.shutdownGracefully();
        	bossGroup.shutdownGracefully();
        }
    }
    
    public static void main(String[] args) throws Exception{
    	System.setProperty("io.netty.noJavassist", "true");
    	int localPort = Integer.parseInt(System.getProperty("listen", "8888"));
    	String remoteHost = System.getProperty("host");
    	int remotePort = Integer.parseInt(System.getProperty("port"));
    	SocketProxyServer sps = new SocketProxyServer(localPort, remoteHost, remotePort);
    	sps.run();
    }
}
