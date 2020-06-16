package com.github.eriksen.hello_netty.webdemo;

public class SecondServlet extends GPServlet {
  
  public void doGet(GPRequest request, GPResponse response) throws Exception {
    this.doPost(request, response);
  }
  
  public void doPost(GPRequest request, GPResponse response) throws Exception {
    response.write("Second Servlet");
  }
}
