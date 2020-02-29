package com.github.eriksen.hello_netty.embedded;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class FixedLenFrameDecoder extends ByteToMessageDecoder {
  private final int frameLen;

  public FixedLenFrameDecoder(int frameLen) {
    this.frameLen = frameLen;
  }

  protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
    while (internalBuffer().readableBytes() >= frameLen) {
      ByteBuf buf = internalBuffer().readBytes(frameLen);
      list.add(buf);
      System.out.println("Data Decoded");
    }
  }
}
