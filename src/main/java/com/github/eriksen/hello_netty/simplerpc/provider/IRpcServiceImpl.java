package com.github.eriksen.hello_netty.simplerpc.provider;

import com.github.eriksen.hello_netty.simplerpc.api.IRpcService;

public class IRpcServiceImpl implements IRpcService {
  @Override
  public int add(int a, int b) {
    return a + b;
  }
  
  @Override
  public int sub(int a, int b) {
    return a - b;
  }
  
  @Override
  public int multi(int a, int b) {
    return a * b;
  }
  
  @Override
  public int div(int a, int b) {
    return a / b;
  }
}
