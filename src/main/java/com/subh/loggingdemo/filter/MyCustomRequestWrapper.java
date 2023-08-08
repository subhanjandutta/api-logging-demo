package com.subh.loggingdemo.filter;

import org.springframework.util.StreamUtils;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;

public class MyCustomRequestWrapper extends HttpServletRequestWrapper {

  private final byte[] requestBody;
  public MyCustomRequestWrapper(HttpServletRequest request) throws IOException {
    super(request);
    this.requestBody = StreamUtils.copyToByteArray(request.getInputStream());
  }

  @Override
  public ServletInputStream getInputStream() {
    return new ServletInputStreamWrapper(this.requestBody);
  }
}
