package com.subh.loggingdemo.filter;

import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.stream.Collectors;

@Component
public class LoggingFilter implements Filter {

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
          throws IOException, ServletException {
    /*
    Here I have to create my own MyCustomRequestWrapper so that I can override the ServletInputStream getter method.
    Spring has its own ContentCachingRequestWrapper, but it won't cache the request body unless the
    input stream is consumed first time and that will be done at the controller level. Hence, since I want to log
    the request before it reaches the next filter and finally to the controller, I have to use own wrapper or else I
    can only log it after passing the request through filterChain.doFilter, which will be the same time as the
    response is returned.
    Note: InputStream can be consumed only once.
    Therefore, with MyCustomRequestWrapper we can create a behaviour where we can store the input stream in a
    byte array and return that array converted to input stream every tinme the getter for InputStream is invoked
    by creating a new object of the ServletInputStreamWrapper class.
    Therefore, we are overriding the ServletInputStream getter method in the MyCustomRequestWrapper and
    also creating a custom wrapper for ServletInputStream known as ServletInputStreamWrapper which
    converts the byte array to input stream in its constructor and allows the invoker to read the stream using
    read() method.
     */
    MyCustomRequestWrapper myCustomRequestWrapper =
            new MyCustomRequestWrapper((HttpServletRequest) servletRequest);
    ContentCachingResponseWrapper responseWrapper =
            new ContentCachingResponseWrapper((HttpServletResponse) servletResponse);

    printRequestHeaders(myCustomRequestWrapper);
    String requestBody = new BufferedReader(
            new InputStreamReader(myCustomRequestWrapper.getInputStream(), StandardCharsets.UTF_8))
            .lines()
            .map(String::trim)
            .collect(Collectors.joining(""));
    System.out.println("\nREQUEST BODY: "+ requestBody);

    filterChain.doFilter(myCustomRequestWrapper,responseWrapper);

    printResponseHeaders(responseWrapper);
    System.out.println("\nRESPONSE BODY: "
            + getStringValue(responseWrapper.getContentAsByteArray(), responseWrapper.getCharacterEncoding()));

    //copies response body to actual response object
    responseWrapper.copyBodyToResponse();
  }

  private String getStringValue(byte[] contentAsByteArray, String characterEncoding) {
    try {
      return new String(contentAsByteArray, characterEncoding);
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return "";
  }

  public void printRequestHeaders(MyCustomRequestWrapper myCustomRequestWrapper) {
    Enumeration<String> headers = myCustomRequestWrapper.getHeaderNames();
    System.out.print("REQUEST HEADERS: ");
    while(headers.hasMoreElements()) {
      String header = headers.nextElement();
      System.out.print(header+": " +myCustomRequestWrapper.getHeader(header)+" | ");
    }
  }

  public void printResponseHeaders(ContentCachingResponseWrapper contentCachingResponseWrapper) {
    System.out.print("RESPONSE HEADERS: "+ "HTTP Status: "+
            contentCachingResponseWrapper.getStatus()+" | Content-type: "+
            contentCachingResponseWrapper.getContentType()+" | Content-length: "+
            contentCachingResponseWrapper.getContentSize());
  }
}