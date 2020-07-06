package com.github.eriksen.hello_netty.utils;

import io.netty.util.concurrent.FastThreadLocal;

public class FastThreadLocalDemo {
  ThreadLocal<Integer> counter = new ThreadLocal<>();
  FastThreadLocal<Integer> fastCounter = new FastThreadLocal<>();
  
  public static void main(String[] args) {
    FastThreadLocalDemo demo = new FastThreadLocalDemo();
    new Thread(() -> {
      for (int i = 1; i < 10; i++) {
        demo.counter.set(i);
        System.out.println(demo.counter.get() + " : " + i);
        
        demo.fastCounter.set(i);
        System.out.println("fast " + demo.fastCounter.get() + " : " + i);
        try {
          Thread.sleep(200);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }).start();
    
    new Thread(() -> {
      for (int i = 1; i < 10; i++) {
        System.out.println(demo.counter.get() + " : " + i);
        System.out.println("fast " + demo.fastCounter.get() + " : " + i);
        try {
          Thread.sleep(300);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }).start();
  }
}
