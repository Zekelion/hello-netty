package com.github.eriksen.hello_netty.webdemo;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;

import java.io.UnsupportedEncodingException;

public class GPResponse {
  private final ChannelHandlerContext ctx;
  private final HttpRequest request;
  
  public GPResponse(ChannelHandlerContext ctx, HttpRequest request) {
    this.ctx = ctx;
    this.request = request;
  }
  
  public void write(String out) throws UnsupportedEncodingException {
    try {
      if (out == null || out.length() == 0) return;
      
      FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer(out.getBytes("utf-8")));
      response.headers().set("Content-type", "text/html");
      
      ctx.write(response);
    } finally {
      ctx.flush();
      ctx.close();
    }
  }
}
