package com.github.eriksen.hello_netty.simplerpc.registry;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RpcRegistry {
  private final int port;
  
  public RpcRegistry(int port) {
    this.port = port;
  }
  
  public void start() {
    EventLoopGroup bossGroup = new NioEventLoopGroup();
    EventLoopGroup workerGroup = new NioEventLoopGroup();
    
    try {
      ServerBootstrap bootstrap = new ServerBootstrap();
      bootstrap.group(bossGroup, workerGroup)
              .channel(NioServerSocketChannel.class)
              .childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) {
                  ChannelPipeline pipeline = socketChannel.pipeline();
                  
                  /*
                   * maxFrameLength 最大长度
                   * lengthFieldOffset 定长属性偏移量
                   * lengthFieldLength 定长字段长度
                   * lengthAdjustment 添加到长度属性值的补偿值
                   * initialBytesToStrip 从解码帧中取出的第一个字节数
                   * */
                  pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4))
                          .addLast(new LengthFieldPrepender(4))
                          .addLast("encoder", new ObjectEncoder())
                          .addLast("decoder", new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)))
                          .addLast(new RegistryHandler());
                }
              })
              .option(ChannelOption.SO_BACKLOG, 128)
              .childOption(ChannelOption.SO_KEEPALIVE, true);
      
      ChannelFuture future = bootstrap.bind(port).sync();
      log.info("Server start at local:{}", port);
      future.channel().closeFuture().sync();
    } catch (InterruptedException e) {
      e.printStackTrace();
      bossGroup.shutdownGracefully();
      workerGroup.shutdownGracefully();
    }
  }
  
  public static void main(String[] args) {
    new RpcRegistry(8088).start();
  }
}
