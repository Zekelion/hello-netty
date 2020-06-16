package com.github.eriksen.hello_netty.webdemo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Slf4j
public class GPWebDemo {
  private final int port = 8080;
  private final Map<String, GPServlet> servletMap = new HashMap<>();
  private final Properties properties = new Properties();
  
  private void init() {
    try {
      String WEB_INF = GPWebDemo.class.getResource("/").getPath();
      FileInputStream inputStream = new FileInputStream(WEB_INF + "web.properties");
      properties.load(inputStream);
      
      for (Object k : properties.keySet()) {
        String key = k.toString();
        if (key.endsWith(".url")) {
          String servletName = key.replaceAll("\\.url$", "");
          String url = properties.getProperty(key);
          String className = properties.getProperty(servletName + ".className");
          GPServlet obj = (GPServlet) Class.forName(className).newInstance();
          servletMap.put(url, obj);
          log.info("load servlet {}", className);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  public void start() {
    init();
    
    EventLoopGroup bossGroup = new NioEventLoopGroup();
    EventLoopGroup workerGroup = new NioEventLoopGroup();
    
    try {
      ServerBootstrap server = new ServerBootstrap();
      server.group(bossGroup, workerGroup)
              .channel(NioServerSocketChannel.class)
              .childHandler(new ChannelInitializer<SocketChannel>() {
                protected void initChannel(SocketChannel socketChannel) {
                  socketChannel.pipeline()
                          .addLast(new HttpResponseEncoder())
                          .addLast(new HttpRequestDecoder())
                          .addLast(new GPWebHandler(servletMap));
                }
              })
              .option(ChannelOption.SO_BACKLOG, 128)
              .childOption(ChannelOption.SO_KEEPALIVE, true);
  
      ChannelFuture future = server.bind(port).sync();
      log.info("Start---");
      future.channel().closeFuture().sync();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
  
  public static void main(String[] args) {
    new GPWebDemo().start();
  }
}
