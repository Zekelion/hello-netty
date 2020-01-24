package com.github.eriksen.embedded;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

import static org.junit.Assert.*;

public class FixedLenFrameDecoderTest {
  @Test
  public void testFrameDecoded() {
    ByteBuf buf = Unpooled.buffer();
    for (int i = 0; i < 9; i++) {
      buf.writeByte(i);
    }

    ByteBuf input = buf.duplicate();
    EmbeddedChannel channel = new EmbeddedChannel(new FixedLenFrameDecoder(3));

    // write msg
    assertFalse(channel.writeInbound(input.readBytes(2))); // 不足数据帧长度
    assertTrue(channel.writeInbound(input.readBytes(7)));
    assertTrue(channel.finish());

    // read msg
    ByteBuf read = channel.readInbound();
    assertEquals(buf.readSlice(3), read);
    read.release();

    read = channel.readInbound();
    assertEquals(buf.readSlice(3), read);
    read.release();

    read = channel.readInbound();
    assertEquals(buf.readSlice(3), read);
    read.release();

    assertNull(channel.readInbound());
    buf.release();
  }
}