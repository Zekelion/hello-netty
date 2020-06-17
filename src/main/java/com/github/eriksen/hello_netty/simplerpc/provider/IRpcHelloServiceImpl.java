package com.github.eriksen.hello_netty.simplerpc.provider;

import com.github.eriksen.hello_netty.simplerpc.api.IRpcHelloService;

public class IRpcHelloServiceImpl implements IRpcHelloService {
  @Override
  public String hello(String name) {
    return "Hello " + name;
  }
}
