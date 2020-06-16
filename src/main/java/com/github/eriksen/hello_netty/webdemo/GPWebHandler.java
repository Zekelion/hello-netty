package com.github.eriksen.hello_netty.webdemo;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpRequest;

import java.util.Map;

public class GPWebHandler extends ChannelInboundHandlerAdapter {
  private final Map<String, GPServlet> servletMap;
  
  public GPWebHandler(Map<String, GPServlet> servletMap) {
    this.servletMap = servletMap;
  }
  
  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    if (msg instanceof HttpRequest) {
      HttpRequest request = (HttpRequest) msg;
      GPRequest gpRequest = new GPRequest(ctx, request);
      GPResponse gpResponse = new GPResponse(ctx, request);
      
      String url = gpRequest.getUrl();
      if (servletMap.containsKey(url)) {
        servletMap.get(url).service(gpRequest, gpResponse);
      } else {
        gpResponse.write("404-Not Found");
      }
    }
  }
}
