package com.github.eriksen.echo;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.net.InetSocketAddress;

public class Client {
  private static final String host = "localhost";
  private static final int port = 8090;

  public void start() throws InterruptedException {
    EventLoopGroup group = new NioEventLoopGroup();
    try {
      Bootstrap b = new Bootstrap();
      b.group(group)
        .channel(NioSocketChannel.class)
        .remoteAddress(new InetSocketAddress(host, port))
        .handler(new LoggingHandler(LogLevel.INFO))
        .handler(new ChannelInitializer<SocketChannel>() {
          protected void initChannel(SocketChannel ch) throws Exception {
            ch.pipeline().addLast(new ClientChannelHandler());
          }
        });

      ChannelFuture future = b.connect().sync();
      future.channel().closeFuture().sync();
    } finally {
      group.shutdownGracefully();
    }
  }

  public static void main(String[] args) throws InterruptedException {
    new Client().start();
  }
}
