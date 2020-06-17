package com.github.eriksen.hello_netty.simplerpc.api;

public interface IRpcService {
  public int add(int a, int b);
  
  public int sub(int a, int b);
  
  public int multi(int a, int b);
  
  public int div(int a, int b);
}
