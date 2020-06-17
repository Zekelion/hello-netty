package com.github.eriksen.hello_netty.simplerpc.registry;

import com.github.eriksen.hello_netty.simplerpc.protocol.InvokerProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class RegistryHandler extends ChannelInboundHandlerAdapter {
  public static ConcurrentHashMap<String, Object> registryMap = new ConcurrentHashMap<>();
  private final List<String> classNames = new ArrayList<>();
  
  public RegistryHandler() {
    scanClass("com.github.eriksen.hello_netty.simplerpc.provider");
    doRegistry();
  }
  
  private void scanClass(String packageName) {
    URL url = this.getClass().getResource("/" + packageName.replaceAll("\\.", "/"));
    File dir = new File(url.getFile());
    
    for (File file : Objects.requireNonNull(dir.listFiles())) {
      if (file.isDirectory()) scanClass(packageName + "." + file.getName());
      else {
        classNames.add(packageName + "." + file.getName().replace(".class", "").trim());
      }
    }
  }
  
  private void doRegistry() {
    for (String className : classNames) {
      try {
        Class<?> clazz = Class.forName(className);
        Class<?> _interface = clazz.getInterfaces()[0];
        
        registryMap.put(_interface.getName(), clazz.newInstance());
        log.info("API Registry : {}", _interface.getName());
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
  
  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    InvokerProtocol request = (InvokerProtocol) msg;
    Object result = null;
    
    if (registryMap.containsKey(request.getClassName())) {
      Object clazz = registryMap.get(request.getClassName());
      Method method = clazz.getClass().getMethod(request.getMethodName(), request.getParams());
      
      result = method.invoke(clazz, request.getValues());
    }
    
    ctx.write(result);
    ctx.flush();
    ctx.close();
  }
  
  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    cause.printStackTrace();
    ctx.close();
  }
}
