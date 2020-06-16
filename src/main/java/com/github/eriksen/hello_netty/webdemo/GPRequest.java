package com.github.eriksen.hello_netty.webdemo;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;

import java.util.List;
import java.util.Map;

public class GPRequest {
  private final ChannelHandlerContext ctx;
  private final HttpRequest request;
  
  public GPRequest(ChannelHandlerContext ctx, HttpRequest request) {
    this.ctx = ctx;
    this.request = request;
  }
  
  public String getUrl() {
    return request.uri();
  }
  
  public String getMethod() {
    return request.method().toString();
  }
  
  public ChannelHandlerContext getCtx() {
    return ctx;
  }
  
  public Map<String, List<String>> getParams() {
    QueryStringDecoder decoder = new QueryStringDecoder(request.uri());
    return decoder.parameters();
  }
  
  public String getParameter(String name) {
    Map<String, List<String>> map = getParams();
    List<String> list = map.get(name);
    if (list == null) return null;
    
    return list.get(0);
  }
}
