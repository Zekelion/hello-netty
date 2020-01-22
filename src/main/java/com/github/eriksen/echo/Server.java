package com.github.eriksen.echo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.Data;

import java.net.InetSocketAddress;

@Data
public class Server {
  private final int port = 8090;

  public void start() throws InterruptedException {
    final ServerChannelHandler serverChannelHandler = new ServerChannelHandler();
    EventLoopGroup group = new NioEventLoopGroup();

    try {
      ServerBootstrap b = new ServerBootstrap();
      b.group(group)
        .channel(NioServerSocketChannel.class) // 指定使用的NIO传输Channel
        .localAddress(new InetSocketAddress(port))
        .handler(new LoggingHandler(LogLevel.INFO))
        .childHandler(new ChannelInitializer<SocketChannel>() { // 添加handler到子channel的pipeline
          protected void initChannel(SocketChannel ch) throws Exception {
            ch.pipeline().addLast(serverChannelHandler);
          }
        });

      ChannelFuture future = b.bind().sync();
      future.channel().closeFuture().sync();
    } finally {
      group.shutdownGracefully();
    }
  }

  public static void main(String[] args) throws InterruptedException {
    new Server().start();
  }
}
