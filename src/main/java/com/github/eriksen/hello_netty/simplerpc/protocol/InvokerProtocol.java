package com.github.eriksen.hello_netty.simplerpc.protocol;

import lombok.Data;

import java.io.Serializable;

@Data
public class InvokerProtocol implements Serializable {
  private String className;
  private String methodName;
  private Class<?>[] params;
  private Object[] values;
}
