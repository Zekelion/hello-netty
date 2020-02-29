package com.github.eriksen.hello_netty.embedded;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

public class AbsIntegerEncoder extends MessageToMessageEncoder<ByteBuf> {

  protected void encode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
    while (byteBuf.readableBytes() >= 4) {
      int value = Math.abs(byteBuf.readInt());
      list.add(value);
    }
  }
}
