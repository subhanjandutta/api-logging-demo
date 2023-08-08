package com.subh.loggingdemo.api;

import com.subh.loggingdemo.model.Employee;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HelloController {

  @GetMapping
  public ResponseEntity<Map<String,String>> sayHelloToGuest() {
    Map<String,String> messageResponse = new HashMap<>();
    messageResponse.put("message", "Hello Guest");
    return ResponseEntity.ok(messageResponse);
  }

  @PostMapping
  public ResponseEntity<Map<String,String>> sayHelloToUser(@RequestBody Employee e) {
    Map<String,String> messageResponse = new HashMap<>();
    messageResponse.put("message", "User " + e.getName() + " Created");
    return ResponseEntity.status(HttpStatus.CREATED).body(messageResponse);
  }
}
