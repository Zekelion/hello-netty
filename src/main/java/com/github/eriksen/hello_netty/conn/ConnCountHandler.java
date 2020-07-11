package com.github.eriksen.hello_netty.conn;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@ChannelHandler.Sharable
public class ConnCountHandler extends ChannelInboundHandlerAdapter {
  private final AtomicInteger counter = new AtomicInteger();
  
  public ConnCountHandler() {
    Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
      log.info("Conn count {}", counter.get());
    }, 0, 2, TimeUnit.SECONDS);
  }
  
  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    counter.incrementAndGet();
  }
  
  @Override
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    counter.decrementAndGet();
  }
}
