package com.github.eriksen.hello_netty.simplerpc.consumer;

import com.github.eriksen.hello_netty.simplerpc.api.IRpcHelloService;
import com.github.eriksen.hello_netty.simplerpc.api.IRpcService;
import com.github.eriksen.hello_netty.simplerpc.proxy.RpcProxy;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RpcConsumer {
  public static void main(String[] args) {
    IRpcHelloService helloSvc = RpcProxy.create(IRpcHelloService.class);
    
    log.info(helloSvc.hello("阿呆"));
    
    IRpcService service = RpcProxy.create(IRpcService.class);
    log.info("Add {}", service.add(1, 1));
    log.info("Sub {}", service.sub(1, 1));
    log.info("Multi {}", service.multi(1, 1));
    log.info("Div {}", service.div(1, 1));
  }
}
