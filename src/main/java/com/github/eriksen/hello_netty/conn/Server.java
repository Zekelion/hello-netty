package com.github.eriksen.hello_netty.conn;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Server {
  public final static int BEGIN_PORT = 8000;
  public final static int END_PORT = 8100;
  
  public static void main(String[] args) {
    log.info("Start...");
    
    EventLoopGroup bossGroup = new NioEventLoopGroup();
    EventLoopGroup workerGroup = new NioEventLoopGroup();
    
    ServerBootstrap bootstrap = new ServerBootstrap();
    bootstrap.group(bossGroup, workerGroup)
            .channel(NioServerSocketChannel.class)
            .childOption(ChannelOption.SO_REUSEADDR, true)
            .childHandler(new ConnCountHandler());
    
    for (int i = BEGIN_PORT; i <= END_PORT; i++) {
      final int port = i;
      bootstrap.bind(i).addListener((ChannelFutureListener) future -> log.info("Bind port {}", port));
    }
  }
  
  
}
