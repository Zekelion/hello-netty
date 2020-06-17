package com.github.eriksen.hello_netty.simplerpc.proxy;

import com.github.eriksen.hello_netty.simplerpc.protocol.InvokerProtocol;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class RpcProxy {
  @SuppressWarnings("unchecked")
  public static <T> T create(Class<?> clazz) {
    MethodProxy proxy = new MethodProxy(clazz);
    Class<?>[] interfaces = clazz.isInterface() ? new Class[]{clazz} : clazz.getInterfaces();
    return (T) Proxy.newProxyInstance(clazz.getClassLoader(), interfaces, proxy);
  }
  
  private static class MethodProxy implements InvocationHandler {
    private final Class<?> clazz;
    
    public MethodProxy(Class<?> clazz) {
      this.clazz = clazz;
    }
    
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
      if (Object.class.equals(method.getDeclaringClass())) {
        return method.invoke(this, args);
      }
      
      return rpcInvoke(proxy, method, args);
    }
    
    public Object rpcInvoke(Object proxy, Method method, Object[] args) {
      InvokerProtocol msg = new InvokerProtocol();
      msg.setClassName(this.clazz.getName());
      msg.setMethodName(method.getName());
      msg.setParams(method.getParameterTypes());
      msg.setValues(args);
      
      final RpcProxyHandler consumerHandler = new RpcProxyHandler();
      EventLoopGroup group = new NioEventLoopGroup();
      try {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                  @Override
                  protected void initChannel(SocketChannel socketChannel) throws Exception {
                    ChannelPipeline pipeline = socketChannel.pipeline();
                    
                    pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4))
                            .addLast("frameEncoder", new LengthFieldPrepender(4))
                            .addLast("encoder", new ObjectEncoder())
                            .addLast("decoder", new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)))
                            .addLast("handler", consumerHandler);
                  }
                });
        
        ChannelFuture future = bootstrap.connect("localhost", 8088).sync();
        future.channel().writeAndFlush(msg).sync();
        future.channel().closeFuture().sync();
      } catch (InterruptedException e) {
        e.printStackTrace();
      } finally {
        group.shutdownGracefully();
      }
      
      return consumerHandler.getResponse();
    }
  }
}
