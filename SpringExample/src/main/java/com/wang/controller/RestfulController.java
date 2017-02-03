package com.wang.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.wang.data.Person;
import com.wang.service.ChatRoomService;
import com.wang.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * @RestController: 结合了@ResponseBody和@Controller
 *
 * 处理JSON，XML 或者自定义的媒体类型
 */
@RestController
@RequestMapping("/rest")
public class RestfulController {

    @Autowired
    private PersonService personService;

    @Autowired
    private ChatRoomService chatRoomService;

    /* 使用 @JsonView 时，类中的get方法中必须使用同样的方法标记 */
    @GetMapping("/person/allperson")
    @JsonView(Person.WithoutPasswordView.class)
    public List<Person> getAllPerson () {
        return personService.getAllPerson();
    }

    @GetMapping("/person/persons/password")
    @JsonView(Person.WithPasswordView.class)
    public List<Person> getAllPersonWithPassword () {
        return personService.getAllPerson();
    }

    @GetMapping("/person/something")
    public ResponseEntity<String> handle(HttpEntity<byte[]> requestEntity) {
        String requestHeader = requestEntity.getHeaders().getFirst("MyRequestHeader");
        byte[] requestBody = requestEntity.getBody();
        System.out.println("requestHeader: " + requestHeader);
        System.out.println("requestBody: " + new String(requestBody));

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("MyResponseHeader", "MyValue");
        return new ResponseEntity<>("Hello World", responseHeaders, HttpStatus.CREATED);
    }

    /* 交互的话使用websocket */
    @RequestMapping("/events/{name}")
    public SseEmitter handle(@PathVariable String name) {
        SseEmitter emitter = new SseEmitter();
        // Save the emitter somewhere..
        chatRoomService.addEmitters(name, emitter);
        return emitter;
    }

    @RequestMapping("/event/push")
    public void pushMessage() throws IOException {
        chatRoomService.sendMessage("message");
    }

//    @RequestMapping("/download")
//    public StreamingResponseBody handleDownload() {
//        return (OutputStream outputStream) -> {
//            // write the file and something to download
//        };
//    }
}
