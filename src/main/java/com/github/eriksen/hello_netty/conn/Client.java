package com.github.eriksen.hello_netty.conn;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;

@Slf4j
public class Client {
  private static final String host = "127.0.0.1";
  public final static int BEGIN_PORT = 8000;
  public final static int END_PORT = 8100;
  
  public static void main(String[] args) {
    log.info("Start...");
    
    EventLoopGroup group = new NioEventLoopGroup();
    Bootstrap bootstrap = new Bootstrap();
    
    bootstrap.group(group)
            .channel(NioSocketChannel.class)
            .option(ChannelOption.SO_REUSEADDR, true)
            .handler(new ChannelInitializer<SocketChannel>() {
              @Override
              protected void initChannel(SocketChannel ch) throws Exception {
              }
            });
    
    int index = 0;
    int port;
    while (!Thread.interrupted()) {
      port = BEGIN_PORT + index;
      
      try {
        ChannelFuture future = bootstrap.connect(host, port);
        future.addListener((ChannelFutureListener) future1 -> {
          if (!future1.isSuccess()) {
            log.error("Conn failed");
            System.exit(0);
          }
        });
        
        future.get();
      } catch (InterruptedException | ExecutionException e) {
        e.printStackTrace();
      }
      
      if (port == END_PORT) index = 0;
      else index++;
    }
  }
}
