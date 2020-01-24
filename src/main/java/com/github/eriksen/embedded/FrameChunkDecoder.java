package com.github.eriksen.embedded;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.TooLongFrameException;

import java.util.List;

public class FrameChunkDecoder extends ByteToMessageDecoder {
  private final int maxSize;

  public FrameChunkDecoder(int maxSize) {
    this.maxSize = maxSize;
  }

  protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
    int readableBytes = byteBuf.readableBytes();
    if (readableBytes > maxSize) {
      byteBuf.clear();
      throw new TooLongFrameException();
    }

    list.add(byteBuf.readBytes(readableBytes));
  }
}
