package com.github.eriksen.hello_netty.embedded;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

import static org.junit.Assert.*;

public class AbsIntegerEncoderTest {
  @Test
  public void testEncoded() {
    ByteBuf buf = Unpooled.buffer();
    for (int i = 1; i < 10; i++) {
      buf.writeInt(i);
    }

    EmbeddedChannel channel = new EmbeddedChannel(new AbsIntegerEncoder());
    assertTrue(channel.writeOutbound(buf));
    assertTrue(channel.finish());

    for (int i = 1; i < 10; i++) {
      assertEquals(i, java.util.Optional.ofNullable(channel.readOutbound()));
    }

    assertNull(channel.readOutbound());
  }
}