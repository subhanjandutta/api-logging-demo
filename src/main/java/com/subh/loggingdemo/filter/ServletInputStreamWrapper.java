package com.subh.loggingdemo.filter;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ServletInputStreamWrapper extends ServletInputStream {

  private final InputStream inputStream;

  public ServletInputStreamWrapper(byte[] requestBody) {
    super();
    this.inputStream = new ByteArrayInputStream(requestBody);
  }

  @Override
  public boolean isFinished() {
    try {
      return this.inputStream.available()==0;
    } catch (IOException e) {
      return false;
    }
  }

  @Override
  public boolean isReady() {
    return true;
  }

  @Override
  public void setReadListener(ReadListener readListener) {

  }

  @Override
  public int read() throws IOException {
    return this.inputStream.read();
  }
}
