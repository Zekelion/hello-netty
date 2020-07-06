package com.github.eriksen.hello_netty.utils;

import io.netty.util.Recycler;
import lombok.Data;

public class RecyclerDemo {
  private static final Recycler<User> RECYCLER = new Recycler<User>() {
    @Override
    protected User newObject(Handle<User> handle) {
      return new User(handle);
    }
  };
  
  @Data
  static class User {
    private final Recycler.Handle<User> handle;
    
    public void recycle() {
      handle.recycle(this);
    }
  }
  
  public static void main(String[] args) {
    User user1 = RECYCLER.get();
    user1.recycle();
    User user2 = RECYCLER.get();
    user2.recycle();
    
    System.out.println(user1 == user2);
  }
}
