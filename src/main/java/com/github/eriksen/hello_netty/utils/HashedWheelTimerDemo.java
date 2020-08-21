package com.github.eriksen.hello_netty.utils;

import io.netty.util.HashedWheelTimer;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
public class HashedWheelTimerDemo {
  public static void main(String[] args) {
    HashedWheelTimer timer = new HashedWheelTimer(Executors.defaultThreadFactory(), 100, TimeUnit.MILLISECONDS, 32);
    
    timer.newTimeout(timeout -> log.debug("haha"), 100, TimeUnit.MILLISECONDS);
  }
}
